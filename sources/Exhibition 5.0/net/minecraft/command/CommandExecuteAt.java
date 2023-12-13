// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.command;

import java.util.Collection;
import java.util.List;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.Block;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.util.Vec3;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.BlockPos;
import net.minecraft.entity.Entity;

public class CommandExecuteAt extends CommandBase
{
    private static final String __OBFID = "CL_00002344";
    
    @Override
    public String getCommandName() {
        return "execute";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.execute.usage";
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length < 5) {
            throw new WrongUsageException("commands.execute.usage", new Object[0]);
        }
        final Entity var3 = CommandBase.func_175759_a(sender, args[0], Entity.class);
        final double var4 = CommandBase.func_175761_b(var3.posX, args[1], false);
        final double var5 = CommandBase.func_175761_b(var3.posY, args[2], false);
        final double var6 = CommandBase.func_175761_b(var3.posZ, args[3], false);
        final BlockPos var7 = new BlockPos(var4, var5, var6);
        byte var8 = 4;
        if ("detect".equals(args[4]) && args.length > 10) {
            final World var9 = sender.getEntityWorld();
            final double var10 = CommandBase.func_175761_b(var4, args[5], false);
            final double var11 = CommandBase.func_175761_b(var5, args[6], false);
            final double var12 = CommandBase.func_175761_b(var6, args[7], false);
            final Block var13 = CommandBase.getBlockByText(sender, args[8]);
            final int var14 = CommandBase.parseInt(args[9], -1, 15);
            final BlockPos var15 = new BlockPos(var10, var11, var12);
            final IBlockState var16 = var9.getBlockState(var15);
            if (var16.getBlock() != var13 || (var14 >= 0 && var16.getBlock().getMetaFromState(var16) != var14)) {
                throw new CommandException("commands.execute.failed", new Object[] { "detect", var3.getName() });
            }
            var8 = 10;
        }
        final String var17 = CommandBase.func_180529_a(args, var8);
        final ICommandSender var18 = new ICommandSender() {
            private static final String __OBFID = "CL_00002343";
            
            @Override
            public String getName() {
                return var3.getName();
            }
            
            @Override
            public IChatComponent getDisplayName() {
                return var3.getDisplayName();
            }
            
            @Override
            public void addChatMessage(final IChatComponent message) {
                sender.addChatMessage(message);
            }
            
            @Override
            public boolean canCommandSenderUseCommand(final int permissionLevel, final String command) {
                return sender.canCommandSenderUseCommand(permissionLevel, command);
            }
            
            @Override
            public BlockPos getPosition() {
                return var7;
            }
            
            @Override
            public Vec3 getPositionVector() {
                return new Vec3(var4, var5, var6);
            }
            
            @Override
            public World getEntityWorld() {
                return var3.worldObj;
            }
            
            @Override
            public Entity getCommandSenderEntity() {
                return var3;
            }
            
            @Override
            public boolean sendCommandFeedback() {
                final MinecraftServer var1 = MinecraftServer.getServer();
                return var1 == null || var1.worldServers[0].getGameRules().getGameRuleBooleanValue("commandBlockOutput");
            }
            
            @Override
            public void func_174794_a(final CommandResultStats.Type p_174794_1_, final int p_174794_2_) {
                var3.func_174794_a(p_174794_1_, p_174794_2_);
            }
        };
        final ICommandManager var19 = MinecraftServer.getServer().getCommandManager();
        try {
            final int var20 = var19.executeCommand(var18, var17);
            if (var20 < 1) {
                throw new CommandException("commands.execute.allInvocationsFailed", new Object[] { var17 });
            }
        }
        catch (Throwable var21) {
            throw new CommandException("commands.execute.failed", new Object[] { var17, var3.getName() });
        }
    }
    
    @Override
    public List addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos) {
        return (args.length == 1) ? CommandBase.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames()) : ((args.length > 1 && args.length <= 4) ? CommandBase.func_175771_a(args, 1, pos) : ((args.length > 5 && args.length <= 8 && "detect".equals(args[4])) ? CommandBase.func_175771_a(args, 5, pos) : ((args.length == 9 && "detect".equals(args[4])) ? CommandBase.func_175762_a(args, Block.blockRegistry.getKeys()) : null)));
    }
    
    @Override
    public boolean isUsernameIndex(final String[] args, final int index) {
        return index == 0;
    }
}
