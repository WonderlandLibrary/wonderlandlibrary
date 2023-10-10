// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.protocol1_14to1_13_2.metadata;

import com.viaversion.viaversion.api.minecraft.VillagerData;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.api.type.Type;
import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.protocol.packet.PacketType;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.ClientboundPackets1_14;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.api.minecraft.item.DataItem;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.type.types.Particle;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.storage.EntityTracker1_14;
import com.viaversion.viaversion.api.type.types.version.Types1_14;
import com.viaversion.viaversion.api.connection.UserConnection;
import java.util.List;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_14Types;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_13Types;
import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.Protocol1_14To1_13_2;
import com.viaversion.viaversion.rewriter.EntityRewriter;

public class MetadataRewriter1_14To1_13_2 extends EntityRewriter<Protocol1_14To1_13_2>
{
    public MetadataRewriter1_14To1_13_2(final Protocol1_14To1_13_2 protocol) {
        super(protocol);
        this.mapTypes(Entity1_13Types.EntityType.values(), Entity1_14Types.class);
        this.mapEntityType(Entity1_13Types.EntityType.OCELOT, Entity1_14Types.CAT);
    }
    
    @Override
    protected void handleMetadata(final int entityId, final EntityType type, final Metadata metadata, final List<Metadata> metadatas, final UserConnection connection) throws Exception {
        metadata.setMetaType(Types1_14.META_TYPES.byId(metadata.metaType().typeId()));
        final EntityTracker1_14 tracker = this.tracker(connection);
        if (metadata.metaType() == Types1_14.META_TYPES.itemType) {
            ((Protocol1_14To1_13_2)this.protocol).getItemRewriter().handleItemToClient((Item)metadata.getValue());
        }
        else if (metadata.metaType() == Types1_14.META_TYPES.blockStateType) {
            final int data = (int)metadata.getValue();
            metadata.setValue(((Protocol1_14To1_13_2)this.protocol).getMappingData().getNewBlockStateId(data));
        }
        else if (metadata.metaType() == Types1_14.META_TYPES.particleType) {
            this.rewriteParticle((Particle)metadata.getValue());
        }
        if (type == null) {
            return;
        }
        if (metadata.id() > 5) {
            metadata.setId(metadata.id() + 1);
        }
        if (metadata.id() == 8 && type.isOrHasParent(Entity1_14Types.LIVINGENTITY)) {
            final float v = ((Number)metadata.getValue()).floatValue();
            if (Float.isNaN(v) && Via.getConfig().is1_14HealthNaNFix()) {
                metadata.setValue(1.0f);
            }
        }
        if (metadata.id() > 11 && type.isOrHasParent(Entity1_14Types.LIVINGENTITY)) {
            metadata.setId(metadata.id() + 1);
        }
        if (type.isOrHasParent(Entity1_14Types.ABSTRACT_INSENTIENT) && metadata.id() == 13) {
            tracker.setInsentientData(entityId, (byte)((((Number)metadata.getValue()).byteValue() & 0xFFFFFFFB) | (tracker.getInsentientData(entityId) & 0x4)));
            metadata.setValue(tracker.getInsentientData(entityId));
        }
        if (type.isOrHasParent(Entity1_14Types.PLAYER)) {
            if (entityId != tracker.clientEntityId()) {
                if (metadata.id() == 0) {
                    final byte flags = ((Number)metadata.getValue()).byteValue();
                    tracker.setEntityFlags(entityId, flags);
                }
                else if (metadata.id() == 7) {
                    tracker.setRiptide(entityId, (((Number)metadata.getValue()).byteValue() & 0x4) != 0x0);
                }
                if (metadata.id() == 0 || metadata.id() == 7) {
                    metadatas.add(new Metadata(6, Types1_14.META_TYPES.poseType, recalculatePlayerPose(entityId, tracker)));
                }
            }
        }
        else if (type.isOrHasParent(Entity1_14Types.ZOMBIE)) {
            if (metadata.id() == 16) {
                tracker.setInsentientData(entityId, (byte)((tracker.getInsentientData(entityId) & 0xFFFFFFFB) | (metadata.getValue() ? 4 : 0)));
                metadatas.remove(metadata);
                metadatas.add(new Metadata(13, Types1_14.META_TYPES.byteType, tracker.getInsentientData(entityId)));
            }
            else if (metadata.id() > 16) {
                metadata.setId(metadata.id() - 1);
            }
        }
        if (type.isOrHasParent(Entity1_14Types.MINECART_ABSTRACT)) {
            if (metadata.id() == 10) {
                final int data = (int)metadata.getValue();
                metadata.setValue(((Protocol1_14To1_13_2)this.protocol).getMappingData().getNewBlockStateId(data));
            }
        }
        else if (type.is(Entity1_14Types.HORSE)) {
            if (metadata.id() == 18) {
                metadatas.remove(metadata);
                final int armorType = (int)metadata.getValue();
                Item armorItem = null;
                if (armorType == 1) {
                    armorItem = new DataItem(((Protocol1_14To1_13_2)this.protocol).getMappingData().getNewItemId(727), (byte)1, (short)0, null);
                }
                else if (armorType == 2) {
                    armorItem = new DataItem(((Protocol1_14To1_13_2)this.protocol).getMappingData().getNewItemId(728), (byte)1, (short)0, null);
                }
                else if (armorType == 3) {
                    armorItem = new DataItem(((Protocol1_14To1_13_2)this.protocol).getMappingData().getNewItemId(729), (byte)1, (short)0, null);
                }
                final PacketWrapper equipmentPacket = PacketWrapper.create(ClientboundPackets1_14.ENTITY_EQUIPMENT, null, connection);
                equipmentPacket.write(Type.VAR_INT, entityId);
                equipmentPacket.write(Type.VAR_INT, 4);
                equipmentPacket.write(Type.FLAT_VAR_INT_ITEM, armorItem);
                equipmentPacket.scheduleSend(Protocol1_14To1_13_2.class);
            }
        }
        else if (type.is(Entity1_14Types.VILLAGER)) {
            if (metadata.id() == 15) {
                metadata.setTypeAndValue(Types1_14.META_TYPES.villagerDatatType, new VillagerData(2, getNewProfessionId((int)metadata.getValue()), 0));
            }
        }
        else if (type.is(Entity1_14Types.ZOMBIE_VILLAGER)) {
            if (metadata.id() == 18) {
                metadata.setTypeAndValue(Types1_14.META_TYPES.villagerDatatType, new VillagerData(2, getNewProfessionId((int)metadata.getValue()), 0));
            }
        }
        else if (type.isOrHasParent(Entity1_14Types.ABSTRACT_ARROW)) {
            if (metadata.id() >= 9) {
                metadata.setId(metadata.id() + 1);
            }
        }
        else if (type.is(Entity1_14Types.FIREWORK_ROCKET)) {
            if (metadata.id() == 8) {
                metadata.setMetaType(Types1_14.META_TYPES.optionalVarIntType);
                if (metadata.getValue().equals(0)) {
                    metadata.setValue(null);
                }
            }
        }
        else if (type.isOrHasParent(Entity1_14Types.ABSTRACT_SKELETON) && metadata.id() == 14) {
            tracker.setInsentientData(entityId, (byte)((tracker.getInsentientData(entityId) & 0xFFFFFFFB) | (metadata.getValue() ? 4 : 0)));
            metadatas.remove(metadata);
            metadatas.add(new Metadata(13, Types1_14.META_TYPES.byteType, tracker.getInsentientData(entityId)));
        }
        if (type.isOrHasParent(Entity1_14Types.ABSTRACT_ILLAGER_BASE) && metadata.id() == 14) {
            tracker.setInsentientData(entityId, (byte)((tracker.getInsentientData(entityId) & 0xFFFFFFFB) | ((((Number)metadata.getValue()).byteValue() != 0) ? 4 : 0)));
            metadatas.remove(metadata);
            metadatas.add(new Metadata(13, Types1_14.META_TYPES.byteType, tracker.getInsentientData(entityId)));
        }
        if ((type.is(Entity1_14Types.WITCH) || type.is(Entity1_14Types.RAVAGER) || type.isOrHasParent(Entity1_14Types.ABSTRACT_ILLAGER_BASE)) && metadata.id() >= 14) {
            metadata.setId(metadata.id() + 1);
        }
    }
    
    @Override
    public EntityType typeFromId(final int type) {
        return Entity1_14Types.getTypeFromId(type);
    }
    
    private static boolean isSneaking(final byte flags) {
        return (flags & 0x2) != 0x0;
    }
    
    private static boolean isSwimming(final byte flags) {
        return (flags & 0x10) != 0x0;
    }
    
    private static int getNewProfessionId(final int old) {
        switch (old) {
            case 0: {
                return 5;
            }
            case 1: {
                return 9;
            }
            case 2: {
                return 4;
            }
            case 3: {
                return 1;
            }
            case 4: {
                return 2;
            }
            case 5: {
                return 11;
            }
            default: {
                return 0;
            }
        }
    }
    
    private static boolean isFallFlying(final int entityFlags) {
        return (entityFlags & 0x80) != 0x0;
    }
    
    public static int recalculatePlayerPose(final int entityId, final EntityTracker1_14 tracker) {
        final byte flags = tracker.getEntityFlags(entityId);
        int pose = 0;
        if (isFallFlying(flags)) {
            pose = 1;
        }
        else if (tracker.isSleeping(entityId)) {
            pose = 2;
        }
        else if (isSwimming(flags)) {
            pose = 3;
        }
        else if (tracker.isRiptide(entityId)) {
            pose = 4;
        }
        else if (isSneaking(flags)) {
            pose = 5;
        }
        return pose;
    }
}
