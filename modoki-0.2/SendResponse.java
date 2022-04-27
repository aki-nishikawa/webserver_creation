import java.io.*;

class SendResponse {
    static void sendOkResponse(
        OutputStream output, InputStream fis, String ext
    ) throws Exception {
        // response header
        Util.writeLine(output, "HTTP/1.1 200 OK");
        Util.writeLine(
            output, "Date: " + Util.getDataStringUtc());
        Util.writeLine(output, "Server: Modoki/0.2");
        Util.writeLine(output, "Connection: close");
        Util.writeLine(
            output, "Content-type: " + Util.getContentType(ext));
        Util.writeLine(output, "");

        // response body
        int ch;
        while ((ch = fis.read()) != -1)
            output.write(ch);
    }

    static void sendMovePermanentlyResponse(
        OutputStream output, String location
    ) throws Exception {
        // response header
        Util.writeLine(output, "HTTP/1.1 301 Moved Permanently");
        Util.writeLine(
            output, "Date: " + Util.getDataStringUtc());
        Util.writeLine(output, "Server: Modoki/0.2");
        Util.writeLine(output, "Location: " + location);
        Util.writeLine(output, "Connection: close");
        Util.writeLine(output, "");
    }

    static void sendNotFoundResponse(
        OutputStream output, String errorDocumentRoot
    ) throws Exception{
        // response header
        Util.writeLine(output, "HTTP/1.1 404 Not Found");
        Util.writeLine(
            output, "Date: " + Util.getDataStringUtc());
        Util.writeLine(output, "Server: Modoki/0.2");
        Util.writeLine(output, "Connection: close");
        Util.writeLine(output, "Content-type: text/html");
        Util.writeLine(output, "");

        // response body
        try (InputStream fis =
            new BufferedInputStream(
                new FileInputStream(errorDocumentRoot + "/404.html")
            )
        ) {
            int ch;
            while ((ch = fis.read()) != -1)
                output.write(ch);
        }
    }

}
