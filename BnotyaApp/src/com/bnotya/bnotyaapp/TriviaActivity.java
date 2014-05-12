package com.bnotya.bnotyaapp;

import java.util.List;
import com.bnotya.bnotyaapp.helpers.About;
import com.bnotya.bnotyaapp.helpers.DatabaseHelper;
import com.bnotya.bnotyaapp.models.Answer;
import com.bnotya.bnotyaapp.models.Question;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class TriviaActivity extends ActionBarActivity implements OnCheckedChangeListener
{
	// Database Helper
	DatabaseHelper _db;
	
	TextView _questionView;
	RadioGroup _answers;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.trivia_view);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
		{
			getActionBar().setDisplayHomeAsUpEnabled(true);
			getActionBar().setHomeButtonEnabled(true);
		}
		else
		{
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setHomeButtonEnabled(true);
		}

		_questionView = (TextView) findViewById(R.id.tvQuestion);
		_answers = (RadioGroup) findViewById(R.id.rgAnswers);

		_db = DatabaseHelper.getInstance(this);
		
		_db.clearDb();

		_db.createDb();

		initQuestion();
	}	

	private void initQuestion()
	{
		// get all questions
		List<Question> allQuestions = _db.getAllQuestions();
		// get question
		Question question = allQuestions.get(0);
		// set question		
		_questionView.setText(question.getContent());
		_questionView.setTag(question.getId());
		
		// Setup the answers
		List<Answer> allAnswers = _db.getAllAnswersByQuestion(question
				.getId());
		
		for (Answer answer : allAnswers)
		{
			RadioButton radioButton = new RadioButton(this);
			radioButton.setText(answer.getContent());
			radioButton.setId((int)answer.getId());
			_answers.addView(radioButton, new RadioGroup.LayoutParams(
					RadioGroup.LayoutParams.WRAP_CONTENT,
					RadioGroup.LayoutParams.WRAP_CONTENT));
		}
		
		_answers.setOnCheckedChangeListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.page_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle presses on the action bar items
		switch (item.getItemId())
		{
			case android.R.id.home:
				NavUtils.navigateUpTo(this,
						new Intent(this, MainActivity.class));
				return true;
			case R.id.action_home:
				NavUtils.navigateUpTo(this,
						new Intent(this, MainActivity.class));
				return true;
			case R.id.action_settings:
				startActivity(new Intent(this, Preferences.class));
				return true;
			case R.id.action_about:
				About.showAboutDialog(this);
				return true;
			case R.id.action_exit:
				finish();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId)
	{	
		boolean result = _db.getIsCorrectAnswer(
				Long.valueOf(_questionView.getTag().toString()), (long)checkedId);
		if(result)
		{
			Toast.makeText(getApplicationContext(),
					R.string.correct_answer, Toast.LENGTH_SHORT).show();
		}
		else
		{
			Toast.makeText(getApplicationContext(), 
					R.string.wrong_answer, Toast.LENGTH_SHORT).show();
		}
	}
}
