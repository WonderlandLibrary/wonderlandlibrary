
package Method.Client.module.misc;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.Screens.SubGui;
import Method.Client.utils.system.Connection;
import org.lwjgl.input.Keyboard;

import static Method.Client.Main.setmgr;

public class Antipacket extends Module {

    /////////////////////

    public Antipacket() {
        super("Antipacket", Keyboard.KEY_NONE, Category.MISC, "Cancel Packets");
    }

    Setting Gui = setmgr.add(new Setting("Gui", this, Main.AntiPacketgui));


    @Override
    public boolean onPacket(Object packet, Connection.Side side) {
        for (SubGui.SelectedThing selectedThing : Main.AntiPacketgui.listGui.list) {
            if (selectedThing.isSelected)
                if (packet.getClass().isInstance(selectedThing.packet))
                    return false;
        }
        return true;
    }
}
