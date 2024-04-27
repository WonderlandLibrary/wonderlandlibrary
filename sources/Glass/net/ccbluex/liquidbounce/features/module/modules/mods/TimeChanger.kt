package net.ccbluex.liquidbounce.features.module.modules.mods

import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.network.play.server.S03PacketTimeUpdate

@ModuleInfo(name = "TimeChanger", category = ModuleCategory.MODS)
class TimeChanger : Module() {

    private val timeModeValue = ListValue("TimeMode", arrayOf("None", "Normal", "Custom"), "Normal")
    private val customWorldTimeValue = IntegerValue("CustomTime", 1000, 0, 24000).displayable { timeModeValue.equals("Custom") }
    private val changeWorldTimeSpeedValue = IntegerValue("ChangeWorldTimeSpeed", 150, 10, 500).displayable { timeModeValue.equals("Normal") }

    var i = 0L

    override fun onDisable() {
        i = 0
    }

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        when (timeModeValue.get().lowercase()) {
            "normal" -> {
                if (i < 24000) {
                    i += changeWorldTimeSpeedValue.get()
                } else {
                    i = 0
                }
                mc.theWorld.worldTime = i
            }
            "custom" -> {
                mc.theWorld.worldTime = customWorldTimeValue.get().toLong()
            }
        }

    }

    @EventTarget
    fun onPacket(event: PacketEvent) {
        val packet = event.packet

        if (!timeModeValue.equals("none") && packet is S03PacketTimeUpdate) {
            event.cancelEvent()
        }
    }
}