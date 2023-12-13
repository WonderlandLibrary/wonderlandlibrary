// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.render;

import net.minecraft.client.renderer.DestroyBlockProgress;
import net.minecraft.util.math.Vec3d;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import net.minecraft.entity.Entity;
import com.elementars.eclient.util.MathUtil;
import com.elementars.eclient.util.XuluTessellator;
import net.minecraft.init.Blocks;
import com.elementars.eclient.event.events.RenderEvent;
import java.util.Collection;
import java.util.Arrays;
import java.util.HashMap;
import com.elementars.eclient.module.Category;
import dev.xulu.settings.Value;
import java.util.Map;
import net.minecraft.util.math.BlockPos;
import io.netty.util.internal.ConcurrentSet;
import java.util.ArrayList;
import com.elementars.eclient.module.Module;

public class BreakESP extends Module
{
    private ArrayList<String> options;
    public ConcurrentSet<BlockPos> breaking;
    private ConcurrentSet<BlockPos> test;
    private Map<Integer, Integer> alphaMap;
    BlockPos pos;
    private final Value<String> mode;
    private final Value<Boolean> ignoreSelf;
    private final Value<Boolean> onlyObby;
    private final Value<Boolean> fade;
    private final Value<Integer> red;
    private final Value<Integer> green;
    private final Value<Integer> blue;
    private final Value<Integer> alpha;
    private final Value<Integer> alphaF;
    float inc;
    public static BreakESP INSTANCE;
    
    public BreakESP() {
        super("BreakESP", "Highlights blocks being broken", 0, Category.RENDER, true);
        this.breaking = (ConcurrentSet<BlockPos>)new ConcurrentSet();
        this.test = (ConcurrentSet<BlockPos>)new ConcurrentSet();
        this.alphaMap = new HashMap<Integer, Integer>();
        this.mode = this.register(new Value<String>("Mode", this, "Solid", new ArrayList<String>(Arrays.asList("Solid", "Outline", "Full"))));
        this.ignoreSelf = this.register(new Value<Boolean>("Ignore Self", this, true));
        this.onlyObby = this.register(new Value<Boolean>("Only Obsidian", this, true));
        this.fade = this.register(new Value<Boolean>("Fade Progress", this, true));
        this.red = this.register(new Value<Integer>("Red", this, 255, 0, 255));
        this.green = this.register(new Value<Integer>("Green", this, 0, 0, 255));
        this.blue = this.register(new Value<Integer>("Blue", this, 0, 0, 255));
        this.alpha = this.register(new Value<Integer>("Alpha", this, 70, 0, 255));
        this.alphaF = this.register(new Value<Integer>("Full Alpha", this, 100, 0, 255));
        this.alphaMap.put(0, 28);
        this.alphaMap.put(1, 56);
        this.alphaMap.put(2, 84);
        this.alphaMap.put(3, 112);
        this.alphaMap.put(4, 140);
        this.alphaMap.put(5, 168);
        this.alphaMap.put(6, 196);
        this.alphaMap.put(7, 224);
        this.alphaMap.put(8, 255);
        this.alphaMap.put(9, 255);
    }
    
    @Override
    public void onWorldRender(final RenderEvent event) {
        int transparency;
        IBlockState iBlockState3;
        Vec3d interp3;
        IBlockState iBlockState4;
        Vec3d interp4;
        BreakESP.mc.renderGlobal.damagedBlocks.forEach((integer, destroyBlockProgress) -> {
            if (destroyBlockProgress != null) {
                if (!this.ignoreSelf.getValue() || BreakESP.mc.world.getEntityByID((int)integer) != BreakESP.mc.player) {
                    if (!this.onlyObby.getValue() || BreakESP.mc.world.getBlockState(destroyBlockProgress.getPosition()).getBlock() == Blocks.OBSIDIAN) {
                        transparency = (this.fade.getValue() ? this.alphaMap.get(destroyBlockProgress.getPartialBlockDamage()) : ((int)this.alpha.getValue()));
                        if (this.mode.getValue().equalsIgnoreCase("Solid")) {
                            XuluTessellator.prepare(7);
                            XuluTessellator.drawBox(destroyBlockProgress.getPosition(), this.red.getValue(), this.green.getValue(), this.blue.getValue(), transparency, 63);
                            XuluTessellator.release();
                        }
                        else if (this.mode.getValue().equalsIgnoreCase("Full")) {
                            iBlockState3 = BreakESP.mc.world.getBlockState(destroyBlockProgress.getPosition());
                            interp3 = MathUtil.interpolateEntity((Entity)BreakESP.mc.player, BreakESP.mc.getRenderPartialTicks());
                            XuluTessellator.drawFullBox(iBlockState3.getSelectedBoundingBox((World)BreakESP.mc.world, destroyBlockProgress.getPosition()).grow(0.0020000000949949026).offset(-interp3.x, -interp3.y, -interp3.z), destroyBlockProgress.getPosition(), 1.5f, this.red.getValue(), this.green.getValue(), this.blue.getValue(), transparency, this.alphaF.getValue());
                        }
                        else if (this.mode.getValue().equalsIgnoreCase("Outline")) {
                            iBlockState4 = BreakESP.mc.world.getBlockState(destroyBlockProgress.getPosition());
                            interp4 = MathUtil.interpolateEntity((Entity)BreakESP.mc.player, BreakESP.mc.getRenderPartialTicks());
                            XuluTessellator.drawBoundingBox(iBlockState4.getSelectedBoundingBox((World)BreakESP.mc.world, destroyBlockProgress.getPosition()).grow(0.0020000000949949026).offset(-interp4.x, -interp4.y, -interp4.z), 1.5f, this.red.getValue(), this.green.getValue(), this.blue.getValue(), transparency);
                        }
                        else {
                            XuluTessellator.prepare(7);
                            XuluTessellator.drawBox(destroyBlockProgress.getPosition(), this.red.getValue(), this.green.getValue(), this.blue.getValue(), transparency, 63);
                            XuluTessellator.release();
                        }
                    }
                }
            }
        });
    }
}
