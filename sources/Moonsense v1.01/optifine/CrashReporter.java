// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import shadersmod.client.Shaders;
import net.minecraft.client.settings.GameSettings;
import java.util.Map;
import java.util.HashMap;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.CrashReport;

public class CrashReporter
{
    public static void onCrashReport(final CrashReport crashReport, final CrashReportCategory category) {
        try {
            final GameSettings e = Config.getGameSettings();
            if (e == null) {
                return;
            }
            if (!e.snooperEnabled) {
                return;
            }
            final Throwable cause = crashReport.getCrashCause();
            if (cause == null) {
                return;
            }
            if (cause.getClass() == Throwable.class) {
                return;
            }
            if (cause.getClass().getName().contains(".fml.client.SplashProgress")) {
                return;
            }
            extendCrashReport(category);
            final String url = "http://optifine.net/crashReport";
            final String reportStr = makeReport(crashReport);
            final byte[] content = reportStr.getBytes("ASCII");
            final IFileUploadListener listener = new IFileUploadListener() {
                @Override
                public void fileUploadFinished(final String url, final byte[] content, final Throwable exception) {
                }
            };
            final HashMap headers = new HashMap();
            headers.put("OF-Version", Config.getVersion());
            headers.put("OF-Summary", makeSummary(crashReport));
            final FileUploadThread fut = new FileUploadThread(url, headers, content, listener);
            fut.setPriority(10);
            fut.start();
            Thread.sleep(1000L);
        }
        catch (Exception var10) {
            Config.dbg(String.valueOf(var10.getClass().getName()) + ": " + var10.getMessage());
        }
    }
    
    private static String makeReport(final CrashReport crashReport) {
        final StringBuffer sb = new StringBuffer();
        sb.append("OptiFineVersion: " + Config.getVersion() + "\n");
        sb.append("Summary: " + makeSummary(crashReport) + "\n");
        sb.append("\n");
        sb.append(crashReport.getCompleteReport());
        sb.append("\n");
        return sb.toString();
    }
    
    private static String makeSummary(final CrashReport crashReport) {
        final Throwable t = crashReport.getCrashCause();
        if (t == null) {
            return "Unknown";
        }
        final StackTraceElement[] traces = t.getStackTrace();
        String firstTrace = "unknown";
        if (traces.length > 0) {
            firstTrace = traces[0].toString().trim();
        }
        final String sum = String.valueOf(t.getClass().getName()) + ": " + t.getMessage() + " (" + crashReport.getDescription() + ")" + " [" + firstTrace + "]";
        return sum;
    }
    
    public static void extendCrashReport(final CrashReportCategory cat) {
        cat.addCrashSection("OptiFine Version", Config.getVersion());
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
