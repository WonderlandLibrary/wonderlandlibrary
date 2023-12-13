package io.github.nevalackin.client.module.misc.world;

import io.github.nevalackin.client.module.Category;
import io.github.nevalackin.client.module.Module;
import io.github.nevalackin.client.event.player.UpdatePositionEvent;
import io.github.nevalackin.client.event.world.PlayerDamageBlockEvent;
import io.github.nevalackin.client.property.BooleanProperty;
import io.github.nevalackin.client.property.DoubleProperty;
import io.github.nevalackin.homoBus.Listener;
import io.github.nevalackin.homoBus.annotations.EventLink;

public final class FastBreak extends Module {

    private final DoubleProperty breakPercentageProperty = new DoubleProperty("Break Percentage", 70, 1, 100, 1);
    private final BooleanProperty noHitDelayProperty = new BooleanProperty("No Hit Delay", true);

    public FastBreak() {
        super("Fast Break", Category.MISC, Category.SubCategory.MISC_WORLD);

        this.register(this.breakPercentageProperty, this.noHitDelayProperty);
    }

    @EventLink
    private final Listener<PlayerDamageBlockEvent> onPlayerDamageBlock = event -> {
        if (this.mc.playerController.curBlockDamageMP >= this.breakPercentageProperty.getValue().floatValue() / 100.0f) {
            this.mc.playerController.curBlockDamageMP = 1.0F;
        }
    };

    @EventLink
    private final Listener<UpdatePositionEvent> onUpdate = event -> {
        if (event.isPre() && this.noHitDelayProperty.getValue()) {
            this.mc.playerController.setBlockHitDelay(0);
        }
    };

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
