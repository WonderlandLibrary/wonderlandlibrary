/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.module;

import com.darkmagician6.eventapi.EventManager;
import cow.milkgod.cheese.Cheese;
import cow.milkgod.cheese.files.FileManager;
import cow.milkgod.cheese.files.files.modulefiles.Modules;
import cow.milkgod.cheese.module.Category;
import cow.milkgod.cheese.properties.Property;
import cow.milkgod.cheese.properties.PropertyManager;
import cow.milkgod.cheese.utils.Logger;
import cow.milkgod.cheese.utils.Utils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import org.apache.commons.lang3.StringUtils;

public class Module {
    private String name;
    private int bind;
    private Category category;
    private boolean isEnabled;
    private int colour;
    private Utils util = new Utils();
    private boolean visible;
    private String description;
    private String[] alias;
    private String displayName;

    public Module(String name, int bind, Category category, int colour, boolean visible, String description, String[] alias) {
        this.name = name;
        this.category = category;
        this.bind = bind;
        this.colour = colour;
        this.visible = visible;
        this.description = description;
        this.alias = alias;
        this.addCommand();
    }

    public Module(String name, int bind, Category category, int colour, boolean visible, String description, String[] alias, String display) {
        this.name = name;
        this.category = category;
        this.bind = bind;
        this.colour = colour;
        this.visible = visible;
        this.description = description;
        this.alias = alias;
        this.displayName = display;
        this.addCommand();
    }

    public String getDisplayName() {
        if (this.displayName == null) {
            this.displayName = this.name;
        }
        return this.displayName;
    }

    public void setDisplayName(String name) {
        this.displayName = name;
    }

    public void setColor(int color) {
        this.colour = color;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public String[] getAlias() {
        return this.alias;
    }

    public int getBind() {
        return this.bind;
    }

    public int getKeybind() {
        return this.bind;
    }

    public int getColour() {
        return this.colour;
    }

    public Category getCategory() {
        return this.category;
    }

    public boolean getState() {
        return this.isEnabled;
    }

    public void setState(boolean state) {
        this.onToggle();
        this.isEnabled = state;
        if (this.isEnabled) {
            Random randomService = new Random();
            StringBuilder sb2 = new StringBuilder();
            sb2.append("0x");
            while (sb2.length() < 10) {
                sb2.append(Integer.toHexString(randomService.nextInt()));
            }
            sb2.setLength(8);
            this.colour = Integer.decode(sb2.toString());
            this.onEnable();
        } else {
            this.onDisable();
        }
        try {
            Cheese.getInstance();
            Cheese.fileManager.getFile(Modules.class).saveFile();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logValues() {
        Cheese.getInstance();
        for (Property p2 : Cheese.propertyManager.getPropertiesFromModule(this)) {
            if (p2.value instanceof Boolean) {
                Logger.logChat("\u00a7e" + StringUtils.capitalize(p2.getName()) + ": " + ((Boolean)p2.value != false ? "\u00a7a" : "\u00a7c") + p2.value);
                continue;
            }
            Logger.logChat("\u00a7e" + StringUtils.capitalize(p2.getName()) + ": " + "\u00a77" + p2.value);
        }
    }

    public void logValue(Property p2) {
        if (p2.value instanceof Boolean) {
            Logger.logChat("\u00a7e" + StringUtils.capitalize(p2.getName()) + ": " + ((Boolean)p2.value != false ? "\u00a7a" : "\u00a7c") + p2.value);
        } else {
            Logger.logChat("\u00a7e" + StringUtils.capitalize(p2.getName()) + ": " + "\u00a77" + p2.value);
        }
    }

    public void setKeybind(int nbind) {
        this.bind = nbind;
    }

    public void toggleModule() {
        this.setState(!this.getState());
        try {
            Cheese.getInstance();
            Cheese.fileManager.getFile(Modules.class).saveFile();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public final String getActive() {
        if (this.getState()) {
            return "\u00a7f" + this.name + "\u00a7a" + " Active";
        }
        return "\u00a7f" + this.name + "\u00a7c" + " Inactive";
    }

    public void returnChatStatus() {
        Logger.logChat(this.getActive());
    }

    public void onToggle() {
    }

    public void onEnable() {
        EventManager.register(this);
    }

    public void onDisable() {
        EventManager.unregister(this);
    }

    public void onUpdate() {
    }

    public void onRender() {
    }

    public final boolean isCategory(Category s) {
        if (s == this.category) {
            return true;
        }
        return false;
    }

    public ArrayList<Property> getModuleProperties() {
        ArrayList<Property> properties = new ArrayList<Property>();
        for (Property p2 : PropertyManager.properties) {
            if (!p2.getModule().equals(this)) continue;
            properties.add(p2);
        }
        return properties;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public void setVisible(boolean vis) {
        this.visible = vis;
    }

    protected void addCommand() {
    }
}

