package slavikcodd3r.rainbow.command.commands;

import java.util.Iterator;
import net.minecraft.entity.Entity;
import slavikcodd3r.rainbow.command.Com;
import slavikcodd3r.rainbow.command.Command;
import slavikcodd3r.rainbow.utils.ClientUtils;

@Com(names = { "pc", "playercoords", "playercoordinates", "near", "nearplayers" })
public class PlayerCoordinates extends Command
{
    @Override
    public String getHelp() {
        return "playercoordinates";
    }
    
    @Override
    public void runCommand(final String[] args) {
        for (final Entity e : ClientUtils.loadedEntityList()) {
                ClientUtils.sendMessage(e.getName() + " " + e.posX + " " + e.posY + " " + e.posZ);
        }
    }
}
