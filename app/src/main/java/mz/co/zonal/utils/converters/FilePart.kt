package mz.co.zonal.utils.converters

import android.net.Uri
import androidx.annotation.NonNull
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class FilePart {

    companion object {
        fun prepareFilePart(
            partName: String,
            fileUri: Uri
        ): MultipartBody.Part? {

            val file = File(fileUri.path!!)

            val requestFile: RequestBody = RequestBody.create(
                MediaType.parse("image/*"),
                file
            )

            return MultipartBody.Part.createFormData(partName, file.name, requestFile)
        }

        @NonNull
        fun createPartFromString(descriptionString: String): RequestBody? {
            return RequestBody.create(
                MultipartBody.FORM, descriptionString
            )
        }
    }
}