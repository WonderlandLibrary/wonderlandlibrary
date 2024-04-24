package me.offeex.bloomware.client.module.hud

import me.offeex.bloomware.api.manager.managers.FontManagerr
import me.offeex.bloomware.client.module.HudModule
import me.offeex.bloomware.client.module.client.Colors
import net.minecraft.client.util.math.MatrixStack

object ModuleNotifier :
    HudModule(
        "ModuleNotifier",
        "Adds the window with the notifications about toggling modules.",
        Category.HUD
    ) {
    private var time: Long = 0
    private var message = ""
    override fun draw(matrices: MatrixStack, tickDelta: Float) {
        if (System.currentTimeMillis() - time < 2000) {
            FontManagerr.drawString(
                matrices, message, x, y, Colors.hud, this
            )
        }
    }

    fun setMessage(msg: String) {
        time = System.currentTimeMillis()
        message = msg
    }
}