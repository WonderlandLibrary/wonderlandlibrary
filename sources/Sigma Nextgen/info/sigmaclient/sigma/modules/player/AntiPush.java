package info.sigmaclient.sigma.modules.player;

import info.sigmaclient.sigma.config.values.ModeValue;
import info.sigmaclient.sigma.event.net.PacketEvent;
import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import net.minecraft.block.AirBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import java.util.Random;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class AntiPush extends Module {
    public AntiPush() {
        super("AntiPush", Category.Player, "I hate piston.");
    }
}
