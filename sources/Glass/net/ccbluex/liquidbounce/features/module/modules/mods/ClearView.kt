/*
 * GlassClient PVP Client
 * A free open-source mixin-based PVP client based on liquidbounce with all cheats removed.
 * https://github.com/GlassClient/GlassClient
 */
package net.ccbluex.liquidbounce.features.module.modules.mods

import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FloatValue

@ModuleInfo(name = "ClearView", category = ModuleCategory.MODS)
class ClearView : Module() {
    val confusionEffectValue = BoolValue("NoNausea", true)
    val AchievementValue = BoolValue("AchievementValue", true)
    val pumpkinEffectValue = BoolValue("AntiPumpkin", true)
    val fireEffectValue = FloatValue("FireAlpha", 0.3f, 0f, 1f)
    val bossHealth = BoolValue("Boss-Health", true)
    //val barriersValue = BoolValue("ShowBarriers", true)
    //val entitiesValue = BoolValue("ShowEntities", true)
    val entitiesValue = false // this is a cheat...
    val barriersValue = false // check if crative mode before adding back

    @EventTarget(ignoreCondition = true)
    fun onUpdate(event: UpdateEvent) {
        if(!AchievementValue.get()){ mc.guiAchievement.clearAchievements() }
    }
}