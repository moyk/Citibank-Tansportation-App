package Modules;

import java.util.List;

/**
 * Created by yianmo on 10/13/17.
 */

public interface DirectionFinderListener {
    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<Route> route);
}