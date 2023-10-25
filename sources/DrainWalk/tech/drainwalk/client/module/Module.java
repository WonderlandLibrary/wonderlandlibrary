package tech.drainwalk.client.module;

import com.darkmagician6.eventapi.EventManager;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import tech.drainwalk.animation.Animation;
import tech.drainwalk.client.module.category.Category;
import tech.drainwalk.client.module.category.Type;
import tech.drainwalk.client.option.Option;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Module  {
    protected static final Minecraft mc = Minecraft.getMinecraft();
    @Getter
    private final String name;
    @Getter
    private final Category category;
    @Getter
    private Type type = Type.MAIN;
    @Getter
    private String description = "Module haven't description";
    @Getter
    public int key = 0;
    @Setter
    @Getter
    private boolean enabled;
    @Getter
    private final Animation hoveredAnimation = new Animation();
    @Getter
    private final Animation animation = new Animation();

    public Module(String name, Category category) {
        this.name = name;
        this.category = category;
    }

    public Module addDescription(String description) {
        this.description = description;
        return this;
    }

    public Module addKey(int key) {
        this.key = key;
        return this;
    }

    public Module addType(Type type) {
        this.type = type;
        return this;
    }


    public void onEnable() {

    }

    public void onDisable() {

    }

    public void toggle() {
        enabled = !enabled;
        if (enabled) {
            EventManager.register(this);
            System.out.println("ENABLED " + name);
            onEnable();
        } else {
            onDisable();
            System.out.println("DISABLED " + name);
            EventManager.unregister(this);
        }
    }

    @Getter
    private final List<Option<?>> settingList = new ArrayList<>();

    public void register(Option<?> ... settings) {
        settingList.addAll(Arrays.asList(settings));
    }
}