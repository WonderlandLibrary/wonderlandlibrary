package tech.dort.dortware.impl.modules.render;

import com.google.common.eventbus.Subscribe;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.EntityLivingBase;
import tech.dort.dortware.api.module.Module;
import tech.dort.dortware.api.module.ModuleData;
import tech.dort.dortware.api.property.SliderUnit;
import tech.dort.dortware.api.property.impl.BooleanValue;
import tech.dort.dortware.api.property.impl.EnumValue;
import tech.dort.dortware.api.property.impl.NumberValue;
import tech.dort.dortware.api.property.impl.interfaces.INameable;
import tech.dort.dortware.impl.events.KeyboardEvent;
import tech.dort.dortware.impl.events.RenderHUDEvent;
import tech.dort.dortware.impl.gui.tab.Tab;
import tech.dort.dortware.impl.modules.render.hud.*;

import java.util.HashMap;

public final class Hud extends Module {

    public final EnumValue<Mode> mode = new EnumValue<>("Mode", this, Hud.Mode.values());
    public final EnumValue<RainbowMode> rainbowMode = new EnumValue<>("Rainbow Mode", this, Hud.RainbowMode.values());
    public final EnumValue<TargetHudMode> targetHudMode = new EnumValue<>("Target HUD Mode", this, TargetHudMode.values());
    public final EnumValue<AlternativeNameMode> alternativeNameMode = new EnumValue<>("Name Mode", this, AlternativeNameMode.values());
    public final EnumValue<LineMode> lineMode = new EnumValue<>("Line Mode", this, LineMode.values());
    public final NumberValue red = new NumberValue("Red", this, 255, 0, 255, true);
    public final NumberValue green = new NumberValue("Green", this, 50, 0, 255, true);
    public final NumberValue blue = new NumberValue("Blue", this, 50, 0, 255, true);
    public final NumberValue alpha = new NumberValue("Background Alpha", this, 50, 0, 255, true);
    public final NumberValue targetX = new NumberValue("Target HUD X", this, 20, 0, 300, SliderUnit.X, true);
    public final NumberValue targetY = new NumberValue("Target HUD Y", this, 20, 0, 200, SliderUnit.Y, true);
    public final NumberValue spacing = new NumberValue("Spacing", this, 12, 6, 20, SliderUnit.Y, true);
    public final BooleanValue tabGui = new BooleanValue("Tab UI", this, true);
    public final BooleanValue rainbow = new BooleanValue("Rainbow", this, false);
    public final BooleanValue armorHUD = new BooleanValue("Armor HUD", this, true);
    public final BooleanValue targetHUD = new BooleanValue("Target HUD", this, true);
    public final BooleanValue playerModel = new BooleanValue("Player Model", this, false);
    public final BooleanValue background = new BooleanValue("Background", this, false);
    public final BooleanValue watermark = new BooleanValue("Watermark", this, true);
    public final BooleanValue edition = new BooleanValue("Edition", this, false);
    public final BooleanValue version = new BooleanValue("Version", this, true);
    public final BooleanValue fpsCounter = new BooleanValue("FPS", this, true);
    public final BooleanValue bpsCounter = new BooleanValue("BPS", this, true);
    public final BooleanValue ping = new BooleanValue("Ping", this, true);
    public final BooleanValue lowercase = new BooleanValue("Lowercase", this, false);
    private final HashMap<String, Theme> hudThemes;
    final Tab tab = new Tab();

    public Hud(ModuleData moduleData) {
        super(moduleData);
        this.hudThemes = new HashMap<>();
        this.hudThemes.put("Dortware", new DortwareTheme(this));
        this.hudThemes.put("Dortware Icon", new DortwareIconTheme(this));
        this.hudThemes.put("Dortware Flash", new DortwareFlashTheme(this));
        this.hudThemes.put("Skeet", new SkeetTheme(this));
        this.hudThemes.put("Headedware", new HeadedTheme(this));
        this.hudThemes.put("Meme", new OGMemeClientTheme(this));
        this.hudThemes.put("Astolfo", new AstolfoTheme(this));
        this.hudThemes.put("Vaziak", new VaziakTheme(this));
        this.hudThemes.put("Port 3000", new Port3000Theme(this));
        register(mode, rainbowMode, targetHudMode, alternativeNameMode, lineMode, red, green, blue, alpha, targetX, targetY, spacing, tabGui, rainbow, armorHUD, targetHUD, playerModel, background, watermark, version, edition, fpsCounter, bpsCounter, ping, lowercase);
    }

    public void drawEntityOnScreen(int posX, int posY, EntityLivingBase entityLivingBase) {
        if (entityLivingBase instanceof EntityPlayerSP) {
            EntityPlayerSP entityPlayer = (EntityPlayerSP) entityLivingBase;
            float oldYaw = entityPlayer.rotationYaw;
            float oldPitch = entityPlayer.rotationPitch;
            float oldYawOffset = entityPlayer.renderYawOffset;
            float oldYawHead = entityPlayer.rotationYawHead;
            float oldRYawHead = entityPlayer.renderYawHead;
            float oldRPitchHead = entityPlayer.renderPitchHead;
            entityPlayer.rotationYaw = 0;
            entityPlayer.rotationPitch = 0;
            entityPlayer.renderYawOffset = 0;
            entityPlayer.rotationYawHead = 0;
            entityPlayer.renderYawHead = 0;
            entityPlayer.renderPitchHead = 0;
            GuiInventory.drawEntityOnScreen(posX, posY, 35, 0, 0, entityPlayer);
            entityPlayer.rotationYaw = oldYaw;
            entityPlayer.rotationPitch = oldPitch;
            entityPlayer.renderYawOffset = oldYawOffset;
            entityPlayer.rotationYawHead = oldYawHead;
            entityPlayer.renderYawHead = oldRYawHead;
            entityPlayer.renderPitchHead = oldRPitchHead;
        } else {
            float oldYaw = entityLivingBase.rotationYaw;
            float oldPitch = entityLivingBase.rotationPitch;
            float oldYawOffset = entityLivingBase.renderYawOffset;
            float oldYawHead = entityLivingBase.rotationYawHead;
            entityLivingBase.rotationYaw = 0;
            entityLivingBase.rotationPitch = 0;
            entityLivingBase.renderYawOffset = 0;
            entityLivingBase.rotationYawHead = 0;
            GuiInventory.drawEntityOnScreen(posX, posY, 35, 0, 0, entityLivingBase);
            entityLivingBase.rotationYaw = oldYaw;
            entityLivingBase.rotationPitch = oldPitch;
            entityLivingBase.renderYawOffset = oldYawOffset;
            entityLivingBase.rotationYawHead = oldYawHead;
        }
    }

    @Subscribe
    public void onRender(RenderHUDEvent event) {
        if (mc.gameSettings.showDebugInfo)
            return;

        try {
            this.hudThemes.get(mode.getValue().getDisplayName()).render(event);
            this.tab.render();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void onKeyboard(KeyboardEvent event) {
        if (mc.gameSettings.showDebugInfo)
            return;

        tab.updateKeys(event);
    }

    public enum Mode implements INameable {
        DORTWARE("Dortware"), DORTWARE_ICON("Dortware Icon"), DORTWARE_FLASH("Dortware Flash"), SKEET("Skeet"), HEADEDWARE("Headedware"), MEME("Meme"), ASTOLFO("Astolfo"), VAZIAK("Vaziak"), SECTION_1_THE_MYSTERIOUS_PORT_3000("Port 3000");
        public final String name;

        Mode(String name) {
            this.name = name;
        }

        @Override
        public String getDisplayName() {
            return name;
        }
    }

    public enum RainbowMode implements INameable {
        NORMAL("Normal"), OLD("Old"), FAST("Fast");
        public final String name;

        RainbowMode(String name) {
            this.name = name;
        }

        @Override
        public String getDisplayName() {
            return name;
        }
    }

    public enum TargetHudMode implements INameable {
        SIMPLE("Simple"), HELIUM("Helium");
        public final String name;

        TargetHudMode(String name) {
            this.name = name;
        }

        @Override
        public String getDisplayName() {
            return name;
        }
    }

    public enum AlternativeNameMode implements INameable {
        NORMAL("Normal"), MEMES("Memes"), DUMB("Orialeng");
        public final String name;

        AlternativeNameMode(String name) {
            this.name = name;
        }

        @Override
        public String getDisplayName() {
            return name;
        }
    }

    public enum LineMode implements INameable {
        RIGHT("Right"), LEFT("Left"), BOTH("Both"), NONE("None");
        public final String name;

        LineMode(String name) {
            this.name = name;
        }

        @Override
        public String getDisplayName() {
            return name;
        }
    }
}
