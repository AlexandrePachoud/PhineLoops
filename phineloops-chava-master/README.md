# phineloops-chava
_phineloops-chava created by GitHub Classroom_

phineloops-chava is a project created for the course called Oriented-Object Programming in Master 1 MIAGE at Paris-Dauphine University.

The game consists of connecting all the sides of all the pieces of a board game.

There are different parts in this project:
1. Generation: Creation of a new random game
2. Solver: From a game given in argument, we are trying to solve it, making all the pieces connected

## Running the project
Explain how to run the project

#### Command
The command to execute the project is `java -jar phineloops-1.0-jar-with-dependencies.jar`

#### Options
Some options can be added to specify some preferences:

- _generate_ : Generate a grid of size height x width

`java -jar projet.jar --g widthxweight`

- _check_ : Check whether the grid in _file_ is solved

`java -jar projet.jar --c file`

- _solve_ : Solve the grid stored in _file_

`java -jar projet.jar --s file`

- _output_ : Store the generated or solved grid in file
(Use only with --generate and --solve)

`java -jar projet.jar --g widthxweight --o file`

- _threads_ : Maximum number of solver threads
(Use only with --solve)

`java -jar projet.jar --s file --t numberOfThreads`

- _nbcc_ : Maximum number of connected components
(Use only with --generate)

`java -jar projet.jar --g widthxweight --nbcc numberConnectedComponents`

- _gui_ : Run with the graphic user interface

`java -jar projet.jar --G`

- _help_ : Display this help

`java -jar projet.jar --h`

## Contributors
- Azzaz Myriam
- Dauvergne Sidney
- Pachoud Alexandre

