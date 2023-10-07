// 
// Decompiled by Procyon v0.6.0
// 

package de.gerrygames.viarewind.protocol.protocol1_8to1_9.entityreplacement;

import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.ClientboundPackets1_7;
import com.viaversion.viaversion.api.protocol.Protocol;
import de.gerrygames.viarewind.utils.PacketUtil;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.Protocol1_8TO1_9;
import com.viaversion.viaversion.api.type.types.version.Types1_8;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.metadata.MetadataRewriter;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_10Types;
import com.viaversion.viaversion.api.minecraft.metadata.MetaType;
import com.viaversion.viaversion.api.minecraft.metadata.types.MetaType1_9;
import com.viaversion.viaversion.api.type.Type;
import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.protocol.packet.PacketType;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.protocols.protocol1_8.ClientboundPackets1_8;
import java.util.Iterator;
import java.util.ArrayList;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import java.util.List;

public class ShulkerReplacement extends EntityReplacement1_8to1_9
{
    private final int entityId;
    private final List<Metadata> datawatcher;
    private double locX;
    private double locY;
    private double locZ;
    
    public ShulkerReplacement(final int entityId, final UserConnection user) {
        super(user);
        this.datawatcher = new ArrayList<Metadata>();
        this.entityId = entityId;
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
        this.sendTeleport(this.entityId, this.locX, this.locY, this.locZ, 0.0f, 0.0f);
    }
    
    public void updateMetadata() {
        final PacketWrapper metadataPacket = PacketWrapper.create(ClientboundPackets1_8.ENTITY_METADATA, null, this.user);
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
        this.sendSpawn(this.entityId, 62);
    }
    
    @Override
    public void despawn() {
        final PacketWrapper despawn = PacketWrapper.create(ClientboundPackets1_7.DESTROY_ENTITIES, null, this.user);
        despawn.write(Type.VAR_INT_ARRAY_PRIMITIVE, new int[] { this.entityId });
        PacketUtil.sendPacket(despawn, Protocol1_8TO1_9.class, true, true);
    }
    
    @Override
    public int getEntityId() {
        return this.entityId;
    }
}
