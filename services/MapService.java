package psl.habitats.services;

import psl.habitats.*;

import java.util.Hashtable;

public class MapService implements ServiceInterface {    private static String serviceDescription = "MapService";
  public MapService() {	}
  public void initialize(Habitat _h) { }
  public String getDescription() {
    return serviceDescription;
  }
  public void startDisplay() { }
  public Hashtable performService(String _senderService, Hashtable ipList) {
    return null;
  }
}
