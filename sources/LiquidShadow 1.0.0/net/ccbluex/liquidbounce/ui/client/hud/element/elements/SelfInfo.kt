package net.ccbluex.liquidbounce.ui.client.hud.element.elements

import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.extensions.getDistanceToEntityBox
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import java.awt.Color
import java.lang.Math.pow
import java.lang.Math.sqrt
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import kotlin.math.abs
import kotlin.math.pow

@ElementInfo(name = "SelfInfo")
class SelfInfo : Element() {
    private val decimalFormat = DecimalFormat("##0.00", DecimalFormatSymbols(Locale.ENGLISH))
    private val fadeSpeed = FloatValue("FadeSpeed", 2F, 1F, 9F)
    private val backgroundRValue = IntegerValue("BackgroundR",75,0,255)
    private val backgroundGValue = IntegerValue("BackgroundG",75,0,255)
    private val backgroundBValue = IntegerValue("BackgroundB",75,0,255)
    private val backgroundAlphaValue = IntegerValue("BackgroundAlpha",75,0,255)
    private val speedValue = BoolValue("Speed",true)
    private val speedRValue = IntegerValue("SpeedR",65,0,255)
    private val speedGValue = IntegerValue("SpeedG",215,0,255)
    private val speedBValue = IntegerValue("SpeedB",255,0,255)
    private val pingValue = BoolValue("Ping",true)
    private val pingRValue = IntegerValue("PingR",255,0,255)
    private val pingGValue = IntegerValue("PingG",125,0,255)
    private val pingBValue = IntegerValue("PingB",0,0,255)
    private val healthValue = BoolValue("Health",true)
    private val healthRValue = IntegerValue("HealthR",255,0,255)
    private val healthGValue = IntegerValue("HealthG",80,0,255)
    private val healthBValue = IntegerValue("HealthB",80,0,255)
    private val healthDamageRValue = IntegerValue("HealthDamageR",252,0,255)
    private val healthDamageGValue = IntegerValue("HealthDamageG",185,0,255)
    private val healthDamageBValue = IntegerValue("HealthDamageB",65,0,255)
    private val healthRegenRValue = IntegerValue("HealthRegenR",44,0,255)
    private val healthRegenGValue = IntegerValue("HealthRegenG",201,0,255)
    private val healthRegenBValue = IntegerValue("HealthRegenB",144,0,255)
    private val lowHpWarnValue = BoolValue("LowHpWarn",true)
    private val warnHealthValue = IntegerValue("WarnHealth",6,0,14)
    private val healthWarnRValue = IntegerValue("HealthWarnR",255,0,255)
    private val healthWarnGValue = IntegerValue("HealthWarnG",80,0,255)
    private val healthWarnBValue = IntegerValue("HealthWarnB",80,0,255)

    private var easingHealth: Float = 0F
    private var lastTarget: Entity? = null

    private var speed = 0F
    private var lastX = 0.0
    private var lastZ = 0.0

    override fun updateElement() {
        if (mc.thePlayer == null) return
        speed = sqrt(pow(lastX - mc.thePlayer.posX, 2.0) + pow(lastZ - mc.thePlayer.posZ, 2.0)).toFloat() * 20F * mc.timer.timerSpeed
        lastX = mc.thePlayer.posX
        lastZ = mc.thePlayer.posZ
    }

    override fun drawElement(): Border {
        val self = mc.thePlayer

        if (self is EntityPlayer) {
            if (self != lastTarget || easingHealth < 0 || easingHealth > self.maxHealth ||
                abs(easingHealth - self.health) < 0.01) {
                easingHealth = self.health
            }

            val width = 120F

            // Draw rect box
            if (self.health < warnHealthValue.get() && lowHpWarnValue.get()) {
                RenderUtils.drawRect(0F, 0F, width, 12F, Color(healthWarnRValue.get(),healthWarnGValue.get(),healthWarnBValue.get(),backgroundAlphaValue.get()).rgb)
            } else {
                RenderUtils.drawRect(0F, 0F, width, 12F, Color(backgroundRValue.get(),backgroundBValue.get(),backgroundGValue.get(),backgroundAlphaValue.get()).rgb)
            }

            if (healthValue.get()) {
                // Damage animation
                if (easingHealth > self.health)
                    RenderUtils.drawRect(0F, 9F, (easingHealth / self.maxHealth) * width,
                        12F, Color(healthDamageRValue.get(),healthDamageGValue.get(),healthDamageBValue.get()).rgb)

                // Health bar
                RenderUtils.drawRect(0F, 9F, (self.health / self.maxHealth) * width,
                    12F, Color(healthRValue.get(),healthGValue.get(),healthBValue.get()).rgb)

                // Heal animation
                if (easingHealth < self.health)
                    RenderUtils.drawRect((easingHealth / self.maxHealth) * width, 9F,
                        (self.health / self.maxHealth) * width, 12F, Color(healthRegenRValue.get(),healthRegenGValue.get(),healthRegenBValue.get()).rgb)
            }

            easingHealth += ((self.health - easingHealth) / 2.0F.pow(10.0F - fadeSpeed.get())) * RenderUtils.deltaTime

            if (speedValue.get()) {
                Fonts.font35.drawString("$speed BPS", 2, 2, Color(speedRValue.get(),speedGValue.get(),speedBValue.get()).rgb)
            }

            // Draw info
            val playerInfo = mc.netHandler.getPlayerInfo(self.uniqueID)
            if (playerInfo != null && pingValue.get()) {
                Fonts.font35.drawString("${playerInfo.responseTime.coerceAtLeast(0)} ms",
                    118 - Fonts.font35.getStringWidth("${playerInfo.responseTime.coerceAtLeast(0)} ms"), 2, Color(pingRValue.get(),pingGValue.get(),pingBValue.get()).rgb)
            }
        }

        lastTarget = self
        return Border(0F, 0F, 120F, 12F)
    }
}