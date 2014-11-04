package com.access;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import com.google.gdata.client.photos.PicasawebService;
import com.google.gdata.data.Link;
import com.google.gdata.data.OtherContent;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.photos.AlbumEntry;
import com.google.gdata.data.photos.PhotoEntry;
import com.google.gdata.util.ContentType;

public class ReadPicasa {

	private static final String PASSWORD = "xyz";
	private static final String USERNAME = "post.lyricsopedia";

	public static String readPicasaAlbums(String imageURL) throws Exception {

		PicasawebService service = new PicasawebService("LOPWebClient");
		PicasawebClient client = new PicasawebClient(service, USERNAME,
				PASSWORD);
		List<AlbumEntry> albumEntries = client.getAlbums();

		for (AlbumEntry aEntry : albumEntries) {
			
			if ("LyricsOPedia".equalsIgnoreCase(aEntry.getName())) {
				PhotoEntry phtEntry  = createPhoto(aEntry, client, service, imageURL);
				String photoLink = phtEntry.getHtmlLink().getHref();

				if (phtEntry.getMediaContents().size() > 0) {
					photoLink = phtEntry.getMediaContents().get(0).getUrl();
				}
				return photoLink;
			}
		}
		return "";
	}

	private static PhotoEntry createPhoto(AlbumEntry albumEntry,
			PicasawebClient client, PicasawebService service, String imgeURL) throws Exception {
		PhotoEntry photo = new PhotoEntry();

		String title = "Title";
		photo.setTitle(new PlainTextConstruct(title));
		String description = "Description";
		photo.setDescription(new PlainTextConstruct(description));
		photo.setTimestamp(new Date());

		OtherContent content = new OtherContent();

		URL imageURL = new URL(imgeURL);
		BufferedImage originalImage = ImageIO.read(imageURL);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(originalImage, "jpg", baos);

		content.setBytes(baos.toByteArray());
		content.setMimeType(new ContentType("image/jpeg"));
		photo.setContent(content);

		String feedUrl = client.getLinkByRel(albumEntry.getLinks(),
				Link.Rel.FEED);
		return service.insert(new URL(feedUrl), photo);

	}

}
