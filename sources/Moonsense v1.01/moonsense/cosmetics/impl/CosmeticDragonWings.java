// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.cosmetics.impl;

import net.minecraft.client.renderer.GlStateManager;
import moonsense.cosmetics.CosmeticsManager;
import org.lwjgl.opengl.GL11;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.model.ModelBase;

public class CosmeticDragonWings extends ModelBase implements LayerRenderer
{
    private final RenderPlayer playerRenderer;
    private Minecraft mc;
    private ResourceLocation location;
    private ModelRenderer wing;
    private ModelRenderer wingTip;
    private boolean playerUsesFullHeight;
    
    public CosmeticDragonWings(final RenderPlayer playerRendererIn) {
        this.playerRenderer = playerRendererIn;
        this.mc = Minecraft.getMinecraft();
        this.location = new ResourceLocation("streamlined/cosmetics/wings.png");
        this.setTextureOffset("wing.bone", 0, 0);
        this.setTextureOffset("wing.skin", -10, 8);
        this.setTextureOffset("wingtip.bone", 0, 5);
        this.setTextureOffset("wingtip.skin", -10, 18);
        (this.wing = new ModelRenderer(this, "wing")).setTextureSize(30, 30);
        this.wing.setRotationPoint(-2.0f, 0.0f, 0.0f);
        this.wing.addBox("bone", -10.0f, -1.0f, -1.0f, 10, 2, 2);
        this.wing.addBox("skin", -10.0f, 0.0f, 0.5f, 10, 0, 10);
        (this.wingTip = new ModelRenderer(this, "wingtip")).setTextureSize(30, 30);
        this.wingTip.setRotationPoint(-10.0f, 0.0f, 0.0f);
        this.wingTip.addBox("bone", -10.0f, -0.5f, -0.5f, 10, 1, 1);
        this.wingTip.addBox("skin", -10.0f, 0.0f, 0.5f, 10, 0, 10);
        this.wing.addChild(this.wingTip);
    }
    
    private float interpolate(final float yaw1, final float yaw2, final float percent) {
        float f = (yaw1 + (yaw2 - yaw1) * percent) % 360.0f;
        if (f < 0.0f) {
            f += 360.0f;
        }
        return f;
    }
    
    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
    
    @Override
    public void doRenderLayer(final EntityLivingBase player, final float var2, final float var3, final float partialTicks, final float var5, final float var6, final float var7, final float var8) {
        if (player.isInvisible()) {
            return;
        }
        if (player.getName().equalsIgnoreCase(Minecraft.getMinecraft().session.getUsername()) || player.isInvisible()) {
            final double rotate = this.interpolate(player.prevRenderYawOffset, player.renderYawOffset, partialTicks);
            GL11.glPushMatrix();
            final float[] color = CosmeticsManager.getCosmeticColor();
            GL11.glColor3f(color[0], color[1], color[2]);
            GL11.glScaled(-1.0, -1.0, 1.0);
            GL11.glTranslated(0.0, -1.45, 0.0);
            GL11.glTranslated(0.0, 1.3, 0.2);
            if (player.isSneaking()) {
                GlStateManager.translate(0.0f, -0.142f, -0.0178f);
            }
            GL11.glRotated(180.0, 1.0, 0.0, 0.0);
            GL11.glRotated(180.0, 0.0, 1.0, 0.0);
            this.mc.getTextureManager().bindTexture(this.location);
            for (int j = 0; j < 2; ++j) {
                GL11.glEnable(2884);
                float f11;
                if (player.isSneaking()) {
                    f11 = (float)(System.currentTimeMillis() * 0.2 % 1000.0) / 1000.0f * 3.1415927f * 2.0f;
                }
                else if (Minecraft.getMinecraft().thePlayer.capabilities.isFlying && Minecraft.getMinecraft().thePlayer.isAirBorne) {
                    f11 = System.currentTimeMillis() % 1000L / 1000.0f * 3.1415927f * 2.0f;
                }
                else {
                    f11 = (float)(System.currentTimeMillis() * 0.5 % 1000.0) / 1000.0f * 3.1415927f * 2.0f;
                }
                this.wing.rotateAngleX = (float)Math.toRadians(-80.0) - (float)Math.cos(f11) * 0.2f;
                this.wing.rotateAngleY = (float)Math.toRadians(20.0) + (float)Math.sin(f11) * 0.4f;
                this.wing.rotateAngleZ = (float)Math.toRadians(20.0);
                this.wingTip.rotateAngleZ = -(float)(Math.sin(f11 + 2.0f) + 0.5) * 0.75f;
                this.wing.render(0.0625f);
                GL11.glScalef(-1.0f, 1.0f, 1.0f);
                if (j == 0) {
                    GL11.glCullFace(1028);
                }
            }
            GL11.glCullFace(1029);
            GL11.glDisable(2884);
            GL11.glColor3f(255.0f, 255.0f, 255.0f);
            GL11.glPopMatrix();
        }
    }
}
