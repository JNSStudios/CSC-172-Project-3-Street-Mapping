import java.util.Hashtable;

public class Intersection implements Comparable<Intersection> {
    // the NODE object
    private String id;
    private Double lat;
    private Double lon;

    private Double distTo;
    private Intersection prev;
    private Hashtable<String, Road> connectedRoads;

    public Intersection(String i, Double la, Double lo){
        this.id = i;
        this.lat = la;
        this.lon = lo;
        this.distTo = Double.POSITIVE_INFINITY;
        this.prev = null;
        connectedRoads = new Hashtable<String, Road>();
    }

    public String getID() {return id;}
    public String setID(String newID) {this.id = newID; return newID;}
    public Double getLatitude() {return lat;}
    public Double setLatitude(Double newLat) {this.lat = newLat; return newLat;}
    public Double getLongitude() {return lon;}
    public Double setLongitude(Double newLon) {this.lon = newLon; return newLon;}
    public Double getDistanceTo() {return distTo;}
    public void setDistanceTo(Double newDistTo) {this.distTo = newDistTo;}
    public Intersection getPrev() {return this.prev;}
    public void setPrev(Intersection newPrev) {this.prev = newPrev;}
    
    public void addConnectedRoad(Road road){
            connectedRoads.put(road.getID(), road);
    }

    public void removeConnectedRoad(String roadID){
        connectedRoads.remove(roadID);
    }
    
    public boolean containsConnectedRoad(String roadID){
        return connectedRoads.containsKey(roadID);
    }

    public Hashtable<String, Road> allConnectedRoads(){
        return connectedRoads;
}


    @Override
    public String toString(){			
        return "(\'" + this.id + "\', " + this.lat + " | " + this.lon + ")";
    }

    @Override
    public int compareTo(Intersection o) {
        // TODO Auto-generated method stub
        Hashtable<String, Road> myRoads = this.allConnectedRoads(), otherRoads = o.allConnectedRoads();
        Road myShortest = null, otherShortest = null;
        for(String s : myRoads.keySet()){
            Road r = myRoads.get(s);
            if(myShortest == null || r.getDistance() < myShortest.getDistance())
                myShortest = r;
        }
        for(String s : otherRoads.keySet()){
            Road r = otherRoads.get(s);
            if(otherShortest == null || r.getDistance() < otherShortest.getDistance())
                otherShortest = r;
        }
        return (int) Math.round(otherShortest.getDistance() - myShortest.getDistance());
    }
}
