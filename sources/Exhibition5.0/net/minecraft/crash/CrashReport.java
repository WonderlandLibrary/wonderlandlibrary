// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.crash;

import org.apache.logging.log4j.LogManager;
import net.minecraft.util.ReportedException;
import java.io.FileWriter;
import java.util.Date;
import java.text.SimpleDateFormat;
import net.minecraft.optifine.CrashReporter;
import org.apache.commons.io.IOUtils;
import java.io.Writer;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.apache.commons.lang3.ArrayUtils;
import net.minecraft.optifine.Reflector;
import net.minecraft.world.gen.layer.IntCache;
import java.util.Iterator;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ManagementFactory;
import java.util.concurrent.Callable;
import com.google.common.collect.Lists;
import java.io.File;
import java.util.List;
import org.apache.logging.log4j.Logger;

public class CrashReport
{
    private static final Logger logger;
    private final String description;
    private final Throwable cause;
    private final CrashReportCategory theReportCategory;
    private final List crashReportSections;
    private File crashReportFile;
    private boolean field_85059_f;
    private StackTraceElement[] stacktrace;
    private static final String __OBFID = "CL_00000990";
    private boolean reported;
    
    public CrashReport(final String descriptionIn, final Throwable causeThrowable) {
        this.theReportCategory = new CrashReportCategory(this, "System Details");
        this.crashReportSections = Lists.newArrayList();
        this.field_85059_f = true;
        this.stacktrace = new StackTraceElement[0];
        this.reported = false;
        this.description = descriptionIn;
        this.cause = causeThrowable;
        this.populateEnvironment();
    }
    
    private void populateEnvironment() {
        this.theReportCategory.addCrashSectionCallable("Minecraft Version", new Callable() {
            private static final String __OBFID = "CL_00001197";
            
            @Override
            public String call() {
                return "1.8";
            }
        });
        this.theReportCategory.addCrashSectionCallable("Operating System", new Callable() {
            private static final String __OBFID = "CL_00001222";
            
            @Override
            public String call() {
                return System.getProperty("os.name") + " (" + System.getProperty("os.arch") + ") version " + System.getProperty("os.version");
            }
        });
        this.theReportCategory.addCrashSectionCallable("Java Version", new Callable() {
            private static final String __OBFID = "CL_00001248";
            
            @Override
            public String call() {
                return System.getProperty("java.version") + ", " + System.getProperty("java.vendor");
            }
        });
        this.theReportCategory.addCrashSectionCallable("Java VM Version", new Callable() {
            private static final String __OBFID = "CL_00001275";
            
            @Override
            public String call() {
                return System.getProperty("java.vm.name") + " (" + System.getProperty("java.vm.info") + "), " + System.getProperty("java.vm.vendor");
            }
        });
        this.theReportCategory.addCrashSectionCallable("Memory", new Callable() {
            private static final String __OBFID = "CL_00001302";
            
            @Override
            public String call() {
                final Runtime var1 = Runtime.getRuntime();
                final long var2 = var1.maxMemory();
                final long var3 = var1.totalMemory();
                final long var4 = var1.freeMemory();
                final long var5 = var2 / 1024L / 1024L;
                final long var6 = var3 / 1024L / 1024L;
                final long var7 = var4 / 1024L / 1024L;
                return var4 + " bytes (" + var7 + " MB) / " + var3 + " bytes (" + var6 + " MB) up to " + var2 + " bytes (" + var5 + " MB)";
            }
        });
        this.theReportCategory.addCrashSectionCallable("JVM Flags", new Callable() {
            private static final String __OBFID = "CL_00001329";
            
            @Override
            public String call() {
                final RuntimeMXBean var1 = ManagementFactory.getRuntimeMXBean();
                final List var2 = var1.getInputArguments();
                int var3 = 0;
                final StringBuilder var4 = new StringBuilder();
                for (final String var6 : var2) {
                    if (var6.startsWith("-X")) {
                        if (var3++ > 0) {
                            var4.append(" ");
                        }
                        var4.append(var6);
                    }
                }
                return String.format("%d total; %s", var3, var4.toString());
            }
        });
        this.theReportCategory.addCrashSectionCallable("IntCache", new Callable() {
            private static final String __OBFID = "CL_00001355";
            
            @Override
            public String call() {
                return IntCache.getCacheSizes();
            }
        });
        if (Reflector.FMLCommonHandler_enhanceCrashReport.exists()) {
            final Object instance = Reflector.call(Reflector.FMLCommonHandler_instance, new Object[0]);
            Reflector.callString(instance, Reflector.FMLCommonHandler_enhanceCrashReport, this, this.theReportCategory);
        }
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public Throwable getCrashCause() {
        return this.cause;
    }
    
    public void getSectionsInStringBuilder(final StringBuilder builder) {
        if ((this.stacktrace == null || this.stacktrace.length <= 0) && this.crashReportSections.size() > 0) {
            this.stacktrace = (StackTraceElement[])ArrayUtils.subarray((Object[])this.crashReportSections.get(0).getStackTrace(), 0, 1);
        }
        if (this.stacktrace != null && this.stacktrace.length > 0) {
            builder.append("-- Head --\n");
            builder.append("Stacktrace:\n");
            for (final StackTraceElement var9 : this.stacktrace) {
                builder.append("\t").append("at ").append(var9.toString());
                builder.append("\n");
            }
            builder.append("\n");
        }
        for (final CrashReportCategory var11 : this.crashReportSections) {
            var11.appendToStringBuilder(builder);
            builder.append("\n\n");
        }
        this.theReportCategory.appendToStringBuilder(builder);
    }
    
    public String getCauseStackTraceOrString() {
        StringWriter var1 = null;
        PrintWriter var2 = null;
        Object var3 = this.cause;
        if (((Throwable)var3).getMessage() == null) {
            if (var3 instanceof NullPointerException) {
                var3 = new NullPointerException(this.description);
            }
            else if (var3 instanceof StackOverflowError) {
                var3 = new StackOverflowError(this.description);
            }
            else if (var3 instanceof OutOfMemoryError) {
                var3 = new OutOfMemoryError(this.description);
            }
            ((Throwable)var3).setStackTrace(this.cause.getStackTrace());
        }
        String var4 = ((Throwable)var3).toString();
        try {
            var1 = new StringWriter();
            var2 = new PrintWriter(var1);
            ((Throwable)var3).printStackTrace(var2);
            var4 = var1.toString();
        }
        finally {
            IOUtils.closeQuietly((Writer)var1);
            IOUtils.closeQuietly((Writer)var2);
        }
        return var4;
    }
    
    public String getCompleteReport() {
        if (!this.reported) {
            this.reported = true;
            CrashReporter.onCrashReport(this);
        }
        final StringBuilder var1 = new StringBuilder();
        var1.append("---- Minecraft Crash Report ----\n");
        var1.append("// ");
        var1.append(getWittyComment());
        var1.append("\n\n");
        var1.append("Time: ");
        var1.append(new SimpleDateFormat().format(new Date()));
        var1.append("\n");
        var1.append("Description: ");
        var1.append(this.description);
        var1.append("\n\n");
        var1.append(this.getCauseStackTraceOrString());
        var1.append("\n\nA detailed walkthrough of the error, its code path and all known details is as follows:\n");
        for (int var2 = 0; var2 < 87; ++var2) {
            var1.append("-");
        }
        var1.append("\n\n");
        this.getSectionsInStringBuilder(var1);
        return var1.toString();
    }
    
    public File getFile() {
        return this.crashReportFile;
    }
    
    public boolean saveToFile(final File toFile) {
        if (this.crashReportFile != null) {
            return false;
        }
        if (toFile.getParentFile() != null) {
            toFile.getParentFile().mkdirs();
        }
        try {
            final FileWriter var3 = new FileWriter(toFile);
            var3.write(this.getCompleteReport());
            var3.close();
            this.crashReportFile = toFile;
            return true;
        }
        catch (Throwable var4) {
            CrashReport.logger.error("Could not save crash report to " + toFile, var4);
            return false;
        }
    }
    
    public CrashReportCategory getCategory() {
        return this.theReportCategory;
    }
    
    public CrashReportCategory makeCategory(final String name) {
        return this.makeCategoryDepth(name, 1);
    }
    
    public CrashReportCategory makeCategoryDepth(final String categoryName, final int stacktraceLength) {
        final CrashReportCategory var3 = new CrashReportCategory(this, categoryName);
        if (this.field_85059_f) {
            final int var4 = var3.getPrunedStackTrace(stacktraceLength);
            final StackTraceElement[] var5 = this.cause.getStackTrace();
            StackTraceElement var6 = null;
            StackTraceElement var7 = null;
            final int var8 = var5.length - var4;
            if (var8 < 0) {
                System.out.println("Negative index in crash report handler (" + var5.length + "/" + var4 + ")");
            }
            if (var5 != null && 0 <= var8 && var8 < var5.length) {
                var6 = var5[var8];
                if (var5.length + 1 - var4 < var5.length) {
                    var7 = var5[var5.length + 1 - var4];
                }
            }
            this.field_85059_f = var3.firstTwoElementsOfStackTraceMatch(var6, var7);
            if (var4 > 0 && !this.crashReportSections.isEmpty()) {
                final CrashReportCategory var9 = this.crashReportSections.get(this.crashReportSections.size() - 1);
                var9.trimStackTraceEntriesFromBottom(var4);
            }
            else if (var5 != null && var5.length >= var4 && 0 <= var8 && var8 < var5.length) {
                System.arraycopy(var5, 0, this.stacktrace = new StackTraceElement[var8], 0, this.stacktrace.length);
            }
            else {
                this.field_85059_f = false;
            }
        }
        this.crashReportSections.add(var3);
        return var3;
    }
    
    private static String getWittyComment() {
        final String[] var0 = { "Who set us up the TNT?", "Everything's going to plan. No, really, that was supposed to happen.", "Uh... Did I do that?", "Oops.", "Why did you do that?", "I feel sad now :(", "My bad.", "I'm sorry, Dave.", "I let you down. Sorry :(", "On the bright side, I bought you a teddy bear!", "Daisy, daisy...", "Oh - I know what I did wrong!", "Hey, that tickles! Hehehe!", "I blame Dinnerbone.", "You should try our sister game, Minceraft!", "Don't be sad. I'll do better next time, I promise!", "Don't be sad, have a hug! <3", "I just don't know what went wrong :(", "Shall we play a game?", "Quite honestly, I wouldn't worry myself about that.", "I bet Cylons wouldn't have this problem.", "Sorry :(", "Surprise! Haha. Well, this is awkward.", "Would you like a cupcake?", "Hi. I'm Minecraft, and I'm a crashaholic.", "Ooh. Shiny.", "This doesn't make any sense!", "Why is it breaking :(", "Don't do that.", "Ouch. That hurt :(", "You're mean.", "This is a token for 1 free hug. Redeem at your nearest Mojangsta: [~~HUG~~]", "There are four lights!", "But it works on my machine." };
        try {
            return var0[(int)(System.nanoTime() % var0.length)];
        }
        catch (Throwable var2) {
            return "Witty comment unavailable :(";
        }
    }
    
    public static CrashReport makeCrashReport(final Throwable causeIn, final String descriptionIn) {
        CrashReport var2;
        if (causeIn instanceof ReportedException) {
            var2 = ((ReportedException)causeIn).getCrashReport();
        }
        else {
            var2 = new CrashReport(descriptionIn, causeIn);
        }
        return var2;
    }
    
    static {
        logger = LogManager.getLogger();
    }
}
