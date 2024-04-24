package me.offeex.bloomware.client.module.hud

import me.offeex.bloomware.api.helper.cPlayer
import me.offeex.bloomware.api.helper.cWorld
import me.offeex.bloomware.api.manager.managers.HoleManager
import me.offeex.bloomware.api.util.RenderUtil.drawItem2D
import me.offeex.bloomware.client.module.HudModule
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos

object HoleInfo : HudModule("HoleInfo", "Shows hole info", Category.HUD) {
    init {
        width = 38
        height = 37
    }

    override fun draw(matrices: MatrixStack, tickDelta: Float) {
        if (HoleManager.has(cPlayer.blockPos)) {
            drawItem2D(matrices, getStack(cPlayer.blockPos.west()), x + 3, y + 20, false)
            drawItem2D(matrices, getStack(cPlayer.blockPos.north()), x + 20, y + 1, false)
            drawItem2D(matrices, getStack(cPlayer.blockPos.east()), x + 38, y + 20, false)
            drawItem2D(matrices, getStack(cPlayer.blockPos.south()), x + 20, y + 37, false)
        }
    }

    private fun getStack(pos: BlockPos): ItemStack {
        return ItemStack(cWorld.getBlockState(pos).block)
    }
}