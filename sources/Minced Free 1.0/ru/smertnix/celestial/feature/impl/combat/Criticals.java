package ru.smertnix.celestial.feature.impl.combat;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import ru.smertnix.celestial.event.EventTarget;
import ru.smertnix.celestial.event.events.impl.player.EventUpdate;
import ru.smertnix.celestial.feature.Feature;
import ru.smertnix.celestial.feature.impl.FeatureCategory;

public class Criticals extends Feature {


    public Criticals() {
        super("Criticals", "Позволяют бить с места на Mega Grief и Rip Server", FeatureCategory.Combat);
        addSettings();
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {
    }
}