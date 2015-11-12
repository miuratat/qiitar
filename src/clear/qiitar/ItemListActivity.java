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
 * Item���X�g�A�N�e�B�r�e�B.
 * 
 * @author miuratat
 *
 */
@SuppressWarnings("deprecation")
public class ItemListActivity extends ActionBarActivity implements ItemListFragment.Callbacks {

	// �Z�N�V�����y�[�W�A�_�v�^�[
	private SectionsPagerAdapter sectionsPagerAdapter_;
	// �r���[�y�C�W���[
	private ViewPager viewPager_;
	// ���X�g�E���\���t���O
	private boolean right_;
	// �ݒ��ʖ߂�l�^�O
	private static int SETTING_TAG = 1;
	// �ڍ׉�ʃt���O�����g
	ItemDetailFragment detailfragment_;

	ActionBar actionBar_;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		actionBar_ = getSupportActionBar();

		actionBar_.setTitle(getString(R.string.all_label));
		actionBar_.setSubtitle("");
		actionBar_.setBackgroundDrawable(getResources().getDrawable(R.color.qiita));

		// �ݒ�t�@�C���擾
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

		// ���X�g�E���\����
		right_ = sharedPreferences.getBoolean("right", false);

		// �^�O���X�g���擾
		String tags = getString(R.string.tags);

		// ���X�g�E���\���̏ꍇ
		if (right_) {
			setContentView(R.layout.activity_item_twopane_right);
			this.viewPager_ = (ViewPager) findViewById(R.id.pager_r);
		} else {
			setContentView(R.layout.activity_item_twopane);
			this.viewPager_ = (ViewPager) findViewById(R.id.pager);
		}

		if (savedInstanceState == null || savedInstanceState.getBoolean("restore") == false) {
			// �T�[�r�X�Z�b�g�A�b�v
			QiitaService.setup(this, tags, sharedPreferences);
		}

		// �Z�N�V�����y�[�W�A�_�v�^�[����
		this.sectionsPagerAdapter_ = new SectionsPagerAdapter(getSupportFragmentManager());

		// �Z�N�V�����y�[�W�A�_�v�^�[���r���[�y�C�W���[�ɃZ�b�g
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
	 * Item�I����.
	 */
	@Override
	public void onItemSelected(Item item) {

		// ��������
		Bundle arguments = new Bundle();

		// ItemID���Z�b�g
		arguments.putSerializable(ItemDetailFragment.ARG_ITEM_ID, item);

		// �ڍ׉�ʐ���
		detailfragment_ = new ItemDetailFragment();

		// �����Z�b�g
		detailfragment_.setArguments(arguments);

		// ���X�g�E���\���̏ꍇ
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
		// �A�N�V�����o�[���j���[����
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.actions, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		// �ݒ�{�^���̏ꍇ
		case R.id.action_setting:

			// Intent����
			Intent intent = new Intent(this, SettingActivity.class);

			// �^�O���X�g���Z�b�g
			intent.putExtra("tags", getResources().getString(R.string.tags));

			// �ݒ��ʕ\��
			startActivityForResult(intent, SETTING_TAG);

			return true;
		}
		return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);

		// �ݒ��ʂ���̖߂�̏ꍇ
		if (requestCode == SETTING_TAG) {
			// �ݒ��ʂɕύX���������ꍇ
			if (resultCode == RESULT_OK) {

				// �ݒ�t�@�C���擾
				SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

				// ���X�g�E���\����
				right_ = sharedPreferences.getBoolean("right", false);

				// ���X�g�E���\���̏ꍇ
				if (right_) {
					setContentView(R.layout.activity_item_twopane_right);
					this.viewPager_ = (ViewPager) findViewById(R.id.pager_r);
				} else {
					setContentView(R.layout.activity_item_twopane);
					this.viewPager_ = (ViewPager) findViewById(R.id.pager);
				}

				// �^�O���X�g�擾
				String tags = getResources().getString(R.string.tags);

				// �T�[�r�X�Z�b�g�A�b�v
				QiitaService.setup(this, tags, sharedPreferences);

				// �Z�N�V�����y�[�W�A�_�v�^�[�Đ���
				this.sectionsPagerAdapter_ = new SectionsPagerAdapter(getSupportFragmentManager());

				// �y�[�W�����ׂď���
				sectionsPagerAdapter_.destroyAllItem(viewPager_);

				// �Z�N�V�����y�[�W�A�_�v�^�[�Z�b�g
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

				// �Z�N�V�����y�[�W�A�_�v�^�[�ɕύX��ʒm
				sectionsPagerAdapter_.notifyDataSetChanged();
			}
		}
	}

	/**
	 * �Z�N�V�����y�[�W�A�_�v�^�[.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		// �\�����̃t���O�����g
		private Fragment current_;
		// �\�����̃y�[�W�ԍ�
		private int position_;

		/**
		 * �R���X�g���N�^.
		 */
		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {

			// ��������
			Bundle args = new Bundle();
			// �Y���̃^�O���擾
			args.putString("tag", QiitaService.getFeed(position));
			// �t���O�����g����
			ItemListFragment frg = new ItemListFragment();
			// �����Z�b�g
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
		 * �y�[�W��S�ď���
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
			// �y�[�W�����擾
			return QiitaService.getFeedSize();
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putBoolean("restore", true);
	}
}
