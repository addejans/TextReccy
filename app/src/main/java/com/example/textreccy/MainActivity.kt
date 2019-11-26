package com.example.textreccy

import android.content.Intent
import android.graphics.Bitmap
import android.hardware.camera2.CaptureRequest
import android.hardware.camera2.CaptureRequest.*
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.FirebaseApp
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionText
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer


class MainActivity : AppCompatActivity() {

    lateinit var cameraButton : Button
    private val REQUEST_CAMERA_CAPTURE = 124 //private final static int REQUEST_CAMERA_CAPTURE = 124
    lateinit var textRecogizer : FirebaseVisionTextRecognizer
    lateinit var image : FirebaseVisionImage


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //mImageReader = ImageReader.newInstance(pictureSizeValue.getWidth(), pictureSizeValue.getHeight(), format, maxImages);

        setContentView(R.layout.activity_main)

        FirebaseApp.initializeApp(this)

        cameraButton = findViewById(R.id.camera_button) //ToDo: activity_main xml replacement

        cameraButton.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                var takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                if (takePictureIntent.resolveActivity(packageManager) != null) { //getPackageManager()
                    startActivityForResult(takePictureIntent, REQUEST_CAMERA_CAPTURE)
                }
            }})
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CAMERA_CAPTURE && resultCode == RESULT_OK){

//            lateinit var mCaptureRequestBuilder : Builder
//            mCaptureRequestBuilder.createCaptureRequest(2)
//            mCaptureRequestBuilder.set(CaptureRequest.JPEG_QUALITY, 100.toByte())

            var extras = data?.extras //Bundle extras = data.getExtras();
            var bitmap = extras?.get("data") as Bitmap
            recognizeMyText(bitmap)
        }
    }

    private fun recognizeMyText(bitmap: Bitmap) {
        try {
            image = FirebaseVisionImage.fromBitmap(bitmap)
            textRecogizer = FirebaseVision
                .getInstance()
                .onDeviceTextRecognizer
        } catch (e: Exception) {
            e.printStackTrace()
        }

        textRecogizer.processImage(image)
            .addOnSuccessListener(object: OnSuccessListener<FirebaseVisionText> {
                override fun onSuccess(firebaseVisionText: FirebaseVisionText) {
                    var resultText = firebaseVisionText.text

                    if (resultText.isEmpty()) {
                        Toast.makeText(this@MainActivity, "NO TEXT DETECTED", Toast.LENGTH_SHORT).show()
                    } else {
                        val intent = Intent(this@MainActivity,ResultActivity::class.java) // new Intent(this, ResultActivity.class)
                        intent.putExtra(LCOTextRecognition.RESULT_TEXT, resultText)
                        startActivity(intent)
                    }
                }
            })
            .addOnFailureListener(object: OnFailureListener {
                override fun onFailure(e : Exception ) {
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            })
    }
}