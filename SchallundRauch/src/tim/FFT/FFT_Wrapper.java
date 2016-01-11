package tim.FFT;

import java.util.Queue;

import org.jtransforms.fft.DoubleFFT_1D;

public class FFT_Wrapper {
	public static void transoform(Queue<Integer> stream)
	{
		TransformForwardThread tft = new TransformForwardThread(stream);
		tft.start();
	}
}
