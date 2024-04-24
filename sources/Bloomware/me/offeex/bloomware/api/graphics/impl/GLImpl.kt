package me.offeex.bloomware.api.graphics.impl

import com.mojang.blaze3d.systems.RenderSystem
import me.offeex.bloomware.api.graphics.GLApi
import org.lwjgl.opengl.GL40.*
import java.nio.ByteBuffer

class GLImpl : GLApi {
    private inline fun <T> exec(fn: () -> T): T {
        if (RenderSystem.isOnRenderThread()) return fn()
        else throw IllegalStateException("GL calls must be executed on render thread")
    }

    private fun resolveCapability(capability: GLApi.Capability) = when (capability) {
        GLApi.Capability.BLEND -> GL_BLEND
        GLApi.Capability.CULL_FACE -> GL_CULL_FACE
        GLApi.Capability.DEPTH_TEST -> GL_DEPTH_TEST
        GLApi.Capability.DITHER -> GL_DITHER
        GLApi.Capability.POLYGON_OFFSET_FILL -> GL_POLYGON_OFFSET_FILL
        GLApi.Capability.SAMPLE_ALPHA_TO_COVERAGE -> GL_SAMPLE_ALPHA_TO_COVERAGE
        GLApi.Capability.SAMPLE_COVERAGE -> GL_SAMPLE_COVERAGE
        GLApi.Capability.SCISSOR_TEST -> GL_SCISSOR_TEST
        GLApi.Capability.STENCIL_TEST -> GL_STENCIL_TEST
    }

    private fun resolveFace(face: GLApi.Face) = when (face) {
        GLApi.Face.FRONT -> GL_FRONT
        GLApi.Face.BACK -> GL_BACK
        GLApi.Face.FRONT_AND_BACK -> GL_FRONT_AND_BACK
    }

    private fun resolveBlendEquation(equation: GLApi.BlendEquation) = when (equation) {
        GLApi.BlendEquation.FUNC_ADD -> GL_FUNC_ADD
        GLApi.BlendEquation.FUNC_SUBTRACT -> GL_FUNC_SUBTRACT
        GLApi.BlendEquation.FUNC_REVERSE_SUBTRACT -> GL_FUNC_REVERSE_SUBTRACT
        GLApi.BlendEquation.MIN -> GL_MIN
        GLApi.BlendEquation.MAX -> GL_MAX
    }

    private fun resolveBlendFunction(func: GLApi.BlendFunction) = when (func) {
        GLApi.BlendFunction.ZERO -> GL_ZERO
        GLApi.BlendFunction.ONE -> GL_ONE
        GLApi.BlendFunction.SRC_COLOR -> GL_SRC_COLOR
        GLApi.BlendFunction.ONE_MINUS_SRC_COLOR -> GL_ONE_MINUS_SRC_COLOR
        GLApi.BlendFunction.DST_COLOR -> GL_DST_COLOR
        GLApi.BlendFunction.ONE_MINUS_DST_COLOR -> GL_ONE_MINUS_DST_COLOR
        GLApi.BlendFunction.SRC_ALPHA -> GL_SRC_ALPHA
        GLApi.BlendFunction.ONE_MINUS_SRC_ALPHA -> GL_ONE_MINUS_SRC_ALPHA
        GLApi.BlendFunction.DST_ALPHA -> GL_DST_ALPHA
        GLApi.BlendFunction.ONE_MINUS_DST_ALPHA -> GL_ONE_MINUS_DST_ALPHA
        GLApi.BlendFunction.CONSTANT_COLOR -> GL_CONSTANT_COLOR
        GLApi.BlendFunction.ONE_MINUS_CONSTANT_COLOR -> GL_ONE_MINUS_CONSTANT_COLOR
        GLApi.BlendFunction.CONSTANT_ALPHA -> GL_CONSTANT_ALPHA
        GLApi.BlendFunction.ONE_MINUS_CONSTANT_ALPHA -> GL_ONE_MINUS_CONSTANT_ALPHA
        GLApi.BlendFunction.SRC_ALPHA_SATURATE -> GL_SRC_ALPHA_SATURATE
        GLApi.BlendFunction.SRC1_COLOR -> GL_SRC1_COLOR
        GLApi.BlendFunction.ONE_MINUS_SRC1_COLOR -> GL_ONE_MINUS_SRC1_COLOR
        GLApi.BlendFunction.SRC1_ALPHA -> GL_SRC1_ALPHA
        GLApi.BlendFunction.ONE_MINUS_SRC1_ALPHA -> GL_ONE_MINUS_SRC1_ALPHA
    }

    private fun resolveDepthFunction(func: GLApi.DepthFunction) = when (func) {
        GLApi.DepthFunction.NEVER -> GL_NEVER
        GLApi.DepthFunction.LESS -> GL_LESS
        GLApi.DepthFunction.EQUAL -> GL_EQUAL
        GLApi.DepthFunction.LEQUAL -> GL_LEQUAL
        GLApi.DepthFunction.GREATER -> GL_GREATER
        GLApi.DepthFunction.NOTEQUAL -> GL_NOTEQUAL
        GLApi.DepthFunction.GEQUAL -> GL_GEQUAL
        GLApi.DepthFunction.ALWAYS -> GL_ALWAYS
    }

    private fun resolveStencilFunction(func: GLApi.StencilFunction) = when (func) {
        GLApi.StencilFunction.NEVER -> GL_NEVER
        GLApi.StencilFunction.LESS -> GL_LESS
        GLApi.StencilFunction.EQUAL -> GL_EQUAL
        GLApi.StencilFunction.LEQUAL -> GL_LEQUAL
        GLApi.StencilFunction.GREATER -> GL_GREATER
        GLApi.StencilFunction.NOTEQUAL -> GL_NOTEQUAL
        GLApi.StencilFunction.GEQUAL -> GL_GEQUAL
        GLApi.StencilFunction.ALWAYS -> GL_ALWAYS
    }

    private fun resolveStencilOp(op: GLApi.StencilOp) = when (op) {
        GLApi.StencilOp.KEEP -> GL_KEEP
        GLApi.StencilOp.ZERO -> GL_ZERO
        GLApi.StencilOp.REPLACE -> GL_REPLACE
        GLApi.StencilOp.INCR -> GL_INCR
        GLApi.StencilOp.INCR_WRAP -> GL_INCR_WRAP
        GLApi.StencilOp.DECR -> GL_DECR
        GLApi.StencilOp.DECR_WRAP -> GL_DECR_WRAP
        GLApi.StencilOp.INVERT -> GL_INVERT
    }

    private fun resolvePrimitiveType(type: GLApi.PrimitiveType) = when (type) {
        GLApi.PrimitiveType.BYTE -> GL_BYTE
        GLApi.PrimitiveType.UNSIGNED_BYTE -> GL_UNSIGNED_BYTE
        GLApi.PrimitiveType.SHORT -> GL_SHORT
        GLApi.PrimitiveType.UNSIGNED_SHORT -> GL_UNSIGNED_SHORT
        GLApi.PrimitiveType.INT -> GL_INT
        GLApi.PrimitiveType.UNSIGNED_INT -> GL_UNSIGNED_INT
        GLApi.PrimitiveType.FIXED -> GL_UNSIGNED_BYTE
        GLApi.PrimitiveType.HALF_FLOAT -> GL_HALF_FLOAT
        GLApi.PrimitiveType.FLOAT -> GL_FLOAT
        GLApi.PrimitiveType.DOUBLE -> GL_DOUBLE
    }

    private fun resolveDrawMode(mode: GLApi.DrawMode) = when (mode) {
        GLApi.DrawMode.POINTS -> GL_POINTS
        GLApi.DrawMode.LINES -> GL_LINES
        GLApi.DrawMode.LINE_STRIP -> GL_LINE_STRIP
        GLApi.DrawMode.LINE_LOOP -> GL_LINE_LOOP
        GLApi.DrawMode.TRIANGLES -> GL_TRIANGLES
        GLApi.DrawMode.TRIANGLE_STRIP -> GL_TRIANGLE_STRIP
        GLApi.DrawMode.TRIANGLE_FAN -> GL_TRIANGLE_FAN
        GLApi.DrawMode.QUADS -> GL_QUADS
        GLApi.DrawMode.QUAD_STRIP -> GL_QUAD_STRIP
        GLApi.DrawMode.POLYGON -> GL_POLYGON
    }

    private fun resolveTextureTarget(target: GLApi.TextureTarget) = when (target) {
        GLApi.TextureTarget.TEXTURE_1D -> GL_TEXTURE_1D
        GLApi.TextureTarget.TEXTURE_2D -> GL_TEXTURE_2D
        GLApi.TextureTarget.TEXTURE_3D -> GL_TEXTURE_3D
        GLApi.TextureTarget.TEXTURE_1D_ARRAY -> GL_TEXTURE_1D_ARRAY
        GLApi.TextureTarget.TEXTURE_2D_ARRAY -> GL_TEXTURE_2D_ARRAY
        GLApi.TextureTarget.TEXTURE_RECTANGLE -> GL_TEXTURE_RECTANGLE
        GLApi.TextureTarget.TEXTURE_CUBE_MAP -> GL_TEXTURE_CUBE_MAP
        GLApi.TextureTarget.TEXTURE_BUFFER -> GL_TEXTURE_BUFFER
        GLApi.TextureTarget.TEXTURE_2D_MULTISAMPLE -> GL_TEXTURE_2D_MULTISAMPLE
        GLApi.TextureTarget.TEXTURE_2D_MULTISAMPLE_ARRAY -> GL_TEXTURE_2D_MULTISAMPLE_ARRAY
    }

    private fun resolveTextureFilter(filter: GLApi.TextureFilter) = when (filter) {
        GLApi.TextureFilter.NEAREST -> GL_NEAREST
        GLApi.TextureFilter.LINEAR -> GL_LINEAR
        GLApi.TextureFilter.NEAREST_MIPMAP_NEAREST -> GL_NEAREST_MIPMAP_NEAREST
        GLApi.TextureFilter.LINEAR_MIPMAP_NEAREST -> GL_LINEAR_MIPMAP_NEAREST
        GLApi.TextureFilter.NEAREST_MIPMAP_LINEAR -> GL_NEAREST_MIPMAP_LINEAR
        GLApi.TextureFilter.LINEAR_MIPMAP_LINEAR -> GL_LINEAR_MIPMAP_LINEAR
    }

    private fun resolveTextureFormat(format: GLApi.TextureFormat) = when (format) {
        GLApi.TextureFormat.RED -> GL_RED
        GLApi.TextureFormat.RG -> GL_RG
        GLApi.TextureFormat.RGB -> GL_RGB
        GLApi.TextureFormat.RGBA -> GL_RGBA
        GLApi.TextureFormat.BGR -> GL_BGR
        GLApi.TextureFormat.BGRA -> GL_BGRA
        GLApi.TextureFormat.RED_INTEGER -> GL_RED_INTEGER
        GLApi.TextureFormat.RG_INTEGER -> GL_RG_INTEGER
        GLApi.TextureFormat.RGB_INTEGER -> GL_RGB_INTEGER
        GLApi.TextureFormat.RGBA_INTEGER -> GL_RGBA_INTEGER
        GLApi.TextureFormat.BGR_INTEGER -> GL_BGR_INTEGER
        GLApi.TextureFormat.BGRA_INTEGER -> GL_BGRA_INTEGER
        GLApi.TextureFormat.STENCIL_INDEX -> GL_STENCIL_INDEX
        GLApi.TextureFormat.DEPTH_COMPONENT -> GL_DEPTH_COMPONENT
        GLApi.TextureFormat.DEPTH_STENCIL -> GL_DEPTH_STENCIL
    }

    private fun resolveTextureInternalFormat(format: GLApi.TextureInternalFormat) = when (format) {
        GLApi.TextureInternalFormat.R8 -> GL_R8
        GLApi.TextureInternalFormat.R8_SNORM -> GL_R8_SNORM
        GLApi.TextureInternalFormat.R16 -> GL_R16
        GLApi.TextureInternalFormat.R16_SNORM -> GL_R16_SNORM
        GLApi.TextureInternalFormat.RG8 -> GL_RG8
        GLApi.TextureInternalFormat.RG8_SNORM -> GL_RG8_SNORM
        GLApi.TextureInternalFormat.RG16 -> GL_RG16
        GLApi.TextureInternalFormat.RG16_SNORM -> GL_RG16_SNORM
        GLApi.TextureInternalFormat.R3_G3_B2 -> GL_R3_G3_B2
        GLApi.TextureInternalFormat.RGB4 -> GL_RGB4
        GLApi.TextureInternalFormat.RGB5 -> GL_RGB5
        GLApi.TextureInternalFormat.RGB8 -> GL_RGB8
        GLApi.TextureInternalFormat.RGB8_SNORM -> GL_RGB8_SNORM
        GLApi.TextureInternalFormat.RGB10 -> GL_RGB10
        GLApi.TextureInternalFormat.RGB12 -> GL_RGB12
        GLApi.TextureInternalFormat.RGB16_SNORM -> GL_RGB16_SNORM
        GLApi.TextureInternalFormat.RGBA2 -> GL_RGBA2
        GLApi.TextureInternalFormat.RGBA4 -> GL_RGBA4
        GLApi.TextureInternalFormat.RGB5_A1 -> GL_RGB5_A1
        GLApi.TextureInternalFormat.RGBA8 -> GL_RGBA8
        GLApi.TextureInternalFormat.RGBA8_SNORM -> GL_RGBA8_SNORM
        GLApi.TextureInternalFormat.RGB10_A2 -> GL_RGB10_A2
        GLApi.TextureInternalFormat.RGB10_A2UI -> GL_RGB10_A2UI
        GLApi.TextureInternalFormat.RGBA12 -> GL_RGBA12
        GLApi.TextureInternalFormat.RGBA16 -> GL_RGBA16
        GLApi.TextureInternalFormat.SRGB8 -> GL_SRGB8
        GLApi.TextureInternalFormat.SRGB8_ALPHA8 -> GL_SRGB8_ALPHA8
        GLApi.TextureInternalFormat.R16F -> GL_R16F
        GLApi.TextureInternalFormat.RG16F -> GL_RG16F
        GLApi.TextureInternalFormat.RGB16F -> GL_RGB16F
        GLApi.TextureInternalFormat.RGBA16F -> GL_RGBA16F
        GLApi.TextureInternalFormat.R32F -> GL_R32F
        GLApi.TextureInternalFormat.RG32F -> GL_RG32F
        GLApi.TextureInternalFormat.RGB32F -> GL_RGB32F
        GLApi.TextureInternalFormat.RGBA32F -> GL_RGBA32F
        GLApi.TextureInternalFormat.R11F_G11F_B10F -> GL_R11F_G11F_B10F
        GLApi.TextureInternalFormat.RGB9_E5 -> GL_RGB9_E5
        GLApi.TextureInternalFormat.R8I -> GL_R8I
        GLApi.TextureInternalFormat.R8UI -> GL_R8UI
        GLApi.TextureInternalFormat.R16I -> GL_R16I
        GLApi.TextureInternalFormat.R16UI -> GL_R16UI
        GLApi.TextureInternalFormat.R32I -> GL_R32I
        GLApi.TextureInternalFormat.R32UI -> GL_R32UI
        GLApi.TextureInternalFormat.RG8I -> GL_RG8I
        GLApi.TextureInternalFormat.RG8UI -> GL_RG8UI
        GLApi.TextureInternalFormat.RG16I -> GL_RG16I
        GLApi.TextureInternalFormat.RG16UI -> GL_RG16UI
        GLApi.TextureInternalFormat.RG32I -> GL_RG32I
        GLApi.TextureInternalFormat.RG32UI -> GL_RG32UI
        GLApi.TextureInternalFormat.RGB8I -> GL_RGB8I
        GLApi.TextureInternalFormat.RGB8UI -> GL_RGB8UI
        GLApi.TextureInternalFormat.RGB16I -> GL_RGB16I
        GLApi.TextureInternalFormat.RGB16UI -> GL_RGB16UI
        GLApi.TextureInternalFormat.RGB32I -> GL_RGB32I
        GLApi.TextureInternalFormat.RGB32UI -> GL_RGB32UI
        GLApi.TextureInternalFormat.RGBA8I -> GL_RGBA8I
        GLApi.TextureInternalFormat.RGBA8UI -> GL_RGBA8UI
        GLApi.TextureInternalFormat.RGBA16I -> GL_RGBA16I
        GLApi.TextureInternalFormat.RGBA16UI -> GL_RGBA16UI
        GLApi.TextureInternalFormat.RGBA32I -> GL_RGBA32I
        GLApi.TextureInternalFormat.RGBA32UI -> GL_RGBA32UI
        GLApi.TextureInternalFormat.DEPTH_COMPONENT16 -> GL_DEPTH_COMPONENT16
        GLApi.TextureInternalFormat.DEPTH_COMPONENT24 -> GL_DEPTH_COMPONENT24
        GLApi.TextureInternalFormat.DEPTH_COMPONENT32F -> GL_DEPTH_COMPONENT32F
        GLApi.TextureInternalFormat.DEPTH24_STENCIL8 -> GL_DEPTH24_STENCIL8
        GLApi.TextureInternalFormat.DEPTH32F_STENCIL8 -> GL_DEPTH32F_STENCIL8
        GLApi.TextureInternalFormat.COMPRESSED_RED -> GL_COMPRESSED_RED
        GLApi.TextureInternalFormat.COMPRESSED_RG -> GL_COMPRESSED_RG
        GLApi.TextureInternalFormat.COMPRESSED_RGB -> GL_COMPRESSED_RGB
        GLApi.TextureInternalFormat.COMPRESSED_RGBA -> GL_COMPRESSED_RGBA
        GLApi.TextureInternalFormat.COMPRESSED_SRGB -> GL_COMPRESSED_SRGB
        GLApi.TextureInternalFormat.COMPRESSED_SRGB_ALPHA -> GL_COMPRESSED_SRGB_ALPHA
        GLApi.TextureInternalFormat.COMPRESSED_RED_RGTC1 -> GL_COMPRESSED_RED_RGTC1
        GLApi.TextureInternalFormat.COMPRESSED_SIGNED_RED_RGTC1 -> GL_COMPRESSED_SIGNED_RED_RGTC1
        GLApi.TextureInternalFormat.COMPRESSED_RG_RGTC2 -> GL_COMPRESSED_RG_RGTC2
        GLApi.TextureInternalFormat.COMPRESSED_SIGNED_RG_RGTC2 -> GL_COMPRESSED_SIGNED_RG_RGTC2
    }

    private fun resolveTextureType(type: GLApi.TextureType) = when (type) {
        GLApi.TextureType.UNSIGNED_BYTE -> GL_UNSIGNED_BYTE
        GLApi.TextureType.BYTE -> GL_BYTE
        GLApi.TextureType.UNSIGNED_SHORT -> GL_UNSIGNED_SHORT
        GLApi.TextureType.SHORT -> GL_SHORT
        GLApi.TextureType.UNSIGNED_INT -> GL_UNSIGNED_INT
        GLApi.TextureType.INT -> GL_INT
        GLApi.TextureType.FLOAT -> GL_FLOAT
        GLApi.TextureType.UNSIGNED_BYTE_3_3_2 -> GL_UNSIGNED_BYTE_3_3_2
        GLApi.TextureType.UNSIGNED_BYTE_2_3_3_REV -> GL_UNSIGNED_BYTE_2_3_3_REV
        GLApi.TextureType.UNSIGNED_SHORT_5_6_5 -> GL_UNSIGNED_SHORT_5_6_5
        GLApi.TextureType.UNSIGNED_SHORT_5_6_5_REV -> GL_UNSIGNED_SHORT_5_6_5_REV
        GLApi.TextureType.UNSIGNED_SHORT_4_4_4_4 -> GL_UNSIGNED_SHORT_4_4_4_4
        GLApi.TextureType.UNSIGNED_SHORT_4_4_4_4_REV -> GL_UNSIGNED_SHORT_4_4_4_4_REV
        GLApi.TextureType.UNSIGNED_SHORT_5_5_5_1 -> GL_UNSIGNED_SHORT_5_5_5_1
        GLApi.TextureType.UNSIGNED_SHORT_1_5_5_5_REV -> GL_UNSIGNED_SHORT_1_5_5_5_REV
        GLApi.TextureType.UNSIGNED_INT_8_8_8_8 -> GL_UNSIGNED_INT_8_8_8_8
        GLApi.TextureType.UNSIGNED_INT_8_8_8_8_REV -> GL_UNSIGNED_INT_8_8_8_8_REV
        GLApi.TextureType.UNSIGNED_INT_10_10_10_2 -> GL_UNSIGNED_INT_10_10_10_2
        GLApi.TextureType.UNSIGNED_INT_2_10_10_10_REV -> GL_UNSIGNED_INT_2_10_10_10_REV
    }

    private fun resolveTextureWrap(wrap: GLApi.TextureWrap) = when (wrap) {
        GLApi.TextureWrap.REPEAT -> GL_REPEAT
        GLApi.TextureWrap.MIRRORED_REPEAT -> GL_MIRRORED_REPEAT
        GLApi.TextureWrap.CLAMP_TO_EDGE -> GL_CLAMP_TO_EDGE
        GLApi.TextureWrap.CLAMP_TO_BORDER -> GL_CLAMP_TO_BORDER
    }

    private fun resolveBufferTarget(target: GLApi.BufferTarget) = when (target) {
        GLApi.BufferTarget.ARRAY_BUFFER -> GL_ARRAY_BUFFER
        GLApi.BufferTarget.ELEMENT_ARRAY_BUFFER -> GL_ELEMENT_ARRAY_BUFFER
        GLApi.BufferTarget.PIXEL_PACK_BUFFER -> GL_PIXEL_PACK_BUFFER
        GLApi.BufferTarget.PIXEL_UNPACK_BUFFER -> GL_PIXEL_UNPACK_BUFFER
        GLApi.BufferTarget.UNIFORM_BUFFER -> GL_UNIFORM_BUFFER
        GLApi.BufferTarget.TEXTURE_BUFFER -> GL_TEXTURE_BUFFER
        GLApi.BufferTarget.TRANSFORM_FEEDBACK_BUFFER -> GL_TRANSFORM_FEEDBACK_BUFFER
        GLApi.BufferTarget.COPY_READ_BUFFER -> GL_COPY_READ_BUFFER
        GLApi.BufferTarget.COPY_WRITE_BUFFER -> GL_COPY_WRITE_BUFFER
    }

    private fun resolveBufferUsage(usage: GLApi.BufferUsage) = when (usage) {
        GLApi.BufferUsage.STREAM_DRAW -> GL_STREAM_DRAW
        GLApi.BufferUsage.STREAM_READ -> GL_STREAM_READ
        GLApi.BufferUsage.STREAM_COPY -> GL_STREAM_COPY
        GLApi.BufferUsage.STATIC_DRAW -> GL_STATIC_DRAW
        GLApi.BufferUsage.STATIC_READ -> GL_STATIC_READ
        GLApi.BufferUsage.STATIC_COPY -> GL_STATIC_COPY
        GLApi.BufferUsage.DYNAMIC_DRAW -> GL_DYNAMIC_DRAW
        GLApi.BufferUsage.DYNAMIC_READ -> GL_DYNAMIC_READ
        GLApi.BufferUsage.DYNAMIC_COPY -> GL_DYNAMIC_COPY
    }

    private fun resolveFramebufferTarget(target: GLApi.FramebufferTarget) = when (target) {
        GLApi.FramebufferTarget.FRAMEBUFFER -> GL_FRAMEBUFFER
        GLApi.FramebufferTarget.DRAW_FRAMEBUFFER -> GL_DRAW_FRAMEBUFFER
        GLApi.FramebufferTarget.READ_FRAMEBUFFER -> GL_READ_FRAMEBUFFER
    }

    private fun resolveFramebufferAttachment(attachment: GLApi.FramebufferAttachment) = when (attachment) {
        GLApi.FramebufferAttachment.COLOR_ATTACHMENT -> GL_COLOR_ATTACHMENT0
        GLApi.FramebufferAttachment.DEPTH_ATTACHMENT -> GL_DEPTH_ATTACHMENT
        GLApi.FramebufferAttachment.STENCIL_ATTACHMENT -> GL_STENCIL_ATTACHMENT
        GLApi.FramebufferAttachment.DEPTH_STENCIL_ATTACHMENT -> GL_DEPTH_STENCIL_ATTACHMENT
    }

    private fun produceFramebufferStatus(status: Int) = when (status) {
        GL_FRAMEBUFFER_COMPLETE -> GLApi.FramebufferStatus.FRAMEBUFFER_COMPLETE
        GL_FRAMEBUFFER_UNDEFINED -> GLApi.FramebufferStatus.FRAMEBUFFER_UNDEFINED
        GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT -> GLApi.FramebufferStatus.FRAMEBUFFER_INCOMPLETE_ATTACHMENT
        GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT -> GLApi.FramebufferStatus.FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT
        GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER -> GLApi.FramebufferStatus.FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER
        GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER -> GLApi.FramebufferStatus.FRAMEBUFFER_INCOMPLETE_READ_BUFFER
        GL_FRAMEBUFFER_UNSUPPORTED -> GLApi.FramebufferStatus.FRAMEBUFFER_UNSUPPORTED
        GL_FRAMEBUFFER_INCOMPLETE_MULTISAMPLE -> GLApi.FramebufferStatus.FRAMEBUFFER_INCOMPLETE_MULTISAMPLE
        GL_FRAMEBUFFER_INCOMPLETE_LAYER_TARGETS -> GLApi.FramebufferStatus.FRAMEBUFFER_INCOMPLETE_LAYER_TARGETS
        else -> throw IllegalStateException("Unknown framebuffer status: $status")
    }

    // General
    override fun enable(cap: GLApi.Capability) = exec { glEnable(resolveCapability(cap)) }
    override fun disable(cap: GLApi.Capability) = exec { glDisable(resolveCapability(cap)) }
    override fun createStateSnapshot() = exec {
        GLApi.StateSnapshot(
            blend = glGetBoolean(GL_BLEND),
            blendEquationRgb = glGetInteger(GL_BLEND_EQUATION_RGB),
            blendEquationAlpha = glGetInteger(GL_BLEND_EQUATION_ALPHA),
            blendFuncSrcRgb = glGetInteger(GL_BLEND_SRC_RGB),
            blendFuncDstRgb = glGetInteger(GL_BLEND_DST_RGB),
            blendFuncSrcAlpha = glGetInteger(GL_BLEND_SRC_ALPHA),
            blendFuncDstAlpha = glGetInteger(GL_BLEND_DST_ALPHA),
            depthTest = glGetBoolean(GL_DEPTH_TEST),
            depthMask = glGetBoolean(GL_DEPTH_WRITEMASK),
            depthFunc = glGetInteger(GL_DEPTH_FUNC),
            stencilTest = glGetBoolean(GL_STENCIL_TEST),
            stencilMask = glGetInteger(GL_STENCIL_WRITEMASK),
            stencilFuncFrontFunc = glGetInteger(GL_STENCIL_FUNC),
            stencilFuncFrontRef = glGetInteger(GL_STENCIL_REF),
            stencilFuncFrontMask = glGetInteger(GL_STENCIL_VALUE_MASK),
            stencilFuncBackFunc = glGetInteger(GL_STENCIL_BACK_FUNC),
            stencilFuncBackRef = glGetInteger(GL_STENCIL_BACK_REF),
            stencilFuncBackMask = glGetInteger(GL_STENCIL_BACK_VALUE_MASK),
            stencilOpFrontSFail = glGetInteger(GL_STENCIL_FAIL),
            stencilOpFrontDPFail = glGetInteger(GL_STENCIL_PASS_DEPTH_FAIL),
            stencilOpFrontDPPass = glGetInteger(GL_STENCIL_PASS_DEPTH_PASS),
            stencilOpBackSFail = glGetInteger(GL_STENCIL_BACK_FAIL),
            stencilOpBackDPFail = glGetInteger(GL_STENCIL_BACK_PASS_DEPTH_FAIL),
            stencilOpBackDPPass = glGetInteger(GL_STENCIL_BACK_PASS_DEPTH_PASS),
            boundVAO = glGetInteger(GL_VERTEX_ARRAY_BINDING),
            boundFBO = glGetInteger(GL_FRAMEBUFFER_BINDING),
        )
    }
    override fun restoreStateSnapshot(
        snapshot: GLApi.StateSnapshot,
        blend: Boolean,
        depthTest: Boolean,
        stencilTest: Boolean,
        vao: Boolean,
        fbo: Boolean,
    ) = exec {
        if (blend) {
            if (snapshot.blend) {
                glEnable(GL_BLEND)
            }
            glBlendEquationSeparate(snapshot.blendEquationRgb, snapshot.blendEquationAlpha)
            glBlendFuncSeparate(snapshot.blendFuncSrcRgb, snapshot.blendFuncDstRgb, snapshot.blendFuncSrcAlpha, snapshot.blendFuncDstAlpha)
        }
        if (depthTest) {
            if (snapshot.depthTest) {
                glEnable(GL_DEPTH_TEST)
            }
            glDepthMask(snapshot.depthMask)
            glDepthFunc(snapshot.depthFunc)
        }
        if (stencilTest) {
            if (snapshot.stencilTest) {
                glEnable(GL_STENCIL_TEST)
            }
            glStencilMask(snapshot.stencilMask)
            glStencilFuncSeparate(GL_FRONT, snapshot.stencilFuncFrontFunc, snapshot.stencilFuncFrontRef, snapshot.stencilFuncFrontMask)
            glStencilFuncSeparate(GL_BACK, snapshot.stencilFuncBackFunc, snapshot.stencilFuncBackRef, snapshot.stencilFuncBackMask)
            glStencilOpSeparate(GL_FRONT, snapshot.stencilOpFrontSFail, snapshot.stencilOpFrontDPFail, snapshot.stencilOpFrontDPPass)
            glStencilOpSeparate(GL_BACK, snapshot.stencilOpBackSFail, snapshot.stencilOpBackDPFail, snapshot.stencilOpBackDPPass)
        }
        if (vao) {
            glBindVertexArray(snapshot.boundVAO)
        }
        if (fbo) {
            glBindFramebuffer(GL_FRAMEBUFFER, snapshot.boundFBO)
        }
    }


    // Shader & Program
    override fun createVertexShader(): Int = exec { glCreateShader(GL_VERTEX_SHADER) }
    override fun createGeometryShader(): Int = exec { glCreateShader(GL_GEOMETRY_SHADER) }
    override fun createFragmentShader(): Int = exec { glCreateShader(GL_FRAGMENT_SHADER) }
    override fun shaderSource(shader: Int, code: String) = exec { glShaderSource(shader, code) }
    override fun compileShader(shader: Int) = exec { glCompileShader(shader) }
    override fun getShaderCompileStatus(shader: Int) = exec { glGetShaderi(shader, GL_COMPILE_STATUS) == GL_TRUE }
    override fun getShaderInfoLog(shader: Int) = exec { glGetShaderInfoLog(shader) }
    override fun createProgram(): Int = exec { glCreateProgram() }
    override fun attachShader(program: Int, shader: Int) = exec { glAttachShader(program, shader) }
    override fun linkProgram(program: Int) = exec { glLinkProgram(program) }
    override fun useProgram(program: Int) = exec { glUseProgram(program) }
    override fun deleteShader(shader: Int) = exec { glDeleteShader(shader) }
    override fun deleteProgram(program: Int) = exec { glDeleteProgram(program) }

    // Uniforms
    override fun getUniformLocation(program: Int, name: String): Int = exec { glGetUniformLocation(program, name) }
    override fun uniform1i(location: Int, value: Int) = exec { glUniform1i(location, value) }
    override fun uniform1f(location: Int, value: Float) = exec { glUniform1f(location, value) }
    override fun uniform2i(location: Int, value1: Int, value2: Int) = exec { glUniform2i(location, value1, value2) }
    override fun uniform2f(location: Int, value1: Float, value2: Float) = exec { glUniform2f(location, value1, value2) }
    override fun uniform3i(location: Int, value1: Int, value2: Int, value3: Int) = exec { glUniform3i(location, value1, value2, value3) }
    override fun uniform3f(location: Int, value1: Float, value2: Float, value3: Float) = exec { glUniform3f(location, value1, value2, value3) }
    override fun uniform4i(location: Int, value1: Int, value2: Int, value3: Int, value4: Int) = exec { glUniform4i(location, value1, value2, value3, value4) }
    override fun uniform4f(location: Int, value1: Float, value2: Float, value3: Float, value4: Float) = exec { glUniform4f(location, value1, value2, value3, value4) }
    override fun uniformMatrix2fv(location: Int, transpose: Boolean, value: FloatArray) = exec { glUniformMatrix2fv(location, transpose, value) }
    override fun uniformMatrix3fv(location: Int, transpose: Boolean, value: FloatArray) = exec { glUniformMatrix3fv(location, transpose, value) }
    override fun uniformMatrix4fv(location: Int, transpose: Boolean, value: FloatArray) = exec { glUniformMatrix4fv(location, transpose, value) }
    override fun uniformMatrix3x2fv(location: Int, transpose: Boolean, value: FloatArray) = exec { glUniformMatrix3x2fv(location, transpose, value) }
    override fun uniformMatrix4x3fv(location: Int, transpose: Boolean, value: FloatArray) = exec { glUniformMatrix4x3fv(location, transpose, value) }

    // Buffers
    override fun createBuffer() = exec { glGenBuffers() }
    override fun deleteBuffer(buffer: Int) = exec { glDeleteBuffers(buffer) }
    override fun bindBuffer(target: GLApi.BufferTarget, buffer: Int) = exec { glBindBuffer(resolveBufferTarget(target), buffer) }
    override fun bufferData(target: GLApi.BufferTarget, data: ByteBuffer, usage: GLApi.BufferUsage) = exec { glBufferData(resolveBufferTarget(target), data, resolveBufferUsage(usage)) }
    override fun bufferData(target: GLApi.BufferTarget, data: Long, usage: GLApi.BufferUsage) = exec { glBufferData(resolveBufferTarget(target), data, resolveBufferUsage(usage)) }

    // Arrays
    override fun createVertexArray(): Int = exec { glGenVertexArrays() }
    override fun deleteVertexArray(vertexArray: Int) = exec { glDeleteVertexArrays(vertexArray) }
    override fun bindVertexArray(vertexArray: Int) = exec { glBindVertexArray(vertexArray) }
    override fun drawArrays(mode: GLApi.DrawMode, first: Int, count: Int) = exec { glDrawArrays(resolveDrawMode(mode), first, count) }
    override fun drawElements(mode: GLApi.DrawMode, count: Int, type: GLApi.PrimitiveType, offset: Int) = exec { glDrawElements(resolveDrawMode(mode), count, resolvePrimitiveType(type), offset.toLong()) }

    // Attributes
    override fun vertexAttribPointer(index: Int, size: Int, type: GLApi.PrimitiveType, normalized: Boolean, stride: Int, offset: Int) = exec { glVertexAttribPointer(index, size, resolvePrimitiveType(type), normalized, stride, offset.toLong()) }
    override fun enableVertexAttribArray(index: Int) = exec { glEnableVertexAttribArray(index) }
    override fun disableVertexAttribArray(index: Int) = exec { glDisableVertexAttribArray(index) }

    // Textures
    override fun createTexture(): Int = exec { glGenTextures() }
    override fun deleteTexture(texture: Int) = exec { glDeleteTextures(texture) }
    override fun bindTexture(target: GLApi.TextureTarget, texture: Int) = exec { glBindTexture(resolveTextureTarget(target), texture) }
    override fun activeTexture(textureUnit: Int) = exec { glActiveTexture(GL_TEXTURE0 + textureUnit) }
    override fun texMagFilter(target: GLApi.TextureTarget, filter: GLApi.TextureFilter) = exec { glTexParameteri(resolveTextureTarget(target), GL_TEXTURE_MAG_FILTER, resolveTextureFilter(filter)) }
    override fun texMinFilter(target: GLApi.TextureTarget, filter: GLApi.TextureFilter) = exec { glTexParameteri(resolveTextureTarget(target), GL_TEXTURE_MIN_FILTER, resolveTextureFilter(filter)) }
    override fun texWrapS(target: GLApi.TextureTarget, filter: GLApi.TextureWrap) = exec { glTexParameteri(resolveTextureTarget(target), GL_TEXTURE_WRAP_S, resolveTextureWrap(filter)) }
    override fun texWrapT(target: GLApi.TextureTarget, filter: GLApi.TextureWrap) = exec { glTexParameteri(resolveTextureTarget(target), GL_TEXTURE_WRAP_T, resolveTextureWrap(filter)) }
    override fun texImage1D(target: GLApi.TextureTarget, level: Int, internalFormat: GLApi.TextureInternalFormat, width: Int, border: Int, format: GLApi.TextureFormat, type: GLApi.TextureType, pixels: ByteBuffer?) = exec { glTexImage1D(resolveTextureTarget(target), level, resolveTextureInternalFormat(internalFormat), width, border, resolveTextureFormat(format), resolveTextureType(type), pixels) }
    override fun texImage2D(target: GLApi.TextureTarget, level: Int, internalFormat: GLApi.TextureInternalFormat, width: Int, height: Int, border: Int, format: GLApi.TextureFormat, type: GLApi.TextureType, pixels: ByteBuffer?) = exec { glTexImage2D(resolveTextureTarget(target), level, resolveTextureInternalFormat(internalFormat), width, height, border, resolveTextureFormat(format), resolveTextureType(type), pixels) }
    override fun generateMipmap(target: GLApi.TextureTarget) = exec { glGenerateMipmap(resolveTextureTarget(target)) }

    // Framebuffers
    override fun createFramebuffer(): Int = exec { glGenFramebuffers() }
    override fun deleteFramebuffer(framebuffer: Int) = exec { glDeleteFramebuffers(framebuffer) }
    override fun bindFramebuffer(target: GLApi.FramebufferTarget, framebuffer: Int) = exec { glBindFramebuffer(resolveFramebufferTarget(target), framebuffer) }
    override fun framebufferTexture1D(target: GLApi.FramebufferTarget, attachment: GLApi.FramebufferAttachment, colorAttachmentId: Int, textureTarget: GLApi.TextureTarget, texture: Int, level: Int) = exec { glFramebufferTexture1D(resolveFramebufferTarget(target), resolveFramebufferAttachment(attachment) + if (attachment == GLApi.FramebufferAttachment.COLOR_ATTACHMENT) { colorAttachmentId } else { 0 }, resolveTextureTarget(textureTarget), texture, level) }
    override fun framebufferTexture2D(target: GLApi.FramebufferTarget, attachment: GLApi.FramebufferAttachment, colorAttachmentId: Int, textureTarget: GLApi.TextureTarget, texture: Int, level: Int) = exec { glFramebufferTexture2D(resolveFramebufferTarget(target), resolveFramebufferAttachment(attachment) + if (attachment == GLApi.FramebufferAttachment.COLOR_ATTACHMENT) { colorAttachmentId } else { 0 }, resolveTextureTarget(textureTarget), texture, level) }
    override fun checkFramebufferStatus(target: GLApi.FramebufferTarget): GLApi.FramebufferStatus = exec { produceFramebufferStatus(glCheckFramebufferStatus(resolveFramebufferTarget(target))) }

    // Blend, Depth, Stencil
    override fun blendFuncSeparate(srcRGB: GLApi.BlendFunction, dstRGB: GLApi.BlendFunction, srcAlpha: GLApi.BlendFunction, dstAlpha: GLApi.BlendFunction) = exec { glBlendFuncSeparate(resolveBlendFunction(srcRGB), resolveBlendFunction(dstRGB), resolveBlendFunction(srcAlpha), resolveBlendFunction(dstAlpha)) }
    override fun blendEquationSeparate(modeRGB: GLApi.BlendEquation, modeAlpha: GLApi.BlendEquation) = exec { glBlendEquationSeparate(resolveBlendEquation(modeRGB), resolveBlendEquation(modeAlpha)) }
    override fun depthFunc(func: GLApi.DepthFunction) = exec { glDepthFunc(resolveDepthFunction(func)) }
    override fun depthMask(flag: Boolean) = exec { glDepthMask(flag) }
    override fun stencilFuncSeparate(face: GLApi.Face, func: GLApi.StencilFunction, ref: Int, mask: Int) = exec { glStencilFuncSeparate(resolveFace(face), resolveStencilFunction(func), ref, mask) }
    override fun stencilOpSeparate(face: GLApi.Face, sfail: GLApi.StencilOp, dpfail: GLApi.StencilOp, dppass: GLApi.StencilOp) = exec { glStencilOpSeparate(resolveFace(face), resolveStencilOp(sfail), resolveStencilOp(dpfail), resolveStencilOp(dppass)) }
}
