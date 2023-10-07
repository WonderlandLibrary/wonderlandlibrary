package sudo.module.movement;

import net.minecraft.client.option.GameOptions;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import sudo.module.Mod;
import sudo.module.settings.ModeSetting;
import sudo.module.settings.NumberSetting;

public class ElytraFly extends Mod {

	public ModeSetting mode = new ModeSetting("Mode", "Static", "Static", "Boost");
	public NumberSetting speed = new NumberSetting("Speed", 0, 10, 2, 1);

	public ElytraFly() {
		super("ElytraFly", "Allows you to fly without rockets", Category.MOVEMENT, 0);
		addSettings(mode, speed);
	}

    private boolean wearingElytra() {
        ItemStack equippedStack = mc.player.getEquippedStack(EquipmentSlot.CHEST);
        return equippedStack != null && equippedStack.getItem() == Items.ELYTRA;
    }
    
    @Override
    public void onTick() {
    	if (mc.player.isFallFlying() && wearingElytra()) {
    		if (mode.getMode().equalsIgnoreCase("Static")) {
            	GameOptions go = mc.options;
            	float y = mc.player.getYaw();
            	int mx = 0, my = 0, mz = 0;
     
            	if (go.jumpKey.isPressed()) {
            		my++;
            	}
            	if (go.backKey.isPressed()) {
            		mz++;
            	}
            	if (go.leftKey.isPressed()) {
            		mx--;
            	}
            	if (go.rightKey.isPressed()) {
            		mx++;
            	}
            	if (go.sneakKey.isPressed()) {
            		my--;
            	}
            	if (go.forwardKey.isPressed()) {
            		mz--;
            	}
            	double ts = speed.getValueFloat() / 2;
                double s = Math.sin(Math.toRadians(y));
                double c = Math.cos(Math.toRadians(y));
                double nx = ts * mz * s;
                double nz = ts * mz * -c;
                double ny = ts * my;
                nx += ts * mx * -c;
                nz += ts * mx * -s;
                Vec3d nv3 = new Vec3d(nx, ny, nz);
                mc.player.setVelocity(nv3);
                
            } else if (mode.is("Boost")) {
    			if (mc.player.getAbilities().flying) mc.player.getAbilities().flying = false;

    			float yaw = (float) Math.toRadians(mc.player.getYaw());
    			if (mc.options.forwardKey.isPressed()) {
    				mc.player.addVelocity(
    						(-MathHelper.sin(yaw) * speed.getValue() / 30), 
    						(((mc.player.getRotationVector().multiply(speed.getValue()).y /2) * speed.getValue()) / 60),
    						(MathHelper.cos(yaw) * speed.getValue() / 30));
    			}
            }
    		
    	}
    	super.onTick();
    }

}