package Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.citi.cititransit.R;

import java.util.List;

import Modules.Ticket;
import Modules.User;
import Modules.UserCommuteHistory;

/**
 * Created by nick on 11/16/17.
 */

public class CommuteHistoryRowAdapter extends ArrayAdapter<UserCommuteHistory>{


    public CommuteHistoryRowAdapter(@NonNull Context context, @NonNull int resource,
                                    @NonNull List<UserCommuteHistory> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View view  = convertView;
        if (convertView == null) {
            LayoutInflater vi = LayoutInflater.from(getContext());
            view = vi.inflate(R.layout.commute_history_row, null);
        }
        UserCommuteHistory history = getItem(position);
        if(history != null){
            TextView origin = (TextView)view.findViewById(R.id.history_origin_text);
            TextView destination = (TextView)view.findViewById(R.id.history_destination_text);
            TextView timeStamp = (TextView)view.findViewById(R.id.history_time_text);
            TextView cost = (TextView)view.findViewById(R.id.history_cost_text);
            if(origin != null){
                origin.setText(history.getStartPoint());
            }
            if(destination != null){
                destination.setText(history.getDestination());
            }
            if(timeStamp != null){
                timeStamp.setText(history.getTimeStamp());
            }
            if(cost != null){
                cost.setText("$" + String.valueOf(history.getTripCost()));
            }
        }
        return view;
    }
}
