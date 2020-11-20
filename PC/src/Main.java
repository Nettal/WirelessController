public class Main {

    public static void main(String[] args){
        if (args.length > 1) {
            System.out.println("Usage: [port]");
            System.exit(0);
        }
         new Starter(0).startServer();

    }
}
