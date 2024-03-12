// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.render.wings;

import net.minecraft.client.Minecraft;
import dev.tenacity.utils.render.RenderUtil;
import dev.tenacity.module.impl.render.CustomModel;
import org.lwjgl.opengl.GL11;
import net.minecraft.util.MathHelper;
import java.awt.Color;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.model.ModelBase;

public class WingModel extends ModelBase
{
    private final ResourceLocation location;
    private final ModelRenderer wingTip;
    private final ModelRenderer wing;
    
    public WingModel() {
        this.location = new ResourceLocation("Tenacity/Models/wings.png");
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
    
    public void renderWings(final EntityPlayer player, final float partialTicks, final double scale, final Color color) {
        final double angle = this.interpolate(MathHelper.wrapAngleTo180_float(player.prevRenderYawOffset), MathHelper.wrapAngleTo180_float(player.renderYawOffset), partialTicks);
        GL11.glPushMatrix();
        GL11.glScaled(-scale, -scale, scale);
        GL11.glRotated(angle + 180.0, 0.0, 1.0, 0.0);
        GL11.glTranslated(0.0, CustomModel.enabled ? -0.65 : (-1.25 / scale), 0.0);
        GL11.glTranslated(0.0, 0.0, 0.2 / scale);
        if (player.isSneaking()) {
            GL11.glTranslated(0.0, 0.125 / scale, 0.0);
        }
        RenderUtil.color(color.getRGB());
        Minecraft.getMinecraft().getTextureManager().bindTexture(this.location);
        for (int i = 0; i < 2; ++i) {
            GL11.glEnable(2884);
            final float f11 = System.currentTimeMillis() % 1500L / 1500.0f * MathHelper.PI * 2.0f;
            this.wing.rotateAngleX = -1.4f - (float)Math.cos(f11) * 0.2f;
            this.wing.rotateAngleY = 0.35f + (float)Math.sin(f11) * 0.4f;
            this.wing.rotateAngleZ = 0.35f;
            this.wingTip.rotateAngleZ = -(float)(Math.sin(f11 + 2.0f) + 0.5) * 0.75f;
            this.wing.render(0.0625f);
            GL11.glScalef(-1.0f, 1.0f, 1.0f);
            if (i == 0) {
                GL11.glCullFace(1028);
            }
        }
        GL11.glCullFace(1029);
        GL11.glDisable(2884);
        GL11.glColor3f(1.0f, 1.0f, 1.0f);
        GL11.glPopMatrix();
    }
    
    private float interpolate(final float current, final float target, final float percent) {
        final float f = (current + (target - current) * percent) % 360.0f;
        return (f < 0.0f) ? (f + 360.0f) : f;
    }
}
