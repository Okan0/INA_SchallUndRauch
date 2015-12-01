package sebastian.connection;

import mraa.Platform;
import mraa.Result;
import mraa.mraa;
import mraa.*;
//import upm
public class I2SConnection {
	
	private static final int I2S_CLK = 13; 
	private static final int I2S_RXD = 12; 
	private static final int I2S_TXD = 11; 
	private static final int I2S_FS = 10;  
	
	private Gpio I2S_RECEIVE;
	private Gpio I2S_TRANSMIT;
	private Gpio I2S_FREQUENCY;
	private Gpio I2S_CLOCK;
	
	
	public I2SConnection(){
		this.init();
	}
	
	private void init(){
		
		try {
			System.loadLibrary("mraajava");
		} catch (UnsatisfiedLinkError e) {
			System.err.println("Native code library failed to load. See the chapter on "
			                    + "Dynamic Linking Problems in the SWIG Java documentation for help.\n" + e);
		}
		
		this.I2S_RECEIVE = new Gpio(I2S_RXD);
		this.I2S_RECEIVE.mode(Mode.MODE_PULLUP);
		this.I2S_RECEIVE.dir(Dir.DIR_IN);
		
		this.I2S_TRANSMIT = new Gpio(I2S_TXD);
		this.I2S_TRANSMIT.mode(Mode.MODE_PULLUP);
		this.I2S_TRANSMIT.dir(Dir.DIR_IN);
		
		this.I2S_FREQUENCY = new Gpio(I2S_FS);
		this.I2S_FREQUENCY.mode(Mode.MODE_PULLUP);
		this.I2S_FREQUENCY.dir(Dir.DIR_IN);
		
		this.I2S_CLOCK = new Gpio(I2S_CLK);
		this.I2S_CLOCK.mode(Mode.MODE_PULLUP);
		this.I2S_CLOCK.dir(Dir.DIR_IN);
		
		System.out.println(mraa.getPinCount());
		System.out.println("Initalisiere Schnittstelle " + mraa.getPlatformName() + "...");
		System.out.println("\t...Clock Pin: " + mraa.getPinName(I2S_CLK));
		System.out.println("\t...Receive Data Pin: " + mraa.getPinName(I2S_RXD));
		System.out.println("\t...Transmit Data Pin: " + mraa.getPinName(I2S_TXD));
		System.out.println("\t...Frequency Select Pin: " + mraa.getPinName(I2S_FS));
		System.out.println("... finished!");
	}
	
	public Byte[] read(){
		
		//Hier Mikrofon auslesen und empfangene Daten an Analyse schicken
		
		
		int input;
		Byte[] xy = null;
		input = I2S_RECEIVE.read();
		
		return xy;
	}

}
