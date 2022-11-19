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
package com.intellij.idea.plugin.hybris.vfs.listeners

import com.intellij.idea.plugin.hybris.common.HybrisConstants
import com.intellij.idea.plugin.hybris.type.system.meta.TSMetaModelAccess
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.DumbService
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.vfs.newvfs.BulkFileListener
import com.intellij.openapi.vfs.newvfs.events.VFileEvent
import com.intellij.openapi.wm.WindowManager

class TSBulkFileListener : BulkFileListener {

    private val logger = Logger.getInstance(TSBulkFileListener::class.java)

    init {
    }

    override fun after(events: MutableList<out VFileEvent>) {
        val project = getActiveProject() ?: return

        if (DumbService.isDumb(project)) return

        val fileIndex = ProjectRootManager.getInstance(project).fileIndex

        val items = events
            .filter { it.path.endsWith(HybrisConstants.HYBRIS_ITEMS_XML_FILE_ENDING) }
            .mapNotNull { it.file }
            .filter { fileIndex.isInContent(it) }

        if (items.isNotEmpty()) {
            logger.debug("Re-triggering GlobalMetaModel re-calculation due following items.xml changes: ${items.map { it.nameWithoutExtension }}")
            // re-triggering GlobalMetaModel state on file changes
            TSMetaModelAccess.getInstance(project).getMetaModel()
        }
    }

    private fun getActiveProject(): Project? {
        val windowManager = WindowManager.getInstance()

        return ProjectManager.getInstance().openProjects
            .lastOrNull { windowManager.suggestParentWindow(it)?.isActive ?: false }
    }

}