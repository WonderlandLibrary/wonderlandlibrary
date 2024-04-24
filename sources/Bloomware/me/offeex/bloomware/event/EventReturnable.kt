package me.offeex.bloomware.event

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

abstract class EventReturnable : Event() {
    var cirValue: Any? = null

    fun <T : Event> post(cir: CallbackInfoReturnable<Any?>) = post<T>(cir as CallbackInfo).also {
        if (cirValue != null) cir.returnValue = cirValue
    }
}