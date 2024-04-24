package me.offeex.bloomware.client.module.hud

import me.offeex.bloomware.api.manager.managers.FontManagerr
import me.offeex.bloomware.api.manager.managers.SessionManager
import me.offeex.bloomware.api.util.MathUtil
import me.offeex.bloomware.client.module.HudModule
import me.offeex.bloomware.client.module.client.Colors
import me.offeex.bloomware.client.setting.setting
import net.minecraft.client.util.math.MatrixStack

object Speedometer : HudModule("Speedometer", "Your speed.", Category.HUD) {
    private val mode = setting("Mode").enum("Km/h", "M/s")
    private val speedType = setting("SpeedType").bool(true)

    override fun draw(matrices: MatrixStack, tickDelta: Float) {
        FontManagerr.drawString(
            matrices, "Speed: ${speed()}${label()}", x, y, Colors.hud, this
        )
    }

    private fun label() = if (speedType.toggled) " ${mode.selected}" else ""

    private fun speed(): Double = run {
        val speed = SessionManager.horizontalSpeed() * SessionManager.timer
        if (mode.like("Km/h")) MathUtil.roundDecimal(MathUtil.toKmH(speed), 1)
        else MathUtil.roundDecimal(MathUtil.toMS(speed), 1)
    }
}