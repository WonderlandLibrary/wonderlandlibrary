// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.management.command.impl;

import exhibition.util.misc.ChatUtil;
import exhibition.module.Module;
import exhibition.management.command.Command;

public class LoadConfig extends Command
{
    public LoadConfig(final String[] names, final String description) {
        super(names, description);
    }
    
    @Override
    public void fire(final String[] args) {
        Module.loadSettings();
        ChatUtil.printChat("Loaded");
    }
    
    @Override
    public String getUsage() {
        return null;
    }
}
