package com.vkassin.mtrade;

import android.app.Activity;

import org.json.JSONException;
import org.stockchart.StockChartView;
import org.stockchart.core.Area;
import org.stockchart.core.Axis;
import org.stockchart.core.Axis.ILabelFormatProvider;
import org.stockchart.core.Axis.Side;
import org.stockchart.core.AxisRange;
import org.stockchart.core.Line;
import org.stockchart.indicators.MacdIndicator;
import org.stockchart.points.AbstractPoint;
import org.stockchart.points.BarPoint;
import org.stockchart.points.LinePoint;
import org.stockchart.points.StockPoint;
import org.stockchart.series.BarSeries;
import org.stockchart.series.LinearSeries;
import org.stockchart.series.SeriesBase;
import org.stockchart.series.StockSeries;
import org.stockchart.series.StockSeries.OutlineStyle;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.ViewGroup.LayoutParams;

public class ChartActivity extends Activity {

    // NOTE: I recommend you to start reading from onCreate method and return here 
    // as soon as variables appear

    // we use timer, based on handler callbacks
    private Handler mHandler = new Handler();
        
    // series that we need (we'll initialize them later)
    private StockSeries s1;
    private BarSeries s2;    
    private LinearSeries s3;
    
    // areas 
    private Area a1;
    private Area a2;
    private Area a3;
    // variable to produce unique id's 
    private static long tttt = 0;
    
    // StockChart itself
    StockChartView s;
    
    // this runnable will be called by handler's timeout
        private Runnable mUpdateTimeTask = new Runnable() 
        {
           public void run() 
           {
                    // generating stock data
                   
                double high = 1000 + Math.random()*500;
                double low = 500+Math.random()*100; 
                double open = 700 + Math.random()*200;
                double close = 700 + Math.random()*200;
                
                // you can use your class's fields to access areas and series but I search them
                // every time this callback fired
                Area a1 = s.findAreaByName("A1");
                Area a2 = s.findAreaByName("A2");
                
                StockSeries s1 = (StockSeries)a1.findSeriesByName("stock");
                BarSeries s2 = (BarSeries)a1.findSeriesByName("volume");
                LinearSeries s3 = (LinearSeries)a2.findSeriesByName("line");
                
                // add point to s1
                long id =++tttt; 
                StockPoint ss = new StockPoint();
                ss.setID(id);
                ss.setValues(open,high,low,close);
                s1.Points.add(ss);
                
                // add point to s2
                double volume = Math.random()*20000;
                BarPoint bp = s2.addPoint(0, volume);
                bp.setID(id);
                // add point to s3
                LinePoint lp = s3.addPoint(close);
                lp.setID(id);
                
                // if you want to see the last value, pass something here:
                s1.setLastValue(close);
                s2.setLastValue(volume);
                
                s3.setLastValue(close);

                // don't forget to recalc indicators before invalidate
                s.recalcIndicators();
                // don't forget to call invalidate when finish
                s.invalidate();
                
                if(tttt < 100)
                        mHandler.postDelayed(this, 100);
        
           }
        };
        
        protected void onSaveInstanceState (Bundle outState)
        {
                try 
                {
                        // if something changes (i.e. orientation) we want to 
                        // restore our state later. So, why don't we save it first?
                        outState.putString("chart", s.save());
                } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }
        }
        
        // this method is called when only once
        private void newChart()
        {                       
                // initializing areas
                
                // first area:
        a1 = new Area();
        a1.setName("A1");
    
        a1.getLeftAxis().setVisible(true);
        a1.getLeftAxis().setLinesCount(2);
        // we don't need neither top axis...
        a1.getTopAxis().setVisible(false);
        // ... nor bottom axis!
        a1.getBottomAxis().setVisible(false);
        
        // second area:
        a2 = new Area();
        a2.setName("A2");
        // disabling autoheight
        a2.setAutoHeight(false);        
        // setting height explicitly
        a2.setHeightInPercents(0.2f);        
        // we need left axis
        a2.getLeftAxis().setVisible(false);

        // but not the top one
        a2.getTopAxis().setVisible(false);
        
        // third area 
        a3 = new Area();
        a3.getLeftAxis().setVisible(false);
        // areas done.
        // let's add them to our chart!
        s.Areas.add(a1);
        s.Areas.add(a2);             
        s.Areas.add(a3);        

        
        // creating stock series
        s1 = new StockSeries();
        // despite each series has unique name, I recommend you to give them names
        s1.setName("stock");
        // setting outline style and width to make our candlesticks look more attractive. 
        // i like this combination more than others. I don't recommend setting outline width too
        // large. 1...3f is enough.             
        s1.setOutlineStyle(OutlineStyle.USE_DARKER_BODY_COLOR);
        s1.setOutlineWidth(2.0f);
        
        
        // creating bar series for volume
        s2 = new BarSeries();
        s2.setName("volume");           
        // attaching s2 to left axis
        s2.setYAxisSide(Side.LEFT);
        // change the color of bar series
        s2.setBarColor(Color.GRAY);
        
        // creating linear series for close prices              
            s3 = new LinearSeries();
            s3.setName("line");
            // attaching s3 to right axis
            s3.setYAxisSide(Side.RIGHT);        
                    
            // series done. let's add them to corresponding areas        
        a1.Series.add(s2);
        a1.Series.add(s1);
        
        a2.Series.add(s3);
                
        // adding constant lines  (rulers) to the first area just for fun
        a1.Lines.add(new Line(10,Axis.Side.BOTTOM));
        a1.Lines.add(new Line(800,Axis.Side.RIGHT));
        
        // also we need MACD indicator to know for sure, when to buy or sell
        // to satisfy the MACD conditions we (typically) need:
        // 1. Lines x2
        // 2. Histogram x1
        // 3. Own area x1 
        // In theory, you can create only series and attach them to any area you want
        // But i prefer classical representation of the MACD
        // Here we go:
       
        LinearSeries macd = new LinearSeries();
        LinearSeries signal = new LinearSeries();
        BarSeries hist = new BarSeries();
        a3.Series.add(macd);
        a3.Series.add(signal);
        a3.Series.add(hist);
        // cool zero level for the histogram
        a3.Lines.add(new Line(0.0,Axis.Side.RIGHT));
        
        // add indicator
        s.Indicators.add(new MacdIndicator(s1,0,macd,signal,hist,26,12,9));
        
        // the last thing we need, is to make all areas behave as one.
        // we need to set global range
        AxisRange ar = new AxisRange();
        ar.setMovable(true);
        ar.setZoomable(true);
        
        s.GlobalRanges.put(Axis.Side.BOTTOM, ar);       
        }
        
        private void restoreChart(String sss)
        {
                // all we have to do is to restore our chart from JSON.
                // All series created automatically, so we need to find them by name
                try 
                {
                        s.load(sss);
                } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }
                
                a1 = s.findAreaByName("A1");
                a2 = s.findAreaByName("A2");
                s1 = (StockSeries)a1.findSeriesByName("stock");
                s2 = (BarSeries)a1.findSeriesByName("volume");
                s3 = (LinearSeries)a1.findSeriesByName("line");
        }
                
        private void wireEvents()
        {
        // we need to wire events each time activity is created.
                // you can provide custom formatter for each axis.
                // for example you may want to format your price with decimals
                
                a1.getRightAxis().setLabelFormatProvider(new ILabelFormatProvider()
                {
                
//                        @Override
                        public String getAxisLabel(Axis sender, double value) {
                                return String.valueOf((long)value);
                        }
                
                });
                        
           a2.getBottomAxis().setLabelFormatProvider(new ILabelFormatProvider() 
        {
//                        @Override
                        public String getAxisLabel(Axis sender, double value) 
                        {                       
                                // here you are formatting the X axis. 
                                // it is used to display time. But I don't have time in my example,
                                // but you can set time as ID of each point.
                                Area a = sender.getParent();
                                
                                for(int i=0;i<a.Series.size();i++)
                                {
                                        SeriesBase s = a.Series.get(i);
                                        
                                        int index = s.convertToArrayIndex(value);
                                        if(index >=0 && index < s.getPointCount())
                                        {
                                                Object id = ((AbstractPoint)s.getPointAt(index)).getID();
                                                
                                                if(null != id)
                                                        return id.toString();
                                        }
                                }
                                
                                return null;
                        }
                
        });
           
           a3.getBottomAxis().setLabelFormatProvider(new ILabelFormatProvider()
                {
                
//                        @Override
                        public String getAxisLabel(Axis sender, double value) 
                        {
                                return "";
                        }
                });
                

        }
        
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // create new blank stock chart
        s = new StockChartView(this);
                             
        if(null != savedInstanceState)
        {
                // if we have something to restore, then restore it
                 String sss = savedInstanceState.getString("chart");
                 restoreChart(sss);        
        }
        else
        {
                // if we have nothing to restore (i.e. first launch) then initialize
                newChart();
        }

        wireEvents();


        setContentView(s, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
              
        // timer
        mHandler.removeCallbacks(mUpdateTimeTask);
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }
}
