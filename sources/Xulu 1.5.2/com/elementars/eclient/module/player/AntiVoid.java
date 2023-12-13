// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.player;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import com.elementars.eclient.event.EventTarget;
import net.minecraft.network.play.server.SPacketDisconnect;
import com.elementars.eclient.event.events.EventReceivePacket;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import java.util.HashSet;
import com.elementars.eclient.module.Category;
import java.util.Set;
import dev.xulu.settings.Value;
import com.elementars.eclient.module.Module;

public class AntiVoid extends Module
{
    private final Value<String> mode;
    private final Value<Integer> y_level;
    public Set<String> ipList;
    public static AntiVoid INSTANCE;
    private boolean wasElytraFlying;
    int delay;
    
    public AntiVoid() {
        super("AntiVoid", "Prevents death from afk flying", 0, Category.PLAYER, true);
        this.mode = this.register(new Value<String>("Mode", this, "Normal", new String[] { "AFK Elytra", "Normal" }));
        this.y_level = this.register(new Value<Integer>("Y Level", this, 100, 0, 256));
        this.ipList = new HashSet<String>();
        AntiVoid.INSTANCE = this;
    }
    
    @Override
    public void onEnable() {
        FMLCommonHandler.instance().bus().register((Object)this);
    }
    
    @Override
    public void onDisable() {
        FMLCommonHandler.instance().bus().unregister((Object)this);
    }
    
    private boolean isOverVoid() {
        return AntiVoid.mc.world.getBlockState(new BlockPos((int)AntiVoid.mc.player.posX, 0, (int)AntiVoid.mc.player.posZ)).getBlock() == Blocks.AIR;
    }
    
    @Override
    public void onUpdate() {
        if (AntiVoid.mc.player != null && AntiVoid.mc.world != null) {
            if (this.mode.getValue().equalsIgnoreCase("Normal")) {
                boolean isVoid = true;
                for (int i = (int)AntiVoid.mc.player.posY; i > -1; --i) {
                    if (AntiVoid.mc.world.getBlockState(new BlockPos(AntiVoid.mc.player.posX, (double)i, AntiVoid.mc.player.posZ)).getBlock() != Blocks.AIR) {
                        isVoid = false;
                        break;
                    }
                }
                if (AntiVoid.mc.player.posY < this.y_level.getValue() && isVoid) {
                    AntiVoid.mc.player.motionY = 0.0;
                }
            }
            else if (AntiVoid.mc.player.isElytraFlying()) {
                this.wasElytraFlying = true;
            }
            else if (AntiVoid.mc.player.posY < this.y_level.getValue() && this.wasElytraFlying && this.isOverVoid()) {
                AntiVoid.mc.world.sendQuittingDisconnectingPacket();
                this.wasElytraFlying = false;
            }
        }
    }
    
    @EventTarget
    public void onPacket(final EventReceivePacket event) {
        if (event.getPacket() instanceof SPacketDisconnect && AntiVoid.mc.getCurrentServerData() != null && AntiVoid.mc.player != null) {
            this.ipList.add(AntiVoid.mc.getCurrentServerData().serverIP);
        }
    }
    
    @SubscribeEvent
    public void onDisconnect(final FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        if (AntiVoid.mc.getCurrentServerData() != null && AntiVoid.mc.player != null) {
            this.ipList.add(AntiVoid.mc.getCurrentServerData().serverIP);
        }
    }
    
    @SubscribeEvent
    public void onConnect(final FMLNetworkEvent.ClientConnectedToServerEvent event) {
        if (AntiVoid.mc.getCurrentServerData() != null) {
            this.ipList.remove(AntiVoid.mc.getCurrentServerData().serverIP);
        }
    }
}
