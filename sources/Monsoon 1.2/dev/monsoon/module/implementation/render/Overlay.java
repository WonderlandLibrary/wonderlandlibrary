package dev.monsoon.module.implementation.render;

import dev.monsoon.Monsoon;
import dev.monsoon.event.Event;
import dev.monsoon.event.listeners.EventRender3D;
import dev.monsoon.module.base.Module;
import dev.monsoon.module.setting.impl.BooleanSetting;
import dev.monsoon.util.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import org.lwjgl.input.Keyboard;
import dev.monsoon.module.enums.Category;

public class Overlay extends Module {

	BooleanSetting blocks = new BooleanSetting("Blocks", true, this);
	BooleanSetting entites = new BooleanSetting("Entites", true, this);

	public Overlay() {
		super("Overlay", Keyboard.KEY_NONE, Category.RENDER);
		this.addSettings(blocks,entites);
	}
	
	public void onEnable() {
		
	}
	
	public void onDisable() {
		
	}
	
	
	
	public void onEvent(Event e) {
		if(e instanceof EventRender3D) {
			if(entites.isEnabled()) {
				if (mc.pointedEntity != null && mc.pointedEntity instanceof EntityLivingBase && mc.pointedEntity != Monsoon.manager.killAura.target) {
					try {
						EntityLivingBase player = (EntityLivingBase) Minecraft.getMinecraft().pointedEntity;
						Monsoon.manager.killAura.targetESPBox(player, 1, 1, 1);
					} catch(Exception ex) {
						ex.printStackTrace();
					}
				}
			}

			if(blocks.isEnabled()) {
				int x, y, z;
				if(mc.objectMouseOver.entityHit == null) {
					x = mc.objectMouseOver.func_178782_a().getX();
					y = mc.objectMouseOver.func_178782_a().getY();
					z = mc.objectMouseOver.func_178782_a().getZ();
					BlockPos pos = new BlockPos(x, y, z);
					if (mc.theWorld.getBlockState(pos).getBlock() != Blocks.air && !Monsoon.manager.scaffold.isEnabled()) {
						RenderUtil.drawBoxFromBlockpos(pos, 1, 1, 1, 1);
					}
				}
			}
		}
	}
}
