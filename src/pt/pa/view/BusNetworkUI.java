package pt.pa.view;

import pt.pa.controller.BusNetworkController;
import pt.pa.observerpattern.Observer;

public interface BusNetworkUI extends Observer {


    void displayError(String msg);
    void setTriggers(BusNetworkController controller);
}
