
package net.ccbluex.liquidbounce.features.module.modules.mods


import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.utils.ClientUtils
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.minecraft.network.play.server.S0EPacketSpawnObject
import net.minecraft.network.play.server.S2BPacketChangeGameState

@ModuleInfo(name = "Fixer", category = ModuleCategory.MODS, array = false)
class Fixer : Module() {
    private val guardianValue = BoolValue("LessGuardian", true)
    private val demoGuiValue = BoolValue("StopFakeDemoGui", true)
    private val maxArrowSpawn = IntegerValue("MaxArrowPerSecond", 100, 1, 1000)

    private var tick = 0
    private var arrowMax = 0
    private var guardianEffect = false

    private fun sendMessage(s: String) {
        ClientUtils.displayChatMessage("§7[§a§lGlassFixer§7]§6 $s")
    }

    @EventTarget
    fun onPacket(event: PacketEvent) {
        val packet = event.packet

        if (packet is S2BPacketChangeGameState) {
            when(packet.gameState) {
                // this stops servers from sending you ilegal demo guis
                5 -> {
                    if(demoGuiValue.get()){
                        if(!mc.isDemo) {
                            event.cancelEvent()
                            sendMessage("we stopped the server from sending an Illegal Demo GUI.")
                        }
                    }
                }
                // if you have guardian effect this stops the server spaming it
                10 -> {
                    if(guardianValue.get()) {
                        if (!guardianEffect) {
                            guardianEffect = true
                        } else {
                            event.cancelEvent()
                            sendMessage("Cancelled additional guardian effect.")
                        }
                    }

                }
            }
        }

        if (packet is S0EPacketSpawnObject && packet.type == 60) {
            // limits the ammount of arrows that can be spawned in a second
            if (arrowMax++ > maxArrowSpawn.get()) {
                event.cancelEvent()
                sendMessage("Reached max arrow spawn per second.")
            }
        }
    }

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        if (tick++ >= 20) {
            tick = 0
            arrowMax = 0
            guardianEffect = false
        }
    }


}