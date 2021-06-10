package softeng.aueb.tiktok;

import java.util.ArrayList;

public interface AsyncResponse{
    void processFinish(ArrayList<String> brokerPorts, int appBrokerPort);
}