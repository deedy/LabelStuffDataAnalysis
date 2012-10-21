
public class MaterialCount implements Comparable<MaterialCount> {
	private int count;
	private String matname;
	public MaterialCount(String matname) {
		this.matname=matname;
		this.count=1;
	}
	public String getMaterial () {
		return matname;
	}
	public int getCount () {
		return count;
	}
	public void setMaterial (String matname) {
		this.matname=matname;
	}
	public void incrementCount() {
		count++;
	}
	public boolean equals (MaterialCount o) {
		return (o.getMaterial().equals(this.getMaterial()));
	}
	
	
	public int compareTo(MaterialCount o) {
		return o.count-this.count;
	}
}
