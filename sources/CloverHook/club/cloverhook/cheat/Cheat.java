package club.cloverhook.cheat;

import club.cloverhook.cheat.impl.visual.Hud;
import club.cloverhook.utils.font.Fonts;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.potion.Potion;

import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;

import club.cloverhook.Cloverhook;
import club.cloverhook.utils.Timer;
import club.cloverhook.utils.property.abs.Property;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * @author antja03
 */
public abstract class Cheat {
    protected String mode;
    private final String id, description;
    private int bind;
    private final CheatCategory category;
    private final LinkedHashMap<String, Property> propertyRegistry;
    private boolean state;
    protected Minecraft mc = Minecraft.getMinecraft();
    public int animation;

    public Cheat(String id, String description) {
        this.id = id;
        this.description = description;
        this.bind = Keyboard.KEY_NONE;
        this.category = CheatCategory.MISC;
        this.propertyRegistry = new LinkedHashMap<>();
        this.state = false;
    }

    public Cheat(String id, String description, int bind) {
        this.id = id;
        this.description = description;
        this.bind = bind;
        this.category = CheatCategory.MISC;
        this.propertyRegistry = new LinkedHashMap<>();
        this.state = false;
    }

    public Cheat(String id, String description, CheatCategory category) {
        this.id = id;
        this.description = description;
        this.bind = Keyboard.KEY_NONE;
        this.category = category;
        this.propertyRegistry = new LinkedHashMap<>();
        this.state = false;
    }

    public Cheat(String id, String description, int bind, CheatCategory category) {
        this.id = id;
        this.description = description;
        this.bind = bind;
        this.category = category;
        this.propertyRegistry = new LinkedHashMap<>();
        this.state = false;
    }
    protected void onEnable() {
        String name = this.getId() + (this.getMode() != null ? " " + EnumChatFormatting.GRAY + this.getMode() : "");
        this.animation = mc.fontRendererObj.getStringWidth(name);
    }

    protected void onDisable() {
        String name = this.getId() + (this.getMode() != null ? " " + EnumChatFormatting.GRAY + this.getMode() : "");
        this.animation = mc.fontRendererObj.getStringWidth(name) + 10;
    }

    protected void registerProperties(Property... properties) {
        for (Property property : properties) {
            propertyRegistry.put(property.getId(), property);
        }
    }

    protected Minecraft getMc() {
        return Minecraft.getMinecraft();
    }

    protected EntityPlayerSP getPlayer() {
        return Minecraft.getMinecraft().thePlayer;
    }

    protected WorldClient getWorld() {
        return Minecraft.getMinecraft().theWorld;
    }

    protected GameSettings getGameSettings() {
        return Minecraft.getMinecraft().gameSettings;
    }

    protected PlayerControllerMP getPlayerController() {
        return Minecraft.getMinecraft().playerController;
    }

    public void setMode(String themode) {
        mode = themode;
    }

    public String getMode() {
        return mode;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public int getBind() {
        return bind;
    }

    public void setBind(int bind) {
        this.bind = bind;
    }

    public CheatCategory getCategory() {
        return category;
    }

    public HashMap<String, Property> getPropertyRegistry() {
        return propertyRegistry;
    }

    public boolean getState() {
        return state;
    }

    Timer Noise = new Timer();

    public void setState(boolean state, boolean notification) {
        if (this.state == state)
            return;

        this.state = state;

        if (state) {
            onEnable();
            if (this.mc.theWorld != null && Noise.hasPassed(100)) {
                mc.thePlayer.playSound("random.click", 0.4F, 0.8F);
                Noise.reset();
            }
            Cloverhook.instance.eventBus.register(this);
        } else {
            Cloverhook.instance.eventBus.unregister(this);
            onDisable();
            if (this.mc.theWorld != null && Noise.hasPassed(100)) {
                mc.thePlayer.playSound("random.click", 0.5F, 1.0F);
                Noise.reset();
            }
        }
    }

    public int getAnimation() {
        return animation;
    }

    public void setAnimation(int animation) {
        this.animation = animation;
    }
}
