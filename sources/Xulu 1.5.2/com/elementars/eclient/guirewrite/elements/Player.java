// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.guirewrite.elements;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.gui.inventory.GuiInventory;
import org.lwjgl.input.Mouse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.gui.ScaledResolution;
import com.elementars.eclient.module.Module;
import dev.xulu.settings.Value;
import com.elementars.eclient.guirewrite.Element;

public class Player extends Element
{
    private final Value<String> noLook;
    private final Value<Integer> scale;
    
    public Player() {
        super("Player");
        this.noLook = this.register(new Value<String>("Look Mode", this, "Mouse", new String[] { "Mouse", "Free", "None" }));
        this.scale = this.register(new Value<Integer>("Scale", this, 30, 1, 100));
    }
    
    @Override
    public void onEnable() {
        this.width = 34.0;
        this.height = 63.0;
        super.onEnable();
    }
    
    @Override
    public void onRender() {
        final ScaledResolution s = new ScaledResolution(Player.mc);
        if (Player.mc.player == null) {
            return;
        }
        if (Player.mc.gameSettings.thirdPersonView != 0) {
            return;
        }
        GlStateManager.pushMatrix();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        if (this.noLook.getValue().equalsIgnoreCase("Free")) {
            this.drawPlayer((EntityPlayer)Player.mc.player, (int)this.x, (int)this.y);
        }
        else {
            GuiInventory.drawEntityOnScreen((int)this.x + 17, (int)this.y + 60, 30, this.noLook.getValue().equalsIgnoreCase("None") ? 0.0f : ((float)this.x - Mouse.getX()), this.noLook.getValue().equalsIgnoreCase("None") ? 0.0f : (-s.getScaledHeight() + (float)Mouse.getY()), (EntityLivingBase)Player.mc.player);
        }
        GlStateManager.popMatrix();
    }
    
    public void drawPlayer(final EntityPlayer player, final int x, final int y) {
        final EntityPlayer ent = player;
        GlStateManager.pushMatrix();
        GlStateManager.color(1.0f, 1.0f, 1.0f);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enableAlpha();
        GlStateManager.shadeModel(7424);
        GlStateManager.enableAlpha();
        GlStateManager.enableDepth();
        GlStateManager.rotate(0.0f, 0.0f, 5.0f, 0.0f);
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)(x + 16), (float)(y + 55), 50.0f);
        GlStateManager.scale(-1.0f * this.scale.getValue(), 1.0f * this.scale.getValue(), 1.0f * this.scale.getValue());
        GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(135.0f, 0.0f, 1.0f, 0.0f);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-(float)Math.atan(y / 40.0f) * 20.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.translate(0.0f, 0.0f, 0.0f);
        final RenderManager rendermanager = Player.mc.getRenderManager();
        rendermanager.setPlayerViewY(180.0f);
        rendermanager.setRenderShadow(false);
        try {
            rendermanager.renderEntity((Entity)ent, 0.0, 0.0, 0.0, 0.0f, 1.0f, false);
        }
        catch (Exception ex) {}
        rendermanager.setRenderShadow(true);
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GlStateManager.depthFunc(515);
        GlStateManager.resetColor();
        GlStateManager.disableDepth();
        GlStateManager.popMatrix();
    }
}
