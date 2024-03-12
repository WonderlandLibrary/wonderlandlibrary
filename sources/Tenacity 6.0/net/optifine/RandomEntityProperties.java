// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine;

import net.minecraft.src.Config;
import java.util.List;
import java.util.ArrayList;
import java.util.Properties;
import net.optifine.config.ConnectedParser;
import net.minecraft.util.ResourceLocation;

public class RandomEntityProperties
{
    public String name;
    public String basePath;
    public ResourceLocation[] resourceLocations;
    public RandomEntityRule[] rules;
    
    public RandomEntityProperties(final String path, final ResourceLocation[] variants) {
        this.name = null;
        this.basePath = null;
        this.resourceLocations = null;
        this.rules = null;
        final ConnectedParser connectedparser = new ConnectedParser("RandomEntities");
        this.name = connectedparser.parseName(path);
        this.basePath = connectedparser.parseBasePath(path);
        this.resourceLocations = variants;
    }
    
    public RandomEntityProperties(final Properties props, final String path, final ResourceLocation baseResLoc) {
        this.name = null;
        this.basePath = null;
        this.resourceLocations = null;
        this.rules = null;
        final ConnectedParser connectedparser = new ConnectedParser("RandomEntities");
        this.name = connectedparser.parseName(path);
        this.basePath = connectedparser.parseBasePath(path);
        this.rules = this.parseRules(props, path, baseResLoc, connectedparser);
    }
    
    public ResourceLocation getTextureLocation(final ResourceLocation loc, final IRandomEntity randomEntity) {
        if (this.rules != null) {
            for (int i = 0; i < this.rules.length; ++i) {
                final RandomEntityRule randomentityrule = this.rules[i];
                if (randomentityrule.matches(randomEntity)) {
                    return randomentityrule.getTextureLocation(loc, randomEntity.getId());
                }
            }
        }
        if (this.resourceLocations != null) {
            final int j = randomEntity.getId();
            final int k = j % this.resourceLocations.length;
            return this.resourceLocations[k];
        }
        return loc;
    }
    
    private RandomEntityRule[] parseRules(final Properties props, final String pathProps, final ResourceLocation baseResLoc, final ConnectedParser cp) {
        final List list = new ArrayList();
        for (int i = props.size(), j = 0; j < i; ++j) {
            final int k = j + 1;
            String s = props.getProperty("textures." + k);
            if (s == null) {
                s = props.getProperty("skins." + k);
            }
            if (s != null) {
                final RandomEntityRule randomentityrule = new RandomEntityRule(props, pathProps, baseResLoc, k, s, cp);
                if (randomentityrule.isValid(pathProps)) {
                    list.add(randomentityrule);
                }
            }
        }
        final RandomEntityRule[] arandomentityrule = list.toArray(new RandomEntityRule[list.size()]);
        return arandomentityrule;
    }
    
    public boolean isValid(final String path) {
        if (this.resourceLocations == null && this.rules == null) {
            Config.warn("No skins specified: " + path);
            return false;
        }
        if (this.rules != null) {
            for (int i = 0; i < this.rules.length; ++i) {
                final RandomEntityRule randomentityrule = this.rules[i];
                if (!randomentityrule.isValid(path)) {
                    return false;
                }
            }
        }
        if (this.resourceLocations != null) {
            for (int j = 0; j < this.resourceLocations.length; ++j) {
                final ResourceLocation resourcelocation = this.resourceLocations[j];
                if (!Config.hasResource(resourcelocation)) {
                    Config.warn("Texture not found: " + resourcelocation.getResourcePath());
                    return false;
                }
            }
        }
        return true;
    }
    
    public boolean isDefault() {
        return this.rules == null && this.resourceLocations == null;
    }
}
