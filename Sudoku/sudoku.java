/*
 * 	Egan Wall
 *  COP3503, Program 1
 *  Sudoku Puzzle Solver
 */


import java.util.*; // so we can use scanners and arraylists

public class sudoku 
{
	// constants for our dimensions
	public static final int LENGTH = 9; 
	public static final int WIDTH = 9;
	
	
	public static void main(String[] args) 
	{
		// initialize the scanner to read from standard in
		Scanner scanIn = new Scanner(System.in);

		int numCases = scanIn.nextInt();

		// System.out.println(numCases);

		int currCase = 1;

		while(currCase <= numCases)
		{
			System.out.printf("Test case %d:\n\n", currCase);

			// our 2D array to contain our sudoku puzzle
			int[][] puzzle = new int[LENGTH][WIDTH];

			for(int i = 0; i < LENGTH; i++)
			{
				for(int j = 0; j < LENGTH; j++)
				{
					puzzle[i][j] = scanIn.nextInt();
				}
			}

			// start timing 
			long start = System.currentTimeMillis();

			/*
				Now we'll just fill in spaces with only one possible value
			*/
			for(int i = 0; i < LENGTH; i++)
			{
				for(int j = 0; j < WIDTH; j++)
				{
					if(puzzle[i][j] == 0)
					{
						ArrayList<Integer> vals = possibleValues(i, j, puzzle);

						if(vals.size() == 1)
						{
							puzzle[i][j] = vals.get(0);
						}
					}
				}
			}

			puzzle = solveRec(puzzle, getNextRow(puzzle), getNextCol(puzzle));

			// stop timing
			long end = System.currentTimeMillis();
			long diff = end - start;

			//System.out.printf("Time: %d\n", diff);

			if(puzzle == null)
			{
				System.out.println("No solution possible.");
			} else
			{
				printPuzzle(puzzle);
			}

			System.out.printf("\n\n");

			currCase++;
		}

		scanIn.close();
	}
	
	/*
	 * 	This method will simply check if the puzzle is, in its current state,
	 * 	solved. It will first check if the sum of each row and column is correct
	 * 	as an attempt to short-circuit the brute-force nature of the method and 
	 * 	hopefully streamline it somewhat. If the sums all check out, then we will 
	 *  check for duplicates in the row and then the column. If no tests fail, it 
	 *  returns TRUE. If any fail, it returns FALSE.
	 */

	public static boolean isSolved(int[][] puzzle)
	{
		// first we can check for the sections being
		// solved

		for(int i = 0; i < LENGTH; i += 3)
		{
			for(int j = 0; j < WIDTH; j += 3)
			{
				if(!sectionSolved(i, j, puzzle))
				{
					return false;
				}
			}
		}

		// the next thing we can check is the sum of each row and column
		
		// this set of loops checks the row sums
		for(int i = 0; i < LENGTH; i++)
		{
			int sum = 0; 
			
			for(int j = 0; j < WIDTH; j++)
			{
				sum += puzzle[i][j];
			}

			// System.out.printf("The sum for row %d is %d.\n", i + 1, sum);
			
			// the sum of the integers 1-9 inclusive is 45, so before 
			// we check for duplicates we can simply check the sum.
			if(sum != 45)
			{
				// if the sum is not 45, then there's either a duplicate or a blank
				return false;
			}
		}
		
		// this set of loops checks the column sums
		for(int j = 0; j < WIDTH; j++)
		{
			int sum = 0; 
			
			for(int i = 0; i < LENGTH; i++)
			{
				sum += puzzle[i][j];
			}

			// System.out.printf("The sum for column %d is %d.\n", j + 1, sum);
			
			if(sum != 45)
			{
				// if we get the wrong sum, the puzzle is not solved yet
				return false;
			}
		}
		
		// if the puzzle passes all of these tests, then we will have to check 
		// for duplicates in each row/column. this will use a small arraylist for 
		// the row and another one for the column, because we can easily check to 
		// see if the arraylist already contains a certain value
		

		for(int i = 0; i < LENGTH; i++)
		{
			ArrayList<Integer> rowArray = new ArrayList<Integer>();

			for(int j = 0; j < WIDTH; j++)
			{
				if(rowArray.contains(puzzle[i][j]))
				{
					// System.out.printf("There is a duplicate at row %d (%d).\n", i + 1, puzzle[i][j]);
					return false;
				}

				// if it's not already there, add it to the arraylist
				rowArray.add(puzzle[i][j]);
			}
		}

		// and now we can do the columns
		for(int j = 0; j < WIDTH; j++)
		{
			ArrayList<Integer> colArray = new ArrayList<Integer>();

			for(int i = 0; i < LENGTH; i++)
			{
				if(colArray.contains(puzzle[i][j]))
				{
					// System.out.printf("There is a duplicate at column %d (%d).\n", j + 1, puzzle[i][j]);
					return false;
				}

				colArray.add(puzzle[i][j]);
			}
		}


		return true;
	}

	/*
		This function is a subroutine called by the isSolved() function
		to cehck whether the 3x3 region with the upper-left square at 
		row, col is solved. It uses an arraylist as a hash table 
		to check for duplicates of numbers.
	*/
	public static boolean sectionSolved(int row, int col, int[][] puzzle)
	{
		ArrayList<Integer> numArr = new ArrayList<Integer>();

		for(int i = row; i < row + 3; i++)
		{
			for(int j = col; j < col + 3; j++)
			{
				if(puzzle[i][j] == 0)
				{
					// System.out.printf("Blank square in section at %d, %d.\n", row, col);
					return false;
				}

				if(numArr.contains(puzzle[i][j]))
				{
					// System.out.printf("Section at %d, %d is not solved.\n", row, col);
					return false;
				}

				numArr.add(puzzle[i][j]);
			}
		}

		return true;
	}

	/*
		This function will simply print the contents of the puzzle
		array to standard output.
	*/
	public static void printPuzzle(int[][] puzzle)
	{
		for(int i = 0; i < LENGTH; i++)
		{
			for(int j = 0; j < WIDTH; j++)
			{
				System.out.print(puzzle[i][j] + " ");
			}

			System.out.println();
		}
	}

	/*
		Requires an x and y value as input, and returns all the
		possible integers that can be used to fill in the space at 
		puzzle[x][y]
	*/
	public static ArrayList<Integer> possibleValues(int row, int col, int[][] puzzle)
	{
		ArrayList<Integer> vals = new ArrayList<Integer>();
		ArrayList<Integer> pres = presentInSection(row, col, puzzle);
		
		// loop through the row and column, using the same hashtable technique from
		// our validation function to get values not in the row or column

		int presentVals[] = new int[WIDTH + 1];

		for(int i = 1; i < 10; i++)
		{
			if(pres.contains(i))
			{
				presentVals[i] = i;
			}
		}

		// System.out.printf("Calculating possible values for %d, %d...\n", row, col);

		for(int i = 0; i < WIDTH; i++)
		{
			// loop through the row (puzzle[row][i])
			if(puzzle[row][i] != 0)
			{
				presentVals[puzzle[row][i]] = puzzle[row][i];
			}

			// and the column(puzzle[i][col])
			if(puzzle[i][col] != 0)
			{
				presentVals[puzzle[i][col]] = puzzle[i][col];
			}
		}

		for(int i = 1; i < presentVals.length; i++)
		{
			if(presentVals[i] == 0)
			{
				vals.add(i);
				// System.out.printf("Adding %d to the list of possible values.\n", i);
			}
		}

		// System.out.println(vals);

		return vals;
	}

	/*
		This function will recursively solve the puzzle by filling in a single 
		possible value at each iteration, then backtracking after it finds a wrong
		value.
	*/
	public static int[][] solveRec(int[][] puzzle, int row, int col)
	{
		final int BLANK = 0; 

		int newCol, newRow;

		ArrayList<Integer> vals = possibleValues(row, col, puzzle);

		// if the puzzle is already solved, then we can return it
		if(isSolved(puzzle))
		{
			return puzzle;
		}

		// iterate through our arraylist of possible values
		for(int i : vals)
		{
			puzzle[row][col] = i;

			// System.out.printf("Trying %d at (%d, %d):\n", i, row, col);
			// printPuzzle(puzzle);
			// System.out.printf("\n\n\n");

			// figure out the next position
			newRow = getNextRow(puzzle);
			newCol = getNextCol(puzzle);

			// recursively call the function for the next iteration
			int[][] answer = solveRec(puzzle, newRow, newCol);

			// see if the answer is null
			if(answer != null)
			{
				// if not, return the new array
				return answer;
			}

			puzzle[row][col] = BLANK;
		}

		// if there is no solution, return NULL
		return null;
	}

	public static int getNextRow(int[][] puzzle)
	{
		for(int i = 0; i < LENGTH; i++)
		{
			for(int j = 0; j < WIDTH; j++)
			{
				if(puzzle[i][j] == 0)
				{
					return i;
				}
			}
		}

		return WIDTH - 1;
	}

	public static int getNextCol(int[][] puzzle)
	{
		for(int i = 0; i < LENGTH; i++)
		{
			for(int j = 0; j < WIDTH; j++)
			{
				if(puzzle[i][j] == 0)
				{
					return j;
				}
			}
		}

		return WIDTH - 1;
	}

	public static void printArray(int[] arr)
	{
		for(int j = 0; j < arr.length; j++)
		{
			// System.out.printf("%d ", arr[j]);
		}

		// System.out.println();
	}

	public static ArrayList<Integer> presentInSection(int row, int col, int[][] puzzle)
	{
		ArrayList<Integer> presentVals = new ArrayList<Integer>();

		int newRow = (row / 3) * 3;
		int newCol = (col / 3) * 3;

		for(int i = newRow; i < newRow + 3; i++)
		{
			for(int j = newCol; j < newCol + 3; j++)
			{
				if(!presentVals.contains(puzzle[i][j]) && puzzle[i][j] != 0)
					presentVals.add(puzzle[i][j]);
			}
		}

		// System.out.printf("Values present in the square: ");
		// System.out.println(presentVals);	

		return presentVals;
	}
}