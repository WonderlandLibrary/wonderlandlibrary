package cc.novoline.modules.visual;

import cc.novoline.events.EventTarget;
import cc.novoline.events.events.LivingUpdateEvent;
import cc.novoline.events.events.PlayerUpdateEvent;
import cc.novoline.events.events.Render3DEvent;
import cc.novoline.modules.AbstractModule;
import cc.novoline.modules.EnumModuleType;
import cc.novoline.modules.ModuleManager;
import cc.novoline.utils.RenderUtils;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockPos;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.lwjgl.opengl.GL11;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class DMGParticle extends AbstractModule {

    private final HashMap<EntityLivingBase, Float> healthMap = new HashMap<>();
    private final List<Particles> particles = new ArrayList<>();

    public DMGParticle(@NonNull ModuleManager moduleManager) {
        super(moduleManager, "DamageParticles", "Damage Particles", EnumModuleType.VISUALS, "");
    }

    @EventTarget
    public void onLivingUpdate(LivingUpdateEvent e) {
        EntityLivingBase entity = (EntityLivingBase) e.getEntity();
        if (entity == this.mc.player) return;
        if (!this.healthMap.containsKey(entity)) this.healthMap.put(entity, entity.getHealth());
        float floatValue = this.healthMap.get(entity);
        float health = entity.getHealth();
        if (floatValue != health) {
            boolean heal = health > floatValue;
            boolean crit = entity.hurtResistantTime < 18 || mc.player.motionY < 0 && !mc.player.onGround;
            String color = heal ? "\247a" : crit ? "\247c" : "\247e";
            String text = floatValue - health < 0.0f ? color + roundToPlace((floatValue - health) * -1.0f, 1) : color + roundToPlace(floatValue - health, 1);
            Location location = new Location(entity);
            location.setY(entity.getEntityBoundingBox().minY + (entity.getEntityBoundingBox().maxY - entity.getEntityBoundingBox().minY) / 2.0);
            location.setX(location.getX() - 0.5 + new Random(System.currentTimeMillis()).nextInt(5) * 0.1);
            location.setZ(location.getZ() - 0.5 + new Random(System.currentTimeMillis() + 1).nextInt(5) * 0.1);
            this.particles.add(new Particles(location, text));
            this.healthMap.remove(entity);
            this.healthMap.put(entity, entity.getHealth());
        }
    }

    @EventTarget
    public void onRender(Render3DEvent e) {
        for (Particles p : this.particles) {
            double x = p.location.getX();
            double n = x - mc.getRenderManager().renderPosX;
            double y = p.location.getY();
            double n2 = y - mc.getRenderManager().renderPosY;
            double z = p.location.getZ();
            double n3 = z - mc.getRenderManager().renderPosZ;
            GlStateManager.pushMatrix();
            GlStateManager.enablePolygonOffset();
            GlStateManager.doPolygonOffset(1.0f, -1500000.0f);
            GlStateManager.translate((float) n, (float) n2, (float) n3);
            GlStateManager.rotate(-this.mc.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
            float textY = this.mc.gameSettings.thirdPersonView == 2 ? -1.0F : 1.0F;
            GlStateManager.rotate(this.mc.getRenderManager().playerViewX, textY, 0.0f, 0.0f);
            final double size = 0.03;
            GlStateManager.scale(-size, -size, size);
            RenderUtils.enableGL2D();
            RenderUtils.disableGL2D();
            GL11.glDepthMask(false);
            mc.fontRendererObj.drawStringWithShadow(p.text, (float) -(this.mc.fontRendererObj.getStringWidth(p.text) / 2), (float) -(this.mc.fontRendererObj.getHeight() - 1), 0);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glDepthMask(true);
            GlStateManager.doPolygonOffset(1.0f, 1500000.0f);
            GlStateManager.disablePolygonOffset();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }

    public static double roundToPlace(double p_roundToPlace_0_, int p_roundToPlace_2_) {
        if (p_roundToPlace_2_ < 0) throw new IllegalArgumentException();
        return new BigDecimal(p_roundToPlace_0_).setScale(p_roundToPlace_2_, RoundingMode.HALF_UP).doubleValue();
    }

    @EventTarget
    public void onUpdate(PlayerUpdateEvent eventUpdate) {
        this.particles.forEach(update -> {
            ++update.ticks;
            if (update.ticks <= 10) update.location.setY(update.location.getY() + update.ticks * 0.005);
            if (update.ticks > 20) this.particles.remove(update);
        });
    }

    public static class Particles {
        public int ticks;
        public Location location;
        public String text;

        public Particles(final Location location, final String text) {
            this.location = location;
            this.text = text;
            this.ticks = 0;
        }

    }

    public static class Location {
        private double x;
        private double y;
        private double z;
        private float yaw;
        private float pitch;

        public Location(double x, double y, double z, float yaw, float pitch) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.yaw = yaw;
            this.pitch = pitch;
        }

        public Location(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.yaw = 0.0F;
            this.pitch = 0.0F;
        }

        public Location(BlockPos pos) {
            this.x = pos.getX();
            this.y = pos.getY();
            this.z = pos.getZ();
            this.yaw = 0.0F;
            this.pitch = 0.0F;
        }

        public Location(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.yaw = 0.0F;
            this.pitch = 0.0F;
        }

        public Location(EntityLivingBase entity) {
            this.x = entity.posX;
            this.y = entity.posY;
            this.z = entity.posZ;
            this.yaw = 0.0f;
            this.pitch = 0.0f;
        }

        public Location add(int x, int y, int z) {
            this.x += x;
            this.y += y;
            this.z += z;
            return this;
        }

        public Location add(double x, double y, double z) {
            this.x += x;
            this.y += y;
            this.z += z;
            return this;
        }

        public Location subtract(int x, int y, int z) {
            this.x -= x;
            this.y -= y;
            this.z -= z;
            return this;
        }

        public Location subtract(double x, double y, double z) {
            this.x -= x;
            this.y -= y;
            this.z -= z;
            return this;
        }

        public Block getBlock() {
            return Minecraft.getInstance().world.getBlockState(this.toBlockPos()).getBlock();
        }

        public double getX() {
            return this.x;
        }

        public Location setX(double x) {
            this.x = x;
            return this;
        }

        public double getY() {
            return this.y;
        }

        public Location setY(double y) {
            this.y = y;
            return this;
        }

        public double getZ() {
            return this.z;
        }

        public Location setZ(double z) {
            this.z = z;
            return this;
        }

        public float getYaw() {
            return this.yaw;
        }

        public Location setYaw(float yaw) {
            this.yaw = yaw;
            return this;
        }

        public float getPitch() {
            return this.pitch;
        }

        public Location setPitch(float pitch) {
            this.pitch = pitch;
            return this;
        }

        public static Location fromBlockPos(BlockPos blockPos) {
            return new Location(blockPos.getX(), blockPos.getY(), blockPos.getZ());
        }

        public BlockPos toBlockPos() {
            return new BlockPos(this.getX(), this.getY(), this.getZ());
        }

        public double distanceTo(Location loc) {
            double dx = loc.x - this.x;
            double dz = loc.z - this.z;
            double dy = loc.y - this.y;
            return Math.sqrt(dx * dx + dy * dy + dz * dz);
        }

        public double distanceToY(Location loc) {
            double dy = loc.y - this.y;
            return Math.sqrt(dy * dy);
        }
    }

}