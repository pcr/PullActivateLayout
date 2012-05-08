/*
 * Copyright (C) 2012 by PCR (Wanzheng Ma)
 * The Pull to activate action Layout for Android Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pcr.android.ui;

import com.pcr.android.widget.PullActivateLayout;
import com.pcr.android.widget.PullActivateLayout.OnPullListener;
import com.pcr.android.widget.PullActivateLayout.OnPullStateListener;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

/**
 * The class is an example to show how to use a {@link PullActiveLayout} to implement
 * Pull to refresh effect.
 * 
 * @author PCR
 */
public class ExampleActivity extends ListActivity implements OnPullListener, OnPullStateListener {
	/* Handler message id */
	private final static int MSG_LOADING = 1;
	private final static int MSG_LOADED = 2;

	/* Views, widgets, animations & drawables */
	private PullActivateLayout mPullLayout;
	private TextView mActionText;
	private TextView mTimeText;
	private View mProgress;
	private View mActionImage;

	private Animation mRotateUpAnimation;
	private Animation mRotateDownAnimation;

	/* Variable */
	private boolean mInLoading = false;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case MSG_LOADING:
					//XXX at these load data, and notify complete by MSG_LOADED
					sendEmptyMessageDelayed(MSG_LOADED, 2000);
					break;
				case MSG_LOADED:
					dataLoaded();
					break;
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		initView();
		initData();
	}

	private void initView() {
		mRotateUpAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_up);
		mRotateDownAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_down);

		mPullLayout = (PullActivateLayout) findViewById(R.id.pull_container);
		mPullLayout.setOnActionPullListener(this);
		mPullLayout.setOnPullStateChangeListener(this);

		mProgress = findViewById(android.R.id.progress);
		mActionImage = findViewById(android.R.id.icon);
		mActionText = (TextView) findViewById(R.id.pull_note);
		mTimeText = (TextView) findViewById(R.id.refresh_time);

		mTimeText.setText(R.string.note_not_update);
		mActionText.setText(R.string.note_pull_down);
	}

	private void initData() {
		ArrayList<String> data = new ArrayList<String>();
		data.add("1");
		data.add("2");
		data.add("3");
		data.add("4");
		data.add("5");
		data.add("6");
		data.add("7");
		data.add("8");
		data.add("9");
		data.add("0");
		data.add("1");
		data.add("2");
		data.add("3");
		data.add("4");
		data.add("5");
		data.add("6");
		data.add("7");
		data.add("8");
		data.add("9");
		data.add("0");

		ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
				android.R.id.text1, data);
		setListAdapter(adapter);
	}

	private void startLoading() {
		if (!mInLoading) {
			mInLoading = true;
			mPullLayout.setEnableStopInActionView(true);
			mActionImage.clearAnimation();
			mActionImage.setVisibility(View.GONE);
			mProgress.setVisibility(View.VISIBLE);
			mActionText.setText(R.string.note_pull_loading);
			mHandler.sendEmptyMessage(MSG_LOADING);
		}
	}

	private void dataLoaded() {
		if (mInLoading) {
			mInLoading = false;
			mPullLayout.setEnableStopInActionView(false);
			mPullLayout.hideActionView();
			mActionImage.setVisibility(View.VISIBLE);
			mProgress.setVisibility(View.GONE);

			if (mPullLayout.isPullOut()) {
				mActionText.setText(R.string.note_pull_refresh);
				mActionImage.clearAnimation();
				mActionImage.startAnimation(mRotateUpAnimation);
			} else {
				mActionText.setText(R.string.note_pull_down);
			}

			mTimeText.setText(getString(R.string.note_update_at, DateFormat.getTimeFormat(this)
					.format(new Date(System.currentTimeMillis()))));
		}
	}

	@Override
	public void onPullOut() {
		if (!mInLoading) {
			mActionText.setText(R.string.note_pull_refresh);
			mActionImage.clearAnimation();
			mActionImage.startAnimation(mRotateUpAnimation);
		}
	}

	@Override
	public void onPullIn() {
		if (!mInLoading) {
			mActionText.setText(R.string.note_pull_down);
			mActionImage.clearAnimation();
			mActionImage.startAnimation(mRotateDownAnimation);
		}
	}

	@Override
	public void onSnapToTop() {
		startLoading();
	}

	@Override
	public void onShow() {

	}

	@Override
	public void onHide() {

	}
}
