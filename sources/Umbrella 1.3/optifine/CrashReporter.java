/*
 * Decompiled with CFR 0.150.
 */
package optifine;

import java.util.HashMap;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import optifine.Config;
import optifine.FileUploadThread;
import optifine.IFileUploadListener;
import shadersmod.client.Shaders;

public class CrashReporter {
    public static void onCrashReport(CrashReport crashReport, CrashReportCategory category) {
        try {
            GameSettings e = Config.getGameSettings();
            if (e == null) {
                return;
            }
            if (!e.snooperEnabled) {
                return;
            }
            Throwable cause = crashReport.getCrashCause();
            if (cause == null) {
                return;
            }
            if (cause.getClass() == Throwable.class) {
                return;
            }
            if (cause.getClass().getName().contains(".fml.client.SplashProgress")) {
                return;
            }
            CrashReporter.extendCrashReport(category);
            String url = "http://optifine.net/crashReport";
            String reportStr = CrashReporter.makeReport(crashReport);
            byte[] content = reportStr.getBytes("ASCII");
            IFileUploadListener listener = new IFileUploadListener(){

                @Override
                public void fileUploadFinished(String url, byte[] content, Throwable exception) {
                }
            };
            HashMap<String, String> headers = new HashMap<String, String>();
            headers.put("OF-Version", Config.getVersion());
            headers.put("OF-Summary", CrashReporter.makeSummary(crashReport));
            FileUploadThread fut = new FileUploadThread(url, headers, content, listener);
            fut.setPriority(10);
            fut.start();
            Thread.sleep(1000L);
        }
        catch (Exception var10) {
            Config.dbg(String.valueOf(var10.getClass().getName()) + ": " + var10.getMessage());
        }
    }

    private static String makeReport(CrashReport crashReport) {
        StringBuffer sb = new StringBuffer();
        sb.append("OptiFineVersion: " + Config.getVersion() + "\n");
        sb.append("Summary: " + CrashReporter.makeSummary(crashReport) + "\n");
        sb.append("\n");
        sb.append(crashReport.getCompleteReport());
        sb.append("\n");
        return sb.toString();
    }

    private static String makeSummary(CrashReport crashReport) {
        Throwable t = crashReport.getCrashCause();
        if (t == null) {
            return "Unknown";
        }
        StackTraceElement[] traces = t.getStackTrace();
        String firstTrace = "unknown";
        if (traces.length > 0) {
            firstTrace = traces[0].toString().trim();
        }
        String sum = String.valueOf(t.getClass().getName()) + ": " + t.getMessage() + " (" + crashReport.getDescription() + ")" + " [" + firstTrace + "]";
        return sum;
    }

    public static void extendCrashReport(CrashReportCategory cat) {
        cat.addCrashSection("OptiFine Version", Config.getVersion());
        if (Config.getGameSettings() != null) {
            cat.addCrashSection("Render Distance Chunks", "" + Config.getChunkViewDistance());
            cat.addCrashSection("Mipmaps", "" + Config.getMipmapLevels());
            cat.addCrashSection("Anisotropic Filtering", "" + Config.getAnisotropicFilterLevel());
            cat.addCrashSection("Antialiasing", "" + Config.getAntialiasingLevel());
            cat.addCrashSection("Multitexture", "" + Config.isMultiTexture());
        }
        cat.addCrashSection("Shaders", Shaders.getShaderPackName());
        cat.addCrashSection("OpenGlVersion", Config.openGlVersion);
        cat.addCrashSection("OpenGlRenderer", Config.openGlRenderer);
        cat.addCrashSection("OpenGlVendor", Config.openGlVendor);
        cat.addCrashSection("CpuCount", "" + Config.getAvailableProcessors());
    }
}

