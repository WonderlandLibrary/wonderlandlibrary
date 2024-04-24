package me.offeex.bloomware.client.module.hud

import me.offeex.bloomware.api.helper.cPlayer
import me.offeex.bloomware.api.manager.managers.FontManagerr
import me.offeex.bloomware.api.util.MathUtil.roundDecimal
import me.offeex.bloomware.client.module.HudModule
import me.offeex.bloomware.client.module.client.Colors
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.MathHelper

object YawPitch : HudModule("YawPitch", "Shows your yaw & pitch", Category.HUD) {
    init {
        height *= 2
    }

    override fun draw(matrices: MatrixStack, tickDelta: Float) {
        val yawText = "Yaw: " + roundDecimal(MathHelper.wrapDegrees(cPlayer.yaw).toDouble(), 1)
        val pitchText = "Pitch: " + roundDecimal(cPlayer.pitch.toDouble(), 1)
        width = FontManagerr.width(yawText).coerceAtLeast(FontManagerr.width(pitchText)).toInt()
        height = 24
        FontManagerr.drawString(matrices, yawText, x, y - 4, Colors.hud.color)
        FontManagerr.drawString(
            matrices,
            pitchText,
            x,
            y + getOffsetBottom(pitchText),
            Colors.hud.color
        )
    }
}