package me.offeex.bloomware.client.module.hud

import me.offeex.bloomware.api.helper.cNetHandler
import me.offeex.bloomware.api.manager.managers.FontManagerr
import me.offeex.bloomware.client.module.HudModule
import me.offeex.bloomware.client.module.client.Colors
import net.minecraft.client.util.math.MatrixStack

object PlayerCount : HudModule("PlayerCount", "Shows current interact count", Category.HUD) {
    override fun draw(matrices: MatrixStack, tickDelta: Float) {
        FontManagerr.drawString(
            matrices,
            "Players: " + if (cNetHandler.playerList == null) 0 else cNetHandler.playerList.size,
            x,
            y,
            Colors.hud,
            this
        )
    }
}