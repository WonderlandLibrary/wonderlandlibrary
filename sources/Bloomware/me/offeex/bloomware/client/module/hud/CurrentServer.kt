package me.offeex.bloomware.client.module.hud

import me.offeex.bloomware.Bloomware.mc
import me.offeex.bloomware.api.manager.managers.FontManagerr
import me.offeex.bloomware.client.module.HudModule
import me.offeex.bloomware.client.module.client.Colors
import net.minecraft.client.util.math.MatrixStack

object CurrentServer :
    HudModule("CurrentServer", "Shows current server you playing", Category.HUD) {
    var server = "s"
    override fun draw(matrices: MatrixStack, tickDelta: Float) {
        server =
            if (mc.currentServerEntry != null) mc.currentServerEntry!!.address else if (mc.isInSingleplayer) "SinglePlayer" else "null"
        FontManagerr.drawString(
            matrices, "Server: $server", x, y, Colors.hud, this
        )
    }
}