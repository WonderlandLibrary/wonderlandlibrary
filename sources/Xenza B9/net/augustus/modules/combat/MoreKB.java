// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus.modules.combat;

import net.lenni0451.eventapi.reflection.EventTarget;
import net.minecraft.network.Packet;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.util.MathHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MovingObjectPosition;
import net.augustus.events.EventTick;
import net.augustus.modules.Categorys;
import java.awt.Color;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;
import net.augustus.modules.Module;

public class MoreKB extends Module
{
    public StringValue mode;
    public DoubleValue idkWhatToCallThisLmao;
    public BooleanValue intelligent;
    private boolean shouldSprintReset;
    
    public MoreKB() {
        super("MoreKB", new Color(252, 73, 3), Categorys.COMBAT);
        this.mode = new StringValue(1, "Modes", this, "Legit", new String[] { "Legit", "LessPacket", "Packet", "DoublePacket", "MorePacket" });
        this.idkWhatToCallThisLmao = new DoubleValue(42069, "Packets", this, 5.0, 3.0, 10.0, 0);
        this.intelligent = new BooleanValue(1, "Intelligent", this, false);
        this.shouldSprintReset = false;
    }
    
    @EventTarget
    public void onEventTick(final EventTick eventTick) {
        EntityLivingBase entity = null;
        if (MoreKB.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && MoreKB.mc.objectMouseOver.entityHit instanceof EntityLivingBase) {
            entity = (EntityLivingBase)MoreKB.mc.objectMouseOver.entityHit;
        }
        if (MoreKB.mm.killAura.isToggled()) {
            final KillAura killAura = MoreKB.mm.killAura;
            if (KillAura.target != null) {
                final KillAura killAura2 = MoreKB.mm.killAura;
                entity = KillAura.target;
            }
        }
        if (entity != null) {
            final double x = MoreKB.mc.thePlayer.posX - entity.posX;
            final double z = MoreKB.mc.thePlayer.posZ - entity.posZ;
            final float calcYaw = (float)(MathHelper.func_181159_b(z, x) * 180.0 / 3.141592653589793 - 90.0);
            final float diffY = Math.abs(MathHelper.wrapAngleTo180_float(calcYaw - entity.rotationYawHead));
            if (!this.intelligent.getBoolean() || diffY <= 120.0f) {
                final String selected;
                final String var9 = selected = this.mode.getSelected();
                switch (selected) {
                    case "MorePacket": {
                        if (entity.hurtTime == 10) {
                            for (int i = 0; i < this.idkWhatToCallThisLmao.getValue(); ++i) {
                                MoreKB.mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(MoreKB.mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
                                MoreKB.mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(MoreKB.mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
                            }
                            MoreKB.mc.thePlayer.setServerSprintState(true);
                            break;
                        }
                        break;
                    }
                    case "Packet": {
                        if (entity.hurtTime == 10) {
                            MoreKB.mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(MoreKB.mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
                            MoreKB.mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(MoreKB.mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
                            MoreKB.mc.thePlayer.setServerSprintState(true);
                            break;
                        }
                        break;
                    }
                    case "DoublePacket": {
                        if (entity.hurtTime == 10) {
                            MoreKB.mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(MoreKB.mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
                            MoreKB.mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(MoreKB.mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
                            MoreKB.mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(MoreKB.mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
                            MoreKB.mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(MoreKB.mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
                            MoreKB.mc.thePlayer.setServerSprintState(true);
                            break;
                        }
                        break;
                    }
                    case "Legit": {
                        if (entity.hurtTime == 10) {
                            this.shouldSprintReset = true;
                            MoreKB.mc.thePlayer.reSprint = 2;
                            this.shouldSprintReset = false;
                            System.out.println("Resprint   " + entity.hurtTime);
                            break;
                        }
                        break;
                    }
                    case "LessPacket": {
                        if (entity.hurtTime == 10) {
                            if (MoreKB.mc.thePlayer.isSprinting()) {
                                MoreKB.mc.thePlayer.setSprinting(false);
                            }
                            MoreKB.mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(MoreKB.mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
                            MoreKB.mc.thePlayer.setServerSprintState(true);
                            break;
                        }
                        break;
                    }
                }
            }
        }
    }
}
