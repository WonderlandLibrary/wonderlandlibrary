package com.enjoytheban.api;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.enjoytheban.Client;
import com.enjoytheban.api.events.world.EventPostUpdate;
import com.enjoytheban.api.events.world.EventPreUpdate;
import com.enjoytheban.api.events.world.EventTick;
import com.enjoytheban.module.Module;
import com.enjoytheban.utils.Helper;

import net.minecraft.server.MinecraftServer;

/**
 * A basic EventBus/Management system
 */
public class EventBus {

	//Creating shit
	private ConcurrentHashMap<Class<? extends Event>, List<Handler>> registry;
	private final Comparator<Handler> comparator = (h, h1) -> Byte.compare(h.priority, h1.priority);
	private final Lookup lookup = MethodHandles.lookup();
	private static final EventBus instance = new EventBus();

	/**	
	 * returns the default instance
	 */
	public static EventBus getInstance() {
		return instance;
	}

	/**
	 * Used when making new Event Managers
	 */
	public EventBus() {
		this.registry = new ConcurrentHashMap<>();
	}

	/**
	 * Register handlers from the specified Object(s)
	 * 
	 * @param objs
	 */
	public void register(Object... objs) {
	//	MinecraftServer.getServer().theProfiler.startSection("_NAME_");
		for (Object obj : objs) {
			for (Method m : obj.getClass().getDeclaredMethods()) {
				if (m.getParameterCount() != 1 || !m.isAnnotationPresent(EventHandler.class)) {
					continue;
				}
				Class<? extends Event> eventClass = (Class<? extends Event>) m.getParameterTypes()[0];
				if (!this.registry.containsKey(eventClass)) {
					this.registry.put(eventClass, new CopyOnWriteArrayList<>());
				}
				this.registry.get(eventClass)
						.add(new Handler(m, obj, m.getDeclaredAnnotation(EventHandler.class).priority()));
				this.registry.get(eventClass).sort(this.comparator);
			}
		}
	//	MinecraftServer.getServer().theProfiler.endSection();
	}

	/**
	 * Unregister handlers from the specified Object(s)
	 * 
	 * @param objs
	 */
	public void unregister(Object... objs) {
		for (Object obj : objs) {
			for (List<Handler> list : this.registry.values()) {
				for (Handler data : list) {
					if (data.parent != obj) {
						continue;
					}
					list.remove(data);
				}
			}
		}
	}

	/**
	 * Call the Event specified by {@code event} & return it after all handlers
	 * have been called
	 * 
	 * @param event
	 * @return
	 */
	public <E extends Event> E call(E event) {
			boolean whiteListedEvents = event instanceof EventTick  || event instanceof EventPreUpdate || event instanceof EventPostUpdate;

		List<Handler> list = this.registry.get(event.getClass());

		if (!(list == null || list.isEmpty())) {
			for (Handler data : list) {
				try {
					if(list instanceof Module) {
						if(((Module) list).isEnabled()) {
							if(whiteListedEvents)
								Helper.mc.mcProfiler.startSection(((Module) list).getName());
							if(whiteListedEvents)
								Helper.mc.mcProfiler.endSection();
						} 
					} else {
						if(whiteListedEvents)
							Helper.mc.mcProfiler.startSection("non module");
						if(whiteListedEvents)
							Helper.mc.mcProfiler.endSection();
					}
					data.handler.invokeExact(data.parent, event);
				} catch (Throwable e1) {
					e1.printStackTrace();
				}
			}
		}
		
		return event;
	}

	/**
	 * Not much, just holds data for each Handler
	 *
	 */
	private class Handler {
		private MethodHandle handler;
		private Object parent;
		private byte priority;

		/**
		 * Used to make a new Handler
		 * 
		 * @param method
		 * @param parent
		 * @param priority
		 */
		public Handler(Method method, Object parent, byte priority) {
			if (!method.isAccessible()) {
				method.setAccessible(true);
			}
			MethodHandle m = null;
			try {
				m = lookup.unreflect(method);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			if (m != null) {
				this.handler = m.asType(
						m.type().changeParameterType(0, Object.class).changeParameterType(1, Event.class));
			}
			this.parent = parent;
			this.priority = priority;
		}
	}
}
