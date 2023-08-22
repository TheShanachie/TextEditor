import java.io.*;
import java.util.ArrayList;

/***
 * DocumentIO object created automatically and never removed.  
 * A BufferStructure object created at the start, but is 
 * replaced with a new structure everytime a file is loaded.
 */
public class Document implements DocumentInterface {

    private BufferStructure bStruct;
    private DocumentIO      dio;
    
    private String type;

    /***
     * Default constructor.  DocumentIO object created
     * automatically and never removed.  A BufferStructure
     * object created at the start, but is replaced with a 
     * new structure everytime a file is loaded.
     */
    Document(String type) {
        //the variable type will be used when declaring BufferStructure.
        this.type = type;
        
        bStruct = new BufferStructure(type);
        dio     = new DocumentIO();
    }

    /***
     * Used to determine if the internal file is open.
     * 
     * @return whether the internal file handler is open or not.
     */
    public boolean is_open()
    {
        return dio.is_open();
    }
    
    //added
    /**
     * This method will the position of which line in the BufferStructure the cursor is on
     * as an integer value.
     * 
     * @return the position of which line in the BufferStructure the cursor is on
     * as an integer value.
     * 
     * Assumption: This method is not called in the Editer user ineterface where 
     * the cursor is undeclared, therefore we will assume this method won't be 
     * declared for such circumstances.
     */
    public int cursor_line_position() {
        return bStruct.cursor_line_position();
    }
    
    /**
     * This method will return the position on the line that the cursor 
     * is on as an integer value.
     * 
     * @return the position on the line that the cursor is on as an integer value.
     * 
     * Assumption: This method is not called in the Editer user ineterface where 
     * the cursor is undeclared, therefore we will assume this method won't be 
     * declared for such circumstances.
     */
    public int cursor_position_in_line() {
        return bStruct.cursor_position_in_line();
    }

    /***
     * Will open a file, create a new buffer structure, and
     * load the file into the structure.
     * 
     * @return Will return true if successful.
     */
    public boolean load_file (String filename) throws IOException {

        String temp = null;

        bStruct = new BufferStructure(type); // create new buffer structure, dumping old one

        dio.set_io_mode("input");        // set the mode

        dio.open_file(filename);         // open file and read contents

        while(dio.has_more_lines()) {
            temp = dio.read_line();
            bStruct.load_line_at_end(temp);
        }

        dio.close_file();                // close file

        return true;
    }

    /***
     * Will store the current buffer structure to the identified file.
     * 
     * @return Returns true in all occasions, accept when there is an Exception.
     */
    public boolean store_file(String filename) throws IOException {

        String temp = null;

        dio.set_io_mode("output");           // set mode and open output file
        dio.open_file(filename);

        int line_cnt = bStruct.line_count(); // determine number of lines

        for(int index = 0; index < line_cnt; index++) {  // transfer data to the file
            temp = bStruct.toStringLine(index);
            dio.write_line(temp);
        }

        dio.close_file();                    // close file

        return true;
    }

    /***
     * Will store the current buffer structure to most recently used file.
     * 
     * @return Returns true in all occasions, accept when there is an Exception.
     */
    public boolean store_file() throws IOException{ 

        String temp = null;

        dio.set_io_mode("output");           // set mode and open current file
        dio.open_file();

        int line_cnt = bStruct.line_count(); // determine number of lines

        for(int index = 0; index < line_cnt; index++) {  // transfer data to the file
            temp = bStruct.toStringLine(index);
            dio.write_line(temp);
        }

        dio.close_file();                    // close file

        return true;
    }

    /***
     * Return the current file name as stored in the 
     * DocumentIO class.
     * 
     * @return If file was previously manipulated, 
     *         then returns the name, otherwise null.
     */
    public String current_file_name(){ 
        return dio.curr_file_name();
    }

    /***
     * Return an ArrayList that contains all the stored strings, line by line.
     *
     * @return Returns the string to the raw buffered text.
     */
    public ArrayList<String>  toStringDocument(){ 
        return bStruct.toList();
    }

    /***
     * Returns the string at a given position.
     * 
     * @return Returns null if a negative index is passed, or
     *         goes beyond the gap buffer array.
     */
    public String  toStringLine(int line_index){ 
        return bStruct.toStringLine(line_index);
    }

    /***
     * Obtain the string length of the line at a given position.
     *
     * @return Returns -1 if a negative index is passed, or
     *         goes beyond the gap buffer array.
     */
    public int     line_length(int line_index){ 
        return bStruct.position_line_length(line_index);
    }

    /***
     *  Obtain the string length of the current line.
     *  
     *  @return Return the stored length in the currently referenced buffer.
     */
    public int     line_length(){ 
        return bStruct.curr_line_length();
    }

    /***
     * The count of gap buffers in the array.
     * 
     * @return Returns the current number of lines.
     */
    public int     line_count(){ 
        return bStruct.line_count();
    }

    /***
     *  Pass cursor_left command through to the current line.
     *  
     *  @return Return the result of gap buffer cursor_left.
     */
    public boolean cursor_left(){ 
        return bStruct.cursor_left();
    }

    /***
     *  Pass cursor_left command through to the current line.
     *  
     *  @return Return the result of gap buffer cursor_left.
     */
    public boolean cursor_left(int char_count){ 
        return bStruct.cursor_left(char_count);
    }

    /***
     *  Pass cursor_right command through to the current line.
     *  
     *  @return Return the result of gap buffer cursor_right.
     */
    public boolean cursor_right(){ 
        return bStruct.cursor_right();
    }

    /***
     *  Pass cursor_right command through to the current line.
     *  
     *  @return Return the result of gap buffer cursor_right.
     */
    public boolean cursor_right(int char_count){ 
        return bStruct.cursor_right(char_count);
    }

    /***
     * Move the cursor up one line. Adjusting the cursor appropriately
     * as it is moved.
     * 
     * @return Return true if next line up is moved to, and false if no movement.
     */
    public boolean cursor_up(){ 
        return bStruct.cursor_up();
    }

    /***
     *  Move to the next n lines up in the stored structure.  This will
     *  be as if the cursor_up() method is called n times.
     *  
     *  @return Return true if new line moved to, and false if no movement.
     */
    public boolean cursor_up(int line_count){ 
        return bStruct.cursor_up(line_count); 
    }

    /***
     * Move the cursor down one line. Adjusting the cursor appropriately
     * as it is moved.
     * 
     * @return Return true if next line down is moved to, and false if no movement.
     */
    public boolean cursor_down(){ 
        return bStruct.cursor_down();
    }

    /***
     *  Move to the next n lines down in the stored structure.  This will
     *  be as if the cursor_down() method is called n times.
     *  
     *  @return Return true if new line moved to, and false if no movement.
     */
    public boolean cursor_down(int line_count){ 
        return bStruct.cursor_down(line_count);
    }

    /***
     *  Move to the first line in the stored structure, and set the new line cursor 
     *  to the previous line.  Doing this without interrupting the cursors inbetween.
     *  
     *  @return Return true if new line moved to, and false if no movement.
     */
    public boolean cursor_move_first_line(){ 
        return bStruct.cursor_move_first_line();
    }

    /***
     *  Move to the last line in the stored structure, and set the new line cursor 
     *  to the previous line.  Doing this without interrupting the cursors inbetween.
     *  
     *  @return Return true if new line moved to, and false if no movement.
     */
    public boolean cursor_move_last_line(){ 
        return bStruct.cursor_move_last_line();
    }

    /***
     *  Pass cursor_move_start_line command through to the current line.
     *  
     *  @return Return the result of gap buffer cursor_move_start_line.
     */
    public boolean cursor_move_start_line(){ 
        return bStruct.cursor_move_start_line();
    }

    /***
     *  Pass cursor_move_end_line command through to the current line.
     *  
     *  @return Return the result of gap buffer cursor_move_end_line.
     */
    public boolean cursor_move_end_line(){ 
        return bStruct.cursor_move_end_line();
    }

    /***
     * Inserts a new empty gap buffer above the current line. 
     * 
     * Assumption: Cursor will set to the index of the new buffer.
     * 
     * @return Return true if line was inserted, otherwise false.
     */
    public boolean insert_line_above(){ 
        return bStruct.insert_empty_line_above();
    }

    /***
     * Inserts a new empty gap buffer below the current line. 
     * 
     * Assumption: Cursor will remain positioned at current line before insert.
     * 
     * @return Return true if line was inserted, otherwise false.
     */
    public boolean insert_line_below(){ 
        return bStruct.insert_empty_line_below();
    }

    /***
     * Will remove current line and characters in current line.  Placing
     * cursor at the start of the new line.
     * 
     * Assumption: Cursor will point to the start of the line that
     * was moved into the current position.  Unless, the last line
     * is removed then cursor will be set to end of the new last line.
     * 
     * @return Return True if line was removed.
     */
    public boolean remove_line(){ 
        return bStruct.remove_line();
    }

    /***
     *  Pass remove_char_toleft command through to the current line.
     *  
     *  @return Return the result of gap buffer remove_char_toleft.
     */
    public boolean remove_char_toleft(){ 
        return bStruct.remove_char_toleft();
    }

    /***
     *  Pass remove_char_toleft command through to the current line.
     *  
     *  @return Return the result of gap buffer remove_char_toleft.
     */
    public boolean remove_char_toleft(int char_count){ 
        return bStruct.remove_char_toleft(char_count);
    }

    public void load_line_at_start(String str_value) {
        bStruct.load_line_at_start(str_value);
    }

    /***
     *  Pass insert_text command through to the current line.
     *  
     *  @return Return the result of gap buffer insert_text.
     */
    public boolean insert_text(String str_value){ 
        return bStruct.insert_text(str_value);
    }

    /***
     *  Pass insert_text command through to the current line.
     *  
     *  @return Return the result of gap buffer insert_text.
     */
    public boolean insert_text(char char_value){ 
        return bStruct.insert_text(char_value);
    }

    /***
     * Get the current line and cursor position.
     * 
     * %return Return the current line and cursor position in the form xx_yy, with xx being 
     * the current line and yy the cursor position.
     */
    public String curr_cursor_string() {
        return bStruct.curr_cursor_string();
    }
}
