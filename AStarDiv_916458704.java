import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/// A sample AI that takes a very suboptimal path.
/**
 * This is a sample AI that moves as far horizontally as necessary to reach the target,
 * then as far vertically as necessary to reach the target.  It is intended primarily as
 * a demonstration of the various pieces of the program.
 * 
 */


public class AStarDiv_916458704 implements AIModule{
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
        // frontier
        final ArrayList<Data> frontier =new ArrayList<>(); 
        frontier.add(new Data(CurrentPoint,0,getHeuristic(map, CurrentPoint, EndPoint), null));

        // explored
        final Set<Point> explored = new HashSet <>();

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

            //System.out.println(explored.size());

            frontier.remove(0);
            
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
                if (!explored.contains(child) && !contains(frontier,child)){
                    frontier.add(new Data(child,new_cost, f, cur));
        
                    
                }else if(contains(frontier,child) && frontier.get(indexOf(frontier, child)).cost > new_cost){
                    frontier.remove(indexOf(frontier, child));
                    frontier.add(new Data(child,new_cost,f, cur));
                }
            }
        }
        Data cur =  frontier.get(0);
        ArrayList<Point> end = makePath(path, cur, map.getStartPoint());
        return end;
    }
    private double getHeuristic(final TerrainMap map, final Point pt1, final Point pt2){
        
        double h =  Math.abs(map.getTile(pt2) - map.getTile(pt1));
        double d = Math.max(Math.abs(pt2.x-pt1.x),Math.abs(pt2.y-pt1.y));
      

        if (map.getTile(pt1) == map.getTile(pt2)) {
            return ((double) (map.getTile(pt1)) / ((double) map.getTile(pt1) + 1.0)) * d;
        }
        if (map.getTile(pt1) < map.getTile(pt2)) {
            if (h < d) {
                return h+ ((double) map.getTile(pt2) / ((double) map.getTile(pt2) + 1.0)) * (d - h);
            } else {
                return (double) (map.getTile(pt2)) / ((double) map.getTile(pt1)+ 1.0) + (double) map.getTile(pt2) / ((double) map.getTile(pt2) + 1.0) * (d - 1);
            }
        } else {
    
            if (h < d) {
                return (double) (map.getTile(pt2)) / ((double) map.getTile(pt1) + 1.0)
                        + ((double) (map.getTile(pt2)) / ((double) map.getTile(pt2) + 1.0)) * (d - 1);
            } else {
                return (double) (map.getTile(pt2)) / ((double) map.getTile(pt1) + 1.0) + ((double) (map.getTile(pt2)) / ((double) map.getTile(pt2) + 1.0)) * (d - 1);
            }
        }
        
    }


    private ArrayList<Point> makePath(ArrayList<Point> paths, Data cur, Point begin){
        if(cur.point.x == begin.x && cur.point.y == begin.y){
            paths.add(new Point(cur.point));
            return paths;
        }
        makePath(paths, cur.parent,begin);
        paths.add(new Point(cur.point));
        return paths;
    }
    private boolean contains( ArrayList<Data> frontier, Point p) {
       
        for (int i =0; i<frontier.size();i++){
            Point check = frontier.get(i).point;
            if(p.x == check.x && p.y == check.y){
                return true;
            }
        }
        return false;   
    }

    private int indexOf (ArrayList<Data> frontier, Point p){
        for (int i =0; i < frontier.size();i++){
            Point check = frontier.get(i).point;
            if(p.x == check.x && p.y == check.y){
                return i;
            }
        }
        return -1;
    }
}
