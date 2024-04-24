package me.offeex.bloomware.api.config.impl

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import me.offeex.bloomware.Bloomware
import me.offeex.bloomware.api.config.Config
import me.offeex.bloomware.api.helper.FilePath
import me.offeex.bloomware.api.util.NetworkUtil
import java.net.NetworkInterface

object TokenConfig : Config() {
    override val filePath = FilePath.TOKEN

    private var launchToken: String = ""

    override fun save(): JsonElement? {
        return if (launchToken.isNotEmpty()) {
            JsonObject().apply { addProperty("token", launchToken) }
        } else null
    }

    override fun load(element: JsonElement) {
        signIn("launch", mapOf(
            "token" to element.asJsonObject["token"].asString,
            "hwid" to getHWID(),
        ))
    }

    fun login(email: String, password: String): String {
        return signIn("activate", mapOf(
            "email" to email,
            "password" to password,
            "hwid" to getHWID(),
        ))
    }

    private fun signIn(endpoint: String, any: Map<String, String>): String {
        try {
            val res = NetworkUtil.CLIENT.post("/client/${endpoint}") { body.form = any }
            val body = res.body.json

            if (res.status != 200) {
                if (body.has("data") && body["data"]["problem"].string == "NO_SUBSCRIPTION") {
                    return "You don't have a subscription"
                }
                return "Invalid credentials"
            }

            if (body.has("token")) {
                launchToken = body["token"].string!!
                saveExternal()
            }

            Bloomware.expiresAt = body["subscription"]["expires"].number!!.toLong() * 1000L
            Bloomware.currentScreen = null
            Bloomware.LOGGER.info("Logged in")

            return "Logged in"
        } catch (e: Exception) {
            Bloomware.LOGGER.error("*elephant sounds*")
            return "*elephant sounds*"
        }
    }

    private fun getHWID() = "${System.getProperty("os.name")}%${System.getProperty("os.arch")}%${Runtime.getRuntime().availableProcessors()}%${Runtime.getRuntime().maxMemory()}%${
        NetworkInterface.getNetworkInterfaces()
            .asSequence()
            .mapNotNull { ni ->
                ni.hardwareAddress?.joinToString(separator = "-") {
                    "%02X".format(it)
                }
            }.toList().first()
    }"
}