import java.util.ArrayList;

//class MinHeap {
//    int k;
//    ArrayList<Pair> heap;
//
//    public MinHeap(){
//        k =2;
//        heap = new ArrayList<>();
//    }
//
//    public MinHeap(int k){
//        this.k = k;
//        heap = new ArrayList<>();
//    }
//
//    public void add(int key, double dist){
//        Pair pair = new Pair(key,dist);
//        heap.add(pair);
//        heapifyUp(heap.size()-1);
//    }
//
//    public int extractMin() {
//
//        Pair min = heap.get(0);
//        Pair last = heap.get(heap.size()-1);
//        heap.set(0,last);
//        heap.remove(heap.size()-1);
//        heapifyDown(0);
//        return min.key;
//    }
//
//    public ArrayList<Integer> getHeap(){
//
//        ArrayList<Integer> keyHeap = new ArrayList<>();
//        for (int i =0; i< heap.size();i++){
//            keyHeap.add(heap.get(i).getKey());
//        }
//        return keyHeap;
//    }
//
//    private void heapifyUp(int index)
//    {
//        Pair temp = heap.get(index);
//        while (index > 0 && temp.getDist() <= heap.get(parent(index)).getDist())
//        {
//            if(temp.getDist() == heap.get(parent(index)).getDist()){
//                if(temp.getKey() < heap.get(parent(index)).getKey()){
//                    heap.set(index,heap.get(parent(index)));
//                    index = parent(index);
//                }
//                else
//                    break;
//            }
//            else {
//                heap.set(index, heap.get(parent(index)));
//                index = parent(index);
//            }
//        }
//        heap.set(index,temp);
//    }
//
//    private void heapifyDown(int index)
//    {
//        int child;
//        Pair tmp = heap.get(index);
//        while (kthChild(index, 1) < heap.size())
//        {
//            child = minChild(index);
//            if (heap.get(child).dist <= tmp.dist) {
//                if (heap.get(child).dist == tmp.dist){
//                    if (heap.get(child).key < tmp.key)
//                        heap.set(index, heap.get(child));
//                }
//                else {
//                    heap.set(index, heap.get(child));
//                }
//            }
//            else
//                break;
//            index = child;
//        }
//        heap.set(index, tmp);
//    }
//
//    private int minChild(int ind)
//    {
//        int bestChild = kthChild(ind, 1);
//        int d = 2;
//        int pos = kthChild(ind, d);
//        while ((d <= k) && (pos < heap.size()))
//        {
//            if (heap.get(pos).dist <= heap.get(bestChild).dist) {
//                if (heap.get(pos).dist == heap.get(bestChild).dist){
//                    if (heap.get(pos).key <= heap.get(bestChild).key)
//                        bestChild = pos;
//                }
//                else
//                    bestChild = pos;
//            }
//            pos = kthChild(ind, d++);
//        }
//        return bestChild;
//    }
//
//    private int parent(int i)
//    {
//        return (i - 1)/k;
//    }
//
//    private int kthChild(int i, int d)
//    {
//        return k * i + d;
//    }
//}
//
//class Pair{
//
//    int key;
//    double dist;
//
//    public Pair(int key,double dist){
//        this.key=key;
//        this.dist = dist;
//    }
//
//    public int getKey(){
//        return key;
//    }
//
//    public double getDist(){
//        return dist;
//    }
//
//}

