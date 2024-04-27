/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.text.StringsKt
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.features.module.modules.render;

import java.awt.Color;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.api.IExtractedFunctions;
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
import net.ccbluex.liquidbounce.utils.render.OutlineUtils;
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
    private final BoolValue furnaceValue;
    private final BoolValue hopperValue;
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Box", "OtherBox", "Outline", "ShaderOutline", "ShaderGlow", "2D", "WireFrame"}, "Outline");
    private final BoolValue enderChestValue;
    private final BoolValue chestValue = new BoolValue("Chest", true);
    private final BoolValue shulkerBoxValue;
    private final BoolValue dispenserValue;

    /*
     * Unable to fully structure code
     */
    @EventTarget
    public final void onRender3D(Render3DEvent var1_1) {
        try {
            var2_2 = (String)this.modeValue.get();
            if (StringsKt.equals((String)var2_2, (String)"outline", (boolean)true)) {
                ClientUtils.disableFastRender();
                OutlineUtils.checkSetupFBO();
            }
            var3_4 = MinecraftInstance.mc.getGameSettings().getGammaSetting();
            MinecraftInstance.mc.getGameSettings().setGammaSetting(100000.0f);
            v0 = MinecraftInstance.mc.getTheWorld();
            if (v0 == null) {
                Intrinsics.throwNpe();
            }
            for (Object var4_6 : v0.getLoadedTileEntityList()) {
                block26: {
                    block25: {
                        if (((Boolean)this.chestValue.get() != false && MinecraftInstance.classProvider.isTileEntityChest(var4_6) != false && ChestAura.INSTANCE.getClickedBlocks().contains(var4_6.getPos()) == false ? new Color(0, 66, 255) : ((Boolean)this.enderChestValue.get() != false && MinecraftInstance.classProvider.isTileEntityEnderChest(var4_6) != false && ChestAura.INSTANCE.getClickedBlocks().contains(var4_6.getPos()) == false ? Color.MAGENTA : ((Boolean)this.furnaceValue.get() != false && MinecraftInstance.classProvider.isTileEntityFurnace(var4_6) != false ? Color.BLACK : ((Boolean)this.dispenserValue.get() != false && MinecraftInstance.classProvider.isTileEntityDispenser(var4_6) != false ? Color.BLACK : ((Boolean)this.hopperValue.get() != false && MinecraftInstance.classProvider.isTileEntityHopper(var4_6) != false ? Color.GRAY : ((Boolean)this.shulkerBoxValue.get() != false && MinecraftInstance.classProvider.isTileEntityShulkerBox(var4_6) != false ? new Color(110, 77, 110).brighter() : null)))))) == null) {
                            continue;
                        }
                        var6_7 = var6_7;
                        if (!MinecraftInstance.classProvider.isTileEntityChest(var4_6) && !MinecraftInstance.classProvider.isTileEntityChest(var4_6)) {
                            RenderUtils.drawBlockBox(var4_6.getPos(), (Color)var6_7, StringsKt.equals((String)var2_2, (String)"otherbox", (boolean)true) == false);
                            continue;
                        }
                        var7_8 = var2_2;
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

                                RenderUtils.drawBlockBox(var4_6.getPos(), (Color)var6_7, StringsKt.equals((String)var2_2, (String)"otherbox", (boolean)true) == false);
                                ** break;
                            }
                        }
                        RenderUtils.draw2D(var4_6.getPos(), var6_7.getRGB(), Color.BLACK.getRGB());
                        ** break;
                    }
                    RenderUtils.glColor((Color)var6_7);
                    OutlineUtils.renderOne(3.0f);
                    StorageESP.access$getFunctions$p$s1046033730().renderTileEntity((ITileEntity)var4_6, var1_1.getPartialTicks(), -1);
                    OutlineUtils.renderTwo();
                    StorageESP.access$getFunctions$p$s1046033730().renderTileEntity((ITileEntity)var4_6, var1_1.getPartialTicks(), -1);
                    OutlineUtils.renderThree();
                    StorageESP.access$getFunctions$p$s1046033730().renderTileEntity((ITileEntity)var4_6, var1_1.getPartialTicks(), -1);
                    OutlineUtils.renderFour((Color)var6_7);
                    StorageESP.access$getFunctions$p$s1046033730().renderTileEntity((ITileEntity)var4_6, var1_1.getPartialTicks(), -1);
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
                StorageESP.access$getFunctions$p$s1046033730().renderTileEntity((ITileEntity)var4_6, var1_1.getPartialTicks(), -1);
                RenderUtils.glColor((Color)var6_7);
                GL11.glLineWidth((float)1.5f);
                StorageESP.access$getFunctions$p$s1046033730().renderTileEntity((ITileEntity)var4_6, var1_1.getPartialTicks(), -1);
                GL11.glPopAttrib();
                GL11.glPopMatrix();
                ** break;
lbl75:
                // 10 sources

            }
            v2 = MinecraftInstance.mc.getTheWorld();
            if (v2 == null) {
                Intrinsics.throwNpe();
            }
            for (Object var4_6 : v2.getLoadedEntityList()) {
                block28: {
                    block27: {
                        if (!MinecraftInstance.classProvider.isEntityMinecartChest(var4_6)) continue;
                        var6_7 = var2_2;
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
                                ** GOTO lbl102
                            }
                            case 97739: {
                                if (!var6_7.equals("box")) ** break;
lbl102:
                                // 2 sources

                                RenderUtils.drawEntityBox((IEntity)var4_6, new Color(0, 66, 255), StringsKt.equals((String)var2_2, (String)"otherbox", (boolean)true) == false);
                                ** break;
                            }
                        }
                        RenderUtils.draw2D(var4_6.getPosition(), new Color(0, 66, 255).getRGB(), Color.BLACK.getRGB());
                        ** break;
                    }
                    var7_10 = MinecraftInstance.mc.getGameSettings().getEntityShadows();
                    MinecraftInstance.mc.getGameSettings().setEntityShadows(false);
                    RenderUtils.glColor(new Color(0, 66, 255));
                    OutlineUtils.renderOne(3.0f);
                    MinecraftInstance.mc.getRenderManager().renderEntityStatic((IEntity)var4_6, MinecraftInstance.mc.getTimer().getRenderPartialTicks(), true);
                    OutlineUtils.renderTwo();
                    MinecraftInstance.mc.getRenderManager().renderEntityStatic((IEntity)var4_6, MinecraftInstance.mc.getTimer().getRenderPartialTicks(), true);
                    OutlineUtils.renderThree();
                    MinecraftInstance.mc.getRenderManager().renderEntityStatic((IEntity)var4_6, MinecraftInstance.mc.getTimer().getRenderPartialTicks(), true);
                    OutlineUtils.renderFour(new Color(0, 66, 255));
                    MinecraftInstance.mc.getRenderManager().renderEntityStatic((IEntity)var4_6, MinecraftInstance.mc.getTimer().getRenderPartialTicks(), true);
                    OutlineUtils.renderFive();
                    OutlineUtils.setColor(Color.WHITE);
                    MinecraftInstance.mc.getGameSettings().setEntityShadows(var7_10);
                    ** break;
                }
                var7_10 = MinecraftInstance.mc.getGameSettings().getEntityShadows();
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
                MinecraftInstance.mc.getRenderManager().renderEntityStatic((IEntity)var4_6, MinecraftInstance.mc.getTimer().getRenderPartialTicks(), true);
                RenderUtils.glColor(new Color(0, 66, 255));
                GL11.glLineWidth((float)1.5f);
                MinecraftInstance.mc.getRenderManager().renderEntityStatic((IEntity)var4_6, MinecraftInstance.mc.getTimer().getRenderPartialTicks(), true);
                GL11.glPopAttrib();
                GL11.glPopMatrix();
                MinecraftInstance.mc.getGameSettings().setEntityShadows(var7_10);
                ** break;
lbl149:
                // 10 sources

            }
            RenderUtils.glColor(new Color(255, 255, 255, 255));
            MinecraftInstance.mc.getGameSettings().setGammaSetting(var3_4);
        }
        catch (Exception var2_3) {
            // empty catch block
        }
    }

    public static final IExtractedFunctions access$getFunctions$p$s1046033730() {
        return MinecraftInstance.functions;
    }

    public StorageESP() {
        this.enderChestValue = new BoolValue("EnderChest", true);
        this.furnaceValue = new BoolValue("Furnace", true);
        this.dispenserValue = new BoolValue("Dispenser", true);
        this.hopperValue = new BoolValue("Hopper", true);
        this.shulkerBoxValue = new BoolValue("ShulkerBox", true);
    }

    @EventTarget
    public final void onRender2D(Render2DEvent render2DEvent) {
        String string = (String)this.modeValue.get();
        FramebufferShader framebufferShader = StringsKt.equals((String)string, (String)"shaderoutline", (boolean)true) ? (FramebufferShader)OutlineShader.OUTLINE_SHADER : (FramebufferShader)(StringsKt.equals((String)string, (String)"shaderglow", (boolean)true) ? GlowShader.GLOW_SHADER : null);
        if (framebufferShader == null) {
            return;
        }
        FramebufferShader framebufferShader2 = framebufferShader;
        framebufferShader2.startDraw(render2DEvent.getPartialTicks());
        IRenderManager iRenderManager = MinecraftInstance.mc.getRenderManager();
        IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
        if (iWorldClient == null) {
            Intrinsics.throwNpe();
        }
        for (Object object : iWorldClient.getLoadedTileEntityList()) {
            if (!MinecraftInstance.classProvider.isTileEntityChest(object) || ChestAura.INSTANCE.getClickedBlocks().contains(object.getPos())) continue;
            MinecraftInstance.mc.getRenderManager().renderEntityAt((ITileEntity)object, (double)object.getPos().getX() - iRenderManager.getRenderPosX(), (double)object.getPos().getY() - iRenderManager.getRenderPosY(), (double)object.getPos().getZ() - iRenderManager.getRenderPosZ(), render2DEvent.getPartialTicks());
        }
        IWorldClient iWorldClient2 = MinecraftInstance.mc.getTheWorld();
        if (iWorldClient2 == null) {
            Intrinsics.throwNpe();
        }
        for (Object object : iWorldClient2.getLoadedEntityList()) {
            if (!MinecraftInstance.classProvider.isEntityMinecartChest(object)) continue;
            iRenderManager.renderEntityStatic((IEntity)object, render2DEvent.getPartialTicks(), true);
        }
        framebufferShader2.stopDraw(new Color(0, 66, 255), StringsKt.equals((String)string, (String)"shaderglow", (boolean)true) ? 2.5f : 1.5f, 1.0f);
    }
}

