package CakeClient;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import CakeClient.modules.combat.KillAura;
import CakeClient.modules.movement.NoFall;
import CakeClient.modules.movement.Fly;
import CakeClient.modules.render.FullBright;
import CakeClient.modules.render.Xray;
import CakeClient.modules.movement.PermaSprint;
import CakeClient.modules.Module;
import CakeClient.modules.combat.AutoWtap;
import CakeClient.modules.movement.NoSlowDown;
import CakeClient.modules.movement.Speed;
import CakeClient.modules.movement.DontFallFromBlock;
import CakeClient.modules.movement.AntiKnockback;


import CakeClient.ui.HUD;
import ModuleSources.XraySource;
import net.minecraft.util.ObjectIntIdentityMap;

public class Client
{
    public static String name;
    public static HUD hud;
    public static Module[] modules;
    public static Integer safetyKey = Keyboard.KEY_O;
    
    static {
    	XraySource.initXRayBlocks();
        Client.name = "Cake Client 1.8";
        Client.hud = new HUD(35, 200, 208, Keyboard.KEY_NUMPAD0, Keyboard.KEY_LEFT, Keyboard.KEY_RIGHT);
        Client.modules = new Module[] { 
        		new FullBright(),
        		new Xray(),
        		// new Module("dummy"),
        		new Fly(),
        		new PermaSprint(),
        		new NoFall(),  
        		new KillAura(),
        		new AutoWtap(),
        		new NoSlowDown(),
        		new Speed(),
        		new AntiKnockback(),
        		new DontFallFromBlock()
        		};
    }
    
    public static void startup() {
        Display.setTitle(Client.name);
        System.out.println("Loaded " + Client.modules.length + " modules");
    }
    
    public static void onUpdate() {
        Module[] modules;
        for (int length = (modules = Client.modules).length, i = 0; i < length; ++i) {
            final Module m = modules[i];
            if (m.enabled) {
                m.onUpdate();
            }
        }
    }
    
    public static void onDraw() {
        Client.hud.onDraw();
        Module[] modules;
        for (int length = (modules = Client.modules).length, i = 0; i < length; ++i) {
            final Module m = modules[i];
            if (m.enabled) {
                m.onDraw();
            }
        }
    }
    
    public static void onKeyPress(final int key) {
    	if (key == safetyKey)
    	{
    		for (Module module: modules)
    		{
    			module.disable();
    		}
    	}
    	else
    	{
    		Client.hud.keyUpdate(key);
            Module[] modules;
            for (int length = (modules = Client.modules).length, i = 0; i < length; ++i) {
                final Module m = modules[i];
                m.keyUpdate(key);
            }
    	}
    }



}