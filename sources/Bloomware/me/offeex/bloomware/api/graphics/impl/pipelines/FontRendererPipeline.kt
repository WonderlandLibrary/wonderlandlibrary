package me.offeex.bloomware.api.graphics.impl.pipelines

import me.offeex.bloomware.api.graphics.GLApi
import me.offeex.bloomware.api.graphics.elements.Pipeline
import me.offeex.bloomware.api.graphics.elements.Shader
import me.offeex.bloomware.api.graphics.elements.Texture
import me.offeex.bloomware.api.graphics.impl.framebuffers.MinecraftFramebuffer
import net.minecraft.client.gl.Framebuffer
import org.joml.*

class FontRendererPipeline(fb: Framebuffer): Pipeline<FontRendererPipeline>() {
    private val program = program {
        vertex.resource("/assets/bloomware/shaders/custom/font_renderer.vsh")
        fragment.resource("/assets/bloomware/shaders/custom/font_renderer.fsh")
    }

    private val output = output(
        defaultDrawMode = GLApi.DrawMode.QUADS,
        defaultFramebuffer = reg(MinecraftFramebuffer(fb)),
        defaultBlend = true,
        defaultBlendEquation = GLApi.BlendEquationSeparate(GLApi.BlendEquation.FUNC_ADD, GLApi.BlendEquation.FUNC_ADD),
        defaultBlendFunction = GLApi.BlendFunctionSeparate(GLApi.BlendFunction.SRC_ALPHA, GLApi.BlendFunction.ONE_MINUS_SRC_ALPHA, GLApi.BlendFunction.ONE, GLApi.BlendFunction.ZERO),
    )

    private val position = attribute(0, Vector3f())
    private val color = attribute(1, Vector4f(), true)
    private val textureCoords = attribute(2, Vector2f())

    private val modelViewMat = uniform<Matrix4f>("ModelViewMat")
    private val projMat = uniform<Matrix4f>("ProjMat")

    private val atlas = texture("AtlasSampler")

    fun atlas(texture: Texture) = apply {
        this.atlas.set(texture)
    }

    fun matrices(modelViewMat: Matrix4f, projMat: Matrix4f) {
        this.modelViewMat.set(modelViewMat)
        this.projMat.set(projMat)
    }

    fun pos(matrix: Matrix4f, x: Float, y: Float, z: Float) = apply {
        val vector4f: Vector4f = matrix.transform(Vector4f(x, y, z, 1.0f))
        this.position.put(Vector3f(vector4f.x, vector4f.y, vector4f.z))
    }

    fun color(r: Float, g: Float, b: Float, a: Float) = apply {
        this.color.put(Vector4f(r, g, b, a))
    }

    fun tex(x: Float, y: Float) = apply {
        this.textureCoords.put(Vector2f(x, y))
    }
}