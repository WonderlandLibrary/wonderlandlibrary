package xyz.northclient.features.modules;

import xyz.northclient.draggable.impl.Watermark;
import xyz.northclient.features.AbstractModule;
import xyz.northclient.features.Category;
import xyz.northclient.features.EventLink;
import xyz.northclient.features.ModuleInfo;
import xyz.northclient.features.events.UpdateEvent;
import xyz.northclient.features.values.BoolValue;
import xyz.northclient.features.values.DoubleValue;
import xyz.northclient.features.values.Mode;
import xyz.northclient.features.values.ModeValue;

@ModuleInfo(name = "Animations",description = "Sword modifications",category = Category.WORLD)
public class Animations extends AbstractModule {

    public static ModeValue animation;
    public static BoolValue betterClick;
    public static DoubleValue scale;

    public Animations() {
        animation = new ModeValue("Animation", this)
                .add(new AnimationMode("Minecraft",this))
                .add(new AnimationMode("Classic",this))
                .add(new AnimationMode("Apollo",this))
                .add(new AnimationMode("Exhi",this))
                .add(new AnimationMode("Astolfo",this))
                .add(new AnimationMode("Swang",this))
                .add(new AnimationMode("Swong",this))
                .setDefault("Minecraft");

        betterClick = new BoolValue("Better Click",this)
                .setDefault(true);

        scale = new DoubleValue("Scale",this)
                .setDefault(1.0)
                .setMax(5.0)
                .setMin(0.1)
                .setIncrement(0.1);
    }


    @EventLink
    public void onUpdate(UpdateEvent event) {
        setSuffix(animation.get().getName());
    }

    public static class AnimationMode extends Mode<Animations> {

        public AnimationMode(String name, AbstractModule parent) {
            super(name, parent);
        }
    }
}
