import java.io.*;
import java.net.*;

public class TcpClient {
    public static void main(String[] argv) throws Exception
    {
        try (
            Socket socket = new Socket("localhost", 8001);
            FileOutputStream fos = new FileOutputStream("client_recv.txt");
            FileInputStream fis = new FileInputStream("../client_send.txt");
        ){
            int ch;

            System.out.println("client start sending data");
            OutputStream output = socket.getOutputStream();
            while ((ch = fis.read()) != -1)
            {
                output.write(ch);
            }
            output.write(0);
            System.out.println("client finish sending data");

            System.out.println("client start recieving data");
            InputStream input = socket.getInputStream();
            while ((ch = input.read()) != -1)
            {
                fos.write(ch);
            }
            System.out.println("client finish recieving data");

        }catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}