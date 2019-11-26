import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

@WebServlet("/degree.ctl")
public class DegreeController extends HttpServlet {
    //POST,http://49.235.26.77:8080/degree.ctl,增加学位
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //根据request对象，获得代表参数的JSON字串
        String degree_json = JSONUtil.getJSON(request);
        //将JSON字串解析为Degree对象
        Degree degreeToAdd = JSON.parseObject(degree_json, Degree.class);
        System.out.println(degreeToAdd);
        JSONObject message = new JSONObject();
        try {
            DegreeService.getInstance().add(degreeToAdd);
            message.put("statusCode", "200");
            message.put("message", "add successfully");
        } catch (SQLException e) {
            e.printStackTrace();
            message.put("data", null);
        }
        response.getWriter().println(message);
    }
    //DELETE,http://49.235.26.77:8080/degree.ctl,删除学位
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //读取参数id
        String id_str = request.getParameter("id");
        int id = Integer.parseInt(id_str);
        //到数据库表中删除对应的学院
        JSONObject message = new JSONObject();
        try {
            DegreeService.getInstance().delete(id);
            message.put("statusCode", "200");
            message.put("message", "delete successfully");
        } catch (SQLException e) {
            e.printStackTrace();
            message.put("data", null);
        }
        //响应
        response.getWriter().println(message);
    }
    //PUT,http://49.235.26.77:8080/degree.ctl,修改学位
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String degree_json = JSONUtil.getJSON(request);
        //将JSON字串解析为Degree对象
        Degree degreeToAdd = JSON.parseObject(degree_json, Degree.class);
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //增加加Degree对象
        try {
            DegreeService.getInstance().update(degreeToAdd);
            //加入数据信息
            message.put("statusCode", "200");
            message.put("message", "update successfully");
        } catch (SQLException e) {
            e.printStackTrace();
            message.put("data", null);
        }
        //响应message到前端
        response.getWriter().println(message);
    }
    //GET,http://49.235.26.77:8080/degree.ctl,查找学位
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //读取参数id
        String id_str = request.getParameter("id");
        //如果id = null, 表示响应所有学位对象，否则响应id指定的学位对象
        if(id_str == null){
            try {
                responseDegrees(response);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            int id = Integer.parseInt(id_str);
            try {
                responseDegree(id, response);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    //响应一个学位对象
    private void responseDegree(int id, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //根据id查找学院
        Degree degree = DegreeService.getInstance().find(id);
        String degree_json = JSON.toJSONString(degree);

        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //加入数据信息
        message.put("statusCode", "200");
        message.put("message", "get successfully");

        //响应
        response.getWriter().println(degree_json);
        response.getWriter().println(message);
    }
    //响应所有学位对象
    private void responseDegrees(HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //获得所有学院
        Collection<Degree> degrees = DegreeService.getInstance().findAll();
        String degrees_json = JSON.toJSONString(degrees);

        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //加入数据信息
        message.put("statusCode", "200");
        message.put("message", "列表成功");
        //响应
        response.getWriter().println(degrees_json);
        response.getWriter().println(message);
    }
}