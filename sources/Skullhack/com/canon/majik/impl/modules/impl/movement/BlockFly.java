package com.canon.majik.impl.modules.impl.movement;

import com.canon.majik.api.event.eventBus.EventListener;
import com.canon.majik.api.event.events.MoveEvent;
import com.canon.majik.api.event.events.Render3DEvent;
import com.canon.majik.api.utils.client.TColor;
import com.canon.majik.api.utils.player.PlayerUtils;
import com.canon.majik.api.utils.render.RenderUtils;
import com.canon.majik.impl.modules.api.Category;
import com.canon.majik.impl.modules.api.Module;
import com.canon.majik.impl.setting.settings.BooleanSetting;
import com.canon.majik.impl.setting.settings.ColorSetting;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class BlockFly extends Module {

    BooleanSetting tower = setting("Tower", true);
    BooleanSetting packet = setting("Packet", true);
    ColorSetting color = setting("Color", new TColor(255, 0,0, 100));

    public BlockFly(String name, Category category) {
        super(name, category);
    }
    BlockPos pos;

    @EventListener
    public void onMove(MoveEvent event){
        if(nullCheck()) return;
        pos = new BlockPos(mc.player.posX,mc.player.posY-1, mc.player.posZ);
        if(isAir(pos) && mc.player.inventory.getCurrentItem().getItem() instanceof ItemBlock){
            PlayerUtils.placeBlock(pos,EnumFacing.UP,EnumHand.MAIN_HAND,packet.getValue());
        }
        event.setCancelled(true);
        if(tower.getValue()){
            if(mc.gameSettings.keyBindJump.isKeyDown() && !(PlayerUtils.isMoving())) {
                event.setY(0.42);
            }
        }
    }

    @EventListener
    public void onRender(Render3DEvent event){
        if(pos != null){
            RenderUtils.renderBox(new AxisAlignedBB(pos), true, true, color.getValue());
        }
    }

    private boolean isAir(BlockPos pos) {
        return this.mc.world.getBlockState(pos).getBlock() == Blocks.AIR;
    }
}
