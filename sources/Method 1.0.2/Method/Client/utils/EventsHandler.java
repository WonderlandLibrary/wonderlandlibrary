package Method.Client.utils;

import Method.Client.Main;
import Method.Client.managers.CommandManager;
import Method.Client.module.Module;
import Method.Client.module.ModuleManager;
import Method.Client.module.combat.AntiBot;
import Method.Client.utils.Patcher.Core.ClassTransformer;
import Method.Client.utils.Patcher.Events.*;
import Method.Client.utils.Screens.Screen;
import Method.Client.utils.proxy.Overrides.MoveOverride;
import Method.Client.utils.system.Connection;
import Method.Client.utils.system.Wrapper;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.GetCollisionBoxesEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

import static Method.Client.module.ModuleManager.*;

public class EventsHandler {
    public static boolean isInit = false;

    private void tryEvent(Event event) {
        if (!isInit)
            return;
        Main.eventBus.post(event);
    }

    private void noVerifyevent(Event event) {
        Main.eventBus.post(event);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (Wrapper.INSTANCE.player() != null && Wrapper.INSTANCE.world() != null) {
            noVerifyevent(event);
            if (!isInit) {
                new Connection();
                isInit = true;
            }
        } else {
            AntiBot.bots.clear();
            isInit = false;
        }
    }


    ////
    // No player world check events
    ////

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void GuiScreenEventInit(GuiScreenEvent.InitGuiEvent.Post event) {
        noVerifyevent(event);
    }


    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void GuiScreenEventPost(GuiScreenEvent.ActionPerformedEvent.Post event) {
        noVerifyevent(event);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void GuiScreenEventPre(GuiScreenEvent.ActionPerformedEvent.Pre event) {
        noVerifyevent(event);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void GuiOpen(GuiOpenEvent event) {
        noVerifyevent(event);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onWorldUnload(WorldEvent.Unload event) {
        noVerifyevent(event);
    }


    //////
    // Static events
    ///////
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onWorldLoad(WorldEvent.Load event) {
        tryEvent(event);
        if (!isInit) {
            return;
        }
        for (Module module : FileManagerLoader) {
            module.setToggled(true);
        }
        FileManagerLoader.clear();
        MoveOverride.toggle();
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void renderNamePlate(@SuppressWarnings("rawtypes") RenderLivingEvent.Specials.Pre e) {
        if (ModuleManager.toggledModules.contains(getModuleByName("NameTags"))) {
            if (e.getEntity() instanceof EntityPlayer)
                e.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void PlayerLoggedInEvent(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.player != null) {
            ArrayList<IRecipe> recipes = Lists.newArrayList(CraftingManager.REGISTRY);
            recipes.removeIf((recipe) -> recipe.getRecipeOutput().isEmpty());
            recipes.removeIf((recipe) -> recipe.getIngredients().isEmpty());
            event.player.unlockRecipes(recipes);
        }
        tryEvent(event);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void PlayerLoggedOutEvent(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.player != null) {
            tryEvent(event);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void PlayerChangedDimensionEvent(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.player != null) {
            tryEvent(event);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void PlayerRespawnEvent(PlayerEvent.PlayerRespawnEvent event) {
        if (event.player != null) {
            tryEvent(event);
        }
    }

    public EventsHandler() {
        if (ClassTransformer.obfuscated)
            //noinspection ConstantConditions
            if (!Main.MODID.startsWith("m"))  // High tech ᑕOᑭYᖇIGᕼT !
                if (!Main.MODID.endsWith("d")) {
                    byte[] decodedBytes = Base64.getDecoder().decode(
                            "TWV0aG9kIENsaWVudCAqVW5vZmZpY2lhbCBQb3J0Kg==");
                    byte[] D = Base64.getDecoder().decode(
                            "aHR0cHM6Ly9naXRodWIuY29tL2Rhbm1hc3RlcjIvTWV0aG9kQ2xpZW50");
                    try {
                        Desktop.getDesktop().browse(new URI(new String(D)));
                    } catch (IOException | URISyntaxException e) {
                        e.printStackTrace();
                    }
                    String s = new String(decodedBytes);
                    System.out.println(s);
                    System.err.print(s);
                    Display.setTitle(s);
                    Minecraft.getMinecraft().setIngameNotInFocus();
                    Toolkit.getDefaultToolkit().getSystemClipboard()
                            .setContents(new StringSelection(s), new StringSelection(s));
                    try {
                        TimeUnit.SECONDS.sleep(15);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (!isInit) {
            return;
        }
        int key = Keyboard.getEventKey();
        if (Keyboard.getEventKeyState()) {
            onKeyPressed(key);
        }
    }


    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(Main.MODID)) {
            new Thread(() -> {
                try {
                    while (Wrapper.mc.currentScreen != Main.ClickGui) {
                        Wrapper.mc.displayGuiScreen(Main.ClickGui);
                        //noinspection BusyWait
                        Thread.sleep(25);
                        if (Wrapper.mc.currentScreen == Main.ClickGui) {
                            break;
                        }
                    }
                    Wrapper.mc.displayGuiScreen(Main.ClickGui);
                } catch (Exception ignored) {
                }
            }).start();
            Main.config.syncConfig();
        }
    }


    ////////////////////////
    ////////Normal Events
    ////    ////    ////    ////

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void Chunkevent(ChunkEvent.Load event) {
        tryEvent(event);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void Chunkevent(ChunkEvent.Unload event) {
        tryEvent(event);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onRenderGameOverlay(RenderGameOverlayEvent.Text event) {
        tryEvent(event);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onRenderPre(RenderGameOverlayEvent.Pre event) {
        tryEvent(event);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void RenderGameOverLayPost(RenderGameOverlayEvent.Post event) {
        tryEvent(event);
    }


    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
        tryEvent(event);
    }


    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void BackgroundDrawnEvent(GuiScreenEvent.BackgroundDrawnEvent event) {
        tryEvent(event);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void postDrawScreen(GuiScreenEvent.DrawScreenEvent.Post event) {
        tryEvent(event);
    }

    // lazy fix so we dont double event
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void postDrawScreen(GuiScreenEvent.DrawScreenEvent event) {
        for (Screen screen : Screen.Screens) {
            screen.DrawScreenEvent(event);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void ChatReceived(ClientChatReceivedEvent event) {
        tryEvent(event);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void GetCollisionBoxesEvent(GetCollisionBoxesEvent event) {
        tryEvent(event);
    }


    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void DrawBlockHighlightEvent(DrawBlockHighlightEvent event) {
        tryEvent(event);
    }


    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void ClientChatEvent(ClientChatEvent event) {
        tryEvent(event);
        if (event.getMessage().startsWith(String.valueOf(CommandManager.cmdPrefix))) {
            CommandManager.getInstance().runCommands(CommandManager.cmdPrefix + event.getMessage().substring(1));
            event.setCanceled(true);
            event.setMessage(null);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onLivingDeath(LivingDeathEvent event) {
        tryEvent(event);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void LivingUpdateEvent(LivingEvent.LivingUpdateEvent event) {
        tryEvent(event);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void RenderBlockOverlayEvent(RenderBlockOverlayEvent event) {
        tryEvent(event);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onCameraSetup(EntityViewRenderEvent.CameraSetup event) {
        tryEvent(event);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void fogColor(EntityViewRenderEvent.FogColors event) {
        tryEvent(event);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void fogDensity(EntityViewRenderEvent.FogDensity event) {
        tryEvent(event);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void FOVModifier(EntityViewRenderEvent.FOVModifier event) {
        tryEvent(event);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onTooltip(ItemTooltipEvent event) {
        tryEvent(event);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void BedSleep(PlayerSleepInBedEvent event) {
        tryEvent(event);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onItemPickup(EntityItemPickupEvent event) {
        tryEvent(event);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onProjectileImpact(ProjectileImpactEvent event) {
        tryEvent(event);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onAttackEntity(AttackEntityEvent event) {
        tryEvent(event);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void LivingHurtEvent(LivingHurtEvent event) {
        tryEvent(event);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        tryEvent(event);
    }

    @SubscribeEvent
    public void RenderTickEvent(TickEvent.RenderTickEvent event) {
        tryEvent(event);
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        tryEvent(event);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        tryEvent(event);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        tryEvent(event);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void WorldEvent(WorldEvent event) {
        tryEvent(event);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void postBackgroundTooltipRender(RenderTooltipEvent.PostBackground event) {
        tryEvent(event);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void RendertooltipPre(RenderTooltipEvent.Pre event) {
        tryEvent(event);
    }


    /////////
    /// Custom Events
    ///////

    // Implemented from EntityPlayerSpPatch
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void PreMotionEvent(PreMotionEvent event) {
        tryEvent(event);
    }

    // Implemented from EntityPlayerSpPatch
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void PostMotionEvent(PostMotionEvent event) {
        tryEvent(event);
    }

    // Implemented from EntityPlayerSpPatch
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPlayerMove(PlayerMoveEvent event) {
        tryEvent(event);
    }

    // Implemented from EntityPlayerPatch
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPlayerJump(EntityPlayerJumpEvent event) {
        tryEvent(event);
    }

    // Implemented from PlayerControllerMPPatch
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void DamageBlock(PlayerDamageBlockEvent event) {
        tryEvent(event);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onGetAmbientOcclusionLightValue(GetAmbientOcclusionLightValueEvent event) {
        tryEvent(event);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void Liquidvisitor(EventCanCollide event) {
        tryEvent(event);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onRenderTileEntity(RenderTileEntityEvent event) {
        tryEvent(event);
    }

    // special case must call onRenderBlockModel.isrunning = true
    public void onRenderBlockModel(RenderBlockModelEvent event) {
        tryEvent(event);
    }


}
