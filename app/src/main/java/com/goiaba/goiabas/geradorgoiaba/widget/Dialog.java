package com.goiaba.goiabas.geradorgoiaba.widget;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.goiaba.goiabas.geradorgoiaba.R;

public class Dialog extends android.app.Dialog{
	
	String message;
	TextView messageTextView;
	String title;
	TextView titleTextView;

    Button buttonAccept;
    Button buttonCancel;

	View.OnClickListener onAcceptButtonClickListener;
	View.OnClickListener onCancelButtonClickListener;
	

	public Dialog(Context context,String title, String message) {
		super(context, android.R.style.Theme_Translucent);
		this.message = message;
		this.title = title;
	}

    public Dialog(Context context, String message) {
        super(context, android.R.style.Theme_Translucent);
        this.title = "";
        this.message = message;
    }
	
	@Override
	  protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.dialog);
	    
	    this.titleTextView = (TextView) findViewById(R.id.title);
	    setTitle(title);
	    
	    this.messageTextView = (TextView) findViewById(R.id.message);
	    setMessage(message);
	    
	    this.buttonAccept = (Button) findViewById(R.id.button_accept);
	    buttonAccept.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismiss();
				if(onAcceptButtonClickListener != null)
			    	onAcceptButtonClickListener.onClick(v);
			}
		});
	    this.buttonCancel = (Button) findViewById(R.id.button_cancel);
    	buttonCancel.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					dismiss();
					if(onCancelButtonClickListener != null)
				    	onCancelButtonClickListener.onClick(v);
				}
			});
	}

    public void setButtonCancelEnabled(boolean enabled) {
        if(buttonCancel != null) {
            if(enabled) {
               buttonCancel.setVisibility(View.VISIBLE);
            } else {
                buttonCancel.setVisibility(View.GONE);
            }
        }
    }

    public void setButtonOkEnabled(boolean enabled) {
        if(buttonAccept != null) {
            if(enabled) {
                buttonAccept.setVisibility(View.VISIBLE);
            } else {
                buttonAccept.setVisibility(View.GONE);
            }
        }
    }
	
	// GETERS & SETTERS

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
		messageTextView.setText(message);
	}

	public TextView getMessageTextView() {
		return messageTextView;
	}

	public void setMessageTextView(TextView messageTextView) {
		this.messageTextView = messageTextView;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
		if(title == null)
			titleTextView.setVisibility(View.GONE);
		else{
			titleTextView.setVisibility(View.VISIBLE);
			titleTextView.setText(title);
		}
	}

	public TextView getTitleTextView() {
		return titleTextView;
	}

	public void setTitleTextView(TextView titleTextView) {
		this.titleTextView = titleTextView;
	}

	public Button getButtonAccept() {
		return buttonAccept;
	}

	public void setButtonAccept(Button buttonAccept) {
		this.buttonAccept = buttonAccept;
	}

	public Button getButtonCancel() {
		return buttonCancel;
	}

	public void setButtonCancel(Button buttonCancel) {
		this.buttonCancel = buttonCancel;
	}

	public void setOnAcceptButtonClickListener(
			View.OnClickListener onAcceptButtonClickListener) {
		this.onAcceptButtonClickListener = onAcceptButtonClickListener;
		if(buttonAccept != null)
			buttonAccept.setOnClickListener(onAcceptButtonClickListener);
	}

	public void setOnCancelButtonClickListener(
			View.OnClickListener onCancelButtonClickListener) {
		this.onCancelButtonClickListener = onCancelButtonClickListener;
		if(buttonCancel != null)
			buttonCancel.setOnClickListener(onAcceptButtonClickListener);
	}
	
	
	
	

}
