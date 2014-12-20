package org.psliwa.idea.composer.idea

import javax.swing.Icon

import com.intellij.codeInsight.completion.{InsertionContext, InsertHandler}
import com.intellij.codeInsight.lookup.{LookupElementPresentation, LookupElement}
import com.intellij.openapi.fileTypes.FileTypeManager
import com.intellij.openapi.util.Iconable
import com.intellij.psi.{PsiFile, PsiDirectory}

protected[idea] case class BaseLookupElement (
  name: String,
  icon: Option[Icon] = None,
  quoted: Boolean = true,
  insertHandler: Option[InsertHandler[LookupElement]] = None
) extends LookupElement {

  private val presentation = new LookupElementPresentation
  presentation.setIcon(icon.orNull)
  presentation.setItemText(name)

  override def getLookupString = name
  override def renderElement(presentation: LookupElementPresentation): Unit = presentation.copyFrom(this.presentation)
  override def handleInsert(context: InsertionContext): Unit = insertHandler.foreach(_.handleInsert(context, this))

  def withInsertHandler(insertHandler: InsertHandler[LookupElement]) = {
    new BaseLookupElement(name, icon, quoted, Some(insertHandler))
  }
}

protected[idea] object BaseLookupElement {
  def apply(d: PsiDirectory) = new BaseLookupElement(d.getName+"/", Option(d.getIcon(Iconable.ICON_FLAG_VISIBILITY)))
  def apply(f: PsiFile) = new BaseLookupElement(f.getName, Option[Icon](FileTypeManager.getInstance().getFileTypeByFileName(f.getName).getIcon))
}