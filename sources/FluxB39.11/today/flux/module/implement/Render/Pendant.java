package today.flux.module.implement.Render;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.util.ResourceLocation;
import today.flux.event.UIRenderEvent;
import today.flux.gui.clickgui.classic.RenderUtil;
import today.flux.module.Category;
import today.flux.module.Module;
import today.flux.utility.AnimationUtils;
import today.flux.module.value.BooleanValue;
import today.flux.module.value.FloatValue;
import today.flux.module.value.ModeValue;

public class Pendant extends Module {

    public static ModeValue Fubukistyle = new ModeValue("Pendant","Fubukistyle","GIF","Static");

    public static BooleanValue Taco = new BooleanValue("Pendant","Taco",false);
    public static BooleanValue Fubuki = new BooleanValue("Pendant","Fubuki",false);
    public static FloatValue positionY = new FloatValue("Pendant","PositionY",130f,0f,1000f,5f);
    public static FloatValue positionX = new FloatValue("Pendant","PositionX",40f,0f,1000f,5f);
    public static FloatValue size = new FloatValue("Pendant","Size",100f,10f,500f,1f);


    float posX = 0;
    public Pendant() { super("Pendant", Category.Render , false); }

    @EventTarget
    public void flat(UIRenderEvent event) {

        if (Taco.getValue()){
            Taco();
        }

        if (Fubuki.getValue()){
            Fubuki();
        }

    }

    public void Fubuki(){

        if (Fubukistyle.isCurrentMode("GIF")){
            int state = (mc.thePlayer.ticksExisted % 16) + 1;
            RenderUtil.drawImage(new ResourceLocation("Pendant/fubuki/" + state + ".png"),positionX.getValue().intValue() , RenderUtil.height() - positionY.getValue().intValue(), size.getValue().intValue(), size.getValue().intValue());
        }

        if (Fubukistyle.isCurrentMode("Static")){
            RenderUtil.drawImage(new ResourceLocation("Pendant/fubuki/Static.png"),positionX.getValue().intValue() , RenderUtil.height() - positionY.getValue().intValue(), 77 ,250);
        }
    }

    public void Taco(){
        if (posX < RenderUtil.width()) {
            posX += AnimationUtils.delta * 0.1;
        } else {
            posX = 0;
        }
        int state = (mc.thePlayer.ticksExisted % 12) + 1;
        RenderUtil.drawImage(new ResourceLocation("Pendant/taco/" + state + ".png"), posX, RenderUtil.height() - 80, 42, 27);
    }


}
