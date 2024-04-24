package me.offeex.bloomware.client.module.motion

import me.offeex.bloomware.Bloomware.mc
import me.offeex.bloomware.api.helper.cPlayer
import me.offeex.bloomware.client.module.Module
import me.offeex.bloomware.client.setting.setting
import me.offeex.bloomware.event.dispatcher.Subscribe
import me.offeex.bloomware.event.events.EventMovement
import me.offeex.bloomware.event.events.EventPacket
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket

object Step : Module("Step", "", Category.MOTION) {
    private val mode = setting("Mode").enum("Instant")
    private val bypass = setting("Bypass").enum("Off", "NCP")
    private val height = setting("Height").number(1, 1, 3, 0.1)
    private val speed = setting("Speed").number(1, 0.2, 2, 0.01).depend(mode) { mode.like("Spider") }

    private val autoPause = setting("AutoPause").group()
    val pauseOnSurround = autoPause.setting("Surround").bool(true)

    private val autoDisable = setting("AutoDisable").group()
    val disableOnSurround = autoDisable.setting("Surround").bool(true)

    private var wasHorizontalCollision = false
    private var cancel = false
    private var packets: MutableList<PlayerMoveC2SPacket.PositionAndOnGround> = mutableListOf()

    private val ncpOffsets = hashMapOf(
        0.875 to arrayOf(0.39, 0.7, 0.875),
        1.0 to arrayOf(0.42, 0.75),
        1.5 to arrayOf(0.42, 0.78, 0.63, 0.51, 0.9, 1.21, 1.45, 1.43),
        2.0 to arrayOf(0.425, 0.821, 0.699, 0.599, 1.022, 1.372, 1.652, 1.869, 2.019, 1.919)
    )

    init {
        mode.selectedUpdateBus.subscribe { oldValue, _ -> if (oldValue == "Instant") mc.player?.stepHeight = 0.6f }
    }

    @Subscribe
    private fun onPacketSend(event: EventPacket.Send) {
        if (event.shift || !bypass.like("NCP")) return

        val p = event.packet
        if (p is PlayerMoveC2SPacket.PositionAndOnGround) {
            if (cancel && mode.like("Spider") && !packets.contains(p)) {
                event.canceled = true
                return
            }
        }
    }

    @Subscribe
    private fun onStep(event: EventMovement.Step) {
        if (bypass.like("NCP")) {
            val key = ncpOffsets.keys.find { it <= event.vec3d.y } ?: return
            val offsets = ncpOffsets[key] ?: return

            for (offset in offsets) sendPacket(PlayerMoveC2SPacket.PositionAndOnGround(
                cPlayer.x,
                cPlayer.y + offset,
                cPlayer.z,
                false
            ))
        }
    }

    @Subscribe
    private fun onIsClimbing(event: EventMovement.IsClimbing) {
        if (mode.like("Spider")) {
            if (cPlayer.horizontalCollision) {
                event.climbing = true
                if (!wasHorizontalCollision) {
                    cancel = true
                    val key = ncpOffsets.keys.find { it <= 1.0 } ?: return
                    val offsets = ncpOffsets[key] ?: return
                    offsetPackets(offsets)
                }
            } else if (wasHorizontalCollision && !cPlayer.horizontalCollision) cancel = false
        }
        wasHorizontalCollision = cPlayer.horizontalCollision
    }

//    @Subscribe
//    private fun onClimbing(event: EventMove.Climbing) {
//        if (mode.like("Spider")) {
//            if (cPlayer.horizontalCollision) {
//                event.vec3d = event.vec3d.withAxis(Direction.Axis.Y, speed.value)
//                stepped = true
//            }
//
//            if (stepped && !cPlayer.horizontalCollision) {
//                event.vec3d = event.vec3d.withAxis(Direction.Axis.Y, 0.0)
//            }
//
//            stepped = cPlayer.horizontalCollision
//        }
//    }

    override fun onTick() {
        if (mode.like("Instant")) {
            cPlayer.stepHeight =
                if (!cPlayer.isOnGround || cPlayer.isSubmergedInWater || cPlayer.isInLava) 0.6f
                else height.value.toFloat()
        }
    }

    override fun onDisable() {
        cPlayer.stepHeight = 0.6f
    }

    private fun offsetPackets(offsets: Array<Double>) {
        packets.clear()
        packets.addAll(offsets.map { PlayerMoveC2SPacket.PositionAndOnGround(cPlayer.x, cPlayer.y + it, cPlayer.z, false) })
        packets.forEach {
            println("Intented: ${it.getX(0.0)} ${it.getY(0.0)} ${it.getZ(0.0)} ${it.isOnGround}")
            sendPacket(it)
        }
    }
}