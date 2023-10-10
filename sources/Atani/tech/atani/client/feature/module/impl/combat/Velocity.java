package tech.atani.client.feature.module.impl.combat;

import cn.muyang.nativeobfuscator.Native;
import com.google.common.base.Supplier;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import tech.atani.client.listener.event.minecraft.network.PacketEvent;
import tech.atani.client.listener.event.minecraft.player.movement.SilentMoveEvent;
import tech.atani.client.listener.event.minecraft.player.movement.UpdateEvent;
import tech.atani.client.listener.event.minecraft.player.movement.UpdateMotionEvent;
import tech.atani.client.listener.radbus.Listen;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.ModuleData;
import tech.atani.client.feature.module.data.enums.Category;
import tech.atani.client.utility.interfaces.Methods;
import tech.atani.client.utility.math.time.TimeHelper;
import tech.atani.client.feature.value.impl.SliderValue;
import tech.atani.client.feature.value.impl.StringBoxValue;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Native
@ModuleData(name = "Velocity", description = "Modifies your velocity", category = Category.COMBAT)
public class Velocity extends Module {

    private final StringBoxValue mode = new StringBoxValue("Mode", "Which mode will the module use?", this, new String[] {"Simple", "Reverse", "Delay", "Intave Jump", "Grim Spoof", "Old Grim", "Grim Flag", "Vulcan", "AAC v4", "AAC v5 Packet", "AAC v5.2.0", "Matrix Semi", "Matrix Reverse", "Polar", "Polar Under-Block", "Fake Lag", "MineMenClub"});
    private final SliderValue<Integer> horizontal = new SliderValue<Integer>("Horizontal %", "How much horizontal velocity will you take?", this, 100, 0, 100, 0, new Supplier[] {() -> mode.is("Simple") || mode.is("Reverse") || mode.is("Delay")});
    private final SliderValue<Integer> vertical = new SliderValue<Integer>("Vertical %", "How much vertical velocity will you take?", this, 100, 0, 100, 0, new Supplier[] {() -> mode.is("Simple") || mode.is("Reverse") || mode.is("Delay")});
    private final SliderValue<Float> aacv4Reduce = new SliderValue<Float>("Reduce", "How much motion will be reduced?", this, 0.62f,0f,1f, 1, new Supplier[] {() -> mode.is("AAC v4")});
    private final SliderValue<Integer> delayTicks = new SliderValue<Integer>("Delay Ticks", "How long will the velocity wait before cancelling it?", this, 500, 0, 1000, 0, new Supplier[]{() -> mode.is("Delay")});

    private double packetX = 0;
    private double packetY = 0;
    private double packetZ = 0;
    private boolean receivedVelocity = false;

    // Delay
    private final TimeHelper delayTimer = new TimeHelper();

    // AAC v5.2.0
    private final TimeHelper aacTimer = new TimeHelper();

    // Intave Jump
    private int counter;

    // Grim
    int grimCancel = 0;
    int updates = 0;

    // Grim Flag
    private boolean grimFlag;

    // Grim Spoof
    private final Queue<Short> transactionQueue = new ConcurrentLinkedQueue<>();
    private boolean grimPacket;

    private boolean attacked;

    // MineMenClub
    private int mmcCounter;

    @Override
    public String getSuffix() {
    	return mode.getValue();
    }

    @Listen
    public void onUpdateMotionEvent(UpdateMotionEvent event) {
        if(Methods.mc.thePlayer == null || Methods.mc.theWorld == null) {
            return;
        }

        switch (mode.getValue()) {
            case "MineMenClub": {
                mmcCounter++;
                break;
            }
            case "Polar": {
                if (mc.thePlayer.isSwingInProgress) {
                    attacked = true;
                }

                if (
                        mc.objectMouseOver.typeOfHit.equals(MovingObjectPosition.MovingObjectType.ENTITY)
                                && mc.thePlayer.hurtTime > 0
                                && !attacked
                ) {
                    mc.thePlayer.motionX *= 0.45D;
                    mc.thePlayer.motionZ *= 0.45D;
                    mc.thePlayer.setSprinting(false);
                }

                attacked = false;
            }
            break;
        }
    }

    @Listen
    public final void onUpdate(UpdateEvent updateEvent) {
        switch (mode.getValue()) {
            case "Grim Spoof": {
                if (transactionQueue.isEmpty() && grimPacket) grimPacket = false;
                break;
            }
            case "Grim Flag": {
                if (mc.thePlayer.hurtTime != 0)
                    mc.thePlayer.setPosition(mc.thePlayer.lastTickPosX, mc.thePlayer.lastTickPosY, mc.thePlayer.lastTickPosZ);
                break;
            }
            case "AAC v5.2.0": {
                if (mc.thePlayer.hurtTime > 0 && this.receivedVelocity) {
                    this.receivedVelocity = false;
                    mc.thePlayer.motionX = 0.0;
                    mc.thePlayer.motionZ = 0.0;
                    mc.thePlayer.motionY = 0.0;
                    mc.thePlayer.jumpMovementFactor = -0.002f;
                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, 1.7976931348623157E+308, mc.thePlayer.posZ, true));
                }
                if (aacTimer.hasReached(80L) && this.receivedVelocity) {
                    this.receivedVelocity = false;
                    mc.thePlayer.motionX = packetX / 8000.0;
                    mc.thePlayer.motionZ = packetZ / 8000.0;
                    mc.thePlayer.motionY = packetY / 8000.0;
                    mc.thePlayer.jumpMovementFactor = -0.002f;
                }
                break;
            }
            case "AAC v4": {
                if (mc.thePlayer.hurtTime > 0 && !mc.thePlayer.onGround) {
                    mc.thePlayer.motionX *= aacv4Reduce.getValue();
                    mc.thePlayer.motionZ *= aacv4Reduce.getValue();
                }
                break;
            }
            case "Old Grim": {
                updates++;

                if (updates >= 0 || updates >= 10) {
                    updates = 0;
                    if (grimCancel > 0) {
                        grimCancel--;
                    }
                }
                break;
            }
        }
    }

    @Listen
    public final void onPacket(PacketEvent packetEvent) {
        if(Methods.mc.thePlayer == null || Methods.mc.theWorld == null) {
            return;
        }

        if(packetEvent.getPacket() instanceof S12PacketEntityVelocity) {
            S12PacketEntityVelocity packet = (S12PacketEntityVelocity) packetEvent.getPacket();
            if(packet.getEntityID() == mc.thePlayer.getEntityId()) {
                this.packetX = packet.getMotionX();
                this.packetY = packet.getMotionY();
                this.packetZ = packet.getMotionZ();
                receivedVelocity = true;
                aacTimer.reset();
            }
        }
        switch (mode.getValue()) {
            case "Delay": {
                if (packetEvent.getPacket() instanceof S12PacketEntityVelocity) {
                    S12PacketEntityVelocity packet = (S12PacketEntityVelocity) packetEvent.getPacket();

                    if(delayTimer.hasReached(delayTicks.getValue() * 50) && packet.getEntityID() == mc.thePlayer.getEntityId()) {
                        if (horizontal.getValue() == 0 && vertical.getValue() == 0)
                            packetEvent.setCancelled(true);
                        packet.setMotionX((int) (packet.getMotionX() * (horizontal.getValue().doubleValue() / 100D)));
                        packet.setMotionY((int) (packet.getMotionY() * (vertical.getValue().doubleValue() / 100D)));
                        packet.setMotionZ((int) (packet.getMotionZ() * (horizontal.getValue().doubleValue() / 100D)));
                        delayTimer.reset();
                    }
                }
                break;
            }
            case "MineMenClub": {
                if (packetEvent.getPacket() instanceof S12PacketEntityVelocity) {
                    S12PacketEntityVelocity packet = (S12PacketEntityVelocity) packetEvent.getPacket();
                    if (mmcCounter > 20) {
                        if (packet.getEntityID() == mc.thePlayer.getEntityId()) {
                            packetEvent.setCancelled(true);
                            mmcCounter = 0;
                        }
                    }
                }
                break;
            }
            case "Fake Lag": {
                if(mc.thePlayer.hurtTime > 0 || (packetEvent.getPacket() instanceof S12PacketEntityVelocity && ((S12PacketEntityVelocity)packetEvent.getPacket()).getEntityID() == mc.thePlayer.getEntityId())) {
                    if (packetEvent.getPacket() instanceof S12PacketEntityVelocity) {
                        S12PacketEntityVelocity packet = (S12PacketEntityVelocity) packetEvent.getPacket();
                        if (packet.getEntityID() == mc.thePlayer.getEntityId()) {
                            packet.setMotionX(0);
                            packet.setMotionY(0);
                            packet.setMotionZ(0);
                        }
                    }
                    packetEvent.setCancelled(true);
                }
                break;
            }
            case "Polar Under-Block": {
                Packet<?> packet = packetEvent.getPacket();
                AxisAlignedBB axisAlignedBB = mc.thePlayer.getEntityBoundingBox().offset(0.0, 1.0, 0.0);

                if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, axisAlignedBB).isEmpty()) {
                    if (packet instanceof S12PacketEntityVelocity) {
                        packetEvent.setCancelled(true);
                        mc.thePlayer.motionY = ((S12PacketEntityVelocity) packet).getMotionY() / 8000.0;
                    }
                }
                break;
            }
            case "Grim Spoof": {
                if (packetEvent.getType() == PacketEvent.Type.INCOMING) {
                    Packet<?> p = packetEvent.getPacket();
                    if (p instanceof S12PacketEntityVelocity && ((S12PacketEntityVelocity) p).getEntityID() == mc.thePlayer.getEntityId()) {
                        packetEvent.setCancelled(true);
                        grimPacket = true;
                    } else if (p instanceof S32PacketConfirmTransaction) {
                        if (!grimPacket) return;
                        packetEvent.setCancelled(true);
                        transactionQueue.add(((S32PacketConfirmTransaction) p).getActionNumber());
                    }
                } else {
                    if (packetEvent.getPacket() instanceof C0FPacketConfirmTransaction) {
                        if (!grimPacket || transactionQueue.isEmpty()) return;
                        if (transactionQueue.remove(((C0FPacketConfirmTransaction) packetEvent.getPacket()).getUid()))
                            packetEvent.setCancelled(true);
                    }
                }
                break;
            }
            case "Grim Flag": {
                if (packetEvent.getType() == PacketEvent.Type.INCOMING) {
                    Packet<?> p = packetEvent.getPacket();
                    if (p instanceof S12PacketEntityVelocity && ((S12PacketEntityVelocity) p).getEntityID() == mc.thePlayer.getEntityId()) {
                        packetEvent.setCancelled(true);
                        mc.thePlayer.motionX += 0.1D;
                        mc.thePlayer.motionY += 0.1D;
                        mc.thePlayer.motionZ += 0.1D;
                    }
                } else if (packetEvent.getType() == PacketEvent.Type.OUTGOING) {
                    if (mc.thePlayer.hurtTime != 0)
                        this.grimFlag = true;
                    if (mc.thePlayer.onGround)
                        this.grimFlag = false;
                    if (this.grimFlag && packetEvent.getPacket() instanceof C03PacketPlayer) {
                        ((C03PacketPlayer) packetEvent.getPacket()).setX(mc.thePlayer.posX + 210.0D);
                        ((C03PacketPlayer) packetEvent.getPacket()).setZ(mc.thePlayer.posZ + 210.0D);
                    }
                }
                break;
            }
            case "Vulcan": {
                if (mc.thePlayer != null && mc.theWorld != null) {
                    if (mc.thePlayer.hurtTime > 0 && packetEvent.getPacket() instanceof C0FPacketConfirmTransaction) {
                        packetEvent.setCancelled(true);
                    }
                }
                if (packetEvent.getPacket() instanceof S12PacketEntityVelocity) {
                    S12PacketEntityVelocity packet = (S12PacketEntityVelocity) packetEvent.getPacket();
                    if (packet.getEntityID() == mc.thePlayer.getEntityId()) {
                        packetEvent.setCancelled(true);
                    }
                }
                break;
            }
            case "Simple": {
                if (packetEvent.getPacket() instanceof S12PacketEntityVelocity) {
                    S12PacketEntityVelocity packet = (S12PacketEntityVelocity) packetEvent.getPacket();
                    if (packet.getEntityID() == mc.thePlayer.getEntityId()) {
                        if (horizontal.getValue() == 0 && vertical.getValue() == 0)
                            packetEvent.setCancelled(true);
                        packet.setMotionX((int) (packet.getMotionX() * (horizontal.getValue().doubleValue() / 100D)));
                        packet.setMotionY((int) (packet.getMotionY() * (vertical.getValue().doubleValue() / 100D)));
                        packet.setMotionZ((int) (packet.getMotionZ() * (horizontal.getValue().doubleValue() / 100D)));
                    }
                }
                break;
            }
            case "Reverse": {
                if (packetEvent.getPacket() instanceof S12PacketEntityVelocity) {
                    S12PacketEntityVelocity packet = (S12PacketEntityVelocity) packetEvent.getPacket();
                    if (packet.getEntityID() == mc.thePlayer.getEntityId()) {
                        if (horizontal.getValue() == 0 && vertical.getValue() == 0)
                            packetEvent.setCancelled(true);
                        packet.setMotionX((int) (packet.getMotionX() * (-horizontal.getValue().doubleValue() / 100D)));
                        packet.setMotionY((int) (packet.getMotionY() * (-vertical.getValue().doubleValue() / 100D)));
                        packet.setMotionZ((int) (packet.getMotionZ() * (-horizontal.getValue().doubleValue() / 100D)));
                    }
                }
                break;
            }
            case "Old Grim": {
                if (packetEvent.getPacket() instanceof S12PacketEntityVelocity) {
                    S12PacketEntityVelocity packet = (S12PacketEntityVelocity) packetEvent.getPacket();
                    if (packet.getEntityID() == mc.thePlayer.getEntityId()) {
                        packetEvent.setCancelled(true);
                        grimCancel = 6;
                    }
                }
                if (packetEvent.getPacket() instanceof S32PacketConfirmTransaction && grimCancel > 0) {
                    packetEvent.setCancelled(true);
                    grimCancel--;
                }
                break;
            }
            case "Matrix Reverse": {
                if (packetEvent.getPacket() instanceof S12PacketEntityVelocity) {
                    if (mc.thePlayer.hurtTime > 0) {
                        mc.thePlayer.motionX *= -0.3;
                        mc.thePlayer.motionZ *= -0.3;
                    }
                }
                break;
            }
            case "Matrix Semi": {
                if (packetEvent.getPacket() instanceof S12PacketEntityVelocity) {
                    if (mc.thePlayer.hurtTime > 0) {
                        mc.thePlayer.motionX *= 0.6;
                        mc.thePlayer.motionZ *= 0.6;
                    }
                }
                break;
            }
            case "AAC v5 Packet": {
                if (packetEvent.getPacket() instanceof S12PacketEntityVelocity) {
                    S12PacketEntityVelocity packet = (S12PacketEntityVelocity) packetEvent.getPacket();
                    if (packet.getEntityID() == mc.thePlayer.getEntityId()) {
                        sendPacketUnlogged(
                                new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, 1.7976931348623157E+308, mc.thePlayer.posZ, true
                                )
                        );
                        packetEvent.setCancelled(true);
                    }
                }
                break;
            }
        }
    }

    @Listen
    public final void onSilent(SilentMoveEvent silentMoveEvent) {
        switch(this.mode.getValue()) {
            case "Intave Jump": {
                if (Velocity.mc.thePlayer.hurtTime == 9 && Velocity.mc.thePlayer.onGround && ++this.counter % 2 == 0) {
                    Velocity.mc.thePlayer.movementInput.jump = true;
                    break;
                }
                break;
            }
        }
    }

    @Override
    public void onEnable() {
        grimCancel = 0;
        packetX = 0;
        packetY = 0;
        packetZ = 0;
        receivedVelocity = false;
        aacTimer.reset();
    }

    @Override
    public void onDisable() {
        if(Methods.mc.thePlayer == null || Methods.mc.theWorld == null) {
            return;
        }

        grimPacket = false;
        transactionQueue.clear();
    }
}
