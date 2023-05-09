package com.example.meme_dating;

import static com.example.meme_dating.FileUtils.getFileFromUri;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

public class UploadFileTask extends AsyncTask<Void, Void, String> {

    private Context context;
    private String cat_id;
    private String title;
    private String u_id;
    private String urlString;
    private Uri fileUri;

    public UploadFileTask(Context context, String cat_id, String title, String u_id, String urlString, Uri fileUri) {
        this.context = context;
        this.cat_id = cat_id;
        this.title = title;
        this.u_id = u_id;
        this.urlString = urlString;
        this.fileUri = fileUri;
    }

    @Override
    protected String doInBackground(Void... params) {
        String boundary = UUID.randomUUID().toString();

        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());

            // add cat_id parameter
            outputStream.writeBytes("--" + boundary + "\r\n");
            outputStream.writeBytes("Content-Disposition: form-data; name=\"cat_id\"\r\n\r\n");
            outputStream.writeBytes(cat_id + "\r\n");

            // add title parameter
            outputStream.writeBytes("--" + boundary + "\r\n");
            outputStream.writeBytes("Content-Disposition: form-data; name=\"title\"\r\n\r\n");
            outputStream.writeBytes(title + "\r\n");

            // add u_id parameter
            outputStream.writeBytes("--" + boundary + "\r\n");
            outputStream.writeBytes("Content-Disposition: form-data; name=\"u_id\"\r\n\r\n");
            outputStream.writeBytes(u_id + "\r\n");

            // add file parameter

            File file = getFileFromUri(context, fileUri);
            String fileName = file.getName();
            String contentType = URLConnection.guessContentTypeFromName(fileName);
            outputStream.writeBytes("--" + boundary + "\r\n");
            outputStream.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\"" + fileName + "\"\r\n");
            outputStream.writeBytes("Content-Type: " + contentType + "\r\n\r\n");

            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] buffer = new byte[4096];
            int bytesRead = -1;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.writeBytes("\r\n");
            fileInputStream.close();

            // end of multipart/form-data
            outputStream.writeBytes("--" + boundary + "--\r\n");
            outputStream.flush();
            outputStream.close();

            // read the response from the server
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            reader.close();
            Log.d("ddd",stringBuilder.toString());
            return stringBuilder.toString();
        } catch (Exception e) {
            Log.e("UploadFileTask", "Error uploading file", e);
            return null;
        }
    }
    @Override
    protected void onPostExecute(String result) {
        // handle the response here
    }
}