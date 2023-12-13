package slavikcodd3r.rainbow.command.commands;

import java.io.IOException;

import org.lwjgl.opengl.Display;

import slavikcodd3r.rainbow.Rainbow;
import slavikcodd3r.rainbow.command.Com;
import slavikcodd3r.rainbow.command.Command;
import slavikcodd3r.rainbow.utils.ClientUtils;

@Com(names = { "nameprotect", "name", "namep", "nprotect", "np" })
public class NameProtect extends Command
{
    public void runCommand(final String[] args) {
        String spacereplace = "";
        spacereplace = args[1];
        final String dotreplace = (spacereplace.replace("$s", " "));
        final String customname = (dotreplace.replace("$d", "."));
        Rainbow.protectedname = customname;
        ClientUtils.sendMessage("Protected name changed to " + Rainbow.protectedname);
        }
    
    @Override
    public String getHelp() {
        return "nameprotect (use $s for space and $d for dot)";
    }
}
