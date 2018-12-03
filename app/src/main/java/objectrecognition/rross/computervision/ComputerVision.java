package objectrecognition.rross.computervision;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import objectrecognition.rross.imageprocessor.async.ImageProcessTask;

public class ComputerVision extends AppCompatActivity {

    private Button btnCapture;
    private ImageView imgCapture;
    private static final int Image_Capture_Code = 1;
    private static Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_computer_vision);
        context = getApplicationContext();
        btnCapture = findViewById(R.id.btnTakePicture);
        imgCapture = findViewById(R.id.capturedImage);
        btnCapture.setOnClickListener(v -> {
            Intent cInt = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cInt,Image_Capture_Code);
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Image_Capture_Code) {
            if (resultCode == RESULT_OK) {
                Bitmap bp = (Bitmap) data.getExtras().get("data");
                imgCapture.setImageBitmap(bp);
                ImageProcessTask task = new ImageProcessTask(context);
                task.execute(bp);
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            }
        }
    }
}
