/*
 * GlassClient PVP Client
 * A free open-source mixin-based PVP client based on liquidbounce with all cheats removed.
 * https://github.com/GlassClient/GlassClient
 */
package net.ccbluex.liquidbounce.features.module.modules.mods

import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.value.BoolValue

@ModuleInfo(name = "Performance", category = ModuleCategory.MODS)
object Performance : Module() {
    @JvmField
    var staticParticleColorValue = BoolValue("StaticParticleColor", false)
    @JvmField
    var fastEntityLightningValue = BoolValue("FastEntityLightning", false)
    @JvmField
    var fastBlockLightningValue = BoolValue("FastBlockLightning", false)
}

