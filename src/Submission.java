import java.util.HashSet;

public class Submission implements Comparable<Submission>{
	public String email;
	public String problemsSolved;
	
	private HashSet<Integer> solvedIds;
	
	Submission(){
		solvedIds = null;
	}
	
	void findSolvedIds(){
		solvedIds = new HashSet<Integer>();
		if(problemsSolved!=null && !problemsSolved.contains("None") && !problemsSolved.contains("none")){
			problemsSolved = problemsSolved.replaceAll(" ", "");
			String[] solvedIdStrings = problemsSolved.split(",");
			for(int i=0;i<solvedIdStrings.length;i++)
				solvedIds.add(Integer.parseInt(solvedIdStrings[i]));		
		}	
	}
	
	public boolean didSolve(int problemId){
		return solvedIds.contains(problemId);
	}
	
	public int getSolvedCount(){
		return solvedIds.size();
	}

	@Override
	public int compareTo(Submission other) {
		return other.solvedIds.size() - solvedIds.size();
	}
}