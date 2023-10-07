package koks.event;

import koks.api.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;

/**
 * @author kroko
 * @created on 21.01.2021 : 10:29
 */

@Getter @Setter @AllArgsConstructor
public class PacketEvent extends Event {
    final Type type;
    Packet<?> packet;
    INetHandler iNetHandler;

    public enum Type {
        SEND, RECEIVE;
    }

}
