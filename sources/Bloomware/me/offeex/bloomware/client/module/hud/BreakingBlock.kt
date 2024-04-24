package me.offeex.bloomware.client.module.hud

import me.offeex.bloomware.Bloomware.mc
import me.offeex.bloomware.api.manager.managers.FontManagerr
import me.offeex.bloomware.client.module.HudModule
import me.offeex.bloomware.client.module.client.Colors
import me.offeex.bloomware.mixin.accessor.IClientPlayerInteractionManager
import net.minecraft.client.util.math.MatrixStack

object BreakingBlock :
    HudModule("BreakingBlock", "Shows percentage progress of breaking block.", Category.HUD) {
    override fun draw(matrices: MatrixStack, tickDelta: Float) {
        FontManagerr.drawString(
            matrices,
            "Breaking progress: " + ((mc.interactionManager as IClientPlayerInteractionManager?)!!.breakingProgress * 100).toInt() + "%",
            x,
            y,
            Colors.hud,
            this
        )
    }
}