package arkratos.gamedev.com.colirfy;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.ImageView;

/**
 * Created by akhilraja on 12/01/18.
 */

public class MyDialog extends DialogFragment {
    NoticeDialogListener mListener;
    ImageView imageView1;
    ImageView imageView2;
    ImageView imageView3;
    ImageView imageView4;
    ImageView imageView5;

    //Create an Object which has 5 string members

    public interface NoticeDialogListener {
        public void onDialogPositiveClick(MyDialog dialog);
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        imageView1 = (ImageView) getActivity().findViewById(R.id.imageView2);
        imageView2 = (ImageView) getActivity().findViewById(R.id.imageView3);
        imageView3 = (ImageView) getActivity().findViewById(R.id.imageView4);
        imageView4 = (ImageView) getActivity().findViewById(R.id.imageView5);
        imageView5 = (ImageView) getActivity().findViewById(R.id.imageView6);


        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.dialog_layout, null))
                // Add action buttons
                .setPositiveButton("Buy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...
                    }
                });

        return builder.create();
    }

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

}
