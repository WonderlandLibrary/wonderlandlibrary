package io.github.nevalackin.client.module.render.world;

import io.github.nevalackin.client.module.Category;
import io.github.nevalackin.client.module.Module;
import io.github.nevalackin.client.event.render.world.DrawSelectedBoundingBoxEvent;
import io.github.nevalackin.client.property.BooleanProperty;
import io.github.nevalackin.client.property.ColourProperty;
import io.github.nevalackin.client.util.render.ColourUtil;
import io.github.nevalackin.homoBus.Listener;
import io.github.nevalackin.homoBus.annotations.EventLink;

public final class BlockOverlay extends Module {

    private final ColourProperty colourProperty = new ColourProperty("Colour",
                                                                     ColourUtil.overwriteAlphaComponent(ColourUtil.getClientColour(), 0x50));
    private final BooleanProperty outlineProperty = new BooleanProperty("Outline", true);

    public BlockOverlay() {
        super("Block Overlay", Category.RENDER, Category.SubCategory.RENDER_WORLD);

        this.register(this.colourProperty, this.outlineProperty);
    }

    @EventLink
    private final Listener<DrawSelectedBoundingBoxEvent> onDrawSelectedBB = event -> {
        event.setFilled(true);
        event.setOutlineWidth(this.outlineProperty.getValue() ? 1 : 0);
        event.setColour(this.colourProperty.getValue());
    };

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
