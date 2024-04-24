package me.offeex.bloomware.client.module.hud

import me.offeex.bloomware.Bloomware.mc
import me.offeex.bloomware.api.manager.managers.FontManagerr
import me.offeex.bloomware.client.module.HudModule
import me.offeex.bloomware.client.module.client.Colors
import me.offeex.bloomware.client.module.client.StreamerMode
import net.minecraft.client.util.math.MatrixStack

object Welcomer : HudModule("Welcomer", "Welcomes you", Category.HUD) {
    override fun draw(matrices: MatrixStack, tickDelta: Float) {
        val module = StreamerMode
        if (!module.enabled || module.enabled && !StreamerMode.hideNickname.toggled) FontManagerr.drawString(
            matrices, "Looking cute today, " + mc.session.username + "! :^)", x, y, Colors.hud, this
        )
    }
}