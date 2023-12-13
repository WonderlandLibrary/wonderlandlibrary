package today.flux.addon.api.module;

import today.flux.addon.api.event.events.Event2DRender;
import today.flux.addon.api.event.events.Event3DRender;
import today.flux.addon.api.event.events.EventMove;
import today.flux.addon.api.event.events.EventPreUpdate;
import today.flux.addon.api.exception.APIException;
import today.flux.addon.api.packet.AddonPacket;
import today.flux.module.Category;
import today.flux.module.Module;

public class AddonModule {
    public Module module;

    /**
     *
     * @param name Module名称
     * @param category Module类型
     */
    public AddonModule(String name, AddonCategory category) {
        Category originCategory;
        if (category == AddonCategory.Combat) originCategory = Category.Combat;
        else if (category == AddonCategory.Movement) originCategory = Category.Movement;
        else if (category == AddonCategory.Player) originCategory = Category.Player;
        else if (category == AddonCategory.World) originCategory = Category.World;
        else if (category == AddonCategory.Render) originCategory = Category.Render;
        else if (category == AddonCategory.Misc) originCategory = Category.Misc;
        else if (category == AddonCategory.Ghost) originCategory = Category.Ghost;
        else originCategory = Category.Combat;

        this.module = new Module(name, originCategory, false);
        this.module.setAddonModule(this);
    }

    public AddonModule(Module module) {
        this.module = module;
    }

    /**
     *
     * @return Module状态
     */
    
    public Boolean getStage() {
        return module.isEnabled();
    }

    /**
     *
     * @return Module 名称
     */
    
    public String getName() {
        return module.getName();
    }

    /**
     *
     * @param stage 输入一个Boolean类型的值,设置Module状态
     */
    
    public void setStage(Boolean stage) {
        this.module.setEnabled(stage);
    }

    /**
     * 切换Module状态
     */
    
    public void toggle() {
        this.module.toggle();
    }

    /**
     * 开启Module
     * @throws APIException
     */
    public void onEnable() {

    }

    /**
     * 关闭Module
     * @throws APIException
     */
    public void onDisable() {

    }

    public void onPreUpdate(EventPreUpdate event) {

    }

    public void onPostUpdate() {

    }

    public void onMove(EventMove event) {

    }

    public void on2DRender(Event2DRender event) {

    }

    public void on3DRender(Event3DRender event) {

    }

    public void onTicks() {

    }

    public void onPacket(AddonPacket packet) {

    }
}
