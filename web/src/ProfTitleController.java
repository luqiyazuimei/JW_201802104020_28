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

@WebServlet("/profTitle.ctl")
public class ProfTitleController extends HttpServlet {
    //POST,http://49.235.26.77:8080/profTitle.ctl,增加职称
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //根据request对象，获得代表参数的JSON字串
        String profTitle_json = JSONUtil.getJSON(request);
        //将JSON字串解析为ProfTitle对象
        ProfTitle profTitleToAdd = JSON.parseObject(profTitle_json, ProfTitle.class);
        System.out.println(profTitleToAdd);
        JSONObject message = new JSONObject();
        try {
            ProfTitleService.getInstance().add(profTitleToAdd);
            message.put("statusCode", "200");
            message.put("message", "add successfully");
        } catch (SQLException e) {
            e.printStackTrace();
            message.put("data", null);
        }
        response.getWriter().println(message);
    }
    //DELETE,http://49.235.26.77:8080/profTitle.ctl,删除职称
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //读取参数id
        String id_str = request.getParameter("id");
        int id = Integer.parseInt(id_str);
        //到数据库表中删除对应的学院
        JSONObject message = new JSONObject();
        try {
            ProfTitleService.getInstance().delete(id);
            message.put("statusCode", "200");
            message.put("message", "delete successfully");
        } catch (SQLException e) {
            e.printStackTrace();
            message.put("data", null);
        }
        response.getWriter().println(message);
    }
    //PUT,http://49.235.26.77:8080/profTitle.ctl,修改职称
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String profTitle_json = JSONUtil.getJSON(request);
        //将JSON字串解析为ProfTitle对象
        ProfTitle profTitleToAdd = JSON.parseObject(profTitle_json, ProfTitle.class);
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //增加加ProfTitle对象
        try {
            ProfTitleService.getInstance().update(profTitleToAdd);
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
    //GET,http://49.235.26.77:8080/profTitle.ctl,查找职称
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //读取参数id
        String id_str = request.getParameter("id");
        //如果id = null, 表示响应所有学位对象，否则响应id指定的学位对象
        if(id_str == null){
            try {
                responseProfTitles(response);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            int id = Integer.parseInt(id_str);
            try {
                responseProfTitle(id, response);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    //响应一个学位对象
    private void responseProfTitle(int id, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //根据id查找学院
        ProfTitle profTitle = ProfTitleService.getInstance().find(id);
        String profTitle_json = JSON.toJSONString(profTitle);

        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //加入数据信息
        message.put("statusCode", "200");
        message.put("message", "get successfully");

        //响应
        response.getWriter().println(profTitle_json);
        response.getWriter().println(message);
    }
    //响应所有学位对象
    private void responseProfTitles(HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //获得所有学院
        Collection<ProfTitle> profTitles = ProfTitleService.getInstance().findAll();
        String profTitles_json = JSON.toJSONString(profTitles);
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //加入数据信息
        message.put("statusCode", "200");
        message.put("message", "列表成功");
        //响应message到前端
        response.getWriter().println(profTitles_json);
        response.getWriter().println(message);
    }
}