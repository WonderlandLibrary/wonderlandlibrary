/*
 * Decompiled with CFR 0.150.
 */
package baritone.behavior;

import baritone.Baritone;
import baritone.api.behavior.ILookBehavior;
import baritone.api.event.events.PlayerUpdateEvent;
import baritone.api.event.events.RotationMoveEvent;
import baritone.api.utils.Rotation;
import baritone.behavior.Behavior;

public final class LookBehavior
extends Behavior
implements ILookBehavior {
    private Rotation target;
    private boolean force;
    private float lastYaw;

    public LookBehavior(Baritone baritone) {
        super(baritone);
    }

    @Override
    public void updateTarget(Rotation target, boolean force) {
        this.target = target;
        this.force = true;
    }

    @Override
    public void onPlayerUpdate(PlayerUpdateEvent event) {
        if (this.target == null) {
            return;
        }
        boolean silent = (Boolean)Baritone.settings().antiCheatCompatibility.value != false && !this.force;
        switch (event.getState()) {
            case PRE: {
                if (this.force) {
                    float desiredPitch;
                    this.ctx.player().rotationYaw = this.target.getYaw();
                    float oldPitch = this.ctx.player().rotationPitch;
                    this.ctx.player().rotationPitch = desiredPitch = this.target.getPitch();
                    this.ctx.player().rotationYaw = (float)((double)this.ctx.player().rotationYaw + (Math.random() - 0.5) * (Double)Baritone.settings().randomLooking.value);
                    this.ctx.player().rotationPitch = (float)((double)this.ctx.player().rotationPitch + (Math.random() - 0.5) * (Double)Baritone.settings().randomLooking.value);
                    if (desiredPitch == oldPitch && !((Boolean)Baritone.settings().freeLook.value).booleanValue()) {
                        this.nudgeToLevel();
                    }
                    this.target = null;
                }
                if (!silent) break;
                this.lastYaw = this.ctx.player().rotationYaw;
                this.ctx.player().rotationYaw = this.target.getYaw();
                break;
            }
            case POST: {
                if (!silent) break;
                this.ctx.player().rotationYaw = this.lastYaw;
                this.target = null;
                break;
            }
        }
    }

    public void pig() {
        if (this.target != null) {
            this.ctx.player().rotationYaw = this.target.getYaw();
        }
    }

    @Override
    public void onPlayerRotationMove(RotationMoveEvent event) {
        if (this.target != null) {
            event.setYaw(this.target.getYaw());
            if (!((Boolean)Baritone.settings().antiCheatCompatibility.value).booleanValue() && event.getType() == RotationMoveEvent.Type.MOTION_UPDATE && !this.force) {
                this.target = null;
            }
        }
    }

    private void nudgeToLevel() {
        if (this.ctx.player().rotationPitch < -20.0f) {
            this.ctx.player().rotationPitch += 1.0f;
        } else if (this.ctx.player().rotationPitch > 10.0f) {
            this.ctx.player().rotationPitch -= 1.0f;
        }
    }
}

