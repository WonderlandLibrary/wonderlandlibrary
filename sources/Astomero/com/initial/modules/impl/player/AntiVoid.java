package com.initial.modules.impl.player;

import com.initial.settings.impl.*;
import net.minecraft.network.*;
import com.initial.modules.*;
import com.initial.settings.*;
import com.initial.utils.movement.*;
import net.minecraft.entity.*;
import net.minecraft.network.play.client.*;
import com.initial.utils.network.*;
import com.initial.events.*;
import net.minecraft.network.play.server.*;
import com.initial.events.impl.*;
import com.initial.*;
import java.util.*;

public class AntiVoid extends Module
{
    public ModeSet modeSet;
    public DoubleSet dist;
    public ModuleCategory verusCategory;
    public BooleanSet verus_float;
    public BooleanSet verus_prevent;
    public DoubleSet verus_motion;
    public BooleanSet verus_boost;
    public DoubleSet verus_boost_factor;
    public ModuleCategory packetCategory;
    public DoubleSet packet_offSet;
    public DoubleSet packet_ammount;
    public BooleanSet packet_ground;
    private double xGround;
    private double yGround;
    private double zGround;
    private boolean isCancelling;
    private boolean shouldCancel;
    private int ticksSinceTP;
    private ArrayList<Packet> cancelPackets;
    
    public AntiVoid() {
        super("AntiVoid", 0, Category.PLAYER);
        this.modeSet = new ModeSet("Watchdog", "Teleport", new String[] { "Packet", "Teleport", "Verus", "Watchdog" });
        this.dist = new DoubleSet("Distance", 5.0, 1.0, 10.0, 1.0);
        this.verusCategory = new ModuleCategory("Verus...");
        this.verus_float = new BooleanSet("Float", false);
        this.verus_prevent = new BooleanSet("Prevent Damage", true);
        this.verus_motion = new DoubleSet("Motion", 2.0, 1.0, 3.0, 0.05);
        this.verus_boost = new BooleanSet("Boost", true);
        this.verus_boost_factor = new DoubleSet("Boost Factor", 3.0, 1.0, 10.0, 0.1);
        this.packetCategory = new ModuleCategory("Packet...");
        this.packet_offSet = new DoubleSet("Offset", 3.0, 1.0, 10.0, 0.1);
        this.packet_ammount = new DoubleSet("Ammount", 1.0, 1.0, 10.0, 1.0);
        this.packet_ground = new BooleanSet("Ground", false);
        this.xGround = 0.0;
        this.yGround = 0.0;
        this.zGround = 0.0;
        this.isCancelling = false;
        this.shouldCancel = false;
        this.ticksSinceTP = 0;
        this.cancelPackets = new ArrayList<Packet>();
        this.addSettings(this.modeSet, this.dist, this.verusCategory, this.packetCategory);
        this.packetCategory.addCatSettings(this.packet_offSet, this.packet_ammount, this.packet_ground);
        this.verusCategory.addCatSettings(this.verus_float, this.verus_prevent, this.verus_motion, this.verus_boost, this.verus_boost_factor);
    }
    
    @EventTarget
    public void onPre(final EventMotion e) {
        this.setDisplayName("Anti Void");
        final String mode = this.modeSet.getMode();
        switch (mode) {
            case "Verus": {
                ++this.ticksSinceTP;
                if (this.ticksSinceTP < 15) {
                    return;
                }
                if (this.mc.thePlayer.onGround && MovementUtils.getOnRealGround(this.mc.thePlayer, 1.0E-4)) {
                    this.xGround = this.mc.thePlayer.posX;
                    this.yGround = this.mc.thePlayer.posY;
                    this.zGround = this.mc.thePlayer.posZ;
                    break;
                }
                if (!MovementUtils.isOverVoid()) {
                    break;
                }
                final double coso = this.yGround - this.dist.getValue();
                if (coso <= AntiVoid.localPlayer.posY) {
                    if (coso < AntiVoid.localPlayer.posY - 3.35 && this.verus_prevent.isEnabled() && AntiVoid.localPlayer.fallDistance > 2.9) {
                        e.setOnGround(true);
                        AntiVoid.localPlayer.fallDistance = 0.0f;
                    }
                    break;
                }
                e.setOnGround(true);
                if (this.verus_boost.isEnabled()) {
                    MovementUtils.setSpeed1((float)(MovementUtils.getSpeed() * this.verus_boost_factor.getValue()));
                }
                if (!this.verus_float.isEnabled()) {
                    AntiVoid.localPlayer.motionY = this.verus_motion.getValue();
                    break;
                }
                AntiVoid.localPlayer.motionY = 0.0;
                if (AntiVoid.localPlayer.hurtTime > 0) {
                    AntiVoid.localPlayer.motionY = this.verus_motion.getValue();
                    break;
                }
                break;
            }
            case "Teleport": {
                if (this.mc.thePlayer.onGround && MovementUtils.getOnRealGround(this.mc.thePlayer, 1.0E-4)) {
                    this.xGround = this.mc.thePlayer.posX;
                    this.yGround = this.mc.thePlayer.posY;
                    this.zGround = this.mc.thePlayer.posZ;
                    break;
                }
                if (this.shouldLagback()) {
                    AntiVoid.localPlayer.setPosition(this.xGround, this.yGround, this.zGround);
                    break;
                }
                break;
            }
            case "Packet": {
                if (this.shouldLagback()) {
                    for (int a = 0; a < this.packet_ammount.getValue(); ++a) {
                        PacketUtil.sendPacketSilent(new C03PacketPlayer.C04PacketPlayerPosition(AntiVoid.localPlayer.posX, AntiVoid.localPlayer.posY + this.packet_offSet.getValue(), AntiVoid.localPlayer.posZ, this.packet_ground.isEnabled()));
                    }
                    break;
                }
                break;
            }
            case "Watchdog": {
                if (this.shouldLagback()) {
                    PacketUtil.sendPacketSilent(new C03PacketPlayer.C04PacketPlayerPosition(AntiVoid.localPlayer.posX, AntiVoid.localPlayer.posY + 12.0, AntiVoid.localPlayer.posZ, true));
                    break;
                }
                break;
            }
        }
    }
    
    @EventTarget
    public void onGet(final EventReceivePacket e) {
        if (e.getPacket() instanceof S08PacketPlayerPosLook) {
            this.ticksSinceTP = 0;
        }
    }
    
    @EventTarget
    public void onSend(final EventSendPacket e) {
        final String mode = this.modeSet.getMode();
        switch (mode) {
            case "Test": {
                final boolean scaffoldToggled = Astomero.instance.moduleManager.getModuleByName("Scaffold").isToggled();
                if (scaffoldToggled) {
                    return;
                }
                if (!(e.getPacket() instanceof C03PacketPlayer)) {
                    break;
                }
                if (AntiVoid.localPlayer.onGround) {
                    this.xGround = AntiVoid.localPlayer.posX;
                    this.yGround = AntiVoid.localPlayer.posY;
                    this.zGround = AntiVoid.localPlayer.posZ;
                    this.shouldCancel = true;
                    if (this.isCancelling) {
                        for (final Packet p : this.cancelPackets) {
                            PacketUtil.sendPacketSilent(p);
                        }
                        this.cancelPackets.clear();
                        this.isCancelling = false;
                    }
                }
                if (!MovementUtils.isOverVoid()) {
                    break;
                }
                if (AntiVoid.localPlayer.fallDistance < this.dist.getValue() && !AntiVoid.localPlayer.onGround) {
                    e.setCancelled(true);
                    this.cancelPackets.add(e.getPacket());
                    this.isCancelling = true;
                    break;
                }
                if (this.shouldCancel) {
                    this.cancelPackets.clear();
                    this.isCancelling = true;
                    AntiVoid.localPlayer.setPosition(this.xGround, this.yGround, this.zGround);
                    this.shouldCancel = false;
                    break;
                }
                break;
            }
        }
    }
    
    public boolean shouldLagback() {
        return AntiVoid.localPlayer.fallDistance > this.dist.getValue() && MovementUtils.isOverVoid();
    }
}
