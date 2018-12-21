package com.example.warmzhou.criminalintent;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CheckBillFragment extends Fragment {

    private static final int REQUEST_CRIME = 1;

    private PieChart mPiechart;

    private CrimeLab crimeLab;
    private TextView mTextView;
    private TextView mTextShou;
    private TextView mTextJing;
    private TextView mTextZhi;
    private Spinner mySpinner;             //选择统计方式
    private Spinner moSpinner;            //选择月份下拉框
    private List<String> list = new ArrayList<>();
    private ArrayAdapter<String> adapter;


    private RecyclerView mDiffRecyclerView;
    private CrimeAdapter mAdapter;

    private String[] forwhat = new String[]{"饮食","购物","娱乐","其他"};
    private float[] number = new float[]{0,0,0,0};


    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.checkbill,container,false);


        Log.d("CrimeFragment","初始化");
        mPiechart = (PieChart)v.findViewById(R.id.chart_view);
        mTextView = (TextView)v.findViewById(R.id.time_text);
        mTextShou = (TextView)v.findViewById(R.id.shouru);
        mTextZhi = (TextView)v.findViewById(R.id.zhichu);
        mTextJing = (TextView)v.findViewById(R.id.jingzhi);
        moSpinner = (Spinner)v.findViewById(R.id.spinner_month);
        mySpinner = (Spinner)v.findViewById(R.id.spinner);
        mDiffRecyclerView = (RecyclerView)v.findViewById(R.id.show_bills);
        mDiffRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        initView();
        mySpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            Calendar cal = Calendar.getInstance();
            String month = (cal.get(Calendar.MONTH)+1)+"";
            String year = cal.get(Calendar.YEAR)+"";
            String day = cal.get(Calendar.DAY_OF_MONTH)+"";
            public void onItemSelected(AdapterView<?> arg0,View arg1,int arg2,long arg3){
                String tmp = (String)mySpinner.getSelectedItem();
                if(tmp.equals("今日")){
                    moSpinner.setVisibility(View.INVISIBLE);
                    mTextView.setText(month+"月"+day+"日");
                    List<Crime> tmp1 = crimeLab.getDay(year,month,day);
                    SetDate(tmp1);
                    if(tmp1.size()==0){
                        mTextShou.setText("进"+0);
                        mTextZhi.setText("出"+0);
                        mTextJing.setText("净"+0);
                    }else{
                        double sum[]=getSum(tmp1);
                        mTextShou.setText("进"+sum[1]);
                        mTextZhi.setText("出"+Math.abs(sum[2]));
                        mTextJing.setText("净"+sum[0]);
                    }
                    updateUI(tmp1);   //获取今日数据

                }
                if(tmp.equals("本月")){
                    moSpinner.setVisibility(View.VISIBLE);
                    moSpinner.setSelection(Integer.parseInt(month)-1);
                    mTextView.setText(year+"年"+month+"月");
                    List<Crime> tmp2 = crimeLab.getYue(year,month);
                    SetDate(tmp2);
                    if(tmp2.size()==0){
                        mTextShou.setText("进"+0);
                        mTextZhi.setText("出"+0);
                        mTextJing.setText("净"+0);
                    }else{
                        double sum[]=getSum(tmp2);
                        mTextShou.setText("进"+sum[1]);
                        mTextZhi.setText("出"+Math.abs(sum[2]));
                        mTextJing.setText("净"+sum[0]);
                    }
                    updateUI(tmp2);    //获取本月数据

                }
                if(tmp.equals("本年")){
                    moSpinner.setVisibility(View.INVISIBLE);
                    mTextView.setText(year+"年"+month+"月");
                    List<Crime> tmp3 = crimeLab.getYear(year);
                    SetDate(tmp3);
                    if(tmp3.size()==0){
                        mTextShou.setText("进"+0);
                        mTextZhi.setText("出"+0);
                        mTextJing.setText("净"+0);
                    }else{
                        double sum[]=getSum(tmp3);
                        mTextShou.setText("进"+sum[1]);
                        mTextZhi.setText("出"+Math.abs(sum[2]));
                        mTextJing.setText("净"+sum[0]);
                    }
                    updateUI(tmp3);
                }

            }
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                mTextView.setText("时间");
            }
        });
        crimeLab = CrimeLab.get(getActivity());
        for(int i=1;i<13;i++){
            list.add(i+"");
        }
        adapter = new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_spinner_item,list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        moSpinner.setAdapter(adapter);
        moSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            Calendar cal = Calendar.getInstance();
            String year = cal.get(Calendar.YEAR)+"";
            String month;
            public void onItemSelected(AdapterView<?> arg0,View arg1,int arg2,long arg3){
                Log.d("CrimeFragment","当前年份 :"+year);
                month = (String)moSpinner.getSelectedItem();
                Log.d("CrimeFragment","选择的月份 :"+month);
                List<Crime> tmp2 = crimeLab.getYue(year,month);
                SetDate(tmp2);
                if(tmp2.size()==0){
                    mTextShou.setText("进"+0);
                    mTextZhi.setText("出"+0);
                    mTextJing.setText("净"+0);
                }else{
                    double sum[]=getSum(tmp2);
                    mTextShou.setText("进"+sum[1]);
                    mTextZhi.setText("出"+Math.abs(sum[2]));
                    mTextJing.setText("净"+sum[0]);
                }
                updateUI(tmp2);    //获取本月数据
                mTextView.setText(year+"年"+month+"月");
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                mTextView.setText("时间");
            }
        });






        return v;
    }

    //初始化饼图
    private void initView() {

        //设置piecahrt图表点击Item高亮是否可用
        mPiechart.setHighlightPerTapEnabled(true);
        Description description = new Description();
        description.setText("消费结构图");
        mPiechart.setDescription(description);
        //设置X轴的动画
        mPiechart.animateX(1400);
        //使用百分比
        mPiechart.setUsePercentValues(true);
        //设置图列可见
        mPiechart.getLegend().setEnabled(true);
        //设置图列标识的大小
        mPiechart.getLegend().setFormSize(14);
        //设置图列标识文字的大小
        mPiechart.getLegend().setTextSize(14);
        //设置图列的位置
        mPiechart.getLegend().setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        //设置图列标识的形状
        mPiechart.getLegend().setForm(Legend.LegendForm.CIRCLE);
        //设置是否可转动
        mPiechart.setRotationEnabled(true);
    }

    private void SetDate(List<Crime> list){
        //mPiechart.clear();
        for(int i=0;i<4;i++){
            number[i]=0;
        }
        for(Crime c:list){
            if(c.getMoney()!=null&&c.getMoney().equals("")==false&&c.getMoney().length()>0) {
                if (Float.parseFloat(c.getMoney()) < 0) {
                    if (c.getTitle().contains("吃") || c.getTitle().contains("喝")) {
                        number[0] += Float.parseFloat(c.getMoney());
                        Log.d("CrimeFragment",number[0]+"number0");
                    } else if (c.getTitle().contains("买") || c.getTitle().contains("逛")) {
                        number[1] += Float.parseFloat(c.getMoney());
                        Log.d("CrimeFragment",number[1]+"number1");
                    } else if (c.getTitle().contains("唱") || c.getTitle().contains("玩")) {
                        number[2] += Float.parseFloat(c.getMoney());
                        Log.d("CrimeFragment",number[2]+"number2");
                    } else {
                        number[3] += Float.parseFloat(c.getMoney());
                        Log.d("CrimeFragment",number[3]+"number3");
                    }
                }
            }
        }
        ArrayList<String> titles = new ArrayList<>();
        for(int i=0;i<forwhat.length;i++){
            titles.add(forwhat[i]);
        }
        ArrayList<PieEntry> entrys = new ArrayList<PieEntry>();
        for (int i = 0; i < number.length; i++) {
            entrys.add(new PieEntry((0-number[i]), titles.get(i)));
        }
        PieDataSet dataSet = new PieDataSet(entrys,"消费概览");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(10f);

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        PieData pieData = new PieData(dataSet);
        pieData.setDrawValues(true);
        pieData.setValueFormatter(new PercentFormatter());
        pieData.setValueTextSize(15);
        mPiechart.setData(pieData);
        mPiechart.invalidate();

    }

    private double[] getSum(List<Crime> crimes){
        double sum[] = new double[3];
        sum[0] = sum[1] = sum[2] = 0;
        for(int i=0;i<crimes.size();i++){
            if(crimes.get(i).getMoney()!=null&&crimes.get(i).getMoney().equals("")==false&&crimes.get(i).getMoney().length()>0){
                sum[0]+=Double.parseDouble(crimes.get(i).getMoney());
                if(Double.parseDouble(crimes.get(i).getMoney())>0){
                    sum[1]+=Double.parseDouble(crimes.get(i).getMoney());
                }else
                    sum[2]+=Double.parseDouble(crimes.get(i).getMoney());
            }else{
                sum[0]+=0;
                sum[1]+=0;
                sum[2]+=0;
            }

        }
        return sum;
    }

    private void updateUI(List<Crime> crimes){
        if(mAdapter == null){
            mAdapter = new CrimeAdapter(crimes);
            mDiffRecyclerView.setAdapter(mAdapter);
        }else{
            mAdapter.setCrimes(crimes);
            mAdapter.notifyDataSetChanged();
        }
    }

    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private ImageView mSolvedImageView;
        private ImageView mMoneyImageView;
        private Crime mCrime;

        public CrimeHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_crime, parent, false));
            itemView.setOnClickListener(this);

            mTitleTextView = (TextView) itemView.findViewById(R.id.crime_title);
            mDateTextView = (TextView) itemView.findViewById(R.id.crime_date);
            mSolvedImageView = (ImageView) itemView.findViewById(R.id.crime_solved);
            mMoneyImageView = (ImageView) itemView.findViewById(R.id.money);
        }

        public void onClick(View view){

        }

        public void bind(Crime crime) {
            mCrime = crime;
            mTitleTextView.setText("项目名称:" + mCrime.getTitle());
            if (mCrime.getMoney() != null && !mCrime.getMoney().toString().equals("")) {
                if (Double.parseDouble(mCrime.getMoney()) >= 0) {
                    mTitleTextView.setText("项目名称:" + mCrime.getTitle() + "   收入:" + mCrime.getMoney() + "元");
                } else {
                    mTitleTextView.setText("项目名称:" + mCrime.getTitle() + "   支出:" + (0 - Double.parseDouble(mCrime.getMoney())) + "元");
                }
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(mCrime.getDate());
            Log.d("CrimeFragment",mCrime.getDate().toString());
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int min = calendar.get(Calendar.MINUTE);
            int sec = calendar.get(Calendar.SECOND);
            mDateTextView.setText("录入时间:" + calendar.get(Calendar.YEAR) + '年' + (calendar.get(Calendar.MONTH) + 1) + '月' + calendar.get(Calendar.DAY_OF_MONTH) + '日');
//            mDateTextView.setText(mCrime.getDate().toString());
            mSolvedImageView.setVisibility(crime.isSolved() ? View.VISIBLE : View.GONE);
            mMoneyImageView.setVisibility(!crime.isSolved() ? View.VISIBLE : View.GONE);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CRIME) {
            //
        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder>{
        private List<Crime> mCrimes;
        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }
        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new CrimeHolder(layoutInflater, parent);
        }
        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {
            Crime crime = mCrimes.get(position);
            holder.bind(crime);
        }

        public int getItemCount() {
            return mCrimes.size();
        }

        public void setCrimes(List<Crime> crimes) {
            mCrimes = crimes;
        }
    }

}
