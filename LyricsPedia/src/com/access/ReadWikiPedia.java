package com.access;

import java.io.IOException;
import java.util.List;

public class ReadWikiPedia {

	public static String getWikipediaInfo(String wikiTitle, List<String> listOfHrefLinks ) {

//		String wikiTitle = "anjaan_(2014_film)";
//		String wikiTitle = "Thiruda_Thiruda";
		Wiki wiki = new Wiki();
		String wikiOutput = "";
		try {
			String parseText = wiki.parse(wiki.getSectionText(wikiTitle, 0));
			String htmlImg = extractImgURL(parseText, listOfHrefLinks);
			String subText1 = parseText.substring(parseText.indexOf("<table"),
					parseText.indexOf("<ol"));
			// Remove Title Name
			String subText2 = subText1.substring(subText1.indexOf("<tr"),
					subText1.indexOf("</tr>") + 5);
			String subText3 = subText1.replace(subText2, "");
			// Remove Image
			String subText4 = subText3.substring(subText3.indexOf("<tr"),
					subText3.indexOf("</tr>") + 5);
			String subText5 = subText3.replace(subText4, "");
			// Remove superscript
			subText5 = removeSuperscript(subText5);
			String hrefText = subText5.replaceAll("href=\"/wiki",
					"style=\"text-decoration: none; color: #FF6600\" href=\"http://en.wikipedia.org/wiki");
			wikiOutput = htmlImg+"<br/> <h3> Film Information from Wikipedia  </h3> "+swapString(hrefText);
			
		} catch (Exception e) {

			e.printStackTrace();
		}
		return wikiOutput;
	}

	private static String extractImgURL(String parseText, List<String> listOfHrefLinks ) {
		String imgURL = "";
		if(parseText.contains("jpg\" src=\"")){
			imgURL = parseText.substring(parseText.indexOf("jpg\" src=\""), parseText.lastIndexOf(".jpg\"") + 4);	
		}
		else if(parseText.contains("png\" src=\"")) {
			imgURL = parseText.substring(parseText.indexOf("png\" src=\""), parseText.lastIndexOf(".png\"") + 4);
		}
		String imgStart = "<table><tbody><tr><td valign=\"center\"><img width=\"220\" height=\"200\" src=\"";
		String songListStart = "<td valign=\"top\"><u1>";
		String songListEnd = "</u1></td>";
		String songList = songListStart + getSongHrefList(listOfHrefLinks) + songListEnd;
		String imgEnd = "\"></td>";
		String imgTableEnd = "</tr></tbody></table><br/>";
		String imgDiv = "";
		if(imgURL.contains("jpg\" src=\"")) {
			imgDiv = imgStart + imgURL.replace("jpg\" src=\"", "http:") + imgEnd + songList + imgTableEnd;
		}
		else if(imgURL.contains("png\" src=\"")){
			imgDiv = imgStart + imgURL.replace("png\" src=\"", "http:") + imgEnd + songList + imgTableEnd;
		}
		return  imgDiv;
	}

	private static String getSongHrefList(List<String> listOfHrefLinks) {
		StringBuilder consolidatedLinks = new StringBuilder();
		for(String linkStr : listOfHrefLinks) {
			consolidatedLinks.append(linkStr);
		}
		return consolidatedLinks.toString();
	}

	private static String swapString(String hrefText) {
		String subText1 = hrefText.substring(hrefText.indexOf("<p"),
				hrefText.indexOf("</p>") + 4);
		String subText2 = hrefText.replace(subText1, "");
		return subText1 + subText2;
		
	}

	private static String removeSuperscript(String subText5) {
		String findStr = "<sup";
		int lastIndex = 0;
		int count = 0;

		while (lastIndex != -1) {
			lastIndex = subText5.indexOf(findStr, lastIndex);
			if (lastIndex != -1) {
				count++;
				lastIndex += findStr.length();
			}
		}
		for (int i = 1; i <= count; i++) {
			String subText6 = subText5.substring(subText5.indexOf("<sup"),
					subText5.indexOf("</sup>") + 6);
			String subText7 = subText5.replace(subText6, "");
			subText5 = subText7;
		}
		return subText5;
	}

}
