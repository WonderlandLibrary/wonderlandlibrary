package Method.Client.module.misc;

import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.system.Connection;
import Method.Client.utils.system.Wrapper;
import Method.Client.utils.visual.ChatUtils;
import joptsimple.internal.Strings;
import net.minecraft.network.play.client.CPacketTabComplete;
import net.minecraft.network.play.server.SPacketTabComplete;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PluginsGetter extends Module {


    public PluginsGetter() {
        super("PluginsGetter", Keyboard.KEY_NONE, Category.MISC, "Trys Plugins Getter");
    }

    @Override
    public void onEnable() {
        if (mc.player == null) {
            return;
        }
        Wrapper.INSTANCE.sendPacket(new CPacketTabComplete("/", null, false));
        super.onEnable();
    }


    @Override
    public boolean onPacket(Object packet, Connection.Side side) {
        if (packet instanceof SPacketTabComplete) {
            SPacketTabComplete s3APacketTabComplete = (SPacketTabComplete) packet;

            List<String> plugins = new ArrayList<>();
            String[] commands = s3APacketTabComplete.getMatches();

            for (String s : commands) {
                String[] command = s.split(":");

                if (command.length > 1) {
                    String pluginName = command[0].replace("/", "");

                    if (!plugins.contains(pluginName)) {
                        plugins.add(pluginName);
                    }
                }
            }

            Collections.sort(plugins);

            if (!plugins.isEmpty()) {
                ChatUtils.message("Plugins \u00a77(\u00a78" + plugins.size() + "\u00a77): \u00a79" + Strings.join(plugins.toArray(new String[0]), "\u00a77, \u00a79"));
            } else {
                ChatUtils.error("No plugins found.");
            }
            this.toggle();
        }
        return true;
    }
}
