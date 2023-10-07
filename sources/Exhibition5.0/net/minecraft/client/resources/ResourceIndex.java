// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.resources;

import org.apache.logging.log4j.LogManager;
import java.util.Iterator;
import java.io.BufferedReader;
import org.apache.commons.io.IOUtils;
import java.io.FileNotFoundException;
import com.google.gson.JsonParseException;
import net.minecraft.util.JsonUtils;
import com.google.gson.JsonObject;
import java.io.Reader;
import com.google.gson.JsonParser;
import com.google.common.io.Files;
import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import java.io.File;
import java.util.Map;
import org.apache.logging.log4j.Logger;

public class ResourceIndex
{
    private static final Logger field_152783_a;
    private final Map field_152784_b;
    private static final String __OBFID = "CL_00001831";
    
    public ResourceIndex(final File p_i1047_1_, final String p_i1047_2_) {
        this.field_152784_b = Maps.newHashMap();
        if (p_i1047_2_ != null) {
            final File var3 = new File(p_i1047_1_, "objects");
            final File var4 = new File(p_i1047_1_, "indexes/" + p_i1047_2_ + ".json");
            BufferedReader var5 = null;
            try {
                var5 = Files.newReader(var4, Charsets.UTF_8);
                final JsonObject var6 = new JsonParser().parse((Reader)var5).getAsJsonObject();
                final JsonObject var7 = JsonUtils.getJsonObjectFieldOrDefault(var6, "objects", null);
                if (var7 != null) {
                    for (final Map.Entry var9 : var7.entrySet()) {
                        final JsonObject var10 = var9.getValue();
                        final String var11 = var9.getKey();
                        final String[] var12 = var11.split("/", 2);
                        final String var13 = (var12.length == 1) ? var12[0] : (var12[0] + ":" + var12[1]);
                        final String var14 = JsonUtils.getJsonObjectStringFieldValue(var10, "hash");
                        final File var15 = new File(var3, var14.substring(0, 2) + "/" + var14);
                        this.field_152784_b.put(var13, var15);
                    }
                }
            }
            catch (JsonParseException var16) {
                ResourceIndex.field_152783_a.error("Unable to parse resource index file: " + var4);
            }
            catch (FileNotFoundException var17) {
                ResourceIndex.field_152783_a.error("Can't find the resource index file: " + var4);
            }
            finally {
                IOUtils.closeQuietly((Reader)var5);
            }
        }
    }
    
    public Map func_152782_a() {
        return this.field_152784_b;
    }
    
    static {
        field_152783_a = LogManager.getLogger();
    }
}
