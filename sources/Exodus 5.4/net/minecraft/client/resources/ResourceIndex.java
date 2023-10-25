/*
 * Decompiled with CFR 0.152.
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
    private static final Logger logger = LogManager.getLogger();
    private final Map<String, File> resourceMap;

    public Map<String, File> getResourceMap() {
        return this.resourceMap;
    }

    public ResourceIndex(File file, String string) {
        block6: {
            this.resourceMap = Maps.newHashMap();
            if (string != null) {
                BufferedReader bufferedReader;
                block5: {
                    File file2 = new File(file, "objects");
                    File file3 = new File(file, "indexes/" + string + ".json");
                    bufferedReader = null;
                    try {
                        bufferedReader = Files.newReader((File)file3, (Charset)Charsets.UTF_8);
                        JsonObject jsonObject = new JsonParser().parse((Reader)bufferedReader).getAsJsonObject();
                        JsonObject jsonObject2 = JsonUtils.getJsonObject(jsonObject, "objects", null);
                        if (jsonObject2 == null) break block5;
                        for (Map.Entry entry : jsonObject2.entrySet()) {
                            JsonObject jsonObject3 = (JsonObject)entry.getValue();
                            String string2 = (String)entry.getKey();
                            String[] stringArray = string2.split("/", 2);
                            String string3 = stringArray.length == 1 ? stringArray[0] : String.valueOf(stringArray[0]) + ":" + stringArray[1];
                            String string4 = JsonUtils.getString(jsonObject3, "hash");
                            File file4 = new File(file2, String.valueOf(string4.substring(0, 2)) + "/" + string4);
                            this.resourceMap.put(string3, file4);
                        }
                    }
                    catch (JsonParseException jsonParseException) {
                        logger.error("Unable to parse resource index file: " + file3);
                        IOUtils.closeQuietly((Reader)bufferedReader);
                        break block6;
                    }
                    catch (FileNotFoundException fileNotFoundException) {
                        logger.error("Can't find the resource index file: " + file3);
                        IOUtils.closeQuietly((Reader)bufferedReader);
                        break block6;
                    }
                }
                IOUtils.closeQuietly((Reader)bufferedReader);
            }
        }
    }
}

