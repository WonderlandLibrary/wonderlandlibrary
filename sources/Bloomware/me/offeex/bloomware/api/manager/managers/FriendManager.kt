package me.offeex.bloomware.api.manager.managers

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import me.offeex.bloomware.Bloomware
import me.offeex.bloomware.api.helper.FilePath
import me.offeex.bloomware.api.helper.Saveable
import me.offeex.bloomware.api.manager.Manager
import me.offeex.bloomware.api.manager.managers.FileManager.loadFileContent
import me.offeex.bloomware.api.util.SerializationUtil.deserialize
import me.offeex.bloomware.api.util.SerializationUtil.serialize
import net.minecraft.entity.player.PlayerEntity

object FriendManager : Manager(), Runnable, Saveable {
    private val people = HashMap<String, PersonType>()

    override fun run() {
        val content = FilePath.PEOPLE.file.loadFileContent() ?: return
        if (content.isEmpty()) return
        val jsonObject = deserialize(content.joinToString("\n")) ?: return

        try {
            val array = jsonObject.asJsonArray

            for (entry in array) {
                val person = entry.asJsonObject

                try {
                    addPerson(person.get("nickname").asString, PersonType.valueOf(person.get("type").asString))
                } catch (e: Exception) {
                    continue
                }
            }
        } catch (e: Exception) {
            Bloomware.LOGGER.error("People file is corrupted and cannot be loaded!")
        }
    }

    override fun save() {
        val array = JsonArray()

        people.forEach { (nickname, type) ->
            val person = JsonObject()
            person.addProperty("nickname", nickname)
            person.addProperty("type", type.name)
            array.add(person)
        }

        FilePath.PEOPLE.file.printWriter().use { it.write(serialize(array)) }
    }

    fun getType(nickname: String): PersonType? {
        return people[nickname]
    }

    fun getType(player: PlayerEntity): PersonType? {
        return getType(player.entityName)
    }

    fun addPerson(nickname: String, type: PersonType) {
        people.remove(nickname)
        people[nickname] = type
    }

    fun removePerson(nickname: String) {
        people.remove(nickname)
    }

    enum class PersonType {
        FRIEND, ENEMY
    }
}