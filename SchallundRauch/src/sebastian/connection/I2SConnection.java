package sebastian.connection;

import java.util.LinkedList;
import java.util.Queue;

import mraa.Dir;
import mraa.Gpio;
import mraa.Mode;
import mraa.mraa;
//import upm
public class I2SConnection {
	
	private static final int HIGH = 1;
	private static final int LOW = 0;
	
	private static final int I2S_CLK = 13; 
	private static final int I2S_RXD = 12; 
	private static final int I2S_TXD = 11; 
	private static final int I2S_FS = 10;  
	
	private Gpio I2S_RECEIVE;
	private Gpio I2S_TRANSMIT;
	private Gpio I2S_FREQUENCY;
	private Gpio I2S_CLOCK;
	
	private static final int BUFFERSIZE_100MSEC = 10;		//Signallength in 100ms
	private static final int BUFFERSIZE_BYTE = 1 * 3 * BUFFERSIZE_100MSEC;	//Size of Bytearray for inputstream
	
	
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
		this.I2S_TRANSMIT.dir(Dir.DIR_OUT);
		
		this.I2S_FREQUENCY = new Gpio(I2S_FS);
		this.I2S_FREQUENCY.mode(Mode.MODE_PULLUP);
		this.I2S_FREQUENCY.dir(Dir.DIR_OUT);
		
		this.I2S_CLOCK = new Gpio(I2S_CLK);
		this.I2S_CLOCK.mode(Mode.MODE_PULLUP);
		this.I2S_CLOCK.dir(Dir.DIR_IN);
		
		System.out.println(mraa.getPinCount());
		System.out.println("Initialize microphone... " + mraa.getPlatformName() + "...");
		System.out.println("\t...Clock Pin: " + mraa.getPinName(I2S_CLK));
		System.out.println("\t...Receive Data Pin: " + mraa.getPinName(I2S_RXD));
		System.out.println("\t...Transmit Data Pin: " + mraa.getPinName(I2S_TXD));
		System.out.println("\t...Frequency Select Pin: " + mraa.getPinName(I2S_FS));
		System.out.println("... done!");
	}
	
	public void read(){
		
		//read data and send to analyze
		int input = 0x00;				// Inputbyte
		Queue<Integer> inputstream = new LinkedList<Integer>();	//Whenever input received 8 bit, store byte in queue
		inputstream.add(input);
		boolean word = false;
		
//		while(true){				
//			I2S_FREQUENCY.write(HIGH);
//				for(int bit =0; bit<8; bit++){
//					input = (byte) (input << I2S_RECEIVE.read());
//				}
//			
//				if(inputstream.size() > BUFFERSIZE_BYTE){ 		//if queue has max_length, remove first byte
//					inputstream.remove();
//				}
//				inputstream.add(input);
//		}
		I2S_FREQUENCY.write(HIGH);
		for(int a = 0; a<2; a++){
		//while(true){
			I2S_TRANSMIT.write(HIGH);
			for(int i=0; i<8; i++){
				I2S_CLOCK.write(LOW);
				for(int j=0; j<3; j++){
					for(int bit =0; bit<8; bit++){
						I2S_CLOCK.write(HIGH);
						input =  (input << 1);
						input = input + I2S_RECEIVE.read();
						//input = (input + ((int)(Math.random() +0.5)));
						I2S_CLOCK.write(LOW);
					}
					if(inputstream.size() > BUFFERSIZE_BYTE){ 		//if queue has max_length, remove first byte
						inputstream.remove();
					}
					inputstream.add(input);
					input = 0;
					System.out.println(inputstream.toString());
				}
				I2S_CLOCK.write(HIGH);

			}
			System.out.println("\n");
			I2S_TRANSMIT.write(LOW);
		}

	}

}
