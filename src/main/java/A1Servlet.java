import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import com.example.model.Manager;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;


@MultipartConfig
@WebServlet(name = "A1Servlet")
public class A1Servlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String referer = request.getHeader("Referer");
        if (referer == null) {
            HttpSession session = request.getSession();

            session.setAttribute("referer-error", "Referer is not present.");

            response.sendRedirect(request.getContextPath());

            return;
        }

        String download = request.getParameter("download");
        if (download != null)
            downloadMessages(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String referer = request.getHeader("Referer");
        if (referer == null) {
            request.getSession().setAttribute("referer-error", "Referer is not present.");

            response.sendRedirect(request.getContextPath());

            return;
        }

        String post = request.getParameter("post");
        if (post != null)
            postMessage(request, response);

        String clear = request.getParameter("clear");
        if (clear != null)
            clearMessages(request, response);

        String refresh = request.getParameter("refresh");
        if (refresh != null)
            refresh(request, response);

        String switch_theme = request.getParameter("switch-theme");
        if (switch_theme != null)
            switchTheme(request, response);

        response.setHeader("Expires", "0");
    }

    private void downloadMessages(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String from   = request.getParameter("download-from");
        String to     = request.getParameter("download-to");
        String format = request.getParameter("download-format");

        int postId = Integer.parseInt(format);

        File file = Manager.selectFile(postId);

        String fileName = file.getName();

        byte[] fileContent = FileUtils.readFileToByteArray(file);

        String fileExtension = FilenameUtils.getExtension(fileName);
        if (fileExtension.equals("txt"))
            response.setContentType("text/plain");
        else if (fileExtension.equals("xml"))
            response.setContentType("text/xml");

        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        OutputStream outputStream = response.getOutputStream();
        outputStream.write(fileContent);
        outputStream.flush();
        outputStream.close();
    }

    private void postMessage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String user    = request.getParameter("user");
        String message = request.getParameter("message");

        int postId = Integer.parseInt(user);

        Part part = request.getPart("file");

        Manager.insertFile(part, postId);

        ChatManager.PostMessage(user, message);

        request.setAttribute("messages", ChatManager.ListMessages());

        request.getRequestDispatcher("/").forward(request, response);
    }

    private void clearMessages(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String from = request.getParameter("clear-from");
        String to   = request.getParameter("clear-to");

        if (!from.equals("") && !to.equals(""))
            ChatManager.ClearChat(from, to);
        else
            ChatManager.ClearChat();

        request.setAttribute("messages", ChatManager.ListMessages());

        request.getRequestDispatcher("/").forward(request, response);
    }

    private void refresh(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("messages", ChatManager.ListMessages());

        request.getRequestDispatcher("/").forward(request,response);
    }

    private void switchTheme(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        String theme = (String)session.getAttribute("theme");
        if (theme == null || theme.equals("blue"))
            session.setAttribute("theme", "green");
        else
            session.setAttribute("theme", "blue");

        request.setAttribute("messages", ChatManager.ListMessages());

        request.getRequestDispatcher("/").forward(request,response);
    }
}
