// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.protocol.protocol1_8to1_9.entityreplacement;

import com.viaversion.viaversion.api.type.types.version.Types1_8;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.metadata.MetadataRewriter;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_10Types;
import com.viaversion.viaversion.api.minecraft.metadata.MetaType;
import com.viaversion.viaversion.api.minecraft.metadata.types.MetaType1_9;
import com.viaversion.viaversion.api.protocol.Protocol;
import de.gerrygames.viarewind.utils.PacketUtil;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.Protocol1_8TO1_9;
import com.viaversion.viaversion.api.type.Type;
import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import java.util.Iterator;
import java.util.ArrayList;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import java.util.List;
import de.gerrygames.viarewind.replacement.EntityReplacement;

public class ShulkerReplacement implements EntityReplacement
{
    private int entityId;
    private List<Metadata> datawatcher;
    private double locX;
    private double locY;
    private double locZ;
    private UserConnection user;
    
    public ShulkerReplacement(final int entityId, final UserConnection user) {
        this.datawatcher = new ArrayList<Metadata>();
        this.entityId = entityId;
        this.user = user;
        this.spawn();
    }
    
    @Override
    public void setLocation(final double x, final double y, final double z) {
        this.locX = x;
        this.locY = y;
        this.locZ = z;
        this.updateLocation();
    }
    
    @Override
    public void relMove(final double x, final double y, final double z) {
        this.locX += x;
        this.locY += y;
        this.locZ += z;
        this.updateLocation();
    }
    
    @Override
    public void setYawPitch(final float yaw, final float pitch) {
    }
    
    @Override
    public void setHeadYaw(final float yaw) {
    }
    
    @Override
    public void updateMetadata(final List<Metadata> metadataList) {
        for (final Metadata metadata : metadataList) {
            this.datawatcher.removeIf(m -> m.id() == metadata.id());
            this.datawatcher.add(metadata);
        }
        this.updateMetadata();
    }
    
    public void updateLocation() {
        final PacketWrapper teleport = PacketWrapper.create(24, null, this.user);
        teleport.write(Type.VAR_INT, this.entityId);
        teleport.write(Type.INT, (int)(this.locX * 32.0));
        teleport.write(Type.INT, (int)(this.locY * 32.0));
        teleport.write(Type.INT, (int)(this.locZ * 32.0));
        teleport.write(Type.BYTE, (Byte)0);
        teleport.write(Type.BYTE, (Byte)0);
        teleport.write(Type.BOOLEAN, true);
        PacketUtil.sendPacket(teleport, Protocol1_8TO1_9.class, true, true);
    }
    
    public void updateMetadata() {
        final PacketWrapper metadataPacket = PacketWrapper.create(28, null, this.user);
        metadataPacket.write(Type.VAR_INT, this.entityId);
        final List<Metadata> metadataList = new ArrayList<Metadata>();
        for (final Metadata metadata : this.datawatcher) {
            if (metadata.id() != 11 && metadata.id() != 12) {
                if (metadata.id() == 13) {
                    continue;
                }
                metadataList.add(new Metadata(metadata.id(), metadata.metaType(), metadata.getValue()));
            }
        }
        metadataList.add(new Metadata(11, MetaType1_9.VarInt, 2));
        MetadataRewriter.transform(Entity1_10Types.EntityType.MAGMA_CUBE, metadataList);
        metadataPacket.write(Types1_8.METADATA_LIST, metadataList);
        PacketUtil.sendPacket(metadataPacket, Protocol1_8TO1_9.class);
    }
    
    @Override
    public void spawn() {
        final PacketWrapper spawn = PacketWrapper.create(15, null, this.user);
        spawn.write(Type.VAR_INT, this.entityId);
        spawn.write(Type.UNSIGNED_BYTE, (Short)62);
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
        list.add(new Metadata(0, MetaType1_9.Byte, 0));
        spawn.write(Types1_8.METADATA_LIST, list);
        PacketUtil.sendPacket(spawn, Protocol1_8TO1_9.class, true, true);
    }
    
    @Override
    public void despawn() {
        final PacketWrapper despawn = PacketWrapper.create(19, null, this.user);
        despawn.write(Type.VAR_INT_ARRAY_PRIMITIVE, new int[] { this.entityId });
        PacketUtil.sendPacket(despawn, Protocol1_8TO1_9.class, true, true);
    }
    
    @Override
    public int getEntityId() {
        return this.entityId;
    }
}
