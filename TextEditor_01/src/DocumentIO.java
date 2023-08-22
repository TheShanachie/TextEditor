import java.io.*;
import java.util.Scanner;

/***
 * This class can both read and write to a file. But
 * not both.  To select behavior use method set_io_mode.
 * If the mode is changed while the file is open, the
 * file will be closed and the mode will be changed.
 */
public class DocumentIO implements DocumentIOInterface {

    // stores reference to the file and the name
    private File file;
    private String file_name;

    // Flag to determine if the file is open or closed,
    // and is checked everywhere.
    private boolean file_open;

    // stores the input and output stream handlers
    private Scanner     sc;
    private PrintStream ps;

    // Flag to determine "input" or "output" mode,
    // stored as a string.
    private String  mode;

    /***
     * Default constructor.
     */
    DocumentIO() {

        // initialize fields
        file      = null;
        file_open = false;
        file_name = null;

        sc        = null;    // nothing is open
        ps        = null;

        mode      = "input"; // default mode to input
    }

    /***
     * Used to determine if the internal file is open.
     * 
     * @return whether the internal file handler is open or not.
     */
    public boolean is_open() 
    { 
        return file_open;
    }

    /***
     * Used to determine if the IO mode is input or output.
     * If there is a mode switch, then any open file should
     * be closed.
     * 
     * @return False is returned if mode is unknown, otherwise true.
     */
    public boolean set_io_mode(String mode) {

        if(mode.equals("input") || mode.equals("output")) {

            if(mode.equals(this.mode)) {
                // the same, do nothing
            }
            else {     // different close open files

                try {  // if failure only report
                    close_file();
                } catch (IOException e) {
                    System.err.println(e);
                    return false;
                }

                this.mode = mode; // set mode if worked
            }
        }

        else {
            return false; // unknown mode type, return false
        }

        return true; // mode modified
    }

    /***
     * Attempt to open file using passed file name.
     * 
     * @parameter file_name Name of the file to open.
     */
    public void open_file(String file_name) throws IOException {        
        this.file_name = file_name;
        open_file();
    }

    /***
     * Attempt to open file using stored file name.
     */
    public void open_file() throws IOException {

        close_file();  // close existing file if open

        if(mode.equals("input")) {              // check for input, the alternative is output
            file = new File(file_name);
            sc   = new Scanner(file);
        } else {
            ps   = new PrintStream(file_name);
        }

        file_open = true;
    }
    
    /***
     * Used by both internal and external methods, will check
     * if file is open, then close the appropriate stream if it is.
     * Does nothing if file is closed.
     */
    public void close_file() throws IOException {
 
        if (file_open) {
            if(mode.equals("input")) {          // check for input, the alternative is output
                sc.close();
                sc = null;
            } else {
                ps.close();
                ps = null;
            }
            
            file_open = false;
        }
    }

    /***
     * Used during input mode, and will return a string if
     * something to be read is available.  Checks to see if 
     * file is open and in correct mode.
     * 
     * @return Returns string from scanner.  If mode is not
     *         correct, file not open, or has no more lines;
     *         returns a null value;
     */
    public String read_line() throws IOException {
        if (mode.equals("input") && file_open) {

            if(has_more_lines()) {
                return sc.nextLine();
            }

            return "";
        } else {
            return null;
        }
    }

    /***
     * Used during input mode, and will return a true if
     * something is available to be read.  Checks to see if 
     * file is open and in correct mode.
     * 
     * @return Returns true if something is available, and
     *         false in all other cases (including incorrect mode);
     */
    
    public boolean has_more_lines() throws IOException {
        if (mode.equals("input") && file_open) {
            return sc.hasNextLine();
        } 
        else {
            return false;
        }
    }

    /***
     * Will write a string to the open file with a new line at the end.
     * Checks for correct mode and if file is open.
     * 
     * @return Returns true if file was written to, otherwise false.
     */
    public boolean write_line(String str) throws IOException {
        
        if (mode.equals("output") && file_open) {
            ps.println(str);
            return true;
        }

        return false;
    }

    /***
     * Will write a string to the open file without a new line 
     * at the end.  Checks for correct mode and if file is open.
     * 
     * @return Returns true if file was written to, otherwise false.
     */
    public boolean write(String str) throws IOException {

        if (mode.equals("output") && file_open) {
            ps.print(str);
            return true;
        }

        return false;
    }
    
    /***
     * Name of the file that is currently being manipulated.
     * 
     * @return Return string of file name, or null if no file.
     */
    public String curr_file_name() {
        return file_name;
    }
}