package me.offeex.bloomware.api.graphics.elements

import me.offeex.bloomware.api.graphics.GLApi
import me.offeex.bloomware.api.graphics.GLElement

abstract class Framebuffer(protected open val existingFbo: Int? = null): GLElement() {
    private var fbo_ = 0
    var fbo
        get() = existingFbo ?: fbo_
        private set (value) {
            fbo_ = value
        }

    private fun assertNewFramebuffer() {
        if (existingFbo != null) throw IllegalStateException("This action isn't available for pre-defined framebuffer")
    }

    fun bind(target: GLApi.FramebufferTarget = GLApi.FramebufferTarget.FRAMEBUFFER) {
        assertInit()
        glApi.bindFramebuffer(target, fbo)
    }

    override fun close() {
        if (existingFbo != null) return
        assertInit()
        glApi.deleteFramebuffer(fbo)
    }
}