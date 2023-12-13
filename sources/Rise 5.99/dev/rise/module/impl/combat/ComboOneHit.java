/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.rise.module.impl.combat;

import dev.rise.event.impl.other.AttackEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.setting.impl.NumberSetting;
import dev.rise.util.player.PacketUtil;
import net.minecraft.network.play.client.C02PacketUseEntity;

@ModuleInfo(name = "ComboOneHit", description = "Instantly kills people in the Combo gamemode", category = Category.COMBAT)
public final class ComboOneHit extends Module {

    public final NumberSetting packets = new NumberSetting("Packets", this, 50, 1, 1000, 25);

    @Override
    public void onAttackEvent(final AttackEvent event) {
        for (int i = 0; i < (int) packets.getValue(); i++) {
            PacketUtil.sendPacketWithoutEvent(new C02PacketUseEntity(event.getTarget(), C02PacketUseEntity.Action.ATTACK));
        }
    }
}
