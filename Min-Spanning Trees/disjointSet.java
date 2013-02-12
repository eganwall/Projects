import java.util.*;

public class disjointSet
{
	public static void main(String[] args)
	{
		Scanner stdin = new Scanner(System.in);

		System.out.println("How many items do you want in your disjoint set?");
		int numItems = stdin.nextInt();

		disjointSet mySet = new disjointSet(numItems);

		// loop until we want to quit
		while(true)
		{
			System.out.println("Do you want to quit? (1 - yes, 0 - no)");
			int answer = stdin.nextInt();

			if(answer == 1) break; // if the user quits, break out

			System.out.println("Which items do you want to union? 0 to " + (numItems - 1));
			int item1 = stdin.nextInt();
			int item2 = stdin.nextInt();

			boolean result = mySet.union(item1, item2);

			if(result == false)
			{
				System.out.println("Those elements are in the same set!");
			} else
			{
				System.out.println("New parent list: " + mySet);
			}
		}
	}

	private Pair[] parents;

	public disjointSet(int numElements)
	{
		// create the array used to store the parents of each element
		parents = new Pair[numElements];

		for(int i = 0; i < numElements; i++)
		{
			// create a pair at each element of 0 height 
			// with the data of the current integer

			// this is the starting point of our disjoint sets, because
			// none of the sets have a common element. at this point we 
			// have *numElements* unique sets:
			//		{0}, {1}, ... {numElements - 1}, {numElements}
			parents[i] = new Pair(i, 0);
		}
	}

	public int find(int data)
	{
		// loop until we find an element whose parent is itself, 
		// because that means that we found the root
		while(data != parents[data].getData())
		{
			data = parents[data].getData();
		}

		return data;
	}

	public boolean union(int data1, int data2)
	{
		// get the parent nodes of both elements
		int root1 = find(data1);
		int root2 = find(data2);

		// if they're the same, they're already part of the same set
		if(root1 == root2)
		{
			return false; // so no union needed
		}

		// now comes the actual union. in order to keep the heights the
		// same, we will attach the shorter set to the taller set, so 
		// we have to compare their heights. if the sets are the same 
		// height, we have to increment the height of the set we are 
		// using as the root

		if(parents[root1].getHeight() > parents[root2].getHeight())
		{
			// if tree 2 is shorter, attach it to tree 1
			parents[root2].setData(root1);
		} 
		else if(parents[root2].getHeight() > parents[root1].getHeight())
		{
			// if tree 1 is shorter, attach it to tree 2
			parents[root1].setData(root2);
		}
		else
		{
			// if they are the same, attach tree 2 to tree 1 and increment the height
			parents[root2].setData(root1);
			parents[root1].increaseHeight();
		}

		// let us know that the union was successful
		return true;
	}

	public String toString()
	{
		String newStr = "";

		for(int i = 0; i < parents.length; i++)
		{
			newStr = newStr + parents[i].getData() + " ";
		}

		return newStr;
	}
}

// The Pair class contains information about each element in our set.
// The data field stores the current element's parent in the tree, 
// and the height field stores the height of the element's subtree.
class Pair
{
	private int data; // the contents of the node
	private int height; // the height of the pair's subtree

	// constructor for the pair class
	public Pair(int myData, int myHeight)
	{
		data = myData;
		height = myHeight;
	}

	public int getData()
	{
		return data;
	}

	public int getHeight()
	{
		return height;
	}

	public void setData(int newData)
	{
		data = newData;
	}

	public void increaseHeight()
	{
		height++;
	}
}