/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.ui.client.newdropdown.impl;

import java.util.HashMap;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.ui.client.newdropdown.impl.Component;
import net.ccbluex.liquidbounce.ui.client.newdropdown.utils.animations.Animation;
import net.ccbluex.liquidbounce.ui.client.newdropdown.utils.animations.Direction;
import net.ccbluex.liquidbounce.ui.client.newdropdown.utils.animations.impl.DecelerateAnimation;
import net.ccbluex.liquidbounce.ui.client.newdropdown.utils.animations.impl.EaseInOutQuad;
import net.ccbluex.liquidbounce.ui.client.newdropdown.utils.objects.PasswordField;
import net.ccbluex.liquidbounce.ui.client.newdropdown.utils.render.GuiEvents;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.ccbluex.liquidbounce.value.TextValue;
import net.ccbluex.liquidbounce.value.Value;

public class SettingComponents
extends Component {
    public Animation settingHeightScissor;
    private PasswordField selectedField;
    private final HashMap modesHoverAnimation;
    private final HashMap sliderfloatMap;
    public static float scale;
    public Module binding;
    private boolean hueFlag;
    private TextValue selectedStringSetting;
    private final HashMap modeSettingClick;
    public float rectHeight;
    private final HashMap sliderintMap;
    public float panelLimitY;
    public float x;
    private final HashMap sliderintAnimMap;
    public float width;
    private final HashMap keySettingAnimMap = new HashMap();
    private final HashMap toggleAnimation;
    public int alphaAnimation;
    private final HashMap modeSettingAnimMap;
    public float y;
    private final Module module;
    public double settingSize;
    private final HashMap sliderfloatAnimMap;
    public Value draggingNumber;

    @Override
    public void initGui() {
    }

    @Override
    public void keyTyped(char c, int n) {
        if (this.selectedField != null) {
            if (n == 1) {
                this.selectedField = null;
                this.selectedStringSetting = null;
                return;
            }
            this.selectedField.textboxKeyTyped(c, n);
            this.selectedStringSetting.set(this.selectedField.getText());
        }
    }

    @Override
    public void mouseReleased(int n, int n2, int n3) {
        this.handle(n, n2, n3, GuiEvents.RELEASE);
    }

    /*
     * Exception decompiling
     */
    public void handle(int var1, int var2, int var3, GuiEvents var4) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Invalid stack depths @ lbl1395 : IINC - null : trying to set 5 previously set to 4
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

    public SettingComponents(Module module) {
        this.sliderintMap = new HashMap();
        this.sliderintAnimMap = new HashMap();
        this.sliderfloatMap = new HashMap();
        this.sliderfloatAnimMap = new HashMap();
        this.toggleAnimation = new HashMap();
        this.modeSettingAnimMap = new HashMap();
        this.modeSettingClick = new HashMap();
        this.modesHoverAnimation = new HashMap();
        this.module = module;
        this.keySettingAnimMap.put(module, new Animation[]{new EaseInOutQuad(250, 1.0, Direction.BACKWARDS), new DecelerateAnimation(225, 1.0, Direction.BACKWARDS)});
        for (Value value : module.getValues()) {
            if (value instanceof FloatValue) {
                this.sliderfloatMap.put((FloatValue)value, Float.valueOf(0.0f));
                this.sliderfloatAnimMap.put((FloatValue)value, new Animation[]{new DecelerateAnimation(250, 1.0, Direction.BACKWARDS), new DecelerateAnimation(200, 1.0, Direction.BACKWARDS)});
            }
            if (value instanceof IntegerValue) {
                this.sliderintMap.put((IntegerValue)value, Float.valueOf(0.0f));
                this.sliderintAnimMap.put((IntegerValue)value, new Animation[]{new DecelerateAnimation(250, 1.0, Direction.BACKWARDS), new DecelerateAnimation(200, 1.0, Direction.BACKWARDS)});
            }
            if (value instanceof BoolValue) {
                this.toggleAnimation.put((BoolValue)value, new Animation[]{new DecelerateAnimation(225, 1.0, Direction.BACKWARDS), new DecelerateAnimation(200, 1.0, Direction.BACKWARDS)});
            }
            if (!(value instanceof ListValue)) continue;
            ListValue listValue = (ListValue)value;
            this.modeSettingClick.put(listValue, false);
            this.modeSettingAnimMap.put(listValue, new Animation[]{new DecelerateAnimation(225, 1.0, Direction.BACKWARDS), new EaseInOutQuad(250, 1.0, Direction.BACKWARDS)});
            HashMap<String, DecelerateAnimation> hashMap = new HashMap<String, DecelerateAnimation>();
            for (String string : listValue.getValues()) {
                hashMap.put(string, new DecelerateAnimation(225, 1.0, Direction.BACKWARDS));
            }
            this.modesHoverAnimation.put(listValue, hashMap);
        }
    }

    @Override
    public void drawScreen(int n, int n2) {
        this.handle(n, n2, -1, GuiEvents.DRAW);
    }

    @Override
    public void mouseClicked(int n, int n2, int n3) {
        this.handle(n, n2, n3, GuiEvents.CLICK);
    }
}

