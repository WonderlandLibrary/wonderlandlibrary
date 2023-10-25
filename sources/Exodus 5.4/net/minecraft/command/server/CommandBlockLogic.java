/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.command.server;

import io.netty.buffer.ByteBuf;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Callable;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ReportedException;
import net.minecraft.world.World;

public abstract class CommandBlockLogic
implements ICommandSender {
    private boolean trackOutput = true;
    private String commandStored = "";
    private static final SimpleDateFormat timestampFormat = new SimpleDateFormat("HH:mm:ss");
    private IChatComponent lastOutput = null;
    private int successCount;
    private final CommandResultStats resultStats = new CommandResultStats();
    private String customName = "@";

    @Override
    public IChatComponent getDisplayName() {
        return new ChatComponentText(this.getName());
    }

    public void setTrackOutput(boolean bl) {
        this.trackOutput = bl;
    }

    public int getSuccessCount() {
        return this.successCount;
    }

    @Override
    public String getName() {
        return this.customName;
    }

    public void setCommand(String string) {
        this.commandStored = string;
        this.successCount = 0;
    }

    public void trigger(World world) {
        MinecraftServer minecraftServer;
        if (world.isRemote) {
            this.successCount = 0;
        }
        if ((minecraftServer = MinecraftServer.getServer()) != null && minecraftServer.isAnvilFileSet() && minecraftServer.isCommandBlockEnabled()) {
            ICommandManager iCommandManager = minecraftServer.getCommandManager();
            try {
                this.lastOutput = null;
                this.successCount = iCommandManager.executeCommand(this, this.commandStored);
            }
            catch (Throwable throwable) {
                CrashReport crashReport = CrashReport.makeCrashReport(throwable, "Executing command block");
                CrashReportCategory crashReportCategory = crashReport.makeCategory("Command to be executed");
                crashReportCategory.addCrashSectionCallable("Command", new Callable<String>(){

                    @Override
                    public String call() throws Exception {
                        return CommandBlockLogic.this.getCommand();
                    }
                });
                crashReportCategory.addCrashSectionCallable("Name", new Callable<String>(){

                    @Override
                    public String call() throws Exception {
                        return CommandBlockLogic.this.getName();
                    }
                });
                throw new ReportedException(crashReport);
            }
        } else {
            this.successCount = 0;
        }
    }

    public IChatComponent getLastOutput() {
        return this.lastOutput;
    }

    @Override
    public boolean canCommandSenderUseCommand(int n, String string) {
        return n <= 2;
    }

    public boolean tryOpenEditCommandBlock(EntityPlayer entityPlayer) {
        if (!entityPlayer.capabilities.isCreativeMode) {
            return false;
        }
        if (entityPlayer.getEntityWorld().isRemote) {
            entityPlayer.openEditCommandBlock(this);
        }
        return true;
    }

    public abstract void updateCommand();

    public String getCommand() {
        return this.commandStored;
    }

    public void setName(String string) {
        this.customName = string;
    }

    public boolean shouldTrackOutput() {
        return this.trackOutput;
    }

    public abstract int func_145751_f();

    public abstract void func_145757_a(ByteBuf var1);

    @Override
    public void setCommandStat(CommandResultStats.Type type, int n) {
        this.resultStats.func_179672_a(this, type, n);
    }

    public void readDataFromNBT(NBTTagCompound nBTTagCompound) {
        this.commandStored = nBTTagCompound.getString("Command");
        this.successCount = nBTTagCompound.getInteger("SuccessCount");
        if (nBTTagCompound.hasKey("CustomName", 8)) {
            this.customName = nBTTagCompound.getString("CustomName");
        }
        if (nBTTagCompound.hasKey("TrackOutput", 1)) {
            this.trackOutput = nBTTagCompound.getBoolean("TrackOutput");
        }
        if (nBTTagCompound.hasKey("LastOutput", 8) && this.trackOutput) {
            this.lastOutput = IChatComponent.Serializer.jsonToComponent(nBTTagCompound.getString("LastOutput"));
        }
        this.resultStats.readStatsFromNBT(nBTTagCompound);
    }

    public void writeDataToNBT(NBTTagCompound nBTTagCompound) {
        nBTTagCompound.setString("Command", this.commandStored);
        nBTTagCompound.setInteger("SuccessCount", this.successCount);
        nBTTagCompound.setString("CustomName", this.customName);
        nBTTagCompound.setBoolean("TrackOutput", this.trackOutput);
        if (this.lastOutput != null && this.trackOutput) {
            nBTTagCompound.setString("LastOutput", IChatComponent.Serializer.componentToJson(this.lastOutput));
        }
        this.resultStats.writeStatsToNBT(nBTTagCompound);
    }

    public CommandResultStats getCommandResultStats() {
        return this.resultStats;
    }

    public void setLastOutput(IChatComponent iChatComponent) {
        this.lastOutput = iChatComponent;
    }

    @Override
    public void addChatMessage(IChatComponent iChatComponent) {
        if (this.trackOutput && this.getEntityWorld() != null && !this.getEntityWorld().isRemote) {
            this.lastOutput = new ChatComponentText("[" + timestampFormat.format(new Date()) + "] ").appendSibling(iChatComponent);
            this.updateCommand();
        }
    }

    @Override
    public boolean sendCommandFeedback() {
        MinecraftServer minecraftServer = MinecraftServer.getServer();
        return minecraftServer == null || !minecraftServer.isAnvilFileSet() || minecraftServer.worldServers[0].getGameRules().getBoolean("commandBlockOutput");
    }
}

