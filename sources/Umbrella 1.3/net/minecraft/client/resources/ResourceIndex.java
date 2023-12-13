/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Charsets
 *  com.google.common.collect.Maps
 *  com.google.common.io.Files
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParseException
 *  com.google.gson.JsonParser
 *  org.apache.commons.io.IOUtils
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.client.resources;

import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Map;
import net.minecraft.util.JsonUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ResourceIndex {
    private static final Logger field_152783_a = LogManager.getLogger();
    private final Map field_152784_b;
    private static final String __OBFID = "CL_00001831";

    public ResourceIndex(File p_i1047_1_, String p_i1047_2_) {
        block9: {
            this.field_152784_b = Maps.newHashMap();
            if (p_i1047_2_ != null) {
                File var3 = new File(p_i1047_1_, "objects");
                File var4 = new File(p_i1047_1_, "indexes/" + p_i1047_2_ + ".json");
                BufferedReader var5 = null;
                try {
                    var5 = Files.newReader((File)var4, (Charset)Charsets.UTF_8);
                    JsonObject var6 = new JsonParser().parse((Reader)var5).getAsJsonObject();
                    JsonObject var7 = JsonUtils.getJsonObjectFieldOrDefault(var6, "objects", null);
                    if (var7 != null) {
                        for (Map.Entry var9 : var7.entrySet()) {
                            JsonObject var10 = (JsonObject)var9.getValue();
                            String var11 = (String)var9.getKey();
                            String[] var12 = var11.split("/", 2);
                            String var13 = var12.length == 1 ? var12[0] : String.valueOf(var12[0]) + ":" + var12[1];
                            String var14 = JsonUtils.getJsonObjectStringFieldValue(var10, "hash");
                            File var15 = new File(var3, String.valueOf(var14.substring(0, 2)) + "/" + var14);
                            this.field_152784_b.put(var13, var15);
                        }
                    }
                }
                catch (JsonParseException var20) {
                    field_152783_a.error("Unable to parse resource index file: " + var4);
                    IOUtils.closeQuietly((Reader)var5);
                    break block9;
                }
                catch (FileNotFoundException var21) {
                    try {
                        field_152783_a.error("Can't find the resource index file: " + var4);
                    }
                    catch (Throwable throwable) {
                        IOUtils.closeQuietly(var5);
                        throw throwable;
                    }
                    IOUtils.closeQuietly((Reader)var5);
                    break block9;
                }
                IOUtils.closeQuietly((Reader)var5);
            }
        }
    }

    public Map func_152782_a() {
        return this.field_152784_b;
    }
}

