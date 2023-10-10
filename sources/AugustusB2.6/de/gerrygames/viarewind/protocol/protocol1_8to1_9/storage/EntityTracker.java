// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.protocol.protocol1_8to1_9.storage;

import com.viaversion.viaversion.api.protocol.Protocol;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.Protocol1_8TO1_9;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.metadata.MetadataRewriter;
import com.viaversion.viaversion.api.type.types.version.Types1_8;
import com.viaversion.viaversion.api.type.Type;
import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.minecraft.Vector;
import de.gerrygames.viarewind.replacement.EntityReplacement;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_10Types;
import java.util.List;
import java.util.Map;
import com.viaversion.viaversion.api.data.entity.ClientEntityIdChangeListener;
import com.viaversion.viaversion.api.connection.StoredObject;

public class EntityTracker extends StoredObject implements ClientEntityIdChangeListener
{
    private final Map<Integer, List<Integer>> vehicleMap;
    private final Map<Integer, Entity1_10Types.EntityType> clientEntityTypes;
    private final Map<Integer, List<Metadata>> metadataBuffer;
    private final Map<Integer, EntityReplacement> entityReplacements;
    private final Map<Integer, Vector> entityOffsets;
    private int playerId;
    private int playerGamemode;
    
    public EntityTracker(final UserConnection user) {
        super(user);
        this.vehicleMap = new ConcurrentHashMap<Integer, List<Integer>>();
        this.clientEntityTypes = new ConcurrentHashMap<Integer, Entity1_10Types.EntityType>();
        this.metadataBuffer = new ConcurrentHashMap<Integer, List<Metadata>>();
        this.entityReplacements = new ConcurrentHashMap<Integer, EntityReplacement>();
        this.entityOffsets = new ConcurrentHashMap<Integer, Vector>();
        this.playerGamemode = 0;
    }
    
    public void setPlayerId(final int entityId) {
        this.playerId = entityId;
    }
    
    public int getPlayerId() {
        return this.playerId;
    }
    
    public int getPlayerGamemode() {
        return this.playerGamemode;
    }
    
    public void setPlayerGamemode(final int playerGamemode) {
        this.playerGamemode = playerGamemode;
    }
    
    public void removeEntity(final int entityId) {
        this.vehicleMap.remove(entityId);
        this.vehicleMap.forEach((vehicle, passengers) -> passengers.remove((Object)entityId));
        this.vehicleMap.entrySet().removeIf(entry -> entry.getValue().isEmpty());
        this.clientEntityTypes.remove(entityId);
        this.entityOffsets.remove(entityId);
        if (this.entityReplacements.containsKey(entityId)) {
            this.entityReplacements.remove(entityId).despawn();
        }
    }
    
    public void resetEntityOffset(final int entityId) {
        this.entityOffsets.remove(entityId);
    }
    
    public Vector getEntityOffset(final int entityId) {
        return this.entityOffsets.computeIfAbsent(entityId, key -> new Vector(0, 0, 0));
    }
    
    public void addToEntityOffset(final int entityId, final short relX, final short relY, final short relZ) {
        this.entityOffsets.compute(entityId, (key, offset) -> {
            if (offset == null) {
                return new Vector(relX, relY, relZ);
            }
            else {
                offset.setBlockX(offset.getBlockX() + relX);
                offset.setBlockY(offset.getBlockY() + relY);
                offset.setBlockZ(offset.getBlockZ() + relZ);
                return offset;
            }
        });
    }
    
    public void setEntityOffset(final int entityId, final short relX, final short relY, final short relZ) {
        this.entityOffsets.compute(entityId, (key, offset) -> {
            if (offset == null) {
                return new Vector(relX, relY, relZ);
            }
            else {
                offset.setBlockX(relX);
                offset.setBlockY(relY);
                offset.setBlockZ(relZ);
                return offset;
            }
        });
    }
    
    public void setEntityOffset(final int entityId, final Vector offset) {
        this.entityOffsets.put(entityId, offset);
    }
    
    public List<Integer> getPassengers(final int entityId) {
        return this.vehicleMap.getOrDefault(entityId, new ArrayList<Integer>());
    }
    
    public void setPassengers(final int entityId, final List<Integer> passengers) {
        this.vehicleMap.put(entityId, passengers);
    }
    
    public void addEntityReplacement(final EntityReplacement entityReplacement) {
        this.entityReplacements.put(entityReplacement.getEntityId(), entityReplacement);
    }
    
    public EntityReplacement getEntityReplacement(final int entityId) {
        return this.entityReplacements.get(entityId);
    }
    
    public Map<Integer, Entity1_10Types.EntityType> getClientEntityTypes() {
        return this.clientEntityTypes;
    }
    
    public void addMetadataToBuffer(final int entityID, final List<Metadata> metadataList) {
        if (this.metadataBuffer.containsKey(entityID)) {
            this.metadataBuffer.get(entityID).addAll(metadataList);
        }
        else if (!metadataList.isEmpty()) {
            this.metadataBuffer.put(entityID, metadataList);
        }
    }
    
    public List<Metadata> getBufferedMetadata(final int entityId) {
        return this.metadataBuffer.get(entityId);
    }
    
    public boolean isInsideVehicle(final int entityId) {
        for (final List<Integer> vehicle : this.vehicleMap.values()) {
            if (vehicle.contains(entityId)) {
                return true;
            }
        }
        return false;
    }
    
    public int getVehicle(final int passenger) {
        for (final Map.Entry<Integer, List<Integer>> vehicle : this.vehicleMap.entrySet()) {
            if (vehicle.getValue().contains(passenger)) {
                return vehicle.getKey();
            }
        }
        return -1;
    }
    
    public boolean isPassenger(final int vehicle, final int passenger) {
        return this.vehicleMap.containsKey(vehicle) && this.vehicleMap.get(vehicle).contains(passenger);
    }
    
    public void sendMetadataBuffer(final int entityId) {
        if (!this.metadataBuffer.containsKey(entityId)) {
            return;
        }
        if (this.entityReplacements.containsKey(entityId)) {
            this.entityReplacements.get(entityId).updateMetadata(this.metadataBuffer.remove(entityId));
        }
        else {
            final PacketWrapper wrapper = PacketWrapper.create(28, null, this.getUser());
            wrapper.write(Type.VAR_INT, entityId);
            wrapper.write(Types1_8.METADATA_LIST, this.metadataBuffer.get(entityId));
            MetadataRewriter.transform(this.getClientEntityTypes().get(entityId), this.metadataBuffer.get(entityId));
            if (!this.metadataBuffer.get(entityId).isEmpty()) {
                try {
                    wrapper.send(Protocol1_8TO1_9.class);
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            this.metadataBuffer.remove(entityId);
        }
    }
    
    @Override
    public void setClientEntityId(final int playerEntityId) {
        this.clientEntityTypes.remove(this.playerId);
        this.playerId = playerEntityId;
        this.clientEntityTypes.put(this.playerId, Entity1_10Types.EntityType.ENTITY_HUMAN);
    }
}
