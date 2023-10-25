/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.pattern.ConverterKeys;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;
import org.apache.logging.log4j.util.ProcessIdUtil;

@Plugin(name="ProcessIdPatternConverter", category="Converter")
@ConverterKeys(value={"pid", "processId"})
public final class ProcessIdPatternConverter
extends LogEventPatternConverter {
    private static final String DEFAULT_DEFAULT_VALUE = "???";
    private final String pid;

    private ProcessIdPatternConverter(String ... options) {
        super("Process ID", "pid");
        String defaultValue = options.length > 0 ? options[0] : DEFAULT_DEFAULT_VALUE;
        String discoveredPid = ProcessIdUtil.getProcessId();
        this.pid = discoveredPid.equals("-") ? defaultValue : discoveredPid;
    }

    public String getProcessId() {
        return this.pid;
    }

    public static void main(String[] args) {
        System.out.println(new ProcessIdPatternConverter((String[])new String[0]).pid);
    }

    public static ProcessIdPatternConverter newInstance(String[] options) {
        return new ProcessIdPatternConverter(options);
    }

    @Override
    public void format(LogEvent event, StringBuilder toAppendTo) {
        toAppendTo.append(this.pid);
    }
}

