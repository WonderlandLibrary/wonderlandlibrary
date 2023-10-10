import net.minecraft.client.main.Main;

import java.util.Arrays;

public class Start {
    public static void main(final String[] args) {
        Main.main(concat(new String[]{"--version", "1.8.9-Optifine_HD_U_L5", "--accessToken", "0", "--assetsDir", "assets", "--assetIndex", "1.8", "--userProperties", "{}"}, args));
    }

    public static <T> T[] concat(final T[] first, final T[] second) {
        final T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }
}