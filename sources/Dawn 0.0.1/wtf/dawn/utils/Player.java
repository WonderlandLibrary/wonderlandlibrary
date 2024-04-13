package wtf.dawn.utils;

import net.minecraft.client.Minecraft;

public class Player {

    protected static Minecraft mc;

    public boolean isPlayerInGame() {
        return mc.thePlayer != null && mc.theWorld != null;
    }
}
