/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.ui.client

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.ui.client.altmanager.GuiAltManager
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.minecraft.client.gui.*
import net.minecraft.client.resources.I18n
import net.minecraft.util.ResourceLocation
import org.lwjgl.input.Mouse
import java.awt.Color

class GuiMainMenu : GuiScreen() {

    private var liquidShadowIcon = ResourceLocation("liquidshadow/logo_large.png")

    private var altManagerButton = ResourceLocation("liquidshadow/buttons/altmanager.png")
    private var multiplayerButton = ResourceLocation("liquidshadow/buttons/multi.png")
    private var quitButton = ResourceLocation("liquidshadow/buttons/quit.png")
    private var settingsButton = ResourceLocation("liquidshadow/buttons/settings.png")
    private var singleButton = ResourceLocation("liquidshadow/buttons/single.png")
    private var customizeButton = ResourceLocation("liquidshadow/buttons/customize.png")

    override fun initGui() {

    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        drawBackground(0)

        RenderUtils.drawImage(liquidShadowIcon,(width / 2) - 119,height / 4,250,70)

        RenderUtils.drawRect(width / 2 - 80,(height / 3) * 2 + 28,width / 2 + 80,height / 3 * 2 + 53,Color(255,255,255,45).rgb)

        RenderUtils.drawCircle(width / 2F - 12, ((height / 3F) * 2F) + 40F, 9F, 1, 400, 65, 215, 255, 175)
        RenderUtils.drawCircle(width / 2F + 12, ((height / 3F) * 2F) + 40F, 9F, 1, 400, 65, 215, 255, 175)
        RenderUtils.drawCircle(width / 2F - 36, ((height / 3F) * 2F) + 40F, 9F, 1, 400, 65, 215, 255, 175)
        RenderUtils.drawCircle(width / 2F - 60, ((height / 3F) * 2F) + 40F, 9F, 1, 400, 65, 215, 255, 175)
        RenderUtils.drawCircle(width / 2F + 36, ((height / 3F) * 2F) + 40F, 9F, 1, 400, 65, 215, 255, 175)
        RenderUtils.drawCircle(width / 2F + 60, ((height / 3F) * 2F) + 40F, 9F, 1, 400, 255,80,80, 175)

        if (mouseY > ((height / 3F) * 2F) + 40F - 9 &&
            mouseY < ((height / 3F) * 2F) + 40F + 9) {
            if (
                mouseX > width / 2F - 12 - 10 &&
                mouseX < width / 2F - 12 + 18 - 8
            ) {
                RenderUtils.drawFilledCircle(width / 2 - 12, ((height / 3) * 2) + 41, 9F, Color(65,215,255,175))
                Fonts.font35.drawCenteredString(I18n.format("menu.multiplayer"),width / 2F,((height / 3F) * 2F) + 55F,Color.WHITE.rgb)
                if (Mouse.isButtonDown(0)) {
                    mc.displayGuiScreen(GuiMultiplayer(this))
                }
            } else if (
                mouseX > width / 2F + 12 - 10 &&
                mouseX < width / 2F + 12 + 18 - 8
                    ) {
                RenderUtils.drawFilledCircle(width / 2 + 12, ((height / 3) * 2) + 41, 9F, Color(65,215,255,175))
                Fonts.font35.drawCenteredString(I18n.format("menu.singleplayer"),width / 2F,((height / 3F) * 2F) + 55F,Color.WHITE.rgb)
                if (Mouse.isButtonDown(0)) {
                    mc.displayGuiScreen(GuiSelectWorld(this))
                }
            } else if (
                mouseX > width / 2F - 36 - 10 &&
                        mouseX < width / 2F - 36 + 18 - 8
            ) {
                RenderUtils.drawFilledCircle(width / 2 - 36, ((height / 3) * 2) + 41, 9F, Color(65,215,255,175))
                Fonts.font35.drawCenteredString("AltManager",width / 2F,((height / 3F) * 2F) + 55F,Color.WHITE.rgb)
                if (Mouse.isButtonDown(0)) {
                    mc.displayGuiScreen(GuiAltManager(this))
                }
            } else if (
                mouseX > width / 2F - 60 - 10 &&
                        mouseX < width / 2F - 60 + 18 - 8
            ) {
                RenderUtils.drawFilledCircle(width / 2 - 60, ((height / 3) * 2) + 41, 9F, Color(65,215,255,175))
                Fonts.font35.drawCenteredString("Customize",width / 2F,((height / 3F) * 2F) + 55F,Color.WHITE.rgb)
                if (Mouse.isButtonDown(0)) {
                    mc.displayGuiScreen(GuiCustomize(this))
                }
            } else if (
                mouseX > width / 2F + 36 - 10 &&
                        mouseX < width / 2F + 36 + 18 - 8
            ) {
                RenderUtils.drawFilledCircle(width / 2 + 36, ((height / 3) * 2) + 41, 9F, Color(65,215,255,175))
                Fonts.font35.drawCenteredString(I18n.format("menu.options"),width / 2F,((height / 3F) * 2F) + 55F,Color.WHITE.rgb)
                if (Mouse.isButtonDown(0)) {
                    mc.displayGuiScreen(GuiOptions(this,mc.gameSettings))
                }
            } else if (
                mouseX > width / 2F + 60 - 10 &&
                        mouseX < width / 2F + 60 + 18 - 8
            ) {
                Fonts.font35.drawCenteredString(I18n.format("menu.quit"),width / 2F,((height / 3F) * 2F) + 55F,Color.WHITE.rgb)
                RenderUtils.drawFilledCircle(width / 2 + 60, ((height / 3) * 2) + 41, 9F, Color(255,80,80,175))
                if (Mouse.isButtonDown(0)) {
                    mc.shutdown()
                }
            }
        }


        RenderUtils.drawImage(multiplayerButton,width / 2 - 18,((height / 3) * 2 - 5)  + 40,12,12)
        RenderUtils.drawImage(singleButton,width / 2 + 6,((height / 3) * 2 - 5)  + 40,12,12)
        RenderUtils.drawImage(settingsButton,width / 2 + 30,((height / 3) * 2 - 5)  + 40,12,12)
        RenderUtils.drawImage(altManagerButton,width / 2 - 42,((height / 3) * 2 - 5)  + 40,12,12)
        RenderUtils.drawImage(customizeButton,width / 2 - 66,((height / 3) * 2 - 5)  + 40,12,12)
        RenderUtils.drawImage(quitButton,width / 2 + 55,((height / 3) * 2 - 5)  + 40,12,12)

        Fonts.font35.drawCenteredString(LiquidBounce.CLIENT_VERSION, (width / 2F) + 100, height / 4F + 10 + Fonts.font35.fontHeight, 0xffffff, true)

        val updateLog = LiquidBounce.UPDATE_LOG

        var updateHeight = 8
        if (LiquidBounce.IN_DEV) {
            Fonts.font35.drawString("THIS IS THE BETA VERSION!",8,8,Color(255,80,80).rgb)
            Fonts.font35.drawString("if there's any bug please create a issue in github issues.",8,8 + Fonts.font35.FONT_HEIGHT + 2,Color(255,80,80).rgb)
            Fonts.font35.drawString("https://github.com/woodteam/LiquidShadow/issues",8,8 + (Fonts.font35.FONT_HEIGHT + 2) * 2,Color(255,80,80).rgb)
            updateHeight = 8 + (Fonts.font35.FONT_HEIGHT + 2) * 3
        }
        Fonts.font35.drawString("Whats new of version " + LiquidBounce.CLIENT_VERSION,8,updateHeight,Color(255,225,25).rgb)
        updateHeight += Fonts.font35.FONT_HEIGHT + 2
        for (update in updateLog) {
            Fonts.font35.drawString(update,8,updateHeight,Color.WHITE.rgb)
            updateHeight += Fonts.font35.FONT_HEIGHT + 2
        }

        Fonts.font35.drawString(LiquidBounce.CLIENT_NAME + " " + LiquidBounce.CLIENT_VERSION,width - 8 - Fonts.font35.getStringWidth(LiquidBounce.CLIENT_NAME + " " + LiquidBounce.CLIENT_VERSION),8,Color(65,215,255).rgb)
        Fonts.font35.drawString("by " + LiquidBounce.CLIENT_CREATOR,width - 8 - Fonts.font35.getStringWidth("by " + LiquidBounce.CLIENT_CREATOR),8 + Fonts.font35.FONT_HEIGHT + 2,Color(65,215,255).rgb)

        super.drawScreen(mouseX, mouseY, partialTicks)
    }

    override fun actionPerformed(button: GuiButton) {
        when (button.id) {
            0 -> mc.displayGuiScreen(GuiOptions(this, mc.gameSettings))
            1 -> mc.displayGuiScreen(GuiSelectWorld(this))
            2 -> mc.displayGuiScreen(GuiMultiplayer(this))
            4 -> mc.shutdown()
            100 -> mc.displayGuiScreen(GuiAltManager(this))
            102 -> mc.displayGuiScreen(GuiBackground(this))
            103 -> mc.displayGuiScreen(GuiCustomize(this))
        }
    }
}