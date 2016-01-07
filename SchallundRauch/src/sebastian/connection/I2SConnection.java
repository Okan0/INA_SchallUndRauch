package sebastian.connection;

import java.util.LinkedList;
import java.util.Queue;

import tim.FFT.Complex;
import tim.FFT.FFT;
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
	
	private static final int RESOLUTION = 200;
	private static final int BUFFERSIZE_MSEC = 500;		//Signallength in 100ms
	private static final int BUFFERSIZE_BYTE = RESOLUTION * 3 * BUFFERSIZE_MSEC/1000;	//Size of Bytearray for inputstream
	private static final int TIME_BETWEEN_ANALYSIS = 500; //Zeit in ms zwischen zwei analysen
	private static final int READCYCLES_BETWEEN_ANALYSIS = RESOLUTION * TIME_BETWEEN_ANALYSIS/1000;	//Lesevorg�nge bis zum abschicken an analyse, jeweils 3 Byte werden gelesen
	
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
		
		I2S_FREQUENCY.write(HIGH);
		while(true){
			I2S_TRANSMIT.write(HIGH);
			for(int i=0; i<READCYCLES_BETWEEN_ANALYSIS; i++){ //Schleife bis einmal Analyse aufgerufen wird
				for(int j=0; j<3; j++){ //Schleife f�r 3 Byte
					for(int bit =0; bit<8; bit++){ //Schleife f�r 8 bit
						input = input << 1;
						//input = input + I2S_RECEIVE.read();
						input = (input + ((int)(Math.random() +0.5)));
					}
					if(inputstream.size() > BUFFERSIZE_BYTE){ 		//if queue has max_length, remove first byte
						inputstream.remove();
					}
					inputstream.add(input);
					input = 0;
					System.out.println(inputstream.toString());
				}

			}
			System.out.println("\n");
			I2S_TRANSMIT.write(LOW);
			//send to FFT
			
			//FFT.FFT((Complex[]) inputstream.toArray(), -1);
		
		}
	}
}
