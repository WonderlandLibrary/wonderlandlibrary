// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.resources;

import org.apache.logging.log4j.LogManager;
import javax.annotation.Nullable;
import net.minecraft.util.ResourceLocation;
import java.util.Iterator;
import java.io.BufferedReader;
import org.apache.commons.io.IOUtils;
import java.io.FileNotFoundException;
import com.google.gson.JsonParseException;
import com.google.gson.JsonElement;
import net.minecraft.util.JsonUtils;
import com.google.gson.JsonObject;
import java.io.Reader;
import com.google.gson.JsonParser;
import com.google.common.io.Files;
import java.nio.charset.StandardCharsets;
import com.google.common.collect.Maps;
import java.io.File;
import java.util.Map;
import org.apache.logging.log4j.Logger;

public class ResourceIndex
{
    private static final Logger LOGGER;
    private final Map<String, File> resourceMap;
    
    protected ResourceIndex() {
        this.resourceMap = (Map<String, File>)Maps.newHashMap();
    }
    
    public ResourceIndex(final File assetsFolder, final String indexName) {
        this.resourceMap = (Map<String, File>)Maps.newHashMap();
        final File file1 = new File(assetsFolder, "objects");
        final File file2 = new File(assetsFolder, "indexes/" + indexName + ".json");
        BufferedReader bufferedreader = null;
        try {
            bufferedreader = Files.newReader(file2, StandardCharsets.UTF_8);
            final JsonObject jsonobject = new JsonParser().parse((Reader)bufferedreader).getAsJsonObject();
            final JsonObject jsonobject2 = JsonUtils.getJsonObject(jsonobject, "objects", null);
            if (jsonobject2 != null) {
                for (final Map.Entry<String, JsonElement> entry : jsonobject2.entrySet()) {
                    final JsonObject jsonobject3 = (JsonObject)entry.getValue();
                    final String s = entry.getKey();
                    final String[] astring = s.split("/", 2);
                    final String s2 = (astring.length == 1) ? astring[0] : (astring[0] + ":" + astring[1]);
                    final String s3 = JsonUtils.getString(jsonobject3, "hash");
                    final File file3 = new File(file1, s3.substring(0, 2) + "/" + s3);
                    this.resourceMap.put(s2, file3);
                }
            }
        }
        catch (JsonParseException var20) {
            ResourceIndex.LOGGER.error("Unable to parse resource index file: {}", (Object)file2);
        }
        catch (FileNotFoundException var21) {
            ResourceIndex.LOGGER.error("Can't find the resource index file: {}", (Object)file2);
        }
        finally {
            IOUtils.closeQuietly((Reader)bufferedreader);
        }
    }
    
    @Nullable
    public File getFile(final ResourceLocation location) {
        final String s = location.toString();
        return this.resourceMap.get(s);
    }
    
    public boolean isFileExisting(final ResourceLocation location) {
        final File file1 = this.getFile(location);
        return file1 != null && file1.isFile();
    }
    
    public File getPackMcmeta() {
        return this.resourceMap.get("pack.mcmeta");
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
