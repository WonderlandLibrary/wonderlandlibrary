/*
 * Decompiled with CFR 0.150.
 */
package optifine;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;
import net.minecraft.util.ResourceLocation;
import optifine.Config;
import optifine.CustomPanoramaProperties;
import optifine.MathUtils;

public class CustomPanorama {
    private static CustomPanoramaProperties customPanoramaProperties = null;
    private static final Random random = new Random();

    public static CustomPanoramaProperties getCustomPanoramaProperties() {
        return customPanoramaProperties;
    }

    public static void update() {
        customPanoramaProperties = null;
        String[] astring = CustomPanorama.getPanoramaFolders();
        if (astring.length > 1) {
            CustomPanoramaProperties custompanoramaproperties;
            Properties[] aproperties = CustomPanorama.getPanoramaProperties(astring);
            int[] aint = CustomPanorama.getWeights(aproperties);
            int i = CustomPanorama.getRandomIndex(aint);
            String s = astring[i];
            Properties properties = aproperties[i];
            if (properties == null) {
                properties = aproperties[0];
            }
            if (properties == null) {
                properties = new Properties();
            }
            customPanoramaProperties = custompanoramaproperties = new CustomPanoramaProperties(s, properties);
        }
    }

    private static String[] getPanoramaFolders() {
        ArrayList<String> list = new ArrayList<String>();
        list.add("textures/gui/title/background");
        for (int i = 0; i < 100; ++i) {
            String s = "optifine/gui/background" + i;
            String s1 = s + "/panorama_0.png";
            ResourceLocation resourcelocation = new ResourceLocation(s1);
            if (!Config.hasResource(resourcelocation)) continue;
            list.add(s);
        }
        String[] astring = list.toArray(new String[list.size()]);
        return astring;
    }

    private static Properties[] getPanoramaProperties(String[] p_getPanoramaProperties_0_) {
        Properties[] aproperties = new Properties[p_getPanoramaProperties_0_.length];
        for (int i = 0; i < p_getPanoramaProperties_0_.length; ++i) {
            String s = p_getPanoramaProperties_0_[i];
            if (i == 0) {
                s = "optifine/gui";
            } else {
                Config.dbg("CustomPanorama: " + s);
            }
            ResourceLocation resourcelocation = new ResourceLocation(s + "/background.properties");
            try {
                InputStream inputstream = Config.getResourceStream(resourcelocation);
                if (inputstream == null) continue;
                Properties properties = new Properties();
                properties.load(inputstream);
                Config.dbg("CustomPanorama: " + resourcelocation.getPath());
                aproperties[i] = properties;
                inputstream.close();
                continue;
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }
        return aproperties;
    }

    private static int[] getWeights(Properties[] p_getWeights_0_) {
        int[] aint = new int[p_getWeights_0_.length];
        for (int i = 0; i < aint.length; ++i) {
            Properties properties = p_getWeights_0_[i];
            if (properties == null) {
                properties = p_getWeights_0_[0];
            }
            if (properties == null) {
                aint[i] = 1;
                continue;
            }
            String s = properties.getProperty("weight", null);
            aint[i] = Config.parseInt(s, 1);
        }
        return aint;
    }

    private static int getRandomIndex(int[] p_getRandomIndex_0_) {
        int i = MathUtils.getSum(p_getRandomIndex_0_);
        int j = random.nextInt(i);
        int k = 0;
        for (int l = 0; l < p_getRandomIndex_0_.length; ++l) {
            if ((k += p_getRandomIndex_0_[l]) <= j) continue;
            return l;
        }
        return p_getRandomIndex_0_.length - 1;
    }
}

