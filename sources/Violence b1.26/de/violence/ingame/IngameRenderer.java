package de.violence.ingame;

import de.violence.Violence;
import de.violence.font.FontManager;
import de.violence.font.SlickFont;
import de.violence.gui.VSetting;
import de.violence.module.Module;
import de.violence.save.manager.LoadConfig;
import de.violence.tabgui.TabGui;
import de.violence.ui.Colours;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class IngameRenderer {
	static Minecraft mc = Minecraft.getMinecraft();
	static TabGui tabGui = new TabGui(1, 10);
	static boolean setup = false;
	static double currentSlot = 0.0D;
	public static double y;

	public static TabGui getTabGui() {
		return tabGui;
	}

	public static void onRender() {
		if (!setup) {
			new LoadConfig();
			setup = true;
		}

		renderModules();
		String theme = VSetting.getByName("Hud Theme", Module.getByName("Hud")).getActiveMode();
		if (theme.equalsIgnoreCase("Simple")) {
			if (tabGui.getY() != 22) {
				tabGui.setY(22);
			}

			GuiIngame var10000 = mc.ingameGUI;
			GuiIngame.drawRect(0, 3, FontManager.mainMenu.getStringWidth(Violence.NAME), 22, Colours.getLightBlue(255));
			FontManager.mainMenu.drawString(Violence.NAME, 0, 0, -1);
		} else if (theme.equalsIgnoreCase("Reliant")) {
			mc.fontRendererObj.drawString(Violence.NAME + " (" + Violence.VERSION + ")", 1, 1, -1);
			if (tabGui.getY() != 8) {
				tabGui.setY(8);
			}
		}

		if (VSetting.getByName("Enabled", Module.getByName("TabGui")).isToggled()) {
			tabGui.renderTabGui();
		}

	}

	static List<Module> getSorted(final SlickFont slickFont) {
		final List<Module> modules = new ArrayList<Module>();
		for (Object mod : Module.getModuleList()) {
			Module mods = (Module) mod;
			modules.add(mods);
		}
		modules.sort(new Comparator<Module>() {
			@Override
			public int compare(final Module o1, final Module o2) {
				if (slickFont != null) {
					return slickFont.getStringWidth(o2.displayName) - slickFont.getStringWidth(o1.displayName);
				}
				return IngameRenderer.mc.fontRendererObj.getStringWidth(o2.displayName)
						- IngameRenderer.mc.fontRendererObj.getStringWidth(o1.displayName);
			}
		});
		return modules;
	}

	public static void renderHotbar() {
		if (mc.currentScreen != null && mc.currentScreen instanceof GuiChat) {
			if (y > 0.0D) {
				y -= 0.3D;
			}
		} else if (y < 22.0D) {
			y += 0.3D;
		}

		double targetSlot = (double) mc.thePlayer.inventory.currentItem;
		if (Math.abs(currentSlot - targetSlot) < 0.01D) {
			currentSlot = targetSlot;
		} else {
			currentSlot += (targetSlot - currentSlot) / 10.0D;
		}

		if (!VSetting.getByName("Slow Scrolling", Module.getByName("Hud")).isToggled()) {
			currentSlot = targetSlot;
		}

		GuiIngame var10000;
		if (VSetting.getByName("Hotbar Background", Module.getByName("Hud")).isToggled()) {
			var10000 = mc.ingameGUI;
			GuiIngame.drawRect(0, (int) ((double) ScaledResolution.getScaledHeight() - y),
					ScaledResolution.getScaledWidth(), ScaledResolution.getScaledHeight(),
					Colours.getColor(0, 0, 0, 160));
		}

		if (VSetting.getByName("Hotbar Image", Module.getByName("Hud")).isToggled()) {
			GL11.glPushMatrix();
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glScalef(0.2F, 0.2F, 0.2F);
			ResourceLocation i = new ResourceLocation("textures/gui/violence/violencehotbar.png");
			mc.getTextureManager().bindTexture(i);
			mc.ingameGUI.drawTexturedModalRect(-30,
					(int) ((double) (ScaledResolution.getScaledHeight() * 5) - y * 8.6363D), 255, 260, 255, 255);
			GL11.glPopMatrix();
		}

		if (VSetting.getByName("Hotbar Coordinates", Module.getByName("Hud")).isToggled()) {
			FontManager.arrayList_Bebasneue.drawString("X: " + (double) Math.round(mc.thePlayer.posX * 10.0D) / 10.0D,
					45, (int) ((double) ScaledResolution.getScaledHeight() - y + 5.0D), -1);
			FontManager.arrayList_Bebasneue.drawString("Y: " + (double) Math.round(mc.thePlayer.posY * 10.0D) / 10.0D,
					45 + FontManager.arrayList_Bebasneue
							.getStringWidth("X: " + (double) Math.round(mc.thePlayer.posX * 10.0D) / 10.0D) + 5,
					(int) ((double) ScaledResolution.getScaledHeight() - y + 5.0D), -1);
			FontManager.arrayList_Bebasneue.drawString("Z: " + (double) Math.round(mc.thePlayer.posZ * 10.0D) / 10.0D,
					45 + FontManager.arrayList_Bebasneue
							.getStringWidth("X: " + (double) Math.round(mc.thePlayer.posX * 10.0D) / 10.0D)
							+ FontManager.arrayList_Bebasneue.getStringWidth(
									"Y: " + (double) Math.round(mc.thePlayer.posY * 10.0D) / 10.0D)
							+ 10,
					(int) ((double) ScaledResolution.getScaledHeight() - y + 5.0D), -1);
		}

		var10000 = mc.ingameGUI;
		GuiIngame.drawRect(ScaledResolution.getScaledWidth() - 5,
				(int) ((double) ScaledResolution.getScaledHeight() - y), ScaledResolution.getScaledWidth(),
				ScaledResolution.getScaledHeight(), Colours.getColor(255, 255, 255, 255));
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		int var6 = ScaledResolution.getScaledWidth() / 2;
		var10000 = mc.ingameGUI;
		GuiIngame.drawRect(var6 - 91, (int) ((double) ScaledResolution.getScaledHeight() - y), var6 - 91 + 180,
				ScaledResolution.getScaledHeight(), Colours.getColor(0, 0, 0, 100));
		var10000 = mc.ingameGUI;
		GuiIngame.drawRect((int) ((double) (var6 - 91) + currentSlot * 20.0D),
				(int) ((double) ScaledResolution.getScaledHeight() - y),
				(int) ((double) (var6 - 91) + currentSlot * 20.0D + 20.0D), ScaledResolution.getScaledHeight(),
				Colours.getColor(255, 255, 255, 180));
		GlStateManager.enableRescaleNormal();
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		RenderHelper.enableGUIStandardItemLighting();

		for (int j = 0; j < 9; ++j) {
			int k = ScaledResolution.getScaledWidth() / 2 - 90 + j * 20 + 1;
			int l = (int) ((double) ScaledResolution.getScaledHeight() - (y - 6.0D) - 3.0D);
			mc.ingameGUI.renderHotbarItem(j, k, l, mc.timer.renderPartialTicks, mc.thePlayer);
		}

		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableRescaleNormal();
		GlStateManager.disableBlend();
	}

	static void renderModules() {
		int y = 1;
		SlickFont slickFont = null;
		String fontTheme = VSetting.getByName("ArrayList Theme", Module.getByName("Hud")).getActiveMode();
		if (fontTheme.equalsIgnoreCase("Big Noodle Titling")) {
			slickFont = FontManager.arrayList_Bignoodletitling;
		} else if (fontTheme.equalsIgnoreCase("Arial")) {
			slickFont = FontManager.arrayList_Arial;
		}

		Iterator var4 = getSorted(slickFont).iterator();

		while (var4.hasNext()) {
			Module modules = (Module) var4.next();
			if (modules.isToggled()) {
				moduleSlideMechanic(modules, slickFont);
				boolean moduleRect = VSetting.getByName("Module Background", Module.getByName("Hud")).isToggled();
				GuiIngame var10000;
				if (moduleRect) {
					var10000 = mc.ingameGUI;
					GuiIngame.drawRect(ScaledResolution.getScaledWidth() + modules.x - 1, y,
							ScaledResolution.getScaledWidth(), y + 8, Colours.getColor(0, 0, 0, 255));
					var10000 = mc.ingameGUI;
					GuiIngame.drawRect(ScaledResolution.getScaledWidth() - modules.x - 1, y,
							ScaledResolution.getScaledWidth(), y + 8, Colours.getColor(0, 0, 0, 150));
				}

				boolean littleRect = VSetting.getByName("Little Rect", Module.getByName("Hud")).isToggled();
				if (littleRect) {
					var10000 = mc.ingameGUI;
					GuiIngame.drawRect(ScaledResolution.getScaledWidth() - modules.x - 2, y,
							ScaledResolution.getScaledWidth() - modules.x, y + 8, modules.moduleColor);
				}

				int color = modules.moduleColor;
				if (slickFont == null) {
					if (VSetting.getByName("White Array", Module.getByName("Hud")).isToggled()) {
						color = Colours.getColor(255, 255, 255, 255);
					}

					mc.fontRendererObj.drawStringWithShadow(modules.getName(),
							(float) (ScaledResolution.getScaledWidth() - modules.x), (float) y, color);
					if (modules.nameAddon != null) {
						mc.fontRendererObj.drawStringWithShadow(" " + modules.nameAddon,
								(float) (ScaledResolution.getScaledWidth() - modules.x
										+ mc.fontRendererObj.getStringWidth(modules.getName())),
								(float) y, -1);
					}
				} else {
					slickFont.drawString(modules.getName(), ScaledResolution.getScaledWidth() - modules.x, y - 2,
							color);
					if (modules.nameAddon != null) {
						slickFont.drawString(" " + modules.nameAddon, ScaledResolution.getScaledWidth() - modules.x
								+ slickFont.getStringWidth(modules.getName()), y - 2, -1);
					}
				}

				y += 8;
			}
		}

	}

	static void moduleSlideMechanic(Module module, SlickFont slickFont) {
		if (slickFont == null) {
			if (module.x < mc.fontRendererObj.getStringWidth(module.displayName)) {
				++module.x;
			}

			if (module.x - 1 > mc.fontRendererObj.getStringWidth(module.displayName)) {
				--module.x;
			}
		} else {
			if (module.x < slickFont.getStringWidth(module.displayName)) {
				++module.x;
			}

			if (module.x - 1 > slickFont.getStringWidth(module.displayName)) {
				--module.x;
			}
		}

	}
}
