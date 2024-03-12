// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.commands.impl;

import dev.tenacity.Tenacity;
import dev.tenacity.commands.Command;

public final class ScriptCommand extends Command
{
    public ScriptCommand() {
        super("scriptreload", "Reloads all scripts", ".scriptreload", new String[0]);
    }
    
    @Override
    public void execute(final String[] args) {
        Tenacity.INSTANCE.getScriptManager().reloadScripts();
    }
}
