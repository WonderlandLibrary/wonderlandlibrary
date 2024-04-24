package me.offeex.bloomware.event.events

import me.offeex.bloomware.event.Event
import net.minecraft.client.particle.SpriteBillboardParticle

class EventParticle(
    var particle: SpriteBillboardParticle, var velocityX: Double, var velocityY: Double, var velocityZ: Double) : Event()