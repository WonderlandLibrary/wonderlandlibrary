/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  org.apache.commons.io.IOUtils
 *  org.apache.commons.lang3.ArrayUtils
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.crash;

import com.google.common.collect.Lists;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import net.minecraft.world.gen.layer.IntCache;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CrashReport {
    private final List<CrashReportCategory> crashReportSections;
    private final CrashReportCategory theReportCategory = new CrashReportCategory(this, "System Details");
    private final String description;
    private boolean field_85059_f = true;
    private StackTraceElement[] stacktrace;
    private static final Logger logger = LogManager.getLogger();
    private final Throwable cause;
    private File crashReportFile;

    public CrashReportCategory getCategory() {
        return this.theReportCategory;
    }

    public CrashReport(String string, Throwable throwable) {
        this.crashReportSections = Lists.newArrayList();
        this.stacktrace = new StackTraceElement[0];
        this.description = string;
        this.cause = throwable;
        this.populateEnvironment();
    }

    public String getDescription() {
        return this.description;
    }

    public String getCompleteReport() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("---- Minecraft Crash Report ----\n");
        stringBuilder.append("// ");
        stringBuilder.append(CrashReport.getWittyComment());
        stringBuilder.append("\n\n");
        stringBuilder.append("Time: ");
        stringBuilder.append(new SimpleDateFormat().format(new Date()));
        stringBuilder.append("\n");
        stringBuilder.append("Description: ");
        stringBuilder.append(this.description);
        stringBuilder.append("\n\n");
        stringBuilder.append(this.getCauseStackTraceOrString());
        stringBuilder.append("\n\nA detailed walkthrough of the error, its code path and all known details is as follows:\n");
        int n = 0;
        while (n < 87) {
            stringBuilder.append("-");
            ++n;
        }
        stringBuilder.append("\n\n");
        this.getSectionsInStringBuilder(stringBuilder);
        return stringBuilder.toString();
    }

    private static String getWittyComment() {
        String[] stringArray = new String[]{"Who set us up the TNT?", "Everything's going to plan. No, really, that was supposed to happen.", "Uh... Did I do that?", "Oops.", "Why did you do that?", "I feel sad now :(", "My bad.", "I'm sorry, Dave.", "I let you down. Sorry :(", "On the bright side, I bought you a teddy bear!", "Daisy, daisy...", "Oh - I know what I did wrong!", "Hey, that tickles! Hehehe!", "I blame Dinnerbone.", "You should try our sister game, Minceraft!", "Don't be sad. I'll do better next time, I promise!", "Don't be sad, have a hug! <3", "I just don't know what went wrong :(", "Shall we play a game?", "Quite honestly, I wouldn't worry myself about that.", "I bet Cylons wouldn't have this problem.", "Sorry :(", "Surprise! Haha. Well, this is awkward.", "Would you like a cupcake?", "Hi. I'm Minecraft, and I'm a crashaholic.", "Ooh. Shiny.", "This doesn't make any sense!", "Why is it breaking :(", "Don't do that.", "Ouch. That hurt :(", "You're mean.", "This is a token for 1 free hug. Redeem at your nearest Mojangsta: [~~HUG~~]", "There are four lights!", "But it works on my machine."};
        try {
            return stringArray[(int)(System.nanoTime() % (long)stringArray.length)];
        }
        catch (Throwable throwable) {
            return "Witty comment unavailable :(";
        }
    }

    public String getCauseStackTraceOrString() {
        StringWriter stringWriter = null;
        PrintWriter printWriter = null;
        Throwable throwable = this.cause;
        if (throwable.getMessage() == null) {
            if (throwable instanceof NullPointerException) {
                throwable = new NullPointerException(this.description);
            } else if (throwable instanceof StackOverflowError) {
                throwable = new StackOverflowError(this.description);
            } else if (throwable instanceof OutOfMemoryError) {
                throwable = new OutOfMemoryError(this.description);
            }
            throwable.setStackTrace(this.cause.getStackTrace());
        }
        String string = throwable.toString();
        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);
        throwable.printStackTrace(printWriter);
        string = stringWriter.toString();
        IOUtils.closeQuietly((Writer)stringWriter);
        IOUtils.closeQuietly((Writer)printWriter);
        return string;
    }

    public CrashReportCategory makeCategory(String string) {
        return this.makeCategoryDepth(string, 1);
    }

    public CrashReportCategory makeCategoryDepth(String string, int n) {
        CrashReportCategory crashReportCategory = new CrashReportCategory(this, string);
        if (this.field_85059_f) {
            int n2 = crashReportCategory.getPrunedStackTrace(n);
            StackTraceElement[] stackTraceElementArray = this.cause.getStackTrace();
            StackTraceElement stackTraceElement = null;
            StackTraceElement stackTraceElement2 = null;
            int n3 = stackTraceElementArray.length - n2;
            if (n3 < 0) {
                System.out.println("Negative index in crash report handler (" + stackTraceElementArray.length + "/" + n2 + ")");
            }
            if (stackTraceElementArray != null && n3 >= 0 && n3 < stackTraceElementArray.length) {
                stackTraceElement = stackTraceElementArray[n3];
                if (stackTraceElementArray.length + 1 - n2 < stackTraceElementArray.length) {
                    stackTraceElement2 = stackTraceElementArray[stackTraceElementArray.length + 1 - n2];
                }
            }
            this.field_85059_f = crashReportCategory.firstTwoElementsOfStackTraceMatch(stackTraceElement, stackTraceElement2);
            if (n2 > 0 && !this.crashReportSections.isEmpty()) {
                CrashReportCategory crashReportCategory2 = this.crashReportSections.get(this.crashReportSections.size() - 1);
                crashReportCategory2.trimStackTraceEntriesFromBottom(n2);
            } else if (stackTraceElementArray != null && stackTraceElementArray.length >= n2 && n3 >= 0 && n3 < stackTraceElementArray.length) {
                this.stacktrace = new StackTraceElement[n3];
                System.arraycopy(stackTraceElementArray, 0, this.stacktrace, 0, this.stacktrace.length);
            } else {
                this.field_85059_f = false;
            }
        }
        this.crashReportSections.add(crashReportCategory);
        return crashReportCategory;
    }

    public void getSectionsInStringBuilder(StringBuilder stringBuilder) {
        if ((this.stacktrace == null || this.stacktrace.length <= 0) && this.crashReportSections.size() > 0) {
            this.stacktrace = (StackTraceElement[])ArrayUtils.subarray((Object[])this.crashReportSections.get(0).getStackTrace(), (int)0, (int)1);
        }
        if (this.stacktrace != null && this.stacktrace.length > 0) {
            stringBuilder.append("-- Head --\n");
            stringBuilder.append("Stacktrace:\n");
            StackTraceElement[] stackTraceElementArray = this.stacktrace;
            int n = this.stacktrace.length;
            int n2 = 0;
            while (n2 < n) {
                StackTraceElement object = stackTraceElementArray[n2];
                stringBuilder.append("\t").append("at ").append(object.toString());
                stringBuilder.append("\n");
                ++n2;
            }
            stringBuilder.append("\n");
        }
        for (CrashReportCategory crashReportCategory : this.crashReportSections) {
            crashReportCategory.appendToStringBuilder(stringBuilder);
            stringBuilder.append("\n\n");
        }
        this.theReportCategory.appendToStringBuilder(stringBuilder);
    }

    private void populateEnvironment() {
        this.theReportCategory.addCrashSectionCallable("Minecraft Version", new Callable<String>(){

            @Override
            public String call() {
                return "1.8.8";
            }
        });
        this.theReportCategory.addCrashSectionCallable("Operating System", new Callable<String>(){

            @Override
            public String call() {
                return String.valueOf(System.getProperty("os.name")) + " (" + System.getProperty("os.arch") + ") version " + System.getProperty("os.version");
            }
        });
        this.theReportCategory.addCrashSectionCallable("Java Version", new Callable<String>(){

            @Override
            public String call() {
                return String.valueOf(System.getProperty("java.version")) + ", " + System.getProperty("java.vendor");
            }
        });
        this.theReportCategory.addCrashSectionCallable("Java VM Version", new Callable<String>(){

            @Override
            public String call() {
                return String.valueOf(System.getProperty("java.vm.name")) + " (" + System.getProperty("java.vm.info") + "), " + System.getProperty("java.vm.vendor");
            }
        });
        this.theReportCategory.addCrashSectionCallable("Memory", new Callable<String>(){

            @Override
            public String call() {
                Runtime runtime = Runtime.getRuntime();
                long l = runtime.maxMemory();
                long l2 = runtime.totalMemory();
                long l3 = runtime.freeMemory();
                long l4 = l / 1024L / 1024L;
                long l5 = l2 / 1024L / 1024L;
                long l6 = l3 / 1024L / 1024L;
                return String.valueOf(l3) + " bytes (" + l6 + " MB) / " + l2 + " bytes (" + l5 + " MB) up to " + l + " bytes (" + l4 + " MB)";
            }
        });
        this.theReportCategory.addCrashSectionCallable("JVM Flags", new Callable<String>(){

            @Override
            public String call() {
                RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
                List<String> list = runtimeMXBean.getInputArguments();
                int n = 0;
                StringBuilder stringBuilder = new StringBuilder();
                for (String string : list) {
                    if (!string.startsWith("-X")) continue;
                    if (n++ > 0) {
                        stringBuilder.append(" ");
                    }
                    stringBuilder.append(string);
                }
                return String.format("%d total; %s", n, stringBuilder.toString());
            }
        });
        this.theReportCategory.addCrashSectionCallable("IntCache", new Callable<String>(){

            @Override
            public String call() throws Exception {
                return IntCache.getCacheSizes();
            }
        });
    }

    public boolean saveToFile(File file) {
        if (this.crashReportFile != null) {
            return false;
        }
        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(this.getCompleteReport());
            fileWriter.close();
            this.crashReportFile = file;
            return true;
        }
        catch (Throwable throwable) {
            logger.error("Could not save crash report to " + file, throwable);
            return false;
        }
    }

    public Throwable getCrashCause() {
        return this.cause;
    }

    public static CrashReport makeCrashReport(Throwable throwable, String string) {
        CrashReport crashReport = throwable instanceof ReportedException ? ((ReportedException)throwable).getCrashReport() : new CrashReport(string, throwable);
        return crashReport;
    }

    public File getFile() {
        return this.crashReportFile;
    }
}

