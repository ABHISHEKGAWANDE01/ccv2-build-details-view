/*
 * This file is part of "SAP Commerce Developers Toolset" plugin for Intellij IDEA.
 * Copyright (C) 2019-2023 EPAM Systems <hybrisideaplugin@epam.com> and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.intellij.idea.plugin.hybris.properties.impl

import com.intellij.idea.plugin.hybris.common.HybrisConstants
import com.intellij.idea.plugin.hybris.common.yExtensionName
import com.intellij.idea.plugin.hybris.properties.PropertiesService
import com.intellij.lang.properties.IProperty
import com.intellij.lang.properties.PropertiesFileType
import com.intellij.lang.properties.psi.PropertiesFile
import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager
import com.intellij.psi.search.DelegatingGlobalSearchScope
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.GlobalSearchScope
import java.io.File
import java.util.*
import java.util.regex.Pattern

/*
Improve order of the properties - https://help.sap.com/docs/SAP_COMMERCE/b490bb4e85bc42a7aa09d513d0bcb18e/8b8e13c9866910149d40b151a9196543.html?locale=en-US
 */
class PropertiesServiceImpl(val project: Project) : PropertiesService {

    private val nestedPropertyPrefix = "\${"
    private val nestedPropertySuffix = "}"
    private val optionalPropertiesFilePattern = Pattern.compile("([1-9]\\d)-(\\w*)\\.properties")

    override fun getLanguages(): Set<String> {
        val languages = findMacroProperty(HybrisConstants.PROPERTY_LANG_PACKS)
            ?.value
            ?.split(",")
            ?.map { it.trim() }
            ?: emptyList()

        val uniqueLanguages = languages.toMutableSet()
        uniqueLanguages.add(HybrisConstants.DEFAULT_LANGUAGE_ISOCODE)

        return uniqueLanguages
            .map { it.lowercase() }
            .toSet()
    }

    override fun containsLanguage(language: String, supportedLanguages: Set<String>) = supportedLanguages
        .contains(language.lowercase())

    override fun findAutoCompleteProperties(query: String): List<IProperty> = findAllProperties()
        .filter { it.key != null && it.key!!.contains(query) || query.isBlank() }

    override fun getProperty(key: String): IProperty? = findAllProperties()
        .firstOrNull { it.key != null && it.key == key }

    override fun findMacroProperty(query: String): IProperty? {
        val allProps = findAllProperties()
        if (allProps.isEmpty()) {
            return null;
        }
        val filteredProps = allProps.filter { it.key != null && query.contains(it.key!!) || query.isBlank() }
        if (filteredProps.isEmpty()) {
            return null;
        }

        return filteredProps.reduce { one, two -> if (one.key!!.length > two.key!!.length) one else two }
    }

    override fun findProperty(query: String): String? = findMacroProperty(query)
        ?.value
        ?.replace("\\", "")

    private fun resolvePropertyValue(value: String?): String {
        return resolvePropertyValue(value, HashMap())
    }

    private fun resolvePropertyValue(value: String?, resolvedProperties: MutableMap<String, String>): String {
        if (value == null) {
            return ""
        }
        var index = 0
        val sb = StringBuilder()
        while (index != -1) {
            val startIndex = value.indexOf(nestedPropertyPrefix, index)
            val endIndex = value.indexOf(nestedPropertySuffix, startIndex)
            if (startIndex != -1 && endIndex != -1) {
                sb.append(value, index, startIndex)
                val propertyKey = value.substring(startIndex + nestedPropertyPrefix.length, endIndex)
                var resolvedValue: String? = resolvedProperties[propertyKey]
                if (resolvedValue != null) {
                    sb.append(resolvedValue)
                } else {
                    val property = findMacroProperty(propertyKey)
                    if (property != null) {
                        resolvedValue = resolvePropertyValue(property.value)
                        sb.append(resolvedValue)
                        resolvedProperties[propertyKey] = resolvedValue
                    } else {
                        sb.append(nestedPropertyPrefix).append(propertyKey).append(nestedPropertySuffix)
                    }
                }
                index = endIndex + 1
            } else {
                sb.append(value, index, value.length)
                index = startIndex
            }
        }
        return sb.toString()
    }

   private fun findAllProperties(): List<IProperty> {
        val result = LinkedHashMap<String, IProperty>()
        val configModule = obtainConfigModule() ?: return emptyList()
        val platformModule = obtainPlatformModule() ?: return emptyList()
        val scope = createSearchScope(configModule, platformModule)
        var envPropsFile: PropertiesFile? = null
        var advancedPropsFile: PropertiesFile? = null
        var localPropsFile: PropertiesFile? = null

        FileTypeIndex.getFiles(PropertiesFileType.INSTANCE, scope)
            .mapNotNull { PsiManager.getInstance(project).findFile(it) }
            .mapNotNull { it as? PropertiesFile }
            .forEach { file ->
                when (file.name) {
                    "env.properties" -> envPropsFile = file
                    "advanced.properties" -> advancedPropsFile = file
                    "local.properties" -> localPropsFile = file
                    else -> {
                        for (property in file.properties) {
                            if (property.key != null) {
                                result[property.key!!] = property
                            }
                        }
                    }
                }
            }
        addPropertyFile(result, envPropsFile)
        addPropertyFile(result, advancedPropsFile)
        addPropertyFile(result, localPropsFile)

        val optDir = result[HybrisConstants.PROPERTY_OPTIONAL_CONFIG_DIR]
        addOptionalConfiguration(result, optDir)

        return ArrayList(result.values)
    }



    private fun addOptionalConfiguration(result: LinkedHashMap<String, IProperty>, optDir: IProperty?) {
        if (optDir == null) {
            return
        }
        val dir = File(optDir.value)
        if (!dir.isDirectory) {
            return
        }
        val matchedFiles = dir.listFiles { _, name -> optionalPropertiesFilePattern.matcher(name).matches() }
            ?: return
        val propertyFiles = TreeMap<String, File>()
        Arrays.stream(matchedFiles).forEach { file -> propertyFiles[file.name] = file }

        propertyFiles.values.forEach { file ->
            val virtualFile = LocalFileSystem.getInstance().findFileByIoFile(file)
            if (virtualFile == null || !virtualFile.exists()) {
                return
            }
            val psiFile = PsiManager.getInstance(project).findFile(virtualFile)
            if (psiFile is PropertiesFile) {
                addPropertyFile(result, psiFile as PropertiesFile?)
            }
        }
    }

    private fun addPropertyFile(result: LinkedHashMap<String, IProperty>, propertiesFile: PropertiesFile?) {
        if (propertiesFile == null) {
            return
        }
        for (property in propertiesFile.properties) {
            if (property.key != null) {
                result[property.key!!] = property
            }
        }
    }

    private fun createSearchScope(configModule: Module, platformModule: Module): GlobalSearchScope {
        val projectPropertiesScope = GlobalSearchScope.getScopeRestrictedByFileTypes(GlobalSearchScope.everythingScope(project), PropertiesFileType.INSTANCE)
            .filter { it.name == "project.properties" }
        val envPropertiesScope = platformModule.moduleContentScope.filter { it.name == "env.properties" }
        val advancedPropertiesScope = platformModule.moduleContentScope.filter { it.name == "advanced.properties" }
        val localPropertiesScope = configModule.moduleContentScope.filter { it.name == "local.properties" }

        return projectPropertiesScope.or(envPropertiesScope.or(advancedPropertiesScope.or(localPropertiesScope)))
    }

    private fun obtainConfigModule() = ModuleManager.getInstance(project)
        .modules
        .firstOrNull { it.yExtensionName() == HybrisConstants.EXTENSION_NAME_CONFIG }

    private fun obtainPlatformModule() = ModuleManager.getInstance(project)
        .modules
        .firstOrNull { it.yExtensionName() == HybrisConstants.EXTENSION_NAME_PLATFORM }

    fun GlobalSearchScope.filter(filter: (VirtualFile) -> Boolean) = object : DelegatingGlobalSearchScope(this) {
        override fun contains(file: VirtualFile): Boolean {
            return filter(file) && super.contains(file)
        }
    }

    fun GlobalSearchScope.or(otherScope: GlobalSearchScope): GlobalSearchScope = union(otherScope)

}