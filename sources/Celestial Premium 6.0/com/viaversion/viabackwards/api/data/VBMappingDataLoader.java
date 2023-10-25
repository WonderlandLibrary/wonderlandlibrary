/*
 * Decompiled with CFR 0.150.
 */
package com.viaversion.viabackwards.api.data;

import com.viaversion.viabackwards.ViaBackwards;
import com.viaversion.viabackwards.api.data.MappedItem;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.data.MappingDataLoader;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectMap;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectOpenHashMap;
import com.viaversion.viaversion.libs.fastutil.objects.Object2IntMap;
import com.viaversion.viaversion.libs.gson.JsonElement;
import com.viaversion.viaversion.libs.gson.JsonIOException;
import com.viaversion.viaversion.libs.gson.JsonObject;
import com.viaversion.viaversion.libs.gson.JsonPrimitive;
import com.viaversion.viaversion.libs.gson.JsonSyntaxException;
import com.viaversion.viaversion.util.GsonUtil;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

public class VBMappingDataLoader {
    public static JsonObject loadFromDataDir(String name) {
        JsonObject jsonObject;
        File file = new File(ViaBackwards.getPlatform().getDataFolder(), name);
        if (!file.exists()) {
            return VBMappingDataLoader.loadData(name);
        }
        FileReader reader = new FileReader(file);
        try {
            jsonObject = GsonUtil.getGson().fromJson((Reader)reader, JsonObject.class);
        }
        catch (Throwable throwable) {
            try {
                try {
                    reader.close();
                }
                catch (Throwable throwable2) {
                    throwable.addSuppressed(throwable2);
                }
                throw throwable;
            }
            catch (JsonSyntaxException e) {
                ViaBackwards.getPlatform().getLogger().warning(name + " is badly formatted!");
                e.printStackTrace();
                ViaBackwards.getPlatform().getLogger().warning("Falling back to resource's file!");
                return VBMappingDataLoader.loadData(name);
            }
            catch (JsonIOException | IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        reader.close();
        return jsonObject;
    }

    public static JsonObject loadData(String name) {
        JsonObject jsonObject;
        InputStream stream = VBMappingDataLoader.class.getClassLoader().getResourceAsStream("assets/viabackwards/data/" + name);
        InputStreamReader reader = new InputStreamReader(stream);
        try {
            jsonObject = GsonUtil.getGson().fromJson((Reader)reader, JsonObject.class);
        }
        catch (Throwable throwable) {
            try {
                try {
                    reader.close();
                }
                catch (Throwable throwable2) {
                    throwable.addSuppressed(throwable2);
                }
                throw throwable;
            }
            catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        reader.close();
        return jsonObject;
    }

    public static void mapIdentifiers(int[] output, JsonObject oldIdentifiers, JsonObject newIdentifiers, JsonObject diffIdentifiers) {
        VBMappingDataLoader.mapIdentifiers(output, oldIdentifiers, newIdentifiers, diffIdentifiers, true);
    }

    public static void mapIdentifiers(int[] output, JsonObject oldIdentifiers, JsonObject newIdentifiers, JsonObject diffIdentifiers, boolean warnOnMissing) {
        Object2IntMap<String> newIdentifierMap = MappingDataLoader.indexedObjectToMap(newIdentifiers);
        for (Map.Entry<String, JsonElement> entry : oldIdentifiers.entrySet()) {
            String key = entry.getValue().getAsString();
            int mappedId = newIdentifierMap.getInt(key);
            if (mappedId == -1) {
                if (diffIdentifiers != null) {
                    int dataIndex;
                    String diffValue;
                    JsonPrimitive diffValueJson = diffIdentifiers.getAsJsonPrimitive(key);
                    String string = diffValue = diffValueJson != null ? diffValueJson.getAsString() : null;
                    if (diffValue == null && (dataIndex = key.indexOf(91)) != -1 && (diffValueJson = diffIdentifiers.getAsJsonPrimitive(key.substring(0, dataIndex))) != null && (diffValue = diffValueJson.getAsString()).endsWith("[")) {
                        diffValue = diffValue + key.substring(dataIndex + 1);
                    }
                    if (diffValue != null) {
                        mappedId = newIdentifierMap.getInt(diffValue);
                    }
                }
                if (mappedId == -1) {
                    if ((!warnOnMissing || Via.getConfig().isSuppressConversionWarnings()) && !Via.getManager().isDebug()) continue;
                    ViaBackwards.getPlatform().getLogger().warning("No key for " + entry.getValue() + " :( ");
                    continue;
                }
            }
            output[Integer.parseInt((String)entry.getKey())] = mappedId;
        }
    }

    public static Map<String, String> objectToMap(JsonObject object) {
        HashMap<String, String> mappings = new HashMap<String, String>();
        for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
            String value;
            String key = entry.getKey();
            if (key.indexOf(58) == -1) {
                key = "minecraft:" + key;
            }
            if ((value = entry.getValue().getAsString()).indexOf(58) == -1) {
                value = "minecraft:" + value;
            }
            mappings.put(key, value);
        }
        return mappings;
    }

    public static Int2ObjectMap<MappedItem> loadItemMappings(JsonObject oldMapping, JsonObject newMapping, JsonObject diffMapping) {
        return VBMappingDataLoader.loadItemMappings(oldMapping, newMapping, diffMapping, false);
    }

    public static Int2ObjectMap<MappedItem> loadItemMappings(JsonObject oldMapping, JsonObject newMapping, JsonObject diffMapping, boolean warnOnMissing) {
        Int2ObjectOpenHashMap<MappedItem> itemMapping = new Int2ObjectOpenHashMap<MappedItem>(diffMapping.size(), 1.0f);
        Object2IntMap<String> newIdenfierMap = MappingDataLoader.indexedObjectToMap(newMapping);
        Object2IntMap<String> oldIdenfierMap = MappingDataLoader.indexedObjectToMap(oldMapping);
        for (Map.Entry<String, JsonElement> entry : diffMapping.entrySet()) {
            JsonObject object = entry.getValue().getAsJsonObject();
            String mappedIdName = object.getAsJsonPrimitive("id").getAsString();
            int mappedId = newIdenfierMap.getInt(mappedIdName);
            if (mappedId == -1) {
                if (Via.getConfig().isSuppressConversionWarnings() && !Via.getManager().isDebug()) continue;
                ViaBackwards.getPlatform().getLogger().warning("No key for " + mappedIdName + " :( ");
                continue;
            }
            int oldId = oldIdenfierMap.getInt(entry.getKey());
            if (oldId == -1) {
                if (Via.getConfig().isSuppressConversionWarnings() && !Via.getManager().isDebug()) continue;
                ViaBackwards.getPlatform().getLogger().warning("No old entry for " + mappedIdName + " :( ");
                continue;
            }
            String name = object.getAsJsonPrimitive("name").getAsString();
            itemMapping.put(oldId, new MappedItem(mappedId, name));
        }
        if (warnOnMissing && !Via.getConfig().isSuppressConversionWarnings()) {
            for (Object2IntMap.Entry entry : oldIdenfierMap.object2IntEntrySet()) {
                if (newIdenfierMap.containsKey(entry.getKey()) || itemMapping.containsKey(entry.getIntValue())) continue;
                ViaBackwards.getPlatform().getLogger().warning("No item mapping for " + (String)entry.getKey() + " :( ");
            }
        }
        return itemMapping;
    }
}

