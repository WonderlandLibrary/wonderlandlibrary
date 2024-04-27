/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.scoreboard.ScoreObjective
 *  net.minecraft.scoreboard.ScorePlayerTeam
 *  net.minecraft.scoreboard.Scoreboard
 */
package net.dev.important.injection.forge.mixins.bugfixes;

import java.util.Map;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={Scoreboard.class})
public abstract class ScoreboardMixin_FixLogSpam {
    @Shadow
    public abstract ScorePlayerTeam func_96508_e(String var1);

    @Inject(method={"removeTeam"}, at={@At(value="HEAD")}, cancellable=true)
    private void patcher$checkIfTeamIsNull(ScorePlayerTeam team, CallbackInfo ci) {
        if (team == null) {
            ci.cancel();
        }
    }

    @Redirect(method={"removeTeam"}, at=@At(value="INVOKE", target="Ljava/util/Map;remove(Ljava/lang/Object;)Ljava/lang/Object;", ordinal=0, remap=false))
    private <K, V> V patcher$checkIfRegisteredNameIsNull(Map<K, V> instance, K o) {
        if (o != null) {
            return instance.remove(o);
        }
        return null;
    }

    @Inject(method={"removeObjective"}, at={@At(value="HEAD")}, cancellable=true)
    private void patcher$checkIfObjectiveIsNull(ScoreObjective objective, CallbackInfo ci) {
        if (objective == null) {
            ci.cancel();
        }
    }

    @Redirect(method={"removeObjective"}, at=@At(value="INVOKE", target="Ljava/util/Map;remove(Ljava/lang/Object;)Ljava/lang/Object;", ordinal=0, remap=false))
    private <K, V> V patcher$checkIfNameIsNull(Map<K, V> instance, K o) {
        if (o != null) {
            return instance.remove(o);
        }
        return null;
    }

    @Inject(method={"createTeam"}, at={@At(value="CONSTANT", args={"stringValue=A team with the name '"})}, cancellable=true)
    private void patcher$returnExistingTeam(String name, CallbackInfoReturnable<ScorePlayerTeam> cir) {
        cir.setReturnValue(this.func_96508_e(name));
    }

    @Inject(method={"removePlayerFromTeam"}, at={@At(value="CONSTANT", args={"stringValue=Player is either on another team or not on any team. Cannot remove from team '"})}, cancellable=true)
    private void patcher$silenceException(CallbackInfo ci) {
        ci.cancel();
    }
}

