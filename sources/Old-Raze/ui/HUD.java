	package markgg.ui;

import java.awt.Color;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;

import markgg.modules.impl.render.HUD2;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import markgg.RazeClient;
import markgg.modules.Module;
import markgg.settings.ModeSetting;
import markgg.settings.Setting;
import markgg.util.MathUtil;
import markgg.util.others.RandomUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;

public class HUD {

	public Minecraft mc = Minecraft.getMinecraft();

	public void draw() {
		ScaledResolution sr = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
		FontRenderer fr = mc.fontRendererObj;

		int primaryColor = 0xFFE44964;
		int whiteColor = -1;
		int settingColor = -1;

		int scaledHeight = sr.getScaledHeight();
		int xPos = 2;
		int yPos = scaledHeight - (mc.fontRendererObj.FONT_HEIGHT + 7);
		float squareMotion = (float)(MathUtil.square(mc.thePlayer.motionX) + MathUtil.square(mc.thePlayer.motionZ));
		float bps = (float)MathUtil.round(Math.sqrt(squareMotion) * 20.0D * mc.timer.timerSpeed, 2.0D);
		float xCord = (float)MathUtil.round(mc.thePlayer.posX, 0.0D);
		float yCord = (float)MathUtil.round(mc.thePlayer.posY, 0.0D);
		float zCord = (float)MathUtil.round(mc.thePlayer.posZ, 0.0D);

		if (RazeClient.getModuleManager().getModule(HUD2.class).isEnabled()) {
			double offset1 = (fr.FONT_HEIGHT + 6);

			GlStateManager.pushMatrix();
			GlStateManager.translate(4.0F, 4.0F, 0.0F);
			GlStateManager.scale(1.0F, 1.0F, 1.0F);
			if(RazeClient.getModuleManager().getModule(HUD2.class).BPS.getValue()) {
				fr.drawStringWithShadow(!RazeClient.getModuleManager().getModule(HUD2.class).lowCase.getValue() ? "BPS: " + EnumChatFormatting.WHITE
						+ bps : ("BPS: " + EnumChatFormatting.WHITE + bps).toLowerCase(Locale.ROOT), xPos, yPos - 10, primaryColor);
			}
			if(RazeClient.getModuleManager().getModule(HUD2.class).FPS.getValue()) {
				fr.drawStringWithShadow(!RazeClient.getModuleManager().getModule(HUD2.class).lowCase.getValue() ? "FPS: " + EnumChatFormatting.WHITE
						+ mc.getDebugFPS() : ("FPS: " + EnumChatFormatting.WHITE + mc.getDebugFPS()).toLowerCase(Locale.ROOT), xPos, yPos - 20, primaryColor);
			}
			if(RazeClient.getModuleManager().getModule(HUD2.class).coordinates.getValue()) {
				fr.drawStringWithShadow(!RazeClient.getModuleManager().getModule(HUD2.class).lowCase.getValue() ?
						"XYZ: " + EnumChatFormatting.WHITE + xCord + " | " + yCord + " | " + zCord :
						("XYZ: " + EnumChatFormatting.WHITE + xCord + " | " + yCord + " | " + zCord).toLowerCase(Locale.ROOT), xPos, yPos, primaryColor);
			}
			if(RazeClient.getModuleManager().getModule(HUD2.class).waterMark.getValue()) {
				switch(RazeClient.getModuleManager().getModule(HUD2.class).markMode.getMode()) {
					case "Classic":
						GlStateManager.translate(-4.0F, -4.0F, 0.0F);
						Gui.drawRect(116, 2, 6, 16, 0);
						Gui.drawRect(116, 1, 6, 2, 0);
						GlStateManager.scale(0.7F, 0.7F, 0.7F);
						fr.drawStringWithShadow(RazeClient.INSTANCE.getVersion(), 8, 32, primaryColor);
						GlStateManager.scale(3F, 3F, 3F);
						fr.drawStringWithShadow("R", 2, 2, primaryColor);
						fr.drawStringWithShadow("aze", 8, 2, -1);
						break;
					case "Sense":
						GlStateManager.translate(1F, 1F, 1F);
						Gui.drawRect(-5, 14, 102 + fr.getStringWidth(" " + mc.getSession().getUsername()+ " - " + mc.getDebugFPS() + " fps") - 35, -5, 0xFF3B3B3B);
						Gui.drawRect(-5, 13, 102 + fr.getStringWidth(" " + mc.getSession().getUsername()+ " - " + mc.getDebugFPS() + " fps") - 35, -4, 0xFF292727);
						Gui.drawRect(-3, 12, 100 + fr.getStringWidth(" " + mc.getSession().getUsername()+ " - " + mc.getDebugFPS() + " fps") - 35, -3, 0xFF171717);
						Gui.drawRect(-3, 12, 100 + fr.getStringWidth(" " + mc.getSession().getUsername() + " - " + mc.getDebugFPS() + " fps") - 35, 11, primaryColor);
						if (RazeClient.getModuleManager().getModule(HUD2.class).lowCase.getValue())
							fr.drawStringWithShadow(EnumChatFormatting.WHITE + "raze" + EnumChatFormatting.RESET + "sense" + EnumChatFormatting.WHITE + " - " + mc.getSession().getUsername().toLowerCase() + " - " + mc.getDebugFPS() + " fps", 0, 0, primaryColor);
						else
							fr.drawStringWithShadow(EnumChatFormatting.WHITE + "raze" + EnumChatFormatting.RESET + "sense" + EnumChatFormatting.WHITE +  " - " + mc.getSession().getUsername() + " - " + mc.getDebugFPS() + " fps", 0, 0, primaryColor);

						break;
					case "Simple":
						GlStateManager.translate(-4.0F, -4.0F, 0.0F);
						GlStateManager.scale(1F, 1F, 1F);
						String title = Display.getTitle();
						if (RazeClient.getModuleManager().getModule(HUD2.class).lowCase.getValue())
							title = title.toLowerCase();
						String firstLetter = Character.toString(title.charAt(0)),restOfTitle = title.substring(1);
						fr.drawStringWithShadow(firstLetter, 2, 2, primaryColor);
						fr.drawStringWithShadow(restOfTitle, 2 + fr.getStringWidth(firstLetter), 2, -1);
						break;
					case "LGBT":
						GlStateManager.translate(-8.0F, -8.0F, 0.0F);
						Gui.drawRect(116, 2, 6, 16, 0);
						Gui.drawRect(116, 1, 6, 2, 0);
						GlStateManager.scale(0.7F, 0.7F, 0.7F);
						GlStateManager.scale(3F, 3F, 3F);
						fr.drawStringWithShadow("L", 2, 2, new Color(227, 2, 2).getRGB());
						fr.drawStringWithShadow("G", 7, 2, new Color(254, 140, 0).getRGB());
						fr.drawStringWithShadow("B", 13, 2, new Color(254, 237, 0).getRGB());
						fr.drawStringWithShadow("T", 18, 2, new Color(0, 128, 36).getRGB());
						fr.drawStringWithShadow("Q", 23, 2, new Color(0, 76, 254).getRGB());
						fr.drawStringWithShadow("+", 29, 2, new Color(117, 6, 134).getRGB());
						break;
				}
			}

			GlStateManager.popMatrix();

			int count = 0;
			if(RazeClient.getModuleManager().getModule(HUD2.class).moduleList.getValue()) {
				for (Module m : RazeClient.getModuleManager().getModules().values()) {
					if (!m.toggled || m.name.equals("Cape"))
						continue;

					switch(RazeClient.getModuleManager().getModule(HUD2.class).moduleMode.getMode()) {
						case "Raze":

							String nameMode = m.name;

							boolean hasDisplayedModeSetting1 = false;

							int rightPosition = sr.getScaledWidth();
							int rectangleWidth = fr.getStringWidth(nameMode) + 1;
							int rectangleHeight = 6 + fr.FONT_HEIGHT;

							double offset = (count * (fr.FONT_HEIGHT + 6));

							for (Setting setting : m.settings) {
								if (setting instanceof ModeSetting) {
									if (!hasDisplayedModeSetting1) {
										String mode = ((ModeSetting) setting).getMode();
										nameMode = m.name;
										if (RazeClient.getModuleManager().getModule(HUD2.class).lowCase.getValue()) {
											nameMode = m.name.toLowerCase();
											mode = ((ModeSetting) setting).getMode().toLowerCase();
										}
										Gui.drawRect((sr.getScaledWidth() - fr.getStringWidth(nameMode + " " + mode) - 8), offset, sr.getScaledWidth(), (6 + fr.FONT_HEIGHT) + offset, -1879048192);
										fr.drawStringWithShadow(nameMode + EnumChatFormatting.GRAY + " " + mode, (sr.getScaledWidth() - fr.getStringWidth(nameMode + " " + mode) - 4), 4.0D + offset, settingColor);
										hasDisplayedModeSetting1 = true;
									}
								}
							}
							if (!hasDisplayedModeSetting1) {
								nameMode = m.name;
								if (RazeClient.getModuleManager().getModule(HUD2.class).lowCase.getValue())
									nameMode = m.name.toLowerCase();
								Gui.drawRect((sr.getScaledWidth() - fr.getStringWidth(nameMode) - 8), offset, sr.getScaledWidth(), (6 + fr.FONT_HEIGHT) + offset, -1879048192);
								fr.drawStringWithShadow(nameMode, (sr.getScaledWidth() - fr.getStringWidth(nameMode) - 4), 4.0D + offset, settingColor);
							}
							Gui.drawRect(rightPosition - 1, offset, rightPosition, offset + rectangleHeight, primaryColor);
							count++;
							break;
						case "Simple":
							double offset2 = (count * (fr.FONT_HEIGHT + 2));
							boolean hasDisplayedModeSetting = false;
							
							for (Setting setting : m.settings) {
								
								if (setting instanceof ModeSetting) {
									
									if (!hasDisplayedModeSetting) {
										
										String mode = ((ModeSetting) setting).getMode();
										String nameMode1 = m.name;
										
										if (RazeClient.getModuleManager().getModule(HUD2.class).lowCase.getValue()) {
											nameMode1 = m.name.toLowerCase();
											mode = ((ModeSetting) setting).getMode().toLowerCase();
										}

										fr.drawStringWithShadow(
												nameMode1 + EnumChatFormatting.GRAY + " " + mode,
												(sr.getScaledWidth() - fr.getStringWidth(nameMode1 + " " + mode) - 4),
												4D + offset2,
												settingColor
												);
										
										hasDisplayedModeSetting = true;
									}
								}
							}
							
							if (!hasDisplayedModeSetting) {
								
								String nameMode1 = m.name;
								
								if (RazeClient.getModuleManager().getModule(HUD2.class).lowCase.getValue())
									nameMode1 = m.name.toLowerCase();
								
								fr.drawStringWithShadow(nameMode1, (sr.getScaledWidth() - fr.getStringWidth(nameMode1) - 4), 4D + offset2, settingColor);
							}
							
							count++;
							break;
					}
				}
			}
		}

	}
}