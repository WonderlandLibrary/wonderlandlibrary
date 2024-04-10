package info.sigmaclient.sigma.modules.combat;

import info.sigmaclient.sigma.config.values.BooleanValue;
import info.sigmaclient.sigma.config.values.ModeValue;
import info.sigmaclient.sigma.config.values.NumberValue;
import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.event.player.WorldEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import info.sigmaclient.sigma.sigma5.killaura.NCPRotation;
import info.sigmaclient.sigma.utils.player.Rotation;
import info.sigmaclient.sigma.utils.player.RotationUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.SwordItem;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import top.fl0wowp4rty.phantomshield.annotations.Native;


public class Aimbot extends Module {
    public static ModeValue mode = new ModeValue("Type", "Vanilla", new String[]{"Vanilla", "Bypass"});
    public NumberValue range = new NumberValue("Range", 5, 0, 8, NumberValue.NUMBER_TYPE.FLOAT);
    BooleanValue swordOnly = new BooleanValue("Sword Only", true);
    BooleanValue clickAim = new BooleanValue("Click Aim", true);
    public float[] rot = null;
    public Aimbot() {
        super("Aimbot", Category.Combat, "Auto aim");
     registerValue(mode);
     registerValue(range);
     registerValue(swordOnly);
     registerValue(clickAim);
    }

    public Entity findTarget() {
        List<Entity> e = new ArrayList<>();
        float f = range.getValue().floatValue();
        for (final Entity o : mc.world.getLoadedEntityList()) {
            if(!(o instanceof LivingEntity)) continue;
            LivingEntity livingBase = (LivingEntity) o;
            if (o instanceof PlayerEntity) {
                if (AntiBot.isServerBots((PlayerEntity) livingBase)) continue;
                if (livingBase != mc.player && mc.player.getDistance(o) <= f) {
                    e.add(o);
                }
            }
        }
        if(e.size() == 0) return null;
        e.sort(Comparator.comparingInt(a -> (int) (a.getDistance(mc.player) * 8000)));
        return e.get(0);
    }
    @Override
    public void onWorldEvent(WorldEvent event) {
        super.onWorldEvent(event);
    }

    @Override
    public void onEnable() {
        rot = null;
        super.onEnable();
    }
    public void updateRotation(){
        if(rot != null){
            mc.player.prevRotationYaw = rot[0];
            mc.player.prevRotationPitch = rot[1];
            mc.player.rotationYaw = mc.player.prevRotationYaw;
            mc.player.rotationPitch = mc.player.prevRotationPitch;
        }
    }
    @Override
    public void onUpdateEvent(UpdateEvent event) {
        range.max = 20;
        if(event.isPre()){
            rot = null;
            if(clickAim.getValue() && !mc.gameSettings.keyBindAttack.pressed) return;
            if(swordOnly.getValue() && !(mc.player.getHeldItem(Hand.MAIN_HAND).getItem() instanceof SwordItem)) return;

            Entity target = findTarget();
            if(target == null) return;

            Rotation rots2 = NCPRotation.NCPRotation(target.getBoundingBox());
            Rotation rot = RotationUtils.toRotation(target.getEyePosition(1.0f));
            switch (mode.getValue()){
                case "Vanilla":
                    this.rot = new float[]{rot.getYaw(), rot.getPitch()};
                    break;
                case "Bypass":
                    if(rots2 == null) return;
                    float yy = rots2.getYaw();
                    float pp = rots2.getPitch();
                    yy += new Random().nextInt(8) + (mc.player.ticksExisted % 3) / 12.32184f;
                    pp += new Random().nextInt(8) + (mc.player.ticksExisted % 3) / 16.32184f;
                    yy = (float)(Math.round(yy*10))/10;
                    pp = (float)(Math.round(pp*10))/10;
                    pp = Math.max(Math.min(pp, 90), -90);
                    float[] smoot = RotationUtils.mouseSens(yy, pp, mc.player.lastReportedYaw, mc.player.lastReportedPitch);

                    this.rot = new float[]{MathHelper.wrapAngleTo180_float(smoot[0]), smoot[1]};
                    break;
            }
        }
        super.onUpdateEvent(event);
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

}
