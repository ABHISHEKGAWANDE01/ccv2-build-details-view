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

// Generated on Mon Jan 09 16:31:21 CET 2023
// DTD/Schema  :    null
package com.intellij.idea.plugin.hybris.system.extensioninfo.model;

import com.intellij.util.xml.*;
import com.intellij.util.xml.DomElement;
import org.jetbrains.annotations.NotNull;

/**
 * null:metaType interface.
 * <pre>
 * <h3>Type null:metaType documentation</h3>
 * Configures metadata.
 * </pre>
 */
public interface Meta extends DomElement {

	/**
	 * Returns the value of the key child.
	 * <pre>
	 * <h3>Attribute null:key documentation</h3>
	 * Metadata key.
	 * </pre>
	 * @return the value of the key child.
	 */
	@NotNull
	@Attribute ("key")
	@Required
	GenericAttributeValue<String> getKey();


	/**
	 * Returns the value of the value child.
	 * <pre>
	 * <h3>Attribute null:value documentation</h3>
	 * Metadata value.
	 * </pre>
	 * @return the value of the value child.
	 */
	@NotNull
	@Attribute ("value")
	@Required
	GenericAttributeValue<String> getValue();


}
