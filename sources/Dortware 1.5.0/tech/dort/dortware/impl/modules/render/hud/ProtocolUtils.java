package tech.dort.dortware.impl.modules.render.hud;

import tech.dort.dortware.impl.utils.java.ReflectUtils;

import java.lang.reflect.InvocationTargetException;

public class ProtocolUtils {
    public static String getProtocolName(int clientSideProtocol) {
        try {
            int clientSideVer = ((Number) ReflectUtils.getField(null, "com.github.creeper123123321.viafabric.ViaFabric", "clientSideVersion")).intValue();
            return String.valueOf(ReflectUtils.call(null, "com.github.creeper123123321.viafabric.util.ProtocolUtils", "getProtocolName", clientSideVer));
        } catch (ClassNotFoundException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return "1.8.x";
    }
}
