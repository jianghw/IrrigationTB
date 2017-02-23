package com.cjyun.tb.supervisor.fragment;

/**
 * Created by Administrator on 2016/5/3 0003.
 */
public class VisitFragment{

/* extends SuBaseFragment implements ViewPager.OnPageChangeListener, Contract.SuVisitView {

    @Bind(R.id.vp_month)
    ViewPager mVpMonth;
    @Bind(R.id.tv_date)
    TextView mTvDate;
    @Bind(R.id.lr_web)
    TextView lrWeb;
    @Bind(R.id.lv_su_visit_events_show)
    ListView lvSuVisitEventsShow;

    private Activity mActivity;
    private ArrayList<Fragment> mFragmentList = new ArrayList<>();
    private SuVisitPresenter mPresenter;
    private int id;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        View root = inflater.inflate(R.layout.fragment_su_visit, container, false);
        ButterKnife.bind(this, root);
        initChildFragment();
        id = ((SuPatientNewsActivity) getActivity()).getId();

        return root;
    }

    @Override
    protected void initData() {


    }

    @Override
    protected void initListener() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;
    }

    public static VisitFragment newInstance() {
        return new VisitFragment();
    }

    private void initChildFragment() {

        if (mFragmentList.size() == 0) {
            mFragmentList.add(PrevMonFragment.newInstance());
            mFragmentList.add(CurMonFragment.newInstance());
        }
        mVpMonth.setAdapter(new RecordFgtAdapter(getChildFragmentManager(), mFragmentList));
        mVpMonth.setCurrentItem(1);
        mVpMonth.setOnPageChangeListener(this);

        setTitleDate(1);
    }

    *//**
     * 上月
     *//*
    @OnClick(R.id.relay_btn_prev)
    public void onMoveToPrev() {
        mVpMonth.setCurrentItem(0);
    }

    *//**
     * 本月
     *//*
    @OnClick(R.id.relay_btn_cur)
    public void onMoveToCur() {
        mVpMonth.setCurrentItem(1);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setTitleDate(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void setTitleDate(int position) {
        if (!isAdded()) return;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM", Locale.SIMPLIFIED_CHINESE);
        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.MONTH, position - 1);
        mTvDate.setText(simpleDateFormat.format(calendar.getTime()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    *//**
     * 没有访问内容时，点击添加 并传递ID值
     *//*
    @OnClick(R.id.lr_web)
    public void onClick() {

        Intent intent = new Intent();
        intent.setClass(getActivity(), SuAddVisitActivity.class);
        intent.putExtra("suPatutentDetails", id);
        startActivity(intent);
    }


    @Override
    public void onResume() {
        super.onResume();
        mPresenter = new SuVisitPresenter(
                SuInjection.provideTwoRepository(getActivity()), this);

        // mPresenter.onVisitDetails(id); TODO　真实的数据加载
    }

    *//**
     * 动态设置listview的 高度
     *
     * @param listView
     *//*
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        if (listView == null) return;
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        .setLayoutParams(params);
    }

    // 访问的内容
    @Override
    public void onVisitContent(List list) {

        if (list != null) {
            lvSuVisitEventsShow.setVisibility(View.VISIBLE);
            lvSuVisitEventsShow.setAdapter(new VisitDetailsAdapter(getActivity(), list));
            setListViewHeightBasedOnChildren(lvSuVisitEventsShow);

        } else {

            lrWeb.setVisibility(View.VISIBLE);
        }

        // 　解析访问时间
        HashMap<Integer, Boolean> hashMap = new HashMap<>();
        //  for (VisitDetailsBean bean : list) {

        for (int i = 0; i < list.size(); i++) {

            VisitDetailsBean bean = (VisitDetailsBean) list.get(i);
            String[] days = bean.getSetting_date().split("T");
            String day = days[0].split("-")[2];
            if (day.startsWith("0")) day = day.substring(1, day.length());
            hashMap.put(Integer.valueOf(day), true);

            *//*if (!bean.isTaken()) {
                hashMap.put(Integer.valueOf(day), true); //选中
            }*//*
            if (hashMap.containsKey(Integer.valueOf(day))) {
                hashMap.remove(Integer.valueOf(day)); // 移除
            }
        }
      //  CurMonFragment.newInstance().(hashMap);
        PrevMonFragment.newInstance().setPitch(hashMap);
    }
*/

}