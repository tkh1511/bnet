package ru.timerchat.bnet_test_tkh1511.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import ru.timerchat.bnet_test_tkh1511.Utils;
import ru.timerchat.bnet_test_tkh1511.model.ModelNote;
import ru.timerchat.bnet_test_tkh1511.R;

/**
 * Created by Timur on 22.03.16.
 */
public class NoteFragment  extends DialogFragment {


    public static NoteFragment newInstance(ModelNote note) {
        NoteFragment noteFragment = new NoteFragment();

        Bundle args = new Bundle();
        args.putString("id",  note.getId());
        args.putString("body",  note.getBody());
        args.putLong("da", note.getDa());
        args.putLong("dm", note.getDm());

        noteFragment.setArguments(args);
        return noteFragment;
    }




    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle args = getArguments();
        String id = args.getString("id");
        String body = args.getString("body");
        long da = args.getLong("da");
        long dm = args.getLong("dm");


        final ModelNote note = new ModelNote(id, body, da, dm);


        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Запись id: "+ id);



        LayoutInflater inflater = getActivity().getLayoutInflater();

        View container = inflater.inflate(R.layout.dialog_note, null);

        final TextView tvNoteDateCreation = (TextView)container.findViewById(R.id.tvNoteDateCreation);
        final TextView tvNoteDateModify = (TextView)container.findViewById(R.id.tvNoteDateModify);
        final TextView tvNoteBody = (TextView)container.findViewById(R.id.tvNoteBody);

        tvNoteBody.setText(note.getBody());
        tvNoteDateCreation.setText(Utils.getFullDate(note.getDa()));
        tvNoteDateModify.setText(Utils.getFullDate(note.getDm()));






        builder.setView(container);










        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });



        AlertDialog alertDialog = builder.create();



        return alertDialog;


    }









}
