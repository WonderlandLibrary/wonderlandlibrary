package net.ccbluex.liquidbounce.features.module.modules.mods

import net.ccbluex.liquidbounce.event.ClientShutdownEvent
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.potion.Potion
import net.minecraft.potion.PotionEffect


@ModuleInfo(name = "FullBright", category = ModuleCategory.MODS)
class FullBright : Module() {
    private val brightValue = ListValue("FullBright", arrayOf("None", "Both", "Gamma", "NightVision"), "Gamma")
    private var prevGamma = -1f

    override fun onEnable() {
        prevGamma = mc.gameSettings.gammaSetting
    }

    override fun onDisable() {
        if (prevGamma == -1f) return
        mc.gameSettings.gammaSetting = prevGamma
        prevGamma = -1f
        if (mc.thePlayer != null) mc.thePlayer.removePotionEffectClient(Potion.nightVision.id)
    }


    @EventTarget(ignoreCondition = true)
    fun onUpdate(event: UpdateEvent) {
        if (state) {
            when (brightValue.get().lowercase()) {
                "gamma" -> if (mc.gameSettings.gammaSetting <= 100f) mc.gameSettings.gammaSetting++
                "nightvision" -> mc.thePlayer.addPotionEffect(PotionEffect(Potion.nightVision.id, 1337, 1))
                "both" -> {
                    mc.thePlayer.addPotionEffect(PotionEffect(Potion.nightVision.id, 1337, 1))
                    if (mc.gameSettings.gammaSetting <= 100f) mc.gameSettings.gammaSetting++
                }
            }
        } else if (prevGamma != -1f) {
            mc.gameSettings.gammaSetting = prevGamma
            prevGamma = -1f
        }
    }

    @EventTarget(ignoreCondition = true)
    fun onShutdown(event: ClientShutdownEvent) {
        onDisable()
    }
}