/*
 * Decompiled with CFR 0.152.
 */
package lombok.launch;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import lombok.launch.Main;

final class Agent {
    Agent() {
    }

    public static void agentmain(String agentArgs, Instrumentation instrumentation) throws Throwable {
        Agent.runLauncher(agentArgs, instrumentation, true);
    }

    public static void premain(String agentArgs, Instrumentation instrumentation) throws Throwable {
        Agent.runLauncher(agentArgs, instrumentation, false);
    }

    private static void runLauncher(String agentArgs, Instrumentation instrumentation, boolean injected) throws Throwable {
        ClassLoader cl = Main.getShadowClassLoader();
        try {
            Class<?> c2 = cl.loadClass("lombok.core.AgentLauncher");
            Method m2 = c2.getDeclaredMethod("runAgents", String.class, Instrumentation.class, Boolean.TYPE, Class.class);
            m2.invoke(null, agentArgs, instrumentation, injected, Agent.class);
        }
        catch (InvocationTargetException e2) {
            throw e2.getCause();
        }
    }
}

