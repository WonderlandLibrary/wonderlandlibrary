package intent.AquaDev.aqua.modules;

import events.Event;
import intent.AquaDev.aqua.Aqua;
import intent.AquaDev.aqua.fr.lavache.anime.Animate;
import intent.AquaDev.aqua.modules.Category;
import intent.AquaDev.aqua.modules.misc.Disabler;
import net.minecraft.client.Minecraft;

public class Module {
    private String name;
    private String displayname;
    private Category category;
    public boolean toggeld;
    public Animate anim = new Animate();
    public Animate anim2 = new Animate();
    private int keyBind;
    private boolean open;
    public static Minecraft mc = Minecraft.getMinecraft();

    public String getDisplayname() {
        return this.displayname;
    }

    public void onEvent(Event event) {
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getKeyBind() {
        return this.keyBind;
    }

    public void setKeyBind(int keyBind) {
        this.keyBind = keyBind;
    }

    public Category getCategory() {
        return this.category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public boolean isToggled() {
        return this.toggeld;
    }

    public void toggle() {
        this.setState(!this.isToggled());
    }

    public void toggleOpen() {
        this.open = !this.open;
    }

    public void setState(boolean state) {
        if (state != this.toggeld) {
            if (state) {
                this.onEnable();
            } else {
                this.anim.reset();
                this.anim2.reset();
                this.onDisable();
            }
        }
        this.toggeld = state;
    }

    public void setup() {
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    public Module(String name, String displayname, int keyBind, Category category) {
        this.name = name;
        this.displayname = displayname;
        this.category = category;
        this.keyBind = keyBind;
        this.setup();
    }

    public String getMode() {
        String mode = "";
        Module m = this;
        if (m instanceof Disabler) {
            mode = "" + Aqua.setmgr.getSetting("DisablerModes").getCurrentMode();
        }
        return mode;
    }

    public boolean isOpen() {
        return this.open;
    }
}
