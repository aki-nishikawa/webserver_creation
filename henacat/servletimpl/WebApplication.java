package henacat.servletimpl;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;

public class WebApplication {
    /**
     * A class which contain webApplications.
     * wevAppCollection stores webApplications by using dir as key.
     * servletCollestion stores servlet by using dir as key.
     * An instance of servlet is ServletInfo class.
     */
    private static String WEBAPPS_DIR
        = "/home/nishikawa/playground/webserver_creation/contents/webapps";
    private static Map<String, WebApplication> webAppCollection
        = new HashMap<String, WebApplication>();
    String directory;
    ClassLoader classLoader;
    private Map<String, ServletInfo> servletCollection
        = new HashMap<Stirng, ServletInfo>();
    
    private WebApplication(String dir)
    throws MalformedURLException {
        this.directory = dir;
        FileSystem fs = FileSystems.getDefault();
        Path pathObj = fs.getPath(WEBAPPS_DIR, File.separator + dir);
        this.classLoader
            = URLClassLoader.newInstance(new URL[]{pathObj.toUrl().toURL()});
    }

    public static WebApplication createInstance(String dir)
    throws MalformedURLException{
        WebApplication newApp = new Webapplication(dir);
        webAppCollection.put(dir, newApp);
        return newApp;
    }

    public void addServlet(String urlPattern, String wervletClassName) {
        this.servletCollection.put(
            urlPattern, new ServletInfo(this, urlPattern, servletClassName)
        );
    }

    public ServletInfo serchServlet(String path) {
        return servletCollection.get(path);
    }

    public static WebApplication searchWebApplication(String dir) {
        return webAppCollection.get(dir);
    }

}
