package com.access;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.gdata.data.Entry;
import com.google.gdata.data.HtmlTextConstruct;
import com.google.gdata.data.TextContent;
import com.google.gdata.util.ServiceException;

public class ContentManager {

	private static ReadBlogger readBlogger = new ReadBlogger();
	private String blogId;
	
	public void onRecieveUpdates() throws Exception {
		try {
			blogId = readBlogger.connectBlogger();
			List<LOPSubmissionData> sheetDataList = processSheetData();
			createSongPost(sheetDataList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private List<LOPSubmissionData> processSheetData() throws IOException,
			ServiceException {
		return ReadSpreadSheet.readSheetData();

	}

	private void createSongPost(List<LOPSubmissionData> sheetDataList)
			throws Exception {

		for (LOPSubmissionData sheetData : sheetDataList) {
			if(!"Y".equalsIgnoreCase(sheetData.getPostStatus())) {
				String[] splitStr = sheetData.getSongLyrics().split("\\s+");
				Entry cereatedPost = new Entry();
				String songName = splitStr[1].length() < 4 ? splitStr[0] + " "+ splitStr[1] + " " + splitStr[2] : splitStr[0] + " "+ splitStr[1];
				String postTitle = songName  +" Song Lyrics From " + sheetData.getFilmName();
				String postContent = createSongPostContent(sheetData);
				boolean isHtml = true;
				String authorName = "LOP";
				boolean isDraft = false;
				String postLabel = sheetData.getFilmName();

					cereatedPost = readBlogger.createPost(postTitle, postContent, isHtml,
							authorName, isDraft, postLabel);	
				sheetData.setPostStatus("Y");
//				createOrUpdateMoviePost(cereatedPost, sheetData, songName, readBlogger);
				ReadSpreadSheet.updateSheetData(sheetData);
			}
		}
	}

	private String createSongPostContent(LOPSubmissionData sheetData) throws Exception {
		StringBuilder content = new StringBuilder();
		content.append(" <div><table><tr><td valign=\"top\"><img height=\"200\" src=\""+ getImageURL(sheetData.getImgURL())+"\"width=\"220\" /></td>"); 
		content.append("<td><u1><li>Singers : ").append(sheetData.getSingers()+"</li>")
		.append("<li> Music : ").append(sheetData.getMusicBy()+"</li>")
		.append("<li> Lyrics : ").append(sheetData.getLyricsBy()+"</li>")
		.append("<li> Film : ").append(sheetData.getFilmName() +"</li></u1></td></tr></table>" + "</div>")
		.append("<embed src=\"https://www.youtube.com/v/")
		.append(getYoutubeId(sheetData.getYoutubeURL()))
		.append("?version=2&amp;color1=0xFFFF66&amp;color2=0xFFFF66&amp;border=0&amp;autoplay=1&amp;loop=0&amp;hd=1\"")
		.append("type=\"application/x-shockwave-flash\" allowscriptaccess=\"always\" width=\"300\" height=\"25\"></embed>")
		.append("<br/><br/><div>");
		for(String splitContent: sheetData.getSongLyrics().split("\\r?\\n")) {
			content.append(splitContent + "<br/>");
		}
		content.append("</div><br/><br/>");
		content.append("<p> >> Lyrics Support By "+ sheetData.getPersonName() + "</p>");
		return content.toString();
	}

	private String getImageURL(String imgURL) throws Exception {
		return ReadPicasa.readPicasaAlbums(imgURL);
	}

	private String getYoutubeId(String youtubeURL) {
		if(StringUtils.isNotEmpty(youtubeURL)) {
			return youtubeURL.substring(youtubeURL.indexOf("watch?v=")+8);
		}
		return "";
	}

	private void createOrUpdateMoviePost(Entry createdPost,
			LOPSubmissionData sheetData, String songName, ReadBlogger readBlogger) throws IOException, ServiceException {
		String songHrefLink = "";
		if(createdPost != null && createdPost.getHtmlLink() != null) {
			System.out.println(createdPost.getHtmlLink().getHref() + "\n"
					+ createdPost.getHtmlLink().getTitle() + "\n" + songName);
			 songHrefLink = "<li><a href=\""
					+ createdPost.getHtmlLink().getHref() + "\" title=\""
					+ createdPost.getHtmlLink().getTitle() + "\">" + songName
					+ "</a></li>";
		}
		Entry moviePostEntry = readBlogger.findExistingMoviePost(blogId, sheetData.getFilmName());
		if(moviePostEntry != null ) {
			TextContent txtContent = (TextContent)moviePostEntry.getContent();
			HtmlTextConstruct htmlContruct = (HtmlTextConstruct)txtContent.getContent();
			String htmlString = htmlContruct.getHtml();
			int strIndx = htmlString.indexOf("<li>");
			int endIndx = htmlString.indexOf("</li>");
			String strToReplace = htmlString.substring(strIndx, endIndx+5);
			String result = htmlString.replace(strToReplace, strToReplace+songHrefLink);
			moviePostEntry.setContent(new HtmlTextConstruct(result));
			URL editUrl = new URL(moviePostEntry.getEditLink().getHref());
			readBlogger.updatePost(editUrl, moviePostEntry);
		}
		else {
//			String moviePostWithWikiInfo  = ReadWikiPedia.getWikipediaInfo(sheetData.getWikiId(), Arrays.asList(songHrefLink));
			String songListStart = "<td valign=\"top\"><u1>";
			String songListEnd = "</u1></td>";
			String songList = songListStart + getSongHrefList(Arrays.asList(songHrefLink)) + songListEnd;
			
			if(StringUtils.isNotEmpty(songList)){
				readBlogger.createPost(sheetData.getFilmName(), songList, true,
						"LOP", false, sheetData.getLyricsLanguage().trim());	
			}
		}
	}
	
	private static String getSongHrefList(List<String> listOfHrefLinks) {
		StringBuilder consolidatedLinks = new StringBuilder();
		for(String linkStr : listOfHrefLinks) {
			consolidatedLinks.append(linkStr);
		}
		return consolidatedLinks.toString();
	}
	
	public static void main(String[] args) {
		ContentManager cmgr = new ContentManager();
		try {
			cmgr.onRecieveUpdates();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
