const LOG = require('./logsFormat.js')


/**
 * It takes a set of queens and prints a solution to the console
 * @param queens - A set of numbers representing the row of each queen.
 * @returns A 2D array of the solution
 */
function printSolution(queens){

    /* Creating an array of the alphabet, and then mapping it to the ASCII code. */
    const alphabetASCII = Array.from(Array(queens.size)).map((e, i) => i + 65);
    const alphabet = alphabetASCII.map((x) => ` ${String.fromCharCode(x)}`);

    /* Creating a variable called isCellColored and setting it to true. Creating an empty array
    called solution. */
    let isCellColored = true;
    let solution = []

    /* Printing to the console. */
    console.log(`${LOG.fg.magenta}---- This is your solution ----${LOG.reset}`);
    console.log(`  `, ...alphabet);

    /* Printing the solution to the console. */
    for(let i=0; i<queens.size; i++)
    {
        /* Creating an iterator for the queens set, creating a new array for the solution, and then
        checking if the size of the queens set is even. If it is, then it is setting isCellColored to
        false. */
        let iterator = queens.values();
        solution[i] = new Array();
        queens.size % 2 == 0 ? isCellColored = !isCellColored : {}

        for(let j = 0; j<queens.size; j++)
        {
            /* Inverting the value of isCellColored. */
            isCellColored = !isCellColored

            /* Checking if the value of the iterator is equal to the value of i. If it is, then it is
            pushing the value of 1 to the solution array. */
            if( iterator.next().value == i ) 
            {
                solution[i].push(`${LOG.bg.red}${isCellColored ? LOG.fg.black : LOG.fg.white} 1`) 
                continue;
            } 
            
            /* Pushing a string to the solution array. */
            solution[i].push(`${isCellColored? LOG.bg.white : LOG.bg.black}${isCellColored? LOG.fg.black : LOG.fg.white} 0`)
        }
        console.log(...solution[i], `${LOG.reset}`);
    }

    /* Returning the solution array. */
    return solution
}

module.exports = printSolution