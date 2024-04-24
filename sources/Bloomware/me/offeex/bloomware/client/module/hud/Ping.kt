package me.offeex.bloomware.client.module.hud

import me.offeex.bloomware.api.helper.cNetHandler
import me.offeex.bloomware.api.helper.cPlayer
import me.offeex.bloomware.api.manager.managers.FontManagerr
import me.offeex.bloomware.client.module.HudModule
import me.offeex.bloomware.client.module.client.Colors
import net.minecraft.client.util.math.MatrixStack

object Ping : HudModule("Ping", "Shows your ping", Category.HUD) {
    override fun draw(matrices: MatrixStack, tickDelta: Float) {
        val playerListEntry = cNetHandler.getPlayerListEntry(cPlayer.uuid)
        try {
            FontManagerr.drawString(
                matrices,
                "Ping: " + playerListEntry!!.latency + "ms",
                x,
                y,
                Colors.hud,
                this
            )
        } catch (nullPointerException: NullPointerException) {
            FontManagerr.drawString(matrices, "Ping: 0ms", x, y, Colors.hud, this)
        }
    }
}