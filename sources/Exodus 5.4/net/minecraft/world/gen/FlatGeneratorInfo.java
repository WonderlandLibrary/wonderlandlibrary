/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 */
package net.minecraft.world.gen;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.FlatLayerInfo;

public class FlatGeneratorInfo {
    private int biomeToUse;
    private final List<FlatLayerInfo> flatLayers = Lists.newArrayList();
    private final Map<String, Map<String, String>> worldFeatures = Maps.newHashMap();

    public void func_82645_d() {
        int n = 0;
        for (FlatLayerInfo flatLayerInfo : this.flatLayers) {
            flatLayerInfo.setMinY(n);
            n += flatLayerInfo.getLayerCount();
        }
    }

    public Map<String, Map<String, String>> getWorldFeatures() {
        return this.worldFeatures;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(3);
        stringBuilder.append(";");
        int n = 0;
        while (n < this.flatLayers.size()) {
            if (n > 0) {
                stringBuilder.append(",");
            }
            stringBuilder.append(this.flatLayers.get(n).toString());
            ++n;
        }
        stringBuilder.append(";");
        stringBuilder.append(this.biomeToUse);
        if (!this.worldFeatures.isEmpty()) {
            stringBuilder.append(";");
            n = 0;
            for (Map.Entry<String, Map<String, String>> entry : this.worldFeatures.entrySet()) {
                if (n++ > 0) {
                    stringBuilder.append(",");
                }
                stringBuilder.append(entry.getKey().toLowerCase());
                Map<String, String> map = entry.getValue();
                if (map.isEmpty()) continue;
                stringBuilder.append("(");
                int n2 = 0;
                for (Map.Entry<String, String> entry2 : map.entrySet()) {
                    if (n2++ > 0) {
                        stringBuilder.append(" ");
                    }
                    stringBuilder.append(entry2.getKey());
                    stringBuilder.append("=");
                    stringBuilder.append(entry2.getValue());
                }
                stringBuilder.append(")");
            }
        } else {
            stringBuilder.append(";");
        }
        return stringBuilder.toString();
    }

    private static FlatLayerInfo func_180715_a(int n, String string, int n2) {
        Object object;
        Block block;
        int n3;
        int n4;
        block15: {
            String[] stringArray = n >= 3 ? string.split("\\*", 2) : string.split("x", 2);
            n4 = 1;
            n3 = 0;
            if (stringArray.length == 2) {
                try {
                    n4 = Integer.parseInt(stringArray[0]);
                    if (n2 + n4 >= 256) {
                        n4 = 256 - n2;
                    }
                    if (n4 < 0) {
                        n4 = 0;
                    }
                }
                catch (Throwable throwable) {
                    return null;
                }
            }
            block = null;
            try {
                object = stringArray[stringArray.length - 1];
                if (n < 3) {
                    stringArray = ((String)object).split(":", 2);
                    if (stringArray.length > 1) {
                        n3 = Integer.parseInt(stringArray[1]);
                    }
                    block = Block.getBlockById(Integer.parseInt(stringArray[0]));
                    break block15;
                }
                stringArray = ((String)object).split(":", 3);
                Block block2 = block = stringArray.length > 1 ? Block.getBlockFromName(String.valueOf(stringArray[0]) + ":" + stringArray[1]) : null;
                if (block != null) {
                    n3 = stringArray.length > 2 ? Integer.parseInt(stringArray[2]) : 0;
                } else {
                    block = Block.getBlockFromName(stringArray[0]);
                    if (block != null) {
                        int n5 = n3 = stringArray.length > 1 ? Integer.parseInt(stringArray[1]) : 0;
                    }
                }
                if (block != null) break block15;
                return null;
            }
            catch (Throwable throwable) {
                return null;
            }
        }
        if (block == Blocks.air) {
            n3 = 0;
        }
        if (n3 < 0 || n3 > 15) {
            n3 = 0;
        }
        object = new FlatLayerInfo(n, n4, block, n3);
        ((FlatLayerInfo)object).setMinY(n2);
        return object;
    }

    public static FlatGeneratorInfo getDefaultFlatGenerator() {
        FlatGeneratorInfo flatGeneratorInfo = new FlatGeneratorInfo();
        flatGeneratorInfo.setBiome(BiomeGenBase.plains.biomeID);
        flatGeneratorInfo.getFlatLayers().add(new FlatLayerInfo(1, Blocks.bedrock));
        flatGeneratorInfo.getFlatLayers().add(new FlatLayerInfo(2, Blocks.dirt));
        flatGeneratorInfo.getFlatLayers().add(new FlatLayerInfo(1, Blocks.grass));
        flatGeneratorInfo.func_82645_d();
        flatGeneratorInfo.getWorldFeatures().put("village", Maps.newHashMap());
        return flatGeneratorInfo;
    }

    public int getBiome() {
        return this.biomeToUse;
    }

    public static FlatGeneratorInfo createFlatGeneratorFromString(String string) {
        int n;
        if (string == null) {
            return FlatGeneratorInfo.getDefaultFlatGenerator();
        }
        String[] stringArray = string.split(";", -1);
        int n2 = n = stringArray.length == 1 ? 0 : MathHelper.parseIntWithDefault(stringArray[0], 0);
        if (n >= 0 && n <= 3) {
            List<FlatLayerInfo> list;
            FlatGeneratorInfo flatGeneratorInfo = new FlatGeneratorInfo();
            int n3 = stringArray.length == 1 ? 0 : 1;
            if ((list = FlatGeneratorInfo.func_180716_a(n, stringArray[n3++])) != null && !list.isEmpty()) {
                flatGeneratorInfo.getFlatLayers().addAll(list);
                flatGeneratorInfo.func_82645_d();
                int n4 = BiomeGenBase.plains.biomeID;
                if (n > 0 && stringArray.length > n3) {
                    n4 = MathHelper.parseIntWithDefault(stringArray[n3++], n4);
                }
                flatGeneratorInfo.setBiome(n4);
                if (n > 0 && stringArray.length > n3) {
                    String[] stringArray2;
                    String[] stringArray3 = stringArray2 = stringArray[n3++].toLowerCase().split(",");
                    int n5 = stringArray2.length;
                    int n6 = 0;
                    while (n6 < n5) {
                        String string2 = stringArray3[n6];
                        String[] stringArray4 = string2.split("\\(", 2);
                        HashMap hashMap = Maps.newHashMap();
                        if (stringArray4[0].length() > 0) {
                            flatGeneratorInfo.getWorldFeatures().put(stringArray4[0], hashMap);
                            if (stringArray4.length > 1 && stringArray4[1].endsWith(")") && stringArray4[1].length() > 1) {
                                String[] stringArray5 = stringArray4[1].substring(0, stringArray4[1].length() - 1).split(" ");
                                int n7 = 0;
                                while (n7 < stringArray5.length) {
                                    String[] stringArray6 = stringArray5[n7].split("=", 2);
                                    if (stringArray6.length == 2) {
                                        hashMap.put(stringArray6[0], stringArray6[1]);
                                    }
                                    ++n7;
                                }
                            }
                        }
                        ++n6;
                    }
                } else {
                    flatGeneratorInfo.getWorldFeatures().put("village", Maps.newHashMap());
                }
                return flatGeneratorInfo;
            }
            return FlatGeneratorInfo.getDefaultFlatGenerator();
        }
        return FlatGeneratorInfo.getDefaultFlatGenerator();
    }

    private static List<FlatLayerInfo> func_180716_a(int n, String string) {
        if (string != null && string.length() >= 1) {
            ArrayList arrayList = Lists.newArrayList();
            String[] stringArray = string.split(",");
            int n2 = 0;
            String[] stringArray2 = stringArray;
            int n3 = stringArray.length;
            int n4 = 0;
            while (n4 < n3) {
                String string2 = stringArray2[n4];
                FlatLayerInfo flatLayerInfo = FlatGeneratorInfo.func_180715_a(n, string2, n2);
                if (flatLayerInfo == null) {
                    return null;
                }
                arrayList.add(flatLayerInfo);
                n2 += flatLayerInfo.getLayerCount();
                ++n4;
            }
            return arrayList;
        }
        return null;
    }

    public void setBiome(int n) {
        this.biomeToUse = n;
    }

    public List<FlatLayerInfo> getFlatLayers() {
        return this.flatLayers;
    }
}

