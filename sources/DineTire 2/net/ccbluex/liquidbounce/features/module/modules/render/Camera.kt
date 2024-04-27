package net.ccbluex.liquidbounce.features.module.modules.render

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.Render2DEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.utils.render.ColorUtils
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.client.gui.Gui
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.entity.Entity
import org.lwjgl.opengl.GL11
import java.awt.Color

@ModuleInfo(name = "Camera", description = "Allows you to see through walls in third person view.", category = ModuleCategory.RENDER)
class Camera : Module(){
    //
    var alpha2: Int = 0
    //
    val antiBlindValue = BoolValue("AntiBlind", true)
    val fireEffect = BoolValue("Fire", false)
    private val fpsHurtCam = BoolValue("FPSHurtCam", true)
    val hurtcamColorRValue = IntegerValue("HurtColorRed", 255, 0, 255)
    val hurtcamColorGValue = IntegerValue("HurtColorGreen", 255, 0, 255)
    val hurtcamColorBValue = IntegerValue("HurtColorBlue", 255, 0, 255)
    private val colorModeValue = ListValue("Color", arrayOf("Custom"), "Custom")


    //FPSHurtCam
    @EventTarget
    private fun renderHud(event: Render2DEvent) {
        if (fpsHurtCam.get()) {
            val color = getColor( 0);
            run {
                val sr = ScaledResolution(mc)
                if (mc.thePlayer.hurtTime >= 1) {
                    if (alpha2 < 100) {
                        alpha2 += 5
                    }
                } else {
                    if (alpha2 > 0) {
                        alpha2 -= 5
                    }
                }
                this.drawGradientSidewaysV(
                    0.0,
                    0.0,
                    sr.scaledWidth.toDouble(),
                    25.0,
                    Color(color.red,color.green,color.blue,0).rgb,
                    Color(color.red,color.green,color.blue, alpha2).rgb
                )
                this.drawGradientSidewaysV(
                    0.0,
                    (sr.scaledHeight - 25).toDouble(),
                    sr.scaledWidth.toDouble(),
                    sr.scaledHeight.toDouble(),
                    Color(color.red,color.green,color.blue, alpha2).rgb,
                    Color(color.red,color.green,color.blue, 0).rgb
                )
            }
        }
    }

    fun drawGradientSidewaysV(left: Double, top: Double, right: Double, bottom: Double, col1: Int, col2: Int) {
        if (fpsHurtCam.get()) {
            val f = (col1 shr 24 and 255).toFloat() / 255.0f
            val f1 = (col1 shr 16 and 255).toFloat() / 255.0f
            val f2 = (col1 shr 8 and 255).toFloat() / 255.0f
            val f3 = (col1 and 255).toFloat() / 255.0f
            val f4 = (col2 shr 24 and 255).toFloat() / 255.0f
            val f5 = (col2 shr 16 and 255).toFloat() / 255.0f
            val f6 = (col2 shr 8 and 255).toFloat() / 255.0f
            val f7 = (col2 and 255).toFloat() / 255.0f
            GL11.glEnable(3042)
            GL11.glDisable(3553)
            GL11.glBlendFunc(770, 771)
            GL11.glEnable(2848)
            GL11.glShadeModel(7425)
            GL11.glPushMatrix()
            GL11.glBegin(7)
            GL11.glColor4f(f1, f2, f3, f)
            GL11.glVertex2d(left, bottom)
            GL11.glVertex2d(right, bottom)
            GL11.glColor4f(f5, f6, f7, f4)
            GL11.glVertex2d(right, top)
            GL11.glVertex2d(left, top)
            GL11.glEnd()
            GL11.glPopMatrix()
            GL11.glEnable(3553)
            GL11.glDisable(3042)
            GL11.glDisable(2848)
            GL11.glShadeModel(7424)
            Gui.drawRect(0, 0, 0, 0, 0)
        }
    }
    fun getColor(index: Int): Color {
        var colorModeValue = colorModeValue.get()
        var colorRedValue = hurtcamColorRValue.get()
        var colorGreenValue = hurtcamColorGValue.get()
        var colorBlueValue = hurtcamColorBValue.get()

        return when (colorModeValue) {
            "Custom" -> Color(colorRedValue, colorGreenValue, colorBlueValue)
            else -> ColorUtils.fade(Color(colorRedValue, colorGreenValue, colorBlueValue), index, 100)
        }
    }
}
