package me.offeex.bloomware.client.module.hud

import me.offeex.bloomware.api.manager.managers.FontManagerr
import me.offeex.bloomware.api.manager.managers.SessionManager.tps
import me.offeex.bloomware.client.module.HudModule
import me.offeex.bloomware.client.module.client.Colors
import net.minecraft.client.util.math.MatrixStack

object Tps : HudModule("TPS", "shows current server tps", Category.HUD) {
    override fun draw(matrices: MatrixStack, tickDelta: Float) {
        FontManagerr.drawString(
            matrices, "TPS: $tps", x, y, Colors.hud, this
        )
    }
}