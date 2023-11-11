package me.nyan.flush.module.impl.misc;

import me.nyan.flush.event.SubscribeEvent;
import me.nyan.flush.event.impl.EventPacket;
import me.nyan.flush.module.Module;
import me.nyan.flush.utils.other.ChatUtils;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S3APacketTabComplete;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;

public class StaffChecker extends Module {
    private final Map<String, List<String>> staff = new LinkedHashMap<>();
    private final Map<String, String> invertedStaffMap = new LinkedHashMap<>();

    private final ArrayList<String> detected = new ArrayList<>();
    private final ArrayList<String> offline = new ArrayList<>();
    private volatile String lastMessaged;
    private StaffSearchThread thread;

    public StaffChecker() {
        super("StaffChecker", Category.MISC);
    }

    @Override
    public void onEnable() {
        lastMessaged = null;
        detected.clear();
        offline.clear();
        staff.clear();

        String serverIP = mc.getCurrentServerData().serverIP.toLowerCase();

        if (serverIP.contains("funcraft")) {
            try {
                Document document = Jsoup.connect("https://www.funcraft.net/fr/joueurs").get();
                Elements staffSections = document.body().getElementsByClass("staff-section");
                for (Element section : staffSections) {
                    String rank = section.getElementsByClass("staff-header").first()
                            .getElementsByTag("span").first().text();
                    String[] ignored = new String[]{"Youtuber", "Graphiste", "Builder"};
                    boolean b = false;
                    for (String s : ignored) {
                        if (rank.equalsIgnoreCase(s)) {
                            b = true;
                            break;
                        }
                    }
                    if (b) {
                        continue;
                    }

                    staff.put(rank, new ArrayList<>());
                    for (Element playerHeads : section.getElementsByClass("players-heads")) {
                        Elements headTooltips = playerHeads.getElementsByClass("head tooltips");
                        for (Element headTooltip : headTooltips) {
                            staff.get(rank).add(headTooltip.attributes().get("title"));
                        }
                    }
                }

                thread = new StaffSearchThread();
                thread.setDaemon(true);
                thread.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (serverIP.contains("derycube")) {
            ChatUtils.println("Searching for staff");

            staff.put("Admin", Arrays.asList("kiyanor", "ReachAurax_", "KinderBueno__"));
            staff.put("Responsable", Collections.singletonList("Draiikeau"));
            staff.put("Dev", Arrays.asList("PerrierBottle", "DisPro"));
            staff.put("SuperModo", Arrays.asList("Paulo123YT", "le_mana_flux"));
            staff.put("Modo", Arrays.asList("2wu", "Lucduke1", "Tolitoth", "FindouzEnLive", "Cilde", "Farmiwel", "Lazgard", "Uthaar"));
            staff.put("Assistant", Arrays.asList("blnche", "Flash303", "Koziade", "Skowzk", "razspender", "AsGyneco"));

            mc.getNetHandler().addToSendQueueNoEvent(new C14PacketTabComplete(""));
        } else {
            toggle();
        }

        for (Map.Entry<String, List<String>> entry : staff.entrySet()) {
            for (String name : entry.getValue()) {
                invertedStaffMap.put(name, entry.getKey());
            }
        }
    }

    @Override
    public void onDisable() {
        if (thread != null && thread.isAlive()) {
            thread.interrupt();
        }
    }

    @SubscribeEvent
    public void onPacket(EventPacket e) {
        String serverIP = mc.getCurrentServerData().serverIP.toLowerCase();

        if (e.getPacket() instanceof S02PacketChat) {
            S02PacketChat packet = e.getPacket();
            if (packet.getType() != 2) {
                String text = packet.getChatComponent().getUnformattedText();

                if (serverIP.contains("funcraft")) {
                    if (text.equals("Erreur: Merci de patienter un peu entre l'exécution de plusieurs commandes.")) {
                        e.cancel();
                        lastMessaged = null;
                        return;
                    }
                    if (text.equals("Erreur: Ce joueur a désactivé la réception des MP.") ||
                            text.equals("Erreur: Ce joueur n'accepte que les MP de ses amis.")) {
                        for (Map.Entry<String, List<String>> entry : staff.entrySet()) {
                            for (String member : entry.getValue()) {
                                if (member.equals(lastMessaged)) {
                                    ChatUtils.println("Found staff: §c" + entry.getKey() + " " + lastMessaged);
                                    break;
                                }
                            }
                        }
                        detected.add(lastMessaged);
                        lastMessaged = null;
                        e.cancel();
                        return;
                    }

                    for (Map.Entry<String, List<String>> entry : staff.entrySet()) {
                        for (String member : entry.getValue()) {
                            if (text.equals("Erreur: Le joueur `" + member + "` n'est pas connecté !")) {
                                e.cancel();
                                offline.add(member);
                                lastMessaged = null;
                                return;
                            }
                            if (text.startsWith("[Moi -> ") && text.contains(" " + member + "]")) {
                                ChatUtils.println("Found staff: §c" + entry.getKey() + " " + member);
                                e.cancel();
                                detected.add(member);
                                lastMessaged = null;
                                return;
                            }
                        }
                    }
                }
            }
        }

        if (e.getPacket() instanceof S3APacketTabComplete) {
            S3APacketTabComplete packet = e.getPacket();
            String[] results = packet.func_149630_c();
            if (serverIP.contains("derycube")) {
                for (String s : results) {
                    String role = invertedStaffMap.get(s);
                    if (role != null) {
                        ChatUtils.println("Found staff: §c" + role + " " + s);
                    }
                }
                toggle();
            }
        }
    }

    private class StaffSearchThread extends Thread {
        @Override
        public void run() {
            ChatUtils.println("Searching for staff");
            int staffSize = 0;
            for (Map.Entry<String, List<String>> entry : staff.entrySet()) {
                staffSize += entry.getValue().size();
            }

            do {
                for (Map.Entry<String, List<String>> entry : staff.entrySet()) {
                    for (String member : entry.getValue()) {
                        while (lastMessaged != null) {
                        }
                        if (offline.contains(member) || detected.contains(member)) {
                            continue;
                        }
                        mc.getNetHandler().addToSendQueue(new C01PacketChatMessage("/msg " + member + " salut"));
                        lastMessaged = member;
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException ignored) {
                        }
                    }
                }
            } while (offline.size() + detected.size() < staffSize);

            if (detected.isEmpty()) {
                ChatUtils.println("No staff detected");
            } else {
                ChatUtils.println("Found " + detected.size() + " staff members");
            }
            toggle();
        }
    }

    /*
    private boolean sprintEnabled;
    private Sprint sprint;
    private BlockPos lastOnGround;
    private boolean shouldSprint;
    private BlockPos target;
    private boolean tickPassed;

    @Override
    public void onEnable() {
        if (sprint == null) {
            sprint = Flush.moduleManager.getModule(Sprint.class);
        }
        sprintEnabled = sprint.isEnabled();
        if (sprintEnabled) {
            sprint.toggle();
        }
        lastOnGround = null;
        shouldSprint = false;
        target = null;
        tickPassed = false;
    }

    @Override
    public void onDisable() {
        if (sprintEnabled) {
            sprint.toggle();
        }
    }

    @SubscribeEvent
    public void onUpdate(EventUpdate e) {
        EnumFacing facing = mc.thePlayer.getHorizontalFacing();
        BlockPos ground = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 0.5, mc.thePlayer.posZ);

        if (target != null && (ground.equals(target) || target.getBlock() == Blocks.air)) {
            target = null;
        }

        if (sprint.isEnabled()) {
            sprint.toggle();
        }

        mc.thePlayer.setSprinting(shouldSprint && mc.thePlayer.moveForward != 0);

        if (ground.getBlock() != Blocks.air) {
            lastOnGround = ground;
        }

        if (target == null) {
            int xOffset = 0;
            int zOffset = 0;
            switch (facing) {
                case NORTH:
                    zOffset = 1;
                    break;

                case SOUTH:
                    zOffset = -1;
                    break;

                case WEST:
                    xOffset = 1;
                    break;

                case EAST:
                    xOffset = -1;
            }

            ArrayList<BlockPos> positions = new ArrayList<>();
            double y = mc.thePlayer.posY - 0.5;
            for (int j = -6; j < 6; j++) {
                for (int k = -6; k < 6; k++) {
                    double x = mc.thePlayer.posX + (xOffset != 0 ? xOffset * k : j);
                    double z = mc.thePlayer.posZ + (zOffset != 0 ? zOffset * k : j);
                    BlockPos pos = new BlockPos(x, y, z);
                    if ((x == mc.thePlayer.posX && z == mc.thePlayer.posZ) || pos.getBlock() == Blocks.air ||
                            getDistance(pos) > 5.25 || pos.equals(lastOnGround) || pos.getBlock().isReplaceable(mc.theWorld, pos)) {
                        continue;
                    }
                    positions.add(pos);
                }
            }
            BlockPos closest = null;
            Lists.reverse(positions);
            for (BlockPos pos : positions) {
                if ((closest == null || getDistance(pos) < getDistance(closest))) {
                    closest = pos;
                }
            }

            target = closest;
        }

        if (target == null) {
            return;
        }

        double distance = getDistance(target);

        mc.gameSettings.keyBindForward.pressed = true;

        float[] rotations = CombatUtils.getRotations(new Vec3(target).addVector(0.5, 0.5, 0.5));
        mc.thePlayer.rotationYaw = rotations[0];
        mc.thePlayer.rotationPitch = 0;


        if (tickPassed) {
            shouldSprint = distance > 2;
            mc.thePlayer.setSprinting(shouldSprint);
            mc.thePlayer.jump();
            mc.thePlayer.jumpTicks = 10;
            tickPassed = false;
            return;
        }

        if (ground.getBlock() == Blocks.air && !MovementUtils.isOnGround(0.5) && !tickPassed) {
            if (mc.thePlayer.onGround && mc.thePlayer.jumpTicks == 0) {
                if (distance >= 3) {
                    tickPassed = true;
                } else {
                    tickPassed = false;
                    shouldSprint = distance > 2;
                    mc.thePlayer.setSprinting(shouldSprint);
                    mc.thePlayer.jump();
                    mc.thePlayer.jumpTicks = 10;
                }
            }
        }
    }

    private double getDistance(BlockPos pos) {
        EnumFacing facing = mc.thePlayer.getHorizontalFacing();
        double x = mc.thePlayer.posX - pos.getX();
        double z = mc.thePlayer.posZ - pos.getZ();
        return MathHelper.sqrt_double(x * x + z * z) - (facing == EnumFacing.NORTH || facing == EnumFacing.WEST ? 1 : 0);
    }


     */

}