package me.offeex.bloomware.client.module.motion

import me.offeex.bloomware.Bloomware.mc
import me.offeex.bloomware.api.helper.cPlayer
import me.offeex.bloomware.api.helper.cWorld
import me.offeex.bloomware.api.util.MovementUtil.getMovementOnKey
import me.offeex.bloomware.client.module.Module
import me.offeex.bloomware.client.setting.setting
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.PositionAndOnGround

object Flight : Module("Flight", "Allows you to fly.", Category.MOTION) {
    private val mode = setting("Mode").enum("Vanilla", "Static", "Jetpack")
    val speed = setting("Speed").number(120.0, 10.0, 750.0, 1.0)
    val vertSpeed = setting("Vertical").number(100.0, 10.0, 500.0, 1.0)
    private val antiKick = setting("AntiKick").enum("Velocity", "Packet", "Off")
    private val kickDelay = setting("KickDelay").number(1.0, 1.0, 20.0, 1.0)
    private val kickStep = setting("KickStep").number(0.032, 0.01, 0.5, 0.01)

    private var oldFlying = false

    override fun onTick() {
        cPlayer.apply {
            when (mode.selected) {
                "Vanilla" -> {
                    abilities.flying = true
                    abilities.flySpeed = (this@Flight.speed.value / 783.97).toFloat()
                }

                "Static" -> {
                    abilities.flying = false
                    velocity = getMovementOnKey(this@Flight.speed.value / 72.14, vertSpeed.value / 72.14)
                }

                "Jetpack" -> {
                    abilities.flying = false
                    val motion = getMovementOnKey(this@Flight.speed.value / 783.97, vertSpeed.value / 783.97)
                    addVelocity(
                        motion.x, if (mc.options.sneakKey.isPressed) motion.y * 0.5 else motion.y, motion.z
                    )
                }
            }
            if (!mode.like("Jetpack") && shouldAntiKick()) {
                if (antiKick.like("Velocity")) velocity = velocity.subtract(0.0, kickStep.value, 0.0)
                else if (antiKick.like("Packet")) sendPacket(PositionAndOnGround(x, y - kickStep.value, z, false))
            }
        }
    }

    override fun onEnable() {
        oldFlying = cPlayer.abilities.flying
    }

    override fun onDisable() {
        cPlayer.abilities.flying = oldFlying
        cPlayer.abilities.flySpeed = 0.05f
    }

    private fun shouldAntiKick() = cPlayer.run {
        age % kickDelay.value == 0.0 && !antiKick.like("Off") && !isTouchingWater && !isInLava && !isOnGround && cWorld.getBlockState(
            blockPos.down()
        ).isAir
    }
}