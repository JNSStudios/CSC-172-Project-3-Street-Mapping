public class Road {
    // the EDGE object
    private String id;
    private Intersection start;
    private Intersection end;
    private Double distance;

    

    public Road(String i, Intersection int1, Intersection int2){
        this.id = i;
        this.start = int1;
        this.end = int2;
        this.distance = calculateDistance();
    }

    public String getID() {return id;}
    public void setID(String newID) {this.id = newID;}
    public Double getDistance() { return distance;}
    public Intersection getStartIntersection() {return start;}
    public void setStartIntersection(Intersection newStart) {this.start = newStart;}
    public Intersection getEndIntersection() {return end;}
    public void setEndIntersection(Intersection newEnd) {this.end = newEnd;}

    private Double calculateDistance(){
        // ‘haversine’ formula
        // referenced https://stackoverflow.com/questions/70055152/problem-with-the-calculating-distance-between-locations-using-haversine-formula
        // NOTE: all calculations are done in KILOMETERS first, then converted to meters.
        Double  la1 = start.getLatitude(), la2 = end.getLatitude(), 
                lo1 = start.getLongitude(), lo2 = end.getLongitude(),
                
        dLat = Math.toRadians(la1 - la2),
        dLong = Math.toRadians(lo1 - lo2),
        // earth radius, 6,378.1 km = 6378100
        earthRadius = 6378.1,
        
        a = Math.pow(Math.sin(dLat / 2), 2)
        + Math.cos(Math.toRadians(la1)) * Math.cos(Math.toRadians(la2))
        * Math.pow(Math.sin(dLong / 2), 2),

        c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return (earthRadius * c)*1000;
    }

    @Override
    public String toString(){			
        return "(\'" + this.id + "\', " + this.start + " -> " + this.end + ", d=" + this.distance + ")";
    }
}
