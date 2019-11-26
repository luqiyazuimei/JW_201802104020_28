import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public final class SchoolDao {
    private static SchoolDao schoolDao = new SchoolDao();

    public static SchoolDao getInstance(){
        return schoolDao;
    }

    public Collection<School> findAll() throws SQLException {
        Set<School> schools = new HashSet<School>();
        //获得连接对象
        Connection connection = JdbcHelper.getConn();
        Statement statement = connection.createStatement();
        //执行SQL查询语句并获得结果集对象（游标指向结果集的开头）
        ResultSet resultSet = statement.executeQuery("SELECT * FROM SCHOOL");
        //若结果集仍然有下一条记录，则执行循环体
        while (resultSet.next()){
            //创建Degree对象，根据遍历结果中的id,description,no,remarks值
            School school = new School(resultSet.getInt("id"),resultSet.getString("description"),
                    resultSet.getString("no"),resultSet.getString("remarks"));
            //向degrees集合中添加Degree对象
            schools.add(school);
        }
        //关闭资源
        JdbcHelper.close(resultSet,statement,connection);
        return schools;
    }

    public School find(Integer id) throws SQLException{
        School school = null;
        Connection connection = JdbcHelper.getConn();
        //创建sql语句，“？”作为占位符
        String str = "SELECT * FROM SCHOOL WHERE ID = ?";
        //创建PreparedStatement接口对象，包装编译后的目标代码（可以设置参数，安全性高）
        PreparedStatement pstmt = connection.prepareStatement(str);
        pstmt.setInt(1,id);
        ResultSet resultSet = pstmt.executeQuery();
        if (resultSet.next()){
            //创建Degree对象，根据遍历结果中的id,description,no,remarks值
            school = new School(resultSet.getInt("id"),resultSet.getString("description"),
                    resultSet.getString("no"),resultSet.getString("remarks"));
        }
        JdbcHelper.close(resultSet,pstmt,connection);
        return school;
    }

    public boolean add (School school) throws SQLException{
        //加载驱动程序
        Connection connection = JdbcHelper.getConn();
        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO SCHOOL (ID,NO,DESCRIPTION,REMARKS) VALUES" +
                        "(?,?,?,?)");
        preparedStatement.setInt(1,school.getId());
        preparedStatement.setString(2,school.getNo());
        preparedStatement.setString(3,school.getDescription());
        preparedStatement.setString(4,school.getRemarks());
        int affectRowNum = preparedStatement.executeUpdate();
        System.out.println("增加了 " + affectRowNum + " 行。");
        JdbcHelper.close(preparedStatement,connection);
        return affectRowNum > 0;
    }

    public boolean delete(Integer id) throws SQLException{
        Connection connection = JdbcHelper.getConn();
        //创建sql语句，“？”作为占位符
        String deleteSchool_sql = "DELETE FROM SCHOOL WHERE ID = ?";
        //创建PreparedStatement接口对象，包装编译后的目标代码（可以设置参数，安全性高）
        PreparedStatement pstmt = connection.prepareStatement(deleteSchool_sql);
        //为预编译的语句参数赋值
        pstmt.setInt(1,id);
        //执行预编译对象的executeUpdate()方法，获取删除记录的行数
        int affectedRowNum = pstmt.executeUpdate();
        System.out.println("删除了 "+affectedRowNum+" 行");
        JdbcHelper.close(pstmt,connection);
        return affectedRowNum > 0;
    }

    public boolean update(School school) throws SQLException{
        Connection connection = JdbcHelper.getConn();
        //创建sql语句，“？”作为占位符
        String updateSchool_sql = "UPDATE SCHOOL SET DESCRIPTION = ?, NO = ? ,REMARKS = ? WHERE ID = ?";
        //创建PreparedStatement接口对象，包装编译后的目标代码（可以设置参数，安全性高）
        PreparedStatement pstmt = connection.prepareStatement(updateSchool_sql);
        //为预编译的语句参数赋值
        pstmt.setString(1,school.getDescription());
        pstmt.setString(2,school.getNo());
        pstmt.setString(3,school.getRemarks());
        pstmt.setInt(4,school.getId());
        //执行预编译对象的executeUpdate()方法，获取修改记录的行数
        int affectedRowNum = pstmt.executeUpdate();
        System.out.println("修改了 "+affectedRowNum+" 行");
        JdbcHelper.close(pstmt,connection);
        return affectedRowNum > 0;
    }

    public static void main(String[] args) throws SQLException {
        SchoolDao schoolDao = new SchoolDao();
        School school = schoolDao.find(972);
        System.out.println(school);
        school.setDescription("环境学院");
        schoolDao.update(school);
        School school1 = schoolDao.find(972);
        //打印修改后返回的对象
        System.out.println(school);
        System.out.println("修改School对象成功！");
    }
}
