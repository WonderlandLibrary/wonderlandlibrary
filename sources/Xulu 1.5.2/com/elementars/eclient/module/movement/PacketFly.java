// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.movement;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.common.MinecraftForge;
import com.elementars.eclient.module.Category;
import dev.xulu.settings.Value;
import com.elementars.eclient.module.Module;

public class PacketFly extends Module
{
    private final Value<Boolean> defaults;
    private final Value<Short> cooldown;
    private final Value<Float> fallSpeed;
    private final Value<Float> upSpeed;
    private float counter;
    int j;
    
    public PacketFly() {
        super("PacketFly", "Flies with packets", 0, Category.MOVEMENT, true);
        this.defaults = this.register(new Value<Boolean>("Defaults", this, false));
        this.cooldown = this.register(new Value<Short>("Cooldown", this, (Short)0, (Short)0, (Short)10));
        this.fallSpeed = this.register(new Value<Float>("Fall Speed", this, 0.005f, 0.0f, 10.0f));
        this.upSpeed = this.register(new Value<Float>("Up Speed", this, 0.05f, 0.0f, 10.0f));
        this.counter = 0.0f;
    }
    
    @Override
    public void onUpdate() {
        if (this.defaults.getValue()) {
            this.cooldown.setValue((Short)0);
            this.fallSpeed.setValue(0.005f);
            this.upSpeed.setValue(0.05f);
        }
    }
    
    @Override
    public void onEnable() {
        MinecraftForge.EVENT_BUS.register((Object)this);
        this.j = 0;
    }
    
    @Override
    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister((Object)this);
    }
    
    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event) {
        if (PacketFly.mc.player == null) {
            return;
        }
        if (event.phase == TickEvent.Phase.END) {
            if (!PacketFly.mc.player.isElytraFlying()) {
                if (this.counter < 1.0f) {
                    this.counter += this.cooldown.getValue();
                    PacketFly.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(PacketFly.mc.player.posX, PacketFly.mc.player.posY, PacketFly.mc.player.posZ, PacketFly.mc.player.onGround));
                    PacketFly.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(PacketFly.mc.player.posX, PacketFly.mc.player.posY - 0.03, PacketFly.mc.player.posZ, PacketFly.mc.player.onGround));
                    PacketFly.mc.player.connection.sendPacket((Packet)new CPacketConfirmTeleport(++this.j));
                }
                else {
                    --this.counter;
                }
            }
        }
        else if (PacketFly.mc.gameSettings.keyBindJump.isPressed()) {
            PacketFly.mc.player.motionY = this.upSpeed.getValue();
        }
        else {
            PacketFly.mc.player.motionY = -this.fallSpeed.getValue();
        }
    }
}
