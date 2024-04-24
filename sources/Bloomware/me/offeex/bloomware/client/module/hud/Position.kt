package me.offeex.bloomware.client.module.hud

import me.offeex.bloomware.Bloomware.mc
import me.offeex.bloomware.api.helper.cPlayer
import me.offeex.bloomware.api.manager.managers.FontManagerr
import me.offeex.bloomware.api.util.MathUtil.roundDecimal
import me.offeex.bloomware.api.util.WorldUtil.dimension
import me.offeex.bloomware.client.module.HudModule
import me.offeex.bloomware.client.module.client.Colors
import me.offeex.bloomware.client.module.client.StreamerMode
import me.offeex.bloomware.client.setting.setting
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.Direction

object Position : HudModule("Position", "Shows current position", Category.HUD) {
    private val direction = setting("Direction").bool(true)
    private val otherDimension = setting("OtherDimension").bool(true)

    init {
        direction.toggledUpdateBus.subscribe { _, newValue ->
            height = if (mc.world == null) height.times(if (newValue) 2 else 1)
            else height.times(if (newValue) 2.0 else 0.5).toInt()
        }
    }

    override fun draw(matrices: MatrixStack, tickDelta: Float) {
        val sm = StreamerMode
        if (sm.enabled && StreamerMode.hideCoords.toggled) return
        if (direction.toggled) {
            height = 24
            FontManagerr.drawString(
                matrices,
                "Direction: " + cPlayer.horizontalFacing.axis.getName()
                    .uppercase() + if (cPlayer.horizontalFacing.direction == Direction.AxisDirection.POSITIVE) "+" else "-",
                x,
                y - 4,
                Colors.hud.color
            )
        }
        var text = ""
        text += "XYZ: " + roundDecimal(cPlayer.x, 1) + "  " + roundDecimal(
            cPlayer.y, 1
        ) + "  " + roundDecimal(cPlayer.z, 1)
        if (otherDimension.toggled) {
            val multiplier: Float = if (dimension == 0) 0.125f else if (dimension == 1) 8f else 1f
            text += " (" + roundDecimal(cPlayer.x * multiplier, 1) + "  " + roundDecimal(
                cPlayer.z * multiplier, 1
            ) + ")"
        }
        val result = text.replace(",", "")
        FontManagerr.drawString(
            matrices,
            result,
            x,
            y + if (direction.toggled) getOffsetBottom(result) + 2 else 0,
            Colors.hud,
            this
        )
    }
}