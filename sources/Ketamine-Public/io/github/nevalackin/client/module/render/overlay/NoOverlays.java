package io.github.nevalackin.client.module.render.overlay;

import io.github.nevalackin.client.module.Category;
import io.github.nevalackin.client.module.Module;
import io.github.nevalackin.client.event.render.overlay.RenderGameOverlayEvent;
import io.github.nevalackin.client.property.BooleanProperty;
import io.github.nevalackin.homoBus.Listener;
import io.github.nevalackin.homoBus.annotations.EventLink;

public final class NoOverlays extends Module {

    private final BooleanProperty bossHealthBarProperty = new BooleanProperty("Boss Health Bar", false);

    public NoOverlays() {
        super("No Overlays", Category.RENDER, Category.SubCategory.RENDER_OVERLAY);

        this.register(this.bossHealthBarProperty);
    }

    @EventLink
    private final Listener<RenderGameOverlayEvent> onRender = event -> {
        event.setRenderBossHealth(this.bossHealthBarProperty.getValue());
    };

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
