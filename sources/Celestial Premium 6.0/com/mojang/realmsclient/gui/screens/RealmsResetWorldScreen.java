/*
 * Decompiled with CFR 0.150.
 */
package com.mojang.realmsclient.gui.screens;

import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.dto.RealmsServer;
import com.mojang.realmsclient.dto.WorldTemplate;
import com.mojang.realmsclient.dto.WorldTemplatePaginatedList;
import com.mojang.realmsclient.exception.RealmsServiceException;
import com.mojang.realmsclient.gui.RealmsConstants;
import com.mojang.realmsclient.gui.screens.RealmsLongRunningMcoTaskScreen;
import com.mojang.realmsclient.gui.screens.RealmsResetNormalWorldScreen;
import com.mojang.realmsclient.gui.screens.RealmsScreenWithCallback;
import com.mojang.realmsclient.gui.screens.RealmsSelectFileToUploadScreen;
import com.mojang.realmsclient.gui.screens.RealmsSelectWorldTemplateScreen;
import com.mojang.realmsclient.util.RealmsTasks;
import com.mojang.realmsclient.util.RealmsTextureManager;
import net.minecraft.realms.Realms;
import net.minecraft.realms.RealmsButton;
import net.minecraft.realms.RealmsScreen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class RealmsResetWorldScreen
extends RealmsScreenWithCallback<WorldTemplate> {
    private static final Logger LOGGER = LogManager.getLogger();
    private final RealmsScreen lastScreen;
    private final RealmsServer serverData;
    private final RealmsScreen returnScreen;
    private String title = RealmsResetWorldScreen.getLocalizedString("mco.reset.world.title");
    private String subtitle = RealmsResetWorldScreen.getLocalizedString("mco.reset.world.warning");
    private String buttonTitle = RealmsResetWorldScreen.getLocalizedString("gui.cancel");
    private int subtitleColor = 0xFF0000;
    private static final String SLOT_FRAME_LOCATION = "realms:textures/gui/realms/slot_frame.png";
    private static final String UPLOAD_LOCATION = "realms:textures/gui/realms/upload.png";
    private static final String ADVENTURE_MAP_LOCATION = "realms:textures/gui/realms/adventure.png";
    private static final String SURVIVAL_SPAWN_LOCATION = "realms:textures/gui/realms/survival_spawn.png";
    private static final String NEW_WORLD_LOCATION = "realms:textures/gui/realms/new_world.png";
    private static final String EXPERIENCE_LOCATION = "realms:textures/gui/realms/experience.png";
    private static final String INSPIRATION_LOCATION = "realms:textures/gui/realms/inspiration.png";
    private final int BUTTON_CANCEL_ID = 0;
    private final WorldTemplatePaginatedList templates = new WorldTemplatePaginatedList();
    private final WorldTemplatePaginatedList adventuremaps = new WorldTemplatePaginatedList();
    private final WorldTemplatePaginatedList experiences = new WorldTemplatePaginatedList();
    private final WorldTemplatePaginatedList inspirations = new WorldTemplatePaginatedList();
    private ResetType selectedType = ResetType.NONE;
    public int slot = -1;
    private ResetType typeToReset = ResetType.NONE;
    private ResetWorldInfo worldInfoToReset = null;
    private WorldTemplate worldTemplateToReset = null;
    private String resetTitle = null;
    private int confirmationId = -1;

    public RealmsResetWorldScreen(RealmsScreen lastScreen, RealmsServer serverData, RealmsScreen returnScreen) {
        this.lastScreen = lastScreen;
        this.serverData = serverData;
        this.returnScreen = returnScreen;
    }

    public RealmsResetWorldScreen(RealmsScreen lastScreen, RealmsServer serverData, RealmsScreen returnScreen, String title, String subtitle, int subtitleColor, String buttonTitle) {
        this(lastScreen, serverData, returnScreen);
        this.title = title;
        this.subtitle = subtitle;
        this.subtitleColor = subtitleColor;
        this.buttonTitle = buttonTitle;
    }

    public void setConfirmationId(int confirmationId) {
        this.confirmationId = confirmationId;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public void setResetTitle(String title) {
        this.resetTitle = title;
    }

    @Override
    public void init() {
        this.buttonsClear();
        this.buttonsAdd(RealmsResetWorldScreen.newButton(0, this.width() / 2 - 40, RealmsConstants.row(14) - 10, 80, 20, this.buttonTitle));
        new Thread("Realms-reset-world-fetcher"){

            @Override
            public void run() {
                RealmsClient client = RealmsClient.createRealmsClient();
                try {
                    RealmsResetWorldScreen.this.templates.set(client.fetchWorldTemplates(1, 10, RealmsServer.WorldType.NORMAL));
                    RealmsResetWorldScreen.this.adventuremaps.set(client.fetchWorldTemplates(1, 10, RealmsServer.WorldType.ADVENTUREMAP));
                    RealmsResetWorldScreen.this.experiences.set(client.fetchWorldTemplates(1, 10, RealmsServer.WorldType.EXPERIENCE));
                    RealmsResetWorldScreen.this.inspirations.set(client.fetchWorldTemplates(1, 10, RealmsServer.WorldType.INSPIRATION));
                }
                catch (RealmsServiceException e) {
                    LOGGER.error("Couldn't fetch templates in reset world", (Throwable)e);
                }
            }
        }.start();
    }

    @Override
    public void removed() {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    public void keyPressed(char ch, int eventKey) {
        if (eventKey == 1) {
            Realms.setScreen(this.lastScreen);
        }
    }

    @Override
    public void buttonClicked(RealmsButton button) {
        if (!button.active()) {
            return;
        }
        if (button.id() == 0) {
            Realms.setScreen(this.lastScreen);
        }
    }

    @Override
    public void mouseClicked(int x, int y, int buttonNum) {
        switch (this.selectedType) {
            case NONE: {
                break;
            }
            case GENERATE: {
                Realms.setScreen(new RealmsResetNormalWorldScreen(this, this.title));
                break;
            }
            case UPLOAD: {
                Realms.setScreen(new RealmsSelectFileToUploadScreen(this.serverData.id, this.slot != -1 ? this.slot : this.serverData.activeSlot, this));
                break;
            }
            case ADVENTURE: {
                RealmsSelectWorldTemplateScreen screen = new RealmsSelectWorldTemplateScreen(this, null, RealmsServer.WorldType.ADVENTUREMAP, new WorldTemplatePaginatedList(this.adventuremaps));
                screen.setTitle(RealmsResetWorldScreen.getLocalizedString("mco.reset.world.adventure"));
                Realms.setScreen(screen);
                break;
            }
            case SURVIVAL_SPAWN: {
                RealmsSelectWorldTemplateScreen templateScreen = new RealmsSelectWorldTemplateScreen(this, null, RealmsServer.WorldType.NORMAL, new WorldTemplatePaginatedList(this.templates));
                templateScreen.setTitle(RealmsResetWorldScreen.getLocalizedString("mco.reset.world.template"));
                Realms.setScreen(templateScreen);
                break;
            }
            case EXPERIENCE: {
                RealmsSelectWorldTemplateScreen experienceScreen = new RealmsSelectWorldTemplateScreen(this, null, RealmsServer.WorldType.EXPERIENCE, new WorldTemplatePaginatedList(this.experiences));
                experienceScreen.setTitle(RealmsResetWorldScreen.getLocalizedString("mco.reset.world.experience"));
                Realms.setScreen(experienceScreen);
                break;
            }
            case INSPIRATION: {
                RealmsSelectWorldTemplateScreen inspirationScreen = new RealmsSelectWorldTemplateScreen(this, null, RealmsServer.WorldType.INSPIRATION, new WorldTemplatePaginatedList(this.inspirations));
                inspirationScreen.setTitle(RealmsResetWorldScreen.getLocalizedString("mco.reset.world.inspiration"));
                Realms.setScreen(inspirationScreen);
                break;
            }
            default: {
                return;
            }
        }
    }

    private int frame(int i) {
        return this.width() / 2 - 130 + (i - 1) * 100;
    }

    @Override
    public void render(int xm, int ym, float a) {
        this.selectedType = ResetType.NONE;
        this.renderBackground();
        this.drawCenteredString(this.title, this.width() / 2, 7, 0xFFFFFF);
        this.drawCenteredString(this.subtitle, this.width() / 2, 22, this.subtitleColor);
        this.drawFrame(this.frame(1), RealmsConstants.row(0) + 10, xm, ym, RealmsResetWorldScreen.getLocalizedString("mco.reset.world.generate"), -1L, NEW_WORLD_LOCATION, ResetType.GENERATE);
        this.drawFrame(this.frame(2), RealmsConstants.row(0) + 10, xm, ym, RealmsResetWorldScreen.getLocalizedString("mco.reset.world.upload"), -1L, UPLOAD_LOCATION, ResetType.UPLOAD);
        this.drawFrame(this.frame(3), RealmsConstants.row(0) + 10, xm, ym, RealmsResetWorldScreen.getLocalizedString("mco.reset.world.template"), -1L, SURVIVAL_SPAWN_LOCATION, ResetType.SURVIVAL_SPAWN);
        this.drawFrame(this.frame(1), RealmsConstants.row(6) + 20, xm, ym, RealmsResetWorldScreen.getLocalizedString("mco.reset.world.adventure"), -1L, ADVENTURE_MAP_LOCATION, ResetType.ADVENTURE);
        this.drawFrame(this.frame(2), RealmsConstants.row(6) + 20, xm, ym, RealmsResetWorldScreen.getLocalizedString("mco.reset.world.experience"), -1L, EXPERIENCE_LOCATION, ResetType.EXPERIENCE);
        this.drawFrame(this.frame(3), RealmsConstants.row(6) + 20, xm, ym, RealmsResetWorldScreen.getLocalizedString("mco.reset.world.inspiration"), -1L, INSPIRATION_LOCATION, ResetType.INSPIRATION);
        super.render(xm, ym, a);
    }

    private void drawFrame(int x, int y, int xm, int ym, String text, long imageId, String image, ResetType resetType) {
        boolean hovered = false;
        if (xm >= x && xm <= x + 60 && ym >= y - 12 && ym <= y + 60) {
            hovered = true;
            this.selectedType = resetType;
        }
        if (imageId == -1L) {
            RealmsResetWorldScreen.bind(image);
        } else {
            RealmsTextureManager.bindWorldTemplate(String.valueOf(imageId), image);
        }
        if (hovered) {
            GL11.glColor4f(0.56f, 0.56f, 0.56f, 1.0f);
        } else {
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        }
        RealmsScreen.blit(x + 2, y + 2, 0.0f, 0.0f, 56, 56, 56.0f, 56.0f);
        RealmsResetWorldScreen.bind(SLOT_FRAME_LOCATION);
        if (hovered) {
            GL11.glColor4f(0.56f, 0.56f, 0.56f, 1.0f);
        } else {
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        }
        RealmsScreen.blit(x, y, 0.0f, 0.0f, 60, 60, 60.0f, 60.0f);
        this.drawCenteredString(text, x + 30, y - 12, hovered ? 0xA0A0A0 : 0xFFFFFF);
    }

    @Override
    void callback(WorldTemplate worldTemplate) {
        if (worldTemplate != null) {
            if (this.slot == -1) {
                this.resetWorldWithTemplate(worldTemplate);
            } else {
                switch (worldTemplate.type) {
                    case WORLD_TEMPLATE: {
                        this.typeToReset = ResetType.SURVIVAL_SPAWN;
                        break;
                    }
                    case ADVENTUREMAP: {
                        this.typeToReset = ResetType.ADVENTURE;
                        break;
                    }
                    case EXPERIENCE: {
                        this.typeToReset = ResetType.EXPERIENCE;
                        break;
                    }
                    case INSPIRATION: {
                        this.typeToReset = ResetType.INSPIRATION;
                    }
                }
                this.worldTemplateToReset = worldTemplate;
                this.switchSlot();
            }
        }
    }

    private void switchSlot() {
        this.switchSlot(this);
    }

    public void switchSlot(RealmsScreen screen) {
        RealmsTasks.SwitchSlotTask switchSlotTask = new RealmsTasks.SwitchSlotTask(this.serverData.id, this.slot, screen, 100);
        RealmsLongRunningMcoTaskScreen longRunningMcoTaskScreen = new RealmsLongRunningMcoTaskScreen(this.lastScreen, switchSlotTask);
        longRunningMcoTaskScreen.start();
        Realms.setScreen(longRunningMcoTaskScreen);
    }

    @Override
    public void confirmResult(boolean result, int id) {
        if (id == 100 && result) {
            switch (this.typeToReset) {
                case ADVENTURE: 
                case SURVIVAL_SPAWN: 
                case EXPERIENCE: 
                case INSPIRATION: {
                    if (this.worldTemplateToReset == null) break;
                    this.resetWorldWithTemplate(this.worldTemplateToReset);
                    break;
                }
                case GENERATE: {
                    if (this.worldInfoToReset == null) break;
                    this.triggerResetWorld(this.worldInfoToReset);
                    break;
                }
                default: {
                    return;
                }
            }
            return;
        }
        if (result) {
            Realms.setScreen(this.returnScreen);
            if (this.confirmationId != -1) {
                this.returnScreen.confirmResult(true, this.confirmationId);
            }
        }
    }

    public void resetWorldWithTemplate(WorldTemplate template) {
        RealmsTasks.ResettingWorldTask resettingWorldTask = new RealmsTasks.ResettingWorldTask(this.serverData.id, this.returnScreen, template);
        if (this.resetTitle != null) {
            resettingWorldTask.setResetTitle(this.resetTitle);
        }
        if (this.confirmationId != -1) {
            resettingWorldTask.setConfirmationId(this.confirmationId);
        }
        RealmsLongRunningMcoTaskScreen longRunningMcoTaskScreen = new RealmsLongRunningMcoTaskScreen(this.lastScreen, resettingWorldTask);
        longRunningMcoTaskScreen.start();
        Realms.setScreen(longRunningMcoTaskScreen);
    }

    public void resetWorld(ResetWorldInfo resetWorldInfo) {
        if (this.slot == -1) {
            this.triggerResetWorld(resetWorldInfo);
        } else {
            this.typeToReset = ResetType.GENERATE;
            this.worldInfoToReset = resetWorldInfo;
            this.switchSlot();
        }
    }

    private void triggerResetWorld(ResetWorldInfo resetWorldInfo) {
        RealmsTasks.ResettingWorldTask resettingWorldTask = new RealmsTasks.ResettingWorldTask(this.serverData.id, this.returnScreen, resetWorldInfo.seed, resetWorldInfo.levelType, resetWorldInfo.generateStructures);
        if (this.resetTitle != null) {
            resettingWorldTask.setResetTitle(this.resetTitle);
        }
        if (this.confirmationId != -1) {
            resettingWorldTask.setConfirmationId(this.confirmationId);
        }
        RealmsLongRunningMcoTaskScreen longRunningMcoTaskScreen = new RealmsLongRunningMcoTaskScreen(this.lastScreen, resettingWorldTask);
        longRunningMcoTaskScreen.start();
        Realms.setScreen(longRunningMcoTaskScreen);
    }

    public static class ResetWorldInfo {
        String seed;
        int levelType;
        boolean generateStructures;

        public ResetWorldInfo(String seed, int levelType, boolean generateStructures) {
            this.seed = seed;
            this.levelType = levelType;
            this.generateStructures = generateStructures;
        }
    }

    static enum ResetType {
        NONE,
        GENERATE,
        UPLOAD,
        ADVENTURE,
        SURVIVAL_SPAWN,
        EXPERIENCE,
        INSPIRATION;

    }
}

