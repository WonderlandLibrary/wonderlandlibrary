package tech.atani.client.feature.module.impl.combat;

import cn.muyang.nativeobfuscator.Native;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.MovingObjectPosition;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.ModuleData;
import tech.atani.client.feature.module.data.enums.Category;
import tech.atani.client.utility.interfaces.Methods;
import tech.atani.client.feature.value.impl.CheckBoxValue;
import tech.atani.client.feature.value.impl.SliderValue;
import tech.atani.client.feature.value.impl.StringBoxValue;
import tech.atani.client.listener.event.minecraft.player.combat.AttackEntityEvent;
import tech.atani.client.listener.event.minecraft.player.movement.MoveFlyingEvent;
import tech.atani.client.listener.event.minecraft.player.rotation.RotationEvent;
import tech.atani.client.listener.radbus.Listen;
import tech.atani.client.utility.math.time.TimeHelper;
import tech.atani.client.utility.player.rotation.RotationUtil;

@Native
@ModuleData(name = "AntiFireball", description = "Automatically deflects fireballs", category = Category.COMBAT)
public class AntiFireball extends Module {

    private StringBoxValue swingMode = new StringBoxValue("Swing Mode", "How will the module swing?", this, new String[]{"Normal", "Packet", "None"});
    private CheckBoxValue rotate = new CheckBoxValue("Rotate", "Rotate at fireballs?", this, true);
    public CheckBoxValue rayTrace = new CheckBoxValue("Ray Trace", "Ray Trace?",this, true);
    private CheckBoxValue stopMove = new CheckBoxValue("Stop Move", "Stop movement?", this, false);
    private SliderValue<Float> radius = new SliderValue<>("Radius", "At which radius will the module operate?", this, 3F, 0F, 6F, 1);

    private TimeHelper timeHelper = new TimeHelper();
    private boolean attacking = false;
    private EntityFireball entity;

    @Listen
    public void onAttack(AttackEntityEvent event) {
        for (Object entityObj : Methods.mc.theWorld.loadedEntityList) {
            if (entityObj instanceof EntityFireball) {
                this.entity = (EntityFireball) entityObj;
                if (Methods.mc.thePlayer.getDistanceToEntity(entity) < radius.getValue() && timeHelper.hasReached(300) && (!rayTrace.getValue() || (Methods.mc.objectMouseOver != null && Methods.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && Methods.mc.objectMouseOver.entityHit.getEntityId() == entity.getEntityId()))) {
                    attacking = true;

                    if (swingMode.getValue().equals("Normal")) {
                        Methods.mc.thePlayer.swingItem();
                    } else if (swingMode.getValue().equals("Packet")) {
                        Methods.mc.getNetHandler().addToSendQueue(new C0APacketAnimation());
                    }
                    Methods.mc.getNetHandler().addToSendQueue(new C02PacketUseEntity(entity, C02PacketUseEntity.Action.ATTACK));

                    timeHelper.reset();
                    break;
                }
            }
        }
        this.entity = null;
    }

    @Listen
    public void onRotate(RotationEvent rotationEvent) {
        if(this.entity != null) {
            float[] rotations = RotationUtil.getRotation(entity, "", 0, true, false, 0, 0, 0, 0, false, 180, 180, 180, 180, false, false);
            rotationEvent.setYaw(rotations[0]);
            rotationEvent.setPitch(rotations[1]);
        }
    }

    @Listen
    public void onMoveFlying(MoveFlyingEvent moveFlyingEvent) {
        if(attacking && stopMove.getValue()) {
            attacking = false;
            moveFlyingEvent.setForward(0);
            moveFlyingEvent.setStrafe(0);
            moveFlyingEvent.setFriction(0);
        }
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}
}
