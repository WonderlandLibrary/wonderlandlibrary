package me.offeex.bloomware.api.graphics.elements

import me.offeex.bloomware.api.graphics.GLElement

class Shader(private var code: String, val type: Type, private val sourceCodeResolver: (() -> String)? = null): GLElement() {
    enum class Type {
        VERTEX,
        GEOMETRY,
        FRAGMENT
    }

    var glId = 0
        private set

    override fun init() {
        glId = when (type) {
            Type.VERTEX -> glApi.createVertexShader()
            Type.GEOMETRY -> glApi.createGeometryShader()
            Type.FRAGMENT -> glApi.createFragmentShader()
        }
        glApi.shaderSource(glId, code)
        glApi.compileShader(glId)
        if (!glApi.getShaderCompileStatus(glId)) {
            throw RuntimeException(glApi.getShaderInfoLog(glId))
        }
    }

    override fun close() {
        assertInit()
        glApi.deleteShader(glId)
    }

    fun reload() {
        assertInit()
        if (sourceCodeResolver != null) {
            code = sourceCodeResolver.invoke()
            glApi.deleteShader(glId)
            init()
        }
    }
}