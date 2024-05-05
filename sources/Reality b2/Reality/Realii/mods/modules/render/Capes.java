
package Reality.Realii.mods.modules.render;

import net.minecraft.util.ResourceLocation;
import Reality.Realii.event.EventHandler;
import Reality.Realii.event.events.rendering.Shader3DEvent;
import Reality.Realii.managers.FriendManager;
import Reality.Realii.mods.Module;
import Reality.Realii.mods.ModuleType;

public class Capes
        extends Module {
    public Capes() {
        super("Capes", ModuleType.Render);
    }

    @EventHandler
    public void onRender(Shader3DEvent event) {
        
    }
}

