package org.apache.commons.io;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class EndianUtils {
  public static short swapShort(short value) {
    return (short)(((value >> 0 & 0xFF) << 8) + ((value >> 8 & 0xFF) << 0));
  }
  
  public static int swapInteger(int value) {
    return ((value >> 0 & 0xFF) << 24) + ((value >> 8 & 0xFF) << 16) + ((value >> 16 & 0xFF) << 8) + ((value >> 24 & 0xFF) << 0);
  }
  
  public static long swapLong(long value) {
    return ((value >> 0L & 0xFFL) << 56L) + ((value >> 8L & 0xFFL) << 48L) + ((value >> 16L & 0xFFL) << 40L) + ((value >> 24L & 0xFFL) << 32L) + ((value >> 32L & 0xFFL) << 24L) + ((value >> 40L & 0xFFL) << 16L) + ((value >> 48L & 0xFFL) << 8L) + ((value >> 56L & 0xFFL) << 0L);
  }
  
  public static float swapFloat(float value) {
    return Float.intBitsToFloat(swapInteger(Float.floatToIntBits(value)));
  }
  
  public static double swapDouble(double value) {
    return Double.longBitsToDouble(swapLong(Double.doubleToLongBits(value)));
  }
  
  public static void writeSwappedShort(byte[] data, int offset, short value) {
    data[offset + 0] = (byte)(value >> 0 & 0xFF);
    data[offset + 1] = (byte)(value >> 8 & 0xFF);
  }
  
  public static short readSwappedShort(byte[] data, int offset) {
    return (short)(((data[offset + 0] & 0xFF) << 0) + ((data[offset + 1] & 0xFF) << 8));
  }
  
  public static int readSwappedUnsignedShort(byte[] data, int offset) {
    return ((data[offset + 0] & 0xFF) << 0) + ((data[offset + 1] & 0xFF) << 8);
  }
  
  public static void writeSwappedInteger(byte[] data, int offset, int value) {
    data[offset + 0] = (byte)(value >> 0 & 0xFF);
    data[offset + 1] = (byte)(value >> 8 & 0xFF);
    data[offset + 2] = (byte)(value >> 16 & 0xFF);
    data[offset + 3] = (byte)(value >> 24 & 0xFF);
  }
  
  public static int readSwappedInteger(byte[] data, int offset) {
    return ((data[offset + 0] & 0xFF) << 0) + ((data[offset + 1] & 0xFF) << 8) + ((data[offset + 2] & 0xFF) << 16) + ((data[offset + 3] & 0xFF) << 24);
  }
  
  public static long readSwappedUnsignedInteger(byte[] data, int offset) {
    long low = (((data[offset + 0] & 0xFF) << 0) + ((data[offset + 1] & 0xFF) << 8) + ((data[offset + 2] & 0xFF) << 16));
    long high = (data[offset + 3] & 0xFF);
    return (high << 24L) + (0xFFFFFFFFL & low);
  }
  
  public static void writeSwappedLong(byte[] data, int offset, long value) {
    data[offset + 0] = (byte)(int)(value >> 0L & 0xFFL);
    data[offset + 1] = (byte)(int)(value >> 8L & 0xFFL);
    data[offset + 2] = (byte)(int)(value >> 16L & 0xFFL);
    data[offset + 3] = (byte)(int)(value >> 24L & 0xFFL);
    data[offset + 4] = (byte)(int)(value >> 32L & 0xFFL);
    data[offset + 5] = (byte)(int)(value >> 40L & 0xFFL);
    data[offset + 6] = (byte)(int)(value >> 48L & 0xFFL);
    data[offset + 7] = (byte)(int)(value >> 56L & 0xFFL);
  }
  
  public static long readSwappedLong(byte[] data, int offset) {
    long low = (((data[offset + 0] & 0xFF) << 0) + ((data[offset + 1] & 0xFF) << 8) + ((data[offset + 2] & 0xFF) << 16) + ((data[offset + 3] & 0xFF) << 24));
    long high = (((data[offset + 4] & 0xFF) << 0) + ((data[offset + 5] & 0xFF) << 8) + ((data[offset + 6] & 0xFF) << 16) + ((data[offset + 7] & 0xFF) << 24));
    return (high << 32L) + (0xFFFFFFFFL & low);
  }
  
  public static void writeSwappedFloat(byte[] data, int offset, float value) {
    writeSwappedInteger(data, offset, Float.floatToIntBits(value));
  }
  
  public static float readSwappedFloat(byte[] data, int offset) {
    return Float.intBitsToFloat(readSwappedInteger(data, offset));
  }
  
  public static void writeSwappedDouble(byte[] data, int offset, double value) {
    writeSwappedLong(data, offset, Double.doubleToLongBits(value));
  }
  
  public static double readSwappedDouble(byte[] data, int offset) {
    return Double.longBitsToDouble(readSwappedLong(data, offset));
  }
  
  public static void writeSwappedShort(OutputStream output, short value) throws IOException {
    output.write((byte)(value >> 0 & 0xFF));
    output.write((byte)(value >> 8 & 0xFF));
  }
  
  public static short readSwappedShort(InputStream input) throws IOException {
    return (short)(((read(input) & 0xFF) << 0) + ((read(input) & 0xFF) << 8));
  }
  
  public static int readSwappedUnsignedShort(InputStream input) throws IOException {
    int value1 = read(input);
    int value2 = read(input);
    return ((value1 & 0xFF) << 0) + ((value2 & 0xFF) << 8);
  }
  
  public static void writeSwappedInteger(OutputStream output, int value) throws IOException {
    output.write((byte)(value >> 0 & 0xFF));
    output.write((byte)(value >> 8 & 0xFF));
    output.write((byte)(value >> 16 & 0xFF));
    output.write((byte)(value >> 24 & 0xFF));
  }
  
  public static int readSwappedInteger(InputStream input) throws IOException {
    int value1 = read(input);
    int value2 = read(input);
    int value3 = read(input);
    int value4 = read(input);
    return ((value1 & 0xFF) << 0) + ((value2 & 0xFF) << 8) + ((value3 & 0xFF) << 16) + ((value4 & 0xFF) << 24);
  }
  
  public static long readSwappedUnsignedInteger(InputStream input) throws IOException {
    int value1 = read(input);
    int value2 = read(input);
    int value3 = read(input);
    int value4 = read(input);
    long low = (((value1 & 0xFF) << 0) + ((value2 & 0xFF) << 8) + ((value3 & 0xFF) << 16));
    long high = (value4 & 0xFF);
    return (high << 24L) + (0xFFFFFFFFL & low);
  }
  
  public static void writeSwappedLong(OutputStream output, long value) throws IOException {
    output.write((byte)(int)(value >> 0L & 0xFFL));
    output.write((byte)(int)(value >> 8L & 0xFFL));
    output.write((byte)(int)(value >> 16L & 0xFFL));
    output.write((byte)(int)(value >> 24L & 0xFFL));
    output.write((byte)(int)(value >> 32L & 0xFFL));
    output.write((byte)(int)(value >> 40L & 0xFFL));
    output.write((byte)(int)(value >> 48L & 0xFFL));
    output.write((byte)(int)(value >> 56L & 0xFFL));
  }
  
  public static long readSwappedLong(InputStream input) throws IOException {
    byte[] bytes = new byte[8];
    for (int i = 0; i < 8; i++)
      bytes[i] = (byte)read(input); 
    return readSwappedLong(bytes, 0);
  }
  
  public static void writeSwappedFloat(OutputStream output, float value) throws IOException {
    writeSwappedInteger(output, Float.floatToIntBits(value));
  }
  
  public static float readSwappedFloat(InputStream input) throws IOException {
    return Float.intBitsToFloat(readSwappedInteger(input));
  }
  
  public static void writeSwappedDouble(OutputStream output, double value) throws IOException {
    writeSwappedLong(output, Double.doubleToLongBits(value));
  }
  
  public static double readSwappedDouble(InputStream input) throws IOException {
    return Double.longBitsToDouble(readSwappedLong(input));
  }
  
  private static int read(InputStream input) throws IOException {
    int value = input.read();
    if (-1 == value)
      throw new EOFException("Unexpected EOF reached"); 
    return value;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\io\EndianUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */