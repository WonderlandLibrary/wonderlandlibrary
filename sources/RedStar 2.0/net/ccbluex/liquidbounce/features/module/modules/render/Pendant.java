package net.ccbluex.liquidbounce.features.module.modules.render;

import me.utils.render.VisualUtils;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Render2DEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.render.AnimationUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.ListValue;

@ModuleInfo(name="Pendant", description="flux", category=ModuleCategory.RENDER)
public class Pendant
extends Module {
    public final ListValue Fubukistyle = new ListValue("Fubukistyle", new String[]{"GIF", "Static"}, "GIF");
    public static BoolValue Taco = new BoolValue("Taco", false);
    public static BoolValue Fubuki = new BoolValue("Fubuki", false);
    public final FloatValue positionY = new FloatValue("PositionY", 5.0f, 130.0f, 1000.0f);
    public final FloatValue positionX = new FloatValue("PositionX", 5.0f, 130.0f, 1000.0f);
    public final FloatValue size = new FloatValue("size", 50.0f, 10.0f, 1000.0f);
    float posX = 0.0f;

    @EventTarget
    public void onRender2D(Render2DEvent event) {
        if (((Boolean)Taco.getValue()).booleanValue()) {
            this.Taco();
        }
        if (((Boolean)Fubuki.getValue()).booleanValue()) {
            this.Fubuki();
        }
    }

    public void Fubuki() {
        if (((String)this.Fubukistyle.get()).contains("GIF")) {
            int state = mc.getThePlayer().getTicksExisted() % 16 + 1;
            RenderUtils.drawImage(classProvider.createResourceLocation("pride/fubuki/" + state + ".png"), ((Float)this.positionX.getValue()).intValue(), VisualUtils.height() - ((Float)this.positionY.getValue()).intValue(), ((Float)this.size.getValue()).intValue(), ((Float)this.size.getValue()).intValue());
        }
        if (((String)this.Fubukistyle.get()).contains("Static")) {
            RenderUtils.drawImage(classProvider.createResourceLocation("pride/fubuki/Static.png"), ((Float)this.positionX.getValue()).intValue(), VisualUtils.height() - ((Float)this.positionY.getValue()).intValue(), 77, 250);
        }
    }

    public void Taco() {
        this.posX = this.posX < (float)VisualUtils.width() ? (float)((double)this.posX + AnimationUtils.delta * 0.1) : 0.0f;
        int state = mc.getThePlayer().getTicksExisted() % 12 + 1;
        RenderUtils.drawImage(classProvider.createResourceLocation("pride/taco/" + state + ".png"), (int)this.posX, VisualUtils.height() - 80, 42, 27);
    }
}
