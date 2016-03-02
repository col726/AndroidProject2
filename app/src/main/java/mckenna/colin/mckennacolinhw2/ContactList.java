package mckenna.colin.mckennacolinhw2;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Based on ToDoList Class created by Scott Stanchfield
 *
 */
public class ContactList {

    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(propertyName, listener);
    }

    private List<Contact> list = new ArrayList<>();
    private List<Contact> unmodifiableList = Collections.unmodifiableList(list);

    public List<Contact> getList() { return unmodifiableList;   }

}
