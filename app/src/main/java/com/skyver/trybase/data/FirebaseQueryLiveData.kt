package com.skyver.trybase.data


import android.os.Handler
import androidx.lifecycle.LiveData
import com.google.firebase.firestore.*
import timber.log.Timber.*


class FirebaseQueryLiveData(val reference: DocumentReference) : LiveData<DocumentSnapshot>() {

    private val listener = MyValueEventListener()

    var registration: ListenerRegistration? = null

    private var listenerRemovePending = false
    private val handler = Handler()
    private val removeListener = Runnable {
        registration?.remove()
        listenerRemovePending = false
    }

    override fun onActive() {
        d("onActive")
        //reference.addValueEventListener(listener)

        if (listenerRemovePending) {
            handler.removeCallbacks(removeListener)
        } else {
            registration = reference.addSnapshotListener(listener)
        }
        listenerRemovePending = false
    }

    override fun onInactive() {
        d("onInactive")
        //reference.removeEventListener(listener)

        // Listener removal is schedule on a two second delay
        // It prevents from unneeded new round-trip if it is just configuration change
        handler.postDelayed(removeListener, 2000)
        listenerRemovePending = true
    }

    private inner class MyValueEventListener : EventListener<DocumentSnapshot> {

        override fun onEvent(snapshot: DocumentSnapshot?, e: FirebaseFirestoreException?) {

            if (e != null) {
                e("Listen failed. exception: $e")
                return
            }

            if (snapshot != null && snapshot.exists()) {
                d("Current data: " + snapshot.data)
                value = snapshot
            } else {
                value = snapshot
                d("Current data: null")
            }
        }
    }


}