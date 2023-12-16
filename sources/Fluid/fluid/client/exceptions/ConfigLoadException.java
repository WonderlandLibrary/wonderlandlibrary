// 
// Decompiled by Procyon v0.6.0
// 

package fluid.client.exceptions;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

public class ConfigLoadException extends Exception
{
    public ConfigLoadException(final String msg) {
        LogManager.getLogger().log(Level.FATAL, "FATAL CONFIG ERROR: " + msg);
    }
}
