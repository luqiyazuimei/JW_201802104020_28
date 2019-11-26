import java.io.Serializable;

public final class Teacher implements Comparable<Teacher>,Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String name;
	private ProfTitle profTitle;
	private Degree degree;
	private Department department;

	public Teacher(Integer id,
                   String name,
                   ProfTitle title,
                   Degree degree,
                   Department department) {
		this(name, title, degree, department);
		this.id = id;

	}
	public Teacher(
				   String name,
				   ProfTitle title,
				   Degree degree,
				   Department department) {
		super();
		this.name = name;
		this.profTitle = title;
		this.degree = degree;
		this.department = department;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ProfTitle getTitle() {
		return this.profTitle;
	}

	public void setTitle(ProfTitle title) {
		this.profTitle = title;
	}

	public Degree getDegree() {
		return degree;
	}

	public void setDegree(Degree degree) {
		this.degree = degree;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

//	public Set<GraduateProject> getProjects() {
//		return projects;
//	}
//
//	public void setProjects(Set<GraduateProject> projects) {
//		this.projects = projects;
//	}


	@Override
	public int compareTo(Teacher o) {
		// TODO Auto-generated method stub
		return this.id-o.getId();
	}


	/**
	 * Constructs a <code>String</code> with all attributes
	 * in name = value format.
	 *
	 * @return a <code>String</code> representation 
	 * of this object.
	 */
	public String toString()
	{
	    final String TAB = "    ";
	    
	    String retValue = "";
	    
	    retValue = "Teacher ( "
	        + super.toString() + TAB
	        + "id = " + this.id + TAB
	        + "name = " + this.name + TAB + "\n"
	        + "title = " + this.profTitle + TAB + "\n"
	        + "degree = " + this.degree + TAB + "\n"
	        + "department = " + this.department + TAB
//	        + "projects = " + this.projects + TAB
	        + " )";
	
	    return retValue;
	}

}
