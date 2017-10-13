package Adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.citi.cititransit.R;

import java.util.List;

import Modules.Ticket;

/**
 * Created by nick on 10/13/17.
 */

public class TicketListRowAdapter extends ArrayAdapter<Ticket>{

    public TicketListRowAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
    }

    public TicketListRowAdapter(Context context, int resource, List<Ticket> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View view = convertView;
        if (convertView == null) {
            LayoutInflater vi = LayoutInflater.from(getContext());
            view = vi.inflate(R.layout.ticketlist_row, null);
        }
        Ticket ticket = getItem(position);
        if(ticket != null){
            TextView ticketType = (TextView)view.findViewById(R.id.ticketType);
            TextView ticketPrice = (TextView)view.findViewById(R.id.ticketPrice);
            ImageView thumbnail = (ImageView)view.findViewById(R.id.ticketImage);
            if(ticketType != null) ticketType.setText(ticket.getTicketType());
            if(ticketPrice != null) ticketPrice.setText("$" + Double.toString(ticket.getTicketPrice()));
            if(thumbnail != null){
                switch(ticketType.getText().toString()){
                    case "Subway":
                        thumbnail.setImageResource(R.drawable.subway);
                        break;
                    case "CitiBike":
                        thumbnail.setImageResource(R.drawable.citibike);
                        break;
                }
            }
        }
        return view;
    }
}
