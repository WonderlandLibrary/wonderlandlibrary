package rip.helium.cheat.impl.movement;

import net.minecraft.init.Blocks;
import rip.helium.cheat.Cheat;
import rip.helium.cheat.CheatCategory;

public class IceSpeed extends Cheat {

    public IceSpeed() {
        super("IceSpeed", "Walk faster in water.", CheatCategory.MOVEMENT);
    }


    @Override
    protected void onEnable() {
        Blocks.ice.slipperiness = 0.4F;
        Blocks.packed_ice.slipperiness = 0.4F;
    }

    @Override
    protected void onDisable() {
        Blocks.ice.slipperiness = 0.98F;
        Blocks.packed_ice.slipperiness = 0.98F;
    }
}
