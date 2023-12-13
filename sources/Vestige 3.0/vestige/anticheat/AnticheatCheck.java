package vestige.anticheat;

import com.mojang.realmsclient.gui.ChatFormatting;
import lombok.Getter;
import vestige.Vestige;
import vestige.module.impl.misc.AnticheatModule;
import vestige.util.IMinecraft;
import vestige.util.misc.LogUtil;

@Getter
public abstract class AnticheatCheck implements IMinecraft {

    private final String name;

    protected final ACPlayer player;

    private double buffer;

    private static AnticheatModule module = Vestige.instance.getModuleManager().getModule(AnticheatModule.class);

    public AnticheatCheck(String name, ACPlayer player) {
        this.name = name;
        this.player = player;
    }

    public abstract void check();

    public final double increaseBuffer() {
        return ++buffer;
    }

    public final double increaseBufferBy(double amount) {
        return buffer += Math.max(0, amount);
    }

    public final double decreaseBuffer() {
        return buffer = Math.max(buffer - 1, 0);
    }

    public final double decreaseBufferBy(double amount) {
        return buffer = Math.max(buffer - Math.max(0, amount), 0);
    }

    public final void resetBuffer() {
        buffer = 0;
    }

    public final void alert() {
        if(module.isEnabled()) {
            LogUtil.addChatMessage(player.getEntity().getGameProfile().getName() + ChatFormatting.WHITE + " has failed " + name);
        }
    }

    public final void alert(String message) {
        if(module.isEnabled()) {
            LogUtil.addChatMessage(player.getEntity().getGameProfile().getName() + ChatFormatting.WHITE + " has failed " + name + ChatFormatting.GRAY + " | " + message);
        }
    }

    public double round(double value) {
        return Math.round(value * 1000) / 1000.0;
    }

}
