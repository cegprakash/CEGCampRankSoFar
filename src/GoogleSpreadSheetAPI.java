import java.io.IOException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

public class GoogleSpreadSheetAPI {
	
	SpreadsheetService service;
	GoogleSpreadSheetAPI() throws AuthenticationException, IOException, GeneralSecurityException{
		service = GoogleCredentialsHelper.getSpreadSheetService();
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