/*
 * This file is part of "SAP Commerce Developers Toolset" plugin for Intellij IDEA.
 * Copyright (C) 2019 EPAM Systems <hybrisideaplugin@epam.com>
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

package com.intellij.idea.plugin.hybris.toolwindow.system.bean.components

import com.intellij.idea.plugin.hybris.system.bean.meta.model.BSGlobalMetaBean
import com.intellij.idea.plugin.hybris.system.bean.meta.model.BSMetaProperty
import com.intellij.idea.plugin.hybris.psi.utils.PsiUtils
import com.intellij.idea.plugin.hybris.toolwindow.components.AbstractTable
import com.intellij.openapi.project.Project
import com.intellij.util.ui.ListTableModel

private const val COLUMN_CUSTOM = "C"
private const val COLUMN_DEPRECATED = "D"
private const val COLUMN_EQUALS = "E"
private const val COLUMN_MODULE = "Module"
private const val COLUMN_NAME = "Name"
private const val COLUMN_TYPE = "Type"

class BSMetaPropertiesTable private constructor(myProject: Project) :
    AbstractTable<BSGlobalMetaBean, BSMetaProperty>(myProject) {

    override fun getSearchableColumnNames() = listOf(COLUMN_NAME)
    override fun getFixedWidthColumnNames() = listOf(COLUMN_CUSTOM, COLUMN_DEPRECATED, COLUMN_EQUALS)
    override fun select(meta: BSMetaProperty) = selectRowWithValue(meta.name, COLUMN_NAME)
    override fun getItems(meta: BSGlobalMetaBean) = meta.properties.values.sortedWith(
        compareBy(
            { !it.isCustom },
            { it.module.name },
            { it.name })
    )

    override fun createModel(): ListTableModel<BSMetaProperty> = with(ListTableModel<BSMetaProperty>()) {
        columnInfos = arrayOf(
            createColumn(
                name = COLUMN_CUSTOM,
                valueProvider = { attr -> attr.isCustom },
                columnClass = Boolean::class.java,
                tooltip = "Custom"
            ),
            createColumn(
                name = COLUMN_DEPRECATED,
                valueProvider = { attr -> attr.isDeprecated },
                columnClass = Boolean::class.java,
                tooltip = "Deprecated"
            ),
            createColumn(
                name = COLUMN_EQUALS,
                valueProvider = { attr -> attr.isEquals },
                columnClass = Boolean::class.java,
                tooltip = "Equals"
            ),
            createColumn(
                name = COLUMN_MODULE,
                valueProvider = { attr -> PsiUtils.getModuleName(attr.module) }
            ),
            createColumn(
                name = COLUMN_NAME,
                valueProvider = { attr -> attr.name }
            ),
            createColumn(
                name = COLUMN_TYPE,
                valueProvider = { attr -> attr.type }
            )
        )

        this
    }

    companion object {
        private const val serialVersionUID: Long = 6752572661238637911L

        fun getInstance(project: Project): BSMetaPropertiesTable = with(BSMetaPropertiesTable(project)) {
            init()

            this
        }
    }

}