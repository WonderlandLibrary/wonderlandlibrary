package best.azura.client.impl.ui.customhud.impl;

import best.azura.client.impl.Client;
import best.azura.client.api.ui.customhud.Element;
import best.azura.client.impl.module.impl.render.HUD;
import best.azura.client.impl.ui.font.Fonts;
import best.azura.client.util.color.ColorUtil;
import net.minecraft.client.renderer.GlStateManager;

public class WatermarkElement extends Element {
	public WatermarkElement() {
		super("Watermark", 3, 3, 32, 32);
	}
	
	
	/**
	 * Render method
	 */
	@Override
	public void render() {
		fitInScreen(mc.displayWidth, mc.displayHeight);
		HUD hud = (HUD) Client.INSTANCE.getModuleManager().getModule(HUD.class);
		// Render the Client Watermark on the user position
		final String textA = "§lA§r";
		final String textZura = "§fzura§r";
		final String text = textA+textZura;
		if (hud.useClientFont.getObject()) {
			Fonts.INSTANCE.hudFontBold.drawStringWithShadow(textA,getX(), getY(), ColorUtil.getHudColor(1).getRGB());
			Fonts.INSTANCE.hudFont.drawStringWithShadow(textZura,getX() + Fonts.INSTANCE.hudFontBold.getStringWidth(textA), getY(), ColorUtil.getHudColor(1).getRGB());
		}  else  {
			GlStateManager.scale(2,2,2);
			mc.fontRendererObj.drawStringWithShadow(text,getX() / 2,getY() / 2,ColorUtil.getHudColor(1).getRGB());
			GlStateManager.scale(1.0 / 2,1.0 / 2,1.0 / 2);
		}
		setWidth(hud.useClientFont.getObject()? Fonts.INSTANCE.hudFont.getStringWidth(text) : mc.fontRendererObj.getStringWidth(text) * 2);
		setHeight(hud.useClientFont.getObject()? Fonts.INSTANCE.hudFont.FONT_HEIGHT : mc.fontRendererObj.FONT_HEIGHT * 2);
	}
	
	@Override
	public Element copy() {
		return new WatermarkElement();
	}
}