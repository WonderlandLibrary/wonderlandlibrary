package me.offeex.bloomware.client.module.hud

import me.offeex.bloomware.api.manager.managers.FontManagerr
import me.offeex.bloomware.client.module.HudModule
import me.offeex.bloomware.client.module.client.Colors
import net.minecraft.client.util.math.MatrixStack

object MemoryUsage : HudModule("MemoryUsage", "Shows how many RAM minecraft uses", Category.HUD) {
    override fun draw(matrices: MatrixStack, tickDelta: Float) {
        val maxMemory = Runtime.getRuntime().maxMemory()
        val totalMemory = Runtime.getRuntime().totalMemory()
        val freeMemory = Runtime.getRuntime().freeMemory()
        FontManagerr.drawString(
            matrices, "Memory: " + String.format(
                "% 2d%% %03d/%03dMB",
                (totalMemory - freeMemory) * 100L / maxMemory,
                toMiB(totalMemory - freeMemory),
                toMiB(maxMemory)
            ), x, y, Colors.hud, this
        )
    }

    private fun toMiB(bytes: Long): Long {
        return bytes / 1024L / 1024L
    }
}