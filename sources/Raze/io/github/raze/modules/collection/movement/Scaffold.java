package io.github.raze.modules.collection.movement;

import io.github.raze.Raze;
import io.github.raze.events.collection.motion.EventMotion;
import io.github.nevalackin.radbus.Listen;
import io.github.raze.modules.system.AbstractModule;
import io.github.raze.modules.system.information.ModuleCategory;
import io.github.raze.settings.collection.ArraySetting;
import io.github.raze.settings.collection.BooleanSetting;
import io.github.raze.utilities.collection.math.RandomUtil;
import io.github.raze.utilities.collection.math.TimeUtil;
import io.github.raze.utilities.collection.world.MoveUtil;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.potion.Potion;
import net.minecraft.util.*;

import java.util.Objects;

public class Scaffold extends AbstractModule {

    private final ArraySetting mode, placingMode,rotationMode, towerMode, itemSpoof;
    private final BooleanSetting noSwing, slowDown, sprint;
    public final BooleanSetting safeWalk;

    private final TimeUtil vulcanTimer;

    public static boolean shouldDoSafeWalk = false;
    private static float lastYaw = 0, lastPitch = 0;
    private static int originalSlot;

    public Scaffold() {
        super("Scaffold", "Automatically builds a bridge under your feet. Really useful in skywars.", ModuleCategory.MOVEMENT);
        Raze.INSTANCE.managerRegistry.settingRegistry.add(

                mode = new ArraySetting(this, "Mode", "Vanilla", "Vanilla", "Vulcan", "AAC"),
                placingMode = new ArraySetting(this, "State", "Pre", "Pre", "Post"),
                rotationMode = new ArraySetting(this, "Rotations", "Basic", "Basic", "Advanced", "Straight"),
                towerMode = new ArraySetting(this, "Tower", "None", "None", "Vanilla", "Verus"),
                itemSpoof = new ArraySetting(this, "Item Spoof", "Pick", "Pick"),

                noSwing = new BooleanSetting(this, "No Swing", false),
                slowDown = new BooleanSetting(this, "Slow Down", false),
                safeWalk = new BooleanSetting(this, "Safe Walk", false),
                sprint = new BooleanSetting(this, "Sprint", false)
                        .setHidden(() -> mode.compare("AAC"))
        );

        vulcanTimer = new TimeUtil();
    }


    @Override
    public void onEnable() {
        originalSlot = mc.thePlayer.inventory.currentItem;
    }

    @Override
    public void onDisable() {
        mc.thePlayer.setSprinting(false);
        mc.thePlayer.inventory.currentItem = originalSlot;
        mc.playerController.updateController();
    }

    @Listen
    public void onMotion(EventMotion eventMotion) {
        if (mc.theWorld == null || mc.thePlayer == null)
            return;

        if (eventMotion.getState().name().equalsIgnoreCase(placingMode.get())) {

            BlockPosition playerBlock = new BlockPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);

            BlockData data = findScaffoldBlock();

            if (data == null) {
                return;
            }

            doSprint();
            doSlowdown();

            eventMotion.setYaw(lastYaw);
            eventMotion.setPitch(lastPitch);

            switch (rotationMode.get()) {
                case "Basic":
                    eventMotion.setYaw(180 + mc.thePlayer.rotationYaw);
                    eventMotion.setPitch(82);
                    lastYaw = eventMotion.getYaw();
                    lastPitch = eventMotion.getPitch();
                    break;

                case "Advanced":
                    eventMotion.setYaw(mc.thePlayer.rotationYaw + 180);
                    eventMotion.setPitch(getScaffoldPitch());
                    lastYaw = eventMotion.getYaw();
                    lastPitch = eventMotion.getPitch();
                    break;

                case "Straight":
                    eventMotion.setYaw(mc.thePlayer.rotationYaw);
                    eventMotion.setPitch(82);
                    lastYaw = eventMotion.getYaw();
                    lastPitch = eventMotion.getPitch();
                    break;
            }

            mc.thePlayer.rotationYawHead = eventMotion.getYaw();
            mc.thePlayer.renderYawOffset = eventMotion.getYaw();

            tower();

            if (itemSpoof.compare("Pick")) {
                for (int slot = 0; slot < 9; slot++) {
                    ItemStack stack = mc.thePlayer.inventory.getStackInSlot(slot);
                    if (stack != null && stack.getItem() instanceof ItemBlock) {
                        mc.thePlayer.inventory.currentItem = slot;
                        mc.playerController.updateController();
                        break;
                    }
                }
            }

            if(MoveUtil.isMoving() || mc.gameSettings.keyBindJump.isKeyDown()) {
                if (mc.thePlayer.getHeldItem() != null && mc.theWorld.isAirBlock(playerBlock)) {
                    doSafeWalk();
                    placeBlock(data);
                }
            }
        }
    }

    public void doSprint() {
        if (!MoveUtil.isMoving() || mc.thePlayer.isSprinting()) return;

        if(!sprint.get()) {
            mc.thePlayer.setSprinting(false);
        }

        if(sprint.get() && !mode.compare("AAC")) {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), true);
        }

        if(mode.compare("AAC") && mc.thePlayer.isSprinting()){
            mc.thePlayer.setSprinting(false);
        }
    }

    public void doSafeWalk() {
        shouldDoSafeWalk = safeWalk.get();
    }

    public void doSlowdown() {
        if (slowDown.get() || mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            mc.thePlayer.motionX *= 0.81;
            mc.thePlayer.motionZ *= 0.81;
        }
    }

    public void doSwing() {
        if(noSwing.get()) {
            mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
        } else {
            mc.thePlayer.swingItem();
        }
    }

    public void placeBlock(BlockData data) {

        if(mc.thePlayer.getCurrentEquippedItem() == null) {
            return;
        }
        if(mc.thePlayer.getCurrentEquippedItem().getItem() == null) {
            return;
        }
        if(!(mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBlock)) {
            return;
        }

        if (mode.get().equals("Vulcan")) {
            if (vulcanTimer.elapsed(500)) {
                mc.thePlayer.sendQueue.addToSendQueueSilent(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
                vulcanTimer.reset();
            }
            if (vulcanTimer.lastMS == 75 + RandomUtil.nextInt(0, 100)) {
                mc.thePlayer.sendQueue.addToSendQueueSilent(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
            }
        }

        doSwing();
        mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem(), data.pos, data.face, new Vec3(data.pos.getX(), data.pos.getY(), data.pos.getZ()));
    }

    public void tower() {
        if(mc.thePlayer.onGround || !mc.theWorld.isAirBlock(new BlockPosition(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ))) {
            if(mc.gameSettings.keyBindJump.pressed) {
                towerJump();
            }
        }
    }

    public void towerJump() {
        switch(towerMode.get()) {
            case "None":
                break;

            case "Vanilla":
                mc.thePlayer.motionY = 0.41999998688697815D;
                break;

            case "Verus":
                if(mc.thePlayer.ticksExisted % 2 == 0) {
                    mc.thePlayer.motionY = 0.41999998688697815D;
                }
                break;
        }
    }

    private BlockData getBlockData(BlockPosition pos) {
        if (mc.theWorld.getBlockState(pos.add(0, -1, 0)).getBlock() != Blocks.air)
            return new BlockData(pos.add(0, -1, 0), EnumFacing.UP);
        if (mc.theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock() != Blocks.air)
            return new BlockData(pos.add(-1, 0, 0), EnumFacing.EAST);
        if (mc.theWorld.getBlockState(pos.add(1, 0, 0)).getBlock() != Blocks.air)
            return new BlockData(pos.add(1, 0, 0), EnumFacing.WEST);
        if (mc.theWorld.getBlockState(pos.add(0, 0, -1)).getBlock() != Blocks.air)
            return new BlockData(pos.add(0, 0, -1), EnumFacing.SOUTH);
        if (mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock() != Blocks.air)
            return new BlockData(pos.add(0, 0, 1), EnumFacing.NORTH);
        return null;
    }

    public static class BlockData
    {
        public final BlockPosition pos;
        public final EnumFacing face;

        BlockData(BlockPosition pos, EnumFacing face) {
            this.pos = pos;
            this.face = face;
        }
    }

    private float getScaffoldPitch() {
        float random = (float) Math.random();
        float pitch = 81 + random;

        if(mc.gameSettings.keyBindJump.pressed)
            return 90;

        return pitch;
    }

    private BlockData findScaffoldBlock() {
        BlockData data = null;

        double yDif;
        for (double posY = mc.thePlayer.posY - 1.0D; posY > 0.0D; posY--) {
            BlockData newData = getBlockData(new BlockPosition(mc.thePlayer.posX, posY, mc.thePlayer.posZ));
            if (newData != null) {
                yDif = mc.thePlayer.posY - posY;
                if (yDif <= 3.0D) {
                    data = newData;
                    break;
                }
            }
        }

        if (data == null) {
            return null;
        }

        if (Objects.equals(data.pos, new BlockPosition(0, -1, 0))) {
            mc.thePlayer.motionX = 0.0D;
            mc.thePlayer.motionZ = 0.0D;
        }

        return data;
    }

}
