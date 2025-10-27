package org.techcult.scaleos.core.utils

import androidx.window.core.layout.WindowSizeClass

enum class DeviceConfiguration {
    MOBILE_PORTRAIT,
    MOBILE_LANDSCAPE,
    TABLET_PORTRAIT,
    TABLET_LANDSCAPE,
    DESKTOP;

    companion object {
        fun fromWindowSizeClass(windowSizeClass: WindowSizeClass): DeviceConfiguration {

            return if (windowSizeClass.isWidthAtLeastBreakpoint(840) && windowSizeClass.isHeightAtLeastBreakpoint(
                    900
                )
            ) {
                DESKTOP

            } else if (windowSizeClass.isWidthAtLeastBreakpoint(600) && windowSizeClass.isHeightAtLeastBreakpoint(
                    900
                )
            ) {
                TABLET_PORTRAIT
            } else if (windowSizeClass.isWidthAtLeastBreakpoint(840) && windowSizeClass.isHeightAtLeastBreakpoint(
                    480
                )
            ) {
                TABLET_LANDSCAPE
            } else if (windowSizeClass.isWidthAtLeastBreakpoint(840)
            ) {

                MOBILE_LANDSCAPE

            } else if (windowSizeClass.isHeightAtLeastBreakpoint(900)
            ) {
                MOBILE_PORTRAIT
            } else {
                MOBILE_PORTRAIT
            }


        }

    }
}
