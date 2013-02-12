/*
	Egan Wall
	COP3503, Program 2
	Cables.java
*/

import java.io.*;
import java.util.*;
import java.lang.Math;

public class Cables
{
	public static void main(String[] args)
	{
		Scanner stdin = new Scanner(System.in);
		DisjointSet mySet;
		int numPoints = stdin.nextInt();
		ArrayList<Edge> edgeList;

		while(numPoints != 0)
		{
			// System.out.printf("There are %d points for this case.\n", numPoints);

			float sum = 0;

			mySet = new DisjointSet(numPoints);

			for(int i = 0; i < numPoints; i++)
			{
				mySet.parents[i] = new Point(stdin.nextInt(), stdin.nextInt());
				// System.out.println(mySet.parents[i].toString());
			}

			// now we have to generate all of the edges for the graph
			edgeList = new ArrayList<Edge>();

			for(int i = 0; i < numPoints; i++)
			{
				for(int j = 0; j < numPoints; j++)
				{
					if(i != j)
					{
						edgeList.add(new Edge(mySet.parents[i], mySet.parents[j]));
					}
				}
			}

			Collections.sort(edgeList);

			for(int i = 0; i < edgeList.size(); i++)
			{
				// System.out.printf("Edge %d weight: %f\n", edgeList.get(i).getID(), edgeList.get(i).getWeight());
				// System.out.printf("\t\t%s\n", edgeList.get(i).point1.toString());
				// System.out.printf("\t\t%s\n", edgeList.get(i).point2.toString());
			}

			for(int i = 0; i < edgeList.size(); i++)
			{
				boolean succ = mySet.union(edgeList.get(i).point1, edgeList.get(i).point2);
				mySet.printSet();

				if(succ)
				{
					sum += edgeList.get(i).getWeight();
					// System.out.printf("================================ Adding %.2f to the sum (%.2f).\n", edgeList.get(i).getWeight(), sum);
				}
			}

			System.out.printf("%.2f\n", sum);

			Point.clearPoints();
			Edge.clearEdges();
			numPoints = stdin.nextInt();
		}
	}
}

class DisjointSet
{
	public Point parents[];

	public DisjointSet(int numElements)
	{
		parents = new Point[numElements];
	}

	// remember to add arguments for this shit
	public Point find(Point thePoint)
	{
		while(thePoint.getParent() != thePoint.getCurrValue())
		{
			thePoint = parents[thePoint.getParent()];
		}

		return thePoint;
	}

	public boolean union(Point point1, Point point2)
	{
		Point root1 = find(point1);
		Point root2 = find(point2);

		// System.out.printf("Find returned parents %d and %d.\n", root1.getCurrValue(), root2.getCurrValue());

		if(root1.getCurrValue() == root2.getCurrValue())
		{
			// System.out.println("Union not performed, cycle avoided.");
			return false;
		}

		if(root1.getHeight() > root2.getHeight())
		{
			root2.setParent(root1.getParent());
		} else if(root1.getHeight() < root2.getHeight())
		{
			root1.setParent(root2.getParent());
		} else
		{
			root2.setParent(root1.getParent());
			root1.increaseHeight();
		}

		// System.out.printf("Union successful.\n");
		return true;
	}

	public void printSet()
	{
		for(int i = 0; i < parents.length; i++)
		{
			// System.out.printf("\t%s\n", parents[i].toString());
		}
	}
}

class Edge implements Comparable<Edge>
{
	private double weight;
	private static int numEdges = 0;
	private int ID;

	public Point point1;
	public Point point2;

	// our constructor will accept the 2 points that are being
	// joined, and our weight will be calculated using the distance
	// formula between the two points
	public Edge(Point newPoint1, Point newPoint2)
	{
		weight = Math.sqrt(Math.pow((newPoint2.getX() - newPoint1.getX()), 2.0) + Math.pow((newPoint2.getY() - newPoint1.getY()), 2.0));
		ID = ++numEdges;
		point1 = newPoint1;
		point2 = newPoint2;
	}

	public double getWeight()
	{
		return weight;
	}

	public int getID()
	{
		return ID;
	}

	public int compareTo(Edge compEdge)
	{
		if(compEdge.getWeight() > weight)
		{
			return -1;
		} else if(compEdge.getWeight() < weight)
		{
			return 1;
		} 

		return 0;
	}

	public static void clearEdges()
	{
		numEdges = 0;
	}
}

class Point
{
	private int x;
	private int y;
	private static int numPoints = 0;
	private int parent;
	private int height;
	private int currVal;

	public Point(int newX, int newY)
	{
		x = newX;
		y = newY;
		parent = numPoints++;
		currVal = parent;
		height = 0;
	}

	public int getX()
	{
		return x;
	}

	public int getY()
	{
		return y;
	}

	public int getParent()
	{
		return parent;
	}

	public void setParent(int newParentID)
	{
		parent = newParentID;
	}

	public int getHeight()
	{
		return height;
	}

	public int getCurrValue()
	{
		return currVal;
	}

	public void increaseHeight()
	{
		height++;
	}

	public String toString()
	{
		String outString = "Point X: " + x + " Y: " + y + " Parent: " + parent + " Height: " + height;
		return outString;
	}

	public static void clearPoints()
	{
		numPoints = 0;
	}
}
