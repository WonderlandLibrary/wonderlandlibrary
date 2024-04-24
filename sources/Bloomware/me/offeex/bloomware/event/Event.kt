package me.offeex.bloomware.event

import me.offeex.bloomware.Bloomware
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

abstract class Event(var canceled: Boolean = false, var shift: Boolean = false) {
	fun <T : Event> post(): T {
		Bloomware.EVENTBUS.post(this as T)
		return this
	}

	fun <T : Event> post(ci: CallbackInfo): T {
		val post: T = post()
		if (canceled) ci.cancel()
		return post
	}

	/**
	 * Sets shift to POST
	 */
	fun <T : Event> shift(): T {
		this.shift = true
		return this as T
	}
}