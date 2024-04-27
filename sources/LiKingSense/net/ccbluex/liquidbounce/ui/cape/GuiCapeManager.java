/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  kotlin.Metadata
 *  kotlin.collections.CollectionsKt
 *  kotlin.io.FilesKt
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.random.Random
 *  kotlin.text.Charsets
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.OpenGlHelper
 *  net.minecraft.client.renderer.RenderHelper
 *  net.minecraft.item.ItemStack
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.ui.cape;

import ad.novoline.font.FontRenderer;
import ad.novoline.font.Fonts;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.imageio.ImageIO;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.io.FilesKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.random.Random;
import kotlin.text.Charsets;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.renderer.entity.IRenderManager;
import net.ccbluex.liquidbounce.file.FileManager;
import net.ccbluex.liquidbounce.ui.cape.GuiCapeManager;
import net.ccbluex.liquidbounce.ui.cape.ICape;
import net.ccbluex.liquidbounce.ui.cape.SingleImageCape;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000R\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0007\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013H\u0014J\b\u0010\u0014\u001a\u00020\u0015H\u0016J \u0010\u0016\u001a\u00020\u00112\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u00182\u0006\u0010\u001a\u001a\u00020\u001bH\u0016J\b\u0010\u001c\u001a\u00020\u0011H\u0016J\u0006\u0010\u001d\u001a\u00020\u0011J\u0018\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020!2\u0006\u0010\"\u001a\u00020\nH\u0002J\u0018\u0010#\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020!2\u0006\u0010$\u001a\u00020!H\u0002J\b\u0010%\u001a\u00020\u0011H\u0016J\b\u0010&\u001a\u00020\u0011H\u0002J\u0006\u0010'\u001a\u00020\u0011R\u0017\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0014\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001c\u0010\u000b\u001a\u0004\u0018\u00010\u0005X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000f\u00a8\u0006("}, d2={"Lnet/ccbluex/liquidbounce/ui/cape/GuiCapeManager;", "Lnet/minecraft/client/gui/GuiScreen;", "()V", "capeList", "", "Lnet/ccbluex/liquidbounce/ui/cape/ICape;", "getCapeList", "()Ljava/util/List;", "embeddedCapes", "jsonFile", "Ljava/io/File;", "nowCape", "getNowCape", "()Lnet/ccbluex/liquidbounce/ui/cape/ICape;", "setNowCape", "(Lnet/ccbluex/liquidbounce/ui/cape/ICape;)V", "actionPerformed", "", "p_actionPerformed_1_", "Lnet/minecraft/client/gui/GuiButton;", "doesGuiPauseGame", "", "drawScreen", "p_drawScreen_1_", "", "p_drawScreen_2_", "p_drawScreen_3_", "", "initGui", "load", "loadCapeFromFile", "Lnet/ccbluex/liquidbounce/ui/cape/SingleImageCape;", "name", "", "file", "loadCapeFromResource", "loc", "onGuiClosed", "pushEmbeddedCape", "save", "LiKingSense"})
public final class GuiCapeManager
extends GuiScreen {
    private static final File jsonFile;
    private static final List<ICape> embeddedCapes;
    @Nullable
    private static ICape nowCape;
    @NotNull
    private static final List<ICape> capeList;
    public static final GuiCapeManager INSTANCE;

    @Nullable
    public final ICape getNowCape() {
        return nowCape;
    }

    public final void setNowCape(@Nullable ICape iCape) {
        nowCape = iCape;
    }

    @NotNull
    public final List<ICape> getCapeList() {
        return capeList;
    }

    private final void pushEmbeddedCape() {
        capeList.addAll((Collection<ICape>)embeddedCapes);
    }

    /*
     * Exception decompiling
     */
    public final void load() {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl49 : INVOKESTATIC - null : Stack underflow
         *     at org.benf.cfr.reader.bytecode.analysis.stack.StackSim.getChange(StackSim.java:81)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:242)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:1559)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:434)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    public final void save() {
        JsonObject json = new JsonObject();
        json.addProperty("name", nowCape != null ? nowCape.getName() : "NONE");
        String string = FileManager.PRETTY_GSON.toJson((JsonElement)json);
        Intrinsics.checkExpressionValueIsNotNull((Object)string, (String)"FileManager.PRETTY_GSON.toJson(json)");
        FilesKt.writeText((File)jsonFile, (String)string, (Charset)Charsets.UTF_8);
    }

    private final SingleImageCape loadCapeFromResource(String name, String loc) {
        BufferedImage bufferedImage = ImageIO.read(GuiCapeManager.class.getClassLoader().getResourceAsStream(loc));
        Intrinsics.checkExpressionValueIsNotNull((Object)bufferedImage, (String)"ImageIO.read(GuiCapeMana\u2026getResourceAsStream(loc))");
        return new SingleImageCape(name, bufferedImage);
    }

    private final SingleImageCape loadCapeFromFile(String name, File file) {
        BufferedImage bufferedImage = ImageIO.read(file);
        Intrinsics.checkExpressionValueIsNotNull((Object)bufferedImage, (String)"ImageIO.read(file)");
        return new SingleImageCape(name, bufferedImage);
    }

    public void func_146281_b() {
        this.save();
    }

    /*
     * Exception decompiling
     */
    public void func_73866_w_() {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl14 : INVOKEINTERFACE - null : Stack underflow
         *     at org.benf.cfr.reader.bytecode.analysis.stack.StackSim.getChange(StackSim.java:81)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:242)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:1559)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:434)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    protected void func_146284_a(@NotNull GuiButton p_actionPerformed_1_) {
        Intrinsics.checkParameterIsNotNull((Object)p_actionPerformed_1_, (String)"p_actionPerformed_1_");
        actionPerformed.1 $fun$next$1 = actionPerformed.1.INSTANCE;
        switch (p_actionPerformed_1_.field_146127_k) {
            case 0: {
                this.field_146297_k.func_147108_a(null);
                break;
            }
            case 1: {
                $fun$next$1.invoke(CollectionsKt.indexOf(capeList, (Object)nowCape) - 1);
                break;
            }
            case 2: {
                $fun$next$1.invoke(CollectionsKt.indexOf(capeList, (Object)nowCape) + 1);
                break;
            }
        }
    }

    public void func_73863_a(int p_drawScreen_1_, int p_drawScreen_2_, float p_drawScreen_3_) {
        this.func_146278_c(0);
        GL11.glPushMatrix();
        FontRenderer fontRenderer = Fonts.posterama.posterama13.posterama13;
        CharSequence charSequence = nowCape == null ? "\u00a7cNONE" : "\u00a7a" + nowCape.getName();
        float f = (float)this.field_146294_l * 0.5f;
        float f2 = (float)this.field_146295_m * 0.23f;
        GL11.glScalef((float)2.0f, (float)2.0f, (float)2.0f);
        FontRenderer fontRenderer2 = Fonts.posterama.posterama20.posterama20;
        CharSequence charSequence2 = "Cape Manager";
        float f3 = (float)this.field_146294_l * 0.25f;
        float f4 = (float)this.field_146295_m * 0.03f;
        GL11.glPopMatrix();
        super.func_73863_a(p_drawScreen_1_, p_drawScreen_2_, p_drawScreen_3_);
        if (this.field_146297_k.field_71439_g == null) {
            return;
        }
        GL11.glEnable((int)2884);
        GlStateManager.func_179117_G();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.func_179142_g();
        GlStateManager.func_179094_E();
        GL11.glTranslatef((float)((float)this.field_146294_l * 0.5f - (float)60), (float)((float)this.field_146295_m * 0.3f), (float)0.0f);
        GL11.glScalef((float)2.0f, (float)2.0f, (float)2.0f);
        GL11.glTranslatef((float)30.0f, (float)100.0f, (float)0.0f);
        GlStateManager.func_179109_b((float)0.0f, (float)0.0f, (float)50.0f);
        GlStateManager.func_179152_a((float)-50.0f, (float)50.0f, (float)50.0f);
        GlStateManager.func_179114_b((float)180.0f, (float)0.0f, (float)0.0f, (float)1.0f);
        float renderYawOffset = this.field_146297_k.field_71439_g.field_70761_aq;
        float rotationYaw = this.field_146297_k.field_71439_g.field_70177_z;
        float rotationPitch = this.field_146297_k.field_71439_g.field_70125_A;
        float prevRotationYawHead = this.field_146297_k.field_71439_g.field_70758_at;
        float rotationYawHead = this.field_146297_k.field_71439_g.field_70759_as;
        Object object = this.field_146297_k.field_71439_g.field_71071_by.field_70460_b.get(0);
        Intrinsics.checkExpressionValueIsNotNull((Object)object, (String)"mc.player.inventory.armorInventory[0]");
        ItemStack armor0 = (ItemStack)object;
        Object object2 = this.field_146297_k.field_71439_g.field_71071_by.field_70460_b.get(1);
        Intrinsics.checkExpressionValueIsNotNull((Object)object2, (String)"mc.player.inventory.armorInventory[1]");
        ItemStack armor1 = (ItemStack)object2;
        Object object3 = this.field_146297_k.field_71439_g.field_71071_by.field_70460_b.get(2);
        Intrinsics.checkExpressionValueIsNotNull((Object)object3, (String)"mc.player.inventory.armorInventory[2]");
        ItemStack armor2 = (ItemStack)object3;
        Object object4 = this.field_146297_k.field_71439_g.field_71071_by.field_70460_b.get(3);
        Intrinsics.checkExpressionValueIsNotNull((Object)object4, (String)"mc.player.inventory.armorInventory[3]");
        ItemStack armor3 = (ItemStack)object4;
        Object object5 = this.field_146297_k.field_71439_g.field_71071_by.field_70462_a.get(this.field_146297_k.field_71439_g.field_71071_by.field_70461_c);
        Intrinsics.checkExpressionValueIsNotNull((Object)object5, (String)"mc.player.inventory.main\u2026er.inventory.currentItem]");
        ItemStack current = (ItemStack)object5;
        GlStateManager.func_179114_b((float)135.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        RenderHelper.func_74519_b();
        GlStateManager.func_179114_b((float)-135.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)0.0f, (float)1.0f, (float)0.0f, (float)0.0f);
        this.field_146297_k.field_71439_g.field_70761_aq = 180.0f;
        this.field_146297_k.field_71439_g.field_70177_z = 180.0f;
        this.field_146297_k.field_71439_g.field_70125_A = 0.0f;
        this.field_146297_k.field_71439_g.field_70759_as = this.field_146297_k.field_71439_g.field_70177_z;
        this.field_146297_k.field_71439_g.field_70758_at = this.field_146297_k.field_71439_g.field_70177_z;
        GlStateManager.func_179109_b((float)0.0f, (float)0.0f, (float)0.0f);
        IRenderManager renderManager = MinecraftInstance.mc.getRenderManager();
        renderManager.setPlayerViewY(180.0f);
        renderManager.setRenderShadow(false);
        renderManager.renderEntityWithPosYaw(MinecraftInstance.mc.getThePlayer(), 0.0, 0.0, 0.0, 0.0f, 1.0f);
        renderManager.setRenderShadow(true);
        this.field_146297_k.field_71439_g.field_70761_aq = renderYawOffset;
        this.field_146297_k.field_71439_g.field_70177_z = rotationYaw;
        this.field_146297_k.field_71439_g.field_70125_A = rotationPitch;
        this.field_146297_k.field_71439_g.field_70758_at = prevRotationYawHead;
        this.field_146297_k.field_71439_g.field_70759_as = rotationYawHead;
        this.field_146297_k.field_71439_g.field_71071_by.field_70460_b.set(0, (Object)armor0);
        this.field_146297_k.field_71439_g.field_71071_by.field_70460_b.set(1, (Object)armor1);
        this.field_146297_k.field_71439_g.field_71071_by.field_70460_b.set(2, (Object)armor2);
        this.field_146297_k.field_71439_g.field_71071_by.field_70460_b.set(3, (Object)armor3);
        this.field_146297_k.field_71439_g.field_71071_by.field_70462_a.set(this.field_146297_k.field_71439_g.field_71071_by.field_70461_c, (Object)current);
        GlStateManager.func_179121_F();
        RenderHelper.func_74518_a();
        GlStateManager.func_179101_C();
        GlStateManager.func_179138_g((int)OpenGlHelper.field_77476_b);
        GlStateManager.func_179090_x();
        GlStateManager.func_179138_g((int)OpenGlHelper.field_77478_a);
        GlStateManager.func_179117_G();
    }

    public boolean func_73868_f() {
        return false;
    }

    private GuiCapeManager() {
    }

    static {
        GuiCapeManager guiCapeManager;
        INSTANCE = guiCapeManager = new GuiCapeManager();
        jsonFile = new File(LiquidBounce.INSTANCE.getFileManager().capeDir, "cape.json");
        boolean bl = false;
        embeddedCapes = new ArrayList();
        bl = false;
        capeList = new ArrayList();
        String[] $this$forEach$iv = new String[]{"classic", "classic2", "aurora", "forest", "rose", "lavender", "ocean", "modern1", "modern2", "lava", "citrus", "fire", "nightlife", "abstract", "blur", "owner"};
        boolean $i$f$forEach = false;
        String[] stringArray = $this$forEach$iv;
        int n = stringArray.length;
        for (int i = 0; i < n; ++i) {
            String element$iv;
            String it = element$iv = stringArray[i];
            boolean bl2 = false;
            try {
                embeddedCapes.add(INSTANCE.loadCapeFromResource(it, "assets/minecraft/likingsense/cape/" + it + ".png"));
                continue;
            }
            catch (Throwable e) {
                System.out.println("Failed to load Capes");
            }
        }
        Collection collection = embeddedCapes;
        boolean bl3 = false;
        nowCape = (ICape)CollectionsKt.random((Collection)collection, (Random)((Random)Random.Default));
        guiCapeManager.pushEmbeddedCape();
    }
}

