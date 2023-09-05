# TextEditor
A simple and easy-to-use terminal-based text editor designed for the modification of text (.txt) documents. This editor is not tested with alternative file types nor has it been tested outside of a Windows 10 or 11 environment. Thus user mileage may vary depending on use cases.
## Usage
### How To
#### Initialization
When the program opens, the user will be prompted to input a single-string command, either 'link' or 'gap' which will define the internal data structure used when the program is run in this instance. Once a valid input is received, the user will be prompted by the primary mode of the application.
##### Utilization

### Commands
- Open the command list by entering 'help' as a single-string input. 
- Close the command list by entering 'Exit' as a single-string input when the command list is open.

| Command |                        Description                        |
|---------|:----------------------------------------------------------|
| o xxxxx |                 Open a file named "xxxxx".                |
| c       |   Save the current loaded data to the originating file.   |
| s xxxxx |        Save the current loaded data to a new file.        |
| e       | Edit the current-line, using the current cursor position. |
| r       |                 Move right one character.                 |
| r x     |                  Move right x characters.                 |
| l       |                  Move left one character.                 |
| l x     |                  Move left x characters.                  |
| d       |      Move down one character. Does not add new lines.     |
| d x     |      Move down x characters. Does not add new lines.      |
| u       |       Move up one character. Does not add new lines.      |
| u x     |       Move up x characters. Does not add new lines.       |
| q       |              Exit the program without saving.             |
| ab      |          Add a blank line below the current-line.         |
| aa      |          Add a blank line above the current-line.         |
| dl      |                  Delete the current line.                 |
| dl x    |        Delete the current line, and x-1 line below.       |
| cl      |     Clear the currently loaded data from the program.     |

## Contributors
Author: Benjamin X. Gregory
## Relevant Production Tools
Development Environment(s): Visual Studio Code & BlueJ
