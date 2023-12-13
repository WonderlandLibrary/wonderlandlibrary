/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.command;

import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.EnumDifficulty;

public class CommandDifficulty
extends CommandBase {
    private static final String __OBFID = "CL_00000422";

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
        EnumDifficulty var3 = this.func_180531_e(args[0]);
        MinecraftServer.getServer().setDifficultyForAllWorlds(var3);
        CommandDifficulty.notifyOperators(sender, (ICommand)this, "commands.difficulty.success", new ChatComponentTranslation(var3.getDifficultyResourceKey(), new Object[0]));
    }

    protected EnumDifficulty func_180531_e(String p_180531_1_) throws CommandException {
        return !p_180531_1_.equalsIgnoreCase("peaceful") && !p_180531_1_.equalsIgnoreCase("p") ? (!p_180531_1_.equalsIgnoreCase("easy") && !p_180531_1_.equalsIgnoreCase("e") ? (!p_180531_1_.equalsIgnoreCase("normal") && !p_180531_1_.equalsIgnoreCase("n") ? (!p_180531_1_.equalsIgnoreCase("hard") && !p_180531_1_.equalsIgnoreCase("h") ? EnumDifficulty.getDifficultyEnum(CommandDifficulty.parseInt(p_180531_1_, 0, 3)) : EnumDifficulty.HARD) : EnumDifficulty.NORMAL) : EnumDifficulty.EASY) : EnumDifficulty.PEACEFUL;
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return args.length == 1 ? CommandDifficulty.getListOfStringsMatchingLastWord(args, "peaceful", "easy", "normal", "hard") : null;
    }
}

