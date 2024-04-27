/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.collections.ArraysKt
 *  kotlin.collections.CollectionsKt
 *  kotlin.comparisons.ComparisonsKt
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.client.renderer.GlStateManager
 *  org.jetbrains.annotations.NotNull
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.features.module.modules.render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.comparisons.ComparisonsKt;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.KeyEvent;
import net.ccbluex.liquidbounce.event.Render2DEvent;
import net.ccbluex.liquidbounce.event.TickEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.render.JelloTabGui$WhenMappings;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.Translate;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.renderer.GlStateManager;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

@ModuleInfo(name="JelloTabGui", description="Toggles visibility of the HUD.", category=ModuleCategory.RENDER, array=false)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\u008e\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0005\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0014\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\u001f\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010 \n\u0002\b'\n\u0002\u0010\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0002\b\u0005\b\u0007\u0018\u00002\u00020\u0001:\u0004\u00a9\u0001\u00aa\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\n\u0010\u0092\u0001\u001a\u00030\u0093\u0001H\u0002J\n\u0010\u0094\u0001\u001a\u00030\u0093\u0001H\u0002J\n\u0010\u0095\u0001\u001a\u00030\u0093\u0001H\u0002J\n\u0010\u0096\u0001\u001a\u00030\u0093\u0001H\u0002J\u0013\u0010\u0097\u0001\u001a\u00030\u0093\u00012\u0007\u0010\u0098\u0001\u001a\u00020\u0015H\u0002J\u0014\u0010\u0099\u0001\u001a\u00030\u0093\u00012\b\u0010\u009a\u0001\u001a\u00030\u009b\u0001H\u0007J\u0014\u0010\u009c\u0001\u001a\u00030\u0093\u00012\b\u0010\u009a\u0001\u001a\u00030\u009d\u0001H\u0007J\u0014\u0010\u009e\u0001\u001a\u00030\u0093\u00012\b\u0010\u009f\u0001\u001a\u00030\u00a0\u0001H\u0002J\u0014\u0010\u00a1\u0001\u001a\u00030\u0093\u00012\b\u0010\u00a2\u0001\u001a\u00030\u00a3\u0001H\u0007J\u001b\u0010\u00a4\u0001\u001a\u00020\u00152\b\u0010\u00a5\u0001\u001a\u00030\u00a6\u00012\b\u0010\u00a7\u0001\u001a\u00030\u00a6\u0001J\n\u0010\u00a8\u0001\u001a\u00030\u0093\u0001H\u0002R\u001a\u0010\u0003\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR \u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\nX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000fR\u0011\u0010\u0010\u001a\u00020\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u001a\u0010\u0014\u001a\u00020\u0015X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u0017\"\u0004\b\u0018\u0010\u0019R\u001a\u0010\u001a\u001a\u00020\u0015X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001b\u0010\u0017\"\u0004\b\u001c\u0010\u0019R\u001a\u0010\u001d\u001a\u00020\u0015X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001e\u0010\u0017\"\u0004\b\u001f\u0010\u0019R\u001a\u0010 \u001a\u00020\u0015X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b!\u0010\u0017\"\u0004\b\"\u0010\u0019R\u001a\u0010#\u001a\u00020\u0015X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b$\u0010\u0017\"\u0004\b%\u0010\u0019R\u001a\u0010&\u001a\u00020\u0015X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b'\u0010\u0017\"\u0004\b(\u0010\u0019R\u001a\u0010)\u001a\u00020*X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b+\u0010,\"\u0004\b-\u0010.R\u0011\u0010/\u001a\u000200\u00a2\u0006\b\n\u0000\u001a\u0004\b1\u00102R\u001a\u00103\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b4\u0010\u0006\"\u0004\b5\u0010\bR\u001a\u00106\u001a\u00020\u0015X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b7\u0010\u0017\"\u0004\b8\u0010\u0019R\u001a\u00109\u001a\u00020\u0015X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b:\u0010\u0017\"\u0004\b;\u0010\u0019R\u0011\u0010<\u001a\u00020=\u00a2\u0006\b\n\u0000\u001a\u0004\b>\u0010?R\u001a\u0010@\u001a\u00020\u0015X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bA\u0010\u0017\"\u0004\bB\u0010\u0019R\u001a\u0010C\u001a\u00020\u0015X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bD\u0010\u0017\"\u0004\bE\u0010\u0019R\u0014\u0010F\u001a\u00020\u0004X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\bG\u0010\u0006R\u001a\u0010H\u001a\u00020\u0015X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bI\u0010\u0017\"\u0004\bJ\u0010\u0019R\u001a\u0010K\u001a\u00020\u0015X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bL\u0010\u0017\"\u0004\bM\u0010\u0019R\u001a\u0010N\u001a\u00020\u0015X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bO\u0010\u0017\"\u0004\bP\u0010\u0019R\u001a\u0010Q\u001a\u00020\u0015X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bR\u0010\u0017\"\u0004\bS\u0010\u0019R\u001a\u0010T\u001a\u00020\u0015X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bU\u0010\u0017\"\u0004\bV\u0010\u0019R\u001a\u0010W\u001a\u00020\u0015X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bX\u0010\u0017\"\u0004\bY\u0010\u0019R\u0011\u0010Z\u001a\u000200\u00a2\u0006\b\n\u0000\u001a\u0004\b[\u00102R\u001a\u0010\\\u001a\u00020]X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b^\u0010_\"\u0004\b`\u0010aR\u0011\u0010b\u001a\u00020\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\bc\u0010\u0013R\u001a\u0010d\u001a\u00020eX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bf\u0010g\"\u0004\bh\u0010iR \u0010j\u001a\b\u0012\u0004\u0012\u00020\u00010kX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bl\u0010\r\"\u0004\bm\u0010\u000fR\u001a\u0010n\u001a\u00020\u0015X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bo\u0010\u0017\"\u0004\bp\u0010\u0019R\u001a\u0010q\u001a\u00020\u0015X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\br\u0010\u0017\"\u0004\bs\u0010\u0019R\u0011\u0010t\u001a\u00020\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\bu\u0010\u0013R\u001a\u0010v\u001a\u00020\u0015X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bw\u0010\u0017\"\u0004\bx\u0010\u0019R\u001a\u0010y\u001a\u00020\u0015X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bz\u0010\u0017\"\u0004\b{\u0010\u0019R\u001a\u0010|\u001a\u00020\u0015X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b}\u0010\u0017\"\u0004\b~\u0010\u0019R\u001c\u0010\u007f\u001a\u00020\u0015X\u0086\u000e\u00a2\u0006\u0010\n\u0000\u001a\u0005\b\u0080\u0001\u0010\u0017\"\u0005\b\u0081\u0001\u0010\u0019R\u001d\u0010\u0082\u0001\u001a\u00020\u0015X\u0086\u000e\u00a2\u0006\u0010\n\u0000\u001a\u0005\b\u0083\u0001\u0010\u0017\"\u0005\b\u0084\u0001\u0010\u0019R\u001d\u0010\u0085\u0001\u001a\u00020\u0015X\u0086\u000e\u00a2\u0006\u0010\n\u0000\u001a\u0005\b\u0086\u0001\u0010\u0017\"\u0005\b\u0087\u0001\u0010\u0019R\u001d\u0010\u0088\u0001\u001a\u00020*X\u0086\u000e\u00a2\u0006\u0010\n\u0000\u001a\u0005\b\u0089\u0001\u0010,\"\u0005\b\u008a\u0001\u0010.R\u001d\u0010\u008b\u0001\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u0010\n\u0000\u001a\u0005\b\u008c\u0001\u0010\u0006\"\u0005\b\u008d\u0001\u0010\bR\u0016\u0010\u008e\u0001\u001a\u00020\u0004X\u0086D\u00a2\u0006\t\n\u0000\u001a\u0005\b\u008f\u0001\u0010\u0006R\u0016\u0010\u0090\u0001\u001a\u00020\u0004X\u0086D\u00a2\u0006\t\n\u0000\u001a\u0005\b\u0091\u0001\u0010\u0006\u00a8\u0006\u00ab\u0001"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/render/JelloTabGui;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "ModulePositonY", "", "getModulePositonY", "()F", "setModulePositonY", "(F)V", "Modulecategory", "", "Lnet/ccbluex/liquidbounce/features/module/modules/render/JelloTabGui$AnimaitonCategory;", "getModulecategory", "()Ljava/util/List;", "setModulecategory", "(Ljava/util/List;)V", "Shader", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "getShader", "()Lnet/ccbluex/liquidbounce/value/BoolValue;", "bB", "", "getBB", "()I", "setBB", "(I)V", "bBlue", "getBBlue", "setBBlue", "bG", "getBG", "setBG", "bGreen", "getBGreen", "setBGreen", "bR", "getBR", "setBR", "bRed", "getBRed", "setBRed", "bottom", "Ljava/awt/Color;", "getBottom", "()Ljava/awt/Color;", "setBottom", "(Ljava/awt/Color;)V", "categoryAnimaiton", "Lnet/ccbluex/liquidbounce/utils/Translate;", "getCategoryAnimaiton", "()Lnet/ccbluex/liquidbounce/utils/Translate;", "categoryPositonY", "getCategoryPositonY", "setCategoryPositonY", "colorBottom", "getColorBottom", "setColorBottom", "colorBottomRight", "getColorBottomRight", "setColorBottomRight", "colorMode", "Lnet/ccbluex/liquidbounce/value/ListValue;", "getColorMode", "()Lnet/ccbluex/liquidbounce/value/ListValue;", "colorTop", "getColorTop", "setColorTop", "colorTopRight", "getColorTopRight", "setColorTopRight", "height", "getHeight", "lastbBlue", "getLastbBlue", "setLastbBlue", "lastbGreen", "getLastbGreen", "setLastbGreen", "lastbRed", "getLastbRed", "setLastbRed", "lasttBlue", "getLasttBlue", "setLasttBlue", "lasttGreen", "getLasttGreen", "setLasttGreen", "lasttRed", "getLasttRed", "setLasttRed", "moduleAnimaiton", "getModuleAnimaiton", "msTimer", "Lnet/ccbluex/liquidbounce/utils/timer/MSTimer;", "getMsTimer", "()Lnet/ccbluex/liquidbounce/utils/timer/MSTimer;", "setMsTimer", "(Lnet/ccbluex/liquidbounce/utils/timer/MSTimer;)V", "notselectrect", "getNotselectrect", "openModuleGui", "", "getOpenModuleGui", "()Z", "setOpenModuleGui", "(Z)V", "selecteModule", "", "getSelecteModule", "setSelecteModule", "selecteModuleindex", "getSelecteModuleindex", "setSelecteModuleindex", "selectedCategory", "getSelectedCategory", "setSelectedCategory", "selectgradientBackground", "getSelectgradientBackground", "tB", "getTB", "setTB", "tBlue", "getTBlue", "setTBlue", "tG", "getTG", "setTG", "tGreen", "getTGreen", "setTGreen", "tR", "getTR", "setTR", "tRed", "getTRed", "setTRed", "top", "getTop", "setTop", "width", "getWidth", "setWidth", "x", "getX", "y", "getY", "disabler", "", "disablerScissorBox", "enabler", "enablerScissorBox", "handleKey", "keyCode", "keyevent", "event", "Lnet/ccbluex/liquidbounce/event/KeyEvent;", "onTick", "Lnet/ccbluex/liquidbounce/event/TickEvent;", "parseAction", "action", "Lnet/ccbluex/liquidbounce/features/module/modules/render/JelloTabGui$Action;", "rendertabGui", "evnet", "Lnet/ccbluex/liquidbounce/event/Render2DEvent;", "smoothAnimation", "current", "", "last", "updateBackGound", "Action", "AnimaitonCategory", "LiKingSense"})
public final class JelloTabGui
extends Module {
    @NotNull
    private final BoolValue Shader = new BoolValue("Shader", false);
    @NotNull
    private final ListValue colorMode = new ListValue("Color", new String[]{"Sync", "Tenacity"}, "Tenacity");
    @NotNull
    private final BoolValue notselectrect = new BoolValue("SelectNotDrawRect", true);
    @NotNull
    private final BoolValue selectgradientBackground = new BoolValue("SelectGradientRect", false);
    private final float x = 5.0f;
    private final float y = 50.0f;
    private final float height = 78.5f;
    private float width = 75.0f;
    private boolean openModuleGui;
    private int selectedCategory;
    private int selecteModuleindex;
    @NotNull
    private List<? extends Module> selecteModule = CollectionsKt.emptyList();
    @NotNull
    private List<AnimaitonCategory> Modulecategory;
    @NotNull
    private final Translate categoryAnimaiton;
    @NotNull
    private final Translate moduleAnimaiton;
    private float categoryPositonY;
    private float ModulePositonY;
    @NotNull
    private MSTimer msTimer;
    @NotNull
    private Color top;
    @NotNull
    private Color bottom;
    private int tRed;
    private int tGreen;
    private int tBlue;
    private int lasttRed;
    private int lasttGreen;
    private int lasttBlue;
    private int bRed;
    private int bGreen;
    private int bBlue;
    private int lastbRed;
    private int lastbGreen;
    private int lastbBlue;
    private int colorTop;
    private int colorTopRight;
    private int colorBottom;
    private int colorBottomRight;
    private int tR;
    private int tG;
    private int tB;
    private int bR;
    private int bG;
    private int bB;

    @NotNull
    public final BoolValue getShader() {
        return this.Shader;
    }

    @NotNull
    public final ListValue getColorMode() {
        return this.colorMode;
    }

    @NotNull
    public final BoolValue getNotselectrect() {
        return this.notselectrect;
    }

    @NotNull
    public final BoolValue getSelectgradientBackground() {
        return this.selectgradientBackground;
    }

    public final float getX() {
        return this.x;
    }

    public final float getY() {
        return this.y;
    }

    public final float getHeight() {
        return this.height;
    }

    public final float getWidth() {
        return this.width;
    }

    public final void setWidth(float f) {
        this.width = f;
    }

    public final boolean getOpenModuleGui() {
        return this.openModuleGui;
    }

    public final void setOpenModuleGui(boolean bl) {
        this.openModuleGui = bl;
    }

    public final int getSelectedCategory() {
        return this.selectedCategory;
    }

    public final void setSelectedCategory(int n) {
        this.selectedCategory = n;
    }

    public final int getSelecteModuleindex() {
        return this.selecteModuleindex;
    }

    public final void setSelecteModuleindex(int n) {
        this.selecteModuleindex = n;
    }

    @NotNull
    public final List<Module> getSelecteModule() {
        return this.selecteModule;
    }

    public final void setSelecteModule(@NotNull List<? extends Module> list) {
        Intrinsics.checkParameterIsNotNull(list, (String)"<set-?>");
        this.selecteModule = list;
    }

    @NotNull
    public final List<AnimaitonCategory> getModulecategory() {
        return this.Modulecategory;
    }

    public final void setModulecategory(@NotNull List<AnimaitonCategory> list) {
        Intrinsics.checkParameterIsNotNull(list, (String)"<set-?>");
        this.Modulecategory = list;
    }

    @NotNull
    public final Translate getCategoryAnimaiton() {
        return this.categoryAnimaiton;
    }

    @NotNull
    public final Translate getModuleAnimaiton() {
        return this.moduleAnimaiton;
    }

    public final float getCategoryPositonY() {
        return this.categoryPositonY;
    }

    public final void setCategoryPositonY(float f) {
        this.categoryPositonY = f;
    }

    public final float getModulePositonY() {
        return this.ModulePositonY;
    }

    public final void setModulePositonY(float f) {
        this.ModulePositonY = f;
    }

    @NotNull
    public final MSTimer getMsTimer() {
        return this.msTimer;
    }

    public final void setMsTimer(@NotNull MSTimer mSTimer) {
        Intrinsics.checkParameterIsNotNull((Object)mSTimer, (String)"<set-?>");
        this.msTimer = mSTimer;
    }

    @NotNull
    public final Color getTop() {
        return this.top;
    }

    public final void setTop(@NotNull Color color) {
        Intrinsics.checkParameterIsNotNull((Object)color, (String)"<set-?>");
        this.top = color;
    }

    @NotNull
    public final Color getBottom() {
        return this.bottom;
    }

    public final void setBottom(@NotNull Color color) {
        Intrinsics.checkParameterIsNotNull((Object)color, (String)"<set-?>");
        this.bottom = color;
    }

    public final int getTRed() {
        return this.tRed;
    }

    public final void setTRed(int n) {
        this.tRed = n;
    }

    public final int getTGreen() {
        return this.tGreen;
    }

    public final void setTGreen(int n) {
        this.tGreen = n;
    }

    public final int getTBlue() {
        return this.tBlue;
    }

    public final void setTBlue(int n) {
        this.tBlue = n;
    }

    public final int getLasttRed() {
        return this.lasttRed;
    }

    public final void setLasttRed(int n) {
        this.lasttRed = n;
    }

    public final int getLasttGreen() {
        return this.lasttGreen;
    }

    public final void setLasttGreen(int n) {
        this.lasttGreen = n;
    }

    public final int getLasttBlue() {
        return this.lasttBlue;
    }

    public final void setLasttBlue(int n) {
        this.lasttBlue = n;
    }

    public final int getBRed() {
        return this.bRed;
    }

    public final void setBRed(int n) {
        this.bRed = n;
    }

    public final int getBGreen() {
        return this.bGreen;
    }

    public final void setBGreen(int n) {
        this.bGreen = n;
    }

    public final int getBBlue() {
        return this.bBlue;
    }

    public final void setBBlue(int n) {
        this.bBlue = n;
    }

    public final int getLastbRed() {
        return this.lastbRed;
    }

    public final void setLastbRed(int n) {
        this.lastbRed = n;
    }

    public final int getLastbGreen() {
        return this.lastbGreen;
    }

    public final void setLastbGreen(int n) {
        this.lastbGreen = n;
    }

    public final int getLastbBlue() {
        return this.lastbBlue;
    }

    public final void setLastbBlue(int n) {
        this.lastbBlue = n;
    }

    public final int getColorTop() {
        return this.colorTop;
    }

    public final void setColorTop(int n) {
        this.colorTop = n;
    }

    public final int getColorTopRight() {
        return this.colorTopRight;
    }

    public final void setColorTopRight(int n) {
        this.colorTopRight = n;
    }

    public final int getColorBottom() {
        return this.colorBottom;
    }

    public final void setColorBottom(int n) {
        this.colorBottom = n;
    }

    public final int getColorBottomRight() {
        return this.colorBottomRight;
    }

    public final void setColorBottomRight(int n) {
        this.colorBottomRight = n;
    }

    @EventTarget
    public final void onTick(@NotNull TickEvent event) {
        Intrinsics.checkParameterIsNotNull((Object)event, (String)"event");
        this.lasttRed = this.tRed;
        this.lasttGreen = this.tGreen;
        this.lasttBlue = this.tBlue;
        this.lastbRed = this.bRed;
        this.lastbGreen = this.bGreen;
        this.lastbBlue = this.bBlue;
    }

    public final int getTR() {
        return this.tR;
    }

    public final void setTR(int n) {
        this.tR = n;
    }

    public final int getTG() {
        return this.tG;
    }

    public final void setTG(int n) {
        this.tG = n;
    }

    public final int getTB() {
        return this.tB;
    }

    public final void setTB(int n) {
        this.tB = n;
    }

    public final int getBR() {
        return this.bR;
    }

    public final void setBR(int n) {
        this.bR = n;
    }

    public final int getBG() {
        return this.bG;
    }

    public final void setBG(int n) {
        this.bG = n;
    }

    public final int getBB() {
        return this.bB;
    }

    public final void setBB(int n) {
        this.bB = n;
    }

    /*
     * Exception decompiling
     */
    @EventTarget
    public final void rendertabGui(@NotNull Render2DEvent evnet) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl533 : INVOKESTATIC - null : Stack underflow
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

    /*
     * Exception decompiling
     */
    private final void updateBackGound() {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl171 : INVOKESTATIC - null : Stack underflow
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

    private final void enablerScissorBox() {
        RenderUtils.makeScissorBox(this.x, this.y, this.x + this.width, this.y + this.height);
        GL11.glEnable((int)3089);
    }

    private final void disablerScissorBox() {
        GL11.glDisable((int)3089);
    }

    private final void enabler() {
        GlStateManager.func_179094_E();
    }

    private final void disabler() {
        GlStateManager.func_179121_F();
    }

    @EventTarget
    public final void keyevent(@NotNull KeyEvent event) {
        Intrinsics.checkParameterIsNotNull((Object)event, (String)"event");
        int key = event.getKey();
        this.handleKey(key);
    }

    private final void handleKey(int keyCode) {
        switch (keyCode) {
            case 200: {
                this.parseAction(Action.UP);
                break;
            }
            case 208: {
                this.parseAction(Action.DOWN);
                break;
            }
            case 203: {
                this.parseAction(Action.LEFT);
                break;
            }
            case 205: {
                this.parseAction(Action.RIGHT);
                break;
            }
            case 28: {
                this.parseAction(Action.TOGGLE);
                break;
            }
        }
    }

    public final int smoothAnimation(double current, double last) {
        return (int)(current * (double)MinecraftInstance.mc.getTimer().getRenderPartialTicks() + last * (double)(1.0f - MinecraftInstance.mc.getTimer().getRenderPartialTicks()));
    }

    /*
     * WARNING - void declaration
     */
    private final void parseAction(Action action) {
        switch (JelloTabGui$WhenMappings.$EnumSwitchMapping$0[action.ordinal()]) {
            case 1: {
                int n;
                if (this.selectedCategory > 0 && !this.openModuleGui) {
                    n = this.selectedCategory;
                    this.selectedCategory = n + -1;
                }
                if (this.selecteModuleindex <= 0) break;
                n = this.selecteModuleindex;
                this.selecteModuleindex = n + -1;
                break;
            }
            case 2: {
                int n;
                if (this.selectedCategory < CollectionsKt.getLastIndex(this.Modulecategory) && !this.openModuleGui) {
                    n = this.selectedCategory;
                    this.selectedCategory = n + 1;
                }
                if (this.selecteModuleindex >= CollectionsKt.getLastIndex(this.selecteModule)) break;
                n = this.selecteModuleindex;
                this.selecteModuleindex = n + 1;
                break;
            }
            case 3: {
                List list;
                if (!this.openModuleGui) break;
                this.openModuleGui = false;
                this.selecteModuleindex = 0;
                JelloTabGui jelloTabGui = this;
                boolean bl = false;
                jelloTabGui.selecteModule = list = CollectionsKt.emptyList();
                break;
            }
            case 4: {
                void $this$sortedBy$iv;
                void $this$filterTo$iv$iv;
                Iterable $this$filter$iv;
                if (this.openModuleGui) break;
                this.openModuleGui = true;
                Iterable iterable = LiquidBounce.INSTANCE.getModuleManager().getModules();
                JelloTabGui jelloTabGui = this;
                boolean $i$f$filter = false;
                void var4_12 = $this$filter$iv;
                Collection destination$iv$iv = new ArrayList();
                boolean $i$f$filterTo = false;
                for (Object element$iv$iv : $this$filterTo$iv$iv) {
                    Module it = (Module)element$iv$iv;
                    boolean bl = false;
                    if (!(it.getCategory() == ModuleCategory.values()[this.selectedCategory])) continue;
                    destination$iv$iv.add(element$iv$iv);
                }
                List list = (List)destination$iv$iv;
                $this$filter$iv = list;
                boolean $i$f$sortedBy = false;
                var4_12 = $this$sortedBy$iv;
                boolean bl = false;
                Comparator comparator = new Comparator<T>(){

                    public final int compare(T a, T b) {
                        boolean bl = false;
                        Module it = (Module)a;
                        boolean bl2 = false;
                        it = (Module)b;
                        Comparable comparable = Integer.valueOf(0);
                        bl2 = false;
                        Integer n = 0;
                        return ComparisonsKt.compareValues((Comparable)comparable, (Comparable)n);
                    }
                };
                jelloTabGui.selecteModule = list = CollectionsKt.sortedWith((Iterable)var4_12, (Comparator)comparator);
                break;
            }
            case 5: {
                if (!this.openModuleGui) break;
                Module selecetd = this.selecteModule.get(this.selecteModuleindex);
                selecetd.toggle();
                break;
            }
        }
    }

    /*
     * WARNING - void declaration
     */
    public JelloTabGui() {
        Color color;
        Color color2;
        List list;
        JelloTabGui jelloTabGui = this;
        int n = 0;
        jelloTabGui.Modulecategory = list = (List)new ArrayList();
        this.categoryAnimaiton = new Translate(0.0f, 0.0f);
        this.moduleAnimaiton = new Translate(0.0f, 0.0f);
        this.msTimer = new MSTimer();
        this.setState(true);
        n = 0;
        int n2 = ArraysKt.getLastIndex((Object[])ModuleCategory.values());
        if (n <= n2) {
            while (true) {
                void index;
                AnimaitonCategory animationcategory = new AnimaitonCategory(ModuleCategory.values()[index].getDisplayName(), new Translate(0.0f, 0.0f));
                this.Modulecategory.add(animationcategory);
                if (index == n2) break;
                ++index;
            }
        }
        JelloTabGui jelloTabGui2 = this;
        Color color3 = color2;
        ((JelloTabGui)((Object)color2)).top = (Color)0;
        JelloTabGui jelloTabGui3 = this;
        Color color4 = color;
        ((JelloTabGui)((Object)color)).bottom = (Color)0;
    }

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0007\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007\u00a8\u0006\b"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/render/JelloTabGui$Action;", "", "(Ljava/lang/String;I)V", "UP", "DOWN", "LEFT", "RIGHT", "TOGGLE", "LiKingSense"})
    public static final class Action
    extends Enum<Action> {
        public static final /* enum */ Action UP;
        public static final /* enum */ Action DOWN;
        public static final /* enum */ Action LEFT;
        public static final /* enum */ Action RIGHT;
        public static final /* enum */ Action TOGGLE;
        private static final /* synthetic */ Action[] $VALUES;

        static {
            Action[] actionArray = new Action[5];
            Action[] actionArray2 = actionArray;
            actionArray[0] = UP = new Action();
            actionArray[1] = DOWN = new Action();
            actionArray[2] = LEFT = new Action();
            actionArray[3] = RIGHT = new Action();
            actionArray[4] = TOGGLE = new Action();
            $VALUES = actionArray;
        }

        public static Action[] values() {
            return (Action[])$VALUES.clone();
        }

        public static Action valueOf(String string) {
            return Enum.valueOf(Action.class, string);
        }
    }

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006R\u001a\u0010\u0004\u001a\u00020\u0005X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\nR\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000e\u00a8\u0006\u000f"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/render/JelloTabGui$AnimaitonCategory;", "", "displayname", "", "animation", "Lnet/ccbluex/liquidbounce/utils/Translate;", "(Ljava/lang/String;Lnet/ccbluex/liquidbounce/utils/Translate;)V", "getAnimation", "()Lnet/ccbluex/liquidbounce/utils/Translate;", "setAnimation", "(Lnet/ccbluex/liquidbounce/utils/Translate;)V", "getDisplayname", "()Ljava/lang/String;", "setDisplayname", "(Ljava/lang/String;)V", "LiKingSense"})
    public static final class AnimaitonCategory {
        @NotNull
        private String displayname;
        @NotNull
        private Translate animation;

        @NotNull
        public final String getDisplayname() {
            return this.displayname;
        }

        public final void setDisplayname(@NotNull String string) {
            Intrinsics.checkParameterIsNotNull((Object)string, (String)"<set-?>");
            this.displayname = string;
        }

        @NotNull
        public final Translate getAnimation() {
            return this.animation;
        }

        public final void setAnimation(@NotNull Translate translate) {
            Intrinsics.checkParameterIsNotNull((Object)translate, (String)"<set-?>");
            this.animation = translate;
        }

        public AnimaitonCategory(@NotNull String displayname, @NotNull Translate animation) {
            Intrinsics.checkParameterIsNotNull((Object)displayname, (String)"displayname");
            Intrinsics.checkParameterIsNotNull((Object)animation, (String)"animation");
            this.displayname = displayname;
            this.animation = animation;
        }
    }
}

