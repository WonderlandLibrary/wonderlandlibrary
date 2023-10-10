// 
// Decompiled by Procyon v0.5.36
// 

package net.lenni0451.eventapi.manager;

import javassist.NotFoundException;
import java.util.Iterator;
import javassist.CtMethod;
import javassist.CtConstructor;
import java.lang.reflect.Method;
import javassist.CtNewMethod;
import javassist.CtClass;
import javassist.CtNewConstructor;
import javassist.CtField;
import net.lenni0451.eventapi.injection.IReflectedListener;
import java.lang.reflect.Modifier;
import java.lang.annotation.Annotation;
import net.lenni0451.eventapi.reflection.EventTarget;
import javassist.ClassPool;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ConcurrentHashMap;
import net.lenni0451.eventapi.listener.IErrorListener;
import java.util.List;
import net.lenni0451.eventapi.listener.IEventListener;
import net.lenni0451.eventapi.injection.IInjectionPipeline;
import net.lenni0451.eventapi.events.IEvent;
import java.util.Map;

public class InjectionEventManager
{
    private static final Map<Class<? extends IEvent>, IInjectionPipeline> EVENT_PIPELINE;
    private static final Map<Class<? extends IEvent>, IEventListener[]> EVENT_LISTENER;
    private static final List<IErrorListener> ERROR_LISTENER;
    
    static {
        EVENT_PIPELINE = new ConcurrentHashMap<Class<? extends IEvent>, IInjectionPipeline>();
        EVENT_LISTENER = new ConcurrentHashMap<Class<? extends IEvent>, IEventListener[]>();
        ERROR_LISTENER = new CopyOnWriteArrayList<IErrorListener>();
    }
    
    public static IEventListener[] getListener(final Class<? extends IEvent> eventType) {
        return InjectionEventManager.EVENT_LISTENER.get(eventType);
    }
    
    public static void call(final IEvent event) {
        if (event != null && InjectionEventManager.EVENT_PIPELINE.containsKey(event.getClass())) {
            try {
                InjectionEventManager.EVENT_PIPELINE.get(event.getClass()).call(event);
                if (InjectionEventManager.EVENT_PIPELINE.containsKey(IEvent.class)) {
                    InjectionEventManager.EVENT_PIPELINE.get(IEvent.class).call(event);
                }
            }
            catch (Throwable e) {
                if (InjectionEventManager.ERROR_LISTENER.isEmpty()) {
                    throw new RuntimeException(e);
                }
                InjectionEventManager.ERROR_LISTENER.forEach(errorListener -> errorListener.catchException(e));
            }
        }
    }
    
    public static <T extends IEventListener> void register(final T listener) {
        register(IEvent.class, listener);
    }
    
    public static void register(final Object listener) {
        final ClassPool cp = ClassPool.getDefault();
        Method[] methods;
        for (int length = (methods = listener.getClass().getMethods()).length, i = 0; i < length; ++i) {
            final Method method = methods[i];
            if (method.isAnnotationPresent(EventTarget.class)) {
                final Class[] methodArguments = method.getParameterTypes();
                if (methodArguments.length == 1 && IEvent.class.isAssignableFrom(methodArguments[0]) && Modifier.isPublic(method.getModifiers()) && !Modifier.isStatic(method.getModifiers())) {
                    method.setAccessible(true);
                    final Class<? extends IEvent> eventType = (Class<? extends IEvent>)methodArguments[0];
                    final String methodName = method.getName();
                    final CtClass newListener = cp.makeClass("InjectionListener_" + System.nanoTime());
                    try {
                        newListener.addInterface(cp.get(IReflectedListener.class.getName()));
                    }
                    catch (Throwable e) {
                        throw new RuntimeException("Class could not implement IReflectedListener", e);
                    }
                    try {
                        newListener.addField(CtField.make("private final " + listener.getClass().getName() + " instance;", newListener));
                    }
                    catch (Exception e2) {
                        throw new RuntimeException("Could not add global variables to class", e2);
                    }
                    try {
                        final CtConstructor construct = CtNewConstructor.make("public " + newListener.getName() + "(" + listener.getClass().getName() + " ob) {this.instance = ob;}", newListener);
                        newListener.addConstructor(construct);
                    }
                    catch (Throwable e) {
                        throw new RuntimeException("Could not create new constructor", e);
                    }
                    final StringBuilder sourceBuilder = new StringBuilder().append("{");
                    sourceBuilder.append("this.instance." + methodName + "((" + eventType.getName() + ") $1);");
                    sourceBuilder.append("}");
                    try {
                        final CtMethod onEventMethod = CtNewMethod.make(CtClass.voidType, cp.get(IEventListener.class.getName()).getDeclaredMethods()[0].getName(), new CtClass[] { cp.get(IEvent.class.getName()) }, new CtClass[] { cp.get(Throwable.class.getName()) }, sourceBuilder.toString(), newListener);
                        newListener.addMethod(onEventMethod);
                    }
                    catch (Throwable e3) {
                        throw new RuntimeException("Could not create new on event method", e3);
                    }
                    try {
                        final CtMethod onEventMethod = CtNewMethod.make(cp.get(Object.class.getName()), cp.get(IReflectedListener.class.getName()).getDeclaredMethods()[0].getName(), new CtClass[0], new CtClass[0], "{return this.instance;}", newListener);
                        newListener.addMethod(onEventMethod);
                    }
                    catch (Exception e4) {
                        throw new RuntimeException("Could not create new get instance method", e4);
                    }
                    Class<?> newListenerClass;
                    try {
                        newListenerClass = (Class<?>)newListener.toClass();
                    }
                    catch (Throwable e5) {
                        throw new RuntimeException("Could not compile class", e5);
                    }
                    Object listenerObject;
                    try {
                        listenerObject = newListenerClass.getConstructors()[0].newInstance(listener);
                    }
                    catch (Throwable e6) {
                        throw new RuntimeException("Could not instantiate new class", e6);
                    }
                    register(eventType, listenerObject);
                }
            }
        }
    }
    
    public static <T extends IEventListener> void register(final Class<? extends IEvent> eventType, final T listener) {
        final IEventListener[] eventListener = InjectionEventManager.EVENT_LISTENER.computeIfAbsent(eventType, c -> new IEventListener[0]);
        final IEventListener[] newEventListener = new IEventListener[eventListener.length + 1];
        InjectionEventManager.EVENT_LISTENER.put(eventType, newEventListener);
        for (int i = 0; i <= eventListener.length; ++i) {
            if (i != eventListener.length) {
                newEventListener[i] = eventListener[i];
            }
            else {
                newEventListener[i] = listener;
            }
        }
        InjectionEventManager.EVENT_PIPELINE.put(eventType, rebuildPipeline(newEventListener));
    }
    
    public static void unregister(final Object listener) {
        for (final Map.Entry<Class<? extends IEvent>, IEventListener[]> entry : InjectionEventManager.EVENT_LISTENER.entrySet()) {
            final IEventListener[] eventListener = InjectionEventManager.EVENT_LISTENER.computeIfAbsent(entry.getKey(), c -> new IEventListener[0]);
            final IEventListener[] newEventListener = new IEventListener[eventListener.length - 1];
            entry.setValue(newEventListener);
            int i = 0;
            int x = 0;
            while (i < eventListener.length) {
                if (!eventListener[i].equals(listener) && eventListener[i] instanceof IReflectedListener && !((IReflectedListener)eventListener[i]).getInstance().equals(listener)) {
                    newEventListener[x] = eventListener[i];
                    ++x;
                }
                ++i;
            }
            InjectionEventManager.EVENT_PIPELINE.put(entry.getKey(), rebuildPipeline(newEventListener));
        }
    }
    
    public static void addErrorListener(final IErrorListener errorListener) {
        if (!InjectionEventManager.ERROR_LISTENER.contains(errorListener)) {
            InjectionEventManager.ERROR_LISTENER.add(errorListener);
        }
    }
    
    public static boolean removeErrorListener(final IErrorListener errorListener) {
        return InjectionEventManager.ERROR_LISTENER.remove(errorListener);
    }
    
    private static IInjectionPipeline rebuildPipeline(final IEventListener[] eventListener) {
        final ClassPool cp = ClassPool.getDefault();
        String methodName = null;
        try {
            CtMethod[] declaredMethods;
            for (int length = (declaredMethods = cp.get(InjectionEventManager.class.getName()).getDeclaredMethods()).length, j = 0; j < length; ++j) {
                final CtMethod method = declaredMethods[j];
                if (method.getReturnType().getSimpleName().equals(cp.get(IEventListener[].class.getName()).getSimpleName())) {
                    methodName = method.getName();
                    break;
                }
            }
            if (methodName == null) {
                throw new NullPointerException();
            }
        }
        catch (Throwable e) {
            throw new IllegalStateException("Could not find method name to get listener array", e);
        }
        final StringBuilder sourceBuilder = new StringBuilder().append("{");
        sourceBuilder.append(String.valueOf(IEventListener.class.getName()) + "[] listener = " + InjectionEventManager.class.getName() + "." + methodName + "($1.getClass());");
        for (int i = 0; i < eventListener.length; ++i) {
            try {
                sourceBuilder.append("listener[" + i + "]." + cp.get(IEventListener.class.getName()).getDeclaredMethods()[0].getName() + "($1);");
            }
            catch (NotFoundException e2) {
                if (InjectionEventManager.ERROR_LISTENER.isEmpty()) {
                    throw new RuntimeException(e2);
                }
                InjectionEventManager.ERROR_LISTENER.forEach(errorListener -> errorListener.catchException(e2));
            }
        }
        sourceBuilder.append("}");
        final CtClass newPipeline = cp.makeClass("InjectionPipeline_" + System.nanoTime());
        try {
            newPipeline.addInterface(cp.get(IInjectionPipeline.class.getName()));
        }
        catch (Throwable e3) {
            throw new RuntimeException("Class could not implement IInjectionPipeline", e3);
        }
        CtMethod method2;
        try {
            method2 = CtNewMethod.make(CtClass.voidType, cp.get(IInjectionPipeline.class.getName()).getDeclaredMethods()[0].getName(), new CtClass[] { cp.get(IEvent.class.getName()) }, new CtClass[] { cp.get(Throwable.class.getName()) }, sourceBuilder.toString(), newPipeline);
        }
        catch (Throwable e4) {
            throw new RuntimeException("Could not create new call method", e4);
        }
        try {
            newPipeline.addMethod(method2);
        }
        catch (Throwable e4) {
            throw new RuntimeException("Could not add call method to class", e4);
        }
        Class<? extends IInjectionPipeline> pipelineClass;
        try {
            pipelineClass = (Class<? extends IInjectionPipeline>)newPipeline.toClass();
        }
        catch (Throwable e5) {
            throw new RuntimeException("Could not compile class", e5);
        }
        IInjectionPipeline pipelineObject;
        try {
            pipelineObject = (IInjectionPipeline)pipelineClass.newInstance();
        }
        catch (Throwable e6) {
            throw new RuntimeException("Could not instantiate new class", e6);
        }
        return pipelineObject;
    }
}
