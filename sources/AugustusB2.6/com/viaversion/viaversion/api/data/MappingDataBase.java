// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.data;

import com.viaversion.viaversion.api.Via;
import java.util.logging.Logger;
import com.viaversion.viaversion.libs.gson.JsonArray;
import java.util.Iterator;
import com.viaversion.viaversion.libs.gson.JsonElement;
import java.util.ArrayList;
import com.viaversion.viaversion.libs.fastutil.objects.Object2IntMap;
import com.viaversion.viaversion.libs.gson.JsonObject;
import java.util.EnumMap;
import com.viaversion.viaversion.util.Int2IntBiHashMap;
import com.viaversion.viaversion.api.minecraft.TagData;
import java.util.List;
import com.viaversion.viaversion.api.minecraft.RegistryType;
import java.util.Map;
import com.viaversion.viaversion.util.Int2IntBiMap;

public class MappingDataBase implements MappingData
{
    protected final String oldVersion;
    protected final String newVersion;
    protected final boolean hasDiffFile;
    protected Int2IntBiMap itemMappings;
    protected ParticleMappings particleMappings;
    protected Mappings blockMappings;
    protected Mappings blockStateMappings;
    protected Mappings blockEntityMappings;
    protected Mappings soundMappings;
    protected Mappings statisticsMappings;
    protected Map<RegistryType, List<TagData>> tags;
    protected boolean loadItems;
    
    public MappingDataBase(final String oldVersion, final String newVersion) {
        this(oldVersion, newVersion, false);
    }
    
    public MappingDataBase(final String oldVersion, final String newVersion, final boolean hasDiffFile) {
        this.loadItems = true;
        this.oldVersion = oldVersion;
        this.newVersion = newVersion;
        this.hasDiffFile = hasDiffFile;
    }
    
    @Override
    public void load() {
        this.getLogger().info("Loading " + this.oldVersion + " -> " + this.newVersion + " mappings...");
        final JsonObject diffmapping = this.hasDiffFile ? this.loadDiffFile() : null;
        final JsonObject oldMappings = MappingDataLoader.loadData("mapping-" + this.oldVersion + ".json", true);
        final JsonObject newMappings = MappingDataLoader.loadData("mapping-" + this.newVersion + ".json", true);
        this.blockMappings = this.loadFromObject(oldMappings, newMappings, diffmapping, "blocks");
        this.blockStateMappings = this.loadFromObject(oldMappings, newMappings, diffmapping, "blockstates");
        this.blockEntityMappings = this.loadFromArray(oldMappings, newMappings, diffmapping, "blockentities");
        this.soundMappings = this.loadFromArray(oldMappings, newMappings, diffmapping, "sounds");
        this.statisticsMappings = this.loadFromArray(oldMappings, newMappings, diffmapping, "statistics");
        final Mappings particles = this.loadFromArray(oldMappings, newMappings, diffmapping, "particles");
        if (particles != null) {
            this.particleMappings = new ParticleMappings(oldMappings.getAsJsonArray("particles"), newMappings.getAsJsonArray("particles"), particles);
        }
        if (this.loadItems && newMappings.has("items")) {
            (this.itemMappings = new Int2IntBiHashMap()).defaultReturnValue(-1);
            MappingDataLoader.mapIdentifiers(this.itemMappings, oldMappings.getAsJsonObject("items"), newMappings.getAsJsonObject("items"), (diffmapping != null) ? diffmapping.getAsJsonObject("items") : null, true);
        }
        if (diffmapping != null && diffmapping.has("tags")) {
            this.tags = new EnumMap<RegistryType, List<TagData>>(RegistryType.class);
            final JsonObject tags = diffmapping.getAsJsonObject("tags");
            if (tags.has(RegistryType.ITEM.resourceLocation())) {
                this.loadTags(RegistryType.ITEM, tags, MappingDataLoader.indexedObjectToMap(newMappings.getAsJsonObject("items")));
            }
            if (tags.has(RegistryType.BLOCK.resourceLocation())) {
                this.loadTags(RegistryType.BLOCK, tags, MappingDataLoader.indexedObjectToMap(newMappings.getAsJsonObject("blocks")));
            }
        }
        this.loadExtras(oldMappings, newMappings, diffmapping);
    }
    
    private void loadTags(final RegistryType type, final JsonObject object, final Object2IntMap<String> typeMapping) {
        final JsonObject tags = object.getAsJsonObject(type.resourceLocation());
        final List<TagData> tagsList = new ArrayList<TagData>(tags.size());
        for (final Map.Entry<String, JsonElement> entry : tags.entrySet()) {
            final JsonArray array = entry.getValue().getAsJsonArray();
            final int[] entries = new int[array.size()];
            int i = 0;
            for (final JsonElement element : array) {
                String stringId = element.getAsString();
                if (!typeMapping.containsKey(stringId) && !typeMapping.containsKey(stringId = stringId.replace("minecraft:", ""))) {
                    this.getLogger().warning(type + " Tags contains invalid type identifier " + stringId + " in tag " + entry.getKey());
                }
                else {
                    entries[i++] = typeMapping.getInt(stringId);
                }
            }
            tagsList.add(new TagData(entry.getKey(), entries));
        }
        this.tags.put(type, tagsList);
    }
    
    @Override
    public int getNewBlockStateId(final int id) {
        return this.checkValidity(id, this.blockStateMappings.getNewId(id), "blockstate");
    }
    
    @Override
    public int getNewBlockId(final int id) {
        return this.checkValidity(id, this.blockMappings.getNewId(id), "block");
    }
    
    @Override
    public int getNewItemId(final int id) {
        return this.checkValidity(id, this.itemMappings.get(id), "item");
    }
    
    @Override
    public int getOldItemId(final int id) {
        final int oldId = this.itemMappings.inverse().get(id);
        return (oldId != -1) ? oldId : 1;
    }
    
    @Override
    public int getNewParticleId(final int id) {
        return this.checkValidity(id, this.particleMappings.getMappings().getNewId(id), "particles");
    }
    
    @Override
    public List<TagData> getTags(final RegistryType type) {
        return (this.tags != null) ? this.tags.get(type) : null;
    }
    
    @Override
    public Int2IntBiMap getItemMappings() {
        return this.itemMappings;
    }
    
    @Override
    public ParticleMappings getParticleMappings() {
        return this.particleMappings;
    }
    
    @Override
    public Mappings getBlockMappings() {
        return this.blockMappings;
    }
    
    @Override
    public Mappings getBlockEntityMappings() {
        return this.blockEntityMappings;
    }
    
    @Override
    public Mappings getBlockStateMappings() {
        return this.blockStateMappings;
    }
    
    @Override
    public Mappings getSoundMappings() {
        return this.soundMappings;
    }
    
    @Override
    public Mappings getStatisticsMappings() {
        return this.statisticsMappings;
    }
    
    protected Mappings loadFromArray(final JsonObject oldMappings, final JsonObject newMappings, final JsonObject diffMappings, final String key) {
        if (!oldMappings.has(key) || !newMappings.has(key)) {
            return null;
        }
        final JsonObject diff = (diffMappings != null) ? diffMappings.getAsJsonObject(key) : null;
        return IntArrayMappings.builder().unmapped(oldMappings.getAsJsonArray(key)).mapped(newMappings.getAsJsonArray(key)).diffMappings(diff).build();
    }
    
    protected Mappings loadFromObject(final JsonObject oldMappings, final JsonObject newMappings, final JsonObject diffMappings, final String key) {
        if (!oldMappings.has(key) || !newMappings.has(key)) {
            return null;
        }
        final JsonObject diff = (diffMappings != null) ? diffMappings.getAsJsonObject(key) : null;
        return IntArrayMappings.builder().unmapped(oldMappings.getAsJsonObject(key)).mapped(newMappings.getAsJsonObject(key)).diffMappings(diff).build();
    }
    
    protected JsonObject loadDiffFile() {
        return MappingDataLoader.loadData("mappingdiff-" + this.oldVersion + "to" + this.newVersion + ".json");
    }
    
    protected Logger getLogger() {
        return Via.getPlatform().getLogger();
    }
    
    protected int checkValidity(final int id, final int mappedId, final String type) {
        if (mappedId == -1) {
            this.getLogger().warning(String.format("Missing %s %s for %s %s %d", this.newVersion, type, this.oldVersion, type, id));
            return 0;
        }
        return mappedId;
    }
    
    protected void loadExtras(final JsonObject oldMappings, final JsonObject newMappings, final JsonObject diffMappings) {
    }
}
