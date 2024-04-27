package me.AquaVit.liquidSense.modules.misc

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.event.WorldEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.utils.ClientUtils
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.network.play.server.S02PacketChat
import java.util.*

@ModuleInfo(name = "NoHurt", description = "Hyt NoHurt Bot", category = ModuleCategory.MISC)
class NoHurt : Module() {

    private val hyt: MutableList<String> = ArrayList()
    private val packetChatMessage: MutableList<String> = ArrayList()


    private fun getSubString(text: String, left: String, right: String): String {
        var result = ""
        var zLen: Int
        if (left == null || left.isEmpty()) {
            zLen = 0
        } else {
            zLen = text.indexOf(left)
            if (zLen > -1) {
                zLen += left.length
            } else {
                zLen = 0
            }
        }
        var yLen = text.indexOf(right!!, zLen)
        if (yLen < 0 || right == null || right.isEmpty()) {
            yLen = text.length
        }
        result = text.substring(zLen, yLen)
        return result
    }

    @EventTarget
    fun onPacket(event: PacketEvent) {
        if (mc.thePlayer == null || mc.theWorld == null)
            return
        val packet = event.packet
        if (packet is S02PacketChat) {
            if (packetChatMessage.contains(":") == false) {
                if (packetChatMessage.contains("起床战争 >> " ) && packetChatMessage.contains("死了")) { /*多人*/
                    val str1 = packetChatMessage
                    if(str1.contains("杀死了")){
                        val friend = getSubString(str1.toString(),"杀死了 ","(");
                        if(!hyt.contains(friend)){
                            hyt.add(friend);
                            ClientUtils.displayChatMessage("§l§8[§a§lPang提醒§8]§a§l添加无敌人:§c§l§3$friend");
                            object : Thread() {
                                override fun run() {
                                    try {
                                        sleep(6000)
                                        if (hyt.contains(friend)) {
                                            hyt.remove(friend)
                                            ClientUtils.displayChatMessage("§l§8[§a§lPang提醒§8]§a§l删除无敌人:§c§l§9$friend")
                                        }
                                    } catch (e: InterruptedException) {
                                        e.printStackTrace()
                                    }
                                }
                            }.start()
                        }
                    }else{
                        val friend = getSubString(str1.toString(),">> ","(");
                        if(!hyt.contains(friend)){
                            hyt.add(friend);
                            ClientUtils.displayChatMessage("§l§8[§a§lPang提醒§8]§a§l添加无敌人:§c§l§3$friend");
                            object : Thread() {
                                override fun run() {
                                    try {
                                        sleep(6000)
                                        if (hyt.contains(friend)) {
                                            hyt.remove(friend)
                                            ClientUtils.displayChatMessage("§l§8[§a§lPang提醒§8]§a§l删除无敌人:§c§l§9$friend")
                                        }
                                    } catch (e: InterruptedException) {
                                        e.printStackTrace()
                                    }
                                }
                            }.start()
                        }
                    }
                }
                if (packetChatMessage.contains("起床战争>> " ) && packetChatMessage.contains("死了")) { /*少人*/
                    val str1 = packetChatMessage
                    if(str1.contains("杀死了")){
                        val friend = getSubString(str1.toString(),"杀死了 "," (");
                        if(!hyt.contains(friend)){
                            hyt.add(friend);
                            ClientUtils.displayChatMessage("§l§8[§a§lPang提醒§8]§a§l添加无敌人:§c§l§3$friend");
                            object : Thread() {
                                override fun run() {
                                    try {
                                        sleep(6000)
                                        if (hyt.contains(friend)) {
                                            hyt.remove(friend)
                                            ClientUtils.displayChatMessage("§l§8[§a§lPang提醒§8]§a§l删除无敌人:§c§l§9$friend")
                                        }
                                    } catch (e: InterruptedException) {
                                        e.printStackTrace()
                                    }
                                }
                            }.start()
                        }
                    }else{
                        val friend = getSubString(str1.toString(),">> "," (");
                        if(!hyt.contains(friend)){
                            hyt.add(friend);
                            ClientUtils.displayChatMessage("§l§8[§a§lPang提醒§8]§a§l添加无敌人:§c§l§3$friend");
                            object : Thread() {
                                override fun run() {
                                    try {
                                        sleep(6000)
                                        if (hyt.contains(friend)) {
                                            hyt.remove(friend)
                                            ClientUtils.displayChatMessage("§l§8[§a§lPang提醒§8]§a§l删除无敌人:§c§l§9$friend")
                                        }
                                    } catch (e: InterruptedException) {
                                        e.printStackTrace()
                                    }
                                }
                            }.start()
                        }
                    }
                }

            }

        }

    }

    @EventTarget
    fun onWorld(event: WorldEvent?) {
        clearAll()
    }

    override fun onDisable() {
        clearAll()
        super.onDisable()
    }

    private fun clearAll() {
        hyt.clear()
        packetChatMessage.clear()
    }

    companion object {
        @JvmStatic
        fun isBot(entity: EntityLivingBase): Boolean {
            if (entity !is EntityPlayer) return false
            val antiBot = LiquidBounce.moduleManager.getModule(NoHurt::class.java) as NoHurt?
            if (antiBot == null || !antiBot.state) return false
            if (antiBot.hyt.contains(entity.name)) return true
            return entity.name!!.isEmpty() || entity.name == mc.thePlayer!!.name
        }
    }

}