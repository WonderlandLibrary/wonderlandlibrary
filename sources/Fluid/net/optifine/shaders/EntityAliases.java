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

public class EntityAliases
{
    private static int[] entityAliases;
    private static boolean updateOnResourcesReloaded;
    
    static {
        EntityAliases.entityAliases = null;
    }
    
    public static int getEntityAliasId(final int entityId) {
        if (EntityAliases.entityAliases == null) {
            return -1;
        }
        if (entityId >= 0 && entityId < EntityAliases.entityAliases.length) {
            final int i = EntityAliases.entityAliases[entityId];
            return i;
        }
        return -1;
    }
    
    public static void resourcesReloaded() {
        if (EntityAliases.updateOnResourcesReloaded) {
            EntityAliases.updateOnResourcesReloaded = false;
            update(Shaders.getShaderPack());
        }
    }
    
    public static void update(final IShaderPack shaderPack) {
        reset();
        if (shaderPack != null) {
            if (Reflector.Loader_getActiveModList.exists() && Config.getResourceManager() == null) {
                Config.dbg("[Shaders] Delayed loading of entity mappings after resources are loaded");
                EntityAliases.updateOnResourcesReloaded = true;
            }
            else {
                final List<Integer> list = new ArrayList<Integer>();
                final String s = "/shaders/entity.properties";
                final InputStream inputstream = shaderPack.getResourceAsStream(s);
                if (inputstream != null) {
                    loadEntityAliases(inputstream, s, list);
                }
                loadModEntityAliases(list);
                if (list.size() > 0) {
                    EntityAliases.entityAliases = toArray(list);
                }
            }
        }
    }
    
    private static void loadModEntityAliases(final List<Integer> listEntityAliases) {
        final String[] astring = ReflectorForge.getForgeModIds();
        for (int i = 0; i < astring.length; ++i) {
            final String s = astring[i];
            try {
                final ResourceLocation resourcelocation = new ResourceLocation(s, "shaders/entity.properties");
                final InputStream inputstream = Config.getResourceStream(resourcelocation);
                loadEntityAliases(inputstream, resourcelocation.toString(), listEntityAliases);
            }
            catch (final IOException ex) {}
        }
    }
    
    private static void loadEntityAliases(InputStream in, final String path, final List<Integer> listEntityAliases) {
        if (in != null) {
            try {
                in = MacroProcessor.process(in, path);
                final Properties properties = new PropertiesOrdered();
                properties.load(in);
                in.close();
                Config.dbg("[Shaders] Parsing entity mappings: " + path);
                final ConnectedParser connectedparser = new ConnectedParser("Shaders");
                for (final Object o : ((Hashtable<Object, V>)properties).keySet()) {
                    final String s = (String)o;
                    final String s2 = properties.getProperty(s);
                    final String s3 = "entity.";
                    if (!s.startsWith(s3)) {
                        Config.warn("[Shaders] Invalid entity ID: " + s);
                    }
                    else {
                        final String s4 = StrUtils.removePrefix(s, s3);
                        final int i = Config.parseInt(s4, -1);
                        if (i < 0) {
                            Config.warn("[Shaders] Invalid entity alias ID: " + i);
                        }
                        else {
                            final int[] aint = connectedparser.parseEntities(s2);
                            if (aint != null && aint.length >= 1) {
                                for (int j = 0; j < aint.length; ++j) {
                                    final int k = aint[j];
                                    addToList(listEntityAliases, k, i);
                                }
                            }
                            else {
                                Config.warn("[Shaders] Invalid entity ID mapping: " + s + "=" + s2);
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
            list.add(-1);
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
        EntityAliases.entityAliases = null;
    }
}
