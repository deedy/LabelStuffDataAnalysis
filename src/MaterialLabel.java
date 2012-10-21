
public class MaterialLabel implements Comparable<MaterialLabel> {
	private String username;
	private String matname;
	public MaterialLabel(String username, String matname) {
		this.username=username;
		this.matname=matname;
	}
	public String getUserName () {
		return username;
	}
	public String getMatName () {
		return matname;
	}
	public String getMatNameMain() {
		String invalidchars=":\\/,.;'!@#$%^&*()-_+={}[]><?|`~ ";
		String m=matname;
		for (int i=0;i<invalidchars.length();i++) {
			int pos=m.indexOf(invalidchars.charAt(i));
			if (pos!=-1)
				m=m.substring(0,pos);
		}
		if (m.length()>=2 && m.charAt(m.length()-1)=='s' && m.charAt(m.length()-2)!='s')
			m=m.substring(0,m.length()-1);
		return m;
	}
	public void setUserName (String username) {
		this.username=username;
	}
	public void setMatName (String matname) {
		this.matname=matname;
	}
	
	public int compareToMain(MaterialLabel o) {
		return o.getMatNameMain().compareTo(this.getMatNameMain());
	}
	public int compareTo(MaterialLabel o) {
		return o.matname.compareTo(this.matname);
	}
}
