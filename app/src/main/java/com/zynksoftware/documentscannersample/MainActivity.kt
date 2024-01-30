package com.zynksoftware.documentscannersample

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.zynksoftware.documentscanner.model.ImageCrop
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val changeImage =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val data = it.data
                data?.data?.let { imgUri ->
                    // Set your own predefine point/coordinates for image to be cropped eg: TFLite
                    val imageCrop = ImageCrop(
                        imageUri = imgUri.toString(),
                        xMin = 100,
                        yMin = 200,
                        xMax = 200,
                        yMax = 100
                    )
                    AppScanActivity.start(this, imageCrop)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initListners()
    }

    private fun initListners() {
        scanLibButton.setOnClickListener {
            AppScanActivity.start(this, null)
        }
        cropImageButton.setOnClickListener {
            showImagePicker()
        }
    }

    private fun showImagePicker() {
        val pickImg = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        changeImage.launch(pickImg)
    }
}