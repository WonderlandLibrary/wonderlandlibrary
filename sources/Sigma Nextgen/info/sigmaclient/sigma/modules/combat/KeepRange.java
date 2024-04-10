package info.sigmaclient.sigma.modules.combat;

import info.sigmaclient.sigma.config.values.BooleanValue;
import info.sigmaclient.sigma.config.values.NumberValue;
import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.event.player.WorldEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import info.sigmaclient.sigma.utils.player.Rotation;
import info.sigmaclient.sigma.utils.player.RotationUtils;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class KeepRange extends Module {
    public NumberValue range = new NumberValue("Range", 3, 0, 6, NumberValue.NUMBER_TYPE.FLOAT);
    BooleanValue killauraOnly = new BooleanValue("Killaura Only", true);
    public KeepRange() {
        super("KeepRange", Category.Combat, "Keep your range");
     registerValue(range);
     registerValue(killauraOnly);
    }
    public Entity findTarget() {
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
    @Override
    public void onWorldEvent(WorldEvent event) {
        super.onWorldEvent(event);
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onUpdateEvent(UpdateEvent event) {
        if(event.isPost()){
            Entity target;
            // ?
            if(!killauraOnly.getValue()){
                target = findTarget();
            }else{
                target = Killaura.attackTarget;
            }
            if(target == null) {
                reset();
                mc.gameSettings.keyBindForward.pressed = InputMappings.isKeyDown(mc.gameSettings.keyBindForward);
                return;
            }
            Rotation nearestRot = RotationUtils.nearestRotationFinder(target.getBoundingBox(), false, false, true, 3.0f);
            if(nearestRot == null) {
                mc.gameSettings.keyBindForward.pressed = InputMappings.isKeyDown(mc.gameSettings.keyBindForward);
                return;
            }
            if (RotationUtils.isMouseOver(nearestRot.getYaw(), nearestRot.getPitch(), target, 3.0f, 1f, true)) {
                mc.gameSettings.keyBindForward.pressed = false;
            }else{
                mc.gameSettings.keyBindForward.pressed = InputMappings.isKeyDown(mc.gameSettings.keyBindForward);
            }
        }
        super.onUpdateEvent(event);
    }
    public void reset(){
    }
    @Override
    public void onDisable() {
        super.onDisable();
    }

}
