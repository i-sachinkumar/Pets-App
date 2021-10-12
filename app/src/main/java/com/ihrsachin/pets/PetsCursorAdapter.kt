package com.ihrsachin.pets

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CursorAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import com.ihrsachin.pets.data.PetContract.PetEntry.COLUMN_PET_BREED
import com.ihrsachin.pets.data.PetContract.PetEntry.COLUMN_PET_NAME

class PetsCursorAdapter(context : Context, cursor: Cursor?) : CursorAdapter(context,cursor) {


    override fun newView(context: Context?, cursor: Cursor?, parent: ViewGroup?): View {
        return LayoutInflater.from(context).inflate(R.layout.item_pet,parent,false)
    }

    override fun bindView(view: View?, context: Context?, cursor: Cursor?) {

        //index of name column
        val nameColIndex : Int = cursor!!.getColumnIndex(COLUMN_PET_NAME)

        //index of breed column
        val breedColIndex: Int = cursor.getColumnIndex(COLUMN_PET_BREED)

        //Pet Name as string
        val petName : String = cursor.getString(nameColIndex)

        //Pet Breed as string
        val petBreed: String = cursor.getString(breedColIndex)

        //set these values to the textViews
        val textView : TextView = view!!.findViewById(R.id.pet_name_textView)
        textView.text = petName
        val textView2 : TextView = view.findViewById(R.id.pet_breed_textView)
        textView2.text = petBreed

    }
}