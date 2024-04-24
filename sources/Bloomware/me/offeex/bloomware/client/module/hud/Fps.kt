package me.offeex.bloomware.client.module.hud

import me.offeex.bloomware.Bloomware.mc
import me.offeex.bloomware.api.manager.managers.FontManagerr
import me.offeex.bloomware.client.module.HudModule
import me.offeex.bloomware.client.module.client.Colors
import me.offeex.bloomware.mixin.accessor.IMinecraftClient
import net.minecraft.client.util.math.MatrixStack

object Fps : HudModule("FPS", "Shows the current fps value", Category.HUD) {
    override fun draw(matrices: MatrixStack, tickDelta: Float) {
        FontManagerr.drawString(
            matrices, "FPS: " + (mc as IMinecraftClient).currentFps, x, y, Colors.hud, this
        )
    }
}