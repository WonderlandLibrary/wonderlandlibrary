package me.offeex.bloomware.event.events

import me.offeex.bloomware.event.Event
import net.minecraft.entity.damage.DamageSource

abstract class EventDamage: Event() {
	class Armor(val source: DamageSource, val amount: Float): EventDamage()
}