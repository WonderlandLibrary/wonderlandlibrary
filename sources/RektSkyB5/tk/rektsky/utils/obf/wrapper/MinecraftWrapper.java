/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.utils.obf.wrapper;

public class MinecraftWrapper {
    private static Object obj = null;

    public static <T> T getMinecraft(Class<T> CAST_TYPE) {
        try {
            if (obj == null) {
                obj = Class.forName("net.minecraft.client.Minecraft").getMethod("getMinecraft", new Class[0]).invoke(null, new Object[0]);
            }
            return (T)obj;
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}

