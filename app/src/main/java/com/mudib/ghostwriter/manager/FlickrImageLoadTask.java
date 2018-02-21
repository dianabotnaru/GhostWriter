package com.mudib.ghostwriter.manager;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.mudib.ghostwriter.Interface.FlickrImageLoadTaskInterface;
import com.mudib.ghostwriter.models.SearchResultFlickr;
import com.mudib.ghostwriter.utils.FlickParser;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;


/**
 * Created by diana on 20/02/2018.
 */

public class FlickrImageLoadTask extends AsyncTask<String, Void, List<SearchResultFlickr>> {

    String FlickrQuery_url = "https://api.flickr.com/services/rest/?method=flickr.photos.search";
    String FlickrQuery_text = "&text=";
    String FlickrQuery_key = "&api_key=";
    String FlickrQuery_perPage = "&per_page=";
    String FlickrQuery_page = "&page=";

    String FlickrApiKey = "062a6c0c49e4de1d78497d13a7dbb360";

    Activity activity;
    String error = "";
    public FlickrImageLoadTask(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected List<SearchResultFlickr> doInBackground(String... q) {
        try {
            InputStream result = loadXmlFromNetwork(q[0],q[1],q[2]);
            if(result!=null) {
                return FlickParser.parse(result);
            }else{
                return null;
            }
        } catch (XmlPullParserException e) {
            error = e.getLocalizedMessage();
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            error = e.getLocalizedMessage();
            e.printStackTrace();
            return null;
        }
    }


    @Override
    protected void onPostExecute(final List<SearchResultFlickr> entries) {
        if(entries!=null){
            try{
                ((FlickrImageLoadTaskInterface) activity).onGetFlickrImageList(entries);
            }catch (ClassCastException cce){
                throw new ClassCastException("FlickrImageLoadTaskInterface getTargetActivity is not set");
            }
        }else{
            try{
                ((FlickrImageLoadTaskInterface) activity).onFailedGetFlickrImageList(error);
            }catch (ClassCastException cce){
                throw new ClassCastException("FlickrImageLoadTaskInterface getTargetActivity is not set");
            }
            Log.d("onpostexecute", "imagelist is null");
        }
    }

    private InputStream loadXmlFromNetwork(String query,String perpage, String page) throws XmlPullParserException, IOException {
        Log.d("loadxmlfromnetwork", "into loadxmlfromnw");
        InputStream output = null;
        output = downloadUrl(query,perpage,page);
        return output;
    }


    private InputStream downloadUrl(String query,String perpage, String page) throws IOException {
        InputStream in = null;

        Log.d("downloadurl", query);
        String qString = FlickrQuery_url + FlickrQuery_key + FlickrApiKey
                +  FlickrQuery_text + query +FlickrQuery_page+page+FlickrQuery_perPage+perpage+"&extras=date_taken,owner_name,description";
        qString = qString.replaceAll(" ", "%20");
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(qString);

        try {
            HttpResponse httpResponse = httpClient.execute(httpGet);
            in = httpResponse.getEntity().getContent();

        } catch (ClientProtocolException e) {
            error = e.getLocalizedMessage();
            e.printStackTrace();
        } catch (IOException e) {
            error = e.getLocalizedMessage();
            e.printStackTrace();
        }
        return in;
    }

}
