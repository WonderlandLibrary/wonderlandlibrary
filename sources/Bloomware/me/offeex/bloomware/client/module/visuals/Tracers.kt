package me.offeex.bloomware.client.module.visuals

import me.offeex.bloomware.Bloomware.mc
import me.offeex.bloomware.api.helper.cPlayer
import me.offeex.bloomware.api.helper.cWorld
import me.offeex.bloomware.api.manager.managers.FriendManager.PersonType
import me.offeex.bloomware.api.manager.managers.FriendManager.getType
import me.offeex.bloomware.api.structure.ColorMutable
import me.offeex.bloomware.api.util.RenderUtil.drawLine
import me.offeex.bloomware.client.module.Module
import me.offeex.bloomware.client.setting.setting
import me.offeex.bloomware.client.setting.settings.SettingColor
import me.offeex.bloomware.event.dispatcher.Subscribe
import me.offeex.bloomware.event.events.EventRender
import net.minecraft.client.render.VertexFormat
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d

object Tracers : Module("Tracers", "Renders lines to players", Category.RENDER) {
	private val mode = setting("Mode").enum("2D", "3D")
	private val range = setting("Range").number(150.0, 1.0, 200.0, 1.0)
	private val width = setting("Width").number(2.0, 1.0, 5.0, 0.1)
	private val colorMode = setting("ColorMode").enum("Static", "Distance")
	private val bodyPart = setting("BodyPart").enum("Head", "Chest", "Legs")
	private val colors = setting("Colors").group()
	private val colorPlayers = colors.setting("Players").color(20, 255, 0, 255)
	private val colorFriends = colors.setting("Friends").color(0, 255, 255, 255)
	private val colorEnemies = colors.setting("Enemies").color(255, 0, 0, 255)
	private val colorsDistance =
		colors.setting("Colors").group().depend(colorMode) { colorMode.like("Distance") }
	private val colorClose = colorsDistance.setting("ColorClose").color(ColorMutable.RED)
		.depend(colorMode) { colorMode.like("Distance") }
	private val colorFar = colorsDistance.setting("ColorFar").color(ColorMutable.GREEN)
		.depend(colorMode) { colorMode.like("Distance") }
	private val maxDist = colorsDistance.setting("MaxDistance").number(60.0, 10.0, 120.0, 1.0)

	@Subscribe
	private fun onWorldRender(event: EventRender.World) {
		cWorld.entities.forEach {
			if (it !is PlayerEntity || it === cPlayer || cPlayer.distanceTo(it) > range.value) return@forEach
			val startPos =
				Vec3d(0.0, 0.0, 1.0).rotateX(-Math.toRadians(mc.gameRenderer.camera.pitch.toDouble()).toFloat())
					.rotateY(-Math.toRadians(mc.gameRenderer.camera.yaw.toDouble()).toFloat())
			val endPos = it.getLerpedPos(event.tickDelta).add(0.0, getYOffset(it), 0.0)
			val c = if (colorMode.like("Distance")) fade(
				colorFar, colorClose, MathHelper.clamp(cPlayer.distanceTo(it) / maxDist.value.toFloat(), 0f, 1f)
			) else getColor(it)
			drawLine(
				event.matrices,
				startPos,
				endPos,
				c,
				width.value,
				if (mode.like("2D")) VertexFormat.DrawMode.DEBUG_LINES else VertexFormat.DrawMode.LINES
			)
		}
	}

	private fun getYOffset(player: PlayerEntity): Double {
		return when (bodyPart.selected) {
			"Head" -> player.standingEyeHeight.toDouble()
			"Chest" -> player.standingEyeHeight * 0.5
			else -> 0.0
		}
	}

	private fun getColor(player: PlayerEntity): ColorMutable {
		val type = getType(player.entityName)
		return if (type == null) colorPlayers.color else if (type === PersonType.FRIEND) colorFriends.color else colorEnemies.color
	}

	private fun fade(c1: SettingColor, c2: SettingColor, progress: Float): ColorMutable {
		return ColorMutable(
			(c1.color.red * progress + c2.color.red * (1 - progress)).toInt(),
			(c1.color.green * progress + c2.color.green * (1 - progress)).toInt(),
			(c1.color.blue * progress + c2.color.blue * (1 - progress)).toInt(),
			(c1.color.alpha * progress + c2.color.alpha * (1 - progress)).toInt()
		)
	}
}