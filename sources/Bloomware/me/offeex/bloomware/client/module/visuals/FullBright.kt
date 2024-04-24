package me.offeex.bloomware.client.module.visuals

import me.offeex.bloomware.Bloomware.mc
import me.offeex.bloomware.api.helper.cPlayer
import me.offeex.bloomware.client.module.Module
import me.offeex.bloomware.client.setting.setting
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects

object FullBright : Module("FullBright", "Boosts your brightness", Category.RENDER) {
	val mode = setting("Mode").enum("Lightmap", "Potion")
	val color = setting("Color").color().depend(mode) { mode.like("Lightmap") }
	val skyBrightness = setting("SkyBrightness").number(0, 0, 1, 0.01).depend(mode) { mode.like("Lightmap") }
	private var oldBrightness = 1.0

	init {
		mode.selectedUpdateBus.subscribe { oldValue, _ ->
			if (oldValue == "Potion") mc.player?.removeStatusEffect(StatusEffects.NIGHT_VISION)
		}
	}

	override fun onEnable() {
		if (mode.like("Gamma")) {
			oldBrightness = mc.options.gamma.value
			mc.options.gamma.value = 1.0
		}
	}

	override fun onDisable() {
		mc.options.gamma.value = oldBrightness
		cPlayer.removeStatusEffect(StatusEffects.NIGHT_VISION)
	}

	override fun onTick() {
		if (mode.like("Potion")) cPlayer.addStatusEffect(
			StatusEffectInstance(StatusEffects.NIGHT_VISION, 500, 0)
		)
	}
}