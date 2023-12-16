// 
// Decompiled by Procyon v0.6.0
// 

package net.optifine.shaders;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;
import net.optifine.util.StrUtils;
import net.optifine.config.ConnectedParser;
import net.optifine.util.PropertiesOrdered;
import net.optifine.shaders.config.MacroProcessor;
import java.io.IOException;
import net.minecraft.util.ResourceLocation;
import net.optifine.reflect.ReflectorForge;
import java.io.InputStream;
import java.util.List;
import java.util.ArrayList;
import net.minecraft.src.Config;
import net.optifine.reflect.Reflector;

public class ItemAliases
{
    private static int[] itemAliases;
    private static boolean updateOnResourcesReloaded;
    private static final int NO_ALIAS = Integer.MIN_VALUE;
    
    static {
        ItemAliases.itemAliases = null;
    }
    
    public static int getItemAliasId(final int itemId) {
        if (ItemAliases.itemAliases == null) {
            return itemId;
        }
        if (itemId >= 0 && itemId < ItemAliases.itemAliases.length) {
            final int i = ItemAliases.itemAliases[itemId];
            return (i == Integer.MIN_VALUE) ? itemId : i;
        }
        return itemId;
    }
    
    public static void resourcesReloaded() {
        if (ItemAliases.updateOnResourcesReloaded) {
            ItemAliases.updateOnResourcesReloaded = false;
            update(Shaders.getShaderPack());
        }
    }
    
    public static void update(final IShaderPack shaderPack) {
        reset();
        if (shaderPack != null) {
            if (Reflector.Loader_getActiveModList.exists() && Config.getResourceManager() == null) {
                Config.dbg("[Shaders] Delayed loading of item mappings after resources are loaded");
                ItemAliases.updateOnResourcesReloaded = true;
            }
            else {
                final List<Integer> list = new ArrayList<Integer>();
                final String s = "/shaders/item.properties";
                final InputStream inputstream = shaderPack.getResourceAsStream(s);
                if (inputstream != null) {
                    loadItemAliases(inputstream, s, list);
                }
                loadModItemAliases(list);
                if (list.size() > 0) {
                    ItemAliases.itemAliases = toArray(list);
                }
            }
        }
    }
    
    private static void loadModItemAliases(final List<Integer> listItemAliases) {
        final String[] astring = ReflectorForge.getForgeModIds();
        for (int i = 0; i < astring.length; ++i) {
            final String s = astring[i];
            try {
                final ResourceLocation resourcelocation = new ResourceLocation(s, "shaders/item.properties");
                final InputStream inputstream = Config.getResourceStream(resourcelocation);
                loadItemAliases(inputstream, resourcelocation.toString(), listItemAliases);
            }
            catch (final IOException ex) {}
        }
    }
    
    private static void loadItemAliases(InputStream in, final String path, final List<Integer> listItemAliases) {
        if (in != null) {
            try {
                in = MacroProcessor.process(in, path);
                final Properties properties = new PropertiesOrdered();
                properties.load(in);
                in.close();
                Config.dbg("[Shaders] Parsing item mappings: " + path);
                final ConnectedParser connectedparser = new ConnectedParser("Shaders");
                for (final Object o : ((Hashtable<Object, V>)properties).keySet()) {
                    final String s = (String)o;
                    final String s2 = properties.getProperty(s);
                    final String s3 = "item.";
                    if (!s.startsWith(s3)) {
                        Config.warn("[Shaders] Invalid item ID: " + s);
                    }
                    else {
                        final String s4 = StrUtils.removePrefix(s, s3);
                        final int i = Config.parseInt(s4, -1);
                        if (i < 0) {
                            Config.warn("[Shaders] Invalid item alias ID: " + i);
                        }
                        else {
                            final int[] aint = connectedparser.parseItems(s2);
                            if (aint != null && aint.length >= 1) {
                                for (int j = 0; j < aint.length; ++j) {
                                    final int k = aint[j];
                                    addToList(listItemAliases, k, i);
                                }
                            }
                            else {
                                Config.warn("[Shaders] Invalid item ID mapping: " + s + "=" + s2);
                            }
                        }
                    }
                }
            }
            catch (final IOException var15) {
                Config.warn("[Shaders] Error reading: " + path);
            }
        }
    }
    
    private static void addToList(final List<Integer> list, final int index, final int val) {
        while (list.size() <= index) {
            list.add(Integer.MIN_VALUE);
        }
        list.set(index, val);
    }
    
    private static int[] toArray(final List<Integer> list) {
        final int[] aint = new int[list.size()];
        for (int i = 0; i < aint.length; ++i) {
            aint[i] = list.get(i);
        }
        return aint;
    }
    
    public static void reset() {
        ItemAliases.itemAliases = null;
    }
}
