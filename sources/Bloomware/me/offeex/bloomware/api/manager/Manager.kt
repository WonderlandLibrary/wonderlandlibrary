package me.offeex.bloomware.api.manager

import me.offeex.bloomware.Bloomware.EVENTBUS

abstract class Manager protected constructor() {
    init {
	    EVENTBUS.register(this)
    }
}
