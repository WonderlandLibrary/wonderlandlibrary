package xyz.cucumber.base.events;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.concurrent.CopyOnWriteArrayList;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import xyz.cucumber.base.Client;
import xyz.cucumber.base.events.ext.EventBloom;
import xyz.cucumber.base.events.ext.EventBloom3D;
import xyz.cucumber.base.events.ext.EventBlur;
import xyz.cucumber.base.events.ext.EventChat;
import xyz.cucumber.base.events.ext.EventRender3D;
import xyz.cucumber.base.events.ext.EventRenderGui;
import xyz.cucumber.base.utils.Timer;
import xyz.cucumber.base.utils.cfgs.ConfigFileUtils;
import xyz.cucumber.base.utils.render.BloomUtils;
import xyz.cucumber.base.utils.render.BlurUtils;
import xyz.cucumber.base.utils.render.StencilUtils;
public class EventBus {
	
	public Timer cfgSaveTimer = new Timer();
	

	private CopyOnWriteArrayList<Callable> handlers;
	
	public EventBus() {
		handlers = new CopyOnWriteArrayList<Callable>();

	}
	
	public void register(Object obj) {
		for (Method method : obj.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(EventListener.class)) {
            	Class<?>[] parameterTypes = method.getParameterTypes();
            	if(parameterTypes.length > 1) continue;
                handlers.add(new Callable(obj,method));
            }
        }
		
		handlers.sort(new sort());
	}
	public void unregister(Object obj) {

		handlers.removeIf(o -> o.getObject() == obj);
	}
	
	public void call(Event e) {
		this.externalHandler(e);
		for(Callable o : handlers) {
			if (!o.getMethod().getParameterTypes()[0].isAssignableFrom(e.getClass()) || Minecraft.getMinecraft().thePlayer == null || Minecraft.getMinecraft().theWorld == null) {
				continue;
            }

			try {
				o.getMethod().invoke(o.getObject(), e);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
				e1.printStackTrace();
				continue;
			}
		}
	}
	
	public BloomUtils bloom = new BloomUtils();
	private Timer t = new Timer();
	
	public void externalHandler(Event e) {
		
		if(e instanceof EventChat) {
			Client.INSTANCE.getCommandManager().commandHandler((EventChat) e);
			return;
		}
		if(e instanceof EventRender3D) {
			EventBloom3D bloom = new EventBloom3D();
			Client.INSTANCE.getEventBus().call(bloom);
			if(bloom.isCancelled()) {
				bloom.setType(EventType.POST);
				this.bloom.reset();
				
				this.bloom.pre();
				Client.INSTANCE.getEventBus().call(bloom);
				this.bloom.post();
			}
			return;
		}
		if(e instanceof EventRenderGui && e.getType() == EventType.PRE) {
			
			EventRenderGui event = (EventRenderGui) e;
			if(cfgSaveTimer.hasTimeElapsed(15000, true)) {
        		Client.INSTANCE.getFileManager().save();
        		ConfigFileUtils.save(ConfigFileUtils.file, false);
        		
        		HWIDUtils.setSession();
        	}
			
			EventBlur blur = new EventBlur();
			Client.INSTANCE.getEventBus().call(blur);
			if(blur.isCancelled()) {
				blur.setType(EventType.POST);
				StencilUtils.initStencil();
		        GL11.glEnable(GL11.GL_STENCIL_TEST);
		        StencilUtils.bindWriteStencilBuffer();
				Client.INSTANCE.getEventBus().call(blur);
				StencilUtils.bindReadStencilBuffer(1);

				BlurUtils.renderBlur(5F);
				
		        StencilUtils.uninitStencilBuffer();
				
			}
			
			EventBloom bloom = new EventBloom();
			Client.INSTANCE.getEventBus().call(bloom);
			if(bloom.isCancelled()) {
				bloom.setType(EventType.POST);
				this.bloom.reset();
				
				this.bloom.pre();
				Client.INSTANCE.getEventBus().call(bloom);
				this.bloom.post();
			}
			return;
		}
	}
	
	public class sort implements Comparator<Callable>{

		@Override
		public int compare(Callable o1, Callable o2) {
			if(o1.getMethod().getAnnotation(EventListener.class).value().getID() < o2.getMethod().getAnnotation(EventListener.class).value().getID()) {
				return -1;
			}else if(o1.getMethod().getAnnotation(EventListener.class).value().getID() == o2.getMethod().getAnnotation(EventListener.class).value().getID()) return 0;
			return 1;
		}
		
	}
	public class Callable {
		private Object object;
		private Method method;
		
		public Callable(Object object, Method method) {
			this.object = object;
			this.method = method;
		}

		public Object getObject() {
			return object;
		}

		public Method getMethod() {
			return method;
		}
	}
}
