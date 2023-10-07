// 
// Decompiled by Procyon v0.6.0
// 

package de.gerrygames.viarewind.protocol.protocol1_8to1_9.entityreplacement;

import java.util.List;
import com.viaversion.viaversion.api.type.types.version.Types1_8;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import java.util.ArrayList;
import com.viaversion.viaversion.api.protocol.Protocol;
import de.gerrygames.viarewind.utils.PacketUtil;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.Protocol1_8TO1_9;
import com.viaversion.viaversion.api.type.Type;
import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.protocol.packet.PacketType;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.protocols.protocol1_8.ClientboundPackets1_8;
import com.viaversion.viaversion.api.connection.UserConnection;
import de.gerrygames.viarewind.replacement.EntityReplacement;

public abstract class EntityReplacement1_8to1_9 implements EntityReplacement
{
    protected final UserConnection user;
    
    protected EntityReplacement1_8to1_9(final UserConnection user) {
        this.user = user;
    }
    
    protected void sendTeleportWithHead(final int entityId, final double locX, final double locY, final double locZ, final float yaw, final float pitch, final float headYaw) {
        this.sendTeleport(entityId, locX, locY, locZ, yaw, pitch);
        this.sendHeadYaw(entityId, headYaw);
    }
    
    protected void sendTeleport(final int entityId, final double locX, final double locY, final double locZ, final float yaw, final float pitch) {
        final PacketWrapper teleport = PacketWrapper.create(ClientboundPackets1_8.ENTITY_TELEPORT, null, this.user);
        teleport.write(Type.VAR_INT, entityId);
        teleport.write(Type.INT, (int)(locX * 32.0));
        teleport.write(Type.INT, (int)(locY * 32.0));
        teleport.write(Type.INT, (int)(locZ * 32.0));
        teleport.write(Type.BYTE, (byte)(yaw / 360.0f * 256.0f));
        teleport.write(Type.BYTE, (byte)(pitch / 360.0f * 256.0f));
        teleport.write(Type.BOOLEAN, true);
        PacketUtil.sendPacket(teleport, Protocol1_8TO1_9.class, true, true);
    }
    
    protected void sendHeadYaw(final int entityId, final float headYaw) {
        final PacketWrapper head = PacketWrapper.create(ClientboundPackets1_8.ENTITY_HEAD_LOOK, null, this.user);
        head.write(Type.VAR_INT, entityId);
        head.write(Type.BYTE, (byte)(headYaw / 360.0f * 256.0f));
        PacketUtil.sendPacket(head, Protocol1_8TO1_9.class, true, true);
    }
    
    protected void sendSpawn(final int entityId, final int type) {
        final PacketWrapper spawn = PacketWrapper.create(ClientboundPackets1_8.SPAWN_MOB, null, this.user);
        spawn.write(Type.VAR_INT, entityId);
        spawn.write(Type.UNSIGNED_BYTE, (short)type);
        spawn.write(Type.INT, 0);
        spawn.write(Type.INT, 0);
        spawn.write(Type.INT, 0);
        spawn.write(Type.BYTE, (Byte)0);
        spawn.write(Type.BYTE, (Byte)0);
        spawn.write(Type.BYTE, (Byte)0);
        spawn.write(Type.SHORT, (Short)0);
        spawn.write(Type.SHORT, (Short)0);
        spawn.write(Type.SHORT, (Short)0);
        final List<Metadata> list = new ArrayList<Metadata>();
        spawn.write(Types1_8.METADATA_LIST, list);
        PacketUtil.sendPacket(spawn, Protocol1_8TO1_9.class, true, true);
    }
    
    protected void sendSpawnEntity(final int entityId, final int type) {
        final PacketWrapper spawn = PacketWrapper.create(ClientboundPackets1_8.SPAWN_ENTITY, null, this.user);
        spawn.write(Type.VAR_INT, entityId);
        spawn.write(Type.BYTE, (byte)type);
        spawn.write(Type.INT, 0);
        spawn.write(Type.INT, 0);
        spawn.write(Type.INT, 0);
        spawn.write(Type.BYTE, (Byte)0);
        spawn.write(Type.BYTE, (Byte)0);
        spawn.write(Type.INT, 0);
        PacketUtil.sendPacket(spawn, Protocol1_8TO1_9.class, true, true);
    }
}
