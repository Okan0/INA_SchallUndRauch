import sebastian.connection.I2SConnection;

public class SchallundRauch {


  public static void main(String[] args) {
    // TODO Auto-generated method stub
	  I2SConnection microphone = new I2SConnection();
	  
	  while(true){
		  microphone.read();
	  }
  }

}