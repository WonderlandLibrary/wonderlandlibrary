package tech.atani.client.feature.module.impl.hud;

import tech.atani.client.feature.module.storage.ModuleStorage;
import tech.atani.client.listener.event.minecraft.render.Render2DEvent;
import tech.atani.client.listener.radbus.Listen;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.ModuleData;
import tech.atani.client.feature.module.data.enums.Category;
import tech.atani.client.feature.module.impl.hud.clientOverlay.IClientOverlayComponent;
import tech.atani.client.utility.math.atomic.AtomicFloat;

import java.util.ArrayList;
import java.util.Comparator;

@ModuleData(name = "ClientOverlay", description = "Displays a neat little overlay", category = Category.HUD)
public class ClientOverlay extends Module {

    private ArrayList<IClientOverlayComponent> clientOverlayComponents = new ArrayList<>();

    @Listen
    public void on2D(Render2DEvent render2DEvent) {
        AtomicFloat leftY = new AtomicFloat(0);
        AtomicFloat rightY = new AtomicFloat(0);
        if(clientOverlayComponents.isEmpty()) {
            clientOverlayComponents.add(ModuleStorage.getInstance().getByClass(ModuleList.class));
            clientOverlayComponents.add(ModuleStorage.getInstance().getByClass(WaterMark.class));
        }
        clientOverlayComponents.sort(Comparator.comparingInt(IClientOverlayComponent::getPriority));
        for(IClientOverlayComponent clientOverlayComponent : clientOverlayComponents) {
            clientOverlayComponent.draw(render2DEvent, leftY, rightY);
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
