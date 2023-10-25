package net.minecraft.server.management;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.ParameterizedType;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import net.minecraft.server.management.UserList;
import net.minecraft.server.management.UserListEntry;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserList<K, V extends UserListEntry<K>> {
    protected static final Logger logger = LogManager.getLogger();
    protected final Gson gson;
    private final File saveFile;
    private final Map<String, V> values = Maps.newHashMap();
    private boolean lanServer = true;
    private static final ParameterizedType saveFileFormat = new /* Unavailable Anonymous Inner Class!! */;

    public UserList(File saveFile) {
        this.saveFile = saveFile;
        GsonBuilder gsonbuilder = new GsonBuilder().setPrettyPrinting();
        gsonbuilder.registerTypeHierarchyAdapter(UserListEntry.class, (Object)new Serializer(this, null));
        this.gson = gsonbuilder.create();
    }

    public boolean isLanServer() {
        return this.lanServer;
    }

    public void setLanServer(boolean state) {
        this.lanServer = state;
    }

    public void addEntry(V entry) {
        this.values.put((Object)this.getObjectKey(entry.getValue()), entry);
        try {
            this.writeChanges();
        }
        catch (IOException ioexception) {
            logger.warn("Could not save the list after adding a user.", (Throwable)ioexception);
        }
    }

    public V getEntry(K obj) {
        this.removeExpired();
        return (V)((UserListEntry)this.values.get((Object)this.getObjectKey(obj)));
    }

    public void removeEntry(K entry) {
        this.values.remove((Object)this.getObjectKey(entry));
        try {
            this.writeChanges();
        }
        catch (IOException ioexception) {
            logger.warn("Could not save the list after removing a user.", (Throwable)ioexception);
        }
    }

    public String[] getKeys() {
        return (String[])this.values.keySet().toArray((Object[])new String[this.values.size()]);
    }

    protected String getObjectKey(K obj) {
        return obj.toString();
    }

    protected boolean hasEntry(K entry) {
        return this.values.containsKey((Object)this.getObjectKey(entry));
    }

    private void removeExpired() {
        ArrayList list = Lists.newArrayList();
        for (UserListEntry v : this.values.values()) {
            if (!v.hasBanExpired()) continue;
            list.add(v.getValue());
        }
        for (Object k : list) {
            this.values.remove(k);
        }
    }

    protected UserListEntry<K> createEntry(JsonObject entryData) {
        return new UserListEntry(null, entryData);
    }

    protected Map<String, V> getValues() {
        return this.values;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void writeChanges() throws IOException {
        Collection collection = this.values.values();
        String s = this.gson.toJson((Object)collection);
        BufferedWriter bufferedwriter = null;
        try {
            bufferedwriter = Files.newWriter((File)this.saveFile, (Charset)Charsets.UTF_8);
            bufferedwriter.write(s);
        }
        catch (Throwable throwable) {
            IOUtils.closeQuietly(bufferedwriter);
            throw throwable;
        }
        IOUtils.closeQuietly((Writer)bufferedwriter);
    }
}
