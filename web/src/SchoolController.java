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

@WebServlet("/school.ctl")
public class SchoolController extends HttpServlet {
    //POST,http://49.235.26.77:8080/school.ctl,增加学院
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //根据request对象，获得代表参数的JSON字串
        String school_json = JSONUtil.getJSON(request);
        //将JSON字串解析为School对象
        School schoolToAdd = JSON.parseObject(school_json, School.class);
        schoolToAdd.setId(4+(int)(Math.random()*100));
        System.out.println(schoolToAdd);
        JSONObject message = new JSONObject();
        try {
            SchoolService.getInstance().add(schoolToAdd);
            //message.put("statusCode", "200");
            message.put("message", "add successfully");
        } catch (SQLException e) {
            e.printStackTrace();
            //message.put("data", null);
        }
        response.getWriter().println(message);
    }
    //DELETE,http://49.235.26.77:8080/school.ctl,删除学院
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //读取参数id
        String id_str = request.getParameter("id");
        int id = Integer.parseInt(id_str);
        //到数据库表中删除对应的学院
        JSONObject message = new JSONObject();
        try {
            SchoolService.getInstance().delete(id);
            //message.put("statusCode", "200");
            message.put("message", "delete successfully");
        } catch (SQLException e) {
            e.printStackTrace();
            //message.put("data", null);
        }
        response.getWriter().println(message);
    }
    //PUT,http://49.235.26.77:8080/school.ctl,修改学院
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String school_json = JSONUtil.getJSON(request);
        //将JSON字串解析为School对象
        School schoolToAdd = JSON.parseObject(school_json, School.class);
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //增加加School对象
        try {
            SchoolService.getInstance().update(schoolToAdd);
            //加入数据信息
            //message.put("statusCode", "200");
            message.put("message", "update successfully");
        } catch (SQLException e) {
            e.printStackTrace();
            message.put("data", null);
        }
        //响应message到前端
        response.getWriter().println(message);
    }
    //GET,http://49.235.26.77:8080/school.ctl,查找学院
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //读取参数id
        String id_str = request.getParameter("id");
        //如果id = null, 表示响应所有学位对象，否则响应id指定的学位对象
        if(id_str == null){
            try {
                responseSchools(response);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            int id = Integer.parseInt(id_str);
            try {
                responseSchool(id, response);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    //响应一个学位对象
    private void responseSchool(int id, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //根据id查找学院
        School school = SchoolService.getInstance().find(id);
        String school_json = JSON.toJSONString(school);

        //创建JSON对象message，以便往前端响应信息
        //JSONObject message = new JSONObject();
        //加入数据信息
        //message.put("statusCode", "200");
        //message.put("message", "get successfully");

        //响应
        response.getWriter().println(school_json);

    }
    //响应所有学位对象
    private void responseSchools(HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //获得所有学院
        Collection<School> schools = SchoolService.getInstance().findAll();
        String schools_json = JSON.toJSONString(schools);

        //创建JSON对象message，以便往前端响应信息
        //JSONObject message = new JSONObject();
        //加入数据信息
        //message.put("statusCode", "200");
        //message.put("message", "列表成功");
        //响应
        response.getWriter().println(schools_json);
        //response.getWriter().println(message);
    }
}