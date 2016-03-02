package mckenna.colin.mckennacolinhw2;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Calendar;

/**
 * Based on EditFragment Class created by Scott Stanchfield
 */
public class ViewContactFragment extends Fragment{

    public interface Callbacks {

        void onEdit(long id);
    }


    private Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onEdit(long id) {}

    };

    /**
     * The fragment's current callback object, which is notified of actions
     *   for "done" and "cancel".
     */
    private Callbacks mCallbacks = sDummyCallbacks;

    // edit texts to hold the item's values
    private TextView firstNameText;
    private TextView lastNameText;
    private Button birthdayButton;
    private TextView homePhoneText;
    private TextView mobilePhoneText;
    private TextView workPhoneText;

    private TextView emailText;
    private Contact contact;
    // private Button startButton;
    // private Button endButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_contact, container, false);
        // find the edit texts
        firstNameText = (TextView) view.findViewById(R.id.first_name_view);
        lastNameText = (TextView) view.findViewById(R.id.last_name_view);
        birthdayButton = (Button) view.findViewById(R.id.birthday_view);
        homePhoneText = (TextView) view.findViewById(R.id.home_phone_view);
        mobilePhoneText= (TextView) view.findViewById(R.id.mobile_phone_view);
        workPhoneText = (TextView) view.findViewById(R.id.work_phone_view);
        emailText = (TextView) view.findViewById(R.id.email_view);

        return view;
    }

    public static final int BIRTHDAY_PICKER = 1;


    private String toString(Calendar calendar) {
        return calendar.get(Calendar.MONTH) + "/" +
                calendar.get(Calendar.DAY_OF_MONTH) + "/" +
                calendar.get(Calendar.YEAR);
    }



    /**
     * Allows the activity to tell us which item is being edited
     * We look up the item and set the current field values accordingly
     * @param id the id of the item being edited
     */
    public void setContactId(long id) {
        // if there's an id, lookup the item
        if (id != -1) {
            contact= Util.findContact(getActivity(), id);

            // if no id, create a new item
        } else {
            contact = new Contact();
        }

        // fill in the initial values for the UI
        firstNameText.setText(contact.getFirstName());
        lastNameText.setText(contact.getLastName());
        setButtonText(birthdayButton, "Birthday", contact.getBirthday());
        homePhoneText.setText(contact.getHomePhone());
        mobilePhoneText.setText(contact.getMobilePhone());
        workPhoneText.setText(contact.getWorkPhone());
        emailText.setText(contact.getEmail());


    }

    private void setButtonText(Button button, String label, Calendar calendar) {
        button.setText(label + ": " + toString(calendar));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_view_contact, menu);
    }

    // handle action bar items pressed
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id) {
            // if "done" was pressed, save the data in a new item and return it
            case R.id.action_edit:
                // set up an item to return
                this.contact.setFirstName(firstNameText.getText().toString());
                this.contact.setLastName(lastNameText.getText().toString());
                //Calendar bday = Calendar.getInstance();
                //bday.setTime();
                //this.item.setBirthday(birthdayText.getText());
                this.contact.setHomePhone(homePhoneText.getText().toString());
                this.contact.setMobilePhone(mobilePhoneText.getText().toString());
                this.contact.setWorkPhone(workPhoneText.getText().toString());
                // update the item in the database
                Util.updateContact(getActivity(), this.contact);

                mCallbacks.onEdit(this.contact.getId());
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // NOTE: The template used onAttach(Activity), which is deprecated
    //       I changed it to use onAttach(Context)
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // NOTE: The template assumes that the activity including us implements
        //       our callback interface. I'm not terribly keen on this
        //       because it assumes that an activity will only ever include
        //       a single instance of a given fragment. We'll use it this way
        //       for now, but keep in mind if you ever need multiple instances
        //       of a fragment you'll need to either add some identifying
        //       information in your callback indicating _which_ fragment
        //       instance is calling the activity, or you'll need to have the
        //       activity explicit set the callback via a setCallback method
        //       that you would add to the fragment, and be sure you call
        //       it in the proper places

        // ASSUMPTION: Activities containing this fragment must implement its callbacks.
        if (!(context instanceof Callbacks)) {
            throw new IllegalStateException("Context must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Reset the active callbacks interface to the null-object implementation.
        mCallbacks = sDummyCallbacks;
    }
}
