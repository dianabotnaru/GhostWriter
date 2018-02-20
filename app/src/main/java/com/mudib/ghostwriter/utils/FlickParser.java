package com.mudib.ghostwriter.utils;

import android.util.Xml;

import com.mudib.ghostwriter.models.SearchResultFlickr;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by diana on 20/02/2018.
 */

public class FlickParser {

    public static List<SearchResultFlickr> parse(InputStream in) throws XmlPullParserException, IOException {
        try {

            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }


    private static List<SearchResultFlickr> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        int eventType = parser.getEventType();
        List<SearchResultFlickr> entries = new ArrayList<SearchResultFlickr>();
        String text = "";
        String date;
        String owner;

        String photoId;
        String secret;
        String farmId;
        String serverId;

        SearchResultFlickr entry = new SearchResultFlickr();;

        while (eventType != XmlPullParser.END_DOCUMENT) {
            String tagname = parser.getName();
            switch (eventType) {
                case XmlPullParser.START_TAG:
                    if (tagname.equalsIgnoreCase("photo")) {
                        // create a new instance of employee

                        date = parser.getAttributeValue(null, "datetaken");
                        owner = parser.getAttributeValue(null, "ownername");

                        photoId = parser.getAttributeValue(null, "id");
                        secret = parser.getAttributeValue(null, "secret");
                        farmId =  parser.getAttributeValue(null, "farm");
                        serverId =  parser.getAttributeValue(null, "server");

                        entry = new SearchResultFlickr();
                        entry.setDate(date);
                        entry.setOwner(owner);
                        entry.setUrl("http://farm"+farmId+".staticflickr.com/"+serverId+"/"+ photoId +"_"+secret+"_m.jpg");


                    }
                    break;

                case XmlPullParser.TEXT:
                    text = parser.getText();
                    break;

                case XmlPullParser.END_TAG:

                    if (tagname.equalsIgnoreCase("photo")) {
                        // add employee object to list
                        entries.add(entry);
                    } else if (tagname.equalsIgnoreCase("description")) {
                        entry.setDescription(text);
                    }
                    break;

                default:
                    break;
            }
            eventType = parser.next();
        }
        return entries;
    }

}
