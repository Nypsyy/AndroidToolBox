package fr.isen.mayeul.androidtoolbox.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

open class PermissionManager(val context: Context) {
    fun requestAPermission(activity: Activity, perm: String, code: Int) {
        ActivityCompat.requestPermissions(activity, arrayOf(perm), code)
    }

    fun isPermissionOk(perm: String): Boolean {
        val result = ContextCompat.checkSelfPermission(context, perm)
        return result == PackageManager.PERMISSION_GRANTED
    }

    fun requestMultiplePermissions(activity: Activity, perms: Array<String>, code: Int) {
        ActivityCompat.requestPermissions(activity, perms, code)
    }

    fun arePermissionsOk(perms: Array<String>): Boolean {
        for (p in perms) {
            if (isPermissionOk(p))
                continue
            else
                return false
        }
        return true
    }
}