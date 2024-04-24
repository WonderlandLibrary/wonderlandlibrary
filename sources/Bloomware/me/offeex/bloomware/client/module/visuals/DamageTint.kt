package me.offeex.bloomware.client.module.visuals

import me.offeex.bloomware.Bloomware.mc
import me.offeex.bloomware.api.helper.cInteractManager
import me.offeex.bloomware.api.util.RenderUtil.drawVignette
import me.offeex.bloomware.client.module.Module
import me.offeex.bloomware.client.setting.setting

object DamageTint : Module("DamageTint", "Shows red overlay when your health is low", Category.RENDER) {
	val health = setting("Threshold").number(12.0, 1.0, 36.0, 1.0)
	val power = setting("Amplifier").number(1.0, 0.0, 1.0, 0.1)

	fun draw() {
		if (mc.player == null || mc.interactionManager == null || !enabled) return

		val threshold = health.value.toFloat()
		val power = power.value.toFloat()
		if (cInteractManager.currentGameMode.isSurvivalLike && enabled) drawVignette(threshold, power)
	}
}