package me.offeex.bloomware.client.module

import me.offeex.bloomware.Bloomware
import me.offeex.bloomware.Bloomware.mc
import me.offeex.bloomware.api.gui.screen.ClickGUI
import me.offeex.bloomware.api.manager.managers.FontManagerr
import me.offeex.bloomware.event.dispatcher.Subscribe
import me.offeex.bloomware.event.events.EventRender
import net.minecraft.client.util.math.MatrixStack

abstract class HudModule(n: String, d: String, c: Category) : Module(n, d, c) {
	var x = 10
	var y = 100
	var width = 30
	var height = 12
	var dragX = 0
	var dragY = 0
	private var dragging = false

	@Subscribe
	fun onDrawOverlay(event: EventRender.HUD) {
		if (mc.currentScreen !== Bloomware.hud) draw(event.matrices, event.tickDelta)
	}

	abstract fun draw(matrices: MatrixStack, tickDelta: Float)

	fun updatePosition(mouseX: Int, mouseY: Int) {
		if (dragging) {
			x = 0.coerceAtLeast((mc.window.scaledWidth - width).coerceAtMost(mouseX - dragX))
			y = 0.coerceAtLeast((mc.window.scaledHeight - height).coerceAtMost(mouseY - dragY))
		}
	}

	fun setDragging(dragging: Boolean) {
		if (dragging && ClickGUI.dragging == null) {
			ClickGUI.dragging = this
			this.dragging = true
		} else {
			if (ClickGUI.dragging === this) ClickGUI.dragging = null
			this.dragging = false
		}
	}

	fun isHovered(x: Double, y: Double) =
		x in (this.x - 3.0..this.x + width + 3.0) && y in (this.y - 3.0..this.y + height + 0.0)

	protected fun getOffsetBottom(text: String) = height - FontManagerr.height(text).toInt() - 3

	protected infix fun Double.to(decimalPlace: Int) = String.format("%." + decimalPlace + "f", this)
	protected infix fun Float.to(decimalPlace: Int) = String.format("%." + decimalPlace + "f", this)
}