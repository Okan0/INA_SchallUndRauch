package tim.FFT;

import java.util.Queue;

import org.jtransforms.fft.DoubleFFT_1D;

import pl.edu.icm.jlargearrays.DoubleLargeArray;

public class TransformForwardThread extends Thread {
	Queue<Long> stream;
	public TransformForwardThread(Queue<Long> stream)
	{
		super();
		this.stream = stream;
	}
	public void run()
	{
		System.out.println("done");
		/*
		 * Im folgenden wird die Library "JTransform" verwendet, um die Fast Fourier Transformation(FFT)
		 * auszuführen. Da hierfür ein Real und ein Immaginär-Teil benötigt wird, wir aber nur einen
		 * Realteil verwenden, wird ein double Array der doppelten Länge erstellt und die Imaginär-Teil immer
		 * 0 gesetzt.
		 * */
		System.out.print("Initialize...");
		double[] temp = new double[stream.size()*2];
		int i=0;
		DoubleFFT_1D transform = new DoubleFFT_1D(temp.length/2);
		for(Long v : stream)
		{
			temp[i] = v;
			temp[i+1] = 0.0;
			i+=2;
		}
		System.out.println("done");
		
		System.out.print("Start Transformation...");
		try
		{
			transform.complexForward(temp);
			System.out.println("done");
			System.out.println("");
			System.out.println("Array:");
			for(i=0;i<temp.length;i++)
			{
				//sb.append(((Double)temp[i]).toString() + ";");
				
				System.out.print(((Double)temp[i]).toString() + (i%2==1?" j":";***;"));
				if(i%20 == 0)
				{
					System.out.println("");
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("Failed");
			System.out.println("Exception: ");
			e.printStackTrace();
		}
		
		for(double v : temp)
		{
			double offset0 = v*0.05;
			double offset1 = v*0.01;
			//TODO Offsets und Wert an Johnny übergeben
		}
		//Resultierende Frequenzen gegen Datenbank abfragen:
		////////////////////////////////////////////////////
		//Für jede einzelne Frequenz:
		//	Frequenz an eine Funktion übergeben, welche folgendes macht:
		//		Prozentuales Offset der Frequenz berechnen
		//		Folgende Datenbank abfrage verwenden:
		//			"SELECT * FROM tbl_frequencies hz WHERE hz.frequency <= " 
		//			+ freq.toString() + " AND hz.frequency >= "
		//			+ (freq - offset).toString() + ";"
		//		Überprüfen ob die Abfrage einen treffer hat
		//		Ja	 -> LED aufleuchten lassen(externe Funktion)
		//		Nein -> /
	}
}