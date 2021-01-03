import java.util.LinkedList;

public class BytesQueue {
        private final LinkedList<byte[]> linkedList = new LinkedList<>();

        public synchronized void add(byte[] s) {//需要加上同步
            linkedList.addFirst(s);
        }

        public synchronized byte[] get() {
            byte[] temp = linkedList.getLast();
            linkedList.removeLast();
            return temp;
        }

        public synchronized boolean isEmpty(){
            return linkedList.isEmpty();
        }
}
