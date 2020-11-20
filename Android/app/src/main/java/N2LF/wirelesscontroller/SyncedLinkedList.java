package N2LF.wirelesscontroller;
import java.util.LinkedList;

public class SyncedLinkedList extends LinkedList<String>
{

    @Override
    public synchronized String getLast()
    {
        // TODO: Implement this method
        return super.getLast();
    }

    @Override
    public synchronized String removeLast()
    {
        // TODO: Implement this method
        return super.removeLast();
    }

    @Override
    public synchronized void addFirst(String e)
    {
        // TODO: Implement this method
        super.addFirst(e);
    }
    
    
}
