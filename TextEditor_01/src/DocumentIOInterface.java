import java.io.*;

public interface DocumentIOInterface {
    public void open_file(String file_name) throws IOException;
    public void close_file() throws IOException;
    public String read_line() throws IOException;
    public boolean has_more_lines() throws IOException;
}
