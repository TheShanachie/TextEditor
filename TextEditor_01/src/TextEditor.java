import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class TextEditor {
    Document doc;

    /**
     * This method begins the Editor user interface.
     * This will ask the user to enter 'gap' or 'linked' which will decide which
     * Buffer type to use for line storage. If either 'gap' or 'linked' is entered, the
     * editor will ask for furthor commands, elsewise the editor will instruct that
     * the input was invalid.
     */
    public TextEditor() {
        clearScreen();
        doc = new Document(register_init_cmd());
        System.out.println();
        System.out.println("Enter 'help' to view command menu... ");
        print_divider();
    }

    /***
     * Used to display the current screen configuration for the file.
     */
    public void display()
    {
        clearScreen();
        print_divider();
        display_text_file();
        print_divider();
    }

    /**
     * This method will display the current editor contents
     * and also the file name, if a file has been loaded.
     * 
     * Assumption: Since this method splits lines by '|' characters, this character
     * will be used in the editor to make a new line. The user will either know
     * this,
     * or not intend to use this character in their file.
     */
    public void display_text_file() {
        if ( doc.current_file_name() == null ) { 
            System.out.println("No file to display...");
            return;
        } else { System.out.println("File: " + doc.current_file_name() + " Display..."); }

        // variables for printing the lines of the current file.
        ArrayList<String> docs = doc.toStringDocument();
        for (int i = 0; i < docs.size(); i++) {
            int n = i + 1;
            System.out.println("" + n + "| " + docs.get(i));
        }
        
        // show the current line, if any exists.
        if (docs.isEmpty())
        {
            print_divider();
            display_curr_line();
        }
    }

    /**
     * Register valid commands and execute appropriate functionality.
     */
    public void register_cmd() {
        while (true) {
            display();
            ArrayList<String> cmd = register_command();
            String input = cmd.get(0);
            switch (input) {
                case "o":
                    open_file(cmd.get(1));
                    continue;
                case "c":
                    save_data();
                    continue;
                case "s":
                    save_data_to_new(cmd.get(1));
                    continue;
                case "e":
                    /**Look Further */
                    edit_line( queueLine() );
                    continue;
                case "r":
                    if (cmd.size() == 1) move_right();
                    else {
                        int n = parseInt( cmd.get(1) );
                        if (n != -1) move_right(n);
                    }
                    continue;
                case "l":
                    if (cmd.size() == 1) move_left();
                    else {
                        int n = parseInt( cmd.get(1) );
                        if (n != -1) move_left(n);
                    }
                    continue;
                case "d":
                    if (cmd.size() == 1) move_down();
                    else {
                        int n = parseInt( cmd.get(1) );
                        if (n != -1) move_down(n);
                    }
                    continue;
                case "u":
                    if (cmd.size() == 1) move_up();
                    else {
                        int n = parseInt( cmd.get(1) );
                        if (n != -1) move_up(n);
                    }
                    continue;
                case "q":
                    System.out.println("Thank You. Bye!");
                    return;
                case "ab":
                    add_blank_below();
                    continue;
                case "aa":
                    add_blank_above();
                    continue;
                case "dl":
                    if (cmd.size() == 1) delete_line(1);
                    else {
                        int n = parseInt( cmd.get(1) );
                        if (n != -1) delete_line( n );
                    }
                    continue;
                case "cl":
                    clear_data();
                    continue;
                case "help":
                    help_menu();
                    continue;
                default:
                    continue;
            }
        }
    }

    public void display_curr_line() {
        //split the line at the position of the cursor
        //insert cursor marker inbetween this space
        //output the new line with cursor to the UI.
        if (doc.line_count() > 0) {
            String printer = doc.toStringLine(doc.cursor_line_position());
            String half01 = printer.substring(0, doc.cursor_position_in_line());
            String half02 = printer.substring(doc.cursor_position_in_line());
            System.out.println(doc.cursor_line_position() + 1 + "| " + half01 + "> <" + half02);
        }
    }

    /**
     * This method will open the file of file name 'o'. The current editor
     * will display the contents of the opened file after the corresponding command
     * has been called.
     */
    public boolean open_file(String o) {
        try {
            doc.load_file(o);
            return true;
        } catch (Exception e) {
            print_divider();
            System.out.println("File: " + o + " was not loaded correctly. Please try again.");
            System.out.println("Enter 'help' to view command menu. ");
            return false;
        }
    }

    /**
     * This method will store the current lines into the loaded file.
     */
    public void save_data() {
        try {
            doc.store_file();
        } catch (Exception e) {
        }
    }

    /**
     * This method will store the current lines into the file name s.
     * 
     * @param s is the file name to store the current files into.
     */
    public void save_data_to_new(String s) {
        try {
            doc.store_file(s);
        } catch (Exception e) {
        }
    }

    /**
     * This method will insert the string e at the cursor position within the
     * current line.
     * If the current line count is zero, then the a new line wil be loaded with the
     * String
     * e, thus the cursor will remain at the beginning of the line.
     * 
     * @param e is the string to be inserted into the current line.
     */
    public void edit_line(String e) {
        // load string to current line.
        // load this string to a new line, if none exists.
        if (doc.line_count() == 0) {
            doc.load_line_at_start(e);
        } else {
            doc.insert_text(e);
        }
    }

    /**
     * This method will move the cursor position left a space if it is available.
     */
    public void move_right() {
        doc.cursor_right();
    }

    /**
     * This method will move the cursor position right x spaces or until it reaches
     * the end.
     * 
     * @param x is the number of spaces to move right.
     */
    public void move_right(int x) {
        doc.cursor_right(x);
    }

    /**
     * This method will move the cursor position left a space if it is available.
     */
    public void move_left() {
        doc.cursor_left();
    }

    /**
     * This method will move the cursor position left x spaces or until it reaches
     * the end.
     * 
     * @param x is the number of spaces to move left.
     */
    public void move_left(int x) {
        doc.cursor_left(x);
    }

    /**
     * This method will move the cursor down one space if it is available.
     * The cursor position will be maintained within the line.
     */
    public void move_down() {
        doc.cursor_down();
    }

    /**
     * This method will move the cursor down x spaces or until the cursor is at the
     * bottom.
     * The cursor position will be maintained within the line.
     * 
     * @param x is the number of lines to move the cursor.
     */
    public void move_down(int x) {
        doc.cursor_down(x);
    }

    /**
     * This method will move the cursor up by one space if it is available.
     * The cursor position will be maintained within the line.
     */
    public void move_up() {
        doc.cursor_up();
    }

    /**
     * This method will move the cursor x lines up or until the cursor is at the
     * top.
     * The cursor position will be maintained within the line.
     * 
     * @param x is the number of lines to move the cursor.
     */
    public void move_up(int x) {
        doc.cursor_up(x);
    }

    /**
     * This method will insert a blank line above the line containing the cursor.
     */
    public void add_blank_above() {

        doc.insert_line_above();
    }

    /**
     * This method will insert a blank line below the line containing the cursor.
     */
    public void add_blank_below() {
        doc.insert_line_below();

    }

    /**
     * This method will delete the current line.
     */
    public void delete_line() {

        doc.remove_line();
    }

    /**
     * This method will delete the current line and also x - 1 lines after.
     * 
     * @param x is the number of lines this method will delete.
     */
    public void delete_line(int x) {
        int pos = doc.cursor_line_position();
        if ((pos + x) <= doc.line_count()) {
            for (int curr = 0; curr < x; curr++) {
                doc.remove_line();
            }
        } else {
            x = doc.line_count() - pos;
            for (int curr = 0; curr < x; curr++) {
                doc.remove_line();
            }
        }
    }

    /**
     * This method will clear all data inside of the internal BufferStrcuture.
     * This will erase all data within the current editor.
     */
    public void clear_data() {
        int count = doc.line_count();
        if (count > 0) {
            doc.cursor_move_last_line();
        }
        for (int current = 0; current < count; current++) {
            doc.remove_line();
        }
    }

    /**
     * Print the main instructions to the terminal.
     */
    public void print_instructions() {
        System.out.println("Please use one of the valid program inputs... ");
        System.out.println("Enter 'Exit' to exit the help menu.");
        System.out.println("o xxxxx - Open a file named \"xxxxx\".");
        System.out.println("c       - Save the current loaded data to the originating file.");
        System.out.println("s xxxxx - Save the current loaded data to a new file.");
        System.out.println("e       - Edit the current-line, using the current cursor position.");
        System.out.println("r       - Move right one character.");
        System.out.println("r x     - Move right x characters.");
        System.out.println("l       - Move left one character.");
        System.out.println("l x     - Move left x characters.");
        System.out.println("d       - Move down one character. Does not add new lines.");
        System.out.println("d x     - Move down x characters. Does not add new lines.");
        System.out.println("u       - Move up one character. Does not add new lines.");
        System.out.println("u x     - Move up x characters. Does not add new lines.");
        System.out.println("q       - Exit the program without saving.");
        System.out.println("ab      - Add a blank line below the current-line.");
        System.out.println("aa      - Add a blank line above the current-line.");
        System.out.println("dl      - Delete the current line.");
        System.out.println("dl x    - Delete the current line, and x-1 line below.");
        System.out.println("cl      - Clear the currently loaded data from the program.");
    }

    /**
     * Print an error message for bad or invalid commands.
     */
    public void print_bad_cmd() {
        // Print that the users input is invalid.
        System.out.println("");
        System.out.println("==============================================================");
        System.out.println("Your input is not reconginzed by this editor.");
        System.out.println("Enter 'help' to view command menu. ");
    }

    /**
     * Print a uniform divider.
     */
    public void print_divider() {
        System.out.println("==============================================================");
    }

    /**
     * Print the starting commands for the editor,
     * requesting neccessary information.
     */
    public void print_init_cmd() {
        // The few lines should each add text to the test class Strings
        // The other lines should also output the same text to the Console.
        // This text prompts the user to select a buffer type for their editor.
        System.out.println("==============================================================");
        System.out.println("Welcome to my text editor...");
        System.out.println("Please enter 'link' or 'gap' to choose list type.");
        System.out.print("> ");
    }

    /**
     * Register a user command input, specifically for the editor initialization.
     * This will continually iterate until the user inputs an appropriate command.
     * @return a string holding the users primary command input.
     */
    public String register_init_cmd() {
        while (true) {
            // get a command.
            print_init_cmd();
            ArrayList<String> cmd = get_command();

            // test that the string is valid.
            String fcmd = cmd.get(0);
            if (cmd.size() != 1)
                continue;
            if (!fcmd.equals("gap") && !fcmd.equals("link"))
                continue;
            else
                return fcmd;
        }
    }

    /**
     * Get a user command from the terminal. The input will be parsed by the spacing between strings.
     * Create an arrayList of Strings holding the command information, with the first element, being the primary command input.
     * @return an ArrayList of Strings holding the command information.
     */
    private ArrayList<String> get_command() {
        // create an array list for the command.
        ArrayList<String> cmd = new ArrayList<String>();

        // declare new scanner for command input.
        Scanner in = new Scanner(System.in);
        Scanner input = new Scanner(in.nextLine());

        while (input.hasNext()) {
            cmd.add(input.next());
        }
        
        return cmd;
    }

    /**
     * Get a user command from the terminal. The input will be parsed by the spacing between strings.
     * Create an arrayList of Strings holding the command information, with the first element, being the primary command input.
     * If the command input is not valid, reiterate and request a new or alternative command from the user.
     * @return an ArrayList of Strings holding the command information.
     */
    public ArrayList<String> register_command() {
        while (true) {
            // register a new cmd.
            System.out.print("=> ");
            ArrayList<String> command = get_command();

            if (valid_command(command))
                return command;
            else
                print_bad_cmd();
        }
    }

    /**
     * Check whether a command input is valid.
     * @param command is an ArrayList holding the primary command and any sub-commands as further indexed elements.
     * @return a boolean regarding whether the command was valid (TRUE) or not (FALSE).
     */
    private boolean valid_command(ArrayList<String> command) {
        // check that the command was valid.
        // check the size;
        if (command.size() < 1)
            return false;

        // check the firt character
        List<String> singles = Arrays.asList("o", "c", "s", "e", "r", "l", "d", "u", "q", "ab", "aa", "dl", "cl",
                "help");
        List<String> filecmd = Arrays.asList("o", "s");
        List<String> editcmd = Arrays.asList("r", "l", "d", "u", "dl");

        // check that the first cmd was valid.
        if (singles.contains(command.get(0))) {

            // if it is a file cmd, there is a mandatory second cmd.
            if (filecmd.contains(command.get(0)))
                return (command.size() == 2);

            // if it is an edit cmd, there does not need to be a second command.
            if (editcmd.contains(command.get(0))) {
                if (command.size() == 2) {
                    return isInteger(command.get(1));
                } else
                    return (command.size() == 2 || command.size() == 1);
            }

            // it is a regular command.
            return (command.size() == 1);
        } else
            return false;
    }

    /**
     * Refer the user to the help menu. Exit the help menu once the proper command has been input.
     */
    public void help_menu() {
        while (true) {
            // print the help menu.
            clearScreen();
            print_divider();
            print_instructions();
            print_divider();

            // register an exit command.
            System.out.print("=> ");
            ArrayList<String> command = get_command();

            // check for 'help'.
            if ((command.size() == 1) && command.get(0).equals("Exit")) {
                return;
            }
        }
    }

    /**
     * Check whether or not a String contains only an integer value.
     * @param input is a String
     * @return whether or not a String contains only an integer value.
     */
    public boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check whether or not a String contains only an integer value. If the string is an integer, return that integer.
     * Elsewise, print an error and return -1. This will always return an integer.
     * @param input is a String
     * @return If the string is an integer, return that integer. Elsewise, return -1.
     */
    public int parseInt(String input) {
        try {
            int number = Integer.parseInt(input);
            return number;
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    /**
     * This will queue the user for an input. The method will take the entirety of the user input line, until a newline is found.
     * @return an entire user input line as a String.
     */
    public String queueLine() {
        System.out.print("?> ");
        Scanner in = new Scanner(System.in);
        String r = in.nextLine();
        return r;
    }

    /**
     * This will clear the users terminal.
     */
    public void clearScreen() {
        System.out.flush();  
        try {

            if (System.getProperty("os.name").contains("Windows"))
                new ProcessBuilder("cmd", "/c",
                        "cls").inheritIO().start().waitFor();
            else
                Runtime.getRuntime().exec("clear");
        } catch (IOException | InterruptedException ex) {
            System.out.println("Could not clear Screen...");
        }
        System.out.flush();  
    }
}
