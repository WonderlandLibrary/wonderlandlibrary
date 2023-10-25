/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 */
package net.minecraft.tileentity;

import io.netty.buffer.ByteBuf;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.annotation.Nullable;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommandSender;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ReportedException;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public abstract class CommandBlockBaseLogic
implements ICommandSender {
    private static final SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("HH:mm:ss");
    private long field_193041_b = -1L;
    private boolean field_193042_c = true;
    private int successCount;
    private boolean trackOutput = true;
    private ITextComponent lastOutput;
    private String commandStored = "";
    private String customName = "@";
    private final CommandResultStats resultStats = new CommandResultStats();

    public int getSuccessCount() {
        return this.successCount;
    }

    public void setSuccessCount(int successCountIn) {
        this.successCount = successCountIn;
    }

    public ITextComponent getLastOutput() {
        return this.lastOutput == null ? new TextComponentString("") : this.lastOutput;
    }

    public NBTTagCompound writeToNBT(NBTTagCompound p_189510_1_) {
        p_189510_1_.setString("Command", this.commandStored);
        p_189510_1_.setInteger("SuccessCount", this.successCount);
        p_189510_1_.setString("CustomName", this.customName);
        p_189510_1_.setBoolean("TrackOutput", this.trackOutput);
        if (this.lastOutput != null && this.trackOutput) {
            p_189510_1_.setString("LastOutput", ITextComponent.Serializer.componentToJson(this.lastOutput));
        }
        p_189510_1_.setBoolean("UpdateLastExecution", this.field_193042_c);
        if (this.field_193042_c && this.field_193041_b > 0L) {
            p_189510_1_.setLong("LastExecution", this.field_193041_b);
        }
        this.resultStats.writeStatsToNBT(p_189510_1_);
        return p_189510_1_;
    }

    public void readDataFromNBT(NBTTagCompound nbt) {
        this.commandStored = nbt.getString("Command");
        this.successCount = nbt.getInteger("SuccessCount");
        if (nbt.hasKey("CustomName", 8)) {
            this.customName = nbt.getString("CustomName");
        }
        if (nbt.hasKey("TrackOutput", 1)) {
            this.trackOutput = nbt.getBoolean("TrackOutput");
        }
        if (nbt.hasKey("LastOutput", 8) && this.trackOutput) {
            try {
                this.lastOutput = ITextComponent.Serializer.jsonToComponent(nbt.getString("LastOutput"));
            }
            catch (Throwable throwable) {
                this.lastOutput = new TextComponentString(throwable.getMessage());
            }
        } else {
            this.lastOutput = null;
        }
        if (nbt.hasKey("UpdateLastExecution")) {
            this.field_193042_c = nbt.getBoolean("UpdateLastExecution");
        }
        this.field_193041_b = this.field_193042_c && nbt.hasKey("LastExecution") ? nbt.getLong("LastExecution") : -1L;
        this.resultStats.readStatsFromNBT(nbt);
    }

    @Override
    public boolean canCommandSenderUseCommand(int permLevel, String commandName) {
        return permLevel <= 2;
    }

    public void setCommand(String command) {
        this.commandStored = command;
        this.successCount = 0;
    }

    public String getCommand() {
        return this.commandStored;
    }

    public boolean trigger(World worldIn) {
        if (!worldIn.isRemote && worldIn.getTotalWorldTime() != this.field_193041_b) {
            if ("Searge".equalsIgnoreCase(this.commandStored)) {
                this.lastOutput = new TextComponentString("#itzlipofutzli");
                this.successCount = 1;
                return true;
            }
            MinecraftServer minecraftserver = this.getServer();
            if (minecraftserver != null && minecraftserver.isAnvilFileSet() && minecraftserver.isCommandBlockEnabled()) {
                try {
                    this.lastOutput = null;
                    this.successCount = minecraftserver.getCommandManager().executeCommand(this, this.commandStored);
                }
                catch (Throwable throwable) {
                    CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Executing command block");
                    CrashReportCategory crashreportcategory = crashreport.makeCategory("Command to be executed");
                    crashreportcategory.setDetail("Command", new ICrashReportDetail<String>(){

                        @Override
                        public String call() throws Exception {
                            return CommandBlockBaseLogic.this.getCommand();
                        }
                    });
                    crashreportcategory.setDetail("Name", new ICrashReportDetail<String>(){

                        @Override
                        public String call() throws Exception {
                            return CommandBlockBaseLogic.this.getName();
                        }
                    });
                    throw new ReportedException(crashreport);
                }
            } else {
                this.successCount = 0;
            }
            this.field_193041_b = this.field_193042_c ? worldIn.getTotalWorldTime() : -1L;
            return true;
        }
        return false;
    }

    @Override
    public String getName() {
        return this.customName;
    }

    public void setName(String name) {
        this.customName = name;
    }

    @Override
    public void addChatMessage(ITextComponent component) {
        if (this.trackOutput && this.getEntityWorld() != null && !this.getEntityWorld().isRemote) {
            this.lastOutput = new TextComponentString("[" + TIMESTAMP_FORMAT.format(new Date()) + "] ").appendSibling(component);
            this.updateCommand();
        }
    }

    @Override
    public boolean sendCommandFeedback() {
        MinecraftServer minecraftserver = this.getServer();
        return minecraftserver == null || !minecraftserver.isAnvilFileSet() || minecraftserver.worldServers[0].getGameRules().getBoolean("commandBlockOutput");
    }

    @Override
    public void setCommandStat(CommandResultStats.Type type, int amount) {
        this.resultStats.setCommandStatForSender(this.getServer(), this, type, amount);
    }

    public abstract void updateCommand();

    public abstract int getCommandBlockType();

    public abstract void fillInInfo(ByteBuf var1);

    public void setLastOutput(@Nullable ITextComponent lastOutputMessage) {
        this.lastOutput = lastOutputMessage;
    }

    public void setTrackOutput(boolean shouldTrackOutput) {
        this.trackOutput = shouldTrackOutput;
    }

    public boolean shouldTrackOutput() {
        return this.trackOutput;
    }

    public boolean tryOpenEditCommandBlock(EntityPlayer playerIn) {
        if (!playerIn.canUseCommandBlock()) {
            return false;
        }
        if (playerIn.getEntityWorld().isRemote) {
            playerIn.displayGuiEditCommandCart(this);
        }
        return true;
    }

    public CommandResultStats getCommandResultStats() {
        return this.resultStats;
    }
}

