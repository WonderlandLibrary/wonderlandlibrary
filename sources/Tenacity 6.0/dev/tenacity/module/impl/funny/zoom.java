package dev.tenacity.module.impl.funny;

import dev.tenacity.event.IEventListener;
import dev.tenacity.event.impl.player.MotionEvent;
import dev.tenacity.module.Module;
import dev.tenacity.module.ModuleCategory;
import dev.tenacity.setting.impl.ModeSetting;
import dev.tenacity.util.misc.TimerUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Timer;

import java.util.ArrayList;
import java.util.List;

public final class zoom extends Module {

    private final TimerUtil timerUtil = new TimerUtil();

    private final List<Packet<?>> packetList = new ArrayList<>();

    private float lastMovementYaw;

    private final ModeSetting mode = new ModeSetting("Mode", "Vanilla", "HvH Mode", "uh oh", "oh no", "crash", "Slime");
    // "Mineland", "BlocksMC", "Verus", "Vulcan", "Negativity"
    public zoom() {
        super("Zoom", "Makes you the flash", ModuleCategory.FUNNY);
        initializeSettings(mode);
    }

    private final IEventListener<MotionEvent> onMotionEvent = event -> {
        if (event.isPre()) {
            switch (mode.getCurrentMode()) {
                case "Vanilla": {
                    if (mc.gameSettings.keyBindAttack.isKeyDown()) {
                        mc.thePlayer.motionY = 1;
                    }
                    break;
                }

                case "Slime": {
                        if (event.isPre()) {
                            BlockPos playerPos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
                            for (EnumFacing facing : EnumFacing.values()) {
                                BlockPos adjacentPos = playerPos.offset(facing);
                                IBlockState blockState = mc.theWorld.getBlockState(adjacentPos);
                                Block block = blockState.getBlock();

                                if (block == Blocks.slime_block) {
                                    mc.thePlayer.motionY = 2f;
                                    break;
                                }
                            }
                        }
                    break;
                }

                case "HvH Mode": {
                    if (mc.gameSettings.keyBindAttack.isKeyDown()) {
                        mc.thePlayer.motionY = 9;
                    }
                }
                    break;

                case "uh oh": {
                    if (mc.gameSettings.keyBindAttack.isKeyDown()) {
                        mc.thePlayer.motionY = 100;
                    }
                }
                break;

                case "oh no": {
                    if (mc.gameSettings.keyBindAttack.isKeyDown()) {
                        mc.thePlayer.motionY = 1000;
                    }
                }
                break;

                case "crash": {
                    if (mc.gameSettings.keyBindAttack.isKeyDown()) {
                        mc.thePlayer.motionY = 999999999;
                    }
                break;

//                }
//                case "Mineland": {
//                    if (mc.thePlayer.onGround && MovementUtil.isMoving()) {
//                        mc.thePlayer.jump();
//                        MovementUtil.setSpeed(0.46);
//                        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
//                            MovementUtil.setSpeed(0.1);
//                        }
//                    }
//                }
//                case "Verus": {
//                    if (Keyboard.isKeyDown(KEY_SPACE)) {
//                        MovementUtil.setSpeed(0.2);
//                    }
//                    if (mc.thePlayer.onGround && MovementUtil.isMoving()) {
//                        mc.thePlayer.motionY = 0.42f;
//                        MovementUtil.setSpeed(0.3);
//                    }
//                    break;
//                }
//                case "BlocksMC": {
//                    if (mc.thePlayer.onGround && MovementUtil.isMoving()) {
//                        if (mc.thePlayer.onGround)
//                            mc.thePlayer.jump();
//                        MovementUtil.setSpeed(0.35);
//                        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
//                            toggle();
//                        }
//                        break;
//                    }
//                }
//
//                case "Negativity": {
//                    if (MovementUtil.isMoving()) {
//                        if (Keyboard.isKeyDown(KEY_W) || Keyboard.isKeyDown(KEY_S) || Keyboard.isKeyDown(KEY_A) || Keyboard.isKeyDown(KEY_D) && mc.thePlayer.isAirBorne) {
//                            mc.thePlayer.motionY = 0;
//                            mc.thePlayer.posY = mc.thePlayer.posY + 0.3;
//                            MovementUtil.setSpeed(0.5);
//                            if (timerUtil.hasTimeElapsed(2000, true)) {
//                                mc.thePlayer.motionY = -1000;
//                                mc.thePlayer.posY = mc.thePlayer.posY - 0.3;
//                                MovementUtil.setSpeed(0.5);
//                            }
//                            if (Keyboard.isKeyDown(KEY_SPACE)) {
//                                ChatUtil.error("Disabling due to illegal key press!");
//                                MovementUtil.setSpeed(0);
//                                toggle();
//                            }
//                        }
//                        break;
                }
            }
        }
    };
    @Override
    public void onEnable() {
        packetList.clear();
        lastMovementYaw = -1;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        if(!packetList.isEmpty()) {
            packetList.forEach(mc.getNetHandler()::addToSendQueueNoEvent);
            packetList.clear();
        }
        Timer.timerSpeed = 1;
        super.onDisable();
    }
}
