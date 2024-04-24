package me.offeex.bloomware.client.module.world

import me.offeex.bloomware.Bloomware.mc
import me.offeex.bloomware.api.helper.cPlayer
import me.offeex.bloomware.api.helper.cWorld
import me.offeex.bloomware.api.structure.ColorMutable
import me.offeex.bloomware.api.util.EntityUtil.getOwnerUUID
import me.offeex.bloomware.api.util.MathUtil.getScale
import me.offeex.bloomware.api.util.RenderUtil
import me.offeex.bloomware.api.util.RenderUtil.drawBackground3D
import me.offeex.bloomware.api.util.RenderUtil.drawText3D
import me.offeex.bloomware.api.util.RenderUtil.use
import me.offeex.bloomware.client.module.Module
import me.offeex.bloomware.client.setting.setting
import me.offeex.bloomware.event.dispatcher.Subscribe
import me.offeex.bloomware.event.events.EventEntity
import me.offeex.bloomware.event.events.EventRender
import me.offeex.bloomware.event.events.EventScreen
import net.minecraft.client.gui.screen.DeathScreen
import net.minecraft.entity.LivingEntity

object MobOwner : Module("MobOwner", "Renders mob's owner", Category.WORLD) {
    private val range = setting("Range").number(50.0, 1.0, 100.0, 1.0)
    private val color = setting("Color").color(0, 0, 0, 70)
    private val entities = HashMap<LivingEntity, String>()

    @Subscribe
    private fun onOpenScreen(event: EventScreen.Open) {
        if (event.screen is DeathScreen) entities.clear()
    }

    @Subscribe
    private fun onEntityRemoved(event: EventEntity.Remove) {
        entities.remove(cWorld.getEntityById(event.id))
    }

    @Subscribe
    private fun onWorldRender(event: EventRender.World) {
        for (it in cWorld.entities) {
            if (it !is LivingEntity || cPlayer.distanceTo(it) > range.value || entities.containsKey(it)) continue
            val uuid = it.getOwnerUUID() ?: continue
            entities[it] = uuid.toString()
        }

        val matrices = event.matrices
        for ((key, value) in entities) matrices.use {
            if (cPlayer.distanceTo(key) > range.value || cPlayer.vehicle === key) return@use
            val pos = key.getLerpedPos(event.tickDelta).add(0.0, key.height + 0.75, 0.0)
            val halfWidth = (mc.textRenderer.getWidth("Owned by $value") + 4) / 2

            RenderUtil.translateToCamera(matrices, pos)
            RenderUtil.rotateToCamera(matrices)
            RenderUtil.scale2D(matrices, getScale(key))

            drawBackground3D(event.matrices, -halfWidth, halfWidth, 0, 14, color.color)
            drawText3D(event.matrices, "Owned by $value", -halfWidth + 2f,  0f, ColorMutable.WHITE)
        }
    }
}