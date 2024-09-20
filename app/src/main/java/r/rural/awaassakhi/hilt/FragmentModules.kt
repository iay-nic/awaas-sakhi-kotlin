package r.rural.awaassakhi.hilt

import android.content.Context
import cn.pedant.SweetAlert.SweetAlertDialog
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.android.scopes.FragmentScoped
import r.rural.awaassakhi.R
import javax.inject.Named

@Module
@InstallIn(FragmentComponent::class)
object FragmentModules {

    @Provides
    @FragmentScoped
    @Named("${SweetAlertDialog.WARNING_TYPE}")
    fun provideSweetAlertWarningDialog(@ActivityContext context: Context): SweetAlertDialog {
        val dialog = SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
            .setTitleText(context.getString(R.string.error))
            .setContentText("message")
            .setConfirmText(context.getString(R.string.ok))
            .setConfirmClickListener { sDialog -> if (sDialog.isShowing) sDialog.dismiss() }
        dialog.setCancelable(false)
        return dialog
    }

    @Provides
    @FragmentScoped
    @Named("${SweetAlertDialog.NORMAL_TYPE}")
    fun provideAlertWarningDialog(@ActivityContext context: Context): SweetAlertDialog {
        val dialog = SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
            .setTitleText(context.getString(R.string.warning))
            .setContentText("message")
            .setConfirmText(context.getString(R.string.ok))
            .setConfirmClickListener { sDialog -> if (sDialog.isShowing) sDialog.dismiss() }
        dialog.setCancelable(false)
        return dialog
    }

    @Provides
    @FragmentScoped
    @Named("${SweetAlertDialog.SUCCESS_TYPE}")
    fun provideSweetAlertSuccessDialog(@ActivityContext context: Context): SweetAlertDialog {
        val dialog = SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
            .setTitleText(context.getString(R.string.success))
            .setContentText("message")
            .setConfirmText(context.getString(R.string.ok))
            .setConfirmClickListener { sDialog -> if (sDialog.isShowing) sDialog.dismiss() }
        dialog.setCancelable(false)
        return dialog
    }
}