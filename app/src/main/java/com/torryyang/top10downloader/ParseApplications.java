package com.torryyang.top10downloader;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

/**
 * Created by Torry on 02/06/2016.
 */
public class ParseApplications {
    private String xmlData;
    private ArrayList<Application> applications;

    public ParseApplications(String xmlData) {
        this.xmlData = xmlData;
        applications = new ArrayList<Application>();
    }

    public ArrayList<Application> getApplications() {
        return applications;
    }

    public boolean process() {
        boolean status = true;
        Application curRec = null;
        boolean inEntry = false;
        String textVal = "";

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(this.xmlData));
            int eventType = xpp.getEventType();
            while(eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = xpp.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
//                        Log.d("ParseApplications", "Starting tag for " + tagName);
                        if(tagName.equalsIgnoreCase("entry")){
                            inEntry = true;
                            curRec = new Application();
                        }
                        break;
                    case XmlPullParser.TEXT:
                        textVal = xpp.getText();
                        break;

                    case XmlPullParser.END_TAG:
//                        Log.d("ParseApplications", "Ending tag for " + tagName);
                        if(inEntry) {
                            if(tagName.equalsIgnoreCase("entry")) {
                                applications.add(curRec);
                                inEntry = false;
                            } else if(tagName.equalsIgnoreCase("name")) {
                                curRec.setName(textVal);
                            } else if(tagName.equalsIgnoreCase("artist")) {
                                curRec.setArtist(textVal);
                            } else if(tagName.equalsIgnoreCase("releaseDate")) {
                                curRec.setReleaseDate(textVal);
                            }
                        }
                        break;
                    default:
                }
                eventType = xpp.next();
            }
        } catch (Exception e) {
            status = false;
            e.printStackTrace();
        }
        for(Application app : applications) {
            Log.d("ParseApplications", "********");
            Log.d("ParseApplications", app.getName());
            Log.d("ParseApplications", app.getArtist());
            Log.d("ParseApplications", app.getReleaseDate());
        }
        return true;
    }
}
