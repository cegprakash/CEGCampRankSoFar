import java.util.HashSet;

public class Submission{
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
			for(int i=0;i<solvedIdStrings.length;i++){
				solvedIds.add(Integer.parseInt(solvedIdStrings[i]));		
//				score += Constants.problems[Integer.parseInt(solvedIdStrings[i])-1].score;
			}
		}	
	}
	
	public boolean didSolve(int problemId){
		return solvedIds.contains(problemId);
	}
	
	public int getScore(int points[]){
		int score = 0;
		for(int i=0;i<points.length;i++)
			if(solvedIds.contains(i+1))
				score += points[i];
		
		return score;
	}
}