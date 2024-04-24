package me.offeex.bloomware.api.helper

import me.offeex.bloomware.Bloomware.mc

inline val cPlayer get() = mc.player!!
inline val cWorld get() = mc.world!!
inline val cNetHandler get() = mc.networkHandler!!
inline val cInteractManager get() = mc.interactionManager!!