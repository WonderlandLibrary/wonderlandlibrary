package org.luaj.vm2.customs.events;

import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.*;
import org.luaj.vm2.customs.EventHook;
import wtf.expensive.events.Event;
import wtf.expensive.events.impl.packet.EventPacket;
import wtf.expensive.events.impl.player.EventMotion;

public class EventPacketHook extends EventHook {

    private EventPacket packet;

    public EventPacketHook(Event event) {
        super(event);
        this.packet = (EventPacket) event;
    }

    public boolean isServerPacket(String string) {
        switch (string.toLowerCase()) {
            case "sadvancementinfopacket":
                return packet.getPacket() instanceof SAdvancementInfoPacket;
            case "sanimateblockbreakpacket":
                return packet.getPacket() instanceof SAnimateBlockBreakPacket;
            case "sanimatehandpacket":
                return packet.getPacket() instanceof SAnimateHandPacket;
            case "sblockactionpacket":
                return packet.getPacket() instanceof SBlockActionPacket;
            case "scamerapacket":
                return packet.getPacket() instanceof SCameraPacket;
            case "schangeblockpacket":
                return packet.getPacket() instanceof SChangeBlockPacket;
            case "schangegamestatepacket":
                return packet.getPacket() instanceof SChangeGameStatePacket;
            case "schatpacket":
                return packet.getPacket() instanceof SChatPacket;
            case "schunkdatapacket":
                return packet.getPacket() instanceof SChunkDataPacket;
            case "sclosewindowpacket":
                return packet.getPacket() instanceof SCloseWindowPacket;
            case "scollectitempacket":
                return packet.getPacket() instanceof SCollectItemPacket;
            case "scombatpacket":
                return packet.getPacket() instanceof SCombatPacket;
            case "scommandlistpacket":
                return packet.getPacket() instanceof SCommandListPacket;
            case "sconfirmtransactionpacket":
                return packet.getPacket() instanceof SConfirmTransactionPacket;
            case "scooldownpacket":
                return packet.getPacket() instanceof SCooldownPacket;
            case "scustompayloadplaypacket":
                return packet.getPacket() instanceof SCustomPayloadPlayPacket;
            case "sdestroyentitiespacket":
                return packet.getPacket() instanceof SDestroyEntitiesPacket;
            case "sdisconnectpacket":
                return packet.getPacket() instanceof SDisconnectPacket;
            case "sdisplayobjectivepacket":
                return packet.getPacket() instanceof SDisplayObjectivePacket;
            case "sentityequipmentpacket":
                return packet.getPacket() instanceof SEntityEquipmentPacket;
            case "sentityheadlookpacket":
                return packet.getPacket() instanceof SEntityHeadLookPacket;
            case "sentitymetadatapacket":
                return packet.getPacket() instanceof SEntityMetadataPacket;
            case "sentitypacket":
                return packet.getPacket() instanceof SEntityPacket;
            case "sentitypropertiespacket":
                return packet.getPacket() instanceof SEntityPropertiesPacket;
            case "sentitystatuspacket":
                return packet.getPacket() instanceof SEntityStatusPacket;
            case "sentityteleportpacket":
                return packet.getPacket() instanceof SEntityTeleportPacket;
            case "sentityvelocitypacket":
                return packet.getPacket() instanceof SEntityVelocityPacket;
            case "sexplosionpacket":
                return packet.getPacket() instanceof SExplosionPacket;
            case "shelditemchangepacket":
                return packet.getPacket() instanceof SHeldItemChangePacket;
            case "sjoingamepacket":
                return packet.getPacket() instanceof SJoinGamePacket;
            case "skeepalivepacket":
                return packet.getPacket() instanceof SKeepAlivePacket;
            case "smapdatapacket":
                return packet.getPacket() instanceof SMapDataPacket;
            case "smerchantofferspacket":
                return packet.getPacket() instanceof SMerchantOffersPacket;
            case "smountentitypacket":
                return packet.getPacket() instanceof SMountEntityPacket;
            case "smovevehiclepacket":
                return packet.getPacket() instanceof SMoveVehiclePacket;
            case "smultiblockchangepacket":
                return packet.getPacket() instanceof SMultiBlockChangePacket;
            case "sopenbookwindowpacket":
                return packet.getPacket() instanceof SOpenBookWindowPacket;
            case "sopenhorsewindowpacket":
                return packet.getPacket() instanceof SOpenHorseWindowPacket;
            case "sopensignmenupacket":
                return packet.getPacket() instanceof SOpenSignMenuPacket;
            case "sopenwindowpacket":
                return packet.getPacket() instanceof SOpenWindowPacket;
            case "splaceghostrecipepacket":
                return packet.getPacket() instanceof SPlaceGhostRecipePacket;
            case "splayentityeffectpacket":
                return packet.getPacket() instanceof SPlayEntityEffectPacket;
            case "splayerabilitiespacket":
                return packet.getPacket() instanceof SPlayerAbilitiesPacket;
            case "splayerdiggingpacket":
                return packet.getPacket() instanceof SPlayerDiggingPacket;
            case "splayerlistheaderfooterpacket":
                return packet.getPacket() instanceof SPlayerListHeaderFooterPacket;
            case "splayerlistitempacket":
                return packet.getPacket() instanceof SPlayerListItemPacket;
            case "splayerlookpacket":
                return packet.getPacket() instanceof SPlayerLookPacket;
            case "splayerpositionlookpacket":
                return packet.getPacket() instanceof SPlayerPositionLookPacket;
            case "splaysoundeffectpacket":
                return packet.getPacket() instanceof SPlaySoundEffectPacket;
            case "splaysoundeventpacket":
                return packet.getPacket() instanceof SPlaySoundEventPacket;
            case "splaysoundpacket":
                return packet.getPacket() instanceof SPlaySoundPacket;
            case "squerynbtresponsepacket":
                return packet.getPacket() instanceof SQueryNBTResponsePacket;
            case "srecipebookpacket":
                return packet.getPacket() instanceof SRecipeBookPacket;
            case "sremoveentityeffectpacket":
                return packet.getPacket() instanceof SRemoveEntityEffectPacket;
            case "srespawnpacket":
                return packet.getPacket() instanceof SRespawnPacket;
            case "sscoreboardobjectivepacket":
                return packet.getPacket() instanceof SScoreboardObjectivePacket;
            case "sselectadvancementstabpacket":
                return packet.getPacket() instanceof SSelectAdvancementsTabPacket;
            case "ssendresourcepackpacket":
                return packet.getPacket() instanceof SSendResourcePackPacket;
            case "sserverdifficultypacket":
                return packet.getPacket() instanceof SServerDifficultyPacket;
            case "ssetexperiencepacket":
                return packet.getPacket() instanceof SSetExperiencePacket;
            case "ssetpassengerspacket":
                return packet.getPacket() instanceof SSetPassengersPacket;
            case "ssetslotpacket":
                return packet.getPacket() instanceof SSetSlotPacket;
            case "sspawnexperienceorbpacket":
                return packet.getPacket() instanceof SSpawnExperienceOrbPacket;
            case "sspawnmobpacket":
                return packet.getPacket() instanceof SSpawnMobPacket;
            case "sspawnmovingsoundeffectpacket":
                return packet.getPacket() instanceof SSpawnMovingSoundEffectPacket;
            case "sspawnobjectpacket":
                return packet.getPacket() instanceof SSpawnObjectPacket;
            case "sspawnpaintingpacket":
                return packet.getPacket() instanceof SSpawnPaintingPacket;
            case "sspawnparticlepacket":
                return packet.getPacket() instanceof SSpawnParticlePacket;
            case "sspawnplayerpacket":
                return packet.getPacket() instanceof SSpawnPlayerPacket;
            case "sstatisticspacket":
                return packet.getPacket() instanceof SStatisticsPacket;
            case "sstopsoundpacket":
                return packet.getPacket() instanceof SStopSoundPacket;
            case "stabcompletepacket":
                return packet.getPacket() instanceof STabCompletePacket;
            case "stagslistpacket":
                return packet.getPacket() instanceof STagsListPacket;
            case "steampacket":
                return packet.getPacket() instanceof STeamsPacket;
            case "stitlepacket":
                return packet.getPacket() instanceof STitlePacket;
            case "sunloadchunkpacket":
                return packet.getPacket() instanceof SUnloadChunkPacket;
            case "supdatebossinfopacket":
                return packet.getPacket() instanceof SUpdateBossInfoPacket;
            case "supdatechunkpositionpacket":
                return packet.getPacket() instanceof SUpdateChunkPositionPacket;
            case "supdatehealthpacket":
                return packet.getPacket() instanceof SUpdateHealthPacket;
            case "supdatelightpacket":
                return packet.getPacket() instanceof SUpdateLightPacket;
            case "supdaterecipespacket":
                return packet.getPacket() instanceof SUpdateRecipesPacket;
            case "supdatescorepacket":
                return packet.getPacket() instanceof SUpdateScorePacket;
            case "supdatetileentitypacket":
                return packet.getPacket() instanceof SUpdateTileEntityPacket;
            case "supdatetimepacket":
                return packet.getPacket() instanceof SUpdateTimePacket;
            case "supdateviewdistancepacket":
                return packet.getPacket() instanceof SUpdateViewDistancePacket;
            case "swindowitemspacket":
                return packet.getPacket() instanceof SWindowItemsPacket;
            case "swindowpropertypacket":
                return packet.getPacket() instanceof SWindowPropertyPacket;
            case "sworldborderpacket":
                return packet.getPacket() instanceof SWorldBorderPacket;
            case "sworldspawnchangedpacket":
                return packet.getPacket() instanceof SWorldSpawnChangedPacket;
            default:
                return false;
        }
    }

    public boolean isClientPacket(String string) {
        switch (string.toLowerCase()) {
            // ... (previous cases)

            case "canimatehandpacket":
                return packet.getPacket() instanceof CAnimateHandPacket;
            case "cchatmessagepacket":
                return packet.getPacket() instanceof CChatMessagePacket;
            case "cclickwindowpacket":
                return packet.getPacket() instanceof CClickWindowPacket;
            case "cclientsettingspacket":
                return packet.getPacket() instanceof CClientSettingsPacket;
            case "cclientstatuspacket":
                return packet.getPacket() instanceof CClientStatusPacket;
            case "cclosewindowpacket":
                return packet.getPacket() instanceof CCloseWindowPacket;
            case "cconfirmteleportpacket":
                return packet.getPacket() instanceof CConfirmTeleportPacket;
            case "cconfirmtransactionpacket":
                return packet.getPacket() instanceof CConfirmTransactionPacket;
            case "ccreativeinventoryactionpacket":
                return packet.getPacket() instanceof CCreativeInventoryActionPacket;
            case "ccustompayloadpacket":
                return packet.getPacket() instanceof CCustomPayloadPacket;
            case "ceditbookpacket":
                return packet.getPacket() instanceof CEditBookPacket;
            case "cenchantitempacket":
                return packet.getPacket() instanceof CEnchantItemPacket;
            case "centityactionpacket":
                return packet.getPacket() instanceof CEntityActionPacket;
            case "chelditemchangepacket":
                return packet.getPacket() instanceof CHeldItemChangePacket;
            case "cinputpacket":
                return packet.getPacket() instanceof CInputPacket;
            case "cjigsawblockgeneratepacket":
                return packet.getPacket() instanceof CJigsawBlockGeneratePacket;
            case "ckeepalivepacket":
                return packet.getPacket() instanceof CKeepAlivePacket;
            case "clockdifficultypacket":
                return packet.getPacket() instanceof CLockDifficultyPacket;
            case "cmarkrecipeseenpacket":
                return packet.getPacket() instanceof CMarkRecipeSeenPacket;
            case "cmovevehiclepacket":
                return packet.getPacket() instanceof CMoveVehiclePacket;
            case "cpickitempacket":
                return packet.getPacket() instanceof CPickItemPacket;
            case "cplacerecipepacket":
                return packet.getPacket() instanceof CPlaceRecipePacket;
            case "cplayerabilitiespacket":
                return packet.getPacket() instanceof CPlayerAbilitiesPacket;
            case "cplayerdiggingpacket":
                return packet.getPacket() instanceof CPlayerDiggingPacket;
            case "cplayerpacket":
                return packet.getPacket() instanceof CPlayerPacket;
            case "cplayertryuseitemonblockpacket":
                return packet.getPacket() instanceof CPlayerTryUseItemOnBlockPacket;
            case "cplayertryuseitempacket":
                return packet.getPacket() instanceof CPlayerTryUseItemPacket;
            case "cqueryentitynbtpacket":
                return packet.getPacket() instanceof CQueryEntityNBTPacket;
            case "cquerytileentitynbtpacket":
                return packet.getPacket() instanceof CQueryTileEntityNBTPacket;
            case "crenameitempacket":
                return packet.getPacket() instanceof CRenameItemPacket;
            case "cresourcepackstatuspacket":
                return packet.getPacket() instanceof CResourcePackStatusPacket;
            case "cseenadvancementspacket":
                return packet.getPacket() instanceof CSeenAdvancementsPacket;
            case "cselecttradepacket":
                return packet.getPacket() instanceof CSelectTradePacket;
            case "csetdifficultypacket":
                return packet.getPacket() instanceof CSetDifficultyPacket;
            case "cspectatepacket":
                return packet.getPacket() instanceof CSpectatePacket;
            case "csteerboatpacket":
                return packet.getPacket() instanceof CSteerBoatPacket;
            case "ctabcompletepacket":
                return packet.getPacket() instanceof CTabCompletePacket;
            case "cupdatebeaconpacket":
                return packet.getPacket() instanceof CUpdateBeaconPacket;
            case "cupdatecommandblockpacket":
                return packet.getPacket() instanceof CUpdateCommandBlockPacket;
            case "cupdatejigsawblockpacket":
                return packet.getPacket() instanceof CUpdateJigsawBlockPacket;
            case "cupdateminecartcommandblockpacket":
                return packet.getPacket() instanceof CUpdateMinecartCommandBlockPacket;
            case "cupdaterecipebookstatuspacket":
                return packet.getPacket() instanceof CUpdateRecipeBookStatusPacket;
            case "cupdatesignpacket":
                return packet.getPacket() instanceof CUpdateSignPacket;
            case "cupdatestructureblockpacket":
                return packet.getPacket() instanceof CUpdateStructureBlockPacket;
            case "cuseentitypacket":
                return packet.getPacket() instanceof CUseEntityPacket;

            // ... (remaining cases)

            default:
                return false;
        }
    }
    public void cancel() {
        event.setCancel(true);
    }


    @Override
    public String getName() {
        return "packet_event";
    }
}
