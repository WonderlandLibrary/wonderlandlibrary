package me.earth.earthhack.impl.commands;

import me.earth.earthhack.api.command.Command;
import me.earth.earthhack.api.module.Module;
import me.earth.earthhack.api.util.TextColor;
import me.earth.earthhack.impl.modules.client.commands.Commands;
import me.earth.earthhack.impl.services.chat.ChatManager;
import me.earth.earthhack.impl.services.client.ModuleManager;
import me.earth.earthhack.impl.util.client.ChatIDs;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

import java.util.List;

@SuppressWarnings("unused")
public class ModuleListCommand extends Command
{
    public ModuleListCommand()
    {
        super(new String[][]{{"modules"}});
    }

    @Override
    public void execute(String[] args)
    {
        ChatManager.getInstance().sendDeleteComponent(getComponent(), "moduleListCommand", ChatIDs.MODULE);
    }

    public static TextComponentString getComponent()
    {
        TextComponentString component = new TextComponentString("Modules: ");
        List<Module> moduleList = ModuleManager.getInstance().getModules();
        for (int i = 0; i < moduleList.size(); i++)
        {
            Module module = moduleList.get(i);
            if (module != null)
            {
                component.appendSibling(new TextComponentString((module.isEnabled() ? TextColor.GREEN : TextColor.RED) + module.getName() + (i == moduleList.size() - 1 ? "" : ", "))
                        .setStyle(new Style()
                                .setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString(module.getData().getDescription()))) //Holy shit u can append more here insane
                                .setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, Commands.getInstance().getPrefix() + "hiddenmodule " + module.getName()))));
            }
        }

        return component;
    }

}
