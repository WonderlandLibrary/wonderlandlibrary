package me.offeex.bloomware.client.module.client

import me.offeex.bloomware.Bloomware.NAME
import me.offeex.bloomware.Bloomware.VERSION
import me.offeex.bloomware.Bloomware.mc
import me.offeex.bloomware.api.gui.screen.streamermode.StreamerWindowFrame
import me.offeex.bloomware.api.helper.cPlayer
import me.offeex.bloomware.client.module.Module
import me.offeex.bloomware.client.setting.setting
import me.offeex.bloomware.mixin.accessor.IMinecraftClient
import net.minecraft.util.math.Direction.AxisDirection.POSITIVE
import java.awt.EventQueue

object StreamerMode : Module("StreamerMode", "Shows private info in different window", Category.CLIENT) {
	private val hide = setting("Hide").group()
	val hideCoords = hide.setting("Coordinates").bool(true)
	val hideNickname = hide.setting("Nickname").bool(true)
	private lateinit var window: StreamerWindowFrame

	override fun onEnable() {
		window = StreamerWindowFrame()
		EventQueue.invokeLater {
			window.isVisible = true
		}
	}

	override fun onDisable() {
		window.isVisible = false
	}

	override fun onTick() {
		cPlayer.apply {
			val draw = listOf(
				"$NAME $VERSION",
				" ",
				"FPS: ${(mc as IMinecraftClient).currentFps}",
				"Direction: " + (if (horizontalFacing.direction == POSITIVE) "+" else "-") + horizontalFacing.axis.getName()
					.lowercase(),
				"XYZ: " + x.toInt() + ", " + y.toInt() + ", " + z.toInt()
			)
			window.setStrings(draw)
		}
	}
}