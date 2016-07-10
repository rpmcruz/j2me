// Minefield - (C) 2005-2012 Ricardo Cruz <ric8cruz@gmail.com>

package game;
import javax.microedition.rms.*;

// 1 - novice, 2 - medium, 3 - expert
public class Memory
{
	private RecordStore store;  // Record store, null if not open

	public Memory(String name, int records) {
		try { store = RecordStore.openRecordStore(name, true); }
		catch (RecordStoreException ex) { }

		// hack: if there are no entries, make space for them.
		// ugly, but ugly RecordStore is... :/
		byte [] array = { 0,0,0,0 };
		try {
			while(store.getNumRecords() < records)
				store.addRecord(array, 0, array.length);
		} catch (RecordStoreException ex) { System.err.println("Error: couldn't make space for memory."); }
	}

	public void close() {
		try { if (store != null) store.closeRecordStore(); }
		catch (RecordStoreException ex) { }
	}

	public int readKey(int key) {
		if(store != null) {
			byte [] score_barray = { 0,0,0,0 };
			try { score_barray = store.getRecord(key); }
			catch (RecordStoreException ex) {}
			return getInt(score_barray);
		}
		return -1;
	}

	// Save the score with a key assigned
	// Returns a boolean if this is a new highscore
	public void saveKey(int key, int score) {
		if(store != null) {
			byte[] barray = new byte[4];
			putInt(barray, score);
			try { store.setRecord(key, barray, 0, 4); }
			catch (RecordStoreException ex) { System.err.println("Error: couldn't save integer."); }
		}
    }

	private int getInt(byte[] buf) {
		return (buf[0] & 0xff) << 24 | (buf[1] & 0xff) << 16 |
			(buf[2] & 0xff) << 8 | (buf[3] & 0xff);
	}

	private void putInt(byte[] buf, int value) {
		buf[0] = (byte)((value >> 24) & 0xff);
		buf[1] = (byte)((value >> 16) & 0xff);
		buf[2] = (byte)((value >>  8) & 0xff);
		buf[3] = (byte)((value >>  0) & 0xff);
	}
}
