// 
// Decompiled by Procyon v0.5.36
// 

package Lavish.modules.movement;

import Lavish.utils.misc.NetUtil;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import Lavish.event.events.EventRenderGUI;
import Lavish.event.events.EventUpdate;
import Lavish.event.events.EventPacket;
import Lavish.event.Event;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.C0CPacketInput;
import Lavish.utils.movement.MovementUtil;
import Lavish.ClickGUI.settings.Setting;
import Lavish.Client;
import Lavish.modules.Category;
import net.minecraft.network.Packet;
import java.util.ArrayList;
import Lavish.utils.misc.Timer;
import Lavish.modules.Module;

public class Fly extends Module
{
    Timer timer;
    Timer anothertimer;
    public boolean noMovement;
    int ticks;
    int tickssd;
    boolean donesd;
    boolean done;
    public boolean state;
    public double positionX;
    public double positionY;
    public double positionZ;
    private boolean verusBool;
    private int stage;
    private double flightSpeed;
    public double ypos;
    public boolean damaged;
    public double speed;
    private int counter;
    boolean idk;
    public ArrayList<Packet> packets;
    
    public Fly() {
        super("Fly", 0, true, Category.Movement, "Makes you fly!");
        this.timer = new Timer();
        this.anothertimer = new Timer();
        this.ticks = 0;
        this.tickssd = 0;
        this.donesd = false;
        this.done = false;
        this.idk = false;
        this.packets = new ArrayList<Packet>();
        final ArrayList<String> options = new ArrayList<String>();
        options.add("Vanilla");
        options.add("Creative");
        options.add("Hypixel");
        options.add("SurvivalDub");
        options.add("SurvivalDub DMG");
        options.add("NCP");
        options.add("Collision");
        options.add("Verus");
        Client.instance.setmgr.rSetting(new Setting("Flight Mode", this, "Vanilla", options));
        Client.instance.setmgr.rSetting(new Setting("Fly Speed", this, 1.0, 0.05, 10.0, false));
    }
    
    @Override
    public void onUpdate() {
        if (Client.instance.setmgr.getSettingByName("Flight Mode").getValString().equalsIgnoreCase("Vanilla")) {
            Fly.mc.thePlayer.motionY = 0.0;
            MovementUtil.setSpeed(Client.instance.setmgr.getSettingByName("Fly Speed").getValDouble());
            if (Fly.mc.gameSettings.keyBindJump.isKeyDown()) {
                Fly.mc.thePlayer.motionY = Client.instance.setmgr.getSettingByName("Fly Speed").getValDouble() / 2.0;
            }
            if (Fly.mc.gameSettings.keyBindSneak.isKeyDown()) {
                final EntityPlayerSP thePlayer = Fly.mc.thePlayer;
                thePlayer.motionY -= Client.instance.setmgr.getSettingByName("Fly Speed").getValDouble() / 2.0;
            }
        }
        if (Client.instance.setmgr.getSettingByName("Flight Mode").getValString().equalsIgnoreCase("Creative")) {
            Fly.mc.thePlayer.capabilities.isFlying = true;
        }
        if (Client.instance.setmgr.getSettingByName("Flight Mode").getValString().equalsIgnoreCase("Hypixel")) {
            if (Fly.mc.thePlayer.onGround) {
                Fly.mc.thePlayer.jump();
            }
            if (this.timer.hasTimeElapsed(230.0, false)) {
                Fly.mc.thePlayer.motionY = 0.0;
                final EntityPlayerSP thePlayer2 = Fly.mc.thePlayer;
                thePlayer2.motionX *= 1.0449999570846558;
                final EntityPlayerSP thePlayer3 = Fly.mc.thePlayer;
                thePlayer3.motionZ *= 1.0449999570846558;
            }
            if (this.timer.hasTimeElapsed(650.0, false)) {
                this.toggle();
            }
        }
        if (Client.instance.setmgr.getSettingByName("Flight Mode").getValString().equalsIgnoreCase("SurvivalDub")) {
            if (this.timer.hasTimeElapsed(50.0, true)) {
                Fly.mc.thePlayer.sendQueue.addToSendQueue(new C0CPacketInput());
            }
            Fly.mc.thePlayer.motionY = -0.005499999970197678;
            if (Fly.mc.thePlayer != null) {
                Fly.mc.thePlayer.onGround = true;
            }
        }
        if (Client.instance.setmgr.getSettingByName("Flight Mode").getValString().equalsIgnoreCase("Verus")) {
            if (Fly.mc.thePlayer.hurtTime > 0) {
                this.damaged = true;
            }
            if (this.damaged) {
                Fly.mc.thePlayer.onGround = true;
                Fly.mc.thePlayer.motionY = 0.0;
                if (!this.done) {
                    if (this.ticks == 0) {
                        this.speed = Client.instance.setmgr.getSettingByName("Fly Speed").getValDouble();
                        Fly.mc.timer.timerSpeed = 0.2f;
                    }
                    ++this.ticks;
                    MovementUtil.setSpeed(this.speed);
                }
            }
        }
    }
    
    @Override
    public void onEvent(final Event e) {
        final boolean b = e instanceof EventPacket;
        final boolean b2 = e instanceof EventUpdate;
        final boolean b3 = e instanceof EventRenderGUI;
        if (Client.instance.setmgr.getSettingByName("Flight Mode").getValString().equalsIgnoreCase("Collision")) {
            Fly.mc.theWorld.setBlockState(new BlockPos(Fly.mc.thePlayer.posX, this.ypos, Fly.mc.thePlayer.posZ), Block.getStateById(166));
        }
    }
    
    @Override
    public void onDisable() {
        Fly.mc.thePlayer.capabilities.isFlying = false;
        Fly.mc.thePlayer.speedInAir = 0.02f;
        Fly.mc.timer.timerSpeed = 1.0f;
        Fly.mc.thePlayer.motionX = 0.0;
        Fly.mc.thePlayer.motionZ = 0.0;
        this.ticks = 0;
        this.tickssd = 0;
    }
    
    @Override
    public void onEnable() {
        this.ypos = Fly.mc.thePlayer.posY - 1.0;
        this.stage = 0;
        if (Client.instance.setmgr.getSettingByName("Flight Mode").getValString().equalsIgnoreCase("SurvivalDub")) {
            Fly.mc.thePlayer.jump();
            Fly.mc.thePlayer.jump();
        }
        if (Client.instance.setmgr.getSettingByName("Flight Mode").getValString().equalsIgnoreCase("SurvivalDub DMG")) {
            damage();
            this.counter = 0;
            this.speed = 0.0;
        }
        if (Client.instance.setmgr.getSettingByName("Flight Mode").getValString().equalsIgnoreCase("Verus")) {
            Fly.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.thePlayer.posX, Fly.mc.thePlayer.posY + 3.0000002, Fly.mc.thePlayer.posZ, false));
            Fly.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.thePlayer.posX, Fly.mc.thePlayer.posY, Fly.mc.thePlayer.posZ, false));
            Fly.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.thePlayer.posX, Fly.mc.thePlayer.posY, Fly.mc.thePlayer.posZ, true));
        }
        this.done = false;
        this.state = false;
        this.timer.reset();
        this.ticks = 0;
        this.tickssd = 0;
        this.damaged = false;
        this.positionX = Fly.mc.thePlayer.posX;
        this.positionZ = Fly.mc.thePlayer.posZ;
        this.positionY = Fly.mc.thePlayer.posY;
    }
    
    public static void damage() {
        for (int i = 0; i < 9; ++i) {
            NetUtil.sendPacketNoEvents(new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.thePlayer.posX, Fly.mc.thePlayer.posY + 0.41999998688697815, Fly.mc.thePlayer.posZ, false));
            NetUtil.sendPacketNoEvents(new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.thePlayer.posX, Fly.mc.thePlayer.posY + 6.248688697814067E-5, Fly.mc.thePlayer.posZ, false));
            NetUtil.sendPacketNoEvents(new C03PacketPlayer(false));
        }
        NetUtil.sendPacketNoEvents(new C03PacketPlayer(true));
    }
}
