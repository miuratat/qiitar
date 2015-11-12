package clear.qiitar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import clear.qiitar.service.Item;
import clear.qiitar.service.QiitaService;

/**
 * Itemリストアクティビティ.
 * 
 * @author miuratat
 *
 */
@SuppressWarnings("deprecation")
public class ItemListActivity extends ActionBarActivity implements ItemListFragment.Callbacks {

	// セクションページアダプター
	private SectionsPagerAdapter sectionsPagerAdapter_;
	// ビューペイジャー
	private ViewPager viewPager_;
	// リスト右部表示フラグ
	private boolean right_;
	// 設定画面戻り値タグ
	private static int SETTING_TAG = 1;
	// 詳細画面フラグメント
	ItemDetailFragment detailfragment_;

	ActionBar actionBar_;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		actionBar_ = getSupportActionBar();

		actionBar_.setTitle(getString(R.string.all_label));
		actionBar_.setSubtitle("");
		actionBar_.setBackgroundDrawable(getResources().getDrawable(R.color.qiita));

		// 設定ファイル取得
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

		// リスト右部表示か
		right_ = sharedPreferences.getBoolean("right", false);

		// タグリストを取得
		String tags = getString(R.string.tags);

		// リスト右部表示の場合
		if (right_) {
			setContentView(R.layout.activity_item_twopane_right);
			this.viewPager_ = (ViewPager) findViewById(R.id.pager_r);
		} else {
			setContentView(R.layout.activity_item_twopane);
			this.viewPager_ = (ViewPager) findViewById(R.id.pager);
		}

		if (savedInstanceState == null || savedInstanceState.getBoolean("restore") == false) {
			// サービスセットアップ
			QiitaService.setup(this, tags, sharedPreferences);
		}

		// セクションページアダプター生成
		this.sectionsPagerAdapter_ = new SectionsPagerAdapter(getSupportFragmentManager());

		// セクションページアダプターをビューペイジャーにセット
		this.viewPager_.setAdapter(sectionsPagerAdapter_);

		viewPager_.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageScrollStateChanged(int state) {
				if (ViewPager.SCROLL_STATE_IDLE == state) {
					actionBar_.setTitle(QiitaService.getFeedLabel(viewPager_.getCurrentItem()));
					actionBar_.setSubtitle("");
				}
			}
		});

	}

	/**
	 * Item選択時.
	 */
	@Override
	public void onItemSelected(Item item) {

		// 引数生成
		Bundle arguments = new Bundle();

		// ItemIDをセット
		arguments.putSerializable(ItemDetailFragment.ARG_ITEM_ID, item);

		// 詳細画面生成
		detailfragment_ = new ItemDetailFragment();

		// 引数セット
		detailfragment_.setArguments(arguments);

		// リスト右部表示の場合
		if (right_) {
			getSupportFragmentManager().beginTransaction().replace(R.id.item_detail_container_r, detailfragment_)
					.commit();
		} else {
			getSupportFragmentManager().beginTransaction().replace(R.id.item_detail_container, detailfragment_)
					.commit();
		}
	}

	@Override
	public void onBackPressed() {
		if (detailfragment_ != null && detailfragment_.canGoBack()) {
			detailfragment_.goBack();
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// アクションバーメニュー生成
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.actions, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		// 設定ボタンの場合
		case R.id.action_setting:

			// Intent生成
			Intent intent = new Intent(this, SettingActivity.class);

			// タグリストをセット
			intent.putExtra("tags", getResources().getString(R.string.tags));

			// 設定画面表示
			startActivityForResult(intent, SETTING_TAG);

			return true;
		}
		return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);

		// 設定画面からの戻りの場合
		if (requestCode == SETTING_TAG) {
			// 設定画面に変更があった場合
			if (resultCode == RESULT_OK) {

				// 設定ファイル取得
				SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

				// リスト右部表示か
				right_ = sharedPreferences.getBoolean("right", false);

				// リスト右部表示の場合
				if (right_) {
					setContentView(R.layout.activity_item_twopane_right);
					this.viewPager_ = (ViewPager) findViewById(R.id.pager_r);
				} else {
					setContentView(R.layout.activity_item_twopane);
					this.viewPager_ = (ViewPager) findViewById(R.id.pager);
				}

				// タグリスト取得
				String tags = getResources().getString(R.string.tags);

				// サービスセットアップ
				QiitaService.setup(this, tags, sharedPreferences);

				// セクションページアダプター再生成
				this.sectionsPagerAdapter_ = new SectionsPagerAdapter(getSupportFragmentManager());

				// ページをすべて除去
				sectionsPagerAdapter_.destroyAllItem(viewPager_);

				// セクションページアダプターセット
				this.viewPager_.setAdapter(sectionsPagerAdapter_);

				viewPager_.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageScrollStateChanged(int state) {
						if (ViewPager.SCROLL_STATE_IDLE == state) {
							actionBar_.setTitle(QiitaService.getFeedLabel(viewPager_.getCurrentItem()));
							actionBar_.setSubtitle("");
						}
					}
				});

				actionBar_.setTitle(getString(R.string.all_label));
				actionBar_.setSubtitle("");

				// セクションページアダプターに変更を通知
				sectionsPagerAdapter_.notifyDataSetChanged();
			}
		}
	}

	/**
	 * セクションページアダプター.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		// 表示中のフラグメント
		private Fragment current_;
		// 表示中のページ番号
		private int position_;

		/**
		 * コンストラクタ.
		 */
		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {

			// 引数生成
			Bundle args = new Bundle();
			// 該当のタグを取得
			args.putString("tag", QiitaService.getFeed(position));
			// フラグメント生成
			ItemListFragment frg = new ItemListFragment();
			// 引数セット
			frg.setArguments(args);
			return frg;
		}

		@Override
		public void setPrimaryItem(ViewGroup container, int position, Object object) {
			if (current_ != object) {
				current_ = (Fragment) object;
				position_ = position;
			}
			super.setPrimaryItem(container, position, object);
		}

		/**
		 * ページを全て除去
		 * 
		 * @param pager
		 */
		public void destroyAllItem(ViewPager pager) {
			for (int i = 0; i < getCount() - 1; i++) {
				try {
					Object obj = this.instantiateItem(pager, i);
					if (obj != null) {
						destroyItem(pager, i, obj);
					}
				} catch (Exception e) {
				}
			}
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			super.destroyItem(container, position, object);

			if (position <= getCount()) {
				FragmentManager manager = ((Fragment) object).getFragmentManager();
				FragmentTransaction trans = manager.beginTransaction();
				trans.remove((Fragment) object);
				trans.commit();
			}
		}

		public Fragment getCurrentFragment() {
			return current_;
		}

		public int getCurrentPosition() {
			return position_;
		}

		@Override
		public int getCount() {
			// ページ数を取得
			return QiitaService.getFeedSize();
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putBoolean("restore", true);
	}
}
