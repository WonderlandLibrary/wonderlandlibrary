package club.dortware.client.module.impl.player;

import club.dortware.client.Client;
import club.dortware.client.event.impl.UpdateEvent;
import club.dortware.client.manager.impl.PropertyManager;
import club.dortware.client.module.Module;
import club.dortware.client.module.annotations.ModuleData;
import club.dortware.client.module.enums.ModuleCategory;
import club.dortware.client.property.impl.BooleanProperty;
import club.dortware.client.property.impl.DoubleProperty;
import com.google.common.eventbus.Subscribe;
import net.minecraft.network.Packet;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

@ModuleData(name = "Fast Interact", category = ModuleCategory.PLAYER)
public class FastInteract extends Module {

    @Override
    public void setup() {
        PropertyManager propertyManager = Client.INSTANCE.getPropertyManager();
        propertyManager.add(new BooleanProperty<>("Fast Place", this, true));
        propertyManager.add(new DoubleProperty<>("Haste Level", this, 1, -1, 3, true));
    }

    @Subscribe
    public void handlePlayerUpdate(UpdateEvent event) {
        if (event.isPre()) {
            PropertyManager propertyManager = Client.INSTANCE.getPropertyManager();
            Double hasteLevel = (Double) propertyManager.getProperty(this, "Haste Level").getValue();
            Boolean fastPlace = (Boolean) propertyManager.getProperty(this, "Fast Place").getValue();

            if (fastPlace) {
                mc.rightClickDelayTimer = 0;
            }

            if (hasteLevel > 0) {
                mc.thePlayer.addPotionEffect(new PotionEffect(Potion.digSpeed.id, 0, hasteLevel.intValue()));
            }
        }
    }

}
