package me.offeex.bloomware.api.graphics.impl.pipelines

import me.offeex.bloomware.api.graphics.GLApi
import me.offeex.bloomware.api.graphics.elements.Pipeline
import me.offeex.bloomware.api.graphics.impl.framebuffers.MinecraftFramebuffer
import net.minecraft.client.gl.Framebuffer
import org.joml.Matrix4f
import org.joml.Vector3f
import org.joml.Vector4f

class ESPPipeline(fb: Framebuffer): Pipeline<ESPPipeline>() {
    private val program = program {
        vertex.resource("/assets/bloomware/shaders/custom/esp.vsh")
        fragment.resource("/assets/bloomware/shaders/custom/esp.fsh")
    }

    private val output = output(
        defaultDrawMode = GLApi.DrawMode.QUADS,
        defaultFramebuffer = reg(MinecraftFramebuffer(fb)),
        defaultBlend = true,
        defaultBlendEquation = GLApi.BlendEquationSeparate(GLApi.BlendEquation.FUNC_ADD, GLApi.BlendEquation.FUNC_ADD),
        defaultBlendFunction = GLApi.BlendFunctionSeparate(GLApi.BlendFunction.SRC_ALPHA, GLApi.BlendFunction.ONE_MINUS_SRC_ALPHA, GLApi.BlendFunction.ONE, GLApi.BlendFunction.ZERO),
        defaultDepthTest = true,
        defaultDepthMask = true,
        defaultDepthFunction = GLApi.DepthFunction.LESS
    )

    private val position = attribute(0, Vector3f())
    private val color = attribute(1, Vector4f(), true)

    private val modelViewMat = uniform<Matrix4f>("ModelViewMat")
    private val projMat = uniform<Matrix4f>("ProjMat")

    fun matrices(modelViewMat: Matrix4f, projMat: Matrix4f) {
        this.modelViewMat.set(modelViewMat)
        this.projMat.set(projMat)
    }

    fun pos(matrix: Matrix4f, x: Double, y: Double, z: Double) = apply {
        val vector4f: Vector4f = matrix.transform(Vector4f(x.toFloat(), y.toFloat(), z.toFloat(), 1.0f))
        this.position.put(Vector3f(vector4f.x, vector4f.y, vector4f.z))
    }

    fun color(r: Float, g: Float, b: Float, a: Float) = apply {
        this.color.put(Vector4f(r, g, b, a))
    }
}