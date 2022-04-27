import java.io.*;
import java.net.*;
import java.nio.file.*;

public class ServerThread implements Runnable
{
    private static final String DOCUMENT_ROOT =
        "/home/nishikawa/playground/webserver_creation/contents";
    private static final String ERROR_DOCUMENT_ROOT = DOCUMENT_ROOT;
    private static final String SERVER_NAME = "localhost:8000";
    private Socket socket;

    @Override
    public void run()
    {
        OutputStream output = null;
        try {

            // parse input
            InputStream input = socket.getInputStream();
            String line;
            String path = null;
            String ext = null;
            String host = null;
            while ((line = Util.readLine(input)) != null)
            {
                if (line.equals("")) break;
                if (line.startsWith("GET")) {
                    path = MyURLDecoder.decode(line.split(" ")[1], "UTF-8");
                    String[] tmp = path.split("\\.");
                    ext = tmp[tmp.length - 1];
                } else if (line.startsWith("Host: ")) {
                    host = line.substring("Host: ".length());
                }
            }
            if (path == null) return;
            
            // DirectoryIndex
            if (path.endsWith("/")) {
                path += "index.html";
                ext = "html";
            }

            // return output
            output = new BufferedOutputStream(socket.getOutputStream());

            // validate requested path
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

            // return response
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
