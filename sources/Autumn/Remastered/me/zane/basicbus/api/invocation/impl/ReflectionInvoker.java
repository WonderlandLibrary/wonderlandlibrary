package me.zane.basicbus.api.invocation.impl;

import me.zane.basicbus.api.invocation.Invoker;

import java.lang.reflect.Method;

public final class ReflectionInvoker implements Invoker {
    public void invoke(Object instance, Method method, Object... parameters) {
        try {
            method.invoke(instance, parameters);
        } catch (IllegalAccessException|java.lang.reflect.InvocationTargetException illegalAccessException) {}
    }
}