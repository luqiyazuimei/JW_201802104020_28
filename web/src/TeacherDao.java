import java.sql.*;
import java.util.Collection;
import java.util.TreeSet;

public final class TeacherDao {
	private static TeacherDao teacherDao=new TeacherDao();
	private TeacherDao(){}
	public static TeacherDao getInstance(){
		return teacherDao;
	}
	public Collection<Teacher> findAll() throws SQLException {
		Collection<Teacher> teachers = new TreeSet<Teacher>();
		//������Ӷ���
		Connection connection = JdbcHelper.getConn();
		Statement statement = connection.createStatement();
		//ִ��SQL��ѯ��䲢��ý���������α�ָ�������Ŀ�ͷ��
		ResultSet resultSet = statement.executeQuery("SELECT * FROM TEACHER");
		//���������Ȼ����һ����¼����ִ��ѭ����
		while(resultSet.next()) {
			ProfTitle profTitle = ProfTitleService.getInstance().find(resultSet.getInt("profTitle_id"));
			Degree degree = DegreeService.getInstance().find(resultSet.getInt("degree_id"));
			Department department = DepartmentService.getInstance().find(resultSet.getInt("department_id"));
			//����Degree���󣬸��ݱ�������е�id,description,no,remarksֵ
			Teacher teacher = new Teacher(resultSet.getInt("id"), resultSet.getString("name"),
					profTitle, degree, department);
			//��degrees���������Degree����
			teachers.add(teacher);
		}
		//�ر���Դ
		JdbcHelper.close(resultSet,statement,connection);
		return teachers;
	}
    public Teacher find(Integer id) throws SQLException{
	    Teacher teacher = null;
        Connection connection = JdbcHelper.getConn();
        //����sql��䣬��������Ϊռλ��
        String str = "SELECT * FROM TEACHER WHERE ID = ?";
        //����PreparedStatement�ӿڶ��󣬰�װ������Ŀ����루�������ò�������ȫ�Ըߣ�
        PreparedStatement pstmt = connection.prepareStatement(str);
        pstmt.setInt(1,id);
        ResultSet resultSet = pstmt.executeQuery();
        if (resultSet.next()){
            ProfTitle profTitle = ProfTitleService.getInstance().find(resultSet.getInt("profTitle_id"));
            Degree degree = DegreeService.getInstance().find(resultSet.getInt("degree_id"));
            Department department = DepartmentService.getInstance().find(resultSet.getInt("department_id"));
            //����Degree���󣬸��ݱ�������е�id,description,no,remarksֵ
            teacher = new Teacher(resultSet.getInt("id"),resultSet.getString("name"),
                    profTitle,degree,department);
        }
        //�ر���Դ
        JdbcHelper.close(resultSet,pstmt,connection);
        return teacher;
    }
	public boolean update(Teacher teacher) throws SQLException {
		Connection connection = JdbcHelper.getConn();
		//����sql��䣬��������Ϊռλ��
		String updateTeacher_sql = "UPDATE TEACHER SET NAME = ?, PROFTITLE_ID = ?, DEGREE_ID = ?, DEPARTMENT_ID = ? WHERE ID = ?";
		//����PreparedStatement�ӿڶ��󣬰�װ������Ŀ����루�������ò�������ȫ�Ըߣ�
		PreparedStatement pstmt = connection.prepareStatement(updateTeacher_sql);
		//ΪԤ�������������ֵ
        pstmt.setString(1,teacher.getName());
		pstmt.setInt(2,teacher.getTitle().getId());
		pstmt.setInt(3,teacher.getDegree().getId());
		pstmt.setInt(4,teacher.getDepartment().getId());
		pstmt.setInt(5,teacher.getId());
		//ִ��Ԥ��������executeUpdate()��������ȡɾ����¼������
		int affectedRowNum = pstmt.executeUpdate();
		System.out.println("�޸��� "+affectedRowNum+" ��");
		JdbcHelper.close(pstmt,connection);
		return affectedRowNum > 0;
	}

	public boolean add (Teacher teacher) throws SQLException{
		//������������
		Connection connection = JdbcHelper.getConn();
		PreparedStatement preparedStatement = connection.prepareStatement(
				"INSERT INTO teacher (id,name,profTitle_id,degree_id,department_id) VALUES" +
						"(?,?,?,?,?)");
		preparedStatement.setInt(1,teacher.getId());
		preparedStatement.setString(2,teacher.getName());
		preparedStatement.setInt(3,teacher.getTitle().getId());
		preparedStatement.setInt(4,teacher.getDegree().getId());
		preparedStatement.setInt(5,teacher.getDepartment().getId());
		int affectRowNum = preparedStatement.executeUpdate();
		System.out.println("������ " + affectRowNum + " �С�");
		JdbcHelper.close(preparedStatement,connection);
		return affectRowNum > 0;
	}
	public boolean delete(Integer id) throws SQLException{
		Connection connection = JdbcHelper.getConn();
		//����sql��䣬��������Ϊռλ��
		String deleteTeacher_sql = "DELETE FROM TEACHER WHERE ID = ?";
		//����PreparedStatement�ӿڶ��󣬰�װ������Ŀ����루�������ò�������ȫ�Ըߣ�
		PreparedStatement pstmt = connection.prepareStatement(deleteTeacher_sql);
		//ΪԤ�������������ֵ
		pstmt.setInt(1,id);
		//ִ��Ԥ��������executeUpdate()��������ȡɾ����¼������
		int affectedRowNum = pstmt.executeUpdate();
		System.out.println("ɾ���� "+affectedRowNum+" ��");
		JdbcHelper.close(pstmt,connection);
		return affectedRowNum > 0;
	}
	public static void main(String[] args) throws SQLException {
		//���school�����Ա��departmenttoadd��school�����Ը�ֵ
        ProfTitle profTitle = ProfTitleService.getInstance().find(2);
		Degree degree = DegreeService.getInstance().find(4);
		//����departmentToAdd����
		Department department = DepartmentService.getInstance().find(3);
		//����Dao����
		TeacherDao teacherDao = new TeacherDao();
		Teacher teacher = teacherDao.find(8);
        System.out.println(teacher);
		teacher.setName("��ͬ");
		teacherDao.update(teacher);
		//ִ��Dao����ķ���
		Teacher teacher1 = teacherDao.find(8);
		//��ӡ��Ӻ󷵻صĶ���
		System.out.println(teacher1);
		System.out.println("�޸�Teacher����ɹ���");
	}
}
