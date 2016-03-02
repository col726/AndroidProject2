package mckenna.colin.mckennacolinhw2;


import java.util.Calendar;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;


public class Contact {

    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(propertyName, listener);
    }

    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(propertyName, listener);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    private long id;
    private String firstName;
    private String lastName;
    private Calendar birthday = Calendar.getInstance();
    private String homePhone;
    private String mobilePhone;
    private String workPhone;
    private String email;

    public Contact(){
        this.id = -1;
        this.firstName = "";
        this.lastName = "";
        this.birthday = Calendar.getInstance();
        this.homePhone = "";
        this.mobilePhone = "";
        this.workPhone = "";
        this.email= "";
    }

    public Contact(long id, String firstName, String lastName, Calendar birthday, String homePhone, String mobilePhone, String workPhone, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.homePhone = homePhone;
        this.mobilePhone = mobilePhone;
        this.workPhone = workPhone;
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        long old = this.id;
        this.id = id;
        pcs.firePropertyChange("id", old, id);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        String old = this.firstName;
        this.firstName = firstName;
        pcs.firePropertyChange("firstName", old, firstName);
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        String old = this.lastName;
        this.lastName = lastName;
        pcs.firePropertyChange("lastName", old, lastName);
    }

    public Calendar getBirthday() {
        return birthday;
    }

    public void setBirthday(Calendar birthday) {
        Calendar old = this.birthday;
        this.birthday = birthday;
        pcs.firePropertyChange("birthday", old, birthday);
    }

    public String getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(String homePhone) {
        String old = this.homePhone;
        this.homePhone = homePhone;
        pcs.firePropertyChange("homePhone", old, homePhone);
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        String old = this.mobilePhone;
        this.mobilePhone = mobilePhone;
        pcs.firePropertyChange("mobilePhone", old, mobilePhone);
    }

    public String getWorkPhone() {
        return workPhone;
    }

    public void setWorkPhone(String workPhone) {
        String old = this.workPhone;
        this.workPhone = workPhone;
        pcs.firePropertyChange("workPhone", old, workPhone);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        String old = this.email;
        this.email = email;
        pcs.firePropertyChange("email", old, email);
    }





}
