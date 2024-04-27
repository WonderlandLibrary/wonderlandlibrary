/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.ui.client

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.features.module.modules.render.ClickGUI
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.minecraft.client.gui.GuiButton
import net.minecraft.client.gui.GuiScreen
import org.lwjgl.input.Keyboard
import org.lwjgl.opengl.GL11
import java.awt.Color

class GuiWelcome : GuiScreen() {

    override fun initGui() {
        this.buttonList.add(GuiButton(1, this.width / 2 - 100, height - 40, "Ok"))
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        drawBackground(0)

        val font = Fonts.font35

        font.drawCenteredString( LiquidBounce.CLIENT_NAME + "B" +  LiquidBounce.CLIENT_VERSION, width / 2F - 1, height / 8F + 70, 0xffffff, true)

        font.drawCenteredString("By CCBlueX Develop By AquaVit", width / 2F - 6, height / 8F + 80F + font.FONT_HEIGHT * 2, 0xffffff, true)
        font.drawCenteredString("本客户端是免费的请勿倒卖以及购买", width / 2F - 13, height / 8F + 80F + font.FONT_HEIGHT * 3, 0xffffff, true)
        font.drawCenteredString("群785835501持续更新 仅供学习勿用与商业", width / 2F - 23, height / 8 + 80F + font.FONT_HEIGHT * 4, 0xffffff, true)
        font.drawCenteredString("最终解释权归AquaVit所有 已经遵循GPLV3协议", width / 2F - 26, height / 8F + 80F + font.FONT_HEIGHT * 5, 0xffffff, true)
        font.drawCenteredString("如果使用过程中发现BUG 请反馈QQ2924270322", width / 2F - 26, height / 8F + 80F + font.FONT_HEIGHT * 6, 0xffffff, true)
        font.drawCenteredString("2020/8/18 UPDATE", width / 2F - 3, height / 8F + 80F + font.FONT_HEIGHT * 7, 0xffffff, true)

        super.drawScreen(mouseX, mouseY, partialTicks)

        // Title
        GL11.glScalef(2F, 2F, 2F)
        Fonts.font40.drawCenteredString("爷懒得写说明", width / 2 / 2F, height / 8F / 2  - 2, Color(0, 140, 255).rgb, true)
    }

    override fun keyTyped(typedChar: Char, keyCode: Int) {
        if (Keyboard.KEY_ESCAPE == keyCode)
            return

        super.keyTyped(typedChar, keyCode)
    }

    override fun actionPerformed(button: GuiButton) {
        if (button.id == 1) {
            mc.displayGuiScreen(GuiMainMenu())
        }
    }
}