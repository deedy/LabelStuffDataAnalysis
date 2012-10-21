
public class AttributeCount implements Comparable<AttributeCount> {
	private int count;
	private String attname;
	public AttributeCount(String attname) {
		this.attname=attname;
		this.count=1;
	}
	public String getAttribute () {
		return attname;
	}
	public int getCount () {
		return count;
	}
	public void setAttribute (String attname) {
		this.attname=attname;
	}
	public void incrementCount() {
		count++;
	}
	public boolean equals (AttributeCount o) {
		return (o.getAttribute().equals(this.getAttribute()));
	}
	
	
	public int compareTo(AttributeCount o) {
		return o.count-this.count;
	}
}
