package com.mehul.corelibrary.core

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.mehul.corelibrary.helper.Event
import io.reactivex.disposables.CompositeDisposable

/**
 * Here we have Use Event class for emitting value only for once.
 * once the value is emitted it will not Emit again.
 *
 * set Message Method is used for communicate with View Class.
 *
 * observedChanges() method used for observing changes in messageString
 *
 * */

open class CoreViewModel(application: Application) : AndroidViewModel(application) {

    protected var mDisposable: CompositeDisposable? = null
    var validationErrorMessage = MutableLiveData<Event<Int>>()
    var apiErrorMessage = MutableLiveData<Event<String>>()
    var successMessage = MutableLiveData<Event<Int>>()
    var successMessageFromAPI = MutableLiveData<Event<String>>()
    var showProgress = MutableLiveData<Event<Boolean>>()

    override fun onCleared() {
        super.onCleared()
        mDisposable?.dispose()
    }

    private fun setMessage(msg: String) {
        messageString.value = msg
    }

    private var messageString = MutableLiveData<String>()

    open fun observedChanges() = messageString

/*    fun shareInviteFriend(invite:String?=null) {
        showProgress.value = Event(true)
        APITask.getInstance().shareInviteFriend(invite, object : OnResponseListener {
            override fun <T> onResponseReceived(response: T, requestCode: Int) {
   *//*             showProgress.value = Event(false)
                response as BaseResponse<*>
                successMessageFromAPI.value = Event(response.message)
                deleteChallenge.value = true*//*
            }

            override fun onResponseError(
                message: String,
                requestCode: Int,
                responseCode: Int,
            ) {
                showProgress.value = Event(false)
                apiErrorMessage.value = Event(message)
            }
        })?.let { mDisposable?.add(it) }
    }*/
}