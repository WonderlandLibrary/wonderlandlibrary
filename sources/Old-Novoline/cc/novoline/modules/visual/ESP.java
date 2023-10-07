package cc.novoline.modules.visual;

import cc.novoline.gui.screen.setting.Setting;
import cc.novoline.gui.screen.setting.SettingType;
import cc.novoline.modules.AbstractModule;
import cc.novoline.modules.EnumModuleType;
import cc.novoline.modules.ModuleManager;
import cc.novoline.modules.configurations.annotation.Property;
import cc.novoline.modules.configurations.property.object.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import org.checkerframework.checker.nullness.qual.NonNull;

import static cc.novoline.gui.screen.setting.Manager.put;
import static cc.novoline.modules.configurations.property.object.PropertyFactory.*;

public final class ESP extends AbstractModule {

    /* properties @off */
    @Property("color")
    public final ColorProperty color = createColor(0xFFA83E5E);
    @Property("alpha")
    public final FloatProperty alpha = createFloat(50.0F).minimum(50.0F).maximum(255.0F);
    @Property("outline-color")
    public final StringProperty outlineColor = createString("Rainbow").acceptableValues("Team", "Static", "Rainbow");
    @Property("targets")
    public final ListProperty<String> targets = createList("Players").acceptableValues("Players", "Animals", "Mobs", "Passives");

    /* constructors @on */
    public ESP(@NonNull ModuleManager moduleManager) {
        super(moduleManager, EnumModuleType.VISUALS, "Glow");
        put(new Setting("ESPTARGETS", "Targets", SettingType.SELECTBOX, this, this.targets));
        put(new Setting("Player_color", "Outline Color", SettingType.COMBOBOX, this, this.outlineColor));
        put(new Setting("ESP_COLOR", "Color", SettingType.COLOR_PICKER, this, this.color, null, () -> outlineColor.get().equalsIgnoreCase("Static")));
        put(new Setting("ESPAlpha", "Alpha", SettingType.SLIDER, this, this.alpha, 5.0F));
    }

    public boolean isValid(EntityLivingBase entity) {
        return isValidType(entity) && entity.isEntityAlive() && !entity.isInvisible() && entity != this.mc.player;
    }

    public boolean isValidType(Entity entity) {
        return this.targets.contains("Players") && entity instanceof EntityPlayer // @off
                || this.targets.contains("Mobs") && (entity instanceof EntityMob
                || entity instanceof EntitySlime)
                || this.targets.contains("Passives") && (entity instanceof EntityVillager
                || entity instanceof EntityGolem)
                || this.targets.contains("Animals") && entity instanceof EntityAnimal; // @on
    }

}
