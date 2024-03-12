// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.combat;

import java.util.concurrent.ThreadLocalRandom;
import dev.tenacity.utils.server.PacketUtils;
import net.minecraft.network.play.client.C18PacketSpectate;
import java.util.UUID;
import dev.tenacity.module.impl.movement.Flight;
import dev.tenacity.Tenacity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import dev.tenacity.module.api.TargetManager;
import dev.tenacity.module.impl.movement.Step;
import dev.tenacity.event.impl.player.MotionEvent;
import dev.tenacity.event.impl.network.PacketSendEvent;
import dev.tenacity.module.settings.Setting;
import dev.tenacity.module.Category;
import dev.tenacity.utils.time.TimerUtil;
import dev.tenacity.module.settings.impl.NumberSetting;
import dev.tenacity.module.settings.impl.ModeSetting;
import dev.tenacity.module.Module;

public final class Criticals extends Module
{
    private boolean stage;
    private double offset;
    private int groundTicks;
    private final ModeSetting mode;
    private final ModeSetting watchdogMode;
    private final NumberSetting delay;
    private final TimerUtil timer;
    
    public Criticals() {
        super("Criticals", "Criticals", Category.COMBAT, "Crit attacks");
        this.mode = new ModeSetting("Mode", "Watchdog", new String[] { "Watchdog", "Packet", "Dev", "Verus" });
        this.watchdogMode = new ModeSetting("Watchdog Mode", "Packet", new String[] { "Packet", "Edit" });
        this.delay = new NumberSetting("Delay", 1.0, 20.0, 0.0, 1.0);
        this.timer = new TimerUtil();
        this.delay.addParent(this.mode, m -> !m.is("Verus") && (!m.is("Watchdog") || !this.watchdogMode.is("Edit")));
        this.watchdogMode.addParent(this.mode, m -> m.is("Watchdog"));
        this.addSettings(this.mode, this.watchdogMode, this.delay);
    }
    
    @Override
    public void onPacketSendEvent(final PacketSendEvent e) {
    }
    
    @Override
    public void onMotionEvent(final MotionEvent e) {
        this.setSuffix(this.mode.getMode());
        final String mode = this.mode.getMode();
        int n = -1;
        switch (mode.hashCode()) {
            case 609795629: {
                if (mode.equals("Watchdog")) {
                    n = 0;
                    break;
                }
                break;
            }
            case -1911998296: {
                if (mode.equals("Packet")) {
                    n = 1;
                    break;
                }
                break;
            }
            case 68597: {
                if (mode.equals("Dev")) {
                    n = 2;
                    break;
                }
                break;
            }
            case 82544993: {
                if (mode.equals("Verus")) {
                    n = 3;
                    break;
                }
                break;
            }
        }
        Label_0883: {
            switch (n) {
                case 0: {
                    if (this.watchdogMode.is("Packet") && KillAura.attacking && e.isOnGround() && !Step.isStepping && TargetManager.target != null && TargetManager.target.hurtTime >= this.delay.getValue().intValue()) {
                        for (final double offset : new double[] { 0.05999999865889549, 0.009999999776482582 }) {
                            Criticals.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Criticals.mc.thePlayer.posX, Criticals.mc.thePlayer.posY + offset + Math.random() * 0.001, Criticals.mc.thePlayer.posZ, false));
                        }
                    }
                    if (!e.isPre() || !this.watchdogMode.is("Edit") || Tenacity.INSTANCE.isEnabled(Flight.class) || Step.isStepping || !KillAura.attacking) {
                        break;
                    }
                    if (!e.isOnGround()) {
                        this.groundTicks = 0;
                        break;
                    }
                    ++this.groundTicks;
                    if (this.groundTicks > 2) {
                        this.stage = !this.stage;
                        e.setY(e.getY() + (this.stage ? 0.015 : 0.01) - Math.random() * 1.0E-4);
                        e.setOnGround(false);
                        break;
                    }
                    break;
                }
                case 1: {
                    if (Criticals.mc.objectMouseOver.entityHit != null && Criticals.mc.thePlayer.onGround && Criticals.mc.objectMouseOver.entityHit.hurtResistantTime > this.delay.getValue().intValue()) {
                        for (final double offset : new double[] { 0.006253453, 0.002253453, 0.001253453 }) {
                            Criticals.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Criticals.mc.thePlayer.posX, Criticals.mc.thePlayer.posY + offset, Criticals.mc.thePlayer.posZ, false));
                        }
                        break;
                    }
                    break;
                }
                case 2: {
                    if (Criticals.mc.objectMouseOver.entityHit != null && Criticals.mc.thePlayer.onGround && Criticals.mc.objectMouseOver.entityHit.hurtResistantTime > this.delay.getValue().intValue()) {
                        for (final double offset : new double[] { 0.06253453, 0.02253453, 0.001253453, 1.135346E-4 }) {
                            Criticals.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Criticals.mc.thePlayer.posX, Criticals.mc.thePlayer.posY + offset, Criticals.mc.thePlayer.posZ, false));
                            PacketUtils.sendPacketNoEvent(new C18PacketSpectate(UUID.randomUUID()));
                        }
                        break;
                    }
                    break;
                }
                case 3: {
                    if (!KillAura.attacking || TargetManager.target == null || !e.isOnGround()) {
                        break;
                    }
                    switch (TargetManager.target.hurtResistantTime) {
                        case 17:
                        case 19: {
                            e.setOnGround(false);
                            e.setY(e.getY() + ThreadLocalRandom.current().nextDouble(0.001, 0.0011));
                            break Label_0883;
                        }
                        case 18:
                        case 20: {
                            e.setOnGround(false);
                            e.setY(e.getY() + 0.03 + ThreadLocalRandom.current().nextDouble(0.001, 0.0011));
                            break Label_0883;
                        }
                    }
                    break;
                }
            }
        }
    }
}
