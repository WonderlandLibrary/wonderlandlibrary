// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viabackwards.api.data;

import com.viaversion.viabackwards.ViaBackwards;
import java.util.logging.Logger;
import com.viaversion.viaversion.api.data.Mappings;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.libs.gson.JsonObject;
import com.google.common.base.Preconditions;
import com.viaversion.viabackwards.api.BackwardsProtocol;
import java.util.Map;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectMap;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.api.data.MappingDataBase;

public class BackwardsMappings extends MappingDataBase
{
    private final Class<? extends Protocol> vvProtocolClass;
    private Int2ObjectMap<MappedItem> backwardsItemMappings;
    private Map<String, String> backwardsSoundMappings;
    private Map<String, String> entityNames;
    
    public BackwardsMappings(final String oldVersion, final String newVersion, final Class<? extends Protocol> vvProtocolClass) {
        this(oldVersion, newVersion, vvProtocolClass, false);
    }
    
    public BackwardsMappings(final String oldVersion, final String newVersion, final Class<? extends Protocol> vvProtocolClass, final boolean hasDiffFile) {
        super(oldVersion, newVersion, hasDiffFile);
        Preconditions.checkArgument(vvProtocolClass == null || !vvProtocolClass.isAssignableFrom(BackwardsProtocol.class));
        this.vvProtocolClass = vvProtocolClass;
        this.loadItems = false;
    }
    
    @Override
    protected void loadExtras(final JsonObject oldMappings, final JsonObject newMappings, final JsonObject diffMappings) {
        if (diffMappings != null) {
            final JsonObject diffItems = diffMappings.getAsJsonObject("items");
            if (diffItems != null) {
                this.backwardsItemMappings = VBMappingDataLoader.loadItemMappings(oldMappings.getAsJsonObject("items"), newMappings.getAsJsonObject("items"), diffItems, this.shouldWarnOnMissing("items"));
            }
            final JsonObject diffSounds = diffMappings.getAsJsonObject("sounds");
            if (diffSounds != null) {
                this.backwardsSoundMappings = VBMappingDataLoader.objectToNamespacedMap(diffSounds);
            }
            final JsonObject diffEntityNames = diffMappings.getAsJsonObject("entitynames");
            if (diffEntityNames != null) {
                this.entityNames = VBMappingDataLoader.objectToMap(diffEntityNames);
            }
        }
        if (this.vvProtocolClass != null) {
            this.itemMappings = ((Protocol)Via.getManager().getProtocolManager().getProtocol(this.vvProtocolClass)).getMappingData().getItemMappings().inverse();
        }
        this.loadVBExtras(oldMappings, newMappings);
    }
    
    @Override
    protected Mappings loadFromArray(final JsonObject oldMappings, final JsonObject newMappings, final JsonObject diffMappings, final String key) {
        if (!oldMappings.has(key) || !newMappings.has(key)) {
            return null;
        }
        final JsonObject diff = (diffMappings != null) ? diffMappings.getAsJsonObject(key) : null;
        return VBMappings.vbBuilder().unmapped(oldMappings.getAsJsonArray(key)).mapped(newMappings.getAsJsonArray(key)).diffMappings(diff).warnOnMissing(this.shouldWarnOnMissing(key)).build();
    }
    
    @Override
    protected Mappings loadFromObject(final JsonObject oldMappings, final JsonObject newMappings, final JsonObject diffMappings, final String key) {
        if (!oldMappings.has(key) || !newMappings.has(key)) {
            return null;
        }
        final JsonObject diff = (diffMappings != null) ? diffMappings.getAsJsonObject(key) : null;
        return VBMappings.vbBuilder().unmapped(oldMappings.getAsJsonObject(key)).mapped(newMappings.getAsJsonObject(key)).diffMappings(diff).warnOnMissing(this.shouldWarnOnMissing(key)).build();
    }
    
    @Override
    protected JsonObject loadDiffFile() {
        return VBMappingDataLoader.loadFromDataDir("mapping-" + this.newVersion + "to" + this.oldVersion + ".json");
    }
    
    protected void loadVBExtras(final JsonObject oldMappings, final JsonObject newMappings) {
    }
    
    protected boolean shouldWarnOnMissing(final String key) {
        return !key.equals("blocks") && !key.equals("statistics");
    }
    
    @Override
    protected Logger getLogger() {
        return ViaBackwards.getPlatform().getLogger();
    }
    
    @Override
    public int getNewItemId(final int id) {
        return this.itemMappings.get(id);
    }
    
    @Override
    public int getNewBlockId(final int id) {
        return this.blockMappings.getNewId(id);
    }
    
    @Override
    public int getOldItemId(final int id) {
        return this.checkValidity(id, this.itemMappings.inverse().get(id), "item");
    }
    
    public MappedItem getMappedItem(final int id) {
        return (this.backwardsItemMappings != null) ? this.backwardsItemMappings.get(id) : null;
    }
    
    public String getMappedNamedSound(String id) {
        if (this.backwardsSoundMappings == null) {
            return null;
        }
        if (id.indexOf(58) == -1) {
            id = "minecraft:" + id;
        }
        return this.backwardsSoundMappings.get(id);
    }
    
    public String mappedEntityName(final String entityName) {
        return this.entityNames.get(entityName);
    }
    
    public Int2ObjectMap<MappedItem> getBackwardsItemMappings() {
        return this.backwardsItemMappings;
    }
    
    public Map<String, String> getBackwardsSoundMappings() {
        return this.backwardsSoundMappings;
    }
}
