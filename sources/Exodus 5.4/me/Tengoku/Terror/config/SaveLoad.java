/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.config;

import de.Hero.settings.Setting;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.module.Module;
import net.minecraft.client.Minecraft;

public class SaveLoad {
    private File dir;
    private File dataFile;

    public SaveLoad() {
        this.dir = new File(Minecraft.getMinecraft().mcDataDir, "Exodus");
        if (!this.dir.exists()) {
            this.dir.mkdir();
        }
        this.dataFile = new File(this.dir, "config.txt");
        if (!this.dataFile.exists()) {
            try {
                this.dataFile.createNewFile();
            }
            catch (IOException iOException) {
                iOException.printStackTrace();
            }
        }
        this.load();
    }

    /*
     * Exception decompiling
     */
    public void load() {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [2[WHILELOOP]], but top level block is 0[TRYBLOCK]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
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

    public void save() {
        ArrayList<String> arrayList = new ArrayList<String>();
        for (Module module : Exodus.INSTANCE.moduleManager.getModules()) {
            arrayList.add("MOD:" + module.getName() + ":" + module.isToggled() + ":" + module.getKey());
        }
        for (Setting setting : Exodus.INSTANCE.settingsManager.getSettings()) {
            if (setting.isCheck()) {
                arrayList.add("SET:" + setting.getName() + ":" + setting.getParentMod().getName() + ":" + setting.getValBoolean());
            }
            if (setting.isCombo()) {
                arrayList.add("SET:" + setting.getName() + ":" + setting.getParentMod().getName() + ":" + setting.getValString());
            }
            if (!setting.isSlider()) continue;
            arrayList.add("SET:" + setting.getName() + ":" + setting.getParentMod().getName() + ":" + setting.getValDouble());
        }
        PrintWriter printWriter = new PrintWriter(this.dataFile);
        for (String string : arrayList) {
            printWriter.println(string);
        }
        try {
            printWriter.close();
        }
        catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }
    }
}

