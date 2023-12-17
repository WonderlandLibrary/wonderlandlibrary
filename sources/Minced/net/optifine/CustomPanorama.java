// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine;

import net.optifine.util.MathUtils;
import java.io.InputStream;
import java.io.IOException;
import java.util.List;
import net.minecraft.src.Config;
import net.minecraft.util.ResourceLocation;
import java.util.ArrayList;
import java.util.Properties;
import net.optifine.util.PropertiesOrdered;
import java.util.Random;

public class CustomPanorama
{
    private static CustomPanoramaProperties customPanoramaProperties;
    private static final Random random;
    
    public static CustomPanoramaProperties getCustomPanoramaProperties() {
        return CustomPanorama.customPanoramaProperties;
    }
    
    public static void update() {
        CustomPanorama.customPanoramaProperties = null;
        final String[] astring = getPanoramaFolders();
        if (astring.length > 1) {
            final Properties[] aproperties = getPanoramaProperties(astring);
            final int[] aint = getWeights(aproperties);
            final int i = getRandomIndex(aint);
            final String s = astring[i];
            Properties properties = aproperties[i];
            if (properties == null) {
                properties = aproperties[0];
            }
            if (properties == null) {
                properties = new PropertiesOrdered();
            }
            final CustomPanoramaProperties custompanoramaproperties = CustomPanorama.customPanoramaProperties = new CustomPanoramaProperties(s, properties);
        }
    }
    
    private static String[] getPanoramaFolders() {
        final List<String> list = new ArrayList<String>();
        list.add("textures/gui/title/background");
        for (int i = 0; i < 100; ++i) {
            final String s = "optifine/gui/background" + i;
            final String s2 = s + "/panorama_0.png";
            final ResourceLocation resourcelocation = new ResourceLocation(s2);
            if (Config.hasResource(resourcelocation)) {
                list.add(s);
            }
        }
        final String[] astring = list.toArray(new String[list.size()]);
        return astring;
    }
    
    private static Properties[] getPanoramaProperties(final String[] folders) {
        final Properties[] aproperties = new Properties[folders.length];
        for (int i = 0; i < folders.length; ++i) {
            String s = folders[i];
            if (i == 0) {
                s = "optifine/gui";
            }
            else {
                Config.dbg("CustomPanorama: " + s);
            }
            final ResourceLocation resourcelocation = new ResourceLocation(s + "/background.properties");
            try {
                final InputStream inputstream = Config.getResourceStream(resourcelocation);
                if (inputstream != null) {
                    final Properties properties = new PropertiesOrdered();
                    properties.load(inputstream);
                    Config.dbg("CustomPanorama: " + resourcelocation.getPath());
                    aproperties[i] = properties;
                    inputstream.close();
                }
            }
            catch (IOException ex) {}
        }
        return aproperties;
    }
    
    private static int[] getWeights(final Properties[] properties) {
        final int[] weights = new int[properties.length];
        for (int i2 = 0; i2 < weights.length; ++i2) {
            Properties prop = properties[i2];
            if (prop == null) {
                prop = properties[0];
            }
            if (prop == null) {
                weights[i2] = 1;
            }
            else {
                final String str = prop.getProperty("weight", null);
                weights[i2] = Config.parseInt(str, 1);
            }
        }
        return weights;
    }
    
    private static int getRandomIndex(final int[] weights) {
        final int i = MathUtils.getSum(weights);
        final int j = CustomPanorama.random.nextInt(i);
        int k = 0;
        for (int l = 0; l < weights.length; ++l) {
            k += weights[l];
            if (k > j) {
                return l;
            }
        }
        return weights.length - 1;
    }
    
    static {
        CustomPanorama.customPanoramaProperties = null;
        random = new Random();
    }
}
