/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.features.module.modules.combat;

import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.item.ArmorComparator;
import net.ccbluex.liquidbounce.utils.item.ArmorPiece;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.IntegerValue;

@ModuleInfo(name="AutoArmor", description="Automatically equips the best armor in your inventory.", category=ModuleCategory.COMBAT)
public class AutoArmor
extends Module {
    private final IntegerValue minDelayValue = new IntegerValue(this, "MinDelay", 100, 0, 400){
        final AutoArmor this$0;
        {
            this.this$0 = autoArmor;
            super(string, n, n2, n3);
        }

        protected void onChanged(Integer n, Integer n2) {
            int n3 = (Integer)AutoArmor.access$000(this.this$0).get();
            if (n3 < n2) {
                this.set((Object)n3);
            }
        }

        @Override
        protected void onChanged(Object object, Object object2) {
            this.onChanged((Integer)object, (Integer)object2);
        }
    };
    public static final ArmorComparator ARMOR_COMPARATOR = new ArmorComparator();
    private final BoolValue invOpenValue;
    private final IntegerValue maxDelayValue = new IntegerValue(this, "MaxDelay", 200, 0, 400){
        final AutoArmor this$0;
        {
            this.this$0 = autoArmor;
            super(string, n, n2, n3);
        }

        protected void onChanged(Integer n, Integer n2) {
            int n3 = (Integer)AutoArmor.access$100(this.this$0).get();
            if (n3 > n2) {
                this.set((Object)n3);
            }
        }

        @Override
        protected void onChanged(Object object, Object object2) {
            this.onChanged((Integer)object, (Integer)object2);
        }
    };
    private boolean locked = false;
    private final BoolValue noMoveValue;
    private final BoolValue simulateInventory;
    private final IntegerValue itemDelayValue;
    private long delay;
    private final BoolValue hotbarValue;

    static IntegerValue access$000(AutoArmor autoArmor) {
        return autoArmor.maxDelayValue;
    }

    /*
     * Exception decompiling
     */
    @EventTarget
    public void onRender3D(Render3DEvent var1) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Invalid stack depths @ lbl132 : IINC - null : trying to set 2 previously set to 0
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

    private static ArmorPiece lambda$onRender3D$1(int n) {
        return new ArmorPiece(mc.getThePlayer().getInventory().getStackInSlot(n), n);
    }

    public AutoArmor() {
        this.invOpenValue = new BoolValue("InvOpen", false);
        this.simulateInventory = new BoolValue("SimulateInventory", true);
        this.noMoveValue = new BoolValue("NoMove", false);
        this.itemDelayValue = new IntegerValue("ItemDelay", 0, 0, 5000);
        this.hotbarValue = new BoolValue("Hotbar", true);
    }

    private boolean lambda$onRender3D$0(int n) {
        IItemStack iItemStack = mc.getThePlayer().getInventory().getStackInSlot(n);
        return iItemStack != null && classProvider.isItemArmor(iItemStack.getItem()) && (n < 9 || System.currentTimeMillis() - iItemStack.getItemDelay() >= (long)((Integer)this.itemDelayValue.get()).intValue());
    }

    public boolean isLocked() {
        return !this.getState() || this.locked;
    }

    static IntegerValue access$100(AutoArmor autoArmor) {
        return autoArmor.minDelayValue;
    }
}

