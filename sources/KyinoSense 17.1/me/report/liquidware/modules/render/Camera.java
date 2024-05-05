/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package me.report.liquidware.modules.render;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.value.BoolValue;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="Camera", description="xd", category=ModuleCategory.RENDER)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0011\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002R\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u0011\u0010\u0007\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0006R\u0011\u0010\t\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u0006R\u0011\u0010\u000b\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\u0006R\u0011\u0010\r\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u0006R\u0011\u0010\u000f\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0006R\u0011\u0010\u0011\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0006R\u0011\u0010\u0013\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0006\u00a8\u0006\u0015"}, d2={"Lme/report/liquidware/modules/render/Camera;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "antiBlindValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "getAntiBlindValue", "()Lnet/ccbluex/liquidbounce/value/BoolValue;", "bossHealth", "getBossHealth", "cameraClipValue", "getCameraClipValue", "confusionEffect", "getConfusionEffect", "fireEffect", "getFireEffect", "noHurt", "getNoHurt", "pumpkinEffect", "getPumpkinEffect", "scoreBoard", "getScoreBoard", "KyinoClient"})
public final class Camera
extends Module {
    @NotNull
    private final BoolValue cameraClipValue = new BoolValue("CameraClip", true);
    @NotNull
    private final BoolValue antiBlindValue = new BoolValue("AntiBlind", true);
    @NotNull
    private final BoolValue noHurt = new BoolValue("NoHurt", true);
    @NotNull
    private final BoolValue confusionEffect = new BoolValue("Confusion", true);
    @NotNull
    private final BoolValue pumpkinEffect = new BoolValue("Pumpkin", true);
    @NotNull
    private final BoolValue fireEffect = new BoolValue("Fire", false);
    @NotNull
    private final BoolValue scoreBoard = new BoolValue("Scoreboard", false);
    @NotNull
    private final BoolValue bossHealth = new BoolValue("Boss-Health", true);

    @NotNull
    public final BoolValue getCameraClipValue() {
        return this.cameraClipValue;
    }

    @NotNull
    public final BoolValue getAntiBlindValue() {
        return this.antiBlindValue;
    }

    @NotNull
    public final BoolValue getNoHurt() {
        return this.noHurt;
    }

    @NotNull
    public final BoolValue getConfusionEffect() {
        return this.confusionEffect;
    }

    @NotNull
    public final BoolValue getPumpkinEffect() {
        return this.pumpkinEffect;
    }

    @NotNull
    public final BoolValue getFireEffect() {
        return this.fireEffect;
    }

    @NotNull
    public final BoolValue getScoreBoard() {
        return this.scoreBoard;
    }

    @NotNull
    public final BoolValue getBossHealth() {
        return this.bossHealth;
    }
}

