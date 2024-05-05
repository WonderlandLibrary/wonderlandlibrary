package net.minecraft.client.gui;

import best.azura.client.impl.Client;
import best.azura.client.impl.module.impl.render.BlurModule;
import best.azura.client.impl.rpc.DiscordRPCImpl;
import best.azura.client.impl.ui.gui.impl.buttons.ButtonImpl;
import best.azura.client.impl.ui.gui.MainLogin;
import best.azura.client.impl.ui.gui.MainMultiplayer;
import best.azura.client.impl.ui.gui.MainSingleplayer;
import best.azura.client.impl.ui.gui.account.MainAccount;
import best.azura.client.impl.ui.gui.proxy.MainProxy;
import best.azura.client.util.render.RenderUtil;
import best.azura.client.util.render.StencilUtil;
import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonStreamParser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.demo.DemoWorldServer;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldInfo;
import net.optifine.CustomPanorama;
import net.optifine.CustomPanoramaProperties;
import net.optifine.reflect.Reflector;
import org.apache.commons.io.Charsets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.Project;

import javax.net.ssl.HttpsURLConnection;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import static org.lwjgl.opengl.GL11.*;

public class GuiMainMenu extends GuiScreen implements GuiYesNoCallback {
	private static final AtomicInteger field_175373_f = new AtomicInteger(0);
	private static final Logger logger = LogManager.getLogger();
	private static final Random RANDOM = new Random();

	/**
	 * Timer used to rotate the panorama, increases every tick.
	 */
	private int panoramaTimer;

	/**
	 * The Object object utilized as a thread lock when performing non thread-safe operations
	 */
	private final Object threadLock = new Object();

	private static final ResourceLocation splashTexts = new ResourceLocation("texts/splashes.txt");
	private static final ResourceLocation minecraftTitleTextures = new ResourceLocation("textures/gui/title/minecraft.png");

	/**
	 * An array of all the paths to the panorama pictures.
	 */
	private static final ResourceLocation[] titlePanoramaPaths = new ResourceLocation[]{new ResourceLocation("textures/gui/title/background/panorama_0.png"), new ResourceLocation("textures/gui/title/background/panorama_1.png"), new ResourceLocation("textures/gui/title/background/panorama_2.png"), new ResourceLocation("textures/gui/title/background/panorama_3.png"), new ResourceLocation("textures/gui/title/background/panorama_4.png"), new ResourceLocation("textures/gui/title/background/panorama_5.png")};
	public static final String field_96138_a = "Please click " + EnumChatFormatting.UNDERLINE + "here" + EnumChatFormatting.RESET + " for more information.";
	private ResourceLocation backgroundTexture;

	/**
	 * Minecraft Realms button.
	 */
	private GuiButton realmsButton;
	private final boolean L;
	private GuiScreen M;
	private GuiScreen modUpdateNotification;

	private final ArrayList<ButtonImpl> buttons = new ArrayList<ButtonImpl>();
	private double animation = 0;
	private long start = 0;
	private GuiScreen toShow;
	private final double beginAnimation = 0;

	public GuiMainMenu() {
		/**
		 * OpenGL graphics card warning.
		 */
		String openGLWarning2 = field_96138_a;
		this.L = false;
		/**
		 * The splash message.
		 */
		String splashText = "missingno";
		BufferedReader bufferedreader = null;

		try {
			List<String> list = Lists.newArrayList();
			bufferedreader = new BufferedReader(new InputStreamReader(Minecraft.getMinecraft().getResourceManager().getResource(splashTexts).getInputStream(), Charsets.UTF_8));
			String s;

			while ((s = bufferedreader.readLine()) != null) {
				s = s.trim();

				if (!s.isEmpty()) {
					list.add(s);
				}
			}

			if (!list.isEmpty()) {
				while (true) {
					splashText = list.get(RANDOM.nextInt(list.size()));

					if (splashText.hashCode() != 125780783) {
						break;
					}
				}
			}
		} catch (IOException var12) {
		} finally {
			if (bufferedreader != null) {
				try {
					bufferedreader.close();
				} catch (IOException var11) {
				}
			}
		}

		/**
		 * Counts the number of screen updates.
		 */
		float updateCounter = RANDOM.nextFloat();
		/**
		 * OpenGL graphics card warning.
		 */
		String openGLWarning1 = "";

		if (!GLContext.getCapabilities().OpenGL20 && !OpenGlHelper.areShadersSupported()) {
			openGLWarning1 = I18n.format("title.oldgl1");
			openGLWarning2 = I18n.format("title.oldgl2");
			/**
			 * Link to the Mojang Support about minimum requirements
			 */
			String openGLWarningLink = "https://help.mojang.com/customer/portal/articles/325948?ref=game";
		}
	}

	private boolean a() {
		return Minecraft.getMinecraft().gameSettings.getOptionOrdinalValue(GameSettings.Options.enumFloat) && this.M != null;
	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	public void updateScreen() {
		++this.panoramaTimer;

		if (this.a()) {
			this.M.updateScreen();
		}
	}

	/**
	 * Returns true if this GUI should pause the game when it is displayed in single-player
	 */
	public boolean doesGuiPauseGame() {
		return false;
	}

	/**
	 * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
	 * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
	 */
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
	 * window resizes, the buttonList is cleared beforehand.
	 */
	public void initGui() {

		if (Client.INSTANCE.sessionToken.isEmpty() || Client.INSTANCE.clientUserName.isEmpty()|| Client.INSTANCE.getIrcConnector() == null || Client.INSTANCE.getIrcConnector().username.isEmpty() || Client.INSTANCE.getIrcConnector().password.isEmpty()) Minecraft.getMinecraft().shutdown();

		try {
			URL url = new URL("https://api.azura.best/session/check");
			HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
			connection.addRequestProperty("User-Agent", "Mozilla/5.0 Gecko/20100316 Firefox/3.6.2 AzuraX/" + Client.VERSION);
			connection.addRequestProperty("Accept", "application/json");
			connection.setRequestProperty("Session-Token", Base64.getEncoder().encodeToString(Client.INSTANCE.sessionToken.getBytes()));
			// Set its primary task to output.
			connection.setDoOutput(true);
			connection.connect();

			if (connection.getResponseCode() == 429) {
				try {
					// Parse the String into a URL.
					url = new URL("https://azura.best/api/session/check");
				} catch (Exception ignore) {
				}

				// Create a URL Connection with the URL.
				connection = (HttpsURLConnection) url.openConnection();

				// Set RequestInfo.
				connection.addRequestProperty("User-Agent", "Mozilla/5.0 Gecko/20100316 Firefox/3.6.2 AzuraX/" + Client.VERSION);
				connection.addRequestProperty("Accept", "application/json");
				connection.setRequestProperty("Session-Token", Base64.getEncoder().encodeToString(Client.INSTANCE.sessionToken.getBytes()));
				if (!connection.getDoOutput()) {
					// Set its primary task to output.
					connection.setDoOutput(true);
				}
			}

			// Variable to convert to JSON.
			JsonStreamParser jsonStreamParser;

			try {
				// Create the Parser.
				jsonStreamParser = new JsonStreamParser(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
			} catch (Exception exception) {
				Minecraft.getMinecraft().shutdown();
				return;
			}

			if (jsonStreamParser.hasNext()) {
				JsonElement jsonElement = jsonStreamParser.next();
				if (jsonElement.isJsonObject()) {
					JsonObject jsonObject1 = jsonElement.getAsJsonObject();
					if (!jsonObject1.has("success") || !jsonObject1.get("success").getAsBoolean())
						Minecraft.getMinecraft().shutdown();
				} else {
					Minecraft.getMinecraft().shutdown();
				}
			} else {
				Minecraft.getMinecraft().shutdown();
			}

			connection.disconnect();
		} catch (Exception ignored) {
			Minecraft.getMinecraft().shutdown();
		}

		this.animation = 0;
		this.start = System.currentTimeMillis();
		this.toShow = null;

		if (Client.INSTANCE.getIrcConnector() != null && Client.INSTANCE.getIrcConnector().getClientSocket() != null && Client.INSTANCE.getIrcConnector().getClientSocket().isClosed()) {
			Client.INSTANCE.getIrcConnector().startConnection();
		}

		/**
		 * Texture allocated for the current viewport of the main menu's panorama background.
		 */
		DynamicTexture viewportTexture = new DynamicTexture(256, 256);
		this.backgroundTexture = this.mc.getTextureManager().getDynamicTextureLocation("background", viewportTexture);

		this.buttons.clear();
		ButtonImpl button;
		this.buttons.add(button = new ButtonImpl("Singleplayer", mc.displayWidth / 2 - 470, mc.displayHeight / 2 - 400, 940, 70, 5));
		button.description = "Play worlds, chill";
		this.buttons.add(button = new ButtonImpl("Multiplayer", mc.displayWidth / 2 - 500, mc.displayHeight / 2 - 325, 940, 70, 5));
		button.description = "Change version, join servers";
		this.buttons.add(button = new ButtonImpl("Accounts", mc.displayWidth / 2 - 500, mc.displayHeight / 2 - 250, 940, 70, 5));
		button.description = "Login to premium/cracked accounts";
		this.buttons.add(button = new ButtonImpl("Options", mc.displayWidth / 2 - 500, mc.displayHeight / 2 - 175, 940, 70, 5));
		button.description = "General settings and video settings";
		this.buttons.add(button = new ButtonImpl("Proxy", mc.displayWidth / 2 - 500, mc.displayHeight / 2 - 100, 940, 70, 5));
		button.description = "Manage the Azura proxy login";
		this.buttons.add(button = new ButtonImpl("Exit", mc.displayWidth / 2 - 500, mc.displayHeight / 2 - 25, 940, 70, 5));
		button.description = "Close the game";

	}

	/**
	 * Adds Singleplayer and Multiplayer buttons on Main Menu for players who have bought the game.
	 */
	private void addSingleplayerMultiplayerButtons(int p_73969_1_, int p_73969_2_) {
		this.buttonList.add(new GuiButton(1, this.width / 2 - 100, p_73969_1_, I18n.format("menu.singleplayer")));
		this.buttonList.add(new GuiButton(2, this.width / 2 - 100, p_73969_1_ + p_73969_2_, I18n.format("menu.multiplayer")));

		if (Reflector.GuiModList_Constructor.exists()) {
			//  this.buttonList.add(this.realmsButton = new GuiButton(14, this.width / 2 + 2, p_73969_1_ + p_73969_2_ * 2, 98, 20, I18n.format("menu.online", new Object[0]).replace("Minecraft", "").trim()));
			GuiButton modButton;
			this.buttonList.add(modButton = new GuiButton(6, this.width / 2 - 100, p_73969_1_ + p_73969_2_ * 2, 98, 20, I18n.format("fml.menu.mods")));
		} else {
			// this.buttonList.add(this.realmsButton = new GuiButton(14, this.width / 2 - 100, p_73969_1_ + p_73969_2_ * 2, I18n.format("menu.online", new Object[0])));
		}
	}

	/**
	 * Adds Demo buttons on Main Menu for players who are playing Demo.
	 */
	private void addDemoButtons(int p_73972_1_) {
		this.buttonList.add(new GuiButton(11, this.width / 2 - 100, p_73972_1_, I18n.format("menu.playdemo")));
		GuiButton buttonResetDemo;
		this.buttonList.add(buttonResetDemo = new GuiButton(12, this.width / 2 - 100, p_73972_1_ + 24, I18n.format("menu.resetdemo")));
		ISaveFormat isaveformat = this.mc.getSaveLoader();
		WorldInfo worldinfo = isaveformat.getWorldInfo("Demo_World");

		if (worldinfo == null) {
			buttonResetDemo.enabled = false;
		}
	}

	/**
	 * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
	 */
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0) {
			this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
		}

    /* if (button.id == 5)
        {
            this.mc.displayGuiScreen(new GuiLanguage(this, this.mc.gameSettings, this.mc.getLanguageManager()));
        }

     */

		if (button.id == 1) {
			this.mc.displayGuiScreen(new GuiSelectWorld(this));
		}

		if (button.id == 2) {
			this.mc.displayGuiScreen(new GuiMultiplayer(this));
		}

		if (button.id == 4) {
			mc.displayGuiScreen(new MainLogin(this));
		}

		if (button.id == 6 && Reflector.GuiModList_Constructor.exists()) {
			this.mc.displayGuiScreen((GuiScreen) Reflector.newInstance(Reflector.GuiModList_Constructor, new Object[]{this}));
		}

		if (button.id == 11) {
			this.mc.launchIntegratedServer("Demo_World", "Demo_World", DemoWorldServer.demoWorldSettings);
		}

		if (button.id == 12) {
			ISaveFormat isaveformat = this.mc.getSaveLoader();
			WorldInfo worldinfo = isaveformat.getWorldInfo("Demo_World");

			if (worldinfo != null) {
				GuiYesNo guiyesno = GuiSelectWorld.makeDeleteWorldYesNo(this, worldinfo.getWorldName(), 12);
				this.mc.displayGuiScreen(guiyesno);
			}
		}
	}

	public void confirmClicked(boolean result, int id) {

	}

	/**
	 * Draws the main menu panorama
	 */
	private void drawPanorama(float p_73970_3_) {
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		GlStateManager.matrixMode(5889);
		GlStateManager.pushMatrix();
		GlStateManager.loadIdentity();
		Project.gluPerspective(120.0F, 1.0F, 0.05F, 10.0F);
		GlStateManager.matrixMode(5888);
		GlStateManager.pushMatrix();
		GlStateManager.loadIdentity();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
		GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.disableCull();
		GlStateManager.depthMask(false);
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		int i = 8;
		int j = 64;
		CustomPanoramaProperties custompanoramaproperties = CustomPanorama.getCustomPanoramaProperties();

		if (custompanoramaproperties != null) {
			j = custompanoramaproperties.getBlur1();
		}

		for (int k = 0; k < j; ++k) {
			GlStateManager.pushMatrix();
			float f = ((float) (k % i) / (float) i - 0.5F) / 64.0F;
			float f1 = ((float) (k / i) / (float) i - 0.5F) / 64.0F;
			float f2 = 0.0F;
			GlStateManager.translate(f, f1, f2);
			GlStateManager.rotate(MathHelper.sin(((float) this.panoramaTimer + p_73970_3_) / 400.0F) * 25.0F + 20.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(-((float) this.panoramaTimer + p_73970_3_) * 0.1F, 0.0F, 1.0F, 0.0F);

			for (int l = 0; l < 6; ++l) {
				GlStateManager.pushMatrix();

				if (l == 1) {
					GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
				}

				if (l == 2) {
					GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
				}

				if (l == 3) {
					GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
				}

				if (l == 4) {
					GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
				}

				if (l == 5) {
					GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
				}

				ResourceLocation[] aresourcelocation = titlePanoramaPaths;

				if (custompanoramaproperties != null) {
					aresourcelocation = custompanoramaproperties.getPanoramaLocations();
				}

				this.mc.getTextureManager().bindTexture(aresourcelocation[l]);
				worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
				int i1 = 255 / (k + 1);
				float f3 = 0.0F;
				worldrenderer.pos(-1.0D, -1.0D, 1.0D).tex(0.0D, 0.0D).color(255, 255, 255, i1).endVertex();
				worldrenderer.pos(1.0D, -1.0D, 1.0D).tex(1.0D, 0.0D).color(255, 255, 255, i1).endVertex();
				worldrenderer.pos(1.0D, 1.0D, 1.0D).tex(1.0D, 1.0D).color(255, 255, 255, i1).endVertex();
				worldrenderer.pos(-1.0D, 1.0D, 1.0D).tex(0.0D, 1.0D).color(255, 255, 255, i1).endVertex();
				tessellator.draw();
				GlStateManager.popMatrix();
			}

			GlStateManager.popMatrix();
			GlStateManager.colorMask(true, true, true, false);
		}

		worldrenderer.setTranslation(0.0D, 0.0D, 0.0D);
		GlStateManager.colorMask(true, true, true, true);
		GlStateManager.matrixMode(5889);
		GlStateManager.popMatrix();
		GlStateManager.matrixMode(5888);
		GlStateManager.popMatrix();
		GlStateManager.depthMask(true);
		GlStateManager.enableCull();
		GlStateManager.enableDepth();
	}

	/**
	 * Rotate and blurs the skybox view in the main menu
	 */
	private void rotateAndBlurSkybox(float p_73968_1_) {
		this.mc.getTextureManager().bindTexture(this.backgroundTexture);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glCopyTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, 0, 0, 256, 256);
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.colorMask(true, true, true, false);
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
		GlStateManager.disableAlpha();
		int i = 3;
		int j = 3;
		CustomPanoramaProperties custompanoramaproperties = CustomPanorama.getCustomPanoramaProperties();

		if (custompanoramaproperties != null) {
			j = custompanoramaproperties.getBlur2();
		}

		for (int k = 0; k < j; ++k) {
			float f = 1.0F / (float) (k + 1);
			int l = this.width;
			int i1 = this.height;
			float f1 = (float) (k - i / 2) / 256.0F;
			worldrenderer.pos(l, i1, this.zLevel).tex(0.0F + f1, 1.0D).color(1.0F, 1.0F, 1.0F, f).endVertex();
			worldrenderer.pos(l, 0.0D, this.zLevel).tex(1.0F + f1, 1.0D).color(1.0F, 1.0F, 1.0F, f).endVertex();
			worldrenderer.pos(0.0D, 0.0D, this.zLevel).tex(1.0F + f1, 0.0D).color(1.0F, 1.0F, 1.0F, f).endVertex();
			worldrenderer.pos(0.0D, i1, this.zLevel).tex(0.0F + f1, 0.0D).color(1.0F, 1.0F, 1.0F, f).endVertex();
		}

		tessellator.draw();
		GlStateManager.enableAlpha();
		GlStateManager.colorMask(true, true, true, true);
	}

	/**
	 * Renders the skybox in the main menu
	 */
	private void renderSkybox(int p_73971_1_, float p_73971_3_) {
		this.mc.getFramebuffer().unbindFramebuffer();
		GlStateManager.viewport(0, 0, 256, 256);
		this.drawPanorama(p_73971_3_);
		this.rotateAndBlurSkybox(p_73971_3_);
		int i = 3;
		CustomPanoramaProperties custompanoramaproperties = CustomPanorama.getCustomPanoramaProperties();

		if (custompanoramaproperties != null) {
			i = custompanoramaproperties.getBlur3();
		}

		for (int j = 0; j < i; ++j) {
			this.rotateAndBlurSkybox(p_73971_3_);
			this.rotateAndBlurSkybox(p_73971_3_);
		}

		this.mc.getFramebuffer().bindFramebuffer(true);
		GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
		float f2 = this.width > this.height ? 120.0F / (float) this.width : 120.0F / (float) this.height;
		float f = (float) this.height * f2 / 256.0F;
		float f1 = (float) this.width * f2 / 256.0F;
		int k = this.width;
		int l = this.height;
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
		worldrenderer.pos(0.0D, l, this.zLevel).tex(0.5F - f, 0.5F + f1).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
		worldrenderer.pos(k, l, this.zLevel).tex(0.5F - f, 0.5F - f1).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
		worldrenderer.pos(k, 0.0D, this.zLevel).tex(0.5F + f, 0.5F - f1).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
		worldrenderer.pos(0.0D, 0.0D, this.zLevel).tex(0.5F + f, 0.5F + f1).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
		tessellator.draw();
	}

	/**
	 * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
	 */
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {

		if (toShow != null) {
			float anim = Math.min(1, (System.currentTimeMillis() - start) / 500f);
			animation = -1 * Math.pow(anim - 1, 6) + 1;
			animation = 1 - animation;
		} else {
			float anim = Math.min(1, (System.currentTimeMillis() - start) / 1000f);
			animation = -1 * Math.pow(anim - 1, 6) + 1;
		}

		if (toShow != null && animation == 0) {
			mc.displayGuiScreen(toShow);
			return;
		}
		GlStateManager.pushMatrix();
		glEnable(GL_BLEND);
		drawDefaultBackground();

		RenderUtil.INSTANCE.scaleFix(1.0);
		final ScaledResolution sr = new ScaledResolution(mc);
		final boolean blur = Client.INSTANCE.getModuleManager().getModule(BlurModule.class).isEnabled() && BlurModule.blurMenu.getObject();
		if (blur) {
			GL11.glPushMatrix();
			RenderUtil.INSTANCE.invertScaleFix(1.0);
			StencilUtil.initStencilToWrite();
			GlStateManager.scale(1.0 / sr.getScaleFactor(), 1.0 / sr.getScaleFactor(), 1);
		}
		RenderUtil.INSTANCE.drawRoundedRect(mc.displayWidth / 2. - 500, mc.displayHeight / 2. - 400, 1000, 800, 10, new Color(0, 0, 0, 170));
		if (blur) {
			GlStateManager.scale(sr.getScaleFactor(), sr.getScaleFactor(), 1);
			StencilUtil.readStencilBuffer(1);
			BlurModule.blurShader.blur();
			StencilUtil.uninitStencilBuffer();
			RenderUtil.INSTANCE.scaleFix(1.0);
			GL11.glPopMatrix();
		}

		int count = 0;
		GlStateManager.enableBlend();
		DiscordRPCImpl.updateNewPresence(
				"Main menu",
				"Logged in as " + Client.INSTANCE.getUsername() + " (" + Client.releaseBuild + ")" + "!"
		);
		Display.setTitle("Minecraft 1.8.9");
		for (ButtonImpl button : buttons) {

			button.animation = animation;

			button.x = mc.displayWidth / 2 - 470;
			button.y = mc.displayHeight / 2 - 380 + count * 78;
			button.draw(mouseX, mouseY);
			count++;

		}

		glDisable(GL_BLEND);
		GlStateManager.popMatrix();

		if (this.a()) {
			this.M.drawScreen(mouseX, mouseY, partialTicks);
		}

		if (this.modUpdateNotification != null) {
			this.modUpdateNotification.drawScreen(mouseX, mouseY, partialTicks);
		}
	}

	/**
	 * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
	 */
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);

		if (toShow != null) return;

		boolean multiplayer = false;
		String clickedButton = "";
		for (ButtonImpl button : buttons) {
			if (button.serverData != null && button.hovered) {
				multiplayer = true;
				clickedButton = button.text;
			} else if (button.hovered) {
				clickedButton = button.text;
			}
		}

		if (multiplayer) {
			// TODO: Connection to current button
			return;
		}

		if (clickedButton.equals("")) {
			return;
		}

		switch (clickedButton) {
			case "Singleplayer":
				toShow = new MainSingleplayer(this);
				animation = 0;
				this.start = System.currentTimeMillis();
				break;
			case "Multiplayer":
				toShow = new MainMultiplayer(this);
				animation = 0;
				this.start = System.currentTimeMillis();
				break;
			case "Accounts":
				toShow = new MainAccount(this);
				animation = 0;
				this.start = System.currentTimeMillis();
				break;
			case "Options":
				mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings));
				break;
			case "Proxy":
				mc.displayGuiScreen(new MainProxy(this));
				break;
			case "Exit":
				mc.shutdown();
				break;
			case "Direct connect":
				mc.displayGuiScreen(new GuiScreenServerList(this, new ServerData("Minecraft Server", "", false)));
				break;
			case "Add":
				mc.displayGuiScreen(new GuiScreenAddServer(this, new ServerData("Minecraft Server", "", false)));
				break;
			case "Back":
				mc.displayGuiScreen(new GuiMainMenu());
				break;
		}
	}

	/**
	 * Called when the screen is unloaded. Used to disable keyboard repeat events
	 */
	public void onGuiClosed() {
		if (this.M != null) {
			this.M.onGuiClosed();
		}
	}
}
