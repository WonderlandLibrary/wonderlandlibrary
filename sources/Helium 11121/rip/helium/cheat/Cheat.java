package rip.helium.cheat;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;
import rip.helium.Helium;
import rip.helium.utils.Timer;
import rip.helium.utils.property.abs.Property;

import java.awt.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Random;

/**
 * @author antja03
 */
public abstract class Cheat {
    protected static Minecraft mc = Minecraft.getMinecraft();
    private final String id, description;
    private final CheatCategory category;
    private final LinkedHashMap<String, Property> propertyRegistry;
    public int animation;
    public int color;
    protected String mode;
    Timer Noise = new Timer();
    private int bind;
    private boolean state;
    private final Random random = new Random();

    public Cheat(String id, String description) {
        this.id = id;
        this.description = description;
        final float hue = random.nextFloat();
        final float saturation = (random.nextInt(8000) + 500) / 10000f;
        this.color = Color.getHSBColor(hue, saturation, 0.9f).getRGB();
        this.bind = Keyboard.KEY_NONE;
        this.category = CheatCategory.MISC;
        this.propertyRegistry = new LinkedHashMap<>();
        this.state = false;
    }

    public Cheat(String id, String description, int bind) {
        this.id = id;
        this.description = description;
        final float hue = random.nextFloat();
        final float saturation = (random.nextInt(8000) + 500) / 10000f;
        this.color = Color.getHSBColor(hue, saturation, 0.9f).getRGB();
        this.bind = bind;
        this.category = CheatCategory.MISC;
        this.propertyRegistry = new LinkedHashMap<>();
        this.state = false;
    }

    public Cheat(String id, String description, CheatCategory category) {
        this.id = id;
        this.description = description;
        final float hue = random.nextFloat();
        final float saturation = (random.nextInt(8000) + 500) / 10000f;
        this.color = Color.getHSBColor(hue, saturation, 0.9f).getRGB();
        this.bind = Keyboard.KEY_NONE;
        this.category = category;
        this.propertyRegistry = new LinkedHashMap<>();
        this.state = false;
    }

    public Cheat(String id, String description, int bind, CheatCategory category) {
        this.id = id;
        this.description = description;
        final float hue = random.nextFloat();
        final float saturation = (random.nextInt(8000) + 500) / 10000f;
        this.color = Color.getHSBColor(hue, saturation, 0.9f).getRGB();
        this.bind = bind;
        this.category = category;
        this.propertyRegistry = new LinkedHashMap<>();
        this.state = false;
    }

    protected static EntityPlayerSP getPlayer() {
        return Minecraft.getMinecraft().thePlayer;
    }

    protected void onEnable() {
        String name = this.getId() + (this.getMode() != null ? " " + EnumChatFormatting.GRAY + "[" + this.getMode() + "]" : "");
        this.animation = mc.fontRendererObj.getStringWidth(name) - 10;

        if (mc.theWorld != null) {
            Minecraft.getMinecraft().thePlayer.playSound("random.click", 0.4f, 1f);
            //NotificationManager.postInfo("Modules", "You" + EnumChatFormatting.GREEN + " enabled " + EnumChatFormatting.WHITE + this.getId());
        }
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    protected void onDisable() {
        String name = this.getId() + (this.getMode() != null ? " " + EnumChatFormatting.GRAY + "[" + this.getMode() + "]" : "");
        this.animation = mc.fontRendererObj.getStringWidth(name) + 10;
        if (mc.theWorld != null) {
            Minecraft.getMinecraft().thePlayer.playSound("random.click", 0.3f, 0.8f);
            //NotificationManager.postInfo("Modules", "You" +  EnumChatFormatting.RED+ " disabled " + EnumChatFormatting.WHITE + this.getId());
        }
    }

    protected void registerProperties(Property... properties) {
        for (Property property : properties) {
            propertyRegistry.put(property.getId(), property);
        }
    }

    protected Minecraft getMc() {
        return Minecraft.getMinecraft();
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

    public String getMode() {
        return mode;
    }

    public void setMode(String themode) {
        mode = "[" + themode + "]";
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

    public void setState(boolean state, boolean notification) {
        if (this.state == state)
            return;

        this.state = state;

        if (state) {
            onEnable();

            if (mc.theWorld != null && Noise.hasPassed(100)) {
                String name = this.getId() + (this.getMode() != null ? " " + EnumChatFormatting.GRAY + "[" + this.getMode() + "]" : "");
                Minecraft.getMinecraft().thePlayer.playSound("random.click", 0.4f, 1f);
                //NotificationManager.postInfo("Modules", "Module " + this.getId() + " has been enabled.");
                this.animation = mc.fontRendererObj.getStringWidth(name) - 10;
                //ChatUtil.chat("§fModule §a" + getId() + " §fwas §fforcefully §aenabled§f.");
                //NotificationManager.postInfo("Enabled", "§fModule §a" + getId() + " §fwas §fforcefully §aenabled§f.");
                Noise.reset();
            }
            Helium.eventBus.register(this);
        } else {
            Helium.eventBus.unregister(this);
            onDisable();
            if (mc.theWorld != null && Noise.hasPassed(100)) {
                Minecraft.getMinecraft().thePlayer.playSound("random.click", 0.3f, 0.8f);
                String name = this.getId() + (this.getMode() != null ? " " + EnumChatFormatting.GRAY + "[" + this.getMode() + "]" : "");
                //NotificationManager.postInfo("Modules", "Module " + this.getId() + " has been disabled.");
                //ChatUtil.chat("§fModule §c" + getId() + " §fwas §fforcefully §cdisabled§f.");
                //NotificationManager.postInfo("Disabled", "§fModule §c" + getId() + " §fwas §fforcefully §cdisabled§f.");
                this.animation = mc.fontRendererObj.getStringWidth(name) + 10;
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
