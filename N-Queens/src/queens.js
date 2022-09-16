/**
 * fillableRows method creates an array of numbers from 0 to the board size
 * @param boardSize - The size of the board.
 * @returns An array of numbers from 0 to the boardSize
 */
function fillableRows(boardSize)
{
    let rowsAvailable = new Array();
    for(let index = 0; index < boardSize; index++)
    {
        rowsAvailable.push(index);
    }
    return rowsAvailable
}

/**
 * If the row is not already occupied by a queen, and the positive and negative diagonals are not
 * occupied by a queen, then the queen can be placed in the row
 * @param boardState - the current state of the board
 * @param row - the row we're currently on
 * @param column - the column we're currently on
 * @param boardSize - The size of the board.
 * @returns A boolean value.
 */
const validate = (boardState, row, column, boardSize) => {
    return  ( !boardState.queens.has(boardState.rowsAvailable[row]) &&
            !boardState.positiveDiagonal.has((boardSize - 1) - boardState.rowsAvailable[row] - column) &&
            !boardState.negativeDiagonal.has(column - boardState.rowsAvailable[row]) )
}

/**
 * placeQueen method takes a board state, a column, and a board size, and returns a boolean and a set of queens
 * @param boardState - The current state of the board.
 * @param column - The column we're currently placing a queen in.
 * @param boardSize - The size of the board.
 * @returns A boolean value and a set of queens.
 */
function placeQueen( boardState, column, boardSize )
{
    /* Create a variable called laps and set it to 0, create a variable called
    row and set it to a random number between 0 and the length of the available rows. */
    let laps = 0;
    let row = Math.floor(Math.random() * (boardState.rowsAvailable.length - 0));

    /* backtracking algorithm. */
    // Loop from the row to the length of rowsAvailable
    for(row; row < boardState.rowsAvailable.length; row++)
    {
        // If the position is a valid position
        if( validate(boardState, row, column, boardSize) )
        {

            /* Add the queen to the board, and remove the row from the available rows. */
            boardState.queens.add(boardState.rowsAvailable[row]);
            boardState.positiveDiagonal.add((boardSize-1)-boardState.rowsAvailable[row]-column);
            boardState.negativeDiagonal.add(column-boardState.rowsAvailable[row]);
            let [value] = boardState.rowsAvailable.splice(row,1);

            /* It checks if the size of the queens is equal to the board size, if it is, then it
            returns true and the queens. */
            if(boardState.queens.size == boardSize) return [true, boardState.queens]

            /* It calls the placeQueen method (itself) */
            var [status, returnedQueens] = placeQueen(boardState, column+1, boardSize)

            /* It's checking if the status is true, if it is, then it's returning the status and the
            queens. */
            if(status) return [status, returnedQueens]

            /* It removes the queen from the board, and adds the row to the available rows. */
            boardState.rowsAvailable.splice(row,0, value)
            boardState.queens.delete(boardState.rowsAvailable[row]);
            boardState.positiveDiagonal.delete((boardSize-1)-boardState.rowsAvailable[row]-column);
            boardState.negativeDiagonal.delete(column-boardState.rowsAvailable[row]);
        }

        /* It checks if the row is equal to the length of the available rows, and if the laps are
        less than 1, then it's setting the row to -1 and adding 1 to the laps. */
        if(row + 1 == boardState.rowsAvailable.length && laps < 1) row = -1, laps += 1;
    }

    /* It's returning a boolean value and a set of queens. */
    return [false, boardState.queens]
}

/**
 * We start with an empty board, and we place a queen in the first column. Then we place a queen in the
 * second column, and so on. If we can't place a queen in a column, we backtrack and try again
 * @param boardSize - The size of the board.
 * @returns It's being returned an array of arrays, each array represents a row and each element of the
 * array represents a column.
 */
function Queens(boardSize)
{

    /* It checks if the board size is less than 4 and it's different of 1, if it is, then it's
    returning a string. */
    if(boardSize<4 && boardSize != 1) return `Not posible! ${boardSize} < 4 and it's different of 1`

    /* It creates the initial state of the board. */
    let rowsAvailable = fillableRows(boardSize);
    let column = 0
    var negativeDiagonal = new Set(); // [ -|n-1|, n-1 ]
    var positiveDiagonal = new Set(); // [ -|n-1|, n-1 ]
    var queens = new Set(); // Max length = n

    const state = {
        rowsAvailable: rowsAvailable,
        negativeDiagonal,
        positiveDiagonal,
        queens
    }

    /* It calls the placeQueen method. */
    let [status, returnedQueens] = placeQueen(state, column, boardSize)

    /* It checks if the status is false, if it is, then it's returning a string, if it's not, then
    it's returning the queens. */
    return !status ? 'It was not posible' : returnedQueens;
}

module.exports = Queens