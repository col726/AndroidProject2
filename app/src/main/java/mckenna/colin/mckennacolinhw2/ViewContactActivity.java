package mckenna.colin.mckennacolinhw2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
/**
 * Based on EditActivity Class created by Scott Stanchfield
 */


public class ViewContactActivity extends AppCompatActivity implements ViewContactFragment.Callbacks  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contact);


        long contactId = getIntent().getLongExtra("contactId", -1);
        ViewContactFragment viewContactFragment =
                (ViewContactFragment) getSupportFragmentManager().findFragmentById(R.id.view_activity_fragment_view_contact);
        viewContactFragment.setContactId(contactId);
    }




    @Override
    public void onEdit(long id) {
        Intent edit = new Intent(ViewContactActivity.this, EditContactActivity.class);
        edit.putExtra("contactId", id);
        startActivity(edit);

    }
}
