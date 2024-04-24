package me.offeex.bloomware.event.events

import me.offeex.bloomware.event.Event
import net.minecraft.network.packet.Packet
import net.minecraft.network.listener.PacketListener

abstract class EventPacket(open val packet: Packet<*>) : Event() {
	class Send(p: Packet<*>) : EventPacket(p)
	class Receive(p: Packet<*>) : EventPacket(p)
}