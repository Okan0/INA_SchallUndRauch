package tim.FFT;

import java.util.Queue;

import org.jtransforms.fft.DoubleFFT_1D;

public class TransformForwardThread extends Thread {
	Queue<Integer> stream;
	public TransformForwardThread(Queue<Integer> stream)
	{
		super();
		this.stream = stream;
	}
	public void run()
	{
		/*
		 * Im folgenden wird die Library "JTransform" verwendet, um die Fast Fourier Transformation(FFT)
		 * auszuführen. Da hierfür ein Real und ein Immaginär-Teil benötigt wird, wir aber nur einen
		 * Realteil verwenden, wird ein double Array der doppelten Länge erstellt und die Imaginär-Teil immer
		 * 0 gesetzt.
		 * */
		double[] temp = new double[stream.size()*2];
		int i=0;
		DoubleFFT_1D transform = new DoubleFFT_1D(temp.length);
		for(Integer v : stream)
		{
			temp[i] = v;
			temp[i+1] = 0.0;
			i+=2;
		}
		transform.complexForward(temp);
		/*
		 * Im folgenden, wird das Ergebnis, welches nun in "temp" gespeichert ist, weiter verarbeitet
		 * */
		String retValue = "";
		
		for(i=0;i<temp.length;i+=2)
		{
			retValue+=((Double)temp[i]).toString() + ";";
		}
	}
}