// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.modules.impl.RENDER;

import net.minecraft.client.gui.ScaledResolution;
import java.util.List;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import com.mojang.realmsclient.gui.ChatFormatting;
import ru.tuskevich.util.font.Fonts;
import javax.vecmath.Vector4d;
import ru.tuskevich.util.movement.RenderUtilitys;
import java.util.Arrays;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.AxisAlignedBB;
import ru.tuskevich.event.events.impl.EventDisplay;
import ru.tuskevich.event.EventTarget;
import net.minecraft.util.math.BlockPos;
import java.util.Iterator;
import ru.tuskevich.util.render.RenderUtility;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.tileentity.TileEntity;
import ru.tuskevich.event.events.impl.EventRender;
import java.awt.Color;
import ru.tuskevich.ui.dropui.setting.imp.ColorSetting;
import ru.tuskevich.modules.Type;
import ru.tuskevich.modules.ModuleAnnotation;
import ru.tuskevich.modules.Module;

@ModuleAnnotation(name = "SpawnerESP", desc = "\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd", type = Type.RENDER)
public class SpawnerESP extends Module
{
    public ColorSetting color;
    
    public SpawnerESP() {
        this.color = new ColorSetting("Color", new Color(255, 255, 255).getRGB());
    }
    
    @EventTarget
    public void onRender(final EventRender eventRender) {
        for (final TileEntity entity : SpawnerESP.mc.world.loadedTileEntityList) {
            final BlockPos pos = entity.getPos();
            if (entity instanceof TileEntityMobSpawner) {
                RenderUtility.drawBlockBox(pos, new Color(0, 0, 0).getRed(), (float)new Color(0, 0, 0).getGreen(), (float)new Color(0, 0, 0).getBlue(), (float)new Color(0, 0, 0).getAlpha());
            }
        }
    }
    
    @EventTarget
    public void onDisplay(final EventDisplay eventRender) {
        for (final TileEntity entity : SpawnerESP.mc.world.loadedTileEntityList) {
            if (!(entity instanceof TileEntityMobSpawner)) {
                continue;
            }
            final TileEntityMobSpawner spawner = (TileEntityMobSpawner)entity;
            final double x = entity.getPos().getX() + 0.5f;
            final double y = entity.getPos().getY() + 0.5f;
            final double z = entity.getPos().getZ() + 0.5f;
            final double width = 0.0;
            final double height = 0.0;
            final AxisAlignedBB axisAlignedBB = new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width);
            final List<Vec3d> vectors = Arrays.asList(new Vec3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ), new Vec3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ), new Vec3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ), new Vec3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ), new Vec3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ), new Vec3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ), new Vec3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ), new Vec3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ));
            SpawnerESP.mc.entityRenderer.setupCameraTransform(SpawnerESP.mc.getRenderPartialTicks(), 0);
            final ScaledResolution sr = eventRender.sr;
            Vector4d position = null;
            for (Vec3d vector : vectors) {
                vector = RenderUtilitys.vectorTo2D(2, vector.x - SpawnerESP.mc.getRenderManager().renderPosX, vector.y - SpawnerESP.mc.getRenderManager().renderPosY, vector.z - SpawnerESP.mc.getRenderManager().renderPosZ);
                if (vector != null && vector.z > 0.0 && vector.z < 1.0) {
                    if (position == null) {
                        position = new Vector4d(vector.x, vector.y, vector.z, 0.0);
                    }
                    position.x = Math.min(vector.x, position.x);
                    position.y = Math.min(vector.y, position.y);
                    position.z = Math.max(vector.x, position.z);
                    position.w = Math.max(vector.y, position.w);
                }
            }
            if (position != null) {
                SpawnerESP.mc.entityRenderer.setupOverlayRendering();
                final double posX = position.x;
                final double posY = position.y;
                Fonts.Nunito11.drawCenteredStringWithShadow(ChatFormatting.GRAY + "Spawner - " + ChatFormatting.RESET + "(" + spawner.getSpawnerBaseLogic().getCachedEntity().getName() + ")", posX, posY, new Color(255, 255, 255).getRGB());
            }
            GL11.glEnable(2929);
            GlStateManager.enableBlend();
            SpawnerESP.mc.entityRenderer.setupOverlayRendering();
        }
        for (TileEntity tileEntity : SpawnerESP.mc.world.loadedTileEntityList) {}
    }
}
