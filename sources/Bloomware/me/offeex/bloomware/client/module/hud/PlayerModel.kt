package me.offeex.bloomware.client.module.hud

import me.offeex.bloomware.Bloomware.mc
import me.offeex.bloomware.api.helper.cPlayer
import me.offeex.bloomware.client.module.HudModule
import me.offeex.bloomware.client.setting.setting
import net.minecraft.client.gui.screen.ingame.InventoryScreen
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.MathHelper

object PlayerModel : HudModule("PlayerModel", "Renders interact model", Category.HUD) {
    private val scale = setting("Scale").number(3, 0.1, 5.0, 0.1)
    override fun draw(matrices: MatrixStack, tickDelta: Float) {
        width = (scale.value * 30).toInt()
        height = (scale.value * 46.2).toInt()
        InventoryScreen.drawEntity(
            matrices,
            x + width / 2,
            (y + height - scale.value * 5).toInt(),
            (scale.value * 20).toInt(),
            -MathHelper.wrapDegrees(cPlayer.prevYaw + (cPlayer.yaw - cPlayer.prevYaw) * mc.tickDelta),
            -cPlayer.pitch,
            cPlayer
        )
    }
}