package today.flux.module;

import com.darkmagician6.eventapi.EventManager;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;

public class SubModule {
    @Getter @Setter
    private String name, parentModName;
    @Getter
    private boolean isEnabled;
    public static Minecraft mc = Minecraft.getMinecraft();

    public SubModule(String name, String mainmodule) {
        this.name = name;
        this.parentModName = mainmodule;
    }

    public void onEnable() {
        EventManager.register(this);
    }

    public void onDisable() {
        EventManager.unregister(this);
    }

    public void setEnabled(boolean enabled) {
        if (enabled) {
            this.isEnabled = true;
            this.onEnable();
        } else {
            this.isEnabled = false;
            this.onDisable();
        }
    }
}
