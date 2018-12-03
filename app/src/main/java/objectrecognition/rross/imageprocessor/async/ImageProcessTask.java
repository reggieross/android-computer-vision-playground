package objectrecognition.rross.imageprocessor.async;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;

import javazoom.jl.decoder.JavaLayerException;
import objectrecognition.rross.imageprocessor.impl.ComputerVisionSvcImpl;

import static android.content.ContentValues.TAG;

public class ImageProcessTask extends AsyncTask<Bitmap, Void, Void> {
    private Context context;
    public ImageProcessTask(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(Bitmap... bitmaps) {
        File filesDir = context.getFilesDir();
        File imageFile = new File(filesDir, "test" + ".jpg");
        try {
            OutputStream os = new FileOutputStream(imageFile);
            bitmaps[0].compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
            ComputerVisionSvcImpl svc = getNewComputerVisionSvcForFile(context);
            svc.identifyObject(imageFile);
            imageFile.delete();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (Exception e) {
            Log.e(TAG, "doInBackground: " + e.toString());
        }

        return null;
    }

    public static ComputerVisionSvcImpl getNewComputerVisionSvcForFile(Context context) {
        return new ComputerVisionSvcImpl(
            "southcentralus.api.cognitive.microsoft.com",
            "/customvision/v2.0/Prediction/e0bb3ae9-1b10-4ecc-8af9-c5c31cfb36ad/image",
            "616e7939-6c0b-4606-abe4-f7e4a44a8298",
            context
        );
    }
}
