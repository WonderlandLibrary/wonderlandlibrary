package me.offeex.bloomware.client.module.motion

import me.offeex.bloomware.Bloomware.mc
import me.offeex.bloomware.api.helper.Stopwatch
import me.offeex.bloomware.api.helper.cInteractManager
import me.offeex.bloomware.api.helper.cPlayer
import me.offeex.bloomware.api.helper.cWorld
import me.offeex.bloomware.api.manager.managers.SessionManager
import me.offeex.bloomware.api.util.CPlayerUtil.isMoving
import me.offeex.bloomware.api.util.CPlayerUtil.isMovingH
import me.offeex.bloomware.api.util.ChatUtil
import me.offeex.bloomware.api.util.ClientUtil.isActuallyPressed
import me.offeex.bloomware.api.util.MovementUtil.getMovementOnKey
import me.offeex.bloomware.api.util.MovementUtil.getVelocity
import me.offeex.bloomware.client.module.Module
import me.offeex.bloomware.client.setting.setting
import me.offeex.bloomware.client.setting.settings.SettingNumber
import me.offeex.bloomware.event.dispatcher.Subscribe
import me.offeex.bloomware.event.events.EventPacket
import me.offeex.bloomware.event.events.EventPlayerTravel
import me.offeex.bloomware.mixin.accessor.IClientPlayerEntity
import me.offeex.bloomware.mixin.accessor.IPlayerMoveC2SPacket
import me.offeex.bloomware.mixin.accessor.IPlayerPositionLookS2CPacket
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.MovementType
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.LookAndOnGround
import net.minecraft.network.packet.s2c.play.EntityTrackerUpdateS2CPacket
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket
import net.minecraft.screen.slot.SlotActionType
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

object ElytraFly : Module(
    "ElytraFly",
    "Allows you to fly using elytra almost without fireworks.",
    Category.MOTION
) {
    private val mode = setting("Mode").enum("Control", "Packet", "Boost")

    //  Control, Packet modes
    private val speedLimit = setting("SpeedLimit").number(260, 1, 500).depend(mode) { !mode.like("Boost") }
    private val verticalSpeed = setting("Down").number(72, 1, 360, 1).depend(mode) { !mode.like("Boost") }
    private val hoverSpeed = setting("Hover").number(0, 0, 10, 0.1).depend(mode) { !mode.like("Boost") }
    private val spoofPitch = setting("SpoofPitch").group(true).depend(mode) { !mode.like("Boost") }
    private val pitchValue = spoofPitch.setting("Pitch").number(-3, -40, 40, 0.01)
    private val accelerate = setting("Accelerate").group(true).depend(mode) { !mode.like("Boost") }
    private val startSpeed = accelerate.setting("StartSpeed").number(0, 0, 500, 1)
    private val accelerationRate = accelerate.setting("Rate").number(3, 0, 25, 0.1)

    //  Packet mode
    private val frequencyPacket = setting("Frequency").number(1, 1, 20, 1).depend(mode) { mode.like("Packet") }
    private val cancelAnimation = setting("CancelAnimation").bool().depend(mode) { mode.like("Packet") }

    //  Boost mode
    private val boost = setting("Boost").number(0.05, 0, 1, 0.01).depend(mode) { mode.like("Boost") }
    private val maxBoost = setting("MaxBoost").number(2.5, 0, 5, 0.01).depend(mode) { mode.like("Boost") }

    //  Control, Boost modes
    private val takeoff = setting("Takeoff").group(true).depend(mode) { !mode.like("Packet") }
    private val takeoffDelay = takeoff.setting("Delay").number(0.75, 0.1, 2, 0.01)
    private val takeoffShift = takeoff.setting("ShiftY").number(-0.01, -0.2, 0, 0.001)
    private val redeploy = setting("Redeploy").group(true).depend(mode) { !mode.like("Packet") }
    private val redeployMode = redeploy.setting("Mode").enum("Packet", "SlotSwap")
    private val redeployFrequency = redeploy.setting("Frequency").number(1, 0.1, 2, 0.01)
    private val redeployShift = redeploy.setting("ShiftY").number(0.030, -0.25, 0.25, 0.001)
    private val holdInAir = setting("HoldInAir").bool()
    private val attempts = redeploy.setting("Attempts").number(3, 1, 8, 1)
    private val interval = redeploy.setting("Interval").number(4, 0, 15, 0.1)
    private val timeOut = redeploy.setting("TimeOut").number(7.5, 0, 15, 0.1)
    private val useTimer = redeploy.setting("UseTimer").bool(true)
    private val debug = redeploy.setting("Debug").bool()

    private var attemptsCounter = 0
    private var prevJumpKeyPressed = false
    private var cancelMovement = false
    private var takeBack = false
    private var allowTakeoff = true
    private var startTimeOut = false
    private val stopwatchTakeOff = Stopwatch()
    private val stopwatchRedeploy = Stopwatch()
    private val stopwatchInterval = Stopwatch()
    private val stopwatchTimeOut = Stopwatch()

    @Subscribe
    private fun onPacketSend(event: EventPacket.Send) {
        if (event.packet is LookAndOnGround && mode.like("Packet")) (event.packet as IPlayerMoveC2SPacket).setPitch(
            .5f
        ) /* For Packet mode */

        /* For Control mode */
        if (mode.like("Control")) {
            if (event.packet is PlayerMoveC2SPacket) {
                val packet = event.packet
                if (cancelMovement && cPlayer.isOnGround == (cPlayer as IClientPlayerEntity).lastOnGround && shouldTakeOff() && cPlayer.velocity.getY() < 0 || cPlayer.isFallFlying && !isMoving) {
                    event.canceled = true
                    if (cPlayer.isFallFlying) cancelMovement = false
                }
                if (isMovingH && !mc.options.jumpKey.isActuallyPressed() && spoofPitch.toggled && cPlayer.isFallFlying) (packet as IPlayerMoveC2SPacket).setPitch(
                    pitchValue.value.toFloat()
                )
            } else if (event.packet is ClientCommandC2SPacket) {
                if (!allowTakeoff && event.packet.mode == ClientCommandC2SPacket.Mode.START_FALL_FLYING) event.canceled =
                    true
            }
        }
    }

    @Subscribe
    private fun onPacketReceive(event: EventPacket.Receive) {
        if (mc.player == null) return
        if (cPlayer.isSpectator || !chest.isOf(Items.ELYTRA) || elytraDurability <= 1 || !cPlayer.isFallFlying && !cPlayer.abilities.flying || mode.like(
                "Boost"
            )
        ) return
        if (event.packet is PlayerPositionLookS2CPacket && !mode.like("Packet") && !mc.options.jumpKey.isActuallyPressed()) (event.packet as IPlayerPositionLookS2CPacket).setPitch(
            cPlayer.pitch
        )

        /* This supposed to cancel FallFlying animation */
        if (event.packet is EntityTrackerUpdateS2CPacket && mode.like("Packet") && cancelAnimation.toggled) {
            val packet = event.packet
            if (packet.id() == cPlayer.id) event.canceled = true
        }
    }

    override fun onDisable() {
        if (mode.like("Packet") && !cPlayer.isFallFlying) sendPacket(
            ClientCommandC2SPacket(
                cPlayer, ClientCommandC2SPacket.Mode.START_FALL_FLYING
            )
        )
        SessionManager.timer = 1f
    }

    @Subscribe
    private fun onPlayerTravel(event: EventPlayerTravel) {
        if (mc.player == null || cPlayer.isSpectator || cPlayer.hasVehicle()) {
            SessionManager.timer = 1f
            return
        }

        /* Redeploy limitations (Control, Boost) */
        if (allowTakeoff) {
            if (!stopwatchInterval.passed(interval.value * 1000f)) {
                if (attemptsCounter >= attempts.value) {
                    allowTakeoff = false
                    startTimeOut = true
                }
            } else {
                stopwatchInterval.reset()
                attemptsCounter = 0
            }
        } else if (startTimeOut && stopwatchTimeOut.passed(timeOut.value * 1000f)) {
            allowTakeoff = true
            startTimeOut = false
            attemptsCounter = 0
            stopwatchTimeOut.reset()
        }

        if (cPlayer.velocity.y < 0 && holdInAir.toggled && !cPlayer.isFallFlying && takeBack) cPlayer.velocity = Vec3d.ZERO

        /* Takeoff and Redeploy (Control, Boost) */
        if (allowTakeoff) {
            if (shouldTakeOff() && stopwatchTakeOff.passed(takeoffDelay.value * 1000f))
                takeoff(event, false)
            else if (shouldRedeploy() && stopwatchRedeploy.passed(redeployFrequency.value * 1000f)) {
                if (redeployMode.like("Packet")) takeoff(event, true)
                else if (redeployMode.like("SlotSwap")) redeploySlotSwap(event)
            } else SessionManager.timer = 1f
        } else SessionManager.timer = 1f

        /* Modes implementation */
        if (!elytraCheck()) return

        when (mode.selected) {
            "Control" -> {
                if (!mc.options.jumpKey.isPressed && cPlayer.isFallFlying) {
                    cPlayer.abilities.flying = false
                    setMotion(event)
                }
            }

            "Packet" -> {
                if (cPlayer.isOnGround || (!cWorld.getBlockState(cPlayer.blockPos.down()).isAir && cPlayer.pos.getY() - cPlayer.blockPos.down().y < 1.1)) return
                setMotion(event)
                if (cPlayer.age % (21 - frequencyPacket.value) == 0.0) sendPacket(
                    ClientCommandC2SPacket(
                        cPlayer, ClientCommandC2SPacket.Mode.START_FALL_FLYING
                    )
                )
                cPlayer.fallDistance = 0f
            }

            "Boost" -> {
                if (cPlayer.isFallFlying && getVelocity(cPlayer) <= maxBoost.value) {
                    val yawRadian = Math.toRadians(cPlayer.yaw.toDouble())
                    if (mc.options.forwardKey.isPressed && cPlayer.pitch > -3) cPlayer.addVelocity(
                        sin(yawRadian) * -boost.value, 0.0, cos(yawRadian) * boost.value
                    )
                    else if (mc.options.backKey.isPressed) cPlayer.addVelocity(
                        sin(yawRadian) * boost.value, 0.0, cos(yawRadian) * -boost.value
                    )
                }
            }
        }

        prevJumpKeyPressed = mc.options.jumpKey.isActuallyPressed()
        if (event.canceled) cPlayer.move(MovementType.SELF, cPlayer.velocity)
    }

    private fun shouldTakeOff() =
        (!cPlayer.isFallFlying && !cPlayer.isOnGround && !cPlayer.abilities.flying && !cPlayer.isSubmergedInWater && !cPlayer.isInsideWaterOrBubbleColumn && !mode.like(
            "Packet"
        )) && cPlayer.pos.getY() - cPlayer.blockPos.down().y >= 0.875 && takeoff.toggled

    private fun shouldRedeploy(): Boolean {
        return cPlayer.isFallFlying
            && prevJumpKeyPressed
            && !mc.options.jumpKey.isActuallyPressed()
            && !mode.like("Packet")
            && redeploy.toggled
    }

    private fun shouldUseTimer(): Boolean {
        return useTimer.toggled && !cPlayer.abilities.flying && !cPlayer.isCreative
    }

    private fun takeoff(event: EventPlayerTravel, redeploy: Boolean) {
        if (takeBack) {
            cInteractManager.clickSlot(
                cPlayer.currentScreenHandler.syncId, 6, 0, SlotActionType.PICKUP, cPlayer
            )
            takeBack = false
        }
        if (!elytraCheck()) return
        val groundPos = cPlayer.blockPos.down()
        cancelMovement = true
        sendPacket(
            LookAndOnGround(
                cPlayer.yaw,
                if (spoofPitch.toggled) pitchValue.value.toFloat() else cPlayer.pitch,
                cPlayer.isOnGround
            )
        )
        if (cPlayer.velocity.y < 0) {
            addMessage(if (redeploy) "Redeploying via packet.." else "Taking off.")
            hold(event, if (redeploy) redeployShift.value else takeoffShift.value)
            if (shouldUseTimer() && cPlayer.pos.getY() - groundPos.y > 0.875 && !mc.isInSingleplayer)
                SessionManager.timer = 0.125f
            sendPacket(ClientCommandC2SPacket(cPlayer, ClientCommandC2SPacket.Mode.START_FALL_FLYING))
            attemptsCounter++
            stopwatchTakeOff.reset() /* For takeOffDelay */
        }
    }

    private fun redeploySlotSwap(event: EventPlayerTravel) {
        addMessage("Redeploying via slotswap..")
        hold(event, redeployShift.value)
        cInteractManager.clickSlot(
            cPlayer.currentScreenHandler.syncId, 6, 0, SlotActionType.PICKUP, cPlayer
        )
        takeBack = true
        stopwatchTakeOff.reset()
        stopwatchRedeploy.reset()
    }

    private fun hold(event: EventPlayerTravel, hoverSpeed: Double) {
        event.canceled = true
        cPlayer.setVelocity(0.0, hoverSpeed, 0.0)
    }

    private val elytraDurability: Int
        get() = chest.maxDamage - chest.damage
    private val chest: ItemStack
        get() = cPlayer.getEquippedStack(EquipmentSlot.CHEST)

    private fun elytraCheck(): Boolean {
        return chest.isOf(Items.ELYTRA) && elytraDurability > 1
    }

    private fun addMessage(text: String) {
        if (debug.toggled) ChatUtil.addMessage(text)
    }

    private fun setMotion(event: EventPlayerTravel) {
        val motion = getMovementOnKey(speedGained.amplify(), verticalSpeed.amplify())
        cPlayer.velocity = motion.subtract(0.0, hoverSpeed.amplify(), 0.0)
        event.canceled = true
    }

    private var speedGained: Double = 0.0
        get() {
            val min = min(startSpeed.value, speedLimit.value)
            field =
                if (!isMovingH) 0.0
                else if (!accelerate.toggled) speedLimit.value
                else MathHelper.clamp(field + accelerationRate.value, min, speedLimit.value)
            return field
        }

    private fun Double.amplify() = this / 72
    private fun SettingNumber.amplify() = value.amplify()
}
