// skeleton program for you to complete...
import java.util.Optional;
public class myastar extends astar_base
{
   public myastar(int r, int c) {
      super(r,c); 
   }

   @Override
   public void customize() {
      ////// Things you can do here...
      //setRandFactor(0.13); // increase amount of water/fire
      setcosts(2,0,5,10); // cost of land, desert, fire, water
      //pathfinder.gap = 15; // change size of graphical hexgagons
      //pathfinder.yoff = 20; // graphical top margin adjustment
       // pathfinder.delaytime = 0; //change animation speed
   }
   
   public static void main(String[] av) {
      pathfinder.main(av);
   }//main

    
   @Override
   public Optional<coord> search(int sy, int sx, int ty, int tx) {
      
      PriorityHeap<coord> frontier = new PriorityHeap<coord>(4*(ROWS+COLS),false);
      coord src = new coord(sy,sx);
      PATH[sy][sx] = src;
      frontier.add(src);
      coord current = src;
      while (frontier.size > 0) {
         current = frontier.poll().get();
         current.interior = true;
         if (current.y == ty && current.x == tx) return Optional.of(current);
         int ny = 0;
         int nx = 0;
         for (int i=0; i<6; i++) {
            ny = current.y + DY[i];
            nx = current.x + DX[current.y%2][i];
            if (nx>=0 && nx<COLS && ny>=0 && ny<ROWS) {
               coord neighbor = new coord(ny,nx);
               neighbor.set_parent(current);
               neighbor.knowncost = costof[M[ny][nx]] + current.knowncost;
               neighbor.estcost = neighbor.knowncost + hexdist(ny,nx,ty,tx);
               if (PATH[ny][nx] == null ) {
                   if (costof[M[ny][nx]] >= 0) {
                       frontier.add(neighbor);
                   }
                  PATH[ny][nx] = neighbor;
               }//coord not in PATH
               else if (PATH[ny][nx].interior == true ) {}//coord already in interior
               else if ( PATH[ny][nx].interior == false ) {
                   if (neighbor.compareTo(PATH[ny][nx]) < 0 && costof[M[ny][nx]] >= 0 ) {
                     PATH[ny][nx].copy_from(neighbor);
                     frontier.reposition(PATH[ny][nx]); 
                   }//put the coord in frontier and reposition
               }//if not in interior
            }//inner if
         }//inner for
        // PATH[ny][nx].interior = true;
      }//main while

      return Optional.empty();
      
   }//search

}//myastar class

/*
Where do I start?

Study the coord class in the coord.java file.  These are objects that
we are going to use to build a search tree.

Study the astar class.  There's an array int[][] M with ROWS rows and
COLS columns.  The value of each M[i][j] is its terrain type (0=OPEN,
3=WATER, etc).

Study the DX and DY vectors in the Hexagon class.  These tell you how to
calculate the array coordinates of each of your six neighbors.

The search function takes a starting position sy,sx and a target position 
ty,tx.  Your tree should have as root a coord object with y=sy, x=sx
(and null for prev, 0 for knowndist and estcost).  You are to build a 
spanning tree until you find ty,tx.  You need to return a coord object
with y=ty and x=tx, and with prev pointer set so we can follow it back to
sy,sx.

   more help: I wrote this version of search that searches the map 
   randomly: just look at it for hings, do not copy (it won't work).
   
        int[] DY = Hexagon.DY;
	int[][] DX = Hexagon.DX;
        coord current = new coord(sy,sx);
	while (current.y!=ty || current.x!=tx)
	    {
		//pick random direction
		int dir = (int)(Math.random()*6);
		int cy = current.y, cx = current.x;
		int ny = cy + DY[dir]; 
		int nx = cx + DX[cy%2][dir];
		if (nx>=0 && nx<COLS && ny>=0 && ny<ROWS)
		    {
			coord next = new coord(ny,nx);
			next.prev = current;
			current = next; 
		    }
		// else, loop back and pick another direction
	    }// main while
	return current;

   To make your code work properly, you need to implement algorithm A*, 
   which starts by creating a frontier using a re-queuable priority heap
   with the smallest value on top.  you can do this by:

   FlexQueue<coord> Frontier = new FlexQueue<coord>(4*(ROWS+COLS),true);

   This version of my FlexQueue constructor sets an estimated initial
   capacity and 'true' inverts the Comparator so that the smallest instead
   of the largest value is placed at the top of the heap.  But searching
   a heap, is as you should know, is O(n).  We can't use a binary search tree
   because coord objects are compared by their cost (estcost), and many
   coords can have the same cost.  So we also need:

   coord[][] Status = new coord[ROWS][COLS];

   this create an array of null pointers, but you can set Status[y][x]
   to coord objects.  Each coord object contains a boolean interior variable
   that indicates if the coord is on the Frontier (and is also pointed by
   something in the FlexQueue heap) or the interior.  Thus given coordinates
   y,x, you can immediately (in O(1)) time determine if a coord object has
   been created for those coordinates (Status[y][x]!=null) and if that
   coord object is on the frontier or interior.  This solves the problem
   of searching a heap.  Searching both the frontier and interior is O(1)
   with the Status array.
*/
