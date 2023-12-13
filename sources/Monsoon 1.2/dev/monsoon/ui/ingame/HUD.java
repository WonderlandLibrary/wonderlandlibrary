package dev.monsoon.ui.ingame;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

import dev.monsoon.Monsoon;
import dev.monsoon.event.listeners.EventRenderGUI;
import dev.monsoon.event.EventManager;
import dev.monsoon.module.base.Module;
import dev.monsoon.notification.NotificationManager;
import dev.monsoon.util.font.GloriFontRenderer;
import dev.monsoon.util.render.AnimationUtil;
import dev.monsoon.util.misc.MathUtils;
import dev.monsoon.util.render.ColorUtil;
import dev.monsoon.util.render.DrawUtil;
import dev.monsoon.util.misc.Timer;
import dev.monsoon.util.world.Timeutil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class HUD {

	Timer timer = new Timer();
	private int healthColor;
	private double healthRect;
	private String winning;

	public Minecraft mc = Minecraft.getMinecraft();
	private final FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;

	//private double x = (int) Monsoon.targethud.targetX.getValue();
	//private double y = (int) Monsoon.targethud.targetY.getValue();


	public static class ModuleComparator implements Comparator<Module> {
		private final FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
		@Override
		public int compare(Module arg0, Module arg1) {
			if(fr.getStringWidth(arg0.getDisplayname()) > fr.getStringWidth(arg1.getDisplayname())) {
				return -1;
			}
			if(fr.getStringWidth(arg0.getDisplayname()) < fr.getStringWidth(arg1.getDisplayname())) {
				return 1;
			}
			return 0;
		}
	}
	public void draw() {
		ScaledResolution sr = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
		//FontRenderer fr = mc.fontRendererObj;
		//FontRenderer2 frbig = new FontRenderer2("Roboto", Font.BOLD, 34);
		//FontRenderer2 fr = new FontRenderer2("Roboto", Font.PLAIN, 19);

		float huehud = (System.currentTimeMillis() % 3500) / 3500f;
		int color = 0;
		int watermarkColor = 0;
		int color2 = 0x90000000;

		//renderTargetHud();

		Collections.sort(Monsoon.modules, new ModuleComparator());

		if(Monsoon.arraylist.color.is("Colorful")) {
			color = ColorUtil.getRGB(4, 0.9f, 1, 10000);
		}
		if(Monsoon.arraylist.color.is("Red")) {
			color = 0xff991010;
		}
		if(Monsoon.arraylist.color.is("Blue")) {
			color = new Color(0, 170, 255, 255).getRGB();
		}
		if(Monsoon.arraylist.color.is("Orange")) {
			color = 0xffeb6517;
		}
		if(Monsoon.arraylist.color.is("Green")) {
			color = 0xff30eb17;
		}
		if(Monsoon.arraylist.color.is("Green")) {
			color = 0xff30eb17;
		}
		if(Monsoon.arraylist.color.is("Black")) {
			color = 0xffffffff;
		}
		if(Monsoon.arraylist.color.is("Discord")) {
			color = new Color(114, 137, 218, 255).getRGB();
			color2 = new Color(47, 49, 54, 255).getRGB();
		}
		if(Monsoon.arraylist.color.is("White")) {
			color = -1;
		}
		if(Monsoon.arraylist.color.is("Purple")) {
			color = new Color(237, 0, 228).getRGB();
		}
		if(Monsoon.arraylist.color.is("Astolfo")) {
			color = ColorUtil.astolfoColors(10, 14);
		}


		float bruh = (float) (MathUtils.square(mc.thePlayer.motionX) + MathUtils.square(mc.thePlayer.motionZ));
		float bps = (float) MathUtils.round((Math.sqrt(bruh) * 20) * mc.timer.timerSpeed, 2);
		String server = "Singleplayer";

		if(!mc.isSingleplayer()) {
			server = mc.getCurrentServerData().serverIP;
		} else {
			server = "Singleplayer";
		}

		if(Monsoon.arraylist.watermarkmode.is("Monsoon")) {
			String watermark = "Monsoon " + Monsoon.version + " | " + mc.debugFPS + " FPS | " + Monsoon.monsoonUsername;

			Gui.drawRect(2,2,fr.getStringWidth(watermark) + 9, 19.5f,new Color(20, 20, 20, 255).getRGB());
			Gui.drawRect(4, 4, fr.getStringWidth(watermark) + 7, 17.5f, new Color(30, 30, 30, 255).getRGB());
			Gui.drawRect(4, 4, fr.getStringWidth(watermark) + 7, 5, color);
			fr.drawStringWithShadow(watermark, 6, 8, -1); //0xff0090ff
			EntityLivingBase target = Monsoon.killAura.target;

		}
		if(Monsoon.arraylist.watermarkmode.is("ZeroDay")) {
			mc.fontRendererObj.drawString("b21", 72, 45, -1);
			mc.getTextureManager().bindTexture(new ResourceLocation("monsoon/zeroday.png"));
			Gui.drawScaledCustomSizeModalRect(5, 5, 50, 50, 50, 50, 70, 50, 50, 50);
		}
		if(Monsoon.arraylist.watermarkmode.is("Plain")) {
			fr.drawStringWithShadow("M" + EnumChatFormatting.WHITE + "onsoon" + EnumChatFormatting.DARK_GRAY +  " [" + EnumChatFormatting.WHITE + mc.debugFPS + " FPS" +  EnumChatFormatting.DARK_GRAY +  "]", 3, 3, color);
		}

		if(Monsoon.arraylist.watermarkmode.is("Off")) {

		}

		if(Monsoon.manager.targethud.mode.is("Monsoon")) {
			renderTargetHud();
		} else if(Monsoon.manager.targethud.mode.is("Exhi")) {
			Monsoon.manager.targethud.renderExhi();
		}


		if(!mc.gameSettings.showDebugInfo && !(mc.currentScreen instanceof GuiChat) && Monsoon.arraylist.info.isEnabled()) {
			fr.drawStringWithShadow("BPS: " + EnumChatFormatting.WHITE + bps, 2, sr.getScaledHeight() - fr.getHeight() * 2, color);
        /*if(Minecraft.getMinecraft().thePlayer != null) {
           fr.drawStringWithShadow("Ping: " + EnumChatFormatting.WHITE + Minecraft.getMinecraft().getNetHandler().func_175102_a(Minecraft.getMinecraft().thePlayer.getUniqueID()).getResponseTime(), 4 + fr.getStringWidth("BPS: " + bps), sr.getScaledHeight() - fr.FONT_HEIGHT * 2, color);
        } else {
           fr.drawStringWithShadow("Ping: " + EnumChatFormatting.WHITE + "error", 4 + fr.getStringWidth("BPS: " + bps), sr.getScaledHeight() - fr.FONT_HEIGHT * 2, color);
        }*/

			fr.drawStringWithShadow("X: " + EnumChatFormatting.WHITE + (int) mc.thePlayer.posX, 2, sr.getScaledHeight() - fr.getHeight(), color);
			fr.drawStringWithShadow("Y: " + EnumChatFormatting.WHITE + (int) mc.thePlayer.posY, 4 + fr.getStringWidth("X: " + EnumChatFormatting.WHITE + (int) mc.thePlayer.posX), sr.getScaledHeight() - fr.getHeight(), color);
			fr.drawStringWithShadow("Z: " + EnumChatFormatting.WHITE + (int) mc.thePlayer.posZ, 8 + fr.getStringWidth("X: " + EnumChatFormatting.WHITE + (int) mc.thePlayer.posX + "Y: " + EnumChatFormatting.WHITE + (int) mc.thePlayer.posY), sr.getScaledHeight() - fr.getHeight(), color);

			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
			fr.drawStringWithShadow(formatter.format(calendar.getTime()), sr.getScaledWidth() - fr.getStringWidth(formatter.format(calendar.getTime())) - 2, sr.getScaledHeight() - fr.getHeight(), -1);
			//fr.drawStringWithShadow(server, sr.getScaledWidth() - fr.getStringWidth(server) - 1, sr.getScaledHeight() - fr.FONT_HEIGHT * 2, color);

		}
		int count = 0;

		if(Monsoon.arraylist.hello.is("Normal")) {
			renderUser();
		}

     /*if(Scaffold.isEnabled) {
          int blockCount = Scaffold.getBlockCount();
          Color blockcolor = new Color(0, 255, 0);
          if (Scaffold.getBlockCount() > 128) {
             blockcolor = new Color(0, 255, 0);
          }
          if (Scaffold.getBlockCount() < 128) {
             blockcolor = new Color(255, 255, 0);
          }
          if (Scaffold.getBlockCount() < 64) {
             blockcolor = new Color(255, 0, 0);
          }
          if(mc.thePlayer.getHeldItem() != null) {
             mc.fontRendererObj.drawStringWithShadow(blockCount + "", (sr.getScaledWidth() / 2 - -20) - mc.fontRendererObj.getStringWidth(blockCount + "") / 2, (sr.getScaledHeight() / 2 + 30) -17, blockcolor.getRGB());
             mc.getRenderItem().func_175042_a(mc.thePlayer.getHeldItem(), (sr.getScaledWidth() / 2 - 7), (sr.getScaledHeight() / 2 + 30) + -21);
          }
     }*/

		if(Monsoon.manager.cheststealer.isEnabled() && mc.currentScreen instanceof GuiChest) {
			fr.drawStringWithShadow("Stealing chest", sr.getScaledWidth() / 2 - fr.getStringWidth("Stealing chest") / 2, sr.getScaledHeight() / 3, -1);
		}

		for(Module m : Monsoon.modules) {
			if(!m.enabled)
				continue;

			double offset = count*(fr.getHeight() + 3);

			if(Monsoon.arraylist.color.is("Colorful")) {
				color = Color.HSBtoRGB(huehud, 1, 1);
			}
			if(Monsoon.arraylist.color.is("Red")) {
				color = 0xff991010;
			}
			if(Monsoon.arraylist.color.is("Blue")) {
				color = new Color(0, 170, 255, 255).getRGB();
			}
			if(Monsoon.arraylist.color.is("Orange")) {
				color = 0xffeb6517;
			}
			if(Monsoon.arraylist.color.is("Green")) {
				color = 0xff30eb17;
			}
			if(Monsoon.arraylist.color.is("Green")) {
				color = 0xff30eb17;
			}
			if(Monsoon.arraylist.color.is("Black")) {
				color = 0xffffffff;
			}
			if(Monsoon.arraylist.color.is("Discord")) {
				color = new Color(114, 137, 218, 255).getRGB();
				color2 = new Color(47, 49, 54, 255).getRGB();
			}
			if(Monsoon.arraylist.color.is("White")) {
				color = -1;
			}
			if(Monsoon.arraylist.color.is("Purple")) {
				color = new Color(237, 0, 228).getRGB();
			}
			if(Monsoon.arraylist.color.is("Astolfo")) {
				color = ColorUtil.astolfoColors(10, count * 14);
			}

			Color arryBgColor = new Color(0,0,0, 0);
			//ColorUtil.changeAlpha(arryBgColor, (float) Monsoon.arraylist.arrayBg.getValue());

			if(Monsoon.arraylist.arrayPos.is("Top Right")) {
				//Gui.drawRect(sr.getScaledWidth() - 2, offset, sr.getScaledWidth(), 6 + fr.getHeight() + offset, color);
				//Gui.drawRect(sr.getScaledWidth() - fr.getStringWidth(m.getDisplayname()) - 8, offset, sr.getScaledWidth(), 6 + fr.getHeight() + offset, arryBgColor.getRGB());
				fr.drawStringWithShadow(m.getDisplayname(), sr.getScaledWidth() - fr.getStringWidth(m.getDisplayname()) - 2, (float) (4 + offset), color);
			}

			if(Monsoon.arraylist.arrayPos.is("Top Left")) {
				if(Monsoon.arraylist.watermarkmode.is("Monsoon")) {
					//Gui.drawRect(sr.getScaledWidth() - 2, offset, sr.getScaledWidth(), 6 + fr.getHeight() + offset, color);
					//Gui.drawRect(sr.getScaledWidth() - fr.getStringWidth(m.getDisplayname()) - 8, offset, sr.getScaledWidth(), 6 + fr.getHeight() + offset, arryBgColor.getRGB());
					fr.drawStringWithShadow(m.getDisplayname(), 6, (float) (19 + offset), color);
				}
				if(Monsoon.arraylist.watermarkmode.is("ZeroDay")) {
					//Gui.drawRect(sr.getScaledWidth() - 2, offset, sr.getScaledWidth(), 6 + fr.getHeight() + offset, color);
					//Gui.drawRect(sr.getScaledWidth() - fr.getStringWidth(m.getDisplayname()) - 8, offset, sr.getScaledWidth(), 6 + fr.getHeight() + offset, arryBgColor.getRGB());
					fr.drawStringWithShadow(m.getDisplayname(), 6, (float) (70 + offset), color);
				}
				if(Monsoon.arraylist.watermarkmode.is("Plain")) {
					//Gui.drawRect(sr.getScaledWidth() - 2, offset, sr.getScaledWidth(), 6 + fr.getHeight() + offset, color);
					//Gui.drawRect(sr.getScaledWidth() - fr.getStringWidth(m.getDisplayname()) - 8, offset, sr.getScaledWidth(), 6 + fr.getHeight() + offset, arryBgColor.getRGB());
					fr.drawStringWithShadow(m.getDisplayname(), 6, (float) (20 + offset), color);
				}
			}

			count++;
		}

		EventManager.call(new EventRenderGUI(sr, bps, count, count));

	}

	public void renderTargetHud() {
		EntityLivingBase target = Monsoon.killAura.target;

		if (Monsoon.killAura.target instanceof EntityLivingBase && !Monsoon.killAura.target.isDead && mc.thePlayer.getDistanceToEntity(Monsoon.killAura.target) < Monsoon.killAura.range.getValue() + 2 && Monsoon.killAura.isEnabled()) {

			Gui.drawRect(getAbsoluteX(), getAbsoluteY(), getAbsoluteX() + 160, getAbsoluteY() + 35, new Color(20, 20, 20, 190).getRGB());

			DrawUtil.setColor(-1);
			GuiInventory.drawEntityOnScreen(getAbsoluteX() + 10, getAbsoluteY() + 30, 25, -40, -5, target);

			fr.drawStringWithShadow(target.getName(), getWidth() - fr.getStringWidth(target.getName()) - 2, getAbsoluteY() + 3, -1);
			fr.drawStringWithShadow(getWin(), getWidth() - fr.getStringWidth(getWin()) - 2, getAbsoluteY() + 23, -1);
			fr.drawStringWithShadow((int) target.getHealth() + " \u2764", getWidth() -fr.getStringWidth((int) target.getHealth() + " \u2764") - 2, getAbsoluteY() + 13, -1);

           /*if(target.getHeldItem() != null)     mc.getRenderItem().func_175042_a(target.getHeldItem(), getAbsoluteX() + 30, getAbsoluteY() + 13);
           if(target.getCurrentArmor(3) != null) mc.getRenderItem().func_175042_a(target.getCurrentArmor(3), getAbsoluteX() + 45, getAbsoluteY() + 13);
           if(target.getCurrentArmor(2) != null) mc.getRenderItem().func_175042_a(target.getCurrentArmor(2), getAbsoluteX() + 60, getAbsoluteY() + 13);
           if(target.getCurrentArmor(1) != null) mc.getRenderItem().func_175042_a(target.getCurrentArmor(1), getAbsoluteX() + 75, getAbsoluteY() + 13);
           if(target.getCurrentArmor(0) != null) mc.getRenderItem().func_175042_a(target.getCurrentArmor(0), getAbsoluteX() + 90, getAbsoluteY() + 13);*/

			drawEntityHealth();
		}
	}

	private String getWin() {
		EntityLivingBase currentTarget = Monsoon.killAura.target;
		if (currentTarget.getHealth() > mc.thePlayer.getHealth()) {
			return "You are Losing";
		} else if (currentTarget.getHealth() == mc.thePlayer.getHealth() && currentTarget.getHealth() != 20 && mc.thePlayer.getHealth() != 20) {
			return "You may win";
		} else if (currentTarget.getHealth() < mc.thePlayer.getHealth()) {
			return "You are Winning";
		} else if (currentTarget.getHealth() == 0) {
			return "You won!";
		} else if (mc.thePlayer.getHealth() == 0) {
			return "You lost!";
		} else if (currentTarget.getHealth() == 20 && mc.thePlayer.getHealth() == 20) {
			return "Not fighting";
		} else {
			return "You May Win";
		}
	}

	private void drawEntityHealth() {
		EntityLivingBase target = (EntityLivingBase) Monsoon.killAura.target;
		double healthBarWidth;
		float health = target.getHealth();
		double hpPercentage = health / target.getMaxHealth();
		hpPercentage = MathHelper.clamp_double(hpPercentage, 0.0, 1.0);
		final double hpWidth = 160.0 * hpPercentage;
		healthRect = AnimationUtil.INSTANCE.animate(hpWidth, healthRect, 0.1);

		int color = new Color(0,170,255).getRGB();
		if(Monsoon.arraylist.color.is("Colorful")) {
			color = ColorUtil.getRGB(11, 1, 1, 100);
		}
		if(Monsoon.arraylist.color.is("Red")) {
			color = 0xff991010;
		}
		if(Monsoon.arraylist.color.is("Blue")) {
			color = new Color(0, 170, 255, 255).getRGB();
		}
		if(Monsoon.arraylist.color.is("Orange")) {
			color = 0xffeb6517;
		}
		if(Monsoon.arraylist.color.is("Green")) {
			color = 0xff30eb17;
		}
		if(Monsoon.arraylist.color.is("Green")) {
			color = 0xff30eb17;
		}
		if(Monsoon.arraylist.color.is("Black")) {
			color = 0xffffffff;
		}
		if(Monsoon.arraylist.color.is("Discord")) {
			color = new Color(114, 137, 218, 255).getRGB();
		}
		if(Monsoon.arraylist.color.is("White")) {
			color = -1;
		}
		if(Monsoon.arraylist.color.is("Purple")) {
			color = new Color(237, 0, 228).getRGB();
		}
		if(Monsoon.arraylist.color.is("Astolfo")) {
			color = ColorUtil.astolfoColors(10, 14);
		}

        /*if(target.getHealth() >= target.getMaxHealth()) {
           healthRect = 160;
        } else if (target.getHealth() >= 19) {
           healthRect = 152;
        } else if (target.getHealth() >= 18) {
           healthRect = 144;
        } else if (target.getHealth() >= 17) {
           healthRect = 136;
        } else if (target.getHealth() >= 16) {
           healthRect = 128;
        } else if (target.getHealth() >= 15) {
           healthRect = 120;
        } else if (target.getHealth() >= 14) {
           healthRect = 112;
        } else if (target.getHealth() >= 13) {
           healthRect = 104;
        } else if (target.getHealth() >= 11) {
           healthRect = 96;
        } else if (target.getHealth() >= 10) {
           healthRect = 88;
        } else if (target.getHealth() >= 9) {
           healthRect = 80;
        } else if (target.getHealth() >= 8) {
           healthRect = 72;
        } else if (target.getHealth() >= 7) {
           healthRect = 64;
        } else if (target.getHealth() >= 6) {
           healthRect = 56;
        } else if (target.getHealth() >= 5) {
           healthRect = 48;
        } else if (target.getHealth() >= 4) {
           healthRect = 40;
        } else if (target.getHealth() >= 3) {
           healthRect = 32;
        } else if (target.getHealth() >= 2) {
           healthRect = 24;
        } else if (target.getHealth() >= 1) {
           healthRect = 16;
        } */

		Gui.drawRect(getAbsoluteX(), getAbsoluteY() + 35, getAbsoluteX() + 160, getAbsoluteY() + 37, new Color(0, 0, 0, 255).getRGB());
		Gui.drawRect(getAbsoluteX(), getAbsoluteY() + 35, getAbsoluteX() + healthRect, getAbsoluteY() + 37, color);

	}

	public int getWidth() {
		return getAbsoluteX() + 160;
	}

	public int getHeight() {
		return 52;
	}

	private int getAbsoluteX() {
		return (int) Monsoon.targethud.targetX.getValue();
	}

	private int getAbsoluteY() {
		return (int) Monsoon.targethud.targetY.getValue();
	}
	public void renderUser() {
		ScaledResolution sr = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
		int time = Timeutil.get_hour();
		String line;

		if (time >= 0 && time < 12) {
			line = "Morning, " + EnumChatFormatting.AQUA + mc.thePlayer.getName() + EnumChatFormatting.RESET + ", you're lookin sexy today";
		} else if (time >= 12 && time < 16) {
			line = "Afternoon, " + EnumChatFormatting.AQUA + mc.thePlayer.getName() + EnumChatFormatting.RESET + ", you're lookin hawt today";
		} else if (time >= 16 && time < 24) {
			line = "Evening, " + EnumChatFormatting.AQUA +  mc.thePlayer.getName() + EnumChatFormatting.RESET + ", you're lookin juicy today";
		} else {
			line = "Welcome, " + EnumChatFormatting.AQUA + mc.thePlayer.getName() + EnumChatFormatting.RESET + ", you're lookin tasty today";
		}

		mc.fontRendererObj.drawStringWithShadow(line, sr.getScaledWidth() / 2f - mc.fontRendererObj.getStringWidth(line) / 2f, 20f, -1);
	}

}

