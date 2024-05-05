package rip.helium.gui.hud;

//import javafx.scene.control.Toggle;

import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import rip.helium.Helium;
import rip.helium.event.minecraft.KeyPressEvent;
import rip.helium.event.minecraft.RenderOverlayEvent;
import rip.helium.gui.hud.element.Element;
import rip.helium.gui.hud.element.impl.ToggledList;
import rip.helium.utils.font.FontRenderer;
import rip.helium.utils.font.Fonts;

import java.util.ArrayList;

public class Hud {
    public final ArrayList<Element> elements;
    protected Minecraft mc;
    FontRenderer logo;
    ScaledResolution resolution;
    String c = "Null";
    private rip.helium.cheat.impl.visual.Hud hud;
    private final double lastPosX;
    private final double lastPosZ;
    private final ArrayList<Double> distances;

    public Hud() {
        this.elements = new ArrayList<Element>();
        this.mc = Minecraft.getMinecraft();
        this.lastPosX = Double.NaN;
        this.lastPosZ = Double.NaN;
        this.distances = new ArrayList<Double>();
        this.logo = new FontRenderer(Fonts.fontFromTTF(new ResourceLocation("client/ElliotSans-Bold.ttf"), 20.0f, 0), true, true);
        this.resolution = new ScaledResolution(Minecraft.getMinecraft());
        this.elements.add(new ToggledList());
        Helium.eventBus.register(this);
    }

    @Collect
    public void render(final RenderOverlayEvent event) {
        if (this.hud == null) {
            this.hud = (rip.helium.cheat.impl.visual.Hud) Helium.instance.cheatManager.getCheatRegistry().get("Hud");
        }
        final ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
        this.elements.forEach(element -> element.drawElement(false));
    }

    @Collect
    public void onKeyPressed(final KeyPressEvent event) {
        this.elements.forEach(module -> module.onKeyPress(event.getKeyCode()));
    }
}
