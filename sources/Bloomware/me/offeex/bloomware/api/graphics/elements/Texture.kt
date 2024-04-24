package me.offeex.bloomware.api.graphics.elements

import me.offeex.bloomware.api.graphics.GLApi
import me.offeex.bloomware.api.graphics.GLElement
import org.joml.Vector2i
import org.lwjgl.opengl.GL40
import java.nio.ByteBuffer

class Texture(val target: GLApi.TextureTarget = GLApi.TextureTarget.TEXTURE_2D): GLElement() {
    companion object {
        fun fromExisting(existingTexture: Int, target: GLApi.TextureTarget = GLApi.TextureTarget.TEXTURE_2D): Texture {
            return Texture(existingTexture, target)
        }
    }

    private var existingTexture: Int? = null
    var texture = 0
        private set
    var resolution: Vector2i? = null
        private set
    var internalFormat: GLApi.TextureInternalFormat? = null
        private set
    var format: GLApi.TextureFormat? = null
        private set
    var type: GLApi.TextureType? = null
        private set

    private constructor(existingTexture: Int, target: GLApi.TextureTarget): this(target) {
        this.existingTexture = existingTexture
        this.texture = existingTexture
    }

    override fun init() {
        if (existingTexture != null) return
        glApi.bindTexture(GLApi.TextureTarget.TEXTURE_2D, 0)
        texture = glApi.createTexture()
    }

    /**
     * Upload texture to GPU
     * @param level Specifies the level-of-detail number. Level 0 is the base image level. Level n is the nth mipmap reduction image. If target is GL_TEXTURE_RECTANGLE or GL_PROXY_TEXTURE_RECTANGLE, level must be 0.
     * @param internalformat Specifies the number of color components in the texture. Possible values: GL_ALPHA, GL_LUMINANCE, GL_LUMINANCE_ALPHA, GL_RGB, GL_RGBA.
     * @param format Specifies the format of the pixel data. Possible values: GL_ALPHA, GL_RGB, GL_RGBA, GL_LUMINANCE, GL_LUMINANCE_ALPHA.
     * @param type Specifies the data type of the pixel data. Possible values: GL_UNSIGNED_BYTE, GL_UNSIGNED_SHORT_5_6_5, GL_UNSIGNED_SHORT_4_4_4_4, GL_UNSIGNED_SHORT_5_5_5_1.
     */
    fun uploadTexture(width: Int, height: Int, level: Int = 0, internalformat: GLApi.TextureInternalFormat = GLApi.TextureInternalFormat.RGBA8, format: GLApi.TextureFormat = GLApi.TextureFormat.RGBA, type: GLApi.TextureType = GLApi.TextureType.UNSIGNED_BYTE, pixels: ByteBuffer? = null) {
        assertInit()
        glApi.bindTexture(target, texture)
        glApi.texImage2D(target, level, internalformat, width, height, 0, format, type, pixels)
        this.resolution = Vector2i(width, height)
        this.internalFormat = internalformat
        this.format = format
        this.type = type
        glApi.bindTexture(target, 0)
    }

    fun generateMipmap() {
        assertInit()
        glApi.bindTexture(target, texture)
        glApi.generateMipmap(target)
        glApi.bindTexture(target, 0)
    }

    fun setMinFilter(filter: GLApi.TextureFilter) {
        assertInit()
        glApi.bindTexture(target, texture)
        glApi.texMinFilter(target, filter)
        glApi.bindTexture(target, 0)
    }

    fun setMagFilter(filter: GLApi.TextureFilter) {
        assertInit()
        glApi.bindTexture(target, texture)
        glApi.texMagFilter(target, filter)
        glApi.bindTexture(target, 0)
    }

    fun setWrapS(wrap: GLApi.TextureWrap) {
        assertInit()
        glApi.bindTexture(target, texture)
        glApi.texWrapS(target, wrap)
        glApi.bindTexture(target, 0)
    }

    fun setWrapT(wrap: GLApi.TextureWrap) {
        assertInit()
        glApi.bindTexture(target, texture)
        glApi.texWrapT(target, wrap)
        glApi.bindTexture(target, 0)
    }

    override fun close() {
        if (existingTexture != null) return
        assertInit()
        glApi.deleteTexture(texture)
    }
}