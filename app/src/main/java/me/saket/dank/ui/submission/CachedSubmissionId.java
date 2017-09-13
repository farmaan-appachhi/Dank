package me.saket.dank.ui.submission;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;

import io.reactivex.functions.Function;
import me.saket.dank.utils.Cursors;

/**
 * Just a table of IDs pointing to {@link CachedSubmissionWithoutComments}.
 */
@AutoValue
public abstract class CachedSubmissionId {

  public static final String TABLE_NAME = "CachedSubmissionId";
  public static final String COLUMN_SUBREDDIT_NAME = "subreddit_name";
  public static final String COLUMN_SORTING_AND_TIME_PERIOD_JSON = "sorting_and_time_period_json";
  public static final String COLUMN_SUBMISSION_FULL_NAME = "submission_full_name";
  public static final String COLUMN_SAVE_TIME = "save_time";

  public static final String QUERY_CREATE_TABLE =
      "CREATE TABLE " + TABLE_NAME + " ("
          + COLUMN_SUBMISSION_FULL_NAME + " TEXT NOT NULL, "
          + COLUMN_SUBREDDIT_NAME + " TEXT NOT NULL, "
          + COLUMN_SORTING_AND_TIME_PERIOD_JSON + " TEXT NOT NULL, "
          + COLUMN_SAVE_TIME + " INTEGER NOT NULL, "
          + "PRIMARY KEY (" + COLUMN_SUBMISSION_FULL_NAME + ", " + COLUMN_SORTING_AND_TIME_PERIOD_JSON + ")"
          + ")";

  public static String constructQueryToGetLastSubmission(String subredditName, String sortingAndTimePeriodJson) {
    return "SELECT * FROM " + TABLE_NAME
        + " WHERE " + COLUMN_SUBREDDIT_NAME + " == '" + subredditName + "'"
        + " AND " + COLUMN_SORTING_AND_TIME_PERIOD_JSON + " == '" + sortingAndTimePeriodJson + "'"
        + " ORDER BY " + COLUMN_SAVE_TIME + " DESC"
        + " LIMIT 1";
  }

  public abstract String submissionFullName();

  public abstract String subredditName();

  public abstract SortingAndTimePeriod sortingAndTimePeriod();

  public abstract long saveTimeMillis();

  public static CachedSubmissionId create(String submissionFullName, String subredditName, SortingAndTimePeriod sortingAndTimePeriod,
      long saveTimeMillis)
  {
    return new AutoValue_CachedSubmissionId(submissionFullName, subredditName, sortingAndTimePeriod, saveTimeMillis);
  }

  public ContentValues toContentValues(JsonAdapter<SortingAndTimePeriod> sortingAndTimePeriodJsonAdapter) {
    ContentValues values = new ContentValues(4);
    values.put(COLUMN_SUBMISSION_FULL_NAME, submissionFullName());
    values.put(COLUMN_SUBREDDIT_NAME, subredditName());
    values.put(COLUMN_SORTING_AND_TIME_PERIOD_JSON, sortingAndTimePeriodJsonAdapter.toJson(sortingAndTimePeriod()));
    values.put(COLUMN_SAVE_TIME, saveTimeMillis());
    return values;
  }

  public static Function<Cursor, String> SUBMISSION_FULLNAME_MAPPER = cursor -> Cursors.string(cursor, COLUMN_SUBMISSION_FULL_NAME);

//  public static Function<Cursor, CachedSubmissionIds> cursorMapper(JsonAdapter<SortingAndTimePeriod> sortingAndTimePeriodJsonAdapter) {
//    return cursor -> {
//      String submissionFullName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SUBMISSION_FULL_NAME));
//      String subredditName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SUBREDDIT_NAME));
//      String sortingAndTimePeriodJson = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SORTING_AND_TIME_PERIOD_JSON));
//      SortingAndTimePeriod sortingAndTimePeriod = sortingAndTimePeriodJsonAdapter.fromJson(sortingAndTimePeriodJson);
//      long saveTimeMillis = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_SAVE_TIME));
//      return create(submissionFullName, subredditName, sortingAndTimePeriod, saveTimeMillis);
//    };
//  }
}
