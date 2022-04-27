import java.io.*;
import java.util.*;
import java.text.*;

class Util {
     
    // read binary line from InputStream
    static String readLine(InputStream input)
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
    static void writeLine(OutputStream output, String str)
    throws Exception {
        for (char ch : str.toCharArray())
        {
            output.write((int)ch);
        }
        output.write('\r');
        output.write('\n');
    }

    // convert time into HTTP format
    static String getDataStringUtc()
    {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        DateFormat df = new SimpleDateFormat(
            "EEE, dd MMM yyyy HH:mm:ss", Locale.US
        );
        df.setTimeZone(cal.getTimeZone());
        return df.format(cal.getTime()) + " GMT";
    }

    // table of tuple File-Extension - Content-Type
    static final HashMap<String, String> contentTypeMap =
        new HashMap<String, String>() {
            private static final long serialVersionUIID = 1L;
            {
            put("html", "text/html");
            put("htm", "text/html");
            put("txt", "text/plain");
            put("css", "text/css");
            put("png", "image/png");
            put("jpg", "image/jpeg");
            put("jpeg", "image/jpeg");
            put("gif", "image/gif");
            }
        };
    // return Content-Type from File-Extension
    static String getContentType(String ext)
    {
        String ret = contentTypeMap.get(ext.toLowerCase());
        if (ret == null) {
            return "application/octet-stream";
        } else {
            return ret;
        }
    }
    
}