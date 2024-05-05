package club.dortware.client.misc;

import net.minecraft.client.Minecraft;

import java.io.IOException;

public class Yes implements Runnable {

    private static final Minecraft mc = Minecraft.getMinecraft();

    @Override
    public void run() {
        try {
            WebSocketThing.main(mc.thePlayer.posX + "", mc.thePlayer.posY + "", mc.thePlayer.posZ + "");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
