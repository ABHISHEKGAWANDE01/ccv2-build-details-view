/*
 * This file is part of "SAP Commerce Developers Toolset" plugin for Intellij IDEA.
 * Copyright (C) 2023 EPAM Systems <hybrisideaplugin@epam.com>
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

package com.intellij.idea.plugin.hybris.facet

import com.intellij.idea.plugin.hybris.project.descriptors.ModuleDescriptorType
import com.intellij.idea.plugin.hybris.project.descriptors.SubModuleDescriptorType

class ExtensionDescriptor(
    var name: String = "",
    var readonly: Boolean = false,
    var useMaven: Boolean = false,
    var type: ModuleDescriptorType = ModuleDescriptorType.NONE,
    var subModuleType: SubModuleDescriptorType? = null,
    var backofficeModule: Boolean = false,
    var hacModule: Boolean = false,
    var webModule: Boolean = false,
    var hmcModule: Boolean = false,
    var coreModule: Boolean = false,
    var deprecated: Boolean = false,
    var extGenTemplateExtension: Boolean = false,
    var addon: Boolean = false,
    var classPathGen: String? = null,
    var moduleGenName: String? = null,
    var installedIntoExtensions: Set<String> = emptySet(),
)