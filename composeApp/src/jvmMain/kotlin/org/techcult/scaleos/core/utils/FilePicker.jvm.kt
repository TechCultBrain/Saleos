package org.techcult.scaleos.core.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import java.io.File
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter

@Composable
actual fun FilePicker(onFilePicked: (String?) -> Unit) {
    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    var filePath by remember { mutableStateOf<String?>(null) }

    val chooser = JFileChooser().apply {
        fileSelectionMode = JFileChooser.FILES_ONLY
        fileFilter = FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png", "bmp", "gif")
    }
    val result = chooser.showOpenDialog(null)
    if (result == JFileChooser.APPROVE_OPTION) {
        val file: File = chooser.selectedFile
        filePath = file.absolutePath
        val uri = file.toURI()

        onFilePicked("file://${uri}")
    }else if (result == JFileChooser.CANCEL_OPTION)
    {
        onFilePicked(null)

    }
}