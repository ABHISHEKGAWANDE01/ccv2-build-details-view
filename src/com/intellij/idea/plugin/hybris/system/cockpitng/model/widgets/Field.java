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

// Generated on Wed Jan 18 00:35:36 CET 2023
// DTD/Schema  :    http://www.hybris.com/cockpitng/config/fulltextsearch

package com.intellij.idea.plugin.hybris.system.cockpitng.model.widgets;

import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;
import com.intellij.util.xml.Required;
import com.intellij.util.xml.SubTagList;
import org.jetbrains.annotations.NotNull;

/**
 * http://www.hybris.com/cockpitng/config/fulltextsearch:FieldType interface.
 */
public interface Field extends DomElement, Positioned {

    /**
     * Returns the value of the name child.
     *
     * @return the value of the name child.
     */
    @NotNull
    @com.intellij.util.xml.Attribute("name")
    @Required
    GenericAttributeValue<String> getName();


    /**
     * Returns the value of the displayName child.
     *
     * @return the value of the displayName child.
     */
    @NotNull
    @com.intellij.util.xml.Attribute("displayName")
    GenericAttributeValue<String> getDisplayName();


    /**
     * Returns the value of the selected child.
     *
     * @return the value of the selected child.
     */
    @NotNull
    @com.intellij.util.xml.Attribute("selected")
    GenericAttributeValue<Boolean> getSelected();


    /**
     * Returns the value of the label child.
     *
     * @return the value of the label child.
     */
    @NotNull
    @com.intellij.util.xml.Attribute("label")
    GenericAttributeValue<String> getLabel();


    /**
     * Returns the value of the editor child.
     *
     * @return the value of the editor child.
     */
    @NotNull
    @com.intellij.util.xml.Attribute("editor")
    GenericAttributeValue<String> getEditor();


    /**
     * Returns the value of the merge-mode child.
     *
     * @return the value of the merge-mode child.
     */
    @NotNull
    @com.intellij.util.xml.Attribute("merge-mode")
    GenericAttributeValue<String> getMergeMode();


    /**
     * Returns the list of editor-parameter children.
     *
     * @return the list of editor-parameter children.
     */
    @NotNull
    @SubTagList("editor-parameter")
    java.util.List<Parameter> getEditorParameters();

    /**
     * Adds new child to the list of editor-parameter children.
     *
     * @return created child
     */
    @SubTagList("editor-parameter")
    Parameter addEditorParameter();


}