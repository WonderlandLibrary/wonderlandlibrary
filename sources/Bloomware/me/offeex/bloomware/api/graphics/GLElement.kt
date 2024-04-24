package me.offeex.bloomware.api.graphics

import java.util.logging.Logger

/**
 * Base class for all elements that interact with graphics API
 */
abstract class GLElement : AutoCloseable {
    protected val logger = Logger.getLogger("BloomwareRenderer")

    /**
     * Graphics API instance
     */
    protected lateinit var glApi: GLApi
        private set

    /**
     * Elements that should be initialized after [glApi] is initialized
     */
    private val toReg = mutableListOf<GLElement>()

    private val afterInitListeners = mutableListOf<() -> Unit>()

    /**
     * Initialize [glApi] and [toReg]
     */
    fun glInit(glApi: GLApi) {
        if (this::glApi.isInitialized) throw IllegalStateException("GLApi is already initialized in this " + this::class.simpleName + " element")
        this.glApi = glApi
        toReg.forEach { it.glInit(glApi) }
        toReg.clear()
        init()
        afterInitListeners.forEach { it() }
        afterInitListeners.clear()
    }

    /**
     * Execute [listener] after this element is initialized
     */
    fun afterInit(listener: () -> Unit) {
        if (this::glApi.isInitialized) {
            listener()
        } else {
            afterInitListeners.add(listener)
        }
    }

    /**
     * Initialize element
     */
    protected open fun init() {}

    /**
     * Register element to [toReg]
     */
    protected fun <T : GLElement> reg(el: T): T {
        if (this::glApi.isInitialized) {
            el.glInit(glApi)
        } else {
            toReg.add(el)
        }
        return el
    }

    /**
     * Assert that [glApi] is initialized
     */
    protected fun assertInit() {
        if (!this::glApi.isInitialized) throw IllegalStateException("GLApi is not initialized")
    }

    /**
     * Assert that [glApi] isn't initialized
     */
    protected fun assertPreInit() {
        if (this::glApi.isInitialized) throw IllegalStateException("GLApi is already initialized")
    }

    /**
     * Close element
     */
    override fun close() {}
}
