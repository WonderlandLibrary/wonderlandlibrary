package me.offeex.bloomware.api.graphics.elements

import me.offeex.bloomware.api.graphics.GLApi
import me.offeex.bloomware.api.graphics.GLElement
import me.offeex.bloomware.api.graphics.tools.NativeBuffer
import me.offeex.bloomware.api.graphics.tools.ObjectByteBufferOutput
import org.joml.*
import org.lwjgl.system.MemoryUtil
import java.io.Externalizable
import java.net.URL
import java.nio.ByteBuffer

@Suppress("FunctionName", "UNCHECKED_CAST")
abstract class Pipeline<P: Pipeline<P>> : GLElement() {
    private val attributes = mutableListOf<PipelineAttributeController<*>>()
    private val uniforms = mutableListOf<PipelineUniformController<*>>()
    private val textures = mutableListOf<PipelineTextureController>()
    private var program: PipelineProgramController? = null
    private var output: PipelineOutputController? = null

    override fun init() {
        if (program == null) throw IllegalStateException("Program controller is required")
        if (output == null) throw IllegalStateException("Output controller is required")
        attributes.sortBy { it.layout }
        val attributeAntiDup = mutableSetOf<Int>()
        attributes.forEach {
            if (attributeAntiDup.contains(it.layout)) throw IllegalStateException("Attribute with layout ${it.layout} is already registered")
            attributeAntiDup.add(it.layout)
        }
    }

    private var programSetupPerformed = false
    /**
     * Setup pipeline shaders
     */
    protected fun program(block: ProgramBuilder.() -> Unit): PipelineProgramController {
        assertPreInit()
        if (programSetupPerformed) throw IllegalStateException("Program setup is already performed")
        programSetupPerformed = true
        val builder = ProgramBuilder(this)
        builder.block()
        val program = builder._build()
        reg(program)
        this.program = program
        return program
    }

    /**
     * Pipeline program builder
     */
    class ProgramBuilder(private val pipeline: Pipeline<*>) {
        private val shaders = mutableListOf<Pair<Shader, Boolean>>()
        class ShaderImporter(private val builder: ProgramBuilder, private val type: Shader.Type) {
            private fun resourceResolver(resource: URL): () -> String = { resource.readText() }
            fun resource(path: String) {
                val resolver = resourceResolver(javaClass.getResource(path)!!)
                val s = builder.pipeline.reg(Shader(resolver(), type, resolver))
                builder.shaders.add(s to true)
            }

            fun resource(resource: URL) {
                val resolver = resourceResolver(resource)
                val s = builder.pipeline.reg(Shader(resolver(), type, resolver))
                builder.shaders.add(s to true)
            }

            fun source(code: String) {
                val s = builder.pipeline.reg(Shader(code, type))
                builder.shaders.add(s to true)
            }

            fun preRegistered(shader: Shader) {
                builder.shaders.add(shader to false)
            }
        }
        val vertex = ShaderImporter(this, Shader.Type.VERTEX)
        val geometry = ShaderImporter(this, Shader.Type.GEOMETRY)
        val fragment = ShaderImporter(this, Shader.Type.FRAGMENT)
        fun _build(): PipelineProgramController {
            return PipelineProgramController(pipeline, shaders)
        }
    }

    /**
     * Program controller
     */
    class PipelineProgramController(private val pipeline: Pipeline<*>, private val shaders: List<Pair<Shader, Boolean>>): GLElement() {
        var programId = 0
            private set

        override fun init() {
            programId = glApi.createProgram()
            shaders.forEach { (shader, _) -> glApi.attachShader(programId, shader.glId) }
            glApi.linkProgram(programId)
        }

        override fun close() {
            assertInit()
            shaders.forEach { (shader, shouldClose) -> if (shouldClose) shader.close() }
            glApi.deleteProgram(programId)
        }

        fun reload() {
            assertInit()
            pipeline.assertInactive()
            shaders.forEach { (shader, _) -> shader.reload() }
            glApi.deleteProgram(programId)
            programId = glApi.createProgram()
            shaders.forEach { (shader, _) -> glApi.attachShader(programId, shader.glId) }
            glApi.linkProgram(programId)
            pipeline.uniforms.forEach { it._reinit() }
            pipeline.textures.forEach { it._reinit() }
        }
    }

    fun reloadProgram() {
        assertInit()
        program!!.reload()
    }

    private var outputSetupPerformed = false

    /**
     * Setup pipeline output
     */
    protected fun output(
        defaultDrawMode: GLApi.DrawMode = GLApi.DrawMode.TRIANGLES,
        defaultFramebuffer: Framebuffer? = null,
        defaultBlend: Boolean = false,
        defaultBlendEquation: GLApi.BlendEquationSeparate = GLApi.BlendEquationSeparate(
            GLApi.BlendEquation.FUNC_ADD,
            GLApi.BlendEquation.FUNC_ADD
        ),
        defaultBlendFunction: GLApi.BlendFunctionSeparate = GLApi.BlendFunctionSeparate(
            GLApi.BlendFunction.ONE,
            GLApi.BlendFunction.ZERO,
            GLApi.BlendFunction.ONE,
            GLApi.BlendFunction.ZERO
        ),
        defaultDepthTest: Boolean = false,
        defaultDepthMask: Boolean = false,
        defaultDepthFunction: GLApi.DepthFunction = GLApi.DepthFunction.LESS,
        defaultStencilTest: Boolean = false,
        defaultStencilFunction: Array<GLApi.StencilFunctionSeparate> = arrayOf(
            GLApi.StencilFunctionSeparate(
                GLApi.Face.FRONT_AND_BACK,
                GLApi.StencilFunction.ALWAYS,
                0,
                0
            )
        ),
        defaultStencilOp: Array<GLApi.StencilOpSeparate> = arrayOf(
            GLApi.StencilOpSeparate(
                GLApi.Face.FRONT_AND_BACK,
                GLApi.StencilOp.KEEP,
                GLApi.StencilOp.KEEP,
                GLApi.StencilOp.KEEP
            )
        ),
    ): PipelineOutputController {
        assertPreInit()
        if (outputSetupPerformed) throw IllegalStateException("Output setup is already performed")
        outputSetupPerformed = true
        val output = PipelineOutputController(
            this,
            defaultDrawMode,
            defaultFramebuffer,
            defaultBlend,
            defaultBlendEquation,
            defaultBlendFunction,
            defaultDepthTest,
            defaultDepthMask,
            defaultDepthFunction,
            defaultStencilTest,
            defaultStencilFunction,
            defaultStencilOp
        )
        reg(output)
        this.output = output
        return output
    }

    class PipelineOutputController(
        private val pipeline: Pipeline<*>,
        private val defaultDrawMode: GLApi.DrawMode,
        private val defaultFramebuffer: Framebuffer?,
        private val defaultBlend: Boolean,
        private val defaultBlendEquation: GLApi.BlendEquationSeparate,
        private val defaultBlendFunction: GLApi.BlendFunctionSeparate,
        private val defaultDepthTest: Boolean,
        private val defaultDepthMask: Boolean,
        private val defaultDepthFunction: GLApi.DepthFunction,
        private val defaultStencilTest: Boolean,
        private val defaultStencilFunction: Array<out GLApi.StencilFunctionSeparate>,
        private val defaultStencilOp: Array<out GLApi.StencilOpSeparate>,
    ): GLElement() {
        var drawMode = defaultDrawMode
            private set
        private var framebuffer: Framebuffer? = defaultFramebuffer
        private var enableBlend = defaultBlend
        private var blendEquation = defaultBlendEquation
        private var blendFunction = defaultBlendFunction
        private var enableDepthTest = defaultDepthTest
        private var depthMask = defaultDepthMask
        private var depthFunction = defaultDepthFunction
        private var enableStencilTest = defaultStencilTest
        private var stencilFunction = defaultStencilFunction
        private var stencilOp = defaultStencilOp

        private var framebufferConfigured = false
        private var blendConfigured = false
        private var blendEquationConfigured = false
        private var blendFunctionConfigured = false
        private var depthTestConfigured = false
        private var depthMaskConfigured = false
        private var depthFunctionConfigured = false
        private var stencilTestConfigured = false
        private var stencilFunctionConfigured = false
        private var stencilOpConfigured = false

        private var wasConfigured = false

        fun _reset() {
            drawMode = defaultDrawMode
            framebuffer = defaultFramebuffer
            enableBlend = defaultBlend
            blendEquation = defaultBlendEquation
            blendFunction = defaultBlendFunction
            enableDepthTest = defaultDepthTest
            depthMask = defaultDepthMask
            depthFunction = defaultDepthFunction
            enableStencilTest = defaultStencilTest
            stencilFunction = defaultStencilFunction
            stencilOp = defaultStencilOp

            if (!wasConfigured) return

            framebufferConfigured = false
            blendConfigured = false
            blendEquationConfigured = false
            blendFunctionConfigured = false
            depthTestConfigured = false
            depthMaskConfigured = false
            depthFunctionConfigured = false
            stencilTestConfigured = false
            stencilFunctionConfigured = false
            stencilOpConfigured = false

            unbindBuffers()

            // Unbind framebuffer
            glApi.bindFramebuffer(GLApi.FramebufferTarget.FRAMEBUFFER, 0)
            if (stateSnapshot != null) {
                glApi.restoreStateSnapshot(stateSnapshot!!)
                stateSnapshot = null
            } else {
                // Disable blend
                glApi.disable(GLApi.Capability.BLEND)
                // Reset blend equation
                glApi.blendEquationSeparate(
                    GLApi.BlendEquation.FUNC_ADD,
                    GLApi.BlendEquation.FUNC_ADD
                )
                // Reset blend function
                glApi.blendFuncSeparate(
                    GLApi.BlendFunction.ONE,
                    GLApi.BlendFunction.ZERO,
                    GLApi.BlendFunction.ONE,
                    GLApi.BlendFunction.ZERO
                )
                // Disable depth test
                glApi.disable(GLApi.Capability.DEPTH_TEST)
                // Reset depth mask
                glApi.depthMask(true)
                // Reset depth function
                glApi.depthFunc(GLApi.DepthFunction.ALWAYS)
                // Disable stencil test
                glApi.disable(GLApi.Capability.STENCIL_TEST)
                // Reset stencil function
                glApi.stencilFuncSeparate(
                    GLApi.Face.FRONT_AND_BACK,
                    GLApi.StencilFunction.ALWAYS,
                    0,
                    0
                )
                // Reset stencil op
                glApi.stencilOpSeparate(
                    GLApi.Face.FRONT_AND_BACK,
                    GLApi.StencilOp.KEEP,
                    GLApi.StencilOp.KEEP,
                    GLApi.StencilOp.KEEP
                )
            }

            wasConfigured = false
        }

        private var stateSnapshot: GLApi.StateSnapshot? = null

        fun _ensureConfigured() {
            if (wasConfigured) return

            if (stateSnapshot == null) {
                stateSnapshot = glApi.createStateSnapshot()
            }

            if (!framebufferConfigured) {
                if (framebuffer == null) {
                    glApi.bindFramebuffer(GLApi.FramebufferTarget.FRAMEBUFFER, 0)
                } else {
                    framebuffer!!.bind()
                }
                framebufferConfigured = true
            }
            if (!blendConfigured) {
                if (enableBlend) {
                    glApi.enable(GLApi.Capability.BLEND)
                } else {
                    glApi.disable(GLApi.Capability.BLEND)
                }
                blendConfigured = true
            }
            if (!blendEquationConfigured) {
                glApi.blendEquationSeparate(
                    blendEquation.rgb,
                    blendEquation.alpha
                )
                blendEquationConfigured = true
            }
            if (!blendFunctionConfigured) {
                glApi.blendFuncSeparate(
                    blendFunction.srcRgb,
                    blendFunction.dstRgb,
                    blendFunction.srcAlpha,
                    blendFunction.dstAlpha
                )
                blendFunctionConfigured = true
            }
            if (!depthTestConfigured) {
                if (enableDepthTest) {
                    glApi.enable(GLApi.Capability.DEPTH_TEST)
                } else {
                    glApi.disable(GLApi.Capability.DEPTH_TEST)
                }
                depthTestConfigured = true
            }
            if (!depthMaskConfigured) {
                glApi.depthMask(depthMask)
                depthMaskConfigured = true
            }
            if (!depthFunctionConfigured) {
                glApi.depthFunc(depthFunction)
                depthFunctionConfigured = true
            }
            if (!stencilTestConfigured) {
                if (enableStencilTest) {
                    glApi.enable(GLApi.Capability.STENCIL_TEST)
                } else {
                    glApi.disable(GLApi.Capability.STENCIL_TEST)
                }
                stencilTestConfigured = true
            }
            if (!stencilFunctionConfigured) {
                stencilFunction.forEach { fn ->
                    glApi.stencilFuncSeparate(
                        fn.face,
                        fn.func,
                        fn.ref,
                        fn.mask
                    )
                }
            }
            if (!stencilOpConfigured) {
                stencilOp.forEach { op ->
                    glApi.stencilOpSeparate(
                        op.face,
                        op.sfail,
                        op.dpfail,
                        op.dppass
                    )
                }
            }

            bindBuffers()

            wasConfigured = true
        }

        fun drawMode(mode: GLApi.DrawMode) {
            assertInit()
            pipeline.assertActive()
            drawMode = mode
        }

        fun blend(enabled: Boolean) {
            assertInit()
            pipeline.assertActive()
            enableBlend = enabled
            blendConfigured = false
        }

        fun blendEquation(equation: GLApi.BlendEquationSeparate) {
            assertInit()
            pipeline.assertActive()
            blendEquation = equation
            blendEquationConfigured = false
        }

        fun blendFunction(function: GLApi.BlendFunctionSeparate) {
            assertInit()
            pipeline.assertActive()
            blendFunction = function
            blendFunctionConfigured = false
        }

        fun depthTest(enabled: Boolean) {
            assertInit()
            pipeline.assertActive()
            enableDepthTest = enabled
            depthTestConfigured = false
        }

        fun depthMask(mask: Boolean) {
            assertInit()
            pipeline.assertActive()
            depthMask = mask
            depthMaskConfigured = false
        }

        fun depthFunction(function: GLApi.DepthFunction) {
            assertInit()
            pipeline.assertActive()
            depthFunction = function
            depthFunctionConfigured = false
        }

        fun stencilTest(enabled: Boolean) {
            assertInit()
            pipeline.assertActive()
            enableStencilTest = enabled
            stencilTestConfigured = false
        }

        fun stencilFunction(vararg function: GLApi.StencilFunctionSeparate) {
            assertInit()
            pipeline.assertActive()
            stencilFunction = function
            stencilFunctionConfigured = false
        }

        fun stencilOp(vararg op: GLApi.StencilOpSeparate) {
            assertInit()
            pipeline.assertActive()
            stencilOp = op
            stencilOpConfigured = false
        }

        fun framebuffer(framebuffer: Framebuffer) {
            assertInit()
            pipeline.assertActive()
            this.framebuffer = framebuffer
            framebufferConfigured = false
        }

        private var vao: Int = 0
        private var vbo: Int = 0
        private var eao: Int = 0

        private var buffersBound = false

        private fun bindBuffers() {
            if (buffersBound) return
            glApi.bindVertexArray(vao)
            glApi.bindBuffer(GLApi.BufferTarget.ARRAY_BUFFER, vbo)
            glApi.bindBuffer(GLApi.BufferTarget.ELEMENT_ARRAY_BUFFER, eao)
            buffersBound = true
        }

        private fun unbindBuffers() {
            if (!buffersBound) return
            glApi.bindBuffer(GLApi.BufferTarget.ELEMENT_ARRAY_BUFFER, 0)
            glApi.bindBuffer(GLApi.BufferTarget.ARRAY_BUFFER, 0)
            glApi.bindVertexArray(0)
            buffersBound = false
        }

        override fun init() {
            vao = glApi.createVertexArray()
            vbo = glApi.createBuffer()
            eao = glApi.createBuffer()
            glApi.bindVertexArray(vao)
            glApi.bindBuffer(GLApi.BufferTarget.ARRAY_BUFFER, vbo)
            glApi.bindBuffer(GLApi.BufferTarget.ELEMENT_ARRAY_BUFFER, eao)
            var offset = 0
            val stride = pipeline.attributes.sumOf { it.parts * it.elementSize }
            pipeline.attributes.forEach { attribute ->
                glApi.enableVertexAttribArray(attribute.layout)
                glApi.vertexAttribPointer(
                    attribute.layout,
                    attribute.parts,
                    attribute.primitiveType,
                    attribute.normalized,
                    stride,
                    offset
                )
                offset += attribute.parts * attribute.elementSize
            }
            glApi.bindBuffer(GLApi.BufferTarget.ELEMENT_ARRAY_BUFFER, 0)
            glApi.bindBuffer(GLApi.BufferTarget.ARRAY_BUFFER, 0)
//            glApi.bindVertexArray(0)
        }

        override fun close() {
            glApi.deleteVertexArray(vao)
            glApi.deleteBuffer(vbo)
            glApi.deleteBuffer(eao)
        }
    }

    /**
     * Register a vertex attribute.
     * Supported composite types: [Vector2i], [Vector2f], [Vector2d], [Vector3i], [Vector3f], [Vector3d], [Vector4i], [Vector4f], [Vector4d], [Matrix2f], [Matrix2d], [Matrix3f], [Matrix3d], [Matrix4f], [Matrix4d], [Matrix3x2f], [Matrix3x2d], [Matrix4x3f], [Matrix4x3d]
     * Supported primitive types: [Byte], [Short], [Int], [Float], [Double]
     * @param layout Attribute layout
     * @param default Default value
     * @param normalized Is attribute normalized (clamps it to [-1; 1])
     */
    protected fun <T : Externalizable> attribute(layout: Int, default: T, normalized: Boolean = false): PipelineAttributeController<T> {
        assertPreInit()
        val controller = when (default) {
            is Vector2i -> PipelineAttributeController<T>(this, layout, normalized, default, GLApi.PrimitiveType.INT, 2, 4)
            is Vector2f -> PipelineAttributeController<T>(this, layout, normalized, default, GLApi.PrimitiveType.FLOAT, 2, 4)
            is Vector2d -> PipelineAttributeController<T>(this, layout, normalized, default, GLApi.PrimitiveType.DOUBLE, 2, 8)
            is Vector3i -> PipelineAttributeController<T>(this, layout, normalized, default, GLApi.PrimitiveType.INT, 3, 4)
            is Vector3f -> PipelineAttributeController<T>(this, layout, normalized, default, GLApi.PrimitiveType.FLOAT, 3, 4)
            is Vector3d -> PipelineAttributeController<T>(this, layout, normalized, default, GLApi.PrimitiveType.DOUBLE, 3, 8)
            is Vector4i -> PipelineAttributeController<T>(this, layout, normalized, default, GLApi.PrimitiveType.INT, 4, 4)
            is Vector4f -> PipelineAttributeController<T>(this, layout, normalized, default, GLApi.PrimitiveType.FLOAT, 4, 4)
            is Vector4d -> PipelineAttributeController<T>(this, layout, normalized, default, GLApi.PrimitiveType.DOUBLE, 4, 8)
            is Matrix2f -> PipelineAttributeController<T>(this, layout, normalized, default, GLApi.PrimitiveType.FLOAT, 4, 4)
            is Matrix2d -> PipelineAttributeController<T>(this, layout, normalized, default, GLApi.PrimitiveType.DOUBLE, 4, 8)
            is Matrix3f -> PipelineAttributeController<T>(this, layout, normalized, default, GLApi.PrimitiveType.FLOAT, 9, 4)
            is Matrix3d -> PipelineAttributeController<T>(this, layout, normalized, default, GLApi.PrimitiveType.DOUBLE, 9, 8)
            is Matrix4f -> PipelineAttributeController<T>(this, layout, normalized, default, GLApi.PrimitiveType.FLOAT, 16, 4)
            is Matrix4d -> PipelineAttributeController<T>(this, layout, normalized, default, GLApi.PrimitiveType.DOUBLE, 16, 8)
            is Matrix3x2f -> PipelineAttributeController<T>(this, layout, normalized, default, GLApi.PrimitiveType.FLOAT, 6, 4)
            is Matrix3x2d -> PipelineAttributeController<T>(this, layout, normalized, default, GLApi.PrimitiveType.DOUBLE, 6, 8)
            is Matrix4x3f -> PipelineAttributeController<T>(this, layout, normalized, default, GLApi.PrimitiveType.FLOAT, 12, 4)
            is Matrix4x3d -> PipelineAttributeController<T>(this, layout, normalized, default, GLApi.PrimitiveType.DOUBLE, 12, 8)
            else -> throw IllegalArgumentException("Unsupported attribute type")
        }
        attributes.add(controller)
        reg(controller)
        return controller
    }

    /**
     * Register a vertex attribute.
     * Supported composite types: [Vector2i], [Vector2f], [Vector2d], [Matrix2f], [Matrix2d], [Matrix3f], [Matrix3d], [Matrix4f], [Matrix4d], [Matrix3x2f], [Matrix3x2d], [Matrix4x3f], [Matrix4x3d]
     * Supported primitive types: [Byte], [Short], [Int], [Float], [Double]
     * @param layout Attribute layout
     * @param default Default value
     * @param normalized Is attribute normalized (clamps it to [-1; 1])
     */
    protected fun <T: Number> attribute(layout: Int, default: T, normalized: Boolean = false): PipelineAttributeController<T> {
        assertPreInit()
        val controller = when (default) {
            is Byte -> PipelineAttributeController<T>(this, layout, normalized, default, GLApi.PrimitiveType.BYTE, 1, 1)
            is Short -> PipelineAttributeController<T>(this, layout, normalized, default, GLApi.PrimitiveType.SHORT, 1, 2)
            is Int -> PipelineAttributeController<T>(this, layout, normalized, default, GLApi.PrimitiveType.INT, 1, 4)
            is Float -> PipelineAttributeController<T>(this, layout, normalized, default, GLApi.PrimitiveType.FLOAT, 1, 4)
            is Double -> PipelineAttributeController<T>(this, layout, normalized, default, GLApi.PrimitiveType.DOUBLE, 1, 8)
            else -> throw IllegalArgumentException("Unsupported attribute type")
        }
        attributes.add(controller)
        reg(controller)
        return controller
    }

    /**
     * Vertex attribute controller
     */
    class PipelineAttributeController<T>(
        private val pipeline: Pipeline<*>,
        val layout: Int,
        val normalized: Boolean,
        private val default: T,
        val primitiveType: GLApi.PrimitiveType,
        val parts: Int,
        val elementSize: Int
    ): GLElement() {
        private val vertices = mutableListOf<T>()
        private var current: Int = -1

        /**
         * Reset vertices buffer
         */
        fun _reset() {
            vertices.clear()
            current = -1
        }

        /**
         * Push a new vertex
         */
        fun _new() {
            assertInit()
            pipeline.assertActive()
            current = vertices.size
            vertices.add(default)
        }

        /**
         * Export the vertices
         */
        fun _export(): (byteBuffer: ByteBuffer, index: Int) -> Unit {
            assertInit()
            pipeline.assertActive()
            return { byteBuffer: ByteBuffer, index: Int ->
                val element = vertices[index]
                serialize(element, byteBuffer)
            }
        }

        private fun serialize(element: T, byteBuffer: ByteBuffer) {
            when (element) {
                is Externalizable -> {
                    element.writeExternal(ObjectByteBufferOutput(byteBuffer))
                }
                is Number -> {
                    when (element) {
                        is Byte -> byteBuffer.put(element)
                        is Short -> byteBuffer.putShort(element)
                        is Int -> byteBuffer.putInt(element)
                        is Float -> byteBuffer.putFloat(element)
                        is Double -> byteBuffer.putDouble(element)
                        else -> throw IllegalArgumentException("Unsupported attribute type")
                    }
                }
                else -> throw IllegalArgumentException("Unsupported attribute type")
            }
        }

        /**
         * Put an attribute value for the current vertex
         */
        fun put(value: T) {
            assertInit()
            pipeline.assertActive()
            if (current == -1) {
                throw IllegalStateException("No vertex selected")
            }
            vertices[current] = value
        }
    }

    /**
     * Register a uniform.
     * Supported composite types: [Vector2i], [Vector2f], [Vector3i], [Vector3f], [Vector4i], [Vector4f], [Matrix2f], [Matrix3f], [Matrix4f], [Matrix3x2f], [Matrix4x3f]
     * Supported primitive types: [Int], [Float]
     * @param name Uniform name
     * @param default Default value
     * @param defaultTransposed Whether the default value should be transposed (if it's a matrix)
     */
    protected fun <T : Externalizable> uniform(name: String, default: T? = null, defaultTransposed: Boolean = true): PipelineUniformController<T> {
        assertPreInit()
        val controller = PipelineUniformController(this, name, default, defaultTransposed)
        uniforms.add(controller)
        reg(controller)
        return controller
    }

    /**
     * Register a uniform
     * @param name Uniform name
     * @param default Default value
     * @param defaultTransposed Whether the default value should be transposed (if it's a matrix)
     */
    protected fun <T : Number> uniform(name: String, default: T? = null, defaultTransposed: Boolean = true): PipelineUniformController<T> {
        assertPreInit()
        val controller = PipelineUniformController(this, name, default, defaultTransposed)
        uniforms.add(controller)
        reg(controller)
        return controller
    }

    /**
     * Uniform controller
     */
    class PipelineUniformController<T>(
        private val pipeline: Pipeline<*>,
        private val name: String,
        private val default: T?,
        private val defaultTransposed: Boolean
    ): GLElement() {
        val defaultAction = if (default != null) { createAction(default, false) } else { null }
        var uniformLocation = 0
            private set
        var found = false
            private set

        override fun init() {
            pipeline.afterInit {
                _reinit()
            }
        }

        fun _reinit() {
            assertInit()
            pipeline.assertInactive()
            uniformLocation = glApi.getUniformLocation(pipeline.program!!.programId, name)
            found = uniformLocation != -1
            if (!found) {
                logger.warning("Uniform $name not found, ignoring")
            }
        }

        private var wasSet = false

        fun _reset() {
            wasSet = false
        }

        fun _ensureSet() {
            assertInit()
            pipeline.assertActive()
            if (!found) return
            if (!wasSet) defaultAction?.let { it() }
        }

        private fun createAction(value: T, transpose: Boolean): () -> Unit {
            when (value) {
                is Int -> return { glApi.uniform1i(uniformLocation, value) }
                is Float -> return { glApi.uniform1f(uniformLocation, value) }
                is Vector2i -> return { glApi.uniform2i(uniformLocation, value.x, value.y) }
                is Vector2f -> return { glApi.uniform2f(uniformLocation, value.x, value.y) }
                is Vector3i -> return { glApi.uniform3i(uniformLocation, value.x, value.y, value.z) }
                is Vector3f -> return { glApi.uniform3f(uniformLocation, value.x, value.y, value.z) }
                is Vector4i -> return { glApi.uniform4i(uniformLocation, value.x, value.y, value.z, value.w) }
                is Vector4f -> return { glApi.uniform4f(uniformLocation, value.x, value.y, value.z, value.w) }
                is Matrix2f -> return { glApi.uniformMatrix2fv(uniformLocation, transpose, floatArrayOf(
                    value.m00(), value.m01(),
                    value.m10(), value.m11()
                )) }
                is Matrix3f -> return { glApi.uniformMatrix3fv(uniformLocation, transpose, floatArrayOf(
                    value.m00(), value.m01(), value.m02(),
                    value.m10(), value.m11(), value.m12(),
                    value.m20(), value.m21(), value.m22()
                )) }
                is Matrix4f -> return { glApi.uniformMatrix4fv(uniformLocation, transpose, floatArrayOf(
                    value.m00(), value.m01(), value.m02(), value.m03(),
                    value.m10(), value.m11(), value.m12(), value.m13(),
                    value.m20(), value.m21(), value.m22(), value.m23(),
                    value.m30(), value.m31(), value.m32(), value.m33()
                )) }
                is Matrix3x2f -> return { glApi.uniformMatrix3x2fv(uniformLocation, transpose, floatArrayOf(
                    value.m00(), value.m01(),
                    value.m10(), value.m11(),
                    value.m20(), value.m21()
                )) }
                is Matrix4x3f -> return { glApi.uniformMatrix4x3fv(uniformLocation, transpose, floatArrayOf(
                    value.m00(), value.m01(), value.m02(),
                    value.m10(), value.m11(), value.m12(),
                    value.m20(), value.m21(), value.m22(),
                    value.m30(), value.m31(), value.m32()
                )) }
                else -> throw IllegalArgumentException("Unsupported uniform type")
            }
        }

        /**
         * Set the uniform value (and submit it to the GPU)
         * @param value Value
         * @param transpose Whether the value should be transposed (if it's a matrix)
         */
        fun <V: T> set(value: V, transpose: Boolean = false) {
            assertInit()
            pipeline.assertActive()
            if (!found) return
            wasSet = true
            val action = createAction(value, transpose)
            action()
        }
    }


    private var textureCount = 0

    protected fun texture(name: String, default: Texture? = null): PipelineTextureController {
        assertPreInit()
        val controller = PipelineTextureController(this, name, default, textureCount++)
        textures.add(controller)
        reg(controller)
        return controller
    }

    class PipelineTextureController(
        private val pipeline: Pipeline<*>,
        private val name: String,
        private val default: Texture?,
        private val textureUnit: Int
    ): GLElement() {
        var uniformLocation = 0
            private set
        var found = false
            private set

        var texture: Texture? = default
            private set

        override fun init() {
            pipeline.afterInit {
                _reinit()
            }
        }

        fun _reinit() {
            assertInit()
            pipeline.assertInactive()
            uniformLocation = glApi.getUniformLocation(pipeline.program!!.programId, name)
            found = uniformLocation != -1
            if (!found) {
                logger.warning("Texture uniform $name not found, ignoring")
            }
        }

        private var wasBound = false
        private var wasUpdated = false
        private var previousTypeBound = GLApi.TextureTarget.TEXTURE_2D

        fun _reset() {
            if (wasBound) {
                glApi.activeTexture(textureUnit)
                glApi.bindTexture(previousTypeBound, 0)
                glApi.activeTexture(0)
            }
            wasBound = false
            wasUpdated = false
            previousTypeBound = GLApi.TextureTarget.TEXTURE_2D
            this.texture = default
        }

        fun _ensureSet() {
            assertInit()
            pipeline.assertActive()
            if (!found) return
            if (wasBound && !wasUpdated) return
            val type = texture?.target ?: GLApi.TextureTarget.TEXTURE_2D
            glApi.activeTexture(textureUnit)
            if (wasBound && type != previousTypeBound) {
                glApi.bindTexture(previousTypeBound, 0)
            }
            previousTypeBound = type
            glApi.bindTexture(type, texture?.texture ?: 0)
            glApi.activeTexture(0)
            glApi.uniform1i(uniformLocation, textureUnit)
            wasBound = true
            wasUpdated = false
        }

        /**
         * Set the texture
         * @param texture Texture
         */
        fun set(texture: Texture) {
            assertInit()
            pipeline.assertActive()
            this.texture = texture
            wasUpdated = true
        }
    }

    /**
     * Current vertex count in the mesh
     */
    var vertexCount = 0
        private set

    private val indices = mutableListOf<Short>()
    @JvmInline
    value class Index(val value: Short)

    /**
     * Starts a new vertex
     */
    fun vertex(): P {
        assertActive()
        attributes.forEach { it._new() }
        indices.add(vertexCount.toShort())
        vertexCount++
        return this as P
    }

    /**
     * Returns the index of the current vertex for future reuse
     */
    fun index(): Index {
        assertActive()
        val index = Index((vertexCount - 1).toShort())
        if (index.value < 0) throw IllegalStateException("No vertex has been created yet")
        return index
    }

    /**
     * Reuses a vertex
     */
    fun index(value: Index) {
        assertActive()
        indices.add(value.value)
    }

    /**
     * Binds everything needed and draws the geometry
     * @param resetMesh if true, resets the mesh after drawing
     */
    fun draw(resetMesh: Boolean = true) {
        assertActive()
        output!!._ensureConfigured()
        uniforms.forEach { it._ensureSet() }
        textures.forEach { it._ensureSet() }

        val drawMode = output!!.drawMode
        val actualDrawMode = if (drawMode == GLApi.DrawMode.QUADS) { GLApi.DrawMode.TRIANGLES } else { drawMode }

        val attrWriter = attributes.map { it._export() }
        val vertexSize = attributes.sumOf { it.parts * it.elementSize }
        val size = vertexSize * vertexCount
        val allocator = MemoryUtil.getAllocator(false)

        val verticesBuffer = NativeBuffer.createOnAllocator(size, allocator)
        for (i in 0 until vertexCount) {
            attrWriter.forEach { data ->
                data(verticesBuffer.buffer, i)
            }
        }
        verticesBuffer.buffer.flip()

        val indices = prepareIndices(drawMode, actualDrawMode)
        val indicesBuffer = NativeBuffer.createOnAllocator(indices.size * 2, allocator)
        indices.forEach { index -> indicesBuffer.buffer.putShort(index) }
        indicesBuffer.buffer.flip()

        glApi.bufferData(GLApi.BufferTarget.ARRAY_BUFFER, verticesBuffer.buffer, GLApi.BufferUsage.STATIC_DRAW)
        glApi.bufferData(GLApi.BufferTarget.ELEMENT_ARRAY_BUFFER, indicesBuffer.buffer, GLApi.BufferUsage.STATIC_DRAW)
        glApi.drawElements(actualDrawMode, indices.size, GLApi.PrimitiveType.UNSIGNED_SHORT, 0)
        glApi.bufferData(GLApi.BufferTarget.ARRAY_BUFFER, 0, GLApi.BufferUsage.STATIC_DRAW)
        glApi.bufferData(GLApi.BufferTarget.ELEMENT_ARRAY_BUFFER, 0, GLApi.BufferUsage.STATIC_DRAW)
        verticesBuffer.close()
        indicesBuffer.close()
        if (resetMesh) resetMesh()
    }

    private fun prepareIndices(drawMode: GLApi.DrawMode, actualDrawMode: GLApi.DrawMode): List<Short> {
        if (drawMode == GLApi.DrawMode.QUADS && actualDrawMode == GLApi.DrawMode.TRIANGLES) {
            val ogIndices = this.indices
            if (ogIndices.size % 4 != 0) throw IllegalStateException("Index count must be a multiple of 4")
            val indices = ArrayList<Short>()
            for (i in 0 until ogIndices.size step 4) {
                indices.add(ogIndices[i])
                indices.add(ogIndices[i + 1])
                indices.add(ogIndices[i + 2])
                indices.add(ogIndices[i])
                indices.add(ogIndices[i + 2])
                indices.add(ogIndices[i + 3])
            }
            return indices
        }
        return this.indices
    }

    private fun resetMesh() {
        attributes.forEach { it._reset() }
        vertexCount = 0
        indices.clear()
    }

    /**
     * Reset pipeline state and unbinds everything
     */
    fun reset() {
        resetMesh()
        uniforms.forEach { it._reset() }
        textures.forEach { it._reset() }
        output!!._reset()
    }

    companion object {
        private val activePipeline = HashMap<GLApi, Pipeline<*>>()
    }

    private fun assertActive() {
        if (activePipeline[glApi] != this) {
            throw IllegalStateException("Pipeline is not active")
        }
    }

    private fun assertInactive() {
        if (activePipeline.containsKey(glApi)) {
            throw IllegalStateException("Some pipeline should not be active for this action")
        }
    }

    fun begin() {
        assertInit()
        assertInactive()
        activePipeline[glApi] = this
        glApi.useProgram(program!!.programId)
    }

    fun end() {
        assertInit()
        assertActive()
        activePipeline.remove(glApi)
        reset()
        glApi.useProgram(0)
    }

    fun use(block: P.() -> Unit) {
        begin()
        @Suppress("UNCHECKED_CAST")
        block(this as P)
        end()
    }

    override fun close() {
        assertInit()
        assertInactive()
        attributes.forEach { it.close() }
        uniforms.forEach { it.close() }
        program!!.close()
    }
}
