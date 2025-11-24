package org.techcult.scaleos.core.presentation.theme

data class AppIcon(
    val name: String,
)

object AppIcons {
    val allIcons = listOf(
        AppIcon("bakery"),
        AppIcon("beverages"),
        AppIcon("coffee"),
        AppIcon("dairy"),
    )


    fun findByName(name: String?): AppIcon? {
        return allIcons.find { it.name == name }
    }
}
