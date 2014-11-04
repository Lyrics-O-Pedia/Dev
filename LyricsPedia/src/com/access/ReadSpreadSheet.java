package com.access;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.util.ServiceException;

public class ReadSpreadSheet {
	// Fill in google account username
	public static final String GOOGLE_ACCOUNT_USERNAME = "post.lyricsopedia@gmail.com";
	// Fill in google account password
	public static final String GOOGLE_ACCOUNT_PASSWORD = "xyz";
	// Fill in google spreadsheet URI
	public static final String SPREADSHEET_URL = "https://spreadsheets.google.com/feeds/spreadsheets/1KmR6qLoXYRI5f32OUwoCl_JSp4adZhDvipuqryR87uM";

	public static List<LOPSubmissionData> readSheetData() throws IOException, ServiceException {
		/** Our view of Google Spreadsheets as an authenticated Google user. */
		SpreadsheetService service = new SpreadsheetService(
				"Print Google Spreadsheet Demo");

		// Login and prompt the user to pick a sheet to use.
		service.setUserCredentials(GOOGLE_ACCOUNT_USERNAME,
				GOOGLE_ACCOUNT_PASSWORD);

		// Load sheet
		URL metafeedUrl = new URL(SPREADSHEET_URL);
		SpreadsheetEntry spreadsheet = service.getEntry(metafeedUrl,
				SpreadsheetEntry.class);
		URL listFeedUrl = ((WorksheetEntry) spreadsheet.getWorksheets().get(1))
				.getListFeedUrl();

		// Print entries
		ListFeed feed = (ListFeed) service.getFeed(listFeedUrl, ListFeed.class);
		List<LOPSubmissionData> lopDataList = new ArrayList<LOPSubmissionData>();
		for (ListEntry entry : feed.getEntries()) {
			LOPSubmissionData lopData = new LOPSubmissionData();
			for (String tag : entry.getCustomElements().getTags()) {
//				System.out.println("     " + tag + ": "
//						+ entry.getCustomElements().getValue(tag));
				if("id".equals(tag)) {
					lopData.setSheetId(entry.getCustomElements().getValue(tag));
				}
				if ("lyricslanguage".equals(tag)) {
					lopData.setLyricsLanguage(entry.getCustomElements().getValue(tag));
				}
				if ("personname".equals(tag)) {
					lopData.setPersonName(entry.getCustomElements().getValue(tag));
				}
				if ("timestamp".equals(tag)) {
					lopData.setTimestamp(entry.getCustomElements().getValue(tag));
				}
				if ("emailid".equals(tag)) {
					lopData.setEmailId(entry.getCustomElements().getValue(tag));
				}
				if ("singers".equals(tag)) {
					lopData.setSingers(entry.getCustomElements().getValue(tag));
				}
				if ("musicby".equals(tag)) {
					lopData.setMusicBy(entry.getCustomElements().getValue(tag));
				}
				if ("lyricsby".equals(tag)) {
					lopData.setLyricsBy(entry.getCustomElements().getValue(tag));
				}
				if ("filmname".equals(tag)) {
					lopData.setFilmName(entry.getCustomElements().getValue(tag));
				}
				if ("youtubeurlofthesong".equals(tag)) {
					lopData.setYoutubeURL(entry.getCustomElements().getValue(tag));
				}
				if ("songlyrics".equals(tag)) {
					lopData.setSongLyrics(entry.getCustomElements().getValue(tag));
				}
				if ("posted".equals(tag)) {
					lopData.setPostStatus(entry.getCustomElements().getValue(tag));
				}
				if("image".equals(tag)) {
					lopData.setImgURL(entry.getCustomElements().getValue(tag));
				}
				if("wikiid".equals(tag)) {
					lopData.setWikiId(entry.getCustomElements().getValue(tag));
				}
			}
			lopDataList.add(lopData);
		}
		return lopDataList;
	}
	
	public static void updateSheetData(LOPSubmissionData sheetData) throws IOException, ServiceException {
		/** Our view of Google Spreadsheets as an authenticated Google user. */
		SpreadsheetService service = new SpreadsheetService(
				"Print Google Spreadsheet Demo");

		// Login and prompt the user to pick a sheet to use.
		service.setUserCredentials(GOOGLE_ACCOUNT_USERNAME,
				GOOGLE_ACCOUNT_PASSWORD);

		// Load sheet
		URL metafeedUrl = new URL(SPREADSHEET_URL);
		SpreadsheetEntry spreadsheet = service.getEntry(metafeedUrl,
				SpreadsheetEntry.class);
		URL listFeedUrl = ((WorksheetEntry) spreadsheet.getWorksheets().get(1))
				.getListFeedUrl();

		// Print entries
		ListFeed feed = (ListFeed) service.getFeed(listFeedUrl, ListFeed.class);
		for (ListEntry entry : feed.getEntries()) {
			for (String tag : entry.getCustomElements().getTags()) {
				if("id".equals(tag) && entry.getCustomElements().getValue(tag).equals(sheetData.getSheetId())) {
					entry.getCustomElements().setValueLocal("posted", sheetData.getPostStatus());
					entry.update();
				}
			}
		}
	}
	
	public static void main(String[] args) throws IOException, ServiceException {
		readSheetData();
	}
}
