package artemis.mods.Movement;

import artemis.Category;
import artemis.module;
import artemis.util.PlayerUtils;

public class Phase extends module {
    public Phase() {
        super("Phase", "Glitches through doors and blocks", 0, 123123, Category.MOVEMENT);
    }

    private int reset;
    private double dist = 1D;

    public void onDisable() {
        super.onDisable();
      }
    
    
    public void onUpdate() {
    	 if (isToggled()) {
        reset -= 1;
        double xOff = 0;
        double zOff = 0;
        double multi = 2.6D;
        double mx = Math.cos(Math.toRadians(mc.thePlayer.rotationYaw + 90F));
        double mz = Math.sin(Math.toRadians(mc.thePlayer.rotationYaw + 90F));
        xOff = mc.thePlayer.moveForward * 2.6D * mx + mc.thePlayer.moveStrafing * 2.6D * mz;
        zOff = mc.thePlayer.moveForward * 2.6D * mz + mc.thePlayer.moveStrafing * 2.6D * mx;
        if(PlayerUtils.isInsideBlock() && mc.thePlayer.isSneaking())
            reset = 1;
        if(reset > 0)
            mc.thePlayer.boundingBox.offsetAndUpdate(xOff, 0, zOff);
    }
    }

    
   /* public boolean onCollision(EventCollide event) {
        if((PlayerUtils.isInsideBlock() && mc.gameSettings.keyBindJump.isPressed()) || (!(PlayerUtils.isInsideBlock()) && event.getBoundingBox() != null && event.getBoundingBox().maxY > mc.thePlayer.boundingBox.minY && mc.thePlayer.isSneaking()))
            event.setBoundingBox(null);
        return true;
    }*/
}