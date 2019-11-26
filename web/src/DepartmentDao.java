import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public final class DepartmentDao {
    private static DepartmentDao departmentDao=new DepartmentDao();

    public static DepartmentDao getInstance(){
        return departmentDao;
    }

    public Collection<Department> findAllBySchool(Integer school_id) throws SQLException{
        Set<Department> departments = new HashSet<>();
        Connection connection = JdbcHelper.getConn();
        Statement statement = connection.createStatement();
        String sql_str = "select * from Department where school_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql_str);
        preparedStatement.setInt(1,school_id);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            School school = SchoolDao.getInstance().find(resultSet.getInt("school_id"));
            Department department = new Department(resultSet.getInt("id"),
                    resultSet.getString("description"),
                    resultSet.getString("no"),
                    resultSet.getString("remarks"),
                    school);
            departments.add(department);
        }
        JdbcHelper.close(resultSet,statement,connection);
        return departments;
    }

    public Collection<Department> findAll() throws SQLException {
        Set<Department> departments = new HashSet<Department>();
        //获得连接对象
        Connection connection = JdbcHelper.getConn();
        Statement statement = connection.createStatement();
        //执行SQL查询语句并获得结果集对象（游标指向结果集的开头）
        ResultSet resultSet = statement.executeQuery("SELECT * FROM DEPARTMENT");
        //若结果集仍然有下一条记录，则执行循环体
        while (resultSet.next()){
            School school = SchoolService.getInstance().find(resultSet.getInt("school_id"));
            //创建Degree对象，根据遍历结果中的id,description,no,remarks值
            Department department = new Department(resultSet.getInt("id"),resultSet.getString("description"),
                    resultSet.getString("no"),resultSet.getString("remarks"),school);
            //向departemnts集合中添加Degree对象
            departments.add(department);
        }
        //关闭资源
        JdbcHelper.close(resultSet,statement,connection);
        return departments;
    }

    public Department find(Integer id) throws SQLException{
        Department department = null;
        Connection connection = JdbcHelper.getConn();
        //创建sql语句，“？”作为占位符
        String str = "SELECT * FROM DEPARTMENT WHERE ID = ?";
        //创建PreparedStatement接口对象，包装编译后的目标代码（可以设置参数，安全性高）
        PreparedStatement pstmt = connection.prepareStatement(str);
        pstmt.setInt(1,id);
        ResultSet resultSet = pstmt.executeQuery();
        if(resultSet.next()){
            School school = SchoolService.getInstance().find(resultSet.getInt("school_id"));
            //创建Degree对象，根据遍历结果中的id,description,no,remarks值
            department = new Department(resultSet.getInt("id"),resultSet.getString("description"),
                    resultSet.getString("no"),resultSet.getString("remarks"),school);

        }
        //若结果集仍然有下一条记录，则执行循环体
        JdbcHelper.close(resultSet,pstmt,connection);
        return department;
    }

    public boolean add(Department department) throws SQLException {
        //获得连接对象
        Connection connection = JdbcHelper.getConn();
        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO DEPARTMENT (ID,NO,DESCRIPTION,REMARKS,SCHOOL_ID) VALUES" +
                        "(?,?,?,?,?)");
        preparedStatement.setInt(1,department.getId());
        preparedStatement.setString(2,department.getNo());
        preparedStatement.setString(3,department.getDescription());
        preparedStatement.setString(4,department.getRemarks());
        preparedStatement.setInt(5,department.getSchool().getId());
        int affectRowNum = preparedStatement.executeUpdate();
        System.out.println("增加了 " + affectRowNum + " 行。");
        JdbcHelper.close(preparedStatement,connection);
        return affectRowNum > 0;
    }

    public boolean delete(Integer id) throws SQLException{
        Connection connection = JdbcHelper.getConn();
        //创建sql语句，“？”作为占位符
        String deleteDepartment_sql = "DELETE FROM DEPARTMENT WHERE ID = ?";
        //创建PreparedStatement接口对象，包装编译后的目标代码（可以设置参数，安全性高）
        PreparedStatement pstmt = connection.prepareStatement(deleteDepartment_sql);
        //为预编译的语句参数赋值
        pstmt.setInt(1,id);
        //执行预编译对象的executeUpdate()方法，获取删除记录的行数
        int affectedRowNum = pstmt.executeUpdate();
        System.out.println("删除了 "+affectedRowNum+" 行");
        JdbcHelper.close(pstmt,connection);
        return affectedRowNum > 0;
    }

    public boolean update(Department department) throws SQLException{
        Connection connection = JdbcHelper.getConn();
        //创建sql语句，“？”作为占位符
        String updateDepartment_sql = "UPDATE DEPARTMENT SET DESCRIPTION = ?, NO = ? ,REMARKS = ?, SCHOOL_ID = ? WHERE ID = ?";
        //创建PreparedStatement接口对象，包装编译后的目标代码（可以设置参数，安全性高）
        PreparedStatement pstmt = connection.prepareStatement(updateDepartment_sql);
        //为预编译的语句参数赋值
        pstmt.setString(1,department.getDescription());
        pstmt.setString(2,department.getNo());
        pstmt.setString(3,department.getRemarks());
        pstmt.setInt(4,department.getSchool().getId());
        pstmt.setInt(5,department.getId());
        //执行预编译对象的executeUpdate()方法，获取删除记录的行数
        int affectedRowNum = pstmt.executeUpdate();
        System.out.println("修改了 "+affectedRowNum+" 行");
        JdbcHelper.close(pstmt,connection);
        return affectedRowNum > 0;
    }

    public static void main(String[] args) throws SQLException {
        DepartmentDao departmentDao = new DepartmentDao();
        Department department = departmentDao.find(3);
        System.out.println(department);
        department.setDescription("土木");
        departmentDao.update(department);
        //打印添加后返回的对象
        Department department2 = departmentDao.find(3);
        System.out.println(department2);
        System.out.println("修改Department对象成功！");
    }
}

