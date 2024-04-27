/*
 * GlassClient PVP Client
 * A free open-source mixin-based PVP client based on liquidbounce with all cheats removed.
 * https://github.com/GlassClient/GlassClient
 */
package net.ccbluex.liquidbounce.features.module.modules.client.button

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.features.module.modules.client.HUD
import net.ccbluex.liquidbounce.font.FontLoaders
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiButton
import java.awt.Color

abstract class AbstractButtonRenderer(protected val button: GuiButton) {

    abstract fun render(mouseX: Int, mouseY: Int, mc: Minecraft)

    open fun drawButtonText(mc: Minecraft) {
val colour = if (HUD.buttonValue.get() == "Wolfram"){ if (button.enabled) -263693982 else Color(30, 30, 60, 100).rgb } else {if (button.enabled) Color.WHITE.rgb else Color.GRAY.rgb }

        FontLoaders.F18.DisplayFonts(
            button.displayString,
            button.xPosition + button.width / 2f - FontLoaders.F18.DisplayFontWidths(FontLoaders.F18,button.displayString) / 2f,
            button.yPosition + button.height / 2f - FontLoaders.F18.height / 2f,
            colour,
            FontLoaders.F18
        )
    }
}