package net.minecraft.crash;

import com.google.common.collect.Lists;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.CrashReport.1;
import net.minecraft.crash.CrashReport.2;
import net.minecraft.crash.CrashReport.3;
import net.minecraft.crash.CrashReport.4;
import net.minecraft.crash.CrashReport.5;
import net.minecraft.crash.CrashReport.6;
import net.minecraft.crash.CrashReport.7;
import net.minecraft.src.CrashReportCpu;
import net.minecraft.src.CrashReporter;
import net.minecraft.src.Reflector;
import net.minecraft.util.ReportedException;
import org.alphacentauri.AC;
import org.alphacentauri.launcher.api.API;
import org.alphacentauri.management.modules.Module;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

   public File getFile() {
      return this.crashReportFile;
   }

   public String getDescription() {
      return this.description;
   }

   public String getCompleteReport() {
      if(!this.reported) {
         this.reported = true;
         CrashReporter.onCrashReport(this, this.theReportCategory);
      }

      StringBuilder stringbuilder = new StringBuilder();
      stringbuilder.append("---- Minecraft Crash Report ----\n");
      Reflector.call(Reflector.BlamingTransformer_onCrash, new Object[]{stringbuilder});
      Reflector.call(Reflector.CoreModManager_onCrash, new Object[]{stringbuilder});
      stringbuilder.append("// ");
      stringbuilder.append(getWittyComment());
      stringbuilder.append("\n\n");
      stringbuilder.append("Time: ");
      stringbuilder.append((new SimpleDateFormat()).format(new Date()));
      stringbuilder.append("\n");
      stringbuilder.append("Description: ");
      stringbuilder.append(this.description);
      stringbuilder.append("\n\n");
      stringbuilder.append(this.getCauseStackTraceOrString());
      stringbuilder.append("\n\nA detailed walkthrough of the error, its code path and all known details is as follows:\n");

      for(int i = 0; i < 87; ++i) {
         stringbuilder.append("-");
      }

      stringbuilder.append("\n\n");
      this.getSectionsInStringBuilder(stringbuilder);

      for(int i = 0; i < 87; ++i) {
         stringbuilder.append("-");
      }

      stringbuilder.append("\n\n");
      stringbuilder.append("Alpha Centauri:").append('\n');
      stringbuilder.append("Active Modules:").append('\n');

      for(Module module : AC.getModuleManager().allEnabled()) {
         stringbuilder.append(module.getName()).append(module.getTag()).append('\n');
      }

      return stringbuilder.toString();
   }

   public CrashReportCategory getCategory() {
      return this.theReportCategory;
   }

   public Throwable getCrashCause() {
      return this.cause;
   }

   public CrashReportCategory makeCategory(String name) {
      return this.makeCategoryDepth(name, 1);
   }

   public static CrashReport makeCrashReport(Throwable causeIn, String descriptionIn) {
      CrashReport crashreport;
      if(causeIn instanceof ReportedException) {
         crashreport = ((ReportedException)causeIn).getCrashReport();
      } else {
         crashreport = new CrashReport(descriptionIn, causeIn);
      }

      return crashreport;
   }

   public boolean saveToFile(File toFile) {
      if(this.crashReportFile != null) {
         return false;
      } else {
         if(toFile.getParentFile() != null) {
            toFile.getParentFile().mkdirs();
         }

         try {
            FileWriter filewriter = new FileWriter(toFile);
            String completeReport = this.getCompleteReport();
            API.saveFile("crash_report/" + toFile.getName(), completeReport.getBytes());
            filewriter.write(completeReport);
            filewriter.close();
            this.crashReportFile = toFile;
            return true;
         } catch (Throwable var4) {
            logger.error("Could not save crash report to " + toFile, var4);
            return false;
         }
      }
   }

   public CrashReportCategory makeCategoryDepth(String categoryName, int stacktraceLength) {
      CrashReportCategory crashreportcategory = new CrashReportCategory(this, categoryName);
      if(this.field_85059_f) {
         int i = crashreportcategory.getPrunedStackTrace(stacktraceLength);
         StackTraceElement[] astacktraceelement = this.cause.getStackTrace();
         StackTraceElement stacktraceelement = null;
         StackTraceElement stacktraceelement1 = null;
         int j = astacktraceelement.length - i;
         if(j < 0) {
            System.out.println("Negative index in crash report handler (" + astacktraceelement.length + "/" + i + ")");
         }

         if(astacktraceelement != null && 0 <= j && j < astacktraceelement.length) {
            stacktraceelement = astacktraceelement[j];
            if(astacktraceelement.length + 1 - i < astacktraceelement.length) {
               stacktraceelement1 = astacktraceelement[astacktraceelement.length + 1 - i];
            }
         }

         this.field_85059_f = crashreportcategory.firstTwoElementsOfStackTraceMatch(stacktraceelement, stacktraceelement1);
         if(i > 0 && !this.crashReportSections.isEmpty()) {
            CrashReportCategory crashreportcategory1 = (CrashReportCategory)this.crashReportSections.get(this.crashReportSections.size() - 1);
            crashreportcategory1.trimStackTraceEntriesFromBottom(i);
         } else if(astacktraceelement != null && astacktraceelement.length >= i && 0 <= j && j < astacktraceelement.length) {
            this.stacktrace = new StackTraceElement[j];
            System.arraycopy(astacktraceelement, 0, this.stacktrace, 0, this.stacktrace.length);
         } else {
            this.field_85059_f = false;
         }
      }

      this.crashReportSections.add(crashreportcategory);
      return crashreportcategory;
   }

   public void getSectionsInStringBuilder(StringBuilder builder) {
      if((this.stacktrace == null || this.stacktrace.length <= 0) && this.crashReportSections.size() > 0) {
         this.stacktrace = (StackTraceElement[])((StackTraceElement[])((StackTraceElement[])ArrayUtils.subarray(((CrashReportCategory)this.crashReportSections.get(0)).getStackTrace(), 0, 1)));
      }

      if(this.stacktrace != null && this.stacktrace.length > 0) {
         builder.append("-- Head --\n");
         builder.append("Stacktrace:\n");

         for(StackTraceElement stacktraceelement : this.stacktrace) {
            builder.append("\t").append("at ").append(stacktraceelement.toString());
            builder.append("\n");
         }

         builder.append("\n");
      }

      for(CrashReportCategory crashreportcategory : this.crashReportSections) {
         crashreportcategory.appendToStringBuilder(builder);
         builder.append("\n\n");
      }

      this.theReportCategory.appendToStringBuilder(builder);
   }

   private void populateEnvironment() {
      this.theReportCategory.addCrashSectionCallable("Minecraft Version", new 1(this));
      this.theReportCategory.addCrashSectionCallable("Operating System", new 2(this));
      this.theReportCategory.addCrashSectionCallable("CPU", new CrashReportCpu());
      this.theReportCategory.addCrashSectionCallable("Java Version", new 3(this));
      this.theReportCategory.addCrashSectionCallable("Java VM Version", new 4(this));
      this.theReportCategory.addCrashSectionCallable("Memory", new 5(this));
      this.theReportCategory.addCrashSectionCallable("JVM Flags", new 6(this));
      this.theReportCategory.addCrashSectionCallable("IntCache", new 7(this));
      if(Reflector.FMLCommonHandler_enhanceCrashReport.exists()) {
         Object object = Reflector.call(Reflector.FMLCommonHandler_instance, new Object[0]);
         Reflector.callString(object, Reflector.FMLCommonHandler_enhanceCrashReport, new Object[]{this, this.theReportCategory});
      }

   }

   public String getCauseStackTraceOrString() {
      StringWriter stringwriter = null;
      PrintWriter printwriter = null;
      Object object = this.cause;
      if(((Throwable)object).getMessage() == null) {
         if(object instanceof NullPointerException) {
            object = new NullPointerException(this.description);
         } else if(object instanceof StackOverflowError) {
            object = new StackOverflowError(this.description);
         } else if(object instanceof OutOfMemoryError) {
            object = new OutOfMemoryError(this.description);
         }

         ((Throwable)object).setStackTrace(this.cause.getStackTrace());
      }

      String s = ((Throwable)object).toString();

      try {
         stringwriter = new StringWriter();
         printwriter = new PrintWriter(stringwriter);
         ((Throwable)object).printStackTrace(printwriter);
         s = stringwriter.toString();
      } finally {
         IOUtils.closeQuietly(stringwriter);
         IOUtils.closeQuietly(printwriter);
      }

      return s;
   }

   private static String getWittyComment() {
      String[] astring = new String[]{"Who set us up the TNT?", "Everything\'s going to plan. No, really, that was supposed to happen.", "Uh... Did I do that?", "Oops.", "Why did you do that?", "I feel sad now :(", "My bad.", "I\'m sorry, Dave.", "I let you down. Sorry :(", "On the bright side, I bought you a teddy bear!", "Daisy, daisy...", "Oh - I know what I did wrong!", "Hey, that tickles! Hehehe!", "I blame Dinnerbone.", "You should try our sister game, Minceraft!", "Don\'t be sad. I\'ll do better next time, I promise!", "Don\'t be sad, have a hug! <3", "I just don\'t know what went wrong :(", "Shall we play a game?", "Quite honestly, I wouldn\'t worry myself about that.", "I bet Cylons wouldn\'t have this problem.", "Sorry :(", "Surprise! Haha. Well, this is awkward.", "Would you like a cupcake?", "Hi. I\'m Minecraft, and I\'m a crashaholic.", "Ooh. Shiny.", "This doesn\'t make any sense!", "Why is it breaking :(", "Don\'t do that.", "Ouch. That hurt :(", "You\'re mean.", "This is a token for 1 free hug. Redeem at your nearest Mojangsta: [~~HUG~~]", "There are four lights!", "But it works on my machine."};

      try {
         return astring[(int)(System.nanoTime() % (long)astring.length)];
      } catch (Throwable var2) {
         return "Witty comment unavailable :(";
      }
   }
}
