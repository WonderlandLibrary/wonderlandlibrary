// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.event.impl;

import net.minecraft.network.play.server.S3FPacketCustomPayload;
import moonsense.event.SCEvent;

public class SCCustomPacketPayloadEvent extends SCEvent
{
    public final S3FPacketCustomPayload packet;
    
    public SCCustomPacketPayloadEvent(final S3FPacketCustomPayload packet) {
        this.packet = packet;
    }
}
