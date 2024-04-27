/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.client.Minecraft
 *  net.minecraft.util.ResourceLocation
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.ui.cape;

import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.ui.cape.ICape;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010!\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\u0002\n\u0000\b&\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\b\u0010\u001a\u001a\u00020\u001bH\u0016R\u0014\u0010\u0005\u001a\u00020\u00068VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0007\u0010\bR\u001a\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\nX\u0084\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u001a\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u000f0\nX\u0084\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\rR\u0014\u0010\u0002\u001a\u00020\u0003X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0014\u0010\u0013\u001a\u00020\u0003X\u0084\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0012R\u001a\u0010\u0015\u001a\u00020\u000bX\u0084\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u0017\"\u0004\b\u0018\u0010\u0019\u00a8\u0006\u001c"}, d2={"Lnet/ccbluex/liquidbounce/ui/cape/DynamicCape;", "Lnet/ccbluex/liquidbounce/ui/cape/ICape;", "name", "", "(Ljava/lang/String;)V", "cape", "Lnet/minecraft/util/ResourceLocation;", "getCape", "()Lnet/minecraft/util/ResourceLocation;", "delays", "", "", "getDelays", "()Ljava/util/List;", "frames", "Ljava/awt/image/BufferedImage;", "getFrames", "getName", "()Ljava/lang/String;", "path", "getPath", "playTime", "getPlayTime", "()I", "setPlayTime", "(I)V", "finalize", "", "LiKingSense"})
public abstract class DynamicCape
implements ICape {
    @NotNull
    private final List<BufferedImage> frames;
    @NotNull
    private final List<Integer> delays;
    private int playTime;
    @NotNull
    private final String path;
    @NotNull
    private final String name;

    @NotNull
    protected final List<BufferedImage> getFrames() {
        return this.frames;
    }

    @NotNull
    protected final List<Integer> getDelays() {
        return this.delays;
    }

    protected final int getPlayTime() {
        return this.playTime;
    }

    protected final void setPlayTime(int n) {
        this.playTime = n;
    }

    @NotNull
    protected final String getPath() {
        return this.path;
    }

    /*
     * WARNING - void declaration
     */
    @Override
    @NotNull
    public ResourceLocation getCape() {
        void i;
        long frameTime = System.currentTimeMillis() % (long)this.playTime;
        int frameId = 0;
        int n = 0;
        int n2 = ((Collection)this.delays).size();
        while (n < n2 && frameTime >= ((Number)this.delays.get((int)i)).longValue()) {
            frameId = i++;
        }
        return new ResourceLocation(this.path + frameId);
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public void finalize() {
        Minecraft mc = Minecraft.func_71410_x();
        int n = 0;
        int n2 = this.frames.size();
        while (n < n2) {
            void i;
            Minecraft minecraft = mc;
            Intrinsics.checkExpressionValueIsNotNull((Object)minecraft, (String)"mc");
            minecraft.func_110434_K().func_147645_c(new ResourceLocation(this.path + (int)i));
            ++i;
        }
    }

    @Override
    @NotNull
    public String getName() {
        return this.name;
    }

    /*
     * Exception decompiling
     */
    public DynamicCape(@NotNull String name) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl66 : INVOKEVIRTUAL - null : Stack underflow
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
}

