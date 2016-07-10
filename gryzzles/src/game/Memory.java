// Gryzzles J2ME - (C) 2011-2012 Ricardo Cruz <ric8cruz@gmail.com>

package game;
import javax.microedition.rms.*;

/*
 * Pass the number of integers values to save in constructor.
 * The keys are then in the range [1,records]
 */

public class Memory
{
	private RecordStore store;  // Record store, null if not open

	public Memory(String name, int records) {
		try { store = RecordStore.openRecordStore(name, true); }
		catch (RecordStoreException ex) {}

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
			byte [] barray = { 0,0,0,0 };
			try { barray = store.getRecord(key); }
			catch (RecordStoreException ex) {}
			return getInt(barray);
		}
		return 0;
	}

	// Save the score with a key assigned
	public void saveKey(int key, int value) {
		if(store != null) {
			byte[] barray = new byte[4];
			putInt(barray, value);
			try {
				store.setRecord(key, barray, 0, 4);
			}
			catch (RecordStoreException ex) { System.err.println("Error: couldn't save integer - " + ex.toString()); }
		}
    }

	private static int getInt(byte[] buf) {
		return (buf[0] & 0xff) << 24 | (buf[1] & 0xff) << 16 |
			(buf[2] & 0xff) << 8 | (buf[3] & 0xff);
	}

	private static void putInt(byte[] buf, int value) {
		buf[0] = (byte)((value >> 24) & 0xff);
		buf[1] = (byte)((value >> 16) & 0xff);
		buf[2] = (byte)((value >>  8) & 0xff);
		buf[3] = (byte)((value >>  0) & 0xff);
	}
}
