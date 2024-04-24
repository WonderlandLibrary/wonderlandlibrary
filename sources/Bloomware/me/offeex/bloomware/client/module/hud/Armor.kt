package me.offeex.bloomware.client.module.hud

import me.offeex.bloomware.api.helper.cPlayer
import me.offeex.bloomware.api.util.RenderUtil.drawItem2D
import me.offeex.bloomware.client.module.HudModule
import me.offeex.bloomware.client.setting.setting
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.ItemStack

object Armor : HudModule("Armor", "Armor hud.", Category.HUD) {
    private val mode = setting("Mode").enum("Vertical", "Horizontal")
    private val drawMainHand = setting("MainHand").bool(true)
    private val drawOffhand = setting("Offhand").bool(true)
    private val items = mutableListOf<ItemStack>()

    var multiplier = 16

    override fun draw(matrices: MatrixStack, tickDelta: Float) {
        var offset = 0
        for (stackItem in cPlayer.armorItems) items.add(stackItem)
        items.reverse()
        for (item in items) {
            if (!item.isEmpty) {
                drawItem2D(matrices, item, getX(offset), getY(offset), true)
                offset++
            }
        }
        if (drawMainHand.toggled && !cPlayer.mainHandStack.isEmpty) {
            drawItem2D(matrices, cPlayer.mainHandStack, getX(offset), getY(offset), true)
            offset++
        }
        if (drawOffhand.toggled && !cPlayer.offHandStack.isEmpty) {
            drawItem2D(matrices, cPlayer.offHandStack, getX(offset), getY(offset), true)
            offset++
        }
        width = if (mode.like("Horizontal")) offset * multiplier else multiplier
        height = if (mode.like("Vertical")) offset * multiplier else multiplier
        items.clear()
    }

    private fun getX(offset: Int) = if (mode.like("Horizontal")) x + offset * multiplier else x
    private fun getY(offset: Int) =
        if (mode.like("Vertical")) y - 2 + offset * multiplier else y - 2
}