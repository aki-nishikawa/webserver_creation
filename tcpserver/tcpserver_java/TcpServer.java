import java.io.*;
import java.net.*;

public class TcpServer {
    public static void main(String[] argv) throws Exception
    {
        try (
            ServerSocket server = new ServerSocket(8001);
            FileOutputStream fos = new FileOutputStream("server_recv.txt");
            FileInputStream fis = new FileInputStream("../server_send.txt");
        ){
            System.out.println("waiting access from client");
            Socket socket = server.accept();
            System.out.println("accessed");

            int ch;

            System.out.println("server start recieving data");
            // InputStream input = socket.getInputStream();
            // while ((ch = input.read()) != 0)
            // {
            //     fos.write(ch);
            // }
            System.out.println("server finish recieving data");

            System.out.println("server start sending data");
            OutputStream output = socket.getOutputStream();
            while ((ch = fis.read()) != -1)
            {
                output.write(ch);
            }
            System.out.println("server finish sending data");

            socket.close();
            System.out.println("connection finished");
        }catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}