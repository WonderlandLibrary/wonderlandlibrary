package net.optifine.shaders;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Config;
import net.minecraft.util.ResourceLocation;
import net.optifine.config.ConnectedParser;
import net.optifine.config.MatchBlock;
import net.optifine.reflect.Reflector;
import net.optifine.reflect.ReflectorForge;
import net.optifine.shaders.BlockAlias;
import net.optifine.shaders.IShaderPack;
import net.optifine.shaders.Shaders;
import net.optifine.shaders.config.MacroProcessor;
import net.optifine.util.PropertiesOrdered;
import net.optifine.util.StrUtils;

public class BlockAliases {
    private static BlockAlias[][] blockAliases = null;
    private static PropertiesOrdered blockLayerPropertes = null;
    private static boolean updateOnResourcesReloaded;

    public static int getBlockAliasId(int blockId, int metadata) {
        if (blockAliases == null) {
            return blockId;
        }
        if (blockId >= 0 && blockId < blockAliases.length) {
            BlockAlias[] ablockalias = blockAliases[blockId];
            if (ablockalias == null) {
                return blockId;
            }
            for (int i = 0; i < ablockalias.length; ++i) {
                BlockAlias blockalias = ablockalias[i];
                if (!blockalias.matches(blockId, metadata)) continue;
                return blockalias.getBlockAliasId();
            }
            return blockId;
        }
        return blockId;
    }

    public static void resourcesReloaded() {
        if (updateOnResourcesReloaded) {
            updateOnResourcesReloaded = false;
            BlockAliases.update(Shaders.getShaderPack());
        }
    }

    public static void update(IShaderPack shaderPack) {
        BlockAliases.reset();
        if (shaderPack != null) {
            if (Reflector.Loader_getActiveModList.exists() && Minecraft.getMinecraft().getResourcePackRepository() == null) {
                Config.dbg((String)"[Shaders] Delayed loading of block mappings after resources are loaded");
                updateOnResourcesReloaded = true;
            } else {
                ArrayList list = new ArrayList();
                String s = "/shaders/block.properties";
                InputStream inputstream = shaderPack.getResourceAsStream(s);
                if (inputstream != null) {
                    BlockAliases.loadBlockAliases(inputstream, s, (List<List<BlockAlias>>)list);
                }
                BlockAliases.loadModBlockAliases((List<List<BlockAlias>>)list);
                if (list.size() > 0) {
                    blockAliases = BlockAliases.toArrays((List<List<BlockAlias>>)list);
                }
            }
        }
    }

    private static void loadModBlockAliases(List<List<BlockAlias>> listBlockAliases) {
        String[] astring = ReflectorForge.getForgeModIds();
        for (int i = 0; i < astring.length; ++i) {
            String s = astring[i];
            try {
                ResourceLocation resourcelocation = new ResourceLocation(s, "shaders/block.properties");
                InputStream inputstream = Config.getResourceStream((ResourceLocation)resourcelocation);
                BlockAliases.loadBlockAliases(inputstream, resourcelocation.toString(), listBlockAliases);
                continue;
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }
    }

    private static void loadBlockAliases(InputStream in, String path, List<List<BlockAlias>> listBlockAliases) {
        if (in != null) {
            try {
                in = MacroProcessor.process((InputStream)in, (String)path);
                PropertiesOrdered properties = new PropertiesOrdered();
                properties.load(in);
                in.close();
                Config.dbg((String)("[Shaders] Parsing block mappings: " + path));
                ConnectedParser connectedparser = new ConnectedParser("Shaders");
                for (String s : properties.keySet()) {
                    String s1 = properties.getProperty(s);
                    if (s.startsWith("layer.")) {
                        if (blockLayerPropertes == null) {
                            blockLayerPropertes = new PropertiesOrdered();
                        }
                        blockLayerPropertes.put((Object)s, (Object)s1);
                        continue;
                    }
                    String s2 = "block.";
                    if (!s.startsWith(s2)) {
                        Config.warn((String)("[Shaders] Invalid block ID: " + s));
                        continue;
                    }
                    String s3 = StrUtils.removePrefix((String)s, (String)s2);
                    int i = Config.parseInt((String)s3, (int)-1);
                    if (i < 0) {
                        Config.warn((String)("[Shaders] Invalid block ID: " + s));
                        continue;
                    }
                    MatchBlock[] amatchblock = connectedparser.parseMatchBlocks(s1);
                    if (amatchblock != null && amatchblock.length >= 1) {
                        BlockAlias blockalias = new BlockAlias(i, amatchblock);
                        BlockAliases.addToList(listBlockAliases, blockalias);
                        continue;
                    }
                    Config.warn((String)("[Shaders] Invalid block ID mapping: " + s + "=" + s1));
                }
            }
            catch (IOException var14) {
                Config.warn((String)("[Shaders] Error reading: " + path));
            }
        }
    }

    private static void addToList(List<List<BlockAlias>> blocksAliases, BlockAlias ba) {
        int[] aint = ba.getMatchBlockIds();
        for (int i = 0; i < aint.length; ++i) {
            int j = aint[i];
            while (j >= blocksAliases.size()) {
                blocksAliases.add(null);
            }
            List list = (List)blocksAliases.get(j);
            if (list == null) {
                list = new ArrayList();
                blocksAliases.set(j, (Object)list);
            }
            BlockAlias blockalias = new BlockAlias(ba.getBlockAliasId(), ba.getMatchBlocks(j));
            list.add((Object)blockalias);
        }
    }

    private static BlockAlias[][] toArrays(List<List<BlockAlias>> listBlocksAliases) {
        BlockAlias[][] ablockalias = new BlockAlias[listBlocksAliases.size()][];
        for (int i = 0; i < ablockalias.length; ++i) {
            List list = (List)listBlocksAliases.get(i);
            if (list == null) continue;
            ablockalias[i] = (BlockAlias[])list.toArray((Object[])new BlockAlias[list.size()]);
        }
        return ablockalias;
    }

    public static PropertiesOrdered getBlockLayerPropertes() {
        return blockLayerPropertes;
    }

    public static void reset() {
        blockAliases = null;
        blockLayerPropertes = null;
    }
}
