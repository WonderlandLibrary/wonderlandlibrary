package com.enjoytheban.module.modules.render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.enjoytheban.Client;
import com.enjoytheban.api.AALAPI;
import com.enjoytheban.api.EventHandler;
import com.enjoytheban.api.events.rendering.EventRender2D;
import com.enjoytheban.api.events.rendering.EventRenderCape;
import com.enjoytheban.api.value.Option;
import com.enjoytheban.management.FriendManager;
import com.enjoytheban.module.Module;
import com.enjoytheban.module.ModuleType;
import com.enjoytheban.module.modules.render.UI.TabUI;
import com.enjoytheban.ui.font.CFontRenderer;
import com.enjoytheban.ui.font.FontLoaders;
import com.enjoytheban.utils.Helper;
import com.enjoytheban.utils.math.RotationUtil;
import com.enjoytheban.utils.render.RenderUtil;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class HUD extends Module {

	/**
	 * The HUD for the client
	 * 
	 * @author Purity
	 */

	public TabUI tabui;

	private Option<Boolean> info = new Option("Information", "information", true);
	private Option<Boolean> rainbow = new Option("Rainbow", "rainbow", false);
	private Option<Boolean> customlogo = new Option("Logo", "logo", false);
	private Option<Boolean> customfont = new Option("Font", "font", false);
	private Option<Boolean> capes = new Option("Capes", "capes", true);

	public static boolean shouldMove;
	public static boolean useFont;

	// directions for direction hud
	private String[] directions = new String[] { "S", "SW", "W", "NW", "N", "NE", "E", "SE" };

	public HUD() {
		super("HUD", new String[] { "gui" }, ModuleType.Render);
		// gen a color
		setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)).getRGB());
		setEnabled(true);
		setRemoved(true);
		addValues(info, rainbow, customlogo, customfont, capes);
	}

	// render the hud
	@EventHandler
	private void renderHud(EventRender2D event) {
		final CFontRenderer font = FontLoaders.kiona18;
		// draw the client name + version
		if (customfont.getValue()) {
			useFont = true;
		} else if (!customfont.getValue()) {
			useFont = false;
		}
		if (!mc.gameSettings.showDebugInfo) {
			if(Helper.onServer("enjoytheban")) {
				boolean cheating = true;
				if(useFont) {
					int ychat = mc.ingameGUI.getChatGUI().getChatOpen() ? 14 : 0;
					font.drawStringWithShadow("Username:§f " + AALAPI.getUsername(), new ScaledResolution(mc).getScaledWidth() - font.getStringWidth("Username: " + AALAPI.getUsername()) - 2 , new ScaledResolution(mc).getScaledHeight() - 20 - ychat, new Color(102, 172, 255, 1).getRGB());
					font.drawStringWithShadow("ETB Battle Royale:§f " + (cheating ? "Cheating":"Legit"), new ScaledResolution(mc).getScaledWidth() - font.getStringWidth("ETB Battle Royale: " + (cheating ? "Cheating":"Legit")) - 2 , new ScaledResolution(mc).getScaledHeight() - 10 - ychat, new Color(102, 172, 255, 1).getRGB());
				} else {
					int ychat = mc.ingameGUI.getChatGUI().getChatOpen() ? 14 : 0;
					mc.fontRendererObj.drawStringWithShadow("Username:§f " + AALAPI.getUsername(), new ScaledResolution(mc).getScaledWidth() - mc.fontRendererObj.getStringWidth("Username: " + AALAPI.getUsername()) - 2 , new ScaledResolution(mc).getScaledHeight() - 20 - ychat, new Color(102, 172, 255, 1).getRGB());
					mc.fontRendererObj.drawStringWithShadow("ETB Battle Royale:§f " + (cheating ? "Cheating":"Legit"), new ScaledResolution(mc).getScaledWidth() - mc.fontRendererObj.getStringWidth("ETB Battle Royale: " + (cheating ? "Cheating":"Legit")) - 2 , new ScaledResolution(mc).getScaledHeight() - 10 - ychat, new Color(102, 172, 255, 1).getRGB());
				}
			}
			if (customlogo.getValue()) {
				shouldMove = true;
				GlStateManager.enableAlpha();
				GlStateManager.enableBlend();
				RenderUtil.drawCustomImage(-4.0, -4.0, 35.0, 33.0, new ResourceLocation("ETB/logo.png"));
				GlStateManager.disableAlpha();
				GlStateManager.disableBlend();
				if (useFont) {
					font.drawStringWithShadow("" + EnumChatFormatting.GRAY + Client.instance.version,
							font.getStringWidth(Client.instance.name + " " + Client.instance.version) - 9,
							shouldMove ? 15 : 2, new Color(102, 172, 255).getRGB());
				} else {
					mc.fontRendererObj.drawStringWithShadow("" + EnumChatFormatting.GRAY + Client.instance.version,
							mc.fontRendererObj.getStringWidth(Client.instance.name + " " + Client.instance.version)
									- 12,
							shouldMove ? 15 : 2, new Color(102, 172, 255).getRGB());
				}
			} else if (!customlogo.getValue()) {
				shouldMove = false;
				if (useFont) {
					font.drawStringWithShadow(
							Client.instance.name + " " + EnumChatFormatting.GRAY + Client.instance.version, 2, 2,
							new Color(102, 172, 255).getRGB());
				} else {
					mc.fontRendererObj.drawStringWithShadow(
							Client.instance.name + " " + EnumChatFormatting.GRAY + Client.instance.version, 2, 2,
							new Color(102, 172, 255).getRGB());
				}
			}

			// Create a array for modules to be sorted
			List<Module> sorted = new ArrayList<Module>();

			// get the mod00l
			for (Module m : Client.instance.getModuleManager().getModules()) {
				// if it is enabled ignore it
				if (!m.isEnabled()) {
					continue;
				}
				if (m.wasRemoved()) {
					continue;
				}
				// add it to the array
				sorted.add(m);
			}

			// do the sorting ting
			if (useFont) {
				sorted.sort((o1,
						o2) -> font
								.getStringWidth(o2.getSuffix().isEmpty() ? o2.getName()
										: String.format("%s %s", o2.getName(), o2.getSuffix()))
								- font.getStringWidth(o1.getSuffix().isEmpty() ? o1.getName()
										: String.format("%s %s", o1.getName(), o1.getSuffix())));
			} else {
				sorted.sort((o1,
						o2) -> mc.fontRendererObj.getStringWidth(o2.getSuffix().isEmpty() ? o2.getName()
								: String.format("%s %s", o2.getName(), o2.getSuffix()))
								- mc.fontRendererObj.getStringWidth(o1.getSuffix().isEmpty() ? o1.getName()
										: String.format("%s %s", o1.getName(), o1.getSuffix())));
			}

			// Sets the Y value to 1
			int y = 1;

			int rainbowTick = 0;
			// gets the module name from the sorting tink
			if (useFont) {
				for (Module m : sorted) {
					String name = m.getSuffix().isEmpty() ? m.getName()
							: String.format("%s %s", m.getName(), m.getSuffix());
					float x = RenderUtil.width() - font.getStringWidth(name);
					Color rainbow = new Color(Color.HSBtoRGB(
							(float) (mc.thePlayer.ticksExisted / 50.0D + Math.sin(rainbowTick / 50.0D * 1.6)) % 1.0F,
							0.5F, 1.0F));
					// draw the module string
					font.drawStringWithShadow(name, x - 3, y + 1,
							this.rainbow.getValue() ? rainbow.getRGB() : m.getColor());
					rainbowTick = (rainbowTick + 1);
					if (rainbowTick > 50) {
						rainbowTick = 0;
					}
					// increment the y value for each module
					y += 9;

				}
			} else {
				for (Module m : sorted) {
					String name = m.getSuffix().isEmpty() ? m.getName()
							: String.format("%s %s", m.getName(), m.getSuffix());
					float x = RenderUtil.width() - mc.fontRendererObj.getStringWidth(name);
					Color rainbow = new Color(Color.HSBtoRGB(
							(float) (mc.thePlayer.ticksExisted / 50.0D + Math.sin(rainbowTick / 50.0D * 1.6)) % 1.0F,
							0.5F, 1.0F));
					// draw the module string
					mc.fontRendererObj.drawStringWithShadow(name, x - 2, y,
							this.rainbow.getValue() ? rainbow.getRGB() : m.getColor());
					rainbowTick = (rainbowTick + 1);
					if (rainbowTick > 50) {
						rainbowTick = 0;
					}
					// increment the y value for each module
					y += 9;

				}
			}
			// now lets do the info hud
			// sets the text we're going to write
			String text = EnumChatFormatting.GRAY + "X" + EnumChatFormatting.WHITE + ": "
					+ MathHelper.floor_double(mc.thePlayer.posX) + " " + EnumChatFormatting.GRAY + "Y"
					+ EnumChatFormatting.WHITE + ": " + MathHelper.floor_double(mc.thePlayer.posY) + " "
					+ EnumChatFormatting.GRAY + "Z" + EnumChatFormatting.WHITE + ": "
					+ MathHelper.floor_double(mc.thePlayer.posZ);
			// render string
			// variable for ychat
			if (useFont) {
				int ychat = mc.ingameGUI.getChatGUI().getChatOpen() ? 24 : 10;
				if (info.getValue()) {
					font.drawStringWithShadow(text, 4, new ScaledResolution(mc).getScaledHeight() - ychat,
							new Color(11, 12, 17).getRGB());
					font.drawStringWithShadow(
							EnumChatFormatting.GRAY + "FPS: " + EnumChatFormatting.WHITE + mc.debugFPS, 2,
							shouldMove ? 90 : 75, -1);
					drawPotionStatus(new ScaledResolution(mc));
					String direction = directions[RotationUtil.wrapAngleToDirection(mc.thePlayer.rotationYaw,
							directions.length)];
					font.drawStringWithShadow("[" + direction + "]",
							font.getStringWidth(Client.instance.name + " " + Client.instance.version + 10),
							shouldMove ? 15 : 2, new Color(102, 172, 255).getRGB());
				}
			} else {
				int ychat = mc.ingameGUI.getChatGUI().getChatOpen() ? 25 : 10;
				if (info.getValue()) {
					mc.fontRendererObj.drawStringWithShadow(text, 4, new ScaledResolution(mc).getScaledHeight() - ychat,
							new Color(11, 12, 17).getRGB());
					mc.fontRendererObj.drawStringWithShadow(
							EnumChatFormatting.GRAY + "FPS: " + EnumChatFormatting.WHITE + mc.debugFPS, 2,
							shouldMove ? 90 : 75, -1);
					drawPotionStatus(new ScaledResolution(mc));
					String direction = directions[RotationUtil.wrapAngleToDirection(mc.thePlayer.rotationYaw,
							directions.length)];
					mc.fontRendererObj.drawStringWithShadow("[" + direction + "]",
							mc.fontRendererObj.getStringWidth(Client.instance.name + " " + Client.instance.version + 2),
							shouldMove ? 15 : 2, new Color(102, 172, 255).getRGB());
				}
			}
		}
	}

	private void drawPotionStatus(ScaledResolution sr) {
		final CFontRenderer font = FontLoaders.kiona18;
		int y = 0;
		for (final PotionEffect effect : (Collection<PotionEffect>) this.mc.thePlayer.getActivePotionEffects()) {
			Potion potion = Potion.potionTypes[effect.getPotionID()];
			String PType = I18n.format(potion.getName());
			switch (effect.getAmplifier()) {
			case 1:
				PType = PType + " II";
				break;
			case 2:
				PType = PType + " III";
				break;
			case 3:
				PType = PType + " IV";
				break;
			default:
				break;
			}
			if (effect.getDuration() < 600 && effect.getDuration() > 300) {
				PType = PType + "\2477:\2476 " + Potion.getDurationString(effect);
			} else if (effect.getDuration() < 300) {
				PType = PType + "\2477:\247c " + Potion.getDurationString(effect);
			} else if (effect.getDuration() > 600) {
				PType = PType + "\2477:\2477 " + Potion.getDurationString(effect);
			}
			int ychat = mc.ingameGUI.getChatGUI().getChatOpen() ? 5 : -10;
			if (useFont) {
				font.drawStringWithShadow(PType, sr.getScaledWidth() - font.getStringWidth(PType) - 2,
						sr.getScaledHeight() - font.getHeight() + y - 12 - ychat, potion.getLiquidColor());
			} else {
				mc.fontRendererObj.drawStringWithShadow(PType,
						sr.getScaledWidth() - mc.fontRendererObj.getStringWidth(PType) - 2,
						sr.getScaledHeight() - mc.fontRendererObj.FONT_HEIGHT + y - 12 - ychat,
						potion.getLiquidColor());
			}
			y -= 10;
		}
	}

	@EventHandler
	public void onRender(EventRenderCape event) {
		if (capes.getValue()) {
			if (mc.theWorld != null && FriendManager.isFriend(event.getPlayer().getName())) {
				event.setLocation(Client.CLIENT_CAPE);
				event.setCancelled(true);
			}
		}
	}
}