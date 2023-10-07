package sudo.module.render;

import java.awt.Color;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import sudo.module.Mod;
import sudo.module.settings.ColorSetting;
import sudo.utils.render.QuadColor;
import sudo.utils.render.RenderUtils;
import sudo.utils.world.EntityUtils;

public class CityESP extends Mod {

	public ColorSetting color = new ColorSetting("Color", new Color(0xffFF1464));
	public CityESP() {
		super("CityESP", "Highlight blocks to break to city players", Category.RENDER, 0);
		addSetting(color);
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
	}
	@Override
	public void onDisable() {
		super.onDisable();
	}
	
	
	@Override
	public void onWorldRender(MatrixStack matrices) {		
		for (Entity player : mc.world.getEntities()) {
			if (player instanceof PlayerEntity && EntityUtils.getCityBlock((PlayerEntity) player) != null) {
				RenderUtils.drawBoxOutline(EntityUtils.getCityBlock((PlayerEntity)player), QuadColor.single(new Color(color.getColor().getRed(), color.getColor().getGreen(), color.getColor().getBlue(), 255).getRGB()), 2);
				RenderUtils.drawBoxFill(EntityUtils.getCityBlock((PlayerEntity)player), QuadColor.single(new Color(color.getColor().getRed(), color.getColor().getGreen(), color.getColor().getBlue(), 100).getRGB()));

			}
		}
		super.onWorldRender(matrices);
	}
}