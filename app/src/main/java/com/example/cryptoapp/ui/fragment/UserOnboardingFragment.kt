package com.example.cryptoapp.ui.fragment

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.example.cryptoapp.R
import com.example.cryptoapp.databinding.FragmentUserOnboardingBinding
import com.example.cryptoapp.ui.CryptoViewModel
import com.example.cryptoapp.ui.MainActivity
import com.example.cryptoapp.util.FileHelper
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class UserOnboardingFragment : BottomSheetDialogFragment() {

    lateinit var binding: FragmentUserOnboardingBinding
    private var imageUri: Uri? = null
    lateinit var viewModel: CryptoViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUserOnboardingBinding.inflate(layoutInflater, container, false)
        viewModel = (activity as MainActivity).viewModel

        binding.btnMenuBack.setOnClickListener {
            viewModel.saveUserData(
                "Monkey D. Luffy",
                drawableToBitmap(
                    ContextCompat.getDrawable(
                        binding.root.context,
                        R.drawable.luffy
                    )!!
                )
            )
            dismiss()
        }

        binding.btnImageUpload.setOnClickListener {
            getImage.launch("image/*")
        }

        binding.btnSave.setOnClickListener {
            if (binding.etvPopupUserName.text?.isNotBlank() == true && imageUri != null) {
                val userName = binding.etvPopupUserName.text.toString().trim()
                val imageBitmap = uriToBitmap(binding.root.context, imageUri!!)
                viewModel.saveUserData(userName, imageBitmap)
                dismiss()
            }
        }
        return binding.root
    }

    fun drawableToBitmap(drawable: Drawable): Bitmap {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }

        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        return bitmap
    }

    private val getImage = registerForActivityResult(
        ActivityResultContracts.GetContent(), ActivityResultCallback {
            imageUri = it
            binding.btnImageUpload.setImageURI(it)
        }
    )

    private fun uriToBitmap(context: Context, uri: Uri): Bitmap {
        val inputStream = context.contentResolver.openInputStream(uri)
        return BitmapFactory.decodeStream(inputStream)
    }

}