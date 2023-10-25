package fun.expensive.client.feature.impl.player;

import fun.rich.client.event.EventTarget;
import fun.rich.client.event.events.impl.packet.EventReceivePacket;
import fun.rich.client.event.events.impl.player.BlockHelper;
import fun.rich.client.event.events.impl.player.EventPreMotion;
import fun.rich.client.event.events.impl.player.EventUpdate;
import fun.rich.client.event.events.impl.render.EventRender3D;
import fun.rich.client.feature.Feature;
import fun.rich.client.feature.impl.FeatureCategory;
import fun.rich.client.ui.settings.impl.BooleanSetting;
import fun.rich.client.ui.settings.impl.ColorSetting;
import fun.rich.client.ui.settings.impl.ListSetting;
import fun.rich.client.ui.settings.impl.NumberSetting;
import fun.rich.client.utils.math.RotationHelper;
import fun.rich.client.utils.math.TimerHelper;
import fun.rich.client.utils.render.RenderUtils;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.*;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.network.play.server.SPacketMultiBlockChange;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

public class AutoFarm extends Feature {
    private boolean isActive;
    private int oldSlot;
    public static boolean isEating;
    ArrayList<BlockPos> crops = new ArrayList();
    ArrayList<BlockPos> check = new ArrayList();
    TimerHelper timerHelper = new TimerHelper();
    TimerHelper timerHelper2 = new TimerHelper();
    private final BooleanSetting autoFarm = new BooleanSetting("Auto Farm", true, () -> true);
    private ListSetting farmMode = new ListSetting("AutoFarm Mode", "Harvest", autoFarm::getBoolValue, "Harvest", "Plant");
    private final NumberSetting delay = new NumberSetting("AutoFarm Delay", 2.0f, 0.0f, 10.0f, 0.1f, () -> true);
    private final NumberSetting radius = new NumberSetting("AutoFarm Radius", 4.0f, 1.0f, 7.0f, 0.1f, () -> true);
    private final BooleanSetting autoHoe = new BooleanSetting("Auto Hoe", false, () -> true);
    private final BooleanSetting farmESP = new BooleanSetting("Draw Box", true, () -> autoFarm.getBoolValue() && farmMode.currentMode.equals("Harvest"));
    private final ColorSetting color = new ColorSetting("Box Color", new Color(0xFFFFFF).getRGB(), farmESP::getBoolValue);
    private final BooleanSetting autoEat = new BooleanSetting("Auto Eat", true, () -> true);
    private final NumberSetting feed = new NumberSetting("Feed Value", 15.0f, 1.0f, 20.0f, 1.0f, autoEat::getBoolValue);


    public AutoFarm() {
        super("AutoFarm", "Автоматически садит и ломает урожай", FeatureCategory.Player);
        addSettings(farmMode, autoFarm, farmESP, color, autoHoe, delay, radius, autoEat, feed);
    }

    public static boolean doesHaveSeeds() {
        for (int i = 0; i < 9; ++i) {
            mc.player.inventory.getStackInSlot(i);
            if (!(mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemSeeds)) continue;
            return true;
        }
        return false;
    }

    public static int searchSeeds() {
        for (int i = 0; i < 45; ++i) {
            ItemStack itemStack = mc.player.inventoryContainer.getSlot(i).getStack();
            if (!(itemStack.getItem() instanceof ItemSeeds)) continue;
            return i;
        }
        return -1;
    }

    public static int getSlotWithSeeds() {
        for (int i = 0; i < 9; ++i) {
            mc.player.inventory.getStackInSlot(i);
            if (!(mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemSeeds)) continue;
            return i;
        }
        return 0;
    }

    @Override
    public void onEnable() {
        crops.clear();
        check.clear();
        super.onEnable();
    }

    private boolean isOnCrops() {
        for (double x = mc.player.boundingBox.minX; x < mc.player.boundingBox.maxX; x += (double) 0.01f) {
            for (double z = mc.player.boundingBox.minZ; z < mc.player.boundingBox.maxZ; z += (double) 0.01f) {
                Block block = mc.world.getBlockState(new BlockPos(x, mc.player.posY - 0.1, z)).getBlock();
                if (block instanceof BlockFarmland || block instanceof BlockSoulSand || block instanceof BlockSand || block instanceof BlockAir)
                    continue;
                return false;
            }
        }
        return true;
    }

    private boolean IsValidBlockPos(BlockPos pos) {
        IBlockState state = mc.world.getBlockState(pos);
        if (state.getBlock() instanceof BlockFarmland || state.getBlock() instanceof BlockSand || state.getBlock() instanceof BlockSoulSand) {
            return mc.world.getBlockState(pos.up()).getBlock() == Blocks.AIR;
        }
        return false;
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {
        if (mc.player == null || mc.world == null) {
            return;
        }
        if (autoEat.getBoolValue()) {
            if (isFood()) {
                if (isFood() && (float) mc.player.getFoodStats().getFoodLevel() <= feed.getNumberValue()) {
                    isActive = true;
                    KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), true);
                } else if (isActive) {
                    KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), false);
                    isActive = false;
                }
            } else {
                if (isEating && !mc.player.isHandActive()) {
                    if (oldSlot != -1) {
                        mc.player.inventory.currentItem = oldSlot;
                        oldSlot = -1;
                    }
                    isEating = false;
                    KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), false);
                    return;
                }
                if (isEating) {
                    return;
                }
                if (isValid(mc.player.getHeldItemOffhand(), mc.player.getFoodStats().getFoodLevel())) {
                    mc.player.setActiveHand(EnumHand.OFF_HAND);
                    isEating = true;
                    KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), true);
                    mc.rightClickMouse();
                } else {
                    for (int i = 0; i < 9; ++i) {
                        if (!isValid(mc.player.inventory.getStackInSlot(i), mc.player.getFoodStats().getFoodLevel()))
                            continue;
                        oldSlot = mc.player.inventory.currentItem;
                        mc.player.inventory.currentItem = i;
                        isEating = true;
                        KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), true);
                        mc.rightClickMouse();
                        return;
                    }
                }
            }
        }
    }

    private boolean itemCheck(Item item) {
        return item != Items.ROTTEN_FLESH && item != Items.SPIDER_EYE && item != Items.POISONOUS_POTATO && (item != Items.FISH || new ItemStack(Items.FISH).getItemDamage() != 3);
    }

    private boolean isValid(ItemStack stack, int food) {
        return stack.getItem() instanceof ItemFood && feed.getNumberValue() - (float) food >= (float) ((ItemFood) stack.getItem()).getHealAmount(stack) && itemCheck(stack.getItem());
    }

    private boolean isFood() {
        return mc.player.getHeldItemOffhand().getItem() instanceof ItemFood;

    }

    @EventTarget
    public void onRender3D(EventRender3D event) {
        if (mc.player == null || mc.world == null) {
            return;
        }
        if (farmESP.getBoolValue() && farmMode.currentMode.equals("Harvest")) {
            ArrayList<BlockPos> blockPositions = getBlocks(radius.getNumberValue(), 0.0f, radius.getNumberValue());
            for (BlockPos pos : blockPositions) {
                Color cropsColor = new Color(color.getColorValue());
                BlockPos blockPos = new BlockPos(pos.getX(), pos.getY(), pos.getZ());
                RenderUtils.blockEsp(blockPos, cropsColor, false, 1.0, 1.0);
            }
        }
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        BlockPos pos;
        if (mc.player == null && mc.world == null) {
            return;
        }
        if (autoHoe.getBoolValue() && (pos = BlockHelper.getSphere(BlockHelper.getPlayerPosLocal(), radius.getNumberValue(), 6, false, true, 0).stream().filter(BlockHelper::IsValidBlockPos).min(Comparator.comparing(blockPos -> RotationHelper.getDistanceOfEntityToBlock(mc.player, blockPos))).orElse(null)) != null && mc.player.getHeldItemMainhand().getItem() instanceof ItemHoe) {
            float[] rots = RotationHelper.getRotationVector(new Vec3d((float) pos.getX() + 0.5f, (float) pos.getY() + 0.5f, (float) pos.getZ() + 0.5f));
            event.setYaw(rots[0]);
            event.setPitch(rots[1]);
            mc.player.renderYawOffset = rots[0];
            mc.player.rotationYawHead = rots[0];
            mc.player.rotationPitchHead = rots[1];
            if (timerHelper2.hasReached(delay.getNumberValue() * 100.0f)) {
                mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, EnumFacing.UP, EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
                mc.player.swingArm(EnumHand.MAIN_HAND);
                timerHelper2.reset();
            }
        }
        if (farmMode.currentMode.equals("Plant") && !doesHaveSeeds() && searchSeeds() != -1) {
            mc.playerController.windowClick(0, searchSeeds(), 1, ClickType.QUICK_MOVE, mc.player);
        }
    }

    @EventTarget
    public void onPre(EventPreMotion e) {
        if (autoFarm.getBoolValue()) {
            String mode = farmMode.getOptions();
            if (mode.equalsIgnoreCase("Harvest")) {
                ArrayList<BlockPos> blockPositions = getBlocks(radius.getNumberValue(), radius.getNumberValue(), radius.getNumberValue());
                for (BlockPos pos : blockPositions) {
                    BlockCrops crop;
                    IBlockState state = BlockHelper.getState(pos);
                    if (!isCheck(Block.getIdFromBlock(state.getBlock()))) continue;
                    if (!isCheck(0)) {
                        check.add(pos);
                    }
                    Block block = mc.world.getBlockState(pos).getBlock();
                    BlockPos downPos = pos.down(1);
                    if (!(block instanceof BlockCrops) || (crop = (BlockCrops) block).canGrow(mc.world, pos, state, true) || !timerHelper.hasReached(delay.getNumberValue() * 100.0f) || pos == null)
                        continue;
                    float[] rots = RotationHelper.getRotationVector(new Vec3d((float) pos.getX() + 0.5f, (float) pos.getY() + 0.5f, (float) pos.getZ() + 0.5f));
                    e.setYaw(rots[0]);
                    e.setPitch(rots[1]);
                    mc.player.renderYawOffset = rots[0];
                    mc.player.rotationYawHead = rots[0];
                    mc.player.rotationPitchHead = rots[1];
                    mc.playerController.onPlayerDamageBlock(pos, mc.player.getHorizontalFacing());
                    mc.player.swingArm(EnumHand.MAIN_HAND);
                    if (doesHaveSeeds()) {
                        mc.player.connection.sendPacket(new CPacketHeldItemChange(getSlotWithSeeds()));
                        mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(downPos, EnumFacing.UP, EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
                        mc.player.swingArm(EnumHand.MAIN_HAND);
                    }
                    timerHelper.reset();
                }
            } else if (mode.equalsIgnoreCase("Plant")) {
                BlockPos pos = BlockHelper.getSphere(BlockHelper.getPlayerPosLocal(), radius.getNumberValue(), 6, false, true, 0).stream().filter(this::IsValidBlockPos).min(Comparator.comparing(blockPos -> RotationHelper.getDistanceOfEntityToBlock(mc.player, blockPos))).orElse(null);
                Vec3d vec = new Vec3d(0.0, 0.0, 0.0);
                if (timerHelper.hasReached(delay.getNumberValue() * 100.0f) && isOnCrops() && pos != null && doesHaveSeeds()) {
                    float[] rots = RotationHelper.getRotationVector(new Vec3d((float) pos.getX() + 0.5f, (float) pos.getY() + 0.5f, (float) pos.getZ() + 0.5f));
                    e.setYaw(rots[0]);
                    e.setPitch(rots[1]);
                    mc.player.renderYawOffset = rots[0];
                    mc.player.rotationYawHead = rots[0];
                    mc.player.rotationPitchHead = rots[1];
                    mc.player.connection.sendPacket(new CPacketHeldItemChange(getSlotWithSeeds()));
                    mc.playerController.processRightClickBlock(mc.player, mc.world, pos, EnumFacing.VALUES[0].getOpposite(), vec, EnumHand.MAIN_HAND);
                    timerHelper.reset();
                }
            }
        }
    }

    @EventTarget
    public void onReceivePacket(EventReceivePacket e) {
        if (autoFarm.getBoolValue()) {
            if (e.getPacket() instanceof SPacketBlockChange) {
                SPacketBlockChange p = (SPacketBlockChange) e.getPacket();
                if (isEnabled(Block.getIdFromBlock(p.getBlockState().getBlock()))) {
                    crops.add(p.getBlockPosition());
                }
            } else if (e.getPacket() instanceof SPacketMultiBlockChange) {
                SPacketMultiBlockChange p = (SPacketMultiBlockChange) e.getPacket();
                for (SPacketMultiBlockChange.BlockUpdateData dat : p.getChangedBlocks()) {
                    if (!isEnabled(Block.getIdFromBlock(dat.getBlockState().getBlock()))) continue;
                    crops.add(dat.getPos());
                }
            }
        }
    }

    private boolean isCheck(int id) {
        int check = 0;
        if (id != 0) {
            check = 59;
        }
        if (id == 0) {
            return false;
        }
        return id == check;
    }

    private boolean isEnabled(int id) {
        int check = 0;
        if (id != 0) {
            check = 59;
        }
        if (id == 0) {
            return false;
        }
        return id == check;
    }

    private ArrayList<BlockPos> getBlocks(float x, float y, float z) {
        BlockPos min = new BlockPos(mc.player.posX - (double) x, mc.player.posY - (double) y, mc.player.posZ - (double) z);
        BlockPos max = new BlockPos(mc.player.posX + (double) x, mc.player.posY + (double) y, mc.player.posZ + (double) z);
        return BlockHelper.getAllInBox(min, max);
    }
}
