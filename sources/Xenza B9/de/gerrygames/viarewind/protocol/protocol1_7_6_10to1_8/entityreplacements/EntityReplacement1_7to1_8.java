// 
// Decompiled by Procyon v0.6.0
// 

package de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.entityreplacements;

import java.util.ArrayList;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.types.Types1_7_6_10;
import com.viaversion.viaversion.api.protocol.Protocol;
import de.gerrygames.viarewind.utils.PacketUtil;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.Protocol1_7_6_10TO1_8;
import com.viaversion.viaversion.api.type.Type;
import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.protocol.packet.PacketType;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.ClientboundPackets1_7;
import com.viaversion.viaversion.api.connection.UserConnection;
import de.gerrygames.viarewind.replacement.EntityReplacement;

public abstract class EntityReplacement1_7to1_8 implements EntityReplacement
{
    protected final UserConnection user;
    
    public EntityReplacement1_7to1_8(final UserConnection user) {
        this.user = user;
    }
    
    protected void sendTeleportWithHead(final int entityId, final double locX, final double locY, final double locZ, final float yaw, final float pitch, final float headYaw) {
        this.sendTeleport(entityId, locX, locY, locZ, yaw, pitch);
        this.sendHeadYaw(entityId, headYaw);
    }
    
    protected void sendTeleport(final int entityId, final double locX, final double locY, final double locZ, final float yaw, final float pitch) {
        final PacketWrapper teleport = PacketWrapper.create(ClientboundPackets1_7.ENTITY_TELEPORT, null, this.user);
        teleport.write(Type.INT, entityId);
        teleport.write(Type.INT, (int)(locX * 32.0));
        teleport.write(Type.INT, (int)(locY * 32.0));
        teleport.write(Type.INT, (int)(locZ * 32.0));
        teleport.write(Type.BYTE, (byte)(yaw / 360.0f * 256.0f));
        teleport.write(Type.BYTE, (byte)(pitch / 360.0f * 256.0f));
        PacketUtil.sendPacket(teleport, Protocol1_7_6_10TO1_8.class, true, true);
    }
    
    protected void sendHeadYaw(final int entityId, final float headYaw) {
        final PacketWrapper head = PacketWrapper.create(ClientboundPackets1_7.ENTITY_HEAD_LOOK, null, this.user);
        head.write(Type.INT, entityId);
        head.write(Type.BYTE, (byte)(headYaw / 360.0f * 256.0f));
        PacketUtil.sendPacket(head, Protocol1_7_6_10TO1_8.class, true, true);
    }
    
    protected void sendSpawn(final int entityId, final int type, final double locX, final double locY, final double locZ) {
        final PacketWrapper spawn = PacketWrapper.create(ClientboundPackets1_7.SPAWN_MOB, null, this.user);
        spawn.write(Type.VAR_INT, entityId);
        spawn.write(Type.UNSIGNED_BYTE, (short)type);
        spawn.write(Type.INT, (int)(locX * 32.0));
        spawn.write(Type.INT, (int)(locY * 32.0));
        spawn.write(Type.INT, (int)(locZ * 32.0));
        spawn.write(Type.BYTE, (Byte)0);
        spawn.write(Type.BYTE, (Byte)0);
        spawn.write(Type.BYTE, (Byte)0);
        spawn.write(Type.SHORT, (Short)0);
        spawn.write(Type.SHORT, (Short)0);
        spawn.write(Type.SHORT, (Short)0);
        spawn.write((Type<ArrayList>)Types1_7_6_10.METADATA_LIST, new ArrayList());
        PacketUtil.sendPacket(spawn, Protocol1_7_6_10TO1_8.class, true, true);
    }
}
