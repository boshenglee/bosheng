//import com.sun.deploy.util.StringUtils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class RobotPath {

    String _fileName;
    int _nRows;
    int _nCols;
    Coordinate begin;
    Coordinate end;
    Hashtable <Integer, Coordinate> _obstacles; //create a hashtable to store obstacles
    Hashtable <Coordinate,Coordinate> pass; //create a hastable to store the pass through coordinate
    LinkedList<Coordinate> pathForQuick = new LinkedList<Coordinate>(); //a linkedlist store the path for pathForQuick


    public void readInput(String FileName) throws IOException {
        _fileName = FileName;
        BufferedReader reader;
        _obstacles = new Hashtable<Integer,Coordinate>();
        pass = new Hashtable<Coordinate,Coordinate>();

        try {
            reader = new BufferedReader(new FileReader(FileName));
            String line = reader.readLine(); //read first line
            while (line != null) {
                String[] tokens = line.split(" "); //split line into token
                if (tokens[0].equals("nrows")) { //for first line
                    _nRows = Integer.parseInt(tokens[1]);
                    _nCols = Integer.parseInt(tokens[3]);
                    line = reader.readLine();
                } else if (tokens[0].equals("start")) { //second line
                    begin = new Coordinate(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]));
                    line = reader.readLine();
                } else if (tokens[0].equals("dest")) { //third line
                    end = new Coordinate(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]));
                    line = reader.readLine();
                } else if (tokens[0].equals("obstacles")){ //forth and to the end
                    line = reader.readLine();
                    int counter =0;
                    while (line != null) {
                        String[] token = line.split(" ");
                        Coordinate temp = new Coordinate(Integer.parseInt(token[0]), Integer.parseInt(token[1]));
                        _obstacles.put(counter++,temp); //store in the hashtable
                        line = reader.readLine();
                    }
                }
                else{
                    throw new IOException("Wrong format .txt file");
                }
            }
            reader.close();
        } catch (FileNotFoundException e){
            throw new FileNotFoundException("No " + _fileName +"found");
        } catch (IOException e) {
            throw e;
        }
    }

    public void planShortest(){

        pass.clear();
        pathForQuick.clear();
        BFSforShortest(begin);

        pass.forEach((k, v) -> {
            v.unVisit();
        });

        if(pass.get(end)!=null)
            bfsBack();
        drawGrid();
    }

    public void quickPlan(){

        pass.clear();
        pathForQuick.clear();
        DFSforQuick(begin,0);

        if(pass.contains(end)!= false) {
            Coordinate w = pass.get(end);
            if (w._parentForQuick != null) {
                while (!w._parentForQuick.equals(begin)) {
                    pathForQuick.add(w);
                    w = pass.get(w)._parentForQuick;

                }
                pathForQuick.add(pass.get(w));
            }
        }
        else{
            pass.put(new Coordinate(end._row,end._column),new Coordinate(end._row,end._column));
        }
        drawGrid();
    }

    public void output(){

        for(int i =0;i<_nRows;i++) {
            for (int j = 0; j < _nCols; j++) {
                Coordinate c = new Coordinate(i,j);
                if(pass.contains(c)==true){
                    if(pass.get(c)._value==null){
                        System.out.printf("%5s", 0);
                    }
                    else {
                        System.out.printf("%5s", pass.get(c)._value);
                    }
                }
                else{
                    System.out.printf("%5s", 0);
                }
            }
            System.out.printf("\n");
        }
    }

    private void drawGrid(){

        for(int k=0;k<_obstacles.size();k++){
            if(pass.contains(_obstacles.get(k))==true)
                pass.get(_obstacles.get(k))._value = "*";
            else{
                Coordinate obs = new Coordinate(_obstacles.get(k)._row,_obstacles.get(k)._column);
                pass.put(obs,obs);
                pass.get(_obstacles.get(k))._value = "*";
            }
        }
        pass.get(begin)._value = "S";
        if(pass.get(end)._value == "S")
            pass.get(end)._value+= "D";
        else
            pass.get(end)._value = "D";

        for(int i=1;i<pathForQuick.size();i++){
            Coordinate u = pathForQuick.get(i);
            if(!u.equals(begin) && !u.equals(end)) {
                Coordinate w = pathForQuick.get(i-1);
                if( pass.get(u)._value==null) {
                    if(w._row==u._row && w._column>u._column)
                        pass.get(u)._value = "e";
                    else if(w._row>u._row&&w._column==u._column)
                        pass.get(u)._value = "s";
                    else if(w._row<u._row&&w._column==u._column)
                        pass.get(u)._value = "n";
                    else
                        pass.get(u)._value = "w";
                }
            }
        }
    }

    public void BFSforShortest(Coordinate s)
    {
        Queue queue = new LinkedList<Coordinate>();
        pass.put(new Coordinate(s._row,s._column),new Coordinate(s._row,s._column));
        pass.get(s).distance(0);
        queue.add(pass.get(s));

        while (queue.size() != 0)
        {
            Coordinate u = (Coordinate) queue.poll();
            pass.get(u).visit();

            if(pass.get(u)._column!=_nCols-1) {
                Coordinate east = new Coordinate(pass.get(u)._row, pass.get(u)._column+1);
                if ( pass.contains(east) == false) {
                    pass.put(east,east);
                }
                if(_obstacles.contains(east)==false&& pass.get(east)._visited==false){
                    if (u._distance+1 < pass.get(east)._distance){
                        pass.get(east).distance(u._distance+1);
                        pass.get(east)._parent.add(u);
                        queue.add( pass.get(east));
                    }
                    else if(u._distance+1 ==  pass.get(east)._distance){
                        pass.get(east)._parent.add(u);

                    }
                    else{
                        continue;
                    }
                }
            }
            if(pass.get(u)._row!=_nRows - 1) {
                Coordinate south = new Coordinate(pass.get(u)._row + 1, pass.get(u)._column);
                if (pass.contains(south) == false) {
                    pass.put(south,south);
                }
                if(_obstacles.contains(south)==false&& pass.get(south)._visited==false){
                    if (u._distance+1 < pass.get(south)._distance){
                        pass.get(south).distance(u._distance+1);
                        pass.get(south)._parent.add(u);
                        queue.add(pass.get(south));
                    }
                    else if(u._distance+1 == pass.get(south)._distance){
                        pass.get(south)._parent.add(u);

                    }
                    else{
                        continue;
                    }
                }
            }

            if(pass.get(u)._column!=0) {
                Coordinate west = new Coordinate(pass.get(u)._row, pass.get(u)._column-1);
                if (pass.contains(west) == false) {
                    pass.put(west,west);
                }
                if(_obstacles.contains(west)==false&& pass.get(west)._visited==false){
                    if (u._distance+1 < pass.get(west)._distance){
                        pass.get(west).distance(u._distance+1);
                        pass.get(west)._parent.add(u);
                        queue.add(pass.get(west));
                    }
                    else if(u._distance+1 == pass.get(west)._distance){
                        pass.get(west)._parent.add(u);

                    }
                    else{
                        continue;
                    }
                }
            }
            if(pass.get(u)._row!=0){
                Coordinate north = new Coordinate(pass.get(u)._row-1,pass.get(u)._column);
                if (pass.contains(north) == false) {
                    pass.put(north,north);
                }
                if(_obstacles.contains(north)==false&&  pass.get(north)._visited==false){
                    if (u._distance+1 < pass.get(north)._distance){
                        pass.get(north).distance(u._distance+1);
                        pass.get(north)._parent.add(u);
                        queue.add(pass.get(north));
                    }
                    else if(u._distance+1 == pass.get(north)._distance){
                        pass.get(north)._parent.add(u);

                    }
                    else{
                        continue;
                    }
                }
            }
            if(u.equals(end)){break;}
        }
    }

    public int DFSforQuick(Coordinate u, int finish){
        if (u.equals(end)) {
            // if match found then no need to traverse more till depth
            return 1;
        }
        if(pass.contains(u)==false) {
            pass.put(new Coordinate(u._row,u._column),new Coordinate(u._row,u._column));
        }
        pass.get(u).visit();

        ArrayList<Coordinate> neighbours = new ArrayList<Coordinate>();

        if(pass.get(u)._row!=_nRows - 1) {
            Coordinate south = new Coordinate(pass.get(u)._row + 1, pass.get(u)._column);
            if (pass.contains(south)==false) {
                pass.put(south,south);
            }
            if(_obstacles.contains(south)==false&& pass.get(south)._visited==false){
                neighbours.add(pass.get(south));
            }
        }

        if(pass.get(u)._column!=_nCols - 1) {
            Coordinate east = new Coordinate(pass.get(u)._row, pass.get(u)._column+1);
            if (pass.contains(east)==false) {
                pass.put(east,east);
            }
            if(_obstacles.contains(east)==false&& pass.get(east)._visited==false){
                neighbours.add(pass.get(east));
            }
        }

        if(pass.get(u)._column!=0) {
            Coordinate west = new Coordinate(pass.get(u)._row, pass.get(u)._column-1);
            if (pass.contains(west)==false) {
                pass.put(west,west);
            }
            if(_obstacles.contains(west)==false&& pass.get(west)._visited==false){
                neighbours.add(pass.get(west));
            }
        }

        if(pass.get(u)._row!=0){
            Coordinate north = new Coordinate(pass.get(u)._row-1,pass.get(u)._column);
            if (pass.contains(north)==false) {
                pass.put(north,north);
            }
            if(_obstacles.contains(north)==false&& pass.get(north)._visited==false){
                neighbours.add(pass.get(north));
            }
        }
        while (neighbours.size()!=0) {

            double minDist = Math.sqrt(Math.pow(end._row - neighbours.get(0)._row, 2) + Math.pow(end._column - neighbours.get(0)._column, 2));
            int minIndex = 0;
            for (int i = 1; i < neighbours.size(); i++) {
                double dist = Math.sqrt(Math.pow(end._row - neighbours.get(i)._row, 2) + Math.pow(end._column - neighbours.get(i)._column, 2));
                if (dist < minDist) {
                    minDist = dist;
                    minIndex = i;
                }
                else if(dist == minDist){
                    if(neighbours.get(i)._row<neighbours.get(minIndex)._row){
                        minIndex = i;
                    }
                    else if(neighbours.get(i)._row==neighbours.get(minIndex)._row){
                        if(neighbours.get(i)._column<neighbours.get(minIndex)._column){
                            minIndex = i;
                        }
                    }
                }
            }
            Coordinate n = new Coordinate(neighbours.get(minIndex)._row,neighbours.get(minIndex)._column);
            if(pass.get(n)._parentForQuick == null) {
                pass.get(n)._parentForQuick = pass.get(u);
            }
            finish = DFSforQuick(pass.get(n),finish);

            if(finish == 1){
                neighbours.clear();
                break;
            }
            if(neighbours.size()!=0)
                neighbours.remove(minIndex);
        }
        return finish;
    }

    public void bfsBack(){

        Queue queue = new LinkedList<Coordinate>();

        pass.get(end);
        pass.get(end).visit();
        queue.add(pass.get(end));
        while (queue.size()!=0) {

            Coordinate w = (Coordinate) queue.poll();

            if (w.equals(begin))
                break;
            Iterator<Coordinate> i = pass.get(w)._parent.listIterator();
            while(i.hasNext()) {
                Coordinate u = i.next();
                if(!u.equals(begin)&&!u.equals(end)){
                    if( pass.get(u)._value==null) {
                        if(w._row==u._row && w._column>u._column)
                            pass.get(u)._value = "e";
                        else if(w._row>u._row&&w._column==u._column)
                            pass.get(u)._value = "s";
                        else if(w._row==u._row&&w._column<u._column)
                            pass.get(u)._value = "w";
                        else
                            pass.get(u)._value = "n";
                    }
                    else{
                        if(w._row==u._row && w._column>u._column) {
                            if(pass.get(u)._value.indexOf('e')==-1)
                                pass.get(u)._value += "e";
                        }
                        else if(w._row>u._row&&w._column==u._column) {
                            if(pass.get(u)._value.indexOf('s')==-1) {
                                if(pass.get(u)._value.indexOf('e')!=-1)
                                    pass.get(u)._value = "s" + pass.get(u)._value;
                                else
                                    pass.get(u)._value += "s";
                            }
                        }
                        else if(w._row<u._row&&w._column==u._column) {
                            if(pass.get(u)._value.indexOf('n')==-1) {
                                if(pass.get(u)._value.indexOf('e')!=-1||pass.get(u)._value.indexOf('w')!=-1||pass.get(u)._value.indexOf('s')!=-1)
                                    pass.get(u)._value = "n" + pass.get(u)._value;
                                else
                                    pass.get(u)._value += "n";
                            }
                        }
                        else {
                            if(pass.get(u)._value.indexOf('w')==-1) {
                                if(pass.get(u)._value.indexOf('e')!=-1||pass.get(u)._value.indexOf('s')!=-1)
                                    pass.get(u)._value = "w" + pass.get(u)._value;
                                else
                                    pass.get(u)._value += "w";
                            }
                        }
                    }
                }
                if (u._visited==false) {
                    pass.get(u).visit();
                    queue.add(pass.get(u));

                }
            }
        }
    }
}


// extra class
//coor class
//
//
//
//
//

class Coordinate {

    int _row;
    int _column;
    int _distance;
    String _value;
    boolean _visited;
    Coordinate _parentForQuick;
    List<Coordinate> _parent;

    public Coordinate (int row, int column) {
        _row = row;
        _column = column;
        _visited = false;
        _distance = Integer.MAX_VALUE;
        _value = null;
        _parentForQuick = null;
        _parent = new ArrayList<Coordinate>();
    }

    public void distance(int n){
        _distance = n;
    }

    public void visit() {
        _visited = true;
    }

    public void unVisit(){
        _visited = false;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj==this)
            return true;
        if (!(obj instanceof Coordinate))
            return false;

        Coordinate c = (Coordinate) obj;

        return Integer.compare(_row,c._row)==0 && Integer.compare(_column,c._column)==0;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 61 * hash + _row;
        hash = 61 * hash +_column;
        return hash;
    }

    @Override
    public String toString()
    {
        String result ="";
        result += "( "+_row +" , "+_column+" )";

        return (result);
    }

    public int getRow() { return _row; }
    public int getColumn() { return _column; }

}
