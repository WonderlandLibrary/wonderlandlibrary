package me.offeex.bloomware.api.graphics.impl.framebuffers

import me.offeex.bloomware.api.graphics.elements.Framebuffer

class MinecraftFramebuffer(val fb: net.minecraft.client.gl.Framebuffer): Framebuffer() {
    override val existingFbo: Int
        get() = fb.fbo
}
