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

public class PerfectAI implements AIModule
{
    
    class Data implements Comparable <Data>{

        public Data(Point currentPoint, double i, Data parent) {
            this.cost = i;
            this.point = currentPoint;
            this.parent = parent;
        }

        Point point;
        double  cost;
        Data parent;
        
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
        final Point EndPoint = map.getEndPoint();
        // frontier
        final ArrayList<Data> frontier =new ArrayList<>(); 
        frontier.add(new Data(CurrentPoint,0, null));
        // explored
        final Set<Point> explored = new HashSet <>();

        while(true){
            if (frontier.isEmpty()){
                System.out.println("error");
                return new ArrayList<Point>();
            }
            //sort the frontier
            Collections.sort(frontier);
            Data cur =  frontier.get(0);
            Point cur_point = cur.point;
            double cur_cost = cur.cost;

            System.out.println(explored.size());

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
                double new_cost = cur_cost + map.getCost(cur_point, child) + getHeuristic (map, child, EndPoint);
                //System.out.println(child);
                if (!explored.contains(child) && !contains(frontier,child)){
                    frontier.add(new Data(child,new_cost, cur));
        
                    
                }else if(contains(frontier,child) && frontier.get(indexOf(frontier, child)).cost > new_cost){
                    frontier.remove(indexOf(frontier, child));
                    frontier.add(new Data(child,new_cost, cur));
                }
            }
        }
    
    }
    private double getHeuristic(final TerrainMap map, final Point pt1, final Point pt2){
        boolean x = true;
        if (x == true){
            double h = pt2.y - pt1.y;
            double d = pt2.x - pt1.x;
            if (pt1.y == pt2.y){
                return d;
            }
            if (pt1.y <= pt2.y){
                if(h <= d){
                    return Math.exp(1)*h +(d-h);
                }else{
                    return Math.exp(1)*h;
                }
            }else{
                if(h <= d){
                    h = -h;
                    return Math.exp(-1)*h +(d-h);
                }else{
                    return Math.exp(h);
                }
            }
        }else{
            double h = pt2.y - pt1.y;
            double d = pt2.x - pt1.x;
            if (pt1.y == pt2.y){
                return ((double) (pt1.y) /  ( (double) pt1.y+1.0)) * d;
            }
            if (pt1.y <= pt2.y){
                if(h <= d){
                    return h + ((double) (pt1.y) /  ( (double) pt1.y+1.0)) * (d-h);
                }else{
                    return (double) (pt1.y + h) /  ( (double) pt1.y+1.0);
                }
            }else{
                if(h <= d){
                    h = -h;
                    return (double) (pt1.y - h) /  ( (double) pt1.y+1.0) + ((double) (pt1.y)/ ( (double) pt1.y+1.0)) * (d-1);
                }else{
                    return (double) (pt1.y - h) /  ( (double) pt1.y+1.0);
                }
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
