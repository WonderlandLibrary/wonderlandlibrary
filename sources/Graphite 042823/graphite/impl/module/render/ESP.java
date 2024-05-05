package graphite.impl.module.render;

import graphite.api.event.Listen;
import graphite.api.module.Module;
import graphite.api.module.util.Category;
import graphite.api.module.util.ModuleInfo;
import graphite.api.property.impl.ColourProperty;
import graphite.api.property.impl.MultiSelectionEnumProperty;
import graphite.impl.event.render.Render3DEvent;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.Arrays;

@ModuleInfo(name = "ESP", category = Category.Render, key = Keyboard.KEY_V)
public class ESP extends Module {
    private final MultiSelectionEnumProperty<Targets> targets = new MultiSelectionEnumProperty<>("Targets", Arrays.asList(Targets.PLAYERS, Targets.ITEMS), Targets.values());
    private final MultiSelectionEnumProperty<Elements> elements = new MultiSelectionEnumProperty<>("Elements", Arrays.asList(Elements.BOX, Elements.TEXT, Elements.HEALTHBAR, Elements.ARMORBAR), Elements.values());

    private final ColourProperty boxColor = new ColourProperty("Box color", new Color(255, 255, 255).getRGB());

    public ESP() {
        this.register(targets, elements, boxColor);
    }

    @Listen
    public void onRender3D(Render3DEvent event) {
    }

    private enum Targets {
        PLAYERS("Players"),
        MONSTERS("Monsters"),
        ANIMALS("Animals"),
        NEUTRALS("Neutrals"),
        ITEMS("Items");

        private final String name;

        Targets(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    private enum Elements {
        BOX("Box"),
        HEALTHBAR("Health bar"),
        ARMORBAR("Armor bar"),
        TEXT("Text");

        private final String name;

        Elements(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }
}
