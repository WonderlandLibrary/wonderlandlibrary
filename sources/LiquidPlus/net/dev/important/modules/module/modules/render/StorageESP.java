/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.entity.RenderManager
 *  net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityMinecartChest
 *  net.minecraft.tileentity.TileEntity
 *  net.minecraft.tileentity.TileEntityChest
 *  net.minecraft.tileentity.TileEntityDispenser
 *  net.minecraft.tileentity.TileEntityEnderChest
 *  net.minecraft.tileentity.TileEntityFurnace
 *  net.minecraft.tileentity.TileEntityHopper
 *  org.lwjgl.opengl.GL11
 */
package net.dev.important.modules.module.modules.render;

import java.awt.Color;
import net.dev.important.event.EventTarget;
import net.dev.important.event.Render2DEvent;
import net.dev.important.event.Render3DEvent;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.modules.module.modules.world.ChestAura;
import net.dev.important.utils.ClientUtils;
import net.dev.important.utils.render.RenderUtils;
import net.dev.important.utils.render.shader.FramebufferShader;
import net.dev.important.utils.render.shader.shaders.GlowShader;
import net.dev.important.utils.render.shader.shaders.OutlineShader;
import net.dev.important.value.BoolValue;
import net.dev.important.value.ListValue;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityHopper;
import org.lwjgl.opengl.GL11;

@Info(name="StorageESP", spacedName="Storage ESP", description="Allows you to see chests, dispensers, etc. through walls.", category=Category.RENDER, cnName="\u900f\u89c6\u7269\u54c1")
public class StorageESP
extends Module {
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Box", "OtherBox", "Outline", "ShaderOutline", "ShaderGlow", "2D", "WireFrame"}, "Outline");
    private final BoolValue chestValue = new BoolValue("Chest", true);
    private final BoolValue enderChestValue = new BoolValue("EnderChest", true);
    private final BoolValue furnaceValue = new BoolValue("Furnace", true);
    private final BoolValue dispenserValue = new BoolValue("Dispenser", true);
    private final BoolValue hopperValue = new BoolValue("Hopper", true);

    @EventTarget
    public void onRender3D(Render3DEvent event) {
        try {
            String mode = (String)this.modeValue.get();
            if (mode.equalsIgnoreCase("outline")) {
                ClientUtils.disableFastRender();
                RenderUtils.checkSetupFBO();
            }
            float gamma = StorageESP.mc.field_71474_y.field_74333_Y;
            StorageESP.mc.field_71474_y.field_74333_Y = 100000.0f;
            for (TileEntity tileEntity : StorageESP.mc.field_71441_e.field_147482_g) {
                Color color = null;
                if (((Boolean)this.chestValue.get()).booleanValue() && tileEntity instanceof TileEntityChest && !ChestAura.INSTANCE.getClickedBlocks().contains(tileEntity.func_174877_v())) {
                    color = new Color(0, 66, 255);
                }
                if (((Boolean)this.enderChestValue.get()).booleanValue() && tileEntity instanceof TileEntityEnderChest && !ChestAura.INSTANCE.getClickedBlocks().contains(tileEntity.func_174877_v())) {
                    color = Color.MAGENTA;
                }
                if (((Boolean)this.furnaceValue.get()).booleanValue() && tileEntity instanceof TileEntityFurnace) {
                    color = Color.BLACK;
                }
                if (((Boolean)this.dispenserValue.get()).booleanValue() && tileEntity instanceof TileEntityDispenser) {
                    color = Color.BLACK;
                }
                if (((Boolean)this.hopperValue.get()).booleanValue() && tileEntity instanceof TileEntityHopper) {
                    color = Color.GRAY;
                }
                if (color == null) continue;
                if (!(tileEntity instanceof TileEntityChest) && !(tileEntity instanceof TileEntityEnderChest)) {
                    RenderUtils.drawBlockBox(tileEntity.func_174877_v(), color, !mode.equalsIgnoreCase("otherbox"));
                    continue;
                }
                switch (mode.toLowerCase()) {
                    case "otherbox": 
                    case "box": {
                        RenderUtils.drawBlockBox(tileEntity.func_174877_v(), color, !mode.equalsIgnoreCase("otherbox"));
                        break;
                    }
                    case "2d": {
                        RenderUtils.draw2D(tileEntity.func_174877_v(), color.getRGB(), Color.BLACK.getRGB());
                        break;
                    }
                    case "outline": {
                        RenderUtils.glColor(color);
                        RenderUtils.renderOne(3.0f);
                        TileEntityRendererDispatcher.field_147556_a.func_180546_a(tileEntity, event.getPartialTicks(), -1);
                        RenderUtils.renderTwo();
                        TileEntityRendererDispatcher.field_147556_a.func_180546_a(tileEntity, event.getPartialTicks(), -1);
                        RenderUtils.renderThree();
                        TileEntityRendererDispatcher.field_147556_a.func_180546_a(tileEntity, event.getPartialTicks(), -1);
                        RenderUtils.renderFour(color);
                        TileEntityRendererDispatcher.field_147556_a.func_180546_a(tileEntity, event.getPartialTicks(), -1);
                        RenderUtils.renderFive();
                        RenderUtils.setColor(Color.WHITE);
                        break;
                    }
                    case "wireframe": {
                        GL11.glPushMatrix();
                        GL11.glPushAttrib((int)1048575);
                        GL11.glPolygonMode((int)1032, (int)6913);
                        GL11.glDisable((int)3553);
                        GL11.glDisable((int)2896);
                        GL11.glDisable((int)2929);
                        GL11.glEnable((int)2848);
                        GL11.glEnable((int)3042);
                        GL11.glBlendFunc((int)770, (int)771);
                        TileEntityRendererDispatcher.field_147556_a.func_180546_a(tileEntity, event.getPartialTicks(), -1);
                        RenderUtils.glColor(color);
                        GL11.glLineWidth((float)1.5f);
                        TileEntityRendererDispatcher.field_147556_a.func_180546_a(tileEntity, event.getPartialTicks(), -1);
                        GL11.glPopAttrib();
                        GL11.glPopMatrix();
                    }
                }
            }
            for (Entity entity : StorageESP.mc.field_71441_e.field_72996_f) {
                if (!(entity instanceof EntityMinecartChest)) continue;
                switch (mode.toLowerCase()) {
                    case "otherbox": 
                    case "box": {
                        RenderUtils.drawEntityBox(entity, new Color(0, 66, 255), !mode.equalsIgnoreCase("otherbox"));
                        break;
                    }
                    case "2d": {
                        RenderUtils.draw2D(entity.func_180425_c(), new Color(0, 66, 255).getRGB(), Color.BLACK.getRGB());
                        break;
                    }
                    case "outline": {
                        boolean entityShadow = StorageESP.mc.field_71474_y.field_181151_V;
                        StorageESP.mc.field_71474_y.field_181151_V = false;
                        RenderUtils.glColor(new Color(0, 66, 255));
                        RenderUtils.renderOne(3.0f);
                        mc.func_175598_ae().func_147936_a(entity, StorageESP.mc.field_71428_T.field_74281_c, true);
                        RenderUtils.renderTwo();
                        mc.func_175598_ae().func_147936_a(entity, StorageESP.mc.field_71428_T.field_74281_c, true);
                        RenderUtils.renderThree();
                        mc.func_175598_ae().func_147936_a(entity, StorageESP.mc.field_71428_T.field_74281_c, true);
                        RenderUtils.renderFour(new Color(0, 66, 255));
                        mc.func_175598_ae().func_147936_a(entity, StorageESP.mc.field_71428_T.field_74281_c, true);
                        RenderUtils.renderFive();
                        RenderUtils.setColor(Color.WHITE);
                        StorageESP.mc.field_71474_y.field_181151_V = entityShadow;
                        break;
                    }
                    case "wireframe": {
                        boolean entityShadow = StorageESP.mc.field_71474_y.field_181151_V;
                        StorageESP.mc.field_71474_y.field_181151_V = false;
                        GL11.glPushMatrix();
                        GL11.glPushAttrib((int)1048575);
                        GL11.glPolygonMode((int)1032, (int)6913);
                        GL11.glDisable((int)3553);
                        GL11.glDisable((int)2896);
                        GL11.glDisable((int)2929);
                        GL11.glEnable((int)2848);
                        GL11.glEnable((int)3042);
                        GL11.glBlendFunc((int)770, (int)771);
                        RenderUtils.glColor(new Color(0, 66, 255));
                        mc.func_175598_ae().func_147936_a(entity, StorageESP.mc.field_71428_T.field_74281_c, true);
                        RenderUtils.glColor(new Color(0, 66, 255));
                        GL11.glLineWidth((float)1.5f);
                        mc.func_175598_ae().func_147936_a(entity, StorageESP.mc.field_71428_T.field_74281_c, true);
                        GL11.glPopAttrib();
                        GL11.glPopMatrix();
                        StorageESP.mc.field_71474_y.field_181151_V = entityShadow;
                        break;
                    }
                }
            }
            RenderUtils.glColor(new Color(255, 255, 255, 255));
            StorageESP.mc.field_71474_y.field_74333_Y = gamma;
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    @EventTarget
    public void onRender2D(Render2DEvent event) {
        OutlineShader shader;
        String mode = (String)this.modeValue.get();
        FramebufferShader framebufferShader = mode.equalsIgnoreCase("shaderoutline") ? OutlineShader.OUTLINE_SHADER : (shader = mode.equalsIgnoreCase("shaderglow") ? GlowShader.GLOW_SHADER : null);
        if (shader == null) {
            return;
        }
        shader.startDraw(event.getPartialTicks());
        try {
            RenderManager renderManager = mc.func_175598_ae();
            for (TileEntity entity : StorageESP.mc.field_71441_e.field_147482_g) {
                if (!(entity instanceof TileEntityChest) || ChestAura.INSTANCE.getClickedBlocks().contains(entity.func_174877_v())) continue;
                TileEntityRendererDispatcher.field_147556_a.func_147549_a(entity, (double)entity.func_174877_v().func_177958_n() - renderManager.field_78725_b, (double)entity.func_174877_v().func_177956_o() - renderManager.field_78726_c, (double)entity.func_174877_v().func_177952_p() - renderManager.field_78723_d, event.getPartialTicks());
            }
            for (TileEntity entity : StorageESP.mc.field_71441_e.field_72996_f) {
                if (!(entity instanceof EntityMinecartChest)) continue;
                renderManager.func_147936_a((Entity)entity, event.getPartialTicks(), true);
            }
        }
        catch (Exception ex) {
            ClientUtils.getLogger().error("An error occurred while rendering all storages for shader esp", (Throwable)ex);
        }
        shader.stopDraw(new Color(0, 66, 255), mode.equalsIgnoreCase("shaderglow") ? 2.5f : 1.5f, 1.0f);
    }
}

