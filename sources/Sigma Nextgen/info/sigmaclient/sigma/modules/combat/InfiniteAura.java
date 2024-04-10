package info.sigmaclient.sigma.modules.combat;

import info.sigmaclient.sigma.utils.render.rendermanagers.GlStateManager;
import info.sigmaclient.sigma.config.values.BooleanValue;
import info.sigmaclient.sigma.config.values.ModeValue;
import info.sigmaclient.sigma.config.values.NumberValue;
import info.sigmaclient.sigma.event.net.PacketEvent;
import info.sigmaclient.sigma.event.player.ClickEvent;
import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.event.render.Render3DEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import info.sigmaclient.sigma.utils.TimerUtil;
import info.sigmaclient.sigma.utils.render.RenderUtils;
import net.minecraft.block.AirBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.play.client.CPlayerPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import org.lwjgl.opengl.GL11;

import java.util.*;

import static org.lwjgl.opengl.GL11.*;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class InfiniteAura extends Module {
    public ModeValue SortMode = new ModeValue("SortType", "Distance",
            new String[]{
                    "Distance", "Health"
    });
    public ModeValue clickMode = new ModeValue("AttackMode", "Auto", new String[]{
            "Pre", "Legit", "Post"
    });
    public BooleanValue invisible = new BooleanValue("Invisible", false),
            players = new BooleanValue("Players", false),
            mobs = new BooleanValue("Mobs", false),
            npcs = new BooleanValue("NPC", false),
            animals = new BooleanValue("Animals", false);
    public NumberValue maxCPS = new NumberValue("MaxCPS", 10.0, 0, 20, NumberValue.NUMBER_TYPE.INT){
        @Override
        public void onSetValue() {
            if(this.getValue().floatValue() < minCPS.getValue().floatValue()){
                this.pureSetValue(minCPS.getValue());
            }
        }
    };
    public NumberValue minCPS = new NumberValue("MinCPS", 10.0, 1, 20, NumberValue.NUMBER_TYPE.INT){
        @Override
        public void onSetValue() {
            if(this.getValue().floatValue() > maxCPS.getValue().floatValue()){
                this.pureSetValue(maxCPS.getValue());
            }
        }
    };
    public NumberValue range = new NumberValue("Range", 3.0, 0, 100, NumberValue.NUMBER_TYPE.FLOAT);
    public BooleanValue render = new BooleanValue("Render", false);
    // anims
    public static LivingEntity attackTarget = null;
    private int cps;
    private final TimerUtil attackTimer = new TimerUtil();
    private final ArrayList<LivingEntity> targets = new ArrayList<>();
    public float[] lastRotation = null;
    public InfiniteAura() {
        super("InfiniteAura", Category.Combat, "Automatically attack mobs but too far.");
     registerValue(SortMode);
     registerValue(clickMode);
     registerValue(invisible);
        registerValue(players);
     registerValue(mobs);
     registerValue(animals);
     registerValue(npcs);
     registerValue(maxCPS);
     registerValue(minCPS);
     registerValue(range);
     registerValue(render);
    }
    public boolean isTargetEnable(LivingEntity LivingEntity){
        if(!LivingEntity.isLiving()) return false;
        if(LivingEntity instanceof PlayerEntity) {
            if(AntiBot.isServerBots((PlayerEntity) LivingEntity)) return false;
            if(Teams.isTeam((PlayerEntity) LivingEntity)) return false;
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
        if(LivingEntity instanceof MobEntity) {
            return mobs.isEnable();
        }
        return false;
    }
    public void reset(){
        lastRotation = null;
        attackTarget = null;
        targets.clear();
        poses.clear();
    }
    @Override
    public void onEnable() {
        reset();
        super.onEnable();
    }

    @Override
    public void onPacketEvent(PacketEvent event) {
        super.onPacketEvent(event);
    }

    @Override
    public void onDisable() {
        reset();
        super.onDisable();
    }
    public void setPos(double x, double y, double z) {
        mc.getConnection().sendPacket(new CPlayerPacket.PositionPacket(x, y, z, false));
    }
    ArrayList<Vector3d> poses = new ArrayList<>();
    public void clicked(){

        if (attackTimer.hasTimeElapsed(cps, true)) {
            reset();
            for (Entity entity : mc.world.getLoadedEntityList()) {
                if (!(entity instanceof LivingEntity)) continue;
                LivingEntity e = (LivingEntity) entity;
                if (!isTargetEnable(e)) continue;
                if (entity.getDistance(mc.player) > range.getValue().floatValue()) continue;
                targets.add(e);
            }
            switch (SortMode.getValue()) {
                case "Distance":
                    targets.sort(Comparator.comparingDouble(mc.player::getDistance));
                    break;
                case "Health":
                    targets.sort(Comparator.comparingDouble(LivingEntity::getHealth));
                    break;
            }
            if (targets.size() > 0)
                attackTarget = targets.get(0);
            if(attackTarget == null) return;

            int cpss = (int) (minCPS.getValue().intValue() + (maxCPS.getValue().intValue() - minCPS.getValue().intValue() + 1) * new Random().nextDouble());
            if (cpss == 0) return;
            cps = 1000 / cpss - 1;
            poses = new ArrayList<>();
            Vector3d s = mc.player.getPositionVector(), ts = attackTarget.getPositionVector();
            while(s.squareDistanceTo(ts) > 3) {
                if (poses.isEmpty())
                    poses.add(s);
                double fx = Math.min(ts.x - s.x, 4), dy = Math.min(ts.y - s.y, 4), dz = Math.min(ts.z - s.z, 4);
                Vector3d vec = new Vector3d(s.x + fx, s.y + dy, s.z + dz);
                Vector3d vec2 = new Vector3d(vec.x, vec.y, vec.z);
                int[][] added = new int[][]{{0, 1, 1}, {1, 1, 0}, {-1, 1, 0}, {0, 1, -1}, {0, -1, 1}, {1, -1, 0}, {-1, -1, 0}, {0, -1, -1}, {0, 0, 1}, {1, 0, 0}, {-1, 0, 0}, {0, 0, -1}};
                int count = 0;
                l:
                {
                    while (!(mc.world.getBlockState(new BlockPos(vec)).getBlock() instanceof AirBlock)) {
                        count++;
                        if (count > 5) break;
                        for (int[] a : added) {
                            vec = new Vector3d(vec2.x + a[0], vec2.y + a[1], vec2.z + a[2]);
                            if (mc.world.getBlockState(new BlockPos(vec)).getBlock() instanceof AirBlock) {
                                break l;
                            }
                        }
                    }
                }
                poses.add(vec);
                setPos(vec.x, vec.y, vec.z);
                s = vec;
            }
            attackTimer.reset();
            mc.playerController.attackEntity(mc.player, attackTarget);
            mc.player.swingArm(Hand.MAIN_HAND);
            Collections.reverse(poses);
            for(Vector3d vec : poses){
                setPos(vec.x, vec.y, vec.z);
            }
        }
    }

    @Override
    public void onRender3DEvent(Render3DEvent event) {
        if(render.isEnable()) {
            if (attackTarget != null) {
                GL11.glPushMatrix();
                GlStateManager.disableLighting();
                GL11.glLineWidth(1f);
                GL11.glDisable(GL_TEXTURE_2D);
//                GL11.glEnable(GL_LINE_SMOOTH);
//                GL11.glEnable(GL_POINT_SMOOTH);
                GL11.glEnable(GL_BLEND);
                GL11.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
                GL11.glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
                GL11.glHint(GL_POLYGON_SMOOTH_HINT, GL_NICEST);
                GL11.glHint(GL_POINT_SMOOTH_HINT, GL_NICEST);
                GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
//                GlStateManager.alphaFunc(GL_GREATER, 0.0f);
                GL11.glDepthMask(false);
                GlStateManager.disableDepth();
//                GL11.glShadeModel(GL_SMOOTH);
                final boolean enableBlend = GL11.glIsEnabled(3042);
                final boolean disableAlpha = !GL11.glIsEnabled(3008);
                if (!enableBlend) {
                    GL11.glEnable(3042);
                }
                if (!disableAlpha) {
                    GL11.glDisable(3008);
                }
                GlStateManager.disableCull();
                GL11.glBegin(GL_LINE_STRIP);
                double camX = RenderUtils.getRenderPos().renderPosX;
                double camY = RenderUtils.getRenderPos().renderPosY;
                double camZ = RenderUtils.getRenderPos().renderPosZ;
                for (Vector3d vec : poses) {
                    GL11.glColor4f(1,1,1,1);
                    GL11.glVertex3d(vec.x - camX, vec.y - camY, vec.z - camZ);
                }
                GL11.glEnd();
//                GL11.glShadeModel(GL_FLAT);
                GlStateManager.enableDepth();
                GL11.glDepthMask(true);
//                GL11.glAlphaFunc(GL_GREATER, 0.1f);
                GlStateManager.enableCull();
//                GL11.glDisable(GL_LINE_SMOOTH);
//                GL11.glEnable(GL_POINT_SMOOTH);
                GL11.glHint(GL_LINE_SMOOTH_HINT, GL_DONT_CARE);
                GL11.glHint(GL_POLYGON_SMOOTH_HINT, GL_DONT_CARE);
                GL11.glHint(GL_POINT_SMOOTH_HINT, GL_DONT_CARE);
                GL11.glEnable(GL_TEXTURE_2D);
                if (!enableBlend) {
                    GL11.glDisable(3042);
                }
                if (!disableAlpha) {
                    GL11.glEnable(3008);
                }
                GL11.glPopMatrix();
                GL11.glColor3f(1, 1, 1);
//                RenderUtils.drawCircle(attackTarget, new Color(-1), true, 1);
            }
        }
        super.onRender3DEvent(event);
    }

    @Override
    public void onClickEvent(ClickEvent event) {
        if (clickMode.is("Legit"))
            clicked();
        super.onClickEvent(event);
    }

    @Override
    public void onUpdateEvent(UpdateEvent event) {
        if (event.isPre()) {
            if (clickMode.is("Pre"))
                clicked();
        } else {
            if (clickMode.is("Post"))
                clicked();
        }
        super.onUpdateEvent(event);
    }
}
