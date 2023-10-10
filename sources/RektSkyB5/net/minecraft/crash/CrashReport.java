/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.crash;

import com.google.common.collect.Lists;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import net.minecraft.crash.CrashReport3;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import net.minecraft.world.gen.layer.IntCache;
import optfine.CrashReporter;
import optfine.Reflector;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tk.rektsky.Client;

public class CrashReport {
    private static final Logger logger = LogManager.getLogger();
    private final String description;
    private final Throwable cause;
    private final CrashReportCategory theReportCategory = new CrashReportCategory(this, "System Details");
    private final List crashReportSections = Lists.newArrayList();
    private File crashReportFile;
    private boolean field_85059_f = true;
    private StackTraceElement[] stacktrace = new StackTraceElement[0];
    private static final String __OBFID = "CL_00000990";
    private boolean reported = false;

    public CrashReport(String descriptionIn, Throwable causeThrowable) {
        this.description = descriptionIn;
        this.cause = causeThrowable;
        this.populateEnvironment();
    }

    private void populateEnvironment() {
        try {
            this.theReportCategory.addCrashSectionCallable("Minecraft Version", new Callable(){
                private static final String __OBFID = "CL_00001197";

                public String call() {
                    return "1.8.8";
                }
            });
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            this.theReportCategory.addCrashSectionCallable("Operating System", new Callable(){
                private static final String __OBFID = "CL_00001222";

                public String call() {
                    return System.getProperty("os.name") + " (" + System.getProperty("os.arch") + ") version " + System.getProperty("os.version");
                }
            });
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            this.theReportCategory.addCrashSectionCallable("CPU", new CrashReport3(this));
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            this.theReportCategory.addCrashSectionCallable("Java Version", new Callable(){
                private static final String __OBFID = "CL_00001248";

                public String call() {
                    return System.getProperty("java.version") + ", " + System.getProperty("java.vendor");
                }
            });
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            this.theReportCategory.addCrashSectionCallable("Java VM Version", new Callable(){
                private static final String __OBFID = "CL_00001275";

                public String call() {
                    return System.getProperty("java.vm.name") + " (" + System.getProperty("java.vm.info") + "), " + System.getProperty("java.vm.vendor");
                }
            });
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            this.theReportCategory.addCrashSectionCallable("Memory", new Callable(){
                private static final String __OBFID = "CL_00001302";

                public String call() {
                    Runtime runtime = Runtime.getRuntime();
                    long i2 = runtime.maxMemory();
                    long j2 = runtime.totalMemory();
                    long k2 = runtime.freeMemory();
                    long l2 = i2 / 1024L / 1024L;
                    long i1 = j2 / 1024L / 1024L;
                    long j1 = k2 / 1024L / 1024L;
                    return k2 + " bytes (" + j1 + " MB) / " + j2 + " bytes (" + i1 + " MB) up to " + i2 + " bytes (" + l2 + " MB)";
                }
            });
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            this.theReportCategory.addCrashSectionCallable("JVM Flags", new Callable(){
                private static final String __OBFID = "CL_00001329";

                public String call() throws Exception {
                    RuntimeMXBean runtimemxbean = ManagementFactory.getRuntimeMXBean();
                    List<String> list = runtimemxbean.getInputArguments();
                    int i2 = 0;
                    StringBuilder stringbuilder = new StringBuilder();
                    for (String s2 : list) {
                        if (!s2.startsWith("-X")) continue;
                        if (i2++ > 0) {
                            stringbuilder.append(" ");
                        }
                        stringbuilder.append((Object)s2);
                    }
                    return String.format("%d total; %s", i2, stringbuilder.toString());
                }
            });
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            this.theReportCategory.addCrashSectionCallable("IntCache", new Callable(){
                private static final String __OBFID = "CL_00001355";

                public Object call() throws Exception {
                    return IntCache.getCacheSizes();
                }
            });
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            if (Reflector.FMLCommonHandler_enhanceCrashReport.exists()) {
                Object object = Reflector.call(Reflector.FMLCommonHandler_instance, new Object[0]);
                Reflector.callString(object, Reflector.FMLCommonHandler_enhanceCrashReport, this, this.theReportCategory);
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getDescription() {
        return this.description;
    }

    public Throwable getCrashCause() {
        return this.cause;
    }

    public void getSectionsInStringBuilder(StringBuilder builder) {
        if ((this.stacktrace == null || this.stacktrace.length <= 0) && this.crashReportSections.size() > 0) {
            this.stacktrace = ArrayUtils.subarray(((CrashReportCategory)this.crashReportSections.get(0)).getStackTrace(), 0, 1);
        }
        if (this.stacktrace != null && this.stacktrace.length > 0) {
            builder.append("-- Head --\n");
            builder.append("Stacktrace:\n");
            for (StackTraceElement stacktraceelement : this.stacktrace) {
                builder.append("\t").append("at ").append(stacktraceelement.toString());
                builder.append("\n");
            }
            builder.append("\n");
        }
        for (Object crashreportcategory : this.crashReportSections) {
            ((CrashReportCategory)crashreportcategory).appendToStringBuilder(builder);
            builder.append("\n\n");
        }
        this.theReportCategory.appendToStringBuilder(builder);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public String getCauseStackTraceOrString() {
        StringWriter stringwriter = null;
        PrintWriter printwriter = null;
        Throwable object = this.cause;
        if (object.getMessage() == null) {
            if (object instanceof NullPointerException) {
                object = new NullPointerException(this.description);
            } else if (object instanceof StackOverflowError) {
                object = new StackOverflowError(this.description);
            } else if (object instanceof OutOfMemoryError) {
                object = new OutOfMemoryError(this.description);
            }
            object.setStackTrace(this.cause.getStackTrace());
        }
        String s2 = object.toString();
        try {
            stringwriter = new StringWriter();
            printwriter = new PrintWriter(stringwriter);
            object.printStackTrace(printwriter);
            s2 = stringwriter.toString();
        }
        catch (Throwable throwable) {
            IOUtils.closeQuietly(stringwriter);
            IOUtils.closeQuietly(printwriter);
            throw throwable;
        }
        IOUtils.closeQuietly(stringwriter);
        IOUtils.closeQuietly(printwriter);
        return s2;
    }

    public String getCompleteReport() {
        if (!this.reported) {
            this.reported = true;
            CrashReporter.onCrashReport(this);
        }
        StringBuilder stringbuilder = new StringBuilder();
        stringbuilder.append("---- Minecraft Crash Report ----\n");
        stringbuilder.append("// ");
        stringbuilder.append(CrashReport.getWittyComment());
        stringbuilder.append("\n\n");
        stringbuilder.append("Time: ");
        stringbuilder.append(new SimpleDateFormat().format(new Date()));
        stringbuilder.append("\n");
        stringbuilder.append("Description: ");
        stringbuilder.append(this.description);
        stringbuilder.append("\n\n");
        stringbuilder.append(this.getCauseStackTraceOrString());
        stringbuilder.append("\n\nA detailed walkthrough of the error, its code path and all known details is as follows:\n");
        for (int i2 = 0; i2 < 87; ++i2) {
            stringbuilder.append("-");
        }
        stringbuilder.append("\n\n");
        this.getSectionsInStringBuilder(stringbuilder);
        return stringbuilder.toString();
    }

    public File getFile() {
        return this.crashReportFile;
    }

    public boolean saveToFile(File toFile) {
        if (this.crashReportFile != null) {
            return false;
        }
        if (toFile.getParentFile() != null) {
            toFile.getParentFile().mkdirs();
        }
        try {
            FileWriter filewriter = new FileWriter(toFile);
            filewriter.write(this.getCompleteReport());
            filewriter.close();
            this.crashReportFile = toFile;
            return true;
        }
        catch (Throwable throwable) {
            logger.error("Could not save crash report to " + toFile, throwable);
            return false;
        }
    }

    public CrashReportCategory getCategory() {
        return this.theReportCategory;
    }

    public CrashReportCategory makeCategory(String name) {
        if (Client.class.getName().equals("tk.rektsky.Client")) {
            return this.makeCategoryDepth(name, 1);
        }
        return new CrashReportCategory(new CrashReport(name, null), name);
    }

    public CrashReportCategory makeCategoryDepth(String categoryName, int stacktraceLength) {
        CrashReportCategory crashreportcategory = new CrashReportCategory(this, categoryName);
        if (this.field_85059_f) {
            int i2 = crashreportcategory.getPrunedStackTrace(stacktraceLength);
            StackTraceElement[] astacktraceelement = this.cause.getStackTrace();
            StackTraceElement stacktraceelement = null;
            StackTraceElement stacktraceelement1 = null;
            int j2 = astacktraceelement.length - i2;
            if (j2 < 0) {
                System.out.println("Negative index in crash report handler (" + astacktraceelement.length + "/" + i2 + ")");
            }
            if (astacktraceelement != null && 0 <= j2 && j2 < astacktraceelement.length) {
                stacktraceelement = astacktraceelement[j2];
                if (astacktraceelement.length + 1 - i2 < astacktraceelement.length) {
                    stacktraceelement1 = astacktraceelement[astacktraceelement.length + 1 - i2];
                }
            }
            this.field_85059_f = crashreportcategory.firstTwoElementsOfStackTraceMatch(stacktraceelement, stacktraceelement1);
            if (i2 > 0 && !this.crashReportSections.isEmpty()) {
                CrashReportCategory crashreportcategory1 = (CrashReportCategory)this.crashReportSections.get(this.crashReportSections.size() - 1);
                crashreportcategory1.trimStackTraceEntriesFromBottom(i2);
            } else if (astacktraceelement != null && astacktraceelement.length >= i2 && 0 <= j2 && j2 < astacktraceelement.length) {
                this.stacktrace = new StackTraceElement[j2];
                System.arraycopy(astacktraceelement, 0, this.stacktrace, 0, this.stacktrace.length);
            } else {
                this.field_85059_f = false;
            }
        }
        this.crashReportSections.add(crashreportcategory);
        return crashreportcategory;
    }

    private static String getWittyComment() {
        String[] astring = new String[]{"Who set us up the TNT?", "Everything's going to plan. No, really, that was supposed to happen.", "Uh... Did I do that?", "Oops.", "Why did you do that?", "I feel sad now :(", "My bad.", "I'm sorry, Dave.", "I let you down. Sorry :(", "On the bright side, I bought you a teddy bear!", "Daisy, daisy...", "Oh - I know what I did wrong!", "Hey, that tickles! Hehehe!", "I blame Dinnerbone.", "You should try our sister game, Minceraft!", "Don't be sad. I'll do better next time, I promise!", "Don't be sad, have a hug! <3", "I just don't know what went wrong :(", "Shall we play a game?", "Quite honestly, I wouldn't worry myself about that.", "I bet Cylons wouldn't have this problem.", "Sorry :(", "Surprise! Haha. Well, this is awkward.", "Would you like a cupcake?", "Hi. I'm Minecraft, and I'm a crashaholic.", "Ooh. Shiny.", "This doesn't make any sense!", "Why is it breaking :(", "Don't do that.", "Ouch. That hurt :(", "You're mean.", "This is a token for 1 free hug. Redeem at your nearest Mojangsta: [~~HUG~~]", "There are four lights!", "But it works on my machine."};
        try {
            return astring[(int)(System.nanoTime() % (long)astring.length)];
        }
        catch (Throwable var2) {
            return "Witty comment unavailable :(";
        }
    }

    public static CrashReport makeCrashReport(Throwable causeIn, String descriptionIn) {
        causeIn.printStackTrace();
        CrashReport crashreport = causeIn instanceof ReportedException ? ((ReportedException)causeIn).getCrashReport() : new CrashReport(descriptionIn, causeIn);
        return crashreport;
    }
}

