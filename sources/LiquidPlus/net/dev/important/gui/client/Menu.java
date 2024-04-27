/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.gui.GuiConfirmOpenLink
 *  net.minecraft.client.gui.GuiMultiplayer
 *  net.minecraft.client.gui.GuiOptions
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.GuiSelectWorld
 *  net.minecraft.client.gui.GuiYesNo
 *  net.minecraft.client.gui.GuiYesNoCallback
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.OpenGlHelper
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.WorldRenderer
 *  net.minecraft.client.renderer.texture.DynamicTexture
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  net.minecraft.client.resources.I18n
 *  net.minecraft.client.settings.GameSettings$Options
 *  net.minecraft.realms.RealmsBridge
 *  net.minecraft.util.MathHelper
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.world.demo.DemoWorldServer
 *  net.minecraft.world.storage.ISaveFormat
 *  net.minecraft.world.storage.WorldInfo
 *  org.apache.commons.io.Charsets
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.opengl.GLContext
 *  org.lwjgl.util.glu.Project
 */
package net.dev.important.gui.client;

import com.google.common.collect.Lists;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import net.dev.important.gui.client.GuiBackground;
import net.dev.important.gui.client.altmanager.GuiAltManager;
import net.dev.important.utils.MainMenuButton;
import net.dev.important.utils.misc.RandomUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiConfirmOpenLink;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.demo.DemoWorldServer;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldInfo;
import org.apache.commons.io.Charsets;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.Project;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000L\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0007\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0012\n\u0002\u0010\f\n\u0002\b\u000f\u0018\u0000 ?2\u00020\u0001:\u0001?B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u001eH\u0014J\u0018\u0010\u001f\u001a\u00020\u001c2\u0006\u0010 \u001a\u00020\u00042\u0006\u0010!\u001a\u00020\u0004H\u0002J\u0010\u0010\"\u001a\u00020\u001c2\u0006\u0010#\u001a\u00020\u0004H\u0002J\b\u0010$\u001a\u00020\bH\u0016J \u0010%\u001a\u00020\u001c2\u0006\u0010&\u001a\u00020\u00042\u0006\u0010'\u001a\u00020\u00042\u0006\u0010(\u001a\u00020\u0018H\u0002J \u0010)\u001a\u00020\u001c2\u0006\u0010*\u001a\u00020\u00042\u0006\u0010+\u001a\u00020\u00042\u0006\u0010,\u001a\u00020\u0018H\u0016J\b\u0010-\u001a\u00020\bH\u0002J\b\u0010.\u001a\u00020\u001cH\u0016J\u0018\u0010/\u001a\u00020\u001c2\u0006\u00100\u001a\u0002012\u0006\u00102\u001a\u00020\u0004H\u0014J \u00103\u001a\u00020\u001c2\u0006\u00104\u001a\u00020\u00042\u0006\u00105\u001a\u00020\u00042\u0006\u00106\u001a\u00020\u0004H\u0014J\b\u00107\u001a\u00020\u001cH\u0016J \u00108\u001a\u00020\u001c2\u0006\u00109\u001a\u00020\u00042\u0006\u0010:\u001a\u00020\u00042\u0006\u0010;\u001a\u00020\u0018H\u0002J\u0010\u0010<\u001a\u00020\u001c2\u0006\u0010=\u001a\u00020\u0018H\u0002J\b\u0010>\u001a\u00020\u001cH\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\t\u001a\u0004\u0018\u00010\u0001X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0011\u001a\u0004\u0018\u00010\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0012\u001a\u0004\u0018\u00010\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0016X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0017\u001a\u00020\u0018\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u001a\u00a8\u0006@"}, d2={"Lnet/dev/important/gui/client/Menu;", "Lnet/minecraft/client/gui/GuiScreen;", "()V", "astolfo", "", "backgroundTexture", "Lnet/minecraft/util/ResourceLocation;", "field_183502_L", "", "field_183503_M", "field_92019_w", "field_92020_v", "field_92021_u", "field_92022_t", "field_92024_r", "openGLWarning1", "", "openGLWarning2", "openGLWarningLink", "panoramaTimer", "splashText", "threadLock", "", "updateCounter", "", "getUpdateCounter", "()F", "actionPerformed", "", "p_actionPerformed_1_", "Lnet/minecraft/client/gui/GuiButton;", "addDemoButtons", "p_addDemoButtons_1_", "p_addDemoButtons_2_", "addSingleplayerMultiplayerButtons", "p_addSingleplayerMultiplayerButtons_1_", "doesGuiPauseGame", "drawPanorama", "p_drawPanorama_1_", "p_drawPanorama_2_", "p_drawPanorama_3_", "drawScreen", "mouseX", "mouseY", "partialTicks", "func_183501_a", "initGui", "keyTyped", "p_keyTyped_1_", "", "p_keyTyped_2_", "mouseClicked", "p_mouseClicked_1_", "p_mouseClicked_2_", "p_mouseClicked_3_", "onGuiClosed", "renderSkybox", "p_renderSkybox_1_", "p_renderSkybox_2_", "p_renderSkybox_3_", "rotateAndBlurSkybox", "p_rotateAndBlurSkybox_1_", "updateScreen", "Companion", "LiquidBounce"})
public final class Menu
extends GuiScreen {
    @NotNull
    public static final Companion Companion = new Companion(null);
    private final int astolfo;
    private final float updateCounter;
    @NotNull
    private String splashText;
    private int panoramaTimer;
    @NotNull
    private final Object threadLock;
    @NotNull
    private String openGLWarning1;
    @Nullable
    private String openGLWarning2;
    @Nullable
    private String openGLWarningLink;
    private int field_92024_r;
    private int field_92022_t;
    private int field_92021_u;
    private int field_92020_v;
    private int field_92019_w;
    @Nullable
    private ResourceLocation backgroundTexture;
    private boolean field_183502_L;
    @Nullable
    private GuiScreen field_183503_M;
    @NotNull
    private static final Random RANDOM = new Random();
    @NotNull
    private static final ResourceLocation splashTexts = new ResourceLocation("Insane/splashes.txt");
    @NotNull
    private static final ResourceLocation[] titlePanoramaPaths;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Loose catch block
     */
    public Menu() {
        block19: {
            BufferedReader bufferedreader;
            block18: {
                this.splashText = "missingno";
                this.threadLock = new Object();
                bufferedreader = null;
                ArrayList arrayList = Lists.newArrayList();
                Intrinsics.checkNotNullExpressionValue(arrayList, "newArrayList()");
                List list = arrayList;
                bufferedreader = new BufferedReader(new InputStreamReader(Minecraft.func_71410_x().func_110442_L().func_110536_a(splashTexts).func_110527_b(), Charsets.UTF_8));
                String s = null;
                while (true) {
                    String string;
                    String it = string = bufferedreader.readLine();
                    boolean bl = false;
                    Intrinsics.checkNotNullExpressionValue(it, "it");
                    s = it;
                    if (string == null) break;
                    String $this$trim$iv = s;
                    boolean $i$f$trim = false;
                    CharSequence $this$trim$iv$iv = $this$trim$iv;
                    boolean $i$f$trim2 = false;
                    int startIndex$iv$iv = 0;
                    int endIndex$iv$iv = $this$trim$iv$iv.length() - 1;
                    boolean startFound$iv$iv = false;
                    while (startIndex$iv$iv <= endIndex$iv$iv) {
                        boolean match$iv$iv;
                        int index$iv$iv = !startFound$iv$iv ? startIndex$iv$iv : endIndex$iv$iv;
                        char it2 = $this$trim$iv$iv.charAt(index$iv$iv);
                        boolean bl2 = false;
                        boolean bl3 = match$iv$iv = Intrinsics.compare(it2, 32) <= 0;
                        if (!startFound$iv$iv) {
                            if (!match$iv$iv) {
                                startFound$iv$iv = true;
                                continue;
                            }
                            ++startIndex$iv$iv;
                            continue;
                        }
                        if (!match$iv$iv) break;
                        --endIndex$iv$iv;
                    }
                    if (((CharSequence)(s = ((Object)$this$trim$iv$iv.subSequence(startIndex$iv$iv, endIndex$iv$iv + 1)).toString())).length() == 0) continue;
                    list.add(s);
                }
                if (list.isEmpty()) break block18;
                do {
                    this.splashText = (String)list.get(RANDOM.nextInt(list.size()));
                } while (this.splashText.hashCode() == 125780783);
            }
            try {
                bufferedreader.close();
            }
            catch (IOException iOException) {}
            break block19;
            catch (IOException iOException) {
                if (bufferedreader != null) {
                    try {
                        bufferedreader.close();
                    }
                    catch (IOException iOException2) {}
                }
                catch (Throwable throwable) {
                    if (bufferedreader != null) {
                        try {
                            bufferedreader.close();
                        }
                        catch (IOException iOException3) {
                            // empty catch block
                        }
                    }
                    throw throwable;
                }
            }
        }
        this.astolfo = RandomUtils.nextInt(0, 4);
        this.updateCounter = RANDOM.nextFloat();
        this.openGLWarning1 = "";
        if (!GLContext.getCapabilities().OpenGL20 && !OpenGlHelper.func_153193_b()) {
            String string = I18n.func_135052_a((String)"title.oldgl1", (Object[])new Object[0]);
            Intrinsics.checkNotNullExpressionValue(string, "format(\"title.oldgl1\")");
            this.openGLWarning1 = string;
            this.openGLWarning2 = I18n.func_135052_a((String)"title.oldgl2", (Object[])new Object[0]);
            this.openGLWarningLink = "https://help.mojang.com/customer/portal/articles/325948?ref=game";
        }
    }

    public final float getUpdateCounter() {
        return this.updateCounter;
    }

    private final boolean func_183501_a() {
        return this.field_146297_k.field_71474_y.func_74308_b(GameSettings.Options.REALMS_NOTIFICATIONS) && this.field_183503_M != null;
    }

    public void func_73876_c() {
        ++this.panoramaTimer;
        if (this.func_183501_a()) {
            GuiScreen guiScreen = this.field_183503_M;
            Intrinsics.checkNotNull(guiScreen);
            guiScreen.func_73876_c();
        }
    }

    public boolean func_73868_f() {
        return false;
    }

    protected void func_73869_a(char p_keyTyped_1_, int p_keyTyped_2_) throws IOException {
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void func_73866_w_() {
        DynamicTexture viewportTexture = new DynamicTexture(256, 256);
        this.backgroundTexture = this.field_146297_k.func_110434_K().func_110578_a("background", viewportTexture);
        int j = this.field_146295_m / 4 + 48;
        if (this.field_146297_k.func_71355_q()) {
            this.addDemoButtons(j, 24);
        } else {
            this.addSingleplayerMultiplayerButtons(j);
        }
        int defaultHeight = this.field_146295_m / 2 - 8;
        this.field_146292_n.add(new MainMenuButton(0, this.field_146294_l / 2 - 72, defaultHeight + 96, 140, 20, I18n.func_135052_a((String)"menu.options", (Object[])new Object[0])));
        Object object = this.threadLock;
        synchronized (object) {
            boolean bl = false;
            int field_92023_s = this.field_146289_q.func_78256_a(this.openGLWarning1);
            this.field_92024_r = this.field_146289_q.func_78256_a(this.openGLWarning2);
            int k = RangesKt.coerceAtLeast(field_92023_s, this.field_92024_r);
            this.field_92022_t = (this.field_146294_l - k) / 2;
            this.field_92021_u = ((GuiButton)this.field_146292_n.get((int)0)).field_146129_i - 24;
            this.field_92020_v = this.field_92022_t + k;
            this.field_92019_w = this.field_92021_u + 24;
            Unit unit = Unit.INSTANCE;
        }
        this.field_146297_k.func_181537_a(false);
        if (this.field_146297_k.field_71474_y.func_74308_b(GameSettings.Options.REALMS_NOTIFICATIONS) && !this.field_183502_L) {
            RealmsBridge realmsbridge = new RealmsBridge();
            this.field_183503_M = (GuiScreen)realmsbridge.getNotificationScreen((GuiScreen)this);
            this.field_183502_L = true;
        }
        if (this.func_183501_a()) {
            GuiScreen guiScreen = this.field_183503_M;
            Intrinsics.checkNotNull(guiScreen);
            guiScreen.func_183500_a(this.field_146294_l, this.field_146295_m);
            GuiScreen guiScreen2 = this.field_183503_M;
            Intrinsics.checkNotNull(guiScreen2);
            guiScreen2.func_73866_w_();
        }
    }

    private final void addSingleplayerMultiplayerButtons(int p_addSingleplayerMultiplayerButtons_1_) {
        int defaultHeight = this.field_146295_m / 2 - 8;
        this.field_146292_n.add(new MainMenuButton(1, this.field_146294_l / 2 - 72, defaultHeight, 140, 20, I18n.func_135052_a((String)"menu.singleplayer", (Object[])new Object[0])));
        this.field_146292_n.add(new MainMenuButton(2, this.field_146294_l / 2 - 72, defaultHeight + 24, 140, 20, I18n.func_135052_a((String)"menu.multiplayer", (Object[])new Object[0])));
        this.field_146292_n.add(new MainMenuButton(3, this.field_146294_l / 2 - 72, defaultHeight + 48, 140, 20, I18n.func_135052_a((String)"Account Manager", (Object[])new Object[0])));
        this.field_146292_n.add(new MainMenuButton(4, this.field_146294_l / 2 - 72, defaultHeight + 72, 140, 20, I18n.func_135052_a((String)"Background", (Object[])new Object[0])));
    }

    /*
     * WARNING - void declaration
     */
    private final void addDemoButtons(int p_addDemoButtons_1_, int p_addDemoButtons_2_) {
        void it;
        GuiButton guiButton;
        this.field_146292_n.add(new GuiButton(11, this.field_146294_l / 2 - 100, p_addDemoButtons_1_, I18n.func_135052_a((String)"menu.playdemo", (Object[])new Object[0])));
        Object buttonResetDemo = null;
        GuiButton guiButton2 = guiButton = new GuiButton(12, this.field_146294_l / 2 - 100, p_addDemoButtons_1_ + p_addDemoButtons_2_, I18n.func_135052_a((String)"menu.resetdemo", (Object[])new Object[0]));
        List list = this.field_146292_n;
        boolean bl = false;
        buttonResetDemo = it;
        list.add(guiButton);
        ISaveFormat isaveformat = this.field_146297_k.func_71359_d();
        WorldInfo worldinfo = isaveformat.func_75803_c("Demo_World");
        if (worldinfo == null) {
            buttonResetDemo.field_146124_l = false;
        }
    }

    protected void func_146284_a(@NotNull GuiButton p_actionPerformed_1_) throws IOException {
        ISaveFormat isaveformat;
        WorldInfo worldinfo;
        Intrinsics.checkNotNullParameter(p_actionPerformed_1_, "p_actionPerformed_1_");
        if (p_actionPerformed_1_.field_146127_k == 1) {
            this.field_146297_k.func_147108_a((GuiScreen)new GuiSelectWorld((GuiScreen)this));
        }
        if (p_actionPerformed_1_.field_146127_k == 2) {
            this.field_146297_k.func_147108_a((GuiScreen)new GuiMultiplayer((GuiScreen)this));
        }
        if (p_actionPerformed_1_.field_146127_k == 3) {
            this.field_146297_k.func_147108_a((GuiScreen)new GuiAltManager(this));
        }
        if (p_actionPerformed_1_.field_146127_k == 4) {
            this.field_146297_k.func_147108_a((GuiScreen)new GuiBackground(this));
        }
        if (p_actionPerformed_1_.field_146127_k == 0) {
            this.field_146297_k.func_147108_a((GuiScreen)new GuiOptions((GuiScreen)this, this.field_146297_k.field_71474_y));
        }
        if (p_actionPerformed_1_.field_146127_k == 666) {
            this.field_146297_k.func_71400_g();
        }
        if (p_actionPerformed_1_.field_146127_k == 11) {
            this.field_146297_k.func_71371_a("Demo_World", "Demo_World", DemoWorldServer.field_73071_a);
        }
        if (p_actionPerformed_1_.field_146127_k == 12 && (worldinfo = (isaveformat = this.field_146297_k.func_71359_d()).func_75803_c("Demo_World")) != null) {
            GuiYesNo guiyesno = GuiSelectWorld.func_152129_a((GuiYesNoCallback)((GuiYesNoCallback)this), (String)worldinfo.func_76065_j(), (int)12);
            this.field_146297_k.func_147108_a((GuiScreen)guiyesno);
        }
    }

    public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
        GlStateManager.func_179118_c();
        this.renderSkybox(mouseX, mouseY, partialTicks);
        GlStateManager.func_179141_d();
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.func_179094_E();
        GlStateManager.func_179109_b((float)(this.field_146294_l / 2 + 90), (float)70.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)-20.0f, (float)0.0f, (float)0.0f, (float)1.0f);
        float f = 1.8f - MathHelper.func_76135_e((float)(MathHelper.func_76126_a((float)((float)(Minecraft.func_71386_F() % 1000L) / 1000.0f * (float)Math.PI * 2.0f)) * 0.1f));
        f = f * 100.0f / (float)(this.field_146289_q.func_78256_a(this.splashText) + 32);
        GlStateManager.func_179152_a((float)f, (float)f, (float)f);
        GlStateManager.func_179121_F();
        int[] nArray = new int[]{230, 250, 400};
        nArray = new int[]{312, 353, 422, 305, 242};
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        super.func_73863_a(mouseX, mouseY, partialTicks);
        if (this.func_183501_a()) {
            GuiScreen guiScreen = this.field_183503_M;
            Intrinsics.checkNotNull(guiScreen);
            guiScreen.func_73863_a(mouseX, mouseY, partialTicks);
        }
    }

    private final void drawPanorama(int p_drawPanorama_1_, int p_drawPanorama_2_, float p_drawPanorama_3_) {
        Tessellator tessellator = Tessellator.func_178181_a();
        WorldRenderer worldrenderer = tessellator.func_178180_c();
        GlStateManager.func_179128_n((int)5889);
        GlStateManager.func_179094_E();
        GlStateManager.func_179096_D();
        Project.gluPerspective((float)120.0f, (float)1.0f, (float)0.05f, (float)10.0f);
        GlStateManager.func_179128_n((int)5888);
        GlStateManager.func_179094_E();
        GlStateManager.func_179096_D();
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.func_179114_b((float)180.0f, (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)90.0f, (float)0.0f, (float)0.0f, (float)1.0f);
        GlStateManager.func_179147_l();
        GlStateManager.func_179118_c();
        GlStateManager.func_179129_p();
        GlStateManager.func_179132_a((boolean)false);
        GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
        int i = 8;
        int n = 0;
        int n2 = i * i;
        while (n < n2) {
            int j = n++;
            GlStateManager.func_179094_E();
            float f = ((float)(j % i) / (float)i - 0.5f) / 64.0f;
            float f1 = ((float)(j / i) / (float)i - 0.5f) / 64.0f;
            float f2 = 0.0f;
            GlStateManager.func_179109_b((float)f, (float)f1, (float)f2);
            GlStateManager.func_179114_b((float)(MathHelper.func_76126_a((float)(((float)this.panoramaTimer + p_drawPanorama_3_) / 400.0f)) * 25.0f + 20.0f), (float)1.0f, (float)0.0f, (float)0.0f);
            GlStateManager.func_179114_b((float)(-((float)this.panoramaTimer + p_drawPanorama_3_) * 0.1f), (float)0.0f, (float)1.0f, (float)0.0f);
            int n3 = 0;
            while (n3 < 6) {
                int k = n3++;
                GlStateManager.func_179094_E();
                if (k == 1) {
                    GlStateManager.func_179114_b((float)90.0f, (float)0.0f, (float)1.0f, (float)0.0f);
                }
                if (k == 2) {
                    GlStateManager.func_179114_b((float)180.0f, (float)0.0f, (float)1.0f, (float)0.0f);
                }
                if (k == 3) {
                    GlStateManager.func_179114_b((float)-90.0f, (float)0.0f, (float)1.0f, (float)0.0f);
                }
                if (k == 4) {
                    GlStateManager.func_179114_b((float)90.0f, (float)1.0f, (float)0.0f, (float)0.0f);
                }
                if (k == 5) {
                    GlStateManager.func_179114_b((float)-90.0f, (float)1.0f, (float)0.0f, (float)0.0f);
                }
                this.field_146297_k.func_110434_K().func_110577_a(titlePanoramaPaths[k]);
                worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181709_i);
                int l = 255 / (j + 1);
                float f3 = 0.0f;
                worldrenderer.func_181662_b(-1.0, -1.0, 1.0).func_181673_a(0.0, 0.0).func_181669_b(255, 255, 255, l).func_181675_d();
                worldrenderer.func_181662_b(1.0, -1.0, 1.0).func_181673_a(1.0, 0.0).func_181669_b(255, 255, 255, l).func_181675_d();
                worldrenderer.func_181662_b(1.0, 1.0, 1.0).func_181673_a(1.0, 1.0).func_181669_b(255, 255, 255, l).func_181675_d();
                worldrenderer.func_181662_b(-1.0, 1.0, 1.0).func_181673_a(0.0, 1.0).func_181669_b(255, 255, 255, l).func_181675_d();
                tessellator.func_78381_a();
                GlStateManager.func_179121_F();
            }
            GlStateManager.func_179121_F();
            GlStateManager.func_179135_a((boolean)true, (boolean)true, (boolean)true, (boolean)false);
        }
        worldrenderer.func_178969_c(0.0, 0.0, 0.0);
        GlStateManager.func_179135_a((boolean)true, (boolean)true, (boolean)true, (boolean)true);
        GlStateManager.func_179128_n((int)5889);
        GlStateManager.func_179121_F();
        GlStateManager.func_179128_n((int)5888);
        GlStateManager.func_179121_F();
        GlStateManager.func_179132_a((boolean)true);
        GlStateManager.func_179089_o();
        GlStateManager.func_179126_j();
    }

    private final void rotateAndBlurSkybox(float p_rotateAndBlurSkybox_1_) {
        this.field_146297_k.func_110434_K().func_110577_a(this.backgroundTexture);
        GL11.glTexParameteri((int)3553, (int)10241, (int)9729);
        GL11.glTexParameteri((int)3553, (int)10240, (int)9729);
        GL11.glCopyTexSubImage2D((int)3553, (int)0, (int)0, (int)0, (int)0, (int)0, (int)256, (int)256);
        GlStateManager.func_179147_l();
        GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
        GlStateManager.func_179135_a((boolean)true, (boolean)true, (boolean)true, (boolean)false);
        Tessellator tessellator = Tessellator.func_178181_a();
        WorldRenderer worldrenderer = tessellator.func_178180_c();
        worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181709_i);
        GlStateManager.func_179118_c();
        int i = 3;
        int n = 0;
        while (n < i) {
            int j = n++;
            float f = 1.0f / (float)(j + 1);
            int k = this.field_146294_l;
            int l = this.field_146295_m;
            float f1 = (float)(j - i / 2) / 256.0f;
            worldrenderer.func_181662_b((double)k, (double)l, (double)this.field_73735_i).func_181673_a((double)(0.0f + f1), 1.0).func_181666_a(1.0f, 1.0f, 1.0f, f).func_181675_d();
            worldrenderer.func_181662_b((double)k, 0.0, (double)this.field_73735_i).func_181673_a((double)(1.0f + f1), 1.0).func_181666_a(1.0f, 1.0f, 1.0f, f).func_181675_d();
            worldrenderer.func_181662_b(0.0, 0.0, (double)this.field_73735_i).func_181673_a((double)(1.0f + f1), 0.0).func_181666_a(1.0f, 1.0f, 1.0f, f).func_181675_d();
            worldrenderer.func_181662_b(0.0, (double)l, (double)this.field_73735_i).func_181673_a((double)(0.0f + f1), 0.0).func_181666_a(1.0f, 1.0f, 1.0f, f).func_181675_d();
        }
        tessellator.func_78381_a();
        GlStateManager.func_179141_d();
        GlStateManager.func_179135_a((boolean)true, (boolean)true, (boolean)true, (boolean)true);
    }

    private final void renderSkybox(int p_renderSkybox_1_, int p_renderSkybox_2_, float p_renderSkybox_3_) {
        this.field_146297_k.func_147110_a().func_147609_e();
        GlStateManager.func_179083_b((int)0, (int)0, (int)256, (int)256);
        this.drawPanorama(p_renderSkybox_1_, p_renderSkybox_2_, p_renderSkybox_3_);
        this.rotateAndBlurSkybox(p_renderSkybox_3_);
        this.rotateAndBlurSkybox(p_renderSkybox_3_);
        this.rotateAndBlurSkybox(p_renderSkybox_3_);
        this.rotateAndBlurSkybox(p_renderSkybox_3_);
        this.rotateAndBlurSkybox(p_renderSkybox_3_);
        this.rotateAndBlurSkybox(p_renderSkybox_3_);
        this.field_146297_k.func_147110_a().func_147610_a(true);
        GlStateManager.func_179083_b((int)0, (int)0, (int)this.field_146297_k.field_71443_c, (int)this.field_146297_k.field_71440_d);
        float f = this.field_146294_l > this.field_146295_m ? 120.0f / (float)this.field_146294_l : 120.0f / (float)this.field_146295_m;
        float f1 = (float)this.field_146295_m * f / 256.0f;
        float f2 = (float)this.field_146294_l * f / 256.0f;
        int i = this.field_146294_l;
        int j = this.field_146295_m;
        Tessellator tessellator = Tessellator.func_178181_a();
        WorldRenderer worldrenderer = tessellator.func_178180_c();
        worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181709_i);
        worldrenderer.func_181662_b(0.0, (double)j, (double)this.field_73735_i).func_181673_a((double)(0.5f - f1), (double)(0.5f + f2)).func_181666_a(1.0f, 1.0f, 1.0f, 1.0f).func_181675_d();
        worldrenderer.func_181662_b((double)i, (double)j, (double)this.field_73735_i).func_181673_a((double)(0.5f - f1), (double)(0.5f - f2)).func_181666_a(1.0f, 1.0f, 1.0f, 1.0f).func_181675_d();
        worldrenderer.func_181662_b((double)i, 0.0, (double)this.field_73735_i).func_181673_a((double)(0.5f + f1), (double)(0.5f - f2)).func_181666_a(1.0f, 1.0f, 1.0f, 1.0f).func_181675_d();
        worldrenderer.func_181662_b(0.0, 0.0, (double)this.field_73735_i).func_181673_a((double)(0.5f + f1), (double)(0.5f + f2)).func_181666_a(1.0f, 1.0f, 1.0f, 1.0f).func_181675_d();
        tessellator.func_78381_a();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected void func_73864_a(int p_mouseClicked_1_, int p_mouseClicked_2_, int p_mouseClicked_3_) throws IOException {
        super.func_73864_a(p_mouseClicked_1_, p_mouseClicked_2_, p_mouseClicked_3_);
        Object object = this.threadLock;
        synchronized (object) {
            boolean bl = false;
            if (this.openGLWarning1.length() > 0 && p_mouseClicked_1_ >= this.field_92022_t && p_mouseClicked_1_ <= this.field_92020_v && p_mouseClicked_2_ >= this.field_92021_u && p_mouseClicked_2_ <= this.field_92019_w) {
                GuiConfirmOpenLink guiconfirmopenlink = new GuiConfirmOpenLink((GuiYesNoCallback)this, this.openGLWarningLink, 13, true);
                guiconfirmopenlink.func_146358_g();
                this.field_146297_k.func_147108_a((GuiScreen)guiconfirmopenlink);
            }
            Unit unit = Unit.INSTANCE;
        }
    }

    public void func_146281_b() {
        if (this.field_183503_M != null) {
            GuiScreen guiScreen = this.field_183503_M;
            Intrinsics.checkNotNull(guiScreen);
            guiScreen.func_146281_b();
        }
    }

    static {
        ResourceLocation[] resourceLocationArray = new ResourceLocation[]{new ResourceLocation("liquidplus/panorama/panorama_0.png"), new ResourceLocation("liquidplus/panorama/panorama_1.png"), new ResourceLocation("liquidplus/panorama/panorama_2.png"), new ResourceLocation("liquidplus/panorama/panorama_3.png"), new ResourceLocation("liquidplus/panorama/panorama_4.png"), new ResourceLocation("liquidplus/panorama/panorama_5.png")};
        titlePanoramaPaths = resourceLocationArray;
    }

    @Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00060\bX\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\t\u00a8\u0006\n"}, d2={"Lnet/dev/important/gui/client/Menu$Companion;", "", "()V", "RANDOM", "Ljava/util/Random;", "splashTexts", "Lnet/minecraft/util/ResourceLocation;", "titlePanoramaPaths", "", "[Lnet/minecraft/util/ResourceLocation;", "LiquidBounce"})
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}

