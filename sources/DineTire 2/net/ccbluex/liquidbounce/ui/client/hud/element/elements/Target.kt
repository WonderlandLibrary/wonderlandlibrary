/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */

package net.ccbluex.liquidbounce.ui.client.hud.element.elements

import net.ccbluex.liquidbounce.LiquidBounce

import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura
import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.GLUtils
import net.ccbluex.liquidbounce.utils.extensions.getDistanceToEntityBox
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.utils.render.RenderUtilsKyinoSense
import net.ccbluex.liquidbounce.value.FloatValue
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11
import java.awt.Color
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import kotlin.math.abs
import kotlin.math.pow

/**
 * A target hud
 */
@ElementInfo(name = "Target")
class Target : Element() {

    private val fadeSpeed = FloatValue("FadeSpeed", 2F, 1F, 9F)

    private var easingHealth: Float = 0F
    private var lastTarget: Entity? = null

    override fun drawElement(): Border {
        val target = (LiquidBounce.moduleManager[KillAura::class.java] as KillAura).target

        if (target is EntityPlayer) {
            if (target != lastTarget || easingHealth < 0 || easingHealth > target.maxHealth ||
                abs(easingHealth - target.health) < 0.01) {
                easingHealth = target.health
            }

            val width = (38 + (target.name?.let(Fonts.font40::getStringWidth) ?: 0))
                .coerceAtLeast(133)
                .toFloat()

            RenderUtils.drawBorderedRect(0F, 0F, width, 36f, 3F, Color(5, 5, 5, 255).rgb, Color(25, 25, 25, 255).rgb)
            //RenderUtils.drawBorderedRect(0F, 0F, width, 36F, 3F, Color.BLACK.rgb, Color.BLACK.rgb)

            // Damage animation
            if (easingHealth > target.health)
                RenderUtils.drawRect(0F, 34F, (easingHealth / target.maxHealth) * width,
                    36F, Color(252, 185, 65).rgb)

            // Health bar
            RenderUtils.drawRect(0F, 34F, (target.health / target.maxHealth) * width,
                36F, Color(252, 96, 66).rgb)
            GL11.glPushMatrix()
            // Heal animation
            if (easingHealth < target.health)
                RenderUtils.drawRect((easingHealth / target.maxHealth) * width, 34F,
                    (target.health / target.maxHealth) * width, 36F, Color(44, 201, 144).rgb)

            easingHealth += ((target.health - easingHealth) / 2.0F.pow(10.0F - fadeSpeed.get())) * RenderUtils.deltaTime
            GL11.glPopMatrix()
            target.name?.let { Fonts.font40.drawString(it, 36, 3, 0xffffff) }
            //Fonts.font35.drawString("Distance: ${decimalFormat.format(mc.thePlayer!!.getDistanceToEntityBox(target))}", 36, 15, 0xffffff)

            var x = 35
            var y = 12
            for (index in 3 downTo 0) {
                //RenderUtils.drawRect(
                //    x.toFloat(),
                //    y.toFloat(),
                //    x.toFloat() + 18f,
                //    y + 15f,
                //    Color(30, 255, 30, 120).rgb
                //)
                if (target.inventory.armorInventory[index] != null) {
                    GlStateManager.pushMatrix()
                    GlStateManager.scale(0.65, 0.65, 0.65)
                    Fonts.font40.drawStringWithShadow(
                        ((target.inventory.armorInventory[index].maxDamage - target.inventory.armorInventory[index].itemDamage)).toString(),
                        (x.toFloat() + 4f) * 1 / 0.65f,
                        42f,
                        Color.white.rgb
                    )
                    GlStateManager.scale(1 / 0.65, 1 / 0.65, 1 / 0.65)
                    GlStateManager.popMatrix()
                    GL11.glPushMatrix()
                    GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
                    if (mc.theWorld != null) {
                        GLUtils.enableGUIStandardItemLighting()
                    }
                    GlStateManager.pushMatrix()
                    GlStateManager.disableAlpha()
                    GlStateManager.clear(256)
                    mc.renderItem.renderItemIntoGUI(target.inventory.armorInventory[index], x + 1, y - 1)
                    mc.renderItem.zLevel = 0.0f
                    GlStateManager.disableBlend()
                    GlStateManager.scale(0.5, 0.5, 0.5)
                    GlStateManager.disableDepth()
                    GlStateManager.disableLighting()
                    GlStateManager.enableDepth()
                    GlStateManager.scale(2.0f, 2.0f, 25.0f)
                    GlStateManager.enableAlpha()
                    GlStateManager.popMatrix()
                    GL11.glPopMatrix()
                }
                x += 20
            }

            //RenderUtils.drawRect(x.toFloat(), y.toFloat(), x.toFloat() + 18f, y + 15f, Color(30, 255, 30, 120).rgb)
            if (target.inventory.mainInventory[target.inventory.currentItem] != null) {
                if (target.inventory.mainInventory[target.inventory.currentItem].isItemStackDamageable) {
                    GlStateManager.pushMatrix()
                    GlStateManager.scale(0.65, 0.65, 0.65)
                    Fonts.font40.drawStringWithShadow(
                        ((target.inventory.mainInventory[target.inventory.currentItem].maxDamage - target.inventory.mainInventory[target.inventory.currentItem].itemDamage)).toString(),
                        (x.toFloat() + 4f) * 1 / 0.65f,
                        42f,
                        Color.white.rgb
                    )
                    GlStateManager.scale(1 / 0.65, 1 / 0.65, 1 / 0.65)
                    GlStateManager.popMatrix()
                }
                GL11.glPushMatrix()
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
                if (mc.theWorld != null) {
                    RenderHelper.enableGUIStandardItemLighting()
                }
                GlStateManager.pushMatrix()
                GlStateManager.disableAlpha()
                GlStateManager.clear(256)
                mc.renderItem.renderItemIntoGUI(
                    target.inventory.mainInventory[target.inventory.currentItem],
                    x + 1,
                    y - 1
                )
                mc.renderItem.zLevel = 0.0f
                GlStateManager.disableBlend()
                GlStateManager.scale(0.5, 0.5, 0.5)
                GlStateManager.disableDepth()
                GlStateManager.disableLighting()
                GlStateManager.enableDepth()
                GlStateManager.scale(2.0f, 2.0f, 2.0f)
                GlStateManager.enableAlpha()
                GlStateManager.popMatrix()
                GL11.glPopMatrix()
            }

            // Draw info
            val playerInfo = mc.netHandler.getPlayerInfo(target.uniqueID)
            if (playerInfo != null) {
                //Fonts.font35.drawString("Ping: ${playerInfo.responseTime.coerceAtLeast(0)}", 36, 24, 0xffffff)

                // Draw head
                val locationSkin = playerInfo.locationSkin
                drawHead(locationSkin, 30, 30)
            }
        }

        lastTarget = target
        return Border(0F, 0F, 120F, 36F)
    }

    private fun drawHead(skin: ResourceLocation, width: Int, height: Int) {
        GL11.glColor4f(1F, 1F, 1F, 1F)
        mc.textureManager.bindTexture(skin)
        RenderUtils.drawScaledCustomSizeModalRect(2, 2, 8F, 8F, 8, 8, width, height,
            64F, 64F)
    }

}