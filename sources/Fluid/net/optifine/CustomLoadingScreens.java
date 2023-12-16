// 
// Decompiled by Procyon v0.6.0
// 

package net.optifine;

import java.util.Properties;
import java.util.Set;
import java.util.Map;
import org.apache.commons.lang3.tuple.ImmutablePair;
import java.util.Arrays;
import net.minecraft.src.Config;
import net.optifine.util.StrUtils;
import java.util.HashMap;
import net.optifine.util.ResUtils;
import org.apache.commons.lang3.tuple.Pair;
import net.minecraft.network.PacketThreadUtil;

public class CustomLoadingScreens
{
    private static CustomLoadingScreen[] screens;
    private static int screensMinDimensionId;
    
    static {
        CustomLoadingScreens.screens = null;
        CustomLoadingScreens.screensMinDimensionId = 0;
    }
    
    public static CustomLoadingScreen getCustomLoadingScreen() {
        if (CustomLoadingScreens.screens == null) {
            return null;
        }
        final int i = PacketThreadUtil.lastDimensionId;
        final int j = i - CustomLoadingScreens.screensMinDimensionId;
        CustomLoadingScreen customloadingscreen = null;
        if (j >= 0 && j < CustomLoadingScreens.screens.length) {
            customloadingscreen = CustomLoadingScreens.screens[j];
        }
        return customloadingscreen;
    }
    
    public static void update() {
        CustomLoadingScreens.screens = null;
        CustomLoadingScreens.screensMinDimensionId = 0;
        final Pair<CustomLoadingScreen[], Integer> pair = parseScreens();
        CustomLoadingScreens.screens = (CustomLoadingScreen[])pair.getLeft();
        CustomLoadingScreens.screensMinDimensionId = (int)pair.getRight();
    }
    
    private static Pair<CustomLoadingScreen[], Integer> parseScreens() {
        final String s = "optifine/gui/loading/background";
        final String s2 = ".png";
        final String[] astring = ResUtils.collectFiles(s, s2);
        final Map<Integer, String> map = new HashMap<Integer, String>();
        for (int i = 0; i < astring.length; ++i) {
            final String s3 = astring[i];
            final String s4 = StrUtils.removePrefixSuffix(s3, s, s2);
            final int j = Config.parseInt(s4, Integer.MIN_VALUE);
            if (j == Integer.MIN_VALUE) {
                warn("Invalid dimension ID: " + s4 + ", path: " + s3);
            }
            else {
                map.put(j, s3);
            }
        }
        final Set<Integer> set = map.keySet();
        final Integer[] ainteger = set.toArray(new Integer[set.size()]);
        Arrays.sort(ainteger);
        if (ainteger.length <= 0) {
            return (Pair<CustomLoadingScreen[], Integer>)new ImmutablePair((Object)null, (Object)0);
        }
        final String s5 = "optifine/gui/loading/loading.properties";
        final Properties properties = ResUtils.readProperties(s5, "CustomLoadingScreens");
        final int k = ainteger[0];
        final int l = ainteger[ainteger.length - 1];
        final int i2 = l - k + 1;
        final CustomLoadingScreen[] acustomloadingscreen = new CustomLoadingScreen[i2];
        for (int j2 = 0; j2 < ainteger.length; ++j2) {
            final Integer integer = ainteger[j2];
            final String s6 = map.get(integer);
            acustomloadingscreen[integer - k] = CustomLoadingScreen.parseScreen(s6, integer, properties);
        }
        return (Pair<CustomLoadingScreen[], Integer>)new ImmutablePair((Object)acustomloadingscreen, (Object)k);
    }
    
    public static void warn(final String str) {
        Config.warn("CustomLoadingScreen: " + str);
    }
    
    public static void dbg(final String str) {
        Config.dbg("CustomLoadingScreen: " + str);
    }
}
