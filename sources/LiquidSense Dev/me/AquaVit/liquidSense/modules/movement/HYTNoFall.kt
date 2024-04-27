package me.AquaVit.liquidSense.modules.movement;

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.minecraft.network.play.client.C03PacketPlayer

@ModuleInfo("HYTNoFall", "HYT NoFall Bypass:/", ModuleCategory.MOVEMENT)
class HYTNoFall : Module() {
    private var fallDistance = 0.0
    private var packetY = 0.0

    @EventTarget
    private fun onPacket(event: PacketEvent) {
        if (event.packet !is C03PacketPlayer) return
        val packet = event.packet
        val y = packet.y
        if (!packet.isMoving) return/*如果这行报错，刷新Gradle依赖项即可*/
        if (LiquidBounce.moduleManager.getModule("HYTFly")!!.state) packet.onGround = true
        if (!packet.onGround)
            if (y < packetY) fallDistance += packetY - y
        if (mc.thePlayer!!.isInWater || mc.thePlayer!!.isInWeb || mc.thePlayer!!.isOnLadder || mc.thePlayer!!.ridingEntity != null) fallDistance = 0.0
        if (fallDistance >= 3) packet.onGround = true
        if (packet.onGround) fallDistance = 0.0
        packetY = y
    }
}