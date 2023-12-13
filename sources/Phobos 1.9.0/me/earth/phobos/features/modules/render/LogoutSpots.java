package me.earth.phobos.features.modules.render;

import me.earth.phobos.event.events.ConnectionEvent;
import me.earth.phobos.event.events.Render3DEvent;
import me.earth.phobos.features.command.Command;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.modules.client.Colors;
import me.earth.phobos.features.setting.Setting;
import me.earth.phobos.util.ColorUtil;
import me.earth.phobos.util.MathUtil;
import me.earth.phobos.util.RenderUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class LogoutSpots
        extends Module {
    private final Setting<Boolean> colorSync = this.register(new Setting<Boolean>("Sync", false));
    private final Setting<Integer> red = this.register(new Setting<Integer>("Red", 255, 0, 255));
    private final Setting<Integer> green = this.register(new Setting<Integer>("Green", 0, 0, 255));
    private final Setting<Integer> blue = this.register(new Setting<Integer>("Blue", 0, 0, 255));
    private final Setting<Integer> alpha = this.register(new Setting<Integer>("Alpha", 255, 0, 255));
    private final Setting<Boolean> scaleing = this.register(new Setting<Boolean>("Scale", false));
    private final Setting<Float> scaling = this.register(new Setting<Float>("Size", Float.valueOf(4.0f), Float.valueOf(0.1f), Float.valueOf(20.0f)));
    private final Setting<Float> factor = this.register(new Setting<Object>("Factor", Float.valueOf(0.3f), Float.valueOf(0.1f), Float.valueOf(1.0f), v -> this.scaleing.getValue()));
    private final Setting<Boolean> smartScale = this.register(new Setting<Object>("SmartScale", Boolean.valueOf(false), v -> this.scaleing.getValue()));
    private final Setting<Boolean> rect = this.register(new Setting<Boolean>("Rectangle", true));
    private final Setting<Boolean> coords = this.register(new Setting<Boolean>("Coords", true));
    private final Setting<Boolean> notification = this.register(new Setting<Boolean>("Notification", true));
    private final List<LogoutPos> spots = new CopyOnWriteArrayList<LogoutPos>();
    public Setting<Float> range = this.register(new Setting<Float>("Range", Float.valueOf(300.0f), Float.valueOf(50.0f), Float.valueOf(500.0f)));
    public Setting<Boolean> message = this.register(new Setting<Boolean>("Message", false));

    public LogoutSpots() {
        super("LogoutSpots", "Renders LogoutSpots", Module.Category.RENDER, true, false, false);
    }

    @Override
    public void onLogout() {
        this.spots.clear();
    }

    @Override
    public void onDisable() {
        this.spots.clear();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void onRender3D(Render3DEvent event) {
        if (!this.spots.isEmpty()) {
            List<LogoutPos> list = this.spots;
            synchronized (list) {
                this.spots.forEach(spot -> {
                    if (spot.getEntity() != null) {
                        AxisAlignedBB bb = RenderUtil.interpolateAxis(spot.getEntity().getEntityBoundingBox());
                        RenderUtil.drawBlockOutline(bb, this.colorSync.getValue() != false ? Colors.INSTANCE.getCurrentColor() : new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()), 1.0f);
                        double x = this.interpolate(spot.getEntity().lastTickPosX, spot.getEntity().posX, event.getPartialTicks()) - LogoutSpots.mc.getRenderManager().renderPosX;
                        double y = this.interpolate(spot.getEntity().lastTickPosY, spot.getEntity().posY, event.getPartialTicks()) - LogoutSpots.mc.getRenderManager().renderPosY;
                        double z = this.interpolate(spot.getEntity().lastTickPosZ, spot.getEntity().posZ, event.getPartialTicks()) - LogoutSpots.mc.getRenderManager().renderPosZ;
                        this.renderNameTag(spot.getName(), x, y, z, event.getPartialTicks(), spot.getX(), spot.getY(), spot.getZ());
                    }
                });
            }
        }
    }

    @Override
    public void onUpdate() {
        if (!LogoutSpots.fullNullCheck()) {
            this.spots.removeIf(spot -> LogoutSpots.mc.player.getDistanceSq(spot.getEntity()) >= MathUtil.square(this.range.getValue().floatValue()));
        }
    }

    @SubscribeEvent
    public void onConnection(ConnectionEvent event) {
        if (event.getStage() == 0) {
            UUID uuid = event.getUuid();
            EntityPlayer entity = LogoutSpots.mc.world.getPlayerEntityByUUID(uuid);
            if (entity != null && this.message.getValue().booleanValue()) {
                Command.sendMessage("\u00a7a" + entity.getName() + " just logged in" + (this.coords.getValue() != false ? " at (" + (int) entity.posX + ", " + (int) entity.posY + ", " + (int) entity.posZ + ")!" : "!"), this.notification.getValue());
            }
            this.spots.removeIf(pos -> pos.getName().equalsIgnoreCase(event.getName()));
        } else if (event.getStage() == 1) {
            EntityPlayer entity = event.getEntity();
            UUID uuid = event.getUuid();
            String name = event.getName();
            if (this.message.getValue().booleanValue()) {
                Command.sendMessage("\u00a7c" + event.getName() + " just logged out" + (this.coords.getValue() != false ? " at (" + (int) entity.posX + ", " + (int) entity.posY + ", " + (int) entity.posZ + ")!" : "!"), this.notification.getValue());
            }
            if (name != null && entity != null && uuid != null) {
                this.spots.add(new LogoutPos(name, uuid, entity));
            }
        }
    }

    private void renderNameTag(String name, double x, double yi, double z, float delta, double xPos, double yPos, double zPos) {
        double y = yi + 0.7;
        Entity camera = mc.getRenderViewEntity();
        assert (camera != null);
        double originalPositionX = camera.posX;
        double originalPositionY = camera.posY;
        double originalPositionZ = camera.posZ;
        camera.posX = this.interpolate(camera.prevPosX, camera.posX, delta);
        camera.posY = this.interpolate(camera.prevPosY, camera.posY, delta);
        camera.posZ = this.interpolate(camera.prevPosZ, camera.posZ, delta);
        String displayTag = name + " XYZ: " + (int) xPos + ", " + (int) yPos + ", " + (int) zPos;
        double distance = camera.getDistance(x + LogoutSpots.mc.getRenderManager().viewerPosX, y + LogoutSpots.mc.getRenderManager().viewerPosY, z + LogoutSpots.mc.getRenderManager().viewerPosZ);
        int width = this.renderer.getStringWidth(displayTag) / 2;
        double scale = (0.0018 + (double) this.scaling.getValue().floatValue() * (distance * (double) this.factor.getValue().floatValue())) / 1000.0;
        if (distance <= 8.0 && this.smartScale.getValue().booleanValue()) {
            scale = 0.0245;
        }
        if (!this.scaleing.getValue().booleanValue()) {
            scale = (double) this.scaling.getValue().floatValue() / 100.0;
        }
        GlStateManager.pushMatrix();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enablePolygonOffset();
        GlStateManager.doPolygonOffset(1.0f, -1500000.0f);
        GlStateManager.disableLighting();
        GlStateManager.translate((float) x, (float) y + 1.4f, (float) z);
        GlStateManager.rotate(-LogoutSpots.mc.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(LogoutSpots.mc.getRenderManager().playerViewX, LogoutSpots.mc.gameSettings.thirdPersonView == 2 ? -1.0f : 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(-scale, -scale, scale);
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.enableBlend();
        if (this.rect.getValue().booleanValue()) {
            RenderUtil.drawRect(-width - 2, -(this.renderer.getFontHeight() + 1), (float) width + 2.0f, 1.5f, 0x55000000);
        }
        GlStateManager.disableBlend();
        this.renderer.drawStringWithShadow(displayTag, -width, -(this.renderer.getFontHeight() - 1), this.colorSync.getValue() != false ? Colors.INSTANCE.getCurrentColorHex() : ColorUtil.toRGBA(new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue())));
        camera.posX = originalPositionX;
        camera.posY = originalPositionY;
        camera.posZ = originalPositionZ;
        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
        GlStateManager.disablePolygonOffset();
        GlStateManager.doPolygonOffset(1.0f, 1500000.0f);
        GlStateManager.popMatrix();
    }

    private double interpolate(double previous, double current, float delta) {
        return previous + (current - previous) * (double) delta;
    }

    private static class LogoutPos {
        private final String name;
        private final UUID uuid;
        private final EntityPlayer entity;
        private final double x;
        private final double y;
        private final double z;

        public LogoutPos(String name, UUID uuid, EntityPlayer entity) {
            this.name = name;
            this.uuid = uuid;
            this.entity = entity;
            this.x = entity.posX;
            this.y = entity.posY;
            this.z = entity.posZ;
        }

        public String getName() {
            return this.name;
        }

        public UUID getUuid() {
            return this.uuid;
        }

        public EntityPlayer getEntity() {
            return this.entity;
        }

        public double getX() {
            return this.x;
        }

        public double getY() {
            return this.y;
        }

        public double getZ() {
            return this.z;
        }
    }
}

