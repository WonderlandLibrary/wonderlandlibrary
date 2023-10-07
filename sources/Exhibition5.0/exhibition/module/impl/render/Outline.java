// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.module.impl.render;

import java.util.HashMap;
import net.minecraft.client.renderer.OpenGlHelper;
import org.lwjgl.opengl.GL11;
import exhibition.event.impl.EventMotion;
import exhibition.event.RegisterEvent;
import exhibition.event.Event;
import exhibition.module.data.Setting;
import exhibition.module.data.ModuleData;
import exhibition.module.Module;

public class Outline extends Module
{
    public static boolean teamCheck;
    public String TEAM;
    
    public Outline(final ModuleData data) {
        super(data);
        this.TEAM = "TEAM";
        ((HashMap<String, Setting<Boolean>>)this.settings).put(this.TEAM, new Setting<Boolean>(this.TEAM, false, "Team colors."));
    }
    
    @RegisterEvent(events = { EventMotion.class })
    @Override
    public void onEvent(final Event event) {
        Outline.teamCheck = ((HashMap<K, Setting<Boolean>>)this.settings).get(this.TEAM).getValue();
    }
    
    public static void renderOne() {
        GL11.glPushAttrib(1048575);
        GL11.glDisable(3008);
        GL11.glDisable(3553);
        GL11.glDisable(2896);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glLineWidth(0.2f);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glEnable(2960);
        GL11.glClear(1024);
        GL11.glClearStencil(15);
        GL11.glStencilFunc(512, 1, 15);
        GL11.glStencilOp(7681, 7681, 7681);
        GL11.glLineWidth(4.0f);
        GL11.glStencilOp(7681, 7681, 7681);
        GL11.glPolygonMode(1032, 6913);
    }
    
    public static void renderTwo() {
        GL11.glStencilFunc(512, 0, 15);
        GL11.glStencilOp(7681, 7681, 7681);
        GL11.glPolygonMode(1032, 6914);
    }
    
    public static void renderThree() {
        GL11.glStencilFunc(514, 1, 15);
        GL11.glStencilOp(7680, 7680, 7680);
        GL11.glPolygonMode(1032, 6913);
    }
    
    public static void renderFour() {
        GL11.glEnable(10754);
        GL11.glPolygonOffset(1.0f, -2000000.0f);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0f, 240.0f);
    }
    
    public static void renderFive() {
        GL11.glPolygonOffset(1.0f, 2000000.0f);
        GL11.glDisable(10754);
        GL11.glDisable(2960);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glDisable(3042);
        GL11.glEnable(2896);
        GL11.glEnable(3553);
        GL11.glEnable(3008);
        GL11.glPopAttrib();
    }
}
