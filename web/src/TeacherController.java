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

@WebServlet("/teacher.ctl")
public class TeacherController extends HttpServlet {
    //POST,http://49.235.26.77:8080/teacher.ctl,增加老师
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //根据request对象，获得代表参数的JSON字串
        String teacher_json = JSONUtil.getJSON(request);
        //将JSON字串解析为Teacher对象
        Teacher teacherToAdd = JSON.parseObject(teacher_json, Teacher.class);
        System.out.println(teacherToAdd);
        JSONObject message = new JSONObject();
        try {
            TeacherService.getInstance().add(teacherToAdd);
            message.put("statusCode", "200");
            message.put("message", "add successfully");
        } catch (SQLException e) {
            e.printStackTrace();
            message.put("data", null);
        }
        response.getWriter().println(message);
    }
    //DELETE,http://49.235.26.77:8080/teacher.ctl,删除老师
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //读取参数id
        String id_str = request.getParameter("id");
        int id = Integer.parseInt(id_str);
        //到数据库表中删除对应的学院
        JSONObject message = new JSONObject();
        try {
            boolean deleted = TeacherService.getInstance().delete(id);
            if (deleted) {
                message.put("message", "delete successfully");
            }else {
                message.put("message", "there is no record of it");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            message.put("data", null);
        }
        //响应
        response.getWriter().println(message);
    }
    //PUT,http://49.235.26.77:8080/teacher.ctl,修改老师
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String teacher_json = JSONUtil.getJSON(request);
        //将JSON字串解析为Teacher对象
        Teacher teacherToAdd = JSON.parseObject(teacher_json, Teacher.class);
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //增加加Teacher对象
        try {
            TeacherService.getInstance().update(teacherToAdd);
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
    //GET,http://49.235.26.77:8080/teacher.ctl,查找老师
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //读取参数id
        String id_str = request.getParameter("id");
        //如果id = null, 表示响应所有学位对象，否则响应id指定的学位对象
        if(id_str == null){
            try {
                responseTeachers(response);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            int id = Integer.parseInt(id_str);
            try {
                responseTeacher(id, response);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    //响应一个学位对象
    private void responseTeacher(int id, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //根据id查找学院
        Teacher teacher = TeacherService.getInstance().find(id);
        String teacher_json = JSON.toJSONString(teacher);
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //加入数据信息
        message.put("statusCode", "200");
        message.put("message", "get successfully");
        //响应
        response.getWriter().println(teacher_json);
        response.getWriter().println(message);
    }
    //响应所有学位对象
    private void responseTeachers(HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //获得所有学院
        Collection<Teacher> teachers = TeacherService.getInstance().findAll();
        String teachers_json = JSON.toJSONString(teachers);
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //加入数据信息
        message.put("statusCode", "200");
        message.put("message", "列表成功");
        //响应
        response.getWriter().println(teachers_json);
        response.getWriter().println(message);
    }
}