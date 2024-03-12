package xyz.cucumber.base.module.feat.visuals;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import net.minecraft.util.Vec3;
import xyz.cucumber.base.events.EventListener;
import xyz.cucumber.base.events.ext.EventRender3D;
import xyz.cucumber.base.events.ext.EventTick;
import xyz.cucumber.base.module.Category;
import xyz.cucumber.base.module.Mod;
import xyz.cucumber.base.module.ModuleInfo;
import xyz.cucumber.base.module.settings.ColorSettings;
import xyz.cucumber.base.module.settings.ModeSettings;
import xyz.cucumber.base.module.settings.NumberSettings;
import xyz.cucumber.base.utils.MovementUtils;
import xyz.cucumber.base.utils.RenderUtils;
import xyz.cucumber.base.utils.render.ColorUtils;
@ModuleInfo(category = Category.VISUALS, description = "Displays trail when you walk", name = "Trail")
public class TrailModule extends Mod{

	
	private ArrayList<Vec3> crumbs = new ArrayList();
	private ArrayList<Integer> time = new ArrayList();
	
	public ColorSettings color = new ColorSettings("Color", "Rainbow", -1, -1, 100);
	
	private ModeSettings mode = new ModeSettings("Mode", new String[] {
			"Line", "Gradient"
	});
	private NumberSettings fadeOut = new NumberSettings("Fade time", 2000, 100, 5000, 1);
	
	public TrailModule() {
		this.addSettings(mode, fadeOut,color);
	}
	
	@EventListener
	public void onRender3D(EventRender3D e) {


        GL11.glPushMatrix();
        RenderUtils.start3D();
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glLineWidth(4);
        
        if(mode.getMode().toLowerCase().equals("gradient")) {
        	 GL11.glBegin(GL11.GL_QUAD_STRIP);
    	}else GL11.glBegin(GL11.GL_LINE_STRIP);
        
        
        for(int i = 0; i < crumbs.size(); i++) {
        	Vec3 pos = crumbs.get(i);
        	int t = time.get(i);
        	
        	double a =1f/fadeOut.getValue()*(t- System.nanoTime()/1000000f) ;
        	
        	if(t < System.nanoTime()/1000000f) {
        		
        		crumbs.remove(i);
        		time.remove(i);
        		
        		continue;
        	}
        	
        	RenderUtils.color(ColorUtils.getColor(color, 0, (float)((float)(i) / (float)(crumbs.size()))*360f, 1), (float) a);
        	
        	GL11.glVertex3d(pos.xCoord - mc.getRenderManager().viewerPosX, pos.yCoord - mc.getRenderManager().viewerPosY, pos.zCoord - mc.getRenderManager().viewerPosZ);
        	if(mode.getMode().toLowerCase().equals("gradient")) {
        		RenderUtils.color(ColorUtils.getColor(color, 0, (float)((float)(i) / (float)(crumbs.size()))*360f, 1), (float) 0);
        		GL11.glVertex3d(pos.xCoord - mc.getRenderManager().viewerPosX, pos.yCoord+1 - mc.getRenderManager().viewerPosY, pos.zCoord - mc.getRenderManager().viewerPosZ);
        	}
        	
        }
        
        GL11.glEnd();
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glShadeModel(GL11.GL_FLAT);
        RenderUtils.stop3D();
        GL11.glPopMatrix();
	}
	
	@EventListener
	public void onTick(EventTick e) {
		if(MovementUtils.isMoving() || !mc.thePlayer.onGround) {
			crumbs.add(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ));
			time.add((int) (System.nanoTime()/1000000 + fadeOut.getValue()));
		}
	}
	
}
