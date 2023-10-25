package fun.expensive.client.feature.impl.combat;

import fun.rich.client.event.EventTarget;
import fun.rich.client.event.events.impl.player.EventPostAttackSilent;
import fun.rich.client.event.events.impl.player.EventUpdate;
import fun.rich.client.feature.Feature;
import fun.rich.client.feature.impl.FeatureCategory;
import fun.rich.client.ui.settings.impl.ListSetting;
import fun.rich.client.ui.settings.impl.NumberSetting;
import fun.rich.client.utils.render.SoundUtils;

public class HitSounds
        extends Feature {
    private final ListSetting soundMode = new ListSetting("Sound Mode", "NeverLose", () -> true, "NeverLose", "Pop", "UwU");
    private final NumberSetting volume = new NumberSetting("Volume", 50.0f, 1.0f, 100.0f, 1.0f, () -> true);

    public HitSounds() {
        super("HitSounds", "Воспроизводит звук при ударе", FeatureCategory.Combat);
        this.addSettings(this.soundMode, this.volume);
    }

    @EventTarget
    public void onSuffixUpdate(EventUpdate event) {
        this.setSuffix(this.soundMode.getCurrentMode());
    }

    @EventTarget
    public void onPostAttack(EventPostAttackSilent event) {
        float volume = this.volume.getNumberValue() / 10.0f;
        if (KillAura.isBreaked) {
            return;
        }
        if (this.soundMode.currentMode.equals("NeverLose")) {
            SoundUtils.playSound("neverlose.wav", -30.0f + volume * 3.0f, false);
        } else if (this.soundMode.currentMode.equals("Pop")) {
            SoundUtils.playSound("pop.wav", -30.0f + volume * 3.0f, false);
        } else if (this.soundMode.currentMode.equals("UwU")) {
            SoundUtils.playSound("uwu.wav", -30.0f + volume * 3.0f, false);
        }

    }
}
