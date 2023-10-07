// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.management.command.impl;

import exhibition.util.misc.ChatUtil;
import exhibition.management.command.Command;

public class PhaseMode extends Command
{
    public static Phase phase;
    
    public PhaseMode(final String[] names, final String description) {
        super(names, description);
    }
    
    @Override
    public void fire(final String[] args) {
        if (args == null) {
            ChatUtil.printChat("§4[§cE§4]§8 Current phase mode:§c " + PhaseMode.phase.name());
            this.printUsage();
            return;
        }
        if (args.length > 0) {
            for (final Phase type : Phase.values()) {
                if (type.name().toLowerCase().contains(args[0].toLowerCase())) {
                    PhaseMode.phase = type;
                    ChatUtil.printChat("§4[§cE§4]§8 Phase mode has been set to:§c " + type.name());
                }
            }
        }
    }
    
    @Override
    public String getUsage() {
        return "<Spider, Skip, Normal, FullBlock, Silent>";
    }
    
    static {
        PhaseMode.phase = Phase.Normal;
    }
    
    public enum Phase
    {
        Spider, 
        Skip, 
        Normal, 
        FullBlock, 
        Silent;
    }
}
