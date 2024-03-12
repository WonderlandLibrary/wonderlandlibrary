package de.theBest.MythicX.modules;

import de.theBest.MythicX.MythicX;
import de.theBest.MythicX.modules.combat.Velocity;
import de.theBest.MythicX.modules.hook.MinecraftHook;
import de.theBest.MythicX.modules.movement.Step;
import de.theBest.MythicX.modules.visual.ESP;
import de.theBest.MythicX.modules.world.NoFall;
import eventapi.EventManager;

import java.awt.*;

public abstract class Module implements MinecraftHook {
    private String name;
    private final String description;
    public Type type;
    private String displayname;
    private Category category;
    public Color color;
    private int keyBind;
    public static double x;
    public static double y;
    public static double width;
    public static double height;
    public static boolean colormode = false;

    private boolean toggled;

    public static boolean isColormode() {
        return colormode;
    }

    public static void setColormode(boolean colormode) {
        Module.colormode = colormode;
    }

    public Module(String name, Type type, int keyBind, Category category,Color moduleColor,String description) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.displayname = name;
        this.category = category;
        this.keyBind = keyBind;
        this.color = moduleColor;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayname() {
        return name;
    }
    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public Category getCategory() {
        return category;
    }
    public void setCategory(Category category) {
        this.category = category;
    }

    public int getKeyBind() {
        return keyBind;
    }
    public void setKeyBind(int keyBind) {
        this.keyBind = keyBind;
    }

    public boolean isEnabled(){
        return toggled;
    }

    public boolean isToggled() {
        return toggled;
    }

    public void setToggled(boolean toggled) {
        this.toggled = toggled;

        if (toggled) {
            EventManager.register(this);
            onEnable();
        } else {
            EventManager.unregister(this);
            onDisable();
        }
    }

    public void toggle() {
        if(toggled) {
            EventManager.unregister(this);
            toggled = false;
            onDisable();
        }else{
            EventManager.register(this);
            toggled = true;
            onEnable();
        }
    }


    public void onEnable(){

    }

    public void setup() {

    }

    public void onDisable(){

    }


    public void callSetup() {
        setup();
    }

    public enum Type {
        Combat(Color.decode("#E74C3C").getRGB(), 'j', "a"), Movement(Color.decode("#2ECC71").getRGB(), 'b', "b"),
        Visual(Color.decode("#3700CE").getRGB(), 'g', "g"), Player(Color.decode("#8E44AD").getRGB(), 'k', "c"),
        World(Color.decode("#3498DB").getRGB(), 'h', "f");

        private int color;
        private char icon;


        private Type(int color, char icon, String test) {
            this.color = color;
            this.icon = icon;
        }

        public int getColor() {
            return color;
        }

        public char getIcon() {
            return icon;
        }


    }
    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public static boolean isHovered(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    public String getMode() {
        String mode = "";
        Module m = Module.this;

        if (m instanceof ESP) {
            mode = "" + "Vanilla";
        }
        if (m instanceof Velocity) {
            mode = "" + MythicX.setmgr.getSettingByName("Velocity").getValString();
        }
        if (m instanceof Step) {
            mode = "" + "Vanilla";
        }
        if (m instanceof NoFall){
            mode = "" + MythicX.setmgr.getSettingByName("Nofall").getValString();
        }

        return mode;
    }

}
