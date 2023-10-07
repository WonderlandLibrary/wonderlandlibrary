// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.server.management;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonDeserializer;
import java.util.List;
import java.lang.reflect.Type;
import org.apache.logging.log4j.LogManager;
import java.io.BufferedWriter;
import java.util.Collection;
import java.io.Writer;
import org.apache.commons.io.IOUtils;
import com.google.common.io.Files;
import com.google.common.base.Charsets;
import com.google.gson.JsonObject;
import java.util.Iterator;
import java.util.ArrayList;
import com.google.common.collect.Lists;
import java.io.IOException;
import com.google.gson.GsonBuilder;
import com.google.common.collect.Maps;
import java.lang.reflect.ParameterizedType;
import java.util.Map;
import java.io.File;
import com.google.gson.Gson;
import org.apache.logging.log4j.Logger;

public class UserList
{
    protected static final Logger logger;
    protected final Gson gson;
    private final File saveFile;
    private final Map values;
    private boolean lanServer;
    private static final ParameterizedType saveFileFormat;
    private static final String __OBFID = "CL_00001876";
    
    public UserList(final File saveFile) {
        this.values = Maps.newHashMap();
        this.lanServer = true;
        this.saveFile = saveFile;
        final GsonBuilder var2 = new GsonBuilder().setPrettyPrinting();
        var2.registerTypeHierarchyAdapter((Class)UserListEntry.class, (Object)new Serializer(null));
        this.gson = var2.create();
    }
    
    public boolean isLanServer() {
        return this.lanServer;
    }
    
    public void setLanServer(final boolean state) {
        this.lanServer = state;
    }
    
    public void addEntry(final UserListEntry entry) {
        this.values.put(this.getObjectKey(entry.getValue()), entry);
        try {
            this.writeChanges();
        }
        catch (IOException var3) {
            UserList.logger.warn("Could not save the list after adding a user.", (Throwable)var3);
        }
    }
    
    public UserListEntry getEntry(final Object obj) {
        this.removeExpired();
        return this.values.get(this.getObjectKey(obj));
    }
    
    public void removeEntry(final Object p_152684_1_) {
        this.values.remove(this.getObjectKey(p_152684_1_));
        try {
            this.writeChanges();
        }
        catch (IOException var3) {
            UserList.logger.warn("Could not save the list after removing a user.", (Throwable)var3);
        }
    }
    
    public String[] getKeys() {
        return (String[])this.values.keySet().toArray(new String[this.values.size()]);
    }
    
    protected String getObjectKey(final Object obj) {
        return obj.toString();
    }
    
    protected boolean hasEntry(final Object entry) {
        return this.values.containsKey(this.getObjectKey(entry));
    }
    
    private void removeExpired() {
        final ArrayList var1 = Lists.newArrayList();
        for (final UserListEntry var3 : this.values.values()) {
            if (var3.hasBanExpired()) {
                var1.add(var3.getValue());
            }
        }
        for (final Object var4 : var1) {
            this.values.remove(var4);
        }
    }
    
    protected UserListEntry createEntry(final JsonObject entryData) {
        return new UserListEntry(null, entryData);
    }
    
    protected Map getValues() {
        return this.values;
    }
    
    public void writeChanges() throws IOException {
        final Collection var1 = this.values.values();
        final String var2 = this.gson.toJson((Object)var1);
        BufferedWriter var3 = null;
        try {
            var3 = Files.newWriter(this.saveFile, Charsets.UTF_8);
            var3.write(var2);
        }
        finally {
            IOUtils.closeQuietly((Writer)var3);
        }
    }
    
    static {
        logger = LogManager.getLogger();
        saveFileFormat = new ParameterizedType() {
            private static final String __OBFID = "CL_00001875";
            
            @Override
            public Type[] getActualTypeArguments() {
                return new Type[] { UserListEntry.class };
            }
            
            @Override
            public Type getRawType() {
                return List.class;
            }
            
            @Override
            public Type getOwnerType() {
                return null;
            }
        };
    }
    
    class Serializer implements JsonDeserializer, JsonSerializer
    {
        private static final String __OBFID = "CL_00001874";
        
        private Serializer() {
        }
        
        public JsonElement serializeEntry(final UserListEntry p_152751_1_, final Type p_152751_2_, final JsonSerializationContext p_152751_3_) {
            final JsonObject var4 = new JsonObject();
            p_152751_1_.onSerialization(var4);
            return (JsonElement)var4;
        }
        
        public UserListEntry deserializeEntry(final JsonElement p_152750_1_, final Type p_152750_2_, final JsonDeserializationContext p_152750_3_) {
            if (p_152750_1_.isJsonObject()) {
                final JsonObject var4 = p_152750_1_.getAsJsonObject();
                final UserListEntry var5 = UserList.this.createEntry(var4);
                return var5;
            }
            return null;
        }
        
        public JsonElement serialize(final Object p_serialize_1_, final Type p_serialize_2_, final JsonSerializationContext p_serialize_3_) {
            return this.serializeEntry((UserListEntry)p_serialize_1_, p_serialize_2_, p_serialize_3_);
        }
        
        public Object deserialize(final JsonElement p_deserialize_1_, final Type p_deserialize_2_, final JsonDeserializationContext p_deserialize_3_) {
            return this.deserializeEntry(p_deserialize_1_, p_deserialize_2_, p_deserialize_3_);
        }
        
        Serializer(final UserList this$0, final Object p_i1141_2_) {
            this(this$0);
        }
    }
}
