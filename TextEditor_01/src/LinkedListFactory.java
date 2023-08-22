
/**
 * This factory class will aid in switching between the GapBuffer and LinkedListBuffer at startup.
 *
 * @author Benjamin Gregory
 * @version 3/13/2022
 */
public class LinkedListFactory implements BufferFactoryObjectInterface
{
     /**
     * This method will return a LinkedListBuffer object.
     * @return returns an object of the LinkedListBuffer class.
     */
     public GapBufferInterface create() {
         return new LinkedListBuffer();
     }
    
}
