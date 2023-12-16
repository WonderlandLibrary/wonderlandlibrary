// 
// Decompiled by Procyon v0.6.0
// 

package net.optifine.shaders;

import java.util.Hashtable;
import net.optifine.config.MatchBlock;
import java.util.Iterator;
import java.util.Properties;
import net.optifine.util.StrUtils;
import net.optifine.config.ConnectedParser;
import net.optifine.shaders.config.MacroProcessor;
import java.io.IOException;
import net.minecraft.util.ResourceLocation;
import net.optifine.reflect.ReflectorForge;
import java.io.InputStream;
import java.util.List;
import java.util.ArrayList;
import net.minecraft.src.Config;
import net.minecraft.client.Minecraft;
import net.optifine.reflect.Reflector;
import net.optifine.util.PropertiesOrdered;

public class BlockAliases
{
    private static BlockAlias[][] blockAliases;
    private static PropertiesOrdered blockLayerPropertes;
    private static boolean updateOnResourcesReloaded;
    
    static {
        BlockAliases.blockAliases = null;
        BlockAliases.blockLayerPropertes = null;
    }
    
    public static int getBlockAliasId(final int blockId, final int metadata) {
        if (BlockAliases.blockAliases == null) {
            return blockId;
        }
        if (blockId < 0 || blockId >= BlockAliases.blockAliases.length) {
            return blockId;
        }
        final BlockAlias[] ablockalias = BlockAliases.blockAliases[blockId];
        if (ablockalias == null) {
            return blockId;
        }
        for (int i = 0; i < ablockalias.length; ++i) {
            final BlockAlias blockalias = ablockalias[i];
            if (blockalias.matches(blockId, metadata)) {
                return blockalias.getBlockAliasId();
            }
        }
        return blockId;
    }
    
    public static void resourcesReloaded() {
        if (BlockAliases.updateOnResourcesReloaded) {
            BlockAliases.updateOnResourcesReloaded = false;
            update(Shaders.getShaderPack());
        }
    }
    
    public static void update(final IShaderPack shaderPack) {
        reset();
        if (shaderPack != null) {
            if (Reflector.Loader_getActiveModList.exists() && Minecraft.getMinecraft().getResourcePackRepository() == null) {
                Config.dbg("[Shaders] Delayed loading of block mappings after resources are loaded");
                BlockAliases.updateOnResourcesReloaded = true;
            }
            else {
                final List<List<BlockAlias>> list = new ArrayList<List<BlockAlias>>();
                final String s = "/shaders/block.properties";
                final InputStream inputstream = shaderPack.getResourceAsStream(s);
                if (inputstream != null) {
                    loadBlockAliases(inputstream, s, list);
                }
                loadModBlockAliases(list);
                if (list.size() > 0) {
                    BlockAliases.blockAliases = toArrays(list);
                }
            }
        }
    }
    
    private static void loadModBlockAliases(final List<List<BlockAlias>> listBlockAliases) {
        final String[] astring = ReflectorForge.getForgeModIds();
        for (int i = 0; i < astring.length; ++i) {
            final String s = astring[i];
            try {
                final ResourceLocation resourcelocation = new ResourceLocation(s, "shaders/block.properties");
                final InputStream inputstream = Config.getResourceStream(resourcelocation);
                loadBlockAliases(inputstream, resourcelocation.toString(), listBlockAliases);
            }
            catch (final IOException ex) {}
        }
    }
    
    private static void loadBlockAliases(InputStream in, final String path, final List<List<BlockAlias>> listBlockAliases) {
        if (in != null) {
            try {
                in = MacroProcessor.process(in, path);
                final Properties properties = new PropertiesOrdered();
                properties.load(in);
                in.close();
                Config.dbg("[Shaders] Parsing block mappings: " + path);
                final ConnectedParser connectedparser = new ConnectedParser("Shaders");
                for (final Object o : ((Hashtable<Object, V>)properties).keySet()) {
                    final String s = (String)o;
                    final String s2 = properties.getProperty(s);
                    if (s.startsWith("layer.")) {
                        if (BlockAliases.blockLayerPropertes == null) {
                            BlockAliases.blockLayerPropertes = new PropertiesOrdered();
                        }
                        BlockAliases.blockLayerPropertes.put(s, s2);
                    }
                    else {
                        final String s3 = "block.";
                        if (!s.startsWith(s3)) {
                            Config.warn("[Shaders] Invalid block ID: " + s);
                        }
                        else {
                            final String s4 = StrUtils.removePrefix(s, s3);
                            final int i = Config.parseInt(s4, -1);
                            if (i < 0) {
                                Config.warn("[Shaders] Invalid block ID: " + s);
                            }
                            else {
                                final MatchBlock[] amatchblock = connectedparser.parseMatchBlocks(s2);
                                if (amatchblock != null && amatchblock.length >= 1) {
                                    final BlockAlias blockalias = new BlockAlias(i, amatchblock);
                                    addToList(listBlockAliases, blockalias);
                                }
                                else {
                                    Config.warn("[Shaders] Invalid block ID mapping: " + s + "=" + s2);
                                }
                            }
                        }
                    }
                }
            }
            catch (final IOException var14) {
                Config.warn("[Shaders] Error reading: " + path);
            }
        }
    }
    
    private static void addToList(final List<List<BlockAlias>> blocksAliases, final BlockAlias ba) {
        final int[] aint = ba.getMatchBlockIds();
        for (int i = 0; i < aint.length; ++i) {
            final int j = aint[i];
            while (j >= blocksAliases.size()) {
                blocksAliases.add(null);
            }
            List<BlockAlias> list = blocksAliases.get(j);
            if (list == null) {
                list = new ArrayList<BlockAlias>();
                blocksAliases.set(j, list);
            }
            final BlockAlias blockalias = new BlockAlias(ba.getBlockAliasId(), ba.getMatchBlocks(j));
            list.add(blockalias);
        }
    }
    
    private static BlockAlias[][] toArrays(final List<List<BlockAlias>> listBlocksAliases) {
        final BlockAlias[][] ablockalias = new BlockAlias[listBlocksAliases.size()][];
        for (int i = 0; i < ablockalias.length; ++i) {
            final List<BlockAlias> list = listBlocksAliases.get(i);
            if (list != null) {
                ablockalias[i] = list.toArray(new BlockAlias[list.size()]);
            }
        }
        return ablockalias;
    }
    
    public static PropertiesOrdered getBlockLayerPropertes() {
        return BlockAliases.blockLayerPropertes;
    }
    
    public static void reset() {
        BlockAliases.blockAliases = null;
        BlockAliases.blockLayerPropertes = null;
    }
}
