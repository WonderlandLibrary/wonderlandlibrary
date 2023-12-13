package io.github.nevalackin.client.module.render.world;

import io.github.nevalackin.client.module.Category;
import io.github.nevalackin.client.module.Module;
import io.github.nevalackin.client.property.ColourProperty;

public final class WorldColour extends Module {

    private final ColourProperty colourProperty = new ColourProperty("Colour", 0xFFFFFFFF);

    public WorldColour() {
        super("World Colour", Category.RENDER, Category.SubCategory.RENDER_WORLD);

        this.register(this.colourProperty);
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
