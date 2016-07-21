package com.mars.markting.app;

import java.util.ArrayList;

import com.zdp.aseo.content.AseoZdpAseo;

import android.app.Activity; 
import android.app.Application; 
 
public class SysApplication extends Application { 
    private ArrayList<Activity> mList = new ArrayList<Activity>(); 
    private ArrayList<Activity> detailList = new ArrayList<Activity>();
    private static SysApplication instance; 
 
    private SysApplication() {   
    } 
    public synchronized static SysApplication getInstance() { 
        if (null == instance) { 
            instance = new SysApplication(); 
        } 
        return instance; 
    } 
    // add Activity  
    public void addActivity(Activity activity) { 
        mList.add(activity); 
        AseoZdpAseo.initType(activity, AseoZdpAseo.INSERT_TYPE);
    } 
 
    public void exit() { 
        try { 
            for (Activity activity : mList) { 
                if (activity != null) 
                    activity.finish(); 
            } 
        } catch (Exception e) { 
            e.printStackTrace(); 
        } finally { 
            System.exit(0); 
        } 
    } 
    
    public void addDetailActivity(Activity activity) {
    	detailList.add(activity);
    }
    
    /**
     * �رն����Detailҳ�棬ʹ�������2��
     */
    public void removeExtraActivity() {
    	if (detailList.size() > 2) {
    		detailList.get(0).finish();
    		detailList.remove(0);
    	}
    }
    
    public void onLowMemory() { 
        super.onLowMemory();     
        System.gc(); 
    }  
}
