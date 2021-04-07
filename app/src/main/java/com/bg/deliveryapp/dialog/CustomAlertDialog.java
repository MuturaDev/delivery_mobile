//package com.bg.deliveryapp.dialog;
//
//import android.app.AlertDialog;
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.widget.ProgressBar;
//
//import com.bg.deliveryapp.R;
//
//import cn.pedant.SweetAlert.SweetAlertDialog;
//
//public class CustomAlertDialog {
//
//   private ProgressDialog pDialog;
//   private AlertDialog.Builder pAlert;
//
//    public void init(Context context){
//        pAlert = new AlertDialog.Builder(context);
//    }
//
//    public static AlertDialog.Builder showSuccessDialog(Context context, String title, String message){
//
//        pAlert.setTitle(title);
//        pAlert.setMessage(message);
//        pAlert.setCancelable(false);
//        return pAlert;
//    }
//
//    public static ProgressDialog showProgressDialog(Context context, String message){
////        SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
////        pDialog.getProgressHelper().setBarColor(R.color.colorGreen);
////        pDialog.setTitleText("Login confirmation...");
////        pDialog.setCancelable(false);
////        return pDialog;
//
//        ProgressDialog pDialog = new ProgressDialog(context);
//        //pDialog.setTitle(title);
//        pDialog.setMessage(message);
//        pDialog.setCancelable(false);
//        return pDialog;
//    }
//
//
//    public static AlertDialog.Builder showErrorDialog(String title,String message,Context context){
//        //SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
////        pDialog.setTitleText(title);
////        pDialog.setContentText(message);
////        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
////            @Override
////            public void onClick(SweetAlertDialog sDialog) {
////                sDialog.dismissWithAnimation();
////            }
////        });
//
//        AlertDialog.Builder pDialog = new AlertDialog.Builder(context);
//        pDialog.setTitle(title);
//        pDialog.setMessage(message);
//        pDialog.setCancelable(false);
//        pDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//        return pDialog;
//    }
//
//}
