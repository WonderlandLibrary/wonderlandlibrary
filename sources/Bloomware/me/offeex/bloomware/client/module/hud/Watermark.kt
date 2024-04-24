package me.offeex.bloomware.client.module.hud

import me.offeex.bloomware.Bloomware
import me.offeex.bloomware.api.manager.managers.FontManagerr
import me.offeex.bloomware.client.module.HudModule
import me.offeex.bloomware.client.module.client.Colors
import net.minecraft.client.util.math.MatrixStack

object Watermark : HudModule("Watermark", "Shows logo", Category.HUD) {
    override fun draw(matrices: MatrixStack, tickDelta: Float) {
        FontManagerr.drawString(
            matrices,
            Bloomware.NAME + " v" + Bloomware.VERSION,
            x,
            y,
            Colors.hud,
            this
        )
    }
}