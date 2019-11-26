import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "Filter1",
    urlPatterns = {"/*"})
public class Filter1 implements Filter {
    public void destroy() {
    }
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        String path = request.getRequestURI();
        boolean contains = path.contains("/login");
        String method = request.getMethod();
        if(!contains){
            if (method.equals("PUT") || method.equals("POST")){
                //设置响应字符编码为UTF-8
                response.setContentType("text/html;charset=UTF-8");
                //设置请求字符编码为UTF-8
                request.setCharacterEncoding("UTF-8");
            }else {
                //设置请求字符编码为UTF-8
                response.setContentType("text/html;charset=UTF-8");
            }
        }
        chain.doFilter(req, resp);
        System.out.println("Filter1 - encoding ends");
    }
    public void init(FilterConfig config) throws ServletException {
    }
}
