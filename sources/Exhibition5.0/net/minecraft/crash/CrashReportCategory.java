// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.crash;

import net.minecraft.block.state.IBlockState;
import net.minecraft.block.Block;
import java.util.Iterator;
import java.util.concurrent.Callable;
import net.minecraft.util.BlockPos;
import com.google.common.collect.Lists;
import java.util.List;

public class CrashReportCategory
{
    private final CrashReport theCrashReport;
    private final String name;
    private final List children;
    private StackTraceElement[] stackTrace;
    private static final String __OBFID = "CL_00001409";
    
    public CrashReportCategory(final CrashReport p_i1353_1_, final String name) {
        this.children = Lists.newArrayList();
        this.stackTrace = new StackTraceElement[0];
        this.theCrashReport = p_i1353_1_;
        this.name = name;
    }
    
    public static String getCoordinateInfo(final double x, final double y, final double z) {
        return String.format("%.2f,%.2f,%.2f - %s", x, y, z, getCoordinateInfo(new BlockPos(x, y, z)));
    }
    
    public static String getCoordinateInfo(final BlockPos pos) {
        final int var1 = pos.getX();
        final int var2 = pos.getY();
        final int var3 = pos.getZ();
        final StringBuilder var4 = new StringBuilder();
        try {
            var4.append(String.format("World: (%d,%d,%d)", var1, var2, var3));
        }
        catch (Throwable var15) {
            var4.append("(Error finding world loc)");
        }
        var4.append(", ");
        try {
            final int var5 = var1 >> 4;
            final int var6 = var3 >> 4;
            final int var7 = var1 & 0xF;
            final int var8 = var2 >> 4;
            final int var9 = var3 & 0xF;
            final int var10 = var5 << 4;
            final int var11 = var6 << 4;
            final int var12 = (var5 + 1 << 4) - 1;
            final int var13 = (var6 + 1 << 4) - 1;
            var4.append(String.format("Chunk: (at %d,%d,%d in %d,%d; contains blocks %d,0,%d to %d,255,%d)", var7, var8, var9, var5, var6, var10, var11, var12, var13));
        }
        catch (Throwable var16) {
            var4.append("(Error finding chunk loc)");
        }
        var4.append(", ");
        try {
            final int var5 = var1 >> 9;
            final int var6 = var3 >> 9;
            final int var7 = var5 << 5;
            final int var8 = var6 << 5;
            final int var9 = (var5 + 1 << 5) - 1;
            final int var10 = (var6 + 1 << 5) - 1;
            final int var11 = var5 << 9;
            final int var12 = var6 << 9;
            final int var13 = (var5 + 1 << 9) - 1;
            final int var14 = (var6 + 1 << 9) - 1;
            var4.append(String.format("Region: (%d,%d; contains chunks %d,%d to %d,%d, blocks %d,0,%d to %d,255,%d)", var5, var6, var7, var8, var9, var10, var11, var12, var13, var14));
        }
        catch (Throwable var17) {
            var4.append("(Error finding world loc)");
        }
        return var4.toString();
    }
    
    public void addCrashSectionCallable(final String sectionName, final Callable callable) {
        try {
            this.addCrashSection(sectionName, callable.call());
        }
        catch (Throwable var4) {
            this.addCrashSectionThrowable(sectionName, var4);
        }
    }
    
    public void addCrashSection(final String sectionName, final Object value) {
        this.children.add(new Entry(sectionName, value));
    }
    
    public void addCrashSectionThrowable(final String sectionName, final Throwable throwable) {
        this.addCrashSection(sectionName, throwable);
    }
    
    public int getPrunedStackTrace(final int size) {
        final StackTraceElement[] var2 = Thread.currentThread().getStackTrace();
        if (var2.length <= 0) {
            return 0;
        }
        System.arraycopy(var2, 3 + size, this.stackTrace = new StackTraceElement[var2.length - 3 - size], 0, this.stackTrace.length);
        return this.stackTrace.length;
    }
    
    public boolean firstTwoElementsOfStackTraceMatch(final StackTraceElement s1, final StackTraceElement s2) {
        if (this.stackTrace.length == 0 || s1 == null) {
            return false;
        }
        final StackTraceElement var3 = this.stackTrace[0];
        if (var3.isNativeMethod() != s1.isNativeMethod() || !var3.getClassName().equals(s1.getClassName()) || !var3.getFileName().equals(s1.getFileName()) || !var3.getMethodName().equals(s1.getMethodName())) {
            return false;
        }
        if (s2 != null != this.stackTrace.length > 1) {
            return false;
        }
        if (s2 != null && !this.stackTrace[1].equals(s2)) {
            return false;
        }
        this.stackTrace[0] = s1;
        return true;
    }
    
    public void trimStackTraceEntriesFromBottom(final int amount) {
        final StackTraceElement[] var2 = new StackTraceElement[this.stackTrace.length - amount];
        System.arraycopy(this.stackTrace, 0, var2, 0, var2.length);
        this.stackTrace = var2;
    }
    
    public void appendToStringBuilder(final StringBuilder builder) {
        builder.append("-- ").append(this.name).append(" --\n");
        builder.append("Details:");
        for (final Entry var3 : this.children) {
            builder.append("\n\t");
            builder.append(var3.getKey());
            builder.append(": ");
            builder.append(var3.getValue());
        }
        if (this.stackTrace != null && this.stackTrace.length > 0) {
            builder.append("\nStacktrace:");
            for (final StackTraceElement var7 : this.stackTrace) {
                builder.append("\n\tat ");
                builder.append(var7.toString());
            }
        }
    }
    
    public StackTraceElement[] getStackTrace() {
        return this.stackTrace;
    }
    
    public static void addBlockInfo(final CrashReportCategory category, final BlockPos pos, final Block blockIn, final int blockData) {
        final int var4 = Block.getIdFromBlock(blockIn);
        category.addCrashSectionCallable("Block type", new Callable() {
            private static final String __OBFID = "CL_00001426";
            
            @Override
            public String call() {
                try {
                    return String.format("ID #%d (%s // %s)", var4, blockIn.getUnlocalizedName(), blockIn.getClass().getCanonicalName());
                }
                catch (Throwable var2) {
                    return "ID #" + var4;
                }
            }
        });
        category.addCrashSectionCallable("Block data value", new Callable() {
            private static final String __OBFID = "CL_00001441";
            
            @Override
            public String call() {
                if (blockData < 0) {
                    return "Unknown? (Got " + blockData + ")";
                }
                final String var1 = String.format("%4s", Integer.toBinaryString(blockData)).replace(" ", "0");
                return String.format("%1$d / 0x%1$X / 0b%2$s", blockData, var1);
            }
        });
        category.addCrashSectionCallable("Block location", new Callable() {
            private static final String __OBFID = "CL_00001465";
            
            @Override
            public String call() {
                return CrashReportCategory.getCoordinateInfo(pos);
            }
        });
    }
    
    public static void addBlockInfo(final CrashReportCategory category, final BlockPos pos, final IBlockState state) {
        category.addCrashSectionCallable("Block", new Callable() {
            private static final String __OBFID = "CL_00002617";
            
            public String func_175753_a() {
                return state.toString();
            }
            
            @Override
            public Object call() {
                return this.func_175753_a();
            }
        });
        category.addCrashSectionCallable("Block location", new Callable() {
            private static final String __OBFID = "CL_00002616";
            
            public String func_175751_a() {
                return CrashReportCategory.getCoordinateInfo(pos);
            }
            
            @Override
            public Object call() {
                return this.func_175751_a();
            }
        });
    }
    
    static class Entry
    {
        private final String key;
        private final String value;
        private static final String __OBFID = "CL_00001489";
        
        public Entry(final String key, final Object value) {
            this.key = key;
            if (value == null) {
                this.value = "~~NULL~~";
            }
            else if (value instanceof Throwable) {
                final Throwable var3 = (Throwable)value;
                this.value = "~~ERROR~~ " + var3.getClass().getSimpleName() + ": " + var3.getMessage();
            }
            else {
                this.value = value.toString();
            }
        }
        
        public String getKey() {
            return this.key;
        }
        
        public String getValue() {
            return this.value;
        }
    }
}
