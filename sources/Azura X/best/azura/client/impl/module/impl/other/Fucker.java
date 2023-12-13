package best.azura.client.impl.module.impl.other;

import best.azura.client.impl.events.EventClickMouse;
import best.azura.client.impl.events.EventWorldChange;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.impl.events.EventMotion;
import best.azura.client.api.module.Category;
import best.azura.client.api.module.Module;
import best.azura.client.api.module.ModuleInfo;
import best.azura.client.util.other.ChatUtil;
import best.azura.client.util.player.RotationUtil;
import best.azura.client.util.other.ServerUtil;
import best.azura.client.impl.value.BooleanValue;
import best.azura.client.impl.value.ComboSelection;
import best.azura.client.impl.value.ComboValue;
import best.azura.client.impl.value.ModeValue;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.*;

import java.util.ArrayList;

@ModuleInfo(name = "Fucker", description = "Break blocks", category = Category.OTHER)
public class Fucker extends Module {

    private final ComboValue comboValue = new ComboValue("Target", "Select which blocks you want to target",
            new ComboSelection("Bed", true), new ComboSelection("Cake", false), new ComboSelection("Egg", false));
    private final ModeValue cakeMode = new ModeValue("Cake Mode", "Select how cakes should be handled", () -> comboValue.isSelected("Cake"), "Break", "Break", "Eat");
    private final BooleanValue throughWallsBypass = new BooleanValue("Walls bypass", "Bypass the watchdog breaking beds through walls check", ServerUtil::isHypixel, true);
    private final BooleanValue doWhitelist = new BooleanValue("Whitelist", "Whitelist blocks", true);
    private final BooleanValue autoWhitelist = new BooleanValue("Whitelist own bed", "Whitelist your own bed automatically", doWhitelist::getObject, true);
    private final BooleanValue instantBreak = new BooleanValue("Instant break", "Instantly break the block", true);
    private BlockPos pos;
    private final ArrayList<BlockPos> whitelistedPositions = new ArrayList<>();

    @EventHandler
    public final Listener<EventWorldChange> eventWorldChangeListener = e -> {
        whitelistedPositions.clear();
    };

    @EventHandler
    public final Listener<EventClickMouse> eventClickMouseListener = e -> {
        if (!doWhitelist.getObject()) return;
        if (e.getButton() == 0 && e.getObjectMouseOver() != null && e.getObjectMouseOver().getBlockPos() != null &&
                (mc.theWorld.getBlockState(e.getObjectMouseOver().getBlockPos()).getBlock() == Blocks.bed ||
                        mc.theWorld.getBlockState(e.getObjectMouseOver().getBlockPos()).getBlock() == Blocks.cake ||
                        mc.theWorld.getBlockState(e.getObjectMouseOver().getBlockPos()).getBlock() == Blocks.dragon_egg)) {
            whitelistedPositions.remove(e.getObjectMouseOver().getBlockPos());
        }
        if (mc.gameSettings.keyBindSneak.pressed && e.getButton() == 1 && e.getObjectMouseOver() != null && e.getObjectMouseOver().getBlockPos() != null &&
                (mc.theWorld.getBlockState(e.getObjectMouseOver().getBlockPos()).getBlock() == Blocks.bed ||
                        mc.theWorld.getBlockState(e.getObjectMouseOver().getBlockPos()).getBlock() == Blocks.cake ||
                        mc.theWorld.getBlockState(e.getObjectMouseOver().getBlockPos()).getBlock() == Blocks.dragon_egg)) {
            whitelistedPositions.add(e.getObjectMouseOver().getBlockPos());
            final BlockPos bp = e.getObjectMouseOver().getBlockPos();
            ChatUtil.sendChat("Whitelisted bed at " + bp.getX() + ", " + bp.getY() + ", " + bp.getZ());
        }
    };

    @EventHandler
    public final Listener<EventMotion> eventMotionListener = e -> {
        if (e.isUpdate()) {
            if (pos == null) return;
            float[] rots = RotationUtil.faceVector(RotationUtil.getBlockVecCenter(pos));
            float yaw = rots[0];
            float pitch = rots[1];
            float f = MathHelper.cos(-yaw * 0.017453292F - (float)Math.PI);
            float f1 = MathHelper.sin(-yaw * 0.017453292F - (float)Math.PI);
            float f2 = -MathHelper.cos(-pitch * 0.017453292F);
            float f3 = MathHelper.sin(-pitch * 0.017453292F);
            Vec3 vec = new Vec3(f1 * f2, f3, f * f2);
            EnumFacing facing = EnumFacing.getFacingFromVector((float) vec.xCoord, (float) vec.yCoord, (float) vec.zCoord);
            if ((ServerUtil.isHypixel() || !throughWallsBypass.getObject()) && comboValue.isSelected("Bed") &&
                    mc.theWorld.getBlockState(pos).getBlock() == Blocks.bed) {
                EnumFacing facing1 = facing == EnumFacing.DOWN || facing == EnumFacing.UP ? facing : facing.getOpposite();
                Vec3i direction = facing.getDirectionVec();
                Vec3 vec1 = new Vec3(pos).add(new Vec3(Math.max(direction.getX(), -direction.getX()), Math.max(direction.getY(), -direction.getY()), Math.max(direction.getZ(), -direction.getZ())));
                mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
                if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem(), pos, facing1, vec1))
                    mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
                facing = facing.getOpposite();
            }
            mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
            if (comboValue.isSelected("Cake") && mc.theWorld.getBlockState(pos).getBlock() == Blocks.cake && cakeMode.getObject().equals("Eat")) {
                Vec3i dir = facing.getDirectionVec();
                vec = new Vec3(pos).add(new Vec3(Math.max(dir.getX(), -dir.getX()), Math.max(dir.getY(), -dir.getY()), Math.max(dir.getZ(), -dir.getZ())));
                mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem(), pos,
                        facing == EnumFacing.DOWN || facing == EnumFacing.UP ? facing : facing.getOpposite(), vec);
                mc.playerController.onStoppedUsingItem(mc.thePlayer);
            } else {
                if (instantBreak.getObject()) {
                    mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, facing));
                    mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, pos, facing));
                } else {
                    mc.playerController.onPlayerDamageBlock(pos, facing);
                }
            }
        }
        if (e.isPre()) {
            pos = null;
            for (int xx = -4; xx < 4; xx++) {
                for (int yy = -4; yy < 4; yy++) {
                    for (int zz = -4; zz < 4; zz++) {
                        BlockPos bp = new BlockPos(mc.thePlayer.posX + xx, mc.thePlayer.getEntityBoundingBox().minY + yy, mc.thePlayer.posZ + zz);
                        if (doWhitelist.getObject() && autoWhitelist.getObject() && whitelistedPositions.isEmpty() && mc.theWorld.getBlockState(bp).getBlock() == Blocks.bed) {
                            whitelistedPositions.add(bp);
                            ChatUtil.sendChat("Whitelisted bed at " + bp.getX() + ", " + bp.getY() + ", " + bp.getZ());
                        }
                        if (whitelistedPositions.contains(bp) && doWhitelist.getObject()) continue;
                        if (mc.theWorld.getBlockState(bp).getBlock() == Blocks.bed && comboValue.isSelected("Bed") &&
                                (whitelistedPositions.contains(bp.add(1, 0, 0)) ||
                                        whitelistedPositions.contains(bp.add(-1, 0, 0)) ||
                                        whitelistedPositions.contains(bp.add(0, 0, 1)) ||
                                        whitelistedPositions.contains(bp.add(0, 0, -1))) && doWhitelist.getObject()) continue;
                        if (mc.theWorld.getBlockState(bp).getBlock() == Blocks.bed && comboValue.isSelected("Bed")) pos = bp;
                        if (mc.theWorld.getBlockState(bp).getBlock() == Blocks.cake && comboValue.isSelected("Cake")) pos = bp;
                        if (mc.theWorld.getBlockState(bp).getBlock() == Blocks.dragon_egg && comboValue.isSelected("Egg")) pos = bp;
                        if (pos != null) break;
                    }
                    if (pos != null) break;
                }
                if (pos != null) break;
            }
            if (pos != null) {
                float[] rots = RotationUtil.faceVector(RotationUtil.getBlockVecCenter(pos));
                if (throughWallsBypass.getObject() && ServerUtil.isHypixel() &&
                        !(mc.thePlayer.ticksExisted % 3 == 0 && throughWallsBypass.getObject() && comboValue.isSelected("Bed") &&
                        mc.theWorld.getBlockState(pos).getBlock() == Blocks.bed)) {
                    pos = null;
                    return;
                }
                e.yaw = rots[0];
                e.pitch = rots[1];
                mc.thePlayer.rotationYawHead = rots[0];
                mc.thePlayer.rotationPitchHead = rots[1];
                if (!ClientModule.otherRotations.getObject()) mc.thePlayer.renderYawOffset = rots[0];
            }
        }
    };

}