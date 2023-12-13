package me.earth.phobos.features.modules.combat;

import com.google.common.util.concurrent.AtomicDouble;
import me.earth.phobos.Phobos;
import me.earth.phobos.event.events.PacketEvent;
import me.earth.phobos.event.events.UpdateWalkingPlayerEvent;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.modules.client.ClickGui;
import me.earth.phobos.features.modules.client.ServerModule;
import me.earth.phobos.features.setting.Setting;
import me.earth.phobos.util.*;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockWorkbench;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBed;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class BedBomb
        extends Module {
    private final Setting<Boolean> server = this.register(new Setting<Boolean>("Server", false));
    private final Setting<Boolean> place = this.register(new Setting<Boolean>("Place", false));
    private final Setting<Integer> placeDelay = this.register(new Setting<Object>("Placedelay", Integer.valueOf(50), Integer.valueOf(0), Integer.valueOf(500), v -> this.place.getValue()));
    private final Setting<Float> placeRange = this.register(new Setting<Object>("PlaceRange", Float.valueOf(6.0f), Float.valueOf(1.0f), Float.valueOf(10.0f), v -> this.place.getValue()));
    private final Setting<Boolean> extraPacket = this.register(new Setting<Object>("InsanePacket", Boolean.valueOf(false), v -> this.place.getValue()));
    private final Setting<Boolean> packet = this.register(new Setting<Object>("Packet", Boolean.valueOf(false), v -> this.place.getValue()));
    private final Setting<Boolean> explode = this.register(new Setting<Boolean>("Break", true));
    private final Setting<BreakLogic> breakMode = this.register(new Setting<Object>("BreakMode", BreakLogic.ALL, v -> this.explode.getValue()));
    private final Setting<Integer> breakDelay = this.register(new Setting<Object>("Breakdelay", Integer.valueOf(50), Integer.valueOf(0), Integer.valueOf(500), v -> this.explode.getValue()));
    private final Setting<Float> breakRange = this.register(new Setting<Object>("BreakRange", Float.valueOf(6.0f), Float.valueOf(1.0f), Float.valueOf(10.0f), v -> this.explode.getValue()));
    private final Setting<Float> minDamage = this.register(new Setting<Object>("MinDamage", Float.valueOf(5.0f), Float.valueOf(1.0f), Float.valueOf(36.0f), v -> this.explode.getValue()));
    private final Setting<Float> range = this.register(new Setting<Object>("Range", Float.valueOf(10.0f), Float.valueOf(1.0f), Float.valueOf(12.0f), v -> this.explode.getValue()));
    private final Setting<Boolean> suicide = this.register(new Setting<Object>("Suicide", Boolean.valueOf(false), v -> this.explode.getValue()));
    private final Setting<Boolean> removeTiles = this.register(new Setting<Boolean>("RemoveTiles", false));
    private final Setting<Boolean> rotate = this.register(new Setting<Boolean>("Rotate", false));
    private final Setting<Boolean> oneDot15 = this.register(new Setting<Boolean>("1.15", false));
    private final Setting<Logic> logic = this.register(new Setting<Object>("Logic", Logic.BREAKPLACE, v -> this.place.getValue() != false && this.explode.getValue() != false));
    private final Setting<Boolean> craft = this.register(new Setting<Boolean>("Craft", false));
    private final Setting<Boolean> placeCraftingTable = this.register(new Setting<Object>("PlaceTable", Boolean.valueOf(false), v -> this.craft.getValue()));
    private final Setting<Boolean> openCraftingTable = this.register(new Setting<Object>("OpenTable", Boolean.valueOf(false), v -> this.craft.getValue()));
    private final Setting<Boolean> craftTable = this.register(new Setting<Object>("CraftTable", Boolean.valueOf(false), v -> this.craft.getValue()));
    private final Setting<Float> tableRange = this.register(new Setting<Object>("TableRange", Float.valueOf(6.0f), Float.valueOf(1.0f), Float.valueOf(10.0f), v -> this.craft.getValue()));
    private final Setting<Integer> craftDelay = this.register(new Setting<Object>("CraftDelay", Integer.valueOf(4), Integer.valueOf(1), Integer.valueOf(10), v -> this.craft.getValue()));
    private final Setting<Integer> tableSlot = this.register(new Setting<Object>("TableSlot", Integer.valueOf(8), Integer.valueOf(0), Integer.valueOf(8), v -> this.craft.getValue()));
    private final Setting<Boolean> sslot = this.register(new Setting<Boolean>("S-Slot", false));
    private final Timer breakTimer = new Timer();
    private final Timer placeTimer = new Timer();
    private final Timer craftTimer = new Timer();
    private final AtomicDouble yaw = new AtomicDouble(-1.0);
    private final AtomicDouble pitch = new AtomicDouble(-1.0);
    private final AtomicBoolean shouldRotate = new AtomicBoolean(false);
    private EntityPlayer target = null;
    private boolean sendRotationPacket = false;
    private boolean one;
    private boolean two;
    private boolean three;
    private boolean four;
    private boolean five;
    private boolean six;
    private boolean seven;
    private boolean eight;
    private boolean nine;
    private boolean ten;
    private BlockPos maxPos = null;
    private boolean shouldCraft;
    private int craftStage = 0;
    private final int lastCraftStage = -1;
    private int lastHotbarSlot = -1;
    private int bedSlot = -1;
    private BlockPos finalPos;
    private EnumFacing finalFacing;

    public BedBomb() {
        super("BedBomb", "AutoPlace and Break for beds", Module.Category.COMBAT, true, false, false);
    }

    @Override
    public void onEnable() {
        if (!BedBomb.fullNullCheck() && this.shouldServer()) {
            BedBomb.mc.player.connection.sendPacket(new CPacketChatMessage("@Serverprefix" + ClickGui.getInstance().prefix.getValue()));
            BedBomb.mc.player.connection.sendPacket(new CPacketChatMessage("@Server" + ClickGui.getInstance().prefix.getValue() + "module BedBomb set Enabled true"));
        }
    }

    @Override
    public void onDisable() {
        if (!BedBomb.fullNullCheck() && this.shouldServer()) {
            BedBomb.mc.player.connection.sendPacket(new CPacketChatMessage("@Serverprefix" + ClickGui.getInstance().prefix.getValue()));
            BedBomb.mc.player.connection.sendPacket(new CPacketChatMessage("@Server" + ClickGui.getInstance().prefix.getValue() + "module BedBomb set Enabled false"));
            if (this.sslot.getValue().booleanValue()) {
                BedBomb.mc.player.connection.sendPacket(new CPacketHeldItemChange(BedBomb.mc.player.inventory.currentItem));
            }
        }
    }

    @SubscribeEvent
    public void onPacket(PacketEvent.Send event) {
        if (this.shouldRotate.get() && event.getPacket() instanceof CPacketPlayer) {
            CPacketPlayer packet = event.getPacket();
            packet.yaw = (float) this.yaw.get();
            packet.pitch = (float) this.pitch.get();
            this.shouldRotate.set(false);
        }
    }

    private boolean shouldServer() {
        return ServerModule.getInstance().isConnected() && this.server.getValue() != false;
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
        if (BedBomb.fullNullCheck() || BedBomb.mc.player.dimension != -1 && BedBomb.mc.player.dimension != 1 || this.shouldServer()) {
            return;
        }
        if (event.getStage() == 0) {
            this.doBedBomb();
            if (this.shouldCraft && BedBomb.mc.currentScreen instanceof GuiCrafting) {
                int woolSlot = InventoryUtil.findInventoryWool(false);
                int woodSlot = InventoryUtil.findInventoryBlock(BlockPlanks.class, true);
                if (woolSlot == -1 || woodSlot == -1) {
                    mc.displayGuiScreen(null);
                    BedBomb.mc.currentScreen = null;
                    this.shouldCraft = false;
                    return;
                }
                if (this.craftStage > 1 && !this.one) {
                    BedBomb.mc.playerController.windowClick(((GuiContainer) BedBomb.mc.currentScreen).inventorySlots.windowId, woolSlot, 0, ClickType.PICKUP, BedBomb.mc.player);
                    BedBomb.mc.playerController.windowClick(((GuiContainer) BedBomb.mc.currentScreen).inventorySlots.windowId, 1, 1, ClickType.PICKUP, BedBomb.mc.player);
                    BedBomb.mc.playerController.windowClick(((GuiContainer) BedBomb.mc.currentScreen).inventorySlots.windowId, woolSlot, 0, ClickType.PICKUP, BedBomb.mc.player);
                    this.one = true;
                } else if (this.craftStage > 1 + this.craftDelay.getValue() && !this.two) {
                    BedBomb.mc.playerController.windowClick(((GuiContainer) BedBomb.mc.currentScreen).inventorySlots.windowId, woolSlot, 0, ClickType.PICKUP, BedBomb.mc.player);
                    BedBomb.mc.playerController.windowClick(((GuiContainer) BedBomb.mc.currentScreen).inventorySlots.windowId, 2, 1, ClickType.PICKUP, BedBomb.mc.player);
                    BedBomb.mc.playerController.windowClick(((GuiContainer) BedBomb.mc.currentScreen).inventorySlots.windowId, woolSlot, 0, ClickType.PICKUP, BedBomb.mc.player);
                    this.two = true;
                } else if (this.craftStage > 1 + this.craftDelay.getValue() * 2 && !this.three) {
                    BedBomb.mc.playerController.windowClick(((GuiContainer) BedBomb.mc.currentScreen).inventorySlots.windowId, woolSlot, 0, ClickType.PICKUP, BedBomb.mc.player);
                    BedBomb.mc.playerController.windowClick(((GuiContainer) BedBomb.mc.currentScreen).inventorySlots.windowId, 3, 1, ClickType.PICKUP, BedBomb.mc.player);
                    BedBomb.mc.playerController.windowClick(((GuiContainer) BedBomb.mc.currentScreen).inventorySlots.windowId, woolSlot, 0, ClickType.PICKUP, BedBomb.mc.player);
                    this.three = true;
                } else if (this.craftStage > 1 + this.craftDelay.getValue() * 3 && !this.four) {
                    BedBomb.mc.playerController.windowClick(((GuiContainer) BedBomb.mc.currentScreen).inventorySlots.windowId, woodSlot, 0, ClickType.PICKUP, BedBomb.mc.player);
                    BedBomb.mc.playerController.windowClick(((GuiContainer) BedBomb.mc.currentScreen).inventorySlots.windowId, 4, 1, ClickType.PICKUP, BedBomb.mc.player);
                    BedBomb.mc.playerController.windowClick(((GuiContainer) BedBomb.mc.currentScreen).inventorySlots.windowId, woodSlot, 0, ClickType.PICKUP, BedBomb.mc.player);
                    this.four = true;
                } else if (this.craftStage > 1 + this.craftDelay.getValue() * 4 && !this.five) {
                    BedBomb.mc.playerController.windowClick(((GuiContainer) BedBomb.mc.currentScreen).inventorySlots.windowId, woodSlot, 0, ClickType.PICKUP, BedBomb.mc.player);
                    BedBomb.mc.playerController.windowClick(((GuiContainer) BedBomb.mc.currentScreen).inventorySlots.windowId, 5, 1, ClickType.PICKUP, BedBomb.mc.player);
                    BedBomb.mc.playerController.windowClick(((GuiContainer) BedBomb.mc.currentScreen).inventorySlots.windowId, woodSlot, 0, ClickType.PICKUP, BedBomb.mc.player);
                    this.five = true;
                } else if (this.craftStage > 1 + this.craftDelay.getValue() * 5 && !this.six) {
                    BedBomb.mc.playerController.windowClick(((GuiContainer) BedBomb.mc.currentScreen).inventorySlots.windowId, woodSlot, 0, ClickType.PICKUP, BedBomb.mc.player);
                    BedBomb.mc.playerController.windowClick(((GuiContainer) BedBomb.mc.currentScreen).inventorySlots.windowId, 6, 1, ClickType.PICKUP, BedBomb.mc.player);
                    BedBomb.mc.playerController.windowClick(((GuiContainer) BedBomb.mc.currentScreen).inventorySlots.windowId, woodSlot, 0, ClickType.PICKUP, BedBomb.mc.player);
                    this.recheckBedSlots(woolSlot, woodSlot);
                    BedBomb.mc.playerController.windowClick(((GuiContainer) BedBomb.mc.currentScreen).inventorySlots.windowId, 0, 0, ClickType.QUICK_MOVE, BedBomb.mc.player);
                    this.six = true;
                    this.one = false;
                    this.two = false;
                    this.three = false;
                    this.four = false;
                    this.five = false;
                    this.six = false;
                    this.craftStage = -2;
                    this.shouldCraft = false;
                }
                ++this.craftStage;
            }
        } else if (event.getStage() == 1 && this.finalPos != null) {
            Vec3d hitVec = new Vec3d(this.finalPos.down()).add(0.5, 0.5, 0.5).add(new Vec3d(this.finalFacing.getOpposite().getDirectionVec()).scale(0.5));
            BedBomb.mc.player.connection.sendPacket(new CPacketEntityAction(BedBomb.mc.player, CPacketEntityAction.Action.START_SNEAKING));
            InventoryUtil.switchToHotbarSlot(this.bedSlot, false);
            BlockUtil.rightClickBlock(this.finalPos.down(), hitVec, this.bedSlot == -2 ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, EnumFacing.UP, this.packet.getValue());
            BedBomb.mc.player.connection.sendPacket(new CPacketEntityAction(BedBomb.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
            this.placeTimer.reset();
            this.finalPos = null;
        }
    }

    public void recheckBedSlots(int woolSlot, int woodSlot) {
        int i;
        for (i = 1; i <= 3; ++i) {
            if (BedBomb.mc.player.openContainer.getInventory().get(i) != ItemStack.EMPTY) continue;
            BedBomb.mc.playerController.windowClick(1, woolSlot, 0, ClickType.PICKUP, BedBomb.mc.player);
            BedBomb.mc.playerController.windowClick(1, i, 1, ClickType.PICKUP, BedBomb.mc.player);
            BedBomb.mc.playerController.windowClick(1, woolSlot, 0, ClickType.PICKUP, BedBomb.mc.player);
        }
        for (i = 4; i <= 6; ++i) {
            if (BedBomb.mc.player.openContainer.getInventory().get(i) != ItemStack.EMPTY) continue;
            BedBomb.mc.playerController.windowClick(1, woodSlot, 0, ClickType.PICKUP, BedBomb.mc.player);
            BedBomb.mc.playerController.windowClick(1, i, 1, ClickType.PICKUP, BedBomb.mc.player);
            BedBomb.mc.playerController.windowClick(1, woodSlot, 0, ClickType.PICKUP, BedBomb.mc.player);
        }
    }

    public void incrementCraftStage() {
        if (this.craftTimer.passedMs(this.craftDelay.getValue().intValue())) {
            ++this.craftStage;
            if (this.craftStage > 9) {
                this.craftStage = 0;
            }
            this.craftTimer.reset();
        }
    }

    private void doBedBomb() {
        switch (this.logic.getValue()) {
            case BREAKPLACE: {
                this.mapBeds();
                this.breakBeds();
                this.placeBeds();
                break;
            }
            case PLACEBREAK: {
                this.mapBeds();
                this.placeBeds();
                this.breakBeds();
            }
        }
    }

    private void breakBeds() {
        if (this.explode.getValue().booleanValue() && this.breakTimer.passedMs(this.breakDelay.getValue().intValue())) {
            if (this.breakMode.getValue() == BreakLogic.CALC) {
                if (this.maxPos != null) {
                    RayTraceResult result;
                    Vec3d hitVec = new Vec3d(this.maxPos).add(0.5, 0.5, 0.5);
                    float[] rotations = RotationUtil.getLegitRotations(hitVec);
                    this.yaw.set(rotations[0]);
                    if (this.rotate.getValue().booleanValue()) {
                        this.shouldRotate.set(true);
                        this.pitch.set(rotations[1]);
                    }
                    EnumFacing facing = (result = BedBomb.mc.world.rayTraceBlocks(new Vec3d(BedBomb.mc.player.posX, BedBomb.mc.player.posY + (double) BedBomb.mc.player.getEyeHeight(), BedBomb.mc.player.posZ), new Vec3d((double) this.maxPos.getX() + 0.5, (double) this.maxPos.getY() - 0.5, (double) this.maxPos.getZ() + 0.5))) == null || result.sideHit == null ? EnumFacing.UP : result.sideHit;
                    BlockUtil.rightClickBlock(this.maxPos, hitVec, EnumHand.MAIN_HAND, facing, true);
                    this.breakTimer.reset();
                }
            } else {
                for (TileEntity entityBed : BedBomb.mc.world.loadedTileEntityList) {
                    RayTraceResult result;
                    if (!(entityBed instanceof TileEntityBed) || BedBomb.mc.player.getDistanceSq(entityBed.getPos()) > MathUtil.square(this.breakRange.getValue().floatValue()))
                        continue;
                    Vec3d hitVec = new Vec3d(entityBed.getPos()).add(0.5, 0.5, 0.5);
                    float[] rotations = RotationUtil.getLegitRotations(hitVec);
                    this.yaw.set(rotations[0]);
                    if (this.rotate.getValue().booleanValue()) {
                        this.shouldRotate.set(true);
                        this.pitch.set(rotations[1]);
                    }
                    EnumFacing facing = (result = BedBomb.mc.world.rayTraceBlocks(new Vec3d(BedBomb.mc.player.posX, BedBomb.mc.player.posY + (double) BedBomb.mc.player.getEyeHeight(), BedBomb.mc.player.posZ), new Vec3d((double) entityBed.getPos().getX() + 0.5, (double) entityBed.getPos().getY() - 0.5, (double) entityBed.getPos().getZ() + 0.5))) == null || result.sideHit == null ? EnumFacing.UP : result.sideHit;
                    BlockUtil.rightClickBlock(entityBed.getPos(), hitVec, EnumHand.MAIN_HAND, facing, true);
                    this.breakTimer.reset();
                }
            }
        }
    }

    private void mapBeds() {
        this.maxPos = null;
        float maxDamage = 0.5f;
        if (this.removeTiles.getValue().booleanValue()) {
            ArrayList<BedData> removedBlocks = new ArrayList<BedData>();
            for (TileEntity tile : BedBomb.mc.world.loadedTileEntityList) {
                if (!(tile instanceof TileEntityBed)) continue;
                TileEntityBed bed = (TileEntityBed) tile;
                BedData data = new BedData(tile.getPos(), BedBomb.mc.world.getBlockState(tile.getPos()), bed, bed.isHeadPiece());
                removedBlocks.add(data);
            }
            for (BedData data : removedBlocks) {
                BedBomb.mc.world.setBlockToAir(data.getPos());
            }
            for (BedData data : removedBlocks) {
                float selfDamage;
                BlockPos pos;
                if (!data.isHeadPiece() || !(BedBomb.mc.player.getDistanceSq(pos = data.getPos()) <= MathUtil.square(this.breakRange.getValue().floatValue())) || !((double) (selfDamage = DamageUtil.calculateDamage(pos, BedBomb.mc.player)) + 1.0 < (double) EntityUtil.getHealth(BedBomb.mc.player)) && DamageUtil.canTakeDamage(this.suicide.getValue()))
                    continue;
                for (EntityPlayer player : BedBomb.mc.world.playerEntities) {
                    float damage;
                    if (!(player.getDistanceSq(pos) < MathUtil.square(this.range.getValue().floatValue())) || !EntityUtil.isValid(player, this.range.getValue().floatValue() + this.breakRange.getValue().floatValue()) || !((damage = DamageUtil.calculateDamage(pos, player)) > selfDamage || damage > this.minDamage.getValue().floatValue() && !DamageUtil.canTakeDamage(this.suicide.getValue())) && !(damage > EntityUtil.getHealth(player)) || !(damage > maxDamage))
                        continue;
                    maxDamage = damage;
                    this.maxPos = pos;
                }
            }
            for (BedData data : removedBlocks) {
                BedBomb.mc.world.setBlockState(data.getPos(), data.getState());
            }
        } else {
            for (TileEntity tile : BedBomb.mc.world.loadedTileEntityList) {
                float selfDamage;
                BlockPos pos;
                TileEntityBed bed;
                if (!(tile instanceof TileEntityBed) || !(bed = (TileEntityBed) tile).isHeadPiece() || !(BedBomb.mc.player.getDistanceSq(pos = bed.getPos()) <= MathUtil.square(this.breakRange.getValue().floatValue())) || !((double) (selfDamage = DamageUtil.calculateDamage(pos, BedBomb.mc.player)) + 1.0 < (double) EntityUtil.getHealth(BedBomb.mc.player)) && DamageUtil.canTakeDamage(this.suicide.getValue()))
                    continue;
                for (EntityPlayer player : BedBomb.mc.world.playerEntities) {
                    float damage;
                    if (!(player.getDistanceSq(pos) < MathUtil.square(this.range.getValue().floatValue())) || !EntityUtil.isValid(player, this.range.getValue().floatValue() + this.breakRange.getValue().floatValue()) || !((damage = DamageUtil.calculateDamage(pos, player)) > selfDamage || damage > this.minDamage.getValue().floatValue() && !DamageUtil.canTakeDamage(this.suicide.getValue())) && !(damage > EntityUtil.getHealth(player)) || !(damage > maxDamage))
                        continue;
                    maxDamage = damage;
                    this.maxPos = pos;
                }
            }
        }
    }

    private void placeBeds() {
        if (this.place.getValue().booleanValue() && this.placeTimer.passedMs(this.placeDelay.getValue().intValue()) && this.maxPos == null) {
            this.bedSlot = this.findBedSlot();
            if (this.bedSlot == -1) {
                if (BedBomb.mc.player.getHeldItemOffhand().getItem() == Items.BED) {
                    this.bedSlot = -2;
                } else {
                    if (this.craft.getValue().booleanValue() && !this.shouldCraft && EntityUtil.getClosestEnemy(this.placeRange.getValue().floatValue()) != null) {
                        this.doBedCraft();
                    }
                    return;
                }
            }
            this.lastHotbarSlot = BedBomb.mc.player.inventory.currentItem;
            this.target = EntityUtil.getClosestEnemy(this.placeRange.getValue().floatValue());
            if (this.target != null) {
                BlockPos targetPos = new BlockPos(this.target.getPositionVector());
                this.placeBed(targetPos, true);
                if (this.craft.getValue().booleanValue()) {
                    this.doBedCraft();
                }
            }
        }
    }

    private void placeBed(BlockPos pos, boolean firstCheck) {
        if (BedBomb.mc.world.getBlockState(pos).getBlock() == Blocks.BED) {
            return;
        }
        float damage = DamageUtil.calculateDamage(pos, BedBomb.mc.player);
        if ((double) damage > (double) EntityUtil.getHealth(BedBomb.mc.player) + 0.5) {
            if (firstCheck && this.oneDot15.getValue().booleanValue()) {
                this.placeBed(pos.up(), false);
            }
            return;
        }
        if (!BedBomb.mc.world.getBlockState(pos).getMaterial().isReplaceable()) {
            if (firstCheck && this.oneDot15.getValue().booleanValue()) {
                this.placeBed(pos.up(), false);
            }
            return;
        }
        ArrayList<BlockPos> positions = new ArrayList<BlockPos>();
        HashMap<BlockPos, EnumFacing> facings = new HashMap<BlockPos, EnumFacing>();
        for (EnumFacing facing : EnumFacing.values()) {
            BlockPos position;
            if (facing == EnumFacing.DOWN || facing == EnumFacing.UP || !(BedBomb.mc.player.getDistanceSq(position = pos.offset(facing)) <= MathUtil.square(this.placeRange.getValue().floatValue())) || !BedBomb.mc.world.getBlockState(position).getMaterial().isReplaceable() || BedBomb.mc.world.getBlockState(position.down()).getMaterial().isReplaceable())
                continue;
            positions.add(position);
            facings.put(position, facing.getOpposite());
        }
        if (positions.isEmpty()) {
            if (firstCheck && this.oneDot15.getValue().booleanValue()) {
                this.placeBed(pos.up(), false);
            }
            return;
        }
        positions.sort(Comparator.comparingDouble(pos2 -> BedBomb.mc.player.getDistanceSq(pos2)));
        this.finalPos = positions.get(0);
        this.finalFacing = facings.get(this.finalPos);
        float[] rotation = RotationUtil.simpleFacing(this.finalFacing);
        if (!this.sendRotationPacket && this.extraPacket.getValue().booleanValue()) {
            RotationUtil.faceYawAndPitch(rotation[0], rotation[1]);
            this.sendRotationPacket = true;
        }
        this.yaw.set(rotation[0]);
        this.pitch.set(rotation[1]);
        this.shouldRotate.set(true);
        Phobos.rotationManager.setPlayerRotations(rotation[0], rotation[1]);
    }

    @Override
    public String getDisplayInfo() {
        if (this.target != null) {
            return this.target.getName();
        }
        return null;
    }

    public void doBedCraft() {
        BlockPos target;
        List targets;
        int woolSlot = InventoryUtil.findInventoryWool(false);
        int woodSlot = InventoryUtil.findInventoryBlock(BlockPlanks.class, true);
        if (woolSlot == -1 || woodSlot == -1) {
            if (BedBomb.mc.currentScreen instanceof GuiCrafting) {
                mc.displayGuiScreen(null);
                BedBomb.mc.currentScreen = null;
            }
            return;
        }
        if (this.placeCraftingTable.getValue().booleanValue() && BlockUtil.getBlockSphere(this.tableRange.getValue().floatValue() - 1.0f, BlockWorkbench.class).size() == 0 && !(targets = BlockUtil.getSphere(EntityUtil.getPlayerPos(BedBomb.mc.player), this.tableRange.getValue().floatValue(), this.tableRange.getValue().intValue(), false, true, 0).stream().filter(pos -> BlockUtil.isPositionPlaceable(pos, false) == 3).sorted(Comparator.comparingInt(pos -> -this.safety(pos))).collect(Collectors.toList())).isEmpty()) {
            target = (BlockPos) targets.get(0);
            int tableSlot = InventoryUtil.findHotbarBlock(BlockWorkbench.class);
            if (tableSlot != -1) {
                BedBomb.mc.player.inventory.currentItem = tableSlot;
                BlockUtil.placeBlock(target, EnumHand.MAIN_HAND, this.rotate.getValue(), true, false);
            } else {
                if (this.craftTable.getValue().booleanValue()) {
                    this.craftTable();
                }
                if ((tableSlot = InventoryUtil.findHotbarBlock(BlockWorkbench.class)) != -1) {
                    BedBomb.mc.player.inventory.currentItem = tableSlot;
                    BlockUtil.placeBlock(target, EnumHand.MAIN_HAND, this.rotate.getValue(), true, false);
                }
            }
        }
        if (this.openCraftingTable.getValue().booleanValue()) {
            List<BlockPos> tables = BlockUtil.getBlockSphere(this.tableRange.getValue().floatValue(), BlockWorkbench.class);
            tables.sort(Comparator.comparingDouble(pos -> BedBomb.mc.player.getDistanceSq(pos)));
            if (!tables.isEmpty() && !(BedBomb.mc.currentScreen instanceof GuiCrafting)) {
                RayTraceResult result;
                target = tables.get(0);
                BedBomb.mc.player.connection.sendPacket(new CPacketEntityAction(BedBomb.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                if (BedBomb.mc.player.getDistanceSq(target) > MathUtil.square(this.breakRange.getValue().floatValue())) {
                    return;
                }
                Vec3d hitVec = new Vec3d(target);
                float[] rotations = RotationUtil.getLegitRotations(hitVec);
                this.yaw.set(rotations[0]);
                if (this.rotate.getValue().booleanValue()) {
                    this.shouldRotate.set(true);
                    this.pitch.set(rotations[1]);
                }
                EnumFacing facing = (result = BedBomb.mc.world.rayTraceBlocks(new Vec3d(BedBomb.mc.player.posX, BedBomb.mc.player.posY + (double) BedBomb.mc.player.getEyeHeight(), BedBomb.mc.player.posZ), new Vec3d((double) target.getX() + 0.5, (double) target.getY() - 0.5, (double) target.getZ() + 0.5))) == null || result.sideHit == null ? EnumFacing.UP : result.sideHit;
                BlockUtil.rightClickBlock(target, hitVec, EnumHand.MAIN_HAND, facing, true);
                this.breakTimer.reset();
                if (BedBomb.mc.player.isSneaking()) {
                    BedBomb.mc.player.connection.sendPacket(new CPacketEntityAction(BedBomb.mc.player, CPacketEntityAction.Action.START_SNEAKING));
                }
            }
            this.shouldCraft = BedBomb.mc.currentScreen instanceof GuiCrafting;
            this.craftStage = 0;
            this.craftTimer.reset();
        }
    }

    public void craftTable() {
        int woodSlot = InventoryUtil.findInventoryBlock(BlockPlanks.class, true);
        if (woodSlot != -1) {
            BedBomb.mc.playerController.windowClick(0, woodSlot, 0, ClickType.PICKUP, BedBomb.mc.player);
            BedBomb.mc.playerController.windowClick(0, 1, 1, ClickType.PICKUP, BedBomb.mc.player);
            BedBomb.mc.playerController.windowClick(0, 2, 1, ClickType.PICKUP, BedBomb.mc.player);
            BedBomb.mc.playerController.windowClick(0, 3, 1, ClickType.PICKUP, BedBomb.mc.player);
            BedBomb.mc.playerController.windowClick(0, 4, 1, ClickType.PICKUP, BedBomb.mc.player);
            BedBomb.mc.playerController.windowClick(0, 0, 0, ClickType.QUICK_MOVE, BedBomb.mc.player);
            int table = InventoryUtil.findInventoryBlock(BlockWorkbench.class, true);
            if (table != -1) {
                BedBomb.mc.playerController.windowClick(0, table, 0, ClickType.PICKUP, BedBomb.mc.player);
                BedBomb.mc.playerController.windowClick(0, this.tableSlot.getValue().intValue(), 0, ClickType.PICKUP, BedBomb.mc.player);
                BedBomb.mc.playerController.windowClick(0, table, 0, ClickType.PICKUP, BedBomb.mc.player);
            }
        }
    }

    @Override
    public void onToggle() {
        this.lastHotbarSlot = -1;
        this.bedSlot = -1;
        this.sendRotationPacket = false;
        this.target = null;
        this.yaw.set(-1.0);
        this.pitch.set(-1.0);
        this.shouldRotate.set(false);
        this.shouldCraft = false;
    }

    private int findBedSlot() {
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = BedBomb.mc.player.inventory.getStackInSlot(i);
            if (stack == ItemStack.EMPTY || stack.getItem() != Items.BED) continue;
            return i;
        }
        return -1;
    }

    private int safety(BlockPos pos) {
        int safety = 0;
        for (EnumFacing facing : EnumFacing.values()) {
            if (BedBomb.mc.world.getBlockState(pos.offset(facing)).getMaterial().isReplaceable()) continue;
            ++safety;
        }
        return safety;
    }

    public enum BreakLogic {
        ALL,
        CALC

    }

    public enum Logic {
        BREAKPLACE,
        PLACEBREAK

    }

    public static class BedData {
        private final BlockPos pos;
        private final IBlockState state;
        private final boolean isHeadPiece;
        private final TileEntityBed entity;

        public BedData(BlockPos pos, IBlockState state, TileEntityBed bed, boolean isHeadPiece) {
            this.pos = pos;
            this.state = state;
            this.entity = bed;
            this.isHeadPiece = isHeadPiece;
        }

        public BlockPos getPos() {
            return this.pos;
        }

        public IBlockState getState() {
            return this.state;
        }

        public boolean isHeadPiece() {
            return this.isHeadPiece;
        }

        public TileEntityBed getEntity() {
            return this.entity;
        }
    }
}

