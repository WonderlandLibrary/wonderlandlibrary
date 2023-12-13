package dev.rise.module.impl.movement;

import dev.rise.event.impl.packet.PacketSendEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.setting.impl.BooleanSetting;
import dev.rise.util.player.PlayerUtil;
import net.minecraft.block.BlockSoulSand;
import net.minecraft.network.play.client.C03PacketPlayer;

@ModuleInfo(name = "Terrain", description = "Prevents certain blocks from slowing you down", category = Category.MOVEMENT)
public class Terrain extends Module {
    private final BooleanSetting ladder = new BooleanSetting("Ladders", this, true);
    private final BooleanSetting web = new BooleanSetting("Webs", this, true);
    private final BooleanSetting soulSand = new BooleanSetting("Soul Sand", this, true);
    private final BooleanSetting vines = new BooleanSetting("Vines", this, true);
    private final BooleanSetting slime = new BooleanSetting("Slime", this, true);
}
