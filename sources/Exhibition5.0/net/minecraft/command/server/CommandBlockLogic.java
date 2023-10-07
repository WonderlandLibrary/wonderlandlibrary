// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.command.server;

import net.minecraft.entity.player.EntityPlayer;
import io.netty.buffer.ByteBuf;
import java.util.Date;
import net.minecraft.util.ChatComponentText;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.command.ICommandManager;
import net.minecraft.util.ReportedException;
import java.util.concurrent.Callable;
import net.minecraft.crash.CrashReport;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.command.CommandResultStats;
import net.minecraft.util.IChatComponent;
import java.text.SimpleDateFormat;
import net.minecraft.command.ICommandSender;

public abstract class CommandBlockLogic implements ICommandSender
{
    private static final SimpleDateFormat timestampFormat;
    private int successCount;
    private boolean trackOutput;
    private IChatComponent lastOutput;
    private String commandStored;
    private String customName;
    private final CommandResultStats field_175575_g;
    private static final String __OBFID = "CL_00000128";
    
    public CommandBlockLogic() {
        this.trackOutput = true;
        this.lastOutput = null;
        this.commandStored = "";
        this.customName = "@";
        this.field_175575_g = new CommandResultStats();
    }
    
    public int getSuccessCount() {
        return this.successCount;
    }
    
    public IChatComponent getLastOutput() {
        return this.lastOutput;
    }
    
    public void writeDataToNBT(final NBTTagCompound p_145758_1_) {
        p_145758_1_.setString("Command", this.commandStored);
        p_145758_1_.setInteger("SuccessCount", this.successCount);
        p_145758_1_.setString("CustomName", this.customName);
        p_145758_1_.setBoolean("TrackOutput", this.trackOutput);
        if (this.lastOutput != null && this.trackOutput) {
            p_145758_1_.setString("LastOutput", IChatComponent.Serializer.componentToJson(this.lastOutput));
        }
        this.field_175575_g.func_179670_b(p_145758_1_);
    }
    
    public void readDataFromNBT(final NBTTagCompound p_145759_1_) {
        this.commandStored = p_145759_1_.getString("Command");
        this.successCount = p_145759_1_.getInteger("SuccessCount");
        if (p_145759_1_.hasKey("CustomName", 8)) {
            this.customName = p_145759_1_.getString("CustomName");
        }
        if (p_145759_1_.hasKey("TrackOutput", 1)) {
            this.trackOutput = p_145759_1_.getBoolean("TrackOutput");
        }
        if (p_145759_1_.hasKey("LastOutput", 8) && this.trackOutput) {
            this.lastOutput = IChatComponent.Serializer.jsonToComponent(p_145759_1_.getString("LastOutput"));
        }
        this.field_175575_g.func_179668_a(p_145759_1_);
    }
    
    @Override
    public boolean canCommandSenderUseCommand(final int permissionLevel, final String command) {
        return permissionLevel <= 2;
    }
    
    public void setCommand(final String p_145752_1_) {
        this.commandStored = p_145752_1_;
        this.successCount = 0;
    }
    
    public String getCustomName() {
        return this.commandStored;
    }
    
    public void trigger(final World worldIn) {
        if (worldIn.isRemote) {
            this.successCount = 0;
        }
        final MinecraftServer var2 = MinecraftServer.getServer();
        if (var2 != null && var2.func_175578_N() && var2.isCommandBlockEnabled()) {
            final ICommandManager var3 = var2.getCommandManager();
            try {
                this.lastOutput = null;
                this.successCount = var3.executeCommand(this, this.commandStored);
            }
            catch (Throwable var5) {
                final CrashReport var4 = CrashReport.makeCrashReport(var5, "Executing command block");
                final CrashReportCategory var6 = var4.makeCategory("Command to be executed");
                var6.addCrashSectionCallable("Command", new Callable() {
                    private static final String __OBFID = "CL_00002154";
                    
                    public String func_180324_a() {
                        return CommandBlockLogic.this.getCustomName();
                    }
                    
                    @Override
                    public Object call() {
                        return this.func_180324_a();
                    }
                });
                var6.addCrashSectionCallable("Name", new Callable() {
                    private static final String __OBFID = "CL_00002153";
                    
                    public String func_180326_a() {
                        return CommandBlockLogic.this.getName();
                    }
                    
                    @Override
                    public Object call() {
                        return this.func_180326_a();
                    }
                });
                throw new ReportedException(var4);
            }
        }
        else {
            this.successCount = 0;
        }
    }
    
    @Override
    public String getName() {
        return this.customName;
    }
    
    @Override
    public IChatComponent getDisplayName() {
        return new ChatComponentText(this.getName());
    }
    
    public void func_145754_b(final String p_145754_1_) {
        this.customName = p_145754_1_;
    }
    
    @Override
    public void addChatMessage(final IChatComponent message) {
        if (this.trackOutput && this.getEntityWorld() != null && !this.getEntityWorld().isRemote) {
            this.lastOutput = new ChatComponentText("[" + CommandBlockLogic.timestampFormat.format(new Date()) + "] ").appendSibling(message);
            this.func_145756_e();
        }
    }
    
    @Override
    public boolean sendCommandFeedback() {
        final MinecraftServer var1 = MinecraftServer.getServer();
        return var1 == null || !var1.func_175578_N() || var1.worldServers[0].getGameRules().getGameRuleBooleanValue("commandBlockOutput");
    }
    
    @Override
    public void func_174794_a(final CommandResultStats.Type p_174794_1_, final int p_174794_2_) {
        this.field_175575_g.func_179672_a(this, p_174794_1_, p_174794_2_);
    }
    
    public abstract void func_145756_e();
    
    public abstract int func_145751_f();
    
    public abstract void func_145757_a(final ByteBuf p0);
    
    public void func_145750_b(final IChatComponent p_145750_1_) {
        this.lastOutput = p_145750_1_;
    }
    
    public void func_175573_a(final boolean p_175573_1_) {
        this.trackOutput = p_175573_1_;
    }
    
    public boolean func_175571_m() {
        return this.trackOutput;
    }
    
    public boolean func_175574_a(final EntityPlayer p_175574_1_) {
        if (!p_175574_1_.capabilities.isCreativeMode) {
            return false;
        }
        if (p_175574_1_.getEntityWorld().isRemote) {
            p_175574_1_.func_146095_a(this);
        }
        return true;
    }
    
    public CommandResultStats func_175572_n() {
        return this.field_175575_g;
    }
    
    static {
        timestampFormat = new SimpleDateFormat("HH:mm:ss");
    }
}
