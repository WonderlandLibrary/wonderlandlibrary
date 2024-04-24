package Method.Client.module;

import Method.Client.Main;
import Method.Client.clickgui.component.Component;
import Method.Client.modmaker.CatchCodeExecuter;
import Method.Client.modmaker.CodeExecuter;
import Method.Client.modmaker.Windows.DragableBlock;
import Method.Client.utils.system.Connection;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

import java.io.Serializable;
import java.util.ArrayList;

public class Module implements Serializable {
    protected static Minecraft mc = Minecraft.getMinecraft();
    protected static Minecraft MC = Minecraft.getMinecraft();

    public ArrayList<Integer> keys;
    public boolean cancelPacket;

    private boolean toggled;
    public boolean visible = true;
    private String name;
    private String displayName;
    private final String tooltip;
    private Category category;

    public boolean multiThreaded = false;

    public CodeExecuter codeExecuter;
    public ArrayList<DragableBlock> ActiveBlocks = new ArrayList<>();

    public boolean devMode = false;

    public String changedSetting;

    public Module(String name, int key, Category category, String tooltip) {
        this.name = name;
        this.tooltip = tooltip;
        this.keys = new ArrayList<>();
        this.keys.add(key);
        this.category = category;
        toggled = false;
        this.codeExecuter = new CatchCodeExecuter(this);
        setup();
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    public boolean onPacket(Object packet, Connection.Side side) {
        return this.codeExecuter.onPacket(packet,side);
    }

    public boolean onDisablePacket(Object packet, Connection.Side side) {
        return this.codeExecuter.onDisablePacket(packet);
    }

    public void onToggle() {
    }

    public void toggle() {
        toggled = !toggled;
        Toggle(toggled);
    }

    public void setToggled(boolean Enable) {
        this.toggled = Enable;
        Toggle(Enable);
    }

    private void Toggle(boolean Enable) {
        onToggle();
        this.codeExecuter.onToggle();
        if (Enable) {
            if (!ModuleManager.toggledModules.contains(this)) {
                ModuleManager.toggledModules.add(this);
            }
            try {
                Main.eventBus.register(this);
                this.codeExecuter.loadBlocks();
                Main.eventBus.register(this.codeExecuter);
            } catch (IllegalArgumentException e) {
            }
            onEnable();
            this.codeExecuter.onEnable();
        } else {
            ModuleManager.toggledModules.remove(this);
            try {
                Main.eventBus.unregister(this);
                Main.eventBus.unregister(this.codeExecuter);
            } catch (IllegalArgumentException e) {
            }
            onDisable();
            this.codeExecuter.onDisable();
        }
    }

    public ArrayList<String> fontoptions() {
        ArrayList<String> Fontoptions = new ArrayList<>();
        Fontoptions.add("Arial");
        Fontoptions.add("Impact");
        Fontoptions.add("Times");
        Fontoptions.add("MC");
        return Fontoptions;
    }

    public ArrayList<String> BlockEspOptions() {
        ArrayList<String> BlockOptions = new ArrayList<>();
        BlockOptions.add("Outline");
        BlockOptions.add("Full");
        BlockOptions.add("Flat");
        BlockOptions.add("FlatOutline");
        BlockOptions.add("Beacon");
        BlockOptions.add("Xspot");
        BlockOptions.add("Tracer");
        BlockOptions.add("Shape");
        BlockOptions.add("None");
        // Outline,Full,Flat,FlatOutline,Beacon,Xspot,Tracer,Shape,None

        return BlockOptions;
    }

    public boolean isToggled() {
        return toggled;
    }

    public String getName() {
        return name;
    }

    public String getTooltip() {
        return tooltip;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Integer> getKeys() {
        return keys;
    }

    public void setKey(int Key, boolean Control, boolean Shift, boolean Alt) {
        this.keys = new ArrayList<>();
        if (Control)
            this.keys.add(Keyboard.KEY_LCONTROL);
        if (Shift)
            this.keys.add(Keyboard.KEY_LSHIFT);
        if (Alt)
            this.keys.add(Keyboard.KEY_LMENU);

        this.keys.add(Key);
    }

    public void setKeys(String keys) {
        if (keys != null) {
            keys = keys.replaceAll("\\[", "");
            keys = keys.replaceAll("]", "");
            keys = keys.replaceAll(" ", "");
            String[] tryit = keys.split(",");
            ArrayList<Integer> key = new ArrayList<>();
            for (String s : tryit) {
                key.add(Integer.valueOf(s));
            }
            this.keys = key;
        }
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getDisplayName() {
        return displayName == null ? name : displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setup() {
    }

    public void setsave() {
    }

    public void setAll() {
    }

    public void settingChanged(Component.ClickType visual) {
        codeExecuter.settingChanged(visual);
    }
}