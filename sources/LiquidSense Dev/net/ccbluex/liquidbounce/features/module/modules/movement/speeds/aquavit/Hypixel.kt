package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aquavit;

import me.AquaVit.liquidSense.API.Particles.roundToPlace
import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.EventState
import net.ccbluex.liquidbounce.event.JumpEvent
import net.ccbluex.liquidbounce.event.MotionEvent
import net.ccbluex.liquidbounce.event.MoveEvent
import net.ccbluex.liquidbounce.features.module.modules.combat.AutoPot
import net.ccbluex.liquidbounce.features.module.modules.movement.Speed
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode
import net.ccbluex.liquidbounce.utils.MovementUtils.*
import org.lwjgl.input.Keyboard
import java.lang.Math.max
import java.lang.Math.sqrt
import java.util.*


class Hypixel : SpeedMode("Hypixel") {
    var stage = 0
    var speed = 0.0
    private var lastDist = 0.0

    override fun onEnable() {
        stage = 0
    }

    override fun onDisable() {

    }

    override fun onJump(e: JumpEvent) {
        if (mc.thePlayer != null && !mc.thePlayer.isSneaking)
            e.cancelEvent()
    }

    override fun onMotion(event: MotionEvent) {
        when (event.eventState) {
            EventState.PRE -> {
                val xDist: Double = mc.thePlayer.posX - mc.thePlayer.lastTickPosX
                val zDist: Double = mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ
                lastDist = sqrt(xDist * xDist + zDist * zDist)
            }

            EventState.POST -> {
                if (isMoving() && isOnGround(0.01)) {
                    mc.thePlayer.motionY -= 2.4e-6
                }
            }
        }
    }

    override fun onUpdate() {
    }

    override fun onMove(e: MoveEvent) {
        val speedModule = LiquidBounce.moduleManager.getModule(Speed::class.java) as Speed?

        when {
            speedModule!!.oldHypixel.get() -> {
                when (stage) {
                    1 -> if (isMoving() && mc.thePlayer.onGround) {
                        speed = getBaseMoveSpeed()
                    }

                    2 -> {
                        if (isMoving() && mc.thePlayer.onGround) {
                            //stage = 0
                            e.y = getJumpBoostModifier(speedModule.motionYValue.get().toDouble(), true).also { mc.thePlayer.motionY = it }
                        }

                        speed = speedModule.movementSpeedValue.get() * getBaseMoveSpeed()
                    }

                    3 -> {
                        speed = lastDist - 0.66 * (lastDist - getBaseMoveSpeed())
                    }

                    else -> {
                        if (mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.entityBoundingBox.offset(0.0, mc.thePlayer.motionY, 0.0)).size > 0
                                || mc.thePlayer.isCollidedVertically && stage > 0)
                            stage = if (isMoving()) 1 else 0

                        speed = lastDist - lastDist / 99
                    }
                }
                speed = max(speed, getBaseMoveSpeed())
                setMotion(e, speed, 1.0)

                if (isMoving()) {
                    ++stage
                }
            }
            else -> {
                if (isMoving()) {
                    if (mc.thePlayer.onGround && !mc.thePlayer.isInWater) {
                        stage = 0
                        e.y = getJumpBoostModifier(speedModule.motionYValue.get().toDouble(), true).also { mc.thePlayer.motionY = it }
                    }

                    ++stage
                } else stage = 0

                val speed = (getHypixelSpeed(stage) + 0.0331) * roundToPlace(speedModule.movementSpeedValue.get(), 2) * 0.98
                setMotion(e, speed, 1.0)
            }
        }
    }

    private fun getHypixelSpeed(stage: Int): Double {
        var speed = 0.64 + getSpeedEffect() * 0.125
        val firstValue = 0.4145 + getSpeedEffect() / 12.5 // speed = 0.417
        val decr = stage / 150.0
        if (stage == 1) {
            speed = firstValue
        } else if (stage >= 2) {
            speed = firstValue - decr
        }
        return max(speed, getBaseMoveSpeed() + getSpeedEffect() * 0.028)
    }
}