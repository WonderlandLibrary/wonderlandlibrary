/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.beust.jcommander.JCommander
 *  com.beust.jcommander.Parameter
 */
package org.apache.logging.log4j.core.util;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

public class BasicCommandLineArguments {
    @Parameter(names={"--help", "-?", "-h"}, help=true, description="Prints this help.")
    private boolean help;

    public static <T extends BasicCommandLineArguments> T parseCommandLine(String[] mainArgs, Class<?> clazz, T args) {
        JCommander jCommander = new JCommander(args);
        jCommander.setProgramName(clazz.getName());
        jCommander.setCaseSensitiveOptions(false);
        jCommander.parse(mainArgs);
        if (args.isHelp()) {
            jCommander.usage();
        }
        return args;
    }

    public boolean isHelp() {
        return this.help;
    }

    public void setHelp(boolean help) {
        this.help = help;
    }
}

