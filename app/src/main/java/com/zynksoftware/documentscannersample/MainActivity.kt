package com.zynksoftware.documentscannersample

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.zynksoftware.documentscanner.model.ImageCrop
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : AppCompatActivity() {

    private var imagePath: String = ""
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
                    launchImageCrop(imageCrop)
                }
            }
        }

    private val cropLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.extras?.getString(AppScanActivity.KEY_IMAGE_RESULT_PATH).let {
                imagePath = it.orEmpty()
            }
            val imageFile = File(imagePath)
            if (imageFile.exists()) {
                val bitmap = MediaStore.Images.Media.getBitmap(
                    this.contentResolver, imageFile.toUri()
                )
                ivImageResult.setImageBitmap(bitmap)
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

    private fun launchImageCrop(imageCrop: ImageCrop) {
        val takePictureIntent = Intent(this, AppScanActivity::class.java)
        takePictureIntent.putExtra(AppScanActivity.KEY_IMAGE_CROP, imageCrop)
        cropLauncher.launch(takePictureIntent)
    }

    private fun showImagePicker() {
        val pickImg = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        changeImage.launch(pickImg)
    }
}