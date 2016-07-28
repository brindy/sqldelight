package com.squareup.sqldelight.intellij.lang.formatter.util

import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiWhiteSpace

internal fun ASTNode.siblingSemicolon(): Int? =
  if (text == ";") textRange.endOffset
  else if (treeNext == null) null
  else treeNext.siblingSemicolon()

internal fun List<ASTNode>.textRange() = dropWhile { it is PsiWhiteSpace }
    .dropLastWhile { it is PsiWhiteSpace }
    .fold(null as TextRange?, { textRange, node ->
      if (textRange == null) {
        node.textRange
      } else {
        TextRange(
            Math.min(textRange.startOffset, node.startOffset),
            Math.max(textRange.endOffset, node.textRange.endOffset)
        )
      }
    })

internal fun ASTNode.rangeToEnd(): TextRange =
    TextRange(startOffset, treeNext?.rangeToEnd()?.endOffset ?: textRange.endOffset)
