package me.report.modules.player

import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.utils.misc.RandomUtils
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.network.play.client.C01PacketChatMessage


@ModuleInfo(name = "ChatBypass", category = ModuleCategory.PLAYER, description = "idk")
class ChatBypass : Module() {
    private val modeValue = ListValue("Mode", arrayOf("Null", "RandomChar"), "Null")
    private val chanceValue = FloatValue("Chance", 0.2F, 0F, 0.5F)
    private val minUnicodeValue: IntegerValue = object : IntegerValue("MinUnicode", 1000, 0, 100000) {
        override fun onChanged(oldValue: Int, newValue: Int) {
            if (newValue >= maxUnicodeValue.get()) {
                set(oldValue)
            }
        }
    }

    private val maxUnicodeValue: IntegerValue = object : IntegerValue("MaxUnicode", 20000, 0, 100000) {
        override fun onChanged(oldValue: Int, newValue: Int) {
            if (newValue <= minUnicodeValue.get()) {
                set(oldValue)
            }
        }
    }

    @EventTarget
    fun onPacket(event: PacketEvent) {
        if (event.packet is C01PacketChatMessage) {
            val packet = event.packet
            val message = packet.message
            if (message.startsWith("/")) return
            val sb = StringBuilder()

            for (char in message.toCharArray()) {
                when (modeValue.get().toLowerCase()) {
                    "null" -> {
                        sb.append(char)
                        if (Math.random() < chanceValue.get()) {
                            sb.append("\uF8FF")
                        }
                    }

                    "randomchar" -> {
                        sb.append(char)
                        if (Math.random() < chanceValue.get()) {
                            sb.append((RandomUtils.nextInt(minUnicodeValue.get(), maxUnicodeValue.get())).toChar())
                        }
                    }
                }
            }

            packet.message = sb.toString()
            if (packet.message.length> 100) {
                packet.message = packet.message.substring(0, 100)
            }
        }
    }
}