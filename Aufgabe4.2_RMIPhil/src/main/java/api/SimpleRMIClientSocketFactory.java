package api;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.rmi.server.RMIClientSocketFactory;

/**
 * Organization: HM FK07.
 * Project: VerteilteSystemePraktikum, api
 * Author(s): Rene Zarwel
 * Date: 15.06.16
 * OS: MacOS 10.11
 * Java-Version: 1.8
 * System: 2,3 GHz Intel Core i7, 16 GB 1600 MHz DDR3
 */
public class SimpleRMIClientSocketFactory implements RMIClientSocketFactory, Serializable{

  private final String ownIP;

  public SimpleRMIClientSocketFactory(String ownIP) {
    this.ownIP = ownIP;
  }

  @Override
  public Socket createSocket(String host, int port) throws IOException {
    return new Socket(ownIP, port);
  }
}
