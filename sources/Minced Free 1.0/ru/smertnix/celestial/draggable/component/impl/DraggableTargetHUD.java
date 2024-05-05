package ru.smertnix.celestial.draggable.component.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import ru.smertnix.celestial.Celestial;
import ru.smertnix.celestial.draggable.component.DraggableComponent;
import ru.smertnix.celestial.feature.impl.combat.AttackAura;

public class DraggableTargetHUD extends DraggableComponent {

    public DraggableTargetHUD() {
        super("TargetHUD", 350, 25, 1, 1);
    }

    @Override
    public boolean allowDraw() {
        return (Celestial.instance.featureManager.getFeature(AttackAura.class).isEnabled() && AttackAura.target != null) || (Minecraft.getMinecraft().player != null && Minecraft.getMinecraft().currentScreen instanceof GuiChat);
    }
}
