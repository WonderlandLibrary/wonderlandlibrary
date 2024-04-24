package Method.Client.module.render;


import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.visual.ChatUtils;
import Method.Client.utils.visual.ColorUtils;
import Method.Client.utils.visual.RenderUtils;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.item.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static Method.Client.Main.setmgr;
import static Method.Client.utils.visual.RenderUtils.RenderBlock;

public class Trajectories extends Module {
    public Trajectories() {
        super("Trajectories", Keyboard.KEY_NONE, Category.RENDER, "Trajectories");
    }

    public final List<Bpos> Pos = new ArrayList<>();

    Setting FindEpearl = setmgr.add(new Setting("Follow Pearl", this, true));
    Setting ChatPrint = setmgr.add(new Setting("ChatPrint", this, false, FindEpearl));
    Setting RenderTime = setmgr.add(new Setting("RenderTime", this, 5, 0, 35, false, FindEpearl));
    Setting Mode = setmgr.add(new Setting("Mode", this, "Xspot", BlockEspOptions()));
    Setting LineWidth = setmgr.add(new Setting("LineWidth", this, 1, 0, 3, false));
    Setting Color = setmgr.add(new Setting("Color", this, .22, 1, .6, .65));
    Setting skeleton = setmgr.add(new Setting("Skeleton", this, false));

    @Subscribe
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (FindEpearl.getValBoolean()) {
            for (Entity entity : mc.world.loadedEntityList) {
                if (entity instanceof EntityEnderPearl) {
                    EntityEnderPearl e = (EntityEnderPearl) entity;
                    boolean notfound = true;
                    for (Bpos po : Pos) {
                        if (po.getUuid().equals(e.getUniqueID())) {
                            notfound = false;
                            break;
                        }
                    }
                    if (notfound) {
                        Pos.add(new Bpos(new ArrayList<>(Collections.singletonList(e.getPositionVector())), e.getUniqueID(), System.currentTimeMillis()));
                        if (ChatPrint.getValBoolean())
                            ChatUtils.message(e.perlThrower.toString() + " Threw a pearl!");
                    } else {
                        for (Bpos po : Pos) {
                            if (po.uuid.equals(e.getUniqueID())) {
                                po.vec3ds.add(e.getPositionVector());
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean itemcheck(Item item) {
        return item instanceof ItemBow || item instanceof ItemSnowball
                || item instanceof ItemEgg || item instanceof ItemEnderPearl
                || item instanceof ItemSplashPotion
                || item instanceof ItemLingeringPotion
                || item instanceof ItemFishingRod;
    }

    @Subscribe
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        for (Entity entity : mc.world.loadedEntityList) {
            if (entity instanceof EntityLivingBase) {
                EntityLivingBase livingBase = (EntityLivingBase) entity;
                if (livingBase instanceof EntitySkeleton && !skeleton.getValBoolean())
                    return;
                if (itemcheck(livingBase.getHeldItemMainhand().getItem()) || itemcheck(livingBase.getHeldItemOffhand().getItem())) {
                    livingBase.getActiveItemStack();

                    // This code is used all over I really have no idea where it came from
                    // I compacted it as much as possible
                    boolean usingBow = livingBase.getActiveItemStack().getItem() instanceof ItemBow;
                    double arrowPosX = livingBase.lastTickPosX + (livingBase.posX - livingBase.lastTickPosX) * event.getPartialTicks() - MathHelper.cos((float) Math.toRadians(livingBase.rotationYaw)) * 0.16F;
                    double arrowPosY = livingBase.lastTickPosY + (livingBase.posY - livingBase.lastTickPosY) * event.getPartialTicks() + livingBase.getEyeHeight() - 0.1;
                    double arrowPosZ = livingBase.lastTickPosZ + (livingBase.posZ - livingBase.lastTickPosZ) * event.getPartialTicks() - MathHelper.sin((float) Math.toRadians(livingBase.rotationYaw)) * 0.16F;
                    float arrowMotionFactor = usingBow ? 1F : 0.4F;
                    float yaw = (float) Math.toRadians(livingBase.rotationYaw);
                    float pitch = (float) Math.toRadians(livingBase.rotationPitch);
                    float arrowMotionX = -MathHelper.sin(yaw) * MathHelper.cos(pitch) * arrowMotionFactor;
                    float arrowMotionY = -MathHelper.sin(pitch) * arrowMotionFactor;
                    float arrowMotionZ = MathHelper.cos(yaw) * MathHelper.cos(pitch) * arrowMotionFactor;
                    double arrowMotion = Math.sqrt(arrowMotionX * arrowMotionX + arrowMotionY * arrowMotionY + arrowMotionZ * arrowMotionZ);
                    double bowPower = 1.5D;
                    if (usingBow) {
                        bowPower = (72000 - livingBase.getItemInUseCount()) / 20F;
                        bowPower = (bowPower * bowPower + bowPower * 2F) / 3F;
                        bowPower = (bowPower > 1F || bowPower <= 0.1F) ? 3F : bowPower * 3F;
                    }
                    arrowMotionX = (float) ((arrowMotionX / arrowMotion) * bowPower);
                    arrowMotionY = (float) ((arrowMotionY / arrowMotion) * bowPower);
                    arrowMotionZ = (float) ((arrowMotionZ / arrowMotion) * bowPower);

                    double gravity = usingBow ? 0.05D : (livingBase.getHeldItemMainhand().getItem() instanceof ItemPotion || livingBase.getHeldItemOffhand().getItem() instanceof ItemPotion) ? 0.4D : (livingBase.getHeldItemMainhand().getItem() instanceof ItemFishingRod || livingBase.getHeldItemOffhand().getItem() instanceof ItemFishingRod) ? 0.15D : 0.03D;
                    Vec3d playerVector = new Vec3d(livingBase.posX, livingBase.posY + livingBase.getEyeHeight(), livingBase.posZ);

                    RenderUtils.OpenGl();
                    GL11.glEnable(GL13.GL_MULTISAMPLE);
                    GlStateManager.glLineWidth((float) LineWidth.getValDouble());
                    ColorUtils.glColor(Color.getcolor());
                    GlStateManager.glBegin(GL11.GL_LINE_STRIP);

                    RenderManager renderManager = mc.getRenderManager();
                    for (int i = 0; i < 1000; i++) {
                        GL11.glVertex3d(arrowPosX - renderManager.viewerPosX, arrowPosY - renderManager.viewerPosY, arrowPosZ - renderManager.viewerPosZ);

                        arrowPosX += arrowMotionX * 0.1;
                        arrowPosY += arrowMotionY * 0.1;
                        arrowPosZ += arrowMotionZ * 0.1;
                        arrowMotionX *= 0.999D;
                        arrowMotionY = (float) ((arrowMotionY * 0.999D) - gravity * 0.1);
                        arrowMotionZ *= 0.999D;

                        if (mc.world.rayTraceBlocks(playerVector, new Vec3d(arrowPosX, arrowPosY, arrowPosZ)) != null)
                            break;
                    }
                    GlStateManager.glEnd();

                    double renderX = arrowPosX - renderManager.viewerPosX;
                    double renderY = arrowPosY - renderManager.viewerPosY;
                    double renderZ = arrowPosZ - renderManager.viewerPosZ;

                    AxisAlignedBB bb = new AxisAlignedBB(renderX - 0.5, renderY, renderZ - 0.5, renderX + 0.5, renderY + 0.5, renderZ + 0.5);

                    RenderBlock(Mode.getValString(), bb, Color.getcolor(), LineWidth.getValDouble());

                    RenderUtils.ReleaseGl();
                    GL11.glDisable(GL13.GL_MULTISAMPLE);
                }
            }
        }


        if (FindEpearl.getValBoolean()) {
            RenderUtils.OpenGl();
            GlStateManager.glLineWidth((float) LineWidth.getValDouble() * 3);
            List<Bpos> toremove = new ArrayList<>();
            for (Bpos po : Pos) {
                if (po.getaLong() + (RenderTime.getValDouble() * 1000) < System.currentTimeMillis())
                    toremove.add(po);
            }
            Pos.removeAll(toremove);
            if (!Pos.isEmpty())
                for (Bpos po : Pos) {
                    GlStateManager.glBegin(1);
                    ColorUtils.glColor(Color.getcolor());
                    double[] rPos = rPos();
                    Vec3d priorpoint = po.getVec3ds().get(0);
                    for (Vec3d vec3d : po.getVec3ds()) {
                        // not using statemanager as it just links to gl11 and it only takes floats...
                        GL11.glVertex3d((vec3d).x - rPos[0], (vec3d).y - rPos[1], (vec3d).z - rPos[2]);
                        GL11.glVertex3d((priorpoint).x - rPos[0], (priorpoint).y - rPos[1], (priorpoint).z - rPos[2]);
                        priorpoint = vec3d;
                    }
                    GlStateManager.glEnd();
                }
            RenderUtils.ReleaseGl();
        }
    }

    private double[] rPos() {
        try {
            double renderPosX = mc.getRenderManager().viewerPosX;
            double renderPosY = mc.getRenderManager().viewerPosY;
            double renderPosZ = mc.getRenderManager().viewerPosZ;
            return new double[]{renderPosX, renderPosY, renderPosZ};
        } catch (Exception e) {
            return new double[]{0.0D, 0.0D, 0.0D};
        }
    }

    static class Bpos {
        private final List<Vec3d> vec3ds;
        private final UUID uuid;
        private final long aLong;

        public Bpos(List<Vec3d> vec3ds, UUID uuid, long l) {
            this.vec3ds = vec3ds;
            this.uuid = uuid;
            this.aLong = l;
        }

        public UUID getUuid() {
            return uuid;
        }
        public List<Vec3d> getVec3ds() {
            return vec3ds;
        }

        public long getaLong() {
            return aLong;
        }
    }

}
