/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.combat;

import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.packet.EventPostAttackSilent;
import org.celestial.client.event.events.impl.player.EventUpdate;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.feature.impl.combat.KillAura;
import org.celestial.client.helpers.math.MathematicHelper;
import org.celestial.client.helpers.sound.SoundHelper;
import org.celestial.client.settings.impl.ListSetting;
import org.celestial.client.settings.impl.NumberSetting;

public class HitSounds
extends Feature {
    private final ListSetting soundMode = new ListSetting("Sound Mode", "NeverLose", "NeverLose", "Moan");
    private final NumberSetting volume = new NumberSetting("Volume", 50.0f, 1.0f, 100.0f, 1.0f, () -> true);

    public HitSounds() {
        super("HitSounds", "\u0412\u043e\u0441\u043f\u0440\u043e\u0438\u0437\u0432\u043e\u0434\u0438\u0442 \u0437\u0432\u0443\u043a \u043f\u0440\u0438 \u0443\u0434\u0430\u0440\u0435", Type.Combat);
        this.addSettings(this.soundMode, this.volume);
    }

    @EventTarget
    public void onSuffixUpdate(EventUpdate event) {
        this.setSuffix(this.soundMode.getCurrentMode());
    }

    @EventTarget
    public void onPostAttack(EventPostAttackSilent event) {
        float volume = this.volume.getCurrentValue() / 10.0f;
        if (KillAura.isBreaked) {
            return;
        }
        if (this.soundMode.currentMode.equals("NeverLose")) {
            SoundHelper.playSound("neverlose.wav", -30.0f + volume * 3.0f, false);
        } else if (this.soundMode.currentMode.equals("Moan")) {
            String randomCount = "moan" + (int)MathematicHelper.randomizeFloat(1.0f, 6.0f);
            SoundHelper.playSound("moan/" + randomCount + ".wav", -30.0f + volume * 3.0f, false);
        }
    }
}

