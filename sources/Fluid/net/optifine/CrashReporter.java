// 
// Decompiled by Procyon v0.6.0
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
        catch (final Exception exception) {
            Config.dbg(String.valueOf(exception.getClass().getName()) + ": " + exception.getMessage());
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
        final String s2 = String.valueOf(throwable.getClass().getName()) + ": " + throwable.getMessage() + " (" + crashReport.getDescription() + ")" + " [" + s + "]";
        return s2;
    }
    
    public static void extendCrashReport(final CrashReportCategory cat) {
        cat.addCrashSection("OptiFine Version", Config.getVersion());
        cat.addCrashSection("OptiFine Build", Config.getBuild());
        if (Config.getGameSettings() != null) {
            cat.addCrashSection("Render Distance Chunks", new StringBuilder().append(Config.getChunkViewDistance()).toString());
            cat.addCrashSection("Mipmaps", new StringBuilder().append(Config.getMipmapLevels()).toString());
            cat.addCrashSection("Anisotropic Filtering", new StringBuilder().append(Config.getAnisotropicFilterLevel()).toString());
            cat.addCrashSection("Antialiasing", new StringBuilder().append(Config.getAntialiasingLevel()).toString());
            cat.addCrashSection("Multitexture", new StringBuilder().append(Config.isMultiTexture()).toString());
        }
        cat.addCrashSection("Shaders", new StringBuilder().append(Shaders.getShaderPackName()).toString());
        cat.addCrashSection("OpenGlVersion", new StringBuilder().append(Config.openGlVersion).toString());
        cat.addCrashSection("OpenGlRenderer", new StringBuilder().append(Config.openGlRenderer).toString());
        cat.addCrashSection("OpenGlVendor", new StringBuilder().append(Config.openGlVendor).toString());
        cat.addCrashSection("CpuCount", new StringBuilder().append(Config.getAvailableProcessors()).toString());
    }
}
