// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.render;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.world.GameType;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import com.elementars.eclient.util.XuluTessellator;
import com.elementars.eclient.util.RainbowUtils;
import net.minecraft.entity.Entity;
import com.elementars.eclient.util.MathUtil;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.client.Minecraft;
import com.elementars.eclient.event.events.RenderEvent;
import net.minecraftforge.common.MinecraftForge;
import com.elementars.eclient.module.Category;
import dev.xulu.settings.Value;
import net.minecraft.util.math.BlockPos;
import com.elementars.eclient.module.Module;

public class BlockHighlight extends Module
{
    private static BlockPos position;
    private final Value<String> mode;
    private final Value<Boolean> rainbow;
    private final Value<Integer> red;
    private final Value<Integer> green;
    private final Value<Integer> blue;
    private final Value<Integer> alpha;
    private final Value<Integer> alphaF;
    private final Value<Float> width;
    
    public BlockHighlight() {
        super("BlockHighlight", "Highlights block you're looking at", 0, Category.RENDER, true);
        this.mode = this.register(new Value<String>("Mode", this, "Outline", new String[] { "Solid", "Outline", "Full" }));
        this.rainbow = this.register(new Value<Boolean>("Rainbow", this, false));
        this.red = this.register(new Value<Integer>("Red", this, 255, 0, 255));
        this.green = this.register(new Value<Integer>("Green", this, 255, 0, 255));
        this.blue = this.register(new Value<Integer>("Blue", this, 255, 0, 255));
        this.alpha = this.register(new Value<Integer>("Alpha", this, 255, 0, 255));
        this.alphaF = this.register(new Value<Integer>("Alpha Full", this, 255, 0, 255));
        this.width = this.register(new Value<Float>("Width", this, 1.0f, 1.0f, 10.0f));
    }
    
    @Override
    public void onEnable() {
        MinecraftForge.EVENT_BUS.register((Object)this);
    }
    
    @Override
    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister((Object)this);
        BlockHighlight.position = null;
    }
    
    @Override
    public void onWorldRender(final RenderEvent event) {
        final Minecraft mc = Minecraft.getMinecraft();
        final RayTraceResult ray = mc.objectMouseOver;
        if (ray != null && ray.typeOfHit == RayTraceResult.Type.BLOCK) {
            final BlockPos blockpos = ray.getBlockPos();
            final IBlockState iblockstate = mc.world.getBlockState(blockpos);
            if (iblockstate.getMaterial() != Material.AIR && mc.world.getWorldBorder().contains(blockpos)) {
                final Vec3d interp = MathUtil.interpolateEntity((Entity)mc.player, mc.getRenderPartialTicks());
                int r = this.red.getValue();
                int g = this.green.getValue();
                int b = this.blue.getValue();
                if (this.rainbow.getValue()) {
                    r = RainbowUtils.r;
                    g = RainbowUtils.g;
                    b = RainbowUtils.b;
                }
                if (this.mode.getValue().equalsIgnoreCase("Solid")) {
                    XuluTessellator.prepare(7);
                    XuluTessellator.drawBox(blockpos, r, g, b, this.alpha.getValue(), 63);
                    XuluTessellator.release();
                }
                else if (this.mode.getValue().equalsIgnoreCase("Outline")) {
                    XuluTessellator.drawBoundingBox(iblockstate.getSelectedBoundingBox((World)mc.world, blockpos).grow(0.0020000000949949026).offset(-interp.x, -interp.y, -interp.z), this.width.getValue(), r, g, b, this.alpha.getValue());
                }
                else if (this.mode.getValue().equalsIgnoreCase("Full")) {
                    XuluTessellator.drawFullBox(iblockstate.getSelectedBoundingBox((World)mc.world, blockpos).grow(0.0020000000949949026).offset(-interp.x, -interp.y, -interp.z), blockpos, this.width.getValue(), r, g, b, this.alpha.getValue(), this.alphaF.getValue());
                }
            }
        }
    }
    
    @SubscribeEvent
    public void onDrawBlockHighlight(final DrawBlockHighlightEvent event) {
        if (BlockHighlight.mc.player == null || BlockHighlight.mc.world == null || (!BlockHighlight.mc.playerController.getCurrentGameType().equals((Object)GameType.SURVIVAL) && !BlockHighlight.mc.playerController.getCurrentGameType().equals((Object)GameType.CREATIVE))) {
            return;
        }
        event.setCanceled(true);
    }
}
