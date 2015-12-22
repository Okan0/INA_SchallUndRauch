import sebastian.connection.I2SConnection;

public class SchallundRauch {

  static {
    try {
      System.loadLibrary("mraajava");
    } catch (UnsatisfiedLinkError e) {
      System.err.println(
        "Native code library failed to load. See the chapter on Dynamic Linking Problems in the SWIG Java documentation for help.\n" +
        e);
      System.exit(1);
    }
  }

  public static void main(String[] args) {
    // TODO Auto-generated method stub
	  I2SConnection connection = new I2SConnection();
	  connection.read();
  }

}