package core_lib.simple_network_engine.net_layer;


public class NetRequestHandleNilObject implements INetRequestHandle {

  public NetRequestHandleNilObject() {
    // TODO Auto-generated constructor stub
  }

  @Override
  public boolean isIdle() {
    return true;
  }

  @Override
  public void cancel() {

  }

}
