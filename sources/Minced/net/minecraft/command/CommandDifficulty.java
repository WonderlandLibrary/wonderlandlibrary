// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.server.MinecraftServer;

public class CommandDifficulty extends CommandBase
{
    @Override
    public String getName() {
        return "difficulty";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getUsage(final ICommandSender sender) {
        return "commands.difficulty.usage";
    }
    
    @Override
    public void execute(final MinecraftServer server, final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length <= 0) {
            throw new WrongUsageException("commands.difficulty.usage", new Object[0]);
        }
        final EnumDifficulty enumdifficulty = this.getDifficultyFromCommand(args[0]);
        server.setDifficultyForAllWorlds(enumdifficulty);
        CommandBase.notifyCommandListener(sender, this, "commands.difficulty.success", new TextComponentTranslation(enumdifficulty.getTranslationKey(), new Object[0]));
    }
    
    protected EnumDifficulty getDifficultyFromCommand(final String difficultyString) throws CommandException, NumberInvalidException {
        if ("peaceful".equalsIgnoreCase(difficultyString) || "p".equalsIgnoreCase(difficultyString)) {
            return EnumDifficulty.PEACEFUL;
        }
        if ("easy".equalsIgnoreCase(difficultyString) || "e".equalsIgnoreCase(difficultyString)) {
            return EnumDifficulty.EASY;
        }
        if (!"normal".equalsIgnoreCase(difficultyString) && !"n".equalsIgnoreCase(difficultyString)) {
            return (!"hard".equalsIgnoreCase(difficultyString) && !"h".equalsIgnoreCase(difficultyString)) ? EnumDifficulty.byId(CommandBase.parseInt(difficultyString, 0, 3)) : EnumDifficulty.HARD;
        }
        return EnumDifficulty.NORMAL;
    }
    
    @Override
    public List<String> getTabCompletions(final MinecraftServer server, final ICommandSender sender, final String[] args, @Nullable final BlockPos targetPos) {
        return (args.length == 1) ? CommandBase.getListOfStringsMatchingLastWord(args, "peaceful", "easy", "normal", "hard") : Collections.emptyList();
    }
}
