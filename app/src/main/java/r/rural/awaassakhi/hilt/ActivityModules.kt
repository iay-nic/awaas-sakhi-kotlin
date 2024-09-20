package r.rural.awaassakhi.hilt

import android.content.Context
import cn.pedant.SweetAlert.SweetAlertDialog
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import io.github.rupinderjeet.kprogresshud.KProgressHUD

import r.rural.awaassakhi.R
import javax.inject.Named

@Module
@InstallIn(ActivityComponent::class)
object ActivityModules {

    @Provides
    @ActivityScoped
    fun provideKProgress(@ActivityContext context: Context): KProgressHUD {
        val kProgressHUD = KProgressHUD.create(context)
        kProgressHUD.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setDetailsLabel(context.getString(R.string.loading_data))
            .setCancellable(false)
            .setAnimationSpeed(2)
            .setDimAmount(0.5f)
        return kProgressHUD
    }

    @Provides
    @ActivityScoped
    @Named("ACTIVITY_WARNING")
    fun provideSweetAlertWarningDialog(@ActivityContext context: Context): SweetAlertDialog {
        val dialog = SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
            .setTitleText(context.getString(R.string.error))
            .setContentText("message")
            .setConfirmText(context.getString(R.string.ok))
            .setConfirmClickListener { sDialog -> if (sDialog.isShowing) sDialog.dismiss() }
        dialog.setCancelable(false)
        return dialog
    }


}