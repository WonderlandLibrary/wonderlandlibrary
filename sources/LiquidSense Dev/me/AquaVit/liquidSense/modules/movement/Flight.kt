/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package me.AquaVit.liquidSense.modules.movement

import me.AquaVit.liquidSense.utils.MoveHackUtil
import net.ccbluex.liquidbounce.event.*
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.utils.ClientUtils
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.block.BlockAir
import net.minecraft.entity.player.PlayerCapabilities
import net.minecraft.network.play.client.C03PacketPlayer
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition
import net.minecraft.network.play.server.S08PacketPlayerPosLook
import net.minecraft.potion.Potion
import net.minecraft.util.AxisAlignedBB
import net.minecraft.util.MathHelper
import org.lwjgl.input.Keyboard
import java.awt.Color
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin
import kotlin.math.sqrt

@ModuleInfo(
    name = "Flight",
    description = "Allows you to fly in survival mode.",
    category = ModuleCategory.MOVEMENT,
    keyBind = Keyboard.KEY_F
)
class Flight : Module() {
    val modeValue = ListValue(
        "Mode", arrayOf(
            "Vanilla",
            "SmoothVanilla",
            "BoostHypixel"
        ), "Vanilla"
    )

    private val UpTimer = FloatValue("UpTimer", 0.4f, 0.4f, 1f)
    private val fallHeight = FloatValue("fall-Height", 3f, 1f, 4f)

    private val vanillaSpeedValue = FloatValue("VanillaSpeed", 2f, 0f, 5f)
    private val vanillaKickBypassValue = BoolValue("VanillaKickBypass", false)
    private val DisablerValue = BoolValue("Hypixel-DisablerNCP", true)
    private val spoofflyTimeValue = FloatValue("Hypixel-SpoofFlyTimer", 1f, 0f, 2f)
    private val spoofflyValue = IntegerValue("Hypixel-SpoofFlyTick", 100, 0, 500)
    val spoofmodeValue = ListValue(
        "SpoofMode", arrayOf(
            "immobilized",
            "Spring"
        ), "Spring"
    )

    // Hypixel
    private val hypixelBoost = BoolValue("Hypixel-Boost", true)
    private val hypixelBoostDelay = IntegerValue("Hypixel-BoostDelay", 1200, 0, 2000)
    private val hypixelBoostTimer = FloatValue("Hypixel-BoostTimer", 1f, 0f, 5f)

    // Visuals
    private val markValue = BoolValue("Mark", true)
    private var startY = 0.0
    private val flyTimer = MSTimer()
    private val groundTimer = MSTimer()
    private var noPacketModify = false
    private var aacJump = 0.0
    private var noFlag = false
    private var wasDead = false
    private var boostHypixelState = 1
    private var moveSpeed = 0.0
    private var lastDistance = 0.0
    private var failedStart = false
    private var isTimerboost = false
    private var acceptFly = false
    private var fly = 0f
    private var Const = 0


    override fun onEnable() {
        val thePlayer = mc.thePlayer ?: return

        flyTimer.reset()
        noPacketModify = true


        var move = MoveHackUtil()

        run {
            when (modeValue.get().toLowerCase()) {
                "boosthypixel" -> {
                    acceptFly = false
                    mc.timer.timerSpeed = 1f + spoofflyTimeValue.get()
                    val timer = Timer()
                    timer.schedule(object : TimerTask() {
                        override fun run() {
                            var move = MoveHackUtil()
                            val mode = modeValue.get()
                            when (mode.toLowerCase()) {
                                "boosthypixel" -> {

                                    if (DisablerValue.get()) {
                                        for (i in 0..9) {
                                            //Imagine flagging to NCP.
                                            mc.netHandler.addToSendQueue(
                                                    C04PacketPlayerPosition(
                                                            mc.thePlayer!!.posX,
                                                            mc.thePlayer!!.posY,
                                                            mc.thePlayer!!.posZ,
                                                            true
                                                    )
                                            )
                                        }
                                    }

                                    var playerAbilities = PlayerCapabilities()
                                    move.onHypixelFly(modeValue.get(), fallHeight.get())
                                    acceptFly = true
                                    playerAbilities.isFlying = true
                                    boostHypixelState = 1
                                    moveSpeed = 0.1
                                    lastDistance = 0.0
                                    failedStart = false
                                }
                            }
                        }
                    }, spoofflyValue.get().toLong())
                }
            }
        }

        startY = thePlayer.posY
        aacJump = -3.8
        noPacketModify = false

        super.onEnable()
    }


    override fun onDisable() {
        wasDead = false

        val thePlayer = mc.thePlayer ?: return

        noFlag = false

        mc.thePlayer!!.motionX = 0.0.also { mc.thePlayer!!.motionZ = it }
        isTimerboost = false
        thePlayer.capabilities.isFlying = false
        mc.timer.timerSpeed = 1f
        thePlayer.speedInAir = 0.02f
        acceptFly = false
    }

    @EventTarget
    fun onUpdate(event: UpdateEvent?) {
        val vanillaSpeed = vanillaSpeedValue.get()
        val thePlayer = mc.thePlayer!!

        run {
            when (modeValue.get().toLowerCase()) {
                "vanilla" -> {
                    thePlayer.capabilities.isFlying = false
                    thePlayer.motionY = 0.0
                    thePlayer.motionX = 0.0
                    thePlayer.motionZ = 0.0
                    if (mc.gameSettings.keyBindJump.isKeyDown) thePlayer.motionY += vanillaSpeed
                    if (mc.gameSettings.keyBindSneak.isKeyDown) thePlayer.motionY -= vanillaSpeed
                    MovementUtils.strafe(vanillaSpeed)
                    handleVanillaKickBypass()
                }
                "smoothvanilla" -> {
                    thePlayer.capabilities.isFlying = true
                    handleVanillaKickBypass()
                }
            }
        }
    }

    @EventTarget
    fun onMotion(event: MotionEvent) {
        if (modeValue.get().equals("boosthypixel", ignoreCase = true)) {
            if (!acceptFly)
                return

            when (event.eventState) {
                EventState.PRE -> {
                    if (boostHypixelState > 1) mc.thePlayer!!.setPosition(
                        mc.thePlayer!!.posX,
                        mc.thePlayer!!.posY - 0.000016,
                        mc.thePlayer!!.posZ
                    )
                    when (++this.Const) {
                        1 -> {
                            this.fly *= -0.94666665455465f
                        }
                        2, 3, 4 -> {
                            this.fly += 1.45E-3f
                        }
                        5 -> {
                            this.fly += 1.0E-3f
                            this.Const = 0
                        }
                    }
                    mc.thePlayer!!.setPosition(mc.thePlayer!!.posX, mc.thePlayer!!.posY + this.fly, mc.thePlayer!!.posZ)
                    if (boostHypixelState > 1) mc.thePlayer!!.setPosition(
                        mc.thePlayer!!.posX,
                        mc.thePlayer!!.posY + 0.000016,
                        mc.thePlayer!!.posZ
                    )
                    mc.thePlayer!!.setPosition(mc.thePlayer!!.posX, mc.thePlayer!!.posY - 1.0E-16, mc.thePlayer!!.posZ)
                    if (!failedStart) mc.thePlayer!!.motionY = 0.0
                }
                EventState.POST -> {
                    val xDist = mc.thePlayer!!.posX - mc.thePlayer!!.prevPosX
                    val zDist = mc.thePlayer!!.posZ - mc.thePlayer!!.prevPosZ
                    lastDistance = sqrt(xDist * xDist + zDist * zDist)
                }
            }
        }
    }

    fun randomDouble(min: Double, max: Double): Double {
        return MathHelper.clamp_double(min + Random().nextDouble() * max, min, max)
    }

    @EventTarget
    fun onRender3D(event: Render3DEvent?) {
        val mode = modeValue.get()
        if (!markValue.get() || mode.equals("Vanilla", ignoreCase = true) || mode.equals(
                "SmoothVanilla",
                ignoreCase = true
            )
        ) return
        val y = startY + 2.0
        RenderUtils.drawPlatform(
            y,
            if (mc.thePlayer!!.entityBoundingBox.maxY < y) Color(0, 255, 0, 90) else Color(255, 0, 0, 90),
            1.0
        )
    }

    @EventTarget
    fun onPacket(event: PacketEvent) {
        if (noPacketModify || !acceptFly) return

        if (event.packet is C03PacketPlayer) {
            val packetPlayer = event.packet

            val mode = modeValue.get()

            if (mode.equals("Hypixel", ignoreCase = true) || mode.equals(
                    "BoostHypixel",
                    ignoreCase = true
                )
            ) packetPlayer.onGround = false
        }
        if (event.packet is S08PacketPlayerPosLook) {
            val mode = modeValue.get()
            if (mode.equals("BoostHypixel", ignoreCase = true)) {
                failedStart = true
                ClientUtils.displayChatMessage("§8[§c§lBoostHypixel-§a§lFly§8] §cSetback detected.")
                toggle()
            }
        }
    }

    @EventTarget
    fun onMove(event: MoveEvent) {
        when (modeValue.get().toLowerCase()) {
            "boosthypixel" -> {
                val move = MoveHackUtil()
                if (!acceptFly) {
                    if (!mc.thePlayer!!.onGround) {
                        mc.timer.timerSpeed = 1.0f
                    } else {
                        if(spoofmodeValue.get().equals( "immobilized" , true)) {
                            event.x = 0.0
                            event.z = 0.0
                        }
                    }
                    return
                }

                if (!isTimerboost) {
                    mc.timer.timerSpeed = UpTimer.get()
                }

                if (event.y == BigDecimal(event.y).setScale(3, RoundingMode.HALF_UP).toDouble() || failedStart) {
                    mc.timer.timerSpeed = 1.0f

                    if (!failedStart)
                        isTimerboost = true
                }

                if (!MovementUtils.isMoving()) {
                    event.x = 0.0
                    event.z = 0.0
                    return
                }
                if (failedStart)
                    return

                val amplifier = 1 + if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) 0.2 *
                        (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).amplifier + 1.0) else 0.0

                val baseSpeed = 0.29 * amplifier


                val boostDelay = hypixelBoostDelay.get()
                if (hypixelBoost.get() && !flyTimer.hasTimePassed(boostDelay.toLong())) {
                    mc.timer.timerSpeed =
                        1f + hypixelBoostTimer.get()
                }

                when (boostHypixelState) {
                    1 -> {
                        if (mc.thePlayer!!.onGround) {
                            event.y = 0.42
                        }

                        moveSpeed = (if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) 1.56 else 2.034) * baseSpeed
                        boostHypixelState = 2
                    }
                    2 -> {
                        moveSpeed *= 2.16
                        boostHypixelState = 3
                    }
                    3 -> {
                        moveSpeed =
                            lastDistance - (if (mc.thePlayer!!.ticksExisted % 2 == 0) 0.0103 else 0.0123) * (lastDistance - baseSpeed)
                        boostHypixelState = 4
                    }
                    else -> {
                        moveSpeed = lastDistance - lastDistance / 159.8
                    }
                }

                moveSpeed = max(moveSpeed, 0.3)

                val yaw = MovementUtils.getDirection();

                event.x = -sin(yaw) * moveSpeed
                event.z = cos(yaw) * moveSpeed

                mc.thePlayer!!.motionX = event.x
                mc.thePlayer!!.motionZ = event.z
            }
        }
    }

    @EventTarget
    fun onBB(event: BlockBBEvent) {
        if (mc.thePlayer == null) return
        val mode = modeValue.get()
        if (event.block is BlockAir && (mode.equals("Hypixel", ignoreCase = true) ||
                    mode.equals(
                        "BoostHypixel",
                        ignoreCase = true
                    ) && mc.thePlayer.inventory.getCurrentItem() == null) && event.y < mc.thePlayer!!.posY
        ) event.boundingBox = AxisAlignedBB.fromBounds(event.x.toDouble(), event.y.toDouble(), event.z.toDouble(), event.x + 1.toDouble(), mc.thePlayer.posY, event.z + 1.toDouble())
    }

    @EventTarget
    fun onJump(e: JumpEvent) {
        val mode = modeValue.get()
        if (mode.equals("Hypixel", ignoreCase = true) || mode.equals(
                "BoostHypixel",
                ignoreCase = true
            ) && mc.thePlayer.inventory.getCurrentItem() == null
        ) e.cancelEvent()
    }

    @EventTarget
    fun onStep(e: StepEvent) {
        val mode = modeValue.get()
        if (mode.equals("Hypixel", ignoreCase = true) || mode.equals(
                "BoostHypixel",
                ignoreCase = true
            ) && mc.thePlayer.inventory.getCurrentItem() == null
        ) e.stepHeight = 0f
    }

    private fun handleVanillaKickBypass() {
        if (!vanillaKickBypassValue.get() || !groundTimer.hasTimePassed(1000)) return
        val ground = calculateGround()
        run {
            var posY = mc.thePlayer.posY
            while (posY > ground) {
                mc.netHandler.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, posY, mc.thePlayer.posZ, true))
                if (posY - 8.0 < ground) break // Prevent next step
                posY -= 8.0
            }
        }
        mc.netHandler.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, ground, mc.thePlayer.posZ, true))
        var posY = ground
        while (posY < mc.thePlayer.posY) {
            mc.netHandler.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, posY, mc.thePlayer.posZ, true))
            if (posY + 8.0 > mc.thePlayer.posY) break // Prevent next step
            posY += 8.0
        }
        mc.netHandler.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true))
        groundTimer.reset()
    }

    // TODO: Make better and faster calculation lol
    private fun calculateGround(): Double {
        val playerBoundingBox: AxisAlignedBB = mc.thePlayer.entityBoundingBox
        var blockHeight = 1.0
        var ground = mc.thePlayer.posY
        while (ground > 0.0) {
            val customBox = AxisAlignedBB(playerBoundingBox.maxX, ground + blockHeight, playerBoundingBox.maxZ, playerBoundingBox.minX, ground, playerBoundingBox.minZ)
            if (mc.theWorld.checkBlockCollision(customBox)) {
                if (blockHeight <= 0.05) return ground + blockHeight
                ground += blockHeight
                blockHeight = 0.05
            }
            ground -= blockHeight
        }
        return 0.0
    }

    override val tag: String
        get() = modeValue.get()
}
