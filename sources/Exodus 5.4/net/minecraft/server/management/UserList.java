/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Charsets
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  com.google.common.io.Files
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  com.google.gson.JsonDeserializationContext
 *  com.google.gson.JsonDeserializer
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParseException
 *  com.google.gson.JsonSerializationContext
 *  com.google.gson.JsonSerializer
 *  org.apache.commons.io.IOUtils
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.server.management;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import net.minecraft.server.management.UserListEntry;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserList<K, V extends UserListEntry<K>> {
    private static final ParameterizedType saveFileFormat;
    private boolean lanServer = true;
    protected final Gson gson;
    private final Map<String, V> values = Maps.newHashMap();
    protected static final Logger logger;
    private final File saveFile;

    protected Map<String, V> getValues() {
        return this.values;
    }

    static {
        logger = LogManager.getLogger();
        saveFileFormat = new ParameterizedType(){

            @Override
            public Type[] getActualTypeArguments() {
                return new Type[]{UserListEntry.class};
            }

            @Override
            public Type getOwnerType() {
                return null;
            }

            @Override
            public Type getRawType() {
                return List.class;
            }
        };
    }

    protected UserListEntry<K> createEntry(JsonObject jsonObject) {
        return new UserListEntry<Object>(null, jsonObject);
    }

    public void addEntry(V v) {
        this.values.put(this.getObjectKey(((UserListEntry)v).getValue()), v);
        try {
            this.writeChanges();
        }
        catch (IOException iOException) {
            logger.warn("Could not save the list after adding a user.", (Throwable)iOException);
        }
    }

    public void setLanServer(boolean bl) {
        this.lanServer = bl;
    }

    public boolean isLanServer() {
        return this.lanServer;
    }

    protected boolean hasEntry(K k) {
        return this.values.containsKey(this.getObjectKey(k));
    }

    public void removeEntry(K k) {
        this.values.remove(this.getObjectKey(k));
        try {
            this.writeChanges();
        }
        catch (IOException iOException) {
            logger.warn("Could not save the list after removing a user.", (Throwable)iOException);
        }
    }

    protected String getObjectKey(K k) {
        return k.toString();
    }

    public V getEntry(K k) {
        this.removeExpired();
        return (V)((UserListEntry)this.values.get(this.getObjectKey(k)));
    }

    public void writeChanges() throws IOException {
        Collection<V> collection = this.values.values();
        String string = this.gson.toJson(collection);
        BufferedWriter bufferedWriter = null;
        bufferedWriter = Files.newWriter((File)this.saveFile, (Charset)Charsets.UTF_8);
        bufferedWriter.write(string);
        IOUtils.closeQuietly((Writer)bufferedWriter);
    }

    public String[] getKeys() {
        return this.values.keySet().toArray(new String[this.values.size()]);
    }

    private void removeExpired() {
        ArrayList arrayList = Lists.newArrayList();
        for (Object object : this.values.values()) {
            if (!((UserListEntry)object).hasBanExpired()) continue;
            arrayList.add(((UserListEntry)object).getValue());
        }
        for (Object object : arrayList) {
            this.values.remove(object);
        }
    }

    public UserList(File file) {
        this.saveFile = file;
        GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();
        gsonBuilder.registerTypeHierarchyAdapter(UserListEntry.class, (Object)new Serializer());
        this.gson = gsonBuilder.create();
    }

    class Serializer
    implements JsonDeserializer<UserListEntry<K>>,
    JsonSerializer<UserListEntry<K>> {
        public UserListEntry<K> deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            if (jsonElement.isJsonObject()) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                UserListEntry userListEntry = UserList.this.createEntry(jsonObject);
                return userListEntry;
            }
            return null;
        }

        private Serializer() {
        }

        public JsonElement serialize(UserListEntry<K> userListEntry, Type type, JsonSerializationContext jsonSerializationContext) {
            JsonObject jsonObject = new JsonObject();
            userListEntry.onSerialization(jsonObject);
            return jsonObject;
        }
    }
}

