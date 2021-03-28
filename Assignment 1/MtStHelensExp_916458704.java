import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.HashMap;
/// A sample AI that takes a very suboptimal path.
/**
 * This is a sample AI that moves as far horizontally as necessary to reach the target,
 * then as far vertically as necessary to reach the target.  It is intended primarily as
 * a demonstration of the various pieces of the program.
 * 
 */

public class MtStHelensExp_916458704 implements AIModule{
    
    class Data implements Comparable <Data>{

        public Data(Point currentPoint, double i, double f, Data parent) {
            this.f = f;
            this.cost = i;
            this.point = currentPoint;
            this.parent = parent;
        }

        Point point;
        double  cost;
        double f;
        Data parent;
        
        @Override
        public int compareTo(Data d) {
            double cur = d.f;
            if (cur < this.f) return 1;
            if (cur > this.f) return -1;
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
        final Point EndPoint = map.getEndPoint();
        final HashMap<Point, Double> getG = new HashMap<Point,Double>();
        // frontier
        final ArrayList<Data> frontier =new ArrayList<>(); 
        frontier.add(new Data(CurrentPoint,0,getHeuristic(map, CurrentPoint, EndPoint), null));
        getG.put(CurrentPoint,0.00);
        
        // explored
        final HashSet<Point> explored = new HashSet <>();
        final HashSet<Point> frontier_set = new HashSet <>();
        

        while(frontier.size()>0){
            if (frontier.isEmpty()){
                System.out.println("error");
                return new ArrayList<Point>();
            }
            //sort the frontier
            Collections.sort(frontier);
            Data cur =  frontier.get(0);
            Point cur_point = cur.point;
            double cur_cost = cur.cost;

            frontier.remove(0);
            frontier_set.remove(cur_point);

            if (cur_point.x == EndPoint.x && cur_point.y == EndPoint.y){
                //create path
                ArrayList<Point> end = makePath(path, cur, map.getStartPoint());
                return end;
            }
            explored.add(cur_point);
            final Point[] neighbors = map.getNeighbors(cur_point);
            for (int i =0; i< neighbors.length; i++){
                Point child = neighbors[i];
                //double new_cost = cur_cost + map.getCost(cur_point, child);
                double new_cost = cur_cost + map.getCost(cur_point, child);
                double f = cur_cost + map.getCost(cur_point, child ) + getHeuristic(map, child, EndPoint);
                //System.out.println(new_cost);
                if (!explored.contains(child) && !frontier_set.contains(child)){
                    frontier.add(new Data(child,new_cost, f, cur));
                    frontier_set.add(child);
                    getG.put(child,new_cost);
                    
                }else if(frontier_set.contains(child) && getG.get(child) > new_cost){
                    int pos = indexOf(frontier, child);
                    if(pos != -1){
                        frontier.remove(pos);
                        frontier.add(new Data(child,new_cost,f, cur));
                        getG.replace(child,new_cost);
                    }
                }
            }
        }
        Data cur =  frontier.get(0);
        ArrayList<Point> end = makePath(path, cur, map.getStartPoint());
        return end;
    }

    private double getHeuristic(final TerrainMap map, final Point pt1, final Point pt2){
        double curHeight = map.getTile(pt1);
        double goalHeight = map.getTile(pt2);
        double h = Math.abs(goalHeight - curHeight);
        double d = Math.max(Math.abs(pt2.x-pt1.x),Math.abs(pt2.y-pt1.y));

        if (curHeight > goalHeight){
           return Math.exp(-1)*h +(d-h);
        }
        if (curHeight == goalHeight){
            return d;
        }else{
            if(h > d){
                return(Math.exp(1) * h);
            }
            return (Math.exp(1) * h + (d-h));
        }

    }


    private ArrayList<Point> makePath(ArrayList<Point> paths, Data cur, Point begin){
        paths.add(cur.point);
        while (cur.parent != null){
            Point parent = cur.parent.point;
            paths.add(0,parent);
            cur = cur.parent;
        }
        return paths;
    }



    private int indexOf (ArrayList<Data> frontier, Point p){
        for (int i =0; i < frontier.size();i++){
            Point check = frontier.get(i).point;
            if(p.equals(check)){
                return i;
            }
        }
        return -1;
    }

    
}