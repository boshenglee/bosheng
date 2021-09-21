import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

public class PathFinder {

    int intersection; //number of vertex
    int roadways; //number of edge
    Coordinates[] coor; //an array of coordinate
    Graph g; //graph g

    public PathFinder(){
        // default constructor
    }

    /**
     * a method to read the input from the provided file name
     * @param fileName name of the file to read
     */
    public void readInput(String fileName){

        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(fileName));
            String line = reader.readLine(); //read first line
            int numLine =1; //keep track the number of line
            while (line != null) {
                String[] tokens = line.trim().split("\\s+"); //split line into token
                if(numLine==1){ //for the first line
                    intersection = Integer.parseInt(tokens[0]); //set the number of intersection
                    roadways = Integer.parseInt(tokens[1]); // set the number of roadways
                    coor = new Coordinates[intersection];
                    g = new Graph(intersection);//create a graph
                    line = reader.readLine();
                    numLine++;
                }
                else if(numLine>1&&numLine<intersection+2){ //for all intersection
                    while(numLine>1&&numLine<intersection+2){
                        tokens = line.trim().split("\\s+");
                        coor[Integer.parseInt(tokens[0])] = new Coordinates(Integer.parseInt(tokens[1]),Integer.parseInt(tokens[2])); //add into coor array to keep track the coor of intersection
                        line = reader.readLine();
                        numLine++;
                    }
                }
                else if(numLine ==intersection+2){ //skip the space line
                    line = reader.readLine();
                    numLine++;
                    while(numLine<roadways+intersection+3){ // for all the roadways, only include the number of roadways mention in the first line
                        tokens = line.trim().split("\\s+");
                        int fst = Integer.parseInt(tokens[0]);
                        int snd = Integer.parseInt(tokens[1]);
                        g.addEgde(fst,snd,coor[fst].distTo(coor[snd]));
                        line = reader.readLine();
                        numLine++;
                    }
                }
                else if(numLine >= roadways+intersection+3)
                    break;
            }
            reader.close();
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Calculate the distance to a destination
     * @param s starting point
     * @param t destination
     * @param k the k-nary heap
     * @param method method dijkstra's algo
     * @return the distance to destination
     */
    public double dist2Dest(int s, int t, int k, int method){
        ArrayList<Integer> mPath = path2Dest(s,t,k,method); //call the path2dest method
        double pathDist =0;
        if(mPath!=null) {
            for (int i = 0; i < mPath.size()-1; i++) {
                pathDist += coor[mPath.get(i)].distTo(coor[mPath.get(i+1)]); //calculate ethe path
            }
        }else{
            pathDist=-1;
        }

        return pathDist;
    }

    /**
     * Show the path to a destination
     * @param s
     * @param t
     * @param k
     * @param method
     * @return array list of path
     */
    public ArrayList<Integer> path2Dest(int s, int t, int k, int method){
        double INFINITY = Double.MAX_VALUE;
        boolean[] SPT = new boolean[intersection];
        double[] d = new double[intersection];
        int[] parent = new int[intersection];

        for (int i = 0; i <intersection ; i++) {
            d[i] = INFINITY;
            parent[i] = -1;
        }
        d[s] = 0;

        MinHeap minHeap = new MinHeap(k);
        for (int i = 0; i <intersection ; i++) {
            minHeap.add(i,d[i]);
        }
        while(!minHeap.isEmpty()){
            int extractedVertex = minHeap.extractMin();

            if(extractedVertex==t) {
                break;
            }
            SPT[extractedVertex] = true;

            LinkedList<Edge> list = g.adjacencylist[extractedVertex];
            for (int i = 0; i <list.size() ; i++) {
                Edge edge = list.get(i);
                int destination = edge.destination;
                if(SPT[destination]==false ) {
                    double newKey =0;
                    double currentKey=0;
                    if(method==1) { //method 1
                        newKey = d[extractedVertex] + edge.weight;
                        currentKey = d[destination];
                    }
                    else{ //method 2
                        newKey = d[extractedVertex] + edge.weight + coor[destination].distTo(coor[t])-coor[extractedVertex].distTo(coor[t]);
                        currentKey = d[destination];
                    }
                    if(currentKey>=newKey){
                        if(currentKey==newKey){ //if equal need to compare the value of key
                            if(extractedVertex<parent[destination]){
                                minHeap.updateKey(newKey, destination, d[destination]);
                                parent[destination] = extractedVertex;
                                d[destination] = newKey;
                            }
                        }
                        else {
                            minHeap.updateKey(newKey, destination, d[destination]);
                            parent[destination] = extractedVertex;
                            d[destination] = newKey;
                        }
                    }
                }
            }
        }
        //trace back the path using parent properties
        ArrayList<Integer> path = new ArrayList<>();
        if(parent[t]!=-1){
            path.add(t);
            while(parent[t]!= s) {
                path.add(0,parent[t]);
                t = parent[t];
            }
            path.add(0,s);
        }
        else
            path = null;
        return path;
    }

    /**
     * Calculate the number of m path to destination
     * @param s
     * @param t
     * @param k
     * @return the number of m path to destination
     */
    public int noOfMPaths2Dest(int s,int t,int k){
        double INFINITY = Double.MAX_VALUE;
        boolean[] SPT = new boolean[intersection];
        double[] resultPath = new double[intersection];
        LinkedList<Integer>[] parent = new LinkedList[intersection]; //using linked list instead of array to store all the possible parent of an intersection

        for (int i = 0; i <intersection ; i++) {
            resultPath[i] = INFINITY;
            parent[i] = new LinkedList<>();
        }

        //decrease the distance for the first index
        resultPath[s] = 0;

        //add all the vertices to the MinHeap
        MinHeap minHeap = new MinHeap(k);
        for (int i = 0; i <intersection ; i++) {
            minHeap.add(i,resultPath[i]);
        }
        //while minHeap is not empty
        while(!minHeap.isEmpty()){
            //extract the min
            int extractedVertex = minHeap.extractMin();

            if(extractedVertex==t) {
                break;
            }
            SPT[extractedVertex] = true;

            LinkedList<Edge> list = g.adjacencylist[extractedVertex];
            for (int i = 0; i <list.size() ; i++) {
                Edge edge = list.get(i);
                int destination = edge.destination;
                if(SPT[destination]==false ) {

                    double newKey =  resultPath[extractedVertex] + edge.weight ;
                    double currentKey = resultPath[destination];
                    if(currentKey>=newKey){
                        minHeap.updateKey(newKey, destination,resultPath[destination]);
                        parent[destination].add(extractedVertex);
                        resultPath[destination] = newKey;
                    }
                }
            }
        }
        int numofPath =0;

        //calculate the number of path
        if(parent[t]!=null){
            boolean[] isVisited = new boolean[intersection];
            ArrayList<Integer> pathList = new ArrayList<>();
            pathList.add(t);
            numofPath = findAllPathsUtil(s, t, isVisited, pathList,numofPath,parent); //dfs

        }
        return numofPath;
    }

    /**
     * Calculate the distance from one intersection to all other intersection
     * @param s
     * @param k
     * @return the array list of the distance from starting point to the index vertex
     */
    public double[] dist2All(int s, int k){

        double INFINITY = Double.MAX_VALUE;
        boolean[] SPT = new boolean[intersection];

        double[] resultPath = new double[intersection];
        for (int i = 0; i <intersection ; i++) {
            resultPath[i] = INFINITY;
        }
        resultPath[s] = 0;
        MinHeap minHeap = new MinHeap(k);
        for (int i = 0; i <intersection ; i++) {
            minHeap.add(i,resultPath[i]);
        }
        while(!minHeap.isEmpty()){

            int extractedVertex = minHeap.extractMin();

            if(resultPath[extractedVertex]==INFINITY)
                break;

            SPT[extractedVertex] = true;

            LinkedList<Edge> list = g.adjacencylist[extractedVertex];
            for (int i = 0; i <list.size() ; i++) {
                Edge edge = list.get(i);
                int destination = edge.destination;
                if(SPT[destination]==false ) {
                    double newKey =  resultPath[extractedVertex] + edge.weight ;
                    double currentKey = resultPath[destination];
                    if(currentKey>newKey){
                        minHeap.updateKey(newKey, destination,resultPath[destination]);
                        resultPath[destination] = newKey;
                    }
                }
            }
        }
        for(int i =0; i<resultPath.length;i++){
            if(SPT[i]==false)
                resultPath[i]=-1;
        }
        return resultPath;
    }

    /**
     * The parent of the index element in mRtree
     * @param s
     * @param k
     * @return the array of parent of the index element
     */
    public int[] mRtreeFromSource(int s,int k){
        double INFINITY = Double.MAX_VALUE;
        boolean[] SPT = new boolean[intersection];
        double[] d = new double[intersection];
        int[] parent = new int[intersection];

        for (int i = 0; i <intersection ; i++) {
            d[i] = INFINITY;
            parent[i] = -1;
        }

        d[s] = 0;
        parent[s]=s;

        MinHeap minHeap = new MinHeap(k);
        for (int i = 0; i <intersection ; i++) {
            minHeap.add(i,d[i]);
        }
        while(!minHeap.isEmpty()){

            int extractedVertex = minHeap.extractMin();

            if(d[extractedVertex]==INFINITY)
                break;

            SPT[extractedVertex] = true;

            LinkedList<Edge> list = g.adjacencylist[extractedVertex];
            for (int i = 0; i <list.size() ; i++) {
                Edge edge = list.get(i);
                int destination = edge.destination;
                if(SPT[destination]==false ) {

                    double newKey = edge.weight ; //the different part with previous method
                    double currentKey = d[destination];
                    if(currentKey>=newKey){
                        if(currentKey==newKey){
                            if(extractedVertex<parent[destination]){
                                minHeap.updateKey(newKey, destination, d[destination]);
                                parent[destination] = extractedVertex;
                                d[destination] = newKey;
                            }
                        }
                        else {
                            minHeap.updateKey(newKey, destination, d[destination]);
                            parent[destination] = extractedVertex;
                            d[destination] = newKey;
                        }
                    }
                }
            }
        }
        return parent;
    }

    /**
     * Calculate the total cost in mRtree
     * @param s
     * @param k
     * @return the total cost in mRtree
     */
    public double mRtreeCostFromSource(int s,int k){
        int[] mRtree = mRtreeFromSource(s,k); //call the mRtreeFromSource method to find the parent of very vertex in the mRtree
        double cost =0;
        for (int i=0;i<mRtree.length;i++){
            if(mRtree[i]!=-1) {
                cost += coor[i].distTo(coor[mRtree[i]]);
            }
        }
        return cost;
    }

    /**
     * a helper method to find the number of path
     * @param s
     * @param t
     * @param isVisited
     * @param localPathList
     * @param numOfPath
     * @param parent
     * @return the number of path
     */
    private int findAllPathsUtil(int s, int t, boolean[] isVisited, List<Integer> localPathList,int numOfPath,LinkedList<Integer>[] parent )
    {

        if (t==s) {
            numOfPath++;
            return numOfPath;
        }
        isVisited[t] = true;

        for (Integer i : parent[t]) {
            if (!isVisited[i]) {
                localPathList.add(i);
                numOfPath =findAllPathsUtil(s,i , isVisited, localPathList,numOfPath,parent);
                localPathList.remove(i);
            }
        }
        isVisited[t] = false;
        return numOfPath;
    }
}

//edge class
class Edge {
    int source;
    int destination;
    double weight;

    public Edge(int source, int destination, double weight) {
        this.source = source;
        this.destination = destination;
        this.weight = weight;
    }
}

//graph class
class Graph {
    int vertices;
    LinkedList<Edge>[] adjacencylist;

    Graph(int vertices) {
        this.vertices = vertices;
        adjacencylist = new LinkedList[vertices];

        for (int i = 0; i <vertices ; i++) {
            adjacencylist[i] = new LinkedList<>();
        }
    }

    public void addEgde(int source, int destination, double weight) {
        Edge edge = new Edge(source, destination, weight); //bidirectional edge
        Edge edge1 = new Edge(destination,source,weight);
        adjacencylist[source].add(edge);
        adjacencylist[destination].add(edge1);
    }

    public void printGraph(){
        for (int i = 0; i <vertices ; i++) {
            LinkedList<Edge> list = adjacencylist[i];
            for (int j = 0; j <list.size() ; j++) {
                System.out.println("vertex-" + i + " is connected to " +
                        list.get(j).destination + " with weight " +  list.get(j).weight);
            }
        }
    }
}

//minheap
class MinHeap {
    int k;
    ArrayList<Pair> heap;
    Hashtable<Integer, Integer> indexOf;

    public MinHeap(){
        k =2;
        heap = new ArrayList<>();
        indexOf = new Hashtable<>();

    }

    public MinHeap(int k){
        this.k = k;
        heap = new ArrayList<>();
        indexOf = new Hashtable<>();

    }

    public void add(int key, double dist){
        Pair pair = new Pair(key,dist);
        heap.add(pair);
        indexOf.put(key,heap.size());
        heapifyUp(heap.size()-1);
    }

    public int extractMin() {

        Pair min = heap.get(0);
        Pair last = heap.get(heap.size()-1);
        heap.set(0,last);
        indexOf.replace(last.key,0);
        indexOf.remove(min.key);
        heap.remove(heap.size()-1);
        if(heap.size()!=0)
            heapifyDown(0);
        return min.key;
    }

    public boolean isEmpty( )
    {
        return heap.size() == 0;
    }

    public ArrayList<Integer> getHeap(){

        ArrayList<Integer> keyHeap = new ArrayList<>();
        for (int i =0; i< heap.size();i++){
            keyHeap.add(heap.get(i).getKey());
        }
        return keyHeap;
    }

    public void heapifyUp(int index)
    {
        Pair temp = heap.get(index);
        while (index > 0 && temp.getDist() <= heap.get(parent(index)).getDist())
        {
            if(temp.getDist() == heap.get(parent(index)).getDist()){
                if(temp.getKey() < heap.get(parent(index)).getKey()){
                    indexOf.replace(heap.get(parent(index)).key,index);
                    heap.set(index,heap.get(parent(index)));
                    index = parent(index);
                }
                else
                    break;
            }
            else {
                indexOf.replace(heap.get(parent(index)).key,index);
                heap.set(index, heap.get(parent(index)));
                index = parent(index);
            }
        }
        indexOf.replace(temp.key,index);
        heap.set(index,temp);
    }

    private void heapifyDown(int index)
    {
        int child;
        Pair tmp = heap.get(index);
        while (kthChild(index, 1) < heap.size())
        {
            child = minChild(index);
            if (heap.get(child).dist <= tmp.dist) {
                if (heap.get(child).dist == tmp.dist){
                    if (heap.get(child).key < tmp.key) {
                        indexOf.replace(heap.get(child).key,index);
                        heap.set(index, heap.get(child));
                    }
                    else
                        break;
                }
                else {
                    indexOf.replace(heap.get(child).key,index);
                    heap.set(index, heap.get(child));
                }
            }
            else
                break;
            index = child;
        }
        indexOf.replace(tmp.key,index);
        heap.set(index, tmp);
    }

    private int minChild(int ind)
    {
        int bestChild = kthChild(ind, 1);
        int d = 2;
        int pos = kthChild(ind, d);
        while ((d <= k) && (pos < heap.size()))
        {
            if (heap.get(pos).dist <= heap.get(bestChild).dist) {
                if (heap.get(pos).dist == heap.get(bestChild).dist){
                    if (heap.get(pos).key < heap.get(bestChild).key)
                        bestChild = pos;
                }
                else
                    bestChild = pos;
            }
            d++;
            pos = kthChild(ind, d);
        }
        return bestChild;
    }

    public void updateKey(double newKey, int vertex, double distance){

        //get the index which distance's needs a decrease;
//        int index = heap.indexOf(new Pair(vertex,distance)); //big probelm
        int index = indexOf.get(vertex);

        //get the node and update its value
//        HeapNode node = minHeap.mH[index];
//        node.distance = newKey;
        heap.set(index,new Pair(vertex,newKey));
        heapifyUp(index);
    }

    private int parent(int i)
    {
        return (i - 1)/k;
    }

    private int kthChild(int i, int d)
    {
        return k * i + d;
    }
}

//Pair class
class Pair{

    int key;
    double dist;

    public Pair(int key,double dist){
        this.key=key;
        this.dist = dist;
    }

    public int getKey(){
        return key;
    }

    public double getDist(){
        return dist;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Pair)) {
            return false;
        }
        Pair p = (Pair) obj;

        // Compare the data members and return accordingly
        return Integer.compare(key,p.key) == 0
                && Double.compare(dist, p.dist) == 0;
    }
}

//Coordinate class
class Coordinates{
    int x;
    int y;

    public Coordinates(int x, int y){
        this.x = x;
        this.y = y;
    }

    public double distTo(Coordinates c){
        return distance(x,y,c.x,c.y);
    }

    public double distance(int x1,int y1, int x2, int y2){
        double dist = Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
        return dist;
    }
}
