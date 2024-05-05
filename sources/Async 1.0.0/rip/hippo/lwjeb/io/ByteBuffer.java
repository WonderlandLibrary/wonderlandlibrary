package rip.hippo.lwjeb.io;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author Hippo
 * @version 1.0.0, 3/16/21
 * @since 5.2.0
 * <p>
 * A byte buffer a big-endian buffer used to write bytes.
 * </p>
 */
public final class ByteBuffer implements Closeable {

  /**
   * The back end byte array output stream.
   */
  private final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

  /**
   * The back end data output stream.
   */
  private final DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

  /**
   * Writes a byte to the buffer.
   *
   * @param value The byte.
   * @throws IOException If an IO error occurs.
   */
  public void putByte(int value) throws IOException {
    dataOutputStream.writeByte(value);
  }

  /**
   * Writes a short to the buffer.
   *
   * @param value The short.
   * @throws IOException If an IO error occurs.
   */
  public void putShort(int value) throws IOException {
    dataOutputStream.writeShort(value);
  }

  /**
   * Writes an integer to the buffer.
   *
   * @param value The integer.
   * @throws IOException If an IO error occurs.
   */
  public void putInt(int value) throws IOException {
    dataOutputStream.writeInt(value);
  }

  /**
   * Writes UTF characters to the buffer.
   *
   * @param value The characters.
   * @throws IOException If an IO error occurs.
   */
  public void putUTF(String value) throws IOException {
    dataOutputStream.writeUTF(value);
  }

  /**
   * Gets the bytes.
   *
   * @return The bytes.
   */
  public byte[] toByteArray() {
    return byteArrayOutputStream.toByteArray();
  }

  /**
   * Closes the buffer.
   *
   * @throws IOException If an IO error occurs.
   */
  @Override
  public void close() throws IOException {
    dataOutputStream.close();
  }
}
