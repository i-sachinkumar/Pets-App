package com.ihrsachin.pets.data;

import static com.ihrsachin.pets.data.PetContract.PetEntry.TABLE_NAME;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class PetContract {





    public static final class PetEntry implements BaseColumns{



        public static final String TABLE_NAME = "pets";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_PET_NAME = "name";
        public static final String COLUMN_PET_BREED = "breed";
        public static final String COLUMN_PET_GENDER = "gender";
        public static final String COLUMN_PET_WEIGHT = "weight";

        public  static final int GENDER_MALE = 1;
        public  static final int GENDER_FEMALE = 2;
        public  static final int GENDER_UNKNOWN = 0;

        public static final String CONTENT_AUTHORITY = "com.ihrsachin.pets";

        public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI,TABLE_NAME);


        public static boolean isValidGender(int gender) {
            return gender == GENDER_UNKNOWN || gender == GENDER_MALE || gender == GENDER_FEMALE;
        }


        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of pets.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single pet.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;

    }
}
