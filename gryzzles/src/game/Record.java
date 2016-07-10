// Gryzzles (J2ME) - (C) 2011 Ricardo Cruz <rpmcruz0@gmail.com>

package game;
import javax.microedition.rms.*;

public class Record
{
	public static int readLevel() {
		try {
			RecordStore store;
			store = RecordStore.openRecordStore("Gryzzles", true);
			if(store.getNumRecords() > 0) {
				byte [] level = store.getRecord(0);
				store.closeRecordStore();
				return level[0];
			}
		} catch (Exception ex) {}
		return 0;
	}

	public static void saveLevel(int level) {
		try {
			RecordStore store;
			store = RecordStore.openRecordStore("Gryzzles", true);
			byte [] data = { (byte)level };
			if(store.getNumRecords() == 0)
				store.addRecord(data, 0, 1);
			else
				store.setRecord(0, data, 0, 1);
			store.closeRecordStore();
		} catch (Exception ex) {}
	}
}
