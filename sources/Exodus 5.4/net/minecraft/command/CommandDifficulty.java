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
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "commands.difficulty.usage";
    }

    @Override
    public String getCommandName() {
        return "difficulty";
    }

    protected EnumDifficulty getDifficultyFromCommand(String string) throws CommandException, NumberInvalidException {
        return !string.equalsIgnoreCase("peaceful") && !string.equalsIgnoreCase("p") ? (!string.equalsIgnoreCase("easy") && !string.equalsIgnoreCase("e") ? (!string.equalsIgnoreCase("normal") && !string.equalsIgnoreCase("n") ? (!string.equalsIgnoreCase("hard") && !string.equalsIgnoreCase("h") ? EnumDifficulty.getDifficultyEnum(CommandDifficulty.parseInt(string, 0, 3)) : EnumDifficulty.HARD) : EnumDifficulty.NORMAL) : EnumDifficulty.EASY) : EnumDifficulty.PEACEFUL;
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender iCommandSender, String[] stringArray, BlockPos blockPos) {
        return stringArray.length == 1 ? CommandDifficulty.getListOfStringsMatchingLastWord(stringArray, "peaceful", "easy", "normal", "hard") : null;
    }

    @Override
    public void processCommand(ICommandSender iCommandSender, String[] stringArray) throws CommandException {
        if (stringArray.length <= 0) {
            throw new WrongUsageException("commands.difficulty.usage", new Object[0]);
        }
        EnumDifficulty enumDifficulty = this.getDifficultyFromCommand(stringArray[0]);
        MinecraftServer.getServer().setDifficultyForAllWorlds(enumDifficulty);
        CommandDifficulty.notifyOperators(iCommandSender, (ICommand)this, "commands.difficulty.success", new ChatComponentTranslation(enumDifficulty.getDifficultyResourceKey(), new Object[0]));
    }
}

