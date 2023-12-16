// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.ui.screen;

import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.gui.GuiConfirmOpenLink;
import org.lwjgl.input.Mouse;
import java.util.Collection;
import moonsense.ui.utils.GuiUtils;
import java.awt.Color;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.MathHelper;
import org.lwjgl.util.glu.Project;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.util.Session;
import java.util.Iterator;
import moonsense.account.Account;
import moonsense.MoonsenseClient;
import moonsense.ui.elements.button.GuiButtonIcon;
import moonsense.ui.elements.button.GuiCustomButton;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.renderer.OpenGlHelper;
import org.lwjgl.opengl.GLContext;
import java.io.IOException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.apache.commons.io.Charsets;
import net.minecraft.client.Minecraft;
import com.google.common.collect.Lists;
import net.minecraft.util.EnumChatFormatting;
import org.apache.logging.log4j.LogManager;
import moonsense.account.AccountLoginThread;
import moonsense.ui.elements.text.GuiCustomTextField;
import moonsense.ui.elements.button.GuiImageButton;
import moonsense.ui.account.AccountListEntry;
import java.util.ArrayList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.gui.GuiButton;
import java.util.Random;
import org.apache.logging.log4j.Logger;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.client.gui.GuiScreen;

public class GuiAccountManager extends GuiScreen
{
    private static final AtomicInteger field_175373_f;
    private static final Logger logger;
    private static final Random field_175374_h;
    private float updateCounter;
    private String splashText;
    private GuiButton buttonResetDemo;
    public int panoramaTimer;
    private DynamicTexture viewportTexture;
    private boolean field_175375_v;
    private final Object field_104025_t;
    private String field_92025_p;
    private String field_146972_A;
    private String field_104024_v;
    private static final ResourceLocation splashTexts;
    private static final ResourceLocation minecraftTitleTextures;
    private static final ResourceLocation[] titlePanoramaPaths;
    public static final String field_96138_a;
    private int field_92024_r;
    private int field_92023_s;
    private int field_92022_t;
    private int field_92021_u;
    private int field_92020_v;
    private int field_92019_w;
    private ResourceLocation field_110351_G;
    private GuiButton field_175372_K;
    private final GuiScreen parent;
    private final ArrayList<AccountListEntry> accountButtons;
    private int scrollAmount;
    private GuiImageButton mojangButton;
    private GuiImageButton microsoftButton;
    private boolean setRandomAccount;
    private String loginStatus;
    private GuiCustomTextField email;
    private GuiCustomTextField password;
    private AccountLoginThread thread;
    private String offlineLoginStatus;
    private static final String __OBFID = "CL_00001154";
    
    static {
        field_175373_f = new AtomicInteger(0);
        logger = LogManager.getLogger();
        field_175374_h = new Random();
        splashTexts = new ResourceLocation("texts/splashes.txt");
        minecraftTitleTextures = new ResourceLocation("textures/gui/title/minecraft.png");
        titlePanoramaPaths = new ResourceLocation[] { new ResourceLocation("streamlined/background/panorama_0.png"), new ResourceLocation("streamlined/background/panorama_1.png"), new ResourceLocation("streamlined/background/panorama_2.png"), new ResourceLocation("streamlined/background/panorama_3.png"), new ResourceLocation("streamlined/background/panorama_4.png"), new ResourceLocation("streamlined/background/panorama_5.png") };
        field_96138_a = "Please click " + EnumChatFormatting.UNDERLINE + "here" + EnumChatFormatting.RESET + " for more information.";
    }
    
    public GuiAccountManager(final GuiScreen parent) {
        this.field_175375_v = true;
        this.field_104025_t = new Object();
        this.accountButtons = new ArrayList<AccountListEntry>();
        this.scrollAmount = 0;
        this.setRandomAccount = false;
        this.loginStatus = "";
        this.offlineLoginStatus = "";
        this.parent = parent;
        this.field_146972_A = GuiAccountManager.field_96138_a;
        this.splashText = "missingno";
        BufferedReader var1 = null;
        try {
            final ArrayList var2 = Lists.newArrayList();
            var1 = new BufferedReader(new InputStreamReader(Minecraft.getMinecraft().getResourceManager().getResource(GuiAccountManager.splashTexts).getInputStream(), Charsets.UTF_8));
            String var3;
            while ((var3 = var1.readLine()) != null) {
                var3 = var3.trim();
                if (!var3.isEmpty()) {
                    var2.add(var3);
                }
            }
            if (!var2.isEmpty()) {
                do {
                    this.splashText = var2.get(GuiAccountManager.field_175374_h.nextInt(var2.size()));
                } while (this.splashText.hashCode() == 125780783);
            }
        }
        catch (IOException ex) {}
        finally {
            if (var1 != null) {
                try {
                    var1.close();
                }
                catch (IOException ex2) {}
            }
        }
        if (var1 != null) {
            try {
                var1.close();
            }
            catch (IOException ex3) {}
        }
        this.updateCounter = GuiAccountManager.field_175374_h.nextFloat();
        this.field_92025_p = "";
        if (!GLContext.getCapabilities().OpenGL20 && !OpenGlHelper.areShadersSupported()) {
            this.field_92025_p = I18n.format("title.oldgl1", new Object[0]);
            this.field_146972_A = I18n.format("title.oldgl2", new Object[0]);
            this.field_104024_v = "https://help.mojang.com/customer/portal/articles/325948?ref=game";
        }
    }
    
    @Override
    public void updateScreen() {
        ++this.panoramaTimer;
        this.email.updateCursorCounter();
        this.password.updateCursorCounter();
        if (this.email.getText().isEmpty()) {
            this.buttonList.get(0).enabled = false;
        }
        else {
            this.buttonList.get(0).enabled = true;
        }
        if (!this.email.getText().isEmpty() && this.password.getText().isEmpty()) {
            this.buttonList.get(0).displayString = "Set Offline";
        }
        else {
            this.buttonList.get(0).displayString = "Login";
        }
    }
    
    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (this.email.isFocused()) {
            this.email.textboxKeyTyped(typedChar, keyCode);
        }
        else if (this.password.isFocused()) {
            this.password.textboxKeyTyped(typedChar, keyCode);
        }
    }
    
    @Override
    public void initGui() {
        this.viewportTexture = new DynamicTexture(256, 256);
        this.field_110351_G = this.mc.getTextureManager().getDynamicTextureLocation("background", this.viewportTexture);
        final ScaledResolution sr = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
        this.email = new GuiCustomTextField(69, this.width / 2 - 10, sr.getScaledHeight() / 2 - 25, 162, 15, false, "E-Mail");
        this.password = new GuiCustomTextField(69, this.width / 2 - 10, sr.getScaledHeight() / 2 - 5, 162, 15, true, "Password");
        this.buttonList.add(new GuiCustomButton(1, this.width / 2 - 10, sr.getScaledHeight() / 2 + 20, 162, 20, "Login", true));
        this.email.setFocused(true);
        this.password.setFocused(false);
        this.email.setMaxStringLength(Integer.MAX_VALUE);
        this.password.setMaxStringLength(Integer.MAX_VALUE);
        this.buttonList.add(new GuiButtonIcon(2, sr.getScaledWidth() / 2 - 160 + 4, sr.getScaledHeight() / 2 - 90 + 4, 20, 20, "back.png", "", true));
        this.buttonList.add(new GuiButtonIcon(3, sr.getScaledWidth() / 2 + 160 - 24, sr.getScaledHeight() / 2 - 90 + 4, 20, 20, "microsoft.png", "", true));
        final Object var4 = this.field_104025_t;
        synchronized (this.field_104025_t) {
            this.field_92023_s = this.fontRendererObj.getStringWidth(this.field_92025_p);
            this.field_92024_r = this.fontRendererObj.getStringWidth(this.field_146972_A);
            final int var5 = Math.max(this.field_92023_s, this.field_92024_r);
            this.field_92022_t = (this.width - var5) / 2;
            this.field_92020_v = this.field_92022_t + var5;
            this.field_92019_w = this.field_92021_u + 24;
        }
        // monitorexit(this.field_104025_t)
        this.accountButtons.clear();
        for (final Account account : MoonsenseClient.INSTANCE.getAccountManager().getAccounts()) {
            this.accountButtons.add(new AccountListEntry(account.getEmail(), account.getPassword(), account.getUsername()));
        }
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.id == 1 && !this.email.getText().isEmpty()) {
            if (this.password.getText().isEmpty()) {
                if (this.email.getText().length() < 3) {
                    this.offlineLoginStatus = "&4Username too short.";
                }
                else {
                    char[] charArray;
                    for (int length = (charArray = this.email.getText().toCharArray()).length, i = 0; i < length; ++i) {
                        final char c = charArray[i];
                        if (!Character.isAlphabetic(c) && !Character.isDigit(c) && c != '_') {
                            this.offlineLoginStatus = "&4Username contains invalid characters.";
                            return;
                        }
                    }
                    if (Minecraft.getMinecraft().session.getToken() != null && !Minecraft.getMinecraft().session.getToken().equalsIgnoreCase("0") && MoonsenseClient.INSTANCE.getSocketClient().isRunning()) {
                        MoonsenseClient.INSTANCE.getSocketClient().updateSelf(false);
                    }
                    if (MoonsenseClient.INSTANCE.getSocketClient().isRunning()) {
                        MoonsenseClient.INSTANCE.getSocketClient().updateSelf(false);
                    }
                    final boolean isUserOnline = MoonsenseClient.INSTANCE.getSocketClient().isUserOnline(this.email.getText());
                    if (isUserOnline) {
                        this.offlineLoginStatus = "&4That user is online.";
                        if (MoonsenseClient.INSTANCE.getSocketClient().isRunning()) {
                            MoonsenseClient.INSTANCE.getSocketClient().updateSelf(true);
                        }
                    }
                    else {
                        this.offlineLoginStatus = "&aSet your offline username as: " + this.email.getText();
                        this.mc.session = new Session(this.email.getText(), null, null, "mojang");
                        if (MoonsenseClient.INSTANCE.getSocketClient().isRunning()) {
                            MoonsenseClient.INSTANCE.getSocketClient().updateSelf(true);
                        }
                    }
                }
            }
            else {
                this.offlineLoginStatus = "";
                (this.thread = new AccountLoginThread(this.email.getText(), this.password.getText())).start();
            }
        }
        if (button.id == 2) {
            ((GuiMainMenu)this.parent).panoramaTimer = this.panoramaTimer;
            this.mc.displayGuiScreen(this.parent);
        }
    }
    
    private void drawPanorama(final int p_73970_1_, final int p_73970_2_, final float p_73970_3_) {
        final Tessellator var4 = Tessellator.getInstance();
        final WorldRenderer var5 = var4.getWorldRenderer();
        GlStateManager.matrixMode(5889);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        Project.gluPerspective(120.0f, 1.0f, 0.05f, 10.0f);
        GlStateManager.matrixMode(5888);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.rotate(180.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(90.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.disableCull();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        final byte var6 = 8;
        for (int var7 = 0; var7 < var6 * var6; ++var7) {
            GlStateManager.pushMatrix();
            final float var8 = (var7 % var6 / (float)var6 - 0.5f) / 64.0f;
            final float var9 = (var7 / var6 / (float)var6 - 0.5f) / 64.0f;
            final float var10 = 0.0f;
            GlStateManager.translate(var8, var9, var10);
            GlStateManager.rotate(MathHelper.sin((this.panoramaTimer + p_73970_3_) / 400.0f) * 25.0f + 20.0f, 1.0f, 0.0f, 0.0f);
            GlStateManager.rotate(-(this.panoramaTimer + p_73970_3_) * 0.1f, 0.0f, 1.0f, 0.0f);
            for (int var11 = 0; var11 < 6; ++var11) {
                GlStateManager.pushMatrix();
                if (var11 == 1) {
                    GlStateManager.rotate(90.0f, 0.0f, 1.0f, 0.0f);
                }
                if (var11 == 2) {
                    GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
                }
                if (var11 == 3) {
                    GlStateManager.rotate(-90.0f, 0.0f, 1.0f, 0.0f);
                }
                if (var11 == 4) {
                    GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
                }
                if (var11 == 5) {
                    GlStateManager.rotate(-90.0f, 1.0f, 0.0f, 0.0f);
                }
                this.mc.getTextureManager().bindTexture(GuiAccountManager.titlePanoramaPaths[var11]);
                var5.startDrawingQuads();
                var5.func_178974_a(16777215, 255 / (var7 + 1));
                final float var12 = 0.0f;
                var5.addVertexWithUV(-1.0, -1.0, 1.0, 0.0f + var12, 0.0f + var12);
                var5.addVertexWithUV(1.0, -1.0, 1.0, 1.0f - var12, 0.0f + var12);
                var5.addVertexWithUV(1.0, 1.0, 1.0, 1.0f - var12, 1.0f - var12);
                var5.addVertexWithUV(-1.0, 1.0, 1.0, 0.0f + var12, 1.0f - var12);
                var4.draw();
                GlStateManager.popMatrix();
            }
            GlStateManager.popMatrix();
            GlStateManager.colorMask(true, true, true, false);
        }
        var5.setTranslation(0.0, 0.0, 0.0);
        GlStateManager.colorMask(true, true, true, true);
        GlStateManager.matrixMode(5889);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(5888);
        GlStateManager.popMatrix();
        GlStateManager.depthMask(true);
        GlStateManager.enableCull();
        GlStateManager.enableDepth();
    }
    
    private void rotateAndBlurSkybox(final float p_73968_1_) {
        this.mc.getTextureManager().bindTexture(this.field_110351_G);
        GL11.glTexParameteri(3553, 10241, 9729);
        GL11.glTexParameteri(3553, 10240, 9729);
        GL11.glCopyTexSubImage2D(3553, 0, 0, 0, 0, 0, 256, 256);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.colorMask(true, true, true, false);
        final Tessellator var2 = Tessellator.getInstance();
        final WorldRenderer var3 = var2.getWorldRenderer();
        var3.startDrawingQuads();
        GlStateManager.disableAlpha();
        final byte var4 = 3;
        for (int var5 = 0; var5 < var4; ++var5) {
            var3.func_178960_a(1.0f, 1.0f, 1.0f, 1.0f / (var5 + 1));
            final int var6 = this.width;
            final int var7 = this.height;
            final float var8 = (var5 - var4 / 2) / 256.0f;
            var3.addVertexWithUV(var6, var7, this.zLevel, 0.0f + var8, 1.0);
            var3.addVertexWithUV(var6, 0.0, this.zLevel, 1.0f + var8, 1.0);
            var3.addVertexWithUV(0.0, 0.0, this.zLevel, 1.0f + var8, 0.0);
            var3.addVertexWithUV(0.0, var7, this.zLevel, 0.0f + var8, 0.0);
        }
        var2.draw();
        GlStateManager.enableAlpha();
        GlStateManager.colorMask(true, true, true, true);
    }
    
    private void renderSkybox(final int p_73971_1_, final int p_73971_2_, final float p_73971_3_) {
        this.mc.getFramebuffer().unbindFramebuffer();
        GlStateManager.viewport(0, 0, 256, 256);
        this.drawPanorama(p_73971_1_, p_73971_2_, p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.mc.getFramebuffer().bindFramebuffer(true);
        GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
        final Tessellator var4 = Tessellator.getInstance();
        final WorldRenderer var5 = var4.getWorldRenderer();
        var5.startDrawingQuads();
        final float var6 = (this.width > this.height) ? (120.0f / this.width) : (120.0f / this.height);
        final float var7 = this.height * var6 / 256.0f;
        final float var8 = this.width * var6 / 256.0f;
        var5.func_178960_a(1.0f, 1.0f, 1.0f, 1.0f);
        final int var9 = this.width;
        final int var10 = this.height;
        var5.addVertexWithUV(0.0, var10, this.zLevel, 0.5f - var7, 0.5f + var8);
        var5.addVertexWithUV(var9, var10, this.zLevel, 0.5f - var7, 0.5f - var8);
        var5.addVertexWithUV(var9, 0.0, this.zLevel, 0.5f + var7, 0.5f - var8);
        var5.addVertexWithUV(0.0, 0.0, this.zLevel, 0.5f + var7, 0.5f + var8);
        var4.draw();
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        GlStateManager.disableAlpha();
        this.renderSkybox(mouseX, mouseY, partialTicks);
        GlStateManager.enableAlpha();
        final Tessellator var4 = Tessellator.getInstance();
        final WorldRenderer var5 = var4.getWorldRenderer();
        final short var6 = 274;
        final int var7 = this.width / 2 - var6 / 2;
        final byte var8 = 30;
        this.drawGradientRect(0, 0, this.width, this.height, 0, Integer.MIN_VALUE);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        var5.func_178991_c(-1);
        final ScaledResolution sr = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
        GuiUtils.drawRoundedRect((float)(sr.getScaledWidth() / 2 - 160), (float)(sr.getScaledHeight() / 2 - 90), (float)(sr.getScaledWidth() / 2 + 160), (float)(sr.getScaledHeight() / 2 + 90), 7.0f, new Color(42, 50, 55, 100).getRGB());
        GuiUtils.drawRoundedOutline(sr.getScaledWidth() / 2 - 160, sr.getScaledHeight() / 2 - 90, sr.getScaledWidth() / 2 + 160, sr.getScaledHeight() / 2 + 90, 7.0f, 2.0f, new Color(105, 116, 122, 100).getRGB());
        MoonsenseClient.titleRendererLarge.drawCenteredStringWithShadow("Accounts", (float)(sr.getScaledWidth() / 2), (float)(sr.getScaledHeight() / 2 - 84), -1);
        this.drawHorizontalLine(sr.getScaledWidth() / 2 - 160, sr.getScaledWidth() / 2 + 160, sr.getScaledHeight() / 2 - 63, new Color(0, 0, 0, 50).getRGB());
        this.drawVerticalLine(sr.getScaledWidth() / 2 - 20, sr.getScaledHeight() / 2 - 63, sr.getScaledHeight() / 2 + 90, new Color(0, 0, 0, 50).getRGB());
        final ArrayList<AccountListEntry> toRemove = new ArrayList<AccountListEntry>();
        for (final AccountListEntry entry : this.accountButtons) {
            if (entry.shouldQueueForRemove) {
                toRemove.add(entry);
                MoonsenseClient.INSTANCE.getAccountManager().remove(entry.getUsername());
                if (MoonsenseClient.INSTANCE.getAccountManager().getAccounts().isEmpty()) {
                    MoonsenseClient.INSTANCE.getAccountManager().setLastAlt(null);
                    this.loginStatus = "";
                    this.setRandomAccount = true;
                    if (MoonsenseClient.INSTANCE.getSocketClient().isRunning()) {
                        MoonsenseClient.INSTANCE.getSocketClient().updateSelf(false);
                    }
                }
                MoonsenseClient.INSTANCE.getAccountManager().save();
            }
        }
        this.accountButtons.removeAll(toRemove);
        if (this.setRandomAccount) {
            this.mc.session = new Session("Player" + Minecraft.getSystemTime() % 1000L, null, null, "mojang");
            this.setRandomAccount = false;
        }
        for (final AccountListEntry entry : this.accountButtons) {
            entry.updateEntry();
        }
        this.scrollAmount += Mouse.getDWheel() / 8;
        final int maxScroll = -(this.accountButtons.size() * (AccountListEntry.HEIGHT + 5)) + 150;
        if (this.scrollAmount < maxScroll) {
            this.scrollAmount = maxScroll;
        }
        if (this.scrollAmount > 0) {
            this.scrollAmount = 0;
        }
        int rows = 0;
        final boolean hasFoundLoggedIn = false;
        GL11.glEnable(3089);
        GuiUtils.scissorHelper(0, sr.getScaledHeight() / 2 - 63, this.width, sr.getScaledHeight() / 2 + 90);
        for (final AccountListEntry entry2 : this.accountButtons) {
            if (entry2.hasStatus()) {
                this.loginStatus = entry2.getStatus();
            }
            if (entry2.getUsername().equalsIgnoreCase(this.mc.session.getUsername()) && this.mc.session.getToken() != null) {
                entry2.setLoggedIn(true);
            }
            else {
                entry2.setLoggedIn(false);
            }
            entry2.drawEntry(sr.getScaledWidth() / 2 - 150, sr.getScaledHeight() / 2 - 60 + (rows * (AccountListEntry.HEIGHT + 5) + this.scrollAmount), mouseX, mouseY);
            ++rows;
        }
        GL11.glDisable(3089);
        MoonsenseClient.titleRenderer.drawCenteredStringWithShadow("Add a Minecraft Account", (float)(sr.getScaledWidth() / 2 + 70), (float)(sr.getScaledHeight() / 2 - 50), -1);
        this.drawHorizontalLine(sr.getScaledWidth() / 2 - 10, sr.getScaledWidth() / 2 + 150, sr.getScaledHeight() / 2 - 35, new Color(0, 0, 0, 50).getRGB());
        String s;
        int color;
        if (!this.offlineLoginStatus.isEmpty()) {
            s = this.offlineLoginStatus;
            if (s.contains("6")) {
                color = Color.yellow.getRGB();
            }
            else if (s.contains("4")) {
                color = Color.red.getRGB();
            }
            else if (s.contains("a")) {
                color = Color.green.getRGB();
            }
            else if (s.contains("e")) {
                color = Color.yellow.getRGB();
            }
            else {
                color = -1;
            }
        }
        else if (this.thread != null) {
            s = this.thread.getStatus();
            if (s.contains("6")) {
                color = Color.yellow.getRGB();
            }
            else if (s.contains("4")) {
                color = Color.red.getRGB();
            }
            else if (s.contains("a")) {
                color = Color.green.getRGB();
            }
            else if (s.contains("e")) {
                color = Color.yellow.getRGB();
            }
            else {
                color = -1;
            }
        }
        else {
            s = "!!Waiting for login...";
            color = -1;
        }
        this.drawHorizontalLine(sr.getScaledWidth() / 2 - 10, sr.getScaledWidth() / 2 + 150, sr.getScaledHeight() / 2 + 50, new Color(0, 0, 0, 50).getRGB());
        if (this.mc.session.getToken() == null || this.mc.session.getToken().isEmpty()) {
            MoonsenseClient.textRenderer.drawCenteredString("You are offline.", (float)(sr.getScaledWidth() / 2 + 70), (float)(sr.getScaledHeight() / 2 + 55), Color.yellow.getRGB());
            MoonsenseClient.textRenderer.drawCenteredString("Your username is: " + this.mc.session.getUsername(), (float)(sr.getScaledWidth() / 2 + 70), (float)(sr.getScaledHeight() / 2 + 65), Color.yellow.getRGB());
        }
        MoonsenseClient.textRenderer.drawCenteredString(s.substring(2), (float)(sr.getScaledWidth() / 2 + 70), (float)(sr.getScaledHeight() / 2 + 75), color);
        this.email.drawTextBox();
        this.password.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        final ScaledResolution sr = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
        final Object var4 = this.field_104025_t;
        synchronized (this.field_104025_t) {
            if (this.field_92025_p.length() > 0 && mouseX >= this.field_92022_t && mouseX <= this.field_92020_v && mouseY >= this.field_92021_u && mouseY <= this.field_92019_w) {
                final GuiConfirmOpenLink var5 = new GuiConfirmOpenLink(this, this.field_104024_v, 13, true);
                var5.disableSecurityWarning();
                this.mc.displayGuiScreen(var5);
            }
        }
        // monitorexit(this.field_104025_t)
        int rows = 0;
        for (final AccountListEntry entry : this.accountButtons) {
            entry.mouseClicked(sr.getScaledWidth() / 2 - 148, sr.getScaledHeight() / 2 - 60 + rows * (AccountListEntry.HEIGHT + 5), mouseX, mouseY);
            if (entry.hasStatus()) {
                this.loginStatus = entry.getStatus();
            }
            ++rows;
        }
        this.email.mouseClicked(mouseX, mouseY, mouseButton);
        this.password.mouseClicked(mouseX, mouseY, mouseButton);
    }
}
