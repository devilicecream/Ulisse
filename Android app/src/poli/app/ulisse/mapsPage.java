package poli.app.ulisse;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created with IntelliJ IDEA.
 * User: dave
 * Date: 23/05/13
 * Time: 15.23
 * To change this template use File | Settings | File Templates.
 */
public class mapsPage extends Fragment {

    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {

        // fragment not when container null
        if (container == null) {
            return null;
        }
        // inflate view from layout
        View view = (LinearLayout)inflater.inflate(R.layout.left,container,false);

        return view;
    }
}
