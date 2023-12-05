package koks.api.util;

import koks.Koks;
import koks.api.interfaces.Methods;
import net.minecraft.client.Minecraft;

import java.util.HashMap;

/**
 * @author kroko
 * @created on 02.12.2020 : 12:54
 */
public class CustomUtil implements Methods {

    HashMap<String, Object> replace = new HashMap<>();

    public void update() {
        replace.put("version", Koks.getKoks().VERSION);
        replace.put("name", getPlayer().getName());
        replace.put("fps", Minecraft.getDebugFPS());
        replace.put("ping", mc.getCurrentServerData().pingToServer);
        replace.put("x", getX());
        replace.put("y", getY());
        replace.put("z", getZ());
        replace.put("ip", mc.getCurrentServerData().serverIP);
    }

    public String replace(String string) {
        for(String rep : replace.keySet()) {
            string = string.replace("%" + rep + "%", replace.get(rep) + "");
        }
        return string;
    }
}
