package rip.helium

import rip.helium.Helium
import net.minecraft.client.Minecraft
import rip.helium.cheat.CommandManager
import rip.helium.cheat.CheatManager
import rip.helium.configuration.ConfigurationManager
import rip.helium.notification.mgmt.NotificationManager
import rip.helium.account.AccountManager
import rip.helium.account.AccountLoginService
import com.thealtening.AltService
import me.hippo.systems.lwjeb.annotation.Collect
import java.net.URISyntaxException
import java.awt.AWTException
import java.security.NoSuchAlgorithmException
import rip.helium.event.minecraft.StartGameEvent
import java.awt.Desktop
import javax.swing.JOptionPane
import com.jagrosh.discordipc.IPCClient
import com.jagrosh.discordipc.IPCListener
import com.jagrosh.discordipc.entities.RichPresence
import java.time.OffsetDateTime
import java.awt.datatransfer.StringSelection
import com.thealtening.utilities.SSLVerification
import me.hippo.systems.lwjeb.EventBus
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.lwjgl.opengl.Display
import java.lang.Thread
import java.lang.Runnable
import rip.helium.utils.font.Fonts
import rip.helium.account.AccountsFile
import rip.helium.cheat.FocusManager
import rip.helium.event.Stage
import rip.helium.event.minecraft.ExitGameEvent
import rip.helium.gui.hud.Hud
import rip.helium.ui.main.Screen
import rip.helium.utils.*
import java.awt.Toolkit
import java.io.*
import java.lang.Exception
import java.net.URI

//import static rip.helium.cheat.impl.misc.FlagDetector.TabListCheck;

class Helium {

    companion object { //pretty much 'static' shit
        @JvmField
        var instance: Helium? = null
        @JvmField
        var clientDir: File? = null
        var logger: Logger? = null
        const val client_build = 11121
        @JvmField
        var eventBus: EventBus? = null
        @JvmField
        var getClient_name = "Helium"
        @JvmField
        var clientUser: String? = null

        init {
            instance = Helium()
            //clientDir = new File("C:\\" + File.separator + "Helium");
            clientDir = File(Minecraft.getMinecraft().mcDataDir, "Helium")
            logger = LogManager.getLogger()
        }
    }

    @JvmField
    var is18Mode = false
    @JvmField
    var developement = false
    @JvmField
    var cmds: CommandManager? = null
    @JvmField
    var cheatManager: CheatManager? = null
    @JvmField
    var configurationManager: ConfigurationManager? = null
    @JvmField
    var userInterface: Screen? = null
    @JvmField
    var focusManager: FocusManager? = null
    var hud: Hud? = null
    var notificationManager: NotificationManager? = null
    @JvmField
    var accountManager: AccountManager? = null
    @JvmField
    var accountLoginService: AccountLoginService? = null
    @JvmField
    var altService: AltService? = null
    var discordUsername = ""
    var isDiscord = false
    var auth = false
    var stopwatch = Stopwatch()

    /*/
    Creates configs automatically
     */
    fun createConfig(server: String?) {
        when (server) {
            "viper" -> {
                try {
                    val statText = File(Minecraft.getMinecraft().mcDataDir.toString() + System.getProperty("file.separator") + "Helium" + System.getProperty("file.separator") + "configurations" + System.getProperty("file.separator") + "Viper(Default).kkk")
                    val `is` = FileOutputStream(statText)
                    val osw = OutputStreamWriter(`is`)
                    val w: Writer = BufferedWriter(osw)
                    w.write("{\"default\":false,\"Inventory Cleaner\":{\"state\":false,\"bind\":0,\"values\":{}},\"Animations\":{\"state\":true,\"bind\":0,\"values\":{\"Mode\":\"Helium:true,Matt:false,Remix:false,1.7:false,Sigma:false,Slide:false,Kansio:false,Poke:false,oHare:false,Swang:false\"}},\"Brightness\":{\"state\":true,\"bind\":0,\"values\":{\"Gamma Multiplier\":\"250.0\",\"Mode\":\"Gamma:true,Night Vision:false\"}},\"Flight\":{\"state\":false,\"bind\":33,\"values\":{\"Speed\":\"5.099999999999998\",\"Flight\":\"Vanilla:true,LongjumpFly:false\",\"View Bobbing\":\"false\",\"MEME BOBBING\":\"false\",\"AntiKick\":\"true\"}},\"BookExploit\":{\"state\":false,\"bind\":0,\"values\":{\"Fire\":\"true\",\"Sharp\":\"true\",\"Knockback\":\"true\",\"Thorns\":\"true\",\"Level\":\"10.0\",\"Looting\":\"true\"}},\"Teleport\":{\"state\":false,\"bind\":0,\"values\":{}},\"Skeletal\":{\"state\":true,\"bind\":0,\"values\":{\"Color\":\"255.0:0.98999995:0.96999997:255\"}},\"Criticals\":{\"state\":false,\"bind\":0,\"values\":{}},\"Fucker\":{\"state\":false,\"bind\":0,\"values\":{}},\"No Rotate\":{\"state\":false,\"bind\":0,\"values\":{}},\"Items\":{\"state\":true,\"bind\":0,\"values\":{}},\"Spammer\":{\"state\":false,\"bind\":0,\"values\":{}},\"WaterSpeed\":{\"state\":true,\"bind\":0,\"values\":{}},\"Flag Detector\":{\"state\":false,\"bind\":0,\"values\":{}},\"FastBow\":{\"state\":false,\"bind\":0,\"values\":{\"Mode\":\"Multi:true,Viper:false\"}},\"Disabler\":{\"state\":true,\"bind\":0,\"values\":{\"Mode\":\"Viper:true,Muncher:false,Kohi:false,RinaOrc:false,Mineplex:false,Faithful:false,Verus:false,PingSpoof:false,OmegaCraft:false,Ghostly:false,Watchdog:false,Poopful:false\"}},\"NoFall\":{\"state\":true,\"bind\":0,\"values\":{\"Fast Fall\":\"false\",\"Mode\":\"Spoof:true,Hypixel:false\"}},\"Regen\":{\"state\":false,\"bind\":0,\"values\":{\"Health\":\"15.0\"}},\"Sneak\":{\"state\":false,\"bind\":0,\"values\":{}},\"TargetStrafe\":{\"state\":false,\"bind\":0,\"values\":{\"Rainbow\":\"true\",\"Hold Space\":\"true\",\"Distance\":\"2.5\"}},\"AutoPlay\":{\"state\":false,\"bind\":0,\"values\":{\"Mode\":\"Doubles:false,Solo:true\"}},\"Sprint\":{\"state\":true,\"bind\":0,\"values\":{}},\"No Clip\":{\"state\":false,\"bind\":0,\"values\":{\"Amount\":\"10.0\"}},\"InvMove\":{\"state\":true,\"bind\":0,\"values\":{\"Desyncronize\":\"false\"}},\"Tracers\":{\"state\":true,\"bind\":0,\"values\":{}},\"Chams\":{\"state\":true,\"bind\":0,\"values\":{\"Fill\":\"false\",\"Fill Color\":\"1.0:0.0:1.0:255\",\"Fill Rainbow\":\"false\"}},\"KillAura\":{\"state\":false,\"bind\":19,\"values\":{\"Autoblock\":\"true\",\"Prioritize Players\":\"false\",\"Middle Click to Ignore\":\"true\",\"APS\":\"13.699999999999969\",\"Autoblock Mode\":\"Real:true,Fake:false\",\"Priority\":\"Lowest Health:true,Least Armor:false,Closest:false\",\"Mode\":\"Priority:true,Multi:false\",\"KeepSprint\":\"true\",\"Targets\":\"Players:true,Monsters:true,Animals:false,Villagers:false,Golems:false\",\"APS Flucutation\":\"0.6\",\"Max Distance\":\"5.999999999999995\",\"Middle Click Reset\":\"true\",\"Left Click\":\"false\",\"Teams\":\"false\",\"Spinbot\":\"false\",\"HvH\":\"false\",\"Miss Hits\":\"true\",\"Show Circle\":\"true\"}},\"IceSpeed\":{\"state\":false,\"bind\":0,\"values\":{}},\"Phase\":{\"state\":false,\"bind\":0,\"values\":{\"Mode\":\"Aris:true,Vanilla:false,Faithful:false\"}},\"Inventory Manager\":{\"state\":false,\"bind\":0,\"values\":{\"Cleaning Delay\":\"150.0\",\"Automated Tasks\":\"Clean:false,Equip Armor:false,Sword Slot:false\",\"Sword Slot\":\"1.0\",\"Equip Delay\":\"150.0\",\"Only In Inventory\":\"false\",\"Spoof Open\":\"true\"}},\"Crosshair\":{\"state\":true,\"bind\":0,\"values\":{\"Length\":\"5.399999999999989\",\"Color\":\"1.0:0.0:1.0:255\",\"Gap\":\"1.1000000000000003\",\"Width\":\"0.25\",\"Fixed\":\"true\"}},\"AntiVoid\":{\"state\":false,\"bind\":0,\"values\":{}},\"Scaffold\":{\"state\":false,\"bind\":0,\"values\":{\"Downwards\":\"true\",\"Sneak\":\"true\",\"Hypixel\":\"true\",\"Keep Y\":\"true\",\"Switch\":\"true\",\"Mode\":\"Cubecraft:true,Hypixel:false,DEV:false\",\"Tower\":\"true\"}},\"Step\":{\"state\":false,\"bind\":0,\"values\":{}},\"ChatMods\":{\"state\":false,\"bind\":0,\"values\":{\"KillSult Mode\":\"AutoL:true,Insult:false\",\"Anti-Spam Bypass\":\"false\",\"Filter Bypass\":\"false\",\"AutoGG\":\"true\",\"Kill Sults\":\"false\"}},\"Hud\":{\"state\":true,\"bind\":0,\"values\":{\"Background Opacity\":\"0.3\",\"Client Name\":\"§4§lH§felium\",\"Mode\":\"Helium:true,Virtue:false,Memestick:false,Exhi:false,Helium2:false\",\"Shadow\":\"true\",\"Scoreboard Background\":\"true\",\"Target Hud\":\"true\",\"Lowercase\":\"true\",\"Tab GUI\":\"false\",\"Scoreboard Down\":\"10.0\",\"Arraylist\":\"true\",\"ArrayList Color Mode\":\"Wave:false,Custom:false,Rainbow:false,Rainbow2:true,Categories:false,Pulsing:false\",\"Arraylist Color\":\"1.0:0.22999999:0.88:255\",\"Tnt Alert\":\"true\"}},\"AntiBot\":{\"state\":false,\"bind\":0,\"values\":{}},\"LongJump\":{\"state\":false,\"bind\":0,\"values\":{\"Mode\":\"Vanilla:true,Mineplex:false,Fierce:false,Faithful:false\"}},\"NoStrike\":{\"state\":true,\"bind\":0,\"values\":{}},\"AutoPotion\":{\"state\":true,\"bind\":0,\"values\":{}},\"NoSlow\":{\"state\":true,\"bind\":0,\"values\":{\"Mode\":\"NCP:true,Hypixel:false\"}},\"ChestStealer\":{\"state\":false,\"bind\":0,\"values\":{\"BL Properties\":\"Harmful Potion:false,Worse Sword:false,Duplicate Tool:false,Worse Armor:false\",\"Ignore Custom Chest\":\"true\",\"BL Items\":\"\",\"Steal delay\":\"150.0\"}},\"Speed\":{\"state\":false,\"bind\":20,\"values\":{\"Speed\":\"1.2\",\"Mode\":\"Vanilla:true,Viper:false\"}},\"ESP\":{\"state\":false,\"bind\":0,\"values\":{\"Chest\":\"0.1:1.0:0.64:90\",\"Box (default)\":\"1.0:0.0:1.0:255\",\"Health Number\":\"1.0:0.0:1.0:255\",\"Health Style\":\"Number:false,Bar:true\",\"Box (target)\":\"0.0:1.0:0.64:255\",\"Box outline\":\"0.0:0.0:0.0:255\",\"Elements\":\"Box:false,Health:false,Name:false,Chests:false\",\"Avoid\":\"Invisible:false,Teammate:false,Bots:false\",\"Entities\":\"Players:false,Monsters:false,Animals:false,Villagers:false,Golems:false\",\"Name\":\"1.0:0.0:1.0:255\"}},\"NameTags\":{\"state\":true,\"bind\":0,\"values\":{\"Darkness\":\"0.3\"}},\"Velocity\":{\"state\":true,\"bind\":0,\"values\":{\"Vertical Adder\":\"0.21\",\"Mode\":\"Packet:true,Motion:false,OldAGC:false\",\"Horizontal Multiplier\":\"1.1\"}},\"FastUse\":{\"state\":false,\"bind\":0,\"values\":{}},\"Item Physics\":{\"state\":false,\"bind\":0,\"values\":{}},\"Freecam\":{\"state\":false,\"bind\":0,\"values\":{}},\"Xray\":{\"state\":false,\"bind\":0,\"values\":{}},\"Interface\":{\"state\":false,\"bind\":54,\"values\":{\"Cheat Descriptions\":\"true\"}},\"Protector\":{\"state\":false,\"bind\":0,\"values\":{\"Name Protect\":\"true\",\"Friend Protect\":\"true\"}},\"Waterwalk\":{\"state\":false,\"bind\":0,\"values\":{\"Mode\":\"Mineplex:true,Solid:false,NCP:false\"}},\"Chest Aura\":{\"state\":false,\"bind\":0,\"values\":{}},\"NoHurtCam\":{\"state\":false,\"bind\":0,\"values\":{}}}")
                    w.close()
                } catch (e: IOException) {
                    System.err.println("Problem writing to the file statsTest.txt")
                }
            }
        }
    }

    /*/
    Reads the name from the file
     */
    @Throws(IOException::class)
    fun readName() {
        try {
            val file = File(Minecraft.getMinecraft().mcDataDir.toString() + System.getProperty("file.separator") + "Helium" + System.getProperty("file.separator") + "helium.user")
            val br = BufferedReader(FileReader(file))
            var st: String?
            while (br.readLine().also { st = it } != null) {
                clientUser = st
            }
        } catch (fuck: Exception) {

        }
    }

    @Collect
    @Throws(URISyntaxException::class, IOException::class, AWTException::class, NoSuchAlgorithmException::class)
    fun onStartGame(event: StartGameEvent) {
        val rand = Mafs.getRandom(1, 2) //random generator
        val nigger = Desktop.getDesktop() // desktop (for browsing shit)


        //opens fade or never gonna give you up...
        /*if (rand == 1) {
            nigger.browse(URI("https://www.youtube.com/watch?v=dQw4w9WgXcQ"))
        } else {
            repeat(10) { //same as a for loop [for (int i = 0; i < 10; i++)]
                nigger.browse(URI("https://www.youtube.com/watch?v=Jqt9n9DwpgE&t=1s"))
            }
        }*/

        /*/
        Creates the file for names if it doesn't exist.
         */
        val name = File(Minecraft.getMinecraft().mcDataDir.toString() + System.getProperty("file.separator") + "Helium" + System.getProperty("file.separator") + "helium.user")
        if (!name.exists()) { //checks if the name file exists
            clientUser = JOptionPane.showInputDialog(null, "What should we call you?")
            try {
                val file = File(Minecraft.getMinecraft().mcDataDir.toString() + System.getProperty("file.separator") + "Helium" + System.getProperty("file.separator") + "helium.user")
                val `is` = FileOutputStream(file)
                val osw = OutputStreamWriter(`is`)
                val w: Writer = BufferedWriter(osw)
                w.write(clientUser)
                w.close()
            } catch (ex: Exception) {
            }
        }

        readName() //reads the file from the config


        /*/
        18+ mode prompt
         */
        val dialogButton = JOptionPane.YES_NO_OPTION
        val dialogResult = JOptionPane.showConfirmDialog(null, "Do you want to see 18+ modules?", null, dialogButton)
        is18Mode = dialogResult == JOptionPane.YES_OPTION

        /*/
        Discord RPC (ask Kansio for the jar or compile it yourself using maven.)
        note: broken for now.
        todo: fix it fag
        try {
            val client = IPCClient(699380061962371153L)
            client.setListener(object : IPCListener {
                override fun onReady(client: IPCClient) {
                    val builder = RichPresence.Builder()
                    if (developement) {
                        builder.setState("Developer Build: " + client_build)
                                .setDetails("Destroying Anticheats!")
                                .setStartTimestamp(OffsetDateTime.now())
                                .setLargeImage("shoot", "https://discord.gg/Rrb5PwP")
                                .setSmallImage("shoot", "best hacker client")
                    } else {
                        builder.setState("Release Build: " + client_build)
                                .setDetails("Destroying Anticheats!")
                                .setStartTimestamp(OffsetDateTime.now())
                                .setLargeImage("shoot", "https://discord.gg/Rrb5PwP")
                                .setSmallImage("shoot", "best hacker client")
                    }
                    client.sendRichPresence(builder.build())
                }
            })
            client.connect()
        } catch (ev: Exception) {
            ev.printStackTrace()
        }*/


        /*/
        Handles authentication
         */
        if (AuthUtil.check()) {
            auth = true
            NotificationUtil.sendInfo("Client", "You were successfully authenticated!")
        } else {
            auth = false
            val hwid = HWID.getHWID()
            val stringSelection = StringSelection(hwid)
            val clipboard = Toolkit.getDefaultToolkit().systemClipboard
            clipboard.setContents(stringSelection, null)
            AuthUtil.close()
            NotificationUtil.sendError("Client!", "Your HWID is not whitelisted! Bought the client and not whitelisted? Send your HWID to Shotbowxd to be whitelisted.")
        }

        if (event.stage == Stage.PRE) {

            /*/
            Creates the client directory.
             */
            if (!clientDir!!.exists()) {
                logger!!.info("Creating helium... " + if (clientDir!!.mkdirs()) "Done" else "Failed")
            }

            /*/
            Creates the viper config...
            Todo: outdated, should be fixed.
             */
            createConfig("viper")

            if (auth) {
                cheatManager = CheatManager()
                configurationManager = ConfigurationManager()
                notificationManager = NotificationManager()
                focusManager = FocusManager()
                accountManager = AccountManager()
                cmds = CommandManager()
                accountLoginService = AccountLoginService()
                SSLVerification().verify()
                altService = AltService()
                Runtime.getRuntime().addShutdownHook(Thread {
                    onExitGame()
                })
            }
        } else {
            if (auth) {
                Fonts.createFonts()
                cheatManager!!.registerCheats()
                configurationManager!!.loadConfigurationFiles(false)
                AccountsFile.load()
                userInterface = Screen()
                hud = Hud()
                Display.setTitle("Minecraft 1.8.8")
            }
        }
    }

    private fun onExitGame() {
        configurationManager!!.saveConfigurationFiles()
        eventBus!!.publish(ExitGameEvent())
    }

    val member: Any?
        get() = null

    init {
        /*/
        Event bus
         */
        EventBus().field().method().build().also {
            eventBus = it
        }.register(this)
    }
}