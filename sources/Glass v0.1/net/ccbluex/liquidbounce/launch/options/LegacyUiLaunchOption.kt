package net.ccbluex.liquidbounce.launch.options

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.launch.EnumLaunchFilter
import net.ccbluex.liquidbounce.launch.LaunchFilterInfo
import net.ccbluex.liquidbounce.launch.LaunchOption
import net.ccbluex.liquidbounce.launch.data.modernui.ClickGUIModule
import net.ccbluex.liquidbounce.launch.data.modernui.GuiMainMenu
import java.io.File

@LaunchFilterInfo([EnumLaunchFilter.MODERN_UI])
object modernuiLaunchOption : LaunchOption() {

    override fun start() {
        LiquidBounce.mainMenu = GuiMainMenu()
        LiquidBounce.moduleManager.registerModule(ClickGUIModule())
    }
}