package net.optifine;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.src.Config;
import net.optifine.CrashReporter;
import net.optifine.http.FileUploadThread;
import net.optifine.http.IFileUploadListener;
import net.optifine.shaders.Shaders;

public class CrashReporter {
    public static void onCrashReport(CrashReport crashReport, CrashReportCategory category) {
        try {
            Throwable throwable = crashReport.getCrashCause();
            if (throwable == null) {
                return;
            }
            if (throwable.getClass().getName().contains((CharSequence)".fml.client.SplashProgress")) {
                return;
            }
            if (throwable.getClass() == Throwable.class) {
                return;
            }
            CrashReporter.extendCrashReport(category);
            GameSettings gamesettings = Config.getGameSettings();
            if (gamesettings == null) {
                return;
            }
            if (!gamesettings.snooperEnabled) {
                return;
            }
            String s = "http://optifine.net/crashReport";
            String s1 = CrashReporter.makeReport(crashReport);
            byte[] abyte = s1.getBytes("ASCII");
            1 ifileuploadlistener = new /* Unavailable Anonymous Inner Class!! */;
            HashMap map = new HashMap();
            map.put((Object)"OF-Version", (Object)Config.getVersion());
            map.put((Object)"OF-Summary", (Object)CrashReporter.makeSummary(crashReport));
            FileUploadThread fileuploadthread = new FileUploadThread(s, (Map)map, abyte, (IFileUploadListener)ifileuploadlistener);
            fileuploadthread.setPriority(10);
            fileuploadthread.start();
            Thread.sleep((long)1000L);
        }
        catch (Exception exception) {
            Config.dbg((String)(exception.getClass().getName() + ": " + exception.getMessage()));
        }
    }

    private static String makeReport(CrashReport crashReport) {
        StringBuffer stringbuffer = new StringBuffer();
        stringbuffer.append("OptiFineVersion: " + Config.getVersion() + "\n");
        stringbuffer.append("Summary: " + CrashReporter.makeSummary(crashReport) + "\n");
        stringbuffer.append("\n");
        stringbuffer.append(crashReport.getCompleteReport());
        stringbuffer.append("\n");
        return stringbuffer.toString();
    }

    private static String makeSummary(CrashReport crashReport) {
        Throwable throwable = crashReport.getCrashCause();
        if (throwable == null) {
            return "Unknown";
        }
        StackTraceElement[] astacktraceelement = throwable.getStackTrace();
        String s = "unknown";
        if (astacktraceelement.length > 0) {
            s = astacktraceelement[0].toString().trim();
        }
        String s1 = throwable.getClass().getName() + ": " + throwable.getMessage() + " (" + crashReport.getDescription() + ") [" + s + "]";
        return s1;
    }

    public static void extendCrashReport(CrashReportCategory cat) {
        cat.addCrashSection("OptiFine Version", (Object)Config.getVersion());
        cat.addCrashSection("OptiFine Build", (Object)Config.getBuild());
        if (Config.getGameSettings() != null) {
            cat.addCrashSection("Render Distance Chunks", (Object)("" + Config.getChunkViewDistance()));
            cat.addCrashSection("Mipmaps", (Object)("" + Config.getMipmapLevels()));
            cat.addCrashSection("Anisotropic Filtering", (Object)("" + Config.getAnisotropicFilterLevel()));
            cat.addCrashSection("Antialiasing", (Object)("" + Config.getAntialiasingLevel()));
            cat.addCrashSection("Multitexture", (Object)("" + Config.isMultiTexture()));
        }
        cat.addCrashSection("Shaders", (Object)("" + Shaders.getShaderPackName()));
        cat.addCrashSection("OpenGlVersion", (Object)("" + Config.openGlVersion));
        cat.addCrashSection("OpenGlRenderer", (Object)("" + Config.openGlRenderer));
        cat.addCrashSection("OpenGlVendor", (Object)("" + Config.openGlVendor));
        cat.addCrashSection("CpuCount", (Object)("" + Config.getAvailableProcessors()));
    }
}
