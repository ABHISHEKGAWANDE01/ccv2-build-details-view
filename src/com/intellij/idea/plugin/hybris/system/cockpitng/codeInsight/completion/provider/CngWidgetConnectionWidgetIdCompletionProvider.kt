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
package com.intellij.idea.plugin.hybris.system.cockpitng.codeInsight.completion.provider

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.idea.plugin.hybris.common.HybrisConstants
import com.intellij.idea.plugin.hybris.system.cockpitng.codeInsight.lookup.CngLookupElementFactory
import com.intellij.idea.plugin.hybris.system.cockpitng.meta.CngMetaModelAccess
import com.intellij.openapi.application.ApplicationManager
import com.intellij.util.ProcessingContext

open class CngWidgetConnectionWidgetIdCompletionProvider : CompletionProvider<CompletionParameters>() {

    public override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        result: CompletionResultSet
    ) {
        val project = parameters.editor.project ?: return

        val resultCaseInsensitive = result.caseInsensitive()

        with(CngMetaModelAccess.getInstance(project).getMetaModel()) {
            widgets
                .values
                .map { CngLookupElementFactory.build(it) }
                .forEach { resultCaseInsensitive.addElement(it) }

            editorDefinitions
                .values
                .map { CngLookupElementFactory.build(it, HybrisConstants.COCKPIT_NG_WIDGET_ID_STUB + it.id) }
                .forEach { resultCaseInsensitive.addElement(it) }

            widgetDefinitions
                .values
                .map { CngLookupElementFactory.build(it, HybrisConstants.COCKPIT_NG_WIDGET_ID_STUB + it.id) }
                .forEach { resultCaseInsensitive.addElement(it) }
        }
    }

    companion object {
        val instance: CompletionProvider<CompletionParameters> =
            ApplicationManager.getApplication().getService(CngWidgetConnectionWidgetIdCompletionProvider::class.java)
    }
}