/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.gui.GuiLanguage
 *  net.minecraft.client.gui.GuiMultiplayer
 *  net.minecraft.client.gui.GuiOptions
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.GuiWorldSelection
 *  net.minecraft.client.settings.GameSettings
 *  net.minecraftforge.client.event.GuiScreenEvent$ActionPerformedEvent$Post
 *  net.minecraftforge.client.event.GuiScreenEvent$ActionPerformedEvent$Pre
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.fml.common.eventhandler.Event
 *  org.jetbrains.annotations.NotNull
 */
package ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.ui.client.GuiBackground;
import net.ccbluex.liquidbounce.ui.client.GuiModsMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLanguage;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiWorldSelection;
import net.minecraft.client.settings.GameSettings;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.jetbrains.annotations.NotNull;
import ui.HuaHuo4;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000F\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\u0007\n\u0002\b\u0005\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0006\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u001fH\u0014J \u0010 \u001a\u00020\u001d2\u0006\u0010!\u001a\u00020\"2\u0006\u0010#\u001a\u00020\"2\u0006\u0010$\u001a\u00020\nH\u0016J\b\u0010%\u001a\u00020\u001dH\u0016J \u0010&\u001a\u00020\u001d2\u0006\u0010!\u001a\u00020\"2\u0006\u0010#\u001a\u00020\"2\u0006\u0010'\u001a\u00020\"H\u0014R\u001a\u0010\u0003\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u000e\u0010\t\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\f\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u0006\"\u0004\b\u000e\u0010\bR \u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00110\u0010X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\u0013\"\u0004\b\u0014\u0010\u0015R\u001a\u0010\u0016\u001a\u00020\u0017X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0018\u0010\u0019\"\u0004\b\u001a\u0010\u001b\u00a8\u0006("}, d2={"Lui/GuiMainMenu;", "Lnet/minecraft/client/gui/GuiScreen;", "()V", "alrUpdate", "", "getAlrUpdate", "()Z", "setAlrUpdate", "(Z)V", "currentX", "", "currentY", "drag", "getDrag", "setDrag", "huaHuo4s", "", "Lui/HuaHuo4;", "getHuaHuo4s", "()Ljava/util/List;", "setHuaHuo4s", "(Ljava/util/List;)V", "lastAnimTick", "", "getLastAnimTick", "()J", "setLastAnimTick", "(J)V", "actionPerformed", "", "button", "Lnet/minecraft/client/gui/GuiButton;", "drawScreen", "mouseX", "", "mouseY", "partialTicks", "initGui", "mouseClicked", "mouseButton", "LiKingSense"})
public final class GuiMainMenu
extends GuiScreen {
    public float currentX;
    public float currentY;
    public long lastAnimTick;
    public boolean alrUpdate;
    public boolean drag;
    @NotNull
    public List<HuaHuo4> huaHuo4s = new ArrayList();

    public final long getLastAnimTick() {
        return this.lastAnimTick;
    }

    public final void setLastAnimTick(long l) {
        this.lastAnimTick = l;
    }

    public final boolean getAlrUpdate() {
        return this.alrUpdate;
    }

    public final void setAlrUpdate(boolean bl) {
        this.alrUpdate = bl;
    }

    public final boolean getDrag() {
        return this.drag;
    }

    public final void setDrag(boolean bl) {
        this.drag = bl;
    }

    @NotNull
    public final List<HuaHuo4> getHuaHuo4s() {
        return this.huaHuo4s;
    }

    public final void setHuaHuo4s(@NotNull List<HuaHuo4> list) {
        Intrinsics.checkParameterIsNotNull(list, (String)"<set-?>");
        this.huaHuo4s = list;
    }

    /*
     * Exception decompiling
     */
    public void func_73866_w_() {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Invalid stack depths @ lbl51 : GOTO - null : trying to set 7 previously set to 6
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:207)
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
    public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl21 : INVOKESTATIC - null : Stack underflow
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

    public protected void func_146284_a(@NotNull GuiButton button) {
        Intrinsics.checkParameterIsNotNull((Object)button, (String)"button");
        switch (button.field_146127_k) {
            case 1: {
                this.field_146297_k.func_147108_a((GuiScreen)new GuiWorldSelection((GuiScreen)this));
                break;
            }
            case 2: {
                this.field_146297_k.func_147108_a((GuiScreen)new GuiMultiplayer((GuiScreen)this));
                break;
            }
            case 3: {
                this.field_146297_k.func_147108_a((GuiScreen)new GuiOptions((GuiScreen)this, this.field_146297_k.field_71474_y));
                break;
            }
            case 4: {
                this.field_146297_k.func_147108_a((GuiScreen)new GuiModsMenu(this));
                break;
            }
            case 5: {
                this.field_146297_k.func_147108_a((GuiScreen)new GuiBackground(this));
                break;
            }
            case 6: {
                GuiScreen guiScreen = this;
                GameSettings gameSettings = this.field_146297_k.field_71474_y;
                Minecraft minecraft = this.field_146297_k;
                Intrinsics.checkExpressionValueIsNotNull((Object)minecraft, (String)"this.mc");
                this.field_146297_k.func_147108_a((GuiScreen)new GuiLanguage(guiScreen, gameSettings, minecraft.func_135016_M()));
                break;
            }
            case 7: {
                this.field_146297_k.func_71400_g();
                break;
            }
        }
    }

    /*
     * WARNING - void declaration
     */
    public protected void func_73864_a(int mouseX, int mouseY, int mouseButton) {
        HuaHuo4 huaHuo4 = new HuaHuo4(mouseX, mouseY);
        this.huaHuo4s.add(huaHuo4);
        if (mouseButton == 0) {
            void var5_6;
            List list = this.field_146292_n;
            Intrinsics.checkExpressionValueIsNotNull((Object)list, (String)"buttonList");
            int n = ((Collection)list).size();
            while (var5_6 < n) {
                void i;
                Object e = this.field_146292_n.get((int)i);
                if (e == null) {
                    throw new TypeCastException("null cannot be cast to non-null type net.minecraft.client.gui.GuiButton");
                }
                GuiButton guibutton = (GuiButton)e;
                if (guibutton.func_146116_c(this.field_146297_k, mouseX, mouseY)) {
                    GuiScreenEvent.ActionPerformedEvent.Pre event = new GuiScreenEvent.ActionPerformedEvent.Pre((GuiScreen)this, guibutton, this.field_146292_n);
                    if (MinecraftForge.EVENT_BUS.post((Event)event)) break;
                    GuiButton guiButton = event.getButton();
                    Intrinsics.checkExpressionValueIsNotNull((Object)guiButton, (String)"event.button");
                    this.field_146290_a = guibutton = guiButton;
                    Minecraft minecraft = this.field_146297_k;
                    Intrinsics.checkExpressionValueIsNotNull((Object)minecraft, (String)"mc");
                    guibutton.func_146113_a(minecraft.func_147118_V());
                    this.func_146284_a(guibutton);
                    if (Intrinsics.areEqual((Object)((Object)this), (Object)this.field_146297_k.field_71462_r)) {
                        MinecraftForge.EVENT_BUS.post((Event)new GuiScreenEvent.ActionPerformedEvent.Post((GuiScreen)this, event.getButton(), this.field_146292_n));
                    }
                }
                ++i;
            }
        }
    }
}

