package best.azura.client.impl.ui.customhud.impl;

import best.azura.client.impl.Client;
import best.azura.client.api.ui.customhud.Element;
import best.azura.client.api.ui.customhud.corner.Direction;
import best.azura.client.api.ui.customhud.corner.Side;
import best.azura.client.impl.module.impl.render.HUD;
import best.azura.client.impl.ui.font.Fonts;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import java.util.ArrayList;

public class PotionEffectElement extends Element {
	public PotionEffectElement() {
		super("Potions", 2, 15, 64, 64);
	}
	
	private String s, s111 = " ";
	
	@Override
	public void render() {
		HUD hud = (HUD) Client.INSTANCE.getModuleManager().getModule(HUD.class);
		ArrayList<PotionEffect> activeEffects = new ArrayList<>(mc.thePlayer.getActivePotionEffects());
		activeEffects.sort((o1, o2) -> {
			Potion potion = Potion.potionTypes[o1.getPotionID()];
			String s1 = I18n.format(potion.getName());
			if (o1.getAmplifier() == 1) s1 = s1 + " " + I18n.format("enchantment.level.2");
			else if (o1.getAmplifier() == 2) s1 = s1 + " " + I18n.format("enchantment.level.3");
			else if (o1.getAmplifier() == 3) s1 = s1 + " " + I18n.format("enchantment.level.4");
			s = s1 + " §f" + Potion.getDurationString(o1);
			Potion potion1 = Potion.potionTypes[o1.getPotionID()];
			String s11 = I18n.format(potion1.getName());
			if (o2.getAmplifier() == 1) s11 = s11 + " " + I18n.format("enchantment.level.2");
			else if (o2.getAmplifier() == 2) s11 = s11 + " " + I18n.format("enchantment.level.3");
			else if (o2.getAmplifier() == 3) s11 = s11 + " " + I18n.format("enchantment.level.4");
			s111 = s11 + " §7" + o2.getAmplifier() + " - " + (o2.getDuration() < 333 ?
                                                                      "§c" + Potion.getDurationString(o1) :
                                                                      Potion.getDurationString(o1));
			return Double.compare(hud.useClientFont.getObject() ? Fonts.INSTANCE.hudFont.getStringWidth(s111) :
                                          mc.fontRendererObj.getStringWidth(s111) * 2,
					hud.useClientFont.getObject() ? Fonts.INSTANCE.hudFont.getStringWidth(s) :
                            mc.fontRendererObj.getStringWidth(s) * 2);
		});
		
		int offset = 0;
		if (getDirection() == Direction.DOWN) {
			offset = (int) -((10 + (hud.useClientFont.getObject() ? Fonts.INSTANCE.hudFont.FONT_HEIGHT / 2F : mc.fontRendererObj.FONT_HEIGHT)) * (activeEffects.size() - 2));
		}
		if (!hud.useClientFont.getObject()) GlStateManager.scale(2, 2, 2);
		for (PotionEffect effect : activeEffects) {
			
			// Render and offset the text for the duration of the current potion effect.
			
			Potion potion = Potion.potionTypes[effect.getPotionID()];
			StringBuilder s1 = new StringBuilder(I18n.format(potion.getName()));
			if (effect.getAmplifier() == 1) s1.append(" ").append(I18n.format("enchantment.level.2"));
			else if (effect.getAmplifier() == 2) s1.append(" ").append(I18n.format("enchantment.level.3"));
			else if (effect.getAmplifier() == 3) s1.append(" ").append(I18n.format("enchantment.level.4"));
			s = s1 + " " + Potion.getDurationString(effect);

			double addedX = 0;
			if (getSide() == Side.RIGHT)
				addedX = getWidth() - (hud.useClientFont.getObject() ? Fonts.INSTANCE.hudFont.getStringWidth(s) : mc.fontRendererObj.getStringWidth(s) * 2);
			
			if (hud.useClientFont.getObject()) {
				Fonts.INSTANCE.hudFont.drawStringWithShadow(s,
						getX() + addedX, getY() + offset, -1);
			} else {
				mc.fontRendererObj.drawStringWithShadow(s,
						(getX() + addedX) / 2, getY() / 2 + offset / 2F, -1);
			}

			offset += 10 + (hud.useClientFont.getObject() ? Fonts.INSTANCE.hudFont.FONT_HEIGHT / 2F : mc.fontRendererObj.FONT_HEIGHT);
			setWidth(Math.max(getWidth(), hud.useClientFont.getObject() ? Fonts.INSTANCE.hudFont.getStringWidth(s) : mc.fontRendererObj.getStringWidth(s) * 2));
		}
		setHeight(((10 + (hud.useClientFont.getObject() ? Fonts.INSTANCE.hudFont.FONT_HEIGHT / 2F : mc.fontRendererObj.FONT_HEIGHT)) * activeEffects.size()));
		
		if (!hud.useClientFont.getObject()) GlStateManager.scale(1 / 2.0, 1 / 2.0, 1 / 2.0);
	}
	
	@Override
	public Element copy() {
		return new PotionEffectElement();
	}
	
}
