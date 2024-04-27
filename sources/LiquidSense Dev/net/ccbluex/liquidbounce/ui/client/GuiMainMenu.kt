/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.ui.client

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.ui.client.altmanager.GuiAltManager
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.render.RenderUtils.drawArc
import net.ccbluex.liquidbounce.utils.render.RenderUtils.getAnimationState
import net.minecraft.client.gui.*
import net.minecraft.client.resources.I18n
import org.lwjgl.opengl.GL11
import java.awt.Color


class GuiMainMenu : GuiScreen(), GuiYesNoCallback {

    override fun initGui() {
        addbutton()
        super.initGui()
    }
    /*

    var animation = 0.0
    var zoom = 0.0
    var slide = 0
    var sinOffset = 0
    var panoramaTimer = 0

    override fun updateScreen() {
        ++panoramaTimer
    }

    private fun drawPanorama(p_73970_1_: Int, p_73970_2_: Int, p_73970_3_: Float) {
        val tessellator = Tessellator.getInstance()
        val worldrenderer = tessellator.worldRenderer
        GlStateManager.matrixMode(5889)
        GlStateManager.pushMatrix()
        GlStateManager.loadIdentity()
        Project.gluPerspective(120.0f, 1.0f, 0.05f, 10.0f)
        GlStateManager.matrixMode(5888)
        GlStateManager.pushMatrix()
        GlStateManager.loadIdentity()
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)
        GlStateManager.rotate(180.0f, 1.0f, 0.0f, 0.0f)
        GlStateManager.rotate(90.0f, 0.0f, 0.0f, 1.0f)
        GlStateManager.enableBlend()
        GlStateManager.disableAlpha()
        GlStateManager.disableCull()
        GlStateManager.depthMask(false)
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
        val i = 8
        for (j in 0 until i * i) {
            GlStateManager.pushMatrix()
            val f = ((j % i).toFloat() / i.toFloat() - 0.5f) / 64.0f
            val f1 = ((j / i)  / i.toFloat() - 0.5f) / 64.0f
            val f2 = 0.0f
            GlStateManager.translate(f, f1, f2)
            GlStateManager.rotate(MathHelper.sin((this.panoramaTimer + p_73970_3_) / 400.0f) * 25.0f + 20.0f,
                    1.0f, 0.0f, 0.0f)
            GlStateManager.rotate(-(this.panoramaTimer + p_73970_3_) * 0.1f, 0.0f, 1.0f, 0.0f)
            for (k in 0..5) {
                GlStateManager.pushMatrix()
                if (k == 1) {
                    GlStateManager.rotate(90.0f, 0.0f, 1.0f, 0.0f)
                }
                if (k == 2) {
                    GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f)
                }
                if (k == 3) {
                    GlStateManager.rotate(-90.0f, 0.0f, 1.0f, 0.0f)
                }
                if (k == 4) {
                    GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f)
                }
                if (k == 5) {
                    GlStateManager.rotate(-90.0f, 1.0f, 0.0f, 0.0f)
                }
                mc.textureManager.bindTexture(Gui.optionsBackground)
                worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR)
                val l = 255 / (j + 1)
                val f3 = 0.0f
                worldrenderer.pos(-1.0, -1.0, 1.0).tex(0.0, 0.0).color(255, 255, 255, l).endVertex()
                worldrenderer.pos(1.0, -1.0, 1.0).tex(1.0, 0.0).color(255, 255, 255, l).endVertex()
                worldrenderer.pos(1.0, 1.0, 1.0).tex(1.0, 1.0).color(255, 255, 255, l).endVertex()
                worldrenderer.pos(-1.0, 1.0, 1.0).tex(0.0, 1.0).color(255, 255, 255, l).endVertex()
                tessellator.draw()
                GlStateManager.popMatrix()
            }
            GlStateManager.popMatrix()
            GlStateManager.colorMask(true, true, true, false)
        }
        worldrenderer.setTranslation(0.0, 0.0, 0.0)
        GlStateManager.colorMask(true, true, true, true)
        GlStateManager.matrixMode(5889)
        GlStateManager.popMatrix()
        GlStateManager.matrixMode(5888)
        GlStateManager.popMatrix()
        GlStateManager.depthMask(true)
        GlStateManager.enableCull()
        GlStateManager.enableDepth()
    }

    private fun rotateAndBlurSkybox(p_73968_1_: Float) {
        mc.textureManager.bindTexture(Gui.optionsBackground)
        GL11.glTexParameteri(3553, 10241, 9729)
        GL11.glTexParameteri(3553, 10240, 9729)
        GL11.glCopyTexSubImage2D(3553, 0, 0, 0, 0, 0, 256, 256)
        GlStateManager.enableBlend()
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
        GlStateManager.colorMask(true, true, true, false)
        val tessellator = Tessellator.getInstance()
        val worldrenderer = tessellator.worldRenderer
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR)
        GlStateManager.disableAlpha()
        val i = 3
        for (j in 0 until i) {
            val f = 1.0f / (j + 1).toFloat()
            val k = width
            val l = height
            val f1 = (j - i / 2).toFloat() / 256.0f
            worldrenderer.pos(k.toDouble(), l.toDouble(), zLevel.toDouble()).tex(0.0f + f1.toDouble(), 1.0).color(1.0f, 1.0f, 1.0f, f).endVertex()
            worldrenderer.pos(k.toDouble(), 0.0, zLevel.toDouble()).tex(1.0f + f1.toDouble(), 1.0).color(1.0f, 1.0f, 1.0f, f).endVertex()
            worldrenderer.pos(0.0, 0.0, zLevel.toDouble()).tex(1.0f + f1.toDouble(), 0.0).color(1.0f, 1.0f, 1.0f, f).endVertex()
            worldrenderer.pos(0.0, l.toDouble(), zLevel.toDouble()).tex(0.0f + f1.toDouble(), 0.0).color(1.0f, 1.0f, 1.0f, f).endVertex()
        }
        tessellator.draw()
        GlStateManager.enableAlpha()
        GlStateManager.colorMask(true, true, true, true)
    }
    fun renderSkidBackground(mouseX: Int, mouseY: Int, partialTicks: Float) {
        this.mc.getFramebuffer().unbindFramebuffer();
        GlStateManager.viewport(0, 0, 256, 256);
        this.drawPanorama(mouseX, mouseY, partialTicks);
        this.rotateAndBlurSkybox( partialTicks);
        this.rotateAndBlurSkybox( partialTicks);
        this.rotateAndBlurSkybox( partialTicks);
        this.rotateAndBlurSkybox( partialTicks);
        this.rotateAndBlurSkybox( partialTicks);
        this.rotateAndBlurSkybox( partialTicks);
        this.rotateAndBlurSkybox( partialTicks);
        this.mc.getFramebuffer().bindFramebuffer(true);
        if (this.animation < 4.0) {
            this.animation += 0.05000000074505806
        }
        this.zoom = if (this.zoom > 1.0) this.zoom * this.zoom * this.zoom * this.zoom / 350.0.let { this.zoom -= it; this.zoom } else 1.0
        val z = this.zoom
        val ScaledResolution = ScaledResolution(this.mc)
        Gui.drawScaledCustomSizeModalRect(0, 0,
                (-(ScaledResolution.getScaledWidth() / z / 2.0
                        + ScaledResolution.getScaledWidth() / 2.0)).toFloat(),
                (-(ScaledResolution.getScaledHeight() / z) / 2.0
                        + ScaledResolution.getScaledHeight() / 2.0).toFloat(),
                this.width, this.height, (this.width * z).toInt(), (this.height * z).toInt(), this.width.toFloat(),
                height.toFloat())
        val width: Int = ScaledResolution.getScaledWidth()
        var height: Int = ScaledResolution.getScaledHeight()
        height = ((height.toDouble() / (this.animation * this.animation + 1.2)).toInt())
        if (height < 20) {
            height = 20
        }

        var lines = 0
        GlStateManager.disableTexture2D()
        for (ball in Menu.instances) {
            ball.render()
            for (ball2 in Menu.instances) {
                if (lines >= 20 || ball.distance(mouseX.toDouble(), mouseY.toDouble()) >= 60.0
                        || ball2.distance(ball.getX(), ball.getY()) >= 50.0) continue
                ball.drawLineTo(ball2)
                ++lines
            }
        }
        GL11.glPushMatrix()
        GL11.glBegin(7)
        GL11.glColor4f(0.6f, 0.6f, 0.6f, 1.0f)
        GL11.glVertex2d(0.0, height.toDouble() + 0.5)
        GL11.glVertex2d(width.toDouble(), height.toDouble() + 0.5)
        GL11.glVertex2d(width.toDouble(), 0.0)
        GL11.glVertex2d(0.0, 0.0)
        GL11.glEnd()
        GL11.glBegin(7)
        //	GL11.glColor4f(r / 1.5f, g / 1.5f, b / 1.5f, 1.0f);
        GL11.glVertex2d(0.0, height.toDouble())
        GL11.glVertex2d(width.toDouble(), height.toDouble())
        GL11.glVertex2d(width.toDouble(), 0.0)
        GL11.glVertex2d(0.0, 0.0)
        GL11.glEnd()
        GL11.glBegin(7)
        GL11.glColor4f(0.6f, 0.6f, 0.6f, 1.0f)
        GL11.glVertex2d(0.0, ScaledResolution.getScaledHeight().toDouble())
        GL11.glVertex2d(width.toDouble(), ScaledResolution.getScaledHeight().toDouble())
        GL11.glVertex2d(width.toDouble(), (ScaledResolution.getScaledHeight() - height).toDouble() + 4.5)
        GL11.glVertex2d(0.0, (ScaledResolution.getScaledHeight() - height).toDouble() + 4.5)
        GL11.glEnd()
        GL11.glBegin(7)
        //GL11.glColor4f(r, g, b, 1.0f);
        GL11.glVertex2d(0.0, ScaledResolution.getScaledHeight().toDouble())
        GL11.glVertex2d(width.toDouble(), ScaledResolution.getScaledHeight().toDouble())
        GL11.glVertex2d(width.toDouble(), ScaledResolution.getScaledHeight() - height + 5.toDouble())
        GL11.glVertex2d(0.0, ScaledResolution.getScaledHeight() - height + 5.toDouble())
        GL11.glEnd()
        GlStateManager.enableTexture2D()
        GL11.glPopMatrix()
    }
    */


    fun addbutton(){
        this.buttonList.add(GuiButton(1, 0, 0, this.width / 8, 20, I18n.format("menu.singleplayer")))
        this.buttonList.add(GuiButton(2, (this.width / 2 - ((this.width / 8) / 2) - 0) / 2, 0, this.width / 8, 20, I18n.format("menu.multiplayer")))
        this.buttonList.add(GuiButton(100, this.width / 2 - ((this.width / 8) / 2) , 0, this.width / 8, 20, "AltManager"))
        this.buttonList.add(GuiButton(0, (this.width / 2 - ((this.width / 8) / 2)) + ((this.width / 2 - ((this.width / 8) / 2) - 0) / 2), 0,  this.width / 8, 20, I18n.format("menu.options")))
        this.buttonList.add(GuiButton(4, this.width - (this.width / 8), 0, this.width / 8, 20, I18n.format("menu.quit")))

    }

    var animation = 0.0
    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        //this.animation = getAnimationState(this.animation, 100.0, Math.max(10.0, Math.abs(this.animation - 100) * 30.0) * 0.3)
        drawBackground(0)
        /*
        renderSkidBackground(mouseX, mouseY,partialTicks)
         */
        //GL11.glPushMatrix()
        //GL11.glTranslatef((this.width / 2).toFloat(), (this.height / 2).toFloat(), 0.0f)
        //this.animation = RenderUtils.getAnimationState(this.animation, n.toDouble(), Math.max(10.0, Math.abs(this.animation - n) * 30.0) * 0.3)
        //drawArc(1.0f, 1.0f, (this.height / 5).toDouble(), Color.BLUE.rgb, 20, 180.0 + 3.5999999046325684 * animation, 10)
        //GL11.glPopMatrix()

        Gui.drawRect(0, 21, this.width, 22, Color(0, 0, 0).rgb)
        Gui.drawRect(0, this.height - 16, this.width, this.height - 15, Color(0, 0, 0).rgb)
        Gui.drawRect(0, 0, this.width, 22, Integer.MIN_VALUE)
        Gui.drawRect(0, this.height - 16, this.width, this.height, Integer.MIN_VALUE)
        Fonts.font35.drawCenteredString("DevelopMent By AquaVit", this.width / 2F, this.height - 20F + Fonts.font35.FONT_HEIGHT, 0xffffff, true)
        Fonts.fontBold180.drawCenteredString(LiquidBounce.CLIENT_NAME, this.width / 2F, height / 2F - 15, 4673984, true)
        Fonts.font35.drawCenteredString(LiquidBounce.CLIENT_VERSION, this.width / 2F + 148, height / 2F - 15 + Fonts.font35.FONT_HEIGHT, 0xffffff, true)
        super.drawScreen(mouseX, mouseY, partialTicks)
    }

    override fun actionPerformed(button: GuiButton) {
        when (button.id) {
            0 -> mc.displayGuiScreen(GuiOptions(this, mc.gameSettings))
            1 -> mc.displayGuiScreen(GuiSelectWorld(this))
            2 -> mc.displayGuiScreen(GuiMultiplayer(this))
            4 -> mc.shutdown()
            100 -> mc.displayGuiScreen(GuiAltManager(this))
            101 -> mc.displayGuiScreen(GuiServerStatus(this))
            102 -> mc.displayGuiScreen(GuiBackground(this))
            103 -> mc.displayGuiScreen(GuiModsMenu(this))
            108 -> mc.displayGuiScreen(GuiCredits(this))
        }
    }

    override fun keyTyped(typedChar: Char, keyCode: Int) {}
}