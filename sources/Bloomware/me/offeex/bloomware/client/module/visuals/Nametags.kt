package me.offeex.bloomware.client.module.visuals

import me.offeex.bloomware.Bloomware.mc
import me.offeex.bloomware.api.helper.cPlayer
import me.offeex.bloomware.api.helper.cWorld
import me.offeex.bloomware.api.manager.managers.FriendManager
import me.offeex.bloomware.api.manager.managers.FriendManager.getType
import me.offeex.bloomware.api.structure.ColorMutable
import me.offeex.bloomware.api.util.EntityUtil.getFullHealth
import me.offeex.bloomware.api.util.MathUtil.getScale
import me.offeex.bloomware.api.util.PlayerUtil.getGamemode
import me.offeex.bloomware.api.util.PlayerUtil.getPing
import me.offeex.bloomware.api.util.RenderUtil
import me.offeex.bloomware.api.util.RenderUtil.drawBackground3D
import me.offeex.bloomware.api.util.RenderUtil.drawItem3D
import me.offeex.bloomware.api.util.RenderUtil.drawText3D
import me.offeex.bloomware.api.util.RenderUtil.use
import me.offeex.bloomware.client.module.Module
import me.offeex.bloomware.client.setting.setting
import me.offeex.bloomware.client.setting.settings.SettingBool
import me.offeex.bloomware.event.dispatcher.Subscribe
import me.offeex.bloomware.event.events.EventRender
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.math.RotationAxis
import net.minecraft.world.GameMode

object Nametags : Module("Nametags", "Renders different info about interact above", Category.RENDER) {
    private val tagsHeight = setting("TagsHeight").number(0, 0, 1, 0.01)

    private val armor = setting("Armor").bool(true)
    private val health = setting("Health").bool(true)
    private val gamemode = setting("Gamemode").bool()
    private val ping = setting("Ping").bool(true)
    private val healthBar = setting("HealthBar").bool()
    private val distance = setting("Distance").bool()
    private val elementSpace = setting("ElementSpace").number(2, 0, 4, 1)
    private val borderWidth = setting("BorderWidth").number(2, 0, 4, 1)
    private val range = setting("Range").number(150, 1, 400, 5)
    private val color = setting("Color").color(0, 0, 0, 70)

    private val enemyColor = ColorMutable(255, 0, 0, 255)
    private val friendColor = ColorMutable(5, 235, 252, 255)
    private val grayColor = ColorMutable(150, 150, 150, 255)

    @Subscribe
    private fun onLabelRender(event: EventRender.Label) {
        event.canceled = true
    }

    @Subscribe
    private fun onWorldRender(event: EventRender.World) {
        if (!event.shift) return

        val matrices = event.matrices
        cWorld.players.forEach {
            if (cPlayer.distanceTo(it) > range.value) return
            if (it == cPlayer) return@forEach
//
            val pos = it.getLerpedPos(event.tickDelta).add(0.0, it.standingEyeHeight + 0.65 + tagsHeight.value, 0.0)
            val height = mc.textRenderer.fontHeight + 2

            matrices.use {
                RenderUtil.translateToCamera(matrices, pos)
                RenderUtil.rotateToCamera(matrices)
                RenderUtil.scale2D(matrices, getScale(it))

                val width = getWidth(it)
                val halfWidth = width / 2
                val bgHalfWidth = halfWidth + borderWidth.value.toInt()
                val drochka = if (width % 2 != 0) 0 else -1

                matrices.use {
                    matrices.translate(0.0, 0.05, 0.0001)
                    drawBackground3D(matrices, -bgHalfWidth, bgHalfWidth + drochka, 0, height, color.color)
                    if (healthBar.toggled) matrices.use {
                        matrices.translate(0.0, 0.05, 0.0)
                        drawBackground3D(matrices, -bgHalfWidth, bgHalfWidth + drochka, 0, 1, getHealthColor(it))
                    }
                }

                drawStrings(matrices, it, halfWidth.toFloat())
                if (armor.toggled) matrices.use {
                    matrices.translate(0.0, 0.15, 0.0)
                    drawItems(matrices, it)
                }
            }
        }
    }

    private fun drawStrings(matrices: MatrixStack, entity: PlayerEntity, halfWidth: Float) {
        var offset = -halfWidth

        fun draw(setting: SettingBool?, text: String, color: ColorMutable) {
            if (setting == null || setting.toggled) {
                drawText3D(matrices, text, offset, 0f, color)
                offset += mc.textRenderer.getWidth(text) + elementSpace.value.toInt()
            }
        }

        draw(ping, entity.getPing().toString() + "ms", grayColor)
        draw(gamemode, translateGamemode(entity.getGamemode()), ColorMutable.WHITE)
        draw(null, entity.entityName, getNicknameColor(entity))
        draw(health, entity.getFullHealth().toInt().toString(), getHealthColor(entity))
        draw(distance, String.format("%.1f", cPlayer.distanceTo(entity)) + "m", grayColor)
    }

    private fun drawItems(matrices: MatrixStack, entity: PlayerEntity) {
        val items: MutableList<ItemStack> = mutableListOf()

        if (!entity.mainHandStack.isEmpty) items.add(entity.mainHandStack)
        if (!entity.offHandStack.isEmpty) items.add(entity.offHandStack)
        entity.armorItems.filter { !it.isEmpty }.forEach { items.add(it) }

        val space = 0.5
        val totalSize = (items.size - 1) * space
        val negativeOffsetX = totalSize / -2.0
        for (i in items.indices) matrices.use {
            matrices.translate(negativeOffsetX + space * i, 0.2, 0.0)
            matrices.scale(0.5f, 0.5f, 0.5f)
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180f))
            drawItem3D(matrices, items[i])
        }
    }

    private fun getWidth(entity: PlayerEntity): Int {
        fun width(text: String) = mc.textRenderer.getWidth(text) + elementSpace.value.toInt()
        var length = mc.textRenderer.getWidth(entity.entityName)

        if (ping.toggled) length += width(entity.getPing().toString() + "ms")
        if (gamemode.toggled) length += width(translateGamemode(entity.getGamemode()))
        if (health.toggled) length += width(entity.getFullHealth().toInt().toString())
        if (distance.toggled) length += width(String.format("%.1f", cPlayer.distanceTo(entity)) + "m")
        return length
    }

    private fun getNicknameColor(entity: PlayerEntity) =
        if (getType(entity.entityName) == null) ColorMutable.WHITE else if (getType(entity.entityName) === FriendManager.PersonType.FRIEND) friendColor else enemyColor

    private fun translateGamemode(gamemode: GameMode?): String {
        return if (gamemode == null) "[BOT]" else when (gamemode) {
            GameMode.SURVIVAL -> "[S]"
            GameMode.CREATIVE -> "[C]"
            GameMode.SPECTATOR -> "[SP]"
            GameMode.ADVENTURE -> "[A]"
        }
    }

    private fun getHealthColor(entity: PlayerEntity): ColorMutable {
        val health = entity.getFullHealth().toInt()
        if (health in 8..15) return ColorMutable.YELLOW
        return if (health > 15) ColorMutable.GREEN else ColorMutable.RED
    }
}