/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.features.module.modules.movement

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.EventState
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.MotionEvent
import net.ccbluex.liquidbounce.event.SlowDownEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.modules.combat.Aura
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FloatValue
import net.minecraft.item.*
import net.minecraft.network.play.client.C07PacketPlayerDigging
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement
import net.minecraft.util.BlockPos
import net.minecraft.util.EnumFacing
import net.minecraft.util.MathHelper

@ModuleInfo(name = "NoSlowDown", description = "Cancels slowness effects caused by soulsand and using items.",
        category = ModuleCategory.MOVEMENT)
class NoSlow : Module() {

    private val blockForwardMultiplier = FloatValue("BlockForwardMultiplier", 1.0F, 0.2F, 1.0F)
    private val blockStrafeMultiplier = FloatValue("BlockStrafeMultiplier", 1.0F, 0.2F, 1.0F)

    private val consumeForwardMultiplier = FloatValue("ConsumeForwardMultiplier", 1.0F, 0.2F, 1.0F)
    private val consumeStrafeMultiplier = FloatValue("ConsumeStrafeMultiplier", 1.0F, 0.2F, 1.0F)

    private val bowForwardMultiplier = FloatValue("BowForwardMultiplier", 1.0F, 0.2F, 1.0F)
    private val bowStrafeMultiplier = FloatValue("BowStrafeMultiplier", 1.0F, 0.2F, 1.0F)

    private val packet = BoolValue("Packet", true)
    private val Hyt32 = BoolValue("Hyt32", true)
    private val A4 = BoolValue("AAC4", true)
    // Soulsand
    val soulsandValue = BoolValue("Soulsand", true)
    val aura = LiquidBounce.moduleManager[Aura::class.java] as Aura

    @EventTarget
    fun onMotion(event: MotionEvent) {
        val blockState = isBlocking()
        val heldItem = mc.thePlayer.heldItem
        if(!A4.get()){
            if (heldItem == null || heldItem.item !is ItemSword || !MovementUtils.isMoving()){
                return
            }
            if (!mc.thePlayer.isBlocking && !aura.blockingStatus) {
                return
            }
        } else {
            if (heldItem == null || heldItem.item !is ItemSword){
                return
            }
        }

        if (this.packet.get()) {
            when (event.eventState) {
                EventState.PRE -> {
                    val digging = C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos(-.8,-.8,-.8), EnumFacing.DOWN)
                    mc.netHandler.addToSendQueue(digging)
                }
                EventState.POST -> {
                    val blockPlace = C08PacketPlayerBlockPlacement(BlockPos(-.8,-.8,-.8), 255, mc.thePlayer.inventory.getCurrentItem(), 0.0f, 0.0f, 0.0f)
                    mc.netHandler.addToSendQueue(blockPlace)
                }
            }
        }
        var test = fuckKotline(mc.thePlayer.ticksExisted and 1);
        if (this.A4.get() && (test || !(blockState)) && (aura.target != null || mc.thePlayer.isBlocking)) {
            when (event.eventState) {
                EventState.PRE -> {
                    val digging = C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos(-1, -1, -1), EnumFacing.DOWN)
                    mc.netHandler.addToSendQueue(digging)
                }
                EventState.POST -> {
                    val blockPlace = C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem())
                    mc.netHandler.addToSendQueue(blockPlace)
                }
            }
        }
        if (this.Hyt32.get() && aura.target == null) {
            when (event.eventState) {
                EventState.PRE -> {
                    val digging = C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN)
                    mc.netHandler.addToSendQueue(digging)
                }
                EventState.POST -> {
                    mc.thePlayer.sendQueue.addToSendQueue(C08PacketPlayerBlockPlacement(getHypixelBlockpos(), 255, mc.thePlayer.inventory.getCurrentItem(), 0f, 0f, 0f))
                }
            }
        }
    }

    fun fuckKotline(value: Int): Boolean{
        return value == 1
    }

    fun getHypixelBlockpos(): BlockPos? {
        val random = java.util.Random()
        val dx = MathHelper.floor_double(random.nextDouble() / 1000 + 2820)
        val jy = MathHelper.floor_double(random.nextDouble() / 100 * 0.20000000298023224)
        val kz = MathHelper.floor_double(random.nextDouble() / 1000 + 2820)
        return BlockPos(dx, -jy % 255, kz)
    }

    @EventTarget
    fun onSlowDown(event: SlowDownEvent) {
        val heldItem = mc.thePlayer.heldItem?.item

        event.forward = getMultiplier(heldItem, true)
        event.strafe = getMultiplier(heldItem, false)
    }

    private fun getMultiplier(item: Item?, isForward: Boolean) = when (item) {
        is ItemFood, is ItemPotion, is ItemBucketMilk -> {
            if (isForward) this.consumeForwardMultiplier.get() else this.consumeStrafeMultiplier.get()
        }
        is ItemSword -> {
            if (isForward) this.blockForwardMultiplier.get() else this.blockStrafeMultiplier.get()
        }
        is ItemBow -> {
            if (isForward) this.bowForwardMultiplier.get() else this.bowStrafeMultiplier.get()
        }
        else -> 0.2F
    }

    fun isBlocking(): Boolean {
        return mc.thePlayer.isBlocking || aura.blockingStatus
    }

    override val tag: String?
        get() = when {
            packet.get() -> "Packet"
            Hyt32.get() -> "HytPacket"
            A4.get() -> "AAC4"
            else -> "Vanilla"
        }
}
