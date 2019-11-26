import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public final class DegreeDao {

    private static DegreeDao degreeDao= new DegreeDao();

    public static DegreeDao getInstance(){
        return degreeDao;
    }

    public Collection<Degree> findAll() throws SQLException {
        //创建一个集合
        Set<Degree> degreeSet = new HashSet<>();
        //获得一个连接
        Connection connection = JdbcHelper.getConn();
        //创建语句盒子
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM DEGREE");
        //遍历集合中的元素加入到degreeSet集合中
        while (resultSet.next()){
            //创建Degree对象
            Degree degree = new Degree(resultSet.getInt("id"),
                    resultSet.getString("description"),resultSet.getString("no"),
                    resultSet.getString("remarks"));
            degreeSet.add(degree);
        }
        JdbcHelper.close(resultSet,statement,connection);
        return degreeSet;
    }

    public Degree find(Integer id) throws SQLException{
        Degree degree = null;
        //获得连接对象
        Connection connection = JdbcHelper.getConn();
        //创建sql语句，“？”作为占位符
        String str = "SELECT * FROM DEGREE WHERE ID = ?";
        //创建PreparedStatement接口对象，包装编译后的目标代码（可以设置参数，安全性高）
        PreparedStatement pstmt = connection.prepareStatement(str);
        pstmt.setInt(1,id);
        ResultSet resultSet = pstmt.executeQuery();
        if(resultSet.next()){
            degree = new Degree(resultSet.getInt("id"),resultSet.getString("description"),
                    resultSet.getString("no"),resultSet.getString("remarks"));
        }
        JdbcHelper.close(resultSet,pstmt,connection);
        return degree;
    }
    public boolean add(Degree degree) throws SQLException{
        //加载驱动程序
        Connection connection = JdbcHelper.getConn();
        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO DEGREE (ID,NO,DESCRIPTION,REMARKS) VALUES" +
                        "(?,?,?,?)");
        preparedStatement.setInt(1,degree.getId());
        preparedStatement.setString(2,degree.getNo());
        preparedStatement.setString(3,degree.getDescription());
        preparedStatement.setString(4,degree.getRemarks());
        int affectRowNum = preparedStatement.executeUpdate();
        System.out.println("增加了 " + affectRowNum + " 行。");
        JdbcHelper.close(preparedStatement,connection);
        return affectRowNum > 0;
    }
    public boolean delete(Integer id) throws SQLException{
        Connection connection = JdbcHelper.getConn();
        //创建sql语句，“？”作为占位符
        String deleteDegree_sql = "DELETE FROM DEGREE WHERE ID = ?";
        //创建PreparedStatement接口对象，包装编译后的目标代码（可以设置参数，安全性高）
        PreparedStatement pstmt = connection.prepareStatement(deleteDegree_sql);
        //为预编译的语句参数赋值
        pstmt.setInt(1,id);
        //执行预编译对象的executeUpdate()方法，获取删除记录的行数
        int affectedRowNum = pstmt.executeUpdate();
        System.out.println("删除了 "+affectedRowNum+" 行");
        JdbcHelper.close(pstmt,connection);
        return affectedRowNum > 0;
    }
    public boolean update(Degree degree) throws SQLException{
        Connection connection = JdbcHelper.getConn();
        //创建sql语句，“？”作为占位符
        String updateDepartment_sql = "UPDATE DEGREE SET DESCRIPTION = ?, NO = ? ,REMARKS = ? WHERE ID = ?";
        //创建PreparedStatement接口对象，包装编译后的目标代码（可以设置参数，安全性高）
        PreparedStatement pstmt = connection.prepareStatement(updateDepartment_sql);
        //为预编译的语句参数赋值
        pstmt.setString(1,degree.getDescription());
        pstmt.setString(2,degree.getNo());
        pstmt.setString(3,degree.getRemarks());
        pstmt.setInt(4,degree.getId());
        //执行预编译对象的executeUpdate()方法，获取修改记录的行数
        int affectedRowNum = pstmt.executeUpdate();
        System.out.println("修改了 "+affectedRowNum+" 行");
        JdbcHelper.close(pstmt,connection);
        return affectedRowNum > 0;
    }
    public static void main(String[] args) throws SQLException {
        DegreeDao degreeDao = new DegreeDao();
        Degree degree = degreeDao.find(3);
        System.out.println(degree);
        //执行Dao对象的方法
        degree.setDescription("博士");
        degreeDao.update(degree);
        Degree degree1 = degreeDao.find(3);
        //打印修改后返回的对象
        System.out.println(degree1);
        System.out.println("修改Degree对象成功！");
    }
}

