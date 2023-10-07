// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.module.impl.player;

import java.util.HashMap;
import net.minecraft.util.MathHelper;
import net.minecraft.util.EnumFacing;
import net.minecraft.entity.player.EntityPlayer;
import exhibition.event.RegisterEvent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.BlockPos;
import exhibition.event.impl.EventMotion;
import exhibition.event.Event;
import exhibition.module.data.Setting;
import java.util.Arrays;
import net.minecraft.init.Blocks;
import exhibition.module.data.ModuleData;
import exhibition.util.Timer;
import net.minecraft.block.Block;
import java.util.List;
import exhibition.module.Module;

public class Scaffold extends Module
{
    private List<Block> blacklist;
    private BlockData blockData;
    Timer timer;
    Timer towerTimer;
    boolean isLooking;
    
    public Scaffold(final ModuleData data) {
        super(data);
        this.timer = new Timer();
        this.towerTimer = new Timer();
        this.blacklist = Arrays.asList(Blocks.air, Blocks.water, Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava);
        ((HashMap<String, Setting<Boolean>>)this.settings).put("TOWER", new Setting<Boolean>("TOWER", true, "Tower shit idk."));
    }
    
    @RegisterEvent(events = { EventMotion.class })
    @Override
    public void onEvent(final Event event) {
        final EventMotion em = (EventMotion)event;
        if (em.isPre()) {
            final BlockPos blockBelow;
            final Block block = Scaffold.mc.theWorld.getBlockState(blockBelow = new BlockPos(Scaffold.mc.thePlayer.posX, Scaffold.mc.thePlayer.posY - 1.0, Scaffold.mc.thePlayer.posZ)).getBlock();
            if (!Scaffold.mc.thePlayer.isSneaking() && (block == Blocks.air || block == Blocks.snow_layer || block == Blocks.tallgrass) && this.timer.delay(100.0f)) {
                this.timer.reset();
                this.blockData = this.getBlockData(blockBelow);
                if (this.blockData != null) {
                    final float[] rotations = getFacingRotations(this.blockData.position);
                    em.setYaw(rotations[0]);
                    em.setPitch(rotations[1]);
                }
            }
            for (int i = 36; i < 45; ++i) {
                if (Scaffold.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                    final ItemStack is = Scaffold.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                    final Item item = is.getItem();
                    if (item instanceof ItemBlock) {
                        if (this.blockData == null) {
                            return;
                        }
                        Scaffold.mc.rightClickDelayTimer = 2;
                        final int last = Scaffold.mc.thePlayer.inventory.currentItem;
                        Scaffold.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(i - 36));
                        Scaffold.mc.thePlayer.inventory.currentItem = i - 36;
                        Scaffold.mc.playerController.updateController();
                        if (Scaffold.mc.playerController.func_178890_a(Scaffold.mc.thePlayer, Scaffold.mc.theWorld, Scaffold.mc.thePlayer.inventory.getCurrentItem(), this.blockData.position, this.blockData.face, new Vec3(this.blockData.position.getX(), this.blockData.position.getY(), this.blockData.position.getZ()))) {
                            Scaffold.mc.thePlayer.swingItem();
                        }
                        Scaffold.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(Scaffold.mc.thePlayer.inventory.currentItem));
                        Scaffold.mc.thePlayer.inventory.currentItem = last;
                        Scaffold.mc.playerController.updateController();
                        break;
                    }
                }
            }
            this.blockData = null;
        }
        else {
            if (!Scaffold.mc.gameSettings.keyBindJump.getIsKeyPressed()) {
                this.towerTimer.reset();
            }
            if (this.blockData != null && ((HashMap<K, Setting<Boolean>>)this.settings).get("TOWER").getValue() && this.getBlockCount() > 0 && Scaffold.mc.gameSettings.keyBindJump.getIsKeyPressed()) {
                Scaffold.mc.thePlayer.motionX = 0.0;
                Scaffold.mc.thePlayer.motionZ = 0.0;
                Scaffold.mc.thePlayer.motionY = 0.42;
                if (this.towerTimer.delay(1500.0f)) {
                    Scaffold.mc.thePlayer.motionY = -0.28;
                    this.towerTimer.reset();
                }
            }
            for (int j = 36; j < 45; ++j) {
                if (Scaffold.mc.thePlayer.inventoryContainer.getSlot(j).getHasStack()) {
                    final ItemStack is2 = Scaffold.mc.thePlayer.inventoryContainer.getSlot(j).getStack();
                    final Item item2 = is2.getItem();
                    if (item2 instanceof ItemBlock && !this.blacklist.contains(((ItemBlock)item2).getBlock()) && !((ItemBlock)item2).getBlock().getLocalizedName().toLowerCase().contains("chest") && this.blockData != null) {
                        Scaffold.mc.rightClickDelayTimer = 2;
                        final int currentItem = Scaffold.mc.thePlayer.inventory.currentItem;
                        Scaffold.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(j - 36));
                        Scaffold.mc.thePlayer.inventory.currentItem = j - 36;
                        Scaffold.mc.playerController.updateController();
                        try {
                            if (Scaffold.mc.playerController.func_178890_a(Scaffold.mc.thePlayer, Scaffold.mc.theWorld, Scaffold.mc.thePlayer.inventory.getCurrentItem(), this.blockData.position, this.blockData.face, new Vec3(this.blockData.position.getX(), this.blockData.position.getY(), this.blockData.position.getZ()))) {
                                Scaffold.mc.thePlayer.swingItem();
                            }
                        }
                        catch (Exception ex) {}
                        Scaffold.mc.thePlayer.inventory.currentItem = currentItem;
                        Scaffold.mc.playerController.updateController();
                        return;
                    }
                }
            }
            if (this.invCheck()) {
                for (int j = 9; j < 36; ++j) {
                    if (Scaffold.mc.thePlayer.inventoryContainer.getSlot(j).getHasStack()) {
                        final Item item3 = Scaffold.mc.thePlayer.inventoryContainer.getSlot(j).getStack().getItem();
                        if (item3 instanceof ItemBlock && !this.blacklist.contains(((ItemBlock)item3).getBlock()) && !((ItemBlock)item3).getBlock().getLocalizedName().toLowerCase().contains("chest")) {
                            this.swap(j, 7);
                            break;
                        }
                    }
                }
            }
        }
    }
    
    public int getBlockCount() {
        int blockCount = 0;
        for (int i = 0; i < 45; ++i) {
            if (Scaffold.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is = Scaffold.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                final Item item = is.getItem();
                if (is.getItem() instanceof ItemBlock && !this.blacklist.contains(((ItemBlock)item).getBlock())) {
                    blockCount += is.stackSize;
                }
            }
        }
        return blockCount;
    }
    
    protected void swap(final int slot, final int hotbarNum) {
        Scaffold.mc.playerController.windowClick(Scaffold.mc.thePlayer.inventoryContainer.windowId, slot, hotbarNum, 2, Scaffold.mc.thePlayer);
    }
    
    private boolean invCheck() {
        for (int i = 36; i < 45; ++i) {
            if (Scaffold.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                final Item item = Scaffold.mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem();
                if (item instanceof ItemBlock && !this.blacklist.contains(((ItemBlock)item).getBlock())) {
                    return false;
                }
            }
        }
        return true;
    }
    
    private BlockData getBlockData(final BlockPos pos) {
        if (!this.blacklist.contains(Scaffold.mc.theWorld.getBlockState(pos.add(0, -1, 0)).getBlock())) {
            return new BlockData(pos.add(0, -1, 0), EnumFacing.UP);
        }
        if (!this.blacklist.contains(Scaffold.mc.theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock())) {
            return new BlockData(pos.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!this.blacklist.contains(Scaffold.mc.theWorld.getBlockState(pos.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!this.blacklist.contains(Scaffold.mc.theWorld.getBlockState(pos.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (!this.blacklist.contains(Scaffold.mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos.add(0, 0, 1), EnumFacing.NORTH);
        }
        final BlockPos add = pos.add(-1, 0, 0);
        if (!this.blacklist.contains(Scaffold.mc.theWorld.getBlockState(add.add(-1, 0, 0)).getBlock())) {
            return new BlockData(add.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!this.blacklist.contains(Scaffold.mc.theWorld.getBlockState(add.add(1, 0, 0)).getBlock())) {
            return new BlockData(add.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!this.blacklist.contains(Scaffold.mc.theWorld.getBlockState(add.add(0, 0, -1)).getBlock())) {
            return new BlockData(add.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (!this.blacklist.contains(Scaffold.mc.theWorld.getBlockState(add.add(0, 0, 1)).getBlock())) {
            return new BlockData(add.add(0, 0, 1), EnumFacing.NORTH);
        }
        final BlockPos add2 = pos.add(1, 0, 0);
        if (!this.blacklist.contains(Scaffold.mc.theWorld.getBlockState(add2.add(-1, 0, 0)).getBlock())) {
            return new BlockData(add2.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!this.blacklist.contains(Scaffold.mc.theWorld.getBlockState(add2.add(1, 0, 0)).getBlock())) {
            return new BlockData(add2.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!this.blacklist.contains(Scaffold.mc.theWorld.getBlockState(add2.add(0, 0, -1)).getBlock())) {
            return new BlockData(add2.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (!this.blacklist.contains(Scaffold.mc.theWorld.getBlockState(add2.add(0, 0, 1)).getBlock())) {
            return new BlockData(add2.add(0, 0, 1), EnumFacing.NORTH);
        }
        final BlockPos add3 = pos.add(0, 0, -1);
        if (!this.blacklist.contains(Scaffold.mc.theWorld.getBlockState(add3.add(-1, 0, 0)).getBlock())) {
            return new BlockData(add3.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!this.blacklist.contains(Scaffold.mc.theWorld.getBlockState(add3.add(1, 0, 0)).getBlock())) {
            return new BlockData(add3.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!this.blacklist.contains(Scaffold.mc.theWorld.getBlockState(add3.add(0, 0, -1)).getBlock())) {
            return new BlockData(add3.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (!this.blacklist.contains(Scaffold.mc.theWorld.getBlockState(add3.add(0, 0, 1)).getBlock())) {
            return new BlockData(add3.add(0, 0, 1), EnumFacing.NORTH);
        }
        final BlockPos add4 = pos.add(0, 0, 1);
        if (!this.blacklist.contains(Scaffold.mc.theWorld.getBlockState(add4.add(-1, 0, 0)).getBlock())) {
            return new BlockData(add4.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!this.blacklist.contains(Scaffold.mc.theWorld.getBlockState(add4.add(1, 0, 0)).getBlock())) {
            return new BlockData(add4.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!this.blacklist.contains(Scaffold.mc.theWorld.getBlockState(add4.add(0, 0, -1)).getBlock())) {
            return new BlockData(add4.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (!this.blacklist.contains(Scaffold.mc.theWorld.getBlockState(add4.add(0, 0, 1)).getBlock())) {
            return new BlockData(add4.add(0, 0, 1), EnumFacing.NORTH);
        }
        return null;
    }
    
    public static float[] getFacingRotations(final BlockPos pos) {
        final double d0 = pos.getX() - Scaffold.mc.thePlayer.posX;
        final double d2 = pos.getY() - (Scaffold.mc.thePlayer.posY + Scaffold.mc.thePlayer.getEyeHeight());
        final double d3 = pos.getZ() - Scaffold.mc.thePlayer.posZ;
        final double d4 = MathHelper.sqrt_double(d0 * d0 + d3 * d3);
        final float f = (float)(Math.atan2(d3, d0) * 180.0 / 3.141592653589793) - 90.0f;
        final float f2 = (float)(-(Math.atan2(d2, d4) * 180.0 / 3.141592653589793));
        return new float[] { f, f2 };
    }
    
    private class BlockData
    {
        public BlockPos position;
        public EnumFacing face;
        
        public BlockData(final BlockPos position, final EnumFacing face) {
            this.position = position;
            this.face = face;
        }
    }
}
