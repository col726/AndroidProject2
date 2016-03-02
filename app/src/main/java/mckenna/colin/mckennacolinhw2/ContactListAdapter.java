package mckenna.colin.mckennacolinhw2;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Based on ToDoListAdapter Class created by Scott Stanchfield
 */
public class ContactListAdapter extends BaseAdapter
{
    private Activity activity;
    private ContactList contactList;

    public ContactListAdapter(Activity activity, ContactList contactList) {
        this.activity = activity;
        this.contactList = contactList;
        contactList.addPropertyChangeListener("list", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent event) {
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @Override
    public int getCount() {
        return contactList.getList().size();
    }

    @Override
    public Object getItem(int position) {
        return contactList.getList().get(position);
    }

    @Override
    public long getItemId(int position) {
        return contactList.getList().get(position).getId();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isEmpty() {
        return contactList.getList().isEmpty();
    }



    private static class ViewHolder {
        private TextView text1;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = activity.getLayoutInflater().inflate(android.R.layout.simple_list_item_1,
                    null, false);
            viewHolder = new ViewHolder();
            viewHolder.text1 = (TextView) convertView.findViewById(android.R.id.text1);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Contact contact = contactList.getList().get(position);
        viewHolder.text1.setText(contact.getFirstName() + " " + contact.getLastName() + " (" + contact.getHomePhone() + ")");
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }
}
