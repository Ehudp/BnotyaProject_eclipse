package com.bnotya.bnotyaapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.bnotya.bnotyaapp.R;

public class MainMailFragment extends Fragment implements OnClickListener
{
	public static final String ARG_VIEW_NUMBER = "view_number";
	EditText email, name, title, content;
	String emailString, nameString, titleString, contentString;

	public MainMailFragment()
	{
		// Empty constructor required for fragment subclasses
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.fragment_main_mail,
				container, false);		

		getActivity().setTitle(R.string.email_page_title);		
		initControls(rootView);		
		
		return rootView;
	}

	@Override
	public void onClick(View v)
	{
		//sendFullMail();
		sendMinimizedMail();
	}
	
	private void sendMinimizedMail()
	{
		String[]address = {"a@a.a"}; // TODO: Replace with Bnotya's address
		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		emailIntent.setType("plain/text");
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, address); 
		
		startActivity(Intent.createChooser(emailIntent, getString(R.string.chooser_title)));		
	}

	private void sendFullMail()
	{
		convertEditTextToString();
		String[]address = {emailString}; // TODO: Replace with Bnotya's address
		String text = String.format("My name: %s, My email: %s, %s", nameString, emailString, contentString);
		
		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		emailIntent.setType("plain/text");
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, address); 
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, titleString);		
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);
		startActivity(emailIntent);
	}
	
	private void convertEditTextToString()
	{
		emailString = email.getText().toString();
		nameString = name.getText().toString();
		titleString = title.getText().toString();
		contentString = content.getText().toString();
	}
	
	private void initControls(View rootView)
	{
		email = (EditText)rootView.findViewById(R.id.etEmail); 
		name = (EditText)rootView.findViewById(R.id.etName);  
		title = (EditText)rootView.findViewById(R.id.etTitle);  
		content = (EditText)rootView.findViewById(R.id.etContent); 	
		
		Button btnSendEMail = (Button)rootView.findViewById(R.id.btnSendEMail);		
		btnSendEMail.setOnClickListener(this);
	}
}
