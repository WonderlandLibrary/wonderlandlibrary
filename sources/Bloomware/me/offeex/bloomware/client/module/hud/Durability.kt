package me.offeex.bloomware.client.module.hud

import me.offeex.bloomware.api.helper.cPlayer
import me.offeex.bloomware.api.manager.managers.FontManagerr
import me.offeex.bloomware.client.module.HudModule
import me.offeex.bloomware.client.module.client.Colors
import me.offeex.bloomware.client.setting.setting
import net.minecraft.client.util.math.MatrixStack

object Durability : HudModule("Durability", "Show's item durability", Category.HUD) {
    private val showMax = setting("ShowMax").bool(true)

    init {
        height *= 2
    }

    override fun draw(matrices: MatrixStack, tickDelta: Float) {
        val mainStack = cPlayer.mainHandStack
        val offStack = cPlayer.offHandStack
        val mainHand =
            "MainHand: " + (mainStack.maxDamage - mainStack.damage) + if (showMax.toggled) "/" + mainStack.maxDamage else ""
        val offHand =
            "OffHand: " + (offStack.maxDamage - offStack.damage) + if (showMax.toggled) "/" + offStack.maxDamage else ""
        width = FontManagerr.width(mainHand).coerceAtLeast(FontManagerr.width(offHand)).toInt()
        FontManagerr.drawString(matrices, mainHand, x, y - 4, Colors.hud.color)
        FontManagerr.drawString(
            matrices,
            offHand,
            x,
            y + getOffsetBottom(offHand) - 4,
            Colors.hud.color
        )
    }
}