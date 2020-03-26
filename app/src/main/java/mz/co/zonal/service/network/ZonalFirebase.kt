package mz.co.zonal.service.network

import com.google.firebase.firestore.FirebaseFirestore

class ZonalFirebase {
    val zonalFirebase: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

}