// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.protocol1_13to1_12_2.data;

import java.util.Iterator;
import java.io.InputStream;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectOpenHashMap;
import java.io.IOException;
import com.google.common.collect.ObjectArrays;
import java.util.HashMap;
import java.io.Reader;
import com.viaversion.viaversion.libs.gson.reflect.TypeToken;
import com.viaversion.viaversion.util.GsonUtil;
import java.io.InputStreamReader;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectMap;
import java.util.Map;

public class BlockIdData
{
    public static final String[] PREVIOUS;
    public static Map<String, String[]> blockIdMapping;
    public static Map<String, String[]> fallbackReverseMapping;
    public static Int2ObjectMap<String> numberIdToString;
    
    public static void init() {
        final InputStream stream = MappingData.class.getClassLoader().getResourceAsStream("assets/viaversion/data/blockIds1.12to1.13.json");
        try {
            final InputStreamReader reader = new InputStreamReader(stream);
            try {
                final Map<String, String[]> map = GsonUtil.getGson().fromJson(reader, new TypeToken<Map<String, String[]>>() {}.getType());
                BlockIdData.blockIdMapping = new HashMap<String, String[]>(map);
                BlockIdData.fallbackReverseMapping = new HashMap<String, String[]>();
                for (final Map.Entry<String, String[]> entry : BlockIdData.blockIdMapping.entrySet()) {
                    for (final String val : entry.getValue()) {
                        String[] previous = BlockIdData.fallbackReverseMapping.get(val);
                        if (previous == null) {
                            previous = BlockIdData.PREVIOUS;
                        }
                        BlockIdData.fallbackReverseMapping.put(val, ObjectArrays.concat(previous, entry.getKey()));
                    }
                }
                reader.close();
            }
            catch (Throwable t) {
                try {
                    reader.close();
                }
                catch (Throwable exception) {
                    t.addSuppressed(exception);
                }
                throw t;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        final InputStream blockS = MappingData.class.getClassLoader().getResourceAsStream("assets/viaversion/data/blockNumberToString1.12.json");
        try {
            final InputStreamReader blockR = new InputStreamReader(blockS);
            try {
                final Map<Integer, String> map2 = GsonUtil.getGson().fromJson(blockR, new TypeToken<Map<Integer, String>>() {}.getType());
                BlockIdData.numberIdToString = new Int2ObjectOpenHashMap<String>(map2);
                blockR.close();
            }
            catch (Throwable t2) {
                try {
                    blockR.close();
                }
                catch (Throwable exception2) {
                    t2.addSuppressed(exception2);
                }
                throw t2;
            }
        }
        catch (IOException e2) {
            e2.printStackTrace();
        }
    }
    
    static {
        PREVIOUS = new String[0];
    }
}
