package kr.ac.duksung.tensorflow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.tensorflow.lite.support.common.ops.NormalizeOp;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;
import org.tensorflow.lite.task.vision.detector.Detection;
import org.tensorflow.lite.task.vision.detector.ObjectDetector;
import org.tensorflow.lite.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    TextView camButton;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        camButton = (TextView) findViewById(R.id.camButton);
        imageView = (ImageView) findViewById(R.id.imageView);


        //버튼 클릭시 카메라
        camButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i, 0);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 카메라 촬영을 하면 이미지뷰에 사진 삽입
        if (requestCode == 0 && resultCode == RESULT_OK) {
            // Bundle로 데이터를 입력
            Bundle extras = data.getExtras();

            // Bitmap으로 컨버전
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            // 이미지뷰에 Bitmap으로 이미지를 입력

            //imageView.setImageBitmap(imageBitmap);

            try {
                runObjectDetection(imageBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

    private void runObjectDetection(Bitmap bitmap) throws IOException {
        // TODO: Add object detection code here

        int targetWidth = 416;
        int targetHeight = 416;

        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, false);



        // 1. Create an image object
        TensorImage image = TensorImage.fromBitmap(bitmap);




        // 2. Create the detection program object
        ObjectDetector.ObjectDetectorOptions.Builder builder = ObjectDetector.ObjectDetectorOptions.builder();
        builder.setMaxResults(5); // Maximum number of objects to detect from the model
        builder.setScoreThreshold(0.5f); // Confidence threshold for the object detector to return detected objects

        ObjectDetector.ObjectDetectorOptions options = builder.build();

        ObjectDetector detector = ObjectDetector.createFromFileAndOptions(
                this,
                "yolov3-tiny-416.tflite",
                options
        );


        // 3. Feed the image to the detection program
        List<Detection> results = detector.detect(image); // Feed the image to the detector
        // 4. Call the result output method


        if (!results.isEmpty()) {
            //results list contain list of detected objects
            Log.e("result:","Objects = " + results);
        }
    }
/*
    private void debugPrint(List<Detection> results) {
        for (int i = 0; i < results.size(); i++) {
            Detection obj = results.get(i);
            RectF box = obj.getBoundingBox();

            Log.d("result", "Detected object : " + i);
            Log.d("result", "  boundingBox : (" + box.left + ", " + box.top + ") - (" + box.right + ", " + box.bottom + ")");

            for (int j = 0; j < obj.getCategories().size(); j++) {
                DetectionCategory category = obj.getCategories().get(j);
                Log.d("result", "    Label " + j + ": " + category.getLabel());
                int confidence = (int) (category.getScore() * 100);
                Log.d("result", "    Confidence: " + confidence + "%");
            }
        }
    }
*/
}