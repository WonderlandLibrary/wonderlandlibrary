package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class ARBImaging {
   public static final int GL_CONSTANT_COLOR = 32769;
   public static final int GL_ONE_MINUS_CONSTANT_COLOR = 32770;
   public static final int GL_CONSTANT_ALPHA = 32771;
   public static final int GL_ONE_MINUS_CONSTANT_ALPHA = 32772;
   public static final int GL_BLEND_COLOR = 32773;
   public static final int GL_FUNC_ADD = 32774;
   public static final int GL_MIN = 32775;
   public static final int GL_MAX = 32776;
   public static final int GL_BLEND_EQUATION = 32777;
   public static final int GL_FUNC_SUBTRACT = 32778;
   public static final int GL_FUNC_REVERSE_SUBTRACT = 32779;
   public static final int GL_COLOR_MATRIX = 32945;
   public static final int GL_COLOR_MATRIX_STACK_DEPTH = 32946;
   public static final int GL_MAX_COLOR_MATRIX_STACK_DEPTH = 32947;
   public static final int GL_POST_COLOR_MATRIX_RED_SCALE = 32948;
   public static final int GL_POST_COLOR_MATRIX_GREEN_SCALE = 32949;
   public static final int GL_POST_COLOR_MATRIX_BLUE_SCALE = 32950;
   public static final int GL_POST_COLOR_MATRIX_ALPHA_SCALE = 32951;
   public static final int GL_POST_COLOR_MATRIX_RED_BIAS = 32952;
   public static final int GL_POST_COLOR_MATRIX_GREEN_BIAS = 32953;
   public static final int GL_POST_COLOR_MATRIX_BLUE_BIAS = 32954;
   public static final int GL_POST_COLOR_MATRIX_ALPHA_BIAS = 32955;
   public static final int GL_COLOR_TABLE = 32976;
   public static final int GL_POST_CONVOLUTION_COLOR_TABLE = 32977;
   public static final int GL_POST_COLOR_MATRIX_COLOR_TABLE = 32978;
   public static final int GL_PROXY_COLOR_TABLE = 32979;
   public static final int GL_PROXY_POST_CONVOLUTION_COLOR_TABLE = 32980;
   public static final int GL_PROXY_POST_COLOR_MATRIX_COLOR_TABLE = 32981;
   public static final int GL_COLOR_TABLE_SCALE = 32982;
   public static final int GL_COLOR_TABLE_BIAS = 32983;
   public static final int GL_COLOR_TABLE_FORMAT = 32984;
   public static final int GL_COLOR_TABLE_WIDTH = 32985;
   public static final int GL_COLOR_TABLE_RED_SIZE = 32986;
   public static final int GL_COLOR_TABLE_GREEN_SIZE = 32987;
   public static final int GL_COLOR_TABLE_BLUE_SIZE = 32988;
   public static final int GL_COLOR_TABLE_ALPHA_SIZE = 32989;
   public static final int GL_COLOR_TABLE_LUMINANCE_SIZE = 32990;
   public static final int GL_COLOR_TABLE_INTENSITY_SIZE = 32991;
   public static final int GL_CONVOLUTION_1D = 32784;
   public static final int GL_CONVOLUTION_2D = 32785;
   public static final int GL_SEPARABLE_2D = 32786;
   public static final int GL_CONVOLUTION_BORDER_MODE = 32787;
   public static final int GL_CONVOLUTION_FILTER_SCALE = 32788;
   public static final int GL_CONVOLUTION_FILTER_BIAS = 32789;
   public static final int GL_REDUCE = 32790;
   public static final int GL_CONVOLUTION_FORMAT = 32791;
   public static final int GL_CONVOLUTION_WIDTH = 32792;
   public static final int GL_CONVOLUTION_HEIGHT = 32793;
   public static final int GL_MAX_CONVOLUTION_WIDTH = 32794;
   public static final int GL_MAX_CONVOLUTION_HEIGHT = 32795;
   public static final int GL_POST_CONVOLUTION_RED_SCALE = 32796;
   public static final int GL_POST_CONVOLUTION_GREEN_SCALE = 32797;
   public static final int GL_POST_CONVOLUTION_BLUE_SCALE = 32798;
   public static final int GL_POST_CONVOLUTION_ALPHA_SCALE = 32799;
   public static final int GL_POST_CONVOLUTION_RED_BIAS = 32800;
   public static final int GL_POST_CONVOLUTION_GREEN_BIAS = 32801;
   public static final int GL_POST_CONVOLUTION_BLUE_BIAS = 32802;
   public static final int GL_POST_CONVOLUTION_ALPHA_BIAS = 32803;
   public static final int GL_IGNORE_BORDER = 33104;
   public static final int GL_CONSTANT_BORDER = 33105;
   public static final int GL_REPLICATE_BORDER = 33107;
   public static final int GL_CONVOLUTION_BORDER_COLOR = 33108;
   public static final int GL_HISTOGRAM = 32804;
   public static final int GL_PROXY_HISTOGRAM = 32805;
   public static final int GL_HISTOGRAM_WIDTH = 32806;
   public static final int GL_HISTOGRAM_FORMAT = 32807;
   public static final int GL_HISTOGRAM_RED_SIZE = 32808;
   public static final int GL_HISTOGRAM_GREEN_SIZE = 32809;
   public static final int GL_HISTOGRAM_BLUE_SIZE = 32810;
   public static final int GL_HISTOGRAM_ALPHA_SIZE = 32811;
   public static final int GL_HISTOGRAM_LUMINANCE_SIZE = 32812;
   public static final int GL_HISTOGRAM_SINK = 32813;
   public static final int GL_MINMAX = 32814;
   public static final int GL_MINMAX_FORMAT = 32815;
   public static final int GL_MINMAX_SINK = 32816;
   public static final int GL_TABLE_TOO_LARGE = 32817;

   private ARBImaging() {
   }

   public static void glColorTable(int var0, int var1, int var2, int var3, int var4, ByteBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glColorTable;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensureUnpackPBOdisabled(var6);
      BufferChecks.checkBuffer((ByteBuffer)var5, 256);
      nglColorTable(var0, var1, var2, var3, var4, MemoryUtil.getAddress(var5), var7);
   }

   public static void glColorTable(int var0, int var1, int var2, int var3, int var4, DoubleBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glColorTable;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensureUnpackPBOdisabled(var6);
      BufferChecks.checkBuffer((DoubleBuffer)var5, 256);
      nglColorTable(var0, var1, var2, var3, var4, MemoryUtil.getAddress(var5), var7);
   }

   public static void glColorTable(int var0, int var1, int var2, int var3, int var4, FloatBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glColorTable;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensureUnpackPBOdisabled(var6);
      BufferChecks.checkBuffer((FloatBuffer)var5, 256);
      nglColorTable(var0, var1, var2, var3, var4, MemoryUtil.getAddress(var5), var7);
   }

   static native void nglColorTable(int var0, int var1, int var2, int var3, int var4, long var5, long var7);

   public static void glColorTable(int var0, int var1, int var2, int var3, int var4, long var5) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glColorTable;
      BufferChecks.checkFunctionAddress(var8);
      GLChecks.ensureUnpackPBOenabled(var7);
      nglColorTableBO(var0, var1, var2, var3, var4, var5, var8);
   }

   static native void nglColorTableBO(int var0, int var1, int var2, int var3, int var4, long var5, long var7);

   public static void glColorSubTable(int var0, int var1, int var2, int var3, int var4, ByteBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glColorSubTable;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensureUnpackPBOdisabled(var6);
      BufferChecks.checkBuffer((ByteBuffer)var5, 256);
      nglColorSubTable(var0, var1, var2, var3, var4, MemoryUtil.getAddress(var5), var7);
   }

   public static void glColorSubTable(int var0, int var1, int var2, int var3, int var4, DoubleBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glColorSubTable;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensureUnpackPBOdisabled(var6);
      BufferChecks.checkBuffer((DoubleBuffer)var5, 256);
      nglColorSubTable(var0, var1, var2, var3, var4, MemoryUtil.getAddress(var5), var7);
   }

   public static void glColorSubTable(int var0, int var1, int var2, int var3, int var4, FloatBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glColorSubTable;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensureUnpackPBOdisabled(var6);
      BufferChecks.checkBuffer((FloatBuffer)var5, 256);
      nglColorSubTable(var0, var1, var2, var3, var4, MemoryUtil.getAddress(var5), var7);
   }

   static native void nglColorSubTable(int var0, int var1, int var2, int var3, int var4, long var5, long var7);

   public static void glColorSubTable(int var0, int var1, int var2, int var3, int var4, long var5) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glColorSubTable;
      BufferChecks.checkFunctionAddress(var8);
      GLChecks.ensureUnpackPBOenabled(var7);
      nglColorSubTableBO(var0, var1, var2, var3, var4, var5, var8);
   }

   static native void nglColorSubTableBO(int var0, int var1, int var2, int var3, int var4, long var5, long var7);

   public static void glColorTableParameter(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glColorTableParameteriv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 4);
      nglColorTableParameteriv(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglColorTableParameteriv(int var0, int var1, long var2, long var4);

   public static void glColorTableParameter(int var0, int var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glColorTableParameterfv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((FloatBuffer)var2, 4);
      nglColorTableParameterfv(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglColorTableParameterfv(int var0, int var1, long var2, long var4);

   public static void glCopyColorSubTable(int var0, int var1, int var2, int var3, int var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glCopyColorSubTable;
      BufferChecks.checkFunctionAddress(var6);
      nglCopyColorSubTable(var0, var1, var2, var3, var4, var6);
   }

   static native void nglCopyColorSubTable(int var0, int var1, int var2, int var3, int var4, long var5);

   public static void glCopyColorTable(int var0, int var1, int var2, int var3, int var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glCopyColorTable;
      BufferChecks.checkFunctionAddress(var6);
      nglCopyColorTable(var0, var1, var2, var3, var4, var6);
   }

   static native void nglCopyColorTable(int var0, int var1, int var2, int var3, int var4, long var5);

   public static void glGetColorTable(int var0, int var1, int var2, ByteBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetColorTable;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkBuffer((ByteBuffer)var3, 256);
      nglGetColorTable(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   public static void glGetColorTable(int var0, int var1, int var2, DoubleBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetColorTable;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkBuffer((DoubleBuffer)var3, 256);
      nglGetColorTable(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   public static void glGetColorTable(int var0, int var1, int var2, FloatBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetColorTable;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkBuffer((FloatBuffer)var3, 256);
      nglGetColorTable(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglGetColorTable(int var0, int var1, int var2, long var3, long var5);

   public static void glGetColorTableParameter(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetColorTableParameteriv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 4);
      nglGetColorTableParameteriv(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetColorTableParameteriv(int var0, int var1, long var2, long var4);

   public static void glGetColorTableParameter(int var0, int var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetColorTableParameterfv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((FloatBuffer)var2, 4);
      nglGetColorTableParameterfv(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetColorTableParameterfv(int var0, int var1, long var2, long var4);

   public static void glBlendEquation(int var0) {
      GL14.glBlendEquation(var0);
   }

   public static void glBlendColor(float var0, float var1, float var2, float var3) {
      GL14.glBlendColor(var0, var1, var2, var3);
   }

   public static void glHistogram(int var0, int var1, int var2, boolean var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glHistogram;
      BufferChecks.checkFunctionAddress(var5);
      nglHistogram(var0, var1, var2, var3, var5);
   }

   static native void nglHistogram(int var0, int var1, int var2, boolean var3, long var4);

   public static void glResetHistogram(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glResetHistogram;
      BufferChecks.checkFunctionAddress(var2);
      nglResetHistogram(var0, var2);
   }

   static native void nglResetHistogram(int var0, long var1);

   public static void glGetHistogram(int var0, boolean var1, int var2, int var3, ByteBuffer var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glGetHistogram;
      BufferChecks.checkFunctionAddress(var6);
      GLChecks.ensurePackPBOdisabled(var5);
      BufferChecks.checkBuffer((ByteBuffer)var4, 256);
      nglGetHistogram(var0, var1, var2, var3, MemoryUtil.getAddress(var4), var6);
   }

   public static void glGetHistogram(int var0, boolean var1, int var2, int var3, DoubleBuffer var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glGetHistogram;
      BufferChecks.checkFunctionAddress(var6);
      GLChecks.ensurePackPBOdisabled(var5);
      BufferChecks.checkBuffer((DoubleBuffer)var4, 256);
      nglGetHistogram(var0, var1, var2, var3, MemoryUtil.getAddress(var4), var6);
   }

   public static void glGetHistogram(int var0, boolean var1, int var2, int var3, FloatBuffer var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glGetHistogram;
      BufferChecks.checkFunctionAddress(var6);
      GLChecks.ensurePackPBOdisabled(var5);
      BufferChecks.checkBuffer((FloatBuffer)var4, 256);
      nglGetHistogram(var0, var1, var2, var3, MemoryUtil.getAddress(var4), var6);
   }

   public static void glGetHistogram(int var0, boolean var1, int var2, int var3, IntBuffer var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glGetHistogram;
      BufferChecks.checkFunctionAddress(var6);
      GLChecks.ensurePackPBOdisabled(var5);
      BufferChecks.checkBuffer((IntBuffer)var4, 256);
      nglGetHistogram(var0, var1, var2, var3, MemoryUtil.getAddress(var4), var6);
   }

   public static void glGetHistogram(int var0, boolean var1, int var2, int var3, ShortBuffer var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glGetHistogram;
      BufferChecks.checkFunctionAddress(var6);
      GLChecks.ensurePackPBOdisabled(var5);
      BufferChecks.checkBuffer((ShortBuffer)var4, 256);
      nglGetHistogram(var0, var1, var2, var3, MemoryUtil.getAddress(var4), var6);
   }

   static native void nglGetHistogram(int var0, boolean var1, int var2, int var3, long var4, long var6);

   public static void glGetHistogram(int var0, boolean var1, int var2, int var3, long var4) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetHistogram;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOenabled(var6);
      nglGetHistogramBO(var0, var1, var2, var3, var4, var7);
   }

   static native void nglGetHistogramBO(int var0, boolean var1, int var2, int var3, long var4, long var6);

   public static void glGetHistogramParameter(int var0, int var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetHistogramParameterfv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((FloatBuffer)var2, 256);
      nglGetHistogramParameterfv(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetHistogramParameterfv(int var0, int var1, long var2, long var4);

   public static void glGetHistogramParameter(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetHistogramParameteriv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 256);
      nglGetHistogramParameteriv(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetHistogramParameteriv(int var0, int var1, long var2, long var4);

   public static void glMinmax(int var0, int var1, boolean var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glMinmax;
      BufferChecks.checkFunctionAddress(var4);
      nglMinmax(var0, var1, var2, var4);
   }

   static native void nglMinmax(int var0, int var1, boolean var2, long var3);

   public static void glResetMinmax(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glResetMinmax;
      BufferChecks.checkFunctionAddress(var2);
      nglResetMinmax(var0, var2);
   }

   static native void nglResetMinmax(int var0, long var1);

   public static void glGetMinmax(int var0, boolean var1, int var2, int var3, ByteBuffer var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glGetMinmax;
      BufferChecks.checkFunctionAddress(var6);
      GLChecks.ensurePackPBOdisabled(var5);
      BufferChecks.checkBuffer((ByteBuffer)var4, 4);
      nglGetMinmax(var0, var1, var2, var3, MemoryUtil.getAddress(var4), var6);
   }

   public static void glGetMinmax(int var0, boolean var1, int var2, int var3, DoubleBuffer var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glGetMinmax;
      BufferChecks.checkFunctionAddress(var6);
      GLChecks.ensurePackPBOdisabled(var5);
      BufferChecks.checkBuffer((DoubleBuffer)var4, 4);
      nglGetMinmax(var0, var1, var2, var3, MemoryUtil.getAddress(var4), var6);
   }

   public static void glGetMinmax(int var0, boolean var1, int var2, int var3, FloatBuffer var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glGetMinmax;
      BufferChecks.checkFunctionAddress(var6);
      GLChecks.ensurePackPBOdisabled(var5);
      BufferChecks.checkBuffer((FloatBuffer)var4, 4);
      nglGetMinmax(var0, var1, var2, var3, MemoryUtil.getAddress(var4), var6);
   }

   public static void glGetMinmax(int var0, boolean var1, int var2, int var3, IntBuffer var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glGetMinmax;
      BufferChecks.checkFunctionAddress(var6);
      GLChecks.ensurePackPBOdisabled(var5);
      BufferChecks.checkBuffer((IntBuffer)var4, 4);
      nglGetMinmax(var0, var1, var2, var3, MemoryUtil.getAddress(var4), var6);
   }

   public static void glGetMinmax(int var0, boolean var1, int var2, int var3, ShortBuffer var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glGetMinmax;
      BufferChecks.checkFunctionAddress(var6);
      GLChecks.ensurePackPBOdisabled(var5);
      BufferChecks.checkBuffer((ShortBuffer)var4, 4);
      nglGetMinmax(var0, var1, var2, var3, MemoryUtil.getAddress(var4), var6);
   }

   static native void nglGetMinmax(int var0, boolean var1, int var2, int var3, long var4, long var6);

   public static void glGetMinmax(int var0, boolean var1, int var2, int var3, long var4) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetMinmax;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOenabled(var6);
      nglGetMinmaxBO(var0, var1, var2, var3, var4, var7);
   }

   static native void nglGetMinmaxBO(int var0, boolean var1, int var2, int var3, long var4, long var6);

   public static void glGetMinmaxParameter(int var0, int var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetMinmaxParameterfv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((FloatBuffer)var2, 4);
      nglGetMinmaxParameterfv(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetMinmaxParameterfv(int var0, int var1, long var2, long var4);

   public static void glGetMinmaxParameter(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetMinmaxParameteriv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 4);
      nglGetMinmaxParameteriv(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetMinmaxParameteriv(int var0, int var1, long var2, long var4);

   public static void glConvolutionFilter1D(int var0, int var1, int var2, int var3, int var4, ByteBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glConvolutionFilter1D;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensureUnpackPBOdisabled(var6);
      BufferChecks.checkBuffer(var5, GLChecks.calculateImageStorage(var5, var3, var4, var2, 1, 1));
      nglConvolutionFilter1D(var0, var1, var2, var3, var4, MemoryUtil.getAddress(var5), var7);
   }

   public static void glConvolutionFilter1D(int var0, int var1, int var2, int var3, int var4, DoubleBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glConvolutionFilter1D;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensureUnpackPBOdisabled(var6);
      BufferChecks.checkBuffer(var5, GLChecks.calculateImageStorage(var5, var3, var4, var2, 1, 1));
      nglConvolutionFilter1D(var0, var1, var2, var3, var4, MemoryUtil.getAddress(var5), var7);
   }

   public static void glConvolutionFilter1D(int var0, int var1, int var2, int var3, int var4, FloatBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glConvolutionFilter1D;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensureUnpackPBOdisabled(var6);
      BufferChecks.checkBuffer(var5, GLChecks.calculateImageStorage(var5, var3, var4, var2, 1, 1));
      nglConvolutionFilter1D(var0, var1, var2, var3, var4, MemoryUtil.getAddress(var5), var7);
   }

   public static void glConvolutionFilter1D(int var0, int var1, int var2, int var3, int var4, IntBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glConvolutionFilter1D;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensureUnpackPBOdisabled(var6);
      BufferChecks.checkBuffer(var5, GLChecks.calculateImageStorage(var5, var3, var4, var2, 1, 1));
      nglConvolutionFilter1D(var0, var1, var2, var3, var4, MemoryUtil.getAddress(var5), var7);
   }

   public static void glConvolutionFilter1D(int var0, int var1, int var2, int var3, int var4, ShortBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glConvolutionFilter1D;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensureUnpackPBOdisabled(var6);
      BufferChecks.checkBuffer(var5, GLChecks.calculateImageStorage(var5, var3, var4, var2, 1, 1));
      nglConvolutionFilter1D(var0, var1, var2, var3, var4, MemoryUtil.getAddress(var5), var7);
   }

   static native void nglConvolutionFilter1D(int var0, int var1, int var2, int var3, int var4, long var5, long var7);

   public static void glConvolutionFilter1D(int var0, int var1, int var2, int var3, int var4, long var5) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glConvolutionFilter1D;
      BufferChecks.checkFunctionAddress(var8);
      GLChecks.ensureUnpackPBOenabled(var7);
      nglConvolutionFilter1DBO(var0, var1, var2, var3, var4, var5, var8);
   }

   static native void nglConvolutionFilter1DBO(int var0, int var1, int var2, int var3, int var4, long var5, long var7);

   public static void glConvolutionFilter2D(int var0, int var1, int var2, int var3, int var4, int var5, ByteBuffer var6) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glConvolutionFilter2D;
      BufferChecks.checkFunctionAddress(var8);
      GLChecks.ensureUnpackPBOdisabled(var7);
      BufferChecks.checkBuffer(var6, GLChecks.calculateImageStorage(var6, var4, var5, var2, var3, 1));
      nglConvolutionFilter2D(var0, var1, var2, var3, var4, var5, MemoryUtil.getAddress(var6), var8);
   }

   public static void glConvolutionFilter2D(int var0, int var1, int var2, int var3, int var4, int var5, IntBuffer var6) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glConvolutionFilter2D;
      BufferChecks.checkFunctionAddress(var8);
      GLChecks.ensureUnpackPBOdisabled(var7);
      BufferChecks.checkBuffer(var6, GLChecks.calculateImageStorage(var6, var4, var5, var2, var3, 1));
      nglConvolutionFilter2D(var0, var1, var2, var3, var4, var5, MemoryUtil.getAddress(var6), var8);
   }

   public static void glConvolutionFilter2D(int var0, int var1, int var2, int var3, int var4, int var5, ShortBuffer var6) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glConvolutionFilter2D;
      BufferChecks.checkFunctionAddress(var8);
      GLChecks.ensureUnpackPBOdisabled(var7);
      BufferChecks.checkBuffer(var6, GLChecks.calculateImageStorage(var6, var4, var5, var2, var3, 1));
      nglConvolutionFilter2D(var0, var1, var2, var3, var4, var5, MemoryUtil.getAddress(var6), var8);
   }

   static native void nglConvolutionFilter2D(int var0, int var1, int var2, int var3, int var4, int var5, long var6, long var8);

   public static void glConvolutionFilter2D(int var0, int var1, int var2, int var3, int var4, int var5, long var6) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glConvolutionFilter2D;
      BufferChecks.checkFunctionAddress(var9);
      GLChecks.ensureUnpackPBOenabled(var8);
      nglConvolutionFilter2DBO(var0, var1, var2, var3, var4, var5, var6, var9);
   }

   static native void nglConvolutionFilter2DBO(int var0, int var1, int var2, int var3, int var4, int var5, long var6, long var8);

   public static void glConvolutionParameterf(int var0, int var1, float var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glConvolutionParameterf;
      BufferChecks.checkFunctionAddress(var4);
      nglConvolutionParameterf(var0, var1, var2, var4);
   }

   static native void nglConvolutionParameterf(int var0, int var1, float var2, long var3);

   public static void glConvolutionParameter(int var0, int var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glConvolutionParameterfv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((FloatBuffer)var2, 4);
      nglConvolutionParameterfv(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglConvolutionParameterfv(int var0, int var1, long var2, long var4);

   public static void glConvolutionParameteri(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glConvolutionParameteri;
      BufferChecks.checkFunctionAddress(var4);
      nglConvolutionParameteri(var0, var1, var2, var4);
   }

   static native void nglConvolutionParameteri(int var0, int var1, int var2, long var3);

   public static void glConvolutionParameter(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glConvolutionParameteriv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 4);
      nglConvolutionParameteriv(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglConvolutionParameteriv(int var0, int var1, long var2, long var4);

   public static void glCopyConvolutionFilter1D(int var0, int var1, int var2, int var3, int var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glCopyConvolutionFilter1D;
      BufferChecks.checkFunctionAddress(var6);
      nglCopyConvolutionFilter1D(var0, var1, var2, var3, var4, var6);
   }

   static native void nglCopyConvolutionFilter1D(int var0, int var1, int var2, int var3, int var4, long var5);

   public static void glCopyConvolutionFilter2D(int var0, int var1, int var2, int var3, int var4, int var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glCopyConvolutionFilter2D;
      BufferChecks.checkFunctionAddress(var7);
      nglCopyConvolutionFilter2D(var0, var1, var2, var3, var4, var5, var7);
   }

   static native void nglCopyConvolutionFilter2D(int var0, int var1, int var2, int var3, int var4, int var5, long var6);

   public static void glGetConvolutionFilter(int var0, int var1, int var2, ByteBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetConvolutionFilter;
      BufferChecks.checkFunctionAddress(var5);
      GLChecks.ensurePackPBOdisabled(var4);
      BufferChecks.checkDirect(var3);
      nglGetConvolutionFilter(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   public static void glGetConvolutionFilter(int var0, int var1, int var2, DoubleBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetConvolutionFilter;
      BufferChecks.checkFunctionAddress(var5);
      GLChecks.ensurePackPBOdisabled(var4);
      BufferChecks.checkDirect(var3);
      nglGetConvolutionFilter(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   public static void glGetConvolutionFilter(int var0, int var1, int var2, FloatBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetConvolutionFilter;
      BufferChecks.checkFunctionAddress(var5);
      GLChecks.ensurePackPBOdisabled(var4);
      BufferChecks.checkDirect(var3);
      nglGetConvolutionFilter(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   public static void glGetConvolutionFilter(int var0, int var1, int var2, IntBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetConvolutionFilter;
      BufferChecks.checkFunctionAddress(var5);
      GLChecks.ensurePackPBOdisabled(var4);
      BufferChecks.checkDirect(var3);
      nglGetConvolutionFilter(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   public static void glGetConvolutionFilter(int var0, int var1, int var2, ShortBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetConvolutionFilter;
      BufferChecks.checkFunctionAddress(var5);
      GLChecks.ensurePackPBOdisabled(var4);
      BufferChecks.checkDirect(var3);
      nglGetConvolutionFilter(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglGetConvolutionFilter(int var0, int var1, int var2, long var3, long var5);

   public static void glGetConvolutionFilter(int var0, int var1, int var2, long var3) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glGetConvolutionFilter;
      BufferChecks.checkFunctionAddress(var6);
      GLChecks.ensurePackPBOenabled(var5);
      nglGetConvolutionFilterBO(var0, var1, var2, var3, var6);
   }

   static native void nglGetConvolutionFilterBO(int var0, int var1, int var2, long var3, long var5);

   public static void glGetConvolutionParameter(int var0, int var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetConvolutionParameterfv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((FloatBuffer)var2, 4);
      nglGetConvolutionParameterfv(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetConvolutionParameterfv(int var0, int var1, long var2, long var4);

   public static void glGetConvolutionParameter(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetConvolutionParameteriv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 4);
      nglGetConvolutionParameteriv(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetConvolutionParameteriv(int var0, int var1, long var2, long var4);

   public static void glSeparableFilter2D(int var0, int var1, int var2, int var3, int var4, int var5, ByteBuffer var6, ByteBuffer var7) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glSeparableFilter2D;
      BufferChecks.checkFunctionAddress(var9);
      GLChecks.ensureUnpackPBOdisabled(var8);
      BufferChecks.checkDirect(var6);
      BufferChecks.checkDirect(var7);
      nglSeparableFilter2D(var0, var1, var2, var3, var4, var5, MemoryUtil.getAddress(var6), MemoryUtil.getAddress(var7), var9);
   }

   public static void glSeparableFilter2D(int var0, int var1, int var2, int var3, int var4, int var5, ByteBuffer var6, DoubleBuffer var7) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glSeparableFilter2D;
      BufferChecks.checkFunctionAddress(var9);
      GLChecks.ensureUnpackPBOdisabled(var8);
      BufferChecks.checkDirect(var6);
      BufferChecks.checkDirect(var7);
      nglSeparableFilter2D(var0, var1, var2, var3, var4, var5, MemoryUtil.getAddress(var6), MemoryUtil.getAddress(var7), var9);
   }

   public static void glSeparableFilter2D(int var0, int var1, int var2, int var3, int var4, int var5, ByteBuffer var6, FloatBuffer var7) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glSeparableFilter2D;
      BufferChecks.checkFunctionAddress(var9);
      GLChecks.ensureUnpackPBOdisabled(var8);
      BufferChecks.checkDirect(var6);
      BufferChecks.checkDirect(var7);
      nglSeparableFilter2D(var0, var1, var2, var3, var4, var5, MemoryUtil.getAddress(var6), MemoryUtil.getAddress(var7), var9);
   }

   public static void glSeparableFilter2D(int var0, int var1, int var2, int var3, int var4, int var5, ByteBuffer var6, IntBuffer var7) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glSeparableFilter2D;
      BufferChecks.checkFunctionAddress(var9);
      GLChecks.ensureUnpackPBOdisabled(var8);
      BufferChecks.checkDirect(var6);
      BufferChecks.checkDirect(var7);
      nglSeparableFilter2D(var0, var1, var2, var3, var4, var5, MemoryUtil.getAddress(var6), MemoryUtil.getAddress(var7), var9);
   }

   public static void glSeparableFilter2D(int var0, int var1, int var2, int var3, int var4, int var5, ByteBuffer var6, ShortBuffer var7) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glSeparableFilter2D;
      BufferChecks.checkFunctionAddress(var9);
      GLChecks.ensureUnpackPBOdisabled(var8);
      BufferChecks.checkDirect(var6);
      BufferChecks.checkDirect(var7);
      nglSeparableFilter2D(var0, var1, var2, var3, var4, var5, MemoryUtil.getAddress(var6), MemoryUtil.getAddress(var7), var9);
   }

   public static void glSeparableFilter2D(int var0, int var1, int var2, int var3, int var4, int var5, DoubleBuffer var6, ByteBuffer var7) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glSeparableFilter2D;
      BufferChecks.checkFunctionAddress(var9);
      GLChecks.ensureUnpackPBOdisabled(var8);
      BufferChecks.checkDirect(var6);
      BufferChecks.checkDirect(var7);
      nglSeparableFilter2D(var0, var1, var2, var3, var4, var5, MemoryUtil.getAddress(var6), MemoryUtil.getAddress(var7), var9);
   }

   public static void glSeparableFilter2D(int var0, int var1, int var2, int var3, int var4, int var5, DoubleBuffer var6, DoubleBuffer var7) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glSeparableFilter2D;
      BufferChecks.checkFunctionAddress(var9);
      GLChecks.ensureUnpackPBOdisabled(var8);
      BufferChecks.checkDirect(var6);
      BufferChecks.checkDirect(var7);
      nglSeparableFilter2D(var0, var1, var2, var3, var4, var5, MemoryUtil.getAddress(var6), MemoryUtil.getAddress(var7), var9);
   }

   public static void glSeparableFilter2D(int var0, int var1, int var2, int var3, int var4, int var5, DoubleBuffer var6, FloatBuffer var7) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glSeparableFilter2D;
      BufferChecks.checkFunctionAddress(var9);
      GLChecks.ensureUnpackPBOdisabled(var8);
      BufferChecks.checkDirect(var6);
      BufferChecks.checkDirect(var7);
      nglSeparableFilter2D(var0, var1, var2, var3, var4, var5, MemoryUtil.getAddress(var6), MemoryUtil.getAddress(var7), var9);
   }

   public static void glSeparableFilter2D(int var0, int var1, int var2, int var3, int var4, int var5, DoubleBuffer var6, IntBuffer var7) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glSeparableFilter2D;
      BufferChecks.checkFunctionAddress(var9);
      GLChecks.ensureUnpackPBOdisabled(var8);
      BufferChecks.checkDirect(var6);
      BufferChecks.checkDirect(var7);
      nglSeparableFilter2D(var0, var1, var2, var3, var4, var5, MemoryUtil.getAddress(var6), MemoryUtil.getAddress(var7), var9);
   }

   public static void glSeparableFilter2D(int var0, int var1, int var2, int var3, int var4, int var5, DoubleBuffer var6, ShortBuffer var7) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glSeparableFilter2D;
      BufferChecks.checkFunctionAddress(var9);
      GLChecks.ensureUnpackPBOdisabled(var8);
      BufferChecks.checkDirect(var6);
      BufferChecks.checkDirect(var7);
      nglSeparableFilter2D(var0, var1, var2, var3, var4, var5, MemoryUtil.getAddress(var6), MemoryUtil.getAddress(var7), var9);
   }

   public static void glSeparableFilter2D(int var0, int var1, int var2, int var3, int var4, int var5, FloatBuffer var6, ByteBuffer var7) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glSeparableFilter2D;
      BufferChecks.checkFunctionAddress(var9);
      GLChecks.ensureUnpackPBOdisabled(var8);
      BufferChecks.checkDirect(var6);
      BufferChecks.checkDirect(var7);
      nglSeparableFilter2D(var0, var1, var2, var3, var4, var5, MemoryUtil.getAddress(var6), MemoryUtil.getAddress(var7), var9);
   }

   public static void glSeparableFilter2D(int var0, int var1, int var2, int var3, int var4, int var5, FloatBuffer var6, DoubleBuffer var7) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glSeparableFilter2D;
      BufferChecks.checkFunctionAddress(var9);
      GLChecks.ensureUnpackPBOdisabled(var8);
      BufferChecks.checkDirect(var6);
      BufferChecks.checkDirect(var7);
      nglSeparableFilter2D(var0, var1, var2, var3, var4, var5, MemoryUtil.getAddress(var6), MemoryUtil.getAddress(var7), var9);
   }

   public static void glSeparableFilter2D(int var0, int var1, int var2, int var3, int var4, int var5, FloatBuffer var6, FloatBuffer var7) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glSeparableFilter2D;
      BufferChecks.checkFunctionAddress(var9);
      GLChecks.ensureUnpackPBOdisabled(var8);
      BufferChecks.checkDirect(var6);
      BufferChecks.checkDirect(var7);
      nglSeparableFilter2D(var0, var1, var2, var3, var4, var5, MemoryUtil.getAddress(var6), MemoryUtil.getAddress(var7), var9);
   }

   public static void glSeparableFilter2D(int var0, int var1, int var2, int var3, int var4, int var5, FloatBuffer var6, IntBuffer var7) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glSeparableFilter2D;
      BufferChecks.checkFunctionAddress(var9);
      GLChecks.ensureUnpackPBOdisabled(var8);
      BufferChecks.checkDirect(var6);
      BufferChecks.checkDirect(var7);
      nglSeparableFilter2D(var0, var1, var2, var3, var4, var5, MemoryUtil.getAddress(var6), MemoryUtil.getAddress(var7), var9);
   }

   public static void glSeparableFilter2D(int var0, int var1, int var2, int var3, int var4, int var5, FloatBuffer var6, ShortBuffer var7) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glSeparableFilter2D;
      BufferChecks.checkFunctionAddress(var9);
      GLChecks.ensureUnpackPBOdisabled(var8);
      BufferChecks.checkDirect(var6);
      BufferChecks.checkDirect(var7);
      nglSeparableFilter2D(var0, var1, var2, var3, var4, var5, MemoryUtil.getAddress(var6), MemoryUtil.getAddress(var7), var9);
   }

   public static void glSeparableFilter2D(int var0, int var1, int var2, int var3, int var4, int var5, IntBuffer var6, ByteBuffer var7) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glSeparableFilter2D;
      BufferChecks.checkFunctionAddress(var9);
      GLChecks.ensureUnpackPBOdisabled(var8);
      BufferChecks.checkDirect(var6);
      BufferChecks.checkDirect(var7);
      nglSeparableFilter2D(var0, var1, var2, var3, var4, var5, MemoryUtil.getAddress(var6), MemoryUtil.getAddress(var7), var9);
   }

   public static void glSeparableFilter2D(int var0, int var1, int var2, int var3, int var4, int var5, IntBuffer var6, DoubleBuffer var7) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glSeparableFilter2D;
      BufferChecks.checkFunctionAddress(var9);
      GLChecks.ensureUnpackPBOdisabled(var8);
      BufferChecks.checkDirect(var6);
      BufferChecks.checkDirect(var7);
      nglSeparableFilter2D(var0, var1, var2, var3, var4, var5, MemoryUtil.getAddress(var6), MemoryUtil.getAddress(var7), var9);
   }

   public static void glSeparableFilter2D(int var0, int var1, int var2, int var3, int var4, int var5, IntBuffer var6, FloatBuffer var7) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glSeparableFilter2D;
      BufferChecks.checkFunctionAddress(var9);
      GLChecks.ensureUnpackPBOdisabled(var8);
      BufferChecks.checkDirect(var6);
      BufferChecks.checkDirect(var7);
      nglSeparableFilter2D(var0, var1, var2, var3, var4, var5, MemoryUtil.getAddress(var6), MemoryUtil.getAddress(var7), var9);
   }

   public static void glSeparableFilter2D(int var0, int var1, int var2, int var3, int var4, int var5, IntBuffer var6, IntBuffer var7) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glSeparableFilter2D;
      BufferChecks.checkFunctionAddress(var9);
      GLChecks.ensureUnpackPBOdisabled(var8);
      BufferChecks.checkDirect(var6);
      BufferChecks.checkDirect(var7);
      nglSeparableFilter2D(var0, var1, var2, var3, var4, var5, MemoryUtil.getAddress(var6), MemoryUtil.getAddress(var7), var9);
   }

   public static void glSeparableFilter2D(int var0, int var1, int var2, int var3, int var4, int var5, IntBuffer var6, ShortBuffer var7) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glSeparableFilter2D;
      BufferChecks.checkFunctionAddress(var9);
      GLChecks.ensureUnpackPBOdisabled(var8);
      BufferChecks.checkDirect(var6);
      BufferChecks.checkDirect(var7);
      nglSeparableFilter2D(var0, var1, var2, var3, var4, var5, MemoryUtil.getAddress(var6), MemoryUtil.getAddress(var7), var9);
   }

   public static void glSeparableFilter2D(int var0, int var1, int var2, int var3, int var4, int var5, ShortBuffer var6, ByteBuffer var7) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glSeparableFilter2D;
      BufferChecks.checkFunctionAddress(var9);
      GLChecks.ensureUnpackPBOdisabled(var8);
      BufferChecks.checkDirect(var6);
      BufferChecks.checkDirect(var7);
      nglSeparableFilter2D(var0, var1, var2, var3, var4, var5, MemoryUtil.getAddress(var6), MemoryUtil.getAddress(var7), var9);
   }

   public static void glSeparableFilter2D(int var0, int var1, int var2, int var3, int var4, int var5, ShortBuffer var6, DoubleBuffer var7) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glSeparableFilter2D;
      BufferChecks.checkFunctionAddress(var9);
      GLChecks.ensureUnpackPBOdisabled(var8);
      BufferChecks.checkDirect(var6);
      BufferChecks.checkDirect(var7);
      nglSeparableFilter2D(var0, var1, var2, var3, var4, var5, MemoryUtil.getAddress(var6), MemoryUtil.getAddress(var7), var9);
   }

   public static void glSeparableFilter2D(int var0, int var1, int var2, int var3, int var4, int var5, ShortBuffer var6, FloatBuffer var7) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glSeparableFilter2D;
      BufferChecks.checkFunctionAddress(var9);
      GLChecks.ensureUnpackPBOdisabled(var8);
      BufferChecks.checkDirect(var6);
      BufferChecks.checkDirect(var7);
      nglSeparableFilter2D(var0, var1, var2, var3, var4, var5, MemoryUtil.getAddress(var6), MemoryUtil.getAddress(var7), var9);
   }

   public static void glSeparableFilter2D(int var0, int var1, int var2, int var3, int var4, int var5, ShortBuffer var6, IntBuffer var7) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glSeparableFilter2D;
      BufferChecks.checkFunctionAddress(var9);
      GLChecks.ensureUnpackPBOdisabled(var8);
      BufferChecks.checkDirect(var6);
      BufferChecks.checkDirect(var7);
      nglSeparableFilter2D(var0, var1, var2, var3, var4, var5, MemoryUtil.getAddress(var6), MemoryUtil.getAddress(var7), var9);
   }

   public static void glSeparableFilter2D(int var0, int var1, int var2, int var3, int var4, int var5, ShortBuffer var6, ShortBuffer var7) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glSeparableFilter2D;
      BufferChecks.checkFunctionAddress(var9);
      GLChecks.ensureUnpackPBOdisabled(var8);
      BufferChecks.checkDirect(var6);
      BufferChecks.checkDirect(var7);
      nglSeparableFilter2D(var0, var1, var2, var3, var4, var5, MemoryUtil.getAddress(var6), MemoryUtil.getAddress(var7), var9);
   }

   static native void nglSeparableFilter2D(int var0, int var1, int var2, int var3, int var4, int var5, long var6, long var8, long var10);

   public static void glSeparableFilter2D(int var0, int var1, int var2, int var3, int var4, int var5, long var6, long var8) {
      ContextCapabilities var10 = GLContext.getCapabilities();
      long var11 = var10.glSeparableFilter2D;
      BufferChecks.checkFunctionAddress(var11);
      GLChecks.ensureUnpackPBOenabled(var10);
      nglSeparableFilter2DBO(var0, var1, var2, var3, var4, var5, var6, var8, var11);
   }

   static native void nglSeparableFilter2DBO(int var0, int var1, int var2, int var3, int var4, int var5, long var6, long var8, long var10);

   public static void glGetSeparableFilter(int var0, int var1, int var2, ByteBuffer var3, ByteBuffer var4, ByteBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetSeparableFilter;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetSeparableFilter(var0, var1, var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetSeparableFilter(int var0, int var1, int var2, ByteBuffer var3, ByteBuffer var4, DoubleBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetSeparableFilter;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetSeparableFilter(var0, var1, var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetSeparableFilter(int var0, int var1, int var2, ByteBuffer var3, ByteBuffer var4, IntBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetSeparableFilter;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetSeparableFilter(var0, var1, var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetSeparableFilter(int var0, int var1, int var2, ByteBuffer var3, ByteBuffer var4, ShortBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetSeparableFilter;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetSeparableFilter(var0, var1, var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetSeparableFilter(int var0, int var1, int var2, ByteBuffer var3, DoubleBuffer var4, ByteBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetSeparableFilter;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetSeparableFilter(var0, var1, var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetSeparableFilter(int var0, int var1, int var2, ByteBuffer var3, DoubleBuffer var4, DoubleBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetSeparableFilter;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetSeparableFilter(var0, var1, var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetSeparableFilter(int var0, int var1, int var2, ByteBuffer var3, DoubleBuffer var4, IntBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetSeparableFilter;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetSeparableFilter(var0, var1, var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetSeparableFilter(int var0, int var1, int var2, ByteBuffer var3, DoubleBuffer var4, ShortBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetSeparableFilter;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetSeparableFilter(var0, var1, var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetSeparableFilter(int var0, int var1, int var2, ByteBuffer var3, IntBuffer var4, ByteBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetSeparableFilter;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetSeparableFilter(var0, var1, var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetSeparableFilter(int var0, int var1, int var2, ByteBuffer var3, IntBuffer var4, DoubleBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetSeparableFilter;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetSeparableFilter(var0, var1, var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetSeparableFilter(int var0, int var1, int var2, ByteBuffer var3, IntBuffer var4, IntBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetSeparableFilter;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetSeparableFilter(var0, var1, var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetSeparableFilter(int var0, int var1, int var2, ByteBuffer var3, IntBuffer var4, ShortBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetSeparableFilter;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetSeparableFilter(var0, var1, var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetSeparableFilter(int var0, int var1, int var2, ByteBuffer var3, ShortBuffer var4, ByteBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetSeparableFilter;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetSeparableFilter(var0, var1, var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetSeparableFilter(int var0, int var1, int var2, ByteBuffer var3, ShortBuffer var4, DoubleBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetSeparableFilter;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetSeparableFilter(var0, var1, var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetSeparableFilter(int var0, int var1, int var2, ByteBuffer var3, ShortBuffer var4, IntBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetSeparableFilter;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetSeparableFilter(var0, var1, var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetSeparableFilter(int var0, int var1, int var2, ByteBuffer var3, ShortBuffer var4, ShortBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetSeparableFilter;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetSeparableFilter(var0, var1, var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetSeparableFilter(int var0, int var1, int var2, DoubleBuffer var3, ByteBuffer var4, ByteBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetSeparableFilter;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetSeparableFilter(var0, var1, var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetSeparableFilter(int var0, int var1, int var2, DoubleBuffer var3, ByteBuffer var4, DoubleBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetSeparableFilter;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetSeparableFilter(var0, var1, var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetSeparableFilter(int var0, int var1, int var2, DoubleBuffer var3, ByteBuffer var4, IntBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetSeparableFilter;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetSeparableFilter(var0, var1, var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetSeparableFilter(int var0, int var1, int var2, DoubleBuffer var3, ByteBuffer var4, ShortBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetSeparableFilter;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetSeparableFilter(var0, var1, var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetSeparableFilter(int var0, int var1, int var2, DoubleBuffer var3, DoubleBuffer var4, ByteBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetSeparableFilter;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetSeparableFilter(var0, var1, var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetSeparableFilter(int var0, int var1, int var2, DoubleBuffer var3, DoubleBuffer var4, DoubleBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetSeparableFilter;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetSeparableFilter(var0, var1, var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetSeparableFilter(int var0, int var1, int var2, DoubleBuffer var3, DoubleBuffer var4, IntBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetSeparableFilter;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetSeparableFilter(var0, var1, var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetSeparableFilter(int var0, int var1, int var2, DoubleBuffer var3, DoubleBuffer var4, ShortBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetSeparableFilter;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetSeparableFilter(var0, var1, var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetSeparableFilter(int var0, int var1, int var2, DoubleBuffer var3, IntBuffer var4, ByteBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetSeparableFilter;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetSeparableFilter(var0, var1, var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetSeparableFilter(int var0, int var1, int var2, DoubleBuffer var3, IntBuffer var4, DoubleBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetSeparableFilter;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetSeparableFilter(var0, var1, var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetSeparableFilter(int var0, int var1, int var2, DoubleBuffer var3, IntBuffer var4, IntBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetSeparableFilter;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetSeparableFilter(var0, var1, var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetSeparableFilter(int var0, int var1, int var2, DoubleBuffer var3, IntBuffer var4, ShortBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetSeparableFilter;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetSeparableFilter(var0, var1, var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetSeparableFilter(int var0, int var1, int var2, DoubleBuffer var3, ShortBuffer var4, ByteBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetSeparableFilter;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetSeparableFilter(var0, var1, var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetSeparableFilter(int var0, int var1, int var2, DoubleBuffer var3, ShortBuffer var4, DoubleBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetSeparableFilter;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetSeparableFilter(var0, var1, var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetSeparableFilter(int var0, int var1, int var2, DoubleBuffer var3, ShortBuffer var4, IntBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetSeparableFilter;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetSeparableFilter(var0, var1, var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetSeparableFilter(int var0, int var1, int var2, DoubleBuffer var3, ShortBuffer var4, ShortBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetSeparableFilter;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetSeparableFilter(var0, var1, var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetSeparableFilter(int var0, int var1, int var2, FloatBuffer var3, ByteBuffer var4, ByteBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetSeparableFilter;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetSeparableFilter(var0, var1, var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetSeparableFilter(int var0, int var1, int var2, FloatBuffer var3, ByteBuffer var4, DoubleBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetSeparableFilter;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetSeparableFilter(var0, var1, var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetSeparableFilter(int var0, int var1, int var2, FloatBuffer var3, ByteBuffer var4, IntBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetSeparableFilter;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetSeparableFilter(var0, var1, var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetSeparableFilter(int var0, int var1, int var2, FloatBuffer var3, ByteBuffer var4, ShortBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetSeparableFilter;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetSeparableFilter(var0, var1, var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetSeparableFilter(int var0, int var1, int var2, FloatBuffer var3, DoubleBuffer var4, ByteBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetSeparableFilter;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetSeparableFilter(var0, var1, var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetSeparableFilter(int var0, int var1, int var2, FloatBuffer var3, DoubleBuffer var4, DoubleBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetSeparableFilter;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetSeparableFilter(var0, var1, var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetSeparableFilter(int var0, int var1, int var2, FloatBuffer var3, DoubleBuffer var4, IntBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetSeparableFilter;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetSeparableFilter(var0, var1, var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetSeparableFilter(int var0, int var1, int var2, FloatBuffer var3, DoubleBuffer var4, ShortBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetSeparableFilter;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetSeparableFilter(var0, var1, var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetSeparableFilter(int var0, int var1, int var2, FloatBuffer var3, IntBuffer var4, ByteBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetSeparableFilter;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetSeparableFilter(var0, var1, var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetSeparableFilter(int var0, int var1, int var2, FloatBuffer var3, IntBuffer var4, DoubleBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetSeparableFilter;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetSeparableFilter(var0, var1, var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetSeparableFilter(int var0, int var1, int var2, FloatBuffer var3, IntBuffer var4, IntBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetSeparableFilter;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetSeparableFilter(var0, var1, var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetSeparableFilter(int var0, int var1, int var2, FloatBuffer var3, IntBuffer var4, ShortBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetSeparableFilter;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetSeparableFilter(var0, var1, var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetSeparableFilter(int var0, int var1, int var2, FloatBuffer var3, ShortBuffer var4, ByteBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetSeparableFilter;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetSeparableFilter(var0, var1, var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetSeparableFilter(int var0, int var1, int var2, FloatBuffer var3, ShortBuffer var4, DoubleBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetSeparableFilter;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetSeparableFilter(var0, var1, var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetSeparableFilter(int var0, int var1, int var2, FloatBuffer var3, ShortBuffer var4, IntBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetSeparableFilter;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetSeparableFilter(var0, var1, var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetSeparableFilter(int var0, int var1, int var2, FloatBuffer var3, ShortBuffer var4, ShortBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetSeparableFilter;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetSeparableFilter(var0, var1, var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetSeparableFilter(int var0, int var1, int var2, IntBuffer var3, ByteBuffer var4, ByteBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetSeparableFilter;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetSeparableFilter(var0, var1, var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetSeparableFilter(int var0, int var1, int var2, IntBuffer var3, ByteBuffer var4, DoubleBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetSeparableFilter;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetSeparableFilter(var0, var1, var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetSeparableFilter(int var0, int var1, int var2, IntBuffer var3, ByteBuffer var4, IntBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetSeparableFilter;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetSeparableFilter(var0, var1, var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetSeparableFilter(int var0, int var1, int var2, IntBuffer var3, ByteBuffer var4, ShortBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetSeparableFilter;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetSeparableFilter(var0, var1, var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetSeparableFilter(int var0, int var1, int var2, IntBuffer var3, DoubleBuffer var4, ByteBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetSeparableFilter;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetSeparableFilter(var0, var1, var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetSeparableFilter(int var0, int var1, int var2, IntBuffer var3, DoubleBuffer var4, DoubleBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetSeparableFilter;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetSeparableFilter(var0, var1, var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetSeparableFilter(int var0, int var1, int var2, IntBuffer var3, DoubleBuffer var4, IntBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetSeparableFilter;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetSeparableFilter(var0, var1, var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetSeparableFilter(int var0, int var1, int var2, IntBuffer var3, DoubleBuffer var4, ShortBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetSeparableFilter;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetSeparableFilter(var0, var1, var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetSeparableFilter(int var0, int var1, int var2, IntBuffer var3, IntBuffer var4, ByteBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetSeparableFilter;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetSeparableFilter(var0, var1, var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetSeparableFilter(int var0, int var1, int var2, IntBuffer var3, IntBuffer var4, DoubleBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetSeparableFilter;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetSeparableFilter(var0, var1, var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetSeparableFilter(int var0, int var1, int var2, IntBuffer var3, IntBuffer var4, IntBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetSeparableFilter;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetSeparableFilter(var0, var1, var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetSeparableFilter(int var0, int var1, int var2, IntBuffer var3, IntBuffer var4, ShortBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetSeparableFilter;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetSeparableFilter(var0, var1, var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetSeparableFilter(int var0, int var1, int var2, IntBuffer var3, ShortBuffer var4, ByteBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetSeparableFilter;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetSeparableFilter(var0, var1, var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetSeparableFilter(int var0, int var1, int var2, IntBuffer var3, ShortBuffer var4, DoubleBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetSeparableFilter;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetSeparableFilter(var0, var1, var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetSeparableFilter(int var0, int var1, int var2, IntBuffer var3, ShortBuffer var4, IntBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetSeparableFilter;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetSeparableFilter(var0, var1, var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetSeparableFilter(int var0, int var1, int var2, IntBuffer var3, ShortBuffer var4, ShortBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetSeparableFilter;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetSeparableFilter(var0, var1, var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetSeparableFilter(int var0, int var1, int var2, ShortBuffer var3, ByteBuffer var4, ByteBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetSeparableFilter;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetSeparableFilter(var0, var1, var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetSeparableFilter(int var0, int var1, int var2, ShortBuffer var3, ByteBuffer var4, DoubleBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetSeparableFilter;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetSeparableFilter(var0, var1, var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetSeparableFilter(int var0, int var1, int var2, ShortBuffer var3, ByteBuffer var4, IntBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetSeparableFilter;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetSeparableFilter(var0, var1, var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetSeparableFilter(int var0, int var1, int var2, ShortBuffer var3, ByteBuffer var4, ShortBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetSeparableFilter;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetSeparableFilter(var0, var1, var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetSeparableFilter(int var0, int var1, int var2, ShortBuffer var3, DoubleBuffer var4, ByteBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetSeparableFilter;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetSeparableFilter(var0, var1, var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetSeparableFilter(int var0, int var1, int var2, ShortBuffer var3, DoubleBuffer var4, DoubleBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetSeparableFilter;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetSeparableFilter(var0, var1, var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetSeparableFilter(int var0, int var1, int var2, ShortBuffer var3, DoubleBuffer var4, IntBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetSeparableFilter;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetSeparableFilter(var0, var1, var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetSeparableFilter(int var0, int var1, int var2, ShortBuffer var3, DoubleBuffer var4, ShortBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetSeparableFilter;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetSeparableFilter(var0, var1, var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetSeparableFilter(int var0, int var1, int var2, ShortBuffer var3, IntBuffer var4, ByteBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetSeparableFilter;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetSeparableFilter(var0, var1, var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetSeparableFilter(int var0, int var1, int var2, ShortBuffer var3, IntBuffer var4, DoubleBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetSeparableFilter;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetSeparableFilter(var0, var1, var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetSeparableFilter(int var0, int var1, int var2, ShortBuffer var3, IntBuffer var4, IntBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetSeparableFilter;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetSeparableFilter(var0, var1, var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetSeparableFilter(int var0, int var1, int var2, ShortBuffer var3, IntBuffer var4, ShortBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetSeparableFilter;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetSeparableFilter(var0, var1, var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetSeparableFilter(int var0, int var1, int var2, ShortBuffer var3, ShortBuffer var4, ByteBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetSeparableFilter;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetSeparableFilter(var0, var1, var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetSeparableFilter(int var0, int var1, int var2, ShortBuffer var3, ShortBuffer var4, DoubleBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetSeparableFilter;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetSeparableFilter(var0, var1, var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetSeparableFilter(int var0, int var1, int var2, ShortBuffer var3, ShortBuffer var4, IntBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetSeparableFilter;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetSeparableFilter(var0, var1, var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetSeparableFilter(int var0, int var1, int var2, ShortBuffer var3, ShortBuffer var4, ShortBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetSeparableFilter;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetSeparableFilter(var0, var1, var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   static native void nglGetSeparableFilter(int var0, int var1, int var2, long var3, long var5, long var7, long var9);

   public static void glGetSeparableFilter(int var0, int var1, int var2, long var3, long var5, long var7) {
      ContextCapabilities var9 = GLContext.getCapabilities();
      long var10 = var9.glGetSeparableFilter;
      BufferChecks.checkFunctionAddress(var10);
      GLChecks.ensurePackPBOenabled(var9);
      nglGetSeparableFilterBO(var0, var1, var2, var3, var5, var7, var10);
   }

   static native void nglGetSeparableFilterBO(int var0, int var1, int var2, long var3, long var5, long var7, long var9);
}
