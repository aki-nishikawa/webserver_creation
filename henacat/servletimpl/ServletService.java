package henacat.servletimpl;
import java.io.*;
import java.util.*;

import henacat.servlet.http.*;
import henacat.util.*;

public class ServletService {

    private static HttpServlet createServlet(ServletInfo info)
    throws Exception {
        Class<?> clazz
            = info.webApp.classloader.loadClass(info.servletClassname);
        return (HttpServlet)clazz.newInstance();
    }

    private static Map<String, String[]> stringToMap(String str)
    {
        /**
        * Parse query parameter and store them to HashMap.
        */
        Map<String, String[]> parameterMap = new HashMap<String, String[]>();
        if (str != null) {
            String[] paramArray = str.split("&");
            for (String param: paramArray)
            {
                String[] keyValue = param.split("=", -1);
                if (parameterMap.containsKey(keyValue[0])) {
                    String[] array = parameterMap.get(keyValue[0]);
                    String[] newArray = new String[array.length + 1];
                    System.arraycopy(array, 0, newArray, 0, array.length);
                    newArray[array.length] = keyValue[1];
                    parameterMap.put(keyValue[0], newArray);
                } else {
                    parameterMap.put(keyValue[0], new String[] {keyValue[1]});
                }
            }
        }
        return parameterMap;
    }

    private static String readToSize(InputStream input, int size)
    throws Exception{
        /**
        * Convert request body into string.
        */
        int ch;
        StringBuilder sb = new StringBuilder();
        int readSize = 0;
        while (readSize < size && (ch = input.read()) != -1)
        {
            sb.append((char)ch);
            readSize++;
        }
        return sb.toString();
    }

    public static void doService(
        String method, String query, ServletInfo info,
        Map<String, Stirng> requestHeader,
        InputStream input, OutputStream output
    ) throws Exception {
        /**
         * Parse request and return response.
         * Before return response, response is bufferd.
         */

        if (info.servlet == null) {
            info.servlet = createServlet(info);
        }

        // define buffer for response
        ByteArrayOutputStream outputBuffer = new ByteArrayOutputStream();
        HttpServletResponseImpl resp
            = new HttpServletResponseImpl(outputBuffer);
        // parse request
        HttpServletRequest req;
        if (method.equals("GET")) {
            Map<String, String[]> map;
            map = stringToMap(query);
            req = new HttpServletRequestImpl("GET", map);
        } else if (method.equals("POST")) {
            int contentLength
                = Integer.parseInt(requestHeader.get("CONTENT-LENGTH"));
            Map<String, String[]> map;
            String line = readToSize(line);
            req = new HttpServletRequestImpl("POST", map);
        } else {
            throw new AssertionError("BAD METHOD: " + method);
        }
        // put response into buffer
        info.servlet.service(req, resp);

        // check and return response
        if (resp.status == HttpServletResponse.SC_OK) {
            //return 200 response
            SendResponse.sendOkResponseHeader(output, resp.contentType);
            resp.printWriter.flush();
            byte[] outputBytes = outputBuffer.toByteArrary();
            for (byte b: outputBytes)
                output.write((int)b);
        } else if (resp.status == HttpServletResponse.SC_FOUND) {
            // redirect
            String redirectLocation;
            if (resp.redirectLocation.startsWith("/")) {
                String host = requestHeader.get("HOST");
                redirectLocation = "http://"
                            + ((host != null) ? host : Constants.SERVER_NAME)
                            + resp.redirectLocation;
            } else {
                redirectLocation = resp.redirectLocation;
            }
            SendResponse.sendFoundResponse(output, redirectLocation);
        }

    }

}
