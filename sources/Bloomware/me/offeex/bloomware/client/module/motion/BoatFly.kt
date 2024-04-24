package me.offeex.bloomware.client.module.motion

import me.offeex.bloomware.Bloomware.mc
import me.offeex.bloomware.api.helper.cInteractManager
import me.offeex.bloomware.api.helper.cPlayer
import me.offeex.bloomware.api.util.MovementUtil.getMovementOnKey
import me.offeex.bloomware.client.module.Module
import me.offeex.bloomware.client.setting.setting
import me.offeex.bloomware.event.dispatcher.Subscribe
import me.offeex.bloomware.event.events.EventPacket
import me.offeex.bloomware.event.events.EventPlayerTravel
import me.offeex.bloomware.event.events.EventRender
import net.minecraft.entity.vehicle.BoatEntity
import net.minecraft.network.packet.c2s.play.PlayerInputC2SPacket
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.LookAndOnGround
import net.minecraft.network.packet.c2s.play.VehicleMoveC2SPacket
import net.minecraft.network.packet.s2c.play.VehicleMoveS2CPacket
import net.minecraft.util.Hand

object BoatFly : Module("BoatFly", "Allows you fly using boats.", Category.MOTION) {
    private val speed = setting("Speed").number(200.0, 0.0, 1000.0, 1.0)
    private val vertSpeed = setting("Vertical").number(100.0, 0.0, 800.0, 1.0)
    private val hoverSpeed = setting("Hover").number(10.0, 0.0, 20.0, 1.0)
    private val copyYaw = setting("CopyYaw").bool(true)
    private val forceInteract = setting("ForceInteract").bool()
    private val interactDelay = setting("InteractDelay").number(10.0, 0.0, 20.0, 1.0)
    private val packetCancel = setting("PacketCancel").bool()

    private var boat: BoatEntity? = null

    override fun onDisable() {
        boat?.setNoGravity(false)
    }

    @Subscribe
    private fun onPacketSend(event: EventPacket.Send) {
        if (mc.player == null) return
        cPlayer.apply {
            if (forceInteract.toggled && this.vehicle != null && this.vehicle is BoatEntity) {
                if (event.packet is LookAndOnGround || event.packet is PlayerInputC2SPacket) event.canceled =
                    true
                if (event.packet is VehicleMoveC2SPacket) {
                    if (this.age % interactDelay.value == 0.0) cInteractManager.interactEntity(
                        this, this.vehicle, Hand.MAIN_HAND
                    )
                }
            }
        }
    }

    @Subscribe
    private fun onPacketReceive(event: EventPacket.Receive) {
        if (event.packet is VehicleMoveS2CPacket && cPlayer.vehicle is BoatEntity && packetCancel.toggled) event.canceled =
            true
    }

    @Subscribe
    private fun onWorldRender(event: EventRender.World) {
        if (boat != null && cPlayer.vehicle != null && cPlayer.vehicle == boat && copyYaw.toggled) {
            boat!!.yaw = cPlayer.yaw
            boat!!.pitch = cPlayer.pitch
        }
    }

    @Subscribe
    private fun onPlayerTravel(event: EventPlayerTravel) {
        if (mc.player != null && cPlayer.vehicle != null && cPlayer.vehicle is BoatEntity) {
            boat = cPlayer.vehicle as BoatEntity
            val factor = 65
            val vel = getMovementOnKey(speed.value / factor, vertSpeed.value / factor)
            val velY =
                if (mc.options.sprintKey.isPressed) -vertSpeed.value / factor
                else vel.getY() -
                    if (boat!!.isWet && !boat!!.blockStateAtPos.isAir) 0.0
                    else hoverSpeed.value / factor
            boat!!.setNoGravity(true)
            boat!!.setVelocityClient(vel.getX(), velY, vel.getZ())
        }
    }
}