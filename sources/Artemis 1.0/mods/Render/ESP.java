package artemis.mods.Render;
import org.lwjgl.input.Keyboard;

import artemis.Category;
import artemis.module;
import artemis.util.RenderUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;


public class ESP extends module{

	public ESP() {
		super("ESP", "Draws a box around", Keyboard.KEY_K, 45, Category.RENDER);
	}
	
	@Override
	public void onRender() {
		if(this.isToggled()){
			for(Object e: mc.theWorld.loadedEntityList){
				if(e instanceof EntityPlayer){
					RenderUtils.entityESPBox((Entity)e, 0);
				}
			}
		}
	}

}