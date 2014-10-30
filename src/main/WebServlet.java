package main;


import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;


@SuppressWarnings("serial")
public class WebServlet extends HttpServlet {
	public static int SERVER_PORT=8081;
    Logger log=Logger.getLogger("restServer");
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8"); // 加上此行代码，避免出现中文乱码
		response.setStatus(HttpServletResponse.SC_OK);
		PrintWriter writer = response.getWriter();

		String uri = request.getRequestURI();
        uri = uri.substring(1);
        log.info(uri);
		String uriPart[]=uri.split("/");
		if(uriPart[0].equals("data")){
		}
        writer.println("nothing");
        writer.flush();
	}


	public void mainProc() throws Exception {
		Server server = new Server(8081);

		ServletContextHandler servlet = new ServletContextHandler(
				ServletContextHandler.SESSIONS);
		servlet.setContextPath("/");
		servlet.addServlet(new ServletHolder(this), "/*");

		ContextHandler context = new ContextHandler();
		context.setContextPath("/");
		context.setResourceBase(".");
		context.setClassLoader(Thread.currentThread().getContextClassLoader());
		context.setHandler(new ResourceHandler());

		ContextHandlerCollection contexts = new ContextHandlerCollection();
		contexts.setHandlers(new Handler[] { servlet, context });

		server.setHandler(contexts);
		server.start();
		log.info("rest server start successfully.");
	}

	public static void main(String args[]) throws Exception{
		WebServlet s=new WebServlet();
		s.mainProc();
	}
}
