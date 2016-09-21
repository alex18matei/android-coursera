package dailyselfie.mateialexandru.myapplication;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.Serializable;
public class SelfieImage implements Serializable{

    private String mPhotoURI;
    private String mPhotoName;
    private Bitmap mPhotoBitmap;

    public String getmPhotoURI() {
        return mPhotoURI;
    }

    public void setmPhotoURI(String mPhotoURI) {
        this.mPhotoURI = mPhotoURI;
    }

    public Bitmap getmPhotoBitmap() {
        return mPhotoBitmap;
    }

    public void setmPhotoBitmap(Bitmap mPhotoBitmap) {
        this.mPhotoBitmap = mPhotoBitmap;
    }

    public String getmPhotoName() {
        return mPhotoName;
    }

    public void setmPhotoName(String mPhotoName) {
        this.mPhotoName = mPhotoName;
    }

    public void setPhotoNameFromURI(){

        Log.i("aici",":" + mPhotoURI);
        String[] intermediateResult = mPhotoURI.split("_");
        if( intermediateResult != null){
            String[] result = intermediateResult[0].split("/");
            mPhotoName = result[result.length-1];
        }
        else{
            mPhotoName = "default";
        }

    }

}
