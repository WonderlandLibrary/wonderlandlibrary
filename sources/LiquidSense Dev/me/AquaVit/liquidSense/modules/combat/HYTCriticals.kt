package me.AquaVit.liquidSense.modules.combat

import me.AquaVit.liquidSense.modules.movement.HYTNoFall
import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.ccbluex.liquidbounce.value.BoolValue
import net.minecraft.network.play.client.C03PacketPlayer
import net.minecraft.network.play.client.C07PacketPlayerDigging

@ModuleInfo("HYTCriticals", "Hyt Criticals Bypass:/", ModuleCategory.COMBAT)
class HYTCriticals : Module() {
    private val msTimer = MSTimer()
    private val hyt = BoolValue("Hyt", true);
    private var isBreaking = false

    @EventTarget
    private fun onPacket(event: PacketEvent) {
        if (event.packet is C07PacketPlayerDigging) { //classProvider.isCPacketPlayerDigging(event.packet)
            val packet = event.packet
            when (packet.status) {
                C07PacketPlayerDigging.Action.START_DESTROY_BLOCK -> isBreaking = true
                C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK,
                C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK -> isBreaking = false
            }
        }
        if (event.packet is C03PacketPlayer && !isBreaking) {
            val packet = event.packet
            if (hyt.get()) {
                val nofall = LiquidBounce.moduleManager[HYTNoFall::class.java]//在Hyt得和nofall配合使用，不然会出问题
                if (!nofall!!.state) nofall.toggle()
            } else {
                if (mc.thePlayer!!.fallDistance == 0f && mc.thePlayer!!.motionY < 0 && !mc.thePlayer!!.onGround) {
                    packet.onGround = true
                    return
                }
            }
            if (mc.thePlayer!!.onGround) {
                packet.onGround = false
                packet.y += if (mc.thePlayer!!.ticksExisted and 1 == 0 || msTimer.hasTimePassed(80)) 1E-7 else 1E-8
                msTimer.reset()
            }
        }
    }
}