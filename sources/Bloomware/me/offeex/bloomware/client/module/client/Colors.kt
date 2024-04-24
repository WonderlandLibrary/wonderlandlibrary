package me.offeex.bloomware.client.module.client

import me.offeex.bloomware.client.module.Module
import me.offeex.bloomware.client.setting.setting

object Colors : Module("Colors", "Configures colors", Category.CLIENT) {
	val bg = setting("Background").color(0, 0, 0, 100)

	val frame = setting("Frame").group()
	val topBar = frame.setting("TopBar").color(0xFF2C1C3D)
	val dragging = frame.setting("Dragging").color(0xFF15062C)
	val borders = frame.setting("Borders").color(0xFF3B2A5F)
	val list = frame.setting("List").color(0x690A0525)

	val module = setting("Module").group()
	val text = module.setting("Text").color(0xFFD995FF)
	val hovered = module.setting("Hovered").color(0xFF4E5DAA)
	val pressed = module.setting("Pressed").color(0xFF69093F)
	val slider = module.setting("Slider").color(0xFFC451C9)
	val hud = module.setting("Hud").color(0xFFD9C0FF)

	override fun onEnable() = disable()
}