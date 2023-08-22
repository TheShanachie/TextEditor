import java.util.ArrayList;
public class BufferStructure implements BufferStructureInterface {

    private GapBufferInterface[] gb_array;
    private int         growth;
    private int         line_cnt;
    private int         curr_line;
    
    //type and my_factory will be used to to initialize factory
    //objects which will enable the BufferStructure to utilize two 
    //different type of objects for our arrays depending on the argument 
    //that is called to the constructor.
    private BufferFactoryObjectInterface my_factory;

    /***
     * Default constructor.  Sets growth to 5, create an array of
     * GapBuffer, and initialize array to null.
     * 
     * BufferStructure will now also take an argument type in the constructor.
     * This argument will set the type of GapBuffer to be used later by the 
     * class.
     * 
     * @param type is the type of GapBuffer to be passed into the constructor
     * to select the type of Buffer Object.
     * 
     */
    public BufferStructure(String type) {
        //This will set the my_factory object to the correct factory object.
        //When a new instance of a certain type is needed, the class will use
        //the create() method from the BufferFactoryObjectInterface, which all
        //used factory objects implement.
        //this.type = type;
        if (type.equals("gap")){
            my_factory = new GapBufferFactory();
        } else if (type.equals("link")) {
            my_factory = new LinkedListFactory();
        }
        
        growth      = 5;
        gb_array    = new GapBufferInterface[growth];
        line_cnt    = 0;

        // initialize index to all null
        for(int index = 0; index < gb_array.length; index++) {
            gb_array[index] = null;
        }
    }

    /***
     * Grow the buffer array.
     * 
     * @return True if the buffer grew, otherwise false.
     */
    public boolean grow_array() {
        if(line_cnt == gb_array.length) {
            GapBufferInterface [] temp = new GapBufferInterface[gb_array.length + growth];

            // initialize index to all null
            for(int index = 0; index < temp.length; index++) {
                temp[index] = null;
            }

            // copy contents of old array into new
            for(int index = 0; index < gb_array.length; index++) {
                temp[index] = gb_array[index];
            }

            gb_array = temp; // store the new array

            return true;
        }

        return false;
    }

    /***
     * Creates a new buffer and places it at the top of those buffers setting cursor to location zero of the new buffer.
     *
     * Assumption: Cursor will set to the index of the new buffer.
     */
    public void load_line_at_start    (String str_value) {

        // grow array if needed
        grow_array();

        if(line_cnt > 0) { // array is not empty, shift elements
            for (int index = line_cnt; index > 0; index--) {
                gb_array[index] = gb_array[index - 1];
            }
        } 

        // create new buffer and load string
        GapBufferInterface gb = my_factory.create();
        gb.load_string(str_value);

        // store new buffer at first location of array, which should be empty
        gb_array[0] = gb;

        // set current line to the first
        curr_line = 0;
        line_cnt++;
    }

    /***
     * Load a gap buffer to the next open array location, then set the current line
     * to this position.
     * 
     * Assumption: Cursor will set to the index of the new buffer.
     */
    public void load_line_at_end      (String str_value) {

        // grow array if needed
        grow_array();

        // create new buffer and load string
        GapBufferInterface gb = my_factory.create();
        gb.load_string(str_value);

        // set the current line to the end
        curr_line = line_cnt;

        // store new buffer at first location of array, which should be empty
        gb_array[curr_line] = gb;

        // set current line to the first
        line_cnt++;
    }

    /***
     * Creates a new buffer and places at the identified position,
     * shifting the buffers currently at that position and below down.
     * 
     * Assumption: If the number of lines is less than the position, the
     * item will be placed as the last line.  As if load_line_at_end was
     * executed.
     * 
     * Assumption: Cursor will set to the index of the new buffer.
     */
    public void load_line_at_position (String str_value, int position) { 

        // grow array if needed
        grow_array();

        // if the new line will be beyond the last line
        if(position > line_cnt) {
            // change the position to the curr_line
            position = line_cnt;
        }
        else if(position < line_cnt) {
            // shift the items
            for(int index = line_cnt; index > position; index--) {
                gb_array[index] = gb_array[index - 1];
            }
        }
        else {
            // do nothing, process as if load_line_at_end
        }

        // create new buffer and load string
        GapBufferInterface gb = my_factory.create();
        gb.load_string(str_value);

        // store new buffer at first location of array, which should be empty
        gb_array[position] = gb;

        // set current line to the position
        curr_line = position;        
        line_cnt++;
    }

    /***
     * Inserts a new empty gap buffer above the current line. 
     * 
     * Assumption: Cursor will set to the index of the new buffer.
     * 
     * @return Return true if line was inserted, otherwise false.
     */
    public boolean insert_empty_line_above() {

        if(curr_line == 0) {
            load_line_at_start("");
        }
        else { // case is in the middle position
            load_line_at_position ("", curr_line);
        }

        // cursor and list length will be set adjusted by called methods

        return true; 
    }

    /***
     * Inserts a new empty gap buffer below the current line. 
     * 
     * Assumption: Cursor will remain positioned at current line before insert.
     * 
     * @return Return true if line was inserted, otherwise false.
     */
    public boolean insert_empty_line_below() { 

        if(curr_line == line_cnt - 1) {
            load_line_at_end("");
        }
        else { // case is in the middle position
            load_line_at_position ("", curr_line + 1);
        }

        // cursor and list length will be set adjusted by called methods

        return true; 
    }

    /***
     * The count of gap buffers in the array.
     * 
     * @return Returns the current number of lines.
     */
    public int     line_count() { 
        return line_cnt; 
    }

    /***
     *  Obtain the string length of the current line.
     *  
     *  @return Return the stored length in the currently referenced buffer.
     */
    public int     curr_line_length() { 
        return gb_array[curr_line].length();
    }

    /***
     * Obtain the string length of the line at a given position.
     *
     * @return Returns -1 if a negative index is passed, or
     *         goes beyond the gap buffer array.
     */
    public int     position_line_length(int index) { 

        if (index < 0) {             // error checking
            return -1;
        }

        if (index >= line_cnt) {
            return -1;
        }

        return gb_array[index].length();
    }

    /***
     * Returns the line where the cursor is positioned and where
     * in the current line it is positioned.
     * 
     * @return
     */
    public int     cursor_line_position() { 
        return curr_line;
    }

    /***
     *  Pass cursor_position_in_line command through to the current line.
     *  
     *  @return Return the result of gap buffer cursor_position_in_line.
     */
    public int     cursor_position_in_line() { 
        return gb_array[curr_line].cursor_position(); 
    }

    /***
     *  Pass cursor_left command through to the current line.
     *  
     *  @return Return the result of gap buffer cursor_left.
     */
    public boolean cursor_left() { 
        return gb_array[curr_line].cursor_left(); 
    }

    /***
     *  Pass cursor_left command through to the current line.
     *  
     *  @return Return the result of gap buffer cursor_left.
     */
    public boolean cursor_left(int char_count) { 
        return gb_array[curr_line].cursor_left(char_count); 
    }

    /***
     *  Pass cursor_right command through to the current line.
     *  
     *  @return Return the result of gap buffer cursor_right.
     */
    public boolean cursor_right() { 
        return gb_array[curr_line].cursor_right();
    }

    /***
     *  Pass cursor_right command through to the current line.
     *  
     *  @return Return the result of gap buffer cursor_right.
     */
    public boolean cursor_right(int char_count) { 
        return gb_array[curr_line].cursor_right(char_count);
    }

    /***
     * Move the cursor up one line. Adjusting the cursor appropriately
     * as it is moved.
     * 
     * @return Return true if next line up is moved to, and false if no movement.
     */
    public boolean cursor_up() { 

        if(curr_line == 0) {              // cursor at first line, no move
            return false;
        }
        else {                            // cursor before last line, move
            int curr_line_cursor = gb_array[curr_line]    .cursor_position();
            int next_line_cursor = gb_array[curr_line - 1].cursor_position();
            int next_line_length = gb_array[curr_line - 1].length();

            if(next_line_length < curr_line_cursor) {  // new line is shorter
                curr_line--;
                gb_array[curr_line].cursor_move_end_line();
            }
            else {                                     // new line is same or longer

                if(gb_array[curr_line].cursor_position() == 0) { // curr is cursor 0
                    curr_line--;
                    gb_array[curr_line].cursor_move_start_line(); // curr is middle
                } else {

                    int diff = curr_line_cursor - next_line_cursor;
                    curr_line--;

                    if (diff < 0) {            // new cursor to right
                        diff = Math.abs(diff); // find the magnitue value
                        gb_array[curr_line].cursor_left(diff);
                    }

                    else if (diff > 0) {       // new cursor to left
                        gb_array[curr_line].cursor_right(diff);
                    }

                    else {
                        // both cursors the same, do nothing
                    }
                }
            }
        }

        return true; 
    }

    /***
     *  Move to the next n lines up in the stored structure.  This will
     *  be as if the cursor_up() method is called n times.
     *  
     *  @return Return true if new line moved to, and false if no movement.
     */
    public boolean cursor_up(int line_count) { 

        boolean moved = false;

        for(int cnt = 0; cnt < line_count; cnt++) {
            if(cursor_up()) {
                moved = true;
            }
        }

        return moved;
    }

    /***
     * Move the cursor down one line. Adjusting the cursor appropriately
     * as it is moved.
     * 
     * @return Return true if next line down is moved to, and false if no movement.
     */
    public boolean cursor_down() { 

        if(curr_line == line_cnt - 1) {  // cursor at last line, no move
            return false;
        }
        else {                           // cursor before last line, move

            int curr_line_cursor = gb_array[curr_line]    .cursor_position();
            int next_line_cursor = gb_array[curr_line + 1].cursor_position();
            int next_line_length = gb_array[curr_line + 1].length();

            if(next_line_length < curr_line_cursor) {  // new line is shorter
                curr_line++;
                gb_array[curr_line].cursor_move_end_line();
            }
            else {                                     // new line is same or longer

                if(gb_array[curr_line].cursor_position() == 0) { // curr is cursor 0
                    curr_line++;
                    gb_array[curr_line].cursor_move_start_line(); // curr is middle
                } else {

                    int diff = curr_line_cursor - next_line_cursor;
                    curr_line++;

                    if (diff < 0) {           // new cursor to right
                        gb_array[curr_line].cursor_left(diff);
                    }

                    else if (diff > 0) {      // new cursor to left
                        gb_array[curr_line].cursor_right(diff);
                    }

                    else {
                        // both cursors the same, do nothing
                    }

                }
            }

        }

        return true; 
    }

    /***
     *  Move to the next n lines down in the stored structure.  This will
     *  be as if the cursor_down() method is called n times.
     *  
     *  @return Return true if new line moved to, and false if no movement.
     */
    public boolean cursor_down(int line_count) { 

        boolean moved = false;

        for(int cnt = 0; cnt < line_count; cnt++) {
            if(cursor_down()) {
                moved = true;
            }
        }

        return moved;
    }

    /***
     *  Move to the first line in the stored structure, and set the new line cursor 
     *  to the previous line.  Doing this without interrupting the cursors inbetween.
     *  
     *  @return Return true if new line moved to, and false if no movement.
     */
    public boolean cursor_move_first_line() {

        if(curr_line == 0) {              // cursor at first line, no move
            return false;
        }
        else {                            // cursor before last line, move

            int curr_line_cursor = gb_array[curr_line].cursor_position();

            int next_line_cursor = gb_array[0].cursor_position();
            int next_line_length = gb_array[0].length();

            if(next_line_length < curr_line_cursor) {  // new line is shorter
                curr_line = 0;
                gb_array[curr_line].cursor_move_end_line();
            }
            else {                                     // new line is same or longer

                if(gb_array[curr_line].cursor_position() == 0) { // curr is cursor 0
                    curr_line = 0;
                    gb_array[curr_line].cursor_move_start_line(); // curr is middle
                } else {

                    int diff = curr_line_cursor - next_line_cursor;
                    curr_line = 0;

                    if (diff < 0) {           // new cursor to right
                        gb_array[curr_line].cursor_left(diff);
                    }

                    else if (diff > 0) {      // new cursor to left
                        gb_array[curr_line].cursor_right(diff);
                    }

                    else {
                        // both cursors the same, do nothing
                    }
                }
            }
        }

        return true; 
    }

    /***
     *  Move to the last line in the stored structure, and set the new line cursor 
     *  to the previous line.  Doing this without interrupting the cursors inbetween.
     *  
     *  @return Return true if new line moved to, and false if no movement.
     */
    public boolean cursor_move_last_line() { 

        if(curr_line == line_cnt - 1) {  // cursor at last line, no move
            return false;
        }
        else {                           // cursor before last line, move

            int curr_line_cursor = gb_array[curr_line]   .cursor_position();
            int next_line_cursor = gb_array[line_cnt - 1].cursor_position();
            int next_line_length = gb_array[line_cnt - 1].length();

            if(next_line_length < curr_line_cursor) {  // new line is shorter
                curr_line = line_cnt - 1;
                gb_array[curr_line].cursor_move_end_line();
            }
            else {                                     // new line is same or longer

                if(gb_array[curr_line].cursor_position() == 0) { // curr is cursor 0
                    curr_line = line_cnt - 1;
                    gb_array[curr_line].cursor_move_start_line(); // curr is middle
                } else {

                    int diff = curr_line_cursor - next_line_cursor;
                    curr_line = line_cnt - 1;

                    if (diff < 0) {           // new cursor to right
                        gb_array[curr_line].cursor_left(diff);
                    }

                    else if (diff > 0) {      // new cursor to left
                        gb_array[curr_line].cursor_right(diff);
                    }

                    else {
                        // both cursors the same, do nothing
                    }

                }
            }

        }

        return true; 
    }

    /***
     *  Pass cursor_move_start_line command through to the current line.
     *  
     *  @return Return the result of gap buffer cursor_move_start_line.
     */
    public boolean cursor_move_start_line() { 
        return gb_array[curr_line].cursor_move_start_line(); 
    }

    /***
     *  Pass cursor_move_end_line command through to the current line.
     *  
     *  @return Return the result of gap buffer cursor_move_end_line.
     */
    public boolean cursor_move_end_line() { 
        return gb_array[curr_line].cursor_move_end_line(); 
    }

    /***
     * Will remove current line and characters in current line.  Placing
     * cursor at the start of the new line.
     * 
     * Four situations need to be accounted for:
     *  1. Empty array.
     *  2. One element array.
     *  3. Many element array, with cursor at end.
     *  4. Many element array, with cursor above end.
     *  5. Many element array, with cursor above end but
     *     array is full and out of bounds error is possible.
     * 
     * Assumption: Cursor will point to the start of the line that
     * was moved into the current position.  Unless, the last line
     * is removed then cursor will be set to end of the new last line.
     * 
     * @return Return True if line was removed.
     */
    public boolean remove_line() { 

        if(line_cnt == 0) {       // empty array do nothing
            return false;
        } else

        if(line_cnt == 1) {       // one element array, empty it
            gb_array[curr_line] = null;
            line_cnt = 0;
        }

        else {                    // multi-element array

            if(curr_line == line_cnt - 1) { // cursor at end of array
                gb_array[curr_line] = null;
                curr_line--;

                // set cursor of new curr_line to end of buffer
                gb_array[curr_line].cursor_move_start_line();
            }
            else {                          // cursor above end of array

                if(line_cnt == gb_array.length) { // array is full, no last item to copy from

                    for(int index = curr_line; index < line_cnt - 1; index++) {
                        gb_array[index]     = gb_array[index + 1];
                        gb_array[index + 1] = null;
                    }

                    gb_array[line_cnt - 1] = null;
                } 
                else {                            // array is not full

                    for(int index = curr_line; index < line_cnt; index++) {
                        gb_array[index]     = gb_array[index + 1];
                        gb_array[index + 1] = null;
                    }
                }

                // set cursor of new curr_line to front of buffer
                gb_array[curr_line].cursor_move_start_line();
            }

            line_cnt--;
        }

        return true; 
    }

    /***
     *  Pass remove_char_toleft command through to the current line.
     *  
     *  @return Return the result of gap buffer remove_char_toleft.
     */
    public boolean remove_char_toleft() { 
        return gb_array[curr_line].remove_char_toleft(); 
    }

    /***
     *  Pass remove_char_toleft command through to the current line.
     *  
     *  @return Return the result of gap buffer remove_char_toleft.
     */
    public boolean remove_char_toleft(int char_count) { 
        return gb_array[curr_line].remove_char_toleft(char_count); 
    }

    /***
     *  Pass insert_text command through to the current line.
     *  
     *  @return Return the result of gap buffer insert_text.
     */
    public boolean insert_text(String str_value) { 
        return gb_array[curr_line].insert_text(str_value); 
    }

    /***
     *  Pass insert_text command through to the current line.
     *  
     *  @return Return the result of gap buffer insert_text.
     */
    public boolean insert_text(char char_value) { 
        return gb_array[curr_line].insert_text(char_value); 
    }

    /***
     * Get the current line and cursor position.
     * 
     * %return Return the current line and cursor position in the form xx_yy, with xx being 
     * the current line and yy the cursor position.
     */
    public String curr_cursor_string() {

        if(line_cnt == 0) {
            return "";
        }

        return String.format("%02d_%02d", curr_line, gb_array[curr_line].cursor_position());
    }

    /***
     * Return an ArrayList that contains all the stored strings, line by line.
     *
     * @return Returns the string to the raw buffered text.
     */
    public ArrayList<String> toList() {

        ArrayList<String> list = new ArrayList<String>();

        for(int index = 0; index < line_cnt; index++) {
            list.add(gb_array[index].toString());
        }

        return list;

    }

    /***
     * Returns the string at a given position.
     * 
     * @return Returns null if a negative index is passed, or
     *         goes beyond the gap buffer array.
     */
    public String toStringLine(int index) {

        String str = "";

        if (index < 0) {
            return null;
        }

        if (index >= line_cnt) {
            return null;
        }

        str += gb_array[index].toString();

        return str;

    }
}
