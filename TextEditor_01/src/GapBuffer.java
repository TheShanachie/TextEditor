class GapBuffer implements GapBufferInterface {

    private char[] buffer;

    private int    growth_amount;

    private int    cursor_position;
    private int    gap_size;

    /***
     * Default constructor.
     */
    GapBuffer() {
        growth_amount = 20;
        buffer        = new char[growth_amount];    
    }

    /***
     * Overloaded constructor.
     * 
     * @param growth will set the growth field, indication how large
     * the gap buffer will grow by.
     */
    GapBuffer(int growth) {
        growth_amount = growth;
        buffer        = new char[growth_amount];    
    }

    /***
     * Loads a string into the class, removing a previously stored string.
     * 
     * Assumption: is that the cursor will start at location zero.
     */ 
    public void load_string(String str_value) {

        // remove old string
        empty_buffer();

        // Will the new string fit?  Loop till it does.
        while(buffer.length < str_value.length()) {
            try {
                grow_buffer();
            }
            catch (Exception e) {
                System.err.println(e);
            }
        }

        // determine the new gap size
        gap_size = buffer.length - str_value.length();

        // Copy the string into the back of the buffer, with the cntr being
        // the iterator for the new string and the index is the iterator of
        // the existing buffer
        int cntr = 0;
        for(int index = gap_size; index < buffer.length; index++) {
            buffer[index] = str_value.charAt(cntr);
            cntr++;
        }

    }

    /***
     * Utility method, will empty the current buffer, set cursor to zero,
     * make rest of array spaces.
     */
    public void empty_buffer() {

        cursor_position = 0;
        gap_size        = buffer.length;

        for(int index = 0; index < buffer.length; index++) {
            buffer[index] = ' ';
        }
    }

    /***
     * Utilty method, will grow the string, preserving the 
     * internal gap buffer structure.
     */
    private void grow_buffer() throws Exception {

        validate_buffer(); // check that everything is functional

        char[] temp_buffer = new char[buffer.length + growth_amount];

        // initialize temp buffer
        for(int cnt = 0; cnt < temp_buffer.length; cnt++) {
            temp_buffer[cnt] = ' ';
        }
        
        if (buffer.length == gap_size) {  // empty buffer
            // replace buffer with new array
            buffer = temp_buffer;

        } else {                          // non-empty buffer

            // copy the front of the buffer to new buffer
            for(int index = 0; index < cursor_position; index++) {
                temp_buffer[index] = buffer[index];
            }

            // determine the end regions of the two buffers
            int endstr_start      = cursor_position + gap_size;
            int new_gap_size      = gap_size + growth_amount;
            //int temp_endstr_start = cursor_position + new_gap_size;

            // copy the end of the buffer into the new buffer
            int temp_index = temp_buffer.length - after_length();
            for(int buff_index = endstr_start; buff_index < buffer.length; buff_index++) {
                temp_buffer[temp_index] = buffer[buff_index];
                temp_index++;
            }
        
            buffer   = temp_buffer;  // replace old buffer with new
            gap_size = new_gap_size; // update gap size
                                     // cursor should be in correct position
        }
        
    }

    /***
     * Utility method, verify that buffer structure is correct.  These
     * are checks that need to be done in a variety of places.
     */
    private void validate_buffer() throws Exception {

        if (gap_size < 0) {
            throw new Exception("Gap size is negative.");
        }
        if (cursor_position < 0) {
            throw new Exception("Cursor position is negative.");
        }
        if (cursor_position > buffer.length) {
            throw new Exception("Cursor is larger than the size of buffer array");
        }
    }

    /***
     * Return the string stored in the buffer without the middle gap.
     */
    public String toString() { 
        String ret = "";

        // copy the front of the buffer to the string
        for(int index = 0; index < cursor_position; index++) {
            ret += buffer[index];
        }

        // determine the end regions of the two buffers
        int buffer_start      = cursor_position + gap_size;

        // copy the end of the buffer into the string
        for(int index = buffer_start; index < buffer.length; index++) {
            ret += buffer[index];
        }

        return ret;
    }

    /***
     * Utility method, returns length of the stored string without the empty space (gap).
     */
    public int length() { 
        return before_length() + after_length(); 
    }

    /***
     * Utility method, returns the stored string length before the cursor.
     */
    public int before_length() {
        return cursor_position;
    }

    /***
     * Utility method, returns the stored string length after the cursor.
     */
    public int after_length() {
        return buffer.length - cursor_position - gap_size;
    }

    /***
     * Returns the current cursor postion at the start of the empty space.
     */
    public int cursor_position() { 
        return cursor_position; 
    }

    /***
     * Move the cursor left one position.
     * 
     * @return Return a boolean value true if the cursor moved to the left,
     *         and false if it did not.
     */
    public boolean cursor_left() { 

        if(cursor_position == 0) {
            return false;       // cursor did not move
        } 
        else {
            cursor_position--;
            buffer[cursor_position + gap_size] = buffer[cursor_position];
            buffer[cursor_position] = ' ';
        }
        
        return true; 
    }

    /***
     * Move the cursor left n positions equal to char_count.
     * 
     * @return Return a boolean value true if the cursor moved at least once
     *         to the left, and false if it did not.
     */
    public boolean cursor_left(int char_count) {
        
        for(int cnt = 0; cnt < char_count; cnt++) {

            // iterate cursor movement by calling this method
            if(cursor_left() == false) {
                if(cnt == 0) {
                    return false;  // the cursor never moved
                } else {
                    return true;   // the cursor moved at least once
                }
            }
        }
        
        // the cursor moved the required number of times
        return true; 
    }

    /***
     * Move the cursor right one position.
     * 
     * @return Return a boolean value true if the cursor moved to the right,
     *         and false if it did not.
     */
    public boolean cursor_right() { 

        if(cursor_position + gap_size == buffer.length) {
            return false;       // cursor did not move
        } 
        else {
            buffer[cursor_position]            = buffer[cursor_position + gap_size];
            buffer[cursor_position + gap_size] = ' ';
            cursor_position++;
        }
        
        return true; 
    }

    /***
     * Move the cursor right n positions equal to char_count.
     * 
     * @return Return a boolean value true if the cursor moved at least once
     *         to the right, and false if it did not.
     */
    public boolean cursor_right(int char_count) { 

        for(int cnt = 0; cnt < char_count; cnt++) {

            // iterate cursor movement by calling this method
            if(cursor_right() == false) {
                if(cnt == 0) {
                    return false;  // the cursor never moved
                } else {
                    return true;   // the cursor moved at least once
                }
            }
        }
        
        // the cursor moved the required number of times
        return true; 
    }

    /***
     * Move the cursor to the start of the buffer, position 0,
     * and move the front of the string to the appropriate position.
     * This work is done using the cursor_left() method.
     * 
     * @return Return a boolean if the cursor is at the start of
     *         the buffer.
     */
    public boolean cursor_move_start_line() {
        
        // iterate the buffer movement to the left by repeatedly
        // calling cursor_right()
        while(cursor_left());
        
        // validate the cursor position
        if(cursor_position == 0) {
            return true; 
        } else {
            return false;
        }
    }

    /***
     * Move the cursor to the end of the buffer, position 0,
     * and move the back of the string to the appropriate position.
     * This work is done using the cursor_right() method.
     * 
     * @return Return a boolean if the cursor is at end of
     *         the buffer.
     */
    public boolean cursor_move_end_line() { 
        // iterate the buffer movement to the left by repeatedly
        // calling cursor_right()
        while(cursor_right());
        
        // validate the cursor position
        if(cursor_position == buffer.length - gap_size) {
            return true; 
        } else {
            return false;
        }
    }

    /***
     * Remove the character to the left, decreasing string size.
     * 
     * @return Return a boolean value true if one character to the left
     *         was removed, and false if it did not.
     */
    public boolean remove_char_toleft() {
        
        if(cursor_position == 0) {
            return false;
        } else {
            cursor_position--;
            buffer[cursor_position] = ' ';
            gap_size++;
        }
        
        return true;
    }

    /***
     * Remove the character to the left n characters, decreasing string size.
     * 
     * @return Return a boolean value true if at least one character to the left
     *         was removed, and false if it did not.
     */
    public boolean remove_char_toleft(int char_count) { 
        
        for(int cnt = 0; cnt < char_count; cnt++) {
            // iterate character removal by calling this method

            if(remove_char_toleft() == false) {
                if(cnt == 0) {
                    return false;  // the cursor never moved
                } else {
                    return true;   // the cursor moved at least once
                }
            }
        }
        
        return true; 
    }

        
    /***
     * Insert n characters at the cursor position, reducing the gap size.
     * 
     * @return Return true if at least one character was inserted, otherwise false.
     */
    public boolean insert_text(String str_value) { 

        for(int cnt = 0; cnt < str_value.length(); cnt++) {
            if (insert_text(str_value.charAt(cnt)) == false) {
                if(cnt == 0) {
                    return false;  // no characters inserted
                } else {
                    return true;   // at least one character was inserted
                }
            }
        }
        
        return true; 
    }
    
    /***
     * Insert one character at the cursor position, reducing gap size.
     * 
     * @return Return true if the character was inserted, otherwise false.
     */
    public boolean insert_text(char char_value) { 

        if(gap_size == 0) {  // grow buffer size
            try {
                grow_buffer();
            } catch (Exception e) {
                System.err.println(e);
            }
        }
        
        buffer[cursor_position] = char_value;
        cursor_position++;
        gap_size--;
        
        return true; 
    }

}