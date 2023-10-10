/*
 * Decompiled with CFR 0.152.
 */
package javassist;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Comparator;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.ClassFile;
import javassist.bytecode.Descriptor;

public class SerialVersionUID {
    public static void setSerialVersionUID(CtClass clazz) throws CannotCompileException, NotFoundException {
        try {
            clazz.getDeclaredField("serialVersionUID");
            return;
        }
        catch (NotFoundException notFoundException) {
            if (!SerialVersionUID.isSerializable(clazz)) {
                return;
            }
            CtField field = new CtField(CtClass.longType, "serialVersionUID", clazz);
            field.setModifiers(26);
            clazz.addField(field, SerialVersionUID.calculateDefault(clazz) + "L");
            return;
        }
    }

    private static boolean isSerializable(CtClass clazz) throws NotFoundException {
        ClassPool pool = clazz.getClassPool();
        return clazz.subtypeOf(pool.get("java.io.Serializable"));
    }

    public static long calculateDefault(CtClass clazz) throws CannotCompileException {
        try {
            int mods;
            int i2;
            int i3;
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(bout);
            ClassFile classFile = clazz.getClassFile();
            String javaName = SerialVersionUID.javaName(clazz);
            out.writeUTF(javaName);
            CtMethod[] methods = clazz.getDeclaredMethods();
            int classMods = clazz.getModifiers();
            if ((classMods & 0x200) != 0) {
                classMods = methods.length > 0 ? (classMods |= 0x400) : (classMods &= 0xFFFFFBFF);
            }
            out.writeInt(classMods);
            Object[] interfaces = classFile.getInterfaces();
            for (i3 = 0; i3 < interfaces.length; ++i3) {
                interfaces[i3] = SerialVersionUID.javaName((String)interfaces[i3]);
            }
            Arrays.sort(interfaces);
            for (i3 = 0; i3 < interfaces.length; ++i3) {
                out.writeUTF((String)interfaces[i3]);
            }
            CtField[] fields = clazz.getDeclaredFields();
            Arrays.sort(fields, new Comparator<CtField>(){

                @Override
                public int compare(CtField field1, CtField field2) {
                    return field1.getName().compareTo(field2.getName());
                }
            });
            for (int i4 = 0; i4 < fields.length; ++i4) {
                CtField field = fields[i4];
                int mods2 = field.getModifiers();
                if ((mods2 & 2) != 0 && (mods2 & 0x88) != 0) continue;
                out.writeUTF(field.getName());
                out.writeInt(mods2);
                out.writeUTF(field.getFieldInfo2().getDescriptor());
            }
            if (classFile.getStaticInitializer() != null) {
                out.writeUTF("<clinit>");
                out.writeInt(8);
                out.writeUTF("()V");
            }
            CtConstructor[] constructors = clazz.getDeclaredConstructors();
            Arrays.sort(constructors, new Comparator<CtConstructor>(){

                @Override
                public int compare(CtConstructor c1, CtConstructor c2) {
                    return c1.getMethodInfo2().getDescriptor().compareTo(c2.getMethodInfo2().getDescriptor());
                }
            });
            for (i2 = 0; i2 < constructors.length; ++i2) {
                CtConstructor constructor = constructors[i2];
                mods = constructor.getModifiers();
                if ((mods & 2) != 0) continue;
                out.writeUTF("<init>");
                out.writeInt(mods);
                out.writeUTF(constructor.getMethodInfo2().getDescriptor().replace('/', '.'));
            }
            Arrays.sort(methods, new Comparator<CtMethod>(){

                @Override
                public int compare(CtMethod m1, CtMethod m2) {
                    int value = m1.getName().compareTo(m2.getName());
                    if (value == 0) {
                        value = m1.getMethodInfo2().getDescriptor().compareTo(m2.getMethodInfo2().getDescriptor());
                    }
                    return value;
                }
            });
            for (i2 = 0; i2 < methods.length; ++i2) {
                CtMethod method = methods[i2];
                mods = method.getModifiers() & 0xD3F;
                if ((mods & 2) != 0) continue;
                out.writeUTF(method.getName());
                out.writeInt(mods);
                out.writeUTF(method.getMethodInfo2().getDescriptor().replace('/', '.'));
            }
            out.flush();
            MessageDigest digest = MessageDigest.getInstance("SHA");
            byte[] digested = digest.digest(bout.toByteArray());
            long hash = 0L;
            for (int i5 = Math.min(digested.length, 8) - 1; i5 >= 0; --i5) {
                hash = hash << 8 | (long)(digested[i5] & 0xFF);
            }
            return hash;
        }
        catch (IOException e2) {
            throw new CannotCompileException(e2);
        }
        catch (NoSuchAlgorithmException e3) {
            throw new CannotCompileException(e3);
        }
    }

    private static String javaName(CtClass clazz) {
        return Descriptor.toJavaName(Descriptor.toJvmName(clazz));
    }

    private static String javaName(String name) {
        return Descriptor.toJavaName(Descriptor.toJvmName(name));
    }
}

