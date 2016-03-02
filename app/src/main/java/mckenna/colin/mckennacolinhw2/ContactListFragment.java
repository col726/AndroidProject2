package mckenna.colin.mckennacolinhw2;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/**
 * Based on ToDoListFragment Class created by Scott Stanchfield
 */
public class ContactListFragment extends ListFragment
{

    private static final String STATE_ACTIVATED_POSITION = "activated_position";


    private Callbacks mCallbacks = sDummyCallbacks;


    private int mActivatedPosition = ListView.INVALID_POSITION;


    public interface Callbacks {
        /**
         * Callback for when an item has been selected.
         */
        void onItemSelected(long id);
        void onCreateNewItemRequested();
    }

    /**
     * A dummy implementation (aka Null Object) of the {@link Callbacks}
     * interface that does nothing. Used only when this fragment is not
     * attached to an activity.
     */
    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override public void onItemSelected(long id) {}
        @Override public void onCreateNewItemRequested() {}
    };

    // define an id for the loader we'll use to manage a cursor and stick its data in the list
    private static final int CONTACT_LOADER = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Restore the previously serialized activated item position.
        if (savedInstanceState != null &&
                savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
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

        // define the projection - the data selected from the database
        String[] from = {
                ContactProvider.ID,
                ContactProvider.FIRST_NAME,
                ContactProvider.LAST_NAME,
                ContactProvider.HOME_PHONE

        };

        // define how the projection maps to textviews in the row layout
        int[] to = {
                -1, // id not displayed in the layout (but we need it)
                R.id.contact_list_item_first_name,
                R.id.contact_list_item_last_name,
                R.id.contact_list_item_phone
        };

        // create a new adapter that will read data from a cursor and set it up in row layouts
        // the context is the activity that holds this fragment
        // HOWEVER!!! We're not yet attached to the Activity!
        //            THIS IS THE WRONG PLACE TO DO THE SETUP BECAUSE WE NEED
        //            THE ACTIVITY! (If it were a custom adapter that didn't need
        //            the activity, we could set it up here...)
        // set the adapter (use Helper method)
        setListAdapter(new SimpleCursorAdapter(getActivity(), R.layout.contact_list_item, null, from, to, 0));

        // start asynchronous loading of the cursor
        getActivity().getSupportLoaderManager().initLoader(CONTACT_LOADER, null, loaderCallbacks);
    }

    @Override
    public void onDetach() {
        getActivity().getSupportLoaderManager().destroyLoader(CONTACT_LOADER);
        setListAdapter(null);

        super.onDetach();

        // Reset the active callbacks interface to the null-object implementation.
        mCallbacks = sDummyCallbacks;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        // Notify the active callbacks interface (the activity, if the
        // fragment is attached to one) that an item has been selected.
        mCallbacks.onItemSelected(id);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */
    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        getListView().setChoiceMode(activateOnItemClick
                ? ListView.CHOICE_MODE_SINGLE
                : ListView.CHOICE_MODE_NONE);
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }



    // define a loader manager that will asynchronously retrieve data and when finished,
    //   update the list's adapter with the changes
    private LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
        // when the loader is created, setup the projection to retrieve from the database
        //   and create a cursorloader to request a cursor from a content provider (by URI)
        @Override
        public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {
            String[] projection = {
                    ContactProvider.ID,
                    ContactProvider.FIRST_NAME,
                    ContactProvider.LAST_NAME,
                    ContactProvider.BIRTHDAY,
                    ContactProvider.HOME_PHONE,
                    ContactProvider.MOBILE_PHONE,
                    ContactProvider.WORK_PHONE,
                    ContactProvider.EMAIL};
            return new CursorLoader(
                    getActivity(),
                    ContactProvider.CONTENT_URI, // note: this will register for changes
                    projection,
                    null, null, // groupby, having
                    ContactProvider.LAST_NAME + " desc");
        }

        // when the data has been loaded from the content provider, update the list adapter
        //   with the new cursor
        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            ((CursorAdapter)getListAdapter()).swapCursor(cursor); // set the data
        }

        // if the loader has been reset, kill the cursor in the adapter to remove the data from the list
        @Override
        public void onLoaderReset(Loader<Cursor> cursor) {
            ((CursorAdapter)getListAdapter()).swapCursor(null); // clear the data
        }
    };

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_contact_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id) {
            // when the "ADD" action is pressed
            case R.id.add:
                // just tell the activity and let it handle navigation
                mCallbacks.onCreateNewItemRequested();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
