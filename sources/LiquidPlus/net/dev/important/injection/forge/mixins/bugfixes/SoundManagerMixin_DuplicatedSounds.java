/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.audio.ISound
 *  net.minecraft.client.audio.SoundManager
 *  paulscode.sound.SoundSystem
 */
package net.dev.important.injection.forge.mixins.bugfixes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SoundManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulscode.sound.SoundSystem;

@Mixin(value={SoundManager.class})
public abstract class SoundManagerMixin_DuplicatedSounds {
    @Shadow
    @Final
    private Map<String, ISound> field_148629_h;
    private final List<String> patcher$pausedSounds = new ArrayList<String>();

    @Shadow
    public abstract boolean func_148597_a(ISound var1);

    @Redirect(method={"pauseAllSounds"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/audio/SoundManager$SoundSystemStarterThread;pause(Ljava/lang/String;)V", remap=false))
    private void patcher$onlyPauseSoundIfNecessary(@Coerce SoundSystem soundSystem, String sound) {
        if (this.func_148597_a(this.field_148629_h.get(sound))) {
            soundSystem.pause(sound);
            this.patcher$pausedSounds.add(sound);
        }
    }

    @Redirect(method={"resumeAllSounds"}, at=@At(value="INVOKE", target="Ljava/util/Set;iterator()Ljava/util/Iterator;", remap=false))
    private Iterator<String> patcher$iterateOverPausedSounds(Set<String> keySet) {
        return this.patcher$pausedSounds.iterator();
    }

    @Inject(method={"resumeAllSounds"}, at={@At(value="TAIL")})
    private void patcher$clearPausedSounds(CallbackInfo ci) {
        this.patcher$pausedSounds.clear();
    }
}

