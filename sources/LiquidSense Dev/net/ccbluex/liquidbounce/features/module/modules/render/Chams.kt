/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.features.module.modules.render

import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.utils.ChamsColor
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.ListValue

@ModuleInfo(name = "Chams", description = "Allows you to see targets through blocks.", category = ModuleCategory.RENDER)
class Chams : Module() {
    private val colorRedValue = FloatValue("R", 200F, 0F, 255F)
    private val colorGreenValue = FloatValue("G", 100F, 0F, 255F)
    private val colorBlueValue = FloatValue("B", 100F, 0F, 255F)
    private val colorAValue = FloatValue("A", 100F, 0F, 255F)
    private val colorRed2Value = FloatValue("R2", 100F, 0F, 255F)
    private val colorGreen2Value = FloatValue("G2", 100F, 0F, 255F)
    private val colorBlue2Value = FloatValue("B2", 200F, 0F, 255F)
    private val colorA2Value = FloatValue("A2", 200F, 0F, 255F)
    val rainbow = BoolValue("Rainbow",false)
    val targetsValue = BoolValue("Targets", true)
    val chestsValue = BoolValue("Chests", true)
    val itemsValue = BoolValue("Items", true)
    private val all = BoolValue("AllEntity", false)
    val onlyhead = BoolValue("OnlyHead", false)


    fun getBoolValue(): BoolValue? {
        return all
    }

    fun getModeValue(): BoolValue? {
        return rainbow
    }

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        ChamsColor.red=colorRedValue.get()/255
        ChamsColor.green=colorGreenValue.get()/255
        ChamsColor.blue=colorBlueValue.get()/255
        ChamsColor.Apl=colorAValue.get()/255
        ChamsColor.red2=colorRed2Value.get()/255
        ChamsColor.green2=colorGreen2Value.get()/255
        ChamsColor.blue2=colorBlue2Value.get()/255
        ChamsColor.Apl2=colorA2Value.get()/255
    }
}
