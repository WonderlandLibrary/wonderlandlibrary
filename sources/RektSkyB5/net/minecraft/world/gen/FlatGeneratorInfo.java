/*
 * Decompiled with CFR 0.152.
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
    private final List<FlatLayerInfo> flatLayers = Lists.newArrayList();
    private final Map<String, Map<String, String>> worldFeatures = Maps.newHashMap();
    private int biomeToUse;

    public int getBiome() {
        return this.biomeToUse;
    }

    public void setBiome(int p_82647_1_) {
        this.biomeToUse = p_82647_1_;
    }

    public Map<String, Map<String, String>> getWorldFeatures() {
        return this.worldFeatures;
    }

    public List<FlatLayerInfo> getFlatLayers() {
        return this.flatLayers;
    }

    public void func_82645_d() {
        int i2 = 0;
        for (FlatLayerInfo flatlayerinfo : this.flatLayers) {
            flatlayerinfo.setMinY(i2);
            i2 += flatlayerinfo.getLayerCount();
        }
    }

    public String toString() {
        StringBuilder stringbuilder = new StringBuilder();
        stringbuilder.append(3);
        stringbuilder.append(";");
        for (int i2 = 0; i2 < this.flatLayers.size(); ++i2) {
            if (i2 > 0) {
                stringbuilder.append(",");
            }
            stringbuilder.append(this.flatLayers.get(i2).toString());
        }
        stringbuilder.append(";");
        stringbuilder.append(this.biomeToUse);
        if (!this.worldFeatures.isEmpty()) {
            stringbuilder.append(";");
            int k2 = 0;
            for (Map.Entry<String, Map<String, String>> entry : this.worldFeatures.entrySet()) {
                if (k2++ > 0) {
                    stringbuilder.append(",");
                }
                stringbuilder.append(entry.getKey().toLowerCase());
                Map<String, String> map = entry.getValue();
                if (map.isEmpty()) continue;
                stringbuilder.append("(");
                int j2 = 0;
                for (Map.Entry<String, String> entry1 : map.entrySet()) {
                    if (j2++ > 0) {
                        stringbuilder.append(" ");
                    }
                    stringbuilder.append(entry1.getKey());
                    stringbuilder.append("=");
                    stringbuilder.append(entry1.getValue());
                }
                stringbuilder.append(")");
            }
        } else {
            stringbuilder.append(";");
        }
        return stringbuilder.toString();
    }

    private static FlatLayerInfo func_180715_a(int p_180715_0_, String p_180715_1_, int p_180715_2_) {
        String[] astring = p_180715_0_ >= 3 ? p_180715_1_.split("\\*", 2) : p_180715_1_.split("x", 2);
        int i2 = 1;
        int j2 = 0;
        if (astring.length == 2) {
            try {
                i2 = Integer.parseInt(astring[0]);
                if (p_180715_2_ + i2 >= 256) {
                    i2 = 256 - p_180715_2_;
                }
                if (i2 < 0) {
                    i2 = 0;
                }
            }
            catch (Throwable var8) {
                return null;
            }
        }
        Block block = null;
        try {
            String s2 = astring[astring.length - 1];
            if (p_180715_0_ < 3) {
                astring = s2.split(":", 2);
                if (astring.length > 1) {
                    j2 = Integer.parseInt(astring[1]);
                }
                block = Block.getBlockById(Integer.parseInt(astring[0]));
            } else {
                astring = s2.split(":", 3);
                Block block2 = block = astring.length > 1 ? Block.getBlockFromName(astring[0] + ":" + astring[1]) : null;
                if (block != null) {
                    j2 = astring.length > 2 ? Integer.parseInt(astring[2]) : 0;
                } else {
                    block = Block.getBlockFromName(astring[0]);
                    if (block != null) {
                        int n2 = j2 = astring.length > 1 ? Integer.parseInt(astring[1]) : 0;
                    }
                }
                if (block == null) {
                    return null;
                }
            }
            if (block == Blocks.air) {
                j2 = 0;
            }
            if (j2 < 0 || j2 > 15) {
                j2 = 0;
            }
        }
        catch (Throwable var9) {
            return null;
        }
        FlatLayerInfo flatlayerinfo = new FlatLayerInfo(p_180715_0_, i2, block, j2);
        flatlayerinfo.setMinY(p_180715_2_);
        return flatlayerinfo;
    }

    private static List<FlatLayerInfo> func_180716_a(int p_180716_0_, String p_180716_1_) {
        if (p_180716_1_ != null && p_180716_1_.length() >= 1) {
            ArrayList<FlatLayerInfo> list = Lists.newArrayList();
            String[] astring = p_180716_1_.split(",");
            int i2 = 0;
            for (String s2 : astring) {
                FlatLayerInfo flatlayerinfo = FlatGeneratorInfo.func_180715_a(p_180716_0_, s2, i2);
                if (flatlayerinfo == null) {
                    return null;
                }
                list.add(flatlayerinfo);
                i2 += flatlayerinfo.getLayerCount();
            }
            return list;
        }
        return null;
    }

    public static FlatGeneratorInfo createFlatGeneratorFromString(String p_82651_0_) {
        int i2;
        if (p_82651_0_ == null) {
            return FlatGeneratorInfo.getDefaultFlatGenerator();
        }
        String[] astring = p_82651_0_.split(";", -1);
        int n2 = i2 = astring.length == 1 ? 0 : MathHelper.parseIntWithDefault(astring[0], 0);
        if (i2 >= 0 && i2 <= 3) {
            List<FlatLayerInfo> list;
            FlatGeneratorInfo flatgeneratorinfo = new FlatGeneratorInfo();
            int j2 = astring.length == 1 ? 0 : 1;
            if ((list = FlatGeneratorInfo.func_180716_a(i2, astring[j2++])) != null && !list.isEmpty()) {
                flatgeneratorinfo.getFlatLayers().addAll(list);
                flatgeneratorinfo.func_82645_d();
                int k2 = BiomeGenBase.plains.biomeID;
                if (i2 > 0 && astring.length > j2) {
                    k2 = MathHelper.parseIntWithDefault(astring[j2++], k2);
                }
                flatgeneratorinfo.setBiome(k2);
                if (i2 > 0 && astring.length > j2) {
                    String[] astring1;
                    for (String s2 : astring1 = astring[j2++].toLowerCase().split(",")) {
                        String[] astring2 = s2.split("\\(", 2);
                        HashMap<String, String> map = Maps.newHashMap();
                        if (astring2[0].length() <= 0) continue;
                        flatgeneratorinfo.getWorldFeatures().put(astring2[0], map);
                        if (astring2.length <= 1 || !astring2[1].endsWith(")") || astring2[1].length() <= 1) continue;
                        String[] astring3 = astring2[1].substring(0, astring2[1].length() - 1).split(" ");
                        for (int l2 = 0; l2 < astring3.length; ++l2) {
                            String[] astring4 = astring3[l2].split("=", 2);
                            if (astring4.length != 2) continue;
                            map.put(astring4[0], astring4[1]);
                        }
                    }
                } else {
                    flatgeneratorinfo.getWorldFeatures().put("village", Maps.newHashMap());
                }
                return flatgeneratorinfo;
            }
            return FlatGeneratorInfo.getDefaultFlatGenerator();
        }
        return FlatGeneratorInfo.getDefaultFlatGenerator();
    }

    public static FlatGeneratorInfo getDefaultFlatGenerator() {
        FlatGeneratorInfo flatgeneratorinfo = new FlatGeneratorInfo();
        flatgeneratorinfo.setBiome(BiomeGenBase.plains.biomeID);
        flatgeneratorinfo.getFlatLayers().add(new FlatLayerInfo(1, Blocks.bedrock));
        flatgeneratorinfo.getFlatLayers().add(new FlatLayerInfo(2, Blocks.dirt));
        flatgeneratorinfo.getFlatLayers().add(new FlatLayerInfo(1, Blocks.grass));
        flatgeneratorinfo.func_82645_d();
        flatgeneratorinfo.getWorldFeatures().put("village", Maps.newHashMap());
        return flatgeneratorinfo;
    }
}

