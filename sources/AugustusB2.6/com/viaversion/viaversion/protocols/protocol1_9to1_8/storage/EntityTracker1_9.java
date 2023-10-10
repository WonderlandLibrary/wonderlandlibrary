// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.protocol1_9to1_8.storage;

import com.viaversion.viaversion.protocols.protocol1_9to1_8.providers.EntityIdProvider;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.metadata.MetadataRewriter1_9To1_8;
import com.viaversion.viaversion.api.type.types.version.Types1_9;
import java.util.Iterator;
import com.viaversion.viaversion.api.legacy.bossbar.BossStyle;
import com.viaversion.viaversion.api.legacy.bossbar.BossColor;
import com.viaversion.viaversion.api.minecraft.metadata.MetaType;
import com.viaversion.viaversion.api.minecraft.metadata.types.MetaType1_9;
import java.util.Collection;
import java.util.ArrayList;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.providers.BossBarProvider;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.api.minecraft.item.DataItem;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.Protocol1_9To1_8;
import com.viaversion.viaversion.api.type.Type;
import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.protocol.packet.PacketType;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.ClientboundPackets1_9;
import java.util.Map;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import com.google.common.cache.CacheBuilder;
import com.viaversion.viaversion.libs.flare.fastutil.Int2ObjectSyncMap;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_10Types;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.chat.GameMode;
import com.viaversion.viaversion.api.minecraft.Position;
import java.util.Set;
import com.viaversion.viaversion.libs.fastutil.ints.IntSet;
import com.viaversion.viaversion.api.legacy.bossbar.BossBar;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import java.util.List;
import java.util.UUID;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectMap;
import com.viaversion.viaversion.data.entity.EntityTrackerBase;

public class EntityTracker1_9 extends EntityTrackerBase
{
    public static final String WITHER_TRANSLATABLE = "{\"translate\":\"entity.WitherBoss.name\"}";
    public static final String DRAGON_TRANSLATABLE = "{\"translate\":\"entity.EnderDragon.name\"}";
    private final Int2ObjectMap<UUID> uuidMap;
    private final Int2ObjectMap<List<Metadata>> metadataBuffer;
    private final Int2ObjectMap<Integer> vehicleMap;
    private final Int2ObjectMap<BossBar> bossBarMap;
    private final IntSet validBlocking;
    private final Set<Integer> knownHolograms;
    private final Set<Position> blockInteractions;
    private boolean blocking;
    private boolean autoTeam;
    private Position currentlyDigging;
    private boolean teamExists;
    private GameMode gameMode;
    private String currentTeam;
    private int heldItemSlot;
    private Item itemInSecondHand;
    
    public EntityTracker1_9(final UserConnection user) {
        super(user, Entity1_10Types.EntityType.PLAYER);
        this.uuidMap = (Int2ObjectMap<UUID>)Int2ObjectSyncMap.hashmap();
        this.metadataBuffer = (Int2ObjectMap<List<Metadata>>)Int2ObjectSyncMap.hashmap();
        this.vehicleMap = (Int2ObjectMap<Integer>)Int2ObjectSyncMap.hashmap();
        this.bossBarMap = (Int2ObjectMap<BossBar>)Int2ObjectSyncMap.hashmap();
        this.validBlocking = Int2ObjectSyncMap.hashset();
        this.knownHolograms = Int2ObjectSyncMap.hashset();
        this.blockInteractions = Collections.newSetFromMap((Map<Position, Boolean>)CacheBuilder.newBuilder().maximumSize(1000L).expireAfterAccess(250L, TimeUnit.MILLISECONDS).build().asMap());
        this.blocking = false;
        this.autoTeam = false;
        this.currentlyDigging = null;
        this.teamExists = false;
        this.itemInSecondHand = null;
    }
    
    public UUID getEntityUUID(final int id) {
        UUID uuid = this.uuidMap.get(id);
        if (uuid == null) {
            uuid = UUID.randomUUID();
            this.uuidMap.put(id, uuid);
        }
        return uuid;
    }
    
    public void setSecondHand(final Item item) {
        this.setSecondHand(this.clientEntityId(), item);
    }
    
    public void setSecondHand(final int entityID, final Item item) {
        final PacketWrapper wrapper = PacketWrapper.create(ClientboundPackets1_9.ENTITY_EQUIPMENT, null, this.user());
        wrapper.write(Type.VAR_INT, entityID);
        wrapper.write(Type.VAR_INT, 1);
        wrapper.write(Type.ITEM, this.itemInSecondHand = item);
        try {
            wrapper.scheduleSend(Protocol1_9To1_8.class);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public Item getItemInSecondHand() {
        return this.itemInSecondHand;
    }
    
    public void syncShieldWithSword() {
        final boolean swordInHand = this.hasSwordInHand();
        if (!swordInHand || this.itemInSecondHand == null) {
            this.setSecondHand(swordInHand ? new DataItem(442, (byte)1, (short)0, null) : null);
        }
    }
    
    public boolean hasSwordInHand() {
        final InventoryTracker inventoryTracker = this.user().get(InventoryTracker.class);
        final int inventorySlot = this.heldItemSlot + 36;
        final int itemIdentifier = inventoryTracker.getItemId((short)0, (short)inventorySlot);
        return Protocol1_9To1_8.isSword(itemIdentifier);
    }
    
    @Override
    public void removeEntity(final int entityId) {
        super.removeEntity(entityId);
        this.vehicleMap.remove(entityId);
        this.uuidMap.remove(entityId);
        this.validBlocking.remove(entityId);
        this.knownHolograms.remove(entityId);
        this.metadataBuffer.remove(entityId);
        final BossBar bar = this.bossBarMap.remove(entityId);
        if (bar != null) {
            bar.hide();
            Via.getManager().getProviders().get(BossBarProvider.class).handleRemove(this.user(), bar.getId());
        }
    }
    
    public boolean interactedBlockRecently(final int x, final int y, final int z) {
        return this.blockInteractions.contains(new Position(x, (short)y, z));
    }
    
    public void addBlockInteraction(final Position p) {
        this.blockInteractions.add(p);
    }
    
    public void handleMetadata(final int entityId, final List<Metadata> metadataList) {
        final EntityType type = this.entityType(entityId);
        if (type == null) {
            return;
        }
        for (final Metadata metadata : new ArrayList<Metadata>(metadataList)) {
            if (type == Entity1_10Types.EntityType.WITHER && metadata.id() == 10) {
                metadataList.remove(metadata);
            }
            if (type == Entity1_10Types.EntityType.ENDER_DRAGON && metadata.id() == 11) {
                metadataList.remove(metadata);
            }
            if (type == Entity1_10Types.EntityType.SKELETON && this.getMetaByIndex(metadataList, 12) == null) {
                metadataList.add(new Metadata(12, MetaType1_9.Boolean, true));
            }
            if (type == Entity1_10Types.EntityType.HORSE && metadata.id() == 16 && (int)metadata.getValue() == Integer.MIN_VALUE) {
                metadata.setValue(0);
            }
            if (type == Entity1_10Types.EntityType.PLAYER) {
                if (metadata.id() == 0) {
                    final byte data = (byte)metadata.getValue();
                    if (entityId != this.getProvidedEntityId() && Via.getConfig().isShieldBlocking()) {
                        if ((data & 0x10) == 0x10) {
                            if (this.validBlocking.contains(entityId)) {
                                final Item shield = new DataItem(442, (byte)1, (short)0, null);
                                this.setSecondHand(entityId, shield);
                            }
                            else {
                                this.setSecondHand(entityId, null);
                            }
                        }
                        else {
                            this.setSecondHand(entityId, null);
                        }
                    }
                }
                if (metadata.id() == 12 && Via.getConfig().isLeftHandedHandling()) {
                    metadataList.add(new Metadata(13, MetaType1_9.Byte, (byte)((((byte)metadata.getValue() & 0x80) == 0x0) ? 1 : 0)));
                }
            }
            if (type == Entity1_10Types.EntityType.ARMOR_STAND && Via.getConfig().isHologramPatch() && metadata.id() == 0 && this.getMetaByIndex(metadataList, 10) != null) {
                final Metadata meta = this.getMetaByIndex(metadataList, 10);
                final byte data2 = (byte)metadata.getValue();
                final Metadata displayName;
                final Metadata displayNameVisible;
                if ((data2 & 0x20) == 0x20 && ((byte)meta.getValue() & 0x1) == 0x1 && (displayName = this.getMetaByIndex(metadataList, 2)) != null && !((String)displayName.getValue()).isEmpty() && (displayNameVisible = this.getMetaByIndex(metadataList, 3)) != null && (boolean)displayNameVisible.getValue() && !this.knownHolograms.contains(entityId)) {
                    this.knownHolograms.add(entityId);
                    try {
                        final PacketWrapper wrapper = PacketWrapper.create(ClientboundPackets1_9.ENTITY_POSITION, null, this.user());
                        wrapper.write(Type.VAR_INT, entityId);
                        wrapper.write(Type.SHORT, (Short)0);
                        wrapper.write(Type.SHORT, (short)(128.0 * (Via.getConfig().getHologramYOffset() * 32.0)));
                        wrapper.write(Type.SHORT, (Short)0);
                        wrapper.write(Type.BOOLEAN, true);
                        wrapper.scheduleSend(Protocol1_9To1_8.class);
                    }
                    catch (Exception ex) {}
                }
            }
            if (Via.getConfig().isBossbarPatch() && (type == Entity1_10Types.EntityType.ENDER_DRAGON || type == Entity1_10Types.EntityType.WITHER)) {
                if (metadata.id() == 2) {
                    BossBar bar = this.bossBarMap.get(entityId);
                    String title = (String)metadata.getValue();
                    title = (title.isEmpty() ? ((type == Entity1_10Types.EntityType.ENDER_DRAGON) ? "{\"translate\":\"entity.EnderDragon.name\"}" : "{\"translate\":\"entity.WitherBoss.name\"}") : title);
                    if (bar == null) {
                        bar = Via.getAPI().legacyAPI().createLegacyBossBar(title, BossColor.PINK, BossStyle.SOLID);
                        this.bossBarMap.put(entityId, bar);
                        bar.addConnection(this.user());
                        bar.show();
                        Via.getManager().getProviders().get(BossBarProvider.class).handleAdd(this.user(), bar.getId());
                    }
                    else {
                        bar.setTitle(title);
                    }
                }
                else {
                    if (metadata.id() != 6 || Via.getConfig().isBossbarAntiflicker()) {
                        continue;
                    }
                    BossBar bar = this.bossBarMap.get(entityId);
                    final float maxHealth = (type == Entity1_10Types.EntityType.ENDER_DRAGON) ? 200.0f : 300.0f;
                    final float health = Math.max(0.0f, Math.min((float)metadata.getValue() / maxHealth, 1.0f));
                    if (bar == null) {
                        final String title2 = (type == Entity1_10Types.EntityType.ENDER_DRAGON) ? "{\"translate\":\"entity.EnderDragon.name\"}" : "{\"translate\":\"entity.WitherBoss.name\"}";
                        bar = Via.getAPI().legacyAPI().createLegacyBossBar(title2, health, BossColor.PINK, BossStyle.SOLID);
                        this.bossBarMap.put(entityId, bar);
                        bar.addConnection(this.user());
                        bar.show();
                        Via.getManager().getProviders().get(BossBarProvider.class).handleAdd(this.user(), bar.getId());
                    }
                    else {
                        bar.setHealth(health);
                    }
                }
            }
        }
    }
    
    public Metadata getMetaByIndex(final List<Metadata> list, final int index) {
        for (final Metadata meta : list) {
            if (index == meta.id()) {
                return meta;
            }
        }
        return null;
    }
    
    public void sendTeamPacket(final boolean add, final boolean now) {
        final PacketWrapper wrapper = PacketWrapper.create(ClientboundPackets1_9.TEAMS, null, this.user());
        wrapper.write(Type.STRING, "viaversion");
        if (add) {
            if (!this.teamExists) {
                wrapper.write(Type.BYTE, (Byte)0);
                wrapper.write(Type.STRING, "viaversion");
                wrapper.write(Type.STRING, "§f");
                wrapper.write(Type.STRING, "");
                wrapper.write(Type.BYTE, (Byte)0);
                wrapper.write(Type.STRING, "");
                wrapper.write(Type.STRING, "never");
                wrapper.write(Type.BYTE, (Byte)15);
            }
            else {
                wrapper.write(Type.BYTE, (Byte)3);
            }
            wrapper.write(Type.STRING_ARRAY, new String[] { this.user().getProtocolInfo().getUsername() });
        }
        else {
            wrapper.write(Type.BYTE, (Byte)1);
        }
        this.teamExists = add;
        try {
            if (now) {
                wrapper.send(Protocol1_9To1_8.class);
            }
            else {
                wrapper.scheduleSend(Protocol1_9To1_8.class);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void addMetadataToBuffer(final int entityID, final List<Metadata> metadataList) {
        final List<Metadata> metadata = this.metadataBuffer.get(entityID);
        if (metadata != null) {
            metadata.addAll(metadataList);
        }
        else {
            this.metadataBuffer.put(entityID, metadataList);
        }
    }
    
    public void sendMetadataBuffer(final int entityId) {
        final List<Metadata> metadataList = this.metadataBuffer.get(entityId);
        if (metadataList != null) {
            final PacketWrapper wrapper = PacketWrapper.create(ClientboundPackets1_9.ENTITY_METADATA, null, this.user());
            wrapper.write(Type.VAR_INT, entityId);
            wrapper.write(Types1_9.METADATA_LIST, metadataList);
            Via.getManager().getProtocolManager().getProtocol(Protocol1_9To1_8.class).get(MetadataRewriter1_9To1_8.class).handleMetadata(entityId, metadataList, this.user());
            this.handleMetadata(entityId, metadataList);
            if (!metadataList.isEmpty()) {
                try {
                    wrapper.scheduleSend(Protocol1_9To1_8.class);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            this.metadataBuffer.remove(entityId);
        }
    }
    
    public int getProvidedEntityId() {
        try {
            return Via.getManager().getProviders().get(EntityIdProvider.class).getEntityId(this.user());
        }
        catch (Exception e) {
            return this.clientEntityId();
        }
    }
    
    public Map<Integer, UUID> getUuidMap() {
        return this.uuidMap;
    }
    
    public Map<Integer, List<Metadata>> getMetadataBuffer() {
        return this.metadataBuffer;
    }
    
    public Map<Integer, Integer> getVehicleMap() {
        return this.vehicleMap;
    }
    
    public Map<Integer, BossBar> getBossBarMap() {
        return this.bossBarMap;
    }
    
    public Set<Integer> getValidBlocking() {
        return this.validBlocking;
    }
    
    public Set<Integer> getKnownHolograms() {
        return this.knownHolograms;
    }
    
    public Set<Position> getBlockInteractions() {
        return this.blockInteractions;
    }
    
    public boolean isBlocking() {
        return this.blocking;
    }
    
    public void setBlocking(final boolean blocking) {
        this.blocking = blocking;
    }
    
    public boolean isAutoTeam() {
        return this.autoTeam;
    }
    
    public void setAutoTeam(final boolean autoTeam) {
        this.autoTeam = autoTeam;
    }
    
    public Position getCurrentlyDigging() {
        return this.currentlyDigging;
    }
    
    public void setCurrentlyDigging(final Position currentlyDigging) {
        this.currentlyDigging = currentlyDigging;
    }
    
    public boolean isTeamExists() {
        return this.teamExists;
    }
    
    public GameMode getGameMode() {
        return this.gameMode;
    }
    
    public void setGameMode(final GameMode gameMode) {
        this.gameMode = gameMode;
    }
    
    public String getCurrentTeam() {
        return this.currentTeam;
    }
    
    public void setCurrentTeam(final String currentTeam) {
        this.currentTeam = currentTeam;
    }
    
    public void setHeldItemSlot(final int heldItemSlot) {
        this.heldItemSlot = heldItemSlot;
    }
}
