/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.visuals;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumHandSide;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.player.EventTransformSideFirstPerson;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.settings.impl.NumberSetting;

public class ViewModel
extends Feature {
    public NumberSetting mainX = new NumberSetting("MainX", 1.2, 0.0, 6.0, 0.1);
    public NumberSetting mainY = new NumberSetting("MainY", -0.63, -3.0, 3.0, 0.1);
    public NumberSetting mainZ = new NumberSetting("MainZ", -2.17, -5.0, 5.0, 0.1);
    public NumberSetting offX = new NumberSetting("OffX", -1.2, -6.0, 0.0, 0.1);
    public NumberSetting offY = new NumberSetting("OffY", -0.75, -3.0, 3.0, 0.1);
    public NumberSetting offZ = new NumberSetting("OffZ", -2.67, -5.0, 5.0, 0.1);
    public NumberSetting mainAngel = new NumberSetting("MainAngle", 0.0, 0.0, 360.0, 0.1);
    public NumberSetting mainRx = new NumberSetting("MainRotation", 0.0, -1.0, 1.0, 0.1);
    public NumberSetting mainRy = new NumberSetting("MainRotationY", 0.0, -1.0, 1.0, 0.1);
    public NumberSetting mainRz = new NumberSetting("MainRotationZ", 0.0, -1.0, 1.0, 0.1);
    public NumberSetting offAngle = new NumberSetting("OffAngle", 0.0, 0.0, 360.0, 0.1);
    public NumberSetting offRx = new NumberSetting("OffRotationX", 0.0, -1.0, 1.0, 0.1);
    public NumberSetting offRy = new NumberSetting("OffRotationY", 0.0, -1.0, 1.0, 0.1);
    public NumberSetting offRz = new NumberSetting("OffRotationZ", 0.0, -1.0, 1.0, 0.1);
    public static NumberSetting mainScaleX = new NumberSetting("MainScaleX", 1.0, -5.0, 10.0, 0.1);
    public static NumberSetting mainScaleY = new NumberSetting("MainScaleY", 1.0, -5.0, 10.0, 0.1);
    public static NumberSetting mainScaleZ = new NumberSetting("MainScaleZ", 1.0, -5.0, 10.0, 0.1);
    public static NumberSetting offScaleX = new NumberSetting("OffScaleX", 1.0, -5.0, 10.0, 0.1);
    public static NumberSetting offScaleY = new NumberSetting("OffScaleY", 1.0, -5.0, 10.0, 0.1);
    public static NumberSetting offScaleZ = new NumberSetting("OffScaleZ", 1.0, -5.0, 10.0, 0.1);

    public ViewModel() {
        super("ViewModel", "\u041f\u043e\u0437\u0432\u043e\u043b\u044f\u0435\u0442 \u0440\u0435\u0434\u0430\u043a\u0442\u0438\u0440\u043e\u0432\u0430\u0442\u044c \u043f\u043e\u0437\u0438\u0446\u0438\u044e \u043f\u0440\u0435\u0434\u043c\u0435\u0442\u043e\u0432 \u0432 \u0440\u0443\u043a\u0435", Type.Visuals);
        this.addSettings(this.mainX, this.mainY, this.mainZ, this.offX, this.offY, this.offZ, this.mainAngel, this.mainRx, this.mainRy, this.mainRz, this.offAngle, this.offRx, this.offRy, this.offRz, mainScaleX, mainScaleY, mainScaleZ, offScaleX, offScaleY, offScaleZ);
    }

    @EventTarget
    public void onTransformSideFirstPerson(EventTransformSideFirstPerson event) {
        if (event.getEnumHandSide() == EnumHandSide.RIGHT) {
            GlStateManager.translate(this.mainX.getCurrentValue(), this.mainY.getCurrentValue(), this.mainZ.getCurrentValue());
            GlStateManager.scale(mainScaleX.getCurrentValue(), mainScaleY.getCurrentValue(), mainScaleZ.getCurrentValue());
            GlStateManager.rotate(this.mainAngel.getCurrentValue(), this.mainRx.getCurrentValue(), this.mainRy.getCurrentValue(), this.mainRz.getCurrentValue());
        } else if (event.getEnumHandSide() == EnumHandSide.LEFT) {
            GlStateManager.translate(this.offX.getCurrentValue(), this.offY.getCurrentValue(), this.offZ.getCurrentValue());
            GlStateManager.scale(offScaleX.getCurrentValue(), offScaleY.getCurrentValue(), offScaleZ.getCurrentValue());
            GlStateManager.rotate(this.offAngle.getCurrentValue(), this.offRx.getCurrentValue(), this.offRy.getCurrentValue(), this.offRz.getCurrentValue());
        }
    }
}

