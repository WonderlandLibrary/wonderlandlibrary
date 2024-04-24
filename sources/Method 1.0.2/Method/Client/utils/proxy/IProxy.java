
package Method.Client.utils.proxy;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;

public interface IProxy
{
    /**
     * Fml life cycle event for Initialization. This phase is good for registering event listeners, for registering things that depend on things in pre-init from other mods (like
     * recipes, advancements and such.)
     *
     * @param event the event
     */
    void init(FMLInitializationEvent event);
}
