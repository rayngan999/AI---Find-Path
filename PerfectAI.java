import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.io.FileWriter;
import java.io.IOException;
/// A sample AI that takes a very suboptimal path.
/**
 * This is a sample AI that moves as far horizontally as necessary to reach the target,
 * then as far vertically as necessary to reach the target.  It is intended primarily as
 * a demonstration of the various pieces of the program.
 * 
 */

public class PerfectAI implements AIModule
{
    
    class Data implements Comparable <Data>{

        public Data(Point currentPoint, double  i) {
            this.cost = i;
            this.point = currentPoint;
        }

        Point point;
        double  cost;

        
        @Override
        public int compareTo(Data d) {
            double cur = d.cost;
            if (cur < this.cost) return 1;
            if (cur > this.cost) return -1;
            else return 0;
        }
    }
    
   
    /// Creates the path to the goal.
    public List<Point> createPath(final TerrainMap map)
    {
        // Holds the resulting path
        final ArrayList<Point> path = new ArrayList<Point>();
        // node
        final Point CurrentPoint = map.getStartPoint();
        // frontier
        final ArrayList<Data> frontier =new ArrayList<>(); 
        frontier.add(new Data(CurrentPoint,0));
        // explored
        final Set<Point> explored = new HashSet <>();

        try{
            FileWriter myWriter = new FileWriter("log.txt");
        }catch (IOException e) {}
        System.out.println(map.getEndPoint());
        while(true){
            if (frontier.isEmpty()){
                System.out.println("error");
                return new ArrayList<Point>();
            }
            //sort the frontier
            Collections.sort(frontier);
            Data cur =  frontier.get(0);
            Point cur_point = cur.point;
            
            //add to path
            path.add(new Point(cur_point));
            double cur_cost = cur.cost;
            
                
           

            // System.out.println();

            frontier.remove(0);
            if (cur_point == map.getEndPoint()){
                return path;
            }
            explored.add(cur_point);
            final Point[] neighbors = map.getNeighbors(cur_point);
            for (int i =0; i< neighbors.length; i++){
                Point child = neighbors[i];
                double new_cost = cur_cost + map.getCost(cur_point, child);
                //boolean test_b = frontier.contains(new Data(cur_point,0));
                if (!explored.contains(child) && !contains(frontier,child)){
                    frontier.add(new Data(child,new_cost));
        
                    
                }else if(contains(frontier,child) && frontier.get(indexOf(frontier, child)).cost > new_cost){
                    frontier.remove(indexOf(frontier, child));
                    frontier.add(new Data(child, new_cost));
                }
            }
        }
    }

    private boolean contains( ArrayList<Data> frontier, Point p) {
       
        for (int i =0; i<frontier.size();i++){
            if(p == frontier.get(i).point){
                return true;
            }
        }
        return false;   
    }

    private int indexOf (ArrayList<Data> frontier, Point p){
        for (int i =0; i<frontier.size();i++){
            if(p == frontier.get(i).point){
                return i;
            }
        }
        return -1;
    }
}


// Point anonymousClass = new Point(){
    //     /**
    //      *
    //      */
    //     private static final long serialVersionUID = 1L;

    //     @Override
    //     public boolean equals(Object v) {
    //         boolean retVal = false;

    //         if (v instanceof Data){
    //             Data ptr = (Data) v;
    //             retVal = ptr.point == this;
    //         }

    //         return retVal;
    //     }
        
    //     @Override
    //     public int hashCode() {
    //     int hash = 7;
    //     hash = 17 * hash + (this != null ? this.hashCode() : 0);
    //     return hash;
    //     }

    // };