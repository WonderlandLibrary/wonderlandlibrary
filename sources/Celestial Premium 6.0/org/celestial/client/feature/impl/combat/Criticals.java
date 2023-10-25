/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  ru.wendoxd.wclassguard.WXFuscator
 */
package org.celestial.client.feature.impl.combat;

import java.util.function.Supplier;
import net.minecraft.entity.MoverType;
import net.minecraft.inventory.ClickType;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import org.celestial.client.Celestial;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.packet.EventAttackSilent;
import org.celestial.client.event.events.impl.player.EventForDisabler;
import org.celestial.client.event.events.impl.player.EventPreMotion;
import org.celestial.client.event.events.impl.player.EventUpdate;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.feature.impl.combat.KillAura;
import org.celestial.client.feature.impl.movement.Flight;
import org.celestial.client.helpers.misc.TimerHelper;
import org.celestial.client.helpers.player.InventoryHelper;
import org.celestial.client.helpers.player.MovementHelper;
import org.celestial.client.settings.impl.BooleanSetting;
import org.celestial.client.settings.impl.ListSetting;
import ru.wendoxd.wclassguard.WXFuscator;

public class Criticals
extends Feature {
    private final BooleanSetting jump;
    private final BooleanSetting groundFalse;
    private final BooleanSetting reduceDamageOnDisable;
    public static ListSetting critMode;
    private final TimerHelper ncpTimer = new TimerHelper();
    private final double[] offsets = new double[]{0.4100000000000002, 0.01099999999999924};
    private final TimerHelper sendTimer = new TimerHelper();
    private boolean isNotJump;

    public Criticals() {
        super("Criticals", "\u0410\u0432\u0442\u043e\u043c\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0438 \u043d\u0430\u043d\u043e\u0441\u0438\u0442 \u0441\u0443\u0449\u043d\u043e\u0441\u0442\u0438 \u043a\u0440\u0438\u0442\u0438\u0447\u0438\u0441\u043a\u0438\u0439 \u0443\u0440\u043e\u043d \u043f\u0440\u0438 \u0443\u0434\u0430\u0440\u0435", Type.Combat);
        critMode = new ListSetting("Criticals Mode", "Packet", "Packet", "NCP", "NCP New", "Sunrise Ground", "Sunrise Air", "Sunrise Drop", "Old Sunrise", "Old Matrix", "Matrix 6.3.0");
        this.reduceDamageOnDisable = new BooleanSetting("Reduce damage on disable", true, new Supplier<Boolean>(){

            @Override
            public Boolean get() {
                return Criticals.critMode.currentMode.equals("Matrix 6.3.0");
            }
        });
        this.jump = new BooleanSetting("Mini-Jump", false, new Supplier<Boolean>(){

            @Override
            public Boolean get() {
                return !Criticals.critMode.currentMode.equals("Matrix 6.3.0") && !Criticals.critMode.currentMode.equals("Matrix");
            }
        });
        this.groundFalse = new BooleanSetting("Ground False", false, new Supplier<Boolean>(){

            @Override
            public Boolean get() {
                return Criticals.critMode.currentMode.equals("Old Matrix");
            }
        });
        this.addSettings(critMode, this.groundFalse, this.reduceDamageOnDisable, this.jump);
    }

    public static void init() {
    }

    @Override
    public void onDisable() {
        if (Criticals.critMode.currentMode.equals("Matrix 6.3.0") && this.reduceDamageOnDisable.getCurrentValue() && Criticals.mc.player.fallDistance > 3.0f) {
            Criticals.mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(Criticals.mc.player.posX, Criticals.mc.player.posY, Criticals.mc.player.posZ, Criticals.mc.player.rotationYaw, Criticals.mc.player.rotationPitch, true));
            Criticals.mc.player.fallDistance = 0.0f;
        }
        super.onDisable();
    }

    @EventTarget
    public void onAttackSilent(EventAttackSilent event) {
        String mode = critMode.getOptions();
        if (KillAura.target != null && Celestial.instance.featureManager.getFeatureByClass(KillAura.class).getState()) {
            double x = Criticals.mc.player.posX;
            double y = Criticals.mc.player.posY;
            double z = Criticals.mc.player.posZ;
            if (mode.equalsIgnoreCase("Packet")) {
                if (this.jump.getCurrentValue()) {
                    Criticals.mc.player.setPosition(x, y + 0.04, z);
                }
                Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(x, y + 0.11, z, false));
                Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(x, y + 0.1100013579, z, false));
                Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(x, y + 1.3579E-6, z, false));
                Criticals.mc.player.onCriticalHit(event.getTargetEntity());
            } else if (mode.equalsIgnoreCase("NCP")) {
                if (this.ncpTimer.hasReached(800.0) && Criticals.mc.player.onGround) {
                    if (this.jump.getCurrentValue()) {
                        Criticals.mc.player.setPosition(x, y + 0.04, z);
                    }
                    for (double offset : this.offsets) {
                        Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY + offset, Criticals.mc.player.posZ, false));
                    }
                    Criticals.mc.player.onCriticalHit(event.getTargetEntity());
                    this.ncpTimer.reset();
                }
            } else if (mode.equalsIgnoreCase("NCP New") && Criticals.mc.player.onGround && !Criticals.mc.player.isInLiquid() && MovementHelper.airBlockAbove2()) {
                for (double offset : this.offsets) {
                    Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY + offset, Criticals.mc.player.posZ, false));
                }
                Criticals.mc.player.onCriticalHit(event.getTargetEntity());
            }
        }
    }

    @WXFuscator
    @EventTarget
    public void onSunriseCrits(EventUpdate event) {
        if (Criticals.critMode.currentMode.equalsIgnoreCase("Sunrise Air") && KillAura.target != null && Celestial.instance.featureManager.getFeatureByClass(KillAura.class).getState()) {
            MovementHelper.setSpeed(MovementHelper.getSpeed());
        }
    }

    @WXFuscator
    @EventTarget
    public void onDisabler(EventForDisabler event) {
        if (Criticals.critMode.currentMode.equalsIgnoreCase("Matrix 6.3.0") && KillAura.target != null && Celestial.instance.featureManager.getFeatureByClass(KillAura.class).getState()) {
            MovementHelper.setSpeed(MovementHelper.getSpeed());
            if (Criticals.mc.player.onGround && this.isNotJump) {
                Criticals.mc.player.moveNew(MoverType.PLAYER, 0.0, 0.4114, 0.0);
            }
        }
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        if (Criticals.critMode.currentMode.equals("Old Matrix") && Celestial.instance.featureManager.getFeatureByClass(KillAura.class).getState() && KillAura.target != null) {
            if (this.sendTimer.hasReached(300.0)) {
                Criticals.mc.player.connection.sendPacket(new CPacketEntityAction(Criticals.mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
                this.sendTimer.reset();
            }
            if (Celestial.instance.featureManager.getFeatureByClass(Flight.class).getState()) {
                return;
            }
            if (this.groundFalse.getCurrentValue()) {
                Criticals.mc.player.onGround = false;
                MovementHelper.setSpeed(MovementHelper.getSpeed());
            }
            if (this.isNotJump && Criticals.mc.player.onGround) {
                event.setOnGround(false);
                event.setPosY(Criticals.mc.player.ticksExisted % 2 != 1 ? event.getPosY() + (double)0.08f : event.getPosY() + (double)0.05f);
                event.setOnGround(false);
            }
        }
        if (Criticals.critMode.currentMode.equals("Old Sunrise") && Celestial.instance.featureManager.getFeatureByClass(KillAura.class).getState() && KillAura.target != null) {
            if (Celestial.instance.featureManager.getFeatureByClass(Flight.class).getState()) {
                return;
            }
            Criticals.mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
            if (this.isNotJump && Criticals.mc.player.onGround) {
                event.setOnGround(false);
                event.setPosY(Criticals.mc.player.ticksExisted % 2 != 1 ? event.getPosY() + (double)0.14f : event.getPosY() + (double)0.09f);
            }
        }
        if (Criticals.critMode.currentMode.equals("Sunrise Drop") && Celestial.instance.featureManager.getFeatureByClass(KillAura.class).getState() && KillAura.target != null) {
            if (Celestial.instance.featureManager.getFeatureByClass(Flight.class).getState()) {
                return;
            }
            if (this.isNotJump && Criticals.mc.player.onGround) {
                int blockSlot = InventoryHelper.getBlocksAtHotbar();
                if (Celestial.instance.featureManager.getFeatureByClass(KillAura.class).getState() && KillAura.target != null && blockSlot != -1) {
                    if (Criticals.mc.player.ticksExisted % 3 == 0) {
                        Criticals.mc.playerController.windowClick(Criticals.mc.player.inventoryContainer.windowId, blockSlot, 0, ClickType.THROW, Criticals.mc.player);
                    }
                    event.setOnGround(false);
                    event.setPosY(Criticals.mc.player.ticksExisted % 2 == 0 ? event.getPosY() + (double)0.3f : event.getPosY() + (double)0.23f);
                }
            }
        }
        if (Criticals.critMode.currentMode.equalsIgnoreCase("Matrix 6.3.0") && Celestial.instance.featureManager.getFeatureByClass(KillAura.class).getState() && KillAura.target != null) {
            event.setOnGround(false);
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        this.isNotJump = !Criticals.mc.gameSettings.keyBindJump.isKeyDown();
        String mode = critMode.getOptions();
        this.setSuffix(mode);
    }
}

