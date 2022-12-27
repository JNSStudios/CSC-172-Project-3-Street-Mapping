import java.util.ArrayList;
import java.util.Hashtable;

public class Graph {
    // the GRAPH object
    private Hashtable<String, Intersection> interTable;
    private Hashtable<String, Road> roadTable;
    private Hashtable<String, Road> pathTable;
    private Double minX, minY, maxX, maxY;
    

    public Graph(){
        this.interTable = new Hashtable<String, Intersection>();
        this.roadTable = new Hashtable<String, Road>();
        this.pathTable = new Hashtable<String, Road>();
        this.minX = null;
        this.minY = null;
        this.maxX = null;
        this.maxY = null;
    }

    public void addIntersection(String id, Double latitude, Double longitude){
        interTable.put(id, new Intersection(id, latitude, longitude));
    }
    public void addRoad(String id, String start, String end){
        roadTable.put(id, new Road(id, getIntersection(start), getIntersection(end)));
    }

    public Intersection getIntersection(String id){
        return interTable.get(id);
    }

    public Road getRoad(String id){
        return roadTable.get(id);
    }

    public Hashtable<String, Intersection> allIntersections(){
        return interTable;
    }

    public Hashtable<String, Road> allRoads(){
        return roadTable;
    }



    public void dijkstraAlgorithm(String startID, String endID) throws Exception{
        if(!interTable.containsKey(startID))
            throw new Exception("No intersection with ID \"" + startID + "\" can be found");   
        if(!interTable.containsKey(endID))
            throw new Exception("No intersection with ID \"" + endID + "\" can be found");   
        UR_Heap<Intersection> unvisited = new UR_Heap<Intersection>();
        
        for(String i : interTable.keySet()){            // O(n)
            interTable.get(i).setDistanceTo(Double.POSITIVE_INFINITY);
            interTable.get(i).setPrev(null);
        }
        Intersection start = interTable.get(startID);
        start.setDistanceTo(0.0);
        
        unvisited.insert(start);

        while(!unvisited.isEmpty()){    //O()
            Intersection current = unvisited.deleteMin();
            if(current.getID().equals(endID))
                break;
            for(String r : current.allConnectedRoads().keySet()){
                Intersection adjInter = getRoad(r).getStartIntersection().getID().equals(current.getID()) ? getRoad(r).getEndIntersection() : getRoad(r).getStartIntersection();
                // System.out.println(current.getID() + "  compared to " + adjInter.getID());
                Double distBetween = current.allConnectedRoads().get(r).getDistance(),
                totalTravel = current.getDistanceTo() + distBetween;

                // System.out.println(totalTravel + " < " + adjInter.getDistanceTo() + " is " + (totalTravel < adjInter.getDistanceTo()));
                if(totalTravel < adjInter.getDistanceTo()){
                    adjInter.setDistanceTo(totalTravel);
                    adjInter.setPrev(current);
                    unvisited.insert(adjInter);
                }
            }
        }
        preparePathTable(endID);
        printPath(interTable.get(endID));
        System.out.println("The distance between " + startID + " and " + endID + " is " + interTable.get(endID).getDistanceTo());
    }

    private void printPath(Intersection intersection) {
        Intersection curr = intersection;
        ArrayList<Intersection> list = new ArrayList<Intersection>();
        while(curr != null){
            list.add(0, curr);
            curr = curr.getPrev();
        }
        for(Intersection i : list){
            System.out.println(i.getID());
        }
    }

    public Double[] getMinMaxCoords(){
        for(String r : roadTable.keySet()){
            Road road = roadTable.get(r);
            
            Intersection startEnd = road.getStartIntersection(),
            otherEnd = road.getEndIntersection();
            Double x1 = startEnd.getLatitude(), y1 = startEnd.getLongitude(),
            x2 = otherEnd.getLatitude(), y2 = otherEnd.getLongitude();
            if(minX == null || minX == null || minX == null || minX == null){
                minX = (x1 < x2) ? x1 : x2;
                maxX = (x1 > x2) ? x1 : x2;
                minY = (y1 < y2) ? y1 : y2;
                maxY = (y1 > y2) ? y1 : y2;
            }

            if( x1 < minX)
                minX = x1;
            else if(x1 > maxX)
                maxX = x1;

            if( y1 < minY)
                minY = y1;
            else if( y1 > maxY)
                maxY = y1;
            
            if( x2 < minX)
                minX = x2;
            else if(x2 > maxX)
                maxX = x2;                
            
            if( y2 < minY)
                minY = y2;
            else if( y2 > maxY)
                maxY = y2;
        }
        return new Double[]{minX, minY, maxX, maxY};
    }
    
    public void preparePathTable(String endID){

        Intersection curr = interTable.get(endID);

        while(curr.getPrev() != null){
            Intersection prev = curr.getPrev();
            for(String r : curr.allConnectedRoads().keySet()){
                Road road = curr.allConnectedRoads().get(r);
                if(road.getStartIntersection().equals(prev) || road.getEndIntersection().equals(prev)){
                    pathTable.put(road.getID(), road);
                    break;
                }
            }
            curr = prev;
        }
    }

    public Hashtable<String, Road> getRoadTable(){
        return roadTable;
    }

    public Hashtable<String, Road> getPathTable(){
        return pathTable;
    }
}
