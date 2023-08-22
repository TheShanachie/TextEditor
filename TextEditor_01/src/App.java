/**
 * This ProjectMain class is solely used to boot a new editor to the console
 * This will be used so the professor and I can view the user interface if
 * we wish.
 *
 * @author Benjamin Gregory
 * @version 3/13/2022
 */
public class App {
    public static void main(String[] args) {
        // This use of "gap" as an argument is irrelevant since the editor, which the 
        // user will be using will be created when the load_editor() method is called.
        TextEditor te = new TextEditor();
        te.register_cmd();
    }
}
