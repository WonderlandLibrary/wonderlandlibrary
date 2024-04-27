package me.AquaVit.liquidSense.modules.combat

import me.AquaVit.liquidSense.modules.movement.HYTFly
import me.AquaVit.liquidSense.modules.movement.HYTSpeed
import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.EventState
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.MotionEvent
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.modules.world.Scaffold
import net.ccbluex.liquidbounce.utils.InventoryUtils
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.client.gui.inventory.GuiInventory
import net.minecraft.init.Items
import net.minecraft.network.play.client.*
import net.minecraft.potion.Potion

@ModuleInfo(
        name = "AutoApple",
        description = "Makes you automatically eat soup whenever your health is low.",
        category = ModuleCategory.COMBAT
)
class AutoApple : Module() {

    val modeValue =
            ListValue("EadMode", arrayOf("Slow", "Fast","HYT"), "Fast")

    private val maxHealth = FloatValue("MaxHealth", 8f, 1f, 19f)
    val delayValue = IntegerValue("nextDelay", 150, 0, 2000)
    private val RegenVaule = BoolValue("NoRegen", true)

    private val timer = MSTimer()
    var isUse = false
    var eadId = -1

    private var delay = 0L


    override val tag: String
        get() = maxHealth.get().toString()


    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        val thePlayer = mc.thePlayer ?: return
        val GAPPLE = InventoryUtils.findItem(36, 45, Items.golden_apple)
        val SKULL = InventoryUtils.findItem(36, 45, Items.skull)
        val appleInInventory = InventoryUtils.findItem(9, 36, Items.golden_apple)

        if(modeValue.get().equals("hyt" , true)) {
            if (!timer.hasTimePassed(delayValue.get().toLong()))
                return
            if(LiquidBounce.moduleManager.getModule(HYTFly::class.java)!!.state || LiquidBounce.moduleManager.getModule(HYTSpeed::class.java)!!.state)
                return
            if(mc.thePlayer.onGround)
                return
            val thePlayer = mc.thePlayer ?: return

            if (thePlayer.health <= maxHealth.get() && GAPPLE != -1) {
                mc.netHandler.addToSendQueue(C09PacketHeldItemChange(GAPPLE - 36))
                mc.netHandler.addToSendQueue(
                        C08PacketPlayerBlockPlacement(mc.thePlayer.inventoryContainer
                                .getSlot(GAPPLE).stack)
                )
                repeat(35) {
                    mc.netHandler.addToSendQueue(C03PacketPlayer(thePlayer.onGround))
                }

                mc.playerController.onStoppedUsingItem(thePlayer)
                mc.netHandler.addToSendQueue(C09PacketHeldItemChange(thePlayer.inventory.currentItem))
                timer.reset()
                return
            }

            if (appleInInventory != -1 && InventoryUtils.hasSpaceHotbar()) {
                if (mc.currentScreen !is GuiInventory)
                    return

                val openInventory = mc.currentScreen !is GuiInventory
                if (openInventory)
                    mc.netHandler.addToSendQueue(
                            C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT))

                mc.playerController.windowClick(0, appleInInventory, 0, 1, thePlayer)

                if (openInventory)
                    mc.netHandler.addToSendQueue(C0DPacketCloseWindow())

                timer.reset()
            }
        }else{
            if (timer.hasTimePassed(delay) || !LiquidBounce.moduleManager.get(Scaffold::class.java)!!.state || !thePlayer.isDead) {

                if (thePlayer.health >= maxHealth.get()) {
                    if (isUse) {
                        mc.netHandler.addToSendQueue(C09PacketHeldItemChange(thePlayer.inventory.currentItem))
                        isUse = false
                        eadId = -1
                    }
                    return
                }

                val potifinSave = if (RegenVaule.get()) !mc.thePlayer!!.isPotionActive(Potion.regeneration) else true

                (if (GAPPLE == -1 && SKULL != -1) SKULL else GAPPLE).also {
                    if (thePlayer.health <= maxHealth.get() && it != -1 && potifinSave) {

                        isUse = true
                        mc.netHandler.addToSendQueue(C09PacketHeldItemChange(it - 36))
                        mc.netHandler.addToSendQueue(
                                C08PacketPlayerBlockPlacement(mc.thePlayer.inventoryContainer
                                        .getSlot(it).stack)
                        )
                        mc.netHandler.addToSendQueue(C09PacketHeldItemChange(it - 36))
                        if (modeValue.get().equals("fast", true)) {

                            mc.netHandler.addToSendQueue(C09PacketHeldItemChange(thePlayer.inventory.currentItem))
                            isUse = false
                        }
                        timer.reset()
                        delay = delayValue.get().toLong()
                        return
                    }
                    if (modeValue.get().equals("slow", true)) {
                        if (isUse || thePlayer.itemInUseDuration >= 24) {
                            mc.netHandler.addToSendQueue(C09PacketHeldItemChange(thePlayer.inventory.currentItem))
                            isUse = false
                            timer.reset()
                            delay = delayValue.get().toLong()
                        }
                    }
                }
                //  delay = delayValue.get().toLong()
            }
        }
    }
}