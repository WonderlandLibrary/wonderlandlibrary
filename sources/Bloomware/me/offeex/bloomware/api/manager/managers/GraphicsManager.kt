package me.offeex.bloomware.api.manager.managers

import me.offeex.bloomware.Bloomware
import me.offeex.bloomware.api.graphics.GLApi
import me.offeex.bloomware.api.graphics.GLElement
import me.offeex.bloomware.api.graphics.impl.GLImpl
import me.offeex.bloomware.api.graphics.impl.pipelines.ESPPipeline
import me.offeex.bloomware.api.graphics.impl.pipelines.FontRendererPipeline
import me.offeex.bloomware.api.graphics.impl.pipelines.RectanglePipeline
import me.offeex.bloomware.api.manager.Manager

/**
 * Service for interacting with our graphics extension.
 */
object GraphicsManager: Manager(), Runnable {
    val api: GLApi
        get() = this.glApi

    lateinit var pipelineFontRenderer: FontRendererPipeline
    lateinit var pipelineESP: ESPPipeline
    lateinit var pipelineRect: RectanglePipeline

    private fun init() {
        pipelineFontRenderer = reg(FontRendererPipeline(Bloomware.mc.framebuffer))
        pipelineESP = reg(ESPPipeline(Bloomware.mc.framebuffer))
        pipelineRect = reg(RectanglePipeline(Bloomware.mc.framebuffer))
    }

    override fun run() {
        // Instantiating API
        val api = GLImpl()

        // Initializing GraphicsManager and all its registered elements, should run in the main thread
        this.glInit(api)
        init()
    }

    // Shit:
    private lateinit var glApi: GLApi
    private val toReg = mutableListOf<GLElement>()
    fun <T : GLElement> reg(el: T): T {
        if (this::glApi.isInitialized) {
            el.glInit(glApi)
        } else {
            toReg.add(el)
        }
        return el
    }
    fun glInit(glApi: GLApi) {
        if (this::glApi.isInitialized) throw IllegalStateException("GLApi is already initialized")
        this.glApi = glApi
        toReg.forEach { it.glInit(glApi) }
        toReg.clear()
    }
}