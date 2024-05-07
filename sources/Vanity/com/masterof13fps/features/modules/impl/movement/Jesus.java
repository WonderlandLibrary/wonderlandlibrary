package com.masterof13fps.features.modules.impl.movement;

import com.masterof13fps.Methods;
import com.masterof13fps.Wrapper;
import com.masterof13fps.features.modules.Category;
import com.masterof13fps.features.modules.Module;
import com.masterof13fps.features.modules.ModuleInfo;
import com.masterof13fps.manager.eventmanager.Event;
import com.masterof13fps.manager.eventmanager.impl.EventUpdate;
import com.masterof13fps.manager.settingsmanager.Setting;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.optifine.BlockPosM;

@ModuleInfo(name = "Jesus", category = Category.MOVEMENT, description = "Lets you walk on water or lava")
public class Jesus extends Module {

    public Setting mode = new Setting("Mode", this, "Normal", new String[]{"Vanilla", "Dolphin", "AAC", "NCP", "Intave"});

    private int stage;
    private boolean canjump;
    private int delay;
    private int time;

    public boolean getColliding(final int i) {
        final Minecraft mc = Minecraft.mc();
        int mx = i;
        int mz = i;
        int max = i;
        int maz = i;
        if (getPlayer().motionX > 0.01) {
            mx = 0;
        } else if (getPlayer().motionX < -0.01) {
            max = 0;
        }
        if (getPlayer().motionZ > 0.01) {
            mz = 0;
        } else if (getPlayer().motionZ < -0.01) {
            maz = 0;
        }
        final int xmin = MathHelper.floor_double(getPlayer().getEntityBoundingBox().minX - mx);
        final int ymin = MathHelper.floor_double(getPlayer().getEntityBoundingBox().minY - 1.0);
        final int zmin = MathHelper.floor_double(getPlayer().getEntityBoundingBox().minZ - mz);
        final int xmax = MathHelper.floor_double(getPlayer().getEntityBoundingBox().minX + max);
        final int ymax = MathHelper.floor_double(getPlayer().getEntityBoundingBox().minY + 1.0);
        final int zmax = MathHelper.floor_double(getPlayer().getEntityBoundingBox().minZ + maz);
        for (int x = xmin; x <= xmax; ++x) {
            for (int y = ymin; y <= ymax; ++y) {
                for (int z = zmin; z <= zmax; ++z) {
                    final Block block = (Block) getBlock(new BlockPos(x, y, z));
                    if (block instanceof BlockLiquid && !getPlayer().isInsideOfMaterial(Material.lava) && !getPlayer().isInsideOfMaterial(Material.water)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isInLiquid() {
        boolean inLiquid = false;
        int y = (int) getPlayer().boundingBox.minY;
        for (int x = MathHelper.floor_double(getPlayer().boundingBox.minX); x < MathHelper
                .floor_double(getPlayer().boundingBox.maxX) + 1; x++) {
            for (int z = MathHelper.floor_double(getPlayer().boundingBox.minZ); z < MathHelper
                    .floor_double(getPlayer().boundingBox.maxZ) + 1; z++) {
                Block block = mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
                if ((block != null) && (!(block instanceof BlockAir))) {
                    if (!(block instanceof BlockLiquid)) {
                        return false;
                    }
                    inLiquid = true;
                }
            }
        }
        return inLiquid;
    }

    @Override
    public void onToggle() {

    }

    @Override
    public void onEnable() {
        stage = 0;
    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {
            switch (mode.getCurrentMode()) {
                case "Vanilla": {
                    doVanilla();
                    break;
                }
                case "Dolphin": {
                    doDolphin();
                    break;
                }
                case "AAC": {
                    doAAC();
                    break;
                }
                case "NCP": {
                    doNCP();
                    break;
                }
                case "Intave": {
                    doIntave();
                    break;
                }
            }
        }
    }

    private void doIntave() {
        final BlockPos pos = new BlockPos(getX(), getY() - 0.001, getZ());
        if (getWorld().getBlockState(pos).getBlock() == Blocks.water && !Jesus.mc.gameSettings.keyBindJump.isKeyDown()) {
            getPlayer().motionY = 0.001;
            setSpeed(getBaseMoveSpeed() - 0.1);
        }
    }

    private void doNCP() {
        final BlockPos bp = new BlockPos(getPlayer().posX, getPlayer().posY - 1.0, getPlayer().posZ);
        if (mc.theWorld.getBlockState(bp).getBlock() == Blocks.water && timeHelper.isDelayComplete(377L)) {
            getPlayer().motionY = 0.2805;
            timeHelper.reset();
        }
    }

    private void doAAC() {
        if (getPlayer().isInWater()) {
            getPlayer().motionY = 5.9D;
            getPlayer().jumpMovementFactor *= 0.9F;
            getPlayer().motionY = 0.2D;
        }
    }

    private void doDolphin() {
        final BlockPos bp = new BlockPosM(getPlayer().posX, getPlayer().posY - 1.0, getPlayer().posZ);
        if (!canjump && mc.theWorld.getBlockState(bp).getBlock() == Blocks.water) {
            ++delay;
            stage = 0;
            getPlayer().motionY = 0.1;
        } else if (getPlayer().onGround && mc.theWorld.getBlockState(bp).getBlock() != Blocks.water) {
            canjump = false;
            delay = 0;
        }
        if (delay > 2) {
            canjump = true;
        }
        if (canjump) {
            ++stage;
            double moty = getPlayer().motionY;
            switch (stage) {
                case 1: {
                    moty = 0.5;
                    break;
                }
                case 2: {
                    moty = 0.483;
                    break;
                }
                case 3: {
                    moty = 0.46;
                    break;
                }
                case 4: {
                    moty = 0.42;
                    break;
                }
                case 5: {
                    moty = 0.388;
                    break;
                }
                case 6: {
                    moty = 0.348;
                    break;
                }
                case 7: {
                    moty = 0.316;
                    break;
                }
                case 8: {
                    moty = 0.284;
                    break;
                }
                case 9: {
                    moty = 0.252;
                    break;
                }
                case 10: {
                    moty = 0.22;
                    break;
                }
                case 11: {
                    moty = 0.188;
                    break;
                }
                case 12: {
                    moty = 0.166;
                    break;
                }
                case 13: {
                    moty = 0.165;
                    break;
                }
                case 14: {
                    moty = 0.16;
                    break;
                }
                case 15: {
                    moty = 0.136;
                    break;
                }
                case 16: {
                    moty = 0.11;
                    break;
                }
                case 17: {
                    moty = 0.111;
                    break;
                }
                case 18: {
                    moty = 0.1095;
                    break;
                }
                case 19: {
                    moty = 0.073;
                    break;
                }
                case 20: {
                    moty = 0.085;
                    break;
                }
                case 21: {
                    moty = 0.06;
                    break;
                }
                case 22: {
                    moty = 0.036;
                    break;
                }
                case 23: {
                    moty = 0.04;
                    break;
                }
                case 24: {
                    moty = 0.03;
                    break;
                }
                case 25: {
                    moty = 0.004;
                    break;
                }
                case 26: {
                    moty = 0.0025;
                    break;
                }
                case 27: {
                    moty = 0.002;
                    break;
                }
                case 28: {
                    moty = 0.0015;
                    break;
                }
                case 29: {
                    moty = -0.025;
                    break;
                }
                case 30: {
                    moty = -0.06;
                    break;
                }
                case 31: {
                    moty = -0.09;
                    break;
                }
                case 32: {
                    moty = -0.12;
                    break;
                }
                case 33: {
                    moty = -0.15;
                    break;
                }
                case 34: {
                    moty = -0.18;
                    break;
                }
                case 35: {
                    moty = -0.21;
                    break;
                }
                case 36: {
                    moty = -0.24;
                    break;
                }
                case 37: {
                    moty = -0.28;
                    break;
                }
                case 38: {
                    moty = -0.34;
                    break;
                }
                case 39: {
                    moty = -0.4;
                    break;
                }
                case 40: {
                    moty = -0.46;
                    break;
                }
                case 41: {
                    moty = -0.52;
                    break;
                }
                case 42: {
                    moty = -0.58;
                    break;
                }
                case 43: {
                    moty = -0.65;
                    break;
                }
                case 44: {
                    moty = -0.71;
                    break;
                }
                case 45: {
                    canjump = false;
                    break;
                }
            }
            getPlayer().motionY = moty;
        }
        if (getPlayer().moveForward == 0.0f && getPlayer().moveStrafing == 0.0f && !getPlayer().isSneaking() && getColliding(0)) {
            final int delay = 40;
            if (time < delay) {
                ++time;
            } else {
                ++time;
                if (time < delay + 5) {
                    getPlayer().motionX = 0.1;
                } else if (time < delay + 20 && time > delay + 10) {
                    getPlayer().motionZ = -0.1;
                } else if (time < delay + 30 && time > delay + 20) {
                    getPlayer().motionX = -0.1;
                } else if (time < delay + 40 && time > delay + 30) {
                    getPlayer().motionZ = 0.1;
                }
                if (time > delay + 40) {
                    time = delay;
                }
            }
        } else {
            time = 0;
        }
    }

    private void doVanilla() {
        if (getPlayer() == null) {
            return;
        }
        if ((isInLiquid()) && (getPlayer().isInsideOfMaterial(Material.air)) && (!getPlayer().isSneaking())) {
            try {
                if (timeHelper.isDelayComplete(50L)) {
                    getPlayer().motionY = 0.01D;
                    timeHelper.setLastMS();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            getPlayer().motionY = 0.11D;
        }
    }
}