package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Config;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import today.flux.Flux;
import today.flux.addon.AddonManager;
import today.flux.gui.altMgr.GuiAltMgr;
import today.flux.gui.clickgui.classic.GuiRenderUtils;
import today.flux.gui.clickgui.classic.RenderUtil;
import today.flux.gui.fontRenderer.FontManager;
import today.flux.gui.loginui.GuiLogin;
import today.flux.gui.plugingui.GuiPluginMgr;
import today.flux.irc.IRCClient;
import today.flux.utility.AnimationUtils;
import today.flux.utility.ColorUtils;
import today.flux.utility.MathUtils;
import today.flux.utility.shader.shaders.ShaderBlob;

import java.awt.*;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

public class GuiMainMenu extends GuiScreen {

	public ScaledResolution sr;
	public Minecraft mc = Minecraft.getMinecraft();

	// Butt ons
	public CopyOnWriteArrayList<Buton> buttons = new CopyOnWriteArrayList<Buton>();
	public CopyOnWriteArrayList<RoundButton> rButtons = new CopyOnWriteArrayList<>();

	boolean adding = false;

	RoundButton addonButton = new RoundButton(new GuiPluginMgr(this), "Flux Addon", "E", 0, 0);

	@Override
	public void initGui() {
		sr = new ScaledResolution(mc);
		this.buttons.clear();
		this.rButtons.clear();

		addonButton = new RoundButton(new GuiPluginMgr(this), "Flux Addon", "E", -.5f, -.5f);
		adding = false;

		this.buttons.add(new Buton(new GuiAltMgr(this), "Alt Manager", "M", 0, 0));
		this.rButtons.add(new RoundButton(new GuiOptions(this, mc.gameSettings), "Options", "N", 0, 0));
		this.rButtons.add(new RoundButton(null, "Quit Game", "O", 0, 0));
		super.initGui();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		try {
			if (Flux.irc == null || IRCClient.loggedPacket == null) {
				mc.displayGuiScreen(new GuiLogin());
				return;
			}

			float width = sr.getScaledWidth();
			float height = sr.getScaledHeight();

			// 不绘制这个他就给我白屏了(Strange)
			this.drawGradientRect(0, 0, this.width, this.height, 0x00FFFFFF, 0x00FFFFFF);

			if (!Flux.background.getValue()) {
				//B L O B
				if (Flux.INSTANCE.blobShader == null)
					Flux.INSTANCE.blobShader = new ShaderBlob();
				Flux.INSTANCE.blobShader.renderShader(this.width, this.height);
			} else {
				RenderUtil.drawImage(new ResourceLocation("flux/loginbackground.png"), 0, 0, width, height);
			}

			RenderUtil.drawRect(0, 0, width, height, RenderUtil.reAlpha(ColorUtils.BLACK.c, 0.1f));

			// 中间按钮的显示
			float startY = height / 2 - ((this.buttons.size() * 30) / 2f);
			for (Buton b : this.buttons) {
				b.draw(width / 2 - (150 / 2f), startY, mouseX, mouseY);
				startY += 30;
			}

			// Side button render
			float startX = width - 8 - (this.rButtons.size() * 36);
			for (RoundButton b : this.rButtons) {
				b.draw(startX, 12, mouseX, mouseY);
				startX += 36;
			}

			// 信息显示
			FontManager.roboto15.drawStringWithAlpha(Flux.NAME + " b" + Flux.VERSION + " " + Flux.status, 10, height - 35,
					ColorUtils.WHITE.c, 0.6f);
			FontManager.roboto15.drawStringWithAlpha("Copyright Mojang AB. Do not distribute!", 10, height - 25,
					ColorUtils.WHITE.c, 0.6f);
			FontManager.roboto15.drawStringWithAlpha("Minecraft 1.8.9 (" + Config.getVersion() + ")", 10, height - 15,
					ColorUtils.WHITE.c, 0.6f);

			FontManager.roboto15.drawStringWithAlpha("Welcome, " + IRCClient.loggedPacket.getRealUsername(),
					width - FontManager.robotoL15.getStringWidth("Welcome, " + IRCClient.loggedPacket.getRealUsername()) - 11, height - 35, ColorUtils.WHITE.c,
					0.6f);

			boolean drawAPI = false;
			try {
				if (AddonManager.loaded && Flux.INSTANCE.api != null) {
					if (!adding && !rButtons.contains(addonButton) && buttons.size() >= 1) {
						adding = true;
						buttons.add(0, new Buton(new GuiSelectWorld(this), "Single Player", "K", 0, 0));
						buttons.add(1, new Buton(new GuiMultiplayer(this), "Multi Player", "L", 0, 0));
						this.rButtons.add(0, addonButton);
					}
					int apiSize = Flux.INSTANCE.api.getAddonManager().getEnabledFluxAddonList().size();
					String apiString = "Enabled " + EnumChatFormatting.GREEN + apiSize + EnumChatFormatting.WHITE + " Addon" + (apiSize > 1 ? "s" : "") + " (" + Flux.INSTANCE.api.getAddonManager().getFluxAddonList().size() + " Loaded)";
					FontManager.roboto15.drawStringWithAlpha(apiString, width - FontManager.roboto15.getStringWidth(apiString) - 9, height - 25, ColorUtils.WHITE.c, 0.6f);
					drawAPI = true;
				}
			} catch (Throwable e) {
				e.printStackTrace();
			}

			if (!drawAPI) {
				String apiString = "Flux API Loading...";
				FontManager.roboto15.drawStringWithAlpha(apiString, width - FontManager.roboto15.getStringWidth(apiString) - 9, height - 25, ColorUtils.WHITE.c, 0.6f);
			}

			FontManager.roboto15.drawStringWithAlpha(Flux.CREDIT,
					width - FontManager.robotoL15.getStringWidth(Flux.CREDIT) - 12, height - 15, ColorUtils.WHITE.c,
					0.6f);

		} catch (Throwable e) {
			e.printStackTrace();
		}

		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	protected void actionPerformed(GuiButton button){

	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {

		if (!AnimationUtils.animationDone)
			return;

		if(mouseButton == 0) {
			for (Buton b : this.buttons) {
				b.onClick();
			}

			for (RoundButton b : this.rButtons) {
				b.onClick();
			}
		}

		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void updateScreen() {
		sr = new ScaledResolution(mc);
		super.updateScreen();
	}

	public class Buton {
		public float x;
		public float y;

		public GuiScreen screen;
		public String text;
		public String icon;

		public float offX;
		public float offY;

		public boolean isHovered = false;
		public float hoverAni = 0;

		public Buton(GuiScreen s, String t, String icon, float offX, float offY) {
			this.screen = s;
			this.text = t;
			this.icon = icon;
			this.offX = offX;
			this.offY = offY;
		}

		public void draw(float x, float y, int mouseX, int mouseY) {
			this.x = x;
			this.y = y;

			isHovered = RenderUtil.isHovering(mouseX, mouseY, x, y, x + 150, y + 25);
			hoverAni = AnimationUtils.getAnimationState(hoverAni, isHovered ? 30f : 0f, 200);
			float finalAni = MathUtils.clampValue(hoverAni / 100f, 0, 1);

			GuiRenderUtils.drawRect(x, y, 150, 25, new Color(0, 0, 0, 200).getRGB());
			FontManager.sans18_2.drawCenteredStringWithShadow(text, x  + 150 / 2f, y + (25 / 2f) - (FontManager.sans18.getHeight(text) / 2f), ColorUtils.WHITE.c);

			FontManager.icon30.drawStringWithShadow(this.icon, x + 10 + offX, y + (25 / 2f) - (FontManager.icon30.getHeight(icon) / 2) + offY, ColorUtils.WHITE.c);

			if(hoverAni > 1f) {
				GuiRenderUtils.drawRoundedRect(x, y, 150, 25, 2f, RenderUtil.reAlpha(ColorUtils.BLACK.c, finalAni), 1f, RenderUtil.reAlpha(ColorUtils.BLACK.c, finalAni));
			}
		}

		public void onClick() {
			if(isHovered) {
				if (this.screen == null) {
					mc.shutdown();
				} else {
					Minecraft.getMinecraft().displayGuiScreen(screen);
				}
			}
		}
	}

	public class RoundButton {
		public float x;
		public float y;
		public float iconOffsetX;
		public float iconOffsetY;

		public float alphaAni = 0f;

		public GuiScreen screen;
		public String text;
		public String icon;
		public boolean isHovered = false;

		public RoundButton(GuiScreen s, String t, String icon, float iconOffsetX, float iconOffsetY) {
			this.screen = s;
			this.text = t;
			this.icon = icon;
			this.iconOffsetX = iconOffsetX;
			this.iconOffsetY = iconOffsetY;
		}

		public void draw(float x, float y, int mouseX, int mouseY) {
			this.x = x;
			this.y = y;

			isHovered = RenderUtil.isHovering(mouseX, mouseY, x, y, x + 28, y + 28);
			alphaAni = AnimationUtils.getAnimationState(this.alphaAni, this.isHovered ? 30f : 0f, 200);
			float finalAlphaAni = MathUtils.clampValue(alphaAni / 100f, 0f, 1f);

			GuiRenderUtils.drawFilledCircleNoBorder(x + 14, y + 14, 14, new Color(0, 0, 0, 200).getRGB());
			GuiRenderUtils.drawCircle(x + 14, y + 14, 14, .5f, new Color(0, 0, 0, 200).getRGB());
			FontManager.icon20.drawStringWithShadow(icon, x + (28 / 2f) - (FontManager.icon20.getStringWidth(icon) / 2f) + iconOffsetX, y + (28 / 2f) - (FontManager.icon20.getHeight(icon) / 2f) + iconOffsetY, ColorUtils.WHITE.c);

			if(alphaAni > 1f) {
				GuiRenderUtils.drawFilledCircleNoBorder(x + 14, y + 14, 14, RenderUtil.reAlpha(ColorUtils.BLACK.c, finalAlphaAni));
			}
		}

		public void onClick() {
			if(isHovered) {
				if (this.screen == null) {
					mc.shutdown();
				} else {
					Minecraft.getMinecraft().displayGuiScreen(screen);
				}
			}
		}
	}
}
