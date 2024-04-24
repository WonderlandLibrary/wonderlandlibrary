package me.offeex.bloomware.api.manager.managers

import me.offeex.bloomware.api.helper.cNetHandler
import me.offeex.bloomware.api.helper.cPlayer
import me.offeex.bloomware.api.manager.Manager
import me.offeex.bloomware.api.util.CPlayerUtil
import me.offeex.bloomware.api.util.RotationUtil
import me.offeex.bloomware.event.dispatcher.Subscribe
import me.offeex.bloomware.event.events.EventPacket
import me.offeex.bloomware.mixin.accessor.IPlayerMoveC2SPacket
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket
import net.minecraft.util.math.Vec3d

object RotationManager : Manager() {
    var targetYaw = 0f
    var targetPitch = 0f
    var packetYaw = 0f
    var packetPitch = 0f
    var rotate = false

    @Subscribe
    private fun onPacketSend(event: EventPacket.Send) {
        if (event.shift) return

        val packet = event.packet
        if (packet is PlayerMoveC2SPacket) {
            if (rotate) (packet as IPlayerMoveC2SPacket).let {
                it.setYaw(targetYaw)
                it.setPitch(targetPitch)
            }
            cPlayer.apply {
                packetYaw = packet.getYaw(targetYaw)
                packetPitch = packet.getPitch(targetPitch)
            }
        }
    }

    fun sendPacket(yaw: Float = targetYaw, pitch: Float = targetPitch) {
        packetYaw = yaw
        packetPitch = pitch
        cNetHandler.sendPacket(PlayerMoveC2SPacket.LookAndOnGround(packetYaw, packetPitch, cPlayer.isOnGround))
    }

    fun setRotation(yaw: Float = targetYaw, pitch: Float = targetPitch) {
        targetYaw = yaw
        targetPitch = pitch
        rotate = true
    }

    fun setRotation(target: Vec3d) {
        setRotation(RotationUtil.getLookYaw(target), RotationUtil.getLookPitch(target))
    }

    fun rotateTo(target: Vec3d) {
        setRotation(target)
        sendPacket()
    }

    fun reset() {
        rotate = false
        targetYaw = cPlayer.yaw
        targetPitch = cPlayer.pitch
    }
}
