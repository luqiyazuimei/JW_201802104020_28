import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

@WebServlet("/department.ctl")
public class DepartmentController extends HttpServlet {
    private void responseDepartmentOfSchool(int id,HttpServletResponse response)
            throws ServletException,IOException,SQLException{
        JSONObject message = new JSONObject();
        Collection<Department> departments = DepartmentService.getInstance().findAllBySchool(id);
        String department_json = JSON.toJSONString(departments, SerializerFeature.DisableCircularReferenceDetect);
        message.put("statusCode","200");
        message.put("message","查找成功");
        message.put("data",department_json);
        response.getWriter().println(department_json);
    }
    //POST,http://49.235.26.77:8080/department.ctl,增加系
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //根据request对象，获得代表参数的JSON字串
        String department_json = JSONUtil.getJSON(request);
        //将JSON字串解析为Department对象
        Department departmentToAdd = JSON.parseObject(department_json, Department.class);
        System.out.println(departmentToAdd);
        JSONObject message = new JSONObject();
        try {
            DepartmentService.getInstance().add(departmentToAdd);
            message.put("statusCode", "200");
            message.put("message", "add successfully");
        } catch (SQLException e) {
            e.printStackTrace();
            message.put("data", null);
        }
        response.getWriter().println(message);
    }
    //DELETE,http://49.235.26.77:8080/department.ctl,删除系
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //读取参数id
        String id_str = request.getParameter("id");
        int id = Integer.parseInt(id_str);
        //到数据库表中删除对应的学院
        JSONObject message = new JSONObject();
        try {
            boolean deleted = DepartmentService.getInstance().delete(id);
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
        response.setContentType("html/text;charset=UTF8");
        response.getWriter().println(message);
    }
    //PUT,http://49.235.26.77:8080/department.ctl,修改系
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String department_json = JSONUtil.getJSON(request);
        //将JSON字串解析为Department对象
        Department departmentToAdd = JSON.parseObject(department_json, Department.class);
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //增加加Department对象
        try {
            DepartmentService.getInstance().update(departmentToAdd);
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
    //GET,http://49.235.26.77:8080/department.ctl,查找系
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String school = request.getParameter("paraType");
        //读取参数id
        String id_str = request.getParameter("id");
        JSONObject message = new JSONObject();
        //如果id = null, 表示响应所有学位对象，否则响应id指定的学位对象
        try{
            if (id_str == null){
                responseDepartments(response);
            }else if(school == null){
                int id = Integer.parseInt(id_str);
                responseDepartment(id,response);
            }else {
                int id = Integer.parseInt(id_str);
                responseDepartmentOfSchool(id,response);
            }
            message.put("message","查找成功");
        } catch (SQLException e) {
            message.put("message","数据库操作异常");
            response.getWriter().println(message);
        }catch (Exception e){
            message.put("message","网络异常");
            response.getWriter().println(message);
        }
    }
    //响应一个学位对象
    private void responseDepartment(int id, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //根据id查找学院
        Department department = DepartmentService.getInstance().find(id);
        String department_json = JSON.toJSONString(department);

        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //加入数据信息
        message.put("statusCode", "200");
        message.put("message", "get successfully");
        //响应
        response.getWriter().println(department_json);
        response.getWriter().println(message);
    }
    //响应所有学位对象
    private void responseDepartments(HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //获得所有学院
        Collection<Department> departments = DepartmentService.getInstance().findAll();
        String departments_json = JSON.toJSONString(departments);
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //加入数据信息
        message.put("statusCode", "200");
        message.put("message", "列表成功");
        //响应
        response.getWriter().println(departments_json);
        response.getWriter().println(message);
    }
}
