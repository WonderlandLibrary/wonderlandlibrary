package me.aquavit.liquidsense.ui.client.hud.element.elements;

import me.aquavit.liquidsense.utils.render.BlurBuffer;
import me.aquavit.liquidsense.utils.render.RenderUtils;
import me.aquavit.liquidsense.utils.render.Translate;
import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.ui.client.hud.element.Border;
import me.aquavit.liquidsense.ui.client.hud.element.Element;
import me.aquavit.liquidsense.ui.client.hud.element.ElementInfo;
import me.aquavit.liquidsense.ui.font.Fonts;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;


@ElementInfo(name = "KeyBinds")
public class KeyBinds extends Element {

	private Translate translate = new Translate(0f , 0f);

	@Override
	public Border drawElement() {
		int index = 0;
		for (Module module : LiquidSense.moduleManager.getModules()) {
			if (module.getKeyBind() == Keyboard.KEY_NONE)
				continue;
			index++;
		}

		translate.translate(0f , (8 + index * 14) , 2.0);

		if (translate.getY() > 0 ) {
			BlurBuffer.blurArea((int) ((-4.5F + this.getRenderX()) * this.getScale()),
					(int) ((this.getRenderY() + Fonts.csgo40.FONT_HEIGHT - 2) * this.getScale()),
					(Fonts.csgo40.getStringWidth("F") + Fonts.font20.getStringWidth("Binds") + 67) * this.getScale(),
					translate.getY() * this.getScale(),
					true);

			if (!this.getInfo().disableScale())
				GL11.glScalef(this.getScale(), this.getScale(), this.getScale());
			GL11.glTranslated(this.getRenderX(), this.getRenderY(), 0.0);
		}

		int y = 1;
		for (Module module : LiquidSense.moduleManager.getModules()) {
			if (module.getKeyBind() == Keyboard.KEY_NONE ) {
				if(module.getKeytranslate().getY() > 0f)
				module.getKeytranslate().translate(0f, 0f , 2.0);
			} else
				module.getKeytranslate().translate(255f, 14f , 2.0);

			if (module.getKeytranslate().getY() == 0f || module.getKeytranslate().getX() <= 30f)
				continue;

			GlStateManager.resetColor();
			if ((int) module.getKeytranslate().getX() > -1 && (int) module.getKeytranslate().getX() < 256) {
				Fonts.font20.drawString(module.getName(), -1.1F, y + 17, new Color(255 , 255 ,255 , (int) module.getKeytranslate().getX() ).getRGB());
				Fonts.font20.drawString(module.getState() ? "on" : "off", Fonts.csgo40.getStringWidth("F") + Fonts.font20.getStringWidth("Binds") + 46F, y + 17, new Color(255 , 255 ,255 , (int) module.getKeytranslate().getX()).getRGB());

			}
			y += module.getKeytranslate().getY();
		}
		RenderUtils.drawRoundedRect(-5.2F, -5.5F, Fonts.csgo40.getStringWidth("K") + Fonts.font20.getStringWidth("Binds") + 65, Fonts.csgo40.FONT_HEIGHT + 6F, 1.5F,
			new Color(16, 25, 32, 200).getRGB(), 1F, new Color(16, 25, 32, 200).getRGB());
		Fonts.csgo40.drawString("K", -1.5F, -1.5F, new Color(0, 131, 193).getRGB(), false);
		Fonts.font20.drawString("Binds", Fonts.csgo40.getStringWidth("K") + 1.8F, -1F, Color.WHITE.getRGB(), false);
		return new Border(20, 20, 120, 80);
	}
}
