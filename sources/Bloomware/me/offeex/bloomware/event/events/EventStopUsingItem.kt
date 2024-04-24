package me.offeex.bloomware.event.events

import me.offeex.bloomware.event.Event
import net.minecraft.entity.player.PlayerEntity

class EventStopUsingItem(val player: PlayerEntity) : Event()