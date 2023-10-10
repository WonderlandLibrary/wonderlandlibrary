// 
// Decompiled by Procyon v0.5.36
// 

package oshi.util;

import org.slf4j.LoggerFactory;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import com.sun.jna.Platform;
import org.slf4j.Logger;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class ExecutingCommand
{
    private static final Logger LOG;
    private static final String[] DEFAULT_ENV;
    
    private ExecutingCommand() {
    }
    
    private static String[] getDefaultEnv() {
        if (Platform.isWindows()) {
            return new String[] { "LANGUAGE=C" };
        }
        return new String[] { "LC_ALL=C" };
    }
    
    public static List<String> runNative(final String cmdToRun) {
        final String[] cmd = cmdToRun.split(" ");
        return runNative(cmd);
    }
    
    public static List<String> runNative(final String[] cmdToRunWithArgs) {
        return runNative(cmdToRunWithArgs, ExecutingCommand.DEFAULT_ENV);
    }
    
    public static List<String> runNative(final String[] cmdToRunWithArgs, final String[] envp) {
        Process p = null;
        try {
            p = Runtime.getRuntime().exec(cmdToRunWithArgs, envp);
            return getProcessOutput(p, cmdToRunWithArgs);
        }
        catch (SecurityException ex) {}
        catch (IOException e) {
            ExecutingCommand.LOG.trace("Couldn't run command {}: {}", Arrays.toString(cmdToRunWithArgs), e.getMessage());
        }
        finally {
            if (p != null) {
                Label_0204: {
                    if (!Platform.isWindows()) {
                        if (!Platform.isSolaris()) {
                            break Label_0204;
                        }
                    }
                    try {
                        p.getOutputStream().close();
                    }
                    catch (IOException ex2) {}
                    try {
                        p.getInputStream().close();
                    }
                    catch (IOException ex3) {}
                    try {
                        p.getErrorStream().close();
                    }
                    catch (IOException ex4) {}
                }
                p.destroy();
            }
        }
        return Collections.emptyList();
    }
    
    private static List<String> getProcessOutput(final Process p, final String[] cmd) {
        final ArrayList<String> sa = new ArrayList<String>();
        try {
            final BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream(), Charset.defaultCharset()));
            try {
                String line;
                while ((line = reader.readLine()) != null) {
                    sa.add(line);
                }
                p.waitFor();
                reader.close();
            }
            catch (Throwable t) {
                try {
                    reader.close();
                }
                catch (Throwable exception) {
                    t.addSuppressed(exception);
                }
                throw t;
            }
        }
        catch (IOException e) {
            ExecutingCommand.LOG.trace("Problem reading output from {}: {}", Arrays.toString(cmd), e.getMessage());
        }
        catch (InterruptedException ie) {
            ExecutingCommand.LOG.trace("Interrupted while reading output from {}: {}", Arrays.toString(cmd), ie.getMessage());
            Thread.currentThread().interrupt();
        }
        return sa;
    }
    
    public static String getFirstAnswer(final String cmd2launch) {
        return getAnswerAt(cmd2launch, 0);
    }
    
    public static String getAnswerAt(final String cmd2launch, final int answerIdx) {
        final List<String> sa = runNative(cmd2launch);
        if (answerIdx >= 0 && answerIdx < sa.size()) {
            return sa.get(answerIdx);
        }
        return "";
    }
    
    static {
        LOG = LoggerFactory.getLogger(ExecutingCommand.class);
        DEFAULT_ENV = getDefaultEnv();
    }
}
