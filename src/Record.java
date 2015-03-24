public class Record implements Comparable<Record>{
	public String name;
	public String college;
	public String year;
	public String participationMode;
	public int overallPoints;
	
	Record(){
		overallPoints = 0;
	}

	@Override
	public int compareTo(Record other) {
		return other.overallPoints - overallPoints;
	}
}
