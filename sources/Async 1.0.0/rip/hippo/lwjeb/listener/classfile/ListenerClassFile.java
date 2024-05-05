package rip.hippo.lwjeb.listener.classfile;

import rip.hippo.lwjeb.io.ByteBuffer;
import rip.hippo.lwjeb.listener.Listener;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * @author Hippo
 * @version 1.0.0, 3/16/21
 * @since 5.2.0
 * <p>
 * A listener class file is a class file format wrapper designed specifically for invoking listeners.
 * </p>
 */
public final class ListenerClassFile {

  /**
   * The parent class.
   */
  private final Class<?> parent;

  /**
   * The topic.
   */
  private final Class<?> topic;

  /**
   * The method to invoke.
   */
  private final Method method;

  /**
   * The new class file name.
   */
  private final String className;

  /**
   * Creates a new listener class file.
   *
   * @param parent    The parent class.
   * @param topic     The topic.
   * @param method    The method.
   * @param className The class name.
   */
  public ListenerClassFile(Class<?> parent, Class<?> topic, Method method, String className) {
    this.parent = parent;
    this.topic = topic;
    this.method = method;
    this.className = className;
  }

  /**
   * Gets a method descriptor from a method object.
   *
   * @param method The method.
   * @return The descriptor.
   */
  private static String getMethodDescriptor(Method method) {
    StringBuilder stringBuilder = new StringBuilder();

    stringBuilder.append("(");

    for (Class<?> parameterType : method.getParameterTypes()) {
      Class<?> current = parameterType;
      while (current.isArray()) {
        stringBuilder.append('[');
        current = current.getComponentType();
      }
      getDescriptor(stringBuilder, current);
    }

    stringBuilder.append(")");
    getDescriptor(stringBuilder, method.getReturnType());
    return stringBuilder.toString();
  }

  /**
   * Gets a descriptor from a class type and appends it to {@code stringBuilder}.
   *
   * @param stringBuilder The string builder.
   * @param current       The class.
   */
  private static void getDescriptor(StringBuilder stringBuilder, Class<?> current) {
    if (current.isPrimitive()) {
      switch (current.getTypeName()) {
        case "void":
          stringBuilder.append("V");
          break;
        case "boolean":
          stringBuilder.append("Z");
          break;
        case "byte":
          stringBuilder.append("B");
          break;
        case "short":
          stringBuilder.append("S");
          break;
        case "char":
          stringBuilder.append("C");
          break;
        case "int":
          stringBuilder.append("I");
          break;
        case "float":
          stringBuilder.append("F");
          break;
        case "double":
          stringBuilder.append("D");
          break;
        case "long":
          stringBuilder.append("J");
      }
    } else {
      stringBuilder.append("L").append(current.getName().replace(".", "/")).append(";");
    }
  }

  /**
   * Uses a {@link ByteBuffer} to write a new class file.
   *
   * @return The class file.
   * @throws IOException If an IO error occurs.
   */
  public byte[] toByteArray() throws IOException {
    String methodDescriptor = getMethodDescriptor(method);
    String listenerInternal = Listener.class.getName().replace('.', '/');

    try (ByteBuffer byteBuffer = new ByteBuffer()) {
      byteBuffer.putInt(0xCAFEBABE); // class header
      byteBuffer.putShort(0); // minor
      byteBuffer.putShort(49); // major

      byteBuffer.putShort(22); // constant pool count

      // write class (and super) name (class info and utf8 info)
      // 1
      byteBuffer.putByte(7);
      byteBuffer.putShort(2);

      // 2
      byteBuffer.putByte(1);
      byteBuffer.putUTF(className);

      // 3
      byteBuffer.putByte(7);
      byteBuffer.putShort(4);

      // 4
      byteBuffer.putByte(1);
      byteBuffer.putUTF("java/lang/Object");

      // add parent info
      // 5
      byteBuffer.putByte(7);
      byteBuffer.putShort(6);

      // 6
      byteBuffer.putByte(1);
      byteBuffer.putUTF(parent.getName().replace('.', '/'));

      // parent method info
      // 7
      byteBuffer.putByte(12);
      byteBuffer.putShort(8);
      byteBuffer.putShort(9);

      // 8
      byteBuffer.putByte(1);
      byteBuffer.putUTF(method.getName());

      // 9
      byteBuffer.putByte(1);
      byteBuffer.putUTF(methodDescriptor);

      // 10
      byteBuffer.putByte(10);
      byteBuffer.putShort(5);
      byteBuffer.putShort(7);

      // 11
      byteBuffer.putByte(1);
      byteBuffer.putUTF("()V");

      // 12
      byteBuffer.putByte(10);
      byteBuffer.putShort(3);
      byteBuffer.putShort(13);

      //13
      byteBuffer.putByte(12);
      byteBuffer.putShort(14);
      byteBuffer.putShort(11);

      // add method constant pool info
      // 14
      byteBuffer.putByte(1);
      byteBuffer.putUTF("<init>");

      // 15
      byteBuffer.putByte(1);
      byteBuffer.putUTF("invoke");

      // 16
      byteBuffer.putByte(1);
      byteBuffer.putUTF("(Ljava/lang/Object;Ljava/lang/Object;)V");

      // Method attributes
      // 17
      byteBuffer.putByte(1);
      byteBuffer.putUTF("Code");

      // Interfaces
      //18
      byteBuffer.putByte(7);
      byteBuffer.putShort(19);

      //19
      byteBuffer.putByte(1);
      byteBuffer.putUTF(listenerInternal);

      // topic
      //20
      byteBuffer.putByte(7);
      byteBuffer.putShort(21);

      // 21
      byteBuffer.putByte(1);
      byteBuffer.putUTF(topic.getName().replace('.', '/'));

      // access flags
      byteBuffer.putShort(49);

      // this class
      byteBuffer.putShort(1);

      // super class
      byteBuffer.putShort(3);

      // interfaces
      byteBuffer.putShort(1);
      byteBuffer.putShort(18);

      // fields
      byteBuffer.putShort(0);

      // methods
      byteBuffer.putShort(2);

      // <init>()V
      byteBuffer.putShort(1);
      byteBuffer.putShort(14);
      byteBuffer.putShort(11);
      byteBuffer.putShort(1);
      byteBuffer.putShort(17);
      byteBuffer.putInt(17);
      byteBuffer.putShort(1);
      byteBuffer.putShort(1);
      byteBuffer.putInt(5);
      byteBuffer.putByte(42);
      byteBuffer.putByte(183);
      byteBuffer.putShort(12);
      byteBuffer.putByte(177);
      byteBuffer.putShort(0);
      byteBuffer.putShort(0);

      // invoke(Ljava/lang/Object;Ljava/lang/Object;)V
      byteBuffer.putShort(1);
      byteBuffer.putShort(15);
      byteBuffer.putShort(16);
      byteBuffer.putShort(1);
      byteBuffer.putShort(17);
      byteBuffer.putInt(24);
      byteBuffer.putShort(2);
      byteBuffer.putShort(3);
      byteBuffer.putInt(12);
      byteBuffer.putByte(43);
      byteBuffer.putByte(192);
      byteBuffer.putShort(5);
      byteBuffer.putByte(44);
      byteBuffer.putByte(192);
      byteBuffer.putShort(20);
      byteBuffer.putByte(182);
      byteBuffer.putShort(10);
      byteBuffer.putByte(177);
      byteBuffer.putShort(0);
      byteBuffer.putShort(0);

      // class attributes
      byteBuffer.putShort(0);

      return byteBuffer.toByteArray();
    }
  }
}
