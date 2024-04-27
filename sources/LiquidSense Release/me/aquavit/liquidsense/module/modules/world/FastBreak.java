package me.aquavit.liquidsense.module.modules.world;

import me.aquavit.liquidsense.event.EventTarget;
import me.aquavit.liquidsense.event.events.PacketEvent;
import me.aquavit.liquidsense.event.events.UpdateEvent;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;
import me.aquavit.liquidsense.value.FloatValue;
import me.aquavit.liquidsense.value.IntegerValue;
import me.aquavit.liquidsense.value.ListValue;
import me.aquavit.liquidsense.value.Value;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

@ModuleInfo(name = "FastBreak", description = "Allows you to break blocks faster.", category = ModuleCategory.WORLD)
public class FastBreak extends Module {
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Damage", "Haste", "Packet"}, "Packet");
    private final Value<Float> blockDamage  = new FloatValue("BlockDamage ", 0.8F, 0.1F, 1F).displayable(() -> modeValue.get().equals("Damage"));
    private final Value<Integer> hasteLevel = new IntegerValue("HasteLevel", 2, 1, 256).displayable(() -> modeValue.get().equals("Haste"));
    private final Value<Float> breakSpeed = new FloatValue("BreakSpeed", 1.4F, 1F, 2F).displayable(() -> modeValue.get().equals("Packet"));

    private boolean bzs = false;
    private float bzx = 0.0f;
    public BlockPos blockPos;
    public EnumFacing facing;

    @EventTarget
    public void onPacket(PacketEvent event) {
        final Packet<?> packet = event.getPacket();

        if (modeValue.get().equals("Packet")) {
            if (packet instanceof C07PacketPlayerDigging && mc.playerController != null) {
                C07PacketPlayerDigging digging = (C07PacketPlayerDigging) packet;
                if (digging.getStatus() == C07PacketPlayerDigging.Action.START_DESTROY_BLOCK) {
                    bzs = true;
                    blockPos = digging.getPosition();
                    facing = digging.getFacing();
                    bzx = 0.0f;
                } else if (digging.getStatus() == C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK || digging.getStatus() == C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK) {
                    bzs = false;
                    blockPos = null;
                    facing = null;
                }
            }
        }
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (mc.playerController.extendedReach()) {
            mc.playerController.blockHitDelay = 0;
        } else
        switch (modeValue.get()){
            case "Damage": {
                mc.playerController.blockHitDelay = 0;

                if (mc.playerController.curBlockDamageMP > blockDamage.get())
                    mc.playerController.curBlockDamageMP = 1F;

                if (Fucker.currentDamage > blockDamage.get())
                    Fucker.currentDamage = 1F;
                break;
            }

            case "Haste": {
                mc.thePlayer.addPotionEffect(new PotionEffect(Potion.harm.id, 1337, hasteLevel.get()));
                break;
            }

            case "Packet": {
                if(bzs) {
                    Block block = mc.theWorld.getBlockState(blockPos).getBlock();
                    bzx += (block.getPlayerRelativeBlockHardness(mc.thePlayer, mc.theWorld, blockPos) * breakSpeed.get());
                    if (bzx >= 1.0f) {
                        mc.theWorld.setBlockState(blockPos, Blocks.air.getDefaultState(), 11);
                        mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockPos, facing));
                        bzx = 0.0f;
                        bzs = false;
                    }
                }
                break;
            }
        }

    }

}
