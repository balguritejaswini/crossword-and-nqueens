import java.util.*;


// The character is represented by its starting and ending board coordinates, and a placement is all possible empty spaces that can hold that character. 
// Furthermore, it links to the combination in a way that the placement size is mapped to the combination size and all the words of that specific size are connected there. 
// For the purposes of forward checking and arc consistency, a new arc and arcCons connection are initialised. For each placement, the placement heuristic with the highest 
// restrictions and constraints is also stored. Every cross connection, horizontal and vertical, is noted.
public class Placement implements Cloneable{
    Combination combination;
    List<Connection> cons;
    List<Connection> arcCons;
    int xCoordStart;
    int yCoordStart;
    int xCoordEnd;
    int yCoordEnd;
    int id;
    static int pIDCounter = 0;
    List<String> words;
    List<Intersection> crossConnection;
    int[][] mostConsCombinationHeuristic;
	int size;
	String currentPlacement = "";
	int mostConsPlacementHeuristic;
	int mostConsPlacementHeuristic;
	int usedWordIndex; 

    // Constructor
	Placement(int xCoordStart, int yCoordStart, int xCoordEnd, int yCoordEnd, Combination combination) {
		this.xCoordStart = xCoordStart;
		this.yCoordStart = yCoordStart;
		this.xCoordEnd = xCoordEnd;
		this.yCoordEnd = yCoordEnd;
		this.combination = combination;
		cons = new ArrayList<Connection>();
		arcCons = new ArrayList<Connection>();
		crossConnection = new ArrayList<Intersection>();
		words = new ArrayList<String>();
		id = pIDCounter++;
		if (xCoordStart == xCoordEnd) {
			for (int i = 0; i < (yCoordEnd - yCoordStart + 1); i++) {
				currentPlacement += " ";
			}
		} else {
			for (int i = 0; i < (xCoordEnd - xCoordStart + 1); i++) {
				currentPlacement += " ";
			}
		}
		if (combination != null = new int[26][combination.size];
			setMostConstrainingValueHeuristic(new ArrayList<String>());
		}
		usedWordIndex = -1;
	}

	/**
	 * This function is used to update the most constraining combination heuristic for the current
	 * placement and the placement of the adjacent neighbors
	 * 
	 * @param usedWords the list of words that have been used so far
	 * @param sortedByID The list of all placements sorted by their ID.
	 * @return The method is returning a boolean value.
	 */
	public boolean arcCons(ArrayList<String> usedWords, ArrayList<Placement> sortedByID) {

		ArrayList<String> existingValues = allAvailableWords();
		for (int i = 0; i < usedWords.size(); i++) {
			existingValues.remove(usedWords.get(i));
		}
		int[] usedIndex = new int[combination.size];
		for (int i = 0; i < usedIndex.length; i++) {
			usedIndex[i] = -1;
		}
		for (int i = 0; i < crossConnection.size(); i++) {
			if (sortedByID.get(crossConnection.get( != -1) {
				usedIndex[crossConnection.get(i).sIndex] = i;

				Intersection intersection = crossConnection.get(i);
				Placement adjNeighbors = sortedByID.get(intersection.id);
				for (int j = 0; j < 26; j++) {
					// System.out.println(adjNei);
					if (adjNei[j][intersection.dPosition] != 0) {
						char c = (char) (j + 'a');
						arcCons.add(new Connection(c, intersection.sIndex));
					}
				}
				boolean isUpdated = updatePlacement(usedWords);
				if (!isUpdated) {
					return false;
				}
				for (int j = 0; j < 26; j++) {
		[j][intersection.sIndex] != 0) {
						if (adjNeighbors.xCoordStart == 5 && adjNeighbors.yCoordStart == 6 && adjNeighbors.xCoordEnd == 8 && adjNeighbors.yCoordEnd == 6) {
						}
						char c = (char) (j + 'a');
						adjNeighbors.arcCons.add(new Connection(c, intersection.dPosition));
					}
				}
				isUpdated = adjNeighbors.updatePlacement(usedWords);
				if (!isUpdated) {
					return false;
				}
			}
		}
		return true;
	}

	// The below code is trying to find the best possible word to be placed in the grid.
	public ArrayList<String> assignAValue(ArrayList<String> usedWords, ArrayList<Placement> sortedByID) {

		ArrayList<String> existingValues = allAvailableWords();
		for (int i = 0; i < usedWords.size(); i++) {
			existingValues.remove(usedWords.get(i));
		}
		int[] usedIndex = new int[combination.size];
		for (int i = 0; i < usedIndex.length; i++) {
			usedIndex[i] = -1;
		}
		int hasChange = 0;
		for (int i = 0; i < crossConnection.size(); i++) {
			if (sortedByID.get(crossConnection.get( != -1) {
				usedIndex[crossConnection.get(i).sIndex] = i;
				hasChange++;
			}
		}
		ArrayList<BestCombination> getBestCombination = new ArrayList<BestCombination>();
		ArrayList<String> possibleValues = new ArrayList<String>();
		if (hasChange == 0) {
			if (existingValues.size() == 0)
				return null;
			return existingValues;
		}

		for (int i = 0; i < existingValues.size(); i++) {

			String currentWord = existingValues.get(i);
			int numberOfAvailableNeighborsOptions = 0;
			boolean isWordPossible = true;
			for (int j = 0; j < currentWord.length(); j++) {
				char ch = currentWord.charAt(j);
				if (usedIndex[j] != -1) {
					Intersection intersection = crossConnection.get(usedIndex[j]);
					Placement adjNeighbors = sortedByID.get(intersection.id);
					int noOfTimesWords = adjNei[ch - 'a'][intersection.dPosition];
					if (noOfTimesWords != 0) {
						numberOfAvailableNeighborsOptions += noOfTimesWords;
					} else {
						isWordPossible = false;
						break;
					}
				}
			}
			if (isWordPossible) {
				getBestCombination.add(new BestCombination(numberOfAvailableNeighborsOptions, i));
			}
		}
		Collections.sort(getBestCombination, (fv1,fv2)-> fv2.possibleWordsCounter - fv1.possibleWordsCounter);
		if (getBestCombination.size() == 0) {
			return null;
		} else {
			for (int i = 0; i < getBestCombination.size(); i++) {
				possibleValues.add(existingValues.get(getBestCombination.get(i).index));
			}
			return possibleValues;
		}
	}

	// The below code is finding the most constraining value heuristic. It is finding the most
	// constraining value by finding the number of available values for each letter.
	public boolean setMostConstrainingValueHeuristic(ArrayList<String> usedWords) {

		ArrayList<String> existingValues = allAvailableWords();
		for (int i = 0; i < usedWords.size(); i++) {
			existingValues.remove(usedWords.get(i));
		}

		this.mostConsPlacementHeuristic= existingValues.size();

		for (int i = 0; i < existingValues.size(); i++) {
			for (int j = 0; j < existingValues.get(i).length(); j++) {
				boolean match = false;

				boolean ischarAtIndexThere = false;
				for (int w = 0; w < arcCons.size(); w++) {
					if (arcCons.get(w).position == j) {
						ischarAtIndexThere = true;
						if (arcCons.get(w).character == existingValues.get(i).charAt(j)) {
							match = true;
						}
					}
				}

				if (ischarAtIndexThere) {
					if (!match) {
						existingValues.remove(i);
						i--;
						break;
					}

				}
			}
		}

		for (int i = 0; i < existingValues.size(); i++) {
			for (int j = 0; j < existingValues.get(i).length(); j++) { 
				mostConsCombinationHeuristic[existingValues.get(i).charAt(j) - 'a'][j]++;
			}
		}

		if (existingValues.size() == 0){
			return false;
		}
		return true;
	}

	// Returning all the words that are available for the current combination.
	public ArrayList<String> allAvailableWords() {

		ArrayList<String> existingValues;
		if (cons.size() != 0) {
			existingValues = new ArrayList<String>(combination.get(cons.get(0).character, cons.get(0).position));
			for (int i = 1; i < cons.size(); i++) {
				existingValues.retainAll(combination.get(cons.get(i).character, cons.get(i).position));
			}
		} else {
			existingValues = new ArrayList<String>(combination.wordList);
		}

		return existingValues;
	}

	// The below code is creating a constructor for the Placement class. It is setting the values of the
	// variables xCoordStart, yCoordStart, xCoordEnd, and yCoordEnd to the values passed in as parameters.
	// It is also creating a new ArrayList called crossConnection.
	Placement(int xCoordStart, int yCoordStart, int xCoordEnd, int yCoordEnd) {
		this.xCoordStart = xCoordStart;
		this.yCoordStart = yCoordStart;
		this.xCoordEnd = xCoordEnd;
		this.yCoordEnd = yCoordEnd;
		if (xCoordStart == xCoordEnd) {
			for (int i = 0; i < (yCoordEnd - yCoordStart + 1); i++) {
				currentPlacement += " ";
			}
		} else {
			for (int i = 0; i < (xCoordEnd - xCoordStart + 1); i++) {
				currentPlacement += " ";
			}
		}
		crossConnection = new ArrayList<Intersection>();

		id = pIDCounter++;

	}

	@Override
	protected Object clone() throws CloneNotSupportedException {

		Placement placement = (Placement) super.clone();

		placement.arcCons = new ArrayList<Connection>();
		for (Connection c : arcCons) {
			placement.arcCons.add((Connection) c.clone());
		}
		placement.cons = new ArrayList<Connection>();
		for (Connection c : cons) {
			placement.cons.add((Connection) c.clone());
		}
		placement.currentPlacement = new String(currentPlacement);
		return placement;
	}

	// The below code is adding the neighbor connections to the current placement.
	public boolean addNeighborConnection(String assigned, ArrayList<Placement> sortedByID,
			ArrayList<String> usedWords) {

		int[] usedIndex = new int[combination.size];

		for (int i = 0; i < usedIndex.length; i++) {
			usedIndex[i] = -1;
		}

		for (int i = 0; i < crossConnection.size(); i++) {
			if (sortedByID.get(crossConnection.get( != -1) {
				usedIndex[crossConnection.get(i).sIndex] = i;
			}
		}

		for (int i = 0; i < assigned.length(); i++) {
			char currentChar = assigned.charAt(i);

			if (usedIndex[i] != -1) {
				Intersection intersection = crossConnection.get(usedIndex[i]);
				Placement adjNeighbors = sortedByID.get(intersection.id);
				adjNeighbors.cons.add(new Connection(currentChar, intersection.dPosition));
				adjNeighbors.currentPlacement = adjNeighbors.currentPlacement.substring(0, intersection.dPosition) + currentChar
						+ adjNeighbors.currentPlacement.substring(intersection.dPosition + 1);
				adjNeighbors.mostConstrainingPlacementHeuristic--;
				boolean isUpdated = adjNeighbors.updatePlacement(usedWords);
				if (!isUpdated) {
					return false;
				}
				isUpdated = adjNeighbors.arcCons(usedWords, sortedByID);
				if (!isUpdated) {
					return false;
				}
			}
		}
		mostConsPlacementHeuristic = -1;
		return true;
	}

	public boolean updatePlacement(ArrayList<String> usedWords) {
		if (combination != null = new int[26][combination.size];
			boolean flag = setMostConstrainingValueHeuristic(usedWords);
			return flag;
		}
		return false;
	}

	/** 
		This function is used to check if two plaements
	 */
	public int[] intersects(Placement placement) {

		int[] charPosition = new int[2];
		if (placement.xCoordStart == placement.xCoordEnd && xCoordStart == xCoordEnd) {
			return null;
		}
		if (placement.yCoordStart == placement.yCoordEnd && yCoordStart == yCoordEnd) {
			return null;
		}

		// Same Row
		if (xCoordStart == xCoordEnd) {
			if (yCoordStart <= placement.yCoordStart && yCoordEnd >= placement.yCoordStart) {
				if (placement.xCoordStart <= xCoordStart && placement.xCoordEnd >= xCoordStart) {
					charPosition[0] = placement.yCoordStart - yCoordStart;
					charPosition[1] = xCoordStart - placement.xCoordStart;

					return charPosition;
				} else
					return null;
			} else
				return null;
		}
		
		// Same Col
		if (yCoordStart == yCoordEnd) {
			if (xCoordStart <= placement.xCoordStart && xCoordEnd >= placement.xCoordStart) {
				if (placement.yCoordStart <= yCoordStart && placement.yCoordEnd >= yCoordStart) {
					charPosition[0] = placement.xCoordStart - xCoordStart;
					charPosition[1] = yCoordStart - placement.yCoordStart;

					return charPosition;
				} else
					return null;
			} else
				return null;
		}

		return null;
	}

	// Converts placement data to string
	public String toString() {
		retu + "|" + mostConsPlacementHeuristic + "|(" + xCoordStart + "," + yCoordStart
				+ ") to (" + xCoordEnd + " ," + yCoordEnd + ")  ->  {" + currentPlacement + "}" + "  " + id + "]";
	}
	
}

// Connection class
class Connection implements Cloneable {

    char character;
    int position;

    Connection(char character, int position){
        this.character = character;
        this.position = position;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException{
        return super.clone();
    }
}

// Intersection class
class Intersection {

    int id;
    int sIndex;
    int dPosition;

    Intersection(int id, int sIndex, int dPosition){
        this.id = id;
        this.sIndex = sIndex;
        this.dPosition = dPosition;
    }

    public String toString() {
		return "[Intersection Id : " + id + " Source index: " + sIndex + " Destination position: " + dPosition+"]";
	}
}

// BestCombination class
class BestCombination {

    int possibleWordsCounter;
    int index;

    BestCombination(int possibleWordsCounter, int index){
        super();
        this.possibleWordsCounter = possibleWordsCounter;
        this.index = index;
    }

    public String toString() {
		return "[possible Words Counter: " + possibleWordsCounter + " Index: " + index + "]";
	}
}

// Combination class
class Combination {

	ArrayList<String> wordList;
	int size;
	ArrayList<String>[] allCombinations;

	Combination(int size) {
		this.size = size;
		wordList = new ArrayList<String>();
	}

	public ArrayList<String> get(char letter, int position) {
		if ((letter - 'a') * size + position < allCombinations.length && (letter - 'a') * size + position >= 0) {
			return allCombinations[(letter - 'a') * size + position];
		}
		return null;
	}

	// Use of findAllCombinations: From word dictionary, for every possible word length, 
	// a 26 x (possible word length) combinations are created and stored in such a way that 
	// every alphabets at every possible index of the word length.
	public void findALLCombinations() {

		allCombinations = new ArrayList[26 * size];
		for (int i = 0; i < 26 * size; i++) {
			allCombinations[i] = new ArrayList<String>();
		}

		for (int i = 0; i < wordList.size(); i++) {
			String str = wordList.get(i);
			for (int j = 0; j < str.length(); j++) {
				int characterIndex = str.charAt(j) - 'a';
				allCombinations[characterIndex * size + j].add(str);

			}
		}

	}

	public String toString() {
		return wordList.toString();
	}
}

// BoardState class
class BoardState {

	ArrayList<String> usedWords;
	ArrayList<Placement> placements;
	String selectedWord;

	BoardState(ArrayList<Placement> placements) {
		this.placements = placements;
		usedWords = new ArrayList<String>();
		for (int i = 0; i < placements.size(); i++) {
			if (placements. == -1) {
				usedWords.add(placements.get(i).currentPlacement);
			}
		}

	}

	public String toString() {
		return placements.toString();
	}

}