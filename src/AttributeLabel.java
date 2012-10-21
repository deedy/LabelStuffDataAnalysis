
public class AttributeLabel {
	private String username;
	private String attribname;
	public AttributeLabel(String username, String attribname) {
		this.username=username;
		this.attribname=attribname;
	}
	public String getUserName () {
		return username;
	}
	public String getAttribName () {
		return attribname;
	}
	public void setUserName (String username) {
		this.username=username;
	}
	public void setAttribName (String attribname) {
		this.attribname=attribname;
	}
	public int compareTo(AttributeLabel o) {
		return o.attribname.compareTo(this.attribname);
	}
}
