// 
// Decompiled by Procyon v0.6.0
// 

package de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.entityreplacements;

import de.gerrygames.viarewind.utils.math.Vector3d;
import de.gerrygames.viarewind.utils.math.AABB;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.types.MetaType1_7_6_10;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.types.Types1_7_6_10;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.metadata.MetadataRewriter;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_10Types;
import com.viaversion.viaversion.api.minecraft.metadata.MetaType;
import com.viaversion.viaversion.api.protocol.Protocol;
import de.gerrygames.viarewind.utils.PacketUtil;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.Protocol1_7_6_10TO1_8;
import com.viaversion.viaversion.api.type.Type;
import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.protocol.packet.PacketType;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.ClientboundPackets1_7;
import com.viaversion.viaversion.api.minecraft.metadata.types.MetaType1_8;
import java.util.Iterator;
import java.util.ArrayList;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import java.util.List;

public class ArmorStandReplacement extends EntityReplacement1_7to1_8
{
    private final int entityId;
    private final List<Metadata> datawatcher;
    private int[] entityIds;
    private double locX;
    private double locY;
    private double locZ;
    private State currentState;
    private boolean invisible;
    private boolean nameTagVisible;
    private String name;
    private float yaw;
    private float pitch;
    private float headYaw;
    private boolean small;
    private boolean marker;
    
    @Override
    public int getEntityId() {
        return this.entityId;
    }
    
    public ArmorStandReplacement(final int entityId, final UserConnection user) {
        super(user);
        this.datawatcher = new ArrayList<Metadata>();
        this.entityIds = null;
        this.currentState = null;
        this.invisible = false;
        this.nameTagVisible = false;
        this.name = null;
        this.small = false;
        this.marker = false;
        this.entityId = entityId;
    }
    
    @Override
    public void setLocation(final double x, final double y, final double z) {
        if (x != this.locX || y != this.locY || z != this.locZ) {
            this.locX = x;
            this.locY = y;
            this.locZ = z;
            this.updateLocation();
        }
    }
    
    @Override
    public void relMove(final double x, final double y, final double z) {
        if (x == 0.0 && y == 0.0 && z == 0.0) {
            return;
        }
        this.locX += x;
        this.locY += y;
        this.locZ += z;
        this.updateLocation();
    }
    
    @Override
    public void setYawPitch(final float yaw, final float pitch) {
        if ((this.yaw != yaw && this.pitch != pitch) || this.headYaw != yaw) {
            this.yaw = yaw;
            this.headYaw = yaw;
            this.pitch = pitch;
            this.updateLocation();
        }
    }
    
    @Override
    public void setHeadYaw(final float yaw) {
        if (this.headYaw != yaw) {
            this.headYaw = yaw;
            this.updateLocation();
        }
    }
    
    @Override
    public void updateMetadata(final List<Metadata> metadataList) {
        for (final Metadata metadata : metadataList) {
            this.datawatcher.removeIf(m -> m.id() == metadata.id());
            this.datawatcher.add(metadata);
        }
        this.updateState();
    }
    
    public void updateState() {
        byte flags = 0;
        byte armorStandFlags = 0;
        for (final Metadata metadata : this.datawatcher) {
            if (metadata.id() == 0 && metadata.metaType() == MetaType1_8.Byte) {
                flags = ((Number)metadata.getValue()).byteValue();
            }
            else if (metadata.id() == 2 && metadata.metaType() == MetaType1_8.String) {
                this.name = metadata.getValue().toString();
                if (this.name == null || !this.name.equals("")) {
                    continue;
                }
                this.name = null;
            }
            else if (metadata.id() == 10 && metadata.metaType() == MetaType1_8.Byte) {
                armorStandFlags = ((Number)metadata.getValue()).byteValue();
            }
            else {
                if (metadata.id() != 3 || metadata.metaType() != MetaType1_8.Byte) {
                    continue;
                }
                this.nameTagVisible = (((Number)metadata.getValue()).byteValue() != 0);
            }
        }
        this.invisible = ((flags & 0x20) != 0x0);
        this.small = ((armorStandFlags & 0x1) != 0x0);
        this.marker = ((armorStandFlags & 0x10) != 0x0);
        final State prevState = this.currentState;
        if (this.invisible && this.name != null) {
            this.currentState = State.HOLOGRAM;
        }
        else {
            this.currentState = State.ZOMBIE;
        }
        if (this.currentState != prevState) {
            this.despawn();
            this.spawn();
        }
        else {
            this.updateMetadata();
            this.updateLocation();
        }
    }
    
    public void updateLocation() {
        if (this.entityIds == null) {
            return;
        }
        if (this.currentState == State.ZOMBIE) {
            this.updateZombieLocation();
        }
        else if (this.currentState == State.HOLOGRAM) {
            this.updateHologramLocation();
        }
    }
    
    private void updateZombieLocation() {
        this.sendTeleportWithHead(this.entityId, this.locX, this.locY, this.locZ, this.yaw, this.pitch, this.headYaw);
    }
    
    private void updateHologramLocation() {
        final PacketWrapper detach = PacketWrapper.create(ClientboundPackets1_7.ATTACH_ENTITY, null, this.user);
        detach.write(Type.INT, this.entityIds[1]);
        detach.write(Type.INT, -1);
        detach.write(Type.BOOLEAN, false);
        PacketUtil.sendPacket(detach, Protocol1_7_6_10TO1_8.class, true, true);
        this.sendTeleport(this.entityIds[0], this.locX, this.locY + (this.marker ? 54.85 : (this.small ? 56.0 : 57.0)), this.locZ, 0.0f, 0.0f);
        this.sendTeleport(this.entityIds[1], this.locX, this.locY + 56.75, this.locZ, 0.0f, 0.0f);
        final PacketWrapper attach = PacketWrapper.create(ClientboundPackets1_7.ATTACH_ENTITY, null, this.user);
        attach.write(Type.INT, this.entityIds[1]);
        attach.write(Type.INT, this.entityIds[0]);
        attach.write(Type.BOOLEAN, false);
        PacketUtil.sendPacket(attach, Protocol1_7_6_10TO1_8.class, true, true);
    }
    
    public void updateMetadata() {
        if (this.entityIds == null) {
            return;
        }
        final PacketWrapper metadataPacket = PacketWrapper.create(ClientboundPackets1_7.ENTITY_METADATA, null, this.user);
        if (this.currentState == State.ZOMBIE) {
            this.writeZombieMeta(metadataPacket);
        }
        else {
            if (this.currentState != State.HOLOGRAM) {
                return;
            }
            this.writeHologramMeta(metadataPacket);
        }
        PacketUtil.sendPacket(metadataPacket, Protocol1_7_6_10TO1_8.class, true, true);
    }
    
    private void writeZombieMeta(final PacketWrapper metadataPacket) {
        metadataPacket.write(Type.INT, this.entityIds[0]);
        final List<Metadata> metadataList = new ArrayList<Metadata>();
        for (final Metadata metadata : this.datawatcher) {
            if (metadata.id() >= 0) {
                if (metadata.id() > 9) {
                    continue;
                }
                metadataList.add(new Metadata(metadata.id(), metadata.metaType(), metadata.getValue()));
            }
        }
        if (this.small) {
            metadataList.add(new Metadata(12, MetaType1_8.Byte, 1));
        }
        MetadataRewriter.transform(Entity1_10Types.EntityType.ZOMBIE, metadataList);
        metadataPacket.write(Types1_7_6_10.METADATA_LIST, metadataList);
    }
    
    private void writeHologramMeta(final PacketWrapper metadataPacket) {
        metadataPacket.write(Type.INT, this.entityIds[1]);
        final List<Metadata> metadataList = new ArrayList<Metadata>();
        metadataList.add(new Metadata(12, MetaType1_7_6_10.Int, -1700000));
        metadataList.add(new Metadata(10, MetaType1_7_6_10.String, this.name));
        metadataList.add(new Metadata(11, MetaType1_7_6_10.Byte, 1));
        metadataPacket.write(Types1_7_6_10.METADATA_LIST, metadataList);
    }
    
    @Override
    public void spawn() {
        if (this.entityIds != null) {
            this.despawn();
        }
        if (this.currentState == State.ZOMBIE) {
            this.spawnZombie();
        }
        else if (this.currentState == State.HOLOGRAM) {
            this.spawnHologram();
        }
        this.updateMetadata();
        this.updateLocation();
    }
    
    private void spawnZombie() {
        this.sendSpawn(this.entityId, 54, this.locX, this.locY, this.locZ);
        this.entityIds = new int[] { this.entityId };
    }
    
    private void spawnHologram() {
        final int[] entityIds = { this.entityId, this.additionalEntityId() };
        this.sendSpawn(entityIds[0], 66, this.locX, this.locY, this.locZ);
        this.sendSpawn(entityIds[1], 100, this.locX, this.locY, this.locZ);
        this.entityIds = entityIds;
    }
    
    private int additionalEntityId() {
        return 2147467647 - this.entityId;
    }
    
    public AABB getBoundingBox() {
        final double w = this.small ? 0.25 : 0.5;
        final double h = this.small ? 0.9875 : 1.975;
        final Vector3d min = new Vector3d(this.locX - w / 2.0, this.locY, this.locZ - w / 2.0);
        final Vector3d max = new Vector3d(this.locX + w / 2.0, this.locY + h, this.locZ + w / 2.0);
        return new AABB(min, max);
    }
    
    @Override
    public void despawn() {
        if (this.entityIds == null) {
            return;
        }
        final PacketWrapper despawn = PacketWrapper.create(ClientboundPackets1_7.DESTROY_ENTITIES, null, this.user);
        despawn.write(Type.BYTE, (byte)this.entityIds.length);
        for (final int id : this.entityIds) {
            despawn.write(Type.INT, id);
        }
        this.entityIds = null;
        PacketUtil.sendPacket(despawn, Protocol1_7_6_10TO1_8.class, true, true);
    }
    
    private enum State
    {
        HOLOGRAM, 
        ZOMBIE;
    }
}
