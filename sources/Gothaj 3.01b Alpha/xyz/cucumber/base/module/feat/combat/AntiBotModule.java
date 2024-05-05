package xyz.cucumber.base.module.feat.combat;

import org.lwjgl.input.Keyboard;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import xyz.cucumber.base.events.EventListener;
import xyz.cucumber.base.events.ext.EventTick;
import xyz.cucumber.base.module.Category;
import xyz.cucumber.base.module.Mod;
import xyz.cucumber.base.module.ModuleInfo;
import xyz.cucumber.base.module.settings.BooleanSettings;

@ModuleInfo(category = Category.COMBAT, description = "Automatically removes anticheat bot", name = "Anti Bot", key = Keyboard.KEY_NONE)

public class AntiBotModule extends Mod {
	
	public BooleanSettings hypixel = new BooleanSettings("Hypixel", true);
	public BooleanSettings matrix = new BooleanSettings("Matrix", true);
	
	public AntiBotModule() {
		this.addSettings(hypixel, matrix);
	}
	
	@EventListener
	public void onTick(EventTick e) {
		for(Entity ent : mc.theWorld.loadedEntityList) {
			if(ent != mc.thePlayer && ent instanceof EntityPlayer) {
				if(ent.getCustomNameTag() == "" && matrix.isEnabled()) {
    				mc.theWorld.removeEntity(ent);
    			}
				if(ent.isInvisible() && hypixel.isEnabled()) {
    				mc.theWorld.removeEntity(ent);
    			}
			}
		}
	}
}
