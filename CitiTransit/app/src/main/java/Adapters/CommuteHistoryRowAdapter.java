package Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import Modules.Ticket;
import Modules.UserCommuteHistory;

/**
 * Created by nick on 11/16/17.
 */

public class CommuteHistoryRowAdapter extends ArrayAdapter{

    public CommuteHistoryRowAdapter(@NonNull Context context, @NonNull int resource,
                                    @NonNull List<UserCommuteHistory> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View view  = convertView;
        return view;
    }
}
