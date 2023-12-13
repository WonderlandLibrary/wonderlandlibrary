package me.earth.phobos.features.modules.render;

import me.earth.phobos.Phobos;
import me.earth.phobos.event.events.ClientEvent;
import me.earth.phobos.features.command.Command;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.setting.Setting;
import net.minecraft.block.Block;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class XRay
        extends Module {
    private static XRay INSTANCE = new XRay();
    public Setting<String> newBlock = this.register(new Setting<String>("NewBlock", "Add Block..."));
    public Setting<Boolean> showBlocks = this.register(new Setting<Boolean>("ShowBlocks", false));

    public XRay() {
        super("XRay", "Lets you look through walls.", Module.Category.RENDER, false, false, true);
        this.setInstance();
    }

    public static XRay getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new XRay();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        XRay.mc.renderGlobal.loadRenderers();
    }

    @Override
    public void onDisable() {
        XRay.mc.renderGlobal.loadRenderers();
    }

    @SubscribeEvent
    public void onSettingChange(ClientEvent event) {
        if (Phobos.configManager.loadingConfig || Phobos.configManager.savingConfig) {
            return;
        }
        if (event.getStage() == 2 && event.getSetting() != null && event.getSetting().getFeature() != null && event.getSetting().getFeature().equals(this)) {
            if (event.getSetting().equals(this.newBlock) && !this.shouldRender(this.newBlock.getPlannedValue())) {
                this.register(new Setting<Object>(this.newBlock.getPlannedValue(), Boolean.valueOf(true), v -> this.showBlocks.getValue()));
                Command.sendMessage("<Xray> Added new Block: " + this.newBlock.getPlannedValue());
                if (this.isOn()) {
                    XRay.mc.renderGlobal.loadRenderers();
                }
                event.setCanceled(true);
            } else {
                Setting setting = event.getSetting();
                if (setting.equals(this.enabled) || setting.equals(this.drawn) || setting.equals(this.bind) || setting.equals(this.newBlock) || setting.equals(this.showBlocks)) {
                    return;
                }
                if (setting.getValue() instanceof Boolean && !((Boolean) setting.getPlannedValue()).booleanValue()) {
                    this.unregister(setting);
                    if (this.isOn()) {
                        XRay.mc.renderGlobal.loadRenderers();
                    }
                    event.setCanceled(true);
                }
            }
        }
    }

    public boolean shouldRender(Block block) {
        return this.shouldRender(block.getLocalizedName());
    }

    public boolean shouldRender(String name) {
        for (Setting setting : this.getSettings()) {
            if (!name.equalsIgnoreCase(setting.getName())) continue;
            return true;
        }
        return false;
    }
}

