package net.optifine.shaders;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.src.Config;
import net.minecraft.util.ResourceLocation;
import net.optifine.config.ConnectedParser;
import net.optifine.reflect.Reflector;
import net.optifine.reflect.ReflectorForge;
import net.optifine.shaders.IShaderPack;
import net.optifine.shaders.Shaders;
import net.optifine.shaders.config.MacroProcessor;
import net.optifine.util.PropertiesOrdered;
import net.optifine.util.StrUtils;

public class ItemAliases {
    private static int[] itemAliases = null;
    private static boolean updateOnResourcesReloaded;
    private static final int NO_ALIAS = Integer.MIN_VALUE;

    public static int getItemAliasId(int itemId) {
        if (itemAliases == null) {
            return itemId;
        }
        if (itemId >= 0 && itemId < itemAliases.length) {
            int i = itemAliases[itemId];
            return i == Integer.MIN_VALUE ? itemId : i;
        }
        return itemId;
    }

    public static void resourcesReloaded() {
        if (updateOnResourcesReloaded) {
            updateOnResourcesReloaded = false;
            ItemAliases.update(Shaders.getShaderPack());
        }
    }

    public static void update(IShaderPack shaderPack) {
        ItemAliases.reset();
        if (shaderPack != null) {
            if (Reflector.Loader_getActiveModList.exists() && Config.getResourceManager() == null) {
                Config.dbg((String)"[Shaders] Delayed loading of item mappings after resources are loaded");
                updateOnResourcesReloaded = true;
            } else {
                ArrayList list = new ArrayList();
                String s = "/shaders/item.properties";
                InputStream inputstream = shaderPack.getResourceAsStream(s);
                if (inputstream != null) {
                    ItemAliases.loadItemAliases(inputstream, s, (List<Integer>)list);
                }
                ItemAliases.loadModItemAliases((List<Integer>)list);
                if (list.size() > 0) {
                    itemAliases = ItemAliases.toArray((List<Integer>)list);
                }
            }
        }
    }

    private static void loadModItemAliases(List<Integer> listItemAliases) {
        String[] astring = ReflectorForge.getForgeModIds();
        for (int i = 0; i < astring.length; ++i) {
            String s = astring[i];
            try {
                ResourceLocation resourcelocation = new ResourceLocation(s, "shaders/item.properties");
                InputStream inputstream = Config.getResourceStream((ResourceLocation)resourcelocation);
                ItemAliases.loadItemAliases(inputstream, resourcelocation.toString(), listItemAliases);
                continue;
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }
    }

    private static void loadItemAliases(InputStream in, String path, List<Integer> listItemAliases) {
        if (in != null) {
            try {
                in = MacroProcessor.process((InputStream)in, (String)path);
                PropertiesOrdered properties = new PropertiesOrdered();
                properties.load(in);
                in.close();
                Config.dbg((String)("[Shaders] Parsing item mappings: " + path));
                ConnectedParser connectedparser = new ConnectedParser("Shaders");
                for (String s : properties.keySet()) {
                    String s1 = properties.getProperty(s);
                    String s2 = "item.";
                    if (!s.startsWith(s2)) {
                        Config.warn((String)("[Shaders] Invalid item ID: " + s));
                        continue;
                    }
                    String s3 = StrUtils.removePrefix((String)s, (String)s2);
                    int i = Config.parseInt((String)s3, (int)-1);
                    if (i < 0) {
                        Config.warn((String)("[Shaders] Invalid item alias ID: " + i));
                        continue;
                    }
                    int[] aint = connectedparser.parseItems(s1);
                    if (aint != null && aint.length >= 1) {
                        for (int j = 0; j < aint.length; ++j) {
                            int k = aint[j];
                            ItemAliases.addToList(listItemAliases, k, i);
                        }
                        continue;
                    }
                    Config.warn((String)("[Shaders] Invalid item ID mapping: " + s + "=" + s1));
                }
            }
            catch (IOException var15) {
                Config.warn((String)("[Shaders] Error reading: " + path));
            }
        }
    }

    private static void addToList(List<Integer> list, int index, int val) {
        while (list.size() <= index) {
            list.add((Object)Integer.MIN_VALUE);
        }
        list.set(index, (Object)val);
    }

    private static int[] toArray(List<Integer> list) {
        int[] aint = new int[list.size()];
        for (int i = 0; i < aint.length; ++i) {
            aint[i] = (Integer)list.get(i);
        }
        return aint;
    }

    public static void reset() {
        itemAliases = null;
    }
}
