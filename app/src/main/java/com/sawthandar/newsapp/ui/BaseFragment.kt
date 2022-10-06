package com.sawthandar.newsapp.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.kaopiz.kprogresshud.KProgressHUD

abstract class BaseFragment: Fragment() {

    lateinit var kProgressHUD: KProgressHUD

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        kProgressHUD = KProgressHUD.create(requireContext())
        super.onViewCreated(view, savedInstanceState)
    }

    protected fun showProgressDialog() {
        kProgressHUD.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel("Loading")
            .show()
    }

    protected fun hideProgressDialog() {
        kProgressHUD.dismiss()
    }

}