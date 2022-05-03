package henacat.wervletimpl;

import henacat.servlet.http.*;

public class ServletInfo {
    /**
     * A class which store servlet infomation.
     * 
     * webApp           : webApplication which contain the servlet
     * urlPattern       : url to access the servlet
     * servletClassName : name of servlet
     */
    WebApplication webApp;
    String urlPattern;
    String servletClassName;
    HttpServlet servlet;

    public ServletInfo(
        WebApplication webApp, String urlPattern, String servletClassName
    ) {
        this.webApp = webApp;
        this.urlPattern = urlPattern;
        this.servletClassName = servletClassName;
    }
}
