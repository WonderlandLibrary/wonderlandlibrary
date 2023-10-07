/*
* Copyright (C) 2022 Hypnotic Development
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package sudo.events;

import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import sudo.core.event.Event;

public class EventSendPacket extends Event {

	public Packet<?> packet;
	
	public EventSendPacket(Packet<?> packet) {
		this.packet = packet;
	}
	
	public Packet<?> getPacket() {
		return packet;
	}

	public void setPacket(PlayerMoveC2SPacket packet) {
		this.packet = packet;
	}
}
