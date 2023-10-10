package Reality.Realii.mods.modules.render;

import Reality.Realii.event.events.world.EventPacketRecieve;
import Reality.Realii.event.events.world.EventPacketSend;
import Reality.Realii.event.value.Numbers;
import Reality.Realii.managers.FriendManager;
import Reality.Realii.mods.Module;
import Reality.Realii.mods.ModuleType;




public class PlayerSize
        extends Module {
	  public static Numbers<Number> Size = new Numbers<>("Player Size", 0.01f, 0.01f, 5f, 3f);
	
	
    public PlayerSize() {
        super("PlayerSize", ModuleType.Render);
        addValues(Size);
    }
    

}

