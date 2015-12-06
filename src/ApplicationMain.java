import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gdata.util.ServiceException;


public class ApplicationMain {
	public static final String GOOGLE_ACCOUNT_USERNAME = "cegcodingcamp"; // Fill in google account username
	public static final String GOOGLE_ACCOUNT_PASSWORD = "cegcampcoding"; // Fill in google account password
	public static final String GOOGLE_BLOG_ID = "6075750740688262534";
	public static final String PARTICIPANTS_SPREADSHEET_URL = "https://spreadsheets.google.com/feeds/spreadsheets/1x_Z5tlu_FyqU-9oGY-gSaMkRRHCj4kw2zckyylVOKyM"; //Fill in google spreadsheet URI

	public static final String RANKLIST_BLOGPOST_ID = "5905635037977048771";
	public static final String RANKLIST_BLOG_TITLE = "Ranklist till day 1 test";
	public static final String[] submissionSpreadSheetURLs={
		"https://spreadsheets.google.com/feeds/spreadsheets/1JaD6NyHC6VPQomivQ79mctqodcBITlMRcXnCSwqt6yM",
		"https://spreadsheets.google.com/feeds/spreadsheets/1ISbq9IYLXOqpwil-6wv1e4XG2lwqzFmQdCuPzKJvP9M",
		"https://spreadsheets.google.com/feeds/spreadsheets/13Clr_oJWPztIemEDhf7mvoQGxeMR4qW822ZzLzzmqfs",
		"https://spreadsheets.google.com/feeds/spreadsheets/182NO6cCidQMoKMZjfh2oobysTTFjWJ99HxFPHHq3JQE",
		"https://spreadsheets.google.com/feeds/spreadsheets/1St6em7cAb4Rx88CGdGIj-prQOKbZb_edXpTbJf4W6mY",
		"https://spreadsheets.google.com/feeds/spreadsheets/1H2dCpB6cqNpHfoQ7H6VByr6YqFSWGOzbAqU366t6leM",
		"https://spreadsheets.google.com/feeds/spreadsheets/1HDooFzJM65Qj-3YnvMyDXhj-uZXsjc_99JCxo72zMMs",		
	};
	
	static String getColor(int rank, int totalCount){
		if((double)rank / totalCount * 100.0 < 10.0 )
			return "#FF0000";
		else if((double)rank / totalCount * 100.0 < 30.0 )
			return "#AEB404";
		else if((double)rank / totalCount * 100.0 < 50.0 )
			return "#0033CC";
		else if((double)rank / totalCount * 100.0 < 70.0 )
			return "#31B404";
		return "#BEBEBE";
	}
	static String getContent(List<Record> rankList){
		String answer = "";
		answer += "<table style=\"width:100%; border-spacing: 8px;\">";
		answer += "<colgroup>";
		answer += "<col span=\"1\" style =\"width: 10%;\">";
		answer += "<col span=\"1\" style =\"width: 30%;\">";
		answer += "<col span=\"1\" style =\"width: 10%;\">";
		answer += "<col span=\"1\" style =\"width: 10%;\">";
		answer += "<col span=\"1\" style =\"width: 20%;\">";
		answer += "<col span=\"1\" style =\"width: 20%;\">";
		answer += "</colgroup>";
		answer += "<thead>";
		answer+= "<tr>";
		answer += "<th style=\"text-align:center\";>";  answer += "Rank"; answer += "</th>";
		answer += "<th style=\"text-align:center\";>";  answer += "Name"; answer += "</th>";
		answer += "<th style=\"text-align:center\";>";  answer += "College"; answer += "</th>";
		answer += "<th style=\"text-align:center\";>";  answer += "Year"; answer += "</th>";
		answer += "<th style=\"text-align:center\";>";  answer += "Participation Mode"; answer += "</th>";
		answer += "<th style=\"text-align:center\";>";  answer += "Overall Points"; answer += "</th>";
		answer += "<tr/>";		
		answer += "</thead>";
		answer += "<tbody>";
		for(int i=0; i<rankList.size(); i++){
			answer += "<tr>";
			answer += "<td style=\"text-align:center\";>"; answer += String.valueOf(i+1); answer +="</td>";
			answer += "<td style=\"font-weight:bold; color:"; answer +=getColor(i+1,rankList.size()); answer+="\";>"; answer += rankList.get(i).name; answer +="</td>";
			answer += "<td style=\"text-align:center\";>"; answer += rankList.get(i).college; answer +="</td>";
			answer += "<td style=\"text-align:center\";>"; answer += rankList.get(i).year; answer +="</td>";
			answer += "<td style=\"text-align:center\";>"; answer += rankList.get(i).participationMode; answer +="</td>";
			answer += "<td style=\"text-align:center\";>"; answer += rankList.get(i).overallPoints*3; answer +="</td>";
			answer += "</tr>";
		}
		answer += "</tbody>";
		answer += "</table>";
		return answer;
	}
	
	public static void main(String args[]) throws IOException, ServiceException, GeneralSecurityException{
		GoogleSpreadSheetAPI googleSpreadSheetAPI = new GoogleSpreadSheetAPI();
		GoogleBloggerAPI googleBloggerAPI = new GoogleBloggerAPI();
		Map<String, Integer> dictionary = new HashMap<String, Integer>();
		List<Record> rankList = new ArrayList<Record>();
		for(int i=0;i<submissionSpreadSheetURLs.length; i++){
			System.out.println("Fetching spreadsheet "+i);
			List<Submission> submissions = googleSpreadSheetAPI.getSubmissions(submissionSpreadSheetURLs[i]);
			for(int j=0;j<submissions.size();j++){
				if(dictionary.get(submissions.get(j).email) != null)
				{
					dictionary.put(submissions.get(j).email, dictionary.get(submissions.get(j).email)+submissions.get(j).getSolvedCount());
				}
				else
					dictionary.put(submissions.get(j).email, submissions.get(j).getSolvedCount());
			}
		}
		
		List<Participant> participants = googleSpreadSheetAPI.getParticipants();
		for(int i=0;i<participants.size(); i++){
			if(dictionary.get(participants.get(i).email) != null){
				Record record = new Record();
				record.college = participants.get(i).college;
				record.name = participants.get(i).name;
				record.year = participants.get(i).year;
				record.participationMode = participants.get(i).participationMode;
				record.overallPoints = dictionary.get(participants.get(i).email);
				rankList.add(record);
			}
		}
		
		Collections.sort(rankList);
		googleBloggerAPI.udpatePost(getContent(rankList));
		System.out.println("Generated!!");
	}
	
}