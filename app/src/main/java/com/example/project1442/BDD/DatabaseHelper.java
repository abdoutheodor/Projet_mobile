package com.example.project1442.BDD;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.project1442.Activity.User;
import com.example.project1442.Domain.PopularDomain;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Ecommerce.db";
    private static final int DATABASE_VERSION = 2; // Augmenté pour la mise à jour du schéma

    private static final String TABLE_ITEMS = "items";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_PIC_URL = "picUrl";
    private static final String COLUMN_REVIEW = "review";
    private static final String COLUMN_SCORE = "score";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_CATEGORIE = "categorie"; // Nouvelle colonne pour catégorie


    private static final String TABLE_USERS = "users";
    private static final String COLUMN_USER_ID = "userId";
    private static final String COLUMN_FIRST_NAME = "firstName";
    private static final String COLUMN_LAST_NAME = "lastName";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";

    private static final String TABLE_USERS_CREATE =
            "CREATE TABLE " + TABLE_USERS + "(" +
                    COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_FIRST_NAME + " TEXT NOT NULL," +
                    COLUMN_LAST_NAME + " TEXT NOT NULL," +
                    COLUMN_USERNAME + " TEXT NOT NULL UNIQUE," +
                    COLUMN_PASSWORD + " TEXT NOT NULL" +
                    ");";
    private static final String TABLE_ITEMS_CREATE =
            "CREATE TABLE " + TABLE_ITEMS + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_TITLE + " TEXT NOT NULL," +
                    COLUMN_DESCRIPTION + " TEXT NOT NULL," +
                    COLUMN_PIC_URL + " TEXT NOT NULL," +
                    COLUMN_REVIEW + " INTEGER NOT NULL," +
                    COLUMN_SCORE + " REAL NOT NULL," +
                    COLUMN_PRICE + " REAL NOT NULL," +
                    COLUMN_CATEGORIE + " TEXT NOT NULL" + // Ajout de la colonne catégorie
                    ");";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(TABLE_ITEMS_CREATE);
        db.execSQL(TABLE_USERS_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_ITEMS + " ADD COLUMN " + COLUMN_CATEGORIE + " TEXT NOT NULL DEFAULT ''");
            db.execSQL(TABLE_USERS_CREATE);
        }
    }
    public long addUser(String firstName, String lastName, String username, String password) {
        // Vérifie d'abord si le nom d'utilisateur existe déjà
        if (usernameExists(username)) {
            return -1; // Retourne -1 si le nom d'utilisateur existe déjà
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FIRST_NAME, firstName);
        values.put(COLUMN_LAST_NAME, lastName);
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);

        // Effectue l'insertion
        long result = db.insert(TABLE_USERS, null, values);
        db.close();

        // Retourne l'ID de l'utilisateur nouvellement inséré ou -1 en cas d'échec
        return result;
    }

// verifie si le username existe déja
    public boolean usernameExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_USER_ID},
                COLUMN_USERNAME + "=?", new String[]{username}, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();

        return exists;
    }
    @SuppressLint("Range")
    //verifie si les information fournit par l'utilisateur existe ou pas dans la base de données
    public long checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        long userId = -1;
        Cursor cursor = db.query(TABLE_USERS, new String[] {COLUMN_USER_ID},
                COLUMN_USERNAME + "=?" + " AND " + COLUMN_PASSWORD + "=?",
                new String[]{username, password}, null, null, null);

        if (cursor.moveToFirst()) {
            userId = cursor.getLong(cursor.getColumnIndex(COLUMN_USER_ID));
        }
        cursor.close();
        db.close();

        return userId; // Retourne l'ID de l'utilisateur ou -1 si non trouvé
    }

    // permet de mettre a jour les information dans la base de données
    public int updateUser(long userId, String firstName, String lastName, String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FIRST_NAME, firstName);
        values.put(COLUMN_LAST_NAME, lastName);
        values.put(COLUMN_USERNAME, username);

        // Met à jour l'utilisateur dans la base de données
        return db.update(TABLE_USERS, values, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(userId)});
    }


    public boolean addItem(String title, String description, String picUrl, int review, double score, double price, String categorie) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_PIC_URL, picUrl);
        values.put(COLUMN_REVIEW, review);
        values.put(COLUMN_SCORE, score);
        values.put(COLUMN_PRICE, price);
        values.put(COLUMN_CATEGORIE, categorie); // Ajout de la valeur catégorie

        long result = db.insert(TABLE_ITEMS, null, values);
        db.close();
        return result != -1;
    }

    public ArrayList<PopularDomain> getItemsByCategorie(String categorie) {
        ArrayList<PopularDomain> itemsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_ITEMS, null, COLUMN_CATEGORIE + "=?", new String[]{categorie}, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE));
                @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION));
                @SuppressLint("Range") String picUrl = cursor.getString(cursor.getColumnIndex(COLUMN_PIC_URL));
                @SuppressLint("Range") int review = cursor.getInt(cursor.getColumnIndex(COLUMN_REVIEW));
                @SuppressLint("Range") double score = cursor.getDouble(cursor.getColumnIndex(COLUMN_SCORE));
                @SuppressLint("Range") double price = cursor.getDouble(cursor.getColumnIndex(COLUMN_PRICE));
                @SuppressLint("Range") String itemCategorie = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORIE));

                itemsList.add(new PopularDomain(title, description, picUrl, review, score, price, itemCategorie));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return itemsList;
    }
    public String getItemTitle(int itemId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT title FROM items WHERE id = ?", new String[] {String.valueOf(itemId)});
        String title = null;
        if (cursor.moveToFirst()) {
            title = cursor.getString(0);
        }
        cursor.close();
        db.close();
        return title;
    }

    public String getItemDescription(int itemId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT description FROM items WHERE id = ?", new String[] {String.valueOf(itemId)});
        String description = null;
        if (cursor.moveToFirst()) {
            description = cursor.getString(0);
        }
        cursor.close();
        db.close();
        return description;
    }

    // Méthodes similaires pour picUrl, review, score, et price

    public String getItemPicUrl(int itemId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT picUrl FROM items WHERE id = ?", new String[] {String.valueOf(itemId)});
        String picUrl = null;
        if (cursor.moveToFirst()) {
            picUrl = cursor.getString(0);
        }
        cursor.close();
        db.close();
        return picUrl;
    }

    public int getItemReview(int itemId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT review FROM items WHERE id = ?", new String[] {String.valueOf(itemId)});
        int review = 0;
        if (cursor.moveToFirst()) {
            review = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return review;
    }

    public double getItemScore(int itemId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT score FROM items WHERE id = ?", new String[] {String.valueOf(itemId)});
        double score = 0;
        if (cursor.moveToFirst()) {
            score = cursor.getDouble(0);
        }
        cursor.close();
        db.close();
        return score;
    }

    public double getItemPrice(int itemId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT price FROM items WHERE id = ?", new String[] {String.valueOf(itemId)});
        double price = 0;
        if (cursor.moveToFirst()) {
            price = cursor.getDouble(0);
        }
        cursor.close();
        db.close();
        return price;
    }

    public List<Integer> getTopRatedItemIds(int limit) {
        List<Integer> itemIds = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // La requête sélectionne les ID des items avec les scores les plus élevés, limité au nombre spécifié par 'limit'
        Cursor cursor = db.rawQuery("SELECT id FROM items ORDER BY score DESC LIMIT ?", new String[]{String.valueOf(limit)});

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
                itemIds.add(id);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return itemIds;
    }

    public ArrayList<PopularDomain> searchItems(String searchTerm) {
        ArrayList<PopularDomain> searchResults = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ITEMS + " WHERE " + COLUMN_TITLE + " LIKE ?", new String[] {"%" + searchTerm + "%"});

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE));
                @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION));
                @SuppressLint("Range") String picUrl = cursor.getString(cursor.getColumnIndex(COLUMN_PIC_URL));
                @SuppressLint("Range") int review = cursor.getInt(cursor.getColumnIndex(COLUMN_REVIEW));
                @SuppressLint("Range") double score = cursor.getDouble(cursor.getColumnIndex(COLUMN_SCORE));
                @SuppressLint("Range") double price = cursor.getDouble(cursor.getColumnIndex(COLUMN_PRICE));
                @SuppressLint("Range") String itemCategorie = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORIE));

                searchResults.add(new PopularDomain(title, description, picUrl, review, score, price, itemCategorie));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return searchResults;
    }

    public void deleteAllItems() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_ITEMS);
        db.close();
    }

    @SuppressLint("Range")
    public String getItemCategorie(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String categorie = null;
        Cursor cursor = null;

        try {
            cursor = db.query(TABLE_ITEMS,
                    new String[]{COLUMN_CATEGORIE}, // Colonnes à retourner
                    COLUMN_ID + " = ?", // Clause de sélection, "?" est un placeholder
                    new String[]{String.valueOf(id)}, // Valeurs pour remplacer les placeholders
                    null, // groupBy
                    null, // having
                    null); // orderBy

            if (cursor != null && cursor.moveToFirst()) {
                categorie = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORIE));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return categorie;
    }

    public ArrayList<PopularDomain> getAllItemsSortedByScore() {
        ArrayList<PopularDomain> itemsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Modifier la requête pour inclure "ORDER BY COLUMN_SCORE DESC" pour trier par score
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ITEMS + " ORDER BY " + COLUMN_SCORE + " DESC", null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") PopularDomain item = new PopularDomain(
                        cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_PIC_URL)),
                        cursor.getInt(cursor.getColumnIndex(COLUMN_REVIEW)),
                        cursor.getDouble(cursor.getColumnIndex(COLUMN_SCORE)),
                        cursor.getDouble(cursor.getColumnIndex(COLUMN_PRICE)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORIE)));
                itemsList.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return itemsList;
    }


    public ArrayList<PopularDomain> getAllItemsSortedByCategorie() {
        ArrayList<PopularDomain> itemsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Modifier la requête pour inclure "ORDER BY COLUMN_SCORE DESC" pour trier par score
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ITEMS + " ORDER BY " + COLUMN_CATEGORIE, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") PopularDomain item = new PopularDomain(
                        cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_PIC_URL)),
                        cursor.getInt(cursor.getColumnIndex(COLUMN_REVIEW)),
                        cursor.getDouble(cursor.getColumnIndex(COLUMN_SCORE)),
                        cursor.getDouble(cursor.getColumnIndex(COLUMN_PRICE)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORIE)));
                itemsList.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return itemsList;
    }
    @SuppressLint("Range")
    public User getUserById(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_USER_ID, COLUMN_FIRST_NAME, COLUMN_LAST_NAME, COLUMN_USERNAME},
                COLUMN_USER_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);

        User user = null;
        if (cursor.moveToFirst()) {
            user = new User((int) cursor.getLong(cursor.getColumnIndex(COLUMN_USER_ID)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_FIRST_NAME)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_LAST_NAME)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME)));
        }
        cursor.close();
        db.close();
        return user;
    }

}
