package me.offeex.bloomware.api.graphics

import java.nio.ByteBuffer

interface GLApi {
    data class BlendEquationSeparate(val rgb: BlendEquation, val alpha: BlendEquation)

    data class BlendFunctionSeparate(val srcRgb: BlendFunction, val dstRgb: BlendFunction, val srcAlpha: BlendFunction, val dstAlpha: BlendFunction)

    data class StencilFunctionSeparate(val face: Face, val func: StencilFunction, val ref: Int, val mask: Int)

    data class StencilOpSeparate(val face: Face, val sfail: StencilOp, val dpfail: StencilOp, val dppass: StencilOp)
    data class StateSnapshot(
        val blend: Boolean,
        val blendEquationRgb: Int,
        val blendEquationAlpha: Int,
        val blendFuncSrcRgb: Int,
        val blendFuncDstRgb: Int,
        val blendFuncSrcAlpha: Int,
        val blendFuncDstAlpha: Int,
        val depthTest: Boolean,
        val depthMask: Boolean,
        val depthFunc: Int,
        val stencilTest: Boolean,
        val stencilMask: Int,
        val stencilFuncFrontFunc: Int,
        val stencilFuncFrontRef: Int,
        val stencilFuncFrontMask: Int,
        val stencilFuncBackFunc: Int,
        val stencilFuncBackRef: Int,
        val stencilFuncBackMask: Int,
        val stencilOpFrontSFail: Int,
        val stencilOpFrontDPFail: Int,
        val stencilOpFrontDPPass: Int,
        val stencilOpBackSFail: Int,
        val stencilOpBackDPFail: Int,
        val stencilOpBackDPPass: Int,
        val boundVAO: Int,
        val boundFBO: Int,
    )

    enum class Capability {
        BLEND,
        CULL_FACE,
        DEPTH_TEST,
        DITHER,
        POLYGON_OFFSET_FILL,
        SAMPLE_ALPHA_TO_COVERAGE,
        SAMPLE_COVERAGE,
        SCISSOR_TEST,
        STENCIL_TEST
    }

    enum class Face {
        FRONT,
        BACK,
        FRONT_AND_BACK
    }

    enum class BlendEquation {
        /**
         * The source and destination colors are added to each other. O = sS + dD. The The s and d are blending parameters that are multiplied into each of S and D before the addition.
         */
        FUNC_ADD,

        /**
         * Subtracts the destination from the source. O = sS - dD. The source and dest are multiplied by blending parameters.
         */
        FUNC_SUBTRACT,

        /**
         * Subtracts the source from the destination. O = dD - sS. The source and dest are multiplied by blending parameters.
         */
        FUNC_REVERSE_SUBTRACT,

        /**
         * The output color is the component-wise minimum value of the source and dest colors. So performing GL_MIN in the RGB equation means that Or = min(Sr, Dr), Og = min(Sg, Dg), and so forth. The parameters s and d are ignored for this equation.
         */
        MIN,

        /**
         * The output color is the component-wise maximum value of the source and dest colors. The parameters s and d are ignored for this equation.
         */
        MAX
    }

    enum class BlendFunction {
        ZERO,
        ONE,
        SRC_COLOR,
        ONE_MINUS_SRC_COLOR,
        DST_COLOR,
        ONE_MINUS_DST_COLOR,
        SRC_ALPHA,
        ONE_MINUS_SRC_ALPHA,
        DST_ALPHA,
        ONE_MINUS_DST_ALPHA,
        CONSTANT_COLOR,
        ONE_MINUS_CONSTANT_COLOR,
        CONSTANT_ALPHA,
        ONE_MINUS_CONSTANT_ALPHA,
        SRC_ALPHA_SATURATE,
        SRC1_COLOR,
        ONE_MINUS_SRC1_COLOR,
        SRC1_ALPHA,
        ONE_MINUS_SRC1_ALPHA
    }

    enum class DepthFunction {
        /**
         * The depth test always passes.
         */
        ALWAYS,

        /**
         * The depth test never passes.
         */
        NEVER,

        /**
         * Passes if the fragment's depth value is less than the stored depth value.
         */
        LESS,

        /**
         * Passes if the fragment's depth value is equal to the stored depth value.
         */
        EQUAL,

        /**
         * Passes if the fragment's depth value is less than or equal to the stored depth value.
         */
        LEQUAL,

        /**
         * Passes if the fragment's depth value is greater than the stored depth value.
         */
        GREATER,

        /**
         * Passes if the fragment's depth value is not equal to the stored depth value.
         */
        NOTEQUAL,

        /**
         * Passes if the fragment's depth value is greater than or equal to the stored depth value.
         */
        GEQUAL
    }

    enum class StencilFunction {
        /**
         * The test never passes.
         */
        NEVER,

        /**
         * The test passes if the value of the stencil buffer at the pixel location is zero.
         */
        LESS,

        /**
         * The test passes if the value of the stencil buffer at the pixel location is nonzero.
         */
        LEQUAL,

        /**
         * The test passes if the value of the stencil buffer at the pixel location is equal to the reference value.
         */
        EQUAL,

        /**
         * The test passes if the value of the stencil buffer at the pixel location is not equal to the reference value.
         */
        NOTEQUAL,

        /**
         * The test passes if the value of the stencil buffer at the pixel location is greater than the reference value.
         */
        GREATER,

        /**
         * The test passes if the value of the stencil buffer at the pixel location is greater than or equal to the reference value.
         */
        GEQUAL,

        /**
         * The test always passes.
         */
        ALWAYS
    }

    enum class StencilOp {
        /**
         * Keeps the current value.
         */
        KEEP,

        /**
         * Sets the stencil buffer value to 0.
         */
        ZERO,

        /**
         * Sets the stencil buffer value to ref, as specified by glStencilFunc.
         */
        REPLACE,

        /**
         * Increments the current stencil buffer value. Clamps to the maximum representable unsigned value.
         */
        INCR,

        /**
         * Increments the current stencil buffer value. Wraps stencil buffer value to zero when incrementing the maximum representable unsigned value.
         */
        INCR_WRAP,

        /**
         * Decrements the current stencil buffer value. Clamps to 0.
         */
        DECR,

        /**
         * Decrements the current stencil buffer value. Wraps stencil buffer value to the maximum representable unsigned value when decrementing a stencil buffer value of zero.
         */
        DECR_WRAP,

        /**
         * Bitwise inverts the current stencil buffer value.
         */
        INVERT
    }

    enum class PrimitiveType {
        BYTE,
        UNSIGNED_BYTE,
        SHORT,
        UNSIGNED_SHORT,
        INT,
        UNSIGNED_INT,
        FIXED,
        HALF_FLOAT,
        FLOAT,
        DOUBLE
    }

    /**
     * Draw mode determines how the vertices are interpreted.
     */
    enum class DrawMode {
        /**
         * Treats each vertex as a single point. Vertex n defines point n. N points are drawn.
         */
        POINTS,

        /**
         * Treats each pair of vertices as an independent line segment. Vertices 2n - 1 and 2n define line n. N/2 lines are drawn.
         */
        LINES,

        /**
         * Draws a connected group of line segments from the first vertex to the last. Vertices n and n+1 define line n. N - 1 lines are drawn.
         */
        LINE_STRIP,

        /**
         * Draws a connected group of line segments from the first vertex to the last, then back to the first. Vertices n and n + 1 define line n. The last line, however, is defined by vertices N and 1. N lines are drawn.
         */
        LINE_LOOP,

        /**
         * Treats each triplet of vertices as an independent triangle. Vertices 3n - 2, 3n - 1, and 3n define triangle n. N/3 triangles are drawn.
         */
        TRIANGLES,

        /**
         * Draws a connected group of triangles. One triangle is defined for each vertex presented after the first two vertices. For odd n, vertices n, n + 1, and n + 2 define triangle n. For even n, vertices n + 1, n, and n + 2 define triangle n. N - 2 triangles are drawn.
         */
        TRIANGLE_STRIP,

        /**
         * Draws a connected group of triangles. one triangle is defined for each vertex presented after the first two vertices. Vertices 1, n + 1, n + 2 define triangle n. N - 2 triangles are drawn.
         */
        TRIANGLE_FAN,

        /**
         * Treats each group of four vertices as an independent quadrilateral. Vertices 4n - 3, 4n - 2, 4n - 1, and 4n define quadrilateral n. N/4 quadrilaterals are drawn.
         */
        QUADS,

        /**
         * Draws a connected group of quadrilaterals. One quadrilateral is defined for each pair of vertices presented after the first pair. Vertices 2n - 1, 2n, 2n + 2, and 2n + 1 define quadrilateral n. N/2 - 1 quadrilaterals are drawn. Note that the order in which vertices are used to construct a quadrilateral from strip data is different from that used with independent data.
         */
        QUAD_STRIP,

        /**
         * Draws a single, convex polygon. Vertices 1 through N define this polygon.
         */
        POLYGON
    }

    /**
     * Specifies the target texture.
     */
    enum class TextureTarget {
        TEXTURE_1D,
        TEXTURE_2D,
        TEXTURE_3D,
        TEXTURE_1D_ARRAY,
        TEXTURE_2D_ARRAY,
        TEXTURE_RECTANGLE,
        TEXTURE_CUBE_MAP,
        TEXTURE_BUFFER,
        TEXTURE_2D_MULTISAMPLE,
        TEXTURE_2D_MULTISAMPLE_ARRAY
    }

    /**
     * Specifies the texture filtering method.
     */
    enum class TextureFilter {
        NEAREST,
        LINEAR,
        NEAREST_MIPMAP_NEAREST,
        LINEAR_MIPMAP_NEAREST,
        NEAREST_MIPMAP_LINEAR,
        LINEAR_MIPMAP_LINEAR
    }

    /**
     * Specifies the format of the pixel data.
     */
    enum class TextureFormat {
        RED,
        RG,
        RGB,
        BGR,
        RGBA,
        BGRA,
        RED_INTEGER,
        RG_INTEGER,
        RGB_INTEGER,
        BGR_INTEGER,
        RGBA_INTEGER,
        BGRA_INTEGER,
        STENCIL_INDEX,
        DEPTH_COMPONENT,
        DEPTH_STENCIL
    }

    enum class TextureInternalFormat {
        R8,
        R8_SNORM,
        R16,
        R16_SNORM,
        RG8,
        RG8_SNORM,
        RG16,
        RG16_SNORM,
        R3_G3_B2,
        RGB4,
        RGB5,
        RGB8,
        RGB8_SNORM,
        RGB10,
        RGB12,
        RGB16_SNORM,
        RGBA2,
        RGBA4,
        RGB5_A1,
        RGBA8,
        RGBA8_SNORM,
        RGB10_A2,
        RGB10_A2UI,
        RGBA12,
        RGBA16,
        SRGB8,
        SRGB8_ALPHA8,
        R16F,
        RG16F,
        RGB16F,
        RGBA16F,
        R32F,
        RG32F,
        RGB32F,
        RGBA32F,
        R11F_G11F_B10F,
        RGB9_E5,
        R8I,
        R8UI,
        R16I,
        R16UI,
        R32I,
        R32UI,
        RG8I,
        RG8UI,
        RG16I,
        RG16UI,
        RG32I,
        RG32UI,
        RGB8I,
        RGB8UI,
        RGB16I,
        RGB16UI,
        RGB32I,
        RGB32UI,
        RGBA8I,
        RGBA8UI,
        RGBA16I,
        RGBA16UI,
        RGBA32I,
        RGBA32UI,
        DEPTH_COMPONENT16,
        DEPTH_COMPONENT24,
        DEPTH_COMPONENT32F,
        DEPTH24_STENCIL8,
        DEPTH32F_STENCIL8,
        COMPRESSED_RED,
        COMPRESSED_RG,
        COMPRESSED_RGB,
        COMPRESSED_RGBA,
        COMPRESSED_SRGB,
        COMPRESSED_SRGB_ALPHA,
        COMPRESSED_RED_RGTC1,
        COMPRESSED_SIGNED_RED_RGTC1,
        COMPRESSED_RG_RGTC2,
        COMPRESSED_SIGNED_RG_RGTC2,
    }

    /**
     * Specifies the data type of the pixel data.
     */
    enum class TextureType {
        UNSIGNED_BYTE,
        BYTE,
        UNSIGNED_SHORT,
        SHORT,
        UNSIGNED_INT,
        INT,
        FLOAT,
        UNSIGNED_BYTE_3_3_2,
        UNSIGNED_BYTE_2_3_3_REV,
        UNSIGNED_SHORT_5_6_5,
        UNSIGNED_SHORT_5_6_5_REV,
        UNSIGNED_SHORT_4_4_4_4,
        UNSIGNED_SHORT_4_4_4_4_REV,
        UNSIGNED_SHORT_5_5_5_1,
        UNSIGNED_SHORT_1_5_5_5_REV,
        UNSIGNED_INT_8_8_8_8,
        UNSIGNED_INT_8_8_8_8_REV,
        UNSIGNED_INT_10_10_10_2,
        UNSIGNED_INT_2_10_10_10_REV
    }

    enum class TextureWrap {
        REPEAT,
        CLAMP_TO_EDGE,
        MIRRORED_REPEAT,
        CLAMP_TO_BORDER
    }

    /**
     * Specifies the target buffer object being used.
     */
    enum class BufferTarget {
        ARRAY_BUFFER,
        ELEMENT_ARRAY_BUFFER,
        PIXEL_PACK_BUFFER,
        PIXEL_UNPACK_BUFFER,
        UNIFORM_BUFFER,
        TEXTURE_BUFFER,
        TRANSFORM_FEEDBACK_BUFFER,
        COPY_READ_BUFFER,
        COPY_WRITE_BUFFER
    }

    /**
     * Specifies the expected usage pattern of the data store.
     */
    enum class BufferUsage {
        STREAM_DRAW,
        STREAM_READ,
        STREAM_COPY,
        STATIC_DRAW,
        STATIC_READ,
        STATIC_COPY,
        DYNAMIC_DRAW,
        DYNAMIC_READ,
        DYNAMIC_COPY
    }

    enum class FramebufferTarget {
        DRAW_FRAMEBUFFER,
        READ_FRAMEBUFFER,
        FRAMEBUFFER
    }

    enum class FramebufferAttachment {
        COLOR_ATTACHMENT,
        DEPTH_ATTACHMENT,
        STENCIL_ATTACHMENT,
        DEPTH_STENCIL_ATTACHMENT
    }

    enum class FramebufferStatus {
        FRAMEBUFFER_COMPLETE,
        FRAMEBUFFER_UNDEFINED,
        FRAMEBUFFER_INCOMPLETE_ATTACHMENT,
        FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT,
        FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER,
        FRAMEBUFFER_INCOMPLETE_READ_BUFFER,
        FRAMEBUFFER_UNSUPPORTED,
        FRAMEBUFFER_INCOMPLETE_MULTISAMPLE,
        FRAMEBUFFER_INCOMPLETE_LAYER_TARGETS
    }

    // General
    fun enable(cap: Capability)
    fun disable(cap: Capability)
    fun createStateSnapshot(): StateSnapshot
    fun restoreStateSnapshot(snapshot: StateSnapshot, blend: Boolean = true, depthTest: Boolean = true, stencilTest: Boolean = true, vao: Boolean = true, fbo: Boolean = true)

    // Shader & Program
    fun createVertexShader(): Int
    fun createGeometryShader(): Int
    fun createFragmentShader(): Int
    fun shaderSource(shader: Int, code: String)
    fun compileShader(shader: Int)
    fun getShaderCompileStatus(shader: Int): Boolean
    fun getShaderInfoLog(shader: Int): String
    fun createProgram(): Int
    fun attachShader(program: Int, shader: Int)
    fun linkProgram(program: Int)
    fun useProgram(program: Int)
    fun deleteShader(shader: Int)
    fun deleteProgram(program: Int)

    // Uniforms
    fun getUniformLocation(program: Int, name: String): Int
    fun uniform1i(location: Int, value: Int)
    fun uniform1f(location: Int, value: Float)
    fun uniform2i(location: Int, value1: Int, value2: Int)
    fun uniform2f(location: Int, value1: Float, value2: Float)
    fun uniform3i(location: Int, value1: Int, value2: Int, value3: Int)
    fun uniform3f(location: Int, value1: Float, value2: Float, value3: Float)
    fun uniform4i(location: Int, value1: Int, value2: Int, value3: Int, value4: Int)
    fun uniform4f(location: Int, value1: Float, value2: Float, value3: Float, value4: Float)
    fun uniformMatrix2fv(location: Int, transpose: Boolean, value: FloatArray)
    fun uniformMatrix3fv(location: Int, transpose: Boolean, value: FloatArray)
    fun uniformMatrix4fv(location: Int, transpose: Boolean, value: FloatArray)
    fun uniformMatrix3x2fv(location: Int, transpose: Boolean, value: FloatArray)
    fun uniformMatrix4x3fv(location: Int, transpose: Boolean, value: FloatArray)

    // Buffers
    fun createBuffer(): Int
    fun deleteBuffer(buffer: Int)
    fun bindBuffer(target: BufferTarget, buffer: Int)
    fun bufferData(target: BufferTarget, data: ByteBuffer, usage: BufferUsage)
    fun bufferData(target: BufferTarget, data: Long, usage: BufferUsage)

    // Arrays
    fun createVertexArray(): Int
    fun deleteVertexArray(vertexArray: Int)
    fun bindVertexArray(vertexArray: Int)
    fun drawArrays(mode: DrawMode, first: Int, count: Int)
    fun drawElements(mode: DrawMode, count: Int, type: PrimitiveType, offset: Int)

    // Attributes
    fun vertexAttribPointer(index: Int, size: Int, type: PrimitiveType, normalized: Boolean, stride: Int, offset: Int)
    fun enableVertexAttribArray(index: Int)
    fun disableVertexAttribArray(index: Int)

    // Textures
    fun createTexture(): Int
    fun deleteTexture(texture: Int)
    fun bindTexture(target: TextureTarget, texture: Int)
    fun activeTexture(textureUnit: Int)
    fun texMagFilter(target: TextureTarget, filter: TextureFilter)
    fun texMinFilter(target: TextureTarget, filter: TextureFilter)
    fun texWrapS(target: TextureTarget, filter: TextureWrap)
    fun texWrapT(target: TextureTarget, filter: TextureWrap)
    fun texImage1D(target: TextureTarget, level: Int, internalFormat: TextureInternalFormat, width: Int, border: Int, format: TextureFormat, type: TextureType, pixels: ByteBuffer?)
    fun texImage2D(target: TextureTarget, level: Int, internalFormat: TextureInternalFormat, width: Int, height: Int, border: Int, format: TextureFormat, type: TextureType, pixels: ByteBuffer?)
    fun generateMipmap(target: TextureTarget)

    // Framebuffers
    fun createFramebuffer(): Int
    fun deleteFramebuffer(framebuffer: Int)
    fun bindFramebuffer(target: FramebufferTarget, framebuffer: Int)
    fun framebufferTexture1D(target: FramebufferTarget, attachment: FramebufferAttachment, colorAttachmentId: Int = 0, textureTarget: TextureTarget, texture: Int, level: Int)
    fun framebufferTexture2D(target: FramebufferTarget, attachment: FramebufferAttachment, colorAttachmentId: Int = 0, textureTarget: TextureTarget, texture: Int, level: Int)
    fun checkFramebufferStatus(target: FramebufferTarget): FramebufferStatus

    // Blend, Depth, Stencil
    fun blendFuncSeparate(srcRGB: BlendFunction, dstRGB: BlendFunction, srcAlpha: BlendFunction, dstAlpha: BlendFunction)
    fun blendEquationSeparate(modeRGB: BlendEquation, modeAlpha: BlendEquation)
    fun depthFunc(func: DepthFunction)
    fun depthMask(flag: Boolean)
    fun stencilFuncSeparate(face: Face, func: StencilFunction, ref: Int, mask: Int)
    fun stencilOpSeparate(face: Face, sfail: StencilOp, dpfail: StencilOp, dppass: StencilOp)
}
