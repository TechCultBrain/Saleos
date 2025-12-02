package org.techcult.scaleos.core.utils

import androidx.compose.ui.text.AnnotatedString
    import androidx.compose.ui.text.input.OffsetMapping
    import androidx.compose.ui.text.input.TransformedText
    import androidx.compose.ui.text.input.VisualTransformation


    class CurrencyInputVisualTransformation() : VisualTransformation {
        override fun filter(text: AnnotatedString): TransformedText {
            val originalText = text.text
            if (originalText.isEmpty()) {
                return TransformedText(text, OffsetMapping.Identity)
            }

            // Remove non-digit characters for parsing
            val cleanText = originalText.filter { it.isDigit() }

           val formattedText = formatCurrency(cleanText)


            val offsetMapping = object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int {
                    // This mapping needs to be carefully implemented to handle cursor position correctly
                    // based on the formatting applied (e.g., thousands separators, decimal point).
                    // A simple approach is to find the closest digit in the transformed string.
                    var transformedOffset = 0
                    var originalIndex = 0
                    for (i in 0 until formattedText.length) {
                        if (originalIndex == offset) break
                        if (formattedText[i].isDigit()) {
                            originalIndex++
                        }
                        transformedOffset++
                    }
                    return transformedOffset
                }

                override fun transformedToOriginal(offset: Int): Int {
                    var originalOffset = 0
                    var transformedIndex = 0
                    for (i in 0 until originalText.length) {
                        if (transformedIndex == offset) break
                        if (formattedText.getOrNull(transformedIndex)?.isDigit() == true) {
                            originalOffset++
                        }
                        transformedIndex++
                    }
                    return originalOffset
                }
            }

            return TransformedText(AnnotatedString(formattedText), offsetMapping)
        }
    }