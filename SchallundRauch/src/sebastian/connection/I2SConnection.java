package sebastian.connection;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import tim.FFT.FFT_Wrapper;

import mraa.Dir;
import mraa.Edge;
import mraa.Gpio;
import mraa.IsrCallback;
import mraa.Mode;
import mraa.Spi;
import mraa.Spi_Mode;
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
	
	/*
	* ein Signal besteht aus 4 Byte, wobei das letzte Byte 0x00 ist (24Bit Auflösung)
	* ein Wort ist 64 Bit lang ( 4Byte für rechts, 4 Byte für links )
	* die Frequenz beträgt 44,1kHz
	* Taktfrequenz der Clock muss daher betragen:44100*64= 2822400MHz
	*
	*/
	
	
	private static final int RESOLUTION = 44 * 8;	//44,1kHz * 8 Byte (64Bit)Wordlength
	private static final int BUFFERSIZE_MSEC = 2000;	//Signallength in 100ms
	
	//es werden immer 4 Byte in einem Integer gespeichert, daher RESOLUTION/4
	
	private static final int BUFFERSIZE_INTEGER = RESOLUTION/4 * BUFFERSIZE_MSEC/1000;	//Size of Queue<Integer> for inputstream
	private static final int TIME_BETWEEN_ANALYSIS = 2000; //Zeit in ms zwischen zwei analysen
	private static final int READCYCLES_BETWEEN_ANALYSIS = RESOLUTION * TIME_BETWEEN_ANALYSIS/1000;	//Lesevorgänge bis zum abschicken an analyse, jeweils 3 Byte werden gelesen
	
	public Queue<Long> inputstream = new LinkedList<Long>();	//Whenever input received 8 bit, store byte in queue
	public int count = 0;
	public Long input = (long) 0x00;				// Input
	public int WORD_TRIGGER = 0;
	
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
		this.I2S_CLOCK.dir(Dir.DIR_OUT);
		
		
		System.out.println(mraa.getPinCount());
		System.out.println("Initialize microphone... " + mraa.getPlatformName() + "...");
		System.out.println("\t...Clock Pin: " + mraa.getPinName(I2S_CLK));
		System.out.println("\t...Receive Data Pin: " + mraa.getPinName(I2S_RXD));
		System.out.println("\t...Transmit Data Pin: " + mraa.getPinName(I2S_TXD));
		System.out.println("\t...Frequency Select Pin: " + mraa.getPinName(I2S_FS));
		System.out.println("... done!");	
		
		
		//set clock speed
		Spi spi = new Spi(I2S_CLK);
		spi.frequency(RESOLUTION*8);	//Resolution ist in byte angegeben, aus byte bit machen
		spi.bitPerWord(64);
		spi.lsbmode(false);
		
		//set wordselectspeed = clockspeed/64
		Spi wordselect = new Spi(I2S_FS);
		wordselect.frequency(RESOLUTION/8);	//Wordclock auf ein 64tel der CLK 
	}
//	
//	public void read(){
//		IsrCallback callback = new ClockCallback();
//		
//		while(true){
//			I2S_CLOCK.isr(Edge.EDGE_RISING,  callback);
//		}
//	}
//	
//	
//public class ClockCallback extends IsrCallback
//{
//	public ClockCallback(){
//		super();
//		System.out.println("Callback erstellt");
//	}
//	
//	public void run(){
//		
//		count++;
//		System.out.println(count);
//		if(count < 32){
//			input = input << 1;
//			input += (int) (Math.random() + 0.5);
//			// input = I2S_RECEIVE.read();
//		}
//		if(count == 31){
//			if(inputstream.size() >= BUFFERSIZE_INTEGER){
//				inputstream.remove();
//			}
//			inputstream.add(input);
//			input = 0;
//			count = 0;
//			WORD_TRIGGER = 1-WORD_TRIGGER;
//			I2S_FREQUENCY.write(WORD_TRIGGER);
//		}
//		
		
	public void read(){
		while(true){
			I2S_TRANSMIT.write(HIGH);
			for(int i=0; i<READCYCLES_BETWEEN_ANALYSIS; i++){ //Schleife bis einmal Analyse aufgerufen wird
				for(int word=0; word<2; word++){ //ein wort is 64 (2*32) Bit lang
					//4 Bytes hintereinander in einen Integer
					//MSB first
					for(int bit =0; bit<32; bit++){ //Schleife für 32 bit,
						input = input << 1;
						//input = input + I2S_RECEIVE.read();
						if(bit<24){
							input = (input + ((int)(Math.random() +0.5)));
						}
					}//end bit
					if(inputstream.size() > BUFFERSIZE_INTEGER){ 		//if queue has max_length, remove first byte
						inputstream.remove();
					}
					inputstream.add(input);
					input = (long) 0;
//					System.out.printf("0x%x\n",inputstream.toArray()[0]);
//					System.out.println(inputstream.toString());
				
				//wordselect setzen
				
				}// end word

			}
//			System.out.println("\n");
			I2S_TRANSMIT.write(LOW);
			
			//remove 0x00 Bytes before sending to analysis
			Queue<Long> sendToAnalysis = inputstream;
			ArrayList<Integer> zero = new ArrayList<Integer>(1);
			zero.add(0);
			sendToAnalysis.removeAll(zero);
			
			//send to FFT
			FFT_Wrapper.transform(sendToAnalysis);
		}
	}
}
