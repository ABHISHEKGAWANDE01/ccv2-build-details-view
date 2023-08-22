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
package com.intellij.idea.plugin.hybris.impex.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.intellij.idea.plugin.hybris.impex.psi.ImpexTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.idea.plugin.hybris.impex.psi.*;

public class ImpexUserRightsValueLineImpl extends ASTWrapperPsiElement implements ImpexUserRightsValueLine {

  public ImpexUserRightsValueLineImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull ImpexVisitor visitor) {
    visitor.visitUserRightsValueLine(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof ImpexVisitor) accept((ImpexVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public ImpexUserRightsFirstValueGroup getUserRightsFirstValueGroup() {
    return findChildByClass(ImpexUserRightsFirstValueGroup.class);
  }

  @Override
  @NotNull
  public List<ImpexUserRightsValueGroup> getUserRightsValueGroupList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, ImpexUserRightsValueGroup.class);
  }

  @Override
  @Nullable
  public ImpexUserRightsValueGroup getValueGroup(int index) {
    return ImpexPsiUtil.getValueGroup(this, index);
  }

  @Override
  @Nullable
  public ImpexUserRightsHeaderLine getHeaderLine() {
    return ImpexPsiUtil.getHeaderLine(this);
  }

}