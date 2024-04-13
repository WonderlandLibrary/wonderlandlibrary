package net.smoothboot.client.module.render;

import net.smoothboot.client.events.Event;
import net.smoothboot.client.module.Mod;

public class Nohurtcam extends Mod {
    public Nohurtcam() {
        super("No Hurt Cam", "Removes shake animation when hurt", Category.Render);
    }
    public static boolean hurtCam = false;
    @Override
    public void onTick() {
        hurtCam=true;
        super.onTick();
    }
    @Override
    public void onDisable() {
        hurtCam=false;
        super.onDisable();
    }

}