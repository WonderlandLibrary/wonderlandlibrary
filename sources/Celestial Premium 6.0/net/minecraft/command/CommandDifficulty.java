/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
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
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length <= 0) {
            throw new WrongUsageException("commands.difficulty.usage", new Object[0]);
        }
        EnumDifficulty enumdifficulty = this.getDifficultyFromCommand(args[0]);
        server.setDifficultyForAllWorlds(enumdifficulty);
        CommandDifficulty.notifyCommandListener(sender, (ICommand)this, "commands.difficulty.success", new TextComponentTranslation(enumdifficulty.getDifficultyResourceKey(), new Object[0]));
    }

    protected EnumDifficulty getDifficultyFromCommand(String difficultyString) throws CommandException, NumberInvalidException {
        if (!"peaceful".equalsIgnoreCase(difficultyString) && !"p".equalsIgnoreCase(difficultyString)) {
            if (!"easy".equalsIgnoreCase(difficultyString) && !"e".equalsIgnoreCase(difficultyString)) {
                if (!"normal".equalsIgnoreCase(difficultyString) && !"n".equalsIgnoreCase(difficultyString)) {
                    return !"hard".equalsIgnoreCase(difficultyString) && !"h".equalsIgnoreCase(difficultyString) ? EnumDifficulty.getDifficultyEnum(CommandDifficulty.parseInt(difficultyString, 0, 3)) : EnumDifficulty.HARD;
                }
                return EnumDifficulty.NORMAL;
            }
            return EnumDifficulty.EASY;
        }
        return EnumDifficulty.PEACEFUL;
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
        return args.length == 1 ? CommandDifficulty.getListOfStringsMatchingLastWord(args, "peaceful", "easy", "normal", "hard") : Collections.emptyList();
    }
}

