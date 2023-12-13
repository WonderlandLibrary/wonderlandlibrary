package me.earth.earthhack.impl.commands.hidden;

import me.earth.earthhack.api.command.Command;
import me.earth.earthhack.api.command.Completer;
import me.earth.earthhack.api.module.Module;
import me.earth.earthhack.api.util.Globals;
import me.earth.earthhack.api.util.TextColor;
import me.earth.earthhack.impl.modules.client.commands.Commands;
import me.earth.earthhack.impl.services.chat.ChatManager;
import me.earth.earthhack.impl.services.client.ModuleManager;
import me.earth.earthhack.impl.util.client.ChatIDs;
import me.earth.earthhack.impl.util.text.ChatUtil;
import net.minecraft.client.gui.GuiChat;
import org.lwjgl.input.Mouse;

import static me.earth.earthhack.impl.commands.ModuleListCommand.getComponent;

@SuppressWarnings("unused")
public class HiddenModule extends Command implements Globals
{
    public HiddenModule()
    {
        super(new String[][]{{"hiddenmodule"}}, true);
    }

    @Override
    public void execute(String[] args)
    {
        if (args.length > 1)
        {
            String name = args[1];
            Module module = ModuleManager.getInstance().getModule(name);
            if (module != null)
            {
                if (Mouse.isButtonDown(1))
                {
                    mc.displayGuiScreen(new GuiChat(Commands.getInstance().getPrefix() + module.getName() + " "));
                }
                else
                {
                    boolean enabled = module.isEnabled();
                    module.toggle();
                    ChatManager.getInstance().sendDeleteComponent(getComponent(), "moduleListCommand", ChatIDs.MODULE);
                }
            }
            else
            {
                ChatUtil.sendMessage(TextColor.RED + "An error occurred.");
            }
        }
    }

    @Override
    public String getPossibleInputs(String[] args)
    {
        return null;
    }

    @Override
    public Completer onTabComplete(Completer completer)
    {
        return completer;
    }

}
