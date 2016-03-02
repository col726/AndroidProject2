package mckenna.colin.mckennacolinhw2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
/**
 *Based on EditActivity Class created by Scott Stanchfield
 */
public class EditContactActivity extends AppCompatActivity implements EditContactFragment.Callbacks {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        long contactId = getIntent().getLongExtra("contactId", -1);

        EditContactFragment editContactFragment;
        editContactFragment = (EditContactFragment) getSupportFragmentManager().findFragmentById(R.id.edit_activity_fragment_edit_contact);
        editContactFragment.setContactId(contactId);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }

    @Override
    public void onDone(long id) {
        // put the item id in a dummy intent to hold the data
        Intent data = new Intent();
        data.putExtra("contactId", id);

        // set the data as a result to be passed back
        setResult(RESULT_OK, data);

        // flag that we want to remove this activity from the stack and go back
        finish();
    }

    @Override
    public void onCancel(long id) {
        // do NOT set up data for return; just set the "canceled" result
        setResult(RESULT_CANCELED);

        // flag that we want to remove this activity from the stack and go back
        finish();
    }
}
