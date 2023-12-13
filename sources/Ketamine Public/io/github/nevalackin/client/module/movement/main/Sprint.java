package io.github.nevalackin.client.module.movement.main;

import io.github.nevalackin.client.module.Category;
import io.github.nevalackin.client.module.Module;
import io.github.nevalackin.client.event.player.SendSprintStateEvent;
import io.github.nevalackin.client.util.movement.MovementUtil;
import io.github.nevalackin.homoBus.Listener;
import io.github.nevalackin.homoBus.annotations.EventLink;

public final class Sprint extends Module {
    public Sprint() {
        super("Sprint", Category.MOVEMENT, Category.SubCategory.MOVEMENT_MAIN);
    }

    @EventLink
    private final Listener<SendSprintStateEvent> onSendSprint = event -> {
        if (MovementUtil.canSprint(this.mc.thePlayer, false)) {
            event.setSprintState(true);
            this.mc.thePlayer.setSprinting(true);
        }
    };

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
