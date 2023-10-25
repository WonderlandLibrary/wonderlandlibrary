/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 */
package net.minecraft.crash;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.concurrent.Callable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReport;
import net.minecraft.util.BlockPos;

public class CrashReportCategory {
    private final CrashReport crashReport;
    private final String name;
    private final List<Entry> children = Lists.newArrayList();
    private StackTraceElement[] stackTrace = new StackTraceElement[0];

    public StackTraceElement[] getStackTrace() {
        return this.stackTrace;
    }

    public void addCrashSectionCallable(String string, Callable<String> callable) {
        try {
            this.addCrashSection(string, callable.call());
        }
        catch (Throwable throwable) {
            this.addCrashSectionThrowable(string, throwable);
        }
    }

    public static String getCoordinateInfo(BlockPos blockPos) {
        int n;
        int n2;
        int n3;
        int n4;
        int n5;
        int n6;
        int n7;
        int n8;
        int n9 = blockPos.getX();
        int n10 = blockPos.getY();
        int n11 = blockPos.getZ();
        StringBuilder stringBuilder = new StringBuilder();
        try {
            stringBuilder.append(String.format("World: (%d,%d,%d)", n9, n10, n11));
        }
        catch (Throwable throwable) {
            stringBuilder.append("(Error finding world loc)");
        }
        stringBuilder.append(", ");
        try {
            int n12 = n9 >> 4;
            n8 = n11 >> 4;
            n7 = n9 & 0xF;
            n6 = n10 >> 4;
            n5 = n11 & 0xF;
            n4 = n12 << 4;
            n3 = n8 << 4;
            n2 = (n12 + 1 << 4) - 1;
            n = (n8 + 1 << 4) - 1;
            stringBuilder.append(String.format("Chunk: (at %d,%d,%d in %d,%d; contains blocks %d,0,%d to %d,255,%d)", n7, n6, n5, n12, n8, n4, n3, n2, n));
        }
        catch (Throwable throwable) {
            stringBuilder.append("(Error finding chunk loc)");
        }
        stringBuilder.append(", ");
        try {
            int n13 = n9 >> 9;
            n8 = n11 >> 9;
            n7 = n13 << 5;
            n6 = n8 << 5;
            n5 = (n13 + 1 << 5) - 1;
            n4 = (n8 + 1 << 5) - 1;
            n3 = n13 << 9;
            n2 = n8 << 9;
            n = (n13 + 1 << 9) - 1;
            int n14 = (n8 + 1 << 9) - 1;
            stringBuilder.append(String.format("Region: (%d,%d; contains chunks %d,%d to %d,%d, blocks %d,0,%d to %d,255,%d)", n13, n8, n7, n6, n5, n4, n3, n2, n, n14));
        }
        catch (Throwable throwable) {
            stringBuilder.append("(Error finding world loc)");
        }
        return stringBuilder.toString();
    }

    public int getPrunedStackTrace(int n) {
        StackTraceElement[] stackTraceElementArray = Thread.currentThread().getStackTrace();
        if (stackTraceElementArray.length <= 0) {
            return 0;
        }
        this.stackTrace = new StackTraceElement[stackTraceElementArray.length - 3 - n];
        System.arraycopy(stackTraceElementArray, 3 + n, this.stackTrace, 0, this.stackTrace.length);
        return this.stackTrace.length;
    }

    public static void addBlockInfo(CrashReportCategory crashReportCategory, final BlockPos blockPos, final IBlockState iBlockState) {
        crashReportCategory.addCrashSectionCallable("Block", new Callable<String>(){

            @Override
            public String call() throws Exception {
                return iBlockState.toString();
            }
        });
        crashReportCategory.addCrashSectionCallable("Block location", new Callable<String>(){

            @Override
            public String call() throws Exception {
                return CrashReportCategory.getCoordinateInfo(blockPos);
            }
        });
    }

    public void addCrashSection(String string, Object object) {
        this.children.add(new Entry(string, object));
    }

    public static String getCoordinateInfo(double d, double d2, double d3) {
        return String.format("%.2f,%.2f,%.2f - %s", d, d2, d3, CrashReportCategory.getCoordinateInfo(new BlockPos(d, d2, d3)));
    }

    public static void addBlockInfo(CrashReportCategory crashReportCategory, final BlockPos blockPos, final Block block, final int n) {
        final int n2 = Block.getIdFromBlock(block);
        crashReportCategory.addCrashSectionCallable("Block type", new Callable<String>(){

            @Override
            public String call() throws Exception {
                try {
                    return String.format("ID #%d (%s // %s)", n2, block.getUnlocalizedName(), block.getClass().getCanonicalName());
                }
                catch (Throwable throwable) {
                    return "ID #" + n2;
                }
            }
        });
        crashReportCategory.addCrashSectionCallable("Block data value", new Callable<String>(){

            @Override
            public String call() throws Exception {
                if (n < 0) {
                    return "Unknown? (Got " + n + ")";
                }
                String string = String.format("%4s", Integer.toBinaryString(n)).replace(" ", "0");
                return String.format("%1$d / 0x%1$X / 0b%2$s", n, string);
            }
        });
        crashReportCategory.addCrashSectionCallable("Block location", new Callable<String>(){

            @Override
            public String call() throws Exception {
                return CrashReportCategory.getCoordinateInfo(blockPos);
            }
        });
    }

    public boolean firstTwoElementsOfStackTraceMatch(StackTraceElement stackTraceElement, StackTraceElement stackTraceElement2) {
        if (this.stackTrace.length != 0 && stackTraceElement != null) {
            StackTraceElement stackTraceElement3 = this.stackTrace[0];
            if (stackTraceElement3.isNativeMethod() == stackTraceElement.isNativeMethod() && stackTraceElement3.getClassName().equals(stackTraceElement.getClassName()) && stackTraceElement3.getFileName().equals(stackTraceElement.getFileName()) && stackTraceElement3.getMethodName().equals(stackTraceElement.getMethodName())) {
                if (stackTraceElement2 != null != this.stackTrace.length > 1) {
                    return false;
                }
                if (stackTraceElement2 != null && !this.stackTrace[1].equals(stackTraceElement2)) {
                    return false;
                }
                this.stackTrace[0] = stackTraceElement;
                return true;
            }
            return false;
        }
        return false;
    }

    public void addCrashSectionThrowable(String string, Throwable throwable) {
        this.addCrashSection(string, throwable);
    }

    public void appendToStringBuilder(StringBuilder stringBuilder) {
        stringBuilder.append("-- ").append(this.name).append(" --\n");
        stringBuilder.append("Details:");
        for (Entry object : this.children) {
            stringBuilder.append("\n\t");
            stringBuilder.append(object.getKey());
            stringBuilder.append(": ");
            stringBuilder.append(object.getValue());
        }
        if (this.stackTrace != null && this.stackTrace.length > 0) {
            stringBuilder.append("\nStacktrace:");
            StackTraceElement[] stackTraceElementArray = this.stackTrace;
            int n = this.stackTrace.length;
            int n2 = 0;
            while (n2 < n) {
                StackTraceElement stackTraceElement = stackTraceElementArray[n2];
                stringBuilder.append("\n\tat ");
                stringBuilder.append(stackTraceElement.toString());
                ++n2;
            }
        }
    }

    public CrashReportCategory(CrashReport crashReport, String string) {
        this.crashReport = crashReport;
        this.name = string;
    }

    public void trimStackTraceEntriesFromBottom(int n) {
        StackTraceElement[] stackTraceElementArray = new StackTraceElement[this.stackTrace.length - n];
        System.arraycopy(this.stackTrace, 0, stackTraceElementArray, 0, stackTraceElementArray.length);
        this.stackTrace = stackTraceElementArray;
    }

    static class Entry {
        private final String value;
        private final String key;

        public Entry(String string, Object object) {
            this.key = string;
            if (object == null) {
                this.value = "~~NULL~~";
            } else if (object instanceof Throwable) {
                Throwable throwable = (Throwable)object;
                this.value = "~~ERROR~~ " + throwable.getClass().getSimpleName() + ": " + throwable.getMessage();
            } else {
                this.value = object.toString();
            }
        }

        public String getValue() {
            return this.value;
        }

        public String getKey() {
            return this.key;
        }
    }
}

