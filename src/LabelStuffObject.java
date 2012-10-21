import java.util.ArrayList;

import org.w3c.dom.NodeList;
public class LabelStuffObject {
	private int counter=1;
	private String image_name;
	private String name;
	private String username;
	private int deleted;
	private int verified;
	private String date;
	private int id;
	private ArrayList<MaterialLabel> mat_labels=new ArrayList<MaterialLabel>();
	private ArrayList<AttributeLabel> att_labels=new ArrayList<AttributeLabel>();
	private ArrayList<point> polygon=new ArrayList<point>();
	public static ArrayList<LabelStuffObject> objects=new ArrayList<LabelStuffObject>();
	public LabelStuffObject(String image_filename, org.w3c.dom.Node object) {
		this(object);
		this.image_name=image_filename;
	}
	private LabelStuffObject(org.w3c.dom.Node object) {
		NodeList tmp=object.getChildNodes();
		this.name=tmp.item(0).getTextContent().trim().toLowerCase();
		int no_mat_labels=(tmp.item(1).getChildNodes().getLength());
		if (!tmp.item(1).getChildNodes().item(0).getTextContent().equals("undefined")) {
			for (int j=0;j<no_mat_labels;j++) {
				String matusrname=tmp.item(1).getChildNodes().item(j).getChildNodes().item(0).getTextContent().trim();
				String matname=tmp.item(1).getChildNodes().item(j).getChildNodes().item(1).getTextContent().trim().toLowerCase();
				this.addMatLabel(matusrname, matname);
			}
		}
		this.deleted=Integer.parseInt(tmp.item(2).getTextContent());
		this.verified=Integer.parseInt(tmp.item(3).getTextContent());
		this.date=tmp.item(4).getTextContent().trim();
		if (tmp.item(5).getNodeName().equals("id")) {
			this.id=Integer.parseInt(tmp.item(5).getTextContent());
		}
		else if (tmp.item(6).getNodeName().equals("id")) {
			this.id=Integer.parseInt(tmp.item(6).getTextContent());
		}
		if (tmp.item(6).getNodeName().equals("polygon")) {
			this.username=tmp.item(6).getChildNodes().item(0).getTextContent().trim();
			int no_points=tmp.item(6).getChildNodes().getLength();
			for (int j=1;j<no_points;j++) {
				int x=Integer.parseInt(tmp.item(6).getChildNodes().item(j).getChildNodes().item(0).getTextContent());
				int y=Integer.parseInt(tmp.item(6).getChildNodes().item(j).getChildNodes().item(1).getTextContent());
				polygon.add(new point(x,y));
			}
		}
		else if (tmp.item(5).getNodeName().equals("polygon")) {
			this.username=tmp.item(5).getChildNodes().item(0).getTextContent().trim();
			int no_points=tmp.item(5).getChildNodes().getLength();
			for (int j=1;j<no_points;j++) {
				int x=Integer.parseInt(tmp.item(5).getChildNodes().item(j).getChildNodes().item(0).getTextContent());
				int y=Integer.parseInt(tmp.item(5).getChildNodes().item(j).getChildNodes().item(1).getTextContent());
				polygon.add(new point(x,y));
			}
		}
		int no_attlbls=(tmp.item(7).getChildNodes().getLength());
		if (!tmp.item(7).getChildNodes().item(0).getTextContent().equals("none")) {
			for (int j=0;j<no_attlbls;j++) {
				String attusrname=tmp.item(7).getChildNodes().item(j).getChildNodes().item(0).getTextContent().trim();
				String attname="";
				if (tmp.item(7).getChildNodes().item(j).getChildNodes().getLength()>1)
					attname=tmp.item(7).getChildNodes().item(j).getChildNodes().item(1).getTextContent().trim().toLowerCase();
				if (!attname.equals(""))
					this.addAttLabel(attusrname, attname);
			}
		}
		LabelStuffObject.objects.add(this);
	}
	public void setUsername (String username) {
		this.username=username;
	}
	public void addAttLabel (String username, String attribname) {
		att_labels.add(new AttributeLabel(username,attribname));
	}
	public void addMatLabel (String username, String matname) {
		mat_labels.add(new MaterialLabel(username,matname));
	}
	public void addAttLabel (AttributeLabel al) {
		att_labels.add(al);
	}
	public void addMatLabel (MaterialLabel ml) {
		mat_labels.add(ml);
	}
	public void addAttLabel (ArrayList<AttributeLabel> al) {
		att_labels.addAll(al);
	}
	public void addMatLabel (ArrayList<MaterialLabel> ml) {
		mat_labels.addAll(ml);
	}
	public ArrayList<AttributeLabel> getAttLabels () {
		return this.att_labels;
	}
	public ArrayList<MaterialLabel> getMatLabels () {
		return this.mat_labels;
	}
	public AttributeLabel getAttLabel (int index) {
		if (index<0 || index>=att_labels.size())
			return null;
		return att_labels.get(index);
	}
	public int getAttSize () {
		return att_labels.size();
	}
	public MaterialLabel getMatLabel (int index) {
		if (index<0 || index>=mat_labels.size())
			return null;
		return mat_labels.get(index);
	}
	public int getMatSize() {
		return mat_labels.size();
	}
	public String getObjName() {
		return this.name;
	}
	public String getImageName () {
		return this.image_name;
	}
	public int getDeleted() {
		return this.deleted;
	}
	public int getVerified() {
		return this.verified;
	}
	public String getDate() {
		return this.date;
	}
	public int getID() {
		return this.id;
	}
	public String getUsername() {
		return this.username;
	}
	public boolean equals(LabelStuffObject lso) {
		if (lso.polygon.size()!=this.polygon.size()) {
			return false;
		}
		for (int i=0;i<this.polygon.size();i++) {
			if (!this.polygon.get(i).equals(lso.polygon.get(i)))
					return false;
		}
		return true;
	}
	public String toXMLString() {
		String s="<object>";
		s+="<name>"+this.getObjName()+"</name>";
		s+="<material>";
		if (this.mat_labels.size()!=0) {
		for (int i=0;i<this.mat_labels.size();i++) {
			if (!containsNumber(this.mat_labels.get(i).getMatName())) {
				s+="<label>"+"<usrname>"+this.mat_labels.get(i).getUserName()+"</usrname>";
				s+="<matname>"+this.mat_labels.get(i).getMatName()+"</matname>"+"</label>";
			}
			else {
				s+="undefined";
				System.out.println (this.mat_labels.get(i).getMatName()+" label by user "+this.mat_labels.get(i).getUserName()+" in image "+this.image_name+" was deleted.");
			}
		}
		} else {
			s+="undefined";
		}
		s+="</material>";
		s+="<deleted>"+this.getDeleted()+"</deleted>";
		s+="<verified>"+this.getVerified()+"</verified>";
		s+="<date>"+this.getDate()+"</date>";
		s+="<id>"+this.getID()+"</id>";
		s+="<polygon>";
		s+="<username>"+this.getUsername()+"</username>";
		for (int i=0;i<this.polygon.size();i++) {
			s+="<pt>"+"<x>"+this.polygon.get(i).getX()+"</x>";
			s+="<y>"+this.polygon.get(i).getY()+"</y>"+"</pt>";
		}
		s+="</polygon>";
		s+="<attributes>";
		if (this.att_labels.size()!=0) {
		for (int i=0;i<this.att_labels.size();i++) {
			if (!containsNumber(this.att_labels.get(i).getAttribName())) {
				s+="<label>"+"<usrname>"+this.att_labels.get(i).getUserName()+"</usrname>";
				s+="<attribname>"+this.att_labels.get(i).getAttribName()+"</attribname>"+"</label>";
			}
			else {
				s+="none";
				System.out.println (this.att_labels.get(i).getAttribName()+" label by user "+this.att_labels.get(i).getUserName()+" in image "+this.image_name+" was deleted.");
			}
		}
		} else {
			s+="none";
		}
		s+="</attributes>";
		s+="</object>";
		return s;
	}
	private boolean containsNumber (String k) {
		for (int i=0;i<k.length();i++) {
			if (k.charAt(i)>='0' && k.charAt(i)<='9')
				return true;
		}
		return false;
	}
	public String toRawXMLwoMat() {
		String s="<object>";
		s+="<name>"+this.getObjName()+"</name>";
		s+="<material>undefined</material>";
		s+="<deleted>"+this.getDeleted()+"</deleted>";
		s+="<verified>"+this.getVerified()+"</verified>";
		s+="<date>"+this.getDate()+"</date>";
		s+="<id>"+this.getID()+"</id>";
		s+="<polygon>";
		s+="<username>"+this.getUsername()+"</username>";
		for (int i=0;i<this.polygon.size();i++) {
			s+="<pt>"+"<x>"+this.polygon.get(i).getX()+"</x>";
			s+="<y>"+this.polygon.get(i).getY()+"</y>"+"</pt>";
		}
		s+="</polygon>";
		s+="<attributes>none</attributes>";
		s+="</object>";
		return s;
	}
	public int getCount() {
		return counter;
	}
	public void incrementCount() {
		counter++;
	}
	public void sortMatByName() {
	      int i, j;
	      MaterialLabel newValue;
	      for (i = 1; i < this.getMatLabels().size(); i++) {
	            newValue = this.getMatLabel(i);
	            j = i;
	            while (j > 0 && this.getMatLabel(j - 1).compareTo(newValue)<0) {
	                  this.getMatLabels().set(j,this.getMatLabel(j-1));
	                  j--;
	            }
	            this.getMatLabels().set(j,newValue);
	      }
	}
	public ArrayList<MaterialCount> getUnorderedMaterialCounts () {
		ArrayList<MaterialCount> mats=new ArrayList<MaterialCount>();
		if (this.getMatSize()!=0) {
		sortMatByName();
		mats.add(new MaterialCount(this.getMatLabel(0).getMatNameMain()));
		for (int i=1;i< this.getMatSize();i++) {
			if (mats.get(mats.size()-1).getMaterial().equals(this.getMatLabel(i).getMatNameMain()))
				mats.get(mats.size()-1).incrementCount();
			else
				mats.add(new MaterialCount(this.getMatLabel(i).getMatNameMain()));
		}
		}
		return mats;
	}
	public ArrayList<MaterialCount> getOrderedMaterialCounts () {
		int j;
		ArrayList<MaterialCount> mats=getUnorderedMaterialCounts ();
		for (int i = 1; i < mats.size(); i++) {
            MaterialCount newValue = mats.get(i);
            j = i;
            while (j > 0 && mats.get(j - 1).compareTo(newValue)>0) {
                  mats.set(j,mats.get(j-1));
                  j--;
            }
            mats.set(j,newValue);
		}
		return mats;
	}
		
	public ArrayList<AttributeCount> getUnorderedAttributeCounts () {
		ArrayList<AttributeCount> atts=new ArrayList<AttributeCount>();
		if (this.getAttSize()!=0) {
		sortAttByName();
		atts.add(new AttributeCount(this.getAttLabel(0).getAttribName()));
		for (int i=1;i< this.getAttSize();i++) {
			if (atts.get(atts.size()-1).getAttribute().equals(this.getAttLabel(i).getAttribName()))
				atts.get(atts.size()-1).incrementCount();
			else
				atts.add(new AttributeCount(this.getAttLabel(i).getAttribName()));
		}
		}
		return atts;
	}
	public ArrayList<AttributeCount> getOrderedAttributeCounts () {
		int j;
		ArrayList<AttributeCount> atts=getUnorderedAttributeCounts ();
		for (int i = 1; i < atts.size(); i++) {
            AttributeCount newValue = atts.get(i);
            j = i;
            while (j > 0 && atts.get(j - 1).compareTo(newValue)>0) {
                  atts.set(j,atts.get(j-1));
                  j--;
            }
            atts.set(j,newValue);
		}
		return atts;
	}	
	
	public void sortAttByName() {
	      int i, j;
	      AttributeLabel newValue;
	      for (i = 1; i < this.getAttLabels().size(); i++) {
	            newValue = this.getAttLabel(i);
	            j = i;
	            while (j > 0 && this.getAttLabel(j - 1).compareTo(newValue)<0) {
	                  this.getAttLabels().set(j,this.getAttLabel(j-1));
	                  j--;
	            }
	            this.getAttLabels().set(j,newValue);
	      }
	}
	
	

}
