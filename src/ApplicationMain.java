import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gdata.client.GoogleService;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.Entry;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;




public class ApplicationMain {
	public static final String GOOGLE_ACCOUNT_USERNAME = "cegcodingcamp"; // Fill in google account username
	public static final String GOOGLE_ACCOUNT_PASSWORD = "cegcampcoding"; // Fill in google account password
	public static final String GOOGLE_BLOG_ID = "6075750740688262534";
	public static final String PARTICIPANTS_SPREADSHEET_URL = "https://spreadsheets.google.com/feeds/spreadsheets/1OLjh-fiRktXuLekOCaDwEchidVf0ad_qzFwPRNeFkU8"; //Fill in google spreadsheet URI

	public static final String RANKLIST_BLOGPOST_ID = "8069712589131715770";
	public static final String RANKLIST_BLOG_TITLE = "Rank List till Day 4";
	public static final String[] submissionSpreadSheetURLs={
		"https://spreadsheets.google.com/feeds/spreadsheets/1JaD6NyHC6VPQomivQ79mctqodcBITlMRcXnCSwqt6yM",
		"https://spreadsheets.google.com/feeds/spreadsheets/1ISbq9IYLXOqpwil-6wv1e4XG2lwqzFmQdCuPzKJvP9M",
		"https://spreadsheets.google.com/feeds/spreadsheets/13Clr_oJWPztIemEDhf7mvoQGxeMR4qW822ZzLzzmqfs",
		"https://spreadsheets.google.com/feeds/spreadsheets/182NO6cCidQMoKMZjfh2oobysTTFjWJ99HxFPHHq3JQE",
		"https://spreadsheets.google.com/feeds/spreadsheets/1St6em7cAb4Rx88CGdGIj-prQOKbZb_edXpTbJf4W6mY",
		"https://spreadsheets.google.com/feeds/spreadsheets/1H2dCpB6cqNpHfoQ7H6VByr6YqFSWGOzbAqU366t6leM",
		"https://spreadsheets.google.com/feeds/spreadsheets/1HDooFzJM65Qj-3YnvMyDXhj-uZXsjc_99JCxo72zMMs",		
	};
	
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
			answer += "<td>"; answer += rankList.get(i).name; answer +="</td>";
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
	
	public static void main(String args[]) throws IOException, ServiceException{
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
		googleBloggerAPI.createPost(getContent(rankList));
		System.out.println("Generated!!");
	}
	
}

class GoogleBloggerAPI {
	
	GoogleService service;
	
	GoogleBloggerAPI() throws AuthenticationException{
		service = new GoogleService("blogger", "exampleCo-exampleApp-1");
	    service.setUserCredentials(ApplicationMain.GOOGLE_ACCOUNT_USERNAME, ApplicationMain.GOOGLE_ACCOUNT_PASSWORD);	
	}
	
	public Entry createPost(String content) throws ServiceException, IOException {
		Entry myEntry = new Entry();
		myEntry.setTitle(new PlainTextConstruct(ApplicationMain.RANKLIST_BLOG_TITLE));
		myEntry.setContent(new PlainTextConstruct(content));
		URL postUrl = new URL("http://www.blogger.com/feeds/" + ApplicationMain.GOOGLE_BLOG_ID + "/posts/default/" + ApplicationMain.RANKLIST_BLOGPOST_ID);
		return service.update(postUrl, myEntry);
	}
}

class GoogleSpreadSheetAPI {
	
	SpreadsheetService service;
	GoogleSpreadSheetAPI() throws AuthenticationException{
		service = new SpreadsheetService("Google Spreadsheet Demo");
	    service.setUserCredentials(ApplicationMain.GOOGLE_ACCOUNT_USERNAME, ApplicationMain.GOOGLE_ACCOUNT_PASSWORD);
	}
	
	List<Participant> getParticipants() throws IOException, ServiceException{
		List<Participant> participants = new ArrayList<Participant>();
	    URL metafeedUrl = new URL(ApplicationMain.PARTICIPANTS_SPREADSHEET_URL);
	    SpreadsheetEntry spreadsheet = service.getEntry(metafeedUrl, SpreadsheetEntry.class);
	    URL listFeedUrl = ((WorksheetEntry) spreadsheet.getWorksheets().get(0)).getListFeedUrl();

	    ListFeed feed = (ListFeed) service.getFeed(listFeedUrl, ListFeed.class);
	    for(ListEntry entry : feed.getEntries())
	    {
	    	Participant participant = new Participant();
	    	try{ participant.name = entry.getCustomElements().getValue("Name").trim();} catch(Exception e){}	    	
	    	try{ participant.college = entry.getCustomElements().getValue("College").trim();} catch(Exception e){}
	    	try{ participant.branch = entry.getCustomElements().getValue("Branch").trim();} catch(Exception e){}
	    	try{ participant.year = entry.getCustomElements().getValue("Year").trim();} catch(Exception e){}
	    	try{ participant.email = entry.getCustomElements().getValue("Email").trim();} catch(Exception e){}
	    	try{ participant.spojProfileUrl = entry.getCustomElements().getValue("SpojProfileUrl").trim();} catch(Exception e){}
	    	try{ participant.topcoderProfileUrl = entry.getCustomElements().getValue("TopcoderProfileUrl").trim();} catch(Exception e){}
	    	try{ participant.participationMode = entry.getCustomElements().getValue("ParticipationMode").trim();} catch(Exception e){}
	    	participants.add(participant);
	    }
		return participants;
	}
	
	List<Submission> getSubmissions(String contestSpreadSheetURL) throws IOException, ServiceException{
		List<Submission> submissions = new ArrayList<Submission>();
	    URL metafeedUrl = new URL(contestSpreadSheetURL);
	    SpreadsheetEntry spreadsheet = service.getEntry(metafeedUrl, SpreadsheetEntry.class);
	    URL listFeedUrl = ((WorksheetEntry) spreadsheet.getWorksheets().get(0)).getListFeedUrl();

	    ListFeed feed = (ListFeed) service.getFeed(listFeedUrl, ListFeed.class);
	    for(ListEntry entry : feed.getEntries())
	    {
	    	Submission submission = new Submission();
	    	try{ submission.email = entry.getCustomElements().getValue("Email").trim();} catch(Exception e){}
	    	try{ submission.problemsSolved = entry.getCustomElements().getValue("PassedSystemTest").trim();} catch(Exception e){}
	    	submission.findSolvedIds();
	    	submissions.add(submission);
	    }
		return submissions;
	}	
}

