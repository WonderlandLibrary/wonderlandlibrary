package me.offeex.bloomware.client.module.hud

import me.offeex.bloomware.Bloomware.mc
import me.offeex.bloomware.api.helper.cPlayer
import me.offeex.bloomware.api.helper.cWorld
import me.offeex.bloomware.api.manager.managers.FontManagerr
import me.offeex.bloomware.api.structure.ColorMutable
import me.offeex.bloomware.api.util.MathUtil.roundDecimal
import me.offeex.bloomware.api.util.PlayerUtil.getPing
import me.offeex.bloomware.api.util.RenderUtil.drawItem2D
import me.offeex.bloomware.client.module.HudModule
import me.offeex.bloomware.client.setting.setting
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.client.gui.screen.ingame.InventoryScreen
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.math.MathHelper
import java.awt.Color

object Target : HudModule("Target", "Target hud.", Category.HUD) {
    private val range = setting("Range").number(30, 1, 200, 1)
    private val self = setting("Self").bool()
    private var target: PlayerEntity? = null
    private var temp = 10000f
    private var found = false

    private fun getColor(max: Float, value: Float): Color? {
        val percent = (100 / (max / value)).toDouble()
        if (percent <= 30) return Color.RED
        if (30 < percent && percent <= 70) return Color.YELLOW
        return if (percent > 70) Color.GREEN else null
    }

    private fun getWidth(value: Float): Int {
        val percent = (100 / (target!!.maxHealth / value)).toDouble()
        return (170 / 100 * percent).toInt()
    }

    private fun update() {
        if (mc.world == null || mc.player == null) return
        if (self.toggled) {
            target = cPlayer
        }
        else {
            for (player in cWorld.players) {
                if (cPlayer.distanceTo(player) < range.value && cPlayer.distanceTo(player) < temp && player !== cPlayer) {
                    target = player
                    found = true
                    temp = cPlayer.distanceTo(player)
                }
            }
            if (!found) target = null else found = false
            temp = 10000f
        }
    }

    override fun draw(matrices: MatrixStack, tickDelta: Float) {
        width = 166
        height = 66
        if (target != null) {
            DrawableHelper.fill(matrices, x - 3, y - 4, x + 169, y + 66, Color(0, 0, 0, 100).rgb)
            FontManagerr.drawString(
                matrices, target!!.entityName, x + 10, y + 5, ColorMutable.WHITE
            )
            FontManagerr.drawString(
                matrices,
                "${roundDecimal(target!!.health + target!!.absorptionAmount, 1)} HP",
                x + 10,
                y + 16,
                ColorMutable.WHITE
            )
            FontManagerr.drawString(
                matrices, "${target!!.getPing()}ms", x + 50, y + 16, ColorMutable.WHITE
            )
            FontManagerr.drawString(
                matrices,
                "${roundDecimal(cPlayer.distanceTo(target), 1)}m",
                x + 90,
                y + 16,
                ColorMutable.WHITE
            )
            var i = 1
            for (item in target!!.armorItems) {
                drawItem2D(matrices, item!!, x + 10 * i + i * 10 - 10, y + 31, true)
                i++
            }
            drawItem2D(matrices, target!!.mainHandStack, x + 90, y + 31, true)
            drawItem2D(matrices, target!!.offHandStack, x + 110, y + 31, true)
            InventoryScreen.drawEntity(
                matrices,
                x + 150,
                y + 62,
                30,
                -MathHelper.wrapDegrees(target!!.prevYaw + (target!!.yaw - target!!.prevYaw) * mc.tickDelta),
                -target!!.pitch,
                target
            )
            DrawableHelper.fill(
                matrices,
                x - 3,
                y + height - 2,
                x + getWidth(target!!.absorptionAmount + target!!.health) - 6,
                y + height,
                getColor(36f, 100 / 36f * target!!.health + target!!.absorptionAmount)!!.rgb
            )
            update()
        }
        update()
    }
}