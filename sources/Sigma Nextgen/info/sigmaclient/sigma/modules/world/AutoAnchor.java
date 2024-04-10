package info.sigmaclient.sigma.modules.world;

import info.sigmaclient.sigma.config.values.BooleanValue;
import info.sigmaclient.sigma.config.values.ColorValue;
import info.sigmaclient.sigma.config.values.ModeValue;
import info.sigmaclient.sigma.config.values.NumberValue;
import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.event.player.WindowUpdateEvent;
import info.sigmaclient.sigma.event.player.WorldEvent;
import info.sigmaclient.sigma.event.render.Render3DEvent;
import info.sigmaclient.sigma.gui.Sigma5LoadProgressGui;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import info.sigmaclient.sigma.modules.combat.AntiBot;
import info.sigmaclient.sigma.modules.movement.BlockFly;
import info.sigmaclient.sigma.utils.TimerUtil;
import info.sigmaclient.sigma.utils.VecUtils;
import info.sigmaclient.sigma.utils.player.Rotation;
import info.sigmaclient.sigma.utils.player.RotationUtils;
import info.sigmaclient.sigma.utils.player.ScaffoldUtils;
import info.sigmaclient.sigma.utils.render.RenderUtils;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.item.EnderCrystalEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EnderCrystalItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CHeldItemChangePacket;
import net.minecraft.network.play.client.CPlayerDiggingPacket;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import org.lwjgl.opengl.GL11;

import java.util.*;

import static info.sigmaclient.sigma.sigma5.utils.BoxOutlineESP.ࡅ揩柿괠竁頉;
import static info.sigmaclient.sigma.utils.player.RotationUtils.scaffoldRots;
import static info.sigmaclient.sigma.utils.render.RenderUtils.霥瀳놣㠠釒;
import static net.minecraft.world.Explosion.getBlockDensity;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class AutoAnchor extends Module {
    public NumberValue range = new NumberValue("Range", 5, 0, 8, NumberValue.NUMBER_TYPE.FLOAT);
    public NumberValue mcps = new NumberValue("MinCPS", 10, 0, 20, NumberValue.NUMBER_TYPE.INT){
        @Override
        public void onSetValue() {
            if(this.getValue().intValue() > cps.getValue().intValue()){
                this.pureSetValue(cps.getValue().intValue());
            }
            super.onSetValue();
        }
    };
    public NumberValue cps = new NumberValue("MaxCPS", 12, 0, 20, NumberValue.NUMBER_TYPE.INT){
        @Override
        public void onSetValue() {
            if(this.getValue().intValue() < mcps.getValue().intValue()){
                this.pureSetValue(mcps.getValue().intValue());
            }
            super.onSetValue();
        }
    };
    public NumberValue placeRange = new NumberValue("PlaceRange", 4.5, 0, 6, NumberValue.NUMBER_TYPE.FLOAT){
    };
    public NumberValue maxSelfDamage = new NumberValue("MaxSelfDamage", 20, 0, 40, NumberValue.NUMBER_TYPE.FLOAT){
    };
    public NumberValue minTargetDamage = new NumberValue("MibTargetDamage", 20, 0, 40, NumberValue.NUMBER_TYPE.FLOAT){
    };
    static ModeValue damageCalc = new ModeValue("Damage Calc", "0.1", new String[]{"0.1", "0.2", "0.3", "0.75", "Full", "Half", "Sqrt", "Sqrt2"});
    public NumberValue gitRange = new NumberValue("HitRange", 3, 0, 6, NumberValue.NUMBER_TYPE.FLOAT){
    };
    public BooleanValue invisible = new BooleanValue("Invisible", false),
            players = new BooleanValue("Players", false),
            mobs = new BooleanValue("Monster", false),
            npcs = new BooleanValue("NPC", false),
            animals = new BooleanValue("Animals", false);

    public BooleanValue ghostHand = new BooleanValue("GhostHand", false);
    public BooleanValue betterHand = new BooleanValue("GhostHand+", false){
        @Override
        public boolean isHidden() {
            return !ghostHand.isEnable();
        }
    };

    public BooleanValue antiPiston = new BooleanValue("Head Trap", false);
    AutoTrap antiCity = new AutoTrap();
    public BooleanValue antiCitys = new BooleanValue("Anti City", false);
    public NumberValue breakRange = new NumberValue("Break Range", 3, 0, 6, NumberValue.NUMBER_TYPE.INT);
    public NumberValue delay = new NumberValue("Delay", 0, 0, 1, NumberValue.NUMBER_TYPE.FLOAT);
    public BooleanValue rotations = new BooleanValue("Rotations", false);
    public BooleanValue sw = new BooleanValue("Swing", false);
    public ColorValue colorValue = new ColorValue("Color", -1);
    public BooleanValue antiTrap = new BooleanValue("Anti CTrap", false);


    long calc_cps;
    TimerUtil timerUtil = new TimerUtil();
    BlockPos lastPos = null, lastPos2 = null;
    public AutoAnchor() {
        super("AutoAnchor", Category.World, "Auto anchor");
     registerValue(range);
     registerValue(mcps);
     registerValue(cps);
     registerValue(placeRange);
     registerValue(gitRange);
     registerValue(maxSelfDamage);
     registerValue(minTargetDamage);
     registerValue(damageCalc);
     registerValue(invisible);
     registerValue(players);
     registerValue(animals);
     registerValue(npcs);
     registerValue(mobs);
     registerValue(ghostHand);
     registerValue(betterHand);
//     registerValue(antiPiston);
//     registerValue(antiCitys);
//     registerValue(breakRange);
//     registerValue(delay);
//     registerValue(rotations);
//     registerValue(sw);
//     registerValue(colorValue);
//     registerValue(antiTrap);
    }

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
        if(LivingEntity instanceof MonsterEntity || LivingEntity instanceof EnderDragonEntity) {
            return mobs.isEnable();
        }
        return false;
    }
    public Entity findTarget() {
        List<Entity> e = new ArrayList<>();
        float f = range.getValue().floatValue();
        for (final Entity o : mc.world.getLoadedEntityList()) {
            if (!(o instanceof LivingEntity)) continue;
            LivingEntity livingBase = (LivingEntity) o;
            if(!isTargetEnable(livingBase)) continue;
            if (livingBase.isAlive() && livingBase != mc.player && mc.player.getDistance(o) <= f) {
                e.add(o);
            }
        }
        if(e.size() == 0) return null;
        e.sort(Comparator.comparingDouble(a -> a.getDistance(mc.player)));
        return e.get(0);
    }
    public static float damageFromPos(Vector3d crystalPos, Entity tentity) {
        float f3 = 6.0F * 2.0F;
        double d12 = tentity.getDistance(crystalPos.x, crystalPos.y + 1.7, crystalPos.z) / (double) f3;

        if (d12 <= 1.0D) {
            double d14 = getBlockDensity(new Vector3d(crystalPos.x, crystalPos.y + 1, crystalPos.z), tentity);
            double d10 = (1.0D - d12) * d14;
            return (float) ((int) ((d10 * d10 + d10) / 2.0D * 7.0D * (double) f3 + 1.0D));
        }
        return 0f;
    }
    static class SimpleCrystalPos{
        public Vector3d Vector3d;
        public Direction facing;
        public SimpleCrystalPos(Vector3d pos, Direction face){
            this.Vector3d = pos;
            this.facing = face;
        }
    }
    public Direction calcFace(BlockPos blockPos){
        Vector3d eye = mc.player.getEyePosition(1.0F);
        ArrayList<SimpleCrystalPos> directions = new ArrayList<>();
        for(Direction direction : Direction.VALUES) {
            BlockState b = mc.world.getBlockState(blockPos.add(direction.getXOffset(), direction.getYOffset(), direction.getZOffset()));
            if(b.getBlock() instanceof AirBlock)
                directions.add(new SimpleCrystalPos(new Vector3d(blockPos.getX(), blockPos.getY(), blockPos.getZ()).add(direction.getXOffset(), direction.getYOffset(), direction.getZOffset()), direction));
        }
        directions.sort(Comparator.comparingDouble((e)->e.Vector3d.distanceTo(eye)));
        return directions.size() > 0 ? directions.get(0).facing : null;
    }
    public SimpleCrystalPos findBestCrystalPos(LivingEntity target){
        HashMap<Float, SimpleCrystalPos> damageMap = new HashMap<>();
        ArrayList<Float> bestDamage = new ArrayList<>();
        for(int x = -4; x <= 4; x ++)
            for(int y = -3; y <= 3; y ++)
                for(int z = -4; z <= 4; z ++){
                    Vector3d nn = new Vector3d(mc.player.getPosX() + x, mc.player.getPosY() + y, mc.player.getPosZ() + z);
                    BlockPos bp = new BlockPos(nn);
                    nn.x = bp.getX() + 0.5f;
                    nn.y = bp.getY() + 0.5f;
                    nn.z = bp.getZ() + 0.5f;
                    Block bb = mc.world.getBlockState(new BlockPos(nn)).getBlock();
                    Block bb2 = mc.world.getBlockState(new BlockPos(nn.add(0, 1, 0))).getBlock();
//                    Block bb3 = mc.world.getBlockState(new BlockPos(nn.add(0, 2, 0))).getBlock();
//                    if(!(BlockFly.isBlockValid(bb))){
//                        continue;
//                    }
                    if(!(bb2 instanceof AirBlock)){
                        continue;
                    }
                    if((bb instanceof AirBlock) || (bb instanceof FireBlock) || (bb instanceof RespawnAnchorBlock)){
                        continue;
                    }
                    if(mc.player.getDistanceSq(nn.x, nn.y, nn.z) > placeRange.getValue().floatValue() * placeRange.getValue().floatValue()){
                        continue;
                    }
                    float[] calcRotation = scaffoldRots(nn.x, nn.y, nn.z, mc.player.lastReportedYaw, mc.player.lastReportedPitch, 180, 180, false);
                    RayTraceResult r = mc.player.customPick(4.5, 1.0F, calcRotation[0], calcRotation[1]);
                    if(r.getType() != RayTraceResult.Type.BLOCK)
                        continue;
                    if(!mc.world.getEntitiesWithinAABB(
                            LivingEntity.class, new AxisAlignedBB(
                                    bp.getX(),
                                    bp.getY()+1,
                                    bp.getZ(),
                                    bp.getX() + 1,
                                    bp.getY() + 3,
                                    bp.getZ() + 1
                            )
                    ).isEmpty()) continue;
                    Direction placeFace = Direction.UP;
                    boolean canPlace = true;

                    if(!canPlace || placeFace == null) continue;

                    float d = damageFromPos(new Vector3d(new BlockPos(nn)).add(0.5, 1.5, 0.5), target);
                    // no damage
                    if(d == 0) continue;
                    float d2 = damageFromPos(new Vector3d(new BlockPos(nn)).add(0.5, 1.5, 0.5), mc.player);
                    if(d < minTargetDamage.getValue().floatValue()) continue;
                    if(d2 > maxSelfDamage.getValue().floatValue()) continue;
//                    if(d2 > 18) continue;
                    switch (damageCalc.getValue()){
                        case "0.1"->{
                            d -= d2 * 0.1;
                        }
                        case "0.2"->{
                            d -= d2 * 0.2;
                        }
                        case "0.3"->{
                            d -= d2 * 0.3;
                        }
                        case "0.75"->{
                            d -= d2 * 0.75;
                        }
                        case "Full"->{
                        }
                        case "Half"->{
                            d -= d2 * 0.5;
                        }
                        case "Sqrt"->{
                            d -= Math.sqrt(d2);
                        }
                        case "Sqrt2"->{
                            d -= Math.sqrt(Math.sqrt(d2));
                        }
                    }
                    bestDamage.add(d);
                    damageMap.put(d, new SimpleCrystalPos(nn, placeFace));
                }
//        ChatUtils.sendMessageWithPrefix("wdf " + bestDamage.size());
        bestDamage.sort(Comparator.comparingDouble((a) -> a * 100));
        if(bestDamage.size() == 0) return null;
        Float best = bestDamage.get(bestDamage.size() - 1);
        SimpleCrystalPos bestPos = damageMap.get(best);
        return bestPos;
    }

    @Override
    public void onWindowUpdateEvent(WindowUpdateEvent event) {
        int slot = getCrystalSlot();
        int slot2 = getGSSlot();
        if(slot2 == -1){
            return;
        }
        Entity target = findTarget();
        if(target == null) return;
        int sslot = mc.player.inventory.currentItem;
        if(!ghostHand.isEnable()){
            mc.player.inventory.currentItem = slot;
        }
        if(lastPos2 != null) {
            lastPos2 = null;
            return;
        }

        if(timerUtil.hasTimeElapsed(calc_cps)) {
            lastPos = null;
            SimpleCrystalPos bestPos = findBestCrystalPos((LivingEntity) target);
            if(bestPos == null){
                return;
            }
            int cpss = (int) (mcps.getValue().intValue() + (cps.getValue().intValue() - mcps.getValue().intValue() + 1) * new Random().nextDouble());
            if(cpss == 0) return;
            timerUtil.reset();
            calc_cps = 1000 / cpss - 10;
            int oslot = mc.player.inventory.currentItem;
            if(ghostHand.isEnable()){
                mc.player.inventory.currentItem = slot;
                if(betterHand.isEnable())
                    mc.getConnection().sendPacket(new CHeldItemChangePacket(slot));
            }
            float[] calcRotation = scaffoldRots(
                    new BlockPos(bestPos.Vector3d).getX() + 0.5,
                    new BlockPos(bestPos.Vector3d).getY() + 0.5,
                    new BlockPos(bestPos.Vector3d).getZ() + 0.5,
                    mc.player.lastReportedYaw,
                    mc.player.lastReportedPitch,
                    180,
                    180,
                    false);
            RotationUtils.movementFixYaw = calcRotation[0];
            RotationUtils.movementFixPitch = calcRotation[1];
            RotationUtils.fixing = true;
            mc.playerController.processRightClickBlock(
                    mc.player,
                    mc.world,
                    new BlockPos(bestPos.Vector3d),
                    bestPos.facing,
                    VecUtils.blockPosRedirection(new BlockPos(bestPos.Vector3d), bestPos.facing),
                    Hand.MAIN_HAND
            );
            mc.player.swingArm(Hand.MAIN_HAND);
            if(ghostHand.isEnable()){
                mc.player.inventory.currentItem = oslot;
                if(betterHand.isEnable())
                    mc.getConnection().sendPacket(new CHeldItemChangePacket(oslot));
            }
            lastPos = new BlockPos(bestPos.Vector3d);
            lastPos2 = new BlockPos(bestPos.Vector3d);
            BlockPos bp = lastPos2.add(0, 1, 0);
            if(mc.world.getBlockState(bp).getBlock() instanceof RespawnAnchorBlock){
                if(sslot != slot2) {
//                        mc.getConnection().sendPacket(new CHeldItemChangePacket(slot2));
                    mc.player.inventory.currentItem = slot2;
                }
                for(int i = 0; i < 5 ; i ++){
                    mc.playerController.processRightClickBlock(
                            mc.player,
                            mc.world,
                            new BlockPos(bp),
                            Direction.UP,
                            VecUtils.blockPosRedirection(new BlockPos(bp), Direction.UP),
                            Hand.MAIN_HAND
                    );
                }
                mc.player.swingArm(Hand.MAIN_HAND);
                if(sslot != slot2) {
                    mc.player.inventory.currentItem = sslot;
//                        mc.getConnection().sendPacket(new CHeldItemChangePacket(sslot));
                }
            }
        }
        super.onWindowUpdateEvent(event);
    }

    @Override
    public void onWorldEvent(WorldEvent event) {
        super.onWorldEvent(event);
    }

    @Override
    public void onEnable() {
        if(antiCitys.isEnable()) {
            antiCity.onEnable();
        }
        lastPos = null;
        lastPos2 = null;
        super.onEnable();
    }

    @Override
    public void onRender3DEvent(Render3DEvent event) {
        if(antiCitys.isEnable())
            antiCity.onRender3DEvent(event);
        if(lastPos != null) {
            GL11.glDisable(GL_DEPTH_TEST);
            GL11.glDepthMask(false);
            double camX = RenderUtils.getRenderPos().renderPosX;
            double camY = RenderUtils.getRenderPos().renderPosY;
            double camZ = RenderUtils.getRenderPos().renderPosZ;
            final double n = lastPos.getX();
            final double x = n - camX;
            final double n2 = lastPos.getY();
            final double y = n2 - camY;
            final double n3 = lastPos.getZ();
            final double z = n3 - camZ;
            ࡅ揩柿괠竁頉(new AxisAlignedBB(
                    x,y,z,x+1,y+1,z+1
            ), 霥瀳놣㠠釒(-16723258, 0.2f));
            GL11.glEnable(GL_DEPTH_TEST);
            GL11.glDepthMask(true);
//            RenderUtils.drawCircle(lastPos, new Color(-1));
        }
        super.onRender3DEvent(event);
    }
    public int getCrystalSlot(){
        for(int i=0;i<9;i++){
            ItemStack is = mc.player.inventory.getStackInSlot(i);
            if(is.getTranslationKey().contains("anchor")){
                return i;
            }
        }
        return -1;
    }
    public int getGSSlot(){
        for(int i=0;i<9;i++){
            ItemStack is = mc.player.inventory.getStackInSlot(i);
            if(is.getTranslationKey().contains("glow") && is.getTranslationKey().contains("stone")){
                return i;
            }
        }
        return -1;
    }
    @Override
    public void onUpdateEvent(UpdateEvent event) {
        if(event.isPre()){
        }
        super.onUpdateEvent(event);
    }

    @Override
    public void onDisable() {
        if(antiCitys.isEnable()) {
            antiCity.onDisable();
        }
        super.onDisable();
    }

    public class AutoTrap extends Module {
        public AutoTrap() {
            super("AutoTrap", Category.World, "Break feet blocks.");
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
            float f = breakRange.getValue().floatValue();
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
                final int 霥瀳놣㠠釒 = Sigma5LoadProgressGui.霥瀳놣㠠釒(colorValue.getColorInt(), 0.2f);
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
            HashMap<Float, AutoAnchor.SimpleCrystalPos> damageMap = new HashMap<>();
            ArrayList<Float> bestDamage = new ArrayList<>();
            BlockPos targetPos = new BlockPos(e.getPositionVector());
            Vector3d vec3d = new Vector3d(targetPos.getX() + 0.5f, targetPos.getY(), targetPos.getZ() + 0.5f);
            float d = damageFromPos(vec3d.add(-1, 1, 0), e);
            float d2 = damageFromPos(vec3d.add(-1, 1, 0), mc.player);
            d -= d2;
            bestDamage.add(d);
            damageMap.put(d, new AutoAnchor.SimpleCrystalPos(vec3d.add(-1, 0.5, 0), Direction.UP));

            d = damageFromPos(vec3d.add(1, 1, 0), e);
            d2 = damageFromPos(vec3d.add(1, 1, 0), mc.player);
            d -= d2;
            bestDamage.add(d);
            damageMap.put(d, new AutoAnchor.SimpleCrystalPos(vec3d.add(1, 0.5, 0), Direction.UP));

            d = damageFromPos(vec3d.add(0, 1, 1), e);
            d2 = damageFromPos(vec3d.add(0, 1, 1), mc.player);
            d -= d2;
            bestDamage.add(d);
            damageMap.put(d, new AutoAnchor.SimpleCrystalPos(vec3d.add(0, 0.5, 1), Direction.UP));

            d = damageFromPos(vec3d.add(0, 1, -1), e);
            d2 = damageFromPos(vec3d.add(0, 1, -1), mc.player);
            d -= d2;
            bestDamage.add(d);
            damageMap.put(d, new AutoAnchor.SimpleCrystalPos(vec3d.add(0, 0.5, -1), Direction.UP));
            if(isOkBlock(new BlockPos(vec3d))){
                return new ScaffoldUtils.BlockCache(new BlockPos(vec3d), Direction.UP);
            }
            bestDamage.sort(Comparator.comparingDouble((a) -> a * 100));
            if(bestDamage.size() == 0) return null;
            int i = bestDamage.size() - 1;
            while(i >= 0){
                Float best = bestDamage.get(bestDamage.size() - 1);
                AutoAnchor.SimpleCrystalPos bestPos = damageMap.get(best);
                if (isOkBlockAir(new BlockPos(bestPos.Vector3d)))
                    return null;
                i --;
            }
            i = bestDamage.size() - 1;
            while(i >= 0){
                Float best = bestDamage.get(bestDamage.size() - 1);
                AutoAnchor.SimpleCrystalPos bestPos = damageMap.get(best);
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
                int best = getBest(mc.world.getBlockState(lastCurrentPos.getPosition()).getBlock());
                int old = mc.player.inventory.currentItem;
                if(best != -1){
                    mc.player.inventory.currentItem = best;
                    mc.getConnection().sendPacket(new CHeldItemChangePacket(best));
                    slot = old;
                }
                mc.player.swingArm(Hand.MAIN_HAND);
                mc.playerController.sendDiggingPacket(CPlayerDiggingPacket.Action.START_DESTROY_BLOCK, lastCurrentPos.getPosition(), lastCurrentPos.getFacing());
                mc.playerController.sendDiggingPacket(CPlayerDiggingPacket.Action.STOP_DESTROY_BLOCK, lastCurrentPos.getPosition(), lastCurrentPos.getFacing());
                if(best != -1){
                    mc.player.inventory.currentItem = old;
                    mc.getConnection().sendPacket(new CHeldItemChangePacket(old));
                    slot = -1;
                    mc.player.inventory.currentItem = best;
                }
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
        public boolean updateEvent(UpdateEvent event) {
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
                    return false;
                }
                BlockPos currentPos = this.currentPos.getPosition();
                if(rotations.isEnable()){
                    float[] rots = RotationUtils.scaffoldRots(currentPos.getX() + 0.5, currentPos.getY() + 0.5, currentPos.getZ() + 0.5, mc.player.prevRotationYaw, mc.player.prevRotationPitch, 180, 180, false);
                    RotationUtils.movementFixYaw = rots[0];
                    RotationUtils.movementFixPitch = rots[1];
                    RotationUtils.fixing = true;
                }
                Block block = mc.world.getBlockState(blockCache.getPosition()).getBlock();
                if(lastCurrentPos == null) {
                    int best = getBest(block);
                    int old = mc.player.inventory.currentItem;
                    if(best != -1){
                        mc.player.inventory.currentItem = best;
                        mc.getConnection().sendPacket(new CHeldItemChangePacket(best));
                        slot = old;
                    }
                    mc.player.swingArm(Hand.MAIN_HAND);
                    mc.playerController.sendDiggingPacket(CPlayerDiggingPacket.Action.START_DESTROY_BLOCK, currentPos, blockCache.getFacing());
                    mc.playerController.sendDiggingPacket(CPlayerDiggingPacket.Action.STOP_DESTROY_BLOCK, currentPos, blockCache.getFacing());
                    if(best != -1){
                        mc.player.inventory.currentItem = old;
                        mc.getConnection().sendPacket(new CHeldItemChangePacket(old));
                        slot = -1;
                    }
//                if(sw.isEnable())
                    lastCurrentPos = this.currentPos;
                }else {
                    if(sw.isEnable())
                        mc.player.swingArm(Hand.MAIN_HAND);
                }
                int best = getBest(block);
                int old = mc.player.inventory.currentItem;
//                if(best != -1){
//                    mc.player.inventory.currentItem = best;
//                }
                if(sb >= 1.0F){
                    stop();
                    delays = (int) (delay.getValue().floatValue() * 10);
                }
                int old2 = mc.player.inventory.currentItem;
                if(best != -1){
                    mc.player.inventory.currentItem = best;
                }
                sb += block.getPlayerRelativeBlockHardness(block.getDefaultState(), mc.player, mc.world, blockCache.getPosition());
                if(best != -1){
                    mc.player.inventory.currentItem = old2;
                }
            }
            return true;
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
}
