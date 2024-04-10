package info.sigmaclient.sigma.modules.world;

import info.sigmaclient.sigma.config.values.BooleanValue;
import info.sigmaclient.sigma.config.values.ColorValue;
import info.sigmaclient.sigma.config.values.NumberValue;
import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.event.render.Render3DEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import info.sigmaclient.sigma.modules.combat.AntiBot;
import info.sigmaclient.sigma.utils.player.RotationUtils;
import info.sigmaclient.sigma.utils.player.ScaffoldUtils;
import info.sigmaclient.sigma.utils.render.RenderUtils;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CHeldItemChangePacket;
import net.minecraft.network.play.client.CPlayerDiggingPacket;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import static info.sigmaclient.sigma.modules.world.AutoCrystal.damageFromPos;
import static info.sigmaclient.sigma.sigma5.utils.BoxOutlineESP.ࡅ揩柿괠竁頉;
import static info.sigmaclient.sigma.gui.Sigma5LoadProgressGui.霥瀳놣㠠釒;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class AutoTrap extends Module {
    public NumberValue trange = new NumberValue("Range", 5, 0, 8, NumberValue.NUMBER_TYPE.FLOAT);
    public NumberValue range = new NumberValue("BreakRange", 3, 0, 6, NumberValue.NUMBER_TYPE.INT);
    public NumberValue delay = new NumberValue("Delay", 0, 0, 1, NumberValue.NUMBER_TYPE.FLOAT);
    public BooleanValue rot = new BooleanValue("Rotations", false);
    public BooleanValue sw = new BooleanValue("Swing", false);
    public ColorValue colorValue = new ColorValue("Color", -1);
    public AutoTrap() {
        super("AutoTrap", Category.World, "Break feet blocks.");
     registerValue(trange);
     registerValue(range);
     registerValue(delay);
     registerValue(rot);
     registerValue(sw);
     registerValue(colorValue);
    }
    ScaffoldUtils.BlockCache currentPos = null;
    ScaffoldUtils.BlockCache lastCurrentPos = null;
    float sb = 0;
    float msb = 1;
    int delays = 0, slot = -1;
    public boolean isOkBlock(BlockPos block2){
        Block block = mc.world.getBlockState(block2).getBlock();
        Block block22 = mc.world.getBlockState(block2.add(0,1,0)).getBlock();
        if(mc.player.getDistance(block2.getX() + 0.5, block2.getY() + 0.5, block2.getZ() + 0.5) - 0.5f > range.getValue().floatValue() + 0.5f) return false;
        return !(block instanceof AirBlock) && !(block instanceof FlowingFluidBlock) && (block22 instanceof AirBlock) && !(block.getDefaultState() == Blocks.BEDROCK.getDefaultState());
    }
    public boolean isOkBlockAir(BlockPos block2){
        Block block = mc.world.getBlockState(block2).getBlock();
        Block block22 = mc.world.getBlockState(block2.add(0,1,0)).getBlock();
        if(mc.player.getDistance(block2.getX() + 0.5, block2.getY() + 0.5, block2.getZ() + 0.5) - 0.5f > range.getValue().floatValue() + 0.5f) return false;
        return (block instanceof AirBlock) && (block22 instanceof AirBlock);
    }

public BooleanValue invisible = new BooleanValue("Invisible", false),
        players = new BooleanValue("Players", false),
        mobs = new BooleanValue("Monster", false),
        npcs = new BooleanValue("NPC", false),
        animals = new BooleanValue("Animals", false);

    public boolean isTargetEnable(LivingEntity LivingEntity){
        if(LivingEntity instanceof PlayerEntity) {
            if(AntiBot.isServerBots((PlayerEntity) LivingEntity)) return false;
            if(LivingEntity.equals(mc.player)) return false;
            if (invisible.isEnable() && LivingEntity.isInvisible())
                return true;
            return players.isEnable() && !LivingEntity.isInvisible();
        }
        if(LivingEntity instanceof VillagerEntity) {
            if (npcs.isEnable()) {
                return true;
            }
        }
        if(LivingEntity instanceof AnimalEntity) {
            return animals.isEnable();
        }
        if(LivingEntity instanceof MonsterEntity) {
            return mobs.isEnable();
        }
        return false;
    }

    public Entity findTarget() {
        List<Entity> e = new ArrayList<>();
        float f = trange.getValue().floatValue();
        for (final Entity o : mc.world.getLoadedEntityList()) {
            if (!(o instanceof LivingEntity)) continue;
            LivingEntity livingBase = (LivingEntity) o;
            if(!isTargetEnable(livingBase)) continue;
            if (livingBase.isAlive() && livingBase != mc.player &&
                    mc.player.getDistance(o) <= f) {
                e.add(o);
            }
        }
        if(e.size() == 0) return null;
        e.sort(Comparator.comparingInt(a -> (int) (a.getDistance(mc.player) * 8000)));
        return e.get(0);
    }
    @Override
    public void onRender3DEvent(Render3DEvent event) {
        if (this.currentPos != null) {
            final int 霥瀳놣㠠釒 = 霥瀳놣㠠釒(colorValue.getColorInt(), 0.4f);
            GL11.glPushMatrix();
            GL11.glDisable(2929);
            RenderUtils.renderPos r = RenderUtils.getRenderPos();
            final double n = currentPos.getPosition().getX() - r.renderPosX;
            final double n2 = currentPos.getPosition().getY() - r.renderPosY;
            final double n3 = currentPos.getPosition().getZ() - r.renderPosZ;
            double size = sb / msb;
            GL11.glDisable(GL_DEPTH_TEST);
            GL11.glDepthMask(false);
            ࡅ揩柿괠竁頉(new AxisAlignedBB(n + 0.5f - size * 0.5f, n2 + 0.5f - size * 0.5f, n3 + 0.5f - size * 0.5f, n + 0.5f + size * 0.5f, n2 + 0.5f + size * 0.5f, n3 + 0.5f + size * 0.5f), 霥瀳놣㠠釒);
            GL11.glEnable(GL_DEPTH_TEST);
            GL11.glDepthMask(true);
            GL11.glEnable(2929);
            GL11.glPopMatrix();
        }
        super.onRender3DEvent(event);
    }

    public ScaffoldUtils.BlockCache getBlockCache(Entity e,BlockPos pos, int range) {
        HashMap<Float, AutoCrystal.SimpleCrystalPos> damageMap = new HashMap<>();
        ArrayList<Float> bestDamage = new ArrayList<>();
        BlockPos targetPos = new BlockPos(e.getPositionVector());
        Vector3d vec3d = new Vector3d(targetPos.getX() + 0.5f, targetPos.getY(), targetPos.getZ() + 0.5f);
        float d = damageFromPos(vec3d.add(-1, 1, 0), e);
        float d2 = damageFromPos(vec3d.add(-1, 1, 0), mc.player);
        d -= d2;
        bestDamage.add(d);
        damageMap.put(d, new AutoCrystal.SimpleCrystalPos(vec3d.add(-1, 1, 0), Direction.UP));

        d = damageFromPos(vec3d.add(1, 1, 0), e);
        d2 = damageFromPos(vec3d.add(1, 1, 0), mc.player);
        d -= d2;
        bestDamage.add(d);
        damageMap.put(d, new AutoCrystal.SimpleCrystalPos(vec3d.add(1, 1, 0), Direction.UP));

        d = damageFromPos(vec3d.add(0, 1, 1), e);
        d2 = damageFromPos(vec3d.add(0, 1, 1), mc.player);
        d -= d2;
        bestDamage.add(d);
        damageMap.put(d, new AutoCrystal.SimpleCrystalPos(vec3d.add(0, 1, 1), Direction.UP));

        d = damageFromPos(vec3d.add(0, 1, -1), e);
        d2 = damageFromPos(vec3d.add(0, 1, -1), mc.player);
        d -= d2;
        bestDamage.add(d);
        damageMap.put(d, new AutoCrystal.SimpleCrystalPos(vec3d.add(0, 1, -1), Direction.UP));
        if(isOkBlock(new BlockPos(vec3d))){
            return new ScaffoldUtils.BlockCache(new BlockPos(vec3d), Direction.UP);
        }
        bestDamage.sort(Comparator.comparingDouble((a) -> a * 100));
        if(bestDamage.size() == 0) return null;
        int i = bestDamage.size() - 1;
        while(i >= 0){
            Float best = bestDamage.get(bestDamage.size() - 1);
            AutoCrystal.SimpleCrystalPos bestPos = damageMap.get(best);
            if (isOkBlockAir(new BlockPos(bestPos.Vector3d)))
                return null;
            i --;
        }
        i = bestDamage.size() - 1;
        while(i >= 0){
            Float best = bestDamage.get(bestDamage.size() - 1);
            AutoCrystal.SimpleCrystalPos bestPos = damageMap.get(best);
            if (isOkBlock(new BlockPos(bestPos.Vector3d)))
                return new ScaffoldUtils.BlockCache(new BlockPos(bestPos.Vector3d), Direction.UP);
            i --;
        }
        return null;
    }
    public void stop(){
        reset();
        sb = 0;
        delays = 0;
        if(lastCurrentPos != null) {
            mc.playerController.sendDiggingPacket(CPlayerDiggingPacket.Action.STOP_DESTROY_BLOCK, lastCurrentPos.getPosition(), lastCurrentPos.getFacing());
            lastCurrentPos = null;
        }
    }
    public int getBest(Block blocks){
        BlockState block = blocks.getDefaultState();
        int bestSlot = -1;
        float digSpeed = 1;
        for(int i = 0;i <= 8;i ++){
            ItemStack itemStack = mc.player.inventory.getStackInSlot(i);
            float d = itemStack.getDestroySpeed(block);
            if(d > digSpeed){
                bestSlot = i;
                digSpeed = d;
            }
        }
        return bestSlot;
    }
    @Override
    public void onUpdateEvent(UpdateEvent event) {
        if(event.isPre()){
            Entity target = findTarget();
            // LOL
            ScaffoldUtils.BlockCache blockCache = target != null ? getBlockCache(target,
                    new BlockPos(mc.player.getPositionVector()) ,range.getValue().intValue()
            ) : null;
            boolean stop = false;
            currentPos = blockCache;
            if(target == null){
                currentPos = null;
                stop = true;
            }
            if(currentPos != null && mc.player.getDistance(currentPos.getPosition().getX() + 0.5, currentPos.getPosition().getY() + 0.5, currentPos.getPosition().getZ() + 0.5) - 0.5f > range.getValue().floatValue() + 0.5f){
                stop = true;
                currentPos = null;
            }
            if(delays > 0){
                delays --;
                currentPos = null;
                stop = true;
            }
            if(stop || currentPos == null){
                stop();
                return;
            }
            BlockPos currentPos = this.currentPos.getPosition();
            if(rot.isEnable()){
                float[] rots = RotationUtils.scaffoldRots(currentPos.getX() + 0.5, currentPos.getY() + 0.5, currentPos.getZ() + 0.5, mc.player.prevRotationYaw, mc.player.prevRotationPitch, 180, 180, false);
                event.yaw = rots[0];
                event.pitch = rots[1];
            }
            Block block = mc.world.getBlockState(blockCache.getPosition()).getBlock();
            if(lastCurrentPos == null) {
                int best = getBest(block);
                if(best != -1){
                    mc.player.inventory.currentItem = best;
                    mc.getConnection().sendPacket(new CHeldItemChangePacket(best));
                    slot = 0;
                }
                mc.player.swingArm(Hand.MAIN_HAND);
                mc.playerController.sendDiggingPacket(CPlayerDiggingPacket.Action.START_DESTROY_BLOCK, currentPos, blockCache.getFacing());
//                if(sw.isEnable())
                lastCurrentPos = this.currentPos;
            }else {
                if(sw.isEnable())
                    mc.player.swingArm(Hand.MAIN_HAND);
            }
            int best = getBest(block);
            int old = mc.player.inventory.currentItem;
            if(best != -1){
                mc.player.inventory.currentItem = best;
            }
            sb += block.getPlayerRelativeBlockHardness(block.getDefaultState(), mc.player, mc.world, blockCache.getPosition());
            if(sb >= 1.0F){
                stop();
                delays = (int) (delay.getValue().floatValue() * 10);
            }
        }
        super.onUpdateEvent(event);
    }
    @Override
    public void onEnable() {
        currentPos = null;
        sb = 0;
        delays = 0;
        stop();
        super.onEnable();
    }
    public void reset(){
        if(slot != -1){
            mc.player.inventory.currentItem = slot;
            mc.getConnection().sendPacket(new CHeldItemChangePacket(slot));
            slot = -1;
        }
    }
    @Override
    public void onDisable() {
        reset();
        if(currentPos != null){
            stop();
            lastCurrentPos = null;
            currentPos = null;
        }
        super.onDisable();
    }
}
