package koks.module.misc;

import koks.Koks;
import koks.api.event.Event;
import koks.api.manager.value.annotation.Value;
import koks.api.registry.module.Module;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.optifine.player.CapeUtils;

/**
 * @author kroko
 * @created on 12.02.2021 : 17:30
 */

@Module.Info(name = "IRC", description = "You can chat with other users", category = Module.Category.MISC)
public class IRC extends Module implements Module.NotBypass {

    @Value(name = "Mode", modes = {"Local", "Global"})
    String mode = "Global";

    public IRC() {
        setToggled(true);
    }

    @Override
    @Event.Info
    public void onEvent(Event event) {

    }

    @Override
    public void onEnable() {
        refresh();
    }

    @Override
    public void onDisable() {
        refresh();
    }

    public void refresh() {
        for (EntityPlayer player : getWorld().playerEntities)
            if (player instanceof final AbstractClientPlayer abstractClientPlayer) {
                CapeUtils.reloadCape(abstractClientPlayer);
            }
    }
}
