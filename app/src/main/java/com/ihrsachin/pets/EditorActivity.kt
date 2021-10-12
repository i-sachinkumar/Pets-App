package com.ihrsachin.pets


import android.annotation.SuppressLint
import android.content.ContentValues
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.NavUtils
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.ihrsachin.pets.data.PetContract.PetEntry.*


class EditorActivity : AppCompatActivity() {

    /** EditText field to enter the pet's name  */
    var mNameEditText: EditText? = null

    /** EditText field to enter the pet's breed  */
    private var mBreedEditText: EditText? = null

    /** EditText field to enter the pet's weight  */
    private var mWeightEditText: EditText? = null

    /** EditText field to enter the pet's gender  */
    private var mGenderSpinner: Spinner? = null

    /**
     * Gender of the pet. The possible values are:
     * 0 for unknown gender, 1 for male, 2 for female.
     */
    private var mGender = 0



    //Uri of current pet which was clicked
    private var contentPetUri : Uri? = null


    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor)

        // get intent and set title accordingly
        contentPetUri = intent.data // data is uri for item clicked

        if(contentPetUri == null) this.title = "Add a Pet"
        else{
            this.title = "Edit Pet"
        }


        // Find all relevant views that we will need to read user input from
        mNameEditText = findViewById(R.id.edit_pet_name)
        mBreedEditText = findViewById(R.id.edit_pet_breed)
        mWeightEditText =  findViewById(R.id.edit_pet_weight)
        mGenderSpinner =  findViewById(R.id.spinner_gender)

        setupSpinner()

        /**  add previous entry if this activity is opened to edit existing pet
         * by clicking on listview item*/
        if(contentPetUri != null){
            val projection = arrayOf(_ID,COLUMN_PET_NAME, COLUMN_PET_BREED, COLUMN_PET_GENDER, COLUMN_PET_WEIGHT)
            val cursor = contentResolver.query(contentPetUri!!,projection,null,null,null)
            if(cursor!!.moveToNext()) {
                cursor.run {
                    mNameEditText!!.append(cursor.getString(cursor.getColumnIndex(COLUMN_PET_NAME)).toString())
                    mBreedEditText!!.append(cursor.getString(cursor.getColumnIndex(COLUMN_PET_BREED)).toString())
                    mWeightEditText!!.append(cursor.getString(cursor.getColumnIndex(COLUMN_PET_WEIGHT)).toString())
                    mGender = cursor.getString(cursor.getColumnIndex(COLUMN_PET_GENDER)).toInt()
                    mGenderSpinner!!.setSelection(mGender)
                }
            }
            cursor.close()
        }
    }

    /**
     * Setup the dropdown spinner that allows the user to select the gender of the pet.
     */
    private fun  setupSpinner()  {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        val genderSpinnerAdapter : ArrayAdapter<*> = ArrayAdapter.createFromResource(this,
            R.array.array_gender_options, android.R.layout.simple_spinner_item)

        // Specify dropdown layout style - simple list view with 1 item per line
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)

        // Apply the adapter to the spinner
        mGenderSpinner!!.adapter = genderSpinnerAdapter

        // Set the integer mSelected to the constant values
        mGenderSpinner!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long,
            ) {
                val selection = parent.getItemAtPosition(position) as String
                if (!TextUtils.isEmpty(selection)) {
                    mGender = when (selection) {
                        getString(R.string.gender_male) -> GENDER_MALE
                        getString(R.string.gender_female) -> GENDER_FEMALE
                        else -> GENDER_UNKNOWN
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            override fun onNothingSelected(parent: AdapterView<*>?) {
                mGender = GENDER_UNKNOWN // Unknown
            }
        }

    }

    // when save icon is clicked
    private fun insertPet(){
        val mName = mNameEditText!!.text.toString().trim()
        val mBreed = mBreedEditText!!.text.toString().trim()
        if (mWeightEditText!!.text.isNotBlank() && !mWeightEditText!!.text.isNullOrEmpty()) {
            val mWeight = mWeightEditText!!.text.toString().toInt()
            val value = ContentValues()
            value.put(COLUMN_PET_NAME, mName)
            value.put(COLUMN_PET_BREED, mBreed)
            value.put(COLUMN_PET_GENDER, mGender)
            value.put(COLUMN_PET_WEIGHT, mWeight)

            contentResolver.insert(CONTENT_URI, value)
            Toast.makeText(this, getString(R.string.pet_saved_toast), Toast.LENGTH_SHORT).show()
            finish()
        } else {
            mWeightEditText!!.error = getString(R.string.not_a_valid_weight)
        }
    }

    // when save icon is clicked
    private fun updatePet(){
        val mBreed = mBreedEditText!!.text.toString().trim()
        if(mNameEditText!!.text.isBlank() || mNameEditText!!.text.isNullOrEmpty()) {
            mNameEditText!!.error = getString(R.string.required)
        }
        else if(mWeightEditText!!.text.isNotBlank() && !mWeightEditText!!.text.isNullOrEmpty()) {
            val mName = mNameEditText!!.text.toString().trim()
            val mWeight = mWeightEditText!!.text.toString().toInt()
            val value = ContentValues()
            value.put(COLUMN_PET_NAME, mName)
            value.put(COLUMN_PET_BREED, mBreed)
            value.put(COLUMN_PET_GENDER, mGender)
            value.put(COLUMN_PET_WEIGHT, mWeight)

            val update = contentResolver.update(contentPetUri!!, value, null, null)
            if (update != 0) Toast.makeText(this,
                getString(R.string.pet_updated_toast),
                Toast.LENGTH_SHORT).show()
            finish()
        }
        else {
            mWeightEditText!!.error = getString(R.string.not_a_valid_weight)
        }
    }


    private fun deletePet(){
        if(contentPetUri != null){
            contentResolver.delete(contentPetUri!!,null,null)
            Toast.makeText(this, "Pet was deleted", Toast.LENGTH_SHORT).show()
        }
        finish()
    }




    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        menuInflater.inflate(R.menu.menu_editor, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // User clicked on a menu option in the app bar overflow menu
        when (item.itemId) {
            R.id.action_save ->{
                if(contentPetUri == null) insertPet()
                else updatePet()
                return true
            }
            R.id.action_delete ->{
                deletePet()
                return true
            }
            R.id.home -> {
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}