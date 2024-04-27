/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  co.uk.hexeption.utils.OutlineUtils
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.text.StringsKt
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.features.module.modules.render;

import co.uk.hexeption.utils.OutlineUtils;
import java.awt.Color;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.api.minecraft.renderer.entity.IRenderManager;
import net.ccbluex.liquidbounce.api.minecraft.tileentity.ITileEntity;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Render2DEvent;
import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.world.ChestAura;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.render.shader.FramebufferShader;
import net.ccbluex.liquidbounce.utils.render.shader.shaders.GlowShader;
import net.ccbluex.liquidbounce.utils.render.shader.shaders.OutlineShader;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.ListValue;
import org.lwjgl.opengl.GL11;

@ModuleInfo(name="StorageESP", description="Allows you to see chests, dispensers, etc. through walls.", category=ModuleCategory.RENDER)
public final class StorageESP
extends Module {
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Box", "OtherBox", "Outline", "ShaderOutline", "ShaderGlow", "2D", "WireFrame"}, "Outline");
    private final BoolValue chestValue = new BoolValue("Chest", true);
    private final BoolValue enderChestValue = new BoolValue("EnderChest", true);
    private final BoolValue furnaceValue = new BoolValue("Furnace", true);
    private final BoolValue dispenserValue = new BoolValue("Dispenser", true);
    private final BoolValue hopperValue = new BoolValue("Hopper", true);
    private final BoolValue shulkerBoxValue = new BoolValue("ShulkerBox", true);

    /*
     * Unable to fully structure code
     */
    @EventTarget
    public final void onRender3D(Render3DEvent event) {
        try {
            mode = (String)this.modeValue.get();
            if (StringsKt.equals((String)mode, (String)"outline", (boolean)true)) {
                ClientUtils.disableFastRender();
                OutlineUtils.checkSetupFBO();
            }
            gamma = MinecraftInstance.mc.getGameSettings().getGammaSetting();
            MinecraftInstance.mc.getGameSettings().setGammaSetting(100000.0f);
            v0 = MinecraftInstance.mc.getTheWorld();
            if (v0 == null) {
                Intrinsics.throwNpe();
            }
            for (ITileEntity tileEntity : v0.getLoadedTileEntityList()) {
                block26: {
                    block25: {
                        if (((Boolean)this.chestValue.get() != false && MinecraftInstance.classProvider.isTileEntityChest(tileEntity) != false && ChestAura.INSTANCE.getClickedBlocks().contains(tileEntity.getPos()) == false ? new Color(0, 66, 255) : ((Boolean)this.enderChestValue.get() != false && MinecraftInstance.classProvider.isTileEntityEnderChest(tileEntity) != false && ChestAura.INSTANCE.getClickedBlocks().contains(tileEntity.getPos()) == false ? Color.MAGENTA : ((Boolean)this.furnaceValue.get() != false && MinecraftInstance.classProvider.isTileEntityFurnace(tileEntity) != false ? Color.BLACK : ((Boolean)this.dispenserValue.get() != false && MinecraftInstance.classProvider.isTileEntityDispenser(tileEntity) != false ? Color.BLACK : ((Boolean)this.hopperValue.get() != false && MinecraftInstance.classProvider.isTileEntityHopper(tileEntity) != false ? Color.GRAY : ((Boolean)this.shulkerBoxValue.get() != false && MinecraftInstance.classProvider.isTileEntityShulkerBox(tileEntity) != false ? new Color(110, 77, 110).brighter() : null)))))) == null) {
                            continue;
                        }
                        color = color;
                        if (!MinecraftInstance.classProvider.isTileEntityChest(tileEntity) && !MinecraftInstance.classProvider.isTileEntityChest(tileEntity)) {
                            RenderUtils.drawBlockBox(tileEntity.getPos(), color, StringsKt.equals((String)mode, (String)"otherbox", (boolean)true) == false);
                            continue;
                        }
                        var7_8 = mode;
                        var8_11 = false;
                        v1 = var7_8;
                        if (v1 == null) {
                            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                        }
                        var7_8 = v1.toLowerCase();
                        switch (var7_8.hashCode()) {
                            case 1650: {
                                if (!var7_8.equals("2d")) ** break;
                                break;
                            }
                            case -1106245566: {
                                if (!var7_8.equals("outline")) ** break;
                                break block25;
                            }
                            case -941784056: {
                                if (!var7_8.equals("wireframe")) ** break;
                                break block26;
                            }
                            case -1171135301: {
                                if (!var7_8.equals("otherbox")) ** break;
                                ** GOTO lbl41
                            }
                            case 97739: {
                                if (!var7_8.equals("box")) ** break;
lbl41:
                                // 2 sources

                                RenderUtils.drawBlockBox(tileEntity.getPos(), color, StringsKt.equals((String)mode, (String)"otherbox", (boolean)true) == false);
                                ** break;
                            }
                        }
                        RenderUtils.draw2D(tileEntity.getPos(), color.getRGB(), Color.BLACK.getRGB());
                        ** break;
                    }
                    RenderUtils.glColor(color);
                    OutlineUtils.renderOne((float)3.0f);
                    MinecraftInstance.functions.renderTileEntity(tileEntity, event.getPartialTicks(), -1);
                    OutlineUtils.renderTwo();
                    MinecraftInstance.functions.renderTileEntity(tileEntity, event.getPartialTicks(), -1);
                    OutlineUtils.renderThree();
                    MinecraftInstance.functions.renderTileEntity(tileEntity, event.getPartialTicks(), -1);
                    OutlineUtils.renderFour((Color)color);
                    MinecraftInstance.functions.renderTileEntity(tileEntity, event.getPartialTicks(), -1);
                    OutlineUtils.renderFive();
                    OutlineUtils.setColor((Color)Color.WHITE);
                    ** break;
                }
                GL11.glPushMatrix();
                GL11.glPushAttrib((int)1048575);
                GL11.glPolygonMode((int)1032, (int)6913);
                GL11.glDisable((int)3553);
                GL11.glDisable((int)2896);
                GL11.glDisable((int)2929);
                GL11.glEnable((int)2848);
                GL11.glEnable((int)3042);
                GL11.glBlendFunc((int)770, (int)771);
                MinecraftInstance.functions.renderTileEntity(tileEntity, event.getPartialTicks(), -1);
                RenderUtils.glColor(color);
                GL11.glLineWidth((float)1.5f);
                MinecraftInstance.functions.renderTileEntity(tileEntity, event.getPartialTicks(), -1);
                GL11.glPopAttrib();
                GL11.glPopMatrix();
lbl74:
                // 10 sources

            }
            v2 = MinecraftInstance.mc.getTheWorld();
            if (v2 == null) {
                Intrinsics.throwNpe();
            }
            for (IEntity entity : v2.getLoadedEntityList()) {
                block28: {
                    block27: {
                        if (!MinecraftInstance.classProvider.isEntityMinecartChest(entity)) continue;
                        var6_7 = mode;
                        var7_10 = false;
                        v3 = var6_7;
                        if (v3 == null) {
                            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                        }
                        var6_7 = v3.toLowerCase();
                        switch (var6_7.hashCode()) {
                            case 1650: {
                                if (!var6_7.equals("2d")) ** break;
                                break;
                            }
                            case -1106245566: {
                                if (!var6_7.equals("outline")) ** break;
                                break block27;
                            }
                            case -941784056: {
                                if (!var6_7.equals("wireframe")) ** break;
                                break block28;
                            }
                            case -1171135301: {
                                if (!var6_7.equals("otherbox")) ** break;
                                ** GOTO lbl101
                            }
                            case 97739: {
                                if (!var6_7.equals("box")) ** break;
lbl101:
                                // 2 sources

                                RenderUtils.drawEntityBox(entity, new Color(0, 66, 255), StringsKt.equals((String)mode, (String)"otherbox", (boolean)true) == false);
                                ** break;
                            }
                        }
                        RenderUtils.draw2D(entity.getPosition(), new Color(0, 66, 255).getRGB(), Color.BLACK.getRGB());
                        ** break;
                    }
                    entityShadow = MinecraftInstance.mc.getGameSettings().getEntityShadows();
                    MinecraftInstance.mc.getGameSettings().setEntityShadows(false);
                    RenderUtils.glColor(new Color(0, 66, 255));
                    OutlineUtils.renderOne((float)3.0f);
                    MinecraftInstance.mc.getRenderManager().renderEntityStatic(entity, MinecraftInstance.mc.getTimer().getRenderPartialTicks(), true);
                    OutlineUtils.renderTwo();
                    MinecraftInstance.mc.getRenderManager().renderEntityStatic(entity, MinecraftInstance.mc.getTimer().getRenderPartialTicks(), true);
                    OutlineUtils.renderThree();
                    MinecraftInstance.mc.getRenderManager().renderEntityStatic(entity, MinecraftInstance.mc.getTimer().getRenderPartialTicks(), true);
                    OutlineUtils.renderFour((Color)new Color(0, 66, 255));
                    MinecraftInstance.mc.getRenderManager().renderEntityStatic(entity, MinecraftInstance.mc.getTimer().getRenderPartialTicks(), true);
                    OutlineUtils.renderFive();
                    OutlineUtils.setColor((Color)Color.WHITE);
                    MinecraftInstance.mc.getGameSettings().setEntityShadows(entityShadow);
                    ** break;
                }
                entityShadow = MinecraftInstance.mc.getGameSettings().getEntityShadows();
                MinecraftInstance.mc.getGameSettings().setEntityShadows(false);
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
                MinecraftInstance.mc.getRenderManager().renderEntityStatic(entity, MinecraftInstance.mc.getTimer().getRenderPartialTicks(), true);
                RenderUtils.glColor(new Color(0, 66, 255));
                GL11.glLineWidth((float)1.5f);
                MinecraftInstance.mc.getRenderManager().renderEntityStatic(entity, MinecraftInstance.mc.getTimer().getRenderPartialTicks(), true);
                GL11.glPopAttrib();
                GL11.glPopMatrix();
                MinecraftInstance.mc.getGameSettings().setEntityShadows(entityShadow);
lbl147:
                // 10 sources

            }
            RenderUtils.glColor(new Color(255, 255, 255, 255));
            MinecraftInstance.mc.getGameSettings().setGammaSetting(gamma);
        }
        catch (Exception var2_3) {
            // empty catch block
        }
    }

    @EventTarget
    public final void onRender2D(Render2DEvent event) {
        String mode = (String)this.modeValue.get();
        FramebufferShader framebufferShader = StringsKt.equals((String)mode, (String)"shaderoutline", (boolean)true) ? (FramebufferShader)OutlineShader.OUTLINE_SHADER : (FramebufferShader)(StringsKt.equals((String)mode, (String)"shaderglow", (boolean)true) ? GlowShader.GLOW_SHADER : null);
        if (framebufferShader == null) {
            return;
        }
        FramebufferShader shader = framebufferShader;
        shader.startDraw(event.getPartialTicks());
        try {
            IRenderManager renderManager = MinecraftInstance.mc.getRenderManager();
            IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
            if (iWorldClient == null) {
                Intrinsics.throwNpe();
            }
            for (ITileEntity iTileEntity : iWorldClient.getLoadedTileEntityList()) {
                if (!MinecraftInstance.classProvider.isTileEntityChest(iTileEntity) || ChestAura.INSTANCE.getClickedBlocks().contains(iTileEntity.getPos())) continue;
                MinecraftInstance.mc.getRenderManager().renderEntityAt(iTileEntity, (double)iTileEntity.getPos().getX() - renderManager.getRenderPosX(), (double)iTileEntity.getPos().getY() - renderManager.getRenderPosY(), (double)iTileEntity.getPos().getZ() - renderManager.getRenderPosZ(), event.getPartialTicks());
            }
            IWorldClient iWorldClient2 = MinecraftInstance.mc.getTheWorld();
            if (iWorldClient2 == null) {
                Intrinsics.throwNpe();
            }
            for (IEntity iEntity : iWorldClient2.getLoadedEntityList()) {
                if (!MinecraftInstance.classProvider.isEntityMinecartChest(iEntity)) continue;
                renderManager.renderEntityStatic(iEntity, event.getPartialTicks(), true);
            }
        }
        catch (Exception ex) {
            ClientUtils.getLogger().error("An error occurred while rendering all storages for shader esp", (Throwable)ex);
        }
        shader.stopDraw(new Color(0, 66, 255), StringsKt.equals((String)mode, (String)"shaderglow", (boolean)true) ? 2.5f : 1.5f, 1.0f);
    }
}

