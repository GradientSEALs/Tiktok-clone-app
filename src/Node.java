public class Node {
    public List<Broker> brokers = new List<Broker>();

    //public void init(int i)

    public void getBrokers(){
        for (int i=0;i<brokers.size();i++)
            System.out.println(brokers.get(i));
    }

    //public void connect()

    //public void disconnect()

    //public void updateNodes()
}
