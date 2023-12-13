package today.flux.module.implement.World;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import today.flux.event.DamageBlockEvent;
import today.flux.event.PacketSendEvent;
import today.flux.event.UpdateEvent;
import today.flux.module.Category;
import today.flux.module.Module;
import today.flux.module.value.BooleanValue;
import today.flux.module.value.FloatValue;
import today.flux.utility.BlockUtils;

public class FastBreak extends Module {

    private boolean bzs = false;
    private float bzx = 0.0F;
    public BlockPos blockPos;
    public EnumFacing facing;
    public static FloatValue speed = new FloatValue("FastBreak", "Speed", 1.4f, 1.0f, 2.0f, 0.1f);
    public static FloatValue dmg = new FloatValue("FastBreak", "Damage", 0.8f, 0.1f, 1.0f, 0.1f);

    public FastBreak() {
        super("FastBreak", Category.World, false);
    }


    @EventTarget
    public void onBreak(DamageBlockEvent event) {

    }

    @EventTarget
    public void onDamageBlock(PacketSendEvent event) {
        if (event.packet instanceof C07PacketPlayerDigging && !mc.playerController.extendedReach() && mc.playerController != null) {
            C07PacketPlayerDigging c07PacketPlayerDigging = (C07PacketPlayerDigging) event.packet;
            if (c07PacketPlayerDigging.getStatus() == C07PacketPlayerDigging.Action.START_DESTROY_BLOCK) {
                bzs = true;
                blockPos = c07PacketPlayerDigging.getPosition();
                facing = c07PacketPlayerDigging.getFacing();
                bzx = 0;
            } else if (c07PacketPlayerDigging.getStatus() == C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK || c07PacketPlayerDigging.getStatus() == C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK) {
                bzs = false;
                blockPos = null;
                facing = null;
            }
        }
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (mc.playerController.getCurBlockDamageMP() >= speed.getValueState()) {
            mc.playerController.setCurBlockDamageMP(1);
            boolean item = mc.thePlayer.getCurrentEquippedItem() == null;
            mc.thePlayer.addPotionEffect(new PotionEffect(Potion.digSpeed.getId(), 20, item ? 1 : 0));
        }
        if (mc.playerController.extendedReach()) {
            mc.playerController.blockHitDelay = 0;
        } else {
            if (bzs) {
                Block block = mc.theWorld.getBlockState(blockPos).getBlock();
                bzx = (float) bzx + (float) (block.getPlayerRelativeBlockHardness(mc.thePlayer, mc.theWorld, blockPos) * speed.getValueState());
                if (bzx >= 1.0F) {
                    mc.theWorld.setBlockState(blockPos, Blocks.air.getDefaultState(), 11);
                    mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockPos, facing));
                    bzx = 0.0F;
                    bzs = false;
                }
            }
        }
    }


}