package net.optifine;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;

import net.minecraft.src.Config;
import net.minecraft.util.ResourceLocation;
import net.optifine.CustomPanoramaProperties;
import net.optifine.util.MathUtils;
import net.optifine.util.PropertiesOrdered;

public class CustomPanorama {
    private static CustomPanoramaProperties customPanoramaProperties = null;
    private static final Random random = new Random();

    public static CustomPanoramaProperties getCustomPanoramaProperties() {
        return customPanoramaProperties;
    }

    public static void update() {
        CustomPanoramaProperties cpp;
        customPanoramaProperties = null;
        String[] folders = CustomPanorama.getPanoramaFolders();
        if (folders.length <= 1) {
            return;
        }
        Properties[] properties = CustomPanorama.getPanoramaProperties(folders);
        int[] weights = CustomPanorama.getWeights(properties);
        int index = CustomPanorama.getRandomIndex(weights);
        String folder = folders[index];
        Properties props = properties[index];
        if (props == null) {
            props = properties[0];
        }
        if (props == null) {
            props = new PropertiesOrdered();
        }
        customPanoramaProperties = cpp = new CustomPanoramaProperties(folder, props);
    }

    private static String[] getPanoramaFolders() {
        ArrayList<String> listFolders = new ArrayList<String>();
        listFolders.add("textures/gui/title/background");
        for (int i = 0; i < 100; ++i) {
            String folder = "optifine/gui/background" + i;
            String path = folder + "/panorama_0.png";
            ResourceLocation loc = new ResourceLocation(path);
            if (!Config.hasResource((ResourceLocation)loc)) continue;
            listFolders.add(folder);
        }
        String[] folders = listFolders.toArray(new String[listFolders.size()]);
        return folders;
    }

    private static Properties[] getPanoramaProperties(String[] folders) {
        Properties[] propsArr = new Properties[folders.length];
        for (int i = 0; i < folders.length; ++i) {
            String folder = folders[i];
            if (i == 0) {
                folder = "optifine/gui";
            } else {
                Config.dbg((String)("CustomPanorama: " + folder));
            }
            ResourceLocation propLoc = new ResourceLocation(folder + "/background.properties");
            try {
                InputStream in = Config.getResourceStream((ResourceLocation)propLoc);
                if (in == null) continue;
                PropertiesOrdered props = new PropertiesOrdered();
                props.load(in);
                Config.dbg((String)("CustomPanorama: " + propLoc.getResourcePath()));
                propsArr[i] = props;
                in.close();
                continue;
            }
            catch (IOException e) {
                // empty catch block
            }
        }
        return propsArr;
    }

    private static int[] getWeights(Properties[] properties) {
        int[] weights = new int[properties.length];
        for (int i = 0; i < weights.length; ++i) {
            Properties prop = properties[i];
            if (prop == null) {
                prop = properties[0];
            }
            if (prop == null) {
                weights[i] = 1;
                continue;
            }
            String str = prop.getProperty("weight", null);
            weights[i] = Config.parseInt((String)str, (int)1);
        }
        return weights;
    }

    private static int getRandomIndex(int[] weights) {
        int sumWeights = MathUtils.getSum(weights);
        int r = random.nextInt(sumWeights);
        int sum = 0;
        for (int i = 0; i < weights.length; ++i) {
            if ((sum += weights[i]) <= r) continue;
            return i;
        }
        return weights.length - 1;
    }
}

