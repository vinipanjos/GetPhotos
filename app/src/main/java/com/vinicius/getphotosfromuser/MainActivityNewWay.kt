package com.vinicius.getphotosfromuser

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.vinicius.getphotosfromuser.databinding.ActivityMainNewWayBinding

class MainActivityNewWay : AppCompatActivity() {

    companion object{
        private const val PERMISSION_CODE = 1
    }

    //viewBinding
    private lateinit var binding: ActivityMainNewWayBinding

    //variavel de checagem de permissão
    private var check = false

    //recebe resultado da intent de ACTION_PICK
    private val startImageResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            binding.ivImage.setImageURI(result.data?.data)
            val image: Uri? = result.data?.data
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainNewWayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Pedindo permissão
        initPermission()

        //Configurando o botão para abrir a Intent.ACTION_PICK
        binding.pickButton.setOnClickListener { openGalleryForImage() }

    }

    //Abre a intent de action_pick
    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startImageResult.launch(intent)
    }

    //checa se existe ou não permissão
    private fun initPermission() {
        if (!getPermissions()) setPermission()
        else check = true
    }


    //Checa se existe permissão
    private fun getPermissions(): Boolean = (ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.READ_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED)

    private fun setPermission() {
        val permissionList = listOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        ActivityCompat.requestPermissions(this, permissionList.toTypedArray(), PERMISSION_CODE)
    }

    private fun errorPermission(){
        Toast.makeText(this, "Não tem permissão", Toast.LENGTH_SHORT).show()
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<out String>,
                                            grantResults: IntArray) {
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    errorPermission()
                } else {
                    check = true
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }



}

