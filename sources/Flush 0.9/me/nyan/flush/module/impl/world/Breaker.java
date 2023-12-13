package me.nyan.flush.module.impl.world;

import me.nyan.flush.Flush;
import me.nyan.flush.event.SubscribeEvent;
import me.nyan.flush.event.impl.Event2D;
import me.nyan.flush.event.impl.Event3D;
import me.nyan.flush.event.impl.EventMotion;
import me.nyan.flush.event.impl.EventWorldChange;
import me.nyan.flush.module.Module;
import me.nyan.flush.module.settings.BooleanSetting;
import me.nyan.flush.module.settings.ModeSetting;
import me.nyan.flush.module.settings.NumberSetting;
import me.nyan.flush.utils.combat.CombatUtils;
import me.nyan.flush.utils.render.RenderUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockCake;
import net.minecraft.block.BlockDragonEgg;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class Breaker extends Module {
    private final ModeSetting mode = new ModeSetting("Mode", this, "Beds", "Beds", "Cakes", "Eggs");
    private final NumberSetting range = new NumberSetting("Range", this, 4, 1, 6, 0.1);
    private final BooleanSetting rotations = new BooleanSetting("Rotations", this, true),
            instant = new BooleanSetting("Instant", this, false),
            throughWalls = new BooleanSetting("Through Walls", this, true),
            noSwing = new BooleanSetting("No Swing", this, false),
            blockEsp = new BooleanSetting("Block ESP", this, false);

    //private final ArrayList<BlockPos> brokenBeds = new ArrayList<>();
    private Block currentBlock;
    private BlockPos currentPos;
    private float currentDamage;
    private float blockHitDelay;

    public Breaker() {
        super("Breaker", Category.WORLD);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        //brokenBeds.clear();
        reset();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        reset();
    }

    @SubscribeEvent
    public void onMotion(EventMotion e) {
        if (currentPos == null || currentBlock == null) {
            float range = this.range.getValueFloat();
            for (float x = -range; x < range; ++x) {
                for (float y = range; y > -range; --y) {
                    for (float z = -range; z < range; ++z) {
                        BlockPos pos = new BlockPos((int) mc.thePlayer.posX + x, (int) mc.thePlayer.posY + y, (int) mc.thePlayer.posZ + z);
                        Block block = pos.getBlock();
                        if ((mode.is("beds") && block instanceof BlockBed) || (mode.is("cakes") && block instanceof BlockCake) ||
                                (mode.is("eggs") && block instanceof BlockDragonEgg)) {
                            if (!throughWalls.getValue() && mc.theWorld.rayTraceBlocks(new Vec3(mc.thePlayer.posX,
                                    mc.thePlayer.boundingBox.minY + (double) mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ),
                                    new Vec3(pos).addVector(0.5, 0.5, 0.5)) != null) {
                                continue;
                            }
                            currentPos = pos;
                            currentBlock = block;
                            currentDamage = 0;
                        }
                    }
                }
            }
            return;
        }

        if (MathHelper.sqrt_double(mc.thePlayer.getDistanceSqToCenter(currentPos)) > range.getValueFloat() || (!throughWalls.getValue() &&
                mc.theWorld.rayTraceBlocks(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + (double) mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ),
                        new Vec3(currentPos)) != null)) {
            reset();
            return;
        }

        if (blockHitDelay > 0) {
            blockHitDelay--;
            return;
        }

        if (rotations.getValue()) {
            float[] rotations = CombatUtils.getRotations(new Vec3(currentPos));
            e.setYaw(rotations[0]);
            e.setPitch(rotations[1]);
        }

        if (currentBlock instanceof BlockBed) {
            if (instant.getValue()) {
                mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, currentPos, EnumFacing.DOWN));
                if (!noSwing.getValue()) {
                    mc.thePlayer.swingItem();
                }
                mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, currentPos, EnumFacing.DOWN));
                reset();
                return;
            } else {
                if (currentDamage == 0) {
                    mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, currentPos, EnumFacing.DOWN));

                    if (mc.thePlayer.capabilities.isCreativeMode || currentBlock.getPlayerRelativeBlockHardness(mc.thePlayer, mc.theWorld, currentPos) >= 1) {
                        if (!noSwing.getValue())
                            mc.thePlayer.swingItem();
                        mc.playerController.onPlayerDestroyBlock(currentPos);
                        reset();
                        return;
                    }
                }

                currentDamage += currentBlock.getPlayerRelativeBlockHardness(mc.thePlayer, mc.theWorld, currentPos);
                mc.theWorld.sendBlockBreakProgress(mc.thePlayer.getEntityId(), currentPos, (int) (currentDamage * 10F) - 1);

                if (currentDamage >= 1) {
                    mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, currentPos, EnumFacing.DOWN));
                    mc.playerController.onPlayerDestroyBlock(currentPos);
                    blockHitDelay = 4;
                    reset();
                }
            }
        } else {
            if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem(), currentPos, EnumFacing.DOWN, new Vec3(currentPos))) {
                if (!noSwing.getValue()) {
                    mc.thePlayer.swingItem();
                }
                blockHitDelay = 4;
                reset();
            }
        }

        if (!noSwing.getValue()) {
            mc.thePlayer.swingItem();
        }
    }

    @SubscribeEvent
    public void onRender2D(Event2D e) {
        if (currentBlock != null) {
            Gui.drawRect(e.getWidth() / 2f - 34, e.getHeight()/2f + 14, e.getWidth() / 2f + 60, e.getHeight()/2f + 45, 0xAA000000);
            Flush.getFont("GoogleSansDisplay", 24)
                    .drawString(name, e.getWidth() / 2f - 28, e.getHeight() / 2f + 18, -1, true);
            Flush.getFont("GoogleSansDisplay", 18)
                    .drawString("Breaking " + (currentBlock instanceof BlockBed ? "bed" :
                                    currentBlock instanceof BlockCake ? "cake" : "egg"),
                            e.getWidth() / 2f - 28, e.getHeight() / 2f + 32, -1, true);
        }
    }

    @SubscribeEvent
    public void onRender3D(Event3D e) {
        if (currentPos != null && blockEsp.getValue()) {
            GlStateManager.disableTexture2D();
            GlStateManager.disableDepth();
            RenderUtils.drawBlockBox(currentPos, mc.timer.renderPartialTicks, 0xAAFF0000);
            RenderUtils.drawBlockOutline(currentPos, mc.timer.renderPartialTicks, 0xFFEE0000);
        }
    }

    @SubscribeEvent
    public void onWorldChange(EventWorldChange e) {
        //brokenBeds.clear();
    }

    private void reset() {
        currentPos = null;
        currentBlock = null;
        currentDamage = 0;
    }
}