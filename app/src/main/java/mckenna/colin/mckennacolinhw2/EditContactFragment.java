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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Based on EditFragment Class created by Scott Stanchfield
 */
public class EditContactFragment extends Fragment{


    public interface Callbacks {

        void onDone(long id);
        void onCancel(long id);
    }


    private Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onDone(long id) {}

        @Override
        public void onCancel(long id) {}
    };

    /**
     * The fragment's current callback object, which is notified of actions
     *   for "done" and "cancel".
     */
    private Callbacks mCallbacks = sDummyCallbacks;

    // edit texts to hold the item's values
    private EditText firstNameText;
    private EditText lastNameText;
    private Button birthdayButton;
    private EditText homePhoneText;
    private EditText mobilePhoneText;
    private EditText workPhoneText;
    private EditText emailText;
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
        View view = inflater.inflate(R.layout.fragment_edit_contact, container, false);
        // find the edit texts
        firstNameText = (EditText) view.findViewById(R.id.first_name);
        lastNameText = (EditText) view.findViewById(R.id.last_name);
        birthdayButton = (Button) view.findViewById(R.id.birthday);
        homePhoneText = (EditText) view.findViewById(R.id.home_phone);
        mobilePhoneText= (EditText) view.findViewById(R.id.mobile_phone);
        workPhoneText = (EditText) view.findViewById(R.id.work_phone);
        emailText = (EditText) view.findViewById(R.id.email);


       birthdayButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               showPicker(contact.getBirthday(), BIRTHDAY_PICKER, "birthday");
           }
       });

        return view;
    }

    public static final int BIRTHDAY_PICKER = 1;

    public void showPicker(Calendar calendar, int dateId, String type) {
        //InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        //imm.hideSoftInputFromWindow(firstNameText.getWindowToken(), 0);
        DatePickerDialogFragment fragment = DatePickerDialogFragment.create(this, dateId, calendar);
        fragment.show(getActivity().getSupportFragmentManager(), "setting " + type);
    }
    private String toString(Calendar calendar) {
        return calendar.get(Calendar.MONTH) + "/" +
                calendar.get(Calendar.DAY_OF_MONTH) + "/" +
                calendar.get(Calendar.YEAR);
    }

    //@Override
    public void onDateSet(int dateId, int year, int month, int day) {
        switch(dateId) {
            case BIRTHDAY_PICKER:
                contact.getBirthday().set(year, month, day);
                setButtonText(birthdayButton, getString(mckenna.colin.mckennacolinhw2.R.string.birthday_button_label), contact.getBirthday());
                break;
        }
    }


    /**
     * Allows the activity to tell us which item is being edited
     * We look up the item and set the current field values accordingly
     * @param id the id of the item being edited
     */
    public void setContactId(long id) {
        // if there's an id, lookup the item
        if (id != -1) {
            contact = Util.findContact(getActivity(), id);

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
        inflater.inflate(R.menu.menu_edit_contact, menu);
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
            case R.id.action_save:
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

                mCallbacks.onDone(this.contact.getId());
                return true;

            // if "cancel" was pressed, just return "canceled" without an item
            case R.id.action_cancel:
                mCallbacks.onCancel(this.contact.getId());
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
