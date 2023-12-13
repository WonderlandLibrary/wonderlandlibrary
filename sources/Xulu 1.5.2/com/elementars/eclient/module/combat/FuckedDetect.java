// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.combat;

import com.elementars.eclient.friend.Friends;
import com.elementars.eclient.util.EntityUtil;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.Vec3d;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import net.minecraft.entity.Entity;
import com.elementars.eclient.util.MathUtil;
import com.elementars.eclient.util.XuluTessellator;
import java.util.Iterator;
import net.minecraft.util.math.BlockPos;
import com.elementars.eclient.event.events.RenderEvent;
import java.util.HashSet;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import com.elementars.eclient.module.Category;
import net.minecraft.entity.player.EntityPlayer;
import java.util.Set;
import dev.xulu.settings.Value;
import com.elementars.eclient.module.Module;

public class FuckedDetect extends Module
{
    private final Value<Integer> red;
    private final Value<Integer> green;
    private final Value<Integer> blue;
    private final Value<Integer> alpha;
    private final Value<Integer> distance;
    private final Value<Boolean> drawFriends;
    private final Value<Boolean> drawOwn;
    private final Value<String> renderMode;
    public Set<EntityPlayer> fuckedPlayers;
    
    public FuckedDetect() {
        super("FuckedDetector", "Detects when people are fucked", 0, Category.COMBAT, true);
        this.red = this.register(new Value<Integer>("Red", this, 255, 0, 255));
        this.green = this.register(new Value<Integer>("Green", this, 255, 0, 255));
        this.blue = this.register(new Value<Integer>("Blue", this, 255, 0, 255));
        this.alpha = this.register(new Value<Integer>("Alpha", this, 70, 0, 255));
        this.distance = this.register(new Value<Integer>("Draw Distance", this, 20, 0, 30));
        this.drawFriends = this.register(new Value<Boolean>("Draw Friends", this, false));
        this.drawOwn = this.register(new Value<Boolean>("Draw Own", this, false));
        this.renderMode = this.register(new Value<String>("RenderMode", this, "Solid", new ArrayList<String>(Arrays.asList("Solid", "Flat", "Outline", "Full"))));
    }
    
    @Override
    public void onEnable() {
        this.fuckedPlayers = new HashSet<EntityPlayer>();
    }
    
    @Override
    public void onUpdate() {
        if (FuckedDetect.mc.player.isDead || FuckedDetect.mc.player == null || !this.isToggled()) {
            return;
        }
        this.getList();
    }
    
    @Override
    public void onWorldRender(final RenderEvent event) {
        for (final EntityPlayer e : this.fuckedPlayers) {
            this.drawBlock(new BlockPos(e.posX, e.posY, e.posZ), this.red.getValue(), this.green.getValue(), this.blue.getValue());
        }
    }
    
    private void drawBlock(final BlockPos renderBlock, final int r, final int g, final int b) {
        if (this.renderMode.getValue().equalsIgnoreCase("Solid")) {
            XuluTessellator.prepare(7);
            XuluTessellator.drawBox(renderBlock, r, g, b, this.alpha.getValue(), 63);
            XuluTessellator.release();
        }
        else if (this.renderMode.getValue().equalsIgnoreCase("Flat")) {
            XuluTessellator.prepare(7);
            XuluTessellator.drawBox(renderBlock, r, g, b, this.alpha.getValue(), 1);
            XuluTessellator.release();
        }
        else if (this.renderMode.getValue().equalsIgnoreCase("Outline")) {
            final IBlockState iBlockState2 = FuckedDetect.mc.world.getBlockState(renderBlock);
            final Vec3d interp2 = MathUtil.interpolateEntity((Entity)FuckedDetect.mc.player, FuckedDetect.mc.getRenderPartialTicks());
            XuluTessellator.drawBoundingBox(iBlockState2.getSelectedBoundingBox((World)FuckedDetect.mc.world, renderBlock).grow(0.0020000000949949026).offset(-interp2.x, -interp2.y, -interp2.z), 1.5f, r, g, b, this.alpha.getValue());
        }
        else if (this.renderMode.getValue().equalsIgnoreCase("Full")) {
            final IBlockState iBlockState3 = FuckedDetect.mc.world.getBlockState(renderBlock);
            final Vec3d interp3 = MathUtil.interpolateEntity((Entity)FuckedDetect.mc.player, FuckedDetect.mc.getRenderPartialTicks());
            XuluTessellator.drawFullBox(iBlockState3.getSelectedBoundingBox((World)FuckedDetect.mc.world, renderBlock).grow(0.0020000000949949026).offset(-interp3.x, -interp3.y, -interp3.z), renderBlock, 1.5f, r, g, b, this.alpha.getValue(), 255);
        }
    }
    
    public static Vec3d interpolateEntity(final Entity entity, final float time) {
        return new Vec3d(entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * time, entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * time, entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * time);
    }
    
    private boolean canPlaceCrystal(final BlockPos blockPos) {
        final BlockPos boost = blockPos.add(0, 1, 0);
        final BlockPos boost2 = blockPos.add(0, 2, 0);
        return (FuckedDetect.mc.world.getBlockState(blockPos).getBlock() == Blocks.BEDROCK || FuckedDetect.mc.world.getBlockState(blockPos).getBlock() == Blocks.OBSIDIAN) && FuckedDetect.mc.world.getBlockState(boost).getBlock() == Blocks.AIR && FuckedDetect.mc.world.getBlockState(boost2).getBlock() == Blocks.AIR && FuckedDetect.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(boost)).isEmpty() && FuckedDetect.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(boost2)).isEmpty();
    }
    
    public Boolean checkHole(final EntityPlayer ent) {
        final BlockPos pos = new BlockPos(ent.posX, ent.posY - 1.0, ent.posZ);
        if (FuckedDetect.mc.world.getBlockState(pos).getBlock() == Blocks.AIR) {
            return false;
        }
        if (this.canPlaceCrystal(pos.south()) || (this.canPlaceCrystal(pos.south().south()) && FuckedDetect.mc.world.getBlockState(pos.add(0, 1, 1)).getBlock() == Blocks.AIR)) {
            return true;
        }
        if (this.canPlaceCrystal(pos.east()) || (this.canPlaceCrystal(pos.east().east()) && FuckedDetect.mc.world.getBlockState(pos.add(1, 1, 0)).getBlock() == Blocks.AIR)) {
            return true;
        }
        if (this.canPlaceCrystal(pos.west()) || (this.canPlaceCrystal(pos.west().west()) && FuckedDetect.mc.world.getBlockState(pos.add(-1, 1, 0)).getBlock() == Blocks.AIR)) {
            return true;
        }
        return this.canPlaceCrystal(pos.north()) || (this.canPlaceCrystal(pos.north().north()) && FuckedDetect.mc.world.getBlockState(pos.add(0, 1, -1)).getBlock() == Blocks.AIR);
    }
    
    public Set<EntityPlayer> getList() {
        this.fuckedPlayers.clear();
        for (final EntityPlayer ent : FuckedDetect.mc.world.playerEntities) {
            if (EntityUtil.isLiving((Entity)ent)) {
                if (ent.getHealth() <= 0.0f) {
                    continue;
                }
                if (!this.checkHole(ent)) {
                    continue;
                }
                if (!this.drawOwn.getValue() && ent.getName() == FuckedDetect.mc.player.getName()) {
                    continue;
                }
                if (!this.drawFriends.getValue() && Friends.isFriend(ent.getName())) {
                    continue;
                }
                if (FuckedDetect.mc.player.getDistance((Entity)ent) > this.distance.getValue()) {
                    continue;
                }
                this.fuckedPlayers.add(ent);
            }
        }
        return this.fuckedPlayers;
    }
}
