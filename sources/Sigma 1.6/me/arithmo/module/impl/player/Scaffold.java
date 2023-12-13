/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.module.impl.player;

import java.util.Arrays;
import java.util.List;
import me.arithmo.event.Event;
import me.arithmo.event.RegisterEvent;
import me.arithmo.event.impl.EventMotion;
import me.arithmo.event.impl.EventPacket;
import me.arithmo.event.impl.EventRenderGui;
import me.arithmo.module.Module;
import me.arithmo.module.data.ModuleData;
import me.arithmo.module.data.Setting;
import me.arithmo.module.data.SettingsMap;
import me.arithmo.util.Timer;
import me.arithmo.util.render.Colors;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockDynamicLiquid;
import net.minecraft.block.BlockStainedGlassPane;
import net.minecraft.block.BlockStaticLiquid;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;
import net.minecraft.world.World;

public class Scaffold
extends Module {
    private List<Block> blacklist;
    private BlockData blockData;
    private Timer timer = new Timer();
    private Timer timerMotion = new Timer();
    private String TOWER = "TOWER";

    public Scaffold(ModuleData data) {
        super(data);
        this.settings.put(this.TOWER, new Setting<Boolean>(this.TOWER, true, "Helps you build up faster."));
        this.blacklist = Arrays.asList(Blocks.air, Blocks.water, Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava, Blocks.enchanting_table, Blocks.carpet, Blocks.glass_pane, Blocks.stained_glass_pane, Blocks.iron_bars, Blocks.chest, Blocks.torch, Blocks.anvil);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.timerMotion.reset();
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @RegisterEvent(events={EventPacket.class, EventMotion.class, EventRenderGui.class})
    public void onEvent(Event event) {
        if (event instanceof EventRenderGui) {
            ScaledResolution res = new ScaledResolution(mc, Scaffold.mc.displayWidth, Scaffold.mc.displayHeight);
            int color = Colors.getColor(255, 0, 0);
            if (this.getBlockCount() > 128 && 256 > this.getBlockCount()) {
                color = Colors.getColor(255, 255, 0);
            } else if (this.getBlockCount() > 256) {
                color = Colors.getColor(0, 255, 0);
            }
            Scaffold.mc.fontRendererObj.drawStringWithShadow(this.getSuffix() + "", res.getScaledWidth() / 2 - Scaffold.mc.fontRendererObj.getStringWidth("" + this.getBlockCount() + ""), res.getScaledHeight() / 2 - 10, color);
        }
        if (!(event instanceof EventMotion)) return;
        EventMotion em = (EventMotion)event;
        if (em.isPre()) {
            BlockPos blockBelow;
            this.setSuffix("" + this.getBlockCount() + "");
            this.blockData = null;
            double x = Scaffold.mc.thePlayer.posX;
            double y = Scaffold.mc.thePlayer.posY - 1.0;
            double z = Scaffold.mc.thePlayer.posZ;
            double forward = Scaffold.mc.thePlayer.movementInput.moveForward;
            double strafe = Scaffold.mc.thePlayer.movementInput.moveStrafe;
            float yaw = Scaffold.mc.thePlayer.rotationYaw;
            if (Scaffold.mc.thePlayer.isSneaking() || Scaffold.mc.theWorld.getBlockState(blockBelow = new BlockPos(x, y, z)).getBlock() != Blocks.air && Scaffold.mc.theWorld.getBlockState(blockBelow = new BlockPos(x, y, z)).getBlock() != Blocks.snow_layer && Scaffold.mc.theWorld.getBlockState(blockBelow = new BlockPos(x += forward * 0.4 * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * 0.45 * Math.sin(Math.toRadians(yaw + 90.0f)), y, z += forward * 0.4 * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * 0.45 * Math.cos(Math.toRadians(yaw + 90.0f)))).getBlock() != Blocks.tallgrass || !this.timer.delay(100.0f)) return;
            this.timer.reset();
            this.blockData = this.getBlockData(blockBelow);
            if (this.blockData == null) return;
            float[] rotations = this.getFacingRotations(this.blockData.position.getX(), this.blockData.position.getY(), this.blockData.position.getZ(), this.blockData.face);
            em.setYaw(rotations[0]);
            em.setPitch(rotations[1]);
            return;
        } else {
            int i;
            if (!em.isPost()) return;
            if (!Scaffold.mc.gameSettings.keyBindJump.getIsKeyPressed()) {
                this.timerMotion.reset();
            }
            if (this.blockData != null && ((Boolean)((Setting)this.settings.get(this.TOWER)).getValue()).booleanValue() && this.getBlockCount() > 0) {
                Scaffold.mc.rightClickDelayTimer = 0;
                if (Scaffold.mc.gameSettings.keyBindJump.getIsKeyPressed()) {
                    Scaffold.mc.thePlayer.motionX *= 0.3;
                    Scaffold.mc.thePlayer.motionZ *= 0.3;
                    Scaffold.mc.thePlayer.motionY = 0.42;
                    if (this.timerMotion.delay(1500.0f)) {
                        Scaffold.mc.thePlayer.motionY = -0.28;
                        this.timerMotion.reset();
                    }
                }
            }
            if (this.isHotbarEmpty()) {
                for (i = 9; i < 36; ++i) {
                    Item item;
                    if (!Scaffold.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() || !((item = Scaffold.mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem()) instanceof ItemBlock) || this.blacklist.contains(((ItemBlock)item).getBlock())) continue;
                    this.swap(i, 7);
                    break;
                }
            }
            i = 36;
            while (i < 45) {
                Item item;
                ItemStack is;
                if (Scaffold.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() && (item = (is = Scaffold.mc.thePlayer.inventoryContainer.getSlot(i).getStack()).getItem()) instanceof ItemBlock && !this.blacklist.contains(((ItemBlock)item).getBlock()) && this.blockData != null) {
                    Scaffold.mc.rightClickDelayTimer = 2;
                    int last = Scaffold.mc.thePlayer.inventory.currentItem;
                    Scaffold.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(i - 36));
                    Scaffold.mc.thePlayer.inventory.currentItem = i - 36;
                    Scaffold.mc.playerController.updateController();
                    if (Scaffold.mc.playerController.func_178890_a(Scaffold.mc.thePlayer, Scaffold.mc.theWorld, Scaffold.mc.thePlayer.inventory.getCurrentItem(), this.blockData.position, this.blockData.face, new Vec3(this.blockData.position.getX(), this.blockData.position.getY(), this.blockData.position.getZ()))) {
                        Scaffold.mc.thePlayer.swingItem();
                    }
                    Scaffold.mc.thePlayer.inventory.currentItem = last;
                    Scaffold.mc.playerController.updateController();
                    return;
                }
                ++i;
            }
            return;
        }
    }

    protected void swap(int slot, int hotbarNum) {
        Scaffold.mc.playerController.windowClick(Scaffold.mc.thePlayer.inventoryContainer.windowId, slot, hotbarNum, 2, Scaffold.mc.thePlayer);
    }

    private boolean isHotbarEmpty() {
        for (int i = 36; i < 45; ++i) {
            Item item;
            if (!Scaffold.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() || !((item = Scaffold.mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem()) instanceof ItemBlock) || this.blacklist.contains(((ItemBlock)item).getBlock())) continue;
            return false;
        }
        return true;
    }

    private BlockData getBlockData(BlockPos pos) {
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
        BlockPos add = pos.add(-1, 0, 0);
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
        BlockPos add2 = pos.add(1, 0, 0);
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
        BlockPos add3 = pos.add(0, 0, -1);
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
        BlockPos add4 = pos.add(0, 0, 1);
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

    private float[] getFacingRotations(int paramInt1, int paramInt2, int paramInt3, EnumFacing paramEnumFacing) {
        EntityPig localEntityPig = new EntityPig(Scaffold.mc.theWorld);
        localEntityPig.posX = (double)paramInt1 + 0.5;
        localEntityPig.posY = (double)paramInt2 + 0.5;
        localEntityPig.posZ = (double)paramInt3 + 0.5;
        localEntityPig.posX += (double)paramEnumFacing.getDirectionVec().getX() * 0.25;
        localEntityPig.posY += (double)paramEnumFacing.getDirectionVec().getY() * 0.25;
        localEntityPig.posZ += (double)paramEnumFacing.getDirectionVec().getZ() * 0.25;
        return Scaffold.jdMethod_double(localEntityPig);
    }

    private static float[] jdMethod_double(EntityLivingBase paramEntityLivingBase) {
        double d1 = paramEntityLivingBase.posX - Scaffold.mc.thePlayer.posX;
        double d2 = paramEntityLivingBase.posY + (double)paramEntityLivingBase.getEyeHeight() - (Scaffold.mc.thePlayer.posY + (double)Scaffold.mc.thePlayer.getEyeHeight());
        double d3 = paramEntityLivingBase.posZ - Scaffold.mc.thePlayer.posZ;
        double d4 = MathHelper.sqrt_double(d1 * d1 + d3 * d3);
        float f1 = (float)(Math.atan2(d3, d1) * 180.0 / 3.141592653589793) - 90.0f;
        float f2 = (float)(- Math.atan2(d2, d4) * 180.0 / 3.141592653589793);
        return new float[]{f1, f2};
    }

    public int getBlockCount() {
        int blockCount = 0;
        for (int i = 0; i < 45; ++i) {
            if (!Scaffold.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) continue;
            ItemStack is = Scaffold.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            Item item = is.getItem();
            if (!(is.getItem() instanceof ItemBlock) || this.blacklist.contains(((ItemBlock)item).getBlock())) continue;
            blockCount += is.stackSize;
        }
        return blockCount;
    }

    private class BlockData {
        public BlockPos position;
        public EnumFacing face;

        private BlockData(BlockPos position, EnumFacing face) {
            this.position = position;
            this.face = face;
        }
    }

}

