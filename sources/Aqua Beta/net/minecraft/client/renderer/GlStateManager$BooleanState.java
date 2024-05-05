package net.minecraft.client.renderer;

import org.lwjgl.opengl.GL11;

static class GlStateManager.BooleanState {
    private final int capability;
    private boolean currentState = false;

    public GlStateManager.BooleanState(int capabilityIn) {
        this.capability = capabilityIn;
    }

    public void setDisabled() {
        this.setState(false);
    }

    public void setEnabled() {
        this.setState(true);
    }

    public void setState(boolean state) {
        if (state != this.currentState) {
            this.currentState = state;
            if (state) {
                GL11.glEnable((int)this.capability);
            } else {
                GL11.glDisable((int)this.capability);
            }
        }
    }

    static /* synthetic */ boolean access$1200(GlStateManager.BooleanState x0) {
        return x0.currentState;
    }
}
