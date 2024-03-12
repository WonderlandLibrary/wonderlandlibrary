// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine;

import net.optifine.shaders.Shaders;
import java.util.Map;
import net.minecraft.client.settings.GameSettings;
import net.optifine.http.FileUploadThread;
import java.util.HashMap;
import net.optifine.http.IFileUploadListener;
import net.minecraft.src.Config;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.CrashReport;

public class CrashReporter
{
    public static void onCrashReport(final CrashReport crashReport, final CrashReportCategory category) {
        try {
            final Throwable throwable = crashReport.getCrashCause();
            if (throwable == null) {
                return;
            }
            if (throwable.getClass().getName().contains(".fml.client.SplashProgress")) {
                return;
            }
            if (throwable.getClass() == Throwable.class) {
                return;
            }
            extendCrashReport(category);
            final GameSettings gamesettings = Config.getGameSettings();
            if (gamesettings == null) {
                return;
            }
            if (!gamesettings.snooperEnabled) {
                return;
            }
            final String s = "http://optifine.net/crashReport";
            final String s2 = makeReport(crashReport);
            final byte[] abyte = s2.getBytes("ASCII");
            final IFileUploadListener ifileuploadlistener = new IFileUploadListener() {
                @Override
                public void fileUploadFinished(final String url, final byte[] content, final Throwable exception) {
                }
            };
            final Map map = new HashMap();
            map.put("OF-Version", Config.getVersion());
            map.put("OF-Summary", makeSummary(crashReport));
            final FileUploadThread fileuploadthread = new FileUploadThread(s, map, abyte, ifileuploadlistener);
            fileuploadthread.setPriority(10);
            fileuploadthread.start();
            Thread.sleep(1000L);
        }
        catch (Exception exception) {
            Config.dbg(exception.getClass().getName() + ": " + exception.getMessage());
        }
    }
    
    private static String makeReport(final CrashReport crashReport) {
        final StringBuffer stringbuffer = new StringBuffer();
        stringbuffer.append("OptiFineVersion: " + Config.getVersion() + "\n");
        stringbuffer.append("Summary: " + makeSummary(crashReport) + "\n");
        stringbuffer.append("\n");
        stringbuffer.append(crashReport.getCompleteReport());
        stringbuffer.append("\n");
        return stringbuffer.toString();
    }
    
    private static String makeSummary(final CrashReport crashReport) {
        final Throwable throwable = crashReport.getCrashCause();
        if (throwable == null) {
            return "Unknown";
        }
        final StackTraceElement[] astacktraceelement = throwable.getStackTrace();
        String s = "unknown";
        if (astacktraceelement.length > 0) {
            s = astacktraceelement[0].toString().trim();
        }
        final String s2 = throwable.getClass().getName() + ": " + throwable.getMessage() + " (" + crashReport.getDescription() + ") [" + s + "]";
        return s2;
    }
    
    public static void extendCrashReport(final CrashReportCategory cat) {
        cat.addCrashSection("OptiFine Version", Config.getVersion());
        cat.addCrashSection("OptiFine Build", Config.getBuild());
        if (Config.getGameSettings() != null) {
            cat.addCrashSection("Render Distance Chunks", "" + Config.getChunkViewDistance());
            cat.addCrashSection("Mipmaps", "" + Config.getMipmapLevels());
            cat.addCrashSection("Anisotropic Filtering", "" + Config.getAnisotropicFilterLevel());
            cat.addCrashSection("Antialiasing", "" + Config.getAntialiasingLevel());
            cat.addCrashSection("Multitexture", "" + Config.isMultiTexture());
        }
        cat.addCrashSection("Shaders", "" + Shaders.getShaderPackName());
        cat.addCrashSection("OpenGlVersion", "" + Config.openGlVersion);
        cat.addCrashSection("OpenGlRenderer", "" + Config.openGlRenderer);
        cat.addCrashSection("OpenGlVendor", "" + Config.openGlVendor);
        cat.addCrashSection("CpuCount", "" + Config.getAvailableProcessors());
    }
}
