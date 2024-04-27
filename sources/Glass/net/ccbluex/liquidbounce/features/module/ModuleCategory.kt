/*
 * GlassClient PVP Client
 * A free open-source mixin-based PVP client based on liquidbounce with all cheats removed.
 * https://github.com/GlassClient/GlassClient
 */
package net.ccbluex.liquidbounce.features.module

import lombok.Getter
import net.ccbluex.liquidbounce.launch.data.modernui.clickgui.utils.normal.Main
import net.ccbluex.liquidbounce.launch.data.modernui.clickgui.utils.objects.Drag
import net.ccbluex.liquidbounce.launch.data.modernui.clickgui.utils.render.Scroll

enum class ModuleCategory(val displayName: String, val configName: String, val htmlIcon: String) {
    MODS("%module.category.mods%", "Mods", "&#xe5d3;"),
    BANABLE("%module.category.banable%", "Banable", "&#xe868;"),
    COSMETICS("%module.category.cosmetics%", "Cosmetics", "&#xe868;"),
    CLIENT("%module.category.client%", "Client", "&#xe869;");

    var namee: String? = null
    var posX = 0
    var expanded = false

    @Getter
    val scroll: Scroll = Scroll()

    @Getter
    var drag: Drag? = null
    var posY = 20

    open fun ModuleCategory(name: String?) {
        namee = name
        posX = 40 + Main.categoryCount * 120
        drag = Drag(posX.toFloat(), posY.toFloat())
        expanded = true
        Main.categoryCount++
    }

}