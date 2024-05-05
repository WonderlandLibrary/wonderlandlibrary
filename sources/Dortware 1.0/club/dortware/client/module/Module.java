package club.dortware.client.module;

import club.dortware.client.Client;
import club.dortware.client.module.annotations.ModuleData;
import club.dortware.client.module.interfaces.IToggleable;
import net.minecraft.client.Minecraft;

import java.util.Random;

/**
 * @author Dort
 */
public abstract class Module implements IToggleable {
    protected static final Minecraft mc = Minecraft.getMinecraft();
    private final ModuleData moduleData;
    private boolean toggled;
    private int keyBind;
    protected static final Random RANDOM = new Random();
    public Module() {
        moduleData = this.getClass().getAnnotation(ModuleData.class);
        keyBind = moduleData.defaultKeyBind();
        setup();
    }

    public ModuleData getModuleData() {
        return moduleData;
    }

    public int getKeyBind() {
        return keyBind;
    }

    public void setKeyBind(int keyBind) {
        this.keyBind = keyBind;
    }

    @Override
    public void toggle() {
        toggled = !toggled;
        mc.timer.timerSpeed = 1F;
        if (toggled) {
            Client.INSTANCE.getEventBus().register(this);
            onEnable();
        }
        else {
            Client.INSTANCE.getEventBus().unregister(this);
            onDisable();
            mc.thePlayer.setBlocking(false);
        }

        onToggled();

    }

    public void setup() {}

    @Override
    public void onToggled() {}

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

    public boolean isToggled() {
        return toggled;
    }

}
