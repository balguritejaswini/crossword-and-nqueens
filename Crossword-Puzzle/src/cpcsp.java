import java.io.*;
import java.util.*;

public class cpcsp {
    public static void main(String args[]) throws FileNotFoundException, CloneNotSupportedException
	{
        // Calling the cpSolver function with the first two arguments passed to the program.
		cpSolver(args[0], args[1]);
    }

    public static void cpSolver(String crosswordBoardName, String wordListName) throws FileNotFoundException, CloneNotSupportedException
	{
		// The code reads the file and adds all the words to an array list.
		File readFile = new File(wordListName);
		Scanner inputReader = new Scanner(readFile);
        ArrayList<String> listOfWords = new ArrayList<String>();
		while(inputReader.hasNext()) {
			String currentLine = inputReader.nextLine();
			if (currentLine.chars().allMatch(Character::isLetter)) {
				listOfWords.add(currentLine);
			}
		}
		inputReader.close();
		
		// The code reads the file and counts the number of lines and commas in the file.
		File readBoard = new File(crosswordBoardName);
		Scanner inputBoard = new Scanner(readBoard);
		int noOfLines = 0;
		int noOfCommas = 0;
		while(inputBoard.hasNext()) 
		{
			String currentLine = inputBoard.nextLine();
			noOfLines++;
			noOfCommas = 0;
			for(int i=0;i<currentLine.length();i++)
			{
				if(currentLine.charAt(i)==',')
				{
					noOfCommas++;
				}
			}
		}
		inputBoard.close();

		// The below code reads the file and stores it in a 2D array.
		Scanner inputBoardNew = new Scanner(readBoard);
		int index = 0;
		char board[][] = new char[noOfLines][noOfCommas+1];
		while(inputBoardNew.hasNext()) 
		{
			String currentLine = inputBoardNew.nextLine();
			String currLineArr[] = currentLine.split(",");

			for(int i=0;i<currLineArr.length;i++)
			{
				board[index][i] = currLineArr[i].charAt(0);
			}
			index++;
		}
		inputBoardNew.close();

		// The below code creates a hashmap of Combination objects. The key is the size of the word and
		// the value is the Combination object.
		HashMap<Integer, Combination> combinationBySize = new HashMap<Integer, Combination>();
		for (int i = 0; i < listOfWords.size(); i++) 
		{

			String word = listOfWords.get(i);
			int size = word.length();

			if (combinationBySize.containsKey(size)) 
			{
				Combination combination = combinationBySize.get(size);
				combination.wordList.add(word.toLowerCase());
			} 
			else 
			{
				Combination combination = new Combination(size);
				combination.wordList.add(word.toLowerCase());
				combinationBySize.put(size, combination);
			}
		}

		// Finding all the combinations of the given size.
		for (Integer size : combinationBySize.keySet()) {
			combinationBySize.get(size).findALLCombinations();
		}

		// Getting the horizontal and vertical placements of the words.
		List<Placement> horizontalPlacements = new ArrayList<Placement>();
		List<Placement> verticalPlacements = new ArrayList<Placement>();
		getHorizontalPlacements(horizontalPlacements, board, combinationBySize);
		getVerticalPlacements(verticalPlacements, board, combinationBySize);

		// Finding the intersections between the horizontal and vertical placements.
		for (int i = 0; i < horizontalPlacements.size(); i++) 
		{
			for (int j = 0; j < verticalPlacements.size(); j++) 
			{
				int[] charPosition = horizontalPlacements.get(i).intersects(verticalPlacements.get(j));
				if (charPosition != null) 
				{
					horizontalPlacements.get(i).crossConnection.add(new Intersection(verticalPlacements.get(j).id, charPosition[0], charPosition[1]));
				}
			}
			horizontalPlacements.get(i).mostConstrainingPlacementHeuristic = horizontalPlacements.get(i).crossConnection.size();
		}

		// Finding the intersections between the vertical and horizontal placements.
		for (int i = 0; i < verticalPlacements.size(); i++) 
		{
			for (int j = 0; j < horizontalPlacements.size(); j++) 
			{
				int[] charPosition = verticalPlacements.get(i).intersects(horizontalPlacements.get(j));
				if (charPosition != null) 
				{
					verticalPlacements.get(i).crossConnection.add(new Intersection(horizontalPlacements.get(j).id, charPosition[0], charPosition[1]));
				}
			}
			verticalPlacements.get(i).mostConstrainingPlacementHeuristic = verticalPlacements.get(i).crossConnection.size();
		}

		// Creating a new board state with the horizontal and vertical placements.
		ArrayList<Placement> beginState = new ArrayList<Placement>();
		BoardState initialS = new BoardState(beginState);
		beginState.addAll(horizontalPlacements);
		beginState.addAll(verticalPlacements);

		// Sorting the beginState list in descending order based on the mostConstrainingPlacementHeuristic
		// value.
		Collections.sort(beginState, (a,b) -> b.mostConstrainingPlacementHeuristic - a.mostConstrainingPlacementHeuristic);

		// Creating a copy of the beginState ArrayList.
		ArrayList<Placement> beginStateCopyTemp = new ArrayList<Placement>();
		for (Placement placement : beginState) {
			beginStateCopyTemp.add((Placement) placement.clone());
		}
		ArrayList<Placement> beginStateCopy = new ArrayList<Placement>(beginStateCopyTemp);

		// Sorting the beginStateCopy list by the id field.
		Collections.sort(beginStateCopy, (a,b) -> a.id - b.id);

		// Creating a stack of board states and pushing the initial state onto the stack. It is also creating
		// a variable to keep track of the number of backtracks and a variable to keep track of the current
		// crossword board. It is also creating a variable to keep track of the start time.
		Stack<BoardState> currentboardState = new Stack<BoardState>();
		currentboardState.push(initialS);
		int backTrack = 0;
		char[][] currentCrossWordBoard;
		long startTime = System.currentTimeMillis();

		while (!currentboardState.isEmpty()) {
			
			// The code is creating a temporary array list of placements.
			ArrayList<Placement> tempPlacements = new ArrayList<Placement>();
			currentCrossWordBoard = displayPresentState(currentboardState.peek(), board);

			// The code is checking if the current board is solved or not. If it is solved, it prints the
			// solution and breaks out of the loop.
			if (isBoardSolved(currentCrossWordBoard)) 
			{
				System.out.println("Solution Found:\n");
				// Function to print the crossword board.
				displayBoard(currentCrossWordBoard);
				System.out.println();
				ArrayList<String> hors = new ArrayList<String>();
				ArrayList<String> vers = new ArrayList<String>();
				for (int i = 0; i < currentboardState.peek().placements.size(); i++) 
				{
					if (currentboardState.peek().placements.get(i).startXCoord == currentboardState.peek().placements.get(i).endXCoord) 
					{
						hors.add(currentboardState.peek().placements.get(i).currentPlacement);
					} 
					else
						vers.add(currentboardState.peek().placements.get(i).currentPlacement);
				}
				break;
			}

			// Cloning the placements of the current board state.
			for (Placement placement : currentboardState.peek().placements) 
			{
				tempPlacements.add((Placement) placement.clone());
			}

			// Sorting the arraylist by id.
			ArrayList<Placement> tempPlacementsCopy = new ArrayList<Placement>(tempPlacements);
			Collections.sort(tempPlacementsCopy, (a,b) -> a.id - b.id);

			// Finding the placement with the lowest most constrained placement heuristic.
			Placement selectedPlacement = tempPlacements.get(0);
			int currentMCPS = selectedPlacement.mostConstrainingPlacementHeuristic;
			int mrvIndex = 0;
			int mrvPlacement = selectedPlacement.mostConstrainedPlacementHeuristic;

			// Finding the most constrained variable.
			for (int k = 0; k < tempPlacements.size(); k++) 
			{
				// Finding the most constrained placement heuristic.
				if (tempPlacements.get(k).mostConstrainingPlacementHeuristic == currentMCPS && tempPlacements.get(k).combination != null) 
				{
					// Finding the most constrained placement.
					if (tempPlacements.get(k).mostConstrainedPlacementHeuristic <= mrvPlacement) 
					{
						mrvIndex = k;
						mrvPlacement = tempPlacements.get(k).mostConstrainedPlacementHeuristic;
					}
				} 
				else
					break;
			}

			// Getting the placement with the minimum remaining values.
			selectedPlacement = tempPlacements.get(mrvIndex);

			// The code is checking if the most constrained placement heuristic is less than or equal to
			// 0. If it is, then it clears the current board state and breaks out of the loop.
			if (selectedPlacement.mostConstrainedPlacementHeuristic <= 0) 
			{
				currentboardState.clear();
				break;
			}

			// Adding all the words from the current board state to the used word list.
			ArrayList<String> usedWordList = new ArrayList<String>();
			usedWordList.addAll(currentboardState.peek().usedWords);
			usedWordList.addAll(beginStateCopy.get(selectedPlacement.id).words);

			// Assigning a value to the selected placement.
			ArrayList<String> values = selectedPlacement.assignAValue(usedWordList, tempPlacementsCopy);

			// The below code is checking if the values array is null. If it is null, then it clears the words
			// array, pops the current board state, and increments the backtrack variable.
			if (values == null) 
			{
				beginStateCopy.get(selectedPlacement.id).words.clear();
				currentboardState.pop();
				backTrack++;
				continue;
			}

			boolean isStuck = false;

			// The code is trying to assign a word to the placement with the least number of possible
			// words.
			for (int i = 0; i < values.size(); i++) 
			{

				ArrayList<Placement> t1 = new ArrayList<Placement>();

				for (Placement placement : currentboardState.peek().placements) 
				{
					t1.add((Placement) placement.clone());
				}
				Placement selected = t1.get(mrvIndex);
				ArrayList<Placement> t2 = new ArrayList<Placement>(t1);
				Collections.sort(t2, (a,b) -> a.id - b.id);
				boolean wordAlignmentPossible = selected.addNeighborConnection(values.get(i), t2, usedWordList);

				// The code is checking if the word can be placed on the board. If it can, it will place the
				// word on the board and add it to the list of words. It will then sort the list of words by the
				// most constraining placement heuristic. It will then create a new board state with the new list
				// of words and push it onto the stack.
				if (wordAlignmentPossible) 
				{
					selected.currentPlacement = values.get(i);
					beginStateCopy.get(selected.id).words.add(values.get(i));
					Collections.sort(t1, (a,b) -> b.mostConstrainingPlacementHeuristic - a.mostConstrainingPlacementHeuristic);
					BoardState nextState = new BoardState(t1);
					nextState.selectedWord = selected.currentPlacement;
					currentboardState.push(nextState);
					isStuck = true;
					break;
				}
			}

			// Checking if the current placement is stuck. If it is stuck, it will clear the words from the
			// current placement and pop the current board state.
			if (!isStuck) 
			{
				beginStateCopy.get(selectedPlacement.id).words.clear();
				currentboardState.pop();
				backTrack++;
			}
		}

		long endTime = System.currentTimeMillis();

		// The code is checking if the current board state is empty. If it is empty, then it prints out
		// that the solution doesn't exist and the total number of backtracks and the total time taken. If it
		// is not empty, then it prints out the total number of backtracks and the total time taken.
		if (currentboardState.isEmpty()) 
		{
			System.out.println("Solution doesn't exists");
			System.out.println("Total no. of backTracks: "+backTrack);
			System.out.println("Total Time Taken: " + (endTime - startTime) + "ms");
		} 
		else 
		{
			System.out.println("Total no. of backTracks: "+backTrack);
			System.out.println("Total Time Taken: " + (endTime - startTime) + "ms");
		}
    }

	/**
	 * It takes a crossword board and a map of combinations by size, and returns a list of all the
	 * possible horizontal placements on the board
	 * 
	 * @param horizontalPlacements The list of horizontal placements that will be populated by this
	 * method.
	 * @param board The crossword board
	 * @param combinationBySize A HashMap that maps the size of a combination to the combination itself.
	 */
	public static void getHorizontalPlacements(List<Placement> horizontalPlacements, char[][] board, HashMap<Integer, Combination> combinationBySize) 
	{
		for (int i = 0; i < board.length; i++) 
		{
			int position = 0;
			// Finding the horizontal placements.
			for (int j = 0; j < board[i].length; j++) 
			{
				if (board[i][j] == '*') 
				{
					if (position < j) 
					{
						int size = j - position;
						if (size != 1) 
						{
							Combination combination = combinationBySize.get(size);
							if (combination != null) 
							{
								Placement placement = new Placement(i, position, i, j - 1, combination);
								horizontalPlacements.add(placement);
							}
						}
					}
					position = j + 1;
				}
			}
			// Checking if the position is not equal to the length of the crossword board. If it is not, then it
			// is checking if the size is not equal to 1. If it is not, then it is getting the combination by
			// size and creating a new placement.
			if (position != board[i].length) 
			{
				int size = board[i].length - position;
				if (size != 1) 
				{
					Combination combination = combinationBySize.get(size);
					if (combination != null) 
					{
						Placement placement = new Placement(i, position, i, board[i].length - 1, combination);
						horizontalPlacements.add(placement);
					}
				}
			}
		}
	}

	/**
	 * For each column, find the start and end of each consecutive sequence of stars, and if the size of
	 * the sequence is greater than 1, add a placement to the list of vertical placements
	 * 
	 * @param verticalPlacements The list of vertical placements that will be populated by this method.
	 * @param board The crossword board
	 * @param combinationBySize A HashMap that maps the size of a combination to the combination itself.
	 */
	public static void getVerticalPlacements(List<Placement> verticalPlacements, char[][] board, HashMap<Integer, Combination> combinationBySize) 
	{
		for (int i = 0; i < board[0].length; i++) 
		{
			int position = 0;
			// Finding the vertical placements.
			for (int j = 0; j < board.length; j++) 
			{
				if (board[j][i] == '*') 
				{
					if (position < j) 
					{
						int size = j - position;
						if (size != 1) 
						{
							Combination combination = combinationBySize.get(size);
							if (combination != null) 
							{
								Placement placement = new Placement(position, i, j - 1, i, combination);
								verticalPlacements.add(placement);
							}
						}
					}
					position = j + 1;
				}
			}
			// Checking if the position is not equal to the length of the crossword board. If it is not, then it
			// is getting the size of the crossword board and checking if the size is not equal to 1. If it is
			// not, then it is getting the combination by size and checking if the combination is not null. If
			// it is not, then it is creating a new placement and adding it to the vertical placements.
			if (position != board.length) 
			{
				int size = board.length - position;
				if (size != 1) 
				{
					Combination combination = combinationBySize.get(size);
					if (combination != null) 
					{
						Placement placement = new Placement(position, i, board.length - 1, i, combination);
						verticalPlacements.add(placement);
					}
				}
			}
		}
	}
	
	/**
	 * This function takes in a 2D array of characters and prints out the contents of the array in a
	 * formatted way
	 * 
	 * @param board The crossword board that we are going to be filling in.
	 */
	public static void displayBoard(char[][] board) 
	{
		// The below code is printing the crossword board.
		for (int i = 0; i < board.length; i++) 
		{
			for (int j = 0; j < board[0].length; j++) 
			{
				if(j==board[0].length-1)
				{
					System.out.print(board[i][j]);
					continue;
				}
				System.out.print(board[i][j] + ",");
			}
			System.out.println();
		}
	}

	/**
	 * If any of the elements in the 2D array are equal to a space, return false. Otherwise, return true
	 * 
	 * @param board The crossword board that we are trying to solve.
	 * @return A boolean value.
	 */
	public static boolean isBoardSolved(char[][] board) 
	{
		// Checking if the crossword board is full.
		for (int i = 0; i < board.length; i++) 
		{
			for (int j = 0; j < board[0].length; j++) 
			{
				if (board[i][j] == ' ')
					return false;
			}
		}
		return true;
	}

	/**
	 * This function takes in a 2D array of characters and a placement object, and assigns the placement's
	 * currentPlacement string to the 2D array
	 * 
	 * @param board the board that we're placing the word on
	 * @param placement The placement object that contains the word to be placed, the start and end
	 * coordinates, and the direction of the word.
	 */
	public static void assign(char[][] board, Placement placement) 
	{
		// The below code is checking if the placement is horizontal or vertical. If it is horizontal, it
		// will place the word on the board horizontally. If it is vertical, it will place the word on the
		// board vertically.
		if (placement.startXCoord == placement.endXCoord) 
		{
			for (int i = 0; i < placement.currentPlacement.length(); i++) 
			{
				board[placement.startXCoord][placement.startYCoord + i] = placement.currentPlacement.charAt(i);
			}
		} 
		else 
		{
			for (int i = 0; i < placement.currentPlacement.length(); i++) 
			{
				board[placement.startXCoord + i][placement.startYCoord] = placement.currentPlacement.charAt(i);
			}
		}
	}

	/**
	 * This function takes in a board state and a 2D array of characters and returns a 2D array of
	 * characters
	 * 
	 * @param state The current state of the board
	 * @param tempArr This is the original crossword board.
	 * @return The newcrosswordBoard is being returned.
	 */
	public static char[][] displayPresentState(BoardState state, char[][] tempArr) 
	{

		// The below code is creating a new board with the same dimensions as the original board. It then
		// assigns the new board with the placements that have been made.
		char[][] newcrosswordBoard = new char[tempArr.length][tempArr[0].length];
		for (int i = 0; i < tempArr.length; i++) 
		{
			for (int j = 0; j < tempArr[0].length; j++) 
			{
				newcrosswordBoard[i][j] = tempArr[i][j];
			}
		}

		ArrayList<Placement> currentPlacement = state.placements;

		for (int i = 0; i < currentPlacement.size(); i++) 
		{
			Placement placement = currentPlacement.get(i);
			assign(newcrosswordBoard, placement);
		}
		displayBoard(newcrosswordBoard);
		System.out.println();
		System.out.println("******************************\n");
		return newcrosswordBoard;
	}
}