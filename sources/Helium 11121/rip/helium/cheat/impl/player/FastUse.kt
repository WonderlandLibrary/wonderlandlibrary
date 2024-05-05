package rip.helium.cheat.impl.player

import me.hippo.systems.lwjeb.annotation.Collect
import net.minecraft.item.ItemFood
import net.minecraft.item.ItemPotion
import rip.helium.cheat.Cheat
import rip.helium.cheat.CheatCategory
import rip.helium.event.minecraft.PlayerUpdateEvent
import net.minecraft.network.play.client.C03PacketPlayer
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook
import rip.helium.ChatUtil
import rip.helium.utils.property.impl.DoubleProperty
import rip.helium.utils.property.impl.StringsProperty


/*/
Created by Kansio on 1/16/2021
 */

class FastUse : Cheat("FastUse", "Uses shit faster", CheatCategory.PLAYER) {


    var fastuseMode = StringsProperty("Mode", "How the priority target will be selected.", null, false, true, arrayOf("Vanilla", "Ghostly"), arrayOf(true, false));

    @Collect
    fun onPlayerUpdate(event: PlayerUpdateEvent) {
        when (fastuseMode.selectedStrings.get(0)) {
            "Vanilla" -> {
                if (mc.thePlayer.isUsingItem) {
                    if (mc.thePlayer.onGround) {
                        if (isUsingItem()) {
                            repeat(20) {
                                mc.thePlayer.sendQueue.addToSendQueue(C03PacketPlayer(true));
                            }
                        }
                    }
                }
            }
            "Ghostly" -> {
                if (mc.thePlayer.isUsingItem) {
                    if (mc.thePlayer.onGround) {
                        if (this.mc.thePlayer.ticksExisted % 4 == 0) {
                            repeat(8) {
                                val d = mc.thePlayer.posX; Double
                                val d2 = mc.thePlayer.posY + 1.0E-9; Double
                                val d3 = mc.thePlayer.posZ; Double
                                mc.thePlayer.sendQueue.addToSendQueue(C06PacketPlayerPosLook(d, d2, d3, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, true))
                            }
                        }
                    }
                }
            }
        }
    }

    init {
        registerProperties(fastuseMode);
    }

    fun isUsingItem(): Boolean {
        return mc.thePlayer.currentEquippedItem != null && (this.mc.thePlayer.currentEquippedItem.item is ItemFood || this.mc.thePlayer.currentEquippedItem.item is ItemPotion) && mc.thePlayer.onGround
    }

}