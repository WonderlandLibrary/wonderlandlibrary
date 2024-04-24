package me.offeex.bloomware.client.module.motion

import me.offeex.bloomware.Bloomware.mc
import me.offeex.bloomware.api.helper.Stopwatch
import me.offeex.bloomware.api.helper.cPlayer
import me.offeex.bloomware.api.helper.cWorld
import me.offeex.bloomware.api.manager.managers.SessionManager
import me.offeex.bloomware.api.util.CPlayerUtil
import me.offeex.bloomware.api.util.MathUtil
import me.offeex.bloomware.api.util.MovementUtil
import me.offeex.bloomware.api.util.MovementUtil.getMovementOnKey
import me.offeex.bloomware.client.module.Module
import me.offeex.bloomware.client.setting.setting
import me.offeex.bloomware.event.dispatcher.Subscribe
import me.offeex.bloomware.event.events.EventMovement
import me.offeex.bloomware.event.events.EventPacket
import net.minecraft.block.Blocks
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d


object Speed : Module("Speed", "Allows you to go faster.", Category.MOTION) {
    private val mode = setting("Mode").enum("Strafe", "OnGround")

    /* *************** Strafe settings *************** */

    private val speedStrafe = setting("Speed").number(20.6856, 16, 40, 0.01).depend(mode) { mode.like("Strafe") }
    private val maxSpeed = setting("MaxSpeed").number(40, 30, 60, 0.01).depend(mode) { mode.like("Strafe") }

    private val autoJump = setting("Jump").group(true).depend(mode) { mode.like("Strafe") }
    private val jumpStrength = autoJump.setting("Strength").number(0.3999999463558197, 0.35, 0.45, 0.0001)
    private val jumpAcceleration = autoJump.setting("Acceleration").number(1.6835, 1.3, 2.6, 0.0001)
    private val jumpStrictAccelerate = autoJump.setting("Strict").bool(true)

    private val ignoreFriction = setting("IgnoreFriction").bool(false).depend(mode) { mode.like("Strafe") }

    /* *************** OnGround settings *************** */

    private val factorOnGround = setting("Factor").number(2.149, 1.5, 2.5, 0.001).depend(mode) { mode.like("OnGround") }

    /* *************** General settings *************** */

    private val potionFactor = setting("PotionFactor").bool(true)

    private val useTimer = setting("UseTimer").bool()
    private val autoSprint = setting("AutoSprint").bool()

    private val boost = setting("Boost").group()
    private val boostReduce = boost.setting("Reduce").number(0.2, 0, 1, 0.01)
    private val maxBoost = boost.setting("Max").number(0.1, 0, 0.2, 0.001)

    private val setbackCooldown = setting("SetbackCooldown").number(0.05, 0, 1, 0.01)


    /* *************** Strafe *************** */

    private var strafeStage: Stage = Stage.JUMP
    private var moveSpeed = 0.0
    private var boostSpeed = 0.0
    private var accelerate = false

    private val boostStopwatch = Stopwatch()
    private val setbackStopwatch = Stopwatch()

    /* *************** OnGround *************** */

    private var groundStage = Stage.JUMP

    /* *************** General *************** */

    private var oldTimer = 1f


    init {
        useTimer.toggledUpdateBus.subscribe { _, newValue ->
            if (newValue) oldTimer = SessionManager.timer
            else SessionManager.timer = oldTimer
        }
    }

    override fun onEnable() {
        oldTimer = SessionManager.timer
    }

    override fun onDisable() {
        resetProgress()
        SessionManager.timer = oldTimer
    }

    @Subscribe
    private fun onMove(event: EventMovement.Move) {
        if (!handleIdling(event) || !canMoveOn()) return

        if (autoSprint.toggled) cPlayer.isSprinting = true

        val baseSpeed = getBaseSpeed()
        val hSpeed = SessionManager.horizontalSpeed()

        if (!setbackStopwatch.passed(setbackCooldown.value * 1000) && setbackCooldown.value != 0.0) {
            event.velocity = Vec3d.ZERO
            return resetProgress()
        }


        // PEAK stage, we are falling and reducing our speed back to base speed
        fun fallAndSlowdown() {
            // take into account our last tick's move speed
            val scaledStrafeSpeed = 0.66 * (hSpeed - baseSpeed)
            moveSpeed = hSpeed - scaledStrafeSpeed
        }

        // Util method which checks if player is hitting the ground or roof
        fun hittingVertically(offsetY: Double) = !cWorld.isSpaceEmpty(null, cPlayer.boundingBox.offset(0.0, offsetY, 0.0)) || cPlayer.verticalCollision

        // Retrieve and up
        // date motion
        fun updateMotion() {

            // boost handling
            moveSpeed += boostSpeed.coerceAtMost(maxBoost.value)
            boostSpeed = 0.0

            // do not allow movements slower than base speed
            moveSpeed = moveSpeed.coerceAtLeast(baseSpeed)

            val motion = getMovementOnKey(moveSpeed)
            event.velocity = Vec3d(motion.x, event.velocity.y, motion.z)
        }


        if (mode.like("Strafe")) {

            if (useTimer.toggled && SessionManager.timer == 1f) SessionManager.timer = 1.088f

            if (autoJump.toggled || mc.options.jumpKey.isPressed) {
                when (strafeStage) {

                    // start the motion
                    Stage.START -> {
                        moveSpeed = 1.35 * baseSpeed - 0.01
                        strafeStage = Stage.JUMP
                    }

                    // start jumping
                    Stage.JUMP -> {
                        var jumpSpeed = jumpStrength.value

                        if (potionFactor.toggled) jumpSpeed += getJumpSpeed()

                        // jump
                        cPlayer.velocity = cPlayer.velocity.withAxis(Direction.Axis.Y, jumpSpeed)
                        event.velocity = event.velocity.withAxis(Direction.Axis.Y, jumpSpeed)

                        // if we jumped and can accelerate, do it
                        moveSpeed *=
                            if (accelerate || !jumpStrictAccelerate.toggled) jumpAcceleration.value
                            else 1.395
                        strafeStage = Stage.PEAK
                    }

                    // peak of the jump, acceleration is slowing down
                    Stage.PEAK -> {
                        fallAndSlowdown()
                        accelerate = !accelerate
                        strafeStage = Stage.FALL
                    }

                    // Hitting the ground or roof
                    Stage.FALL -> {
                        // if we hit the roof or ground, reset the stage
                        if (hittingVertically(cPlayer.velocity.y)) strafeStage = Stage.JUMP

                        // collision speed
                        moveSpeed = hSpeed - (hSpeed / 159)
                    }
                }
            }

            moveSpeed = moveSpeed.coerceAtMost(MathUtil.fromKmH(maxSpeed.value))

            updateMotion()

        } else if (mode.like("OnGround") && cPlayer.isOnGround) {

            when (groundStage) {
                Stage.JUMP -> {
                    moveSpeed *= factorOnGround.value
                    groundStage = Stage.PEAK
                }

                Stage.PEAK -> {
                    fallAndSlowdown()
                    // we need to "jump" again now
                    groundStage = Stage.JUMP
                }

                else -> {}
            }

            updateMotion()
        }
    }

    @Subscribe
    private fun onPacketReceive(event: EventPacket.Receive) {

        // Boosting speed if player is hit by explosion or projectile
        fun boost(velX: Double, velZ: Double) {
            val newBoostSpeed = MovementUtil.kbInDir(velX, velZ)

            // if the velocity is greater than the current boost speed, set it as the new boost speed
            if (newBoostSpeed > boostSpeed) {
                boostSpeed = newBoostSpeed * (1 - boostReduce.value)
                boostStopwatch.reset()
            }
        }

        when (val packet = event.packet) {
            is PlayerPositionLookS2CPacket -> {
                setbackStopwatch.reset()
            }

            is ExplosionS2CPacket -> {
                boost(packet.playerVelocityX.toDouble(), packet.playerVelocityZ.toDouble())
            }

            is EntityVelocityUpdateS2CPacket -> {
                if (packet.id != cPlayer.id) return
                boost(packet.velocityX / 8000.0, packet.velocityZ / 8000.0)
            }
        }
    }


    private fun resetProgress() {
        strafeStage = Stage.FALL
        groundStage = Stage.JUMP

        moveSpeed = 0.0
        boostSpeed = 0.0
    }

    // Resetting speed if player is not moving
    private fun handleIdling(event: EventMovement.Move): Boolean {
        return if (cPlayer.forwardSpeed == 0f && cPlayer.sidewaysSpeed == 0f) {
            event.velocity = cPlayer.velocity.multiply(0.0, 1.0, 0.0)
            cPlayer.velocity = event.velocity
            false
        } else true
    }

    private fun canMoveOn(): Boolean {
        // Retrieving values
        val inLiquid = CPlayerUtil.isInLiquid()
        val inWeb = cPlayer.blockStateAtPos.isOf(Blocks.COBWEB)
        val flying = cPlayer.isFallFlying || cPlayer.abilities.flying || cPlayer.fallDistance > 2
        val climbing = cPlayer.isClimbing

        val notAllowed = inLiquid || inWeb || flying || climbing

        // Won't allow player to use speed if he is in liquid, web, flying or climbing
        if (ignoreFriction.toggled && notAllowed) {
            resetProgress()
            return false
        }

        // Deny if incompatible modules are enabled
        return !(Flight.enabled || BoatFly.enabled)
    }

    private fun getBaseSpeed() = run {
        var defaultSpeed = MathUtil.fromKmH(speedStrafe.value)

        apply { defaultSpeed *= getSpeedBoost(getAmplifier(StatusEffects.SPEED) ?: return@apply) }
        apply { defaultSpeed /= getSpeedBoost(getAmplifier(StatusEffects.SLOWNESS) ?: return@apply) }

        defaultSpeed
    }

    private fun getSpeedBoost(amp: Int?) = run {
        if (amp == null) 1.0
        else 1.0 + 0.2 * (amp + 1)
    }

    private fun getJumpSpeed(): Double {
        val speed = getAmplifier(StatusEffects.JUMP_BOOST) ?: return 0.0
        return (speed + 1) * 0.1
    }

    private fun getAmplifier(eff: StatusEffect) = cPlayer.getStatusEffect(eff)?.amplifier

    private enum class Stage { START, JUMP, PEAK, FALL }
}