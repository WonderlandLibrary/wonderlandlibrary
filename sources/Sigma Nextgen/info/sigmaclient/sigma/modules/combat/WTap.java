package info.sigmaclient.sigma.modules.combat;

import info.sigmaclient.sigma.config.values.ModeValue;
import info.sigmaclient.sigma.config.values.NumberValue;
import info.sigmaclient.sigma.event.player.AttackEvent;
import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import info.sigmaclient.sigma.utils.player.Rotation;
import info.sigmaclient.sigma.utils.player.RotationUtils;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.play.client.CEntityActionPacket;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class WTap extends Module {
    public ModeValue clickMode = new ModeValue("Type", "Normal", new String[]{
            "Intave", "Normal", "Range"
    });public Entity findTarget() {
        List<Entity> e = new ArrayList<>();
        float f = range.getValue().floatValue();
        for (final Entity o : mc.world.getLoadedEntityList()) {
            if(!(o instanceof LivingEntity)) continue;
            LivingEntity livingBase = (LivingEntity) o;
            if (o instanceof PlayerEntity) {
                if (AntiBot.isServerBots((PlayerEntity) livingBase)) continue;
                if (livingBase.isEntityUndead() && livingBase != mc.player &&
                        mc.player.getDistanceSq(o) <= f * f) {
                    e.add(o);
                }
            }
        }
        if(e.size() == 0) return null;
        e.sort(Comparator.comparingInt(a -> (int) (a.getDistanceSqToEntity(mc.player) * 100)));
        return e.get(0);
    }
    public NumberValue range = new NumberValue("Range", 3, 0, 6, NumberValue.NUMBER_TYPE.FLOAT){
        @Override
        public boolean isHidden() {
            return !clickMode.is("Range");
        }
    };
    int ticks = 0;
    boolean resetSprint = false;
    public WTap() {
        super("WTap", Category.Combat, "Add your knockback");
     registerValue(clickMode);
     registerValue(range);
    }

    @Override
    public void onUpdateEvent(UpdateEvent event) {
        if(event.isPre()) {
            if(ticks > 0) {
                ticks --;
            }
        }else{
            if(clickMode.is("Range")){
                if(event.isPost()){
                    Entity target = Killaura.attackTarget;
                    if(target == null) {
                        if(resetSprint) {
                            resetSprint = false;
                            mc.gameSettings.keyBindForward.pressed = InputMappings.isKeyDown(mc.gameSettings.keyBindForward);
                        }
                        return;
                    }
                    Rotation nearestRot = RotationUtils.nearestRotationFinder(target.getBoundingBox(), false, false, true, 3.0f);
                    if (RotationUtils.isMouseOver(nearestRot.getYaw(), nearestRot.getPitch(), target, 3.0f, 0f, true)) {
                        mc.gameSettings.keyBindForward.pressed = false;
                    }else{
                        mc.gameSettings.keyBindForward.pressed = InputMappings.isKeyDown(mc.gameSettings.keyBindForward);
                    }
                    resetSprint = true;
                }
            }
        }
        super.onUpdateEvent(event);
    }

    @Override
    public void onAttackEvent(AttackEvent event) {
        if(clickMode.is("Normal")) {
            if(!event.post) {
                if (mc.player.isSprinting()) {
                    mc.getConnection().sendPacketNOEvent(new CEntityActionPacket(mc.player, CEntityActionPacket.Action.STOP_SPRINTING));
                }else{
                    mc.getConnection().sendPacketNOEvent(new CEntityActionPacket(mc.player, CEntityActionPacket.Action.START_SPRINTING));
                    mc.getConnection().sendPacketNOEvent(new CEntityActionPacket(mc.player, CEntityActionPacket.Action.STOP_SPRINTING));
                }
            }else {
                if (mc.player.isSprinting()) {
                    mc.getConnection().sendPacketNOEvent(new CEntityActionPacket(mc.player, CEntityActionPacket.Action.START_SPRINTING));
                }
            }
        }else if(clickMode.is("Intave")){
            if(!event.post) {
                if (ticks == 0) {
                    ticks = 1;
                    mc.player.setSprinting(false);
                    mc.gameSettings.keyBindSprint.pressed = false;
                }
            }
        }
        super.onAttackEvent(event);
    }
}
