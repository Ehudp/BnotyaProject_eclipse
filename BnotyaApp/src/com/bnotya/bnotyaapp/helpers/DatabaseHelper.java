package com.bnotya.bnotyaapp.helpers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import com.bnotya.bnotyaapp.models.Answer;
import com.bnotya.bnotyaapp.models.Question;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper
{
	private static DatabaseHelper instance;

	// Logcat tag
	private static final String LOG = "DatabaseHelper";

	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "cardsManager";

	// Table Names
	private static final String TABLE_QUESTION = "questions";
	private static final String TABLE_ANSWER = "answers";
	private static final String TABLE_QUESTION_ANSWER = "questions_answers";

	// Common column names
	private static final String KEY_ID = "id";
	private static final String KEY_CONTENT = "content";

	// TABLE_QUESTION_ANSWER column names
	private static final String KEY_QUESTION_ID = "question_id";
	private static final String KEY_ANSWER_ID = "answer_id";
	private static final String KEY_ISCORRECT = "iscorrect";

	// Table Create Statements
	// TABLE_QUESTION table create statement
	private static final String CREATE_TABLE_QUESTION = "CREATE TABLE "
			+ TABLE_QUESTION + "(" + KEY_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_CONTENT
			+ " TEXT NOT NULL)";

	// TABLE_ANSWER table create statement
	private static final String CREATE_TABLE_ANSWER = "CREATE TABLE "
			+ TABLE_ANSWER + "(" + KEY_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_CONTENT
			+ " TEXT NOT NULL)";

	// TABLE_QUESTION_ANSWER table create statement
	private static final String CREATE_TABLE_QUESTION_ANSWER = "CREATE TABLE "
			+ TABLE_QUESTION_ANSWER 
			+ "(" 
			+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ KEY_QUESTION_ID + " INTEGER NOT NULL, " 
			+ KEY_ANSWER_ID + " INTEGER NOT NULL, "
			+ KEY_ISCORRECT + " BOOLEAN NOT NULL, "
			+ "FOREIGN KEY (" + KEY_ANSWER_ID + ") REFERENCES " + TABLE_ANSWER + " (" + KEY_ID	+ "), " 
			+ "FOREIGN KEY (" + KEY_QUESTION_ID	+ ") REFERENCES " + TABLE_QUESTION + " (" + KEY_ID + ")) ";

	public static DatabaseHelper getInstance(Context context)
	{
		if (instance == null)
		{
			instance = new DatabaseHelper(context.getApplicationContext());
		}
		return instance;
	}

	private DatabaseHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		// creating required tables
		db.execSQL(CREATE_TABLE_QUESTION);
		db.execSQL(CREATE_TABLE_ANSWER);
		db.execSQL(CREATE_TABLE_QUESTION_ANSWER);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		// on upgrade drop older tables
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTION_ANSWER);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTION);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ANSWER);		

		// create new tables
		onCreate(db);
	}

	@Override
	public void onOpen(SQLiteDatabase db)
	{
		super.onOpen(db);
		if (!db.isReadOnly())
		{
			// Enable foreign key constraints
			db.execSQL("PRAGMA foreign_keys=ON;");
		}
	}
	
	public void clearDb()
	{
		SQLiteDatabase db = this.getWritableDatabase();
		int version = db.getVersion();
		onUpgrade(db, version, ++version);
	}

	// ------------- "TABLE_QUESTION" methods ----------------//

	/**
	 * Create a question
	 */
	public long createQuestion(Question question, long[] answer_ids)
	{
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_CONTENT, question.getContent());

		// insert row
		long question_id = db.insert(TABLE_QUESTION, null, values);

		// insert tag_ids
		for (long answer_id : answer_ids)
		{
			createQuestionAnswerEntry(question_id, answer_id);
		}
		
		return question_id;
	}

	/**
	 * Get single question
	 */
	public Question getQuestion(long question_id)
	{
		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT  * FROM " + TABLE_QUESTION + " WHERE "
				+ KEY_ID + " = " + question_id;

		Cursor c = db.rawQuery(selectQuery, null);

		if (c != null) c.moveToFirst();

		Question question = new Question();
		question.setId(c.getInt(c.getColumnIndex(KEY_ID)));
		question.setContent(c.getString(c.getColumnIndex(KEY_CONTENT)));

		return question;
	}

	/**
	 * Get all questions
	 * */
	public List<Question> getAllQuestions()
	{
		List<Question> questions = new ArrayList<Question>();
		String selectQuery = "SELECT  * FROM " + TABLE_QUESTION;

		Log.e(LOG, selectQuery);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst())
		{
			do
			{
				Question question = new Question();
				question.setId(c.getInt(c.getColumnIndex(KEY_ID)));
				question.setContent(c.getString(c.getColumnIndex(KEY_CONTENT)));

				// adding to question list
				questions.add(question);
			} while (c.moveToNext());
		}

		return questions;
	}

	/**
	 * Get question count
	 */
	public int getQuestionCount()
	{
		String countQuery = "SELECT  * FROM " + TABLE_QUESTION;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);

		int count = cursor.getCount();
		cursor.close();

		// return count
		return count;
	}

	/**
	 * Update a question
	 */
	public int updateQuestion(Question question)
	{
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_CONTENT, question.getContent());

		// updating row
		return db.update(TABLE_QUESTION, values, KEY_ID + " = ?", new String[]
		{
			String.valueOf(question.getId())
		});
	}

	/**
	 * Delete a question
	 */
	public void deleteQuestion(Question question,
			boolean should_delete_all_questions_answers)
	{
		SQLiteDatabase db = this.getWritableDatabase();

		// before deleting a question
		// check if answers under it should also be deleted
		if (should_delete_all_questions_answers)
		{
			db.delete(TABLE_QUESTION_ANSWER, KEY_QUESTION_ID + " = ?", new String[]
					{
						String.valueOf(question.getId())
					});
			// get all answers under this question
			List<Answer> allQuestionAnswers = getAllAnswersByQuestion(question
					.getId());

			// delete all answers
			for (Answer answer : allQuestionAnswers)
			{
				// delete answer
				deleteAnswer(answer.getId());
			}
		}

		// now delete the question
		db.delete(TABLE_QUESTION, KEY_ID + " = ?", new String[]
		{
			String.valueOf(question.getId())
		});
	}

	// --------------- "TABLE_ANSWER" methods ----------------//

	/**
	 * Create an answer
	 */
	public long createAnswer(Answer answer)
	{
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_CONTENT, answer.getContent());

		// insert row
		db.insert(TABLE_ANSWER, null, values);

		Cursor c = db.query(TABLE_ANSWER, new String[] {KEY_ID}, null, null, null, null, null);
		c.moveToLast();
		long answer_id = c.getInt(c.getColumnIndex(KEY_ID));		
		
		return answer_id;
	}

	/**
	 * Get single answer
	 */
	public Answer getAnswer(long answer_id)
	{
		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT  * FROM " + TABLE_ANSWER + " WHERE "
				+ KEY_ID + " = " + answer_id;

		Cursor c = db.rawQuery(selectQuery, null);

		if (c != null) c.moveToFirst();

		Answer answer = new Answer();
		answer.setId(c.getInt(c.getColumnIndex(KEY_ID)));
		answer.setContent(c.getString(c.getColumnIndex(KEY_CONTENT)));

		return answer;
	}

	/**
	 * Get all answers
	 * */
	public List<Answer> getAllAnswers()
	{
		List<Answer> answers = new ArrayList<Answer>();
		String selectQuery = "SELECT * FROM " + TABLE_ANSWER;

		Log.e(LOG, selectQuery);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst())
		{
			do
			{
				Answer answer = new Answer();
				answer.setId(c.getInt((c.getColumnIndex(KEY_ID))));
				answer.setContent(c.getString(c.getColumnIndex(KEY_CONTENT)));

				// adding to answers list
				answers.add(answer);
			} while (c.moveToNext());
		}
		return answers;
	}

	/**
	 * Update an answer
	 */
	public int updateAnswer(Answer answer)
	{
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_CONTENT, answer.getContent());

		// updating row
		return db.update(TABLE_ANSWER, values, KEY_ID + " = ?", new String[]
		{
			String.valueOf(answer.getId())
		});
	}

	/**
	 * Delete an answer
	 */
	public void deleteAnswer(long answer_id)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_ANSWER, KEY_ID + " = ?", new String[]
		{
			String.valueOf(answer_id)
		});
	}

	/**
	 * getting all answers under single question
	 * */
	public List<Answer> getAllAnswersByQuestion(long question_id)
	{
		List<Answer> answers = new ArrayList<Answer>();
		SQLiteDatabase db = this.getReadableDatabase();
		
		String selectQuery = "SELECT * FROM " + TABLE_ANSWER + " AS TA JOIN " + TABLE_QUESTION_ANSWER + " AS TQA ON TQA." + KEY_ANSWER_ID + "=TA." + KEY_ID
				+ " WHERE " + KEY_QUESTION_ID + " = " + question_id;
		Cursor c = db.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		if (c.moveToFirst())
		{
			do
			{
				Answer answer = new Answer();
				answer.setId(c.getInt(c
						.getColumnIndex(KEY_ID)));
				answer.setContent(c.getString(c
						.getColumnIndex(KEY_CONTENT)));	
				answers.add(answer);
				
			} while (c.moveToNext());
		}
		

		return answers;
	}

	// --------- "TABLE_QUESTION_ANSWER" methods -----------//

	/**
	 * Create QuestionAnswer entry
	 */
	public long createQuestionAnswerEntry(long question_id, long answer_id)
	{
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_QUESTION_ID, question_id);
		values.put(KEY_ANSWER_ID, answer_id);
		values.put(KEY_ISCORRECT, false);

		long id = db.insert(TABLE_QUESTION_ANSWER, null, values);

		return id;
	}

	/**
	 * Update a QuestionAnswer entry
	 */
	public int updateQuestionAnswerEntry(long question_id, long answer_id,
			boolean isCorrect)
	{
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_ISCORRECT, isCorrect);

		// updating row
		String whereClause = String.format("%s = ? AND %s = ?", KEY_QUESTION_ID, KEY_ANSWER_ID);
		
		return db.update(TABLE_QUESTION_ANSWER, values, whereClause, new String[]
		{
				String.valueOf(question_id), String.valueOf(answer_id)
		});
	}

	/**
	 * Get is correct answer
	 */
	public boolean getIsCorrectAnswer(long question_id, long answer_id)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		String selectQuery = "SELECT * FROM " + TABLE_QUESTION_ANSWER
				+ " WHERE " + KEY_QUESTION_ID + " = " + question_id + " AND "
				+ KEY_ANSWER_ID + " = " + answer_id;

		Cursor c = db.rawQuery(selectQuery, null);

		if (c != null) c.moveToFirst();

		int result = c.getInt(c.getColumnIndex(KEY_ISCORRECT));

		return result == 1;
	}

	/**
	 * Delete a QuestionAnswer entry by question
	 */
	public void deleteQuestionAnswerEntryByQuestion(long question_id)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_QUESTION_ANSWER, KEY_QUESTION_ID + " = ?", new String[]
		{
			String.valueOf(question_id)
		});
	}

	/**
	 * Delete a QuestionAnswer entry by answer
	 */
	public void deleteQuestionAnswerEntryByAnswer(long answer_id)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_QUESTION_ANSWER, KEY_ANSWER_ID + " = ?", new String[]
		{
			String.valueOf(answer_id)
		});
	}

	// closing database
	public void closeDB()
	{
		SQLiteDatabase db = this.getReadableDatabase();
		if (db != null && db.isOpen()) db.close();
	}

	/**
	 * Get datetime
	 * */
	private String getDateTime()
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		Date date = new Date();
		return dateFormat.format(date);
	}
}
