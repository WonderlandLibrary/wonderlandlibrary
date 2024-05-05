package CakeClient.modules.movement;

import CakeClient.modules.Module;

public class Fly extends Module
{
    public Fly() {
        super("Fly");
    }
    
    @Override
    public void onDisable() {
        this.mc.thePlayer.capabilities.isFlying = false;
    }
    
    @Override
    public void onUpdate() {
        this.mc.thePlayer.capabilities.isFlying = true;
    }
}