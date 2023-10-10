// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.util.ProcessIdUtil;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "ProcessIdPatternConverter", category = "Converter")
@ConverterKeys({ "pid", "processId" })
public final class ProcessIdPatternConverter extends LogEventPatternConverter
{
    private static final String DEFAULT_DEFAULT_VALUE = "???";
    private final String pid;
    
    private ProcessIdPatternConverter(final String... options) {
        super("Process ID", "pid");
        final String defaultValue = (options.length > 0) ? options[0] : "???";
        final String discoveredPid = ProcessIdUtil.getProcessId();
        this.pid = (discoveredPid.equals("-") ? defaultValue : discoveredPid);
    }
    
    public String getProcessId() {
        return this.pid;
    }
    
    public static void main(final String[] args) {
        System.out.println(new ProcessIdPatternConverter(new String[0]).pid);
    }
    
    public static ProcessIdPatternConverter newInstance(final String[] options) {
        return new ProcessIdPatternConverter(options);
    }
    
    @Override
    public void format(final LogEvent event, final StringBuilder toAppendTo) {
        toAppendTo.append(this.pid);
    }
}
