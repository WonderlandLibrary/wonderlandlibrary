package info.sigmaclient.sigma.modules.combat;

import info.sigmaclient.sigma.SigmaNG;
import info.sigmaclient.sigma.config.values.BooleanValue;
import info.sigmaclient.sigma.config.values.NumberValue;
import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.event.player.WindowUpdateEvent;
import info.sigmaclient.sigma.event.player.WorldEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import info.sigmaclient.sigma.modules.movement.BlockFly;
import info.sigmaclient.sigma.utils.Variable;
import info.sigmaclient.sigma.utils.VecUtils;
import info.sigmaclient.sigma.utils.player.RotationUtils;
import net.minecraft.block.AirBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CHeldItemChangePacket;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static info.sigmaclient.sigma.modules.world.Timer.violation;
import static info.sigmaclient.sigma.utils.player.RotationUtils.scaffoldRots;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class AutoLava extends Module {
    public static NumberValue range = new NumberValue("MaxRange", 5, 0, 8, NumberValue.NUMBER_TYPE.FLOAT);
    public static NumberValue minRange = new NumberValue("MinRange", 5, 0, 8, NumberValue.NUMBER_TYPE.FLOAT);
    public static NumberValue delay = new NumberValue("Delay", 1, 0, 1, NumberValue.NUMBER_TYPE.FLOAT);
    static BooleanValue killauraOnly = new BooleanValue("Killaura Only", true);
    public AutoLava() {
        super("AutoLava", Category.Combat, "Loser lava user");
     registerValue(range);
     registerValue(minRange);
     registerValue(delay);
     registerValue(killauraOnly);
    }
    public static int delayTicks = 0, durationTicks = 0, useTicks = 0;
    public static boolean isStopping = false, back = false, back2 = false;
    static BlockPos backPos = null;
    static int oldSlot = -1;
    public static Entity findTarget() {
        List<Entity> e = new ArrayList<>();
        float f = range.getValue().floatValue();
        for (final Entity o : mc.world.getLoadedEntityList()) {
            if(!(o instanceof LivingEntity)) continue;
            LivingEntity livingBase = (LivingEntity) o;
            if (o instanceof PlayerEntity) {
                if (AntiBot.isServerBots((PlayerEntity) livingBase)) continue;
                if (livingBase.isAlive() && livingBase != mc.player &&
                        mc.player.getDistanceNearest(o) <= f) {
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
        reset();
        super.onWorldEvent(event);
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }
    public static int getGSSlot(){
        for(int i=0;i<9;i++){
            ItemStack is = mc.player.inventory.getStackInSlot(i);
            if(is.getTranslationKey().contains("lava") && is.getTranslationKey().contains("bucket")){
                return i;
            }
        }
        return -1;
    }
    public static void onUpdate() {
        int slot = getGSSlot();
        if(back){
//            oldSlot = mc.player.inventory.currentItem;
            back = false;
            Variable.stop_killaura = true;
            float[] calcRotation = scaffoldRots(
                    backPos.getX() + 0.5,
                    backPos.getY() + 1,
                    backPos.getZ() + 0.5,
                    mc.player.lastReportedYaw,
                    mc.player.lastReportedPitch,
                    180,
                    180,
                    false);
            RotationUtils.movementFixYaw = calcRotation[0];
            RotationUtils.movementFixPitch = calcRotation[1];
            RotationUtils.fixing = true;
            if(mc.playerController.processRightClickBlock(
                    mc.player,
                    mc.world,
                    backPos,
                    Direction.UP,
                    VecUtils.blockPosRedirection(backPos, Direction.UP),
                    Hand.MAIN_HAND
            ).isSuccess()){
            }
            back2 = true;
            mc.player.swingArm(Hand.MAIN_HAND);
            return;
        }else if(back2) {
            back2 = false;
            if(mc.playerController.processRightClickBlock(
                    mc.player,
                    mc.world,
                    backPos,
                    Direction.UP,
                    VecUtils.blockPosRedirection(backPos, Direction.UP),
                    Hand.MAIN_HAND
            ).isSuccess()){
            }
            mc.player.swingArm(Hand.MAIN_HAND);
            return;
        } else if(oldSlot != -1){
                mc.player.inventory.currentItem = oldSlot;
//            mc.getConnection().sendPacket(new CHeldItemChangePacket(mc.player.inventory.currentItem));
                oldSlot = -1;
                return;
            }
        if(slot == -1) return;
//        if(!SigmaNG.SigmaNG.moduleManager.getModule(AutoTimer.class).enabled)
//            return;
        Entity target;
        // ?
            target = findTarget();
//        } else {
//            target = Killaura.attackTarget;
//        }
        if (killauraOnly.getValue()) {
            if(!SigmaNG.getSigmaNG().moduleManager.getModule(Killaura.class).enabled) return;
        }
        if (target == null) {
            reset();
            return;
        }
        double dist = range.getValue().floatValue();
        double minDist = minRange.getValue().floatValue();
        if (mc.player.getDistanceNearest(target) > dist || mc.player.getDistanceNearest(target) < minDist) {
            reset();
            return;
        }
        delayTicks++;
        if (delayTicks > (int) (delay.getValue().floatValue() * 10)) {
            oldSlot = mc.player.inventory.currentItem;
            mc.player.inventory.currentItem = slot;
//            mc.getConnection().sendPacket(new CHeldItemChangePacket(mc.player.inventory.currentItem));
            Variable.stop_killaura = true;
            BlockPos pis = new BlockPos(target.getPositionVec()).add(0, -0.9, 0);
            if(BlockFly.isBlockValid(mc.world.getBlockState(pis).getBlock()) && mc.world.getBlockState(pis.add(0, 1, 0)).getBlock() instanceof AirBlock && mc.player.getDistance(target) > 1.5f){
                backPos = pis;
                back = true;
            }
            reset();
        }else{
//            mc.timer.setTimerSpeed(1f);
        }
    }

    @Override
    public void onWindowUpdateEvent(WindowUpdateEvent event) {
        onUpdate();
        super.onWindowUpdateEvent(event);
    }
    public static void reset(){
//        mc.timer.setTimerSpeed(1f);
        durationTicks = 0;
        delayTicks = 0;
        isStopping = false;
        useTicks = 0;
//        back = false;
    }
    @Override
    public void onDisable() {
//        mc.timer.setTimerSpeed(1.0f);
        super.onDisable();
    }

}
