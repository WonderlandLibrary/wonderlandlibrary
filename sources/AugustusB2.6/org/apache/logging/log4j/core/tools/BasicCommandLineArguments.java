// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.tools;

import org.apache.logging.log4j.core.tools.picocli.CommandLine;

public class BasicCommandLineArguments
{
    @CommandLine.Option(names = { "--help", "-?", "-h" }, usageHelp = true, description = { "Prints this help and exits." })
    private boolean help;
    
    public boolean isHelp() {
        return this.help;
    }
    
    public void setHelp(final boolean help) {
        this.help = help;
    }
}
