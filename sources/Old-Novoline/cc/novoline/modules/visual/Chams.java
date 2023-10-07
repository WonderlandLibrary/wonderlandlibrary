package cc.novoline.modules.visual;

import cc.novoline.events.EventTarget;
import cc.novoline.events.events.RenderEntityEvent;
import cc.novoline.events.events.TickUpdateEvent;
import cc.novoline.gui.screen.setting.Manager;
import cc.novoline.gui.screen.setting.Setting;
import cc.novoline.gui.screen.setting.SettingType;
import cc.novoline.modules.AbstractModule;
import cc.novoline.modules.EnumModuleType;
import cc.novoline.modules.ModuleManager;
import cc.novoline.modules.PlayerManager;
import cc.novoline.modules.configurations.annotation.Property;
import cc.novoline.modules.configurations.property.object.*;
import cc.novoline.utils.minecraft.FakeEntityPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.lwjgl.opengl.GL11;

import static cc.novoline.modules.configurations.property.object.PropertyFactory.*;

public final class Chams extends AbstractModule {

    /* properties @off */
    @Property("chams-visible")
    private final ColorProperty visible = createColor(0xFF8A8AFF);
    @Property("chams-hidden")
    private final ColorProperty hidden = createColor(0xFF8A8AFF);
    @Property("chams-hand")
    private final ColorProperty handColor = createColor(0xFF8A8AFF);
    @Property("visible-alpha")
    private final FloatProperty visibleAlpha = createFloat(255.0F).minimum(50.0F).maximum(255.0F);
    @Property("pulse-speed")
    private final FloatProperty pulseSpeed = createFloat(10.0F).minimum(5.0F).maximum(20.0F);
    @Property("rainbow")
    private final BooleanProperty rainbow = booleanFalse();
    @Property("colored")
    private final BooleanProperty colored = booleanTrue();
    @Property("pulse")
    private final BooleanProperty pulse = booleanFalse();
    @Property("hand")
    private final BooleanProperty hand = booleanFalse();
    @Property("targets")
    private final ListProperty<String> targets = createList("Players").acceptableValues("Players", "Animals", "Mobs", "Passives");
    @Property("chams-onlyTargets")
    private final BooleanProperty onlyTargets = PropertyFactory.booleanFalse();
    @Property("chams-material")
    private final BooleanProperty material = PropertyFactory.booleanFalse();


    private boolean isF = false;
    private float pulseAlpha;

    public BooleanProperty isColored() {
        return colored;
    }

    public BooleanProperty isMaterial() {
        return material;
    }

    /* constructors @on */
    public Chams(@NonNull ModuleManager moduleManager) {
        super(moduleManager, EnumModuleType.VISUALS, "Chams");
        Manager.put(new Setting("CTARGETS", "Targets", SettingType.SELECTBOX, this, targets));
        Manager.put(new Setting("ChamsColored", "Colored", SettingType.CHECKBOX, this, colored));
        Manager.put(new Setting("VISIBLE_CHAMS", "Visible", SettingType.COLOR_PICKER, this, visible, null, () -> !rainbow.get() && colored.get()));
        Manager.put(new Setting("HIDDEN_CHAMS", "Hidden", SettingType.COLOR_PICKER, this, hidden, null, () -> !rainbow.get() && colored.get()));
        Manager.put(new Setting("VAlpha", "Alpha", SettingType.SLIDER, this, visibleAlpha, 5, colored::get));
        Manager.put(new Setting("CHAMS_MT", "Material", SettingType.CHECKBOX, this, material, colored::get));
        Manager.put(new Setting("ChamsRainbow", "Rainbow", SettingType.CHECKBOX, this, rainbow, colored::get));
        Manager.put(new Setting("PULSE", "Pulse", SettingType.CHECKBOX, this, pulse, colored::get));
        Manager.put(new Setting("HAND", "Hand", SettingType.CHECKBOX, this, hand, colored::get));
        Manager.put(new Setting("HAND_CHAMS", "Hand color", SettingType.COLOR_PICKER, this, handColor, null, () -> !rainbow.get() && colored.get() && hand.get()));
        Manager.put(new Setting("PS", "Pulse speed", SettingType.SLIDER, this, pulseSpeed, 1, () -> pulse.get() && colored.get()));
        Manager.put(new Setting("ONLYTARGETS", "Targets Only", SettingType.CHECKBOX, this, onlyTargets));
    }

    /* methods */
    public boolean isValid(EntityLivingBase entity) {
        return !(entity instanceof FakeEntityPlayer) && isValidType(entity) && entity.isEntityAlive() && !entity.isInvisible() && entity != mc.player && (!onlyTargets.get() || novoline.getPlayerManager().hasType(entity.getName(), PlayerManager.EnumPlayerType.TARGET));
    }

    private boolean isValidType(EntityLivingBase entity) {
        return targets.contains("Players") && entity instanceof EntityPlayer// @off
                || targets.contains("Mobs") && (entity instanceof EntityMob || entity instanceof EntitySlime)
                || targets.contains("Passives") && (entity instanceof EntityVillager
                || entity instanceof EntityGolem) || targets.contains("Animals") && entity instanceof EntityAnimal; // @on
    }

    @EventTarget
    public void onRenderEntity(RenderEntityEvent renderEntityEvent) {
        if (!colored.get()) {
            if (isValid((EntityLivingBase) renderEntityEvent.getEntity())) {
                if (renderEntityEvent.getState() == RenderEntityEvent.State.PRE) {
                    GL11.glEnable(32823);
                    GL11.glPolygonOffset(1.0f, -1100000.0f);
                } else {
                    GL11.glDisable(32823);
                    GL11.glPolygonOffset(1.0f, 1100000.0f);
                }
            }
        }
    }

    @EventTarget
    public void onTick(TickUpdateEvent tickUpdateEvent) {
        pulseAlpha = MathHelper.clamp_float(pulseAlpha, 0, visibleAlpha.get());
        if (!isF && pulseAlpha < visibleAlpha.get()) {
            pulseAlpha = MathHelper.clamp_float(pulseAlpha + pulseSpeed.get(), 0, visibleAlpha.get());
        } else if (!isF && pulseAlpha == visibleAlpha.get()) {
            isF = true;
        } else if (isF && pulseAlpha > 0) {
            pulseAlpha = MathHelper.clamp_float(pulseAlpha - pulseSpeed.get(), 0, visibleAlpha.get());
        } else if (isF && pulseAlpha == 0) {
            isF = false;
        }
    }

    //region Lombok
    public ColorProperty getVisible() {
        return visible;
    }

    public ColorProperty getHidden() {
        return hidden;
    }

    public ColorProperty getHandColor() { return handColor; }

    public BooleanProperty getHand() { return hand; }

    public float getVisibleAlpha() {
        return pulse.get() ? pulseAlpha : visibleAlpha.get();
    }

    public BooleanProperty getRainbow() {
        return rainbow;
    }

    public ListProperty<String> getTargets() {
        return targets;
    }

    @Override
    public void onEnable() {
        pulseAlpha = 0;
        isF = false;
        super.onEnable();
    }

    //endregion

}
