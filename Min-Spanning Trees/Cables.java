import java.io.*;
import java.util.*;
import java.lang.Math;

public class Cables
{
	public static void main(String[] args)
	{
		Scanner stdin = new Scanner(System.in);
	}
}

class DisjointSet
{
	public DisjointSet(int numElements)
	{

	}

	// remember to add arguments for this shit
	public int find()
	{

	}

	public boolean union
}

class Edge
{
	private float weight;

	// our constructor will accept the 2 points that are being
	// joined, and our weight will be calculated using the distance
	// formula between the two points
	public Edge(Point point1, Point point2)
	{
		weight = Math.sqrt(Math.pow(point2.getX() - point1.getX(), 2) + Math.pow(point2.getY() - point1.getY(), 2));
	}

	public float getWeight()
	{
		return weight;
	}
}

class Point
{
	private int x;
	private int y;

	public Point(int newX, int newY)
	{
		x = newX;
		y = newY;
	}

	public int getX()
	{
		return x;
	}

	public int getY()
	{
		return y;
	}
}
