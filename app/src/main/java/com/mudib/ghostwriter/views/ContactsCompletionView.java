package com.mudib.ghostwriter.views;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mudib.ghostwriter.R;
import com.mudib.ghostwriter.models.Keyword;
import com.tokenautocomplete.TokenCompleteTextView;

/**
 * Created by diana on 25/03/2018.
 */

public class ContactsCompletionView extends TokenCompleteTextView<Keyword> {
    public ContactsCompletionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected View getViewForObject(Keyword keyword) {

        LayoutInflater l = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        TextView view = (TextView) l.inflate(R.layout.contact_token, (ViewGroup) getParent(), false);
        view.setText(keyword.getWord());
        return view;
    }

    @Override
    protected Keyword defaultObject(String completionText) {
        Keyword keyword = new Keyword(getContext(),completionText);
        keyword.setEnWord(completionText);
        return keyword;
    }
}
