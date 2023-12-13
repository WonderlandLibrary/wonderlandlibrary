package io.github.nevalackin.client.module.render.overlay;

import io.github.nevalackin.client.module.Category;
import io.github.nevalackin.client.module.Module;
import io.github.nevalackin.client.event.render.game.GetFOVEvent;
import io.github.nevalackin.homoBus.Listener;
import io.github.nevalackin.homoBus.annotations.EventLink;

public final class NoFOV extends Module {
    public NoFOV() {
        super("No FOV", Category.RENDER, Category.SubCategory.RENDER_OVERLAY);
    }

    @EventLink
    private final Listener<GetFOVEvent> onGetFOV = event -> {
        event.setUseModifier(false);
    };

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
