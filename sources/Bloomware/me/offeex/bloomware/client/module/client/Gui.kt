package me.offeex.bloomware.client.module.client

import me.offeex.bloomware.Bloomware
import me.offeex.bloomware.Bloomware.mc
import me.offeex.bloomware.client.module.Module
import me.offeex.bloomware.client.setting.setting
import net.minecraft.client.gui.screen.TitleScreen
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen
import org.lwjgl.glfw.GLFW

object Gui : Module("ClickGUI", "It's gui", Category.CLIENT) {
	private val hue = setting("Hue").group()
	val hueUp = hue.setting("Up").color(0x21FF008F)
	val hueDown = hue.setting("Down").color(0x2FFF2900)

	val offset = setting("Offset").group()
	val categoryOffset = offset.setting("Category").enum("Center", "Right", "Left")
	val moduleOffset = offset.setting("Module").enum("Left", "Center", "Right")
	val enabledStyle = offset.setting("EnabledStyle").enum("Glow", "None", "Fill")

	val blendMode = setting("BlendMode").enum("Normal", "Add")
	val widthMode = setting("WidthMode").enum("2D", "3D")

	val particles = setting("Particles").group(true)

	val size = particles.setting("Size").group()
	val sizeValue = size.setting("Value").number(3, 1.0, 15)
	val sizeDiff = size.setting("Difference").number(2, 0.0, 5.0, 1.0)

	val speed = particles.setting("Speed").group()
	val speedValue = speed.setting("Value").number(4.0, 1.0, 25.0, 1.0)
	val speedDiff = speed.setting("Difference").number(3.0, 1.0, 10.0, 1.0)

	val wind = particles.setting("Wind").group()
	val windValue = wind.setting("Value").number(90.0, 45.0, 135.0, 1.0)
	val windDiff = wind.setting("Difference").number(5.0, 0.0, 25.0, 1.0)

	val colorP = particles.setting("Color").group()
	val colorMode = colorP.setting("Mode").enum("Static", "Random")
	val colorParticle = colorP.setting("Color").color(0x9CBDE751).depend(colorMode) { colorMode.like("Static") }

	val glow = particles.setting("Glow").bool()
	val amount = particles.setting("Amount").number(150.0, 10.0, 500.0, 1.0)

	init {
		key = GLFW.GLFW_KEY_RIGHT_SHIFT
	}

	/**
	 * Impl
	 * @see me.offeex.bloomware.api.gui.screen.ClickGUI
	 */
	override fun toggle() {
		if (!(mc.currentScreen is TitleScreen || mc.currentScreen is MultiplayerScreen)) mc.setScreen(Bloomware.gui) else Bloomware.currentScreen =
			Bloomware.gui
	}
}