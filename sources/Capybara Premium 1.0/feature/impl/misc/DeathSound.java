package fun.expensive.client.feature.impl.misc;

import fun.rich.client.event.EventTarget;
import fun.rich.client.event.events.impl.player.EventUpdate;
import fun.rich.client.feature.Feature;
import fun.rich.client.feature.impl.FeatureCategory;
import fun.rich.client.ui.settings.impl.ListSetting;
import fun.rich.client.ui.settings.impl.NumberSetting;
import fun.rich.client.utils.render.SoundUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class DeathSound
        extends Feature {
    public ListSetting deathSoundMode = new ListSetting("DeathSound Mode", "Wendovsky", () -> true, "Wendovsky");
    public NumberSetting volume = new NumberSetting("Volume", 50.0f, 1.0f, 100.0f, 1.0f, () -> true);

    public DeathSound() {
        super("DeathSounds", "Воспроизводит звуки при смерти какого либо игрока", FeatureCategory.Misc);
        this.addSettings(this.deathSoundMode, this.volume);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        this.setSuffix(this.deathSoundMode.currentMode);
        for (Entity entity : mc.world.loadedEntityList) {
            if (entity == null || !(entity instanceof EntityPlayer) || !(((EntityPlayer) entity).getHealth() <= 0.0f) || ((EntityLivingBase) entity).deathTime >= 1 || !(mc.player.getDistanceToEntity(entity) < 10.0f) || entity.ticksExisted <= 5)
                continue;
            float volume = this.volume.getNumberValue() / 10.0f;
            if (deathSoundMode.currentMode.equalsIgnoreCase("Wendovsky")) {
                SoundUtils.playSound("mem.wav", -30.0f + volume * 3.0f, false);
            }
        }
    }
}