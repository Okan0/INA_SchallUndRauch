package tim.FFT;

import java.util.LinkedList;
import java.util.Queue;

public class Test {
	public static void main(String[] args)
	{
		Queue<Integer> q = new LinkedList<Integer>();
		for(int i=1;i<=128;i++)
		{
			q.add(i);
		}
		FFT_Wrapper.transoform(q);
	}
}
