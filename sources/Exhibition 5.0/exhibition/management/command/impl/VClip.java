// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.management.command.impl;

import exhibition.util.StringConversions;
import exhibition.management.command.Command;

public class VClip extends Command
{
    public VClip(final String[] names, final String description) {
        super(names, description);
    }
    
    @Override
    public void fire(final String[] args) {
        if (args == null) {
            this.printUsage();
            return;
        }
        if (args.length == 1 && StringConversions.isNumeric(args[0])) {
            this.mc.thePlayer.setPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + Double.parseDouble(args[0]), this.mc.thePlayer.posZ);
            return;
        }
        this.printUsage();
    }
    
    @Override
    public String getUsage() {
        return "<Distance>";
    }
}
