package net.minecraft.client.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AccessibilityScreen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.client.renderer.RenderSkybox;
import net.minecraft.client.renderer.RenderSkyboxCube;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.realms.RealmsBridgeScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedConstants;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.optifine.reflect.Reflector;
import net.optifine.reflect.ReflectorForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector4i;
import wtf.expensive.managment.Managment;
import wtf.expensive.ui.midnight.StyleManager;
import wtf.expensive.util.ClientUtil;
import wtf.expensive.util.font.Fonts;
import wtf.expensive.util.render.*;
import wtf.expensive.util.render.animation.AnimationMath;

import javax.annotation.Nullable;

import static wtf.expensive.util.IMinecraft.mc;

public class MainMenuScreen extends Screen {
    private static final Logger field_238656_b_ = LogManager.getLogger();
    public static final RenderSkyboxCube PANORAMA_RESOURCES = new RenderSkyboxCube(new ResourceLocation("textures/gui/title/background/panorama"));
    private static final ResourceLocation PANORAMA_OVERLAY_TEXTURES = new ResourceLocation("textures/gui/title/background/panorama_overlay.png");
    private static final ResourceLocation ACCESSIBILITY_TEXTURES = new ResourceLocation("textures/gui/accessibility.png");
    private final boolean showTitleWronglySpelled;
    private Button buttonResetDemo;
    private static final ResourceLocation MINECRAFT_TITLE_TEXTURES = new ResourceLocation("textures/gui/title/minecraft.png");
    private static final ResourceLocation MINECRAFT_TITLE_EDITION = new ResourceLocation("textures/gui/title/edition.png");
    private int widthCopyright;
    private int widthCopyrightRest;
    private Screen realmsNotification;
    private boolean hasCheckedForRealmsNotification;

    @Nullable
    private String splashText;
    private final RenderSkybox panorama = new RenderSkybox(PANORAMA_RESOURCES);
    private final boolean showFadeInAnimation;
    private long firstRenderTime;

    public MainMenuScreen() {
        this(false);
    }

    @Override
    public void init(Minecraft minecraft, int width, int height) {
        super.init(minecraft, width, height);
        if (ClientUtil.legitMode) {
            if (this.splashText == null) {
                this.splashText = this.minecraft.getSplashes().getSplashText();
            }

            this.widthCopyright = this.font.getStringWidth("Copyright Mojang AB. Do not distribute!");
            this.widthCopyrightRest = this.width - this.widthCopyright - 2;
            int i = 24;
            int j = this.height / 4 + 48;
            net.minecraft.client.gui.widget.button.Button button = null;


            this.addSingleplayerMultiplayerButtons(j, 24);

            if (Reflector.ModListScreen_Constructor.exists()) {
                button = ReflectorForge.makeButtonMods(this, j, 24);
                this.addButton(button);
            }

            this.addButton(new ImageButton(this.width / 2 - 124, j + 72 + 12, 20, 20, 0, 106, 20, net.minecraft.client.gui.widget.button.Button.WIDGETS_LOCATION, 256, 256, (p_lambda$init$0_1_) ->
            {
                this.minecraft.displayGuiScreen(new LanguageScreen(this, this.minecraft.gameSettings, this.minecraft.getLanguageManager()));
            }, new TranslationTextComponent("narrator.button.language")));
            this.addButton(new net.minecraft.client.gui.widget.button.Button(this.width / 2 - 100, j + 72 + 12, 98, 20, new TranslationTextComponent("menu.options"), (p_lambda$init$1_1_) ->
            {
                this.minecraft.displayGuiScreen(new OptionsScreen(this, this.minecraft.gameSettings));
            }));
            this.addButton(new net.minecraft.client.gui.widget.button.Button(this.width / 2 + 2, j + 72 + 12, 98, 20, new TranslationTextComponent("menu.quit"), (p_lambda$init$2_1_) ->
            {
                this.minecraft.shutdown();
            }));
            this.addButton(new ImageButton(this.width / 2 + 104, j + 72 + 12, 20, 20, 0, 0, 20, ACCESSIBILITY_TEXTURES, 32, 64, (p_lambda$init$3_1_) ->
            {
                this.minecraft.displayGuiScreen(new AccessibilityScreen(this, this.minecraft.gameSettings));
            }, new TranslationTextComponent("narrator.button.accessibility")));
            this.minecraft.setConnectedToRealms(false);

            if (this.minecraft.gameSettings.realmsNotifications && !this.hasCheckedForRealmsNotification) {
                RealmsBridgeScreen realmsbridgescreen = new RealmsBridgeScreen();
                this.realmsNotification = realmsbridgescreen.func_239555_b_(this);
                this.hasCheckedForRealmsNotification = true;
            }

            if (this.areRealmsNotificationsEnabled()) {
                this.realmsNotification.init(this.minecraft, this.width, this.height);
            }

            return;
        }

        int buttonWidth = (int) ((int) (353 / 2f));
        int buttonHeight = (int) ((int) (68 / 2f));
        int off = (int) (buttonHeight + 5);
        this.addButton(new Button((int) (mc.getMainWindow().scaledWidth() / 2 - buttonWidth / 2f), mc.getMainWindow().scaledHeight() / 2  - 15, buttonWidth, buttonHeight, new StringTextComponent("singleplayer"), p_onPress_1_ -> {
            mc.displayGuiScreen(new WorldSelectionScreen(this));
        }));
        this.addButton(new Button((int) (mc.getMainWindow().scaledWidth() / 2 - buttonWidth / 2f), mc.getMainWindow().scaledHeight() / 2 - 15 + off, buttonWidth, buttonHeight, new StringTextComponent("multiplayer"), p_onPress_1_ -> {
            mc.displayGuiScreen(new MultiplayerScreen(this));
        }));
        this.addButton(new Button((int) (mc.getMainWindow().scaledWidth() / 2 - buttonWidth / 2f), mc.getMainWindow().scaledHeight() / 2  - 15 + off * 2, buttonWidth, buttonHeight, new StringTextComponent("alt manager"), p_onPress_1_ -> {
            mc.displayGuiScreen(Managment.ALT);
        }));
        this.addButton(new Button((int) (mc.getMainWindow().scaledWidth() / 2 - buttonWidth / 2f), mc.getMainWindow().scaledHeight() / 2  - 15 + off * 3, buttonWidth, buttonHeight, new StringTextComponent("options"), p_onPress_1_ -> {
            mc.displayGuiScreen(new OptionsScreen(this, this.minecraft.gameSettings));
        }));
        ;
        this.addButton(new Button((int) (mc.getMainWindow().scaledWidth() / 2 - buttonWidth / 2f), (int) (mc.getMainWindow().scaledHeight() / 2  - 15 + 160), buttonWidth, buttonHeight, new StringTextComponent("exit"), p_onPress_1_ -> {
            mc.shutdownMinecraftApplet();
        }));
    }

    private boolean areRealmsNotificationsEnabled() {
        return this.minecraft.gameSettings.realmsNotifications && this.realmsNotification != null;
    }

    private void addSingleplayerMultiplayerButtons(int yIn, int rowHeightIn) {
        this.addButton(new net.minecraft.client.gui.widget.button.Button(this.width / 2 - 100, yIn, 200, 20, new TranslationTextComponent("menu.singleplayer"), (p_lambda$addSingleplayerMultiplayerButtons$4_1_) ->
        {
            this.minecraft.displayGuiScreen(new WorldSelectionScreen(this));
        }));
        boolean flag = this.minecraft.isMultiplayerEnabled();
        net.minecraft.client.gui.widget.button.Button.ITooltip button$itooltip = flag ? net.minecraft.client.gui.widget.button.Button.field_238486_s_ : (p_lambda$addSingleplayerMultiplayerButtons$5_1_, p_lambda$addSingleplayerMultiplayerButtons$5_2_, p_lambda$addSingleplayerMultiplayerButtons$5_3_, p_lambda$addSingleplayerMultiplayerButtons$5_4_) ->
        {
            if (!p_lambda$addSingleplayerMultiplayerButtons$5_1_.active) {
                this.renderTooltip(p_lambda$addSingleplayerMultiplayerButtons$5_2_, this.minecraft.fontRenderer.trimStringToWidth(new TranslationTextComponent("title.multiplayer.disabled"), Math.max(this.width / 2 - 43, 170)), p_lambda$addSingleplayerMultiplayerButtons$5_3_, p_lambda$addSingleplayerMultiplayerButtons$5_4_);
            }
        };
        (this.addButton(new net.minecraft.client.gui.widget.button.Button(this.width / 2 - 100, yIn + rowHeightIn * 1, 200, 20, new TranslationTextComponent("menu.multiplayer"), (p_lambda$addSingleplayerMultiplayerButtons$6_1_) ->
        {
            Screen screen = (Screen) (this.minecraft.gameSettings.skipMultiplayerWarning ? new MultiplayerScreen(this) : new MultiplayerWarningScreen(this));
            this.minecraft.displayGuiScreen(screen);
        }, button$itooltip))).active = flag;
        (this.addButton(new net.minecraft.client.gui.widget.button.Button(this.width / 2 - 100, yIn + rowHeightIn * 2, 200, 20, new TranslationTextComponent("menu.online"), (p_lambda$addSingleplayerMultiplayerButtons$7_1_) ->
        {
            this.switchToRealms();
        }, button$itooltip))).active = flag;

        if (Reflector.ModListScreen_Constructor.exists() && this.buttons.size() > 0) {
            Widget widget = this.buttons.get(this.buttons.size() - 1);
            widget.x = this.width / 2 + 2;
            widget.setWidth(98);
        }
    }

    private void switchToRealms() {
        RealmsBridgeScreen realmsbridgescreen = new RealmsBridgeScreen();
        realmsbridgescreen.func_231394_a_(this);
    }

    public MainMenuScreen(boolean fadeIn) {
        super(new TranslationTextComponent("narrator.screen.title"));
        this.showFadeInAnimation = fadeIn;
        this.showTitleWronglySpelled = (double) (new Random()).nextFloat() < 1.0E-4D;
    }

    public void tick() {
        if (this.areRealmsNotificationsEnabled()) {
            this.realmsNotification.tick();
        }
    }

    public static CompletableFuture<Void> loadAsync(TextureManager texMngr, Executor backgroundExecutor) {
        return CompletableFuture.allOf(texMngr.loadAsync(MINECRAFT_TITLE_TEXTURES, backgroundExecutor), texMngr.loadAsync(MINECRAFT_TITLE_EDITION, backgroundExecutor), texMngr.loadAsync(PANORAMA_OVERLAY_TEXTURES, backgroundExecutor), PANORAMA_RESOURCES.loadAsync(texMngr, backgroundExecutor));
    }

    public boolean isPauseScreen() {
        return false;
    }

    public boolean shouldCloseOnEsc() {
        return false;
    }

    protected void init() {

    }

    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        if (ClientUtil.legitMode) {
            if (this.firstRenderTime == 0L && this.showFadeInAnimation) {
                this.firstRenderTime = Util.milliTime();
            }

            float f = this.showFadeInAnimation ? (float) (Util.milliTime() - this.firstRenderTime) / 1000.0F : 1.0F;
            GlStateManager.disableDepthTest();
            fill(matrixStack, 0, 0, this.width, this.height, -1);
            this.panorama.render(partialTicks, MathHelper.clamp(f, 0.0F, 1.0F));
            int i = 274;
            int j = this.width / 2 - 137;
            int k = 30;
            this.minecraft.getTextureManager().bindTexture(PANORAMA_OVERLAY_TEXTURES);
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, this.showFadeInAnimation ? (float) MathHelper.ceil(MathHelper.clamp(f, 0.0F, 1.0F)) : 1.0F);
            blit(matrixStack, 0, 0, this.width, this.height, 0.0F, 0.0F, 16, 128, 16, 128);
            float f1 = this.showFadeInAnimation ? MathHelper.clamp(f - 1.0F, 0.0F, 1.0F) : 1.0F;
            int l = MathHelper.ceil(f1 * 255.0F) << 24;

            if ((l & -67108864) != 0) {
                this.minecraft.getTextureManager().bindTexture(MINECRAFT_TITLE_TEXTURES);
                RenderSystem.color4f(1.0F, 1.0F, 1.0F, f1);

                if (this.showTitleWronglySpelled) {
                    this.blitBlackOutline(j, 30, (p_lambda$render$10_2_, p_lambda$render$10_3_) ->
                    {
                        this.blit(matrixStack, p_lambda$render$10_2_ + 0, p_lambda$render$10_3_, 0, 0, 99, 44);
                        this.blit(matrixStack, p_lambda$render$10_2_ + 99, p_lambda$render$10_3_, 129, 0, 27, 44);
                        this.blit(matrixStack, p_lambda$render$10_2_ + 99 + 26, p_lambda$render$10_3_, 126, 0, 3, 44);
                        this.blit(matrixStack, p_lambda$render$10_2_ + 99 + 26 + 3, p_lambda$render$10_3_, 99, 0, 26, 44);
                        this.blit(matrixStack, p_lambda$render$10_2_ + 155, p_lambda$render$10_3_, 0, 45, 155, 44);
                    });
                } else {
                    this.blitBlackOutline(j, 30, (p_lambda$render$11_2_, p_lambda$render$11_3_) ->
                    {
                        this.blit(matrixStack, p_lambda$render$11_2_ + 0, p_lambda$render$11_3_, 0, 0, 155, 44);
                        this.blit(matrixStack, p_lambda$render$11_2_ + 155, p_lambda$render$11_3_, 0, 45, 155, 44);
                    });
                }

                this.minecraft.getTextureManager().bindTexture(MINECRAFT_TITLE_EDITION);
                blit(matrixStack, j + 88, 67, 0.0F, 0.0F, 98, 14, 128, 16);

                if (Reflector.ForgeHooksClient_renderMainMenu.exists()) {
                    Reflector.callVoid(Reflector.ForgeHooksClient_renderMainMenu, this, matrixStack, this.font, this.width, this.height, l);
                }

                if (this.splashText != null) {
                    RenderSystem.pushMatrix();
                    RenderSystem.translatef((float) (this.width / 2 + 90), 70.0F, 0.0F);
                    RenderSystem.rotatef(-20.0F, 0.0F, 0.0F, 1.0F);
                    float f2 = 1.8F - MathHelper.abs(MathHelper.sin((float) (Util.milliTime() % 1000L) / 1000.0F * ((float) Math.PI * 2F)) * 0.1F);
                    f2 = f2 * 100.0F / (float) (this.font.getStringWidth(this.splashText) + 32);
                    RenderSystem.scalef(f2, f2, f2);
                    drawCenteredString(matrixStack, this.font, this.splashText, 0, -8, 16776960 | l);
                    RenderSystem.popMatrix();
                }

                String s = "Minecraft " + SharedConstants.getVersion().getName();

                if (this.minecraft.isDemo()) {
                    s = s + " Demo";
                } else {
                    s = s + ("release".equalsIgnoreCase(this.minecraft.getVersionType()) ? "" : "/" + this.minecraft.getVersionType());
                }

                if (this.minecraft.isModdedClient()) {
                    s = s + I18n.format("menu.modded");
                }

                if (Reflector.BrandingControl.exists()) {
                    if (Reflector.BrandingControl_forEachLine.exists()) {
                        BiConsumer<Integer, String> biconsumer = (p_lambda$render$12_3_, p_lambda$render$12_4_) ->
                        {
                            drawString(matrixStack, this.font, p_lambda$render$12_4_, 2, this.height - (10 + p_lambda$render$12_3_ * (9 + 1)), 16777215 | l);
                        };
                        Reflector.call(Reflector.BrandingControl_forEachLine, true, true, biconsumer);
                    }

                    if (Reflector.BrandingControl_forEachAboveCopyrightLine.exists()) {
                        BiConsumer<Integer, String> biconsumer1 = (p_lambda$render$13_3_, p_lambda$render$13_4_) ->
                        {
                            drawString(matrixStack, this.font, p_lambda$render$13_4_, this.width - this.font.getStringWidth(p_lambda$render$13_4_), this.height - (10 + (p_lambda$render$13_3_ + 1) * (9 + 1)), 16777215 | l);
                        };
                        Reflector.call(Reflector.BrandingControl_forEachAboveCopyrightLine, biconsumer1);
                    }
                } else {
                    drawString(matrixStack, this.font, s, 2, this.height - 10, 16777215 | l);
                }

                drawString(matrixStack, this.font, "Copyright Mojang AB. Do not distribute!", this.widthCopyrightRest, this.height - 10, 16777215 | l);

                if (mouseX > this.widthCopyrightRest && mouseX < this.widthCopyrightRest + this.widthCopyright && mouseY > this.height - 10 && mouseY < this.height) {
                    fill(matrixStack, this.widthCopyrightRest, this.height - 1, this.widthCopyrightRest + this.widthCopyright, this.height, 16777215 | l);
                }

                for (Widget widget : this.buttons) {
                    widget.setAlpha(f1);
                }
                if (this.areRealmsNotificationsEnabled() && f1 >= 1.0F) {
                    this.realmsNotification.render(matrixStack, mouseX, mouseY, partialTicks);
                }
                super.render(matrixStack, mouseX, mouseY, partialTicks);
            }
            return;
        }

        float widthPerc = (float) mc.getMainWindow().scaledWidth() / 960;
        float heigthPerc = (float) mc.getMainWindow().scaledHeight() / 505;
        Vec2i fixed = ScaleMath.getMouse((int) mouseX, (int) mouseY);
        mouseX = fixed.getX();
        mouseY = fixed.getY();

        mc.gameRenderer.setupOverlayRendering(2);
        RenderUtil.Render2D.drawImage(new ResourceLocation("expensive/images/backmenu.png"), 0, 0, mc.getMainWindow().scaledWidth(), mc.getMainWindow().scaledHeight(), -1);


        super.render(matrixStack, mouseX, mouseY, partialTicks);
        RenderUtil.Render2D.drawImage(new ResourceLocation("expensive/images/expensive.png"), (mc.getMainWindow().scaledWidth() / 2f) - (317.5f / 2) * widthPerc, mc.getMainWindow().scaledHeight() / 2f - 125,317.5f * widthPerc,50.5f * heigthPerc, -1);

        Fonts.msLight[11].drawString(matrixStack,"all rights reserved 2023.",mc.getMainWindow().scaledWidth() / 2f - Fonts.msLight[11].getWidth("all rights reserved 2023.") / 2f, (mc.getMainWindow().scaledHeight()) / 1.1f, ColorUtil.rgba(200,200,200,100));
        BloomHelper.draw(15, 2, false);
        mc.gameRenderer.setupOverlayRendering();

    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        Vec2i fixed = ScaleMath.getMouse((int) mouseX, (int) mouseY);
        mouseX = fixed.getX();
        mouseY = fixed.getY();
        return super.mouseClicked(mouseX, mouseY, button);
    }

    public void onClose() {
        if (this.realmsNotification != null) {
            this.realmsNotification.onClose();
        }
    }

    public static class Button extends AbstractButton {

        public static final net.minecraft.client.gui.widget.button.Button.ITooltip field_238486_s_ = (button, matrixStack, mouseX, mouseY) ->
        {
        };
        protected final net.minecraft.client.gui.widget.button.Button.IPressable onPress;
        protected final net.minecraft.client.gui.widget.button.Button.ITooltip onTooltip;

        public Button(int x, int y, int width, int height, ITextComponent title, net.minecraft.client.gui.widget.button.Button.IPressable pressedAction) {
            this(x, y, width, height, title, pressedAction, field_238486_s_);
        }

        public Button(int x, int y, int width, int height, ITextComponent title, net.minecraft.client.gui.widget.button.Button.IPressable pressedAction, net.minecraft.client.gui.widget.button.Button.ITooltip onTooltip) {
            super(x, y, width, height, title);
            this.onPress = pressedAction;
            this.onTooltip = onTooltip;
        }

        public float animation;

        public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
            animation = AnimationMath.lerp(animation, isHovered() ? 1f : 0, 10);

            RenderUtil.Render2D.drawRoundOutline(x, y, width, height, 5f, 0f, ColorUtil.rgba(25, 26, 33, 0), new Vector4i(
                    ColorUtil.interpolateColor(ColorUtil.rgba(53, 56, 81,255), ColorUtil.rgba(129, 147, 196,255), animation)
            ));
            GaussianBlur.startBlur();
            RenderUtil.Render2D.drawRoundOutline(x, y, width, height, 5f, 0f, StyleManager.HexColor.reAlphaInt(0x13151b, 255), new Vector4i(
                    ColorUtil.rgba(25, 26, 33, 0), ColorUtil.rgba(25, 26, 33, 0), ColorUtil.rgba(25, 26, 33, 0), ColorUtil.rgba(25, 26, 33, 0)
            ));
            GaussianBlur.endBlur(20 + (animation * 10), 2);

            BloomHelper.registerRenderCall(() -> {
                Fonts.msRegular[23].drawCenteredString(matrixStack, this.getMessage().getString(), x + width / 2f, y + height / 2f - Fonts.msRegular[23].getFontHeight() / 2f + 2, RenderUtil.reAlphaInt(ColorUtil.interpolateColor(ColorUtil.rgba(132, 135, 147,255), -1, animation), (int) (255 * animation)));

            });

            Fonts.msRegular[23].drawCenteredString(matrixStack, this.getMessage().getString(), x + width / 2f, y + height / 2f - Fonts.msRegular[23].getFontHeight() / 2f + 2, RenderUtil.reAlphaInt(ColorUtil.interpolateColor(ColorUtil.rgba(132, 135, 147,255), -1, animation), (int) (255)));
        }


        public void onPress() {
            this.onPress.onPress(this);
        }


    }
}
