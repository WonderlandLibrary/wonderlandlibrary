package me.offeex.bloomware.client.module.hud

import me.offeex.bloomware.api.manager.managers.FontManagerr
import me.offeex.bloomware.api.manager.managers.SessionManager
import me.offeex.bloomware.client.module.HudModule
import me.offeex.bloomware.client.module.client.Colors
import me.offeex.bloomware.client.setting.setting
import net.minecraft.client.util.math.MatrixStack

object LastPacket : HudModule("LastPacket", "shows time since last packet received", Category.HUD) {
    private val showPacket = setting("ShowPacket").bool()
    override fun draw(matrices: MatrixStack, tickDelta: Float) {
        val packet = SessionManager.lastTimePacket
        val lastTime = packet.time
        FontManagerr.drawString(
            matrices,
            "Since last packet: " + String.format(
                "%.2f", (System.currentTimeMillis() - lastTime) / 1000
            ) + "s " + if (showPacket.toggled) packet.javaClass.simpleName else "",
            x,
            y,
            Colors.hud,
            this
        )
    }
}