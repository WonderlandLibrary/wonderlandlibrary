package tech.drainwalk;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import tech.drainwalk.font.FontManager;
import tech.drainwalk.gui.menu.MenuMain;
import tech.drainwalk.client.module.ModuleManager;
import tech.drainwalk.client.profile.Profile;
import tech.drainwalk.client.scripting.ScriptManager;

public class DrainWalk {
    @Getter
    public static final DrainWalk instance = new DrainWalk();
    @Getter
    @Setter
    public boolean roflMode = false;

    public static final String CLIENT_NAME = "HUNNER";
    public static final String BUILD = "23 December 2022";
    public static final String RELEASE_TYPE = "Nightly";
    public static final String VERSION = "1.0";

    public static final String USERNAME = "DIGGER";

    @Getter
    public static ModuleManager moduleManager;
    @Getter
    public static ScriptManager scriptManager;
    @Getter
    public final MenuMain menuMain;
    @Getter
    public final Profile profile;

    public DrainWalk() {
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("drainwalk/images/alpha_horizontal.png"));
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("drainwalk/images/hue_horizontal.png"));
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("drainwalk/images/hue_vertical.png"));
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("drainwalk/images/main_menu_image.png"));
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("drainwalk/images/main_menu.png"));
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("drainwalk/images/deus_mode.png"));
        FontManager.initFonts();

        moduleManager = new ModuleManager();
        scriptManager = new ScriptManager();
        profile = new Profile("p4trick", 1, "28.12.2022");
        menuMain = new MenuMain();


    }
}
