package mpd;

import com.sun.org.apache.xalan.internal.xsltc.util.IntegerArray;

import java.lang.reflect.Array;
import java.util.List;

public class ThreadedMinimumPairwiseDistance implements MinimumPairwiseDistance {

    private int numThreads;
    private int begin;
    private int end;
    private int[] dist;

    public ThreadedMinimumPairwiseDistance(int numThreads) {
        this.numThreads = numThreads;
    }

    private ThreadedMinimumPairwiseDistance(int[] dist, int begin, int end) {
        this.dist = dist;
        this.begin = begin;
        this.end = end;
    }

    @Override
    public long minimumPairwiseDistance(int[] values)  throws InterruptedException{
//        throw new UnsupportedOperationException();

        int splicer = dist.length/numThreads;

        Thread[] threads = new Thread[numThreads];  // Create and start a bunch of threads

        long result = Integer.MAX_VALUE; // java's positive infinity

        for (int i = 0; i < values.length; ++i) {
            for (int j = 0; j < i; ++j) {
                // Gives us all the pairs (i, j) where 0 <= j < i < values.length
                //long diff = Math.abs(values[i] - values[j]);

                difference();

                threads[i] = new Thread(ThreadedMinimumPairwiseDistance(dist, ));



            }
        }

        for (int i=0; i<numThreads; ++i) { // Wait for all the threads to finish
            threads[i].join();
        }

        return result;
    }

    public void run() {}

    public void difference (int value1, int value2, long result, int[] values){
        int diff = Math.abs(values[value1] - values[value2]);

        if(diff < result) {result = diff;}
    }

}


