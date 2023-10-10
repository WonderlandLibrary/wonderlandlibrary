/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.AveReborn.mod.mods.MOVEMENT;

import com.darkmagician6.eventapi.EventTarget;
import java.util.ArrayList;
import me.AveReborn.Value;
import me.AveReborn.events.EventMove;
import me.AveReborn.events.EventPostMotion;
import me.AveReborn.mod.Category;
import me.AveReborn.mod.Mod;
import me.AveReborn.util.PlayerUtil;
import me.AveReborn.util.Vec3Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Timer;
import net.minecraft.util.Vec3;

public class Fly
extends Mod {
    public int counter2 = 0;
    public Value<String> mode = new Value("Fly", "Mode", 0);

    public Fly() {
        super("Fly", Category.MOVEMENT);
        this.mode.mode.add("Hypixel");
        this.mode.mode.add("DAC");
    }

    @Override
    public void onEnable() {
        if (this.mode.isCurrentMode("Hypixel")) {
            Minecraft.thePlayer.setPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 0.2, Minecraft.thePlayer.posZ);
        }
        super.onEnable();
    }

    @EventTarget
    public void onPre(EventPostMotion event) {
        if (this.mode.isCurrentMode("DAC")) {
            this.setDisplayName("DAC");
            if (Minecraft.thePlayer.fallDistance > 3.0f) {
                this.mc.timer.timerSpeed = 0.1f;
                Minecraft.thePlayer.motionY *= 0.6000000238418579;
                Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
                Vec3 pos = new Vec3(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ);
                Vec3Util vec = new Vec3Util(pos, - Minecraft.thePlayer.rotationYaw, 0.0f, 7.0);
                Minecraft.thePlayer.setPosition(vec.getEndVector().xCoord, vec.getEndVector().yCoord, vec.getEndVector().zCoord);
                Minecraft.thePlayer.fallDistance = 0.0f;
            } else {
                this.mc.timer.timerSpeed = 1.0f;
            }
        }
    }

    @EventTarget
    public void onMove(EventMove event) {
        if (this.mode.isCurrentMode("Hypixel")) {
            this.setDisplayName("Hypixel " + Minecraft.thePlayer.posY);
            Minecraft.thePlayer.onGround = false;
            Minecraft.thePlayer.capabilities.isFlying = false;
            if (this.mc.gameSettings.keyBindSneak.isKeyDown()) {
                Minecraft.thePlayer.motionY *= 0.0;
            } else if (this.mc.gameSettings.keyBindJump.isKeyDown()) {
                Minecraft.thePlayer.motionY *= 0.0;
            }
            if (PlayerUtil.MovementInput()) {
                this.setSpeed(PlayerUtil.getBaseMoveSpeed() - 0.05);
            } else {
                this.setSpeed(0.0);
            }
            ++this.counter2;
            if (PlayerUtil.MovementInput()) {
                this.setSpeed(PlayerUtil.getBaseMoveSpeed());
            } else {
                Minecraft.thePlayer.motionX *= 0.0;
                Minecraft.thePlayer.motionZ *= 0.0;
                this.mc.timer.timerSpeed = 1.0f;
            }
            Minecraft.thePlayer.setPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ);
            switch (this.counter2) {
                case 1: {
                    break;
                }
                case 2: {
                    Minecraft.thePlayer.setPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 1.0E-5, Minecraft.thePlayer.posZ);
                    this.counter2 = 0;
                    break;
                }
                case 3: {
                    Minecraft.thePlayer.setPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY - 1.0E-5, Minecraft.thePlayer.posZ);
                    this.counter2 = 0;
                }
            }
            Minecraft.thePlayer.motionY = 0.0;
            event.y = 0.0;
        }
    }

    public void setSpeed(double speed) {
        Minecraft.thePlayer.motionX = (double)(- MathHelper.sin(PlayerUtil.getDirection())) * speed;
        Minecraft.thePlayer.motionZ = (double)MathHelper.cos(PlayerUtil.getDirection()) * speed;
    }

    @Override
    public void onDisable() {
        if (!Minecraft.thePlayer.capabilities.isCreativeMode) {
            Minecraft.thePlayer.capabilities.isFlying = false;
            Minecraft.thePlayer.onGround = false;
            Minecraft.thePlayer.capabilities.allowFlying = false;
        }
        this.mc.timer.timerSpeed = 1.0f;
        Minecraft.thePlayer.speedInAir = 0.02f;
        super.onDisable();
    }
}

