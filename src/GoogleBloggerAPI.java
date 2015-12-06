import java.io.IOException;
import java.security.GeneralSecurityException;

import com.google.api.services.blogger.Blogger.Posts.Update;
import com.google.api.services.blogger.model.Post;
import com.google.gdata.util.AuthenticationException;


public class GoogleBloggerAPI {	
	public void udpatePost(String content) throws IOException, AuthenticationException, GeneralSecurityException{
		Post post = new Post();
		post.setTitle(ApplicationMain.RANKLIST_BLOG_TITLE);
		post.setContent(content);
		Update updateAction = GoogleCredentialsHelper.getBlog().posts().update(ApplicationMain.GOOGLE_BLOG_ID, ApplicationMain.RANKLIST_BLOGPOST_ID, post);
		updateAction.setFields("author/displayName,content,published,title,url");
		post = updateAction.execute();
		System.out.println("Published: " + post.getPublished());  
	}
}