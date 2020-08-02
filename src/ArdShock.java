import jssc.SerialPort;
import jssc.SerialPortException;

public class ArdShock {

    //mac is "/dev/tty.usbmodem14201"
	//linux is "/dev/ttyACM0"
	//win is "COM1"
	//Numbers change from port to port
    
    public static void main(String[] args) {
        turnON(null);
    }

    public static void turnON(String SerialPort){
        SerialPort serialPort = new SerialPort(SerialPort);
        try {
            System.out.println("Port opened: " + serialPort.openPort());
            System.out.println("Params setted: " + serialPort.setParams(9600, 8, 1, 0, false, false));
            System.out.println("\"o\" successfully writen to port: " + serialPort.writeBytes("o".getBytes()));
            System.out.println("Port closed: " + serialPort.closePort());

        }
        catch (SerialPortException ex){
            System.out.println(ex);
        }
    }


}
