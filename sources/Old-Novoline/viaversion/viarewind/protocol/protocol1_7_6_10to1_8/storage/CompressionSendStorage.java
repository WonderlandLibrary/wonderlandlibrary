package viaversion.viarewind.protocol.protocol1_7_6_10to1_8.storage;

import java.util.Objects;
import viaversion.viaversion.api.data.StoredObject;
import viaversion.viaversion.api.data.UserConnection;

public class CompressionSendStorage extends StoredObject {

	private boolean compressionSend = false;

	public CompressionSendStorage(UserConnection user) {
		super(user);
	}

	public CompressionSendStorage(UserConnection user, boolean compressionSend) {
		super(user);
		this.compressionSend = compressionSend;
	}

	public boolean isCompressionSend() { return compressionSend; }
	public void setCompressionSend(boolean compressionSend) { this.compressionSend = compressionSend; }

	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(!(o instanceof CompressionSendStorage)) return false;

		CompressionSendStorage other = (CompressionSendStorage) o;
		return compressionSend == other.compressionSend;
	}

	@Override
	public int hashCode() {
		return Objects.hash(compressionSend);
	}

	@Override
	public String toString() {
		return "CompressionSendStorage{" + "compressionSend=" + compressionSend + '}';
	}
}
