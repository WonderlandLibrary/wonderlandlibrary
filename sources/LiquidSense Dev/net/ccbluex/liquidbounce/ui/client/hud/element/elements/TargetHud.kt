package net.ccbluex.liquidbounce.ui.client.hud.element.elements

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.modules.combat.Aura
import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import net.ccbluex.liquidbounce.ui.client.hud.element.Side
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.GuiPlayerTabOverlay
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.utils.render.shader.shaders.RainbowShader
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.gui.Gui
import net.minecraft.client.network.NetworkPlayerInfo
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import org.lwjgl.opengl.GL11
import java.awt.Color
import kotlin.math.atan

@ElementInfo(name = "TargetHud")
class TargetHud(x: Double = 40.0, y: Double = 100.0 , side: Side = Side(Side.Horizontal.MIDDLE, Side.Vertical.DOWN)) : Element(x, y ,1.0f,side) {
    private val Mode = ListValue("Mode", arrayOf("Head", "Model"), "Head")
    private val rainbowX = FloatValue("Rainbow-X", -1000F, -2000F, 2000F)
    private val rainbowY = FloatValue("Rainbow-Y", -1000F, -2000F, 2000F)
    private val backgroundColorModeValue = ListValue("Background-Color", arrayOf("Custom","Rainbow"), "Custom")
    private val backgroundColorRedValue = IntegerValue("Background-R", 0, 0, 255)
    private val backgroundColorGreenValue = IntegerValue("Background-G", 0, 0, 255)
    private val backgroundColorBlueValue = IntegerValue("Background-B", 0, 0, 255)
    private val backgroundColorAlphaValue = IntegerValue("Background-Alpha", 0, 0, 255)


    private var modules = emptyList<Module>()


    /**
     * @Dev : LiquidSlowly : 727819556@qq.com
     */
    override fun drawElement(): Border {
        var Y = 3F
        var width = 100F
        val backgroundColorMode = backgroundColorModeValue.get()
        val backgroundRectRainbow = backgroundColorMode.equals("Rainbow", ignoreCase = true)
        val backgroundCustomColor = Color(backgroundColorRedValue.get(), backgroundColorGreenValue.get(), backgroundColorBlueValue.get(), backgroundColorAlphaValue.get()).rgb
        val killAura = LiquidBounce.moduleManager.getModule(Aura::class.java) as Aura
        var Name ="§f" + killAura.target?.name
        var Health = "§2" + killAura.target?.health?.times(5)?.toUInt() + "%"
        var Armor = "Armor>>"
        var Distance = "Dist>>"
        var h = (killAura.target?.maxHealth?.minus(killAura.target?.health!!))?.times(5)

        if (killAura.target != null) {
            RenderUtils.drawBorderedRect(0f, 0f, width, 43f, 3f ,  Color(0,0,0,150).rgb , 0)
            RenderUtils.drawRect(0f, 0f, width, 43f, Color(15, 15, 15).rgb)
            //
            Fonts.minecraftFont.drawString(Name, 37.5f, Y + 2, Color(255, 255, 255).rgb, false)
            Y += 24
            Fonts.minecraftFont.drawString(Armor, 35f , Y, Color(255, 255, 255).rgb, false)

            if (h != null) {
                //Armor
                Armor(killAura,Y)

                //DistanceToEntity
                DistanceToEntity(Fonts.minecraftFont,killAura,Y,Distance)

                //Main Render
                DefBackground(killAura,width,Y)

                var x2 = if(killAura.target?.health!! <= 20) 99F * killAura.target?.maxHealth!! / 20 - h.toFloat() else 99f

                RainbowShader.begin(backgroundRectRainbow, if (rainbowX.get() == 0.0F) 0.0F else 1.0F / rainbowX.get(), if (rainbowY.get() == 0.0F) 0.0F else 1.0F / rainbowY.get(), System.currentTimeMillis() % 10000 / 100000F).use {
                    RenderUtils.drawRect(1f, 38f, x2, 41f, when {
                        backgroundRectRainbow -> 0xFF shl 24
                        else -> backgroundCustomColor
                    })
                }
            }
            when (Mode.get().toLowerCase()) {
                "head" -> {
                    head(killAura)
                }
                "model" -> {
                    GlStateManager.pushMatrix()
                    GlStateManager.scale(0.27,0.27,0.27)
                    GlStateManager.translate(60f,130f,40f)
                    Model(killAura.target?.rotationYaw!!.toFloat(), killAura.target?.rotationPitch!!.toFloat(),killAura.target!!)
                    GlStateManager.popMatrix()
                }
            }
        }

        return Border(0f, 0f, width, 43f)
    }

    fun DistanceToEntity (fontRenderer : FontRenderer,killAura: Aura , Y : Float , Distance : String) {
        var Distances = if(killAura.target?.getDistanceToEntity(mc.thePlayer)!! <= 8) (killAura.target?.getDistanceToEntity(mc.thePlayer)!! * 2.25f) else 18f
        GlStateManager.pushMatrix()
        Y == 26.5f
        GlStateManager.translate(75.5f,-10f,0f)
        fontRenderer.drawString(Distance, -32f , 27f, Color(255, 255, 255).rgb, false)
        drawBox(-0.5f,Y + 0.5f, 18.5f, Y + 5.25f, 0.5f , Color(24,27,30).rgb, Color(0,0,0).rgb)
        RenderUtils.drawRect(0f,Y+1f, Distances  ,31.72f,Color(170,100,50).rgb)
        GlStateManager.popMatrix()
    }

    fun Armor (killAura: Aura , Y : Float) {
        GlStateManager.pushMatrix()
        Y == 26.5f
        GlStateManager.translate(75.5f, 0f, 0f)
        drawBox(-0.5f,Y + 2f, 18.5f, Y + 6.75f, 0.5f , Color(24,27,30).rgb, Color(0,0,0).rgb)
        RenderUtils.drawRect(0.0f, Y+2.5f,  (killAura.target?.totalArmorValue!!) * 0.9f , 33.2f, Color(50, 100, 200).rgb)
        GlStateManager.popMatrix()
    }

    fun DefBackground (killAura: Aura , width :  Float , Y : Float) {
        drawBox(0.5f, Y  + 10.5f, width - 1f, Y + 14.5f, 0.5f, if (killAura.target?.health!! <= 20) Color(35, 35, 35).rgb else Color(130, 90, 80).rgb, Color(50, 50, 50,150).rgb)
    }

    fun head (killAura: Aura) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
        val var5: List<*> = GuiPlayerTabOverlay.field_175252_a.sortedCopy<NetworkPlayerInfo>(mc.thePlayer.sendQueue.getPlayerInfoMap())
        val var17 = var5.iterator()

        GL11.glPushMatrix()
        GL11.glTranslated(0.5,0.5, 0.0)
        while (var17.hasNext()) {
            val aVar5 = var17.next()!!
            val var24 = aVar5 as NetworkPlayerInfo
            if (killAura.target is EntityPlayer) {
                mc.getTextureManager().bindTexture(var24.locationSkin)
                Gui.drawScaledCustomSizeModalRect(2, 2, 8.0f, 8.0f, 8, 8, 32, 32, 64.0f, 64.0f)
                if ((killAura.target as EntityPlayer).isInWater()) {
                    Gui.drawScaledCustomSizeModalRect(2, 2, 40.0f, 8.0f, 8, 8, 32, 32, 64.0f, 64.0f)
                }
                GlStateManager.bindTexture(0)
            }
        }
        drawBox(1.0f,1.0f,35f, 35.0f, 0.5f , Color(0,0,0,0).rgb, Color(75,75,75).rgb )
        GL11.glPopMatrix()
    }

    private fun drawBox(x: Float, y: Float, x2: Float, y2: Float, size: Float, color: Int, color2: Int) {
        // Normal
        RenderUtils.drawRect(x, y, x2, y2, color)
        // Up
        RenderUtils.drawRect(x, y, x2, y + size, color2)
        //Down
        RenderUtils.drawRect(x, y2 - size, x2, y2, color2)
        //lefat
        RenderUtils.drawRect(x, y, x + size, y2, color2)
        //right
        RenderUtils.drawRect(x2 - size, y, x2, y2, color2)
    }


    /**
     * Draw [entityLivingBase] to screen
     */
    private fun Model(yaw: Float, pitch: Float, entityLivingBase: EntityLivingBase) {
        GlStateManager.resetColor()
        GL11.glColor4f(1F, 1F, 1F, 1F)
        GlStateManager.enableColorMaterial()
        GlStateManager.pushMatrix()
        GlStateManager.translate(0F, 0F, 50F)
        GlStateManager.scale(-50F, 50F, 50F)
        GlStateManager.rotate(180F, 0F, 0F, 1F)

        val renderYawOffset = entityLivingBase.renderYawOffset
        val rotationYaw = entityLivingBase.rotationYaw
        val rotationPitch = entityLivingBase.rotationPitch
        val prevRotationYawHead = entityLivingBase.prevRotationYawHead
        val rotationYawHead = entityLivingBase.rotationYawHead

        GlStateManager.rotate(135F, 0F, 1F, 0F)
        RenderHelper.enableStandardItemLighting()
        GlStateManager.rotate(-135F, 0F, 1F, 0F)
        GlStateManager.rotate(-atan(pitch / 40F) * 20.0F, 1F, 0F, 0F)

        entityLivingBase.renderYawOffset = yaw - (yaw / yaw * 0.4f)
        entityLivingBase.rotationYaw =yaw - (yaw / yaw * 0.2f)
        entityLivingBase.rotationPitch = pitch
        entityLivingBase.rotationYawHead = entityLivingBase.rotationYaw
        entityLivingBase.prevRotationYawHead = entityLivingBase.rotationYaw

        GlStateManager.translate(0F, 0F, 0F)

        val renderManager = mc.renderManager
        renderManager.setPlayerViewY(180F)
        renderManager.isRenderShadow = false
        renderManager.renderEntityWithPosYaw(entityLivingBase, 0.0, 0.0, 0.0, 0F, 1F)
        renderManager.isRenderShadow = true

        entityLivingBase.renderYawOffset = renderYawOffset
        entityLivingBase.rotationYaw = rotationYaw
        entityLivingBase.rotationPitch = rotationPitch
        entityLivingBase.prevRotationYawHead = prevRotationYawHead
        entityLivingBase.rotationYawHead = rotationYawHead

        GlStateManager.popMatrix()
        RenderHelper.disableStandardItemLighting()
        GlStateManager.disableRescaleNormal()
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit)
        GlStateManager.disableTexture2D()
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit)
        GlStateManager.resetColor()
    }


}