import java.util.Arrays;

public class UR_Heap<T extends Comparable<T>> {
    private T[] heap;
    private int size;

    private final int defaultSize = 10;
    
    public UR_Heap(){
        this.heap = (T[]) new Comparable[defaultSize];
        this.size = 0;
    }
    public UR_Heap(int initSize){
        this.heap = (T[]) new Comparable[initSize];
        this.size = 0;
    }

    public UR_Heap(T[] inputArr){
        this.heap = inputArr;
        this.size = inputArr.length;
        heapify();
    }

    public void heapify(){
        for(int i = this.size/2-1; i >=0; i--)
            bubbleDown(i);
    }

    
    public void realloc(){
        realloc(size()/2);
    }
    
    public void realloc(int addon){
        // System.out.println("REALLOCATING");
        int newLength = this.heap.length + addon;
        T[] newHeap = (T[]) new Comparable[newLength];
        for(int i = 0; i < this.heap.length; i++)
            newHeap[i] = this.heap[i];
        this.heap = (T[]) new Comparable[newLength];
        for(int i = 0; i < newHeap.length; i++)
            this.heap[i] = newHeap[i];
        recalcSize();
    }

    
    public void insert(T item){
        if(this.size() >= heap.length)
            realloc();
        heap[size()] = item;
        bubbleUp(size());
        this.size++;
    }

    private void bubbleUp(int ind){
        // printHeap();
        // System.out.println("Beginning bubbleup loop at index " + ind + " which contains " + heap[ind]);
        while(ind > 0){
            int parent = (ind-1)/2;
            if(heap[ind].compareTo(heap[parent]) >= 0)
                return;
            else {
                T tmp = heap[parent];
                heap[parent] = heap[ind];
                heap[ind] = tmp;
                ind = parent;
            }
        }

    }
    
    public boolean isEmpty(){
        for(T obj : heap)
            if(obj != null)
                return false;
        
        return true;

    }
    
    public int size(){
        return size;
    }

    private int recalcSize(){
        // this will start counting from the end of the list to the beginning, seting the index of the first actual item found as the heap size
        // used as a failsafe in case the size variable is incorrect
        // it also returns the newly-calcualted size to save a step

        // System.out.println("Recalculating content size (originally " + size());
        // printHeap();
        int finalInd = heap.length-1;
        for(int i = heap.length-1; i >= 0; i--){
            // System.out.println("At index " + i);
            if(heap[i] != null){
                finalInd = i;
                break;
            }
        }
            
        this.size = finalInd+1;
        // System.out.println("Done, size now "+ size());
        return this.size;
    } 

    public T deleteMin(){
        T obj = heap[0];
        // printHeap();
        // System.out.println("heap[0] = " + heap[0] + "    heap[" + (size()-1) + "] = " + heap[size()-1]);
        heap[0] = heap[size()-1];
        heap[size()-1] = null;
        bubbleDown(0);
        this.size--;
        return obj;
    }

    private void bubbleDown(int ind){
        int childInd = 2*ind-1;
        if(childInd < 0)
            childInd = 0;
        T value = heap[ind];

        while(childInd < this.size){
            T maxVal = value;
            int maxInd = -1;
            // System.out.println("Childindex: " + childInd);
            for(int i = 0; i < 2 && i + childInd < this.size-1; i++){
                // System.out.println("heap[" + (i + childInd) + "] = " + heap[i + childInd] +"\nmaxval = " + maxVal);
                if(heap[i + childInd].compareTo(maxVal) < 0){
                    maxVal = heap[i + childInd];
                    maxInd = i + childInd;
                }
            }
            if(maxVal == value)
                return;
            else {
                T tmp = heap[ind];
                heap[ind] = heap[maxInd];
                heap[maxInd] = tmp;
                ind = maxInd;
                childInd = 2*ind-1;
                if(childInd < 0)
                    childInd = 0;
            }
        }

    }


    
//--HELPER / DEBUGGING METHODS-------------------------------------------
    public T[] toArray(){
        return heap;
    }

    public void printHeap(){
        System.out.println(Arrays.toString(heap));
    }

    public void printAllStats(){
        System.out.println("\nSIZE: " + this.size() + "\nALLOCATED: " + heap.length + "\nEmpty? " + this.isEmpty() +
        "\nCONTENTS: " + Arrays.toString(heap) + "\n");
    }
}