package me.offeex.bloomware.client.module.hud

import me.offeex.bloomware.api.manager.managers.FontManagerr
import me.offeex.bloomware.api.manager.managers.SessionManager.convertTime
import me.offeex.bloomware.api.manager.managers.SessionManager.timeOnline
import me.offeex.bloomware.client.module.HudModule
import me.offeex.bloomware.client.module.client.Colors
import net.minecraft.client.util.math.MatrixStack

object OnlineTime : HudModule("OnlineTime", "Shows time you are playing", Category.HUD) {
    override fun draw(matrices: MatrixStack, tickDelta: Float) {
        FontManagerr.drawString(
            matrices,
            "Online for: " + convertTime(timeOnline),
            x,
            y,
            Colors.hud,
            this
        )
    }
}