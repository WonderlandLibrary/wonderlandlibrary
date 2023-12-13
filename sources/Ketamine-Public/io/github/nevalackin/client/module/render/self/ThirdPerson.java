package io.github.nevalackin.client.module.render.self;

import io.github.nevalackin.client.module.Category;
import io.github.nevalackin.client.module.Module;
import io.github.nevalackin.client.event.render.game.GetThirdPersonViewEvent;
import io.github.nevalackin.client.event.render.world.RayTraceCameraEvent;
import io.github.nevalackin.client.property.BooleanProperty;
import io.github.nevalackin.client.property.DoubleProperty;
import io.github.nevalackin.homoBus.Listener;
import io.github.nevalackin.homoBus.annotations.EventLink;
import org.lwjgl.input.Mouse;

public final class ThirdPerson extends Module {

    private final BooleanProperty onKeyProperty = new BooleanProperty("On Middle Click", true);
    private final BooleanProperty viewClipProperty = new BooleanProperty("View Clip", true);
    private final DoubleProperty distanceProperty = new DoubleProperty("Distance", 7.0, 1.0, 20.0, 0.5);

    public ThirdPerson() {
        super("Thirdperson", Category.RENDER, Category.SubCategory.RENDER_SELF);

        this.register(this.viewClipProperty, this.onKeyProperty, this.distanceProperty);
    }

    @EventLink
    private final Listener<GetThirdPersonViewEvent> onOrientCamera = event -> {
        if (this.onKeyProperty.getValue() && Mouse.isButtonDown(2))
            event.setThirdPersonView(1);
    };

    @EventLink
    private final Listener<RayTraceCameraEvent> onRayTrace = event -> {
        event.setDistance(this.distanceProperty.getValue());

        if (this.viewClipProperty.getValue())
            event.setCancelled();
    };

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    private enum DrawServerRotations {
        OFF("Off"),
        MODEL("Model"),
        GHOST("Ghost");

        private final String name;

        DrawServerRotations(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }
}
