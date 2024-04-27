/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.utils;

public class FastMathLiteralArrays {
    private static final double[] EXP_INT_A;
    private static final double[] EXP_INT_B;
    private static final double[] EXP_FRAC_A;
    private static final double[] EXP_FRAC_B;
    private static final double[][] LN_MANT;

    private FastMathLiteralArrays() {
    }

    public static double[] loadExpIntA() {
        return (double[])EXP_INT_A.clone();
    }

    public static double[] loadExpIntB() {
        return (double[])EXP_INT_B.clone();
    }

    public static double[] loadExpFracA() {
        return (double[])EXP_FRAC_A.clone();
    }

    public static double[] loadExpFracB() {
        return (double[])EXP_FRAC_B.clone();
    }

    public static double[][] loadLnMant() {
        return (double[][])LN_MANT.clone();
    }

    /*
     * Exception decompiling
     */
    static {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Underrun type stack
         *     at org.benf.cfr.reader.bytecode.analysis.stack.StackSim.getEntry(StackSim.java:35)
         *     at org.benf.cfr.reader.bytecode.opcode.OperationFactoryDefault.getStackTypes(OperationFactoryDefault.java:51)
         *     at org.benf.cfr.reader.bytecode.opcode.OperationFactoryDup.getStackDelta(OperationFactoryDup.java:18)
         *     at org.benf.cfr.reader.bytecode.opcode.JVMInstr.getStackDelta(JVMInstr.java:315)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:199)
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

