package club.dortware.client.gui.click;

import club.dortware.client.gui.click.element.DGUICategoryElement;
import club.dortware.client.module.Module;
import club.dortware.client.module.enums.ModuleCategory;
import com.google.common.collect.Lists;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.IOException;
import java.util.Collection;

public class DortGUI extends GuiScreen {

	private static final DortGUI INSTANCE = new DortGUI();
	public Module selMod;
	private final Collection<DGUICategoryElement> elements = Lists.newArrayList();

	private int index;
	public ModuleCategory selCat;
	public static final Color BACKGROUND_COLOR = new Color(48, 44, 44);

	public DortGUI() {
		for (ModuleCategory cat : ModuleCategory.values()) {
			this.elements.add(new DGUICategoryElement(cat, cat.getName()));
		}
	}

	public static DortGUI instance() {
		return INSTANCE;
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		this.elements.forEach(s -> {
			s.kt(keyCode);
		});
		super.keyTyped(typedChar, keyCode);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		ScaledResolution scl = new ScaledResolution(mc, width, height);
		GlStateManager.pushMatrix();
		float x = width / 7f;
		float y = height / 24f;
		float x2 = width / 7 + 360;
		float y2 = height / 24 + (75 - 11) + (31 * ModuleCategory.values().length);
		DortGUI.drawRect(new Rect(x, y, x2, y2), BACKGROUND_COLOR.getRGB());
		DortGUI.drawRect(new Rect(x2 - 2, y, x2, y2), BACKGROUND_COLOR.brighter().getRGB());
		DortGUI.drawRect(new Rect(x + 73, y, x + 75, y2), BACKGROUND_COLOR.brighter().getRGB());
		DortGUI.drawRect(new Rect(x, y, x2, y + 2), BACKGROUND_COLOR.brighter().getRGB());
		DortGUI.drawRect(new Rect(x, y, x + 2, y2), BACKGROUND_COLOR.brighter().getRGB());

		GlStateManager.pushMatrix();
		mc.getTextureManager().bindTexture(new ResourceLocation("memeware/memeware.png"));
		DortGUI.drawModalRectWithCustomSizedTexture((int) x + 4, (int) y + 4, 56, 56, 56, 56, 56, 56);
		GlStateManager.popMatrix();
//		mc.fontRendererObj.drawString("Meme", x + 39, y + 15, -1);
//		mc.fontRendererObj.drawString(Client.INSTANCE.getClientVersion(), x + (39 / 1.8F), y + 32 + 15, -1);
		this.elements.forEach(s -> {
			s.drawElement(this.width, this.height, scl, x + 5, y, index, mouseX, mouseY);
			index += 31;
		});
		ScaledResolution scaled = new ScaledResolution(mc, (int) x, (int) y);
		int xx = scaled.getScaledWidth();
		int xy = scaled.getScaledHeight();
		DortGUI.drawRect(xx, xy + index - 13, x2, xy + index - 11, BACKGROUND_COLOR.brighter().getRGB());
		index = 75;
		GlStateManager.popMatrix();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	public static Rect drawRect(Rect rect, int color) {
		Gui.drawRect(rect.x, rect.y, rect.w, rect.h, color);
		return rect;
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		super.mouseReleased(mouseX, mouseY, state);
	}

}
