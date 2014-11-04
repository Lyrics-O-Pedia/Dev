package com.access;

public class Test {

	
	
	public static void main(String[] args) {
//		String htmlString = "<td valign=\"top\"><u1><li><a href=\"http://lyricsopedia.blogspot.com/2014/08/"
//				+ "raasaathi-en-usuru-song-lyrics-from_82.html\" "
//				+ "title=\"Raasaathi en usuru Song Lyrics From Thiruda Thiruda\">"
//				+ "Raasaathi en usuru</a></li></u1></td>";
//		int strIndx = htmlString.indexOf("<li>");
//		int endIndx = htmlString.indexOf("</li>");
//		String strToReplace = htmlString.substring(strIndx, endIndx+5);
//		System.out.println(strToReplace);
//		String strToAdd = "<li><a href=\"http://lyricsopedia.blogspot.com/2014/08/"
//				+ "raasaathi-en-usuru-song-lyrics-from_82.html\" "
//				+ "title=\"Raasaathi en usuru Song Lyrics From Thiruda Thiruda\">"
//				+ "Raasaathi en usuru</a></li>";
//		String result = htmlString.replace(strToReplace, strToReplace+strToAdd);
//		System.out.println(result);
		
		
		String youtubeURL = "https://www.youtube.com/watch?v=7E0iklvIZew";
		
		System.out.println(youtubeURL.substring(youtubeURL.indexOf("watch?v=")+8));
		
	}
	
	
	
}
