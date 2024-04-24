package me.offeex.bloomware.client.module.hud

import me.offeex.bloomware.Bloomware
import me.offeex.bloomware.Bloomware.mc
import me.offeex.bloomware.api.helper.cPlayer
import me.offeex.bloomware.api.util.RenderUtil.drawItem2D
import me.offeex.bloomware.client.module.HudModule
import me.offeex.bloomware.client.setting.setting
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.client.util.math.MatrixStack
import java.awt.Color

object InventoryViewer :
    HudModule("InventoryViewer", "Allows you to see your inventory.", Category.HUD) {
    private val background = setting("Background").bool(true)

    init {
        width = 153
        height = 51
    }

    override fun draw(matrices: MatrixStack, tickDelta: Float) {
        var offsetX = 0
        var offsetY = 0
        for (slot in 9..35) {
            drawItem2D(
                matrices,
                cPlayer.inventory.getStack(slot),
                x + offsetX * 17,
                y + offsetY * 17 - 2,
                true
            )
            offsetX++
            if ((slot + 1) % 9 == 0) {
                offsetY++
                offsetX = 0
            }
        }
        if (background.toggled && mc.currentScreen !== Bloomware.hud) DrawableHelper.fill(
            matrices, x, y, x + 153, y + 51, Color(0, 0, 0, 100).rgb
        )
    }
}