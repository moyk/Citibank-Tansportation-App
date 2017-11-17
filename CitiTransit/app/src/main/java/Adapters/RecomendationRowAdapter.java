package Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.citi.cititransit.R;

import java.util.ArrayList;

/**
 * Created by yianmo on 11/3/17.
 */

public class RecomendationRowAdapter extends ArrayAdapter{

    //to reference the Activity
    private final Activity context;

    //to store the animal images
    private final ArrayList<Integer> imageIDarray;

    //to store the list of countries
    private final ArrayList<String> nameArray;

    //to store the list of countries
    private final ArrayList<String> infoArray;

    private final ArrayList<String> costArray;

    public RecomendationRowAdapter(Activity context, ArrayList<String> nameArrayParam, ArrayList<String> infoArrayParam, ArrayList<String> costInfoParam, ArrayList<Integer> imageIDArrayParam){

        super(context, R.layout.recommendation_row , nameArrayParam);
        this.context=context;
        this.imageIDarray = imageIDArrayParam;
        this.nameArray = nameArrayParam;
        this.infoArray = infoArrayParam;
        this.costArray = costInfoParam;

    }
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.recommendation_row, null,true);

        //this code gets references to objects in the listview_row.xml file
        TextView nameTextField = (TextView) rowView.findViewById(R.id.nameTextViewID);
        TextView infoTextField = (TextView) rowView.findViewById(R.id.infoTextViewID);
        TextView costTextField = (TextView) rowView.findViewById(R.id.costInfoID);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageView1ID);


        //this code sets the values of the objects to values from the arrays
        nameTextField.setText(nameArray.get(position));
        infoTextField.setText(infoArray.get(position));
        costTextField.setText(costArray.get(position));
//        imageView.setImageResource(imageIDarray.get(position));

        return rowView;

    };
}
