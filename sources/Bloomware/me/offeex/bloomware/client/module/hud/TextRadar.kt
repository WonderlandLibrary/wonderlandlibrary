package me.offeex.bloomware.client.module.hud

import me.offeex.bloomware.api.helper.cPlayer
import me.offeex.bloomware.api.helper.cWorld
import me.offeex.bloomware.api.manager.managers.FontManagerr
import me.offeex.bloomware.client.module.HudModule
import me.offeex.bloomware.client.module.client.Colors
import me.offeex.bloomware.client.setting.setting
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Formatting.*

object TextRadar : HudModule("TextRadar", "Shows players around you", Category.HUD) {
    private val showHealth = setting("Health").bool(true)
    private val showDistance = setting("Distance").bool(true)
    private val range = setting("Range").number(100, 1, 200, 1)
    private val maxCount = setting("MaxCount").number(15, 1, 100, 1)

    override fun draw(matrices: MatrixStack, tickDelta: Float) {
        var offsetY = 0
        var longest = ""
        cWorld.players.forEach {
            if (cPlayer.distanceTo(it) <= range.value && offsetY <= maxCount.value && it !== cPlayer) {

                var string = it.entityName
                if (showDistance.toggled) string += "   [$AQUA${cPlayer.distanceTo(it) to 1}$WHITE]"
                if (showHealth.toggled) string += " ($RED${it.health to 1}$WHITE)"

                if (FontManagerr.width(string) > FontManagerr.width(longest)) longest = string

                val ass = string.replace(',', '.')
                FontManagerr.drawString(matrices, ass, x, y + 12 * offsetY - 4, Colors.hud.color)
                offsetY++
            }
            width = FontManagerr.width(longest.erase("§b", "§f", "§c")).toInt()
            height = 12 * offsetY
        }
    }

    private fun String.erase(vararg s: String): String {
        var result = this
        s.forEach {
            result = result.replace(it, "")
        }
        return result
    }
}