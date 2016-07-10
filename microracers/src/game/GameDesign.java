/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.*;
import java.io.IOException;

/**
 * @author rick2
 */
public class GameDesign {
	
//<editor-fold defaultstate="collapsed" desc=" Generated Fields ">//GEN-BEGIN:|fields|0|
private Image tiles;
private Sprite redcar;
public int redcarseq001Delay = 200;
public int[] redcarseq001 = {32, 33, 34, 35, 36};
private Sprite bluecar;
public int bluecarseq001Delay = 200;
public int[] bluecarseq001 = {37, 38, 39, 40, 41};
private TiledLayer Road;
private Sprite bluecar2;
public int bluecar2seq001Delay = 200;
public int[] bluecar2seq001 = {37, 38, 39, 40, 41};
//</editor-fold>//GEN-END:|fields|0|
	
//<editor-fold defaultstate="collapsed" desc=" Generated Methods ">//GEN-BEGIN:|methods|0|
//</editor-fold>//GEN-END:|methods|0|

public Image getTiles() throws java.io.IOException {//GEN-BEGIN:|1-getter|0|1-preInit
if (tiles == null) {//GEN-END:|1-getter|0|1-preInit
 // write pre-init user code here
tiles = Image.createImage("/tiles.png");//GEN-BEGIN:|1-getter|1|1-postInit
}//GEN-END:|1-getter|1|1-postInit
 // write post-init user code here
return this.tiles;//GEN-BEGIN:|1-getter|2|
}//GEN-END:|1-getter|2|











public Sprite getRedcar() throws java.io.IOException {//GEN-BEGIN:|3-getter|0|3-preInit
if (redcar == null) {//GEN-END:|3-getter|0|3-preInit
 // write pre-init user code here
redcar = new Sprite(getTiles(), 16, 16);//GEN-BEGIN:|3-getter|1|3-postInit
redcar.setFrameSequence(redcarseq001);//GEN-END:|3-getter|1|3-postInit
 // write post-init user code here
}//GEN-BEGIN:|3-getter|2|
	return redcar;
}//GEN-END:|3-getter|2|








public Sprite getBluecar() throws java.io.IOException {//GEN-BEGIN:|5-getter|0|5-preInit
if (bluecar == null) {//GEN-END:|5-getter|0|5-preInit
 // write pre-init user code here
bluecar = new Sprite(getTiles(), 16, 16);//GEN-BEGIN:|5-getter|1|5-postInit
bluecar.setFrameSequence(bluecarseq001);//GEN-END:|5-getter|1|5-postInit
 // write post-init user code here
}//GEN-BEGIN:|5-getter|2|
	return bluecar;
}//GEN-END:|5-getter|2|







public TiledLayer getRoad() throws java.io.IOException {//GEN-BEGIN:|8-getter|0|8-preInit
if (Road == null) {//GEN-END:|8-getter|0|8-preInit
 // write pre-init user code here
Road = new TiledLayer(21, 21, getTiles(), 16, 16);//GEN-BEGIN:|8-getter|1|8-midInit
int[][] tiles = {
{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
{ 1, 1, 1, 1, 11, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
{ 1, 1, 1, 2, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 3, 1, 1, 1 },
{ 1, 1, 1, 6, 21, 17, 17, 10, 10, 10, 17, 17, 17, 17, 17, 17, 18, 7, 1, 1, 1 },
{ 1, 1, 1, 6, 17, 17, 9, 9, 9, 9, 9, 9, 9, 9, 9, 17, 17, 7, 1, 1, 1 },
{ 1, 1, 1, 6, 17, 7, 1, 1, 1, 1, 1, 1, 1, 1, 1, 6, 17, 7, 1, 1, 1 },
{ 1, 1, 1, 6, 17, 7, 1, 1, 1, 1, 1, 1, 1, 1, 1, 6, 17, 7, 1, 1, 1 },
{ 1, 1, 1, 12, 17, 7, 1, 1, 1, 1, 1, 1, 1, 1, 11, 6, 17, 7, 1, 1, 1 },
{ 1, 1, 1, 12, 17, 7, 1, 1, 1, 1, 1, 1, 1, 1, 1, 6, 17, 7, 1, 1, 1 },
{ 11, 1, 1, 12, 17, 7, 1, 1, 1, 1, 1, 1, 1, 1, 1, 6, 17, 7, 1, 11, 1 },
{ 1, 1, 1, 6, 17, 7, 1, 1, 11, 1, 1, 1, 1, 1, 1, 6, 17, 7, 1, 1, 1 },
{ 1, 1, 1, 6, 17, 7, 1, 1, 1, 1, 1, 1, 1, 1, 1, 6, 17, 7, 1, 1, 1 },
{ 1, 1, 1, 6, 17, 7, 1, 1, 1, 1, 1, 1, 1, 1, 1, 6, 17, 7, 1, 1, 1 },
{ 1, 1, 1, 6, 17, 7, 1, 1, 1, 1, 1, 1, 1, 1, 1, 6, 17, 7, 1, 1, 1 },
{ 1, 1, 1, 6, 17, 17, 8, 8, 8, 8, 14, 14, 14, 8, 8, 17, 17, 7, 1, 1, 1 },
{ 1, 1, 1, 6, 20, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 19, 7, 1, 1, 1 },
{ 1, 1, 1, 4, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 5, 1, 1, 1 },
{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
{ 1, 11, 1, 1, 1, 1, 1, 1, 1, 11, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 }
};//GEN-END:|8-getter|1|8-midInit
 // write mid-init user code here
for (int row = 0; row < 21; row++) {//GEN-BEGIN:|8-getter|2|8-postInit
for (int col = 0; col < 21; col++) {
Road.setCell(col, row, tiles[row][col]);
}
}
}//GEN-END:|8-getter|2|8-postInit
 // write post-init user code here
	return Road;//GEN-BEGIN:|8-getter|3|
}//GEN-END:|8-getter|3|






public void updateLayerManagerForCircle(LayerManager lm) throws java.io.IOException {//GEN-LINE:|9-updateLayerManager|0|9-preUpdate
 // write pre-update user code here
getBluecar2().setPosition(81, 75);//GEN-BEGIN:|9-updateLayerManager|1|9-postUpdate
getBluecar2().setVisible(true);
lm.append(getBluecar2());
getBluecar().setPosition(98, 75);
getBluecar().setVisible(true);
lm.append(getBluecar());
getRedcar().setPosition(99, 59);
getRedcar().setVisible(true);
lm.append(getRedcar());
getRoad().setPosition(2, 3);
getRoad().setVisible(true);
lm.append(getRoad());//GEN-END:|9-updateLayerManager|1|9-postUpdate
 // write post-update user code here
}//GEN-LINE:|9-updateLayerManager|2|


public Sprite getBluecar2() throws java.io.IOException {//GEN-BEGIN:|38-getter|0|38-preInit
if (bluecar2 == null) {//GEN-END:|38-getter|0|38-preInit
 // write pre-init user code here
bluecar2 = new Sprite(getTiles(), 16, 16);//GEN-BEGIN:|38-getter|1|38-postInit
bluecar2.setFrameSequence(bluecar2seq001);//GEN-END:|38-getter|1|38-postInit
 // write post-init user code here
}//GEN-BEGIN:|38-getter|2|
	return bluecar2;
}//GEN-END:|38-getter|2|







	
}
