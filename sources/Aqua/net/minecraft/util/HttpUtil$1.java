package net.minecraft.util;

import java.io.File;
import java.net.Proxy;
import java.util.Map;
import net.minecraft.util.IProgressUpdate;

static final class HttpUtil.1
implements Runnable {
    final /* synthetic */ IProgressUpdate val$p_180192_4_;
    final /* synthetic */ String val$packUrl;
    final /* synthetic */ Proxy val$p_180192_5_;
    final /* synthetic */ Map val$p_180192_2_;
    final /* synthetic */ File val$saveFile;
    final /* synthetic */ int val$maxSize;

    HttpUtil.1(IProgressUpdate iProgressUpdate, String string, Proxy proxy, Map map, File file, int n) {
        this.val$p_180192_4_ = iProgressUpdate;
        this.val$packUrl = string;
        this.val$p_180192_5_ = proxy;
        this.val$p_180192_2_ = map;
        this.val$saveFile = file;
        this.val$maxSize = n;
    }

    /*
     * Exception decompiling
     */
    public void run() {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [1[TRYBLOCK]], but top level block is 10[WHILELOOP]
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
         *     at org.benf.cfr.reader.Driver.doClass(Driver.java:84)
         *     at org.benf.cfr.reader.Main.doClass(Main.java:18)
         *     at org.benf.cfr.reader.PluginRunner.getDecompilationFor(PluginRunner.java:115)
         *     at me.exeos.decompiler.CFR.decompile(CFR.java:152)
         *     at me.exeos.decompiler.Decompiler.decompile(Decompiler.java:47)
         *     at me.exeos.decompiler.Decompiler.getDecompiled(Decompiler.java:34)
         *     at me.exeos.JTP.lambda$initialize$1(JTP.java:56)
         *     at java.base/java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:183)
         *     at java.base/java.util.stream.ReferencePipeline$2$1.accept(ReferencePipeline.java:179)
         *     at java.base/java.util.HashMap$ValueSpliterator.forEachRemaining(HashMap.java:1779)
         *     at java.base/java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:509)
         *     at java.base/java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:499)
         *     at java.base/java.util.stream.ForEachOps$ForEachOp.evaluateSequential(ForEachOps.java:150)
         *     at java.base/java.util.stream.ForEachOps$ForEachOp$OfRef.evaluateSequential(ForEachOps.java:173)
         *     at java.base/java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
         *     at java.base/java.util.stream.ReferencePipeline.forEach(ReferencePipeline.java:596)
         *     at me.exeos.JTP.initialize(JTP.java:45)
         *     at me.exeos.main.Main.main(Main.java:12)
         */
        throw new IllegalStateException("Decompilation failed");
    }
}
