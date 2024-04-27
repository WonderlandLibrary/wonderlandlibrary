package net.ccbluex.liquidbounce.ui.client.hud.element.elements;

import net.ccbluex.liquidbounce.ui.client.hud.element.Border;
import net.ccbluex.liquidbounce.ui.client.hud.element.Element;
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.Vec3;
import org.jetbrains.annotations.Nullable;

@ElementInfo(name = "RearView")
public class RearView extends Element {


    public RearView() {
        this.pos = new Vec3(0, 0, 0);
        this.yaw = 0;
        this.pitch = 0;
        this.frameBuffer = new Framebuffer(WIDTH_RESOLUTION, HEIGHT_RESOLUTION, true);
        this.frameBuffer.createFramebuffer(WIDTH_RESOLUTION, HEIGHT_RESOLUTION);
    }

    private Vec3 pos;

    private float yaw;

    private float pitch;

    private boolean recording;

    private boolean valid;

    private boolean rendering;

    private boolean firstUpdate;

    private Framebuffer frameBuffer;

    private final int WIDTH_RESOLUTION = 800;
    private final int HEIGHT_RESOLUTION = 600;

    private final Minecraft mc = Minecraft.getMinecraft();

    @Override
    public void Render2DElement() {
        update();
    }

    @Nullable
    @Override
    public Border drawElement() {
        float xOffset = 2;
        float yOffset = 100;
        ScaledResolution sr = new ScaledResolution(mc);
        this.setRendering(true);
        RenderUtils.drawRect(sr.getScaledWidth() - xOffset - 201, sr.getScaledHeight() - yOffset - 121, sr.getScaledWidth() - xOffset + 1, sr.getScaledHeight() - yOffset + 1, -1);//background
        if (this.isValid()) {
            this.setPos(Minecraft.getMinecraft().thePlayer.getPositionEyes(mc.timer.renderPartialTicks).subtract(0, 1, 0));
            this.setYaw(Minecraft.getMinecraft().thePlayer.rotationYaw - 180.0f);
            this.setPitch(0.0f);
            this.render(
                    sr.getScaledWidth() - xOffset - 200, //x1

                    sr.getScaledHeight() - yOffset - 120,//y1

                    sr.getScaledWidth() - xOffset, //x2

                    sr.getScaledHeight() - yOffset //y2
            );
        }
        return new Border(sr.getScaledWidth() - xOffset - 201, sr.getScaledHeight() - yOffset - 121, sr.getScaledWidth() - xOffset + 1, sr.getScaledHeight() - yOffset + 1);
    }

    public void render(float x, float y, float w, float h) {
        if (OpenGlHelper.isFramebufferEnabled()) {
            GlStateManager.pushMatrix();
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableAlpha();
            GlStateManager.disableBlend();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            frameBuffer.bindFramebufferTexture();
            final Tessellator tessellator = Tessellator.getInstance();
            final WorldRenderer worldRenderer = tessellator.getWorldRenderer();
            worldRenderer.begin(6, DefaultVertexFormats.POSITION_TEX);
            worldRenderer.pos(x, h, 0).tex(0, 0).endVertex();
            worldRenderer.pos(w, h, 0).tex(1, 0).endVertex();
            worldRenderer.pos(w, y, 0).tex(1, 1).endVertex();
            worldRenderer.pos(x, y, 0).tex(0, 1).endVertex();
            tessellator.draw();
            frameBuffer.unbindFramebufferTexture();
            GlStateManager.popMatrix();
        }
    }

    public void update() {
        if (!isRecording() && isRendering()) {
            updateFbo();
        }
    }

    public void updateFbo() {
        if (!this.firstUpdate) {
            mc.renderGlobal.loadRenderers();
            this.firstUpdate = true;
        }
        if (mc.thePlayer != null) {
            double posX = mc.thePlayer.posX;
            double posY = mc.thePlayer.posY;
            double posZ = mc.thePlayer.posZ;
            double prevPosX = mc.thePlayer.prevPosX;
            double prevPosY = mc.thePlayer.prevPosY;
            double prevPosZ = mc.thePlayer.prevPosZ;
            double lastTickPosX = mc.thePlayer.lastTickPosX;
            double lastTickPosY = mc.thePlayer.lastTickPosY;
            double lastTickPosZ = mc.thePlayer.lastTickPosZ;

            float rotationYaw = mc.thePlayer.rotationYaw;
            float prevRotationYaw = mc.thePlayer.prevRotationYaw;
            float rotationPitch = mc.thePlayer.rotationPitch;
            float prevRotationPitch = mc.thePlayer.prevRotationPitch;
            boolean sprinting = mc.thePlayer.isSprinting();

            boolean hideGUI = mc.gameSettings.hideGUI;
            int clouds = mc.gameSettings.clouds;
            int thirdPersonView = mc.gameSettings.thirdPersonView;
            float gamma = mc.gameSettings.gammaSetting;
            int ambientOcclusion = mc.gameSettings.ambientOcclusion;
            boolean viewBobbing = mc.gameSettings.viewBobbing;
            int particles = mc.gameSettings.particleSetting;
            int displayWidth = mc.displayWidth;
            int displayHeight = mc.displayHeight;

            int frameLimit = mc.gameSettings.limitFramerate;
            float fovSetting = mc.gameSettings.fovSetting;

            mc.thePlayer.posX = this.getPos().xCoord;
            mc.thePlayer.posY = this.getPos().yCoord;
            mc.thePlayer.posZ = this.getPos().zCoord;

            mc.thePlayer.prevPosX = this.getPos().xCoord;
            mc.thePlayer.prevPosY = this.getPos().yCoord;
            mc.thePlayer.prevPosZ = this.getPos().zCoord;

            mc.thePlayer.lastTickPosX = this.getPos().xCoord;
            mc.thePlayer.lastTickPosY = this.getPos().yCoord;
            mc.thePlayer.lastTickPosZ = this.getPos().zCoord;

            mc.thePlayer.rotationYaw = this.yaw;
            mc.thePlayer.prevRotationYaw = this.yaw;
            mc.thePlayer.rotationPitch = this.pitch;
            mc.thePlayer.prevRotationPitch = this.pitch;
            mc.thePlayer.setSprinting(false);

            mc.gameSettings.hideGUI = true;
            mc.gameSettings.clouds = 0;
            mc.gameSettings.thirdPersonView = 0;
//            mc.gameSettings.gammaSetting = 100;// GAMMA
            mc.gameSettings.ambientOcclusion = 0;
            mc.gameSettings.viewBobbing = false;
            mc.gameSettings.particleSetting = 0;
            mc.displayWidth = WIDTH_RESOLUTION;
            mc.displayHeight = HEIGHT_RESOLUTION;

            mc.gameSettings.limitFramerate = 10;
            mc.gameSettings.fovSetting = 110;//FOV

            this.setRecording(true);
            frameBuffer.bindFramebuffer(true);

            mc.entityRenderer.renderWorld(mc.timer.renderPartialTicks, System.nanoTime());
            mc.entityRenderer.setupOverlayRendering();

            frameBuffer.unbindFramebuffer();
            this.setRecording(false);

            mc.thePlayer.posX = posX;
            mc.thePlayer.posY = posY;
            mc.thePlayer.posZ = posZ;

            mc.thePlayer.prevPosX = prevPosX;
            mc.thePlayer.prevPosY = prevPosY;
            mc.thePlayer.prevPosZ = prevPosZ;

            mc.thePlayer.lastTickPosX = lastTickPosX;
            mc.thePlayer.lastTickPosY = lastTickPosY;
            mc.thePlayer.lastTickPosZ = lastTickPosZ;

            mc.thePlayer.rotationYaw = rotationYaw;
            mc.thePlayer.prevRotationYaw = prevRotationYaw;
            mc.thePlayer.rotationPitch = rotationPitch;
            mc.thePlayer.prevRotationPitch = prevRotationPitch;
            mc.thePlayer.setSprinting(sprinting);

            mc.gameSettings.hideGUI = hideGUI;
            mc.gameSettings.clouds = clouds;
            mc.gameSettings.thirdPersonView = thirdPersonView;
            mc.gameSettings.gammaSetting = gamma;
            mc.gameSettings.ambientOcclusion = ambientOcclusion;
            mc.gameSettings.viewBobbing = viewBobbing;
            mc.gameSettings.particleSetting = particles;
            mc.displayWidth = displayWidth;
            mc.displayHeight = displayHeight;
            mc.gameSettings.limitFramerate = frameLimit;
            mc.gameSettings.fovSetting = fovSetting;

            this.setValid(true);
            this.setRendering(false);
        }
    }

    public Vec3 getPos() {
        return pos;
    }

    public void setPos(Vec3 pos) {
        this.pos = pos;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public boolean isRecording() {
        return recording;
    }

    public void setRecording(boolean recording) {
        this.recording = recording;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public boolean isRendering() {
        return rendering;
    }

    public void setRendering(boolean rendering) {
        this.rendering = rendering;
    }

}
