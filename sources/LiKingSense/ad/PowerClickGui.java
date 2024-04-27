/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.GlStateManager
 *  org.lwjgl.opengl.GL11
 */
package ad;

import ad.PowerButton;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.value.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

public class PowerClickGui
extends GuiScreen {
    public static boolean binding = false;
    public static Module currentMod = null;
    private final PowerButton handlerMid;
    private final PowerButton handlerRight;
    private final PowerButton handler;
    public int moveX = 0;
    public int moveY = 0;
    public int startX = 50;
    public int startY = 40;
    public int selectCategory = 0;
    public Module bmod;
    public boolean dragging;
    public boolean drag;
    public boolean Mdrag;
    ArrayList<Module> mods = new ArrayList<Module>(LiquidBounce.moduleManager.getModules());
    ScaledResolution res;
    Value<?> value;
    ScaledResolution sr;
    private float scrollY;
    private float modscrollY;

    public PowerClickGui() {
        this.handlerMid = new PowerButton(2);
        this.handlerRight = new PowerButton(1);
        this.handler = new PowerButton(0);
        this.res = new ScaledResolution(Minecraft.func_71410_x());
        this.sr = new ScaledResolution(Minecraft.func_71410_x());
    }

    public static void erase(boolean stencil) {
        GL11.glStencilOp((int)(stencil ? 514 : 517), (int)0, (int)0);
        int n = 0;
        GlStateManager.func_179141_d();
        GlStateManager.func_179147_l();
        GL11.glAlphaFunc((int)516, (float)0.0f);
    }

    public static List getValueList(Module module) {
        return module.getValues();
    }

    public static List<Module> getModsInCategory(ModuleCategory cat) {
        ArrayList<Module> list = new ArrayList<Module>();
        for (Module m : LiquidBounce.moduleManager.getModules()) {
            if (m.getCategory() != cat) continue;
            list.add(m);
        }
        return list;
    }

    public boolean func_73868_f() {
        return false;
    }

    protected void func_73869_a(char typedChar, int keyCode) throws IOException {
        if (binding) {
            if (keyCode != 1 && keyCode != 211) {
                this.bmod.setKeyBind(keyCode);
            } else if (keyCode == 211) {
                this.bmod.setKeyBind(0);
            }
            binding = false;
        }
        super.func_73869_a(typedChar, keyCode);
    }

    public void func_146286_b(int mouseX, int mouseY, int state) {
        if (this.dragging) {
            this.dragging = false;
        }
        if (this.drag) {
            this.drag = false;
        }
    }

    /*
     * Exception decompiling
     */
    public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl253 : INVOKEVIRTUAL - null : Stack underflow
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

    public boolean isHovered(float x, float y, float width, float height, int mouseX, int mouseY) {
        return (float)mouseX >= x && (float)mouseX <= width && (float)mouseY >= y && (float)mouseY <= height;
    }

    public void func_146281_b() {
        if (this.field_146297_k.field_71460_t.func_147706_e() != null) {
            this.field_146297_k.field_71460_t.func_147706_e().func_148021_a();
        }
        this.dragging = false;
        this.drag = false;
        this.Mdrag = false;
        super.func_146281_b();
    }
}

