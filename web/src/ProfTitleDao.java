import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public final class ProfTitleDao {
	private static ProfTitleDao profTitleDao=new ProfTitleDao();

	public static ProfTitleDao getInstance(){
		return profTitleDao;
	}

	public Collection<ProfTitle> findAll() throws SQLException {
		Set<ProfTitle> profTitles = new HashSet<ProfTitle>();
		//获得连接对象
		Connection connection = JdbcHelper.getConn();
		Statement statement = connection.createStatement();
		//执行SQL查询语句并获得结果集对象（游标指向结果集的开头）
		ResultSet resultSet = statement.executeQuery("SELECT * FROM PROFTITLE");
		//若结果集仍然有下一条记录，则执行循环体
		while (resultSet.next()){
			//创建Degree对象，根据遍历结果中的id,description,no,remarks值
			ProfTitle profTitle = new ProfTitle(resultSet.getInt("id"),resultSet.getString("description"),
					resultSet.getString("no"),resultSet.getString("remarks"));
			//向degrees集合中添加Degree对象
			profTitles.add(profTitle);
		}
		//关闭资源
		JdbcHelper.close(resultSet,statement,connection);
		return profTitles;
	}

	public ProfTitle find(Integer id) throws SQLException{
	    ProfTitle profTitle = null;
        Connection connection = JdbcHelper.getConn();
        //创建sql语句，“？”作为占位符
        String str = "SELECT * FROM PROFTITLE WHERE ID = ?";
        //创建PreparedStatement接口对象，包装编译后的目标代码（可以设置参数，安全性高）
        PreparedStatement pstmt = connection.prepareStatement(str);
        pstmt.setInt(1,id);
        ResultSet resultSet = pstmt.executeQuery();
        if (resultSet.next()){
            //创建Degree对象，根据遍历结果中的id,description,no,remarks值
            profTitle = new ProfTitle(resultSet.getInt("id"),resultSet.getString("description"),
                    resultSet.getString("no"),resultSet.getString("remarks"));
        }
        JdbcHelper.close(resultSet,pstmt,connection);
        return profTitle;
    }

	public boolean add (ProfTitle profTitle) throws SQLException{
		//加载驱动程序
		Connection connection = JdbcHelper.getConn();
		PreparedStatement preparedStatement = connection.prepareStatement(
				"INSERT INTO PROFTITLE (ID,NO,DESCRIPTION,REMARKS) VALUES" +
						"(?,?,?,?)");
		preparedStatement.setInt(1,profTitle.getId());
		preparedStatement.setString(2,profTitle.getNo());
		preparedStatement.setString(3,profTitle.getDescription());
		preparedStatement.setString(4,profTitle.getRemarks());
		int affectRowNum = preparedStatement.executeUpdate();
		System.out.println("增加了 " + affectRowNum + " 行。");
		JdbcHelper.close(preparedStatement,connection);
		return affectRowNum > 0;
	}

	public boolean delete(Integer id) throws SQLException{
	Connection connection = JdbcHelper.getConn();
	//创建sql语句，“？”作为占位符
	String deleteProfTitle_sql = "DELETE FROM PROFTITLE WHERE ID = ?";
	//创建PreparedStatement接口对象，包装编译后的目标代码（可以设置参数，安全性高）
	PreparedStatement pstmt = connection.prepareStatement(deleteProfTitle_sql);
	//为预编译的语句参数赋值
	pstmt.setInt(1,id);
	//执行预编译对象的executeUpdate()方法，获取删除记录的行数
	int affectedRowNum = pstmt.executeUpdate();
	System.out.println("删除了 "+affectedRowNum+" 行");
	JdbcHelper.close(pstmt,connection);
	return affectedRowNum > 0;
}

	public boolean update(ProfTitle profTitle) throws SQLException {
	Connection connection = JdbcHelper.getConn();
	//创建sql语句，“？”作为占位符
	String updateDepartment_sql = "UPDATE PROFTITLE SET DESCRIPTION = ?, NO = ? ,REMARKS = ? WHERE ID = ?";
	//创建PreparedStatement接口对象，包装编译后的目标代码（可以设置参数，安全性高）
	PreparedStatement pstmt = connection.prepareStatement(updateDepartment_sql);
	//为预编译的语句参数赋值
    pstmt.setString(1,profTitle.getDescription());
    pstmt.setString(2,profTitle.getNo());
    pstmt.setString(3,profTitle.getRemarks());
    pstmt.setInt(4,profTitle.getId());
	//执行预编译对象的executeUpdate()方法，获取删除记录的行数
	int affectedRowNum = pstmt.executeUpdate();
	System.out.println("修改了 "+affectedRowNum+" 行");
	JdbcHelper.close(pstmt,connection);
	return affectedRowNum > 0;
}

	public static void main(String[] args) throws SQLException {
		ProfTitleDao profTitleDao = new ProfTitleDao();
		//执行Dao对象的方法
		ProfTitle profTitle = profTitleDao.find(3);
		System.out.println(profTitle);
		profTitle.setDescription("硕士");
		profTitleDao.update(profTitle);
		ProfTitle profTitle1 = profTitleDao.find(3);
//		//打印添加后返回的对象
		System.out.println(profTitle1);
		System.out.println("修改ProfTitle对象成功！");
	}
}

