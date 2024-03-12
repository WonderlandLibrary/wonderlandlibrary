package xyz.cucumber.base.module.feat.visuals;

import java.awt.Color;

import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import xyz.cucumber.base.events.EventListener;
import xyz.cucumber.base.events.EventType;
import xyz.cucumber.base.events.ext.EventBloom;
import xyz.cucumber.base.module.Category;
import xyz.cucumber.base.module.Mod;
import xyz.cucumber.base.module.ModuleInfo;
import xyz.cucumber.base.module.settings.ColorSettings;
import xyz.cucumber.base.utils.RenderUtils;

@ModuleInfo(category = Category.VISUALS, description = "Displays entities through walls", name = "Chams")
public class ChamsModule extends Mod{
	
	public ColorSettings color = new ColorSettings("Color", "Static", -1, -1, 30);
	public ChamsModule() {
		this.addSettings(
				color
				);
	}
	
}
