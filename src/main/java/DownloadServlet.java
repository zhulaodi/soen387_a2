import com.example.model.Manager;
import com.example.model.Post;
import com.example.model.User;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.servlet.ServletException;

import com.google.protobuf.Enum;
import org.apache.commons.io.FileUtils;


@MultipartConfig
@WebServlet(name = "DownloadServlet")
public class DownloadServlet extends HttpServlet {
    private static final MimetypesFileTypeMap mimetypesFileTypeMap = new MimetypesFileTypeMap();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        User user = (User)request.getSession().getAttribute("user");
        if(user!=null){
            String numPosts = request.getParameter("numPosts");
            if(numPosts==null) {
                displayPosts(request, Manager.getAllPost(), 10);

                request.getRequestDispatcher("message-board.jsp").forward(request, response);
            }
            else{
                displayPosts(request, Manager.getAllPost(), Integer.parseInt(numPosts));
                request.getRequestDispatcher("message-board.jsp").forward(request, response);
            }


        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String register = request.getParameter("register");
        if (register != null)
            register(request, response);

        String login = request.getParameter("login");
        if (login != null)
            login(request, response);

        String logout = request.getParameter("logout");
        if (logout != null)
            logout(request, response);

        String createPost = request.getParameter("create-post");
        if (createPost != null)
            createPost(request, response);

        String updatePost = request.getParameter("update-post");
        if (updatePost != null)
            updatePost(request, response);

        String deletePost = request.getParameter("delete-post");
        if (deletePost != null)
            deletePost(request, response);

        String searchPost = request.getParameter("search-post");
        if (searchPost != null)
            searchPost(request, response);

        String downloadFile = request.getParameter("download-file");
        if (downloadFile != null)
            downloadFile(request, response);
    }

    private void register(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setAttribute("signupError","1");

        request.getRequestDispatcher("login.jsp").forward(request, response);
    }

    private void login(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String email    = request.getParameter("login-email");
        String password = request.getParameter("login-password");

        File usersFile = new File(getServletContext().getRealPath("/") + "users.xml");

        User user = Manager.authenticate(email, password, usersFile);
        if (user != null) {
            request.getSession().setAttribute("user", user);
            List<Post> posts=null;
            if(Manager.getAllPost().size()>10)
                posts = Manager.getAllPost().subList(0, 10);
            else
                posts = Manager.getAllPost();
            request.setAttribute("posts", posts);

            request.getRequestDispatcher("message-board.jsp").forward(request, response);
        }
        else
            request.getRequestDispatcher("login.jsp").forward(request, response);
    }

    private void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getSession().setAttribute("user", null);

        request.getRequestDispatcher("login.jsp").forward(request, response);
    }

    private void createPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String title = request.getParameter("create-post-title");
        String text  = request.getParameter("create-post-text");
        //text=text.replaceAll("\n","<br/>").replaceAll("\r","");
        User user = (User)request.getSession().getAttribute("user");

        int userId = Integer.parseInt(user.getUserId());

        Post post = new Post(userId, title, text);

        Manager.createPost(post);

        int postId = post.getPostId();

        Part part = request.getPart("create-post-file");

        if (part.getSize() > 0)
            Manager.insertFile(part, postId);

        displayPosts(request, Manager.getAllPost(), 10);

        request.getRequestDispatcher("message-board.jsp").forward(request, response);
    }

    private void updatePost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String postIdString = request.getParameter("update-delete-post-id");
        String title        = request.getParameter("update-post-title");
        String text         = request.getParameter("update-post-text");

        int postId = Integer.parseInt(postIdString);

        Post post = Manager.getPost(postId);

        int userId = post.getUserId();

        User currentUser = (User)request.getSession().getAttribute("user");

        int currentUserId = Integer.parseInt(currentUser.getUserId());
        if (currentUserId == userId) {
            post.setTitle(title);
            post.setText(text);

            Manager.updatePost(post);

            Part part = request.getPart("update-post-file");
            if (part.getSize() > 0) {
                File file = Manager.selectFile(postId);
                if (file != null)
                    Manager.updateFile(part, postId);
                else
                    Manager.insertFile(part, postId);
            }
        }

        displayPosts(request, Manager.getAllPost(), 10);

        request.getRequestDispatcher("message-board.jsp").forward(request, response);
    }

    private void deletePost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String postIdString = request.getParameter("update-delete-post-id");

        int postId = Integer.parseInt(postIdString);

        Post post = Manager.getPost(postId);

        int userId = post.getUserId();

        User currentUser = (User)request.getSession().getAttribute("user");

        int currentUserId = Integer.parseInt(currentUser.getUserId());
        if (currentUserId == userId) {
            Manager.deletePost(postId);

            Manager.deleteFile(postId);
        }

        displayPosts(request, Manager.getAllPost(), 10);

        request.getRequestDispatcher("message-board.jsp").forward(request, response);
    }

    private void searchPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userIdString = request.getParameter("search-post-user-id");
        String dateFrom     = request.getParameter("search-post-date-from");
        String dateTo       = request.getParameter("search-post-date-to");
        String hashTag      = request.getParameter("search-post-hash-tag");
        String numString    = request.getParameter("search-post-num-string");

        ArrayList<Post> posts = Manager.getPost(userIdString, dateFrom, dateTo, hashTag, numString);
        request.setAttribute("posts", posts);

        request.getRequestDispatcher("message-board.jsp").forward(request, response);
    }

    private void downloadFile(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String postIdString = request.getParameter("download-file-post-id");
        System.out.println(postIdString);
        int postId = Integer.parseInt(postIdString);

        File file = Manager.selectFile(postId);
        if (file != null) {
            String contentType = mimetypesFileTypeMap.getContentType(file);
            response.setContentType(contentType);

            String fileName = file.getName();
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

            OutputStream outputStream = response.getOutputStream();
            outputStream.write(FileUtils.readFileToByteArray(file));
            outputStream.flush();
            outputStream.close();
        }
        else {
            request.setAttribute("error-message", "No file attached in this post.");

            displayPosts(request, Manager.getAllPost(), 10);

            request.getRequestDispatcher("message-board.jsp").forward(request, response);
        }
    }

    private void displayPosts(HttpServletRequest request, List<Post> posts, int numPosts) {
        for(Post i:posts){
            i.setText(i.getText().replaceAll("\n","<br/>").replaceAll("\r",""));
        }
        if(posts.size() > numPosts)
            request.setAttribute("posts", posts.subList(0, numPosts));
        else
            request.setAttribute("posts", posts);
    }
}