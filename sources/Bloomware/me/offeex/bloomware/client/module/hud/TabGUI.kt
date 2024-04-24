package me.offeex.bloomware.client.module.hud

import me.offeex.bloomware.api.manager.managers.FontManagerr
import me.offeex.bloomware.api.manager.managers.ModuleManager.getModulesByCategory
import me.offeex.bloomware.api.structure.ColorMutable
import me.offeex.bloomware.client.module.HudModule
import me.offeex.bloomware.client.module.client.Colors
import me.offeex.bloomware.event.dispatcher.Subscribe
import me.offeex.bloomware.event.events.EventInput
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.client.util.math.MatrixStack
import org.lwjgl.glfw.GLFW

object TabGUI : HudModule("TabGUI", "It's tab gui lol", Category.HUD) {
    private var selectedCategory = 0
    private var selectedModule = -1
    private val darkBlack = ColorMutable(0, 0, 0, 70)

    override fun draw(matrices: MatrixStack, tickDelta: Float) {
        width = 76
        height = 128
        var offsetY = 0

        for (category in Category.values()) {
            DrawableHelper.fill(
                matrices,
                x - 3,
                y + offsetY - 4,
                x + width + 3,
                y + offsetY + 8,
                if (Category.values()[selectedCategory] === category) Colors.hud.color.argb else darkBlack.argb
            )
            FontManagerr.drawString(
                matrices,
                category.title,
                if (Category.values()[selectedCategory] === category) x + 4 else x + 2,
                y + offsetY - 6,
                ColorMutable.WHITE
            )
            offsetY += 12
        }
        offsetY = 0
        if (selectedModule != -1) {
            for (module in getModulesByCategory(Category.values()[selectedCategory])) {
                DrawableHelper.fill(
                    matrices,
                    x + width + 3,
                    y + offsetY - 4,
                    x + width + 104,
                    y + offsetY + 8,
                    if (getModulesByCategory(Category.values()[selectedCategory])[selectedModule] === module) Colors.hud.color.argb else darkBlack.argb
                )
                FontManagerr.drawString(
                    matrices,
                    module.name,
                    x + width + 6,
                    y + offsetY - 6,
                    if (module.enabled) Colors.hud.color else ColorMutable.WHITE
                )
                offsetY += 12
            }
        }
    }

    @Subscribe
    private fun onKeyPressed(event: EventInput.Key.Press) {
        when (event.key) {
            GLFW.GLFW_KEY_DOWN -> {
                if (selectedModule == -1) selectedCategory =
                    if (Category.values().size - 1 >= selectedCategory + 1) selectedCategory + 1 else 0
                else selectedModule = if (getModulesByCategory(
                        Category.values()[selectedCategory]
                    ).size - 1 >= selectedModule + 1
                ) selectedModule + 1
                else 0
            }

            GLFW.GLFW_KEY_UP -> {
                if (selectedModule == -1) selectedCategory =
                    if (selectedCategory == 0) Category.values().size - 1 else selectedCategory - 1
                else selectedModule = if (selectedModule == 0) getModulesByCategory(
                    Category.values()[selectedCategory]
                ).size - 1
                else selectedModule - 1
            }

            GLFW.GLFW_KEY_RIGHT -> {
                if (selectedModule == -1) selectedModule = 0
                else getModulesByCategory(Category.values()[selectedCategory])[selectedModule].toggle()
            }

            GLFW.GLFW_KEY_LEFT -> selectedModule = -1
        }
    }
}