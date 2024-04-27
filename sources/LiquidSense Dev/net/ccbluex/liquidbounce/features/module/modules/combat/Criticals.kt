/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.features.module.modules.combat

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.AttackEvent
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.modules.movement.Fly
import net.ccbluex.liquidbounce.utils.misc.MiscUtils
import net.ccbluex.liquidbounce.utils.misc.MiscUtils.getRandom
import net.ccbluex.liquidbounce.utils.misc.MiscUtils.random
import net.ccbluex.liquidbounce.utils.misc.RandomUtils
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.entity.EntityLivingBase
import net.minecraft.network.play.client.C03PacketPlayer
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition
import java.util.*


@ModuleInfo(name = "Criticals", description = "Automatically deals critical hits.", category = ModuleCategory.COMBAT)
class Criticals : Module() {
    var modeValue = ListValue("Mode", arrayOf("NewPacket","NewHypixel","Packet", "HypixelPacket", "NoGround", "Hop", "TPHop", "Jump", "LowJump","HYTJump","NGJump","AAC4","AAC4.4.0"), "packet")
    var delayValue = IntegerValue("Delay", 0, 0, 1000)
    private val hurtTimeValue = IntegerValue("HurtTime", 10, 0, 10)

    val msTimer = MSTimer()

    fun getSelectMode(): String{
        return modeValue.get()
    }

    var hypixelOffsets = doubleArrayOf(0.05000000074505806, 0.0015999999595806003, 0.029999999329447746,
            0.001599999959580600)

    var offsets = doubleArrayOf(0.051, 0.0, 0.0125, 0.0)

    var sb = doubleArrayOf(0.00521, 0.0052, 0.05, 1.086765133E-10)

    var motion = doubleArrayOf(0.010, 0.0010, 0.08, 0.002)

    override fun onEnable() {

        if (modeValue.get().equals("NoGround", ignoreCase = true))
            mc.thePlayer.jump()

        if (modeValue.get().equals("NGJump", ignoreCase = true))
            mc.thePlayer.jump()

    }

    @EventTarget
    fun onUpdate(event: UpdateEvent){
        if(modeValue.get().equals("HYTJump",ignoreCase = true)){
            mc.thePlayer.isSprinting = false;
        }

        if(modeValue.get().equals("NGJump",ignoreCase = true)){
            mc.thePlayer.isSprinting = false;
        }


    }

    fun Crit(value: Array<Double>) {
        val var1 = mc.thePlayer.sendQueue.networkManager
        val curX = mc.thePlayer.posX
        val curY = mc.thePlayer.posY
        val curZ = mc.thePlayer.posZ
        for (offset in value) {
            var1.sendPacket(C03PacketPlayer.C06PacketPlayerPosLook(curX, curY + offset, curZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, false))
        }
    }

    @EventTarget
    fun onAttack(event: AttackEvent) {
        if (event.targetEntity is EntityLivingBase) {
            val entity = event.targetEntity

            if (!mc.thePlayer.onGround || mc.thePlayer.isOnLadder || mc.thePlayer.isInWeb || mc.thePlayer.isInWater ||
                    mc.thePlayer.isInLava || mc.thePlayer.ridingEntity != null || entity.hurtTime > hurtTimeValue.get() ||
                    LiquidBounce.moduleManager[Fly::class.java]!!.state || !msTimer.hasTimePassed(delayValue.get().toLong()))
                return

            val x = mc.thePlayer.posX
            val y = mc.thePlayer.posY
            val z = mc.thePlayer.posZ

            when (modeValue.get().toLowerCase()) {

                "newhypixel" -> {
                    Crit(arrayOf(0.04250000001304, 0.00150000001304, 0.01400000001304, 0.00150000001304)) //0.625, -RandomUtils.nextDouble(0.0D, 0.625)
                    mc.thePlayer.onCriticalHit(entity)
                }

                "newpacket" -> {
                    for (sb in sb) {
                        mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + sb + Random().nextDouble() / 4789, mc.thePlayer.posZ, false))
                    }
                }
                "packet" -> {
                    mc.netHandler.addToSendQueue(C04PacketPlayerPosition(x, y + 0.0625, z, true))
                    mc.netHandler.addToSendQueue(C04PacketPlayerPosition(x, y, z, false))
                    mc.netHandler.addToSendQueue(C04PacketPlayerPosition(x, y + 1.1E-5, z, false))
                    mc.netHandler.addToSendQueue(C04PacketPlayerPosition(x, y, z, false))
                    mc.thePlayer.onCriticalHit(entity)
                }
                "aac4" -> {
                    mc.netHandler.addToSendQueue(C04PacketPlayerPosition(x, y + 0.05250000001304, z, false))
                    mc.netHandler.addToSendQueue(C04PacketPlayerPosition(x, y + 0.00150000001304, z, false))
                    mc.netHandler.addToSendQueue(C04PacketPlayerPosition(x, y + 0.01400000001304, z, false))
                    mc.netHandler.addToSendQueue(C04PacketPlayerPosition(x, y + 0.00150000001304, z, false))
                    mc.thePlayer.onCriticalHit(entity)
                }
                "hypixelpacket" -> {
                    val values = doubleArrayOf(0.0425, .0015, if (getRandom().nextBoolean()) .012 else .014)
                    if (mc.thePlayer.ticksExisted % 2 == 0)
                        for (value in values) {
                            val random = if (getRandom().nextBoolean()) RandomUtils.nextDouble(-1E-8, -1E-7) else RandomUtils.nextDouble(1E-7, 1E-8)
                            mc.netHandler.networkManager.sendPacket(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + value + random, mc.thePlayer.posZ, false))
                            mc.thePlayer.onCriticalHit(entity)
                        }
                }

                "hop" -> {
                    mc.thePlayer.motionY = 0.1
                    mc.thePlayer.fallDistance = 0.1f
                    mc.thePlayer.onGround = false
                }

                "tphop" -> {
                    mc.netHandler.addToSendQueue(C04PacketPlayerPosition(x, y + 0.02, z, false))
                    mc.netHandler.addToSendQueue(C04PacketPlayerPosition(x, y + 0.01, z, false))
                    mc.thePlayer.setPosition(x, y + 0.01, z)
                }

                "aac4.4.0" -> {
                    if (mc.thePlayer.motionX == 0.0 && mc.thePlayer.motionZ == 0.0) {
                        mc.netHandler.addToSendQueue(C04PacketPlayerPosition(x, y + 0.02, z, false))
                        mc.netHandler.addToSendQueue(C04PacketPlayerPosition(x, y + 0.01, z, false))
                        mc.thePlayer.setPosition(x, y + 0.01, z)
                    }
                }
                "NGJump" -> mc.thePlayer.motionY = 0.42
                "hytjump" -> mc.thePlayer.motionY = 0.42
                "jump" -> mc.thePlayer.motionY = 0.42
                "lowjump" -> mc.thePlayer.motionY = 0.3425
            }

            msTimer.reset()
        }
    }

    @EventTarget
    fun onPacket(event: PacketEvent) {
        val packet = event.packet

        if (packet is C03PacketPlayer && modeValue.get().equals("NoGround", ignoreCase = true))
            packet.onGround = false

        if (packet is C03PacketPlayer && modeValue.get().equals("NGJump", ignoreCase = true))
            packet.onGround = false

    }

     override val tag: String?
        get() = modeValue.get()


}
