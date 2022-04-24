import java.io.*;
import java.net.*;
import java.util.*;
import java.text.*;

public class Modoki 
{
    private static final String DOCUMENT_ROOT = "/home/nishikawa/playground/webserver_creation/contents";

    // read binary line from InputStream
    private static String readLine(InputStream input)
    throws Exception {
        int ch;
        String ret = "";
        while ((ch = input.read()) != -1)
        {
            if (ch == '\r') {
                ;
            } else if (ch == '\n') {
                break;
            } else {
                ret += (char)ch;
            }
        }
        if (ch == -1) {
            return null;
        } else {
            return ret;
        }
    }

    // write binary line to OutputStream
    private static void writeLine(OutputStream output, String str)
    throws Exception {
        for (char ch : str.toCharArray())
        {
            output.write((int)ch);
        }
        output.write('\r');
        output.write('\n');
    }

    // convert time into HTTP format
    private static String getDataStringUtc()
    {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        DateFormat df = new SimpleDateFormat(
            "EEE, dd MMM yyyy HH:mm:ss", Locale.US
        );
        df.setTimeZone(cal.getTimeZone());
        return df.format(cal.getTime()) + " GMT";
    }

    public static void main(String[] argv)
    throws Exception{
        try (ServerSocket server = new ServerSocket(8000)) {
            Socket socket = server.accept();

            // parse input
            InputStream input = socket.getInputStream();
            String line;
            String path = null;
            while ((line = readLine(input)) != null)
            {
                if (line.equals("")) break;
                if (line.startsWith("GET")) {
                    path = line.split(" ")[1];
                }
            }

            // return output
            OutputStream output = socket.getOutputStream();
            // response header
            writeLine(output, "HTTP/1.1 200 OK");
            writeLine(output, "Date: " + getDataStringUtc());
            writeLine(output, "Server: Modoki/0.1");
            writeLine(output, "Connection: close");
            writeLine(output, "Content-type: text/html");
            writeLine(output, "");
            // response body
            try (FileInputStream fis = new FileInputStream(DOCUMENT_ROOT + path)) {
                int ch;
                while ((ch = fis.read()) != -1)
                {
                    output.write(ch);
                }
            }
            socket.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
