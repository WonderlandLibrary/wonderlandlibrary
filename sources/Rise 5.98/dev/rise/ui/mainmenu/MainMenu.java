package dev.rise.ui.mainmenu;

import dev.rise.Rise;
import dev.rise.font.CustomFont;
import dev.rise.font.fontrenderer.TTFFontRenderer;
import dev.rise.module.impl.render.Blur;
import dev.rise.module.impl.render.ClickGui;
import dev.rise.ui.clickgui.impl.ClickGUI;
import dev.rise.ui.guitheme.GuiTheme;
import dev.rise.ui.guitheme.Theme;
import dev.rise.ui.proxy.ProxyGUI;
import dev.rise.ui.version.VersionGui;
import dev.rise.util.math.TimeUtil;
import dev.rise.util.render.BlurUtil;
import dev.rise.util.render.RenderUtil;
import dev.rise.util.render.UIUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.optifine.CustomPanorama;
import net.optifine.CustomPanoramaProperties;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Project;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

public final class MainMenu extends GuiScreen {

    //Timer used to rotate the panorama, increases every tick.
    public static int panoramaTimer = 1500;

    //Path to images
    private static final ResourceLocation[] titlePanoramaPaths = new ResourceLocation[]{new ResourceLocation("rise/panorama/panorama_0.png"), new ResourceLocation("rise/panorama/panorama_1.png"), new ResourceLocation("rise/panorama/panorama_2.png"), new ResourceLocation("rise/panorama/panorama_3.png"), new ResourceLocation("rise/panorama/panorama_4.png"), new ResourceLocation("rise/panorama/panorama_5.png")};

    // Font renderer
    private static final TTFFontRenderer fontRenderer = CustomFont.FONT_MANAGER.getFont("Dreamscape 96");

    //Positions
    private ScaledResolution sr;

    private float x;
    private float y;

    private int cocks;
    private static boolean rolled;

    private float screenWidth;
    private float screenHeight;

    private float buttonWidth = 50;
    private float buttonHeight = 20;
    private float gap = 4;
    public static float smoothedX, smoothedY;
    public static float xOffSet;
    public static float yOffSet;

    public static boolean easterEgg;
    private ClickGUI clickGUI;
    private final TimeUtil timer = new TimeUtil();

    public float pitch;

    private float themeX;
    private float themeY;
    private float themeWidth;
    private float themeHeight;
    public boolean drawingClickGui;

    private BlurUtil blur;
    private long initTime = 0, clickGuiInitTime = 0;

    //Called from the main game loop to update the screen.
    public void updateScreen() {
        ++panoramaTimer;
    }

    public void initGui() {

        if (Rise.INSTANCE.isDestructed()) mc.displayGuiScreen(new GuiMainMenu());

        panoramaTimer = 150;
        easterEgg = Math.random() > 0.99;

        clickGUI = new ClickGUI(mc);
        blur = new BlurUtil();
        initTime = System.currentTimeMillis();
    }

    @Override
    public void onGuiClosed() {
        mc.timer.timerSpeed = 1;
    }

    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {

        // this breaks scrolling? i don't fucking know dude
//        if (mc.mouseHelper != null) mc.mouseHelper.mouseGrab(false);

        // can someone please explain why this ever existed?
//        mc.timer.timerSpeed = 3f;

        //Draws background
        //this.renderSkybox(mouseX, mouseY, partialTicks);
        if (Rise.INSTANCE.getGuiTheme() == null)
            Rise.INSTANCE.guiTheme = new GuiTheme();

        MainMenu.drawBackground(mouseX, mouseY, width, height);
        final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

        // Render the rise text
        screenWidth = fontRenderer.getWidth(Rise.CLIENT_NAME);
        screenHeight = fontRenderer.getHeight(Rise.CLIENT_NAME);


        UIUtil.logoPosition = /*MathUtil.lerp(UIUtil.logoPosition, */sr.getScaledHeight() / 2.0F - (screenHeight / 2.0F) - 6/*, 0.2f)*/;

        x = (sr.getScaledWidth() / 2.0F) - (screenWidth / 2.0F);
        y = (sr.getScaledHeight() / 2.0F) - (screenHeight / 2.0F) - 6;

        // Box
        //RenderUtil.roundedRect(x - 10, y + fontRenderer.getHeight() + buttonHeight * 2 + gap * 2 + 2 - 108, 125, 145, 10, new Color(0, 0, 0, 35));

        fontRenderer.drawString(easterEgg ? "RICE" : Rise.CLIENT_NAME, x, UIUtil.logoPosition, new Color(255, 255, 255, 150).getRGB());

        buttonWidth = 50;
        buttonHeight = 20;
        gap = 4;

        final ArrayList<String> changes = new ArrayList();

//        changes.add("");
//        changes.add("5.93");
//        changes.add("+ Added Terrain, SafeWalk, AntiAFK modules");
//        changes.add("+ Added ClickGUI in Main Menu - try pressing your bind");
//        changes.add("+ Added new 14 BPS Verus speed - Low");
//        changes.add("+ Added massive visual improvements globally");
//        changes.add("+ Added visual improvements to 2D ESP");
//        changes.add("+ Added New online configs tab");
//        changes.add("+ Added New AntiCheat module checks");
//        changes.add("+ Added NCP Tower");
//        changes.add("+ Added New Verus and Mineland disablers");
//        changes.add("+ Added Hycraft Fly");
//        changes.add("+ Added new ghost module improvements");
//        changes.add("+ Added new Hycraft partial Disabler");
//        changes.add("+ Added new Hypixel Disabler");
//        changes.add("+ Added new Hypixel Crits");
//        changes.add("+ Added new Hypixel infinite fly");
//        changes.add("+ Fixed a potentially unsafe bug with autoclicker");
//        changes.add("+ Fixed a bug with scaffold creating ghost items");
//        changes.add("+ Fixed Hypixel speed flags");
//        changes.add("+ Fixed Hypixel speed in water/lava");

//        changes.add("5.97");
  //      changes.add("+ Fastest Hypixel scaffold after patch (Sprint)");
  //      changes.add("+ New Hypixel Timer Disabler");
   //     changes.add("+ New Hypixel Auto Block");
   //     changes.add("+ Further Hypixel LongJump");
     //   changes.add("");
        changes.add("5.99");
        changes.add("+ Some Changes In Scaffold");
        changes.add("+ Idk");
        changes.add("");
        changes.add("");
        changes.add("");
        changes.add("");
        changes.add("");
        changes.add("");

        if (sr.getScaledWidth() > 600 && sr.getScaledHeight() > 300) {
            Color color = Rise.INSTANCE.getGuiTheme().getCurrentTheme().equals(Theme.DARKMODE) ? new Color(200, 200, 200, 150) : new Color(255, 255, 255, 220);
            CustomFont.drawString("ChangeLog:", 5, 5, color.hashCode());

            for (int i = 0; i < changes.size(); i++) {
                CustomFont.drawString(changes.get(i), 5, 16 + i * 12, color.hashCode());
            }
        }

        if (Rise.devMode)
            CustomFont.drawString("Developer Mode (" + Rise.intentAccount.username + ")", sr.getScaledWidth() - 95, 7, Color.WHITE.getRGB());

        //Close
        //CustomFont.drawString("X", x + 103.6F + buttonWidth - 48, y + fontRenderer.getHeight() - 56, new Color(255, 255, 255, 200).hashCode());

        //Singleplayer
        RenderUtil.roundedRect(x, y + fontRenderer.getHeight(), buttonWidth, buttonHeight + 2, 10, new Color(255, 255, 255, 35));
        CustomFont.drawString("Single", x + buttonWidth - 28, y + fontRenderer.getHeight() + 1 + 6, new Color(255, 255, 255, 240).hashCode());

        //Multiplayer
        RenderUtil.roundedRect(x + buttonWidth + gap, y + fontRenderer.getHeight(), buttonWidth, buttonHeight + 2, 10, new Color(255, 255, 255, 35));
        CustomFont.drawString("Multi", x + buttonWidth * 2 + gap - 27, y + fontRenderer.getHeight() + 6 + 1, new Color(255, 255, 255, 240).hashCode());

        //Altmanager
        RenderUtil.roundedRect(x + buttonWidth + gap, y + fontRenderer.getHeight() + 2 + buttonHeight + gap, buttonWidth, buttonHeight + 2, 10, new Color(255, 255, 255, 35));
        CustomFont.drawString("Alt", x + buttonWidth * 2 + gap - 19, y + fontRenderer.getHeight() + buttonHeight + 10 + 3, new Color(255, 255, 255, 240).hashCode());

        //Version
        RenderUtil.roundedRect(x, y + fontRenderer.getHeight() + 2 + buttonHeight + gap, buttonWidth, buttonHeight + 2, 10, new Color(255, 255, 255, 35));
        CustomFont.drawString("Vers", x + gap + 23, y + fontRenderer.getHeight() + buttonHeight + 10 + 3, new Color(255, 255, 255, 240).hashCode());

        //Settings
        RenderUtil.roundedRect(x, y + fontRenderer.getHeight() + buttonHeight * 2 + gap * 2 + 2 + 2, buttonWidth, buttonHeight + gap, 10, new Color(255, 255, 255, 35));
        CustomFont.drawString("Settings", x + buttonWidth - 35, y + fontRenderer.getHeight() + buttonHeight * 2 + 19.5, new Color(255, 255, 255, 240).hashCode());

        //Proxy
        RenderUtil.roundedRect(x + gap + buttonWidth, y + fontRenderer.getHeight() + buttonHeight * 2 + gap * 2 + 2 + 2, buttonWidth, buttonHeight + gap, 10, new Color(255, 255, 255, 35));
        CustomFont.drawString("Proxy", x + buttonWidth * 2 + gap - 28, y + fontRenderer.getHeight() + buttonHeight * 2 + 19.5, new Color(255, 255, 255, 240).hashCode());

        /*
        RenderUtil.roundedRect(4, 4, buttonWidth, buttonHeight + gap, 10, new Color(255, 255, 255, 35));
        CustomFont.drawString(mc.riseMusicTicker.shouldKeepPlaying ? "Stop music" : "Start Music", buttonWidth - CustomFont.getWidth("Stop music") - 2.5f + 4, 8 + 4, new Color(255, 255, 255, 240).hashCode());

        if (mc.riseMusicTicker.shouldKeepPlaying) {
            final String currentlyPlaying = "Currently playing: The Final Flash of Existence (SCP: SL Main Theme) by Jacek 'Fragik' Rogal";
            CustomFont.drawString(currentlyPlaying, buttonWidth + 8, 4, new Color(255, 255, 255, 240).hashCode());
        }*/

        //Quit
        /*
        RenderUtil.roundedRect(sr.getScaledWidth() - 15, 6, 10, 10, 5, new Color(255, 255, 255, 110));
        CustomFont.drawString("x", sr.getScaledWidth() - 12.5, 6, -1);
        */

        //Note
        final String message = Rise.CLIENT_CREDITS;

        if (sr.getScaledHeight() > 300) {
            Color color = Rise.INSTANCE.getGuiTheme().getCurrentTheme().equals(Theme.DARKMODE) ? new Color(200, 200, 200, 150) : new Color(255, 255, 255, 220);
            CustomFont.drawString(message, sr.getScaledWidth() - CustomFont.getWidth(message) - 2, sr.getScaledHeight() - 12.5, color.hashCode());

            if (Rise.INSTANCE.isViaHasFailed())
                CustomFont.drawString("Version Switcher failed to initialize.", 10, sr.getScaledHeight() - 80, new Color(255, 255, 255, 180).hashCode());

            //Theme selector
            themeX = 10;
            themeY = sr.getScaledHeight() - 61;
            themeWidth = 1920f / 22;
            themeHeight = 1080f / 22;

            float offset = 0;
            boolean mouseOverAnyThemes = false;
            for (final Theme theme : Theme.values()) {
                final boolean mouseOver = mouseOver(themeX + offset, themeY, themeWidth, themeHeight, mouseX, mouseY);

                if (!mouseOverAnyThemes && mouseOver) mouseOverAnyThemes = true;

                for (int i = 1; i <= (mouseOver ? 7 : 6); i++)
                    RenderUtil.roundedRect(themeX - i + offset, themeY - i, themeWidth + i * 2, themeHeight + i * 2, 9, new Color(0, 0, 0, 6));

                final Color themeColor = Rise.INSTANCE.getGuiTheme().getThemeColor(theme);

                RenderUtil.color(themeColor);

                int opacity = 100;

                if (theme == Rise.INSTANCE.getGuiTheme().getCurrentTheme())
                    opacity = 200;
                else if (mouseOver)
                    opacity = 150;

                if (theme.opacityInMainMenu < opacity) opacity = (int) theme.opacityInMainMenu;

                RenderUtil.color(new Color(themeColor.getRed(), themeColor.getGreen(), themeColor.getBlue(), opacity));

                if (mouseOver) {
                    RenderUtil.image(new ResourceLocation("rise/backgrounds/blue.png"), themeX + offset - 1, themeY - 1, themeWidth + 2, themeHeight + 2);
                    theme.nameOpacityInMainMenu += 20;
                } else {
                    RenderUtil.image(new ResourceLocation("rise/backgrounds/blue.png"), themeX + offset, themeY, themeWidth, themeHeight);
                    theme.nameOpacityInMainMenu -= 20;
                }

                theme.nameOpacityInMainMenu = Math.max(0, Math.min(225, theme.nameOpacityInMainMenu));

                if (theme.nameOpacityInMainMenu > 1)
                    CustomFont.drawCenteredString(theme.getName(), themeX + offset + 44, themeY + 2, new Color(255, 255, 255, Math.round(theme.getNameOpacityInMainMenu())).getRGB());

                offset += (themeWidth + 10);
            }

            for (final Theme theme : Theme.values()) {
                if (mouseOverAnyThemes) {
                    theme.opacityInMainMenu += 4;
                } else {
                    theme.opacityInMainMenu -= 2;
                }

                if (theme.opacityInMainMenu > 255) theme.opacityInMainMenu = 255;
                if (theme.opacityInMainMenu < 15) theme.opacityInMainMenu = 15;
            }
        }

        super.drawScreen(mouseX, mouseY, partialTicks);

        if (drawingClickGui) {
            blur.blur(Math.round(Math.min(5, (System.currentTimeMillis() - clickGuiInitTime) / 50F)));
//            drawRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), new Color(0, 0, 0, Math.min((System.currentTimeMillis() - clickGuiInitTime) / 5000F, 0.2F)).hashCode());
            clickGUI.drawScreen(mouseX, mouseY, partialTicks);

            if (timer.hasReached(20)) {
                ClickGUI.updateScroll();
                timer.reset();
            }

            String time = DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now());
            CustomFont.drawStringBig(time, sr.getScaledWidth() - CustomFont.getWidthBig(time) - 10, sr.getScaledHeight() - 30, new Color(237, 237, 237, 150).getRGB());

            CustomFont.drawStringBig(Rise.CLIENT_NAME + " " + Rise.CLIENT_VERSION, 10, sr.getScaledHeight() - 45, new Color(237, 237, 237, 150).getRGB());
            CustomFont.drawStringMedium("Registered to " + Rise.intentAccount.username, 10, sr.getScaledHeight() - 24, new Color(237, 237, 237, 150).getRGB());
        }
    }

    public void mouseClicked(final int mouseX, final int mouseY, final int button) {
        if (drawingClickGui) {
            clickGUI.mouseClicked(mouseX, mouseY, button);
            return;
        }

        if (Rise.INSTANCE.isFirstBoot() && !rolled) {
//            try {
//                openWebpage(new URI("https://www.youtube.com/watch?v=dQw4w9WgXcQ"));
//            } catch (final Exception e) {
//                e.printStackTrace();
//            }

            rolled = true;
        }

        float offset = 0;
        for (final Theme theme : Theme.values()) {

            if (mouseOver(themeX + offset, themeY, themeWidth, themeHeight, mouseX, mouseY)) {
                Rise.INSTANCE.getGuiTheme().setCurrentTheme(theme);
            }
            offset += (themeWidth + 10);
        }

        //Close
//        if (mouseOver(x + 103.6F + buttonWidth - 48, y + fontRenderer.getHeight() - 56, buttonWidth / 7, buttonHeight / 2 - 1, mouseX, mouseY)) {
//            System.exit(-1);
//        }

        //Singleplayer
        if (mouseOver(x, y + fontRenderer.getHeight(), buttonWidth, buttonHeight + 2, mouseX, mouseY)) {
            mc.displayGuiScreen(new GuiSelectWorld(this));
        }

        //Multiplayer
        if (mouseOver(x + buttonWidth + gap, y + fontRenderer.getHeight(), buttonWidth, buttonHeight + 2, mouseX, mouseY)) {
            mc.displayGuiScreen(Rise.INSTANCE.getGuiMultiplayer());
        }

        //Altmanager
        if (mouseOver(x + buttonWidth + gap, y + fontRenderer.getHeight() + 2 + buttonHeight + gap, buttonWidth, buttonHeight + 2, mouseX, mouseY)) {
            mc.displayGuiScreen(Rise.INSTANCE.getAltGUI() /*Rise.INSTANCE.getAltManagerGUI()*/);
        }

        //Settings
        if (mouseOver(x, y + fontRenderer.getHeight() + buttonHeight * 2 + gap * 2 + 2 + 2, buttonWidth, buttonHeight + gap, mouseX, mouseY)) {
            mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings));
        }

        //Proxy
        if (mouseOver(x + gap + buttonWidth, y + fontRenderer.getHeight() + buttonHeight * 2 + gap * 2 + 2 + 2, buttonWidth, buttonHeight + gap, mouseX, mouseY)) {
            mc.displayGuiScreen(new ProxyGUI(this));
        }

        //Version
        if (mouseOver(x, y + fontRenderer.getHeight() + 2 + buttonHeight + gap, buttonWidth, buttonHeight + 2, mouseX, mouseY)) {
            if (!Rise.INSTANCE.isViaHasFailed()) {
                if (Rise.INSTANCE.versionSwitcher == null) {
                    Rise.INSTANCE.versionSwitcher = new VersionGui();
                }

                mc.displayGuiScreen(Rise.INSTANCE.versionSwitcher);
            }
        }

        if (mouseOver(0, 0, buttonWidth, buttonHeight + gap, mouseX, mouseY)) {
            if (!mc.riseMusicTicker.shouldKeepPlaying) {
                mc.riseMusicTicker.shouldKeepPlaying = true;
                mc.mcMusicTicker.func_181557_a();
            } else {
                mc.riseMusicTicker.shouldKeepPlaying = false;
                mc.riseMusicTicker.stopPlaying();
            }
        }

    }

    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        if (drawingClickGui) {
            clickGUI.mouseReleased(mouseX, mouseY, state);
        }
    }

    public boolean mouseOver(final float posX, final float posY, final float width, final float height, final float mouseX, final float mouseY) {
        if (mouseX > posX && mouseX < posX + width) {
            return mouseY > posY && mouseY < posY + height;
        }

        return false;
    }

    public static boolean openWebpage(final URI uri) {
        final Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(uri);
                return true;
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (keyCode == Objects.requireNonNull(Rise.INSTANCE.getModuleManager().getModule("ClickGui")).getKeyBind()) {
            drawingClickGui = true;
            clickGUI.initGui();
            Objects.requireNonNull(Rise.INSTANCE.getModuleManager().getModule("ClickGui")).setEnabled(true);
            blur.init();
            clickGuiInitTime = System.currentTimeMillis();
        } else if (keyCode == Keyboard.KEY_ESCAPE && drawingClickGui) {
            drawingClickGui = false;
            clickGUI.onGuiClosed();
            Objects.requireNonNull(Rise.INSTANCE.getModuleManager().getModule("ClickGui")).setEnabled(false);
        }
    }

    public static void drawBackground(int mouseX, int mouseY, float width, float height) {
        Minecraft.getMinecraft().timer.timerSpeed = 1F;
        ++MainMenu.panoramaTimer;

        RenderUtil.color(Rise.INSTANCE.getGuiTheme().getThemeColor());
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("rise/backgrounds/blue.png"));

        final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

        final float scale = 1.66f;
        final float amount = 1500;

        if (MainMenu.panoramaTimer % ((Minecraft.getMinecraft().currentScreen instanceof MainMenu) ? 200 : 100) == 0) {
            MainMenu.xOffSet = (float) (Math.random() - 0.5f) * amount;
            MainMenu.yOffSet = (float) (Math.random() - 0.5f) * amount;
        }

        MainMenu.smoothedX = (MainMenu.smoothedX * 250 + MainMenu.xOffSet) / 259;
        MainMenu.smoothedY = (MainMenu.smoothedY * 250 + MainMenu.yOffSet) / 259;

        drawModalRectWithCustomSizedTexture(0, 0, width / scale + MainMenu.smoothedX - 150, height / scale + MainMenu.smoothedY - 100, width, height, width * scale, height * scale);
    }

    public ClickGUI getClickGUI() {
        return clickGUI;
    }
}
