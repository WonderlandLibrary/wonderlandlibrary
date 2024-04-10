package info.sigmaclient.sigma.modules.item;

import info.sigmaclient.sigma.config.values.NumberValue;
import info.sigmaclient.sigma.event.player.ClickEvent;
import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BucketItem;
import net.minecraft.util.math.BlockPos;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class AutoMLG extends Module {
    private final NumberValue height = new NumberValue("Height", 3, 3, 10, NumberValue.NUMBER_TYPE.INT);
    public AutoMLG() {
        super("AutoMLG", Category.Item, "Auto use water bu");
     registerValue(height);
    }
    int delay = 0;
    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onClickEvent(ClickEvent event) {
        if (mc.player.fallDistance > height.getValue().longValue()) {
            int item = findNextSlot();
            BlockPos block = findDownBlock();
            if (item == -1 || block == null) {
                return;
            }
            mc.player.inventory.currentItem = item;
            mc.player.rotationPitch = 90;
            mc.rightClickMouse();
        }
        super.onClickEvent(event);
    }

    int findNextSlot(){
        for(int i = 0;i < 9;i++){
            if(mc.player.inventory.mainInventory.get(i).getItem() instanceof BucketItem && ((BucketItem) mc.player.inventory.mainInventory.get(i).getItem()).containedBlock.getDefaultState().getBlockState() == Blocks.WATER.getDefaultState()){
                return i;
            }
        }
        return -1;
    }
    BlockPos findDownBlock(){
        for(int i = 0;i <= 6;i ++){
            BlockPos p = new BlockPos(mc.player.getPositionVector()).add(0, -i, 0);
            Block bl = mc.world.getBlockState(p).getBlock();
            if(!(bl instanceof AirBlock)){
                return p;
            }
        }
        return null;
    }
    @Override
    public void onUpdateEvent(UpdateEvent event) {
        super.onUpdateEvent(event);
    }
}
