// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viabackwards.protocol.protocol1_12_2to1_13.data;

import com.viaversion.viaversion.api.data.IntArrayMappings;
import java.util.Arrays;
import com.viaversion.viaversion.libs.gson.JsonPrimitive;
import com.viaversion.viaversion.libs.fastutil.objects.Object2IntMap;
import com.viaversion.viabackwards.ViaBackwards;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.libs.gson.JsonElement;
import com.viaversion.viaversion.api.data.MappingDataLoader;
import java.util.Iterator;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.data.StatisticMappings;
import com.viaversion.viabackwards.api.data.VBMappings;
import com.viaversion.viaversion.libs.gson.JsonObject;
import java.util.HashMap;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectOpenHashMap;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.Protocol1_13To1_12_2;
import com.viaversion.viaversion.api.data.Mappings;
import java.util.Map;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectMap;

public class BackwardsMappings extends com.viaversion.viabackwards.api.data.BackwardsMappings
{
    private final Int2ObjectMap<String> statisticMappings;
    private final Map<String, String> translateMappings;
    private Mappings enchantmentMappings;
    
    public BackwardsMappings() {
        super("1.13", "1.12", Protocol1_13To1_12_2.class, true);
        this.statisticMappings = new Int2ObjectOpenHashMap<String>();
        this.translateMappings = new HashMap<String, String>();
    }
    
    public void loadVBExtras(final JsonObject oldMappings, final JsonObject newMappings) {
        this.enchantmentMappings = VBMappings.vbBuilder().warnOnMissing(false).unmapped(oldMappings.getAsJsonObject("enchantments")).mapped(newMappings.getAsJsonObject("enchantments")).build();
        for (final Map.Entry<String, Integer> entry : StatisticMappings.CUSTOM_STATS.entrySet()) {
            this.statisticMappings.put((int)entry.getValue(), entry.getKey());
        }
        for (final Map.Entry<String, String> entry2 : Protocol1_13To1_12_2.MAPPINGS.getTranslateMapping().entrySet()) {
            this.translateMappings.put(entry2.getValue(), entry2.getKey());
        }
    }
    
    private static void mapIdentifiers(final int[] output, final JsonObject newIdentifiers, final JsonObject oldIdentifiers, final JsonObject mapping) {
        final Object2IntMap<String> newIdentifierMap = MappingDataLoader.indexedObjectToMap(oldIdentifiers);
        for (final Map.Entry<String, JsonElement> entry : newIdentifiers.entrySet()) {
            final String key = entry.getValue().getAsString();
            int value = newIdentifierMap.getInt(key);
            short hardId = -1;
            if (value == -1) {
                JsonPrimitive replacement = mapping.getAsJsonPrimitive(key);
                final int propertyIndex;
                if (replacement == null && (propertyIndex = key.indexOf(91)) != -1) {
                    replacement = mapping.getAsJsonPrimitive(key.substring(0, propertyIndex));
                }
                if (replacement != null) {
                    if (replacement.getAsString().startsWith("id:")) {
                        final String id = replacement.getAsString().replace("id:", "");
                        hardId = Short.parseShort(id);
                        value = newIdentifierMap.getInt(oldIdentifiers.getAsJsonPrimitive(id).getAsString());
                    }
                    else {
                        value = newIdentifierMap.getInt(replacement.getAsString());
                    }
                }
                if (value == -1) {
                    if (Via.getConfig().isSuppressConversionWarnings() && !Via.getManager().isDebug()) {
                        continue;
                    }
                    if (replacement != null) {
                        ViaBackwards.getPlatform().getLogger().warning("No key for " + entry.getValue() + "/" + replacement.getAsString() + " :( ");
                        continue;
                    }
                    ViaBackwards.getPlatform().getLogger().warning("No key for " + entry.getValue() + " :( ");
                    continue;
                }
            }
            output[Integer.parseInt(entry.getKey())] = ((hardId != -1) ? hardId : ((short)value));
        }
    }
    
    @Override
    protected Mappings loadFromObject(final JsonObject oldMappings, final JsonObject newMappings, final JsonObject diffMappings, final String key) {
        if (key.equals("blockstates")) {
            final int[] oldToNew = new int[8582];
            Arrays.fill(oldToNew, -1);
            mapIdentifiers(oldToNew, oldMappings.getAsJsonObject("blockstates"), newMappings.getAsJsonObject("blocks"), diffMappings.getAsJsonObject("blockstates"));
            return IntArrayMappings.of(oldToNew, -1);
        }
        return super.loadFromObject(oldMappings, newMappings, diffMappings, key);
    }
    
    @Override
    public int getNewBlockStateId(final int id) {
        final int mappedId = super.getNewBlockStateId(id);
        switch (mappedId) {
            case 1595:
            case 1596:
            case 1597: {
                return 1584;
            }
            case 1611:
            case 1612:
            case 1613: {
                return 1600;
            }
            default: {
                return mappedId;
            }
        }
    }
    
    @Override
    protected int checkValidity(final int id, final int mappedId, final String type) {
        return mappedId;
    }
    
    @Override
    protected boolean shouldWarnOnMissing(final String key) {
        return super.shouldWarnOnMissing(key) && !key.equals("items");
    }
    
    public Int2ObjectMap<String> getStatisticMappings() {
        return this.statisticMappings;
    }
    
    public Map<String, String> getTranslateMappings() {
        return this.translateMappings;
    }
    
    public Mappings getEnchantmentMappings() {
        return this.enchantmentMappings;
    }
}
