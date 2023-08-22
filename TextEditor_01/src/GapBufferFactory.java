
/**
 * This factory class will aid in switching between the GapBuffer and LinkedListBuffer at startup.
 *
 * @author Benjamin Gregory
 * @version 3/13/2022
 */
public class GapBufferFactory implements BufferFactoryObjectInterface
{
    /**
     * This method will return a GapBuffer object.
     * @return returns an object of the GabBuffer class.
     */
    public GapBufferInterface create(){
        return new GapBuffer();
    }
}
