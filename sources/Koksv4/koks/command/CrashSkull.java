package koks.command;

import com.mojang.authlib.properties.Property;
import jakarta.xml.bind.DatatypeConverter;
import koks.api.registry.command.Command;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author kroko
 * @created on 01.02.2021 : 04:04
 */

@Command.Info(name = "crashskull", aliases = {"cskull"}, description = "You get a crash skull")
public class CrashSkull extends Command {

    /* Worked atm nicht */
    @Override
    public boolean execute(String[] args) {
        final ItemStack itemStack = createItemStack(Item.getIdFromItem(Items.skull), 1, (byte)3);
        final NBTTagCompound base = new NBTTagCompound();
        final NBTTagCompound properties = new NBTTagCompound();
        final NBTTagList textureList = new NBTTagList();
        final NBTTagCompound textures = new NBTTagCompound();

        String signature = "";
        String value = "";
        for (String s : getPlayer().getGameProfile().getProperties().keySet()) {
            for (Property property : getPlayer().getGameProfile().getProperties().get(s)) {
                if(property.hasSignature())
                    signature = property.getSignature();
                value = property.getValue();
            }
        }

        String decodedValue = new String(Base64.getDecoder().decode(value));
        final String[] split = decodedValue.split("//");
        String url = split[split.length - 1];
        int substring = 0;
        for(int i = 0; i < url.length(); i++) {
            if(url.substring(i, i+1).equalsIgnoreCase(","))
                substring = i - 1;
        }
        url = url.substring(0, substring);
        decodedValue = decodedValue.replace(url, "");
        decodedValue = decodedValue.replace("http://", "");

        final String encodedValue = DatatypeConverter.printBase64Binary(decodedValue.getBytes(StandardCharsets.UTF_8));

        base.setString("id", getPlayer().getUniqueID().toString());
        base.setString("Name", getPlayer().getName());
        base.setString("SkullOwner", getPlayer().getName());
        textures.setString("Signature", signature);
        textures.setString("Value", encodedValue);
        textureList.appendTag(textures);
        properties.setTag("textures", textureList);
        base.setTag("Properties", properties);
        itemStack.setTagCompound(base);
        giveItem(itemStack);
        return true;
    }
}
