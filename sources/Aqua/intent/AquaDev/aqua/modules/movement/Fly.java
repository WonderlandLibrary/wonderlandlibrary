package intent.AquaDev.aqua.modules.movement;

import de.Hero.settings.Setting;
import events.Event;
import events.listeners.EventUpdate;
import intent.AquaDev.aqua.Aqua;
import intent.AquaDev.aqua.modules.Category;
import intent.AquaDev.aqua.modules.Module;
import intent.AquaDev.aqua.utils.PlayerUtil;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class Fly
extends Module {
    public boolean verusDmg;

    public Fly() {
        super("Fly", "Fly", 0, Category.Movement);
        Aqua.setmgr.register(new Setting("MotionReset", (Module)this, true));
        Aqua.setmgr.register(new Setting("Boost", (Module)this, 3.0, 0.3, 9.0, false));
        Aqua.setmgr.register(new Setting("Mode", (Module)this, "Motion", new String[]{"Motion", "Hypixel", "Minemora", "Verus", "Verus2", "Verus3", "Creative"}));
    }

    public void onEnable() {
        if (Aqua.setmgr.getSetting("FlyMode").getCurrentMode().equalsIgnoreCase("Verus3")) {
            Fly.sendPacketUnlogged((Packet<? extends INetHandler>)new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, new ItemStack(Items.water_bucket), 0.0f, 0.5f, 0.0f));
            Fly.sendPacketUnlogged((Packet<? extends INetHandler>)new C08PacketPlayerBlockPlacement(new BlockPos(Fly.getX(), Fly.getY() - 1.5, Fly.getZ()), 1, new ItemStack(Blocks.stone.getItem((World)Fly.mc.theWorld, new BlockPos(-1, -1, -1))), 0.0f, 0.94f, 0.0f));
        }
        if (Aqua.setmgr.getSetting("FlyMode").getCurrentMode().equalsIgnoreCase("Verus")) {
            Fly.sendPacketUnlogged((Packet<? extends INetHandler>)new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, new ItemStack(Items.water_bucket), 0.0f, 0.5f, 0.0f));
            Fly.sendPacketUnlogged((Packet<? extends INetHandler>)new C08PacketPlayerBlockPlacement(new BlockPos(Fly.getX(), Fly.getY() - 1.5, Fly.getZ()), 1, new ItemStack(Blocks.stone.getItem((World)Fly.mc.theWorld, new BlockPos(-1, -1, -1))), 0.0f, 0.94f, 0.0f));
            mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.thePlayer.posX, Fly.mc.thePlayer.posY + 3.001, Fly.mc.thePlayer.posZ, false));
            mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.thePlayer.posX, Fly.mc.thePlayer.posY, Fly.mc.thePlayer.posZ, false));
            mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.thePlayer.posX, Fly.mc.thePlayer.posY, Fly.mc.thePlayer.posZ, true));
        }
        if (Aqua.setmgr.getSetting("FlyMode").getCurrentMode().equalsIgnoreCase("Verus2")) {
            Fly.sendPacketUnlogged((Packet<? extends INetHandler>)new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, new ItemStack(Items.water_bucket), 0.0f, 0.5f, 0.0f));
            Fly.sendPacketUnlogged((Packet<? extends INetHandler>)new C08PacketPlayerBlockPlacement(new BlockPos(Fly.getX(), Fly.getY() - 1.5, Fly.getZ()), 1, new ItemStack(Blocks.stone.getItem((World)Fly.mc.theWorld, new BlockPos(-1, -1, -1))), 0.0f, 0.94f, 0.0f));
            mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.thePlayer.posX, Fly.mc.thePlayer.posY + 3.001, Fly.mc.thePlayer.posZ, false));
            mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.thePlayer.posX, Fly.mc.thePlayer.posY, Fly.mc.thePlayer.posZ, false));
            mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.thePlayer.posX, Fly.mc.thePlayer.posY, Fly.mc.thePlayer.posZ, true));
        }
        super.onEnable();
    }

    public void onDisable() {
        this.verusDmg = false;
        Fly.mc.thePlayer.capabilities.isFlying = false;
        Fly.mc.thePlayer.capabilities.isCreativeMode = false;
        Fly.mc.timer.timerSpeed = 1.0f;
        if (Aqua.setmgr.getSetting("FlyMotionReset").isState()) {
            Fly.mc.thePlayer.motionZ = 0.0;
            Fly.mc.thePlayer.motionX = 0.0;
        }
        super.onDisable();
    }

    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {
            if (Aqua.setmgr.getSetting("FlyMode").getCurrentMode().equalsIgnoreCase("Hypixel")) {
                Fly.mc.thePlayer.motionY = 0.0;
                if (Fly.mc.thePlayer.ticksExisted % 25 == 0) {
                    double yaw = Math.toRadians((double)Fly.mc.thePlayer.rotationYaw);
                    double x3 = -Math.sin((double)yaw) * 7.0;
                    double z3 = Math.cos((double)yaw) * 7.0;
                    double y3 = 1.75;
                    mc.getNetHandler().getNetworkManager().sendPacket((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.thePlayer.posX + x3, Fly.mc.thePlayer.posY - y3, Fly.mc.thePlayer.posZ + z3, false));
                }
            }
            if (Aqua.setmgr.getSetting("FlyMode").getCurrentMode().equalsIgnoreCase("Creative")) {
                Fly.mc.thePlayer.capabilities.isFlying = true;
                Fly.mc.thePlayer.capabilities.isCreativeMode = true;
            }
            if (Aqua.setmgr.getSetting("FlyMode").getCurrentMode().equalsIgnoreCase("Motion")) {
                Fly.mc.thePlayer.motionY = Fly.mc.gameSettings.keyBindJump.pressed ? 1.0 : (Fly.mc.gameSettings.keyBindSneak.pressed ? -1.0 : 0.0);
                if (Fly.mc.thePlayer.isMoving()) {
                    PlayerUtil.setSpeed((double)4.0);
                } else {
                    Fly.mc.thePlayer.motionX = 0.0;
                    Fly.mc.thePlayer.motionZ = 0.0;
                }
            }
            if (Aqua.setmgr.getSetting("FlyMode").getCurrentMode().equalsIgnoreCase("Verus3")) {
                PlayerUtil.setSpeed((double)PlayerUtil.getSpeed());
                Fly.mc.thePlayer.onGround = true;
                if (Fly.mc.gameSettings.keyBindJump.pressed) {
                    Fly.mc.thePlayer.motionY = 1.0;
                    Fly.mc.timer.timerSpeed = 1.0f;
                }
                Fly.sendPacketUnlogged((Packet<? extends INetHandler>)new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, new ItemStack(Items.water_bucket), 0.0f, 0.5f, 0.0f));
                Fly.sendPacketUnlogged((Packet<? extends INetHandler>)new C08PacketPlayerBlockPlacement(new BlockPos(Fly.getX(), Fly.getY() - 1.5, Fly.getZ()), 1, new ItemStack(Blocks.stone.getItem((World)Fly.mc.theWorld, new BlockPos(-1, -1, -1))), 0.0f, 0.94f, 0.0f));
                if (Fly.mc.thePlayer.ticksExisted % 5 == 0) {
                    if (!Fly.mc.gameSettings.keyBindJump.pressed) {
                        Fly.mc.thePlayer.motionY = 0.17f;
                    }
                } else {
                    Fly.mc.timer.timerSpeed = 1.0f;
                }
            }
            if (Aqua.setmgr.getSetting("FlyMode").getCurrentMode().equalsIgnoreCase("Verus2")) {
                Fly.mc.thePlayer.onGround = true;
                Fly.sendPacketUnlogged((Packet<? extends INetHandler>)new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, new ItemStack(Items.water_bucket), 0.0f, 0.5f, 0.0f));
                Fly.sendPacketUnlogged((Packet<? extends INetHandler>)new C08PacketPlayerBlockPlacement(new BlockPos(Fly.getX(), Fly.getY() - 1.5, Fly.getZ()), 1, new ItemStack(Blocks.stone.getItem((World)Fly.mc.theWorld, new BlockPos(-1, -1, -1))), 0.0f, 0.94f, 0.0f));
                float speed = (float)Aqua.setmgr.getSetting("FlyBoost").getCurrentNumber();
                if (Fly.mc.thePlayer.hurtTime != 0) {
                    PlayerUtil.setSpeed((double)speed);
                    Fly.mc.timer.timerSpeed = 0.6f;
                } else {
                    PlayerUtil.setSpeed((double)PlayerUtil.getSpeed());
                    Fly.mc.timer.timerSpeed = 1.0f;
                }
                Fly.mc.thePlayer.motionY = !Fly.mc.gameSettings.keyBindJump.pressed && Fly.mc.gameSettings.keyBindSneak.pressed ? -1.0 : (Fly.mc.gameSettings.keyBindJump.pressed && !Fly.mc.gameSettings.keyBindSneak.pressed ? 1.0 : 0.0);
            }
            if (Aqua.setmgr.getSetting("FlyMode").getCurrentMode().equalsIgnoreCase("Verus")) {
                Fly.sendPacketUnlogged((Packet<? extends INetHandler>)new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, new ItemStack(Items.water_bucket), 0.0f, 0.5f, 0.0f));
                Fly.sendPacketUnlogged((Packet<? extends INetHandler>)new C08PacketPlayerBlockPlacement(new BlockPos(Fly.getX(), Fly.getY() - 1.5, Fly.getZ()), 1, new ItemStack(Blocks.stone.getItem((World)Fly.mc.theWorld, new BlockPos(-1, -1, -1))), 0.0f, 0.94f, 0.0f));
                Fly.mc.thePlayer.onGround = true;
                if (Fly.mc.thePlayer.hurtTime != 0) {
                    this.verusDmg = true;
                }
                if (this.verusDmg) {
                    Fly.mc.thePlayer.motionY = !Fly.mc.gameSettings.keyBindJump.pressed && Fly.mc.gameSettings.keyBindSneak.pressed ? 0.0 : (Fly.mc.gameSettings.keyBindJump.pressed && !Fly.mc.gameSettings.keyBindSneak.pressed ? 0.0 : 0.0);
                    Fly.mc.timer.timerSpeed = 0.2f;
                    PlayerUtil.setSpeed((double)5.0);
                } else {
                    Fly.mc.timer.timerSpeed = 0.2f;
                }
            }
            if (Aqua.setmgr.getSetting("FlyMode").getCurrentMode().equalsIgnoreCase("Minemora")) {
                if (Fly.mc.thePlayer.ticksExisted % 4 == 0) {
                    Fly.mc.thePlayer.motionY = 0.02;
                    Fly.mc.timer.timerSpeed = 0.6f;
                } else {
                    Fly.mc.timer.timerSpeed = 0.8f;
                }
            }
        }
    }

    public static void sendPacketUnlogged(Packet<? extends INetHandler> packet) {
        mc.getNetHandler().getNetworkManager().sendPacket(packet);
    }

    public static double getX() {
        return Fly.mc.thePlayer.posX;
    }

    public static double getY() {
        return Fly.mc.thePlayer.posY;
    }

    public static double getZ() {
        return Fly.mc.thePlayer.posZ;
    }
}
