# CSC-172-Project-3-Street-Mapping
Project 3 for CSC 172 Data Structure and Algorithms, where we were given coordinates of street intersections and were tasked with creating a program to calculate and display the shortest path between those intersections. It was completed on December 10th, 2022.

These files are for the Project 3: Street Mapping project in CSC 172.
- README                this file
- Graph.java            contains the custom Graph data type, used to store all the roads and intersections in a given map. Also contains the methods necessary to run Dijkstra's Algorithm, and calculating information necessary for the drawing of the map to the screen.
- Intersection.java     contains the custom Intersection data type, acting as the "nodes" for the Graph data type. It stores the intersection ID, the latitide and longitude coordinates, the distance to that node (which is utilized in Dijkstra's Algorithm), the previous node in the chain, and a Hashtable of all the Roads it is connected to.
- Road.java             contains the custom Road data type, acting as the "edges" for the Graph data type. It contains the road ID, the start and end intersections that it connects and the distance between them. Additionally, it contains the method for calculating that distance on the Earth.
- UR_Heap.java          a custom implementation of the MinHeap ADT from a previous Lab in this course. It is used in Dijkstra's Algorithm to put the shortest distance in the heap first. 
- StreetMap.java        the main class that runs this code. It checks for command line arguments first, performing error checking as well. It opens the map text file and reads all of the intersections and roads on it, then connects the Roads to their appropriate intersections. If the user typed in "--directions [start] [end]," the program will use Dijkstra's Algorithm to calculate the sequence of nodes and the shortest distance. If the user typed "--show," it will create a window to draw the roads on and display the map to the user, highlighting the path between the start and end intersections if the user provided those as well.

I added extensive error checking to the command line arguments, and throughout most of the program. I am unsure of if this could be worth extra credit, but if it is, I implore you to consider it. I added custom error messaging to display if the user typed in something incorrectly, or if it couldn't find the intersection the user asked for, or if they forgot to type in a start/end intersection for the direction command.

These files import the following Java data types:
- ArrayList
- Arrays
- Hashtable
- File
- Scanner
- java.awt.*;

I referred to the following people and resources for assistance or help with this project:
https://stackoverflow.com/questions/5731863/mapping-a-numeric-range-onto-another
https://stackoverflow.com/questions/70055152/problem-with-the-calculating-distance-between-locations-using-haversine-formula
Krish J.
Various members of the Tutoring Discord Server

The estimated runtime of the Dijkstra's Algorithm is O(n^2)
