package com.saberpro.icfes.pickers;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TimePicker;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class TimePickerDialogFragment extends DialogFragment {

    private TimePickerDialog.OnTimeSetListener listener;
    private TimePicker time;

    public static TimePickerDialogFragment newInstance(TimePickerDialog.OnTimeSetListener listener) {
        TimePickerDialogFragment fragment = new TimePickerDialogFragment();
        fragment.setListener(listener);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hora = c.get(Calendar.HOUR);
        int minutos = c.get(Calendar.MINUTE);
        return new TimePickerDialog(getActivity(),listener,hora,minutos,false);
    }

    public void setListener(TimePickerDialog.OnTimeSetListener listener) {
        this.listener = listener;
    }

}
