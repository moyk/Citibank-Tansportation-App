package Modules;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.maps.model.DirectionsRoute;

/**
 * Created by yianmo on 11/13/17.
 */

public class RouteWrapper implements Parcelable {
    private DirectionsRoute route;

    public RouteWrapper(DirectionsRoute route){
        this.route=route;
    }


    protected RouteWrapper(Parcel in) {
    }

    public static final Creator<RouteWrapper> CREATOR = new Creator<RouteWrapper>() {
        @Override
        public RouteWrapper createFromParcel(Parcel in) {
            return new RouteWrapper(in);
        }

        @Override
        public RouteWrapper[] newArray(int size) {
            return new RouteWrapper[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
