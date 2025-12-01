package org.techcult.scaleos.core.utils

fun codeCreator(text: String,count: Int): String
{
   return text.substring(0, 3)
        .uppercase()  + (count + 1).toString().padStart(3, '0')
}