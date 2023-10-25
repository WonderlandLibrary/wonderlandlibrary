package nl.matsv.viabackwards.api.entities.meta;

import nl.matsv.viabackwards.api.exceptions.RemovedValueException;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;

public interface MetaHandler {
  Metadata handle(MetaHandlerEvent paramMetaHandlerEvent) throws RemovedValueException;
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\api\entities\meta\MetaHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */