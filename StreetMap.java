import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;
import java.awt.*;

import javax.swing.*;


public class StreetMap extends Canvas {
    
    static StreetMap canvas;
    static ArrayList<String> roadPrint = new ArrayList<String>(), pathPrint = new ArrayList<String>();
    static boolean show = false, directions = false, dirChecking = false;
    static String startIntersection = "", endIntersection = "";
    static int prevX = 0, prevY = 0;


    public static void drawCoordinate(java.awt.Graphics g, Hashtable<String, Road> roads, Double minX, Double minY, Double maxX, Double maxY, int winX, int winY, Color c){
        // System.out.println("(" + lat1 + ", " + long1 + ") to (" + lat2 + ", " + long2 + ")");
        for(String a : roads.keySet()){
            g.setColor(c);
            Road r = roads.get(a);
            Intersection start = r.getStartIntersection(), end = r.getEndIntersection();

            int x1 = (int) ((start.getLongitude() - minY) * (winX - 0) / (maxY - minY)),
            y1 = (int) (winY + (-1 * ((start.getLatitude() - minX) * (winY - 0) / (maxX - minX)))),
            x2 = (int) ((end.getLongitude() - minY) * (winX - 0) / (maxY - minY)),
            y2 = (int) (winY + (-1 * ((end.getLatitude() - minX) * (winY - 0) / (maxX - minX))));
            // System.out.println("CHANGED TO\n(" + x1 + ", " + y1 + ") to (" + x2 + ", " + y2 + ")");
            // System.out.println("0 <= " + x1 + " and " + x2 + " <= " + winX + "? " + ((0 <= x1 && x1 <= winX) && (0 <= x2 && x2 <= winX)) + "\n0 <= " + y1 + " and " + y2 + " <= " + winY + "? " + ((0 <= y1 && y1 <= winY) && (0 <= y2 && y2 <= winY)));
            if(x1 != x2 || y1 != y2)
                g.drawLine(x1, y1, x2, y2);
        }

        
    }

    public static void main(String[] args){
        
        // READ THE MAP FILE
        if(args.length == 0){
            System.out.println("Format: java StreetMap [map text file] [ --show ] [ --directions startIntersection endIntersection ]");
            return;
        }

        Scanner mapReader = null;
        try{
            if(!args[0].endsWith(".txt")){
                System.out.println("Map file must be a .txt file.");
                return;
            }
            mapReader = new Scanner( new File(args[0]));
        } catch (Exception e){
            System.out.println("An error occurred when trying to read the map file.");
            e.printStackTrace();
            return;
        }


        // READ FOR ADDITIONAL ARGUMENTS
        // System.out.println(args.length);
        if(args.length > 1){
            for(int i = 1; i < args.length; i++){
                // System.out.println(args[i]);
                if(args[i].indexOf("--") == 0){
                    if(dirChecking){
                        if(startIntersection == "")
                            System.out.println("Invalid format, expected --directions \"startInteresection\" parameter after \"--directions\".\nFormat: --directions [startIntersection endIntersection]");
                        else if(endIntersection == "")
                            System.out.println("Invalid format, expected --directions \"endIntersection\" parameter after \"" + startIntersection + "\".\nFormat: --directions [startIntersection endIntersection]");
                        else 
                            System.out.println("Invalid format, expected --directions parameter after \"" + args[i-1] + "\".\nFormat: --directions [startIntersection endIntersection]");
                        return;
                    }

                    if(args[i].contains("show")){
                        show = true;
                        continue;
                    } else if(args[i].contains("directions")){
                        if(dirChecking || startIntersection != ""){
                            System.out.println("Invalid format, only one --directions argument must be typed.\nFormat: --directions [startIntersection endIntersection]");
                            return;
                        }
                        directions = true;
                        dirChecking = true;
                        continue;
                    } else {
                        System.out.println("Invalid argument \"" + args[i].substring(2) + "\"");
                        return;
                    }
                } else if(dirChecking){
                    if(startIntersection != ""){
                        endIntersection = args[i];
                        dirChecking = false;
                    } else 
                        startIntersection = args[i];
                }
            }
        }

        
        if(directions){
            if(startIntersection == ""){
                System.out.println("Format: --directions [startIntersection endIntersection]");
                return;
            } else if(endIntersection == ""){
                System.out.println("No --directions \"endIntersection\" parameter found.\nFormat: --directions [startIntersection endIntersection]");
                return;
            } else {
                // System.out.println("START: " + startIntersection + "\nEND: " + endIntersection);
            }
            
        }
        // System.out.println("Show map: " + show);

        // BY THIS POINT, ALL ARGUMENTS HAVE BEEN READ AND ACCOUNTED FOR. 


        Graph mapGraph = new Graph();

        System.out.println("Reading file...");
        while(mapReader.hasNextLine()){
            String fullInput = mapReader.nextLine();
            String[] splitInput = fullInput.split("	");
            /** format: [0] "i" for intersection        "r" for road
                        [1] ID: String ID               <--
                        [2] Latitude: double            Intersection 1 ID
                        [3] Longitude: double           Intersection 2 ID
            */
            if(splitInput[0].equals("i"))
                mapGraph.addIntersection(splitInput[1].trim(), Double.parseDouble(splitInput[2]), Double.parseDouble(splitInput[3]));
            else if(splitInput[0].equals("r"))
                mapGraph.addRoad(splitInput[1].trim(), splitInput[2].trim(), splitInput[3].trim());
            else {
                System.out.println("ERROR: Invalid input on this line\n" + fullInput);
                return;
            }
        }
        System.out.println("Connecting roads to intersections...");
        for(String roadID : mapGraph.allRoads().keySet()){
            Road road = mapGraph.getRoad(roadID);
            // System.out.println("Currently reading road \"" + roadID + "\" which has starts and ends of \"" + road.getStartIntersection().getID() + "\" and \"" + road.getEndIntersection().getID() + "\"");
            road.getStartIntersection().addConnectedRoad(road);
            road.getEndIntersection().addConnectedRoad(road);
        }



        // the map is complete
        
        // System.out.println(mapGraph.allIntersections().toString());
        
        // System.out.println("\n\n\n" + mapGraph.allRoads().toString());
        if(directions){
            System.out.println("Calculating directions between intersections.\nThis may take a bit depending on the map size. Sit tight...");
            try {
                mapGraph.dijkstraAlgorithm(startIntersection, endIntersection);
            } catch (Exception e){
                e.printStackTrace();
                return;
            }
        }
        
        

        if(show){
            System.out.println("Creating map window...");
            int winX = 900, winY = 500;
            JFrame frame = new JFrame(args[0]);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            canvas = new StreetMap();
            canvas.setSize(winX, winY);

            frame.add(canvas);
            frame.pack();
            
            canvas.setBackground(Color.WHITE);
            java.awt.Graphics g = canvas.getGraphics();
            canvas.paint(g);
            frame.setVisible(true);
            winX = frame.getWidth();
            winY = frame.getHeight();
            
            Double[] minMaxVals = mapGraph.getMinMaxCoords();
            Double minX = minMaxVals[0], minY = minMaxVals[1],
            maxX = minMaxVals[2], maxY = minMaxVals[3];        

            System.out.println("Drawing map. This may take a bit depending on the size...");
            
            drawCoordinate(g, mapGraph.getRoadTable(), minX, minY, maxX, maxY, winX, winY, Color.BLACK);
            if(directions){
                drawCoordinate(g, mapGraph.getPathTable(), minX, minY, maxX, maxY, winX, winY, Color.RED);
            }


            while(true){
                boolean needsRedraw = false;
                // System.out.println(winX + " = " + frame.getWidth() + "     " + winY + " = " + frame.getHeight());
                if(winX != frame.getWidth() || winY != frame.getHeight()){
                    System.out.println("Resizing...");
                    winX = frame.getWidth();
                    winY = frame.getHeight();
                    g.clearRect(0, 0, winX, winY);
                    needsRedraw = true;
                }
                if(needsRedraw){
                    drawCoordinate(g, mapGraph.getRoadTable(), minX, minY, maxX, maxY, winX, winY, Color.BLACK);
                    if(directions){
                        drawCoordinate(g, mapGraph.getPathTable(), minX, minY, maxX, maxY, winX, winY, Color.RED);
                    }
                }
                
            }
        }
    }
}