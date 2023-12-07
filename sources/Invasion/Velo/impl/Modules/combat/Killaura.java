package Velo.impl.Modules.combat;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.apache.commons.lang3.RandomUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.util.BytesTrie.Iterator;

import Velo.api.Friend.FriendManager;
import Velo.api.Main.Main;
import Velo.api.Module.Module;
import Velo.api.Util.Other.ChatUtil;
import Velo.api.Util.Other.ColorUtil;
import Velo.api.Util.Other.Colors;
import Velo.api.Util.Other.MathUtils;
import Velo.api.Util.Other.MovementUtil;
import Velo.api.Util.Other.RotationUtils;
import Velo.api.Util.Other.Timer;
import Velo.api.Util.Other.UrlTextureUtil;
import Velo.api.Util.Other.other.BlockingUtil;
import Velo.api.Util.Other.other.RaycastUtil;
import Velo.api.Util.Other.other.RotationUtil;
import Velo.api.Util.Render.BlurUtil;
import Velo.api.Util.Render.RenderUtil;
import Velo.api.Util.Render.RenderUtils;
import Velo.api.Util.Render.ScaledRes;
import Velo.api.Util.fontRenderer.Fonts;
import Velo.api.Util.fontRenderer.Utils.Tff;
import Velo.api.Util.menu.Element.AnimationUtils;
import Velo.impl.Event.EventMovement;
import Velo.impl.Event.EventPostMotion;
import Velo.impl.Event.EventPreMotion;
import Velo.impl.Event.EventReceivePacket;
import Velo.impl.Event.EventRender;
import Velo.impl.Event.EventRender3D;
import Velo.impl.Event.EventStrafe;
import Velo.impl.Event.EventUpdate;
import Velo.impl.Settings.BooleanSetting;
import Velo.impl.Settings.ModeSetting;
import Velo.impl.Settings.NumberSetting;
import Velo.impl.Modules.visuals.Blur;
import Velo.impl.Modules.visuals.CustomVelo;
import Velo.impl.Modules.visuals.ESP2D;
import Velo.impl.Modules.visuals.hud.HUD;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C02PacketUseEntity.Action;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S09PacketHeldItemChange;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class Killaura extends Module {

	public static ModeSetting mode = new ModeSetting("Mode", "Single", "Single", "Switch");
	public static ModeSetting attackmode = new ModeSetting("AttackMode", "Post", "Pre", "Post", "AAC");
	public static ModeSetting rotationsmode = new ModeSetting("Rotation", "Normal", "Normal", "Basic", "Smooth",
			"NoRotate", "Hypixel", "AAC");
	public static ModeSetting autoblockmode = new ModeSetting("Autoblock", "Post", "Pre", "Post", "CustomPre",
			"CustomPost", "Hypixel", "NCP", "Off");
	public static ModeSetting swingmode = new ModeSetting("SwingMode", "Client", "Client", "Server", "Off");
	private double lastPosX;
	private double lastPosZ;
	public ArrayList<Double> distances;
	private int y = 200;
	boolean up = true;
	public boolean block;
	public static boolean rayca = false;
	private int x = 600;
	private int targetX = 400, targetY = 300, bpsX = 0, bpsY = 440, playerListX = 30, playerListY = 30, pingX = 0,
			pingY = 425, coordX = 0, coordY = 425, labelX = 2, labelY = 2, buildInfoX = 0, buildInfoY = 400, size = 16,
			potionsX = 37,
			potionsY = (new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight() - (230) - size * 2) - 5,
			moduleListX = new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth(), moduleListY = 0;
	public static ModeSetting targets1 = new ModeSetting("Targets", "Players", "Players", "Animals", "Mobs",
			"Villagers", "All");
	public static ModeSetting thmode = new ModeSetting("TargetHud Mode", "Off", "Off", "Invasion", "Novoline", "Moon New",
			"Moon 3.0", "Moon Old", "Astolfo", "Flux", "Exhibition");
	public static NumberSetting range = new NumberSetting("Range", 4, 0, 15, 0.01);
	public static NumberSetting fov = new NumberSetting("Fov", 360, 0, 360, 10);
	public static NumberSetting smoothrotslowness = new NumberSetting("Smooth Rotations Slowness", 5, 1, 75, 1);
	public static NumberSetting maxcps = new NumberSetting("Max Cps", 11, 1, 20, 1);
	public static NumberSetting mincps = new NumberSetting("Min Cps", 11, 1, 20, 1);
	public static NumberSetting autoblockdelay = new NumberSetting("CustomBlockDelay", 75, 0, 151, 1);
	public static NumberSetting cracksize = new NumberSetting("CrackSize", 0, 0, 15, 1);
	public static NumberSetting enchantcracksize = new NumberSetting("EnchantCrackSize", 0, 0, 15, 1);
	public static BooleanSetting targetesp = new BooleanSetting("TargetEsp", true);
	public static BooleanSetting raycast = new BooleanSetting("Raycast", true);
	public static BooleanSetting strafefix = new BooleanSetting("StrafeFix", true);
	public static BooleanSetting cubecraft = new BooleanSetting("Cubecraft", false);
	public static BooleanSetting disableAuto = new BooleanSetting("Disable Automatically", true),
			players = new BooleanSetting("Players", true), mobs = new BooleanSetting("Mobs", false),
			Animals = new BooleanSetting("Animals", false), Villagers = new BooleanSetting("Villagers", false);

	Timer timer = new Timer();

	// Extra variables
	public static boolean isEnabled = false;
	public static float yaw, pitch;
	public static float strafe = 0, forward = 0, fricition = 0;
	public static float yaw2 = 0, pitch2 = 0;
	public static Entity currentEntity = null;
	private static EntityLivingBase lastTarget = null;
	public static EntityLivingBase target = null;
	public static transient double healthBarTarget = 0, healthBar = 0, hurtTime = 200, hurtTimeTarget = 0;
	public Fonts font1 = new Fonts();
	public static ModeSetting sortmode = new ModeSetting("SortMode", "Distance", "Distance", "Health", "Angle");
	private final String[] winGameStrings = new String[] { "1st Killer - ", "1st Place - ",
			"You died! Want to play again? Click here!", " - Damage Dealt - ", "1st - ", "Winning Team - ", "Winners: ",
			"Winner: ", "Winning Team: ", " win the game!", "1st Place: ", "Last team standing!", "Winner #1 (",
			"Top Survivors", "Winners - ", "Winner" };

	public Killaura() {
		super("Killaura", "Killaura", Keyboard.KEY_R, Category.COMBAT);
		this.img = null;
		this.hasTriedToDownload = false;
		this.loadSettings(mode, attackmode, rotationsmode, autoblockmode, swingmode, sortmode, targets1, range, fov,
				smoothrotslowness, maxcps, mincps, autoblockdelay, cracksize, enchantcracksize, targetesp, raycast, strafefix, cubecraft,
				thmode);
	}

	@Override
	public void onEnable() {
		y = 200;
		x = 600;
		currentEntity = null;
		target = null;
		isEnabled = false;
		// Velo.fontrenderer.utils.DrawFontUtil.loadFonts();
		// Fonts.loadFonts();
		hurtTime = new ScaledResolution(mc).getScaledWidth() / 2 + 41;
		hurtTimeTarget = new ScaledResolution(mc).getScaledWidth() / 2 + 41;
	}

	@Override
	public void onDisable() {
		currentEntity = null;
		target = null;
		isEnabled = false;

	}

	private ResourceLocation img;
	private boolean hasTriedToDownload;

	@Override
	public void onEventReceivePacket(EventReceivePacket event) {

		if (((EventReceivePacket) event).getPacket() instanceof S02PacketChat && disableAuto.isEnabled()) {
			for (String string : winGameStrings) {
				if (((S02PacketChat) ((EventReceivePacket) event).getPacket()).getChatComponent().getUnformattedText()
						.contains(string))
					if (this.isEnabled())
						this.toggle();
			}
		}

	}

	@Override
	public void onRenderUpdate(EventRender event) {

		if (thmode.equalsIgnorecase("Off"))
			return;
		if (Killaura.target == null) {
			healthBar = 0;

			return;
		}

		double barSpeed = 75;
		ScaledResolution sr = new ScaledResolution(mc);
		Tff fr = font1.Hud;
		if (healthBar > healthBarTarget) {
			healthBar = ((healthBar) - ((healthBar - healthBarTarget) / barSpeed));
		} else if (healthBar < healthBarTarget) {
			healthBar = ((healthBar) + ((healthBarTarget - healthBar) / barSpeed));
		}
		if (hurtTime > hurtTimeTarget) {
			hurtTime = ((hurtTime) - ((hurtTime - hurtTimeTarget) / barSpeed));
		} else if (hurtTime < healthBarTarget) {
			hurtTime = ((hurtTime) + ((hurtTimeTarget - hurtTime) * barSpeed));
		}

		float[] hsb = Color.RGBtoHSB(((int) HUD.red.getValue()), ((int) HUD.green.getValue()),
				((int) HUD.blue.getValue()), null);
		// float[] hsb = Color.RGBtoHSB(((int) HUD.red.getValue(), (int)
		// HUD.green.getValue(), (int) HUD.blue.getValue(), 255).getRGB()), null);
		float hue = hsb[0];
		float saturation = hsb[1];
		int color = Color.HSBtoRGB(hue, saturation, 1);
		;
		// if(mode.equalsIgnorecase("Novoline")) {
		if (HUD.colormode.equalsIgnorecase("Rainbow")) {
			float hue1 = System.currentTimeMillis() % (int) ((1000.5f - 100) * 1000) / (float) ((10.5f - 8) * 1000);
			color = Color.HSBtoRGB(hue1, .65f, 1);
		}

		if (HUD.colormode.equalsIgnorecase("Fade")) {
			for (int i = 0; i < 85; i++) {
				float hue1 = System.currentTimeMillis() % (int) ((100.5f - 9) * 1000) / (float) ((100.5f - 8) * 1000);
				color = ColorUtil.getGradientOffset(
						new Color((int) HUD.red2.getValue(), (int) HUD.green2.getValue(), (int) HUD.blue2.getValue(),
								255),
						new Color((int) HUD.red.getValue(), (int) HUD.green.getValue(), (int) HUD.blue.getValue(), 255),
						(Math.abs(((System.currentTimeMillis()) / 7 - i)) / 120D)).getRGB();
			}
		}

		if (thmode.equalsIgnorecase("Exhibition")) {

			Tff pjes = Fonts.Hud;

			DecimalFormat decimalFormat = new DecimalFormat("#.#");
			drawExhiRect(x, y, x + 130, y + 40);
			pjes.drawString(target.getName(), (int) x + 3, (int) y - pjes.FONT_HEIGHT + 32, -1);// +37
			Gui.drawRect(x + 3 - 1.5f, y + 35 - 1.5f, x + 127 + 1.5f, y + 37 + 1.5f, new Color(24, 22, 22).getRGB());
			float width = (x + 127 + 1.5f) - (x + 3 - 1.5f);
			float pjesjesjedzony = width / 5;
			Gui.drawRect(x + 3, y + 35, x + 127, y + 37, new Color(56, 54, 54).getRGB());
			for (int i = 0; i < 5; i++) {
				Gui.drawRect(x + 3 - 1.5f + pjesjesjedzony * i, y + 35, x + 3 - 1.5f + pjesjesjedzony * i + 1, y + 37,
						new Color(28, 26, 26).getRGB());
			}
			pjes.drawString(decimalFormat.format(mc.thePlayer.getDistanceToEntity(target)) + " m", (int) x + 3,
					(int) y - pjes.FONT_HEIGHT + 21, -1);
			GlStateManager.pushMatrix();
			GlStateManager.scale(2, 2, 2);
			float health = target.getHealth();
			pjes.drawString(decimalFormat.format(health) + "",
					(int) (x + 128 - (pjes.getStringWidth(decimalFormat.format(health) + " Ã¢ï¿½Â¤")) * 2) / 2,
					(int) (y - pjes.FONT_HEIGHT + 20) / 2, -1);
			mc.fontRendererObj.drawString("â�¤",
					(int) (x + 144 - (pjes.getStringWidth(decimalFormat.format(health) + " Ã¢ï¿½Â¤")) * 2) / 2,
					(int) (y - pjes.FONT_HEIGHT + 24) / 2, -1);
			GlStateManager.popMatrix();
		}

		if (thmode.equalsIgnorecase("Novoline")) {
			// Main box
			Gui.drawRect(610, 300, 810 + mc.fontRendererObj.getStringWidth(Killaura.target.getName()) - 110, 350,
					0x9036393f);
			// Health
			mc.fontRendererObj.drawStringWithShadow(Math.round(target.getHealth()) + " â�¤", 668, 338f, color);
			healthBarTarget = (mc.fontRendererObj.getStringWidth(Killaura.target.getName() + 30)
					* (Killaura.target.getHealth() / Killaura.target.getMaxHealth()));
			if (healthBar > mc.fontRendererObj.getStringWidth(Killaura.target.getName()) + 30) {
				healthBar = mc.fontRendererObj.getStringWidth(Killaura.target.getName()) + 30;
			}
			// Gui.drawRect(60, 27, 160, 42.5f, 0xb836393f);
			// Gui.drawRect(60, 27, 20 + healthBar, 42.5f, color);
			Gui.drawRect(665, 335, 665, 322, 0xb836393f);
			Gui.drawRect(665, 335, 665 + healthBar, 322, color);
			// Target Name
			font1.Hud.drawString(Killaura.target.getName(), 665, 310, color);
			// Picture of target
			GlStateManager.color(1, 1, 1);
			if (this.img == null && !this.hasTriedToDownload) {
				this.hasTriedToDownload = true;
				UrlTextureUtil.downloadAndSetTexture("https://mc-heads.net/avatar/" + target.getName(),
						new UrlTextureUtil.ResourceLocationCallback() {

							@Override
							public void onTextureLoaded(final ResourceLocation rl) {
								Killaura.access$0(Killaura.this, rl);
							}
						});
			}
			if (this.img != null) {
				final int b = 50;
				GL11.glPushMatrix();
				this.mc.getTextureManager().bindTexture(this.img);
				Gui.drawModalRectWithCustomSizedTexture(610, 300, 0.6f, 0.6f, b, b, (float) b, (float) b);
				GL11.glPopMatrix();
			}
		} else if (thmode.equalsIgnorecase("Moon New")) {
			// Mainbox
			if (!(target instanceof EntityPlayer))
				return;
			
			int count3 = 0;
			for(float i = 0; i < font1.targethudName.getStringWidth(Killaura.target.getName()) - 15 + 1; i++) {
				if(HUD.colormode.equalsIgnorecase("Custom")) {
					Gui.drawRect(633 + i, 313.5, 700 + i, 315, new Color((int) HUD.red.getValue(), (int) HUD.green.getValue(), (int) HUD.blue.getValue(), 255).getRGB());
				}
				if(HUD.colormode.equalsIgnorecase("Fade")) {
					Gui.drawRect(633 + i, 313.5, 700 + i, 315, ColorUtil.getGradientOffset(new Color((int) HUD.red2.getValue(), (int) HUD.green2.getValue(), (int) HUD.blue2.getValue(), 255), new Color((int) HUD.red.getValue(), (int) HUD.green.getValue(), (int) HUD.blue.getValue(), 255), (Math.abs(((System.currentTimeMillis()) / 7D - i*4)) / 120D)).getRGB());
				}
				if(HUD.colormode.equalsIgnorecase("Astolfo")) {
					Gui.drawRect(633 + i, 313.5, 700 + i, 315, ColorUtil.astolfoColors((int) i, 1000));
				}
				if(HUD.colormode.equalsIgnorecase("Rainbow")) {
					Gui.drawRect(633 + i, 313.5, 700 + i, 315, ColorUtil.getRainbow(12, 0.5F, 1, (long) i*30));
				}
				count3++;
			}
			
			if(Blur.isEnabled) {
				BlurUtil.blurAreaBoarder(633, 315, font1.targethudName.getStringWidth(Killaura.target.getName()) + 52, 38,
						5);
			} else {
				Gui.drawRect(633, 315, 700 + font1.targethudName.getStringWidth(Killaura.target.getName()) - 15, 355,
						0x39000000);
			}
			// Health
			font1.targethud.drawString(Math.round(target.getHealth()) + ".0" + " HP", 667, 329, -1);
			healthBarTarget = (font1.targethudName.getStringWidth(Killaura.target.getName())
					+ 50 * (Killaura.target.getHealth() / Killaura.target.getMaxHealth()));
			if (healthBar > mc.fontRendererObj.getStringWidth(Killaura.target.getName())
					* (Killaura.target.getHealth() / Killaura.target.getMaxHealth())) {
				// healthBar = mc.fontRendererObj.getStringWidth(Killaura.target.getName()) *
				// (Killaura.target.getHealth() / Killaura.target.getMaxHealth());
			}

			Gui.drawRect(635, 350.7, 635 + font1.targethudName.getStringWidth(Killaura.target.getName()) + 50, 348.6,
					0x50000000);
			Gui.drawRect(635, 350, 635 + healthBar, 348.8, Color.GREEN.getRGB());
			// HurtTime
			hurtTimeTarget = font1.targethudName.getStringWidth(Killaura.target.getName()) + 50 - (font1.targethudName
					.getStringWidth(Killaura.target.getName())
					+ 30 * ((float) Killaura.target.hurtResistantTime / (float) Killaura.target.maxHurtResistantTime));
			if (hurtTime > font1.targethudName.getStringWidth(Killaura.target.getName()) + 50) {
				hurtTime = font1.targethudName.getStringWidth(Killaura.target.getName()) + 50;
			}
			Gui.drawRect(635, 353.7, 635 + font1.targethudName.getStringWidth(Killaura.target.getName()) + 50, 351.6,
					0x50000000);
			Gui.drawRect(635, 353, 635 + hurtTime, 351.8, 0xff1188ff);
			// Target Name
			font1.targethudName.drawString(Killaura.target.getName(), 667, 315, -1);
			// Image
			if (this.img == null && !this.hasTriedToDownload) {
				this.hasTriedToDownload = true;
				UrlTextureUtil.downloadAndSetTexture("https://mc-heads.net/avatar/" + target.getName(),
						new UrlTextureUtil.ResourceLocationCallback() {
							@Override
							public void onTextureLoaded(final ResourceLocation rl) {
								Killaura.access$0(Killaura.this, rl);
							}
						});
			}
			if (this.img != null) {
				final int b = 30;
				this.mc.getTextureManager().bindTexture(this.img);
				//Gui.drawModalRectWithCustomSizedTexture(635, 317, .5f, .5f, b, b, (float) b, (float) b);
			}
			
			GuiInventory.drawEntityOnScreen(650, 347, (int) (30 / Killaura.currentEntity.height) + 0, 0, 0,
					(EntityLivingBase) Killaura.currentEntity);
			
			NetworkPlayerInfo playerInfo = mc.getNetHandler().getPlayerInfo(target.getUniqueID());
			if (playerInfo != null)
			{
			    mc.getTextureManager().bindTexture(playerInfo.getLocationSkin());
			    GL11.glColor4f(1F, 1F, 1F, 1F);
			    //Gui.drawScaledCustomSizeModalRect(635, 317, 8f, 8f, 8, 8, 30, 29, 64F, 64F);
			}

		} else if (thmode.equalsIgnorecase("Astolfo")) {
			// Main box + Line on top
			Gui.drawRect(534, 300, 600 + mc.fontRendererObj.getStringWidth(Killaura.target.getName()) - 10, 343,
					0x9036393f);
			// Health

			healthBarTarget = (mc.fontRendererObj.getStringWidth(Killaura.target.getName() + 30)
					* (Killaura.target.getHealth() / Killaura.target.getMaxHealth()));
			if (healthBar > mc.fontRendererObj.getStringWidth(Killaura.target.getName()) + 30) {
				healthBar = mc.fontRendererObj.getStringWidth(Killaura.target.getName()) + 30;
			}
			Gui.drawRect(565, 330, 565, 337, 0xb836393f);
			Gui.drawRect(565, 330, 565 + healthBar, 337, color);
			;
			// Target Name
			GlStateManager.pushMatrix();
			GlStateManager.scale(0.9, 0.9, 0.9);

			mc.fontRendererObj.drawStringWithShadow(Killaura.target.getName(), 630, 340, color);
			GlStateManager.popMatrix();
			// 3D model of the target With no name
			GlStateManager.color(1, 1, 1);
			GuiInventory.drawEntityOnScreenWithoutName(550, 340, (int) (30 / Killaura.currentEntity.height) + 3, 0, 0,
					(EntityLivingBase) Killaura.currentEntity);
			GlStateManager.pushMatrix();

			GlStateManager.scale(1.4, 1.4, 1.4);
			mc.fontRendererObj.drawStringWithShadow(Math.round(target.getHealth()) + " â�¤", 405, 225f, color);
			GlStateManager.popMatrix();

		} else if (thmode.equalsIgnorecase("Moon Old")) {
			float x = (event.getScaledResolution().getScaledWidth() >> 1) - 5;
			float y = (event.getScaledResolution().getScaledHeight() >> 1) + 120;
			if (target != null) {
				if (mc.thePlayer != null && target instanceof EntityPlayer) {
					ChatUtil.addChatMessage("pp");
					NetworkPlayerInfo networkPlayerInfo = mc.getNetHandler().getPlayerInfo(target.getUniqueID());
					final String ping = "Ping: "
							+ (Objects.isNull(networkPlayerInfo) ? "0ms" : networkPlayerInfo.getResponseTime() + "ms");
					final String playerName = "Name: "
							+ net.minecraft.util.StringUtils.stripControlCodes(target.getName());
					RenderUtil.drawBorderedRect(x, y, 140, 45, 0.5f, new Color(0, 0, 0, 255).getRGB(),
							new Color(0, 0, 0, 90).getRGB());
					Gui.drawRect(x, y, 45, 45, new Color(0, 0, 0).getRGB());
					font1.targethud.drawStringWithShadow(playerName, x + 46.5f, y + 4, -1);
					font1.targethud.drawStringWithShadow(
							"Distance: " + MathUtils.round(mc.thePlayer.getDistanceToEntity(target), 2), x + 46.5f,
							y + 12, -1);
					font1.targethud.drawStringWithShadow(ping, x + 46.5f, y + 28, new Color(0x5D5B5C).getRGB());
					font1.targethud.drawStringWithShadow(
							"Health: " + MathUtils
									.round((Float.isNaN(target.getHealth()) ? 20 : target.getHealth()) / 2, 2),
							x + 46.5f, y + 20, getHealthColor(target));
					drawFace(x + 0.5, y + 0.5, 8, 8, 8, 8, 44, 44, 64, 64, (AbstractClientPlayer) target);
					RenderUtil.drawBorderedRect1(x + 46, y + 45 - 10, 92, 8, 0.5, new Color(0).getRGB(),
							new Color(35, 35, 35).getRGB());
					double inc = 91 / target.getMaxHealth();
					double end = inc * (Math.min(target.getHealth(), target.getMaxHealth()));
					Gui.drawRect(x + 46.5, y + 45 - 9.5, end, 7, getHealthColor(target));
				}
			}
		} else

		if (thmode.equalsIgnorecase("Moon 3.0")) {
			// Mainbox
			if (!(target instanceof EntityPlayer))
				return;
			
			
			if(Blur.isEnabled) {
				BlurUtil.blurAreaBoarder(633, 315, font1.targethudName.getStringWidth(Killaura.target.getName()) + 52, 38,
						5);
			} else {
				Gui.drawRect(633, 315, 700 + font1.targethudName.getStringWidth(Killaura.target.getName()) - 15, 355,
						0x39000000);
			}
			// Health
			font1.targethud.drawString("Health: " + Math.round(target.getHealth()) + ".0", 667, 329, -1);
			healthBarTarget = (font1.targethudName.getStringWidth(Killaura.target.getName())
					+ 50 * (Killaura.target.getHealth() / Killaura.target.getMaxHealth()));
			if (healthBar > mc.fontRendererObj.getStringWidth(Killaura.target.getName())
					* (Killaura.target.getHealth() / Killaura.target.getMaxHealth())) {
				// healthBar = mc.fontRendererObj.getStringWidth(Killaura.target.getName()) *
				// (Killaura.target.getHealth() / Killaura.target.getMaxHealth());
			}

			Gui.drawRect(635, 350.7, 635 + font1.targethudName.getStringWidth(Killaura.target.getName()) + 50, 348.6,
					0x50000000);
			Gui.drawRect(635, 350, 635 + healthBar, 348.8, Color.GREEN.getRGB());
			// HurtTime
			hurtTimeTarget = font1.targethudName.getStringWidth(Killaura.target.getName()) + 50 - (font1.targethudName
					.getStringWidth(Killaura.target.getName())
					+ 30 * ((float) Killaura.target.hurtResistantTime / (float) Killaura.target.maxHurtResistantTime));
			if (hurtTime > font1.targethudName.getStringWidth(Killaura.target.getName()) + 50) {
				hurtTime = font1.targethudName.getStringWidth(Killaura.target.getName()) + 50;
			}
			Gui.drawRect(635, 353.7, 635 + font1.targethudName.getStringWidth(Killaura.target.getName()) + 50, 351.6,
					0x50000000);
			Gui.drawRect(635, 353, 635 + hurtTime, 351.8, 0xff1188ff);
			// Target Name
			font1.targethudName.drawString(Killaura.target.getName(), 667, 315, -1);
			// Image
			if (this.img == null && !this.hasTriedToDownload) {
				this.hasTriedToDownload = true;
				UrlTextureUtil.downloadAndSetTexture("https://mc-heads.net/avatar/" + target.getName(),
						new UrlTextureUtil.ResourceLocationCallback() {
							@Override
							public void onTextureLoaded(final ResourceLocation rl) {
								Killaura.access$0(Killaura.this, rl);
							}
						});
			}
			if (this.img != null) {
				final int b = 30;
				this.mc.getTextureManager().bindTexture(this.img);
				Gui.drawModalRectWithCustomSizedTexture(635, 317, .5f, .5f, b, b, (float) b, (float) b);
			}
			
			NetworkPlayerInfo playerInfo = mc.getNetHandler().getPlayerInfo(target.getUniqueID());
			if (playerInfo != null)
			{
			    mc.getTextureManager().bindTexture(playerInfo.getLocationSkin());
			    GL11.glColor4f(1F, 1F, 1F, 1F);
			    Gui.drawScaledCustomSizeModalRect(635, 317, 8f, 8f, 8, 8, 30, 29, 64F, 64F);
			}

		} else if (thmode.equalsIgnorecase("Flux")) {
			// Mainbox
			Gui.drawRect(623, 315, 777 + mc.fontRendererObj.getStringWidth(Killaura.target.getName() + 30) - 110, 365,
					0x9036393f);
			// Health
			font1.targethud.drawString("Health: " + Math.round(target.getHealth()), 657, 335, -1);
			healthBarTarget = (mc.fontRendererObj.getStringWidth(Killaura.target.getName() + 340)
					* (Killaura.target.getHealth() / Killaura.target.getMaxHealth()));
			if (healthBar > mc.fontRendererObj.getStringWidth(Killaura.target.getName()) + 340) {
				healthBar = mc.fontRendererObj.getStringWidth(Killaura.target.getName()) + 340;
			}

			Gui.drawRect(635, 353, 635, 348.8, 0xb836393f);
			Gui.drawRect(635, 353, 635 + healthBar, 348.8, Color.GREEN.getRGB());
			// HurtTime

			// drawHead(mc.getNetHandler().getPlayerInfo(target.getUniqueID()).getLocationSkin(),
			// 635, 317);
			mc.fontRendererObj.drawStringWithShadow("ï¿½?ï¿½", 625, 348, -1);
			mc.fontRendererObj.drawStringWithShadow("Ãƒ", 625, 356, -1);

			// Gui.drawRect(635, 363, 762, 357.8, 0xb836393f);
			// Gui.drawRect(635, 365, 622 + (140 * (target.getTotalArmorValue() / 20)),
			// 359.8, 0xff1188ff);

			Gui.drawRect(635, 363, 635, 358.8, 0xb836393f);
			Gui.drawRect(635, 363,
					601 + target.getTotalArmorValue() + mc.fontRendererObj.getStringWidth(Killaura.target.getName()),
					358.8, 0xff1188ff);

			// Target Name
			font1.targethudName.drawString(Killaura.target.getName(), 657, 320, -1);
			// Image
			// Gui.drawRect(665 + 1, 315,633,349, 0x55008800);

			if (this.img == null && !this.hasTriedToDownload) {
				this.hasTriedToDownload = true;
				UrlTextureUtil.downloadAndSetTexture("https://mc-heads.net/avatar/" + target.getName(),
						new UrlTextureUtil.ResourceLocationCallback() {
							@Override
							public void onTextureLoaded(final ResourceLocation rl) {
								Killaura.access$0(Killaura.this, rl);
							}
						});
			}
			if (this.img != null) {
				final int b = 30;
				GlStateManager.color(1, 1, 1);
				this.mc.getTextureManager().bindTexture(this.img);
				Gui.drawModalRectWithCustomSizedTexture(625, 317, .5f, .5f, b, b, (float) b, (float) b);

			}

		} else if (thmode.equalsIgnorecase("Invasion")) {

			// Main box
			GlStateManager.pushMatrix();

			Gui.drawRect(630, 300, 780 + mc.fontRendererObj.getStringWidth(Killaura.target.getName()) - 70, 343,
					0x9036393f);
			Gui.drawRect(630, 300, 780 + mc.fontRendererObj.getStringWidth(Killaura.target.getName()) - 70, 302, color);
			Gui.drawRect(630, 343, 632, 302, color);
			Gui.drawRect(630, 343, 780 + mc.fontRendererObj.getStringWidth(Killaura.target.getName()) - 70, 341, color);
			Gui.drawRect(782 + mc.fontRendererObj.getStringWidth(Killaura.target.getName()) - 70, 300,
					780 + mc.fontRendererObj.getStringWidth(Killaura.target.getName()) - 70, 343, color);

			// Health bar
			float rhealth = target.getHealth();
			float health = Math.round(rhealth);
			font1.targethud.drawString(health + " HP", 668, 315f, -1);
			healthBarTarget = (mc.fontRendererObj.getStringWidth(Killaura.target.getName() + 30)
					* (Killaura.target.getHealth() / Killaura.target.getMaxHealth()));
			if (healthBar > mc.fontRendererObj.getStringWidth(Killaura.target.getName()) + 30) {
				healthBar = mc.fontRendererObj.getStringWidth(Killaura.target.getName()) + 30;
			}
			Gui.drawRect(665, 330, 665, 328, 0xb836393f);
			Gui.drawRect(665, 330, 665 + healthBar, 328, Color.GREEN.getRGB());
			// Hurt time

			hurtTimeTarget = mc.fontRendererObj.getStringWidth(Killaura.target.getName()) + 30 - (mc.fontRendererObj
					.getStringWidth(Killaura.target.getName())
					+ 30 * ((float) Killaura.target.hurtResistantTime / (float) Killaura.target.maxHurtResistantTime));
			if (hurtTime > mc.fontRendererObj.getStringWidth(Killaura.target.getName()) + 30) {
				hurtTime = mc.fontRendererObj.getStringWidth(Killaura.target.getName()) + 30;
			}

			Gui.drawRect(665, 335, 665, 333, 0xb836393f);
			Gui.drawRect(665, 335, 665 + hurtTime, 333, 0xff1188ff);
			// Name
			font1.targethudName.drawString(Killaura.target.getName(), 665, 300, -1);
			// 3D model of the target
			GlStateManager.color(1, 1, 1);
			GuiInventory.drawEntityOnScreen(650, 340, (int) (30 / Killaura.currentEntity.height) + 3, 0, 0,
					(EntityLivingBase) Killaura.currentEntity);
			GlStateManager.popMatrix();
		} else if (thmode.equalsIgnorecase("Astolfo")) {
			// Line on top + Main Box
			Gui.drawRect(630, 300, 775, 343, 0x9036393f);
			Gui.drawRect(630, 300, 775, 302, color);
			// Health bar
			font1.targethud.drawString(target.getHealth() + " HP", 668, 315f, -1);
			healthBarTarget = (140 * (Killaura.target.getHealth() / Killaura.target.getMaxHealth()));
			if (healthBar > 140) {
				healthBar = 140;
			}
			// Health
			Gui.drawRect(665, 330, 765, 328, 0xb836393f);
			Gui.drawRect(665, 330, 665 + healthBar, 328, Color.GREEN.getRGB());
			// TargetName
			font1.targethudName.drawString(Killaura.target.getName(), 665, 300, -1);
			// 3D Model
			GuiInventory.drawEntityOnScreenWithoutName(650, 340, (int) (30 / Killaura.currentEntity.height) + 3, 0, 0,
					(EntityLivingBase) Killaura.currentEntity);
		}
		// Credits To Autumn Client For Code
		if (thmode.equalsIgnorecase("Autumn")) {
			if (this.target != null) {
				float scaledWidth = (float) event.getWidth();
				float scaledHeight = (float) event.getHeight();
				double healthBarWidth = 100;
				double hudHeight = 120;
				float width = 180.0F;
				float height = 80.0F;
				final Color COLOR = new Color(0, 0, 0, 180);
				final Timer animationStopwatch = new Timer();
				float xOffset = 40.0F;
				float x = scaledWidth / 2.0F + 70.0F;
				float y = scaledHeight / 2.0F + 80.0F;
				float health = this.target.getHealth();
				double hpPercentage = (double) (health / this.target.getMaxHealth());
				hpPercentage = MathHelper.clamp_double(hpPercentage, 0.0D, 1.0D);
				double hpWidth = 92.0D * hpPercentage;
				int healthColor = ColorUtil.getHealthColor(this.target.getHealth(), this.target.getMaxHealth())
						.getRGB();
				String healthStr = String.valueOf((float) ((int) this.target.getHealth()) / 2.0F);
				if (animationStopwatch.hasTimedElapsed(15L, true)) {
					healthBarWidth = AnimationUtils.animate(hpWidth, healthBarWidth, 0.3529999852180481D);
					hudHeight = AnimationUtils.animate(40.0D, hudHeight, 0.10000000149011612D);
					animationStopwatch.reset();
				}
				GL11.glEnable(3089);
				RenderUtil.prepareScissorBox(x, y, x + 140.0F, (float) ((double) y + hudHeight));
				Gui.drawRect((double) x, (double) y, (double) (x + 140.0F), (double) (y + 40.0F), COLOR.getRGB());
				Gui.drawRect((double) (x + 40.0F), (double) (y + 15.0F), (double) (x + 40.0F) + healthBarWidth,
						(double) (y + 25.0F), healthColor);
				mc.fontRendererObj.drawStringWithShadow(healthStr,
						x + 40.0F + 46.0F - (float) mc.fontRendererObj.getStringWidth(healthStr) / 2.0F, y + 16.0F, -1);
				mc.fontRendererObj.drawStringWithShadow(this.target.getName(), x + 40.0F, y + 2.0F, -1);
				GuiInventory.drawEntityOnScreen((int) (x + 13.333333F), (int) (y + 40.0F), 20, this.target.rotationYaw,
						this.target.rotationPitch, this.target);
				GL11.glDisable(3089);
			} else {
				double healthBarWidth;
				double hudHeight;
				healthBarWidth = 92.0D;
				hudHeight = 0.0D;
				this.target = null;

			}
		}
		super.onRenderUpdate(event);
	}

	public void onPreMotionUpdate(EventPreMotion event) {
		if (target != null) {
			event.setYaw(yaw);
			event.setPitch(pitch);
			CustomVelo.lastTickPitch = CustomVelo.pitch;
			CustomVelo.pitch = pitch;
		}
	}

	float height = 0;

	public void onRender3DUpdate(EventRender3D event) {
		List<Entity> targets = mc.theWorld.loadedEntityList.stream().filter(EntityLivingBase.class::isInstance)
				.collect(Collectors.toList());
		targets = targets.stream().filter(entity -> entity.getDistanceToEntity(mc.thePlayer) < range.getValue()
				&& entity != mc.thePlayer && !entity.isDead).collect(Collectors.toList());

		if (mode.equalsIgnorecase("Switch")) {
			if (sortmode.equalsIgnorecase("Distance")) {
				targets.sort(Comparator
						.comparingDouble(entity -> ((EntityLivingBase) entity).getDistanceToEntity(mc.thePlayer)));
			}
			if (sortmode.equalsIgnorecase("Health")) {
				targets.sort(Comparator.comparingDouble(entity -> ((EntityLivingBase) entity).getHealth()));
			}
			if (sortmode.equalsIgnorecase("Angle")) {
				targets.sort(Comparator.comparingDouble(entity -> getKaRotations(((EntityLivingBase) entity))[0]));
			}
		}

		if (targets1.equalsIgnorecase("Players")) {
			targets = targets.stream().filter(EntityPlayer.class::isInstance).collect(Collectors.toList());
		}
		if (targets1.equalsIgnorecase("Mobs")) {
			targets = targets.stream().filter(EntityMob.class::isInstance).collect(Collectors.toList());
		}
		if (targets1.equalsIgnorecase("Animals")) {
			targets = targets.stream().filter(EntityAnimal.class::isInstance).collect(Collectors.toList());
		}
		if (targets1.equalsIgnorecase("Villagers")) {
			targets = targets.stream().filter(EntityVillager.class::isInstance).collect(Collectors.toList());
		}
		if (targets1.equalsIgnorecase("All")) {
			targets = targets.stream().filter(Entity.class::isInstance).collect(Collectors.toList());
		}

		if (!targets.isEmpty()) {
			// EntityLivingBase target = (EntityLivingBase) targets.get(0);

			EntityLivingBase target = (EntityLivingBase) targets.get(0);

			if (targetesp.isEnabled()) {

				if (up) {
					if (timer.elapsed(05)) {
						height += 0.02f;
						if (height > 2) {
							up = false;
						}
						timer.reset1();
					}
				} else {
					if (timer.elapsed(05)) {
						height -= 0.02f;
						if (height <= 0) {
							up = true;
						}
						timer.reset1();
					}
				}

				if (!up) {
					renderCircle2(height, 1f, target, mc.timer.elapsedPartialTicks);
					renderCircle2(height + 0.01f, 0.95f, target, mc.timer.elapsedPartialTicks);
					renderCircle2(height + 0.02f, 0.9f, target, mc.timer.elapsedPartialTicks);
					renderCircle2(height + 0.03f, 0.85f, target, mc.timer.elapsedPartialTicks);
					renderCircle2(height + 0.04f, 0.8f, target, mc.timer.elapsedPartialTicks);
					renderCircle2(height + 0.05f, 0.75f, target, mc.timer.elapsedPartialTicks);
					renderCircle2(height + 0.06f, 0.7f, target, mc.timer.elapsedPartialTicks);
					renderCircle2(height + 0.07f, 0.65f, target, mc.timer.elapsedPartialTicks);
					renderCircle2(height + 0.08f, 0.6f, target, mc.timer.elapsedPartialTicks);
					renderCircle2(height + 0.09f, 0.55f, target, mc.timer.elapsedPartialTicks);
					renderCircle2(height + 0.10f, 0.5f, target, mc.timer.elapsedPartialTicks);
					renderCircle2(height + 0.11f, 0.45f, target, mc.timer.elapsedPartialTicks);
					renderCircle2(height + 0.12f, 0.4f, target, mc.timer.elapsedPartialTicks);
					renderCircle2(height + 0.13f, 0.35f, target, mc.timer.elapsedPartialTicks);
					renderCircle2(height + 0.14f, 0.3f, target, mc.timer.elapsedPartialTicks);
					renderCircle2(height + 0.15f, 0.25f, target, mc.timer.elapsedPartialTicks);
					renderCircle2(height + 0.16f, 0.2f, target, mc.timer.elapsedPartialTicks);
					renderCircle2(height + 0.17f, 0.15f, target, mc.timer.elapsedPartialTicks);
					renderCircle2(height + 0.18f, 0.1f, target, mc.timer.elapsedPartialTicks);
					renderCircle2(height + 0.19f, 0.05f, target, mc.timer.elapsedPartialTicks);
					renderCircle2(height + 0.20f, 0.03f, target, mc.timer.elapsedPartialTicks);
					renderCircle2(height + 0.21f, 0.02f, target, mc.timer.elapsedPartialTicks);
					renderCircle2(height + 0.22f, 0.01f, target, mc.timer.elapsedPartialTicks);
				} else {
					renderCircle2(height, 0.1f, target, mc.timer.elapsedPartialTicks);
					renderCircle2(height + 0.01f, 0.15f, target, mc.timer.elapsedPartialTicks);
					renderCircle2(height + 0.02f, 0.2f, target, mc.timer.elapsedPartialTicks);
					renderCircle2(height + 0.03f, 0.25f, target, mc.timer.elapsedPartialTicks);
					renderCircle2(height + 0.04f, 0.3f, target, mc.timer.elapsedPartialTicks);
					renderCircle2(height + 0.05f, 0.35f, target, mc.timer.elapsedPartialTicks);
					renderCircle2(height + 0.06f, 0.4f, target, mc.timer.elapsedPartialTicks);
					renderCircle2(height + 0.07f, 0.45f, target, mc.timer.elapsedPartialTicks);
					renderCircle2(height + 0.08f, 0.5f, target, mc.timer.elapsedPartialTicks);
					renderCircle2(height + 0.09f, 0.55f, target, mc.timer.elapsedPartialTicks);
					renderCircle2(height + 0.10f, 0.6f, target, mc.timer.elapsedPartialTicks);
					renderCircle2(height + 0.11f, 0.65f, target, mc.timer.elapsedPartialTicks);
					renderCircle2(height + 0.12f, 0.7f, target, mc.timer.elapsedPartialTicks);
					renderCircle2(height + 0.13f, 0.75f, target, mc.timer.elapsedPartialTicks);
					renderCircle2(height + 0.14f, 0.8f, target, mc.timer.elapsedPartialTicks);
					renderCircle2(height + 0.15f, 0.85f, target, mc.timer.elapsedPartialTicks);
					renderCircle2(height + 0.16f, 0.9f, target, mc.timer.elapsedPartialTicks);
					renderCircle2(height + 0.17f, 0.95f, target, mc.timer.elapsedPartialTicks);
					renderCircle2(height + 0.18f, 1f, target, mc.timer.elapsedPartialTicks);

				}
				// drawCircle(target, mc.timer.elapsedPartialTicks, target.width);
			}

		}
	}

	public void onPostMotionUpdate(EventPostMotion event) {
		List<Entity> targets = (List<Entity>) mc.theWorld.loadedEntityList.stream()
				.filter(EntityLivingBase.class::isInstance).collect(Collectors.toList());
		targets = targets.stream().filter(entity -> entity.getDistanceToEntity(mc.thePlayer) < range.getValue()
				&& entity != mc.thePlayer && !entity.isDead).collect(Collectors.toList());

		if (mode.equalsIgnorecase("Switch")) {
			if (sortmode.equalsIgnorecase("Distance")) {
				targets.sort(Comparator
						.comparingDouble(entity -> ((EntityLivingBase) entity).getDistanceToEntity(mc.thePlayer)));
			}
			if (sortmode.equalsIgnorecase("Health")) {
				targets.sort(Comparator.comparingDouble(entity -> ((EntityLivingBase) entity).getHealth()));
			}
			if (sortmode.equalsIgnorecase("Angle")) {
				targets.sort(Comparator.comparingDouble(entity -> getKaRotations(((EntityLivingBase) entity))[0]));
			}
		}

		if (targets1.equalsIgnorecase("Players")) {
			targets = targets.stream().filter(EntityPlayer.class::isInstance).collect(Collectors.toList());
		}
		if (targets1.equalsIgnorecase("Mobs")) {
			targets = targets.stream().filter(EntityMob.class::isInstance).collect(Collectors.toList());
		}
		if (targets1.equalsIgnorecase("Animals")) {
			targets = targets.stream().filter(EntityAnimal.class::isInstance).collect(Collectors.toList());
		}
		if (targets1.equalsIgnorecase("Villagers")) {
			targets = targets.stream().filter(EntityVillager.class::isInstance).collect(Collectors.toList());
		}
		if (targets1.equalsIgnorecase("All")) {
			targets = targets.stream().filter(Entity.class::isInstance).collect(Collectors.toList());
		}

		if (!targets.isEmpty()) {
			EntityLivingBase target = (EntityLivingBase) targets.get(0);

			if (attackmode.equalsIgnorecase("Post")) {
				/*
				 * if(raycast.isEnabled()) { Entity raycastEntity =
				 * RaycastUtil.rayCast(range.getValue() + 1.0f, getKaRotations(target)[0],
				 * getKaRotations(target)[1]);
				 * 
				 * if(raycastEntity != null) {
				 * 
				 * } else { return; } }
				 */
			}

			MathUtils random = new MathUtils();

			if (isInFOV(target, fov.getValue())) {
				if (raycast.isEnabled() && rayca) {
					if (attackmode.equalsIgnorecase("Post")) {
						if (timer.hasTimedElapsed(
								(long) (1000 / random.randomInt((int) mincps.getValue(), (int) maxcps.getValue())),
								true) && mc.thePlayer != null && !FriendManager.isFriend(target.getName())) {
							try {
								for (int i = 0; i < cracksize.getValue(); i++) {
									mc.thePlayer.onCriticalHit(target);
								}
								for (int i = 0; i < enchantcracksize.getValue(); i++) {
									mc.thePlayer.onEnchantmentCritical(target);
								}
								if (swingmode.equalsIgnorecase("Client")) {
									mc.thePlayer.swingItem();
								}
								if (swingmode.equalsIgnorecase("Server")) {
									mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
								}

								if (mc.thePlayer != null) {
									if(cubecraft.isEnabled()) {
										try {
											Robot robot = new Robot();
											robot.mousePress(InputEvent.BUTTON1_MASK);
											robot.mouseRelease(InputEvent.BUTTON1_MASK);
										} catch (AWTException e) {
											
										}
									} else {
										mc.thePlayer.sendQueue
												.addToSendQueue(new C02PacketUseEntity(target, Action.ATTACK));
									}
								}

								if (mc.thePlayer.isDead || mc.thePlayer == null) {
									this.toggle();
								}
							} catch (IllegalArgumentException e1) {

							}
						} else {

						}
					}
				} else {
					if (attackmode.equalsIgnorecase("Post")) {
						if (timer.hasTimedElapsed(
								(long) (1000 / random.randomInt((int) mincps.getValue(), (int) maxcps.getValue())),
								true) && mc.thePlayer != null && !FriendManager.isFriend(target.getName())) {
							try {
								for (int i = 0; i < cracksize.getValue(); i++) {
									mc.thePlayer.onCriticalHit(target);
								}
								for (int i = 0; i < enchantcracksize.getValue(); i++) {
									mc.thePlayer.onEnchantmentCritical(target);
								}
								if (swingmode.equalsIgnorecase("Client")) {
									mc.thePlayer.swingItem();
								}
								if (swingmode.equalsIgnorecase("Server")) {
									mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
								}

								if (mc.thePlayer != null) {
									if(cubecraft.isEnabled()) {
										try {
											Robot robot = new Robot();
											robot.mousePress(InputEvent.BUTTON1_MASK);
											robot.mouseRelease(InputEvent.BUTTON1_MASK);
										} catch (AWTException e) {
											
										}
									} else {
										mc.thePlayer.sendQueue
												.addToSendQueue(new C02PacketUseEntity(target, Action.ATTACK));
									}
								}

								if (mc.thePlayer.isDead || mc.thePlayer == null) {
									this.toggle();
								}
							} catch (IllegalArgumentException e1) {

							}
						} else {

						}
					}
				}
				try {
					if (!attackmode.equalsIgnorecase("AAC")) {
						if (mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword) {
							if (autoblockmode.equalsIgnorecase("CustomPost")) {
								mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld,
										mc.thePlayer.inventory.getCurrentItem());
								if (mc.thePlayer.isBlocking()
										&& timer.hasTimedElapsed((long) autoblockdelay.getValue(), true)) {
									mc.thePlayer.sendQueue.addToSendQueue(
											new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM,
													BlockPos.ORIGIN, EnumFacing.DOWN));
								}
							}
							if (autoblockmode.equalsIgnorecase("Post")) {
								mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld,
										mc.thePlayer.inventory.getCurrentItem());
							}
							if (autoblockmode.equalsIgnorecase("Hypixel")) {
								// mc.thePlayer.setItemInUse(mc.thePlayer.inventory.getCurrentItem(), 20);
								if (mc.thePlayer.ticksExisted % 1 == 0) {
									BlockingUtil.sendUseHypixelBlockingPacket(mc.thePlayer, mc.theWorld,
											mc.thePlayer.inventory.getCurrentItem());
									// mc.thePlayer.sendQueue.addToSendQueue(new
									// C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM,
									// BlockPos.ORIGIN, EnumFacing.DOWN));
								}
							}
						}
					}
				} catch (Exception e1) {

				}
			}
		}
	}

	public void onUpdate(EventUpdate event) {
		this.setDisplayName("Killaura " + mode.modes.get(mode.index) + "");

		List<Entity> targets = (List<Entity>) mc.theWorld.loadedEntityList.stream()
				.filter(EntityLivingBase.class::isInstance).collect(Collectors.toList());
		targets = targets.stream().filter(entity -> entity.getDistanceToEntity(mc.thePlayer) < range.getValue()
				&& entity != mc.thePlayer && !entity.isDead).collect(Collectors.toList());

		if (mode.equalsIgnorecase("Switch")) {
			if (sortmode.equalsIgnorecase("Distance")) {
				targets.sort(Comparator
						.comparingDouble(entity -> ((EntityLivingBase) entity).getDistanceToEntity(mc.thePlayer)));
			}
			if (sortmode.equalsIgnorecase("Health")) {
				targets.sort(Comparator.comparingDouble(entity -> ((EntityLivingBase) entity).getHealth()));
			}
			if (sortmode.equalsIgnorecase("Angle")) {
				targets.sort(Comparator.comparingDouble(entity -> getKaRotations(((EntityLivingBase) entity))[0]));
			}
		}

		if (targets1.equalsIgnorecase("Players")) {
			targets = targets.stream().filter(EntityPlayer.class::isInstance).collect(Collectors.toList());
		}
		if (targets1.equalsIgnorecase("Mobs")) {
			targets = targets.stream().filter(EntityMob.class::isInstance).collect(Collectors.toList());
		}
		if (targets1.equalsIgnorecase("Animals")) {
			targets = targets.stream().filter(EntityAnimal.class::isInstance).collect(Collectors.toList());
		}
		if (targets1.equalsIgnorecase("Villagers")) {
			targets = targets.stream().filter(EntityVillager.class::isInstance).collect(Collectors.toList());
		}
		if (targets1.equalsIgnorecase("All")) {
			targets = targets.stream().filter(Entity.class::isInstance).collect(Collectors.toList());
		}

		if (!targets.isEmpty()) {
			EntityLivingBase target = (EntityLivingBase) targets.get(0);

			MathUtils random = new MathUtils();

			if (isInFOV(target, fov.getValue())) {
				if (raycast.isEnabled() && rayca) {
					if (attackmode.equalsIgnorecase("AAC")) {
						if (timer.hasTimedElapsed(
								(long) (1000 / random.randomInt((int) mincps.getValue(), (int) maxcps.getValue())),
								true) && mc.thePlayer != null && !FriendManager.isFriend(target.getName())) {
							try {
								for (int i = 0; i < cracksize.getValue(); i++) {
									mc.thePlayer.onCriticalHit(target);
								}
								for (int i = 0; i < enchantcracksize.getValue(); i++) {
									mc.thePlayer.onEnchantmentCritical(target);
								}
								if (swingmode.equalsIgnorecase("Client")) {
									mc.thePlayer.swingItem();
								}
								if (swingmode.equalsIgnorecase("Server")) {
									mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
								}

								if (mc.thePlayer != null) {
									if(cubecraft.isEnabled()) {
										try {
											Robot robot = new Robot();
											robot.mousePress(InputEvent.BUTTON1_MASK);
											robot.mouseRelease(InputEvent.BUTTON1_MASK);
										} catch (AWTException e) {
											
										}
									} else {
										mc.thePlayer.sendQueue
												.addToSendQueue(new C02PacketUseEntity(target, Action.ATTACK));
									}
								}

								if (mc.thePlayer.isDead || mc.thePlayer == null) {
									this.toggle();
								}
							} catch (IllegalArgumentException e1) {

							}
						} else {

						}
					}
				} else {
					if (attackmode.equalsIgnorecase("AAC")) {
						if (timer.hasTimedElapsed(
								(long) (1000 / random.randomInt((int) mincps.getValue(), (int) maxcps.getValue())),
								true) && mc.thePlayer != null && !FriendManager.isFriend(target.getName())) {
							try {
								for (int i = 0; i < cracksize.getValue(); i++) {
									mc.thePlayer.onCriticalHit(target);
								}
								for (int i = 0; i < enchantcracksize.getValue(); i++) {
									mc.thePlayer.onEnchantmentCritical(target);
								}
								if (swingmode.equalsIgnorecase("Client")) {
									mc.thePlayer.swingItem();
								}
								if (swingmode.equalsIgnorecase("Server")) {
									mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
								}

								if (mc.thePlayer != null) {
									if(cubecraft.isEnabled()) {
										try {
											Robot robot = new Robot();
											robot.mousePress(InputEvent.BUTTON1_MASK);
											robot.mouseRelease(InputEvent.BUTTON1_MASK);
										} catch (AWTException e) {
											
										}
									} else {
										mc.thePlayer.sendQueue
												.addToSendQueue(new C02PacketUseEntity(target, Action.ATTACK));
									}
								}

								if (mc.thePlayer.isDead || mc.thePlayer == null) {
									this.toggle();
								}
							} catch (IllegalArgumentException e1) {

							}
						} else {

						}
					}
				}
				try {
					if (attackmode.equalsIgnorecase("AAC")) {
						if (mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword) {
							if (autoblockmode.equalsIgnorecase("CustomPost")) {
								mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld,
										mc.thePlayer.inventory.getCurrentItem());
								if (mc.thePlayer.isBlocking()
										&& timer.hasTimedElapsed((long) autoblockdelay.getValue(), true)) {
									mc.thePlayer.sendQueue.addToSendQueue(
											new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM,
													BlockPos.ORIGIN, EnumFacing.DOWN));
								}
							}
							if (autoblockmode.equalsIgnorecase("Post")) {
								mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld,
										mc.thePlayer.inventory.getCurrentItem());
							}
							if (autoblockmode.equalsIgnorecase("Hypixel")) {
								BlockingUtil.sendUseHypixelBlockingPacket(mc.thePlayer, mc.theWorld,
										mc.thePlayer.inventory.getCurrentItem());
							}
						}
					}
				} catch (Exception e1) {

				}
			}
		}
	}

	public float[] getKaRotations(Entity e) {
		double deltaX = e.posX + (e.posX - e.lastTickPosX) - mc.thePlayer.posX,
				deltaY = e.posY - 3.5 + e.getEyeHeight() - mc.thePlayer.posY + mc.thePlayer.getEyeHeight(),
				deltaZ = e.posZ + (e.posZ - e.lastTickPosZ) - mc.thePlayer.posZ,
				distance = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaZ, 2));

		float yaw = (float) Math.toDegrees(-Math.atan(deltaX / deltaZ)),
				pitch = (float) -Math.toDegrees(Math.atan(deltaY / distance));

		yaw *= RandomUtils.nextFloat(0.99F, 1.01F);
		pitch *= RandomUtils.nextFloat(0.99F, 1.01F);
		if (pitch > 90) {
			pitch = 90F;
		} else if (pitch < -90) {
			pitch = -90F;
		}

		if (deltaX < 0 && deltaZ < 0) {
			yaw = (float) (90 + Math.toDegrees(Math.atan(deltaZ / deltaX)));
		} else if (deltaX > 0 && deltaZ < 0) {
			yaw = (float) (-90 + Math.toDegrees(Math.atan(deltaZ / deltaX)));

		}

		return new float[] { yaw, pitch };
	}

	private float getAngleDifference(float dir, float yaw) {
		float f = Math.abs(yaw - dir) % 360F;
		float dist = f > 180F ? 360F - f : f;
		return dist;
	}

	private boolean isInFOV(EntityLivingBase entity, double angle) {
		angle *= .5D;
		double angleDiff = getAngleDifference(mc.thePlayer.rotationYaw, getKaRotations(entity)[0]);
		return (angleDiff > 0 && angleDiff < angle) || (-angle < angleDiff && angleDiff < 0);
	}

	public static boolean isOnSameTeam(Entity a) {
		if (((EntityLivingBase) a).getTeam() != null && Minecraft.getMinecraft().thePlayer.getTeam() != null) {
			char c1 = a.getDisplayName().getFormattedText().charAt(1);
			char c2 = Minecraft.getMinecraft().thePlayer.getDisplayName().getFormattedText().charAt(1);
			return c1 == c2;
		}
		return false;
	}

	private void renderCircle2(float height, float alpha, Entity entity, float partialTicks) {
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		RenderUtil.startSmooth();
		double rad = 0.6;
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		GL11.glLineWidth(5.2f);
		GL11.glBegin(GL11.GL_LINE_STRIP);

		final double anim = MathHelper.sin(((System.currentTimeMillis()) % 1500 / (float) 1500) * (float) Math.PI);
		final double anim1 = MathHelper.sin(((System.currentTimeMillis()) % 1500 / (float) 1500) * (float) Math.PI)
				- 0.5f;

		final double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks
				- mc.getRenderManager().viewerPosX;
		final double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks
				- mc.getRenderManager().viewerPosY;
		final double y1 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks
				- mc.getRenderManager().viewerPosY + entity.height - anim1 * 2;
		float points = 90.0F;
		final double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks
				- mc.getRenderManager().viewerPosZ;
		GlStateManager.color(84, 95, 255, alpha);
		final float r = ((float) 1 / 255) * Color.WHITE.getRed();
		final float g = ((float) 1 / 255) * Color.WHITE.getGreen();
		final float b = ((float) 1 / 255) * Color.WHITE.getBlue();

		final double pix2 = Math.PI * 2D;

		for (int i = 0; i <= 90; ++i) {
			GL11.glColor4f(r, g, b, alpha);
			// GL11.glVertex3d(x + rad * Math.cos(i * pix2 / 45.0), y + height, z + rad *
			// Math.sin(i * pix2 / 45.0));
			GL11.glVertex3d(x + rad * Math.cos(i * 6.283185307179586D / points), y + height,
					z + rad * Math.sin(i * 6.283185307179586D / points));
		}

		GL11.glEnd();
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_DEPTH_TEST);

		RenderUtil.endSmooth();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glPopMatrix();
	}

	@Override
	public void onMovementUpdate(EventMovement event) {
		TargetStrafe targetStrafe = new TargetStrafe();
		if (targetStrafe.canStrafe() && targetStrafe.isEnabled()) {
			MovementUtil.setSpeed1(event, MovementUtil.defaultMoveSpeed());
		}
		super.onMovementUpdate(event);
	}

	public void renderCircle2(EventRender3D event, float height, float alpha) {
		Entity entity = target;
		double rad = 0.6;
		float points = 90.0F;
		GlStateManager.enableDepth();
		GL11.glPushMatrix();
		GL11.glDisable(3553);
//	      GL11.glEnable(2848);
		GL11.glEnable(2881);
		GL11.glEnable(2832);
		GL11.glEnable(3042);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glBlendFunc(770, 771);
		GL11.glHint(3154, 4354);
		GL11.glHint(3155, 4354);
		GL11.glHint(3153, 4354);
		GL11.glDisable(2929);
		GL11.glLineWidth(1.3F);
		GL11.glBegin(3);
		GlStateManager.color(84, 95, 255, alpha);

		double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * event.getPartialTicks()
				- RenderManager.renderPosX;
		double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * event.getPartialTicks()
				- RenderManager.renderPosY;
		double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * event.getPartialTicks()
				- RenderManager.renderPosZ;

		for (int i = 0; i <= 90; i++) {
			GL11.glColor4f(84, 95, 255, alpha);
			GL11.glVertex3d(x + rad * Math.cos(i * 6.283185307179586D / points), y + height,
					z + rad * Math.sin(i * 6.283185307179586D / points));
		}

		GL11.glEnd();
		GL11.glDepthMask(true);
		GL11.glEnable(2929);
		GL11.glDisable(2848);
		GL11.glDisable(2881);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(2832);
		GL11.glEnable(3553);
		GL11.glPopMatrix();
	}

	private void drawCircle(Entity entity, float partialTicks, double rad) {
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		RenderUtil.startSmooth();
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		GL11.glLineWidth(5.5f);
		GL11.glBegin(GL11.GL_LINE_STRIP);

		final double anim = MathHelper.sin(((System.currentTimeMillis()) % 1500 / (float) 1500) * (float) Math.PI);
		final double anim1 = MathHelper.sin(((System.currentTimeMillis()) % 1500 / (float) 1500) * (float) Math.PI)
				- 0.5f;

		final double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks
				- mc.getRenderManager().viewerPosX;
		final double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks
				- mc.getRenderManager().viewerPosY + entity.height - anim * 2;
		final double y1 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks
				- mc.getRenderManager().viewerPosY + entity.height - anim1 * 2;

		final double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks
				- mc.getRenderManager().viewerPosZ;

		final float r = ((float) 1 / 255) * Color.WHITE.getRed();
		final float g = ((float) 1 / 255) * Color.WHITE.getGreen();
		final float b = ((float) 1 / 255) * Color.WHITE.getBlue();

		final double pix2 = Math.PI * 2D;

		for (int i = 0; i <= 90; ++i) {
			GL11.glColor3f(r, g, b);
			GL11.glVertex3d(x + rad * Math.cos(i * pix2 / 45.0), y, z + rad * Math.sin(i * pix2 / 45.0));

		}

		GL11.glEnd();
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_DEPTH_TEST);

		RenderUtil.endSmooth();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glPopMatrix();
	}

	public void onStrafe(EventStrafe event) {
		isEnabled = true;
		// this.img = null;

		List<Entity> targets = mc.theWorld.loadedEntityList.stream().filter(EntityLivingBase.class::isInstance)
				.collect(Collectors.toList());
		targets = targets.stream().filter(entity -> entity.getDistanceToEntity(mc.thePlayer) < range.getValue()
				&& entity != mc.thePlayer && !entity.isDead).collect(Collectors.toList());

		if (mode.equalsIgnorecase("Switch")) {
			if (sortmode.equalsIgnorecase("Distance")) {
				targets.sort(Comparator
						.comparingDouble(entity -> ((EntityLivingBase) entity).getDistanceToEntity(mc.thePlayer)));
			}
			if (sortmode.equalsIgnorecase("Health")) {
				targets.sort(Comparator.comparingDouble(entity -> ((EntityLivingBase) entity).getHealth()));
			}
			if (sortmode.equalsIgnorecase("Angle")) {
				targets.sort(Comparator.comparingDouble(entity -> getKaRotations(((EntityLivingBase) entity))[0]));
			}
		}

		if (targets1.equalsIgnorecase("Players")) {
			targets = targets.stream().filter(EntityPlayer.class::isInstance).collect(Collectors.toList());
		}
		if (targets1.equalsIgnorecase("Mobs")) {
			targets = targets.stream().filter(EntityMob.class::isInstance).collect(Collectors.toList());
		}
		if (targets1.equalsIgnorecase("Animals")) {
			targets = targets.stream().filter(EntityAnimal.class::isInstance).collect(Collectors.toList());
		}
		if (targets1.equalsIgnorecase("Villagers")) {
			targets = targets.stream().filter(EntityVillager.class::isInstance).collect(Collectors.toList());
		}
		if (targets1.equalsIgnorecase("All")) {
			targets = targets.stream().filter(Entity.class::isInstance).collect(Collectors.toList());
		}

		if (targets.isEmpty()) {
			target = null;
			this.hasTriedToDownload = false;
			this.img = null;
			CustomVelo.lastTickPitch = CustomVelo.pitch;
			CustomVelo.pitch = mc.thePlayer.rotationPitch;
		}
		if (!targets.isEmpty()) {
			this.target = (EntityLivingBase) targets.get(0);
			this.currentEntity = target;

			if (rotationsmode.equalsIgnorecase("Normal")) {
				pitch = (RotationUtils.getNeededRotations(target)[1]);
				yaw = (RotationUtils.getNeededRotations(target)[0]);

			}

			if (rotationsmode.equalsIgnorecase("Basic")) {

				yaw = (float) (getKaRotations(target)[0]);
				pitch = (float) (getKaRotations(target)[1]);

				mc.thePlayer.rotationYawHead = (yaw);
				mc.thePlayer.renderYawOffset = (yaw);
			} else if (rotationsmode.equalsIgnorecase("Hypixel")) {
				float yaw2 = RotationUtil.getRotations(target, (float) (7.6 * 5F))[0];
				float pitch2 = RotationUtil.getRotations(target, (float) (7.6 * 5F))[1];
				yaw = yaw2;
				pitch = pitch2;
			} else if (rotationsmode.equalsIgnorecase("AAC")) {

				float[] rots = getKaRotations(target);
				// float[] crop = { rots[0] + Random.randomInt(3, -3), rots[1] +
				// Random.randomInt(3, -3) };
				pitch = (RotationUtils.getNeededRotations(target)[1]);
				yaw = (RotationUtils.getNeededRotations(target)[0]);
				
		        float f = mc.gameSettings.mouseSensitivity/2F * 0.6F + 0.2F;
		        float gcd = f * f * f * 1.2F;

		        yaw -= yaw % gcd;
		        pitch -= pitch % gcd;
				// return new float[] { rots[0] + randomNumber(3, -3), rots[1] + randomNumber(3,
				// -3) };
			}
			if (rotationsmode.equalsIgnorecase("Smooth")) {
				if (yaw2 > (getKaRotations(target)[0] + Math.random())) {
					yaw2 = (float) ((yaw2)
							- ((yaw2 - (getKaRotations(target)[0] + Math.random())) / smoothrotslowness.getValue()));
				} else if (yaw2 < (getKaRotations(target)[0] + Math.random())) {
					yaw2 = (float) ((yaw2)
							+ (((getKaRotations(target)[0] + Math.random()) - yaw2) / smoothrotslowness.getValue()));
				}

				yaw = yaw2;
				pitch = ((float) (getKaRotations(target)[1] + Math.random()));
			}

			rayca = true;
			
			mc.thePlayer.rotationYawHead = yaw;
			mc.thePlayer.renderYawOffset = yaw;

			// CustomVelo.yaw = event.getYaw();

			if (attackmode.equalsIgnorecase("Pre")) {
				/*
				 * if(raycast.isEnabled()) { Entity raycastEntity =
				 * RaycastUtil.rayCast(range.getValue() + 1.0f, getKaRotations(target)[0],
				 * getKaRotations(target)[1]);
				 * 
				 * if(raycastEntity != null) {
				 * 
				 * } else { return; } }
				 */
			}

			if (isInFOV(target, fov.getValue()) && !FriendManager.isFriend(target.getName())) {
				try {
					if (mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword) {
						if (autoblockmode.equalsIgnorecase("CustomPre")) {
							mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld,
									mc.thePlayer.inventory.getCurrentItem());
							if (mc.thePlayer.isBlocking()
									&& timer.hasTimedElapsed((long) autoblockdelay.getValue(), true)) {
								mc.thePlayer.sendQueue.addToSendQueue(
										new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM,
												BlockPos.ORIGIN, EnumFacing.DOWN));
							}
						}
						if (autoblockmode.equalsIgnorecase("Pre")) {
							mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld,
									mc.thePlayer.inventory.getCurrentItem());
						}

						if (block && target == null) {
							unblock();
							block = false;
						}

						if (autoblockmode.equalsIgnorecase("NCP")) {
							if (target != null) {
								block();
							}
						}
					}
				} catch (Exception e1) {

				}

				MathUtils random = new MathUtils();

				if (raycast.isEnabled() && rayca) {
					if (attackmode.equalsIgnorecase("Pre") && !FriendManager.isFriend(target.getName())) {
						if (timer.hasTimedElapsed(
								(long) (1000 / random.randomInt((int) mincps.getValue(), (int) maxcps.getValue())),
								true) && mc.thePlayer != null) {
							try {
								for (int i = 0; i < cracksize.getValue(); i++) {
									mc.thePlayer.onCriticalHit(target);
								}
								for (int i = 0; i < enchantcracksize.getValue(); i++) {
									mc.thePlayer.onEnchantmentCritical(target);
								}
								if (swingmode.equalsIgnorecase("Client")) {
									mc.thePlayer.swingItem();
								}
								if (swingmode.equalsIgnorecase("Server")) {
									mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
								}

								if (mc.thePlayer != null) {
									if(cubecraft.isEnabled()) {
										try {
											Robot robot = new Robot();
											robot.mousePress(InputEvent.BUTTON1_MASK);
											robot.mouseRelease(InputEvent.BUTTON1_MASK);
										} catch (AWTException e) {
											
										}
									} else {
										mc.thePlayer.sendQueue
												.addToSendQueue(new C02PacketUseEntity(target, Action.ATTACK));
									}
								}

								if (mc.thePlayer.isDead || mc.thePlayer == null) {
									this.toggle();
								}
							} catch (IllegalArgumentException e1) {

							}
						} else {

						}
					}
				} else {
					if (attackmode.equalsIgnorecase("Pre") && !FriendManager.isFriend(target.getName())) {
						if (timer.hasTimedElapsed(
								(long) (1000 / random.randomInt((int) mincps.getValue(), (int) maxcps.getValue())),
								true) && mc.thePlayer != null) {
							try {
								for (int i = 0; i < cracksize.getValue(); i++) {
									mc.thePlayer.onCriticalHit(target);
								}
								for (int i = 0; i < enchantcracksize.getValue(); i++) {
									mc.thePlayer.onEnchantmentCritical(target);
								}
								if (swingmode.equalsIgnorecase("Client")) {
									mc.thePlayer.swingItem();
								}
								if (swingmode.equalsIgnorecase("Server")) {
									mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
								}

								if (mc.thePlayer != null) {
									if(cubecraft.isEnabled()) {
										try {
											Robot robot = new Robot();
											robot.mousePress(InputEvent.BUTTON1_MASK);
											robot.mouseRelease(InputEvent.BUTTON1_MASK);
										} catch (AWTException e) {
											
										}
									} else {
										mc.thePlayer.sendQueue
												.addToSendQueue(new C02PacketUseEntity(target, Action.ATTACK));
									}
								}

								if (mc.thePlayer.isDead || mc.thePlayer == null) {
									this.toggle();
								}
							} catch (IllegalArgumentException e1) {

							}
						} else {

						}
					}
				}
			}
			
			if(strafefix.isEnabled()) {
				event.setYaw(yaw);
			} else {
				event.setYaw(mc.thePlayer.rotationYaw);
			}
		} else {
			currentEntity = null;
			yaw = mc.thePlayer.rotationYaw;
			pitch = mc.thePlayer.rotationPitch;
		}

	}

	private boolean hasArmor(EntityPlayer player) {
		return player.inventory.armorInventory[0] != null || player.inventory.armorInventory[1] != null
				|| player.inventory.armorInventory[2] != null || player.inventory.armorInventory[3] != null;
	}

	static /* synthetic */ void access$0(final Killaura ka, final ResourceLocation img) {
		ka.img = img;
	}

	public static void drawHead(ResourceLocation skin, int x, int y, int width, int height) {
		GL11.glColor4f(1, 1, 1, 1);
		Minecraft.getMinecraft().getTextureManager().bindTexture(skin);
		Gui.drawScaledCustomSizeModalRect(x, y, 8f, 8f, 8, 8, width, height, 64f, 64f);
	}

	public static int[] getFractionIndicies(final float[] fractions, final float progress) {
		final int[] range = new int[2];
		int startPoint;
		for (startPoint = 0; startPoint < fractions.length && fractions[startPoint] <= progress; ++startPoint) {
		}
		if (startPoint >= fractions.length) {
			startPoint = fractions.length - 1;
		}
		range[0] = startPoint - 1;
		range[1] = startPoint;
		return range;
	}

	public static Color blendColors(final float[] fractions, final Color[] colors, final float progress) {
		Color color = null;
		if (fractions == null) {
			throw new IllegalArgumentException("Fractions can't be null");
		}
		if (colors == null) {
			throw new IllegalArgumentException("Colours can't be null");
		}
		if (fractions.length == colors.length) {
			final int[] indicies = getFractionIndicies(fractions, progress);
			final float[] range = { fractions[indicies[0]], fractions[indicies[1]] };
			final Color[] colorRange = { colors[indicies[0]], colors[indicies[1]] };
			final float max = range[1] - range[0];
			final float value = progress - range[0];
			final float weight = value / max;
			color = blend(colorRange[0], colorRange[1], 1.0f - weight);
			return color;
		}
		throw new IllegalArgumentException("Fractions and colours must have equal number of elements");
	}

	public static Color blend(final Color color1, final Color color2, final double ratio) {
		final float r = (float) ratio;
		final float ir = 1.0f - r;
		final float[] rgb1 = new float[3];
		final float[] rgb2 = new float[3];
		color1.getColorComponents(rgb1);
		color2.getColorComponents(rgb2);
		float red = rgb1[0] * r + rgb2[0] * ir;
		float green = rgb1[1] * r + rgb2[1] * ir;
		float blue = rgb1[2] * r + rgb2[2] * ir;
		if (red < 0.0f) {
			red = 0.0f;
		} else if (red > 255.0f) {
			red = 255.0f;
		}
		if (green < 0.0f) {
			green = 0.0f;
		} else if (green > 255.0f) {
			green = 255.0f;
		}
		if (blue < 0.0f) {
			blue = 0.0f;
		} else if (blue > 255.0f) {
			blue = 255.0f;
		}
		Color color3 = null;
		try {
			color3 = new Color(red, green, blue);
		} catch (IllegalArgumentException exp) {
			final NumberFormat nf = NumberFormat.getNumberInstance();
			System.out.println(
					nf.format((double) red) + "; " + nf.format((double) green) + "; " + nf.format((double) blue));
			exp.printStackTrace();
		}
		return color3;
	}

	public void drawFace(double x, double y, float u, float v, int uWidth, int vHeight, int width, int height,
			float tileWidth, float tileHeight, AbstractClientPlayer target) {
		try {
			ResourceLocation skin = target.getLocationSkin();
			Minecraft.getMinecraft().getTextureManager().bindTexture(skin);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glColor4f(1, 1, 1, 1);
			Gui.drawScaledCustomSizeModalRect((int) x, (int) y, u, v, uWidth, vHeight, width, height, tileWidth,
					tileHeight);
			GL11.glDisable(GL11.GL_BLEND);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void drawExhiRect(float left, float top, float right, float bottom) {
		final float gownoPsa = 1.5f;
		final float gownoZPsem = 1;
		Gui.drawRect(left - gownoPsa - gownoZPsem, top - gownoPsa - gownoZPsem, right + gownoPsa + gownoZPsem,
				bottom + gownoPsa + gownoZPsem, new Color(62, 59, 59).getRGB());
		Gui.drawRect(left - gownoPsa, top - gownoPsa, right + gownoPsa, bottom + gownoPsa,
				new Color(42, 39, 39).getRGB());
		Gui.drawRect(left, top, right, bottom, new Color(18, 16, 16).getRGB());
	}

	public Color getHealthColorTest(EntityLivingBase entityLivingBase) {
		float health = entityLivingBase.getHealth();
		float[] fractions = new float[] { 0.0F, 0.15f, .55F, 0.7f, .9f };
		Color[] colors = new Color[] { new Color(133, 0, 0), Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN };
		float progress = health / entityLivingBase.getMaxHealth();
		return health >= 0.0f ? blendColors(fractions, colors, progress).brighter() : colors[0];
	}

	public static double getIncremental(final double val, final double inc) {
		final double one = 1.0 / inc;
		return Math.round(val * one) / one;
	}

	public static int randomNumber(int max, int min) {
		return Math.round(min + (float) Math.random() * (max - min));
	}

	private int getHealthColor(EntityLivingBase player) {
		float f = player.getHealth();
		float f1 = player.getMaxHealth();
		float f2 = Math.max(0.0F, Math.min(f, f1) / f1);
		return Color.HSBtoRGB(f2 / 3.0F, 1.0F, 0.75F) | 0xFF000000;
	}

	private void block() {
		if (!block && autoblockmode.equalsIgnorecase("NCP") && target != null
				&& mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
			block = true;
			mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(this.target, this.target.getPositionVector()));
			mc.thePlayer.sendQueue
					.addToSendQueue(new C02PacketUseEntity(this.target, C02PacketUseEntity.Action.INTERACT));
			mc.getNetHandler()
					.addToSendQueueSilent(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
		}
	}

	public void unblock() {
		if (autoblockmode.equalsIgnorecase("NCP") && block && target != null) {
			block = false;
			mc.getNetHandler().addToSendQueueSilent(new C07PacketPlayerDigging(
					C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
		}
	}
}
