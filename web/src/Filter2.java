import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Calendar;

@WebFilter(filterName = "Filter2",
        urlPatterns = {"/*"})
public class Filter2 implements Filter {
    public void destroy() {
    }
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        System.out.println("Filter2 - encoding begins");
        HttpServletRequest request = (HttpServletRequest)req;
        String path = request.getRequestURI();
        Calendar cal = Calendar.getInstance();
        String time = cal.get(Calendar.YEAR) + " 年 "
                + (cal.get(Calendar.MONTH) + 1) + " 月 "
                + cal.get(Calendar.DATE) + "日 时间："
                + cal.get(Calendar.HOUR_OF_DAY) + ": "
                + cal.get(Calendar.MINUTE);
        System.out.println("请求的资源为：" + path + " @ " + time);
        chain.doFilter(req, resp);
        System.out.println("Filter2 - encoding ends");
    }
    public void init(FilterConfig config) throws ServletException {
    }
}