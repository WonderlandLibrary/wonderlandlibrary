package me.offeex.bloomware.client.module.hud

import me.offeex.bloomware.Bloomware.mc
import me.offeex.bloomware.api.manager.managers.FontManagerr
import me.offeex.bloomware.api.manager.managers.ModuleManager
import me.offeex.bloomware.client.module.HudModule
import me.offeex.bloomware.client.module.client.Colors
import me.offeex.bloomware.client.setting.setting
import net.minecraft.client.util.math.MatrixStack

object ModuleList : HudModule("ModuleList", "Shows enabled modules", Category.HUD) {
    private val sortMode = setting("SortMode").enum("Length", "Alphabet")
    private val renderMode = setting("RenderMode").enum("Normal", "Uppercase", "Lowercase")

    override fun draw(matrices: MatrixStack, tickDelta: Float) {
        val modules =
            ModuleManager.modules.filter { it.enabled }.map { mode.get(it.name) }.toMutableList()
        if (sortMode.like("Alphabet")) modules.sortBy { it }
        else {
            modules.sortBy { FontManagerr.width(it) }
            if (y <= mc.window.scaledHeight / 2 - width) modules.reverse()
        }
        var offset = 0
        modules.forEach {
            FontManagerr.drawString(
                matrices, mode.get(it), getOffset(x, width, it), y + offset - 6, Colors.hud.color
            )
            offset += 10
        }
        width = FontManagerr.width(getLongest(modules)).toInt() + 4
        height = modules.size * 10
    }

    private fun getOffset(x: Int, width: Int, module: String) =
        if (x >= mc.window.scaledWidth / 2 - width) x + width - mode.width(module).toInt()
        else x + 2

    private fun getLongest(modules: List<String>) = modules.maxBy { mode.width(it) }.ifEmpty { "" }

    private val mode
        get() = Modes.valueOf(renderMode.selected)

    enum class Modes {
        Normal {
            override fun get(name: String) = name
        },
        Uppercase {
            override fun get(name: String) = name.uppercase()
        },
        Lowercase {
            override fun get(name: String) = name.lowercase()
        };

        abstract fun get(name: String): String
        fun width(name: String): Float = FontManagerr.width(get(name))
    }
}
