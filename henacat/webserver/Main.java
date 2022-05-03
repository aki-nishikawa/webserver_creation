package henacat.webserver;

import java.net.*;

import henacat.servletimpl.WebApplication;

public class Main {

    public static void main (String[] argv)
    throws Exception{

        // difine webApplication
        WebApplcation app = WebApplication.createInstance("testbbs");
        app.addServlet("/ShowBBS", "ShowBBS");
        app.addServlet("/PostBBS", "PostBBS");

        // wait access
        try (ServerSocket server = new ServerSocket(8000)) {
            for (;;)
            {
                Socket socket = server.accept();
                ServerThread serverThread = new ServerThread(socket);
                Thread thread = new Thread(serverThread);
                thread.start();
            }
        }

    }

}