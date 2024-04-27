/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.features.module.modules.combat

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.EventState.POST
import net.ccbluex.liquidbounce.event.EventState.PRE
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.MotionEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.utils.*
import net.ccbluex.liquidbounce.utils.misc.FallingPlayer
import net.ccbluex.liquidbounce.utils.misc.RandomUtils
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.client.gui.inventory.GuiInventory
import net.minecraft.item.ItemPotion
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement
import net.minecraft.network.play.client.C09PacketHeldItemChange
import net.minecraft.network.play.client.C0DPacketCloseWindow
import net.minecraft.network.play.client.C16PacketClientStatus
import net.minecraft.potion.Potion

@ModuleInfo(name = "AutoPot", description = "Automatically throws healing potions.", category = ModuleCategory.COMBAT)
class AutoPot : Module() {

    private val healthValue = FloatValue("Health", 15F, 1F, 20F)
    private val delayValue = IntegerValue("Delay", 500, 500, 1000)

    private val openInventoryValue = BoolValue("OpenInv", false)
    private val simulateInventory = BoolValue("SimulateInventory", true)

    private val groundDistanceValue = FloatValue("GroundDistance", 2F, 0F, 5F)
    private val modeValue = ListValue("Mode", arrayOf("Normal", "Jump", "Port"), "Normal")

    private val msTimer = MSTimer()
    private var potion = -1

    @EventTarget
    fun onMotion(motionEvent: MotionEvent) {
        val killAura = LiquidBounce.moduleManager.getModule(Aura::class.java) as Aura

        if ((mc.thePlayer!!.capabilities.allowFlying && mc.thePlayer!!.capabilities.isFlying) || !msTimer.hasTimePassed(delayValue.get().toLong()) || mc.playerController.isInCreativeMode || (mc.thePlayer!!.openContainer != null && mc.thePlayer!!.openContainer!!.windowId != 0) || killAura.target != null)
            return

        val thePlayer = mc.thePlayer ?: return
        val potionInHotbar = findPotion(36, 45)
        // Inventory Potion -> Hotbar Potion
        val potionInInventory = findPotion(9, 36)


        when (motionEvent.eventState) {
            PRE -> {
                // Hotbar Potion

                if (potionInHotbar != -1 && mc.currentScreen !is GuiInventory) {
                    if (thePlayer.onGround) {
                        when (modeValue.get().toLowerCase()) {
                            "jump" -> thePlayer.jump()
                            "port" -> thePlayer.moveEntity(0.0, 0.42, 0.0)
                        }
                    }

                    potion = potionInHotbar - 36
                    mc.netHandler.addToSendQueue(C09PacketHeldItemChange(potion))

                    if (thePlayer.rotationPitch <= 80F) {

                        RotationUtils.setTargetRotation(Rotation(thePlayer.rotationYaw, RandomUtils.nextFloat(80F, 90F)))
                        if (potion != -1 && RotationUtils.serverRotation.pitch >= 80F) {
                            val itemStack = thePlayer.inventory.getStackInSlot(potion)

                            if (itemStack != null) {
                                mc.netHandler.addToSendQueue(C08PacketPlayerBlockPlacement(itemStack))
                                mc.netHandler.addToSendQueue(C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem))
                            }
                            potion = -1
                            msTimer.reset()
                        }
                    }
                }

                if ((potionInInventory != -1 && potion == -1) && InventoryUtils.hasSpaceHotbar()) {
                    if (openInventoryValue.get() && mc.currentScreen !is GuiInventory)
                        return

                    val openInventory = mc.currentScreen !is GuiInventory && simulateInventory.get()

                    if (openInventory)
                        mc.netHandler.addToSendQueue(
                                C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT))

                    mc.playerController.windowClick(0, potionInInventory, 0, 1, thePlayer)

                    if (openInventory)
                        mc.netHandler.addToSendQueue(C0DPacketCloseWindow())

                    msTimer.reset()
                }
            }
        }
    }

    private fun findPotion(startSlot: Int, endSlot: Int): Int {
        val thePlayer = mc.thePlayer!!

        for (i in startSlot until endSlot) {
            val stack = thePlayer.inventoryContainer.getSlot(i).stack

            if (stack == null || stack.item !is ItemPotion || !ItemPotion.isSplash(stack.itemDamage))
                continue

            val itemPotion = stack.item as ItemPotion

            if (thePlayer.health <= healthValue.get()) {
                for (potionEffect in itemPotion.getEffects(stack))
                    if (potionEffect.potionID == Potion.heal.id)
                        return i

                if (!thePlayer.isPotionActive(Potion.regeneration.id))
                    for (potionEffect in itemPotion.getEffects(stack))
                        if (potionEffect.potionID == Potion.regeneration.id)
                            return i
            }

            if (!thePlayer.isPotionActive(Potion.moveSpeed.id))
                for (potionEffect in itemPotion.getEffects(stack))
                    if (potionEffect.potionID == Potion.moveSpeed.id)
                        return i
        }

        return -1
    }

    /*  override val tag: String?
        get() = healthValue.get().toString()*/

}