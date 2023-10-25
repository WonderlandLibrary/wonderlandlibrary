/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSenderWrapper;
import net.minecraft.command.EntityNotFoundException;
import net.minecraft.command.FunctionObject;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class CommandFunction
extends CommandBase {
    @Override
    public String getCommandName() {
        return "function";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "commands.function.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length != 1 && args.length != 3) {
            throw new WrongUsageException("commands.function.usage", new Object[0]);
        }
        ResourceLocation resourcelocation = new ResourceLocation(args[0]);
        FunctionObject functionobject = server.func_193030_aL().func_193058_a(resourcelocation);
        if (functionobject == null) {
            throw new CommandException("commands.function.unknown", resourcelocation);
        }
        if (args.length == 3) {
            boolean flag;
            String s = args[1];
            if ("if".equals(s)) {
                flag = true;
            } else {
                if (!"unless".equals(s)) {
                    throw new WrongUsageException("commands.function.usage", new Object[0]);
                }
                flag = false;
            }
            boolean flag1 = false;
            try {
                flag1 = !CommandFunction.getEntityList(server, sender, args[2]).isEmpty();
            }
            catch (EntityNotFoundException entityNotFoundException) {
                // empty catch block
            }
            if (flag != flag1) {
                throw new CommandException("commands.function.skipped", resourcelocation);
            }
        }
        int i = server.func_193030_aL().func_194019_a(functionobject, CommandSenderWrapper.func_193998_a(sender).func_194000_i().func_193999_a(2).func_194001_a(false));
        CommandFunction.notifyCommandListener(sender, (ICommand)this, "commands.function.success", resourcelocation, i);
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
        if (args.length == 1) {
            return CommandFunction.getListOfStringsMatchingLastWord(args, server.func_193030_aL().func_193066_d().keySet());
        }
        if (args.length == 2) {
            return CommandFunction.getListOfStringsMatchingLastWord(args, "if", "unless");
        }
        return args.length == 3 ? CommandFunction.getListOfStringsMatchingLastWord(args, server.getAllUsernames()) : Collections.emptyList();
    }
}

