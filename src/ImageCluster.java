import java.util.*;
public class ImageCluster {
	public static ArrayList<ImageCluster> clusters=new ArrayList<ImageCluster>();
	private String basename;
	public ArrayList<ImageObject> images=new ArrayList<ImageObject>();
	public ArrayList<LabelStuffObject> labelobjects=new ArrayList<LabelStuffObject>();
	
	private ImageCluster (ImageObject io) {
		this.basename=io.getBaseFileName();
		this.images.add(io);
		this.labelobjects=io.labelobjects;
		ImageCluster.clusters.add(this);
	}
	public static ImageCluster addImageObject(ImageObject io) {
		int pos=ImageCluster.containsBasename(io.getBaseFileName());
		if (pos!=-1) {
			ImageCluster ic=ImageCluster.clusters.get(pos);
			ic.images.add(io);
			ic.integrateLabelObjects(io);
			return ic;
		}
		ImageCluster ic = new ImageCluster (io);
		return ic;
			
	}
	private void integrateLabelObjects(ImageObject io) {
		for (int i=0;i<io.labelobjects.size();i++) {
			int pos=this.containsLabelObject(io.labelobjects.get(i));
			if (pos!=-1) {
				this.labelobjects.get(pos).addAttLabel(io.labelobjects.get(i).getAttLabels());
				this.labelobjects.get(pos).addMatLabel(io.labelobjects.get(i).getMatLabels());
			}
			else {
				this.labelobjects.add(io.labelobjects.get(i));
			}
				
		}
	}
	private int containsLabelObject(LabelStuffObject lso) {
		for (int i=0;i<labelobjects.size();i++) {
			if (labelobjects.get(i).equals(lso))
				return i;
		}
		return -1;
	}
	public static int containsBasename(String bn) {
		for (int i=0;i<clusters.size();i++) {
			if (clusters.get(i).getBasename().equals(bn))
				return i;
		}
		return -1;
	}
	public String getBasename() {
		return this.basename;
	}
	public double getAverageLabelsPerMaterial() {
		int count=0;
    	int tally=0;
		for (int j=0;j<this.labelobjects.size();j++) {
			int k=(this.labelobjects.get(j).getMatSize());
			tally++;
			count+=k;
		}
		return((double)(count*100/tally)/100);
	}
	public static double getGlobalAverageLabelsPerMaterial() {
		int count=0;
    	int tally=0;
    	for (int i=0;i<ImageCluster.clusters.size();i++) {
			for (int j=0;j<ImageCluster.clusters.get(i).labelobjects.size();j++) {
				int k=(ImageCluster.clusters.get(i).labelobjects.get(j).getMatSize());
				tally++;
				count+=k;
			}
		}
    	return((double)(count*100/tally)/100);
	}
	public static ArrayList<LabelStuffObject> getArrayListofObjectsClassifiedByName () {
    	ArrayList<LabelStuffObject> sortedobjects=new ArrayList<LabelStuffObject>();
    	for (int i=0;i<ImageCluster.clusters.size();i++) {
			for (int j=0;j<ImageCluster.clusters.get(i).labelobjects.size();j++) {
				LabelStuffObject lso =ImageCluster.clusters.get(i).labelobjects.get(j);
				int pos=findObject(sortedobjects,lso.getObjName());
				if (pos!=-1) {
					sortedobjects.get(pos).addMatLabel(lso.getMatLabels());
					sortedobjects.get(pos).addAttLabel(lso.getAttLabels());
					sortedobjects.get(pos).incrementCount();
				}
				else {
					sortedobjects.add(lso);
				}
			}
		}
    	for (int i=0;i<sortedobjects.size();i++) {
    		sortedobjects.get(i).sortMatByName();
    		sortedobjects.get(i).sortAttByName();
    	}
    	return sortLabelObjects(sortedobjects);
    }
	private static int findObject (ArrayList<LabelStuffObject> sorted, String s) {
    	for (int i=0;i<sorted.size();i++) {
    		if (sorted.get(i).getObjName().equals(s))
    			return i;
    	}
    	return -1;
    }
	private static ArrayList<LabelStuffObject> sortLabelObjects(ArrayList<LabelStuffObject> a) {
        ArrayList<LabelStuffObject> b=new ArrayList<LabelStuffObject>();
        for (int i=0;i<a.size();i++) 
        	b.add(null);
        sortLabelObjectsByMatSize(a,b,0,a.size()-1);
        return b;
    }

    private static void sortLabelObjectsByMatSize(ArrayList<LabelStuffObject> a, ArrayList<LabelStuffObject> b, int low, int high) {
        if(low < high){
            int middle = (low+high) / 2;
            sortLabelObjectsByMatSize(a,b,low,middle);
            sortLabelObjectsByMatSize(a,b,middle+1,high);
            int s_high = middle+1;
            int s_low = low;
            for(int i = low; i <= high; i++){
                if((s_low <= middle) && ((s_high > high) || (a.get(s_low).getMatSize()>a.get(s_high).getMatSize())))
                    b.set(i,a.get(s_low++));
                else
                    b.set(i,a.get(s_high++));
            }
            for(int i = low; i <= high; i++)
                a.set(i,b.get(i));
        }
    }
}
