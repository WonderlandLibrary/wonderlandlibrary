package xyz.cucumber.base.module.feat.movement;

import org.lwjgl.input.Keyboard;

import net.minecraft.util.MathHelper;
import xyz.cucumber.base.Client;
import xyz.cucumber.base.events.EventListener;
import xyz.cucumber.base.events.EventPriority;
import xyz.cucumber.base.events.ext.EventUpdate;
import xyz.cucumber.base.module.Category;
import xyz.cucumber.base.module.Mod;
import xyz.cucumber.base.module.ModuleInfo;
import xyz.cucumber.base.module.feat.combat.KillAuraModule;
import xyz.cucumber.base.module.feat.player.ScaffoldModule;
import xyz.cucumber.base.module.feat.player.SmoothRotationModule;
import xyz.cucumber.base.utils.MovementUtils;
import xyz.cucumber.base.utils.math.RotationUtils;

@ModuleInfo(category = Category.MOVEMENT, description = "Allows you to sprint always", name = "Sprint", key = 0)
public class SprintModule extends Mod {

	@EventListener(EventPriority.LOW)
	public void onUpdate(EventUpdate e) {
		if (Client.INSTANCE.getModuleManager().getModule(ScaffoldModule.class).isEnabled() && RotationUtils.customRots)
        {
            return;
        }
		if(mc.thePlayer.reSprint == 2) {
			return;
		}
        
        mc.gameSettings.keyBindSprint.pressed = true;

        if (!mc.thePlayer.isCollidedHorizontally && !mc.thePlayer.isSneaking() && mc.gameSettings.keyBindForward.pressed && !mc.thePlayer.isSprinting() && mc.thePlayer.isUsingItem() && Client.INSTANCE.getModuleManager().getModule(NoSlowModule.class).isEnabled())
        {
            mc.thePlayer.setSprinting(true);
        }
        
        KillAuraModule ka = (KillAuraModule) Client.INSTANCE.getModuleManager().getModule("Kill Aura");
        if(RotationUtils.customRots && ka.MoveFix.getMode().equalsIgnoreCase("Silent") && ka.isEnabled()) {
        	if(Math.abs(MathHelper.wrapAngleTo180_float((float) Math.toDegrees(MovementUtils.getDirection(mc.thePlayer.rotationYaw))) - MathHelper.wrapAngleTo180_float(RotationUtils.serverYaw)) > 90 - 22.5) {
				mc.gameSettings.keyBindSprint.pressed = false;
				mc.thePlayer.setSprinting(false);
			}
        }
        
        ScaffoldModule scaffold = (ScaffoldModule) Client.INSTANCE.getModuleManager().getModule("Scaffold");
        SmoothRotationModule smoothRotation = (SmoothRotationModule) Client.INSTANCE.getModuleManager().getModule(SmoothRotationModule.class);
        if(RotationUtils.customRots && smoothRotation.isEnabled()) {
        	if(!scaffold.isEnabled()) {
        		if(Math.abs(MathHelper.wrapAngleTo180_float((float) Math.toDegrees(MovementUtils.getDirection(mc.thePlayer.rotationYaw))) - MathHelper.wrapAngleTo180_float(RotationUtils.serverYaw)) > 90 - 22.5) {
    				mc.gameSettings.keyBindSprint.pressed = false;
    				mc.thePlayer.setSprinting(false);
    			}
        	}
        }
	}
}
