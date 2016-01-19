package tim.FFT;

import java.util.LinkedList;
import java.util.Queue;

public class Test {
	public static void main(String[] args)
	{
		Queue<Long> q = new LinkedList<Long>();
		for(Long i=(long) 1;i<=128;i++)
		{
			q.add(i);
		}
		FFT_Wrapper.transform(q);
	}
}
