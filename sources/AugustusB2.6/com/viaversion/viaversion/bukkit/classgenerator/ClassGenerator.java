// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.bukkit.classgenerator;

import com.viaversion.viaversion.classgenerator.generated.BasicHandlerConstructor;
import org.bukkit.event.EventException;
import java.lang.reflect.Method;
import org.bukkit.event.Event;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import com.viaversion.viaversion.libs.javassist.CtNewMethod;
import com.viaversion.viaversion.libs.javassist.CtNewConstructor;
import com.viaversion.viaversion.libs.javassist.CtField;
import com.viaversion.viaversion.libs.javassist.expr.ConstructorCall;
import com.viaversion.viaversion.libs.javassist.expr.ExprEditor;
import org.bukkit.plugin.Plugin;
import com.viaversion.viaversion.libs.javassist.NotFoundException;
import com.viaversion.viaversion.libs.javassist.CannotCompileException;
import com.viaversion.viaversion.libs.javassist.CtMethod;
import com.viaversion.viaversion.libs.javassist.CtClass;
import com.viaversion.viaversion.bukkit.handlers.BukkitEncodeHandler;
import com.viaversion.viaversion.bukkit.handlers.BukkitDecodeHandler;
import com.viaversion.viaversion.bukkit.util.NMSUtil;
import com.viaversion.viaversion.libs.javassist.ClassPath;
import com.viaversion.viaversion.libs.javassist.LoaderClassPath;
import org.bukkit.Bukkit;
import com.viaversion.viaversion.libs.javassist.ClassPool;
import com.viaversion.viaversion.ViaVersionPlugin;
import com.viaversion.viaversion.classgenerator.generated.HandlerConstructor;

public final class ClassGenerator
{
    private static final boolean useModules;
    private static HandlerConstructor constructor;
    private static String psPackage;
    private static Class psConnectListener;
    
    public static HandlerConstructor getConstructor() {
        return ClassGenerator.constructor;
    }
    
    public static void generate() {
        if (!ViaVersionPlugin.getInstance().isCompatSpigotBuild()) {
            if (!ViaVersionPlugin.getInstance().isProtocolSupport()) {
                return;
            }
        }
        try {
            final ClassPool pool = ClassPool.getDefault();
            pool.insertClassPath(new LoaderClassPath(Bukkit.class.getClassLoader()));
            for (final Plugin p : Bukkit.getPluginManager().getPlugins()) {
                pool.insertClassPath(new LoaderClassPath(p.getClass().getClassLoader()));
            }
            Label_0237: {
                if (ViaVersionPlugin.getInstance().isCompatSpigotBuild()) {
                    final Class decodeSuper = NMSUtil.nms("PacketDecoder", "net.minecraft.network.PacketDecoder");
                    final Class encodeSuper = NMSUtil.nms("PacketEncoder", "net.minecraft.network.PacketEncoder");
                    addSpigotCompatibility(pool, BukkitDecodeHandler.class, decodeSuper);
                    addSpigotCompatibility(pool, BukkitEncodeHandler.class, encodeSuper);
                    break Label_0237;
                }
                if (isMultiplatformPS()) {
                    ClassGenerator.psConnectListener = makePSConnectListener(pool);
                    return;
                }
                try {
                    final String psPackage = getOldPSPackage();
                    final Class decodeSuper2 = Class.forName(psPackage.equals("unknown") ? "protocolsupport.protocol.pipeline.common.PacketDecoder" : (psPackage + ".wrapped.WrappedDecoder"));
                    final Class encodeSuper2 = Class.forName(psPackage.equals("unknown") ? "protocolsupport.protocol.pipeline.common.PacketEncoder" : (psPackage + ".wrapped.WrappedEncoder"));
                    addPSCompatibility(pool, BukkitDecodeHandler.class, decodeSuper2);
                    addPSCompatibility(pool, BukkitEncodeHandler.class, encodeSuper2);
                    final CtClass generated = pool.makeClass("com.viaversion.viaversion.classgenerator.generated.GeneratedConstructor");
                    final CtClass handlerInterface = pool.get(HandlerConstructor.class.getName());
                    generated.setInterfaces(new CtClass[] { handlerInterface });
                    pool.importPackage("com.viaversion.viaversion.classgenerator.generated");
                    pool.importPackage("com.viaversion.viaversion.classgenerator");
                    pool.importPackage("com.viaversion.viaversion.api.connection");
                    pool.importPackage("io.netty.handler.codec");
                    generated.addMethod(CtMethod.make("public MessageToByteEncoder newEncodeHandler(UserConnection info, MessageToByteEncoder minecraftEncoder) {\n        return new BukkitEncodeHandler(info, minecraftEncoder);\n    }", generated));
                    generated.addMethod(CtMethod.make("public ByteToMessageDecoder newDecodeHandler(UserConnection info, ByteToMessageDecoder minecraftDecoder) {\n        return new BukkitDecodeHandler(info, minecraftDecoder);\n    }", generated));
                    ClassGenerator.constructor = (HandlerConstructor)toClass(generated).getConstructor((Class<?>[])new Class[0]).newInstance(new Object[0]);
                }
                catch (CannotCompileException e) {
                    e.printStackTrace();
                }
            }
        }
        catch (ReflectiveOperationException ex) {}
        catch (CannotCompileException ex2) {}
        catch (NotFoundException ex3) {}
    }
    
    private static void addSpigotCompatibility(final ClassPool pool, final Class input, final Class superclass) {
        final String newName = "com.viaversion.viaversion.classgenerator.generated." + input.getSimpleName();
        try {
            final CtClass generated = pool.getAndRename(input.getName(), newName);
            if (superclass != null) {
                final CtClass toExtend = pool.get(superclass.getName());
                generated.setSuperclass(toExtend);
                if (superclass.getName().startsWith("net.minecraft") && generated.getConstructors().length != 0) {
                    generated.getConstructors()[0].instrument(new ExprEditor() {
                        @Override
                        public void edit(final ConstructorCall c) throws CannotCompileException {
                            if (c.isSuper()) {
                                c.replace("super(null);");
                            }
                            super.edit(c);
                        }
                    });
                }
            }
            toClass(generated);
        }
        catch (NotFoundException e) {
            e.printStackTrace();
        }
        catch (CannotCompileException e2) {
            e2.printStackTrace();
        }
    }
    
    private static void addPSCompatibility(final ClassPool pool, final Class input, final Class superclass) {
        final boolean newPS = getOldPSPackage().equals("unknown");
        final String newName = "com.viaversion.viaversion.classgenerator.generated." + input.getSimpleName();
        try {
            final CtClass generated = pool.getAndRename(input.getName(), newName);
            if (superclass != null) {
                final CtClass toExtend = pool.get(superclass.getName());
                generated.setSuperclass(toExtend);
                if (!newPS) {
                    pool.importPackage(getOldPSPackage());
                    pool.importPackage(getOldPSPackage() + ".wrapped");
                    if (superclass.getName().endsWith("Decoder")) {
                        generated.addMethod(CtMethod.make("public void setRealDecoder(IPacketDecoder dec) {\n        ((WrappedDecoder) this.minecraftDecoder).setRealDecoder(dec);\n    }", generated));
                    }
                    else {
                        pool.importPackage("protocolsupport.api");
                        pool.importPackage("java.lang.reflect");
                        generated.addMethod(CtMethod.make("public void setRealEncoder(IPacketEncoder enc) {\n         try {\n             Field field = enc.getClass().getDeclaredField(\"version\");\n             field.setAccessible(true);\n             ProtocolVersion version = (ProtocolVersion) field.get(enc);\n             if (version == ProtocolVersion.MINECRAFT_FUTURE) enc = enc.getClass().getConstructor(\n                 new Class[]{ProtocolVersion.class}).newInstance(new Object[] {ProtocolVersion.getLatest()});\n         } catch (Exception e) {\n         }\n        ((WrappedEncoder) this.minecraftEncoder).setRealEncoder(enc);\n    }", generated));
                    }
                }
            }
            toClass(generated);
        }
        catch (NotFoundException e) {
            e.printStackTrace();
        }
        catch (CannotCompileException e2) {
            e2.printStackTrace();
        }
    }
    
    private static Class makePSConnectListener(final ClassPool pool) {
        final HandshakeProtocolType type = handshakeVersionMethod();
        try {
            final CtClass toExtend = pool.get("protocolsupport.api.Connection$PacketListener");
            final CtClass connectListenerClazz = pool.makeClass("com.viaversion.viaversion.classgenerator.generated.ProtocolSupportConnectListener");
            connectListenerClazz.setSuperclass(toExtend);
            pool.importPackage("java.util.Arrays");
            pool.importPackage("protocolsupport.api.ProtocolVersion");
            pool.importPackage("protocolsupport.api.ProtocolType");
            pool.importPackage("protocolsupport.api.Connection");
            pool.importPackage("protocolsupport.api.Connection.PacketListener");
            pool.importPackage("protocolsupport.api.Connection.PacketListener.PacketEvent");
            pool.importPackage("protocolsupport.protocol.ConnectionImpl");
            pool.importPackage(NMSUtil.nms("PacketHandshakingInSetProtocol", "net.minecraft.network.protocol.handshake.PacketHandshakingInSetProtocol").getName());
            connectListenerClazz.addField(CtField.make("private ConnectionImpl connection;", connectListenerClazz));
            connectListenerClazz.addConstructor(CtNewConstructor.make("public ProtocolSupportConnectListener (ConnectionImpl connection) {\n    this.connection = connection;\n}", connectListenerClazz));
            connectListenerClazz.addMethod(CtNewMethod.make("public void onPacketReceiving(protocolsupport.api.Connection.PacketListener.PacketEvent event) {\n    if (event.getPacket() instanceof PacketHandshakingInSetProtocol) {\n        PacketHandshakingInSetProtocol packet = (PacketHandshakingInSetProtocol) event.getPacket();\n        int protoVersion = packet." + type.methodName() + "();\n        if (connection.getVersion() == ProtocolVersion.MINECRAFT_FUTURE && protoVersion == com.viaversion.viaversion.api.Via.getAPI().getServerVersion().lowestSupportedVersion()) {\n            connection.setVersion(ProtocolVersion.getLatest(ProtocolType.PC));\n        }\n    }\n    connection.removePacketListener(this);\n}", connectListenerClazz));
            return toClass(connectListenerClazz);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static void registerPSConnectListener(final ViaVersionPlugin plugin) {
        if (ClassGenerator.psConnectListener != null) {
            try {
                final Class<? extends Event> connectionOpenEvent = (Class<? extends Event>)Class.forName("protocolsupport.api.events.ConnectionOpenEvent");
                Bukkit.getPluginManager().registerEvent((Class)connectionOpenEvent, (Listener)new Listener() {}, EventPriority.HIGH, (EventExecutor)new EventExecutor() {
                    public void execute(final Listener listener, final Event event) throws EventException {
                        try {
                            final Object connection = event.getClass().getMethod("getConnection", (Class<?>[])new Class[0]).invoke(event, new Object[0]);
                            final Object connectListener = ClassGenerator.psConnectListener.getConstructor(connection.getClass()).newInstance(connection);
                            final Method addConnectListener = connection.getClass().getMethod("addPacketListener", Class.forName("protocolsupport.api.Connection$PacketListener"));
                            addConnectListener.invoke(connection, connectListener);
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, (Plugin)plugin);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public static Class getPSConnectListener() {
        return ClassGenerator.psConnectListener;
    }
    
    public static String getOldPSPackage() {
        if (ClassGenerator.psPackage == null) {
            try {
                Class.forName("protocolsupport.protocol.core.IPacketDecoder");
                ClassGenerator.psPackage = "protocolsupport.protocol.core";
            }
            catch (ClassNotFoundException e) {
                try {
                    Class.forName("protocolsupport.protocol.pipeline.IPacketDecoder");
                    ClassGenerator.psPackage = "protocolsupport.protocol.pipeline";
                }
                catch (ClassNotFoundException e2) {
                    ClassGenerator.psPackage = "unknown";
                }
            }
        }
        return ClassGenerator.psPackage;
    }
    
    public static boolean isMultiplatformPS() {
        try {
            Class.forName("protocolsupport.zplatform.impl.spigot.network.pipeline.SpigotPacketEncoder");
            return true;
        }
        catch (ClassNotFoundException e) {
            return false;
        }
    }
    
    public static HandshakeProtocolType handshakeVersionMethod() {
        Class<?> clazz = null;
        try {
            clazz = NMSUtil.nms("PacketHandshakingInSetProtocol", "net.minecraft.network.protocol.handshake.PacketHandshakingInSetProtocol");
            clazz.getMethod("getProtocolVersion", (Class<?>[])new Class[0]);
            return HandshakeProtocolType.MAPPED;
        }
        catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        catch (NoSuchMethodException ex) {
            try {
                if (clazz.getMethod("b", (Class<?>[])new Class[0]).getReturnType() == Integer.TYPE) {
                    return HandshakeProtocolType.OBFUSCATED_OLD;
                }
                if (clazz.getMethod("c", (Class<?>[])new Class[0]).getReturnType() == Integer.TYPE) {
                    return HandshakeProtocolType.OBFUSCATED_NEW;
                }
                throw new UnsupportedOperationException("Protocol version method not found in " + clazz.getSimpleName());
            }
            catch (ReflectiveOperationException e2) {
                throw new RuntimeException(e2);
            }
        }
    }
    
    private static Class<?> toClass(final CtClass ctClass) throws CannotCompileException {
        return ClassGenerator.useModules ? ctClass.toClass(HandlerConstructor.class) : ctClass.toClass(HandlerConstructor.class.getClassLoader());
    }
    
    private static boolean hasModuleMethod() {
        try {
            Class.class.getDeclaredMethod("getModule", (Class<?>[])new Class[0]);
            return true;
        }
        catch (NoSuchMethodException e) {
            return false;
        }
    }
    
    static {
        useModules = hasModuleMethod();
        ClassGenerator.constructor = new BasicHandlerConstructor();
    }
    
    private enum HandshakeProtocolType
    {
        MAPPED("getProtocolVersion"), 
        OBFUSCATED_OLD("b"), 
        OBFUSCATED_NEW("c");
        
        private final String methodName;
        
        private HandshakeProtocolType(final String methodName) {
            this.methodName = methodName;
        }
        
        public String methodName() {
            return this.methodName;
        }
    }
}
