package CakeClient.modules.movement;

import CakeClient.modules.Module;

public class PermaSprint extends Module
{
    public PermaSprint() {
        super("PermaSprint");
    }
    
    @Override
    public void onUpdate() {
        this.mc.thePlayer.setSprinting(true);
    }
}