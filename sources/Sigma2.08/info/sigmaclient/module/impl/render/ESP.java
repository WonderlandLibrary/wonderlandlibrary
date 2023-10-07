package info.sigmaclient.module.impl.render;

import info.sigmaclient.event.Event;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventUpdate;
import info.sigmaclient.event.impl.EventRenderGui;
import info.sigmaclient.module.Module;

/**
 * Created by cool1 on 2/4/2017.
 */
public class ESP extends Module {

    public ESP(ModuleData data) {
        super(data);
    }

    @Override
    @RegisterEvent(events = {EventRenderGui.class, EventUpdate.class})
    public void onEvent(Event event) {
        if(event instanceof EventRenderGui) {
            EventRenderGui er = (EventRenderGui)event;
/*
            Vec3 Position = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
            Vec3 Transform = new Vec3(0,0,0);

            //Get the Dot Products from the View Angles of the player
            Transform.xCoord = DotProduct(Position, RefDef->viewAxis[1]);
            Transform.yCoord = DotProduct(Position, RefDef->viewAxis[2]);
            Transform.zCoord = DotProduct(Position, RefDef->viewAxis[0]);

            //Make sure the enemy is in front of the player. If not, return.
            if (Transform.z < 0.1f)
                return false;

            //Calculate the center of the screen
            Vector2D Center = Vector2D((float)RefDef->Width * 0.5f, (float)RefDef->Height * 0.5f);

            //Calculates the screen coordinates
            ScreenX = Center.x * (1 - (Transform.x / RefDef->fov.x / Transform.z));
            ScreenY = Center.y * (1 - (Transform.y / RefDef->fov.y / Transform.z));
*/


        }
    }

}
