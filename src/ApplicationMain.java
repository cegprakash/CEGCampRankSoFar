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
	public static final String PARTICIPANTS_SPREADSHEET_URL = "https://spreadsheets.google.com/feeds/spreadsheets/1fHvCasXwAQrA394oEfPABhBpCFjYhoDN25oT-f92pPU"; //Fill in google spreadsheet URI

	public static final String RANKLIST_BLOGPOST_ID = "1367817479425101911";
	public static final String RANKLIST_BLOG_TITLE = "CEG Coding Camp 2017 overall rankings";
	public static final String[] submissionSpreadSheetURLs={
		"https://spreadsheets.google.com/feeds/spreadsheets/1cUxHfu4SCz8rM6Hr4u_4uyRjDwqFfJkNBE0DL3wny4E",
		"https://spreadsheets.google.com/feeds/spreadsheets/1ge9IjGp4Vo89BHb6dwElTuGuJLDLOeHiyKCATjCmMlw",
		"https://spreadsheets.google.com/feeds/spreadsheets/1HPdVtonw38xhW_XnR1bgo8V5EL3WgkSnVxA5icnbsJY",
		"https://spreadsheets.google.com/feeds/spreadsheets/1luBjN0lGxC0mXg-e_D3E8hzrqoNDndzrnmO4zwhMjyA",
		"https://spreadsheets.google.com/feeds/spreadsheets/1ocFjx9ZXVwrI3eeIx5nmd58-QDyA8x-sCiF20gtOKh8",
		"https://spreadsheets.google.com/feeds/spreadsheets/1CVHp_owBlLBfAttWrxX22PxYezg6jStFthiNPrM0S6A",
		"https://spreadsheets.google.com/feeds/spreadsheets/1s4ayafOdTH9K6diprLMDeyr8fVy5mFYUnsa_3-yDgKo",
		"https://spreadsheets.google.com/feeds/spreadsheets/1qsuHxxuvow79s33AkNG2ixMJuFoyBcKr3C7G2k2pOms",
		"https://spreadsheets.google.com/feeds/spreadsheets/1oh0GA61Ct4wOWobVMUBLKRAp-4YQnYnPQ4PkkUBA_qM",
		"https://spreadsheets.google.com/feeds/spreadsheets/1ZTAt_Af1pKPzujLEFQH0hqMUlCAKZBA7epmd8kaunak"		
	};
	
	public static final int[][] points = {
		{3,2,2,3,2,1},
		{2,1,3,1},
		{3,3,2,2,1,1},
		{3,3,3,2,1},
		{2,2,2,2},
		{2,3,1,3,2},
		{3,3,1,1},
		{2,3},
		{2,1,1,1,3},
		{2,2,2,2,2,1,1}
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
			answer += "<td style=\"text-align:center\";>"; answer += rankList.get(i).overallPoints; answer +="</td>";
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
					dictionary.put(submissions.get(j).email, dictionary.get(submissions.get(j).email)+submissions.get(j).getScore(points[i]));
				}
				else
					dictionary.put(submissions.get(j).email, submissions.get(j).getScore(points[i]));
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