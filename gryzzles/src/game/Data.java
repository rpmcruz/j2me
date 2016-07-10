// Gryzzles J2ME - (C) 2011-2012 Ricardo Cruz <ric8cruz@gmail.com>

/*
 * The levels data. Copied over from KGryzzles, a KDE1 adaption
 * of the Atari game. The KGryzzles data was itself copied over
 * from the Atari game.
 * 
 * The original game and levels data was written by Ekkehard Hess.
 */

package game;

public class Data {
	public static final int COLS = 16, ROWS = 10;
	
	public static final int Data[][] = {
{65535,36849,36849,65535,64575,64575,65535,36849,36849,65535},
{13932,15996,15996,65535,58983,58983,65535,15996,15996,13932},
{32766,65535,46125,46701,42597,42597,46701,46125,65535,32766},
{2016,32766,32382,59367,58983,58983,59367,32382,32766,2016},
{65535,65535,37833,37449,65151,65151,37449,37833,65535,65535},
{29646,32382,65151,65151,65151,65151,32766,32190,7608,7224},
{65535,58983,58983,65535,39321,39321,65535,58983,58983,65535},
{65535,40953,40569,65151,61455,61455,65151,40569,40953,65535},
{32766,65535,46029,48093,47709,33345,65535,64575,62415,32766},
{58983,65535,52275,64959,51171,51171,64959,52275,65535,65535},
{32382,65535,55707,65535,58311,50787,52275,59799,65535,32766},
{8184,32190,29070,65535,57735,65535,61838,32190,8184,0},
{63471,39993,47085,65535,57927,57927,65535,47085,39993,63471},
{13287,31727,64990,65532,31726,13935,28671,65534,64956,26392},
{7742,32639,56815,63422,30588,32252,61406,65535,32479,13302},
{32766,32767,56567,65527,65511,32631,65407,63481,51167,65535},
{65535,49539,62415,65535,50787,50787,65535,62415,49539,65535},
{65535,33153,49149,48573,65151,65151,48573,49149,33153,65535},
{65535,65535,64575,59367,50115,50115,59367,64575,65535,65535},
{32382,65535,56283,65535,63903,65535,55323,65535,65151,32766},
{57116,57119,57343,65507,65519,65535,65531,63463,50927,65230},
{65535,65401,51191,56823,56831,64638,65022,59270,65534,32508},
{32638,61902,63483,32379,56190,62335,47871,65470,32766,16094},
{32728,65534,48863,46717,63479,32758,65406,65407,14335,7615},
{12536,64508,56158,24575,32671,64895,57343,32766,63454,62238},
{64991,65535,55995,32764,14206,57342,57271,63359,65503,62364},
{15559,32751,32702,60671,65023,57334,63166,32511,64479,64271},
{32191,62959,63454,65407,47055,65278,64503,49023,63467,32187},
{64575,65151,65535,56763,15996,15996,56763,65535,65151,64575},
{32766,64575,34785,61431,60471,60471,61431,34785,64575,32766},
{15342,65455,65533,64503,47102,60890,57342,63471,65531,62462},
{16190,65019,65519,64379,65535,55997,65527,44414,49135,65415},
{13104,31676,65516,65518,31726,32383,64377,63951,32703,16383},
{16256,25543,57215,49147,49135,55758,26591,65343,60922,65502},
{32734,65407,65023,30646,57343,64959,65275,65391,62975,56815},
{13279,63231,65467,65535,16351,28150,65518,43899,65527,30719},
{32766,65151,65151,65535,65151,64575,63519,61455,65535,65535},
{65467,55023,32767,65399,62431,63483,56815,32503,65407,65019},
{4060,16255,32767,55294,64883,65503,57343,32238,16251,13279},
{52460,57326,62463,65527,28031,65503,60862,65150,57340,49592},
{32480,32752,8056,32638,32254,60863,65439,32758,31228,24696},
{57139,65023,57295,30714,65022,57215,30685,60791,65535,56319},
{32239,56767,63483,32558,60911,63195,31550,57327,63455,65020},
{61839,63903,65535,32766,13260,15324,32766,65535,63903,61839},
{15999,32603,56798,64503,61183,48555,61438,64239,64443,65534},
{4080,8184,15420,32766,65151,65151,32766,15420,8184,4080},
{51171,53235,57339,65535,57339,57339,65535,57339,53235,51171},
{32752,65528,65532,65534,65535,65535,65535,32767,16382,8188},
{64575,65151,65535,32766,15996,15996,32766,65535,65151,64575},
{2016,4080,8184,16380,32766,32766,16380,8184,4080,2016},
		};

	public static final byte DataPos[][] = {
		{1,9},{5,2},{15,9},{1,7},{9,8},{9,9},{16,10},{8,10},{16,8},{9,5},{2,1},{1,6},{1,6},{3,1},{5,2},{6,1},{1,9},{1,4},{1,10},{10,1},{11,5},{3,8},{3,6},{5,6},{2,10},{12,3},{12,4},{15,3},{16,10},{3,9},{10,8},{16,5},{2,9},{7,3},{5,3},{9,6},{9,4},{3,2},{3,3},{9,5},{5,6},{14,4},{1,3},{8,10},{14,9},{1,6},{8,6},{10,5},{1,10},{9,6}
	};
}