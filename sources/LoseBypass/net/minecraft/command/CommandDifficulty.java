/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.command;

import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.EnumDifficulty;

public class CommandDifficulty
extends CommandBase {
    @Override
    public String getCommandName() {
        return "difficulty";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "commands.difficulty.usage";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length <= 0) {
            throw new WrongUsageException("commands.difficulty.usage", new Object[0]);
        }
        EnumDifficulty enumdifficulty = this.getDifficultyFromCommand(args[0]);
        MinecraftServer.getServer().setDifficultyForAllWorlds(enumdifficulty);
        CommandDifficulty.notifyOperators(sender, (ICommand)this, "commands.difficulty.success", new ChatComponentTranslation(enumdifficulty.getDifficultyResourceKey(), new Object[0]));
    }

    protected EnumDifficulty getDifficultyFromCommand(String p_180531_1_) throws CommandException, NumberInvalidException {
        EnumDifficulty enumDifficulty;
        if (!p_180531_1_.equalsIgnoreCase("peaceful") && !p_180531_1_.equalsIgnoreCase("p")) {
            if (!p_180531_1_.equalsIgnoreCase("easy") && !p_180531_1_.equalsIgnoreCase("e")) {
                if (!p_180531_1_.equalsIgnoreCase("normal") && !p_180531_1_.equalsIgnoreCase("n")) {
                    if (!p_180531_1_.equalsIgnoreCase("hard") && !p_180531_1_.equalsIgnoreCase("h")) {
                        enumDifficulty = EnumDifficulty.getDifficultyEnum(CommandDifficulty.parseInt(p_180531_1_, 0, 3));
                        return enumDifficulty;
                    }
                    enumDifficulty = EnumDifficulty.HARD;
                    return enumDifficulty;
                }
                enumDifficulty = EnumDifficulty.NORMAL;
                return enumDifficulty;
            }
            enumDifficulty = EnumDifficulty.EASY;
            return enumDifficulty;
        }
        enumDifficulty = EnumDifficulty.PEACEFUL;
        return enumDifficulty;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        if (args.length != 1) return null;
        List<String> list = CommandDifficulty.getListOfStringsMatchingLastWord(args, "peaceful", "easy", "normal", "hard");
        return list;
    }
}

