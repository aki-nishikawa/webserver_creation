package henacat.webserver;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;

import henacat.servletimpl.*;
import henacat.util.*;

public class ServerThread implements Runnable
{
    private static final String DOCUMENT_ROOT =
        "/home/nishikawa/playground/webserver_creation/contents";
    private static final String ERROR_DOCUMENT_ROOT = DOCUMENT_ROOT;
    private Socket socket;

    private static void addRequestHeader(
        Map<String, String> requestHeader, String line
    ) {
        int colonPos = line.indexOf(':');
        if (colonPos == -1) return;
        
        String headerName = line.substring(0, colonPos).toUpperCase();
        String headerValue = line.substring(colonPos + 1).trim();
        requestHeader.put(headerName, headerValue);
    }

    @Override
    public void run()
    {
        OutputStream output = null;
        try {

            /*
            * parse input
            */
            // get request from input
            InputStream input = socket.getInputStream();
            String line;
            String requestLine = null;
            String method = null;
            Map<String, String> requestHeader = new HashMap<String, String>();
            while ((line = Util.readLine(input)) != null)
            {
                if (line.equals("")) break;
                if (line.startsWith("GET")) {
                    method = "GET";
                    requestLine = line;
                } else if (line.startWith("POST")) {
                    method = "POST";
                    requestLine = line;
                } else{
                    addRequestHeader(requestHeader, line);
                }
            }
            if (requestLine == null) return;
            // get path and query parameter from request
            String reqUri = requestLine.split(" ")[1];
            String[] pathAndQuery = reqUri.split("\\?");
            Stirng path = MyURLDecoder.decode(pathAndQuery[0], "UTF-8");
            Stirng query = null;
            if (pathAndQuery.length > 1)
                query = pathAndQuery[1];
            
            /*
            * return output
            */
            output = new BufferedOutputStream(socket.getOutputStream());
            // do application service
            String appDir = path.substring(1).split("/")[0];
            WebApplication webApp = WebApplication.searchWebApplication(appDir);
            if (webApp != null) {
                ServletInfo servletInfo = webApp.searchServlet(path.substring(appDir.length() + 1));
                if (servletInfo != null) {
                    ServletService.doService(
                        method, query, servletInfo, requestHeader, input, output
                    );
                    return;
                }
            }
            // DirectoryIndex
            if (path.endsWith("/")) {
                path += "index.html";
                ext = "html";
            }
            // validate path
            FileSystem fs = FileSystems.getDefault();
            Path pathObj = fs.getPath(DOCUMENT_ROOT + path);
            Path realPath;
            try {
                realPath = pathObj.toRealPath();
            } catch (NoSuchFileException ex) {
                SendResponse.sendNotFoundResponse(output, ERROR_DOCUMENT_ROOT);
                return;
            }
            if (!realPath.startsWith(DOCUMENT_ROOT)) {
                SendResponse.sendNotFoundResponse(output, ERROR_DOCUMENT_ROOT);
                return;
            } else if (Files.isDirectory(realPath)) {
                String location = "http://"
                    + ((host != null) ? host : SERVER_NAME)
                    + path + "/";
                SendResponse.sendMovePermanentlyResponse(output, location);
                return;
            }
            // return resource
            try (
                InputStream fis = new BufferedInputStream(
                    Files.newInputStream(realPath)
                )
            ) {
                SendResponse.sendOkResponse(output, fis, ext);
            } catch (FileNotFoundException ex) {
                SendResponse.sendNotFoundResponse(output, ERROR_DOCUMENT_ROOT);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (output != null) output.close();
                socket.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            } 
        }
    }

    ServerThread(Socket socket) {
        this.socket = socket;
    }
}
