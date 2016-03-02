package mckenna.colin.mckennacolinhw2;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * Based on MainActivity Class created by Scott Stanchfield
 */

public class MainActivity extends AppCompatActivity implements ContactListFragment.Callbacks, ViewContactFragment.Callbacks, EditContactFragment.Callbacks{

    private boolean hasTwoPanes;
    private EditContactFragment editFragment;
    private ViewContactFragment viewFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editFragment = (EditContactFragment) getSupportFragmentManager().findFragmentById(R.id.contact_edit_fragment);
        viewFragment = (ViewContactFragment) getSupportFragmentManager().findFragmentById(R.id.contact_view_fragment);
        hasTwoPanes = (editFragment != null && editFragment.isInLayout());

        if (hasTwoPanes) {
            ContactListFragment listFragment = (ContactListFragment)
                    getSupportFragmentManager().findFragmentById(R.id.contact_list_fragment);
            listFragment.setActivateOnItemClick(hasTwoPanes);
            showHide(viewFragment,editFragment);

        }
    }

    private void showHide(Fragment toShow, Fragment toHide) {
        getSupportFragmentManager()
                .beginTransaction()
                .show(toShow)
                .hide(toHide)
                .commit();
    }

    // Respond to "an item in the list has been selected"
    @Override
    public void onItemSelected(long id) {
        // we need to handle the selection IN THIS ACTIVITY if we're in two-pane mode
        if (hasTwoPanes) {

            showHide(viewFragment, editFragment);
            viewFragment.setContactId(id);

        } else {
            // set up an intent to edit that item, passing it as an long extra (its id)
            Intent intent = new Intent(MainActivity.this, ViewContactActivity.class);
            intent.putExtra("contactId", id);
            startActivity(intent);
        }
    }

    @Override
    public void onCreateNewItemRequested() {
        // we need to handle the creation IN THIS ACTIVITY if we're in two-pane mode
        if (hasTwoPanes) {
            editFragment.setContactId(-1);
            // not needed in this example but will be needed in the homework
            showHide(editFragment, viewFragment);
            // NOTE: we really should have the fragment check if it's "dirty"
            //       and if so, ask the user to save before changing the fields

        } else {
            // start activity
            // we're creating a new item; just pass -1 as the id
            Intent intent = new Intent(this, EditContactActivity.class);
            intent.putExtra("contactId", -1);
            startActivity(intent);
    }
    }

    @Override
    public void onDone(long id) {
        if (hasTwoPanes) {
            viewFragment.setContactId(id);
            showHide(viewFragment, editFragment);

        } else {
            // start activity
            // we're creating a new item; just pass -1 as the id
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("contactId", id);
            startActivity(intent);
        }
    }

    @Override
    public void onCancel(long id) {
        // nothing to do in this example...
        // in the assignment, you'll need to switch between the edit and display fragments using
         showHide(viewFragment, editFragment);
    }


    @Override
    public void onEdit(long id) {
        if (hasTwoPanes) {
            editFragment.setContactId(id);
            // not needed in this example but will be needed in the homework
            showHide(editFragment, viewFragment);
            // NOTE: we really should have the fragment check if it's "dirty"
            //       and if so, ask the user to save before changing the fields

        } else {
            // start activity
            // we're creating a new item; just pass -1 as the id
            Intent intent = new Intent(this, EditContactActivity.class);
            intent.putExtra("contactId", id);
            startActivity(intent);
        }
    }
}
