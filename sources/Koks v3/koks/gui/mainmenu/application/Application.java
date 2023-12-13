package koks.gui.mainmenu.application;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

/**
 * @author kroko
 * @created on 08.11.2020 : 02:10
 */
public class Application {

    public final Type type;
    public String ip, name;

    public Application(Type type, String name, String ip) {
        this.type = type;
        this.ip = ip;
        this.name = name;
    }

    public void draw(int indexX, int indexY) {
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution sr = new ScaledResolution(mc);

        int width = sr.getScaledWidth() / 64;
        int height = sr.getScaledHeight() / 64;

        int x = Math.round(width * indexX);
        int y = Math.round(height * indexY);

    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public enum Type {
        SERVER;
    }
}
