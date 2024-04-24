package me.offeex.bloomware.client.module.pvp

import me.offeex.bloomware.api.helper.cNetHandler
import me.offeex.bloomware.api.helper.cPlayer
import me.offeex.bloomware.client.module.Module
import me.offeex.bloomware.client.setting.setting
import me.offeex.bloomware.event.dispatcher.Subscribe
import me.offeex.bloomware.event.events.EventUpdate
import net.minecraft.item.Items
import net.minecraft.text.Text

object AutoLog : Module("AutoLog", "Disconnects you when your health is low.", Category.PVP) {
	private val autoDisable = setting("AutoDisable").bool(true)
	private val health = setting("Health").number(2.0, 1.0, 20.0, 1.0)
	private val totemCheck = setting("TotemCheck").bool(true)
	private var hp = 0f

	@Subscribe
	private fun onUpdateHealth(event: EventUpdate.Health) {
		if (event.health >= health.value) return
		hp = event.health
		if (totemCheck.toggled) {
			if (cPlayer.inventory.count(Items.TOTEM_OF_UNDYING) == 0) log()
		} else log()
	}

	private fun log() {
		cNetHandler.connection.disconnect(Text.of("Logged with health left: $hp. Reason: AutoLog"))
		if (autoDisable.toggled) disable()
	}
}