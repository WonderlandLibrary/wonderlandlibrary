package net.minecraft.network.play.server;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.WorldSettings;

public class S38PacketPlayerListItem implements Packet {
   private final List players = Lists.newArrayList();
   private S38PacketPlayerListItem.Action action;
   private static volatile int[] $SWITCH_TABLE$net$minecraft$network$play$server$S38PacketPlayerListItem$Action;

   public void writePacketData(PacketBuffer var1) throws IOException {
      var1.writeEnumValue(this.action);
      var1.writeVarIntToBuffer(this.players.size());
      Iterator var3 = this.players.iterator();

      while(true) {
         while(var3.hasNext()) {
            S38PacketPlayerListItem.AddPlayerData var2 = (S38PacketPlayerListItem.AddPlayerData)var3.next();
            switch($SWITCH_TABLE$net$minecraft$network$play$server$S38PacketPlayerListItem$Action()[this.action.ordinal()]) {
            case 1:
               var1.writeUuid(var2.getProfile().getId());
               var1.writeString(var2.getProfile().getName());
               var1.writeVarIntToBuffer(var2.getProfile().getProperties().size());
               Iterator var5 = var2.getProfile().getProperties().values().iterator();

               while(var5.hasNext()) {
                  Property var4 = (Property)var5.next();
                  var1.writeString(var4.getName());
                  var1.writeString(var4.getValue());
                  if (var4.hasSignature()) {
                     var1.writeBoolean(true);
                     var1.writeString(var4.getSignature());
                  } else {
                     var1.writeBoolean(false);
                  }
               }

               var1.writeVarIntToBuffer(var2.getGameMode().getID());
               var1.writeVarIntToBuffer(var2.getPing());
               if (var2.getDisplayName() == null) {
                  var1.writeBoolean(false);
               } else {
                  var1.writeBoolean(true);
                  var1.writeChatComponent(var2.getDisplayName());
               }
               break;
            case 2:
               var1.writeUuid(var2.getProfile().getId());
               var1.writeVarIntToBuffer(var2.getGameMode().getID());
               break;
            case 3:
               var1.writeUuid(var2.getProfile().getId());
               var1.writeVarIntToBuffer(var2.getPing());
               break;
            case 4:
               var1.writeUuid(var2.getProfile().getId());
               if (var2.getDisplayName() == null) {
                  var1.writeBoolean(false);
               } else {
                  var1.writeBoolean(true);
                  var1.writeChatComponent(var2.getDisplayName());
               }
               break;
            case 5:
               var1.writeUuid(var2.getProfile().getId());
            }
         }

         return;
      }
   }

   public S38PacketPlayerListItem.Action func_179768_b() {
      return this.action;
   }

   public void processPacket(INetHandler var1) {
      this.processPacket((INetHandlerPlayClient)var1);
   }

   public void processPacket(INetHandlerPlayClient var1) {
      var1.handlePlayerListItem(this);
   }

   public S38PacketPlayerListItem(S38PacketPlayerListItem.Action var1, EntityPlayerMP... var2) {
      this.action = var1;
      EntityPlayerMP[] var6 = var2;
      int var5 = var2.length;

      for(int var4 = 0; var4 < var5; ++var4) {
         EntityPlayerMP var3 = var6[var4];
         this.players.add(new S38PacketPlayerListItem.AddPlayerData(this, var3.getGameProfile(), var3.ping, var3.theItemInWorldManager.getGameType(), var3.getTabListDisplayName()));
      }

   }

   static int[] $SWITCH_TABLE$net$minecraft$network$play$server$S38PacketPlayerListItem$Action() {
      int[] var10000 = $SWITCH_TABLE$net$minecraft$network$play$server$S38PacketPlayerListItem$Action;
      if (var10000 != null) {
         return var10000;
      } else {
         int[] var0 = new int[S38PacketPlayerListItem.Action.values().length];

         try {
            var0[S38PacketPlayerListItem.Action.ADD_PLAYER.ordinal()] = 1;
         } catch (NoSuchFieldError var6) {
         }

         try {
            var0[S38PacketPlayerListItem.Action.REMOVE_PLAYER.ordinal()] = 5;
         } catch (NoSuchFieldError var5) {
         }

         try {
            var0[S38PacketPlayerListItem.Action.UPDATE_DISPLAY_NAME.ordinal()] = 4;
         } catch (NoSuchFieldError var4) {
         }

         try {
            var0[S38PacketPlayerListItem.Action.UPDATE_GAME_MODE.ordinal()] = 2;
         } catch (NoSuchFieldError var3) {
         }

         try {
            var0[S38PacketPlayerListItem.Action.UPDATE_LATENCY.ordinal()] = 3;
         } catch (NoSuchFieldError var2) {
         }

         $SWITCH_TABLE$net$minecraft$network$play$server$S38PacketPlayerListItem$Action = var0;
         return var0;
      }
   }

   public S38PacketPlayerListItem(S38PacketPlayerListItem.Action var1, Iterable var2) {
      this.action = var1;
      Iterator var4 = var2.iterator();

      while(var4.hasNext()) {
         EntityPlayerMP var3 = (EntityPlayerMP)var4.next();
         this.players.add(new S38PacketPlayerListItem.AddPlayerData(this, var3.getGameProfile(), var3.ping, var3.theItemInWorldManager.getGameType(), var3.getTabListDisplayName()));
      }

   }

   public void readPacketData(PacketBuffer var1) throws IOException {
      this.action = (S38PacketPlayerListItem.Action)var1.readEnumValue(S38PacketPlayerListItem.Action.class);
      int var2 = var1.readVarIntFromBuffer();

      for(int var3 = 0; var3 < var2; ++var3) {
         GameProfile var4 = null;
         int var5 = 0;
         WorldSettings.GameType var6 = null;
         IChatComponent var7 = null;
         switch($SWITCH_TABLE$net$minecraft$network$play$server$S38PacketPlayerListItem$Action()[this.action.ordinal()]) {
         case 1:
            var4 = new GameProfile(var1.readUuid(), var1.readStringFromBuffer(16));
            int var8 = var1.readVarIntFromBuffer();
            int var9 = 0;

            for(; var9 < var8; ++var9) {
               String var10 = var1.readStringFromBuffer(32767);
               String var11 = var1.readStringFromBuffer(32767);
               if (var1.readBoolean()) {
                  var4.getProperties().put(var10, new Property(var10, var11, var1.readStringFromBuffer(32767)));
               } else {
                  var4.getProperties().put(var10, new Property(var10, var11));
               }
            }

            var6 = WorldSettings.GameType.getByID(var1.readVarIntFromBuffer());
            var5 = var1.readVarIntFromBuffer();
            if (var1.readBoolean()) {
               var7 = var1.readChatComponent();
            }
            break;
         case 2:
            var4 = new GameProfile(var1.readUuid(), (String)null);
            var6 = WorldSettings.GameType.getByID(var1.readVarIntFromBuffer());
            break;
         case 3:
            var4 = new GameProfile(var1.readUuid(), (String)null);
            var5 = var1.readVarIntFromBuffer();
            break;
         case 4:
            var4 = new GameProfile(var1.readUuid(), (String)null);
            if (var1.readBoolean()) {
               var7 = var1.readChatComponent();
            }
            break;
         case 5:
            var4 = new GameProfile(var1.readUuid(), (String)null);
         }

         this.players.add(new S38PacketPlayerListItem.AddPlayerData(this, var4, var5, var6, var7));
      }

   }

   public S38PacketPlayerListItem() {
   }

   public List func_179767_a() {
      return this.players;
   }

   public String toString() {
      return Objects.toStringHelper((Object)this).add("action", this.action).add("entries", this.players).toString();
   }

   public static enum Action {
      UPDATE_DISPLAY_NAME,
      REMOVE_PLAYER,
      UPDATE_GAME_MODE,
      UPDATE_LATENCY;

      private static final S38PacketPlayerListItem.Action[] ENUM$VALUES = new S38PacketPlayerListItem.Action[]{ADD_PLAYER, UPDATE_GAME_MODE, UPDATE_LATENCY, UPDATE_DISPLAY_NAME, REMOVE_PLAYER};
      ADD_PLAYER;
   }

   public class AddPlayerData {
      private final int ping;
      private final IChatComponent displayName;
      private final WorldSettings.GameType gamemode;
      final S38PacketPlayerListItem this$0;
      private final GameProfile profile;

      public String toString() {
         return Objects.toStringHelper((Object)this).add("latency", this.ping).add("gameMode", this.gamemode).add("profile", this.profile).add("displayName", this.displayName == null ? null : IChatComponent.Serializer.componentToJson(this.displayName)).toString();
      }

      public WorldSettings.GameType getGameMode() {
         return this.gamemode;
      }

      public GameProfile getProfile() {
         return this.profile;
      }

      public int getPing() {
         return this.ping;
      }

      public IChatComponent getDisplayName() {
         return this.displayName;
      }

      public AddPlayerData(S38PacketPlayerListItem var1, GameProfile var2, int var3, WorldSettings.GameType var4, IChatComponent var5) {
         this.this$0 = var1;
         this.profile = var2;
         this.ping = var3;
         this.gamemode = var4;
         this.displayName = var5;
      }
   }
}
