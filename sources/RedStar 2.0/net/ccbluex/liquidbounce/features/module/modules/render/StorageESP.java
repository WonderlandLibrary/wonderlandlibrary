package net.ccbluex.liquidbounce.features.module.modules.render;

import co.uk.hexeption.utils.OutlineUtils;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import kotlin.Metadata;
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
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

@ModuleInfo(name="StorageESP", description="Allows you to see chests, dispensers, etc. through walls.", category=ModuleCategory.RENDER)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000:\n\n\n\b\n\n\b\n\n\b\n\n\u0000\n\n\u0000\n\n\u0000\n\n\u0000\n\n\u0000\b\u000020B¢J\f0\r20HJ020HJ020HR0X¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R\b0X¢\n\u0000R\t0\nX¢\n\u0000R0X¢\n\u0000¨"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/render/StorageESP;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "chestValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "dispenserValue", "enderChestValue", "furnaceValue", "hopperValue", "modeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "shulkerBoxValue", "getColor", "Ljava/awt/Color;", "tileEntity", "Lnet/ccbluex/liquidbounce/api/minecraft/tileentity/ITileEntity;", "onRender2D", "", "event", "Lnet/ccbluex/liquidbounce/event/Render2DEvent;", "onRender3D", "Lnet/ccbluex/liquidbounce/event/Render3DEvent;", "Pride"})
public final class StorageESP
extends Module {
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Box", "OtherBox", "Outline", "ShaderOutline", "ShaderGlow", "2D", "WireFrame"}, "Outline");
    private final BoolValue chestValue = new BoolValue("Chest", true);
    private final BoolValue enderChestValue = new BoolValue("EnderChest", true);
    private final BoolValue furnaceValue = new BoolValue("Furnace", true);
    private final BoolValue dispenserValue = new BoolValue("Dispenser", true);
    private final BoolValue hopperValue = new BoolValue("Hopper", true);
    private final BoolValue shulkerBoxValue = new BoolValue("ShulkerBox", true);

    private final Color getColor(ITileEntity tileEntity) {
        return (Boolean)this.chestValue.get() != false && MinecraftInstance.classProvider.isTileEntityChest(tileEntity) && !ChestAura.INSTANCE.getClickedBlocks().contains(tileEntity.getPos()) ? new Color(0, 66, 255) : ((Boolean)this.enderChestValue.get() != false && MinecraftInstance.classProvider.isTileEntityEnderChest(tileEntity) && !ChestAura.INSTANCE.getClickedBlocks().contains(tileEntity.getPos()) ? Color.MAGENTA : ((Boolean)this.furnaceValue.get() != false && MinecraftInstance.classProvider.isTileEntityFurnace(tileEntity) ? Color.BLACK : ((Boolean)this.dispenserValue.get() != false && MinecraftInstance.classProvider.isTileEntityDispenser(tileEntity) ? Color.BLACK : ((Boolean)this.hopperValue.get() != false && MinecraftInstance.classProvider.isTileEntityHopper(tileEntity) ? Color.GRAY : ((Boolean)this.shulkerBoxValue.get() != false && MinecraftInstance.classProvider.isTileEntityShulkerBox(tileEntity) ? new Color(110, 77, 110).brighter() : null)))));
    }

    /*
     * Unable to fully structure code
     */
    @EventTarget
    public final void onRender3D(@NotNull Render3DEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        try {
            mode = (String)this.modeValue.get();
            if (StringsKt.equals(mode, "outline", true)) {
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
                        if (this.getColor(tileEntity) == null) {
                            continue;
                        }
                        if (!MinecraftInstance.classProvider.isTileEntityChest(tileEntity) && !MinecraftInstance.classProvider.isTileEntityEnderChest(tileEntity)) {
                            RenderUtils.drawBlockBox(tileEntity.getPos(), color, StringsKt.equals(mode, "otherbox", true) == false);
                            continue;
                        }
                        var7_8 = mode;
                        var8_11 = false;
                        v1 = var7_8;
                        if (v1 == null) {
                            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                        }
                        v2 = v1.toLowerCase();
                        Intrinsics.checkExpressionValueIsNotNull(v2, "(this as java.lang.String).toLowerCase()");
                        var7_8 = v2;
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
                                ** GOTO lbl42
                            }
                            case 97739: {
                                if (!var7_8.equals("box")) ** break;
lbl42:
                                // 2 sources

                                RenderUtils.drawBlockBox(tileEntity.getPos(), color, StringsKt.equals(mode, "otherbox", true) == false);
                                ** break;
                            }
                        }
                        v3 = tileEntity.getPos();
                        v4 = color.getRGB();
                        v5 = Color.BLACK;
                        Intrinsics.checkExpressionValueIsNotNull(v5, "Color.BLACK");
                        RenderUtils.draw2D(v3, v4, v5.getRGB());
                        ** break;
                    }
                    RenderUtils.glColor(color);
                    OutlineUtils.renderOne(3.0f);
                    MinecraftInstance.functions.renderTileEntity(tileEntity, event.getPartialTicks(), -1);
                    OutlineUtils.renderTwo();
                    MinecraftInstance.functions.renderTileEntity(tileEntity, event.getPartialTicks(), -1);
                    OutlineUtils.renderThree();
                    MinecraftInstance.functions.renderTileEntity(tileEntity, event.getPartialTicks(), -1);
                    OutlineUtils.renderFour(color);
                    MinecraftInstance.functions.renderTileEntity(tileEntity, event.getPartialTicks(), -1);
                    OutlineUtils.renderFive();
                    OutlineUtils.setColor(Color.WHITE);
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
                RenderUtils.glColor(color);
                GL11.glLineWidth((float)1.5f);
                MinecraftInstance.functions.renderTileEntity(tileEntity, event.getPartialTicks(), -1);
                GL11.glPopAttrib();
                GL11.glPopMatrix();
                ** break;
lbl79:
                // 10 sources

            }
            v6 = MinecraftInstance.mc.getTheWorld();
            if (v6 == null) {
                Intrinsics.throwNpe();
            }
            for (IEntity entity : v6.getLoadedEntityList()) {
                block28: {
                    block27: {
                        if (!MinecraftInstance.classProvider.isEntityMinecartChest(entity)) continue;
                        var6_7 = mode;
                        var7_10 = false;
                        v7 = var6_7;
                        if (v7 == null) {
                            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                        }
                        v8 = v7.toLowerCase();
                        Intrinsics.checkExpressionValueIsNotNull(v8, "(this as java.lang.String).toLowerCase()");
                        var6_7 = v8;
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
                                ** GOTO lbl108
                            }
                            case 97739: {
                                if (!var6_7.equals("box")) ** break;
lbl108:
                                // 2 sources

                                RenderUtils.drawEntityBox(entity, new Color(0, 66, 255), StringsKt.equals(mode, "otherbox", true) == false);
                                ** break;
                            }
                        }
                        v9 = entity.getPosition();
                        v10 = new Color(0, 66, 255).getRGB();
                        v11 = Color.BLACK;
                        Intrinsics.checkExpressionValueIsNotNull(v11, "Color.BLACK");
                        RenderUtils.draw2D(v9, v10, v11.getRGB());
                        ** break;
                    }
                    entityShadow = MinecraftInstance.mc.getGameSettings().getEntityShadows();
                    MinecraftInstance.mc.getGameSettings().setEntityShadows(false);
                    RenderUtils.glColor(new Color(0, 66, 255));
                    OutlineUtils.renderOne(3.0f);
                    MinecraftInstance.mc.getRenderManager().renderEntityStatic(entity, MinecraftInstance.mc.getTimer().getRenderPartialTicks(), true);
                    OutlineUtils.renderTwo();
                    MinecraftInstance.mc.getRenderManager().renderEntityStatic(entity, MinecraftInstance.mc.getTimer().getRenderPartialTicks(), true);
                    OutlineUtils.renderThree();
                    MinecraftInstance.mc.getRenderManager().renderEntityStatic(entity, MinecraftInstance.mc.getTimer().getRenderPartialTicks(), true);
                    OutlineUtils.renderFour(new Color(0, 66, 255));
                    MinecraftInstance.mc.getRenderManager().renderEntityStatic(entity, MinecraftInstance.mc.getTimer().getRenderPartialTicks(), true);
                    OutlineUtils.renderFive();
                    OutlineUtils.setColor(Color.WHITE);
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
                ** break;
lbl159:
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
    public final void onRender2D(@NotNull Render2DEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        String mode = (String)this.modeValue.get();
        FramebufferShader framebufferShader = StringsKt.equals(mode, "shaderoutline", true) ? (FramebufferShader)OutlineShader.OUTLINE_SHADER : (FramebufferShader)(StringsKt.equals(mode, "shaderglow", true) ? GlowShader.GLOW_SHADER : null);
        if (framebufferShader == null) {
            return;
        }
        FramebufferShader shader = framebufferShader;
        float partialTicks = event.getPartialTicks();
        IRenderManager renderManager = MinecraftInstance.mc.getRenderManager();
        shader.startDraw(event.getPartialTicks());
        try {
            Object color;
            HashMap entityMap = new HashMap();
            IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
            if (iWorldClient == null) {
                Intrinsics.throwNpe();
            }
            for (ITileEntity tileEntity : iWorldClient.getLoadedTileEntityList()) {
                if (this.getColor(tileEntity) == null) {
                    continue;
                }
                if (!entityMap.containsKey(color)) {
                    entityMap.put(color, new ArrayList());
                }
                Object v = entityMap.get(color);
                if (v == null) {
                    Intrinsics.throwNpe();
                }
                ((ArrayList)v).add(tileEntity);
            }
            color = entityMap;
            boolean bl = false;
            Iterator<Object> iterator2 = color.entrySet().iterator();
            while (iterator2.hasNext()) {
                Map.Entry entry;
                Map.Entry entry2 = entry = (Map.Entry)iterator2.next();
                boolean bl2 = false;
                color = (Color)entry2.getKey();
                entry2 = entry;
                bl2 = false;
                ArrayList arr = (ArrayList)entry2.getValue();
                shader.startDraw(partialTicks);
                for (ITileEntity entity : arr) {
                    IRenderManager iRenderManager = MinecraftInstance.mc.getRenderManager();
                    ITileEntity iTileEntity = entity;
                    Intrinsics.checkExpressionValueIsNotNull(iTileEntity, "entity");
                    iRenderManager.renderEntityAt(iTileEntity, (double)entity.getPos().getX() - renderManager.getRenderPosX(), (double)entity.getPos().getY() - renderManager.getRenderPosY(), (double)entity.getPos().getZ() - renderManager.getRenderPosZ(), partialTicks);
                }
                shader.stopDraw((Color)color, StringsKt.equals(mode, "shaderglow", true) ? 2.5f : 1.5f, 1.0f);
            }
        }
        catch (Exception ex) {
            ClientUtils.getLogger().error("An error occurred while rendering all storages for shader esp", (Throwable)ex);
        }
        shader.stopDraw(new Color(0, 66, 255), StringsKt.equals(mode, "shaderglow", true) ? 2.5f : 1.5f, 1.0f);
    }
}
