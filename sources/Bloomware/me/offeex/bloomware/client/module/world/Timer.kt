package me.offeex.bloomware.client.module.world

import me.offeex.bloomware.api.manager.managers.ModuleManager
import me.offeex.bloomware.api.manager.managers.SessionManager
import me.offeex.bloomware.client.module.Module
import me.offeex.bloomware.client.setting.setting

object Timer : Module("Timer", "Changes flow of time", Category.WORLD) {
	private val multiplier = setting("Multiplier").number(1.0, 0.0, 5.0, 0.1)

	init {
	    multiplier.valueUpdateBus.subscribe { _, newValue ->
	        ModuleManager.checkNull { SessionManager.timer = newValue.toFloat() }
	    }
	}

	override fun onEnable() {
		SessionManager.timer = multiplier.value.toFloat()
	}

	override fun onDisable() {
		SessionManager.timer = 1f
	}
}