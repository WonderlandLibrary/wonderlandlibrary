package me.earth.phobos.features.modules.client;

import me.earth.phobos.features.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;

public class PhysicsCapes
        extends Module {
    private final ResourceLocation capeTexture = new ResourceLocation("textures/cape.png");
    public ModelPhyscisCapes cape = new ModelPhyscisCapes();

    public PhysicsCapes() {
        super("PhysicsCapes", "Capes with superior physics", Module.Category.CLIENT, true, false, false);
    }

    @SubscribeEvent
    public void onPlayerRender(RenderPlayerEvent.Post event) {
        GlStateManager.pushMatrix();
        float f11 = (float) System.currentTimeMillis() / 1000.0f;
        HashMap<ModelRenderer, Float> waveMap = new HashMap<ModelRenderer, Float>();
        float fuck = f11;
        for (ModelRenderer renderer : this.cape.boxList) {
            waveMap.put(renderer, Float.valueOf((float) Math.sin((double) fuck / 0.5) * 4.0f));
            fuck += 1.0f;
        }
        double rotate = this.interpolate(event.getEntityPlayer().prevRenderYawOffset, event.getEntityPlayer().renderYawOffset, event.getPartialRenderTick());
        GlStateManager.translate(0.0f, 0.0f, 0.125f);
        double d0 = event.getEntityPlayer().prevChasingPosX + (event.getEntityPlayer().chasingPosX - event.getEntityPlayer().prevChasingPosX) * (double) event.getPartialRenderTick() - (event.getEntityPlayer().prevPosX + (event.getEntityPlayer().posX - event.getEntityPlayer().prevPosX) * (double) event.getPartialRenderTick());
        double d1 = event.getEntityPlayer().prevChasingPosY + (event.getEntityPlayer().chasingPosY - event.getEntityPlayer().prevChasingPosY) * (double) event.getPartialRenderTick() - (event.getEntityPlayer().prevPosY + (event.getEntityPlayer().posY - event.getEntityPlayer().prevPosY) * (double) event.getPartialRenderTick());
        double d2 = event.getEntityPlayer().prevChasingPosZ + (event.getEntityPlayer().chasingPosZ - event.getEntityPlayer().prevChasingPosZ) * (double) event.getPartialRenderTick() - (event.getEntityPlayer().prevPosZ + (event.getEntityPlayer().posZ - event.getEntityPlayer().prevPosZ) * (double) event.getPartialRenderTick());
        float f = event.getEntityPlayer().prevRenderYawOffset + (event.getEntityPlayer().renderYawOffset - event.getEntityPlayer().prevRenderYawOffset) * event.getPartialRenderTick();
        double d3 = MathHelper.sin(f * ((float) Math.PI / 180));
        double d4 = -MathHelper.cos(f * ((float) Math.PI / 180));
        float f1 = (float) d1 * 10.0f;
        f1 = MathHelper.clamp(f1, -6.0f, 32.0f);
        float f2 = (float) (d0 * d3 + d2 * d4) * 100.0f;
        float f3 = (float) (d0 * d4 - d2 * d3) * 100.0f;
        if (f2 < 0.0f) {
            f2 = 0.0f;
        }
        float f4 = event.getEntityPlayer().prevCameraYaw + (event.getEntityPlayer().cameraYaw - event.getEntityPlayer().prevCameraYaw) * event.getPartialRenderTick();
        f1 += MathHelper.sin((event.getEntityPlayer().prevDistanceWalkedModified + (event.getEntityPlayer().distanceWalkedModified - event.getEntityPlayer().prevDistanceWalkedModified) * event.getPartialRenderTick()) * 6.0f) * 32.0f * f4;
        if (event.getEntityPlayer().isSneaking()) {
            f1 += 25.0f;
        }
        GL11.glRotated(-rotate, 0.0, 1.0, 0.0);
        GlStateManager.rotate(180.0f, 1.0f, 0.0f, 0.0f);
        GL11.glTranslated(0.0, -((double) event.getEntityPlayer().height - (event.getEntityPlayer().isSneaking() ? 0.25 : 0.0) - 0.38), 0.0);
        GlStateManager.rotate(6.0f + f2 / 2.0f + f1, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(f3 / 2.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(-f3 / 2.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
        if (PhysicsCapes.mc.player.moveForward != 0.0f || PhysicsCapes.mc.player.moveStrafing != 0.0f) {
            for (ModelRenderer renderer : this.cape.boxList) {
                renderer.rotateAngleX = waveMap.get(renderer).floatValue();
            }
        } else {
            for (ModelRenderer renderer : this.cape.boxList) {
                renderer.rotateAngleX = 0.0f;
            }
        }
        Minecraft.getMinecraft().getTextureManager().bindTexture(this.capeTexture);
        this.cape.render(event.getEntity(), 0.0f, 0.0f, -0.1f, 0.0f, 0.0f, 0.0625f);
        Minecraft.getMinecraft().getTextureManager().deleteTexture(this.capeTexture);
        GlStateManager.popMatrix();
    }

    public float interpolate(float yaw1, float yaw2, float percent) {
        float rotation = (yaw1 + (yaw2 - yaw1) * percent) % 360.0f;
        if (rotation < 0.0f) {
            rotation += 360.0f;
        }
        return rotation;
    }

    public class ModelPhyscisCapes
            extends ModelBase {
        public ModelRenderer shape1;
        public ModelRenderer shape2;
        public ModelRenderer shape3;
        public ModelRenderer shape4;
        public ModelRenderer shape5;
        public ModelRenderer shape6;
        public ModelRenderer shape7;
        public ModelRenderer shape8;
        public ModelRenderer shape9;
        public ModelRenderer shape10;
        public ModelRenderer shape11;
        public ModelRenderer shape12;
        public ModelRenderer shape13;
        public ModelRenderer shape14;
        public ModelRenderer shape15;
        public ModelRenderer shape16;

        public ModelPhyscisCapes() {
            this.textureWidth = 64;
            this.textureHeight = 32;
            this.shape9 = new ModelRenderer(this, 0, 8);
            this.shape9.setRotationPoint(-5.0f, 8.0f, -1.0f);
            this.shape9.addBox(0.0f, 0.0f, 0.0f, 10, 1, 1, 0.0f);
            this.shape15 = new ModelRenderer(this, 0, 14);
            this.shape15.setRotationPoint(-5.0f, 14.0f, -1.0f);
            this.shape15.addBox(0.0f, 0.0f, 0.0f, 10, 1, 1, 0.0f);
            this.shape3 = new ModelRenderer(this, 0, 2);
            this.shape3.setRotationPoint(-5.0f, 2.0f, -1.0f);
            this.shape3.addBox(0.0f, 0.0f, 0.0f, 10, 1, 1, 0.0f);
            this.shape7 = new ModelRenderer(this, 0, 6);
            this.shape7.setRotationPoint(-5.0f, 6.0f, -1.0f);
            this.shape7.addBox(0.0f, 0.0f, 0.0f, 10, 1, 1, 0.0f);
            this.shape1 = new ModelRenderer(this, 0, 0);
            this.shape1.setRotationPoint(-5.0f, 0.0f, -1.0f);
            this.shape1.addBox(0.0f, 0.0f, 0.0f, 10, 1, 1, 0.0f);
            this.shape6 = new ModelRenderer(this, 0, 5);
            this.shape6.setRotationPoint(-5.0f, 5.0f, -1.0f);
            this.shape6.addBox(0.0f, 0.0f, 0.0f, 10, 1, 1, 0.0f);
            this.shape14 = new ModelRenderer(this, 0, 13);
            this.shape14.setRotationPoint(-5.0f, 13.0f, -1.0f);
            this.shape14.addBox(0.0f, 0.0f, 0.0f, 10, 1, 1, 0.0f);
            this.shape10 = new ModelRenderer(this, 0, 9);
            this.shape10.setRotationPoint(-5.0f, 9.0f, -1.0f);
            this.shape10.addBox(0.0f, 0.0f, 0.0f, 10, 1, 1, 0.0f);
            this.shape13 = new ModelRenderer(this, 0, 12);
            this.shape13.setRotationPoint(-5.0f, 12.0f, -1.0f);
            this.shape13.addBox(0.0f, 0.0f, 0.0f, 10, 1, 1, 0.0f);
            this.shape4 = new ModelRenderer(this, 0, 3);
            this.shape4.setRotationPoint(-5.0f, 3.0f, -1.0f);
            this.shape4.addBox(0.0f, 0.0f, 0.0f, 10, 1, 1, 0.0f);
            this.shape8 = new ModelRenderer(this, 0, 7);
            this.shape8.setRotationPoint(-5.0f, 7.0f, -1.0f);
            this.shape8.addBox(0.0f, 0.0f, 0.0f, 10, 1, 1, 0.0f);
            this.shape16 = new ModelRenderer(this, 0, 15);
            this.shape16.setRotationPoint(-5.0f, 15.0f, -1.0f);
            this.shape16.addBox(0.0f, 0.0f, 0.0f, 10, 1, 1, 0.0f);
            this.shape12 = new ModelRenderer(this, 0, 11);
            this.shape12.setRotationPoint(-5.0f, 11.0f, -1.0f);
            this.shape12.addBox(0.0f, 0.0f, 0.0f, 10, 1, 1, 0.0f);
            this.shape5 = new ModelRenderer(this, 0, 4);
            this.shape5.setRotationPoint(-5.0f, 4.0f, -1.0f);
            this.shape5.addBox(0.0f, 0.0f, 0.0f, 10, 1, 1, 0.0f);
            this.shape11 = new ModelRenderer(this, 0, 10);
            this.shape11.setRotationPoint(-5.0f, 10.0f, -1.0f);
            this.shape11.addBox(0.0f, 0.0f, 0.0f, 10, 1, 1, 0.0f);
            this.shape2 = new ModelRenderer(this, 0, 1);
            this.shape2.setRotationPoint(-5.0f, 1.0f, -1.0f);
            this.shape2.addBox(0.0f, 0.0f, 0.0f, 10, 1, 1, 0.0f);
            this.boxList.add(this.shape1);
            this.boxList.add(this.shape2);
            this.boxList.add(this.shape3);
            this.boxList.add(this.shape4);
            this.boxList.add(this.shape5);
            this.boxList.add(this.shape6);
            this.boxList.add(this.shape7);
            this.boxList.add(this.shape8);
            this.boxList.add(this.shape9);
            this.boxList.add(this.shape10);
            this.boxList.add(this.shape11);
            this.boxList.add(this.shape12);
            this.boxList.add(this.shape13);
            this.boxList.add(this.shape14);
            this.boxList.add(this.shape15);
            this.boxList.add(this.shape16);
        }

        public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
            this.shape9.render(f5);
            this.shape15.render(f5);
            this.shape3.render(f5);
            this.shape7.render(f5);
            this.shape1.render(f5);
            this.shape6.render(f5);
            this.shape14.render(f5);
            this.shape10.render(f5);
            this.shape13.render(f5);
            this.shape4.render(f5);
            this.shape8.render(f5);
            this.shape16.render(f5);
            this.shape12.render(f5);
            this.shape5.render(f5);
            this.shape11.render(f5);
            this.shape2.render(f5);
        }

        public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
            modelRenderer.rotateAngleX = x;
            modelRenderer.rotateAngleY = y;
            modelRenderer.rotateAngleZ = z;
        }
    }
}

