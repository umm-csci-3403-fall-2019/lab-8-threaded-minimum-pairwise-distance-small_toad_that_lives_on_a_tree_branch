package mpd;

import com.sun.org.apache.xalan.internal.xsltc.util.IntegerArray;

import javax.xml.transform.Result;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class ThreadedMinimumPairwiseDistance implements MinimumPairwiseDistance, Runnable {

    private int numThreads;
    private int halfN;
    private int N;
    private int marker;
    private int[] values;
    private long result = Integer.MAX_VALUE;

    private static int thread1Result;
    private static int thread2Result;
    private static int thread3Result;
    private static int thread4Result;

    public ThreadedMinimumPairwiseDistance() {
        this.numThreads = 4;
    }

    private ThreadedMinimumPairwiseDistance(int[] values, int halfN, int N, int marker) {
        this.values = values;
        this.halfN = halfN;
        this.N = N;
        this.marker = marker;
    }

    @Override
    public long minimumPairwiseDistance(int[] values)  { //throws InterruptedException --> may need
        int val = values.length;
        //Result sharedResult = new Result();

        Thread[] threads = new Thread[numThreads];  // Create and start a bunch of threads

        ThreadedMinimumPairwiseDistance bottomLeft = new ThreadedMinimumPairwiseDistance(values, val/2, val, 0);
        threads[0] = new Thread(bottomLeft);
        threads[0].start();

        ThreadedMinimumPairwiseDistance bottomRight = new ThreadedMinimumPairwiseDistance(values, val/2, val, 1);
        threads[1] = new Thread(bottomRight);
        threads[1].start();

        ThreadedMinimumPairwiseDistance center = new ThreadedMinimumPairwiseDistance(values, val/2, val, 2);
        threads[2] = new Thread(center);
        threads[2].start();

        ThreadedMinimumPairwiseDistance top = new ThreadedMinimumPairwiseDistance(values, val/2, val, 3);
        threads[3] = new Thread(top);
        threads[3].start();
        try {
            for (int i=0; i<numThreads; ++i) { // Wait for all the threads to finish
                threads[i].join();
            }
        } catch(InterruptedException e){
            System.out.println(e);
        }

        int[] threadResults = {thread1Result, thread2Result, thread3Result, thread4Result};
        System.out.println("Array of results: " + Arrays.toString(threadResults));
        Arrays.sort(threadResults);
        System.out.println("Array of sorted results: " + Arrays.toString(threadResults));
        System.out.println("Final result = " + threadResults[0]);

        return threadResults[0];
    }

    public void run(){
        if(marker == 0){ // handles pairs in the ranges of 0 <= J < I < N/2 and N/2 <= J < I < N
            int localResult = Integer.MAX_VALUE;
            for (int i=0; i < halfN; i++) {
                for (int j=0; j < i; j++) {
                    int diff = Math.abs(values[i] - values[j]);
                    if(diff < localResult){
                        localResult = diff;
                    }
                }
            }
            thread1Result = localResult;
        }else if (marker == 1){ // handles pairs in the range of N/2 <= J+ N/2 < I < N
            int localResult = Integer.MAX_VALUE;
            for (int i=halfN; i<N; i++) {
                for (int j=0; (j+halfN)<i; j++) {
                    int diff = Math.abs(values[i] - values[j]);
                    if(diff < localResult){
                        localResult = diff;
                    }
                }
            }
            thread2Result = localResult;
        }else if (marker == 2){ // handles pairs in the range of N/2 <= I < J + N/2 < N
            int localResult = Integer.MAX_VALUE;
            for (int j=0; (j + halfN)<N; j++) {
                for (int i=halfN; i< j + halfN; i++) {
                    int diff = Math.abs(values[i] - values[j]);
                    if(diff < localResult){
                        localResult = diff;
                    }
                }
            }
            thread3Result = localResult;
        }else{ // handles pairs in the ranges of 0 <= J < I < N/2 and N/2 <= J < I < N
            int localResult = Integer.MAX_VALUE;
            for (int i=halfN; i < N; i++) {
                for (int j=halfN; j < i; j++) {
                    int diff = Math.abs(values[i] - values[j]);
                    if(diff < localResult){
                        localResult = diff;
                    }
                }
            }
            thread4Result = localResult;
        }
    }
}