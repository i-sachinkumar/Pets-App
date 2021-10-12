package com.ihrsachin.pets


import android.app.AlertDialog
import android.app.LoaderManager
import android.content.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ihrsachin.pets.data.PetContract.PetEntry.*
import android.database.Cursor
import android.net.Uri
import android.view.View
import android.widget.ListView
import android.widget.Toast
import com.ihrsachin.pets.data.PetContract.PetEntry.CONTENT_URI


class MainActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<Cursor> {

    final val PET_LOADER : Int = 0
    lateinit var cursorAdapter : PetsCursorAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fab : FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this, EditorActivity::class.java)
            startActivity(intent)
        }

        //Declaring listView
        val listView : ListView = findViewById(R.id.listView)

        // setting a view to show when list is empty
        val emptyView : View? = findViewById(R.id.empty_view)
        listView.emptyView = emptyView

        //setting adapter to the listview to populate the thing
        cursorAdapter = PetsCursorAdapter(this,null)
        listView.adapter = cursorAdapter

        //initialize loader
        loaderManager.initLoader(PET_LOADER,null,this)

        listView.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(this, EditorActivity::class.java)
            intent.data = Uri.withAppendedPath(CONTENT_URI,id.toString()) // pass PET_ID Uri
            startActivity(intent)
        }
    }

    /**
     * Temporary helper method to display information in the onscreen TextView about the state of
     * the pets database.
     */
//    @SuppressLint("Range")
//    private fun displayDatabaseInfo() {
//
//        val projection = arrayOf(_ID,COLUMN_PET_NAME, COLUMN_PET_BREED, COLUMN_PET_GENDER, COLUMN_PET_WEIGHT)
//
//        val cursor = contentResolver.query(
//            CONTENT_URI,
//            projection,
//            null,
//            null,
//            null
//        )
//            val listView : ListView = findViewById(R.id.listView)
//
//            val adapter = PetsCursorAdapter(this, cursor!!)
//            listView.adapter = adapter
//        val emptyView : RelativeLayout = findViewById(R.id.empty_view)
//        if(cursor.count ==0){
//            emptyView.visibility = VISIBLE
//        }
//        else{
//            emptyView.visibility = GONE
//        }
//
//    }

    private fun insertPet(){
        val value = ContentValues();
        value.put(COLUMN_PET_NAME,"Toto")
        value.put(COLUMN_PET_BREED, "beerier")
        value.put(COLUMN_PET_GENDER,2)
        value.put(COLUMN_PET_WEIGHT, 6)
        contentResolver.insert(CONTENT_URI, value)
       // displayDatabaseInfo()
    }

    private fun deleteAllPets(){


        /*************************************************************************
         Alert dialog for confirmation of call as it delete all pets
        **************************************************************************/

        AlertDialog.Builder(this)
            .setTitle("clear all data?")
            .setMessage("All pets information will be lost")
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setPositiveButton("delete",
                DialogInterface.OnClickListener { dialog, whichButton ->
                    /*****perform operation when delete is clicked*****/
                    contentResolver.delete(CONTENT_URI,null,null)
                    //displayDatabaseInfo()
                })
            .setNegativeButton(android.R.string.cancel,
                DialogInterface.OnClickListener { dialog, whichButton ->
                    /*****perform no operation when cancel is clicked*****/
                })
            .show()
    }

    override fun onStart() {
        //displayDatabaseInfo()
        super.onStart()
    }


    override fun onCreateOptionsMenu(menu : Menu): Boolean {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        menuInflater.inflate(R.menu.menu_catalog, menu)
        return true
    }


    override fun onOptionsItemSelected(item : MenuItem) : Boolean{
        // User clicked on a menu option in the app bar overflow menu
        when (item.itemId) {
            // Respond to a click on the "Insert dummy data" menu option
            R.id.action_insert_dummy_data ->{
                insertPet()
                return true
            }
            // Do nothing for now

            // Respond to a click on the "Delete all entries" menu option
            R.id.action_delete_all_entries -> {
                deleteAllPets()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


/************************************************************************************************************************************************************************
    Background task of reading writing a database using loader                                                                                                     */
    override fun onCreateLoader(p0: Int, p1: Bundle?): Loader<Cursor> {
        val projection = arrayOf(_ID,COLUMN_PET_NAME, COLUMN_PET_BREED/**, COLUMN_PET_GENDER, COLUMN_PET_WEIGHT*/)
        return CursorLoader(
            this,
            CONTENT_URI,
            projection,
            null,
            null,
            null
        )
    }

    override fun onLoadFinished(p0: Loader<Cursor>?, cursor: Cursor?) {
        cursorAdapter.swapCursor(cursor)
    }

    override fun onLoaderReset(p0: Loader<Cursor>?) {
        cursorAdapter.swapCursor(null)
    }                                                                                                                                                                  /**
*******************************************************************************************************************************************************************************/
}