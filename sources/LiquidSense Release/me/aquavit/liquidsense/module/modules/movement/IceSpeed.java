package me.aquavit.liquidsense.module.modules.movement;

import me.aquavit.liquidsense.event.EventTarget;
import me.aquavit.liquidsense.event.events.UpdateEvent;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;
import me.aquavit.liquidsense.utils.block.BlockUtils;
import me.aquavit.liquidsense.value.ListValue;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

@ModuleInfo(name = "IceSpeed", description = "Allows you to walk faster on ice.", category = ModuleCategory.MOVEMENT)
public class IceSpeed extends Module {

    private final ListValue modeValue = new ListValue("Mode", new String[] {"NCP", "AAC", "Spartan"}, "NCP");

    @Override
    public void onEnable() {
        if(modeValue.get().equalsIgnoreCase("NCP")) {
            Blocks.ice.slipperiness = 0.39F;
            Blocks.packed_ice.slipperiness = 0.39F;
        }
        super.onEnable();
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        final String mode = modeValue.get();

        if(mode.equalsIgnoreCase("NCP")) {
            Blocks.ice.slipperiness = 0.39F;
            Blocks.packed_ice.slipperiness = 0.39F;
        }else{
            Blocks.ice.slipperiness = 0.98F;
            Blocks.packed_ice.slipperiness = 0.98F;
        }

        if(mc.thePlayer.onGround && !mc.thePlayer.isOnLadder() && !mc.thePlayer.isSneaking() && mc.thePlayer.isSprinting() && mc.thePlayer.movementInput.moveForward > 0D) {
            if(mode.equalsIgnoreCase("AAC")) {
                final Material material = BlockUtils.getMaterial(mc.thePlayer.getPosition().down());

                if(material == Material.ice || material == Material.packedIce) {
                    mc.thePlayer.motionX *= 1.342D;
                    mc.thePlayer.motionZ *= 1.342D;
                    Blocks.ice.slipperiness = 0.6F;
                    Blocks.packed_ice.slipperiness = 0.6F;
                }
            }

            if(mode.equalsIgnoreCase("Spartan")) {
                final Material material = BlockUtils.getMaterial(mc.thePlayer.getPosition().down());

                if(material == Material.ice || material == Material.packedIce) {
                    final Block upBlock = BlockUtils.getBlock(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY + 2D, mc.thePlayer.posZ));

                    if(!(upBlock instanceof BlockAir)) {
                        mc.thePlayer.motionX *= 1.342D;
                        mc.thePlayer.motionZ *= 1.342D;
                    }else{
                        mc.thePlayer.motionX *= 1.18D;
                        mc.thePlayer.motionZ *= 1.18D;
                    }

                    Blocks.ice.slipperiness = 0.6F;
                    Blocks.packed_ice.slipperiness = 0.6F;
                }
            }
        }
    }

    @Override
    public void onDisable() {
        Blocks.ice.slipperiness = 0.98F;
        Blocks.packed_ice.slipperiness = 0.98F;
        super.onDisable();
    }
}
