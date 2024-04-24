
package Method.Client.module.misc;

import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.system.Connection;
import com.google.common.eventbus.Subscribe;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.realms.RealmsSharedConstants;
import org.lwjgl.input.Keyboard;

import static Method.Client.Main.setmgr;

public class VersionSpoofer extends Module {

    /////////////////////

    public VersionSpoofer() {
        super("VersionSpoofer", Keyboard.KEY_NONE, Category.MISC, "Version Spoofer");
    }

    Setting mode = setmgr.add(new Setting("Mode", this, "1.12.2", "1.7.10", "1.8.9", "1.9",
            "1.12.2", "1.13", "1.14.4", "1.15.1"));

    @Override
    public void onEnable() {
        RealmsSharedConstants.NETWORK_PROTOCOL_VERSION = version();
    }

    @Override
    public boolean onDisablePacket(Object packet, Connection.Side side) {
        if (packet instanceof C00Handshake) {
            ((C00Handshake) packet).protocolVersion = version();
        }
        return true;
    }

    public boolean onPacket(Object packet, Connection.Side side) {
        if (packet instanceof C00Handshake) {
            ((C00Handshake) packet).protocolVersion = version();
        }

        return true;
    }

    private int version() {
        if (mode.getValString().equalsIgnoreCase("1.7.10"))
            return 5;
        if (mode.getValString().equalsIgnoreCase("1.8.9"))
            return 47;
        if (mode.getValString().equalsIgnoreCase("1.9"))
            return 107;
        if (mode.getValString().equalsIgnoreCase("1.12.2"))
            return 340;
        if (mode.getValString().equalsIgnoreCase("1.13"))
            return 393;
        if (mode.getValString().equalsIgnoreCase("1.14.4"))
            return 498;
        if (mode.getValString().equalsIgnoreCase("1.15.1"))
            return 575;
        return 340;
    }

}
