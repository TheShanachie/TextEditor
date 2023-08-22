
/**
 * This LinkedListBuffer will do exactly as the GapBuffer does
 * It will use a LinkedList as a storage type instead of Arrays
 *
 * @author Benjamin Gregory
 * @version 3/1/2022
 */
public class LinkedListBuffer implements GapBufferInterface
{
    private Node start = null;
    private Node end = null;
    private Node cursor = null;
    private int bufferLen;
    private int gap_size;
    private int growth_amount;
    private int cursorPos;

    /**
     * Default Constructor
     */
    LinkedListBuffer () {
        growth_amount = 20;
        start = new Node();
        cursor = start;
        for (int curr = 1; curr < growth_amount; curr++) {
            cursor.set_next(new Node());
            cursor = cursor.get_next();
        }
        end = cursor;
        cursor = start;

        cursorPos = 0;
        gap_size = growth_amount;
        bufferLen = growth_amount;
    }

    /**
     * Overloaded Constructor
     * 
     * @will set the growth feild indicating how large the LinkedListBuffer will grow.
     */
    LinkedListBuffer (int growth) {
        growth_amount = growth;
        start = new Node();
        cursor = start;
        for (int curr = 1; curr < growth_amount; curr++) {
            cursor.set_next(new Node());
            cursor = cursor.get_next();
        }
        end = cursor;
        cursor = start;

        cursorPos = 0;
        gap_size = growth_amount;
        bufferLen = growth_amount;
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
        while(bufferLen < str_value.length()) {
            try {
                grow_buffer();
            }
            catch (Exception e) {
                System.err.println(e);
            }
        }

        // determine the new gap size
        // the gap_size should be the bufferLen previously because of the empty_buffer.
        gap_size = bufferLen - str_value.length();

        // Copy the string into the back of the buffer, with the cntr being
        // the iterator for the new string and the index is the iterator of
        // the existing buffer

        int cntr = 0;
        for (int index = gap_size; index < bufferLen; index++) {
            node_at(index).set_val(str_value.charAt(cntr));
            cntr++;
        }

        //cursor position will remain at zero
        cursorPos = 0;
    }

    /***
     * Utility method, will empty the current buffer, set cursor to zero,
     * make rest of array spaces.
     */
    public void empty_buffer() {
        start = new Node();
        cursor = start;
        for (int curr = 1; curr < growth_amount; curr++) {
            cursor.set_next(new Node());
            cursor = cursor.get_next();
        }
        end = cursor;
        cursor = start;

        cursorPos = 0;
        gap_size = growth_amount;
        bufferLen = growth_amount;     
    }

    /***
     * This method will grow the Linked List by the growth amount without
     * moving the cursor or the position of any key characters in the buffer.
     */
    private void grow_buffer() throws Exception {

        validate_buffer(); // check that everything is functional

        if (cursor_position() == 0) {
            Node hold = start;
            start = new Node();
            Node temp = start;
            for (int curr = 0; curr < growth_amount; curr++) {
                temp.set_next(new Node());
                temp = temp.get_next();
            }
            temp.set_next(hold);
        } else {
            Node hold = node_at(cursorPos-1);
            Node temp = hold.get_next();
            for (int curr = 0; curr < growth_amount; curr++) {
                hold.set_next(new Node());
                hold = hold.get_next();
            }
            hold.set_next(temp);
        }

        bufferLen += growth_amount;
        gap_size += growth_amount;

    }

    /***
     * Utility method, verify that buffer structure is correct.  These
     * are checks that need to be done in a variety of places.
     */
    private void validate_buffer() throws Exception {
        if (gap_size < 0) {
            throw new Exception("Gap size is negative.");
        }
        if (cursorPos < 0) {
            throw new Exception("Cursor position is negative.");
        }
        if (cursorPos > bufferLen) {
            throw new Exception("Cursor is larger than the size of buffer array");
        }
    }

    /***
     * Return the string stored in the buffer without the middle gap.
     */
    public String toString() { 
        String ret = "";
        Node temp;
        // copy the front of the buffer to the string
        for(int index = 0; index < cursorPos; index++) {
            temp = node_at(index);
            if (temp.get_empty()) {
                ret += "" + temp.get_val();
            }
        }

        // determine the end regions of the two buffers
        int buffer_start      = cursor_position() + gap_size-1;

        if (gap_size == 0) {
            buffer_start++;
        }

        // copy the end of the buffer into the string
        for(int index = buffer_start; index < bufferLen; index++) {
            temp = node_at(index);
            if (temp.get_empty()) {
                ret += "" + temp.get_val();
            }
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
        return cursorPos;
    }

    /***
     * Utility method, returns the stored string length after the cursor.
     */
    public int after_length() {
        return bufferLen - cursorPos - gap_size;
    }

    /***
     * Returns the current cursor postion at the start of the empty space.
     */
    public int cursor_position() { 
        return cursorPos;
    }

    /**
     * This will return the specific Node in the linked list.
     * @return the node at the index in the linked list
     * @param index is the index of the node to be returned.
     */
    private Node node_at(int index) {
        if (start == null && end == null) {
            //return nothing if the linked list is empty.
            return null; 
        } else {
            if ((index <= bufferLen) && (index >= 0)) {
                Node t = start;
                int curr = 0;
                for (t = start; curr < index; t = t.get_next()) {  
                    curr++;
                }
                return t;

            } else {
                //return nothing if the index is not within the linked list.
                return null;
            }
        }
    }

    /***
     * Move the cursor left one position.
     * 
     * @return Return a boolean value true if the cursor moved to the left,
     *         and false if it did not.
     */
    public boolean cursor_left() {
        int num = cursor_position();

        cursor = node_at(cursorPos);

        if((num == 0) || (end == null && start == null) || (before_length() == 0)) {
            return false;       // cursor did not move
        } 
        else if (num == 1) {
            Node h = node_at(num + gap_size-1);

            Node temp = start;
            start = cursor;

            Node hold = h.get_next();
            h.set_next(temp);
            temp.set_next(hold);

            cursorPos--;
        }
        else if (num > 1) {
            Node t = node_at(num - 2);
            Node temp = t.get_next();
            t.set_next(cursor);

            Node h = node_at(num + gap_size-2);
            Node hold = h.get_next();
            h.set_next(temp);
            temp.set_next(hold);

            cursorPos--;
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
        int num = cursor_position();

        if(after_length() == 0) {
            return false;       // cursor did not move
        } 
        else {
            //sets h as the node right before the last node in the gap.
            Node h = node_at(num + gap_size-1);
            //sets hold as item directly after the gap.
            Node hold = h.get_next();
            //sets the next item after the h node as the node after the node after h, 
            //effectively eliminating the item in between and shortening the list.
            h.set_next(hold.get_next());
            if (num == 0) {
                //set temp as the start node
                Node temp = start;
                //set start as the previously declared hold node.
                start = hold;
                //set the next value after start as temp.
                start.set_next(temp);
                //set cursor as the item after start.
                cursorPos++;
            } else if (num == 1) {
                //set temp as item after start.
                Node temp = start.get_next();
                //set the item after start as hold.
                start.set_next(hold);
                //set item after hold as the item that was after the previous start.
                hold.set_next(temp);
                cursorPos++;
            } else if (num > 1) {
                //set t as 2 items before the cursor;
                Node temp = node_at(num - 1);
                Node t = temp.get_next();
                //set temp as the item after t;
                //set the item after t as hold.
                temp.set_next(hold);
                //set the item after hold as temp;
                hold.set_next(t);
                cursorPos++;
            }
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
        if(cursor_position() == 0) {
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
        if(cursor_position() == bufferLen - gap_size) {
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

        if(cursor_position() == 0) {
            return false;
        } else {
            cursor = node_at(cursor_position() - 1);
            cursor.set_empty(false);
            gap_size++;
            cursorPos--;
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

        node_at(cursorPos).set_val(char_value);
        cursor = node_at(cursorPos).get_next();
        gap_size--;
        cursorPos++;

        return true; 
    }
}
