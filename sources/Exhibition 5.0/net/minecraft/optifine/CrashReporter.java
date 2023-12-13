// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.optifine;

import net.minecraft.client.settings.GameSettings;
import java.util.Map;
import java.util.HashMap;
import net.minecraft.crash.CrashReport;

public class CrashReporter
{
    public static void onCrashReport(final CrashReport crashReport) {
        try {
            final GameSettings e = Config.getGameSettings();
            if (e == null) {
                return;
            }
            if (!e.snooperEnabled) {
                return;
            }
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
        catch (Exception var8) {
            Config.dbg(var8.getClass().getName() + ": " + var8.getMessage());
        }
    }
    
    private static String makeReport(final CrashReport crashReport) {
        final StringBuffer sb = new StringBuffer();
        sb.append("OptiFineVersion: " + Config.getVersion() + "\n");
        sb.append("Summary: " + makeSummary(crashReport) + "\n");
        sb.append("\n");
        sb.append(crashReport.getCompleteReport());
        sb.append("\n");
        sb.append("OpenGlVersion: " + Config.openGlVersion + "\n");
        sb.append("OpenGlRenderer: " + Config.openGlRenderer + "\n");
        sb.append("OpenGlVendor: " + Config.openGlVendor + "\n");
        sb.append("CpuCount: " + Config.getAvailableProcessors() + "\n");
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
        final String sum = t.getClass().getName() + ": " + t.getMessage() + " (" + crashReport.getDescription() + ") [" + firstTrace + "]";
        return sum;
    }
}
