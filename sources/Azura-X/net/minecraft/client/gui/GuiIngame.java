package net.minecraft.client.gui;

import best.azura.irc.core.entities.*;
import best.azura.client.impl.Client;
import best.azura.client.impl.events.EventRender2D;
import best.azura.client.impl.module.impl.other.AutoHypixel;
import best.azura.client.impl.module.impl.other.Disabler;
import best.azura.client.impl.module.impl.other.StreamerMode;
import best.azura.client.impl.module.impl.render.BlurModule;
import best.azura.client.impl.module.impl.render.Crosshair;
import best.azura.client.impl.module.impl.render.CustomMinecraft;
import best.azura.client.impl.ui.font.Fonts;
import best.azura.client.util.color.ColorUtil;
import best.azura.client.util.other.ServerUtil;
import best.azura.client.util.render.RenderUtil;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.src.Config;
import net.minecraft.util.*;
import net.minecraft.world.border.WorldBorder;
import net.optifine.CustomColors;

import java.awt.*;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class GuiIngame extends Gui {
	private static final ResourceLocation vignetteTexPath = new ResourceLocation("textures/misc/vignette.png");
	private static final ResourceLocation widgetsTexPath = new ResourceLocation("textures/gui/widgets.png");
	private static final ResourceLocation pumpkinBlurTexPath = new ResourceLocation("textures/misc/pumpkinblur.png");
	private final Random rand = new Random();
	private final Minecraft mc;
	private final RenderItem itemRenderer;

	/**
	 * ChatGUI instance that retains all previous chat data
	 */
	private final GuiNewChat persistantChatGUI;
	private int updateCounter;

	/**
	 * The string specifying which record music is playing
	 */
	private String recordPlaying = "";

	/**
	 * How many ticks the record playing message will be displayed
	 */
	private int recordPlayingUpFor;
	private boolean recordIsPlaying;

	/**
	 * Previous frame vignette brightness (slowly changes by 1% each frame)
	 */
	public float prevVignetteBrightness = 1.0F;

	/**
	 * Remaining ticks the item highlight should be visible
	 */
	private int remainingHighlightTicks;

	/**
	 * The ItemStack that is currently being highlighted
	 */
	private ItemStack highlightingItemStack;
	private final GuiOverlayDebug overlayDebug;

	/**
	 * The spectator GUI for this in-game GUI instance
	 */
	private final GuiSpectator spectatorGui;
	private final GuiPlayerTabOverlay overlayPlayerList;

	/**
	 * A timer for the current title and subtitle displayed
	 */
	private int titlesTimer;

	/**
	 * The current title displayed
	 */
	private String displayedTitle = "";

	/**
	 * The current sub-title displayed
	 */
	private String displayedSubTitle = "";

	/**
	 * The time that the title take to fade in
	 */
	private int titleFadeIn;

	/**
	 * The time that the title is display
	 */
	private int titleDisplayTime;

	/**
	 * The time that the title take to fade out
	 */
	private int titleFadeOut;
	private int playerHealth = 0;
	private int lastPlayerHealth = 0;

	/**
	 * The last recorded system time
	 */
	private long lastSystemTime = 0L;

	/**
	 * Used with updateCounter to make the heart bar flash
	 */
	private long healthUpdateCounter = 0L;


	public GuiIngame(Minecraft mcIn) {
		this.mc = mcIn;
		this.itemRenderer = mcIn.getRenderItem();
		this.overlayDebug = new GuiOverlayDebug(mcIn);
		this.spectatorGui = new GuiSpectator(mcIn);
		this.persistantChatGUI = new GuiNewChat(mcIn);
		this.overlayPlayerList = new GuiPlayerTabOverlay(mcIn, this);
		this.setDefaultTitlesTimes();
	}

	/**
	 * Set the differents times for the titles to their default values
	 */
	public void setDefaultTitlesTimes() {
		this.titleFadeIn = 10;
		this.titleDisplayTime = 70;
		this.titleFadeOut = 20;
	}


	public void renderGameOverlay(float partialTicks) {

		ScaledResolution scaledresolution = new ScaledResolution(this.mc);
		int i = scaledresolution.getScaledWidth();
		int j = scaledresolution.getScaledHeight();
		this.mc.entityRenderer.setupOverlayRendering();


		Client.INSTANCE.getEventBus().call(new EventRender2D(partialTicks));

		if (ServerUtil.isHypixel() && !Client.INSTANCE.getModuleManager().getModule(AutoHypixel.class).isEnabled()) {
			final FontRenderer fr = getFontRenderer();
			final String text = "Playing on Hypixel without Auto Hypixel enabled is not advised!";
			final int x = scaledresolution.getScaledWidth() / 2 - fr.getStringWidth(text) / 2, y = 35;
			int offset = 0;
			for (char c : text.toCharArray()) {
				final Color color = ColorUtil.getFadingColor(Color.RED, Color.RED.darker().darker(), -offset, 25.0, 1.5f);
				fr.drawStringWithShadow(Character.toString(c), x + offset, y, color.getRGB());
				offset += fr.getStringWidth(Character.toString(c));
			}
		}
		/*if (ServerUtil.isHypixel() && !String.valueOf(BalanceUtil.getBalance()).startsWith("-")) {
			final FontRenderer fr = getFontRenderer();
			final String text = "Do not use timer, until this goes away!";
			final int x = scaledresolution.getScaledWidth() / 2 - fr.getStringWidth(text) / 2, y = 50;
			int offset = 0;
			for (char c : text.toCharArray()) {
				final Color color = ColorUtil.getFadingColor(Color.RED, Color.RED.darker().darker(), -offset, 25.0, 1.5f);
				fr.drawStringWithShadow(Character.toString(c), x + offset, y, color.getRGB());
				offset += fr.getStringWidth(Character.toString(c));
			}
		}
			
		 */
		if (ServerUtil.isHypixel() && !Client.INSTANCE.getModuleManager().getModule(Disabler.class).isEnabled()) {
			final FontRenderer fr = getFontRenderer();
			final String text = "Please consider using Disabler";
			final int x = scaledresolution.getScaledWidth() / 2 - fr.getStringWidth(text) / 2, y = 65;
			int offset = 0;
			for (char c : text.toCharArray()) {
				final Color color = ColorUtil.getFadingColor(Color.RED, Color.RED.darker().darker(), -offset, 25.0, 1.5f);
				//fr.drawStringWithShadow(Character.toString(c), x + offset, y, color.getRGB());
				//offset += fr.getStringWidth(Character.toString(c));
			}
		}

		GlStateManager.enableBlend();

		if (Config.isVignetteEnabled()) {
			this.renderVignette(this.mc.thePlayer.getBrightness(partialTicks), scaledresolution);
		} else {
			GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		}

		ItemStack itemstack = this.mc.thePlayer.inventory.armorItemInSlot(3);

		if (this.mc.gameSettings.showDebugInfo == 0 && itemstack != null && itemstack.getItem() == Item.getItemFromBlock(Blocks.pumpkin)) {
			this.renderPumpkinOverlay(scaledresolution);
		}

		if (!this.mc.thePlayer.isPotionActive(Potion.confusion)) {
			float f = this.mc.thePlayer.prevTimeInPortal + (this.mc.thePlayer.timeInPortal - this.mc.thePlayer.prevTimeInPortal) * partialTicks;

			if (f > 0.0F) {
				this.renderPortal(f, scaledresolution);
			}
		}

		if (this.mc.playerController.isSpectator()) {
			this.spectatorGui.renderTooltip(scaledresolution, partialTicks);
		} else {
			this.renderTooltip(scaledresolution, partialTicks);
		}

		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(icons);
		GlStateManager.enableBlend();

		if (!Client.INSTANCE.getModuleManager().getModule(Crosshair.class).isEnabled() &&
				this.showCrosshair()) {
			GlStateManager.tryBlendFuncSeparate(775, 769, 1, 0);
			GlStateManager.enableAlpha();
			this.drawTexturedModalRect(i / 2 - 7, j / 2 - 7, 0, 0, 16, 16);
		}
		GlStateManager.resetColor();
		GlStateManager.enableAlpha();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		this.mc.mcProfiler.startSection("bossHealth");
		this.renderBossHealth();
		this.mc.mcProfiler.endSection();

		boolean blur = Client.INSTANCE.getModuleManager().getModule(BlurModule.class).isEnabled() && BlurModule.blurHotbar.getObject();
		if (blur) GlStateManager.translate(0, -5, 0);

		if (this.mc.playerController.shouldDrawHUD()) {
			this.renderPlayerStats(scaledresolution);
		}

		GlStateManager.disableBlend();

		if (this.mc.thePlayer.getSleepTimer() > 0) {
			this.mc.mcProfiler.startSection("sleep");
			GlStateManager.disableDepth();
			GlStateManager.disableAlpha();
			int j1 = this.mc.thePlayer.getSleepTimer();
			float f1 = (float) j1 / 100.0F;

			if (f1 > 1.0F) {
				f1 = 1.0F - (float) (j1 - 100) / 10.0F;
			}

			int k = (int) (220.0F * f1) << 24 | 1052704;
			drawRect(0, 0, i, j, k);
			GlStateManager.enableAlpha();
			GlStateManager.enableDepth();
			this.mc.mcProfiler.endSection();
		}

		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		int k1 = i / 2 - 91;

		final CustomMinecraft customMinecraft = (CustomMinecraft) Client.INSTANCE.getModuleManager().getModule(CustomMinecraft.class);
		if (!(customMinecraft.isEnabled() && customMinecraft.noEXPBar.getObject())) {
			if (this.mc.thePlayer.isRidingHorse()) {
				this.renderHorseJumpBar(scaledresolution, k1);
			} else if (this.mc.playerController.gameIsSurvivalOrAdventure()) {
				this.renderExpBar(scaledresolution, k1);
			}
		}

		if (this.mc.gameSettings.heldItemTooltips && !this.mc.playerController.isSpectator()) {
			this.renderSelectedItem(scaledresolution);
		} else if (this.mc.thePlayer.isSpectator()) {
			this.spectatorGui.renderSelectedItem(scaledresolution);
		}
		if (blur) GlStateManager.translate(0, 5, 0);


		if (this.mc.gameSettings.showDebugProfilerChart) {
			this.overlayDebug.renderDebugInfo(scaledresolution);
		}

		if (this.recordPlayingUpFor > 0) {
			this.mc.mcProfiler.startSection("overlayMessage");
			float f2 = (float) this.recordPlayingUpFor - partialTicks;
			int l1 = (int) (f2 * 255.0F / 20.0F);

			if (l1 > 255) {
				l1 = 255;
			}

			if (l1 > 8) {
				GlStateManager.pushMatrix();
				GlStateManager.translate((float) (i / 2), (float) (j - 68), 0.0F);
				GlStateManager.enableBlend();
				GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
				int l = 16777215;

				if (this.recordIsPlaying) {
					l = MathHelper.hsvToRGB(f2 / 50.0F, 0.7F, 0.6F) & 16777215;
				}

				this.getFontRenderer().drawString(this.recordPlaying, -this.getFontRenderer().getStringWidth(this.recordPlaying) / 2., -4, l + (l1 << 24 & -16777216));
				GlStateManager.disableBlend();
				GlStateManager.popMatrix();
			}

			this.mc.mcProfiler.endSection();
		}


		final best.azura.client.impl.ui.font.FontRenderer fr = Fonts.INSTANCE.arial20;

		final String username = Client.INSTANCE.getUsername();
		final String build = Client.releaseBuild;
		final String release = Client.RELEASE;
		final String splitters = " - §r";
		double y = mc.displayHeight - fr.FONT_HEIGHT;
		if (mc.currentScreen instanceof GuiChat && !customMinecraft.chatMessageLength.getObject())
			y = mc.displayHeight - fr.FONT_HEIGHT - 22;
		RenderUtil.INSTANCE.scaleFix(1.0);
		fr.drawStringWithShadow("§7" + build + "§f" + splitters, mc.displayWidth
				- fr.getStringWidth(splitters)
				- fr.getStringWidth(release)
				- fr.getStringWidth(build)
				- fr.getStringWidth(username)
				- fr.getStringWidth(splitters) - 3, y, -1);

		fr.drawStringWithShadow(release,
				mc.displayWidth
						- fr.getStringWidth(release) - 6, y, -1);
		fr.drawStringWithShadow("§f" + splitters,
				mc.displayWidth
						- fr.getStringWidth(splitters)
						- fr.getStringWidth(release) - 3, y, -1);
		fr.drawStringWithShadow("§7" + username,
				mc.displayWidth
						- fr.getStringWidth(username)
						- fr.getStringWidth(splitters)
						- fr.getStringWidth(release)
						- 3
				, y, -1);


		RenderUtil.INSTANCE.invertScaleFix(1.0);


		if (this.titlesTimer > 0) {
			this.mc.mcProfiler.startSection("titleAndSubtitle");
			float f3 = (float) this.titlesTimer - partialTicks;
			int i2 = 255;


			if (this.titlesTimer > this.titleFadeOut + this.titleDisplayTime) {
				float f4 = (float) (this.titleFadeIn + this.titleDisplayTime + this.titleFadeOut) - f3;
				i2 = (int) (f4 * 255.0F / (float) this.titleFadeIn);
			}

			if (this.titlesTimer <= this.titleFadeOut) {
				i2 = (int) (f3 * 255.0F / (float) this.titleFadeOut);
			}

			i2 = MathHelper.clamp_int(i2, 0, 255);

			if (i2 > 8) {
				GlStateManager.pushMatrix();
				GlStateManager.translate((float) (i / 2), (float) (j / 2), 0.0F);
				GlStateManager.enableBlend();
				GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
				GlStateManager.pushMatrix();
				GlStateManager.scale(4.0F, 4.0F, 4.0F);
				int j2 = i2 << 24 & -16777216;
				this.getFontRenderer().drawString(this.displayedTitle, (float) (-this.getFontRenderer().getStringWidth(this.displayedTitle) / 2), -10.0F, 16777215 | j2, true);
				GlStateManager.popMatrix();
				GlStateManager.pushMatrix();
				GlStateManager.scale(2.0F, 2.0F, 2.0F);
				this.getFontRenderer().drawString(this.displayedSubTitle, (float) (-this.getFontRenderer().getStringWidth(this.displayedSubTitle) / 2), 5.0F, 16777215 | j2, true);
				GlStateManager.popMatrix();
				GlStateManager.disableBlend();
				GlStateManager.popMatrix();
			}

			this.mc.mcProfiler.endSection();
		}

		Scoreboard scoreboard = this.mc.theWorld.getScoreboard();
		ScoreObjective scoreobjective = null;
		ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(this.mc.thePlayer.getName());

		if (scoreplayerteam != null) {
			int i1 = scoreplayerteam.getChatFormat().getColorIndex();

			if (i1 >= 0) {
				scoreobjective = scoreboard.getObjectiveInDisplaySlot(3 + i1);
			}
		}

		ScoreObjective scoreobjective1 = scoreobjective != null ? scoreobjective : scoreboard.getObjectiveInDisplaySlot(1);
		final StreamerMode streamerMode = (StreamerMode) Client.INSTANCE.getModuleManager().getModule(StreamerMode.class);
		if (scoreobjective1 != null) {

			if (!(streamerMode.isEnabled() && streamerMode.disableSB.getObject()))
				this.renderScoreboard(scoreobjective1, scaledresolution);
		}

		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.disableAlpha();
		GlStateManager.pushMatrix();
		GlStateManager.translate(0.0F, (float) (j - 48), 0.0F);
		this.mc.mcProfiler.startSection("chat");
		this.persistantChatGUI.drawChat(this.updateCounter);
		this.mc.mcProfiler.endSection();
		GlStateManager.popMatrix();
		scoreobjective1 = scoreboard.getObjectiveInDisplaySlot(0);

		if (this.mc.gameSettings.keyBindPlayerList.isKeyDown() && (!this.mc.isIntegratedServerRunning() || this.mc.thePlayer.sendQueue.getPlayerInfoMap().size() > 1 || scoreobjective1 != null)) {
			this.overlayPlayerList.updatePlayerList(true);
			this.overlayPlayerList.renderPlayerlist(i, scoreboard, scoreobjective1);
		} else {
			this.overlayPlayerList.updatePlayerList(false);
		}

		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.disableLighting();
		GlStateManager.enableAlpha();
		GlStateManager.enableBlend();
	}

	protected void renderTooltip(ScaledResolution sr, float partialTicks) {
		if (this.mc.getRenderViewEntity() instanceof EntityPlayer) {
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			this.mc.getTextureManager().bindTexture(widgetsTexPath);
			EntityPlayer entityplayer = (EntityPlayer) this.mc.getRenderViewEntity();
			int i = sr.getScaledWidth() / 2;
			float f = this.zLevel;
			this.zLevel = -90.0F;
			boolean blur = Client.INSTANCE.getModuleManager().getModule(BlurModule.class).isEnabled() && BlurModule.blurHotbar.getObject();
			if (blur) {
				BlurModule.blurTasks.add(() -> RenderUtil.INSTANCE.drawRect(i - 95, sr.getScaledHeight() - 26, i - 91 + 187, sr.getScaledHeight(), -1));
				BlurModule.postBlurTasks.add(() -> {
					RenderUtil.INSTANCE.drawHollowRoundedRect(i - 91 + entityplayer.inventory.currentItem * 20, sr.getScaledHeight() - 23, 22, 21, 2, ColorUtil.getLastHudColor());

					RenderUtil.INSTANCE.drawHollowGradientRect(i - 95, sr.getScaledHeight() - 26, i - 91 + 187, sr.getScaledHeight(), ColorUtil.getHudColor(1).getRGB(), ColorUtil.getHudColor(10).getRGB(), 1.5F);
				});
			} else {
				this.drawTexturedModalRect(i - 91, sr.getScaledHeight() - 22, 0, 0, 182, 22);
				this.drawTexturedModalRect(i - 91 - 1 + entityplayer.inventory.currentItem * 20, sr.getScaledHeight() - 22 - 1, 0, 22, 24, 22);
			}
			this.zLevel = f;
			GlStateManager.enableRescaleNormal();
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
			RenderHelper.enableGUIStandardItemLighting();

			for (int j = 0; j < 9; ++j) {
				int k = sr.getScaledWidth() / 2 - 90 + j * 20 + 2;
				int l = sr.getScaledHeight() - 16 - 3;
				this.renderHotbarItem(j, k, l - (blur ? 2 : 0), partialTicks, entityplayer);
			}

			RenderHelper.disableStandardItemLighting();
			GlStateManager.disableRescaleNormal();
			GlStateManager.disableBlend();
		}
	}

	public void renderHorseJumpBar(ScaledResolution scaledRes, int x) {
		this.mc.mcProfiler.startSection("jumpBar");
		this.mc.getTextureManager().bindTexture(Gui.icons);
		float f = this.mc.thePlayer.getHorseJumpPower();
		int i = 182;
		int j = (int) (f * (float) (i + 1));
		int k = scaledRes.getScaledHeight() - 32 + 3;
		this.drawTexturedModalRect(x, k, 0, 84, i, 5);

		if (j > 0) {
			this.drawTexturedModalRect(x, k, 0, 89, j, 5);
		}

		this.mc.mcProfiler.endSection();
	}

	public void renderExpBar(ScaledResolution scaledRes, int x) {
		this.mc.mcProfiler.startSection("expBar");
		this.mc.getTextureManager().bindTexture(Gui.icons);
		int i = this.mc.thePlayer.xpBarCap();

		if (i > 0) {
			int j = 182;
			int k = (int) (this.mc.thePlayer.experience * (float) (j + 1));
			int l = scaledRes.getScaledHeight() - 32 + 3;
			this.drawTexturedModalRect(x, l, 0, 64, j, 5);

			if (k > 0) {
				this.drawTexturedModalRect(x, l, 0, 69, k, 5);
			}
		}

		this.mc.mcProfiler.endSection();

		if (this.mc.thePlayer.experienceLevel > 0) {
			this.mc.mcProfiler.startSection("expLevel");
			int k1 = 8453920;

			if (Config.isCustomColors()) {
				k1 = CustomColors.getExpBarTextColor(k1);
			}

			String s = "" + this.mc.thePlayer.experienceLevel;
			int l1 = (scaledRes.getScaledWidth() - this.getFontRenderer().getStringWidth(s)) / 2;
			int i1 = scaledRes.getScaledHeight() - 31 - 4;
			this.getFontRenderer().drawString(s, l1 + 1, i1, 0);
			this.getFontRenderer().drawString(s, l1 - 1, i1, 0);
			this.getFontRenderer().drawString(s, l1, i1 + 1, 0);
			this.getFontRenderer().drawString(s, l1, i1 - 1, 0);
			this.getFontRenderer().drawString(s, l1, i1, k1);
			this.mc.mcProfiler.endSection();
		}
	}

	public void renderSelectedItem(ScaledResolution scaledRes) {
		this.mc.mcProfiler.startSection("selectedItemName");

		if (this.remainingHighlightTicks > 0 && this.highlightingItemStack != null) {
			String s = this.highlightingItemStack.getDisplayName();

			if (this.highlightingItemStack.hasDisplayName()) {
				s = EnumChatFormatting.ITALIC + s;
			}

			CustomMinecraft customMinecraft = (CustomMinecraft) Client.INSTANCE.getModuleManager().getModule(CustomMinecraft.class);
			int i = (scaledRes.getScaledWidth() - this.getFontRenderer().getStringWidth(s)) / 2;

			int j = scaledRes.getScaledHeight() - 59;

			if (!this.mc.playerController.shouldDrawHUD()) {
				j += 14;
			}

			int k = (int) ((float) this.remainingHighlightTicks * 256.0F / 10.0F);

			if (k > 255) {
				k = 255;
			}

			if (k > 0) {
				GlStateManager.pushMatrix();
				GlStateManager.enableBlend();
				GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
				if (customMinecraft.toolTip.getObject() && customMinecraft.isEnabled()) {
					GlStateManager.scale(0.5, 0.5, 0.5);
					Client.INSTANCE.getFontRenderer().drawStringWithShadow(s, (float) i * 2, (float) j * 2, 16777215 + (k << 24));
				} else this.getFontRenderer().drawStringWithShadow(s, (float) i, (float) j, 16777215 + (k << 24));
				GlStateManager.disableBlend();
				GlStateManager.popMatrix();
			}
		}

		this.mc.mcProfiler.endSection();
	}


	protected boolean showCrosshair() {
		if (this.mc.gameSettings.showDebugProfilerChart && !this.mc.thePlayer.hasReducedDebug() && !this.mc.gameSettings.reducedDebugInfo) {
			return false;
		} else if (this.mc.playerController.isSpectator()) {
			if (this.mc.pointedEntity != null) {
				return true;
			} else {
				if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
					BlockPos blockpos = this.mc.objectMouseOver.getBlockPos();

					return this.mc.theWorld.getTileEntity(blockpos) instanceof IInventory;
				}

				return false;
			}
		} else {
			return true;
		}
	}

	private void renderScoreboard(ScoreObjective objective, ScaledResolution scaledRes) {
		Scoreboard scoreboard = objective.getScoreboard();
		Collection<Score> collection = scoreboard.getSortedScores(objective);
		List<Score> list = collection.stream().filter(p_apply_1_ -> p_apply_1_.getPlayerName() != null && !p_apply_1_.getPlayerName().startsWith("#")).collect(Collectors.toList());

		if (list.size() > 15) {
			collection = Lists.newArrayList(Iterables.skip(list, collection.size() - 15));
		} else {
			collection = list;
		}

		int i = this.getFontRenderer().getStringWidth(objective.getDisplayName());

		for (Score score : collection) {
			ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(score.getPlayerName());
			String s = ScorePlayerTeam.formatPlayerName(scoreplayerteam, score.getPlayerName()) + ": " + EnumChatFormatting.RED + score.getScorePoints();
			i = Math.max(i, this.getFontRenderer().getStringWidth(s));
		}

		int i1 = collection.size() * this.getFontRenderer().FONT_HEIGHT;
		int j1 = scaledRes.getScaledHeight() / 2 + i1 / 3;
		int k1 = 3;
		int l1 = scaledRes.getScaledWidth() - i - k1;
		int j = 0;

		boolean blur = Client.INSTANCE.getModuleManager().getModule(BlurModule.class).isEnabled() && BlurModule.blurScoreboard.getObject();

		if (blur) {
			int k = j1 - collection.size() * this.getFontRenderer().FONT_HEIGHT;
			int l = scaledRes.getScaledWidth() - k1 + 2;
			Collection<Score> finalCollection = collection;
			BlurModule.blurTasks.add(() -> drawRect(l1 - 2, k - getFontRenderer().FONT_HEIGHT - 1, l, k + this.getFontRenderer().FONT_HEIGHT * finalCollection.size(), 1342177280));
		}
		final StreamerMode streamerMode = (StreamerMode) Client.INSTANCE.getModuleManager().getModule(StreamerMode.class);

		for (Score score1 : collection) {
			++j;
			ScorePlayerTeam scoreplayerteam1 = scoreboard.getPlayersTeam(score1.getPlayerName());
			String s1 = filterIP(ScorePlayerTeam.formatPlayerName(scoreplayerteam1, score1.getPlayerName()));
			String s2 = EnumChatFormatting.RED + "" + score1.getScorePoints();

			try {
				String lastColorCode = "";
				for (User user : Client.INSTANCE.getIrcConnector().getIrcCache().getIrcUserHashMap().values().stream().filter(user -> user.getMinecraftName() != null && !user.getMinecraftName().isEmpty()).collect(Collectors.toList())) {
					if (user.getMinecraftName().equals("Player")) continue;
					if (lastColorCode.isEmpty()) {
						String s3 = s1.split(user.getMinecraftName())[0];
						lastColorCode = Character.toString(ChatFormatting.PREFIX_CODE) + s3.charAt(s3.lastIndexOf('§') + 1);
					}
					s1 = s1.replace(user.getMinecraftName(), user.getIrcRank().getPrefix() + " " + user.getUsername() + " " + user.getFormattedIGN() + lastColorCode);
				}
			} catch (Exception ignored) {}

			int k = j1 - j * this.getFontRenderer().FONT_HEIGHT;
			int l = scaledRes.getScaledWidth() - k1 + 2;

			if (!blur) drawRect(l1 - 2, k, l, k + this.getFontRenderer().FONT_HEIGHT, 1342177280);
			if (s1.equals("azura.best")) {
				int xOffset = 0, count = 0;
				boolean skip = false;
				final StringBuilder builder = new StringBuilder();
				final FontRenderer fr = this.getFontRenderer();
				for (char c : s1.toCharArray()) {
					if (skip) {
						skip = false;
						builder.append(c);
						continue;
					}
					if (c == 167) {
						builder.append(c);
						skip = true;
						continue;
					}
					fr.drawStringWithShadow(builder.toString() + c, l1 + xOffset, k, ColorUtil.getHudColor(count).getRGB());
					xOffset += fr.getStringWidth(Character.toString(c));
					count--;
				}
			} else if (s1.contains("§8") && j == collection.size() && streamerMode.isEnabled() && streamerMode.hideGameID.getObject() && ServerUtil.isHypixel()) {
				final String s3 = s1.substring(s1.indexOf("§8"));
				this.getFontRenderer().drawStringWithShadow(s1.split("§8")[0] + "§8§k" + EnumChatFormatting.getTextWithoutFormattingCodes(s3), l1, k, 553648127);
			} else this.getFontRenderer().drawStringWithShadow(s1, l1, k, 553648127);
			this.getFontRenderer().drawStringWithShadow(s2, l - this.getFontRenderer().getStringWidth(s2), k, 553648127);
			if (j == collection.size()) {
				String s3 = objective.getDisplayName();
				if (!blur) {
					drawRect(l1 - 2, k - this.getFontRenderer().FONT_HEIGHT - 1, l, k - 1, 1610612736);
					drawRect(l1 - 2, k - 1, l, k, 1342177280);
				}
				this.getFontRenderer().drawStringWithShadow(s3, l1 + i / 2.0 - this.getFontRenderer().getStringWidth(s3) / 2.0, k - this.getFontRenderer().FONT_HEIGHT, 553648127);
			}
		}
	}

	private String filterIP(String playerName) {
		if (playerName == null) return null;
		if (playerName.matches(".*\\d") && !playerName.matches("\\d.*")) return playerName;
		if (playerName.contains(" ")) return playerName;
		String validHostnameRegex = "([a-zA-Z0-9])\\.([a-zA-Z0-9])";
		String output = playerName;
		output = Pattern.compile(validHostnameRegex).matcher(output).replaceAll("hiding§ip");
		if (output.contains("hiding§ip")) output = "azura.best";
		return output;
	}


	private void renderPlayerStats(ScaledResolution scaledRes) {
		if (this.mc.getRenderViewEntity() instanceof EntityPlayer) {
			EntityPlayer entityplayer = (EntityPlayer) this.mc.getRenderViewEntity();
			int i = MathHelper.ceiling_float_int(entityplayer.getHealth());
			boolean flag = this.healthUpdateCounter > (long) this.updateCounter && (this.healthUpdateCounter - (long) this.updateCounter) / 3L % 2L == 1L;

			if (i < this.playerHealth && entityplayer.hurtResistantTime > 0) {
				this.lastSystemTime = Minecraft.getSystemTime();
				this.healthUpdateCounter = (this.updateCounter + 20);
			} else if (i > this.playerHealth && entityplayer.hurtResistantTime > 0) {
				this.lastSystemTime = Minecraft.getSystemTime();
				this.healthUpdateCounter = (this.updateCounter + 10);
			}

			if (Minecraft.getSystemTime() - this.lastSystemTime > 1000L) {
				this.playerHealth = i;
				this.lastPlayerHealth = i;
				this.lastSystemTime = Minecraft.getSystemTime();
			}

			this.playerHealth = i;
			int j = this.lastPlayerHealth;
			this.rand.setSeed((this.updateCounter * 312871L));
			FoodStats foodstats = entityplayer.getFoodStats();
			int k = foodstats.getFoodLevel();
			IAttributeInstance iattributeinstance = entityplayer.getEntityAttribute(SharedMonsterAttributes.maxHealth);
			int i1 = scaledRes.getScaledWidth() / 2 - 91;
			int j1 = scaledRes.getScaledWidth() / 2 + 91;
			int k1 = scaledRes.getScaledHeight() - 39;
			float f = (float) iattributeinstance.getAttributeValue();
			float f1 = entityplayer.getAbsorptionAmount();
			int l1 = MathHelper.ceiling_float_int((f + f1) / 2.0F / 10.0F);
			int i2 = Math.max(10 - (l1 - 2), 3);
			int j2 = k1 - (l1 - 1) * i2 - 10;
			float f2 = f1;
			int k2 = entityplayer.getTotalArmorValue();
			int l2 = -1;

			if (entityplayer.isPotionActive(Potion.regeneration)) {
				l2 = this.updateCounter % MathHelper.ceiling_float_int(f + 5.0F);
			}

			this.mc.mcProfiler.startSection("armor");

			for (int i3 = 0; i3 < 10; ++i3) {
				if (k2 > 0) {
					int j3 = i1 + i3 * 8;

					if (i3 * 2 + 1 < k2) {
						this.drawTexturedModalRect(j3, j2, 34, 9, 9, 9);
					}

					if (i3 * 2 + 1 == k2) {
						this.drawTexturedModalRect(j3, j2, 25, 9, 9, 9);
					}

					if (i3 * 2 + 1 > k2) {
						this.drawTexturedModalRect(j3, j2, 16, 9, 9, 9);
					}
				}
			}

			this.mc.mcProfiler.endStartSection("health");

			for (int i6 = MathHelper.ceiling_float_int((f + f1) / 2.0F) - 1; i6 >= 0; --i6) {
				int j6 = 16;

				if (entityplayer.isPotionActive(Potion.poison)) {
					j6 += 36;
				} else if (entityplayer.isPotionActive(Potion.wither)) {
					j6 += 72;
				}

				int k3 = 0;

				if (flag) {
					k3 = 1;
				}

				int l3 = MathHelper.ceiling_float_int((float) (i6 + 1) / 10.0F) - 1;
				int i4 = i1 + i6 % 10 * 8;
				int j4 = k1 - l3 * i2;

				if (i <= 4) {
					j4 += this.rand.nextInt(2);
				}

				if (i6 == l2) {
					j4 -= 2;
				}

				int k4 = 0;

				if (entityplayer.worldObj.getWorldInfo().isHardcoreModeEnabled()) {
					k4 = 5;
				}

				this.drawTexturedModalRect(i4, j4, 16 + k3 * 9, 9 * k4, 9, 9);

				if (flag) {
					if (i6 * 2 + 1 < j) {
						this.drawTexturedModalRect(i4, j4, j6 + 54, 9 * k4, 9, 9);
					}

					if (i6 * 2 + 1 == j) {
						this.drawTexturedModalRect(i4, j4, j6 + 63, 9 * k4, 9, 9);
					}
				}

				if (f2 <= 0.0F) {
					if (i6 * 2 + 1 < i) {
						this.drawTexturedModalRect(i4, j4, j6 + 36, 9 * k4, 9, 9);
					}

					if (i6 * 2 + 1 == i) {
						this.drawTexturedModalRect(i4, j4, j6 + 45, 9 * k4, 9, 9);
					}
				} else {
					if (f2 == f1 && f1 % 2.0F == 1.0F) {
						this.drawTexturedModalRect(i4, j4, j6 + 153, 9 * k4, 9, 9);
					} else {
						this.drawTexturedModalRect(i4, j4, j6 + 144, 9 * k4, 9, 9);
					}

					f2 -= 2.0F;
				}
			}

			Entity entity = entityplayer.ridingEntity;

			if (entity == null) {
				this.mc.mcProfiler.endStartSection("food");

				for (int k6 = 0; k6 < 10; ++k6) {
					int j7 = k1;
					int l7 = 16;
					int k8 = 0;

					if (entityplayer.isPotionActive(Potion.hunger)) {
						l7 += 36;
						k8 = 13;
					}

					if (entityplayer.getFoodStats().getSaturationLevel() <= 0.0F && this.updateCounter % (k * 3 + 1) == 0) {
						j7 = k1 + (this.rand.nextInt(3) - 1);
					}


					int j9 = j1 - k6 * 8 - 9;
					this.drawTexturedModalRect(j9, j7, 16 + k8 * 9, 27, 9, 9);

					if (k6 * 2 + 1 < k) {
						this.drawTexturedModalRect(j9, j7, l7 + 36, 27, 9, 9);
					}

					if (k6 * 2 + 1 == k) {
						this.drawTexturedModalRect(j9, j7, l7 + 45, 27, 9, 9);
					}
				}
			} else if (entity instanceof EntityLivingBase) {
				this.mc.mcProfiler.endStartSection("mountHealth");
				EntityLivingBase entitylivingbase = (EntityLivingBase) entity;
				int i7 = (int) Math.ceil(entitylivingbase.getHealth());
				float f3 = entitylivingbase.getMaxHealth();
				int j8 = (int) (f3 + 0.5F) / 2;

				if (j8 > 30) {
					j8 = 30;
				}

				int i9 = k1;

				for (int k9 = 0; j8 > 0; k9 += 20) {
					int l4 = Math.min(j8, 10);
					j8 -= l4;

					for (int i5 = 0; i5 < l4; ++i5) {
						int j5 = 52;


						int l5 = j1 - i5 * 8 - 9;
						this.drawTexturedModalRect(l5, i9, j5, 9, 9, 9);

						if (i5 * 2 + 1 + k9 < i7) {
							this.drawTexturedModalRect(l5, i9, j5 + 36, 9, 9, 9);
						}

						if (i5 * 2 + 1 + k9 == i7) {
							this.drawTexturedModalRect(l5, i9, j5 + 45, 9, 9, 9);
						}
					}

					i9 -= 10;
				}
			}

			this.mc.mcProfiler.endStartSection("air");

			if (entityplayer.isInsideOfMaterial(Material.water)) {
				int l6 = this.mc.thePlayer.getAir();
				int k7 = MathHelper.ceiling_double_int((double) (l6 - 2) * 10.0D / 300.0D);
				int i8 = MathHelper.ceiling_double_int((double) l6 * 10.0D / 300.0D) - k7;

				for (int l8 = 0; l8 < k7 + i8; ++l8) {
					if (l8 < k7) {
						this.drawTexturedModalRect(j1 - l8 * 8 - 9, j2, 16, 18, 9, 9);
					} else {
						this.drawTexturedModalRect(j1 - l8 * 8 - 9, j2, 25, 18, 9, 9);
					}
				}
			}

			this.mc.mcProfiler.endSection();
		}
	}

	/**
	 * Renders dragon's (boss) health on the HUD
	 */
	private void renderBossHealth() {
		if (BossStatus.bossName != null && BossStatus.statusBarTime > 0) {
			--BossStatus.statusBarTime;
			ScaledResolution scaledresolution = new ScaledResolution(this.mc);
			int i = scaledresolution.getScaledWidth();
			int j = 182;
			int k = i / 2 - j / 2;
			int l = (int) (BossStatus.healthScale * (float) (j + 1));
			int i1 = 12;
			this.drawTexturedModalRect(k, i1, 0, 74, j, 5);
			this.drawTexturedModalRect(k, i1, 0, 74, j, 5);

			if (l > 0) {
				this.drawTexturedModalRect(k, i1, 0, 79, l, 5);
			}

			String s = BossStatus.bossName;
			this.getFontRenderer().drawStringWithShadow(s, (float) (i / 2 - this.getFontRenderer().getStringWidth(s) / 2), (float) (i1 - 10), 16777215);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			this.mc.getTextureManager().bindTexture(icons);
		}
	}

	private void renderPumpkinOverlay(ScaledResolution scaledRes) {
		GlStateManager.disableDepth();
		GlStateManager.depthMask(false);
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.disableAlpha();
		this.mc.getTextureManager().bindTexture(pumpkinBlurTexPath);
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
		worldrenderer.pos(0.0D, scaledRes.getScaledHeight(), -90.0D).tex(0.0D, 1.0D).endVertex();
		worldrenderer.pos(scaledRes.getScaledWidth(), scaledRes.getScaledHeight(), -90.0D).tex(1.0D, 1.0D).endVertex();
		worldrenderer.pos(scaledRes.getScaledWidth(), 0.0D, -90.0D).tex(1.0D, 0.0D).endVertex();
		worldrenderer.pos(0.0D, 0.0D, -90.0D).tex(0.0D, 0.0D).endVertex();
		tessellator.draw();
		GlStateManager.depthMask(true);
		GlStateManager.enableDepth();
		GlStateManager.enableAlpha();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	}

	/**
	 * Renders a Vignette arount the entire screen that changes with light level.
	 *
	 * @param lightLevel The current brightness
	 * @param scaledRes  The current resolution of the game
	 */
	private void renderVignette(float lightLevel, ScaledResolution scaledRes) {
		if (!Config.isVignetteEnabled()) {
			GlStateManager.enableDepth();
			GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		} else {
			lightLevel = 1.0F - lightLevel;
			lightLevel = MathHelper.clamp_float(lightLevel, 0.0F, 1.0F);
			WorldBorder worldborder = this.mc.theWorld.getWorldBorder();
			float f = (float) worldborder.getClosestDistance(this.mc.thePlayer);
			double d0 = Math.min(worldborder.getResizeSpeed() * (double) worldborder.getWarningTime() * 1000.0D, Math.abs(worldborder.getTargetSize() - worldborder.getDiameter()));
			double d1 = Math.max(worldborder.getWarningDistance(), d0);

			if ((double) f < d1) {
				f = 1.0F - (float) ((double) f / d1);
			} else {
				f = 0.0F;
			}

			this.prevVignetteBrightness = (float) ((double) this.prevVignetteBrightness + (double) (lightLevel - this.prevVignetteBrightness) * 0.01D);
			GlStateManager.disableDepth();
			GlStateManager.depthMask(false);
			GlStateManager.tryBlendFuncSeparate(0, 769, 1, 0);

			if (f > 0.0F) {
				GlStateManager.color(0.0F, f, f, 1.0F);
			} else {
				GlStateManager.color(this.prevVignetteBrightness, this.prevVignetteBrightness, this.prevVignetteBrightness, 1.0F);
			}

			this.mc.getTextureManager().bindTexture(vignetteTexPath);
			Tessellator tessellator = Tessellator.getInstance();
			WorldRenderer worldrenderer = tessellator.getWorldRenderer();
			worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
			worldrenderer.pos(0.0D, scaledRes.getScaledHeight(), -90.0D).tex(0.0D, 1.0D).endVertex();
			worldrenderer.pos(scaledRes.getScaledWidth(), scaledRes.getScaledHeight(), -90.0D).tex(1.0D, 1.0D).endVertex();
			worldrenderer.pos(scaledRes.getScaledWidth(), 0.0D, -90.0D).tex(1.0D, 0.0D).endVertex();
			worldrenderer.pos(0.0D, 0.0D, -90.0D).tex(0.0D, 0.0D).endVertex();
			tessellator.draw();
			GlStateManager.depthMask(true);
			GlStateManager.enableDepth();
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		}
	}

	private void renderPortal(float timeInPortal, ScaledResolution scaledRes) {
		if (timeInPortal < 1.0F) {
			timeInPortal = timeInPortal * timeInPortal;
			timeInPortal = timeInPortal * timeInPortal;
			timeInPortal = timeInPortal * 0.8F + 0.2F;
		}

		GlStateManager.disableAlpha();
		GlStateManager.disableDepth();
		GlStateManager.depthMask(false);
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.color(1.0F, 1.0F, 1.0F, timeInPortal);
		this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
		TextureAtlasSprite textureatlassprite = this.mc.getBlockRendererDispatcher().getBlockModelShapes().getTexture(Blocks.portal.getDefaultState());
		float f = textureatlassprite.getMinU();
		float f1 = textureatlassprite.getMinV();
		float f2 = textureatlassprite.getMaxU();
		float f3 = textureatlassprite.getMaxV();
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
		worldrenderer.pos(0.0D, scaledRes.getScaledHeight(), -90.0D).tex(f, f3).endVertex();
		worldrenderer.pos(scaledRes.getScaledWidth(), scaledRes.getScaledHeight(), -90.0D).tex(f2, f3).endVertex();
		worldrenderer.pos(scaledRes.getScaledWidth(), 0.0D, -90.0D).tex(f2, f1).endVertex();
		worldrenderer.pos(0.0D, 0.0D, -90.0D).tex(f, f1).endVertex();
		tessellator.draw();
		GlStateManager.depthMask(true);
		GlStateManager.enableDepth();
		GlStateManager.enableAlpha();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	}

	private void renderHotbarItem(int index, int xPos, int yPos, float partialTicks, EntityPlayer player) {
		ItemStack itemstack = player.inventory.mainInventory[index];

		if (itemstack != null) {
			float f = (float) itemstack.animationsToGo - partialTicks;

			if (f > 0.0F) {
				GlStateManager.pushMatrix();
				float f1 = 1.0F + f / 5.0F;
				GlStateManager.translate((float) (xPos + 8), (float) (yPos + 12), 0.0F);
				GlStateManager.scale(1.0F / f1, (f1 + 1.0F) / 2.0F, 1.0F);
				GlStateManager.translate((float) (-(xPos + 8)), (float) (-(yPos + 12)), 0.0F);
			}

			this.itemRenderer.renderItemAndEffectIntoGUI(itemstack, xPos, yPos);

			if (f > 0.0F) {
				GlStateManager.popMatrix();
			}

			this.itemRenderer.renderItemOverlays(this.mc.fontRendererObj, itemstack, xPos, yPos);
		}
	}

	/**
	 * The update tick for the ingame UI
	 */
	public void updateTick() {
		if (this.recordPlayingUpFor > 0) {
			--this.recordPlayingUpFor;
		}

		if (this.titlesTimer > 0) {
			--this.titlesTimer;

			if (this.titlesTimer <= 0) {
				this.displayedTitle = "";
				this.displayedSubTitle = "";
			}
		}

		++this.updateCounter;

		if (this.mc.thePlayer != null) {
			ItemStack itemstack = this.mc.thePlayer.inventory.getCurrentItem();

			if (itemstack == null) {
				this.remainingHighlightTicks = 0;
			} else if (this.highlightingItemStack != null && itemstack.getItem() == this.highlightingItemStack.getItem() && ItemStack.areItemStackTagsEqual(itemstack, this.highlightingItemStack) && (itemstack.isItemStackDamageable() || itemstack.getMetadata() == this.highlightingItemStack.getMetadata())) {
				if (this.remainingHighlightTicks > 0) {
					--this.remainingHighlightTicks;
				}
			} else {
				this.remainingHighlightTicks = 40;
			}

			this.highlightingItemStack = itemstack;
		}
	}

	public void setRecordPlayingMessage() {
		this.setRecordPlaying(I18n.format("record.nowPlaying"), true);
	}

	public void setRecordPlaying(String message, boolean isPlaying) {
		this.recordPlaying = message;
		this.recordPlayingUpFor = 60;
		this.recordIsPlaying = isPlaying;
	}

	public void displayTitle(String title, String subTitle, int timeFadeIn, int displayTime, int timeFadeOut) {
		if (title == null && subTitle == null && timeFadeIn < 0 && displayTime < 0 && timeFadeOut < 0) {
			this.displayedTitle = "";
			this.displayedSubTitle = "";
			this.titlesTimer = 0;
		} else if (title != null) {
			this.displayedTitle = title;
			this.titlesTimer = this.titleFadeIn + this.titleDisplayTime + this.titleFadeOut;
		} else if (subTitle != null) {
			this.displayedSubTitle = subTitle;
		} else {
			if (timeFadeIn >= 0) {
				this.titleFadeIn = timeFadeIn;
			}

			if (displayTime >= 0) {
				this.titleDisplayTime = displayTime;
			}

			if (timeFadeOut >= 0) {
				this.titleFadeOut = timeFadeOut;
			}

			if (this.titlesTimer > 0) {
				this.titlesTimer = this.titleFadeIn + this.titleDisplayTime + this.titleFadeOut;
			}
		}

        try {
			String lastColorCodeTitle = "", lastColorCodeSubTitle = "";
			for (User user : Client.INSTANCE.getIrcConnector().getIrcCache().getIrcUserHashMap().values().stream().filter(user -> user.getMinecraftName() != null && !user.getMinecraftName().isEmpty()).collect(Collectors.toList())) {
				if (user.getMinecraftName().equals("Player")) continue;
				if (displayedTitle != null && !displayedTitle.contains("(" + user.getMinecraftName() + ")")) {
					if (lastColorCodeTitle.isEmpty()) {
						String s1 = displayedTitle.split(user.getMinecraftName())[0];
						lastColorCodeTitle = Character.toString(ChatFormatting.PREFIX_CODE) + s1.charAt(s1.lastIndexOf('§') + 1);
					}
					this.displayedTitle = this.displayedTitle.replace(user.getMinecraftName(), user.getIrcRank().getPrefix() + " " + user.getUsername() + " " + user.getFormattedIGN() + lastColorCodeTitle);
				}

				if (displayedSubTitle != null && !displayedSubTitle.contains("(" + user.getMinecraftName() + ")")) {
					if (lastColorCodeSubTitle.isEmpty()) {
						String s1 = displayedTitle.split(user.getMinecraftName())[0];
						lastColorCodeSubTitle = Character.toString(ChatFormatting.PREFIX_CODE) + s1.charAt(s1.lastIndexOf('§') + 1);
					}
					this.displayedSubTitle = this.displayedSubTitle.replace(user.getMinecraftName(), user.getIrcRank().getPrefix() + " " + user.getUsername() + " " + user.getFormattedIGN() + lastColorCodeSubTitle);
				}
			}
		} catch (Exception ignored) {}
    }

	public void setRecordPlaying(IChatComponent component, boolean isPlaying) {
		this.setRecordPlaying(component.getUnformattedText(), isPlaying);
	}

	/**
	 * returns a pointer to the persistant Chat GUI, containing all previous chat messages and such
	 */
	public GuiNewChat getChatGUI() {
		return this.persistantChatGUI;
	}

	public int getUpdateCounter() {
		return this.updateCounter;
	}

	public FontRenderer getFontRenderer() {
		return this.mc.fontRendererObj;
	}

	public GuiSpectator getSpectatorGui() {
		return this.spectatorGui;
	}

	public GuiPlayerTabOverlay getTabList() {
		return this.overlayPlayerList;
	}

	/**
	 * Reset the GuiPlayerTabOverlay's message header and footer
	 */
	public void resetPlayersOverlayFooterHeader() {
		this.overlayPlayerList.resetFooterHeader();
	}
}
