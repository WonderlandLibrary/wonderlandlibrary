// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.protocol.protocol1_8to1_9.storage;

import com.viaversion.viaversion.api.protocol.Protocol;
import de.gerrygames.viarewind.utils.PacketUtil;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.Protocol1_8TO1_9;
import com.viaversion.viaversion.api.type.Type;
import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.connection.UserConnection;
import de.gerrygames.viarewind.utils.Tickable;
import com.viaversion.viaversion.api.connection.StoredObject;

public class Levitation extends StoredObject implements Tickable
{
    private int amplifier;
    private volatile boolean active;
    
    public Levitation(final UserConnection user) {
        super(user);
        this.active = false;
    }
    
    @Override
    public void tick() {
        if (!this.active) {
            return;
        }
        final int vY = (this.amplifier + 1) * 360;
        final PacketWrapper packet = PacketWrapper.create(18, null, this.getUser());
        packet.write(Type.VAR_INT, this.getUser().get(EntityTracker.class).getPlayerId());
        packet.write(Type.SHORT, (Short)0);
        packet.write(Type.SHORT, (short)vY);
        packet.write(Type.SHORT, (Short)0);
        PacketUtil.sendPacket(packet, Protocol1_8TO1_9.class);
    }
    
    public void setActive(final boolean active) {
        this.active = active;
    }
    
    public void setAmplifier(final int amplifier) {
        this.amplifier = amplifier;
    }
}
